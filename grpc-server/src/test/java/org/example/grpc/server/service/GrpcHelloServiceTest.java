package org.example.grpc.server.service;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.grpc.common.generated.HelloWorldRequest;
import org.example.grpc.common.generated.HelloWorldResponse;
import org.example.grpc.common.generated.HelloWorldServiceGrpc;
import org.example.grpc.server.GrpcServerAppTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@SpringJUnitConfig(classes = GrpcServerAppTestConfiguration.class)
@DirtiesContext // Ensures that the grpc-server is properly shutdown after each test. Avoids "port already in use" during tests
public class GrpcHelloServiceTest {

    @GrpcClient("hello-world")
    private HelloWorldServiceGrpc.HelloWorldServiceBlockingStub helloWorldService;

    @Test
    @DirtiesContext
    public void helloTest() {
        final HelloWorldRequest request = HelloWorldRequest.newBuilder().setName("World").build();
        final HelloWorldResponse response = helloWorldService.sayHello(request);

        assertNotNull(response);
        assertEquals("Hello World", response.getGreeting());
    }

}
