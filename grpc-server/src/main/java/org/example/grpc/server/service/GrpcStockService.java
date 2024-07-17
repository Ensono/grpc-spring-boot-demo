package org.example.grpc.server.service;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.grpc.common.generated.Stock;
import org.example.grpc.common.generated.StockQuote;
import org.example.grpc.common.generated.StockServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

@GrpcService
public class GrpcStockService extends StockServiceGrpc.StockServiceImplBase {
    private static final int OFFERS_COUNT = 5;
    private static final Logger logger = LoggerFactory.getLogger(GrpcStockService.class);

    @Override
    public void serverSideStreamingGetListStockQuotes(Stock request, StreamObserver<StockQuote> responseObserver) {
        for (int i = 1; i <= OFFERS_COUNT; i++) {
            final StockQuote stockQuote = StockQuote.newBuilder()
                    .setPrice(fetchStockPriceBid(request))
                    .setOfferNumber(i)
                    .setDescription("Price for stock: " + request.getTickerSymbol())
                    .build();
            responseObserver.onNext(stockQuote);
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Stock> clientSideStreamingGetStatisticsOfStocks(final StreamObserver<StockQuote> responseObserver) {
        return new StreamObserver<>() {
            private int count;
            private double totalPrice = 0.0;
            private final StringBuffer sb = new StringBuffer();

            @Override
            public void onNext(Stock stock) {
                count++;
                totalPrice += fetchStockPriceBid(stock);
                sb.append(":")
                        .append(stock.getTickerSymbol());
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(StockQuote.newBuilder()
                        .setPrice(totalPrice / count)
                        .setDescription("Statistics-" + sb.toString())
                        .build());
                responseObserver.onCompleted();
            }

            @Override
            public void onError(Throwable t) {
                logger.warn("Client-side streaming failed: {}", t.getMessage(), t);
            }
        };
    }

    @Override
    public StreamObserver<Stock> bidirectionalStreamingGetListsStockQuotes(final StreamObserver<StockQuote> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(Stock request) {

                for (int i = 1; i <= OFFERS_COUNT; i++) {
                    final StockQuote stockQuote = StockQuote.newBuilder()
                            .setPrice(fetchStockPriceBid(request))
                            .setOfferNumber(i)
                            .setDescription("Price for stock:" + request.getTickerSymbol())
                            .build();

                    responseObserver.onNext(stockQuote);
                }
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }

            @Override
            public void onError(Throwable t) {
                logger.warn("Bi-directional streaming failed: {}", t.getMessage(), t);
            }
        };
    }

    private  double fetchStockPriceBid(Stock stock) {
        return stock.getTickerSymbol().length() + ThreadLocalRandom.current().nextDouble(-0.1d, 0.1d);
    }

}
