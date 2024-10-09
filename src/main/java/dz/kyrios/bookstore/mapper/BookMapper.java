package dz.kyrios.bookstore.mapper;

import dz.kyrios.bookstore.dto.BookRequestDto;
import dz.kyrios.bookstore.dto.BookResponseDto;
import dz.kyrios.bookstore.entity.Book;

public interface BookMapper {

    Book requestToEntity(BookRequestDto request);

    BookResponseDto entityToResponse(Book entity);
}
