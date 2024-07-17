package org.example.grpc.client;

import io.grpc.StatusRuntimeException;
import org.example.grpc.client.service.GrpcBookService;
import org.example.grpc.client.service.GrpcHelloService;
import org.example.grpc.client.service.GrpcStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GrpcClientApp implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(GrpcClientApp.class);

    private final GrpcHelloService helloService;
    private final GrpcBookService bookService;
    private final GrpcStockService stockService;

    public GrpcClientApp(GrpcHelloService helloService, GrpcBookService bookService, GrpcStockService stockService) {
        this.helloService = helloService;
        this.bookService = bookService;
        this.stockService = stockService;
    }

    public static void main(String[] args) {
        SpringApplication.run(GrpcClientApp.class, args);
    }

    @Override
    public void run(String... args) {
        logger.info("----------------------TEST START----------------------------");
        logger.info("---------- Testing greetings gRPC method ---------");
        helloService.sayHello("Dear Client");
        logger.info("---------- Testing books creation gRPC method  ---------");
        bookService.createBooks();
        logger.info("---------- Testing book retrieval gRPC method  ---------");
        bookService.findBookById(1);
        bookService.findBookById(2);
        try {
            bookService.findBookById(3);
        } catch (StatusRuntimeException ex) {
            logger.info("Processed exception from gRPC server {}", ex.getLocalizedMessage());
        }
        logger.info("---------- Testing gRPC Streaming  ---------");
        stockService.serverSideStreamingListOfStockPrices();
        try {
            stockService.clientSideStreamingGetStatisticsOfStocks();
            stockService.bidirectionalStreamingGetListsStockQuotes();
        } catch (InterruptedException e) {
            logger.info("Processed interrupted exception {}", e.getLocalizedMessage());
        }

        logger.info("----------------------TEST END----------------------------");
    }

}