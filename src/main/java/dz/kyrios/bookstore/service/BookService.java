package dz.kyrios.bookstore.service;

import dz.kyrios.bookstore.config.exception.AuthorizationDeniedException;
import dz.kyrios.bookstore.config.exception.NotFoundException;
import dz.kyrios.bookstore.dto.BookRequestDto;
import dz.kyrios.bookstore.dto.BookResponseDto;
import dz.kyrios.bookstore.entity.Book;
import dz.kyrios.bookstore.entity.User;
import dz.kyrios.bookstore.mapper.BookMapper;
import dz.kyrios.bookstore.repository.BookRepository;
import dz.kyrios.bookstore.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final AuthService authService;

    private final UserRepository userRepository;

    public BookService(BookRepository bookRepository,
                       BookMapper bookMapper,
                       AuthService authService,
                       UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.authService = authService;
        this.userRepository = userRepository;
    }

    public Page<BookResponseDto> getBooksWithSearch(String keyword, Pageable pageable) {
        List<BookResponseDto> responseList;
        Page<Book> page;
        page = bookRepository.getBooksWithSearch(keyword, pageable);

        responseList = page.getContent().stream()
                .map(bookMapper::entityToResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(responseList, pageable, page.getTotalElements());
    }

    public Page<BookResponseDto> getBooksWithSearchByUser(String keyword, Pageable pageable) {
        User currentUser = userRepository.findByUsername(authService.getCurrentUser())
                .orElseThrow(() -> new NotFoundException("Current User not found"));
        List<BookResponseDto> responseList;
        Page<Book> page;
        page = bookRepository.getBooksWithSearchByUser(currentUser, keyword, pageable);

        responseList = page.getContent().stream()
                .map(bookMapper::entityToResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(responseList, pageable, page.getTotalElements());
    }

    public BookResponseDto getBookByIdPublic(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, "Book not found with id: "));
        return bookMapper.entityToResponse(book);
    }

    public BookResponseDto getBookById(Long id) {
        User currentUser = userRepository.findByUsername(authService.getCurrentUser())
                .orElseThrow(() -> new NotFoundException("Current User not found"));
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, "Book not found with id: "));
        if (currentUser.equals(book.getAuthor())) {
            return bookMapper.entityToResponse(book);
        } else {
            throw new AuthorizationDeniedException("You are not authorized to access this resource.");
        }
    }

    public BookResponseDto saveBook(BookRequestDto request) {
        User currentUser = userRepository.findByUsername(authService.getCurrentUser())
                .orElseThrow(() -> new NotFoundException("Current User not found"));
        Book bookToCreate = bookMapper.requestToEntity(request);
        bookToCreate.setAuthor(currentUser);
        Book created = bookRepository.save(bookToCreate);
        return bookMapper.entityToResponse(created);
    }

    public BookResponseDto updateBook(BookRequestDto request, Long id) {
        User currentUser = userRepository.findByUsername(authService.getCurrentUser())
                .orElseThrow(() -> new NotFoundException("Current User not found"));
        Book entity = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, "Book not found with id: "));

        if (currentUser.equals(entity.getAuthor())) {
            entity.setTitle(request.getTitle());
            entity.setDescription(request.getDescription());
            entity.setPrice(request.getPrice());
            entity.setCoverImagePath(request.getCoverImagePath());

            return bookMapper.entityToResponse(entity);
        } else {
            throw new AuthorizationDeniedException("You are not authorized to edit this resource.");
        }

    }

    public void deleteBook(Long id) {
        User currentUser = userRepository.findByUsername(authService.getCurrentUser())
                .orElseThrow(() -> new NotFoundException("Current User not found"));
        Book entity = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, "Book not found with id: "));
        if (currentUser.equals(entity.getAuthor())) {
            bookRepository.delete(entity);
        } else {
            throw new AuthorizationDeniedException("You are not authorized to delete this resource.");
        }
    }
}
