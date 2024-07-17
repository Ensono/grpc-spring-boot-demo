package org.example.grpc.client.service;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.grpc.common.generated.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class GrpcStockService {
    private static final Logger logger = LoggerFactory.getLogger(GrpcStockService.class);
    private final List<Stock> stocks;

    public GrpcStockService(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public GrpcStockService() {
        this.stocks = Arrays.asList(
                Stock.newBuilder().setTickerSymbol("AR").setCompanyName("Arasaka Corp").build(),
                Stock.newBuilder().setTickerSymbol("BAS").setCompanyName("Bassel Corp").build(),
                Stock.newBuilder().setTickerSymbol("COR").setCompanyName("Corvine Corp").build(),
                Stock.newBuilder().setTickerSymbol("DIA").setCompanyName("Dialogic Corp").build(),
                Stock.newBuilder().setTickerSymbol("ENS").setCompanyName("Ensono Corp").build()
        );
    }

    @GrpcClient("grpc-stock-client")
    private StockServiceGrpc.StockServiceBlockingStub stockServiceBlocking;
    @GrpcClient("grpc-stock-client")
    private StockServiceGrpc.StockServiceStub stockServiceNonBlocking;

    public void serverSideStreamingListOfStockPrices() {
        logger.info("####### START EXAMPLE #######: ServerSideStreaming - list of Stock prices from a given stock");

        final Stock request = Stock.newBuilder()
                .setTickerSymbol("AR")
                .setCompanyName("Arasaka")
                .build();

        logger.info("REQUEST: {}, {}", request.getTickerSymbol(), request.getCompanyName());

        try {
            final Iterator<StockQuote> stockQuotes = stockServiceBlocking.serverSideStreamingGetListStockQuotes(request);

            stockQuotes.forEachRemaining(stockQuote ->
                    logger.info("RESPONSE - Price #{}: {}", stockQuote.getOfferNumber(), stockQuote.getPrice())
            );
        } catch (StatusRuntimeException e) {
            logger.info("RPC failed: {}", e.getStatus());
        }
    }

    public void clientSideStreamingGetStatisticsOfStocks() throws InterruptedException {
        logger.info("####### START EXAMPLE #######: ClientSideStreaming - getStatisticsOfStocks from a list of stocks");

        final CountDownLatch finishLatch = new CountDownLatch(1);
        final StreamObserver<StockQuote> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(StockQuote summary) {
                logger.info("RESPONSE, got stock statistics - Average Price: {}, description: {}", summary.getPrice(), summary.getDescription());
            }

            @Override
            public void onCompleted() {
                logger.info("Finished clientSideStreamingGetStatisticsOfStocks \n");
                finishLatch.countDown();
            }

            @Override
            public void onError(Throwable t) {
                logger.warn("Stock Statistics Failed: {}", Status.fromThrowable(t));
                finishLatch.countDown();
            }
        };

        final StreamObserver<Stock> requestObserver = stockServiceNonBlocking.clientSideStreamingGetStatisticsOfStocks(responseObserver);

        try {
            stocks.forEach(stock -> {
                logger.info("REQUEST: {}, {}", stock.getTickerSymbol(), stock.getCompanyName());
                requestObserver.onNext(stock);
            });
            requestObserver.onCompleted();
        } catch (RuntimeException e) {
            requestObserver.onError(e);
            throw e;
        }

        if (!finishLatch.await(1, TimeUnit.MINUTES)) {
            logger.warn("Client-side streaming for statistics of stocks did not finish within 1 minute");
        }
    }

    public void bidirectionalStreamingGetListsStockQuotes() throws InterruptedException {
        logger.info("####### START EXAMPLE #######: BidirectionalStreaming - getListsStockQuotes from list of stocks");

        final CountDownLatch finishLatch = new CountDownLatch(1);
        final StreamObserver<StockQuote> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(StockQuote stockQuote) {
                logger.info("RESPONSE price #{} : {}, {}", stockQuote.getOfferNumber(), stockQuote.getPrice(), stockQuote.getDescription());
            }

            @Override
            public void onCompleted() {
                logger.info("Finished bidirectionalStreamingGetListsStockQuotes \n");
                finishLatch.countDown();
            }

            @Override
            public void onError(Throwable t) {
                logger.warn("bidirectionalStreamingGetListsStockQuotes Failed: {}", Status.fromThrowable(t));
                finishLatch.countDown();
            }
        };
        final StreamObserver<Stock> requestObserver = stockServiceNonBlocking.bidirectionalStreamingGetListsStockQuotes(responseObserver);

        try {
            for (Stock stock : stocks) {
                logger.info("REQUEST: {}, {}", stock.getTickerSymbol(), stock.getCompanyName());
                requestObserver.onNext(stock);
                Thread.sleep(200);
                if (finishLatch.getCount() == 0) {
                    return;
                }
            }
        } catch (RuntimeException e) {
            requestObserver.onError(e);
            throw e;
        }
        requestObserver.onCompleted();

        if (!finishLatch.await(1, TimeUnit.MINUTES)) {
            logger.warn("bidirectionalStreamingGetListsStockQuotes can not finish within 1 minute");
        }

    }
}
