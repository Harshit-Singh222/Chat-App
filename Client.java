
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class Client{

    Socket socket;

    BufferedReader reader;
    PrintWriter writer;

    public Client(){
        try {
            System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("connection done.");

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println("Connection is closed");
        }
    }

    public void startReading(){
        Runnable r1=()->{
            System.out.println("reader started...");

            try {    
                while (true) { 
                    String msg = reader.readLine();
                    if(msg.equals("exit")){
                        System.out.println("Server terminated the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Server : " + msg);
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
                e.printStackTrace();
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("Server is starting...");
        new Client();
    }
}