import java.awt.*;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;

public class MouseServer extends JFrame {
    private DataOutputStream out;
    private boolean leftClick;
    public MouseServer() throws Exception {
        setTitle("Mouse Server");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Servidor aguardando conexão...");
        Socket clientSocket = serverSocket.accept();
        System.out.println("Cliente conectado");

        out = new DataOutputStream(clientSocket.getOutputStream());

        trackMouse();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    out.close();
                    clientSocket.close();
                    serverSocket.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        while (true) {
            Point point = MouseInfo.getPointerInfo().getLocation();
            sendMouseEvent(point);
            Thread.sleep(10); // Adjust this value as needed for performance
        }
    }

    private void trackMouse() {
        // Add mouse listeners to this JFrame
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    leftClick = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    leftClick = false;
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                // Nothing needed here, mouse position is tracked in loop
            }
        });
    }

    private void sendMouseEvent(Point point) {
        try {
            out.writeInt(point.x);
            out.writeInt(point.y);
            out.writeBoolean(leftClick);
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
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
        try {
            new MouseServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
