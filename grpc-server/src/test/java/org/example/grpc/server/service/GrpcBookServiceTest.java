package org.example.grpc.server.service;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.grpc.common.generated.*;
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
public class GrpcBookServiceTest {

    @GrpcClient("books")
    private BookServiceGrpc.BookServiceBlockingStub bookService;

    @Test
    @DirtiesContext
    public void createBooksTest() {
        final BookList response = bookService.createBooks(generateBookList());

        assertNotNull(response);
        assertEquals(2, response.getBookCount());
        assertEquals("Murder on the Orient Express", response.getBook(0).getTitle());
        assertEquals("National Geographic", response.getBook(1).getTitle());
    }

    @Test
    @DirtiesContext
    public void findBookTest() {
        final Book response = bookService.findBookByISBN(ISBN.newBuilder().setCode(1).build());

        assertNotNull(response);
        assertEquals("A Study in Scarlet", response.getTitle());
    }

    private BookList generateBookList() {
        final Book book1 = Book.newBuilder()
                .setISBN(2)
                .setAuthor("Agatha Christie")
                .setTitle("Murder on the Orient Express")
                .setBookType(BookType.BOOK)
                .build();

        final Book book2 = Book.newBuilder()
                .setISBN(3)
                .setTitle("National Geographic")
                .setBookType(BookType.JOURNAL)
                .build();

        return BookList.newBuilder().addBook(book1).addBook(book2).build();
    }

}
