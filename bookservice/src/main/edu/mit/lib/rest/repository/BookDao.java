package edu.mit.lib.rest.repository;

import edu.mit.lib.rest.entities.Book;

import java.util.List;

/**
 * <p>Title: MIT Library Practice</p>
 * <p>Description: edu.mit.lib.rest.repository.BookDao</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Labs Co., Inc</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @since 8/5/2016
 */
public interface BookDao {

    List<Book> getBooks();

    List<Book> getBooksOrderBy(String orderBy);

    Book getBookById(Long id);

    Book getBookByISBN(String isbn);

    Long deleteBookById(Long id);

    Long createBook(Book book);

    int updateBook(Book book);

    void deleteBooks();
}
