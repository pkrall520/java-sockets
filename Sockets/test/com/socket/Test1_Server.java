package com.socket;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;

public class Test1_Server {
	/**
	 * Run this first and then run the test1_client next
	 * @throws InterruptedException 
	 */
	@Test
	public void server() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		EventManager.start(true);
		EventManager.on("test1", evnt -> {
			System.out.println("Server test1");
			assertEquals(true, true);
			latch.countDown();
		});
		latch.await();
	}
}
