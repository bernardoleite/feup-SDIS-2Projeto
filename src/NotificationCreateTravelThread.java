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

public class NotificationCreateTravelThread implements Runnable {
    //Notification Add Channel
    private static NotificationCreateTravelChannel notificationCreateTravelChannel;
    private static String ip_not_create = "224.0.0.12";
    private static Integer port_not_create = 8888;
    
    private String email;

    public NotificationCreateTravelThread(String email){
        this.email = email;
        setNotificationCreateTravelChannel();
    }

    public static void setNotificationCreateTravelChannel() {
        try{
            notificationCreateTravelChannel = new NotificationCreateTravelChannel(ip_not_create, port_not_create);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            notificationCreateTravelChannel.receiveMessage(email);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}