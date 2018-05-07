import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.io.*;
import java.util.Date;
import java.text.DateFormat;
import java.util.Locale;
import java.text.SimpleDateFormat;

public class MessageTreatment {

	private byte[] messageToBeTreated;
    private byte[] sendMessage;
    private boolean isToSendMessage = false;
	public MessageTreatment(byte[] messageToBeTreated){

        this.messageToBeTreated = messageToBeTreated;
        treatMessage();
    }

    private void treatMessage()  {

        try{

        String email, password;
        String answer = new String(this.messageToBeTreated, 0, this.messageToBeTreated.length);
        String[] splitMessage = answer.split(" ");
        String action= splitMessage[0];

        switch(action){
            case "Register":
                email = splitMessage[1];
                password = splitMessage[2].trim();
                Register register = new Register(email,password);
                isToSendMessage = true;
                if(register.registerUser()){
                    sendMessage = Messages.successRegister(email).getBytes();
                }
                else{
                    sendMessage = Messages.unsuccessRegister(email).getBytes();
                }
                break;
            case "Login":
                email = splitMessage[1];
                password = splitMessage[2].trim();
                isToSendMessage = true;

                Login login = new Login(email,password);
                if(login.existUser()){
                    sendMessage = Messages.successLogin(email).getBytes();
                }
                else{
                    sendMessage = Messages.unsuccessLogin(email).getBytes();
                }
                break;
            case "Create":

                String creator = splitMessage[1].trim();
                String dateform = splitMessage[2] + " " + splitMessage[3];
                String startPoint = splitMessage[4];
                String endPoint = splitMessage[5];
                String nrSeats = splitMessage[6].trim();
            
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
                Date date = format.parse(dateform);
                System.out.println(date); // Sat Jan 02 00:00:00 GMT 2010
                
          
                for(int i = 0 ; i < Server.getUsers().size(); i++){

                    if(Server.getUsers().get(i).getEmail().equals(creator)){
                        Server.createNewTravel(date, startPoint, endPoint, Integer.parseInt(nrSeats), Server.getUsers().get(i) );
                    }
                }

                String code = creator;
                isToSendMessage = true;
                sendMessage = Messages.successCreateTravel(code).getBytes();
                break;
        }

    }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public byte[] getSendMessage() {
        return sendMessage;
    }

    public boolean getIsToSendMessage() {
        return isToSendMessage;
    }
}
