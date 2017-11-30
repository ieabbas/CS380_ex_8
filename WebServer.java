/* CS 380 - Computer Networks
 * Exercise 8 : HTTP Request & Response
 * Ismail Abbas
 */
 
 import java.io.*;
 import java.net.*;
 
 public class WebServer {

 /*
  * The main method, which does the main things (tries the socket and attempts to listen)
  */
	public static void main(String[]args) {
		System.out.println("\nServer Started: Port 8080");
        try {
            ServerSocket socket = new ServerSocket( 8080 );
			// Running forever, accept the request and close after things check out
            while(true) {
                Socket listenHere = socket.accept();
                httpRequest(listenHere);
                listenHere.close();
            }
        } catch (Exception e) { }
    }
	
	/*
	 * This method wil do the actual sending of the HTTP Response with 
	 * it's Header and file contents
	 */
	public static void loadPage(Socket s, File f, String status) throws Exception {

        PrintWriter printW = new PrintWriter(s.getOutputStream());
        BufferedReader buffRead = new BufferedReader(new FileReader(f));

        printW.println("HTTP/1.1 " + status);
        printW.println("Content-type: text/html");
        printW.println("Content-length: " + f.length());
        printW.println("\r\n");

        for(String lineCheck; (lineCheck = buffRead.readLine()) != null; ) {
            printW.println(lineCheck);
        }
        printW.flush();
        System.out.println("\tResponse Sent: " + status);
    }
	
	/*
	 * This method will read an HTTP request. The line will get split
	 * into a string array and  index with PATH to search if it exists,
	 * all if the line starts with GET
	 */
	public static void httpRequest(Socket s) throws Exception {
        InputStreamReader inputStr = new InputStreamReader(s.getInputStream());
        BufferedReader buffRead = new BufferedReader(inputStr);
        String request = "";
        String filePath = "";

        while((request = buffRead.readLine()) != null) {
            if(request.startsWith("GET")) {
                String[] split = request.split(" ");
                filePath = split[1];
                httpResponse(s, filePath);
                // Breaking after because in the case of this application
                // nothing matters other than the File PATH
                break;
            }
            
        }
    }
	
	/*
	 * This method will check if the file exists, and if true, load with the 
	 * OK status, otherwise it will load the fileNotFound.html and run 404
	 */
	public static void httpResponse(Socket s, String filePath) throws Exception {

        // Remember to change this because this is specific to IntelliJ
        File f = new File("www" + filePath );

        if(f.exists()) {
            System.out.println("\n\n\t" + f.getAbsolutePath());
            loadPage(s, f, "200 OK");
        } else {
            System.out.println("\n\n\tDOES NOT EXIST: " + filePath);
            // Remember to change this because this is specific to IntelliJ
            File notFound = new File("www/fileNotFound.html");
            loadPage(s, notFound, "404 Not Found");
        }

    }
 }