package com.socket.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Socket Server which hide in the background collecting clients and
 * allowing them to talk with each other
 * 
 * @author Phillip Krall
 *
 */
public class Server {
	private int port = 0;
	private boolean acceptClients;
	private List<ClientThread> clients = new ArrayList<ClientThread>();

	/**
	 * Create a server
	 * 
	 * @param port
	 *            to run server on
	 */
	public Server(int port) {
		this.port = port;
	}

	/**
	 * Tell the server that you are ready to talk to clients
	 */
	public void start() {
		acceptClients = true;
		try {
			@SuppressWarnings("resource")
			final ServerSocket server = new ServerSocket(port, 0, InetAddress.getByName("localhost"));
			/**
			 * Keep accepting clients until the user has called stop
			 */
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (acceptClients) {
						try {
							Socket clientSocket = server.accept();
							ClientThread client = new ClientThread(clientSocket, evnt -> {
								/*
								 * When a client gets data back, then loop through the clients and tell them all
								 */
								for (ClientThread c : clients) {
									c.emit(evnt);
								}
							});
							clients.add(client);
							client.start();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

			}).start();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
			stop();
		} catch (IOException e1) {
			e1.printStackTrace();
			stop();
		}
	}

	/**
	 * Tell the server to stop taking clients and to shut down.
	 */
	@SuppressWarnings("resource")
	public void stop() {
		// Tell the server to not accept any more clients
		acceptClients = false;
		// Connect to the server as a client to all the server.accpet to be processed
		try {
			new Socket("localhost", port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
