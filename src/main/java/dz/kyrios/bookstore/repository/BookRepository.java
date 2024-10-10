package dz.kyrios.bookstore.repository;

import dz.kyrios.bookstore.entity.Book;

import dz.kyrios.bookstore.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE b.title LIKE %?1% OR b.description LIKE %?1%")
    Page<Book> getBooksWithSearch(String keyword, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE b.author = ?1 and (b.title LIKE %?2% OR b.description LIKE %?2%)")
    Page<Book> getBooksWithSearchByUser(User author, String keyword, Pageable pageable);
}
