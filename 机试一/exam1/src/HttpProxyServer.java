
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 代理服务器
 * @author chenjing
 */
public class HttpProxyServer {

    /**
     * IP地址
     */
    private static final String HOST = "127.0.0.1";
    /**
     * 端口号
     */
    private static final int PORT = 8000;
    /**
     * 创建套接字
     */
    private final ServerSocket serverSocket;

    private final ExecutorService executorService;

    private static final int POOL_SIZE = 4;

    /**
     * 多线程服务
     * @throws IOException
     */
    public HttpProxyServer() throws IOException{
        serverSocket = new ServerSocket(PORT, 2);
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
                .availableProcessors() * POOL_SIZE);
        System.out.println("代理服务器启动成功");
    }

    /**
     *提供服务
     */
    public void service(){
        Socket socket;
        while (true){
            try{
                socket = serverSocket.accept();
                executorService.execute(new ProxyHelper(socket));
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 启动代理服务器
     * @param args 不需要传递运行实参
     * @throws IOException
     */
    public static void main(String[] args) throws IOException{
        new HttpProxyServer().service();
    }

}
