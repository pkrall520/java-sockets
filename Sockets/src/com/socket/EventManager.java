package com.socket;

import java.util.HashMap;
import java.util.Map;

import com.socket.client.Callback;
import com.socket.client.Client;
import com.socket.error.EventManagerNotStarted;
import com.socket.server.Server;

/**
 * A wrapper to host all the socket functionality
 * 
 * @author Phillip Krall
 *
 */
public class EventManager {
	private static boolean started = false;
	private static boolean hasServer = false;
	private static final int PORT = 500;
	private static Server server = null;
	private static Client client = null;
	private static Map<String, Callback> onEvents = new HashMap<String, Callback>();

	/**
	 * Create the client to talk with the server
	 */
	public static void start() {
		started = true;
		client = new Client(PORT);
		client.start(evnt -> {
			if (onEvents.containsKey(evnt.getName())) {
				onEvents.get(evnt.getName()).call(evnt);
			}
		});
	}

	/**
	 * If true Create the server and the client.
	 * 
	 * @param startServer
	 */
	public static void start(boolean startServer) {
		if (startServer) {
			hasServer = true;
			server = new Server(PORT);
			server.start();
		}
		start();
	}

	/**
	 * Bind and event to the client
	 * 
	 * @param evntName
	 * @param callback
	 */
	public static void on(String evntName, Callback callback) {
		onEvents.put(evntName, callback);
	}

	/**
	 * Kill the server and client
	 */
	public static void close() {
		if (hasServer) {
			server.stop();
		}
		client.close();
	}

	/**
	 * Send an event to all clients
	 * 
	 * @param event
	 * @param data
	 */
	public static void emit(String event, Object data) {
		Event evnt = new Event();
		evnt.setName(event);
		evnt.setData(data);
		emit(evnt);
	}

	/**
	 * Send an event to all the clients
	 * 
	 * @param evnt
	 */
	public static void emit(Event evnt) {
		if (!started) {
			throw new EventManagerNotStarted();
		}
		client.emit(evnt);
	}
}
