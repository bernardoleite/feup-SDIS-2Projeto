import java.util.ArrayList;
import java.io.*;


public class Register implements Serializable{

    private String email="";
    private String password="";


    public Register(String email, String password){
        this.email= email;
        this.password=password;
    }

    public boolean registerUser() {
        System.out.println("Registration Attempt");
        if(!Server.alreadyAnUser(email)) {
            return Server.register(this);
        }

        return false;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }
}
