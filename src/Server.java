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
                for(int j=0; j < admins.get(i).getMyTravels().size();j++){
                    if(admins.get(i).getMyTravels().get(j).getID()==idTravel){
                        if(email.equals(admins.get(i).getEmail()))
                            return false;
                        return admins.get(i).getMyTravels().get(j).addPassengerRequest(user);
                    }
                }
            }

            for(int i=0; i < users.size();i++){
                for(int j=0; j < users.get(i).getMyTravels().size();j++){
                    if(users.get(i).getMyTravels().get(j).getID()==idTravel){
                        if(email.equals(users.get(i).getEmail()))
                            return false;
                        return users.get(i).getMyTravels().get(j).addPassengerRequest(user);
                    }
                }
            }
            return false;
        }
        return false;
    }

    public static boolean leaveTravel(String travelID, String email){
        Integer idTravel = Integer.parseInt(travelID);
        User user = getUser(email);
        
        if(user!=null){
        

            for(int i=0; i < admins.size();i++){
                for(int j=0; j < admins.get(i).getMyTravels().size();j++){
                    if(admins.get(i).getMyTravels().get(j).getID()==idTravel){
                        if(email.equals(admins.get(i).getEmail()))
                            return false;
                        boolean b = admins.get(i).getMyTravels().get(j).removePassengersRequest(user);
                        System.out.println(admins.get(i).getMyTravels().get(j).getPassengers().size());
                        return b;
                    }
                }
            }

            for(int i=0; i < users.size();i++){
                //Can't do exit of my Travel
                if(email.equals(users.get(i).getEmail()))
                    return false;
                for(int j=0; j < users.get(i).getMyTravels().size();j++){
                    if(users.get(i).getMyTravels().get(j).getID()==idTravel){
                        boolean b = users.get(i).getMyTravels().get(j).removePassengersRequest(user);
                        System.out.println(users.get(i).getMyTravels().get(j).getPassengers().size());
                        return b;
                    }
                }
            }
            return false;
        }
        return false;
    }

    public static ArrayList<User> getUsers(){
        return users;
    }

    public static boolean createNewTravel(Date date, String startPoint, String endPoint,Integer numberOfSeats,String email){
        
        for(int i = 0 ; i < users.size(); i++){
            if(users.get(i).getEmail().equals(email)){
                users.get(i).addMyTravel(new Travel(counterTravels,date, startPoint, endPoint, numberOfSeats, users.get(i)));
                System.out.println(counterTravels);
                counterTravels++;
            }
        }

        for(int i = 0 ; i < admins.size(); i++){
            if(admins.get(i).getEmail().equals(email)){
                admins.get(i).addMyTravel(new Travel(counterTravels,date, startPoint, endPoint, numberOfSeats, admins.get(i)));
                System.out.println(counterTravels);
                counterTravels++;
            }
        }

        System.out.println("Travel added!");

        return true;
    }

    public static ArrayList<User> getTravelPassengers(String email, Integer travelId){
        ArrayList<User> arrayToReturn= new ArrayList<User>();

        for(int i = 0 ; i < users.size(); i++){
            if(users.get(i).getEmail().equals(email)){
                arrayToReturn = users.get(i).getMyTravel(travelId).getPassengers();
            }
        }

        for(int i = 0 ; i < admins.size(); i++){
            if(admins.get(i).getEmail().equals(email)){
                arrayToReturn = admins.get(i).getMyTravel(travelId).getPassengers();
            }
        }
        
        return arrayToReturn;
    }

    public static ArrayList<User> getTravelPassengersRequest(String email, Integer travelId){
        ArrayList<User> arrayToReturn= new ArrayList<User>();

        for(int i = 0 ; i < users.size(); i++){
            if(users.get(i).getEmail().equals(email)){
                arrayToReturn = users.get(i).getMyTravel(travelId).getPassengersRequest();
            }
        }

        for(int i = 0 ; i < admins.size(); i++){
            if(admins.get(i).getEmail().equals(email)){
                arrayToReturn = admins.get(i).getMyTravel(travelId).getPassengersRequest();
            }
        }
        
        return arrayToReturn;
    }

    public static boolean checkUserTravel(String email, Integer travelId){

        for(int i = 0 ; i < users.size(); i++){
            if(users.get(i).getEmail().equals(email)){
                return users.get(i).checkMyTravel(travelId);
            }
        }

        for(int i = 0 ; i < admins.size(); i++){
            if(admins.get(i).getEmail().equals(email)){
                return admins.get(i).checkMyTravel(travelId);
            }
        }
        
        return false;
    }

    public static boolean addPassenger(String email, Integer travelID, String emailPassenger){
        
        User user = getUser(emailPassenger);

        for(int i = 0 ; i < users.size(); i++){
            if(users.get(i).getEmail().equals(email)){
                return users.get(i).getMyTravel(travelID).addPassenger(user);
            }
        }

        for(int i = 0 ; i < admins.size(); i++){
            if(admins.get(i).getEmail().equals(email)){
                return admins.get(i).getMyTravel(travelID).addPassenger(user);
            }
        }
        
        return false;
    }

    public static boolean removePassenger(String email, Integer travelID, String emailPassenger){
    
        User user = getUser(emailPassenger);

        for(int i = 0 ; i < users.size(); i++){
            if(users.get(i).getEmail().equals(email)){
                return users.get(i).getMyTravel(travelID).removePassenger(user);
            }
        }

        for(int i = 0 ; i < admins.size(); i++){
            if(admins.get(i).getEmail().equals(email)){
                return admins.get(i).getMyTravel(travelID).removePassenger(user);
            }
        }
        
        return false;
    }

    public static ArrayList<Travel> getUserJoinTravels(String email){
    
        ArrayList<Travel> joinTravel = new ArrayList<Travel>();
        for(int i = 0 ; i < users.size(); i++){
            if(users.get(i).getEmail().equals(email)){
                joinTravel = users.get(i).getJoinTravels();
            }
        }

        for(int i = 0 ; i < admins.size(); i++){
            if(admins.get(i).getEmail().equals(email)){
                joinTravel = admins.get(i).getJoinTravels();
            }
        }
        return joinTravel;
    }

    public static ArrayList<Travel> getUserRequestTravels(String email){
    
        ArrayList<Travel> joinTravel = new ArrayList<Travel>();
        for(int i = 0 ; i < users.size(); i++){
            if(users.get(i).getEmail().equals(email)){
                joinTravel = users.get(i).getRequestTravels();
            }
        }

        for(int i = 0 ; i < admins.size(); i++){
            if(admins.get(i).getEmail().equals(email)){
                joinTravel = admins.get(i).getRequestTravels();
            }
        }
        return joinTravel;
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
