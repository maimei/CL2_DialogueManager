package communication;

import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * 
 * Copyright Iam Dempsey, Zoey Prigge
 * University of Bremen, 2015
 * 
 * @version 2015-06-30
 *
 */
public class CLClientIf {

	    
	    private Socket CLCommunicationSocket;
	    
	    public static int PORT_ASR = 50015;
	    public static int PORT_NLU = 50016;
	    public static int PORT_DM = 50017;
	    public static int PORT_NLG = 50018;
	    public static int PORT_SPS = 50019;
	    
	    public CLClientIf(String ipaddress, int port) 
	    {
	    	try {
	    		CLCommunicationSocket = new Socket(ipaddress, port);	    
	    	}
	    	catch (IOException e) {
	    		throw new RuntimeException("Socket could not be openend!");
	    	}
	    	

	    }
	    
	    public void setAsInOut() {
			System.setOut(getPrintStream());
			System.setIn(getInputStream());
	    }
	    
	    public void finalize() {
	    	try {
	    		CLCommunicationSocket.close();
	    	}
	    	catch (IOException e) {
	    		throw new RuntimeException("Socket could not be closed!");
	    	}
	    }
	    
	    private InputStream getInputStream() {
	    	try {
	    		return CLCommunicationSocket.getInputStream();
		    }
	    	catch (IOException e) {
	    		throw new RuntimeException("Input Stream IO Exception!");
	    	}
	    }
	    
	    public PrintStream getPrintStream(){
	    	try {
		    	return new PrintStream(CLCommunicationSocket.getOutputStream());
		    }
	    	catch (IOException e) {
	    		throw new RuntimeException("Output Stream IO Exception!");
	    	}
	    }
	    
	    
	    
	}
