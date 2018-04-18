package com.socket;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;

public class Test2_Client1 {
	@Test
	public void client() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		EventManager.start();
		EventManager.on("test2", evnt -> {
			System.out.println("Client1 Test2");
			assertEquals(evnt.getData(), "Here is some data");
			latch.countDown();
		});
		latch.await();
	}
}
