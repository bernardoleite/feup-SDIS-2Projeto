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

}