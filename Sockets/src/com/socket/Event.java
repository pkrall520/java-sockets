package com.socket;

import java.io.Serializable;

/**
 * Event PoJo used to talk with the client and server sockets
 * 
 * @author Phillip Krall
 *
 */
public class Event implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private Object data;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Event [name=" + name + ", data=" + data + "]";
	}
}
