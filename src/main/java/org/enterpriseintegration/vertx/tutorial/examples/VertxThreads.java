package org.enterpriseintegration.vertx.tutorial.examples;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

public class VertxThreads {
	public static Future<Vertx> deployVerticles(String ref, int instances) {
		System.out.println("Before starting VertX -> " + Thread.activeCount() + " thread ::: currentThread->"
				+ Thread.currentThread());
		Future<Vertx> future = Future.future();
		Vertx vertx = Vertx.vertx();
		System.out.println("After starting VertX  ie.. Vertx.vertx() -> " + Thread.activeCount()
				+ " thread  ::: CurrentThread ->"+ Thread.currentThread());
		return future;
	}
	public static Future<Vertx> deployverticle(String ref){
		return deployVerticles(ref,1);
	}
}
