import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.Timer;

/**
 * 客户端
 *
 * @author chenjing
 */
public class FileClient {

    /*TCP端口号*/
    static final int TCP_PORT = 2021;
    /*UDP端口号*/
    static final int UDP_PORT = 2020;
    /*主机IP*/
    static final String HOST = "127.0.0.1";
    /*每次传输的数据包大小*/
    int PACKET_SIZE = 8 * 1024;

    /*IO相关的变量*/
    BufferedWriter bw = null;
    BufferedReader br = null;
    PrintWriter pw = null;
    Scanner in = null;

    /*客户端套接字*/
    Socket socket;

    /**
     * 构造函数
     * @throws UnknownHostException
     * @throws IOException
     */
    public FileClient() throws UnknownHostException, IOException{

        socket = new Socket();
        // 连接服务器
        socket.connect(new InetSocketAddress(HOST, TCP_PORT));

        // 初始化IO流
        initStream();

        System.out.println("连接成功");
    }

    /**
     * 主函数
     * @param args
     * @throws UnknownHostException
     * @throws IOException
     */
    public static void main(String[] args) throws UnknownHostException, IOException{
        FileClient fileClient = new FileClient();
        fileClient.send();

    }

    /**
     * 初始化IO流
     */
    public void initStream(){
        try{

            // 用于向服务器发送消息
            bw = new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream()));
            // 用于接收服务器消息
            br = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            pw = new PrintWriter(bw, true);
            in = new Scanner(System.in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭IO流
     */
    public void close(){
        try{

            bw.close();
            br.close();
            pw.close();
            in.close();

            System.out.println("disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送数据
     * 	[1]	ls	服务器返回当前目录文件列表（<file/dir>	name	size）
     * 	[2]	cd  <dir>	进入指定目录（需判断目录是否存在，并给出提示）
     * 	[3]	get <file>	通过UDP下载指定文件，保存到客户端当前目录下
     * 	[4]	bye	断开连接，客户端运行完毕
     */
    public void send(){
        try{

            String msg = null;

            Boolean flag = true;  // 当输入bye时，flag变成false，退出while

            while ((msg = in.nextLine()) != null){

                pw.println(msg);  // 发送给服务器消息

                String cmd = msg.split(" ")[0];

                switch (cmd){
                    case "bye":{
                        flag = false;
                        break;
                    }
                    case "ls":{

                        ls();
                        break;
                    }
                    case "cd":{
                        if(msg.split(" ").length == 2){

                            cd();
                        }else {

                            System.out.println(br.readLine());
                        }
                        break;
                    }
                    case "get":{
                        if(msg.split(" ").length == 2){
                            String downloadPath = new File("").getAbsolutePath();
//                            System.out.println(downloadPath);
                            pw.println(downloadPath);
                            get(msg.split(" ")[1]);
                        }else {

                            System.out.println(br.readLine());
                        }
                        break;
                    }
                    default:{
                        System.out.println(br.readLine());  // 输出服务器返回的消息
                        break;
                    }
                }

                if (!flag){
                    break;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {

            // 关闭连接
            if (socket != null){
                try{
                    socket.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            close();
        }
    }

    /**
     * 展示当前目录文件
     */
    public void ls() throws IOException {
        String out = null;
        while (!"".equals(out = br.readLine())){

            System.out.println(out);
        }
    }

    /**
     * 改变目录
     * @throws IOException
     */
    public void cd() throws IOException{
        System.out.println(br.readLine());
    }

    /**
     * 获取文件
     * @param path 文件路径
     * @throws IOException
     */
    public void get(String path) throws IOException{
        String out;
        String downloadPath = new File("").getAbsolutePath();
        String[] tokens;
        String fileName;
        BufferedOutputStream bos = null;

        try{
            tokens = path.split("/");
            fileName = tokens[tokens.length - 1];
        }catch (Exception e){
            e.printStackTrace();
            return;
        }

        if (!"".equals(out = br.readLine())){
            // 出现异常，退出get接收
            System.out.println(out);
            return;
        }

        File file = new File(downloadPath + "/" + fileName);
        if (file.createNewFile()){
            // true表示创建成功，false表示文件已存在
            System.out.println("had create a new file " + fileName);
        }

        // 开始udp传输================================

        DatagramPacket send, recv;
        DatagramSocket datagramSocket = new DatagramSocket();
        byte[] sendBuf, recvBuf;
        InetAddress loaclhost = InetAddress.getByName(HOST);
        try {
            while (true) {

                int len = "start".getBytes(StandardCharsets.UTF_8).length;
                send = new DatagramPacket("start".getBytes(StandardCharsets.UTF_8), len, loaclhost, UDP_PORT);
                datagramSocket.send(send);

                len = "trans".getBytes(StandardCharsets.UTF_8).length;
                recvBuf = new byte[len];
                recv = new DatagramPacket(recvBuf, len);
                datagramSocket.receive(recv);

                String tmp = new String(recv.getData(), StandardCharsets.UTF_8);
                            System.out.println(tmp);
                if ("trans".equals(tmp)) {

                    break;
                }
            }

            System.out.println("准备就绪");

            // 接收文件
            bos = new BufferedOutputStream(new FileOutputStream(file));
            recvBuf = new byte[PACKET_SIZE];
            while (true) {
                recv = new DatagramPacket(recvBuf, PACKET_SIZE);
                datagramSocket.receive(recv);

                String tmp = new String(recv.getData(), 0, recv.getLength());
                if ("end".equals(tmp)) {
                    break;
                }

                bos.write(recv.getData(), 0, recv.getLength());
                bos.flush();
                byte[] ok = "OK".getBytes(StandardCharsets.UTF_8);
                send = new DatagramPacket(ok, ok.length, loaclhost, UDP_PORT);
                datagramSocket.send(send);

            }

            System.out.println("接收成功");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bos.close();
            datagramSocket.close();

        }

    }
}
