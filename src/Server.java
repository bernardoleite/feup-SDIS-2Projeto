import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.util.*;
import java.net.*;
import java.io.*;



public class Server {

    private static ArrayList<User> users = new ArrayList<User>();
    private static ArrayList<User> admins = new ArrayList<User>();
    private static AuthenticationChannel authChannel;
    private String ip_authentication = "224.0.0.3";
    private Integer port_authentication = 4444;
    private static Integer counter=4;

    public Server(){
        createAdministration();
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

    public static void main(String[] args){

        System.setProperty("java.net.preferIPv4Stack", "true");

        Server server = new Server();
    }

    public static boolean alreadyAnUser(String email){

        for(int i=0; i < users.size();i++){
            if(users.get(i).getEmail().equals(email))
                return true;
        }
        for(int i=0; i < admins.size();i++){
            if(admins.get(i).getEmail().equals(email))
                return true;
        }

        return false;
    }


    public static boolean existUser(String email,String password){

        for(int i=0; i < users.size();i++){
            if(users.get(i).getEmail().equals(email) && users.get(i).getPassword().equals(password))
                return true;
        }
        for(int i=0; i < admins.size();i++){
            if(admins.get(i).getEmail().equals(email) && admins.get(i).getPassword().equals(password))
                return true;
        }

        return false;
    }

    public static boolean register(Register register){
        counter++;

        users.add(new User(register.getEmail(),register.getPassword(),false,counter));
        System.out.println("New Registration");
        return true;
    }

    public void createAdministration(){
        admins.add(new User("up201505791@fe.up.pt","123456",true,1));
        admins.add(new User("up201404464@fe.up.pt","123456",true,2));
        admins.add(new User("up201506440@fe.up.pt","123456",true,3));
        admins.add(new User("up201404302@fe.up.pt","123456",true,4));
    }


}
