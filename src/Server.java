import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.util.*;
import java.net.*;
import java.io.*;



public class Server {

    private static ArrayList<User> users = new ArrayList<User>();
    private static ArrayList<User> admins = new ArrayList<User>();


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


    private static Integer counter = 0;

    public Server(){
        createAdministration();
        setAuthenticationChannel();
        setOwnerChannel();
        setJoinTravelChannel();
        setListChannel();
        authChannel.run();
        ownerChannel.run();
        testChannel.run();
        listChannel.run();
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
        System.out.println(password);
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
        String password = "123456";
        System.out.println(Integer.toString(password.hashCode()));

        admins.add(new User("up201505791@fe.up.pt",Integer.toString(password.hashCode()),true,1));
        admins.add(new User("up201404464@fe.up.pt",Integer.toString(password.hashCode()),true,2));
        admins.add(new User("up201506440@fe.up.pt",Integer.toString(password.hashCode()),true,3));
        admins.add(new User("up201404302@fe.up.pt",Integer.toString(password.hashCode()),true,4));
    }


}
