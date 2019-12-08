package org.enterpriseintegration.vertx.tutorial.examples.example01;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class ThousandVerticles extends AbstractVerticle {

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();

		final Map<String, AtomicInteger> threadCounts = new ConcurrentHashMap<>();
		AtomicInteger ai = new AtomicInteger();
		int verticles = 1000;
		int no_of_threads_before_deployment = Thread.activeCount();
		System.out.println(
				"Defore deploying " + verticles + " verticles ->" + no_of_threads_before_deployment + " threads");
		
		/*/* ********* 16 Threads are used to deploy 1000 verticles. 1000 verticles are using 16 threads
		final CountDownLatch latch = new CountDownLatch(verticles);
		
		for (int i = 0; i < verticles; i++) {
			vertx.deployVerticle(new MyVerticle(threadCounts), c -> latch.countDown());
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//*/
		
		/*/ ********* Deploy 1000 verticles ONE by ONE. 1000 verticles are using 1 threads.  
		deployMyVerticle(vertx, threadCounts, ai, verticles);
		//*/
		
		
		// ********* Deploy 1000 WORKER Verticles . 1000 verticles are using 28 threads
		// Deploying one thousand worker verticles added another 20 threads.
		//That’s because worker verticles use a separate thread pool, which is of size 20 by default.
		// https://github.com/eclipse/vert.x/blob/master/src/main/java/io/vertx/core/VertxOptions.java#L43
		final CountDownLatch workersLatch = new CountDownLatch(verticles);
		final DeploymentOptions worker = new DeploymentOptions().setWorker(true);
		for (int i = 0; i < verticles; i++) {
		    vertx.deployVerticle(new MyVerticle(threadCounts), worker, c -> workersLatch.countDown());
		}
		try {
			workersLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//*/
		
		int no_of_threads_after_deployment = Thread.activeCount();
		System.out.println(verticles + " verticles are using "
				+ (no_of_threads_after_deployment - no_of_threads_before_deployment) + " threads");
		
		// *****************
		// You can control the size of this pool by calling setWorkerPoolSize() on VertxOptions, then passing them upon Vert.x initialization:
		//final VertxOptions options = new VertxOptions().setWorkerPoolSize(10);
		//Vertx vertx = Vertx.vertx(options);
		
		// ***************
		// And by the way, you can also control the size of the event loop pool in a similar manner:
		// final VertxOptions options = new VertxOptions().setEventLoopPoolSize(4);
		// Vertx vertx = Vertx.vertx(options);
	}

	private static void deployMyVerticle(final Vertx vertx, final Map<String, AtomicInteger> threadCounts,
			final AtomicInteger counter, final int verticles) {
		vertx.deployVerticle(new MyVerticle(threadCounts), c -> {
			System.out.println(
					"Thread Name = " + Thread.currentThread().getName() );
			if (counter.incrementAndGet() < verticles) {
				deployMyVerticle(vertx, threadCounts, counter, verticles);
			}
		});
	}

}