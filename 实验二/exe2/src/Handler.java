import java.io.*;
import java.util.ArrayList;

/**
 * @author chenjing
 */
public class Handler {

    private static final int BUFFER_SIZE = 8 * 1024;

    /**
     * 发送文件的方法
     * @param ostream 输出用的字节流
     * @param file 需要发送的文件
     * @return 发送是否成功
     * @throws IOException 读写异常
     */
    public static boolean sendFile(BufferedOutputStream ostream, File file) throws IOException {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);

            long contentLength = file.length();
            int bufferSize = Math.toIntExact(Math.min(BUFFER_SIZE, contentLength));
            if (BUFFER_SIZE < contentLength) {
                contentLength = contentLength - BUFFER_SIZE;
            }
            byte[] buffer = new byte[bufferSize];


            while (fileInputStream.read(buffer) >= 0) {
                ostream.write(buffer, 0, buffer.length);
                System.out.println("--------file-----");
                ostream.flush();

                if(fileInputStream.available()==0) {
                    break;
                }
                if (contentLength > BUFFER_SIZE) {
                    bufferSize = BUFFER_SIZE;
                    contentLength = contentLength - BUFFER_SIZE;
                } else {
                    bufferSize = Math.toIntExact(contentLength);
                }
                buffer = new byte[bufferSize];
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 文件接收方法
     * @param istream 用于发送的流
     * @param head 请求头，用于获得文件长度
     * @return 接收到的文件以字节数组列表的形式存储
     * @throws IOException 读写异常
     */
    public static ArrayList<byte[]> receiveFile(BufferedInputStream istream, String head) throws IOException {
        try {
            ArrayList<byte[]> fileBytes=new ArrayList < byte[] > ();
            int contentLength=getContentLength(head);
            int bufferSize = Math.min(BUFFER_SIZE, contentLength);
            byte[] buffer = new byte[bufferSize];
            while (istream.read(buffer) != -1) {
                fileBytes.add(buffer);
                contentLength-=buffer.length;
                if(contentLength<=0) {
                    break;
                }
                bufferSize = Math.min(BUFFER_SIZE, contentLength);
                buffer = new byte[bufferSize];

            }
            return fileBytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从流中读入头
     * @param istream 文件读流
     * @return 头以字符串的形式返回
     */
    public static String receiveHeader(BufferedInputStream istream) {
        try {
            int last = 0, c = 0;
            boolean inHeader = true; // loop control
            StringBuilder header = new StringBuilder();
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
                        //System.out.print((char) c);
                }
            }
            return header.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从文件名中获取文件类型
     * @param fileName 文件名
     * @return 文件类型
     */
    public static String getContentType(String fileName) {
        if (fileName.endsWith(".jpg") || fileName.endsWith("jpeg")) {
            return "image/jpeg";
        }
        if (fileName.endsWith(".txt")) {
            return "ext/plain";
        }
        if (fileName.endsWith(".html")) {
            return "text/html";
        }
        if (fileName.endsWith(".json")) {
            return "application/json";
        }
        if (fileName.endsWith(".pdf")) {
            return "application/pdf";
        }
        return "application/octet-stream";
    }

    /**
     * 从请求头中获取文件大小
     * @param head 请求头
     * @return 文件大小
     */
    public static int getContentLength(String head){
        String []heads=head.split("\n");
        for(String s:heads){
            if(s.contains("Content-Length")){
                return Integer.parseInt(s.split(" ")[1]);
            }
        }
        return 0;
    }

    /**
     * 从html文件中获取类型为resName的文件列表
     * @param html html文件
     * @param resName 类型
     * @return 文件列表
     */
    public static String[] getResources(String html,String resName){
        ArrayList<String> resources=new ArrayList<String> ();
        String []elem=html.split("<");
        for(String e:elem){
            if(e.startsWith(resName)){
                String []token=e.split("\"");
                for(int i=0;i<token.length; i++){
                    if(token[i].contains("src")){
                        resources.add(token[i+1]);
                        break;
                    }
                }
            }
        }
        for(int i=0;i<resources.size(); i++){
            resources.set(i, resources.get(i).replaceAll(" ", ""));
        }

        return resources.toArray(new String[0]);
    }


}
