package dz.kyrios.bookstore.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BookResponseDto {

    private Long id;
    private String title;
    private String description;
    private String coverImagePath;
    private Double price;
    private String author;
}
