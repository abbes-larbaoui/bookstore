package dz.kyrios.bookstore.controller;

import dz.kyrios.bookstore.config.exception.NotFoundException;
import dz.kyrios.bookstore.dto.BookResponseDto;
import dz.kyrios.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/books")
@Tag(name = "Public", description = "Public endpoints")
public class PublicController {
    private final BookService bookService;

    public PublicController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping()
    @Operation(summary = "All Books", description = "Get list of all books, with pagination and search")
    public ResponseEntity<Object> getAllBooks(@RequestParam Optional<Integer> page,
                                              @RequestParam Optional<Integer> size,
                                              @RequestParam Optional<String> keyword) {
        try {
            Pageable pageable = PageRequest.of(page.orElse(0), size.orElse(10));
            return new ResponseEntity<>(bookService.getBooksWithSearch(keyword.orElse(""), pageable), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "One Book", description = "Get a book detail")
    public ResponseEntity<Object> getBookById(@PathVariable Long id) {
        try {
            BookResponseDto response = bookService.getBookByIdPublic(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
