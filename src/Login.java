import java.util.ArrayList;
import java.io.*;

public class Login implements Serializable{

    private String email="";
    private String password="";

    public Login(String email, String password){
        this.email= email;
        this.password=password;
    }

   
    public boolean existUser() {
        System.out.println("Login Attempt");
        return Server.existUser(email,password);
    }
}