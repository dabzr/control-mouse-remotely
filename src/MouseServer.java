import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;

public class MouseServer extends JFrame {
    private DataOutputStream out;
    private boolean leftClick;
    public MouseServer() throws Exception {
        setSize(800, 600);
        setTitle("Mouse Server");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Servidor aguardando conexão...");
        Socket clientSocket = serverSocket.accept();
        System.out.println("Cliente conectado");

        out = new DataOutputStream(clientSocket.getOutputStream());
        DataInputStream in = new DataInputStream(clientSocket.getInputStream());
        out = new DataOutputStream(clientSocket.getOutputStream());

        int screenWidth = in.readInt();
        int screenHeight = in.readInt();
        setSize(screenWidth, screenHeight);
        setLocationRelativeTo(null);
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
            Thread.sleep(10);
        }
    }

    private void trackMouse() {
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
