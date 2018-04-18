package com.socket;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;

/**
 * Client for Test1_Server
 * 
 * @author Phillip Krall
 *
 */
public class Test1_Client {
	@Test
	public void client() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		EventManager.start();
		EventManager.on("test1", evnt -> {
			System.out.println("Made it here");
			assertEquals(evnt.getData(), "Here is some data");
			latch.countDown();
		});
		EventManager.emit("test1", "Here is some data");
		latch.await();
	}
}
