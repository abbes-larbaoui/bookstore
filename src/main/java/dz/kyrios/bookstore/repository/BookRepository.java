package dz.kyrios.bookstore.repository;

import dz.kyrios.bookstore.entity.Book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE b.title LIKE %?1% OR b.description LIKE %?1%")
    Page<Book> search(String keyword, Pageable pageable);
}
