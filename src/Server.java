import java.util.*;
import java.net.*;
import java.io.*;

public class Server {

    private static ArrayList<User> users = new ArrayList<User>();
    private static ArrayList<User> admins = new ArrayList<User>();

    //Registration and Log In
    private static String ip_authentication = "224.0.0.3";
    private static Integer port_authentication = 4444;


    //Create and Delete Travel
    private static String ip_owner = "224.0.0.4";
    private static Integer port_owner = 5555;

    //Join and Exit Travel
    private static String ip_joinTravel = "224.0.0.5";
    private static Integer port_joinTravel = 6666;

    //List Travels
    private static String ip_list = "224.0.0.6";
    private static Integer port_list = 7777;


    private static Integer counterUsers = 4;
    private static Integer counterTravels = 1;

    public Server() {

        try{
            createAdministration();

            Thread authChannel = new Thread(new AuthenticationChannel(ip_authentication, port_authentication));
            authChannel.start();

            Thread ownerChannel = new Thread(new OwnerChannel(ip_owner, port_owner));
            ownerChannel.start();

            Thread joinTravelChannel = new Thread(new JoinTravelChannel(ip_joinTravel, port_joinTravel));
            joinTravelChannel.start();

            Thread listChannel = new Thread(new ListChannel(ip_list, port_list));
            listChannel.start();
        }

        catch(Exception e){
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

    public static User getUser(String email){

        for(int i=0; i < users.size();i++){
            if(users.get(i).getEmail().equals(email))
                return users.get(i);
        }
        for(int i=0; i < admins.size();i++){
            if(admins.get(i).getEmail().equals(email))
                return admins.get(i);
        }

        return null;
    }
    

    public static boolean joinTravel(String travelID, String email){
        Integer idTravel = Integer.parseInt(travelID);
        User user = getUser(email);
        
        if(user!=null){
        for(int i=0; i < admins.size();i++){
            if(admins.get(i).getEmail().equals(email))
                user = admins.get(i);
        }

        for(int i=0; i < users.size();i++){
            if(users.get(i).getEmail().equals(email))
                user = users.get(i);
        }

        for(int i=0; i < admins.size();i++){
            if(email.equals(admins.get(i).getEmail()))
                return false;
            for(int j=0; j < admins.get(i).getMyTravels().size();j++){
                if(admins.get(i).getMyTravels().get(j).getID()==idTravel){
                    admins.get(i).getMyTravels().get(j).addPassenger(user);
                }
            }
        }

        return true;
    }
    return false;
    }

    public static ArrayList<User> getUsers(){
        return users;
    }

    public static boolean createNewTravel(Date date, String startPoint, String endPoint,Integer numberOfSeats,User creator){
        
        Travel newTravel = new Travel(counterTravels,date, startPoint, endPoint, numberOfSeats, creator);

        for(int i = 0 ; i < users.size(); i++){
            if(users.get(i).getEmail().equals(creator.getEmail())){
                users.get(i).addMyTravel(newTravel);
                counterTravels++;
            }
        }

        System.out.println("Travel added!");

        return true;
    }

    public static boolean register(Register register){
        counterUsers++;

        users.add(new User(register.getEmail(),register.getPassword(),false,counterUsers));
        System.out.println("New Registration");
        return true;
    }

    public void createAdministration(){
        String password = "123456";

        admins.add(new User("up201505791@fe.up.pt",Integer.toString(password.hashCode()),true,1));
        admins.add(new User("up201404464@fe.up.pt",Integer.toString(password.hashCode()),true,2));
        admins.add(new User("up201506440@fe.up.pt",Integer.toString(password.hashCode()),true,3));
        admins.add(new User("up201404302@fe.up.pt",Integer.toString(password.hashCode()),true,4));
    }


}
