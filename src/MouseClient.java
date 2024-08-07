import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class MouseClient {
    String host;
    private void HandleIp(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Coloque o IP do servidor: ");
        host = scan.nextLine();
    }
    public void connect(){
        HandleIp();
        try (Socket socket = new Socket(host, 5000)) { // Substitua pelo IP do servidor
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            Robot robot = new Robot();

            // Thread para enviar a posição do mouse
            Thread sendThread = new Thread(() -> {
                try {
                    Point point = MouseInfo.getPointerInfo().getLocation();
                    out.writeInt(point.x);
                    out.writeInt(point.y);
                    out.flush();
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // Thread para receber e aplicar a posição do mouse
            Thread receiveThread = new Thread(() -> {
                try {
                    int x = in.readInt();
                    int y = in.readInt();
                    robot.mouseMove(x, y);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            while(true) {
                sendThread.start();
                receiveThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        MouseClient mouseClient = new MouseClient();
        mouseClient.connect();
    }
}