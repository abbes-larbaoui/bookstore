package dz.kyrios.bookstore.mapper;

import dz.kyrios.bookstore.dto.BookRequestDto;
import dz.kyrios.bookstore.dto.BookResponseDto;
import dz.kyrios.bookstore.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapperImp implements BookMapper {
    @Override
    public Book requestToEntity(BookRequestDto request) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setDescription(request.getDescription());
        book.setPrice(request.getPrice());
        return book;
    }

    @Override
    public BookResponseDto entityToResponse(Book entity) {
        BookResponseDto response = new BookResponseDto();
        response.setId(entity.getId());
        response.setTitle(entity.getTitle());
        response.setDescription(entity.getDescription());
        response.setPrice(entity.getPrice());
        response.setAuthor(entity.getAuthor().getPseudonym());
        response.setCoverImagePath(entity.getCoverImagePath());
        return response;
    }
}
