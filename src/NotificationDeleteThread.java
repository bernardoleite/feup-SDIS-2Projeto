import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.io.IOException;
import java.net.DatagramSocket;
import java.io.*;
import java.util.*;

public class NotificationDeleteThread implements Runnable {
    //Notification Delete Channel
    private static NotificationDeleteChannel notificationDeleteChannel;
    private static String ip_not_delete = "224.0.0.9";
    private static Integer port_not_delete = 9999;
    
    private String email;

    public NotificationDeleteThread(String email){
        this.email = email;
        setNotificationDeleteTravelChannel();

    }

    public static void setNotificationDeleteTravelChannel() {
        try{
            notificationDeleteChannel = new NotificationDeleteChannel(ip_not_delete, port_not_delete);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            notificationDeleteChannel.receiveMessage(email);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}