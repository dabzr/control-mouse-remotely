import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MouseServer {
    private static void printIP(){
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String localIpAddress = localHost.getHostAddress();
            System.out.println("Endereço de IP (Mande para o cliente): " + localIpAddress);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static void trackMouse(Socket clientSocket) throws IOException, InterruptedException, AWTException {
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
        DataInputStream in = new DataInputStream(clientSocket.getInputStream());
        Robot robot = new Robot();

        // Thread para enviar a posição do mouse
        Thread sendThread = new Thread(() -> {
            try {
                while (true) {
                    Point point = MouseInfo.getPointerInfo().getLocation();
                    out.writeInt(point.x);
                    out.writeInt(point.y);
                    out.flush();
                    Thread.sleep(10);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Thread para receber e aplicar a posição do mouse
        Thread receiveThread = new Thread(() -> {
            try {
                while (true) {
                    int x = in.readInt();
                    int y = in.readInt();
                    robot.mouseMove(x, y);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        sendThread.start();
        receiveThread.start();
    }
    private static void connect(){
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Servidor aguardando conexão...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado");
            trackMouse(clientSocket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        printIP();
        connect();
    }
}
