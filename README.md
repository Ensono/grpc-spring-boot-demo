# gRPC Java Code Example

This example consists of 3 parts to help beginners understand how to create gRPC client & server with Spring Boot.

Project consists of 3 modules:
- **grpc-client** - which has a gRPC client code example
- **grpc-server** - which has a gRPC server code example
- **grpc-common** - which has protobuf definitions for the services

Project should be compiled for the necessary code to be generated from the protobuf files.

There are 3 services in the client/service modules.
1. ***Hello Service*** - is the simpliest gRPC implementation of the greeting between client & service.
2. ***Book Service*** - is more complicated example of adding new books to the library, finding books and simple error handling if book is not found.
3. ***Stock Service*** - contains several examples of streaming: client-side streaming, server-side streaming and bidirectional streaming.

You can start the **GrpcServerApp** and then **GrpcClientApp** to run all the service methods and generate application logs.


Code examples in this repo are based on the examples provided here:

[Streaming Service](https://github.com/eugenp/tutorials/tree/master/grpc/src/main/java/com/baeldung/grpc/streaming)

[Book Service](https://github.com/ExampleDriven/spring-boot-grpc-example/tree/master)

## Useful Links

[Quick start](https://grpc.io/docs/languages/java/quickstart/)

[Introduction to gRPC | Baeldung](https://www.baeldung.com/grpc-introduction)

[Streaming with gRPC in Java | Baeldung](https://www.baeldung.com/java-grpc-streaming)

[Error Handling in gRPC | Baeldung](https://www.baeldung.com/grpcs-error-handling)

[Configuring Retry Policy for gRPC Request | Baeldung](https://www.baeldung.com/java-gprc-retry-policy)

[gRPC Authentication in Java Using Application Layer Transport Security (ALTS) | Baeldung](https://www.baeldung.com/java-grpc-authentication-application-layer-transport-security-alts)

[gRPC vs REST - Difference Between Application Designs - AWS](https://aws.amazon.com/compare/the-difference-between-grpc-and-rest/)

[What is gRPC: Main Concepts, Pros and Cons, Use Cases | Alte](https://www.altexsoft.com/blog/what-is-grpc/)

[Examples](https://grpc-ecosystem.github.io/grpc-spring/en/examples.html)

[gRPC Ecosystem](https://github.com/grpc-ecosystem)

[gRPC Performance Tips from Microsoft](https://learn.microsoft.com/en-us/aspnet/core/grpc/performance?view=aspnetcore-8.0)

[gRPC Performance](https://grpc.io/docs/guides/performance/)

[gRPC and AWS Kinesis](https://github.com/aws-samples/aws-blog-grpc-kinesis)