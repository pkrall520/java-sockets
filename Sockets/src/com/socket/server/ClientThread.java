package com.socket.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.socket.Event;
import com.socket.client.Callback;
import com.socket.utils.ObjectUtils;

/**
 * A thread that holds the connection between the server and client
 * 
 * @author Phillip Krall
 *
 */
public class ClientThread extends Thread {
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private Callback callback;

	/**
	 * Create a new client thread
	 * @param socket
	 * @param callback What to call when you have data from the server
	 */
	public ClientThread(Socket socket, Callback callback) {
		this.socket = socket;
		this.callback = callback;
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			// Get data from the client
			try {
				String data = in.readUTF();
				Event evnt = ObjectUtils.fromSerializedObject(data);
				callback.call(evnt); // Tell the server that you got some data back
			} catch (IOException e) {
				// Client has died
				close();
				break;
			}
		}
	}

	/**
	 * Kill the thread
	 */
	public void close() {
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send something back to the client
	 * @param evnt
	 * @return
	 */
	public boolean emit(Event evnt) {
		if (!socket.isConnected()) {
			close();
			return false;
		}
		try {
			out.writeUTF(ObjectUtils.toSerializedObject(evnt));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return true;
	}
}
