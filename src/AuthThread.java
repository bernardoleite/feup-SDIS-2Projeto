import java.net.Socket;
import java.net.ServerSocket;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.*;
import java.util.Arrays;

import java.net.Socket;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.Reader;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;

public class AuthThread extends Thread {

		Socket socket;
		PrintWriter printWriter;
		BufferedReader buffredReader;

		AuthThread(Socket socket){
			this.socket = socket;
		}


    public void sendMessage(byte[] toSendContent) throws Exception{

		try{

			String str = new String(toSendContent, StandardCharsets.UTF_8);
			printWriter.println(str);

		}
		catch(Exception e){
			e.printStackTrace();
		}

	}

		public void run(){

			try{

				printWriter = new PrintWriter(socket.getOutputStream(), true);

				buffredReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				byte [] received = buffredReader.readLine().getBytes();

				MessageTreatment message = new MessageTreatment(received);

				Thread.sleep(100);

				if(message.getIsToSendMessage()){
					sendMessage(message.getSendMessage());
				}
		
			} catch (Exception e){
				e.printStackTrace();
			}

		}

}