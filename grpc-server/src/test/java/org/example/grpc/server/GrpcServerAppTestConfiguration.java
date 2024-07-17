package org.example.grpc.server;

import net.devh.boot.grpc.client.autoconfigure.GrpcClientAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerFactoryAutoConfiguration;
import org.example.grpc.common.generated.Book;
import org.example.grpc.common.generated.BookType;
import org.example.grpc.server.service.GrpcBookService;
import org.example.grpc.server.service.GrpcHelloService;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@ImportAutoConfiguration({
        GrpcServerAutoConfiguration.class,
        GrpcServerFactoryAutoConfiguration.class,
        GrpcClientAutoConfiguration.class})
public class GrpcServerAppTestConfiguration {

    @Bean
    GrpcBookService grpcBookService() {
        final List<Book> list = new ArrayList<>();
        list.add(Book.newBuilder()
                .setISBN(1)
                .setAuthor("Sir Arthur Conan Doyle")
                .setTitle("A Study in Scarlet")
                .setBookType(BookType.BOOK)
                .build());
        return new GrpcBookService(list);
    }

    @Bean
    GrpcHelloService grpcHelloService() {
        return new GrpcHelloService();
    }

}
