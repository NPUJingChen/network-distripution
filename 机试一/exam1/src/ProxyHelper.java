import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 设置代理服务器打印的html页面内容
 */
public class ProxyHelper implements Runnable{

    BufferedOutputStream ostream = null;

    BufferedInputStream istream = null;

    private Socket socket;

    private static final int BUFFER_SIZE = 8 * 1024;
    private static final String CRLF = "\r\n";
    public ProxyHelper(Socket socket){
        this.socket = socket;
    }

    /**
     * 初始化
     */
    public void initStream(){
        try{
            ostream = new BufferedOutputStream(socket.getOutputStream());
            istream = new BufferedInputStream(socket.getInputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
            //打印连接的客户端的IP地址和端口号
            System.out.println("connect with >>> " + socket.getInetAddress() + ":" + socket.getPort());
            //初始化
            initStream();
            StringBuffer header = new StringBuffer();
            int last = 0, c = 0;
            //判断是否在头文件里
            boolean inHeader = true;
            while (inHeader && ((c = istream.read()) != -1)) {
                switch (c) {
                    case '\r':
                        break;
                    case '\n':
                        if (c == last) {
                            inHeader = false;
                            break;
                        }
                        last = c;
                        header.append("\n");
                        break;
                    default:
                        last = c;
                        header.append((char) c);
                }
            }
            String head = new String(header);
            //根据换行符分割头文件
            String[] heads = head.split("\n");
            String newHead = "";

            System.out.println("Client request: \n" + head);
            newHead = heads[0] + CRLF + heads[1] + CRLF + "From: " + socket.getInetAddress() + ":"
                    + socket.getPort() + CRLF + heads[3] + CRLF + CRLF;
            System.out.println("New head: \n" + newHead);

            int targetPort;
            String requestURL = head.split("\n")[0].split(" ")[1];
            if (requestURL.startsWith("/")){
                targetPort = 2021;
            }else {
                System.out.println("-----" + requestURL);
                String[] temp = requestURL.split("/")[2].split(":");
                if(temp.length == 1){
                    targetPort = 2021;
                }else {
                    targetPort = Integer.parseInt(temp[1]);
                }
            }

            //连接
            ClientHelper httpClientHelper = new ClientHelper();
            String targetIp = head.split("\n")[1].split(" ")[1].split(":")[0];
            httpClientHelper.connect(targetIp, targetPort);
            System.out.println("connect " + targetIp + ":" + targetPort + " success");
            httpClientHelper.processGetRequest(newHead);
            System.out.println(httpClientHelper.getResponse());
            //打印html页面内容
            ostream.write(httpClientHelper.getResponse().getBytes(), 0, httpClientHelper.getResponse().length());
            ostream.flush();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                istream.close();
                ostream.close();
                socket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
