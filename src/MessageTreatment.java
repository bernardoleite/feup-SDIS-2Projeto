import java.util.ArrayList;
import java.io.*;
import java.util.Date;
import java.text.DateFormat;
import java.util.Locale;

import javax.lang.model.util.ElementScanner6;

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
        String travelID="";
        String emailPassenger="";

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
                int isAdmin = 0;

                if (Server.checkAdmin(email)){
                    isAdmin = 1;
                }

                Login login = new Login(email,password);
                if(login.existUser()){
                    sendMessage = Messages.successLogin(email, isAdmin).getBytes();
                }
                else{
                    sendMessage = Messages.unsuccessLogin(email).getBytes();
                }
                break;
            case "Create":

                String creator = splitMessage[1];
                String dateform = splitMessage[2] + " " + splitMessage[3];
                String startPoint = splitMessage[4];
                String endPoint = splitMessage[5];
                String nrSeats = splitMessage[6].trim();
            
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
                Date date = format.parse(dateform);
                System.out.println(date); // Sat Jan 02 00:00:00 GMT 2010
              
                Server.createNewTravel(date, startPoint, endPoint, Integer.parseInt(nrSeats), creator);
              
                isToSendMessage = true;
                sendMessage = Messages.successCreateTravel(creator).getBytes();
                break;
            case "Delete":

                creator = splitMessage[1];
                String travelIdstring = splitMessage[3].trim();
                int travelIdentifier = Integer.parseInt(travelIdstring);
              
                Server.deleteTravel(creator, travelIdentifier);
              
                isToSendMessage = true;
                sendMessage = Messages.successDeleteTravel(travelIdstring, creator).getBytes();
                break;
            case "Join":
                email = splitMessage[1];
                travelID = splitMessage[4].trim();
                isToSendMessage = true;
                if(Server.joinTravel(travelID, email))
                    sendMessage = Messages.successJoinTravel(email).getBytes();
                else 
                    sendMessage = Messages.unsuccessJoinTravel(email).getBytes();
                break;
            case "Leave":
                email = splitMessage[1];
                travelID = splitMessage[4].trim();
                isToSendMessage = true;
                if(Server.leaveTravel(travelID, email))
                    sendMessage = Messages.successLeaveTravel(email).getBytes();
                else 
                    sendMessage = Messages.unsuccessLeaveTravel(email).getBytes();
                break;
            case "Passengers":
                email = splitMessage[1];
                travelID = splitMessage[4].trim();
                isToSendMessage = true;
                if(Server.checkUserTravel(email, Integer.parseInt(travelID)))
                    sendMessage = Messages.sendPassengers(email, Server.getTravelPassengers(email, Integer.parseInt(travelID))).getBytes();
                else
                    sendMessage = Messages.sendFailedPassengers(email).getBytes();
                break;
            case "PassengersRequest":
                email = splitMessage[1];
                travelID = splitMessage[4].trim();
                isToSendMessage = true;
                if(Server.checkUserTravel(email, Integer.parseInt(travelID)))
                    sendMessage = Messages.sendPassengersRequest(email, Server.getTravelPassengersRequest(email, Integer.parseInt(travelID))).getBytes();
                else
                    sendMessage = Messages.sendFailedPassengersRequest(email).getBytes();
                break;
                
            case "AddPassenger":
                email = splitMessage[1];
                travelID = splitMessage[4].trim();
                emailPassenger = splitMessage[6].trim();
                isToSendMessage = true;
                if(Server.checkUserTravel(email, Integer.parseInt(travelID)) && Server.addPassenger(email, Integer.parseInt(travelID), emailPassenger))
                    sendMessage = Messages.successAddPassenger(email).getBytes();
                else
                    sendMessage = Messages.unsuccessAddPassenger(email).getBytes();
                break;
                
            case "RemovePassenger":
                email = splitMessage[1];
                travelID = splitMessage[4].trim();
                emailPassenger = splitMessage[6].trim();
                isToSendMessage = true;
                if(Server.checkUserTravel(email, Integer.parseInt(travelID)) && Server.removePassenger(email, Integer.parseInt(travelID), emailPassenger))
                    sendMessage = Messages.successRemovePassenger(email).getBytes();
                else
                    sendMessage = Messages.unsuccessRemovePassenger(email).getBytes();
            break;

                            
            case "ListJoinTravels":
                email = splitMessage[1].trim();
                isToSendMessage = true;
                sendMessage = Messages.sendJoinTravels(email, Server.getUserJoinTravels(email)).getBytes();
            break;

            case "ListRequestTravels":
                email = splitMessage[1].trim();
                isToSendMessage = true;
                sendMessage = Messages.sendRequestTravels(email, Server.getUserRequestTravels(email)).getBytes();
            break;
            case "ListMyTravels":
                email = splitMessage[1].trim();
                isToSendMessage = true;
                sendMessage = Messages.sendMyTravels(email, Server.getUserMyTravels(email)).getBytes();
            break;
            case "ListAllTravels":
                email = splitMessage[1].trim();
                isToSendMessage = true;
                sendMessage = Messages.sendAllTravels(email, Server.getAllTravels()).getBytes();
            break;
            case "SearchComplete":

                email = splitMessage[1].trim();
                dateform = splitMessage[2] + " " + splitMessage[3];
                startPoint = splitMessage[4];
                endPoint = splitMessage[5].trim();
                format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
                date = format.parse(dateform);
                isToSendMessage = true;
                sendMessage = Messages.sendSpecificTravels(email, Server.getSpecificTravels(date, startPoint, endPoint)).getBytes();
            break;
            case "SearchPartial":

                email = splitMessage[1].trim();
                String day = splitMessage[2];
                startPoint = splitMessage[3];
                endPoint = splitMessage[4].trim();
                System.out.println("Entrei aquiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
                //format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
                //date = format.parse(dateform);
                isToSendMessage = true;
                sendMessage = Messages.sendSpecificTravels(email, Server.getSpecificTravels(day, startPoint, endPoint)).getBytes();
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
