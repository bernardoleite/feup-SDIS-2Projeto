import java.util.ArrayList;
import java.io.*;


public class Messages {

    private String action;
    private Integer id;

    public Messages(String action, Integer id){
        this.action=action;
        this.id=id;
    }

    public static String userRegister(String email, String password){
        return "Register " + email + ' ' + password;
    }

    public static String userLogin(String email, String password){
        return "Login " + email + ' ' + password;
    }

    public static String successRegister(String email){
        return "Success "+ email + " registration.";
    }

    public static String unsuccessRegister(String email){
        return "User "+ email + " already exists.";
    }
    
    public static String successLogin(String email){
        return "Success "+ email  + " login.";
    }

    public static String unsuccessLogin(String email){
        return "Failed "+ email + " login.";
    }

    public static String createTravel(String date, String startPoint, String endPoint, String numberOfSeats, String creator){
        return "Create" + " " + creator + " " + date + " " + startPoint + " " + endPoint + " " + numberOfSeats;
    }
    
    public static String deleteTravel(String travelIdentifier, String creator){
        return "Delete" + " " + creator + " " + "travel" + " " + travelIdentifier;
    }

    public static String successDeleteTravel(String travelIdentifier, String creator){
        return "Success" + " " + creator + " " + "delete travel" + " " + travelIdentifier;
    }

    public static String successCreateTravel(String creator){
        return "Success" +  " " + creator + " " + "travel created" ;
    }

    public static String joinTravel(String personIdentifier,String travelIdentifier){
        return "Join " + personIdentifier+  " Travel number " + travelIdentifier;
    }

    public static String leaveTravel(String personIdentifier,String travelIdentifier){
        return "Leave " + personIdentifier+  " Travel number " + travelIdentifier;
    }

    public static String successJoinTravel(String email){
        return "Success "+ email  + " join travel.";
    }

    public static String unsuccessJoinTravel(String email){
        return "Failed "+ email + " join travel.";
    }

    public static String successLeaveTravel(String email){
        return "Success "+ email  + " leave travel.";
    }

    public static String unsuccessLeaveTravel(String email){
        return "Failed "+ email + " leave travel.";
    }

    public static String listPassengers(String email,String travelIdentifier){
        return "Passengers "+ email + " Travel number " + travelIdentifier;
    }

    public static String listPassengersRequest(String email,String travelIdentifier){
        return "PassengersRequest "+ email + " Travel number " + travelIdentifier;
    }

    public static String sendPassengers(String email, ArrayList<User> passengers){
        String string = "SendPassengers "+ email + " Travel number\n";
        String aux;

        for(int i=0; i < passengers.size();i++){
            aux= passengers.get(i).getEmail() + "\n";
            string= string+aux;
        }

        return string;
    }

    public static String sendPassengersRequest(String email, ArrayList<User> passengers){
        String string = "SendPassengersRequest "+ email + " Travel number\n";
        String aux;

        for(int i=0; i < passengers.size();i++){
            aux= " " + passengers.get(i).getEmail() + "\n";
            string= string+aux;
        }

        return string;
    }

    public static String sendFailedPassengers(String email){
        return "FailedPassengers "+ email;
    }

    public static String sendFailedPassengersRequest(String email){
        return "FailedPassengersRequest "+ email;
    }

    public static String addPassenger(String email, String emailPassenger, String travelID){
        return "AddPassenger "+ email + " travel number " + travelID + " passenger " + emailPassenger;
    }

    public static String removePassenger(String email, String emailPassenger, String travelID){
        return "RemovePassenger "+ email + " travel number " + travelID + " passenger " + emailPassenger;
    }

    public static String successAddPassenger(String email){
        return "SuccessAddPassenger "+ email;
    }

    public static String unsuccessAddPassenger(String email){
        return "UnsuccessAddPassenger "+ email;
    }

    public static String successRemovePassenger(String email){
        return "SuccessRemovePassenger "+ email;
    }

    public static String unsuccessRemovePassenger(String email){
        return "UnsuccessRemovePassenger "+ email;
    }

    public static String listJoinTravels(String email){
        return "ListJoinTravels "+ email;
    }

    public static String searchCompleteTravel(String date, String startPoint, String endPoint, String creator){
        return "Search Complete " + creator + " " + date + " " + startPoint + " " + endPoint;
    }

    public static String searchPartialTravel(String day, String startPoint, String endPoint, String creator){
        return "Search Partial " + creator + " " + day + " " + startPoint + " " + endPoint;
    }

    //public static String successSearchTravel(String travelIdentifier, String creator){
      //  return "Success" + " " + creator + " " + "search travel" + " " + travelIdentifier;
    //}

    public static String sendJoinTravels(String email, ArrayList<Travel> travels){
        String string = "SendJoinTravels "+ email + " " + "\n";
        String aux;

        for(int i=0; i < travels.size();i++){
            aux= " " + travels.get(i).getID() + " " + travels.get(i).getDate() + " from " + travels.get(i).getStartPoint() + " to " + travels.get(i).getEndPoint() + "\n";
            string= string+aux;
        }
        
        return string;
    }


    public static String listRequestTravels(String email){
        return "ListRequestTravels "+ email;
    }

    public static String sendRequestTravels(String email, ArrayList<Travel> travels){
        String string = "SendJoinTravels "+ email + " " + "\n";
        String aux;

        for(int i=0; i < travels.size();i++){
            aux= " " + travels.get(i).getID() + " " + travels.get(i).getDate() + " from " + travels.get(i).getStartPoint() + " to " + travels.get(i).getEndPoint() + "\n";
            string= string+aux;
        }
        
        return string;
    }

    public static String listMyTravels(String email){
        return "ListMyTravels "+ email;
    }

    public static String sendMyTravels(String email, ArrayList<Travel> travels){
        String string = "SendMyTravels "+ email + " " + "\n";
        String aux;

        for(int i=0; i < travels.size();i++){
            aux= " " + travels.get(i).getID() + " " + travels.get(i).getDate() + " from " + travels.get(i).getStartPoint() + " to " + travels.get(i).getEndPoint() + "\n";
            string= string+aux;
        }
        
        return string;
    }


    public static String sendNotificationJoinTravel(String emailCreator, String travelID, String email){
        return "NotificationJoinTravel "+ emailCreator + " travel number " + travelID +" passenger " + email;
    }

    public static String sendACKJoinTravel(String email){
        return "ListenedJoinTravel "+ email;
    }
}