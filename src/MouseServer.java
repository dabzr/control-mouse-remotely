import java.awt.MouseInfo;
import java.awt.Point;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

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
    private static void trackMouse(Socket clientSocket) throws IOException, InterruptedException {
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

        while (true) {
            Point point = MouseInfo.getPointerInfo().getLocation();
            out.writeInt(point.x);
            out.writeInt(point.y);
            out.flush();
            Thread.sleep(10);
        }
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
