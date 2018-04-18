package com.socket;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;

/**
 * Client for Test2
 * 
 * @author Phillip Krall
 *
 */
public class Test2_Client2 {
	@Test
	public void client() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		EventManager.start();
		EventManager.on("test2", evnt -> {
			System.out.println("Client2 Test2");
			assertEquals(evnt.getData(), "Here is some data");
			latch.countDown();
		});
		EventManager.emit("test2", "Here is some data");
		latch.await();
	}
}
