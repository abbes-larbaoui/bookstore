package dz.kyrios.bookstore.controller;

import dz.kyrios.bookstore.config.exception.AuthorizationDeniedException;
import dz.kyrios.bookstore.config.exception.NotFoundException;
import dz.kyrios.bookstore.dto.BookRequestDto;
import dz.kyrios.bookstore.dto.BookResponseDto;
import dz.kyrios.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/books")
@Tag(name = "Book", description = "Endpoints for managing book records")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping()
    @Operation(summary = "All Books", description = "Get list of all books of the authenticated author, with pagination and search")
    public ResponseEntity<Object> getAllBooks(@RequestParam Optional<Integer> page,
                                              @RequestParam Optional<Integer> size,
                                              @RequestParam Optional<String> keyword) {
        try {
            Pageable pageable = PageRequest.of(page.orElse(0), size.orElse(10));
            return new ResponseEntity<>(bookService.getBooksWithSearchByUser(keyword.orElse(""), pageable), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "One Book", description = "Get a book detail for the authenticated author")
    public ResponseEntity<Object> getBookById(@PathVariable Long id) {
        try {
            BookResponseDto response = bookService.getBookById(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AuthorizationDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    @Operation(summary = "Save", description = "Publish a new book for the authenticated author")
    public ResponseEntity<Object> save(@RequestParam("file") MultipartFile file,
                                       @RequestParam("title") String title,
                                       @RequestParam("description") String description,
                                       @RequestParam("price") Double price) {
        try {
            BookRequestDto bookRequestDto = new BookRequestDto();
            bookRequestDto.setTitle(title);
            bookRequestDto.setDescription(description);
            bookRequestDto.setPrice(price);
            BookResponseDto response = bookService.saveBook(bookRequestDto, file);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edit", description = "Edit a book for the authenticated author")
    public ResponseEntity<Object> update(@RequestBody BookRequestDto request,
                                         @PathVariable Long id) {
        try {
            BookResponseDto response = bookService.updateBook(request, id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AuthorizationDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }  catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/update-cover-image")
    @Operation(summary = "Change cover image", description = "Change a book cover for the authenticated author")
    public ResponseEntity<Object> updateCoverImage(@RequestParam("file") MultipartFile file,
                                                   @PathVariable Long id) {
        try {
            BookResponseDto response = bookService.updateCoverImage(file, id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AuthorizationDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }  catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete", description = "Unpublish a book for the authenticated author")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AuthorizationDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }  catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
