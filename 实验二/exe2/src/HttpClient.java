import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author chenjing
 */
public class HttpClient {

	/**
	 * Allow a maximum buffer size of 8192 bytes
	 */
	private static final int BUFFER_SIZE = 8192;

	/**
	 * Response is stored in a byte array.
	 */
	private byte[] buffer;

	/**
	 * My socket to the world.
	 */
	Socket socket = null;

	/**
	 * Default port is 2021.
	 */
	private static final int PORT = 2021;

	/**
	 * Output stream to the socket.
	 */
	BufferedOutputStream ostream = null;

	/**
	 * Input stream from the socket.
	 */
	BufferedInputStream istream = null;

	/**
	 * StringBuffer storing the header
	 */
	private StringBuffer header = null;

	/**
	 * StringBuffer storing the response.
	 */
	private StringBuffer response = null;
	/**
	 * String to represent the Carriage Return and Line Feed character sequence.
	 */
	static private final String CRLF = "\r\n";

	/**
	 * 请求文件以字节流数组的形式存储
	 */
	private ArrayList<byte[]> requestFile = null;
	/**
	 * HttpClient constructor;
	 */
	public HttpClient() {
		buffer = new byte[BUFFER_SIZE];
		header = new StringBuffer();
		response = new StringBuffer();
	}

	/**
	 * <em>connect</em> connects to the input host on the default http port --
	 * port 80. This function opens the socket and creates the input and output
	 * streams used for communication.
	 */
	public void connect(String host) throws Exception {

		/**
		 * Open my socket to the specified host at the default port.
		 */
		socket = new Socket(host, PORT);

		/**
		 * Create the output stream. 发送
		 */
		ostream = new BufferedOutputStream(socket.getOutputStream());

		/**
		 * Create the input stream.  接收
		 */
		istream = new BufferedInputStream(socket.getInputStream());
	}

	/**
	 * <em>processGetRequest</em> process the input GET request.
	 */
	public void processGetRequest(String request) throws Exception {
		/**
		 * Send the request to the server.
		 */
		request += CRLF;
		request += "Host: 127.0.0.1" + CRLF;
		request += "Connection: keep-alive" + CRLF;
		request += "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36" + CRLF;
		request += CRLF;
		buffer = request.getBytes();
		ostream.write(buffer, 0, request.length());
		ostream.flush();
		/**
		 * waiting for the response.
		 */
		processResponse(request);
	}

	/**
	 * <em>processPutRequest</em> process the input PUT request.
	 */
	public void processPutRequest(String request) throws Exception {
		//=======start your job here============//
		//Path为路径
		String Path = request.split(" ", 3)[1];
		if (!Path.contains(":") && !Path.startsWith("/")) {
			//不是绝对路径 且不是以/开头
			Path = "/" + Path;
		}

		String path = Path;
		if (path.startsWith("/")) {
			//为相对路径时,改为绝对路径
			path = System.getProperty("user.dir") + path;
		}
		System.out.println(path);
		File file = new File(path);
		if (!file.exists()) {
			System.err.println("File path ERROR!");
			return;
		}
		//构建头
		request = "PUT " + Path + " " + request.split(" ", 3)[2] + CRLF;
		request += "Host: 127.0.0.1" + CRLF;
		request += "ContentType: " + Handler.getContentType(path) + CRLF;
		request += "Accept-Charset: ISO_8859_1" + CRLF;
		request += "Connection: keep-alive" + CRLF;
		request += "Content-Length: " + file.length() + CRLF;
		request += CRLF;
		buffer = request.getBytes(StandardCharsets.UTF_8);
		ostream.write(buffer, 0, request.length());
		ostream.flush();
		System.out.println("request send to server success!");
		if (Handler.sendFile(ostream, file)) {
			System.out.println("File send successfully!");
		}
		processResponse(request);
		//=======end of your job============//
	}

	/**
	 * <em>processResponse</em> process the server response.
	 */
	public void processResponse(String request) throws Exception {
		//如果200OK了

		String head;
		//接受头
		head = Handler.receiveHeader(istream);
		assert head != null;
		header = new StringBuffer(head);

		if (Objects.equals(request.split("\n")[0].split(" ")[0], "GET")) {
			//如果是GET方法，则接受文件
			requestFile = Handler.receiveFile(istream, head);
		}
	}

	/**
	 * Get the response header.
	 */
	public String getHeader() {
		return header.toString();
	}

	/**
	 * 得到从服务器返回的文件
	 * @return 文件字节数组列表
	 */
	public ArrayList<byte[]> getFile() {
		return requestFile;
	}

	/**
	 * Get the server's response.
	 */
	public String getResponse() {
		return response.toString();
	}

	/**
	 * Close all open connections -- sockets and streams.
	 */

	public void close() throws Exception {
		socket.close();
		istream.close();
		ostream.close();
	}
}
