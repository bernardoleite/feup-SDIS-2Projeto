import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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


public class MultiCastChannel implements Runnable{

    private static InetAddress address;
    private static Integer port;
    private static MulticastSocket receiverSocket;

    private static ExecutorService exec;

 
	public MultiCastChannel(String address, int port) throws UnknownHostException{

        exec = Executors.newFixedThreadPool(1000);
			
		try {
			this.address = InetAddress.getByName(address);
			this.port = port;

		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
    }

    public static void openSocket(){
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

				DatagramPacket msgReceiverPacket = new DatagramPacket(buf,buf.length);
				receiverSocket.receive(msgReceiverPacket);


				byte[] toSend = Arrays.copyOfRange(buf, 0, buf.length-1);
				MessageTreatment message = new MessageTreatment(toSend);
			
			}

		}catch(IOException ex){
			ex.printStackTrace();
		}
		

	}

}