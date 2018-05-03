import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.io.*;


public class Login {

    private String email;
    private String password;

    public Login(String email, String password){
        this.email= email;
        this.password=password;
    }

   
}