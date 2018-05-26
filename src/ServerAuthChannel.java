import java.util.*;
import java.net.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import java.net.Socket;
import java.net.ServerSocket;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.Reader;
import java.io.BufferedReader;


public class ServerAuthChannel implements Runnable{


	public ServerAuthChannel() throws Exception{
    }

	@Override
	public void run() {

        try{
        //Special Thread for SSL Socket
        System.setProperty("javax.net.ssl.keyStore", "za.store");
        System.setProperty("javax.net.ssl.keyStorePassword", "password");
        ServerSocket authSocket = ((SSLServerSocketFactory)SSLServerSocketFactory.getDefault()).createServerSocket(4444);
        System.out.println("Socket up & ready for connections (SSL)...");
        while(true){
            new AuthThread(authSocket.accept()).start();
        }

        }
        catch(Exception e){
            e.printStackTrace();
        }

	}

}
