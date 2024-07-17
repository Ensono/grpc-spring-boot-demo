package org.example.grpc.client.service;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.grpc.common.generated.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GrpcBookService {
    private static final Logger logger = LoggerFactory.getLogger(GrpcBookService.class);

    @GrpcClient("grpc-book-client")
    private BookServiceGrpc.BookServiceBlockingStub bookService;

    public void createBooks() {
        final BookList response = bookService.createBooks(generateBookList());

        response.getBookList().forEach(book -> logger.info("Book created: \n" + book));
    }

    public void findBookById(int id) {
      final Book response = bookService.findBookByISBN(ISBN.newBuilder().setCode(id).build());
      logger.info("Book: \n" + response);
    }

    private BookList generateBookList() {
        final Book book1 = Book.newBuilder()
                .setISBN(1)
                .setAuthor("Agatha Christie")
                .setTitle("Murder on the Orient Express")
                .setBookType(BookType.BOOK)
                .build();

        final Book book2 = Book.newBuilder()
                .setISBN(2)
                .setTitle("National Geographic")
                .setBookType(BookType.JOURNAL)
                .build();

        return BookList.newBuilder().addBook(book1).addBook(book2).build();
    }

}
