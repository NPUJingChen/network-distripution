import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 客户端
 * 测试代理服务器连接
 * @author chenjing
 */
public class Client {

    static BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
    static PrintWriter screen = new PrintWriter(System.out, true);

    /**
     * 启动客户端
     * @param args 程序实参，输入代理服务器的端口号
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{

        try{
            ClientHelper myClient = new ClientHelper();
            if(args.length != 2){
                System.err.println("Usage: Client <server>");
                System.exit(0);
            }
            myClient.connect(args[0]);
            screen.println(args[0] +" " + args[1] + " is listening to your request:");
            String request = keyboard.readLine();

            /**
             * 判断是否是get请求方法
             */
            if(request.startsWith("GET")){
                myClient.processGetRequest(request);
            }else {
                screen.println("Bad request!\n");
                myClient.close();
                return;
            }

            screen.println("Header: \n");
            screen.print(myClient.getHeader() + "\n");
            screen.flush();

            if (request.startsWith("GET")){
                if(myClient.getHeader().contains("200 OK")){
                    screen.println();
                    screen.print("Enter the name of the file to save: ");
                    screen.flush();
                    String filename = keyboard.readLine();
                    FileOutputStream outfile = new FileOutputStream(filename);
                    String response = myClient.getResponse();

                    outfile.write(response.getBytes(StandardCharsets.UTF_8));
                    outfile.flush();
                    outfile.close();
                }else {
                    screen.println("Bad request!");
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
