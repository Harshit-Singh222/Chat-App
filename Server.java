import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


class Server{

    ServerSocket server;
    Socket socket;

    BufferedReader reader;
    PrintWriter writer;

    public Server(){
        try {
            server = new ServerSocket(7777);
            System.out.println("Server is ready to accept connection.");
            System.out.println("waiting...");
            socket = server.accept();

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public void startReading(){
        Runnable r1=()->{
            System.out.println("reader started...");

            try {    
                while (true) { 
                    String msg = reader.readLine();
                    if(msg.equals("exit")){
                        System.out.println("Client terminated the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Client : " + msg);
                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("Connection is closed");
            }
        };
        new Thread(r1).start();
    }
    
    public void startWriting(){
        Runnable r2=()->{
            try {
                System.out.println("writer started...");
                while(!socket.isClosed()){
                    BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = reader1.readLine();
                    writer.println(content);
                    writer.flush();

                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }
                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("Connection is closed");
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("Server is starting...");
        new Server();
    }
}