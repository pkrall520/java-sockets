package com.socket.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

import com.socket.Event;

/**
 * Utilities around serializing objects
 * 
 * @author Phillip Krall
 *
 */
public class ObjectUtils {

	/**
	 * Change an event to a serialized object
	 * 
	 * @param evnt
	 * @return
	 */
	public static String toSerializedObject(Event evnt) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(evnt);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Base64.getEncoder().encodeToString(baos.toByteArray());
	}

	/**
	 * Change a String ( previously an event ) to an object
	 * 
	 * @param obj
	 * @return The decoded event
	 */
	public static Event fromSerializedObject(String obj) {
		ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(obj));
		ObjectInput in = null;
		try {
			in = new ObjectInputStream(bis);
			Event evnt = (Event) in.readObject();
			return evnt;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
