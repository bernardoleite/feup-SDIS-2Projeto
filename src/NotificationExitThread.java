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

public class NotificationExitThread implements Runnable {
    //Notification Exit Channel
    private static NotificationExitChannel notificationExitChannel;
    private static String ip_not_exit = "224.0.0.8";
    private static Integer port_not_exit = 9999;
    
    private String email;

    public NotificationExitThread(String email){
        this.email = email;
        setNotificationExitTravelChannel();

    }

    public static void setNotificationExitTravelChannel() {
        try{
            notificationExitChannel = new NotificationExitChannel(ip_not_exit, port_not_exit);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            notificationExitChannel.receiveMessage(email);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}