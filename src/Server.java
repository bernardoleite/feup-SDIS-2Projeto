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

    //Notification Join Channel
    private static NotificationJoinChannel notificationJoinChannel;
    private static String ip_not_join = "224.0.0.7";
    private static Integer port_not_join = 8888;

    //Notification Exit Channel
    private static NotificationExitChannel notificationExitChannel;
    private static String ip_not_exit = "224.0.0.8";
    private static Integer port_not_exit = 9999;

    //Notification Delete Channel
    private static NotificationDeleteChannel notificationDeleteChannel;
    private static String ip_not_delete = "224.0.0.9";
    private static Integer port_not_delete = 9999;

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
        boolean returnBoolean = false;
        String emailCreator = "";
        if(user!=null){
            for(int i=0; i < admins.size();i++){
                for(int j=0; j < admins.get(i).getMyTravels().size();j++){
                    if(admins.get(i).getMyTravels().get(j).getID()==idTravel){
                        if(email.equals(admins.get(i).getEmail()))
                            returnBoolean = false;
                        else{
                            returnBoolean = admins.get(i).getMyTravels().get(j).addPassengerRequest(user);
                            emailCreator = admins.get(i).getEmail();
                        }
                    }
                }
            }

            for(int i=0; i < users.size();i++){
                for(int j=0; j < users.get(i).getMyTravels().size();j++){
                    if(users.get(i).getMyTravels().get(j).getID()==idTravel){
                        if(email.equals(users.get(i).getEmail()))
                            returnBoolean = false;
                        else{
                            returnBoolean = users.get(i).getMyTravels().get(j).addPassengerRequest(user);
                            emailCreator = users.get(i).getEmail();
                        }
                    }
                }
            }
        }
        if (returnBoolean) {
            System.out.println("Send Notification to User!!!");
            try {
                Thread notificationJoinChannel = new Thread(new NotificationJoinChannel(ip_not_join, port_not_join, emailCreator, Messages.sendNotificationJoinTravel(emailCreator, travelID, email)));
                notificationJoinChannel.start();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return returnBoolean;
    }

    public static void sendNotificationExitTravel(String emailCreator, String message) {
        System.out.println("Send Notification to User!!!");
        try {
            Thread notificationExitChannel = new Thread(new NotificationExitChannel(ip_not_exit, port_not_exit, emailCreator, message));
            notificationExitChannel.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendNotificationDeleteTravel(String emailCreator, String message) {
        System.out.println("Send Notification to User!!!");
        try {
            Thread notificationDeleteChannel = new Thread(new NotificationDeleteChannel(ip_not_delete, port_not_delete, emailCreator, message));
            notificationDeleteChannel.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
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

    public static boolean deleteTravel(String creator, int travelIdentifier){

        //remove travel from user request travels
        for(int i = 0 ; i < users.size(); i++){
            for(int j = 0 ; j < users.get(i).getRequestTravels().size(); j++){
                if(users.get(i).getRequestTravels().get(j).getID() == travelIdentifier){
                    users.get(i).deleteRequestTravel(travelIdentifier);
                    System.out.println("HERE!!!!");
                    sendNotificationDeleteTravel(users.get(i).getEmail(), Messages.sendNotificationDeleteTravel(users.get(i).getEmail(), Integer.toString(travelIdentifier), creator));
                }
            }
        }

        //remove travel from users joined
        for(int i = 0 ; i < users.size(); i++){
            for(int j = 0 ; j < users.get(i).getJoinTravels().size(); j++){
                if(users.get(i).getJoinTravels().get(j).getID() == travelIdentifier){
                    users.get(i).deleteJoinTravel(travelIdentifier);
                    System.out.println("HERE!!!!");
                    sendNotificationDeleteTravel(users.get(i).getEmail(), Messages.sendNotificationDeleteTravel(users.get(i).getEmail(), Integer.toString(travelIdentifier), creator));
                }
            }
        }


        //remove travel from user request travels
        for(int i = 0 ; i < admins.size(); i++){
            for(int j = 0 ; j < admins.get(i).getRequestTravels().size(); j++){
                if(admins.get(i).getRequestTravels().get(j).getID() == travelIdentifier){
                    admins.get(i).deleteRequestTravel(travelIdentifier);
                    System.out.println("HERE!!!!");
                    sendNotificationDeleteTravel(admins.get(i).getEmail(), Messages.sendNotificationDeleteTravel(admins.get(i).getEmail(), Integer.toString(travelIdentifier), creator));
                }
            }
        }

        //remove travel from users joined
        for(int i = 0 ; i < admins.size(); i++){
            for(int j = 0 ; j < admins.get(i).getJoinTravels().size(); j++){
                if(admins.get(i).getJoinTravels().get(j).getID() == travelIdentifier){
                    admins.get(i).deleteJoinTravel(travelIdentifier);
                    System.out.println("HERE!!!!");
                    sendNotificationDeleteTravel(admins.get(i).getEmail(), Messages.sendNotificationDeleteTravel(admins.get(i).getEmail(), Integer.toString(travelIdentifier), creator));
                }
            }
        }

        //remove travel from the user creator
        for(int i = 0 ; i < users.size(); i++){
            if(users.get(i).getEmail().equals(creator)){
                users.get(i).deleteMyTravel(travelIdentifier);
            }
        }

        //remove travel from admins
        for(int i = 0 ; i < admins.size(); i++){
            if(admins.get(i).getEmail().equals(creator)){
                admins.get(i).deleteMyTravel(travelIdentifier);
            }
        }

        System.out.println("Travel deleted!");

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


    public static ArrayList<Travel> getUserMyTravels(String email){
    
        ArrayList<Travel> joinTravel = new ArrayList<Travel>();
        for(int i = 0 ; i < users.size(); i++){
            if(users.get(i).getEmail().equals(email)){
                joinTravel = users.get(i).getMyTravels();
            }
        }

        for(int i = 0 ; i < admins.size(); i++){
            if(admins.get(i).getEmail().equals(email)){
                joinTravel = admins.get(i).getMyTravels();
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
