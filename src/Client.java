import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.util.*;
import java.net.*;
import java.io.*;


public class Client {

    private static AuthenticationChannel authChannel;
    private String ip_authentication = "224.0.0.3";
    private Integer port_authentication = 4444;
    public Client(){}

    public void setAuthenticationChannel() {
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

        if(args.length < 3)
            return;

        String action = args[0];
        String email = args[1];
        String password = args[2];
        String message="";

        //Bernardo faz verificação email agora

        if(action.equals("Register")){
            message =  Messages.userRegister(email,password);
        }

        else if(action.equals("Login")){
            message=  Messages.userLogin(email,password);
        }

        System.out.println(message);
        SendMessageToChannel sendMessageToChannel = new SendMessageToChannel("authentication", message.getBytes());
    }
}