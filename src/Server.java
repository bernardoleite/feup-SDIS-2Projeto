import java.util.*;
import java.net.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import java.net.Socket;
import java.net.ServerSocket;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.Reader;
import java.io.BufferedReader;


public class Server implements Serializable{

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
    private static String ip_not_join = "224.0.0.7";
    private static Integer port_not_join = 8888;

    //Notification Exit Channel
    private static String ip_not_exit = "224.0.0.8";
    private static Integer port_not_exit = 9999;

    //Notification Delete Channel
    private static String ip_not_delete = "224.0.0.9";
    private static Integer port_not_delete = 9999;

    //Notification Add Channel
    private static String ip_not_add = "224.0.0.10";
    private static Integer port_not_add = 9999;
    
    //Notification Leave Channel
    private static String ip_not_leave = "224.0.0.11";
    private static Integer port_not_leave = 8888;

    //Notification Create Travel Channel
    private static String ip_not_create = "224.0.0.12";
    private static Integer port_not_create = 8888;

    private static Integer counterUsers = 4;
    private static Integer counterTravels = 1;

    //store information
    private static ArrayOfFiles currentFiles = new ArrayOfFiles();

    private static String filebin = "data.bin";

    public static void deserialize_Object(){

      try{
          ObjectInputStream is = new ObjectInputStream(new FileInputStream(filebin));
          currentFiles = (ArrayOfFiles)is.readObject();
          is.close();
          users.addAll(currentFiles.users);
          admins.addAll(currentFiles.admins);
          counterUsers = currentFiles.counterUsers;
          counterTravels = currentFiles.counterTravels;
      }
      catch(Exception e){
          e.printStackTrace();
      }

    }
    public static void serialize_Object(){

      filebin = "data.bin";
      try{
        currentFiles.users.clear();
        currentFiles.admins.clear();
        currentFiles.users.addAll(users);
        currentFiles.admins.addAll(admins);
        currentFiles.counterUsers = counterUsers;
        currentFiles.counterTravels = counterTravels;
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filebin));
        os.writeObject(currentFiles);
        os.close();
      }
      catch(Exception e)
      {
          e.printStackTrace();
      }

    }

    public Server() throws Exception {

        try{
              createAdministration();

              File f = new File("data.bin");
              if(f.exists() && !f.isDirectory()) {
                deserialize_Object();
              }
              else {
                currentFiles = new ArrayOfFiles();
              }

            Thread authChannel = new Thread(new ServerAuthChannel());
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

    public static void main(String[] args) throws Exception{

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
    
    public static ArrayList<User> getAdmins(){
        return admins;
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
                Thread notificationJoinThread = new Thread(new NotificationJoinChannel(ip_not_join, port_not_join, emailCreator, Messages.sendNotificationJoinTravel(emailCreator, travelID, email)));
                notificationJoinThread.start();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return returnBoolean;
    }

    public static void sendNotificationExitTravel(String emailCreator, String message) {
        System.out.println("Send Notification to User!!!");
        try {
            Thread notificationExitThread = new Thread(new NotificationExitChannel(ip_not_exit, port_not_exit, emailCreator, message));
            notificationExitThread.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendNotificationDeleteTravel(String emailCreator, String message) {
        System.out.println("Send Notification to User!!!");
        try {
            Thread notificationDeleteThread = new Thread(new NotificationDeleteChannel(ip_not_delete, port_not_delete, emailCreator, message));
            notificationDeleteThread.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendNotificationAddPassenger(String emailCreator, String message) {
        System.out.println("Send Notification to User!!!");
        try {
            Thread notificationAddThread= new Thread(new NotificationAddChannel(ip_not_add, port_not_add, emailCreator, message));
            notificationAddThread.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendNotificationLeaveTravel(String emailCreator, String message) {
        System.out.println("Send Notification to User!!!");
        try {
            Thread notificationLeaveThread= new Thread(new NotificationLeaveChannel(ip_not_leave, port_not_leave, emailCreator, message));
            notificationLeaveThread.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendNotificationCreateTravel(String emailCreator, String message) {
        System.out.println("Send Notification to User!!!");
        try {
            Thread notificationCreateTravelThread= new Thread(new NotificationCreateTravelChannel(ip_not_create, port_not_create, emailCreator, message));
            notificationCreateTravelThread.start();
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
                if(users.get(i).addMyTravel(new Travel(counterTravels,date, startPoint, endPoint, numberOfSeats, users.get(i)))){
                    checkNotificationsWaitForTravel(date, startPoint, endPoint, counterTravels);
                    System.out.println(counterTravels);
                    counterTravels++;
                    serialize_Object();
                    return true;
                }
            }
        }

        for(int i = 0 ; i < admins.size(); i++){
            if(admins.get(i).getEmail().equals(email)){
                if(admins.get(i).addMyTravel(new Travel(counterTravels,date, startPoint, endPoint, numberOfSeats, admins.get(i)))) {
                    checkNotificationsWaitForTravel(date, startPoint, endPoint, counterTravels);
                    System.out.println(counterTravels);
                    counterTravels++;
                    serialize_Object();
                    return true;
                }
            }
        }

        System.out.println("Travel Exists!");
        return false;
    }

public static boolean deleteTravel(String creator, int travelIdentifier){

        Boolean isthisAdmin = false;
        for(int i = 0 ; i < admins.size(); i++){
            if(admins.get(i).getEmail().equals(creator)){
                isthisAdmin = true;
            }
        }        

        //remove travel from user request travels
        for(int i = 0 ; i < users.size(); i++){
            for(int j = 0 ; j < users.get(i).getRequestTravels().size(); j++){
                if(users.get(i).getRequestTravels().get(j).getID() == travelIdentifier){
                    users.get(i).deleteRequestTravel(travelIdentifier);
                    sendNotificationDeleteTravel(users.get(i).getEmail(), Messages.sendNotificationDeleteTravel(users.get(i).getEmail(), Integer.toString(travelIdentifier), creator));
                    break;
                }
            }
        }

        //remove travel from users joined
        for(int i = 0 ; i < users.size(); i++){
            for(int j = 0 ; j < users.get(i).getJoinTravels().size(); j++){
                if(users.get(i).getJoinTravels().get(j).getID() == travelIdentifier){
                    users.get(i).deleteJoinTravel(travelIdentifier);
                    sendNotificationDeleteTravel(users.get(i).getEmail(), Messages.sendNotificationDeleteTravel(users.get(i).getEmail(), Integer.toString(travelIdentifier), creator));
                    break;
                }
            }
        }


        //remove travel from user request travels
        for(int i = 0 ; i < admins.size(); i++){
            for(int j = 0 ; j < admins.get(i).getRequestTravels().size(); j++){
                if(admins.get(i).getRequestTravels().get(j).getID() == travelIdentifier){
                    admins.get(i).deleteRequestTravel(travelIdentifier);
                    sendNotificationDeleteTravel(admins.get(i).getEmail(), Messages.sendNotificationDeleteTravel(admins.get(i).getEmail(), Integer.toString(travelIdentifier), creator));
                    break;
                }
            }
        }

        //remove travel from users joined
        for(int i = 0 ; i < admins.size(); i++){
            for(int j = 0 ; j < admins.get(i).getJoinTravels().size(); j++){
                if(admins.get(i).getJoinTravels().get(j).getID() == travelIdentifier){
                    admins.get(i).deleteJoinTravel(travelIdentifier);
                    sendNotificationDeleteTravel(admins.get(i).getEmail(), Messages.sendNotificationDeleteTravel(admins.get(i).getEmail(), Integer.toString(travelIdentifier), creator));
                    break;
                }
            }
        }

        //remove travel from the user creator
        for(int i = 0 ; i < users.size(); i++){
            if(users.get(i).getEmail().equals(creator) || isthisAdmin){
                if(users.get(i).deleteMyTravel(travelIdentifier)) {
                    serialize_Object();
                    return true;
                }
            }
        }

        //remove travel from admins
        for(int i = 0 ; i < admins.size(); i++){
            if(admins.get(i).getEmail().equals(creator) || isthisAdmin){
                if(admins.get(i).deleteMyTravel(travelIdentifier)) {
                    serialize_Object();
                    return true;
                }
            }
        }

        System.out.println("Travel Not Deleted!");
        return false;
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
                if(users.get(i).getMyTravel(travelID).addPassenger(user)){
                    //Send Notification
                    sendNotificationAddPassenger(emailPassenger, Messages.sendNotificationAddPassenger(emailPassenger, Integer.toString(travelID), users.get(i).getEmail()));

                    Server.serialize_Object();
                    return true;
                }
            }
        }

        for(int i = 0 ; i < admins.size(); i++){
            if(admins.get(i).getEmail().equals(email)){
                if(admins.get(i).getMyTravel(travelID).addPassenger(user)){
                    //Send Notification
                    sendNotificationAddPassenger(emailPassenger, Messages.sendNotificationAddPassenger(emailPassenger, Integer.toString(travelID), admins.get(i).getEmail()));

                    Server.serialize_Object();   
                    return true;                 
                }
            }
        }
        
        return false;
    }

    public static boolean removePassenger(String email, Integer travelID, String emailPassenger){
    
        User user = getUser(emailPassenger);

        for(int i = 0 ; i < users.size(); i++){
            if(users.get(i).getEmail().equals(email)){
                if(users.get(i).getMyTravel(travelID).removePassenger(user)){
                    Server.serialize_Object();   
                    return true; 
                }
            }
        }

        for(int i = 0 ; i < admins.size(); i++){
            if(admins.get(i).getEmail().equals(email)){
                if(admins.get(i).getMyTravel(travelID).removePassenger(user)){
                    Server.serialize_Object();   
                    return true; 
                }
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

    public static boolean checkAdmin(String email){

        for(int i = 0 ; i < admins.size(); i++){
            if(admins.get(i).getEmail().equals(email)){
                return true;
            }
        }

        return false;

    }

    public static ArrayList<Travel> getAllTravels(){

        ArrayList<Travel> allTravels = new ArrayList<Travel>();

        for(int i = 0 ; i < users.size(); i++){
            for( int j = 0 ; j < users.get(i).getMyTravels().size(); j++){
                if(!allTravels.contains(users.get(i).getMyTravels().get(j)))
                    allTravels.add(users.get(i).getMyTravels().get(j));
            }
        }

        for(int i = 0 ; i < admins.size(); i++){
            for( int j = 0 ; j < admins.get(i).getMyTravels().size(); j++){
                if(!allTravels.contains(admins.get(i).getMyTravels().get(j)))
                    allTravels.add(admins.get(i).getMyTravels().get(j));
            }
        }

        return allTravels;
    }

    public static ArrayList<Travel> getSpecificTravels(Date dateform, String startPoint, String endPoint){


        SimpleDateFormat dateHour = new SimpleDateFormat("HH");
        SimpleDateFormat dateDay = new SimpleDateFormat("dd");
        SimpleDateFormat dateMonth = new SimpleDateFormat("MM");
        SimpleDateFormat dateYear = new SimpleDateFormat("yyyy");

        int hour = Integer.parseInt(dateHour.format(dateform));
        int dayint = Integer.parseInt(dateDay.format(dateform));
        int month = Integer.parseInt(dateMonth.format(dateform));
        int year = Integer.parseInt(dateYear.format(dateform));

        String day = Integer.toString(dayint)+"/"+Integer.toString(month)+"/"+year;

        ArrayList<Travel> selectedTravels = new ArrayList<Travel>();
        ArrayList<Travel> allTravels = new ArrayList<Travel>();
        allTravels.addAll(getAllTravels());

        for(int i = 0; i < allTravels.size(); i++){
            if(allTravels.get(i).getStartPoint().equals(startPoint) && allTravels.get(i).getEndPoint().equals(endPoint)){
                String dayOnly = Integer.toString(allTravels.get(i).getDayInt())+"/"+Integer.toString(allTravels.get(i).getMonthInt()) +"/"+ Integer.toString(allTravels.get(i).getYearInt());
                int hourOnly = allTravels.get(i).getHourInt();
                if(dayOnly.equals(day) && Math.abs(hour-hourOnly)<=1){
                    selectedTravels.add(allTravels.get(i));
                }
            }
        }


        return selectedTravels;
    }


    public static ArrayList<Travel> getSpecificTravels(String day, String startPoint, String endPoint) throws Exception{

        ArrayList<Travel> selectedTravels = new ArrayList<Travel>();
        ArrayList<Travel> allTravels = new ArrayList<Travel>();
        allTravels.addAll(getAllTravels());

        for(int i = 0; i < allTravels.size(); i++){
            if(allTravels.get(i).getStartPoint().equals(startPoint) && allTravels.get(i).getEndPoint().equals(endPoint)){
                String dayOnly = Integer.toString(allTravels.get(i).getDayInt())+"/"+Integer.toString(allTravels.get(i).getMonthInt()) +"/"+ Integer.toString(allTravels.get(i).getYearInt());
                if(dayOnly.equals(day))
                    selectedTravels.add(allTravels.get(i));
            }
        }


        return selectedTravels;
    }

    public Boolean adminExists(String email){
         for(int i = 0 ; i < admins.size(); i++){
            if(admins.get(i).getEmail().equals(email))
                return true;
        }
        return false;       
    }


    public void createAdministration(){
        String password = "123456";

        if(!adminExists("up201505791@fe.up.pt"))
            admins.add(new User("up201505791@fe.up.pt",Integer.toString(password.hashCode()),true,1));
        if(!adminExists("up201404464@fe.up.pt"))
            admins.add(new User("up201404464@fe.up.pt",Integer.toString(password.hashCode()),true,2));
        if(!adminExists("up201506440@fe.up.pt"))
            admins.add(new User("up201506440@fe.up.pt",Integer.toString(password.hashCode()),true,3));
        if(!adminExists("up201404302@fe.up.pt"))
            admins.add(new User("up201404302@fe.up.pt",Integer.toString(password.hashCode()),true,4));
    }

    public static int waitForTravel(Date date, String startPoint, String endPoint, Integer time, String email){
        
        for(int i = 0 ; i < users.size(); i++){
            if(users.get(i).getEmail().equals(email)){
                if(users.get(i).checkNotificationsWaitForTravel(date, startPoint, endPoint, time))
                    return -2;
                int b = checkTravelExist(date, startPoint, endPoint, time);

                if(b != -1)
                    return b;

                users.get(i).addNotificationsWaitForTravel(new Travel(date, startPoint, endPoint, time));
                return -1;            
                
            }
        }

        for(int i = 0 ; i < admins.size(); i++){
            if(admins.get(i).getEmail().equals(email)){
                if(admins.get(i).checkNotificationsWaitForTravel(date, startPoint, endPoint, time))
                    return -2;
                int b = checkTravelExist(date, startPoint, endPoint, time);

                if(b != -1)
                    return b;

                admins.get(i).addNotificationsWaitForTravel(new Travel(date, startPoint, endPoint, time));
                return -1;
            }
        }
        return -2;
    }        

    private static int checkTravelExist(Date date, String startPoint, String endPoint, Integer time) {
        for(int i = 0 ; i < users.size(); i++){
            int b = users.get(i).checkMyTravels(date,startPoint,endPoint, time);
            if(b!=-1)
                return b;
        }

        for(int i = 0 ; i < admins.size(); i++){
            int b = admins.get(i).checkMyTravels(date,startPoint,endPoint, time);
            
            if(b!=-1)
                return b;
        }

        return -1;
    }

    private static void checkNotificationsWaitForTravel(Date date, String startPoint, String endPoint, Integer travelID) {
        System.out.println("Server Function");
        for(int i = 0 ; i < users.size(); i++){
            if(users.get(i).checkNotificationsWaitForTravel(date, startPoint, endPoint, 0)) {
                sendNotificationCreateTravel(users.get(i).getEmail(), Messages.sendNotificationCreateTravel(users.get(i).getEmail(), Integer.toString(travelID)));
                System.out.println("Sending Notification");

            }
        }

        for(int i = 0 ; i < admins.size(); i++){
            if(admins.get(i).checkNotificationsWaitForTravel(date, startPoint, endPoint, 0)) {
                sendNotificationCreateTravel(admins.get(i).getEmail(), Messages.sendNotificationCreateTravel(admins.get(i).getEmail(), Integer.toString(travelID)));
                System.out.println("Sending Notification");
            }
        }
    }
}
