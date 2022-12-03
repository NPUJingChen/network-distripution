import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ServerHelper implements Runnable{
    private static final String CRLF = "\r\n";
    private static final int BUFFER_SIZE = 8 * 1024;
    /**
     * Output stream to the socket.
     */
    BufferedOutputStream ostream = null;

    /**
     * Input stream from the socket.
     */
    BufferedInputStream istream = null;
    public Socket socket;
    /**
     * 服务器的根目录地址 C:/../Desktop
     */
    String path;
    /**
     * 用于格式化显示日期
     */
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 远程端的不同字符串形式
     */
    String remote;
    String Remote;

    public void initStream() {
        try {
            /**
             * Create the output stream. 发送
             */
            ostream = new BufferedOutputStream(socket.getOutputStream());

            /**
             * Create the input stream.  接收
             */
            istream = new BufferedInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param socket socket接口
     * @param path   为服务器目录
     */
    ServerHelper(Socket socket, String path) {
        this.socket = socket;
        this.path = path;

        remote = socket.getInetAddress() + ":" + socket.getPort() + ">>";
        Remote = socket.getInetAddress() + ":" + socket.getPort() + " ";
    }

    @Override
    public void run() {
        try {
            initStream();
            String info;
            info = Handler.receiveHeader(istream);
            assert info != null;
            String[] req = info.split(" ", 2);
            switch (req[0]) {
                case "GET":{
                    System.out.println(df.format(new Date()) + "|| " + remote + info.split("\n")[0]);
                    processGetRequest(info);
                    break;
                }
                case "PUT": {
                    System.out.println(df.format(new Date()) + "|| " + remote + info.split("\n")[0]);
                    processPutRequest(info);
                    break;
                }
                default : {
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理GET请求
     * @param request GET请求的字符串形式
     */
    public void processGetRequest(String request) {
        // TODO GET resource/1.txt HTTP/1.0
        String requestPath = request.split(" ", 3)[1];
        //如果不是以/开头，则加上/   为了避免GET a.txt HTTP/1.1
        if (!requestPath.startsWith("/"))
            requestPath = "/" + requestPath;
        //请求的文件的路径
        String filePath = path + requestPath;

        File file = new File(filePath);

        try {


            //当请求错误，返回状态码400
            // TODO GET resource/1.txt HTTP/2
            if (!request.split(" ", 4)[2].contains("HTTP/1.1") &&
                    !request.split(" ", 4)[2].contains("HTTP/1.0")) {
                System.out.println("HTTP/1.1 400 Bad Request");
                try {
                    String response = "HTTP/1.1 400 Bad Request" + CRLF;
                    response += "Content-Length: 183" + CRLF;
                    response += "Content-Type: text/html; charset=iso-8859-1" + CRLF;
                    response += "Data: " + new Date().toString() + CRLF;
                    response += CRLF;

                    ostream.write(response.getBytes(), 0, response.length());
                    ostream.flush();
                    File file400 = new File(path + "/resource/400.html");
                    if (Handler.sendFile(ostream, file400)) {
                        System.out.println(df.format(new Date()) + "|| " + "400 Page send to " + Remote + "success!");
                    }
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //当404
            // TODO GET 1.txt HTTP/1.0
            if (!file.exists()) {
                System.out.println("HTTP/1.1 404 Not Found");
                try {
                    String response = "HTTP/1.1 404 Not Found" + CRLF;
                    response += "Content-Length: 199" + CRLF;
                    response += "Content-Type: text/html; charset=iso-8859-1" + CRLF;
                    response += "Data: " + new Date().toString() + CRLF;
                    response += CRLF;

                    ostream.write(response.getBytes(), 0, response.length());
                    ostream.flush();

                    File file404 = new File(path + "/resource/404.html");
                    System.out.println("-------");
                    if (Handler.sendFile(ostream, file404)) {
                        System.out.println(df.format(new Date()) + "|| " + "404Page send to " + Remote + "success!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //正常，状态码200
                // TODO GET resource/2.jpg HTTP/1.0
                System.out.println("HTTP/1.1 200 OK");
                String fileType = Handler.getContentType(requestPath);
                String response = "";
                response += "HTTP/1.1 200 OK" + CRLF;
                response += "Data: " + new Date().toString() + CRLF;
                response += "Content-Type: " + fileType + ";charset=ISO-8859-1" + CRLF;
                response += "Content-Length: " + file.length() + CRLF;
                response += CRLF;
                ostream.write(response.getBytes(), 0, response.length());
                ostream.flush();

                if (Handler.sendFile(ostream, file)) {
                    System.out.println(df.format(new Date()) + "|| " + file.getPath() + " send to " + Remote + "success!");
                }
                run();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理PUT请求
     * @param request PUT请求的字符串形式
     */
    public void processPutRequest(String request) {
        try {
            // 400
            // TODO PUT src/resource/1.html HTTP/2
            System.out.println(request);
            if (!request.contains("HTTP/1.1") && !request.contains("HTTP/1.0")) {
                System.out.println("HTTP/1.1 400 Bad Request");
                String response = "HTTP/1.1 400 Bad Request" + CRLF;
                response += "Content-Length: 183" + CRLF;
                response += "Content-Type: text/html; charset=iso-8859-1" + CRLF;
                response += "Data: " + new Date().toString() + CRLF;
                response += CRLF;
                try {
                    ostream.write(response.getBytes(), 0, response.length());
                    //发送400
                    ostream.flush();
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            //fileName 长这样 /test/asd/ss.txt
            String fileName = request.split(" ", 3)[1];
            //tempFile初始和path一样
            StringBuilder tempPath = new StringBuilder(path);
            String[] filePath = fileName.split("/");
            //到达底层之前,创建文件夹
            for (int i = 0; i < filePath.length - 1; i++) {
                File f = new File(tempPath.toString() + "/" + filePath[i]);
                tempPath.append("/").append(filePath[i]);
                boolean b = f.mkdirs();
            }
            //创建文件
            File file = new File(tempPath.toString() + "/" + filePath[filePath.length - 1]);

            String response;
            if (file.exists()){
                response = "HTTP/1.1 200 OK"+CRLF;
            }else {
                response = "HTTP/1.1 201 Created"+CRLF;
            }
            response += "Data: " + new Date().toString() + CRLF;
            response += "Content-Location: " + file.getPath() + CRLF;
            response += CRLF;
            // TODO PUT src/resource/1.html HTTP/1.0
            FileOutputStream fos = new FileOutputStream(file);

            ArrayList<byte[]> fileBytes = Handler.receiveFile(istream, request);
            assert fileBytes != null;

            for (byte[] f : fileBytes) {
                fos.write(f);
                fos.flush();
            }

            System.out.println(df.format(new Date()) + "|| " + "file from " + Remote + " received success!" + "And saved in "
                    + tempPath.toString() + "/" + filePath[filePath.length - 1]);


            ostream.write(response.getBytes(), 0, response.length());
            ostream.flush();

            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
