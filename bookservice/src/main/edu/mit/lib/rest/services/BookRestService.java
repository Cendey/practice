package edu.mit.lib.rest.services;

import edu.mit.lib.rest.entities.Book;
import edu.mit.lib.rest.repository.BookDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * <p>Title: MIT Library Practice</p>
 * <p>Description: edu.mit.lib.rest.services.BookRestService</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Labs Co., Inc</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @since 8/5/2016
 */
@Component
@Path("/book")
public class BookRestService {

    static private Logger logger = LogManager.getLogger(BookRestService.class);

    private final BookDao bookDao;

    @Autowired
    public BookRestService(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    /**
     * Adds a new resource (book) from the given json format (at least title and feed elements are required
     * at the DB level)
     *
     * @param book <p>Entity of book</p>
     * @return <p>return Ok status, otherwise Bad</p>
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.TEXT_HTML})
    @Transactional
    public Response createBook(Book book) {
        bookDao.createBook(book);
        logger.info("A new book/resource has been created");
        return Response.status(201).entity("A new book/resource has been created").build();
    }

    /**
     * Adds a new resource (book) from "form" (at least title and feed elements are required
     * at the DB level)
     *
     * @param name    <p>Book name</p>
     * @param version <p>Version of current book</p>
     * @param author  <p>Book's author</p>
     * @param isbn    <p>Book's ISBN</p>
     * @return <p>Response to create a book</p>
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.TEXT_HTML})
    @Transactional
    public Response createBookFromForm(
        @FormParam("name") String name, @FormParam("version") String version, @FormParam("author") String author,
        @FormParam("isbn") String isbn) {
        Book book = new Book(name, version, author, isbn);
        bookDao.createBook(book);
        logger.info("A new book/resource has been created");
        return Response.status(201).entity("A new book/resource has been created").build();
    }

    /**
     * A list of resources (here books) provided in json format will be added
     * to the database.
     *
     * @param books <p>All available books</p>
     * @return <p>Collection of all books</p>
     */
    @POST
    @Path("list")
    @Consumes({MediaType.APPLICATION_JSON})
    @Transactional
    public Response createBooks(List<Book> books) {
        for (Book book : books) {
            bookDao.createBook(book);
            logger.info("\nCreate a new book with information:\n" + book.toString());
        }

        return Response.status(204).build();
    }


    /**
     * Returns all resources (books) from the database
     *
     * @return <p>All books</p>
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Book> getBooks() {
        logger.info("Get all books response with JSON or XML data to request.");
        return bookDao.getBooks();
    }

    /**
     * Returns all resources (books) from the database
     *
     * @return <p>All books</p>
     */
    @GET
    @Path("order")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Book> getBooksOrderBy(@QueryParam("orderBy") String orderBy) {
        logger.info("Get all books response order by specified field with JSON or XML data to request.");
        return bookDao.getBooksOrderBy(orderBy);
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response findById(@PathParam("id") Long id) {
        Book bookById = bookDao.getBookById(id);
        if (bookById != null) {
            logger.info("Find the book with id { " + id + " }.\n" + bookById.toString());
            return Response.status(200).entity(bookById).build();
        } else {
            logger.warn("The book does not exist with id { " + id + " }, please double check!");
            return Response.status(404).entity("The book with the id " + id + " does not exist").build();
        }
    }

    @GET
    @Path("/isbn/{isbn}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getBookByISBN(@PathParam("isbn") String isbn) {
        Book bookByISBN = bookDao.getBookByISBN(isbn);
        if (bookByISBN != null) {
            logger.info("Find the book with ISBN { " + isbn + " }.\n" + bookByISBN.toString());
            return Response.status(200).entity(bookByISBN).build();
        } else {
            logger.warn("The book does not exist with ISBN { " + isbn + " }, please double check!");
            return Response.status(404).entity("The book with ISBN " + isbn + " does not exist").build();
        }
    }

    /**
     * Updates the attributes of the book received via JSON for the given @param id
     * <p>
     * If the book does not exist yet in the database (verified by <strong>id</strong>)
     * then the application will try to create a new book resource in the db
     * </p>
     *
     * @param id   <p>Book identify</p>
     * @param book <p>Specified book</p>
     * @return <p>Response for updating the specified book</p>
     */
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.TEXT_HTML})
    @Transactional
    public Response updateBookById(@PathParam("id") Long id, Book book) {
        if (book.getId() == null) book.setId(id);
        String message;
        int status;
        if (bookWasUpdated(book)) {
            status = 200; //OK
            message = "Book has been updated";
            logger.info(message);
        } else if (bookCanBeCreated(book)) {
            bookDao.createBook(book);
            status = 201; //Created
            message = "The book you provided has been added to the database";
            logger.warn(message);
        } else {
            status = 406; //Not acceptable
            message = "The information you provided is not sufficient to perform either an UPDATE or "
                + " an INSERTION of the new book resource <br/>"
                + " If you want to UPDATE please make sure you provide an existent <strong>id</strong> <br/>"
                + " If you want to insert a new book please provide at least a <strong>title</strong> and the <strong>feed</strong> for the book resource";
            logger.error(message);
        }

        return Response.status(status).entity(message).build();
    }

    private boolean bookWasUpdated(Book book) {
        return bookDao.updateBook(book) == 1;
    }

    private boolean bookCanBeCreated(Book book) {
        return book.getIsbn() != null;
    }

    /**
     * Delete specified book
     *
     * @param id <p>The specified book identify</p>
     * @return <p>Response to delete the specified book</p>
     */
    @DELETE
    @Path("{id}")
    @Produces({MediaType.TEXT_HTML})
    @Transactional
    public Response deleteBookById(@PathParam("id") Long id) {
        if (bookDao.deleteBookById(id) == 1) {
            logger.info("Book is deleted with the id { " + id + " }");
            return Response.status(204).build();
        } else {
            logger.warn("Book with the id { " + id + " } is not present in the database");
            return Response.status(404).entity("Book with the id " + id + " is not present in the database").build();
        }
    }

    @DELETE
    @Produces({MediaType.TEXT_HTML})
    @Transactional
    public Response deleteBooks() {
        bookDao.deleteBooks();
        logger.info("All books have been successfully removed");
        return Response.status(200).entity("All books have been successfully removed").build();
    }
}
