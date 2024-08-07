import java.awt.*;
import java.awt.event.InputEvent;
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
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            out.writeInt(screenSize.width);
            out.writeInt(screenSize.height);
            out.flush();
            while (true) {

                int x = in.readInt();
                int y = in.readInt();
                boolean leftClick = in.readBoolean();
                robot.mouseMove(x, y);
                if(leftClick){
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                }
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