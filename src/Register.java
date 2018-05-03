import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.io.*;


public class Register {

    private String email;
    private String password;

    public Register(String email, String password){
        this.email= email;
        this.password=password;
    }

   
}
