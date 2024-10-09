package dz.kyrios.bookstore.dto;

import lombok.Data;

@Data
public class BookRequestDto {

    private String title;
    private String description;
    private String coverImagePath;
    private Double price;
}
