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

public class NotificationAddThread implements Runnable {
    //Notification Add Channel
    private static NotificationAddChannel notificationAddChannel;
    private static String ip_not_add = "224.0.0.10";
    private static Integer port_not_add = 9999;
    
    private String email;

    public NotificationAddThread(String email){
        this.email = email;
        setNotificationAddPassengerChannel();
    }

    public static void setNotificationAddPassengerChannel() {
        try{
            notificationAddChannel = new NotificationAddChannel(ip_not_add, port_not_add);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            notificationAddChannel.receiveMessage(email);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}