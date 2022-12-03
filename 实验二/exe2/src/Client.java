

import java.io.*;
import java.util.ArrayList;

/**
 * Class <em>Client</em> is a class representing a simple HTTP client.
 */

public class Client {

	/**
	 * Input is taken from the keyboard
	 */
	static BufferedReader keyboard = new BufferedReader(new InputStreamReader(
			System.in));

	/**
	 * Output is written to the screen (standard out)
	 */
	static PrintWriter screen = new PrintWriter(System.out, true);

	public static void main(String[] args) throws Exception {
		try {
            /*
              Create a new HttpClient object.
             */
			HttpClient myClient = new HttpClient();

			/**
			 * Parse the input arguments.
			 */
			if (args.length != 1) {
				System.err.println("Usage: Client <server>");
				System.exit(0);
			}

			/**
			 * Connect to the input server
			 */
			myClient.connect(args[0]);

			/**
			 * Read the get request from the terminal.
			 */
			screen.println(args[0] + " is listening to your request:");
			String request = keyboard.readLine();
			if (request.startsWith("GET")) {
				/**
				 * Ask the client to process the GET request.
				 */
				myClient.processGetRequest(request);

			} else if (request.startsWith("PUT")) {
				/**
				 * Ask the client to process the PUT request.
				 */
				myClient.processPutRequest(request);
			} else {
				/**
				 * Do not process other request.
				 */
				screen.println("Bad request! \n");
				myClient.close();
				return;
			}

			/**
			 * Get the headers and display them.
			 */
			screen.println("Header: ");
			screen.print(myClient.getHeader() + "\n");
			screen.flush();

			if (request.startsWith("GET")) {
				/**
				 * Ask the user to input a name to save the GET resultant web page.
				 */
				screen.println();
				screen.print("Enter the name of the file to save: ");
				screen.flush();


				String filename = keyboard.readLine();
				//先以文件名创建一个文件夹，然后把收到的文件都放到这个文件夹内
				String dirName = filename.split("\\.")[0];
				File dir = new File(dirName);
				if (!dir.mkdirs()) {
					screen.println("The directory created failed!");
				}

				File html = new File(dirName +"/"+ filename);
				if ((!html.exists() && html.createNewFile()) || html.exists()) {
					//如果文件不存在，则创建
					;
				}

				FileOutputStream outfile = new FileOutputStream(html);
				/**
				 * Save the response to the specified file.
				 * 文件写入
				 */
				ArrayList<byte[]> fileBytes = myClient.getFile();
				StringBuilder response = new StringBuilder();
				for (byte[] b : fileBytes) {
					outfile.write(b);
					outfile.flush();
					response.append(new String(b));
				}
				outfile.close();
				/*
				 * 查看html文件内有多少个img资源，并列表
				 *
				 */
				String[] resources = Handler.getResources(response.toString(), "img");

				for (String r : resources) {
					//对于每个资源文件都发送GET然后接受
					//生成get
					String get = "GET /" + r + " HTTP/1.1";
					//发送GET
					myClient.processGetRequest(get);
					String fileName=r;
					if(myClient.getHeader().contains("HTTP/1.1 400 Bad Request")){
						//如果某个请求头不对，则文件变为400.html
						fileName="400.html";
					}
					if(myClient.getHeader().contains("HTTP/1.1 404 Not Found")){
						//如果资源不存在，则文件变为404.html
						fileName="404.html";
					}
					//写入文件
					File file = new File(dirName + "/" + fileName);
					if ((!file.exists() && file.createNewFile()) || file.exists()) {
						FileOutputStream fos = new FileOutputStream(file);
						fileBytes = myClient.getFile();
						for (byte[] b : fileBytes) {
							fos.write(b);
							fos.flush();
						}
						fos.close();
					}
				}


			}

            /*
              Close the connection client.
             */
			myClient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

