import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.util.*;
import java.net.*;
import java.io.*;


public class Client {

    private static AuthenticationChannel authChannel;
    private static String ip_authentication = "224.0.0.3";
    private static Integer port_authentication = 4444;
    public Client(){}

    public static void setAuthenticationChannel() {
        try{
            authChannel = new AuthenticationChannel(ip_authentication, port_authentication);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    public static void sendAuthentication(byte[] message) throws RemoteException, UnknownHostException, InterruptedException {
        authChannel.sendMessage(message);
	}

    public static void main(String[] args){

        System.setProperty("java.net.preferIPv4Stack", "true");

        if(args.length < 3)
            return;

        String action = args[0];
        String email = args[1];
        String password = args[2];
        String message="";
      
        boolean setEmail = email.matches("(.+?)@(fe|fa|fba|fcna|fade|direito|fep|ff|letras|med|fmd|fpce|icbas).up.pt");

        if(!setEmail){
            System.out.println("Invalid Email");
            return;
        }
        else{

            if(action.equals("Register")){
                message =  Messages.userRegister(email,password);
            }

            else if(action.equals("Login")){
                message=  Messages.userLogin(email,password);
            }
        
            System.out.println(message);
            SendMessageToChannel sendMessageToChannel = new SendMessageToChannel("authentication", message.getBytes());
            sendMessageToChannel.run();
        }
        try{
        byte[] receive = authChannel.receiveMessage();
        System.out.println(new String(receive));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}