package org.enterpriseintegration.vertx.tutorial.examples.example01;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.vertx.core.AbstractVerticle;

class MyVerticle extends AbstractVerticle {
	private final Map<String, AtomicInteger> threadCounts;

	MyVerticle(Map<String, AtomicInteger> threadCounts) {
		this.threadCounts = threadCounts;
	}

	@Override
	public void start() {
		threadCounts.computeIfAbsent(Thread.currentThread().getName(), t -> new AtomicInteger(0)).incrementAndGet();
		System.out.println(
				"Thread Name = " + Thread.currentThread().getName() );
	}
}
