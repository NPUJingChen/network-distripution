import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.net.HttpCookie;
import java.net.Socket;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.security.cert.CRL;

/**
 * 客户端辅助方法，获取http请求报文
 * @author chenjing
 */
public class ClientHelper {

    private static final int PORT = 8000;
    private static final int BUFFER_SIZE = 8 * 1024;

    private byte[] buffer;

    String host = "127.0.0.1";
    Socket socket = null;
    BufferedOutputStream ostream = null;
    BufferedInputStream istream = null;

    private StringBuffer header;
    private StringBuffer response;
    static private final String CRLF = "\r\n";

    public ClientHelper(){
        buffer = new byte[BUFFER_SIZE];
        header = new StringBuffer();
        response = new StringBuffer();
    }

    /**
     * 用默认端口连接
     */
    public void connect(String host) throws Exception{
        this.host = host;
        socket = new Socket(host, PORT);
        ostream = new BufferedOutputStream(socket.getOutputStream());
        istream = new BufferedInputStream(socket.getInputStream());
    }

    /**
     * 用指定端口连接
     */
    public void connect(String host, int port) throws Exception {

        socket = new Socket(host, port);
        ostream = new BufferedOutputStream(socket.getOutputStream());
        istream = new BufferedInputStream(socket.getInputStream());
    }

    /**
     * 打印客户端的请求信息
     * @param request 客户端请求
     * @throws Exception
     */
    public void processGetRequest(String request) throws Exception{
        if(!request.contains("Host:")){
            request += CRLF;
            request += "Host: " + processHost(request) + CRLF;
            request += "From: 127.0.0.1" + CRLF;
            request += "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36 Edg/94.0.992.38" + CRLF;
            request += CRLF + CRLF;
        }
        buffer = request.getBytes();
        ostream.write(buffer, 0, request.length());
        ostream.flush();
        processResponse();
    }

    public String processHost(String request){
        String target = request.split(" ")[1];
        if(target.startsWith("/")){
            return host;
        }else {
            return target.split("/")[2];
        }
    }

    /**
     * 客户端的响应
     * @throws Exception
     */
    public void processResponse() throws Exception{
        int last = 0, c = 0;
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
        System.out.println(header);

        //打印读取到的信息
        while (istream.read(buffer) != -1){
            String tmp = new String(buffer, StandardCharsets.UTF_8);
            response.append(tmp);
            buffer = new byte[BUFFER_SIZE];
            if(istream.available() == 0){
                break;
            }
        }
    }

    public String getHeader(){
        return header.toString();
    }

    public String getResponse(){
        return response.toString();
    }

    public void close() throws Exception{
        socket.close();
        istream.close();
        ostream.close();
    }

}
