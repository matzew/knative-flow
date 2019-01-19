package com.example.helloworld;

import io.cloudevents.http.reactivex.vertx.VertxCloudEvents;
import io.vertx.core.http.HttpHeaders;
import io.vertx.reactivex.core.AbstractVerticle;

public class HelloWorld extends AbstractVerticle {

    public void start() {

        vertx.createHttpServer()
                .requestHandler(req -> VertxCloudEvents.create().rxReadFromRequest(req)
                        .subscribe((receivedEvent, throwable) -> {
                            if (receivedEvent != null) {
                                System.out.println(receivedEvent.toString());
                                // I got a cloud Event: Echo that
                                req.response()
                                        .putHeader(HttpHeaders.CONTENT_LENGTH, HttpHeaders.createOptimized(String.valueOf(receivedEvent.toString().length())))
                                        .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaders.createOptimized("text/plain"))
                                        .setStatusCode(200)
                                        //.end(receivedEvent.toString());
                                        .end("{\"returned\":\"VERTX\"}");
                            } else {
                                String target = System.getenv("TARGET");
                                if (target == null) {
                                    target = "NOT SPECIFIED";
                                }
                                req.response()
                                        .setChunked(true)
                                        .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaders.createOptimized("text/plain"))
                                        .setStatusCode(200)
                                        .end("Hello World: " + target);
                            }
                        }))
                .rxListen(8080)
                .subscribe(server -> {
                    System.out.println("Server running!");
                });
    }
}
