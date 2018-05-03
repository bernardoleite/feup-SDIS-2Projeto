import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.io.*;


public class MessageTreatment {

	private byte[] messageToBeTreated;

	public MessageTreatment(byte[] messageToBeTreated){
      
        this.messageToBeTreated = messageToBeTreated;
        treatMessage();
    }
    
    private void treatMessage() {

        String answer = new String(this.messageToBeTreated, 0, this.messageToBeTreated.length);
        String[] splitMessage = answer.split(" ");
        String action= splitMessage[0]; 

        switch(action){
            case "Register":
                System.out.println("New Registration");
                Register register = new Register(splitMessage[1],splitMessage[2]);
            case "Login":
                System.out.println("Login Attempt");
                Login login = new Login(splitMessage[1],splitMessage[2]);
        }
    }
}