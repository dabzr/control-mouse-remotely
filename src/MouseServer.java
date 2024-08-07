import java.awt.*;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;

public class MouseServer{
    public static void connect () {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Servidor aguardando conexão...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado");
            trackMouse(clientSocket);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void trackMouse(Socket clientSocket) throws IOException, AWTException, InterruptedException {
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
        Robot robot = new Robot();
        boolean lastLeftButtonState = false;
        while (true) {
            Point point = MouseInfo.getPointerInfo().getLocation();
            boolean leftButtonPressed = (robot.getPixelColor(point.x, point.y).getRGB() != Color.WHITE.getRGB());

            if (leftButtonPressed != lastLeftButtonState) {
                out.writeInt(point.x);
                out.writeInt(point.y);
                out.writeBoolean(leftButtonPressed);
                out.flush();
                lastLeftButtonState = leftButtonPressed;
            }
            Thread.sleep(10);
        }
    }


    private static void printIP() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String localIpAddress = localHost.getHostAddress();
            System.out.println("Endereço de IP (Mande para o cliente): " + localIpAddress);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        printIP();
        connect();
    }
}
