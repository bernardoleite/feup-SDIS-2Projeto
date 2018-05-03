import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.util.*;
import java.net.*;
import java.io.*;



public class Server {

    private ArrayList<User> users;
    private static AuthenticationChannel authChannel;
    private String ip_authentication = "224.0.0.3";
    private Integer port_authentication = 4444;

    public Server(){
        setAuthenticationChannel();
        authChannel.run();
    }

    public void setAuthenticationChannel() {

        try{
            authChannel = new AuthenticationChannel(ip_authentication, port_authentication);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void main(){
        Server server = new Server();
    }


}
