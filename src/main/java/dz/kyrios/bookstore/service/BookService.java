package dz.kyrios.bookstore.service;

import dz.kyrios.bookstore.config.exception.NotFoundException;
import dz.kyrios.bookstore.dto.BookResponseDto;
import dz.kyrios.bookstore.entity.Book;
import dz.kyrios.bookstore.mapper.BookMapper;
import dz.kyrios.bookstore.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    public Page<BookResponseDto> getAllBooks(Pageable pageable) {
        List<BookResponseDto> responseList;
        Page<Book> page;
        page = bookRepository.findAll(pageable);

        responseList = page.getContent().stream()
                .map(bookMapper::entityToResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(responseList, pageable, page.getTotalElements());
    }

    public Page<BookResponseDto> searchBooks(String keyword, Pageable pageable) {
        List<BookResponseDto> responseList;
        Page<Book> page;
        page = bookRepository.search(keyword, pageable);

        responseList = page.getContent().stream()
                .map(bookMapper::entityToResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(responseList, pageable, page.getTotalElements());
    }

    public BookResponseDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, "Book not found with id: "));
        return bookMapper.entityToResponse(book);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
