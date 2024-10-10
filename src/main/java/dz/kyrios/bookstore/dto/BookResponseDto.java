package dz.kyrios.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDto {

    private Long id;
    private String title;
    private String description;
    private String coverImagePath;
    private Double price;
    private String author;
}
