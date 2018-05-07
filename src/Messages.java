import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
        return "Success "+ email + " login.";
    }

    public static String unsuccessLogin(String email){
        return "Failed "+ email + " login.";
    }

    public static String createTravel(String date, String startPoint, String endPoint, String numberOfSeats, String creator){
        return "Create" + " " + date + " " + startPoint + " " + endPoint + " " + numberOfSeats + " " + creator;
    }

    public static String successCreateTravel(String travelIdentifier){
        return "Created" +  " " + travelIdentifier;
    }


}