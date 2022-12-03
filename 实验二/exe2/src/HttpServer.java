import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/**
 * @author chenjing
 */
public class HttpServer {

    ServerSocket serverSocket;
    /*端口号*/
    private final int PORT = 2021;
    /*根路径*/
    private static File rootFile;
    private final ExecutorService executorService;
    /*线程池大小*/
    private static final int POOL_SIZE = 4;
    ArrayList<String> IN_LINK_CLIENT = new ArrayList<>();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public HttpServer() throws IOException {
        serverSocket = new ServerSocket(PORT, 2);
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
                .availableProcessors() * POOL_SIZE);
        System.out.println("The server started successfully");
    }

    /**
     * 服务函数
     * @param path 接收路径
     */
    public void service(String path) {
        Socket socket;
        while (true) {
            try {
                socket = serverSocket.accept();
                String client = socket.getInetAddress() + ":" + socket.getPort();
                if (!IN_LINK_CLIENT.contains(client)) {
                    System.out.println(df.format(new Date()) + "|| new connection from " + client);
                }
                executorService.execute(new ServerHelper(socket, path));
            } catch (IOException | RejectedExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        while (true) {
            //当传入多个参数或者没传入参数，都重新输入
            if (args.length != 1) {
                System.out.println("You did not input the right number of parameter,Please enter again:");
                Scanner in = new Scanner(System.in);
                String s = in.nextLine();
                args = s.split(" ");
                continue;
            }

            rootFile = new File(args[0]);
            //文件夹不存在或者不是一个目录
            if (!rootFile.exists() || !rootFile.isDirectory()) {
                System.out.println(rootFile.getAbsolutePath() + " is not a legal path,please enter again:");
                Scanner in = new Scanner(System.in);
                String s = in.nextLine();
                args = s.split(" ");
                continue;
            }
            break;
        }
        System.out.println("Current file path is : " + rootFile.getAbsolutePath());
        try {
            new HttpServer().service(args[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
