import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class BetterServer {

  /*
  * This server is able to handle infinite incoming requests at the same time thanks to threading.
  * It also prints request information more nicely and is less prone to sucking.
  */
	
	private class ClientConnection implements Runnable {
		
		Socket socket;
		Scanner in;
		PrintWriter out;
		
		public ClientConnection(Socket socket) throws IOException {
			this.socket = socket;
			in = new Scanner(socket.getInputStream());
			out = new PrintWriter(socket.getOutputStream());
			
			new Thread(this).start();
		}
		
		public void run() {
			
			if (!in.hasNext())
				return;
			
			// read header
			System.out.println("CLIENT:       " + socket.getInetAddress().getHostAddress());
			System.out.println("REQUEST TYPE: " + in.next());
			System.out.println("RESOURCE:     " + in.next());
			System.out.println("PROTOCOL:     " + in.next());
			System.out.println();
			
			// write
			out.print("HTTP/1.1 200 OK\n\n");
		
			try {
				Scanner fileIn = new Scanner(new File("index.html"));
				
				while (fileIn.hasNextLine()) {
					out.print(fileIn.nextLine());
				}
				
				out.flush();
				fileIn.close();
				
			} catch (FileNotFoundException e) {}
			
			// close
			in.close();
			out.close();
			try {
				socket.close();
			} catch (IOException e) {}
			
		}
		
	}
	
	public WebServer(int port) {
		
		try {
			
			// create server
			ServerSocket server = new ServerSocket(port);
			
			System.out.println(
					"http://" + InetAddress.getLocalHost().getHostAddress() +
					":" + server.getLocalPort() + "\n");
			
			new Thread() {
				
				public void run() {
					// establish client connection
					try {
						while (server.isBound())
							new ClientConnection(server.accept());
						
						server.close();
					} catch (IOException e) {
						
					}
				}
				
			}.start();
			
        } catch (IOException e) {
            System.err.println();
        }
		
	}
	
	public static void main(String[] args) {
		new WebServer(0);
	}

}
