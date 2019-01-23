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
                                // I got a cloud Event: Echo that

                                final Double val = Double.parseDouble(receivedEvent.getData().get().toString());

                                final Double ret = val * 2;

                                req.response()
                                        .putHeader(HttpHeaders.CONTENT_LENGTH, HttpHeaders.createOptimized(String.valueOf(ret.toString().length())))
                                        .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaders.createOptimized("text/plain"))
                                        .setStatusCode(200)
                                        .end(ret.toString());
                            } else {
                                req.response()
                                        .setChunked(true)
                                        .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaders.createOptimized("text/plain"))
                                        .setStatusCode(500)
                                        .end("nope");
                            }
                        }))
                .rxListen(8080)
                .subscribe(server -> {
                    System.out.println("Server running!");
                });
    }
}
