import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务端
 *
 * @author chenjing
 */
public class FileServer {

    /*TCP端口号*/
    static final int TCP_PORT = 2021;
    /*线程池大小*/
    final int POOL_SIZE = 4;
    /*根目录*/
    private static File rootFile;

    /*服务端套接字*/
    private ServerSocket serverSocket = null;
    /*线程池*/
    private ExecutorService executorService = null;

    /**
     * 构造函数
     * @throws IOException
     */
    public FileServer() throws IOException{
        // 绑定一个地址
        serverSocket = new ServerSocket(TCP_PORT);
        // 创建线程池
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
                .availableProcessors() * POOL_SIZE);
        System.out.println("服务器启动。");
    }

    /**
     * 循环接收tcp请求，分配线程，开启服务
     * @param path 初始目录
     */
    public void service(String path){

        Socket socket = null;

        while (true){

            try{

                socket = serverSocket.accept();
                // 用线程池来维护线程
                executorService.execute(new Handler(socket, path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 主函数
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1){
            System.out.println("usage: java FileServer <dir>.");
            return;
        }

        rootFile = new File(args[0]);

        if (!rootFile.exists() || !rootFile.isDirectory()){
            // 文件夹不存在/不是一个文件夹
            System.out.println(rootFile.getAbsoluteFile() +
                    " is not a legal path.");
            return;
        }

        FileServer fileServer = new FileServer();
        fileServer.service(args[0]);
    }

}
