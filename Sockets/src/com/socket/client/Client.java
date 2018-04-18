package com.socket.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import com.socket.Event;
import com.socket.utils.ObjectUtils;

/**
 * The socket client that can talk back and forth with the server real time
 * 
 * @author Phillip Krall
 *
 */
public class Client {
	private int port = 0;
	private Socket socket = null;
	private DataInputStream in;
	private DataOutputStream out;

	/**
	 * Create a client
	 * 
	 * @param port
	 *            to talk to the server on
	 */
	public Client(int port) {
		this.port = port;
	}

	/**
	 * Tell the client to start talking to the server
	 * 
	 * @param callback
	 *            Function to call then you get data back from the server
	 * @return started or not
	 */
	public boolean start(Callback callback) {
		try {
			socket = new Socket(InetAddress.getByName("localhost"), port);
		} catch (Exception e) {
			return false;
		}
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Listen for messages from the server
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						String data = in.readUTF();
						Event evnt = ObjectUtils.fromSerializedObject(data);
						callback.call(evnt);
					} catch (IOException e) {
						// Stream Disconnect
						close();
						break;
					}
				}
			}
		}).start();
		return true;
	}

	/**
	 * Kill the connections with the server
	 */
	public void close() {
		try {
			socket.close();
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send a message to the server
	 * 
	 * @param evnt
	 */
	public void emit(Event evnt) {
		try {
			out.writeUTF(ObjectUtils.toSerializedObject(evnt));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
