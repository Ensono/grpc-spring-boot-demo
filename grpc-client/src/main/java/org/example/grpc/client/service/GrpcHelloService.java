package org.example.grpc.client.service;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.grpc.common.generated.HelloWorldRequest;
import org.example.grpc.common.generated.HelloWorldResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.example.grpc.common.generated.HelloWorldServiceGrpc;

@Service
public class GrpcHelloService {
    private static final Logger logger = LoggerFactory.getLogger(GrpcHelloService.class);

    @GrpcClient("grpc-hello-client")
    private HelloWorldServiceGrpc.HelloWorldServiceBlockingStub helloWorldService;

    public void sayHello(String name) {
        final HelloWorldRequest request = HelloWorldRequest.newBuilder().setName(name).build();
        final HelloWorldResponse response = helloWorldService.sayHello(request);

        logger.info("gRPC Response received: " + response.getGreeting());
    }
}
