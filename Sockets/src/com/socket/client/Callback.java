package com.socket.client;

import com.socket.Event;

/**
 * Functional interface to pass callbacks around
 * 
 * @author Phillip Krall
 *
 */
public interface Callback {
	public void call(Event evnt);
}
