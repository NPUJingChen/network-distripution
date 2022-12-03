import java.io.*;
import java.net.Socket;

/**
 * @author chenjing
 */

public class GetMessage {

    private static int SERVER_PORT= 80;

    private static String SERVER_IP = "127.0.0.1";

    private static String GET_CSS="GET /css/common.css HTTP/1.1\\r\\n"
            +"Host: cxhz.hep.com.cn\\r\\n";

    private static String GET_IMAGE="GET /img/bg_big.png HTTP/1.1\r\n"
            + "Host: cxhz.hep.com.cn\r\n";

    //因为再浏览器中用f12查看发现文件类型是gzip，所以要保存为.gzip格式
    private static String file01 ="file01.gzip";

    private static String file02 = "file02.jpg";

    private Socket socket;

    public GetMessage(String host,int port) throws IOException {
        socket = new Socket(host, port);
    }

    public Socket getSocket() {
        return socket;
    }

    public static void main(String[] args) throws IOException {
        GetMessage msg = new GetMessage(SERVER_IP,SERVER_PORT);
        msg.service(GET_CSS,file01);
        msg.service(GET_IMAGE, file02);
        msg.getSocket().close();
    }

    /**
     *
     * @param str
     * @return int
     * 获取响应报文响应体的长度
     */
    public int getLength(String str) {

        int i=str.indexOf("Content-Length: ")+16;
        String num="";
        char letter = 'a';
        while(true) {
            letter = str.charAt(i);
            if('0'<=letter&&letter <= '9') {
                num = num+letter;
                i++;
            } else {
                break;
            }
        }
        return Integer.parseInt(num);
    }

    private void service(String request,String fileName) throws IOException{

        BufferedInputStream bfs = new BufferedInputStream(socket.getInputStream());
        BufferedWriter bw =new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        PrintWriter pw = new PrintWriter(bw,true);

        byte x=0,y=0;
        byte[] info = new byte[1024];
        //发送http请求报文
        pw.println(request);

        y=(byte)bfs.read();
        //头文件
        String head="";
        while(true) {
            x=y;
            y=(byte)bfs.read();
            //判断x为\n y为\r时跳出循环，此时已经到头文件的尾部
            //因为响应报文头文件和报文体中间有两个回车换行符
            if(x==10&&y==13){
                System.out.println(head);
                System.out.println("\n\r\n");
                bfs.read();
                break;
            } else {
                head =head+(char)x;
            }
        }

        FileOutputStream fos = new FileOutputStream(new File(fileName));

        int times = getLength(head)/1024 +1;
        while (times > 0) {
            bfs.read(info);
            fos.write(info,0, info.length);
            times--;
        }
        fos.close();    }
}
