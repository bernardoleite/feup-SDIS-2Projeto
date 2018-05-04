import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.util.*;
import java.net.*;
import java.io.*;


public class Client {

    //Registration and Log In
    private static AuthenticationChannel authChannel;
    private static String ip_authentication = "224.0.0.3";
    private static Integer port_authentication = 4444;


    //Create and Delete Travel
    private static OwnerChannel ownerChannel;
    private static String ip_owner = "224.0.0.4";
    private static Integer port_owner = 5555;


    //Join and Exit Travel
    private static JoinTravelChannel testChannel;
    private static String ip_test = "224.0.0.5";
    private static Integer port_test = 6666;

    //List Travels
    private static ListChannel listChannel;
    private static String ip_list = "224.0.0.6";
    private static Integer port_list = 7777;


    public Client(){}

    public static void setAuthenticationChannel() {
        try{
            authChannel = new AuthenticationChannel(ip_authentication, port_authentication);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void setOwnerChannel() {
        try{
            ownerChannel = new OwnerChannel(ip_owner, port_owner);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void setJoinTravelChannel() {
        try{
            testChannel = new JoinTravelChannel(ip_test, port_test);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void setListChannel() {
        try{
            listChannel = new ListChannel(ip_list, port_list);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    public static void sendAuthentication(byte[] message) throws RemoteException, UnknownHostException, InterruptedException {
        authChannel.sendMessage(message);
	  }

    public static void sendOwner(byte[] message) throws RemoteException, UnknownHostException, InterruptedException {
        ownerChannel.sendMessage(message);
    }

    public static void main(String[] args){

        System.setProperty("java.net.preferIPv4Stack", "true");

        boolean quit = false;

        while(!quit) {
            System.out.println();
            System.out.println("Log In - 1");
            System.out.println("Register - 2");
            System.out.println("Exit - 3");
            System.out.println();
            int n = Integer.parseInt(System.console().readLine());

            if(n == 2) {
              System.out.println("Email: ");
              String email = System.console().readLine();
              System.out.println();
              char passwordArray[] = System.console().readPassword("Enter your password: ");
              System.out.println();
              String password = new String(passwordArray);
              System.out.println();


              String message="";


              boolean setEmail = email.matches("(.+?)@(fe|fa|fba|fcna|fade|direito|fep|ff|letras|med|fmd|fpce|icbas).up.pt");
              if(!setEmail){
                System.out.println("Invalid Email");
              }
              else{
                message =  Messages.userRegister(email,Integer.toString(password.hashCode()));
                byte[] receive = sendCLientMessage(message);
                System.out.println(new String(receive));

              }

            }
            else if (n == 1) {
              System.out.println("Email: ");
              String email = System.console().readLine();
              System.out.println();
              char passwordArray[] = System.console().readPassword("Enter your password: ");
              System.out.println();
              String password = new String(passwordArray);
              System.out.println();


              String message="";


              boolean setEmail = email.matches("(.+?)@(fe|fa|fba|fcna|fade|direito|fep|ff|letras|med|fmd|fpce|icbas).up.pt");
              if(!setEmail){
                System.out.println("Invalid Email");
              }
              else{
                message =  Messages.userLogin(email,Integer.toString(password.hashCode()));
                String receive = new String(sendCLientMessage(message));
                System.out.println(receive +"!");
                String[] splitstr = receive.split(" ");
                String action = splitstr[0];
                if(action.equals("Success"))
                  quit = true;
              }
            }
            else if (n == 3) {
              return;
            }
            else {
              System.out.println("Invalid Argument");
              System.out.println();
            }

        }
        System.out.println("You're Logged in");

    }

    private static byte[] sendCLientMessage(String clientmessage) {
        System.out.println(clientmessage + " HERE");

        SendMessageToChannel sendMessageToChannel = new SendMessageToChannel("authentication", clientmessage.getBytes());
        sendMessageToChannel.run();

        byte[] receive = new byte[65000];
        try{
          receive = authChannel.receiveMessage();
          System.out.println(new String(receive));
        }
        catch (Exception e) {
          e.printStackTrace();
        }

        return receive;
    }

}
