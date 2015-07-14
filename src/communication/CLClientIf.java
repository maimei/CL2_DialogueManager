package communication;

import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.*;

/**
 * 
 * Copyright Iam Dempsey, Zoey Prigge University of Bremen, 2015
 * 
 * @version 2015-07-14
 * adapted to new server concept
 */
public class CLClientIf {

	// standard post port
	public static int PORT_POST = 50010;

	// all ports that can be used
	public static int PORT_ASR = 50015;
	public static int PORT_NLU = 50016;
	public static int PORT_DM = 50017;
	public static int PORT_NLG = 50018;
	public static int PORT_SPS = 50019;

	private int destinationPort;
	private int postPort;
	private int getPort;
	private String ipAddress;

	public CLClientIf(String ipaddress, int postport, int port) {
		destinationPort = port + 1;
		postPort = postport;
		ipAddress = ipaddress;
		getPort = port;

	}

	public void write(String string) {

		try {
			boolean quit = false;
			while (!quit) {
				Socket CLPostSocket = new Socket(ipAddress, postPort);
				PrintStream ps = getPrintStream(CLPostSocket);
						ps.println(
								"POST " + destinationPort + " "
										+ (string.length() + 1) + " ");
						//ps.flush();
						ps.println(string);

				String recvd = getUnfilteredResponse(CLPostSocket);
				if (recvd.substring(0, 2).equals("OK")) {
					System.out.println(string+ "\n");
					ps.flush();
					System.out.println(getUnfilteredResponse(CLPostSocket).length());
					if (getUnfilteredResponse(CLPostSocket).substring(0, 4)
							.equals("DONE"))
						quit = true;
				} else if (!recvd.substring(0, 4).equals("BUSY")) {
					System.out.println("HIER : " + recvd);
					throw new RuntimeException(
							"There is an error on the server side");
				}
				CLPostSocket.close();
			}
		} catch (IOException e) {
			throw new RuntimeException("Socket could not be openend!");
		}

	}

	public String response() {
		try {
			Socket CLCommunicationSocket = new Socket(ipAddress, getPort);
			getPrintStream(CLCommunicationSocket).println("GET " + getPort);
			String retVal;
			String recvd = getUnfilteredResponse(CLCommunicationSocket);
			if (recvd.substring(0, 8).equals("RECIEVE ")) {
				getPrintStream(CLCommunicationSocket).println("OK");
				retVal = getUnfilteredResponse(CLCommunicationSocket);
				getPrintStream(CLCommunicationSocket).println("DONE");
			} else {
				throw new RuntimeException("Unknown Operation");
			}
			CLCommunicationSocket.close();
			return retVal;
		} catch (IOException e) {
			throw new RuntimeException("Socket could not be openend!");
		}
	}

	/*
	 * // This won't run so easily anymore as a lot of meta info has been added
	 * to the communication public void setAsInOut() {
	 * System.setOut(getPrintStream()); System.setIn(getInputStream()); }
	 */

	/*public PrintStream getPrintStream(Socket socket1) {
		try {
			return new PrintStream(socket1.getOutputStream());
		} catch (IOException e) {
			throw new RuntimeException("Output Stream IO Exception!");
		}
	}*/
	
	public PrintStream getPrintStream(Socket socket1) {
		try {
			return new PrintStream(socket1.getOutputStream(), true);
		} catch (IOException e) {
			throw new RuntimeException("Output Stream IO Exception!");
		}
	}

	public InputStream getInputStream(Socket socket1) {
		try {
			return socket1.getInputStream();
		} catch (IOException e) {
			throw new RuntimeException("Input Stream IO Exception!");
		}
	}

	public String getUnfilteredResponse(Socket socket1) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					getInputStream(socket1)));
			String rl = null;
			while (rl == null) {
				rl = br.readLine();
			}
			return rl;
		} catch (IOException e) {
			throw new RuntimeException(
					"Input Stream IO Exception...Reading Buffered Reader!");
		}
	}

}
