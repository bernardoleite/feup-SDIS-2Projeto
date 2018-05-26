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

public class NotificationLeaveThread implements Runnable {
    //Notification Leave Channel
    private static NotificationLeaveChannel notificationLeaveChannel;
    private static String ip_not_leave= "224.0.0.11";
    private static Integer port_not_leave = 8888;
    
    private String email;

    public NotificationLeaveThread(String email){
        this.email = email;
        setNotificationLeaveTravelChannel();

    }

    public static void setNotificationLeaveTravelChannel() {
        try{
            notificationLeaveChannel = new NotificationLeaveChannel(ip_not_leave, port_not_leave);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            notificationLeaveChannel.receiveMessage(email);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}