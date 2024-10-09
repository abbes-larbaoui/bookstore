package dz.kyrios.bookstore.mapper;

import dz.kyrios.bookstore.dto.BookRequestDto;
import dz.kyrios.bookstore.dto.BookResponseDto;
import dz.kyrios.bookstore.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapperImp implements BookMapper {
    @Override
    public Book requestToEntity(BookRequestDto request) {
        return Book.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();
    }

    @Override
    public BookResponseDto entityToResponse(Book entity) {
        return BookResponseDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .author(entity.getAuthor().getPseudonym())
                .description(entity.getDescription())
                .coverImagePath(entity.getCoverImagePath())
                .price(entity.getPrice())
                .build();
    }
}
