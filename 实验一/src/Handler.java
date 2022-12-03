import javax.sound.sampled.Port;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.TreeMap;


/**
 * 用于实现线程的逻辑
 *
 * @author chenjing
 */
public class Handler implements Runnable{

    /*UDP端口号*/
    static final int UDP_PORT = 2020;
    /*每次传输的数据包大小*/
    int PACKET_SIZE = 8 * 1024;

    /*IO相关的变量*/
    BufferedReader br;
    BufferedWriter bw;
    PrintWriter pw;

    private Socket socket;

    // UDP服务端
    DatagramSocket datagramSocket;

    String path;

    public Handler(Socket socket, String path) throws SocketException {
        this.socket =socket;
        this.path = path;
    }

    /**
     * 初始化IO流
     */
    public void initStream() throws IOException {

        // 初始化输入输出流对象方法
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bw = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream()));
        pw = new PrintWriter(bw, true);
    }

    public void close(){
        try{

            bw.close();
            br.close();
            pw.close();

//            System.out.println(">>>disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 	[1]	ls	服务器返回当前目录文件列表（<file/dir>	name	size）
     * 	[2]	cd  <dir>	进入指定目录（需判断目录是否存在，并给出提示）
     * 	[3]	get <file>	通过UDP下载指定文件，保存到客户端当前目录下
     * 	[4]	bye	断开连接，客户端运行完毕
     */
    @Override
    public void run() {

        try{

            System.out.println("新连接--->地址: " + socket.getInetAddress() + ":"
                    + socket.getPort());

            initStream();

            String info = null;
            // 连接的客户端信息
            String connect = socket.getInetAddress() + ":" + socket.getPort() + ">>>";

            Boolean flag = true;  // 当输入bye时，flag变成false，退出while

            while ((info = br.readLine()) != null) {

                // 命令 目录/文件
                String[] orders = info.split(" ");
                String cmd = orders[0];

                switch (cmd){
                    case "bye": {

                        // bye 断开连接，客户端运行完毕
                        System.out.println(connect + "Disconnect.");
                        flag = false;
                        break;
                    }
                    case "ls":{
                        // ls 服务器返回当前目录文件列表（<file/dir>	name size）
                        System.out.println(connect + "ls");
                        ls(this.path);
                        break;
                    }
                    case "cd..":{
                        System.out.println(connect + "cd..");
                        cd("..");
                        break;
                    }
                    case "cd":{
                        // cd  <dir>	进入指定目录（需判断目录是否存在，并给出提示）
                        if(orders.length == 2){

                            System.out.println(connect + "cd " + this.path);
                            cd(orders[1]);
                        }else {
                            pw.println("you need input as 'cd  <dir>'.");
                        }
                        break;
                    }
                    case "get":{
                        // get  <file>	通过UDP下载指定文件，保存到客户端当前目录下
                        if(orders.length == 2){

                            System.out.println(connect + "get " + orders[1]);
                            String downloadPath = br.readLine();
                            get(orders[1], downloadPath);
                        }else {

                            pw.println("you need input as 'get <file>'.");
                        }
                        break;
                    }
                    default:{
                        pw.println("unknown " + info);
                        break;
                    }
                }

                if (!flag){
                    break;
                }
            }

        }catch (IOException e){
//            e.printStackTrace();
            System.out.println(socket.getInetAddress() + ":" + socket.getPort() + ">>>" + "error Disconnect.");
        }finally {

            // 关闭连接
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            close();
        }
    }

    /**
     * 展示当前目录文件
     */
    public void ls(String path){
        String out = "";

        File file = new File(path);

        if (file.exists()){
            if (null == file.listFiles()){
                return;
            }

            for (File f : file.listFiles()){

                if (f.isDirectory()){
                    out = "<dir>\t" + f.getName() + "\t" + f.length();
                }
                else if (f.isFile()){
                    out = "<file>\t" + f.getName() + "\t" + f.length();
                }
                pw.println(out);
            }
        }else {
            pw.println(path + "does not exist!");
        }
        pw.println("");
    }

    /**
     * 改变目录
     * @throws IOException
     */
    public void cd(String path) {

        String tmp;
        if (!"..".equals(path)){
            // 绝对路径和相对路径
            // 先判断是不是绝对路径

            File file = new File(path);
            if (file.exists()){
                // 判断这个目录是否是绝对路径
                this.path = file.getAbsolutePath();
                pw.println(this.path + " > OK");
                return;
            }

            // 再判断是不是相对路径
            // 用/分割，一步步的移动
            tmp = this.path;

            for (String token: path.split("/")){

                switch (token){
                    case "":
                    case ".": {
                        break;
                    }
                    case "..":{
                        if (new File(tmp).getParent() != null){
                            tmp = new File(tmp).getParent();
                        }
                        break;
                    }
                    default:{
                        tmp += "/" + token;
                        file = new File(tmp);
                        if (!file.exists()){
                            // 判断这个目录是否存在
                            pw.println(path + " does not exist!");
                            return;
                        }
                    }
                }
            }
            this.path = file.getAbsolutePath();

        }else {
            // cd.. 的情况
            tmp = new File(this.path).getParent();
            if (tmp == null){
                // 判断这个目录是否已经到根目录了
                pw.println(this.path + " > OK");
                return;
            }
            this.path = tmp;
        }
        pw.println(this.path + " > OK");
    }

    /**
     * 获取文件
     * @param path 文件路径
     * @throws IOException
     */
    public void get(String path, String downloadPath){

        BufferedInputStream bis = null;

        try {
            // 开启udp端口监听
            datagramSocket = new DatagramSocket(UDP_PORT);

            // 先在当前目录下载
            System.out.println(this.path + "/" + path);
            File file = new File(this.path + "/" + path);

            if (!file.exists()) {

                pw.println(path + " does not exist!");
                return;
            }

            if (file.isDirectory()) {

                pw.println(path + " is not a file!");
                return;
            }

            pw.println("");

            // 开始udp传输================================

            DatagramPacket send, recv;
            byte[] sendBuf, recvBuf;
            InetAddress remote = null;
            int port = 0;

            while (true) {

                int len = "start".getBytes(StandardCharsets.UTF_8).length;
                recvBuf = new byte[len];
                recv = new DatagramPacket(recvBuf, len);

                datagramSocket.receive(recv);
                String tmp = new String(recv.getData(), StandardCharsets.UTF_8);
                //            System.out.println(tmp);
                if ("start".equals(tmp)) {

                    remote = recv.getAddress();
                    port = recv.getPort();

                    len = "trans".getBytes(StandardCharsets.UTF_8).length;
                    send = new DatagramPacket("trans".getBytes(StandardCharsets.UTF_8), len, remote, port);
                    datagramSocket.send(send);

                    break;
                }
            }

            System.out.println("准备就绪: " + remote + ":" + port);

            // 传输文件
            bis = new BufferedInputStream(new FileInputStream(file));
            int readLen, k = 0;
            sendBuf = new byte[PACKET_SIZE];
            while ((readLen = bis.read(sendBuf)) != -1) {
                send = new DatagramPacket(sendBuf, readLen, remote, port);
                datagramSocket.send(send);

                // 等待回复
                datagramSocket.receive(recv);
            }

            byte[] end = "end".getBytes(StandardCharsets.UTF_8);
            send = new DatagramPacket(end, end.length, remote, port);
            datagramSocket.send(send);

            System.out.println("传输完成");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (bis != null){
                try {
                    bis.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            datagramSocket.close();
        }


    }
}