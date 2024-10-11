package dz.kyrios.bookstore.controller;

import dz.kyrios.bookstore.config.exception.AuthorizationDeniedException;
import dz.kyrios.bookstore.config.exception.NotFoundException;
import dz.kyrios.bookstore.config.security.JwtTokenProvider;
import dz.kyrios.bookstore.dto.BookRequestDto;
import dz.kyrios.bookstore.dto.BookResponseDto;
import dz.kyrios.bookstore.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @WithMockUser(username = "kyrios")
    public void testGetBookById_Success() throws Exception {
        BookResponseDto mockResponse = new BookResponseDto();
        mockResponse.setId(1L);

        when(bookService.getBookById(1L)).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "kyrios")
    public void testGetBookById_NotFound() throws Exception {
        when(bookService.getBookById(1L)).thenThrow(new NotFoundException("Book not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Book not found"));
    }

    @Test
    @WithMockUser(username = "kyrios")
    public void testGetBookById_Forbidden() throws Exception {
        when(bookService.getBookById(1L)).thenThrow(new AuthorizationDeniedException("Access denied"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/1"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Access denied"));
    }

    @Test
    @WithMockUser(username = "kyrios")
    public void testGetAllBooks() throws Exception {
        BookResponseDto book1 = new BookResponseDto();
        book1.setId(1L);

        BookResponseDto book2 = new BookResponseDto();
        book2.setId(2L);

        Page<BookResponseDto> mockPage = new PageImpl<>(
                Arrays.asList(book1, book2),
                PageRequest.of(0, 10),
                2
        );

        when(bookService.getBooksWithSearchByUser("", PageRequest.of(0, 10)))
                .thenReturn(mockPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[1].id").value(2));
    }

    @Test
    @WithMockUser(username = "kyrios")
    public void testSaveBook_Created() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "cover.jpg", MediaType.IMAGE_JPEG_VALUE, "test image content".getBytes(StandardCharsets.UTF_8)
        );

        BookRequestDto requestDto = new BookRequestDto();
        requestDto.setTitle("New Book");
        requestDto.setDescription("Description of the new book");
        requestDto.setPrice(29.99);

        BookResponseDto responseDto = new BookResponseDto();
        responseDto.setId(1L);
        responseDto.setTitle("New Book");
        responseDto.setDescription("Description of the new book");
        responseDto.setPrice(29.99);
        responseDto.setAuthor("kyrios");

        when(bookService.saveBook(requestDto, file)).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/books")
                        .file(file)
                        .param("title", "New Book")
                        .param("description", "Description of the new book")
                        .param("price", "29.99")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Book"))
                .andExpect(jsonPath("$.description").value("Description of the new book"))
                .andExpect(jsonPath("$.author").value("kyrios"))
                .andExpect(jsonPath("$.price").value(29.99));
    }
}
