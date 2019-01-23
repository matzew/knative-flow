package com.example.helloworld;

import io.cloudevents.http.reactivex.vertx.VertxCloudEvents;
import io.vertx.core.http.HttpHeaders;
import io.vertx.reactivex.core.AbstractVerticle;

public class MultiplicationDataProcessor extends AbstractVerticle {

    public void start() {

        vertx.createHttpServer()
                .requestHandler(req -> VertxCloudEvents.create().rxReadFromRequest(req)
                        .subscribe((receivedEvent, throwable) -> {
                            if (receivedEvent != null) {

                                // extract the data/double out of the cloudevent:
                                // I got a cloud Event: Echo that
                                final Double val = Double.parseDouble(receivedEvent.getData().get().toString());

                                // reading multiplication factor
                                final Double multiplicationFactor = getMultiplicationFactor();

                                // apply the math
                                final Double result = val * multiplicationFactor;

                                // return result as plain text response...
                                req.response()
                                        .putHeader(HttpHeaders.CONTENT_LENGTH, HttpHeaders.createOptimized(String.valueOf(result.toString().length())))
                                        .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaders.createOptimized("text/plain"))
                                        .setStatusCode(200)
                                        .end(result.toString());
                            } else {
                                // error if no cloud event is there
                                // 500 does also prevent a reply in Knative;
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

    private Double getMultiplicationFactor() {

        if (System.getenv("CEF_MULTIPL") != null) {
            return Double.parseDouble(System.getenv("CEF_MULTIPL"));
        }

        // defaulting to 2.0

        return 2.0;
    }

}
