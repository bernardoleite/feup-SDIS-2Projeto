import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.io.IOException;
import java.net.DatagramSocket;
import java.io.*;
import java.util.*;


public class NotificationJoinChannel implements Runnable{

    private InetAddress address;
    private Integer port;
    private static MulticastSocket receiverSocket;
	private String message, email;
    private static ExecutorService exec;


	public NotificationJoinChannel(String address, int port, String email, String message) throws UnknownHostException{

		this.message = message;
		this.email = email;

        exec = Executors.newFixedThreadPool(1000);

		try {
			this.address = InetAddress.getByName(address);
			this.port = port;

		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public NotificationJoinChannel(String address, int port) throws UnknownHostException{

        exec = Executors.newFixedThreadPool(1000);

		try {
			this.address = InetAddress.getByName(address);
			this.port = port;

		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
    }

    public void sendMessage(byte[] toSendContent) throws UnknownHostException, InterruptedException{
		System.out.println("Sending ACK: " + new String(toSendContent));
		try(DatagramSocket senderSocket = new DatagramSocket()){
			//create a packet that will contain the data
			DatagramPacket msgPacket = new DatagramPacket(toSendContent ,toSendContent.length,address,port);
			senderSocket.send(msgPacket);

		} catch(IOException ex){
			ex.printStackTrace();
		}
	}

	public void receiveMessage(String email) throws UnknownHostException, InterruptedException{

		byte[] buf = new byte[65000];
		byte[] received = new byte[65000];
		openSocket();
		String receivedEmail = "";
			try{
				do {
				DatagramPacket msgReceiverPacket = new DatagramPacket(buf,buf.length);
				receiverSocket.receive(msgReceiverPacket);
				received = Arrays.copyOfRange(buf, 0, buf.length-1);
				String[] receivedStr = new String(received).split(" ");
				receivedEmail = receivedStr[1].trim();
				} while(!email.equals(receivedEmail));
				System.out.println(new String(received));
				sendMessage(Messages.sendACKJoinTravel(email).getBytes());
				System.out.println("ACK Sended");

			}catch(IOException ex){
				ex.printStackTrace();
			}
		
	}

    public void openSocket(){
		try{
			receiverSocket = new MulticastSocket(port);

			receiverSocket.joinGroup(address);
		}catch(IOException e){
			e.printStackTrace();
		}

	}

	@Override
	public void run(){

		byte[] buf = new byte[65000];
		openSocket();

		try{
			while(true){

				sendMessage(message.getBytes());

				
				DatagramPacket msgReceiverPacket = new DatagramPacket(buf,buf.length);
				receiverSocket.receive(msgReceiverPacket);

				byte[] received = Arrays.copyOfRange(buf, 0, buf.length-1);
        		System.out.println(new String(received));
				String receivedStr = new String(received);
				String[] splitStr = receivedStr.split(" ");
				String receivedEmail = splitStr[1].trim();
				String receivedAction = splitStr[0];
				System.out.println("ACK: " + receivedStr);
				if(receivedAction.equals("ListenedJoinTravel") && receivedEmail.equals(email)) {
					System.out.println("ACK Received!!!");
					return;
				}

				buf = new byte[65000];
				received = new byte[65000];

				Thread.sleep(30 * 1000);
			}

		}catch(Exception ex){
			ex.printStackTrace();
		}


	}

}
