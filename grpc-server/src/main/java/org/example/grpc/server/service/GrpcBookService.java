package org.example.grpc.server.service;

import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.grpc.common.generated.Book;
import org.example.grpc.common.generated.BookList;
import org.example.grpc.common.generated.BookServiceGrpc;
import org.example.grpc.common.generated.ISBN;

import java.util.List;

@GrpcService
public class GrpcBookService extends BookServiceGrpc.BookServiceImplBase {
    private final List<Book> books;

    public GrpcBookService(List<Book> books) {
        this.books = books;
    }

    @Override
    public void createBooks(BookList request, StreamObserver<BookList> responseObserver) {
        books.addAll(request.getBookList());

        responseObserver.onNext(request);
        responseObserver.onCompleted();
    }

    @Override
    public void findBookByISBN(ISBN request, StreamObserver<Book> responseObserver) {
        books.stream()
                .filter(b -> b.getISBN() == request.getCode())
                .findAny()
                .ifPresentOrElse(b -> {
                    responseObserver.onNext(b);
                    responseObserver.onCompleted();
                }, () -> notFoundError(request.getCode(), responseObserver));
    }

    private void notFoundError(int id, StreamObserver<Book> responseObserver) {
        com.google.rpc.Status status = com.google.rpc.Status.newBuilder()
                .setCode(com.google.rpc.Code.NOT_FOUND.getNumber())
                .setMessage("The book with ISBN " + id + " not found")
                .build();
        responseObserver.onError(StatusProto.toStatusRuntimeException(status));
    }

}
