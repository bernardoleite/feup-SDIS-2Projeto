import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.util.*;
import java.net.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


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

    private static String currentUser="";
    private String email; 

    public Client(){

        if(!menuAuthentication())
        return;

      if(menuOptions())
        return;

    }

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

    public static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }

    public boolean menuAuthentication(){

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
              byte[] receive = sendCLientMessage("authentication", message);
              System.out.println(new String(receive));

            }

          }
          else if (n == 1) {
            System.out.println("Email: ");
            String email = System.console().readLine();
            currentUser=email;
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
              String receive = new String(sendCLientMessage("authentication", message));
              System.out.println(receive +"!");
              String[] splitstr = receive.split(" ");
              String action = splitstr[0];
              if(action.equals("Success")) {
                quit = true;
                this.email=email;
              }
            
            }
          }
          else if (n == 3) {
            return false;
          }
          else {
            System.out.println("Invalid Argument");
            System.out.println();
          }

      }
      System.out.println("You're Logged in");
      return true;      
  }

public boolean menuOptions(){
  boolean quit = false;
  String message="";

  while(!quit) {
      System.out.println();
      System.out.println("Create Travel - 1");
      System.out.println("Delete Travel - 2");
      System.out.println("Manage your Travels - 3");
      System.out.println("Search for a Travel - 4");
      System.out.println("Show your notifications - 5");
      System.out.println("Join Travel - 6");
      System.out.println("Exit Travel - 7");
      System.out.println("Go Back - 8");
      System.out.println();
      int n = Integer.parseInt(System.console().readLine());

      if(n == 1) {
        System.out.println("Please, select the date of the Travel (dd/mm/yyyy HH:mm): ");
        String date = System.console().readLine();
        System.out.println();
        System.out.println("Please, select the Start Point of the Travel: ");
        String startPoint = System.console().readLine();
        System.out.println();
        System.out.println("Please, select the Destination of the Travel: ");
        String endPoint = System.console().readLine();
        System.out.println();
        System.out.println("Please, select the maximum number of Seats of the Travel: ");
        String nrSeats = System.console().readLine();
        System.out.println();
/*
        boolean confirmDate = date.matches("([0-9]{2})/([0-9]{2})/([0-9]{4}/s/d{2}:/d{2})");
        if(!confirmDate){
          System.out.println("Invalid Date");
        }*/
        //else{
            message =  Messages.createTravel(date,startPoint,endPoint,nrSeats,currentUser);
            System.out.println("Message to be Sended: " + message);
            String receive = new String(sendCLientMessage("owner", message));
            String[] splitstr = receive.split(" ");
            String action = splitstr[0]+" "+splitstr[1].trim();
            if(action.equals("Created travelIdentifier"))
              quit = true;
        //}

      }
      else if(n==6){
        System.out.println("Please, select the ID of the Travel: ");
        String travelID= System.console().readLine();

        message= Messages.joinTravel(email, travelID);
        System.out.println(message);
      }

  }

  return true;      
}

    public static void main(String[] args){

        System.setProperty("java.net.preferIPv4Stack", "true");

        Client client = new Client();
    }

    private static byte[] sendCLientMessage(String channel, String clientmessage) {


        SendMessageToChannel sendMessageToChannel = new SendMessageToChannel(channel, clientmessage.getBytes());
        sendMessageToChannel.run();

        byte[] receive = new byte[65000];
        try{
          if(channel.equals("authentication"))
            receive = authChannel.receiveMessage();
          else if(channel.equals("owner"))
            receive = ownerChannel.receiveMessage();
          System.out.println("Message Received: " + new String(receive));
        }
        catch (Exception e) {
          e.printStackTrace();
        }

        return receive;
    }

}
