import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.io.*;


public class MessageTreatment {

	private byte[] messageToBeTreated;
    private byte[] sendMessage;
    private boolean isToSendMessage = false;
	public MessageTreatment(byte[] messageToBeTreated){

        this.messageToBeTreated = messageToBeTreated;
        treatMessage();
    }

    private void treatMessage() {

        String answer = new String(this.messageToBeTreated, 0, this.messageToBeTreated.length);
        String[] splitMessage = answer.split(" ");
        String action= splitMessage[0];
        String email = splitMessage[1];
        String password = splitMessage[2].trim();

        switch(action){
            case "Register":
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
							break;
						case "Delete":
							break;
        }
    }

    public byte[] getSendMessage() {
        return sendMessage;
    }

    public boolean getIsToSendMessage() {
        return isToSendMessage;
    }
}
