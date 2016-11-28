package edu.mit.lib.rest.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Title: MIT Library Practice</p>
 * <p>Description: edu.mit.lib.rest.entities.Book</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Labs Co., Inc</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @since 8/5/2016
 */
public class Book implements Serializable {

    private Long id;
    private String name;
    private String version;
    private String author;
    private String publisher;
    private Date publishdate;
    private String isbn;
    private Integer paperback;
    private String summary;

    public Book() {
    }

    public Book(String name, String version, String author, String isbn) {

        this.name = name;
        this.version = version;
        this.author = author;
        this.isbn = isbn;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getPublishdate() {
        return publishdate;
    }

    public void setPublishdate(Date publishdate) {
        this.publishdate = publishdate;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getPaperback() {
        return paperback;
    }

    public void setPaperback(Integer paperback) {
        this.paperback = paperback;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "Book[\nName : " + name + ",\nVersion : " + version + ",\nAuthor : " + author + ",\nPublisher : "
            + publisher + ",\nPublish Date : " + publishdate + ",\nISBN : " + isbn + ",\nPaperback : " + paperback
            + ",\nSummary : " + summary + "\n]";
    }
}
