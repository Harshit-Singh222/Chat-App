
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

class Server extends JFrame {

    ServerSocket server;
    Socket socket;

    BufferedReader reader;
    PrintWriter writer;

    private JLabel heading = new JLabel("Serevr Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    public Server() {
        try {
            server = new ServerSocket(7777);
            System.out.println("Server is ready to accept connection.");
            System.out.println("waiting...");
            socket = server.accept();

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();

            startReading();
            // startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createGUI() {
        this.setTitle("Server Messager");
        this.setSize(500, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        messageInput.setFont(font);

        messageArea.setFont(font);
        messageArea.setEditable(false);
        JScrollPane scrollPaneMessageArea = new JScrollPane(messageArea);

        // heading.setIcon(new ImageIcon("img.png"));
        heading.setFont(font);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        this.setLayout(new BorderLayout());
        this.add(heading, BorderLayout.NORTH);
        this.add(scrollPaneMessageArea, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);

    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    // System.out.println("You have pressed enter");
                    String contentToSend = messageInput.getText();
                    if (!contentToSend.trim().isEmpty()) {
                        messageArea.append("Me : " + contentToSend + "\n");
                        writer.println(contentToSend);
                        writer.flush();
                        messageInput.setText("");
                    }
                }
            }

        });
    }

    public void startReading() {
        Runnable r1 = () -> {
            System.out.println("reader started...");

            try {
                while (true) {
                    String msg = reader.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Client terminated the chat");
                        JOptionPane.showMessageDialog(this, "Client termonated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    messageArea.append("Server : " + msg + "\n");
                    messageArea.setCaretPosition(messageArea.getDocument().getLength());
                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("Connection is closed");
            }
        };
        new Thread(r1).start();
    }

    public void startWriting() {
        Runnable r2 = () -> {
            try {
                System.out.println("writer started...");
                while (!socket.isClosed()) {
                    BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = reader1.readLine();
                    writer.println(content);
                    writer.flush();

                    if (content.equals("exit")) {
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
