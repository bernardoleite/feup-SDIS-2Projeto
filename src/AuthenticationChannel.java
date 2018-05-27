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

import java.net.Socket;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.Reader;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;


public class AuthenticationChannel implements Runnable{

    private static ExecutorService exec;

    BufferedReader socketBufferedReader = null;
    PrintWriter printWriter = null;
    Socket socket = null;


	public AuthenticationChannel() throws UnknownHostException{

        exec = Executors.newFixedThreadPool(1000);

    }

    public void sendMessage(byte[] toSendContent) throws Exception{

		try{
			System.setProperty("javax.net.ssl.trustStore", "za.store");
			InetAddress addr = InetAddress.getByName("localhost");
			socket = ((SSLSocketFactory)SSLSocketFactory.getDefault()).createSocket(addr, 4444);
			socketBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			printWriter = new PrintWriter(socket.getOutputStream(), true);

			String str = new String(toSendContent, StandardCharsets.UTF_8);
			printWriter.println(str);

		}
		catch(Exception e){
			e.printStackTrace();
		}

	}

	public byte[] receiveMessage(String email) throws Exception{

		String receivedEmail = "";
		String received = "";
		try{

			do {
			received = socketBufferedReader.readLine();
			String[] receivedStr = received.split(" ");
			receivedEmail = receivedStr[1].trim();
			} while(!email.equals(receivedEmail));

		}catch(Exception ex){
			ex.printStackTrace();
		}


		finally {
		    printWriter.close(); 
		    socketBufferedReader.close();
		    socket.close();
		}
		return received.getBytes();
	}

	@Override
	public void run(){}

}
