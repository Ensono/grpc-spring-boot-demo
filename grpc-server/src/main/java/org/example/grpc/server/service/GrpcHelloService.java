package org.example.grpc.server.service;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.grpc.common.generated.HelloWorldRequest;
import org.example.grpc.common.generated.HelloWorldResponse;
import org.example.grpc.common.generated.HelloWorldServiceGrpc;

import java.util.Optional;

@GrpcService
public class GrpcHelloService extends HelloWorldServiceGrpc.HelloWorldServiceImplBase {

    @Override
    public void sayHello(HelloWorldRequest request, StreamObserver<HelloWorldResponse> responseObserver) {
        final String name = Optional.ofNullable(request.getName()).orElse("World");
        final HelloWorldResponse response = HelloWorldResponse.newBuilder()
                .setGreeting("Hello " + name)
                        .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
