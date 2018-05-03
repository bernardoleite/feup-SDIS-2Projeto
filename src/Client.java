import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.io.*;


public class Client {


    public Client(){}

    public static void main(String[] args){

        if(args.length < 3)
            return;

        String action = args[0];
        String email = args[1];
        String password = args[2];
        String message="";

        //Bernardo faz verificação email agora

        if(action.equals("Register")){
            message =  Messages.userRegister(email,password);
        }

        else if(action.equals("Login")){
            message=  Messages.userLogin(email,password);
        }

        System.out.println(message);
    }
}