import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner

public class Server {

	public static void main(String[] args) {
		
		try {
		
			// create server
			ServerSocket server = new ServerSocket(0);
			
			System.out.println("put this in your web-browser -> http://127.0.0.1:" + server.getLocalPort());
			
			// establish client connection and IO
			Socket client = server.accept();
			Scanner in = new Scanner(client.getInputStream());
			PrintWriter out = new PrintWriter(client.getOutputStream());
			
			// read GET request
			for (int i=0; i<11; i++) {
				System.out.println(in.nextLine());
			}
			
			// write response (remember to have the header!)
			out.print("HTTP/1.1 200 OK\n\n");
			
			out.print("<!DOCTYPE html>"
						+ "<html>"
						+ "<head>"
						+ "<meta charset='UTF-8'>"
						+ "<title>Title of the document</title>"
						+ "</head>"
						+ "<body>"
						+ "Content of the document......"
						+ "</body>"
						+ "</html>");
			
			out.flush();
			
			// close everything
			in.close();
			out.close();
			client.close();
			server.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
