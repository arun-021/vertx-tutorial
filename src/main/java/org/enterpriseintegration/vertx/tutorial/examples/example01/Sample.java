package org.enterpriseintegration.vertx.tutorial.examples.example01;

import org.enterpriseintegration.vertx.tutorial.examples.ExampleUtil;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

/**
 * Example 01: Expose a basic REST resource using Vert.x router in parsing
 * header parameters
 */
public class Sample extends AbstractVerticle {

	public static void main(String[] args) {
		ExampleUtil.deployVerticle("org.enterpriseintegration.vertx.tutorial.examples.example01.Sample");
	}

	// Start method
	@Override
	public void start(Future<Void> startFuture) {
		// Create a router object allowing to route HTTP request
		final Router router = Router.router(vertx);

		// Create a new get method listening on /hello resource with a parameter
		router.route(HttpMethod.GET, "/hello").handler(routingContext -> {
			// Retrieving request and response objects
			HttpServerRequest request = routingContext.request();
			HttpServerResponse response = routingContext.response();
			
			//Process parameters
			MultiMap map =  request.params();
			Period period = getAge(map);
			
			// Manage output response
			response.putHeader("Content-Type", "text/plain");
			response.setChunked(true);
			response.write("Hello " + map.get("name"));
			response.write("\nYou are now " + period.getYears() + " years " + period.getMonths() + " months and "+period.getDays()+" days old !");
			response.setStatusCode(200);
			response.end();
		});

		// Create an HTTP server listening on port 8080
		vertx.createHttpServer().requestHandler(router::accept).listen(8080, result -> {
			if (result.succeeded()) {
				// If the HTTP server is deployed, we consider the Verticle as
				// correctly deployed
				startFuture.complete();
			} else {
				// If not, we invoke the fail method
				startFuture.fail(result.cause());
			}
		});
	}

	// Stop method
	@Override
	public void stop(Future<Void> stopFuture) throws Exception {
		// Do something
		stopFuture.complete();
	}
	
	Period getAge(MultiMap map)
	{
		// Get the name parameter
		Integer year = Integer.parseInt(map.get("yyyy"));
		Integer month = Integer.parseInt(map.get("mm"));
		Integer day = Integer.parseInt(map.get("dd"));
		LocalDate birthdate = new LocalDate (year, month, day);      //Birth date
		LocalDate now = new LocalDate();                        //Today's date
		Period period = new Period(birthdate, now, PeriodType.yearMonthDay());	 
		//Now access the values as below
		System.out.println(map.get("name")+": "+period.getYears()+" "+period.getMonths()+ " "+period.getDays());
		return period;
	}
}