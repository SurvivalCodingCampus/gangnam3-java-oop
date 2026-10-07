package com.survivalcoding.instance_basic;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Objects;

public class Book implements Comparable<Book>, Cloneable {
    private String title;
    private Date publishDate;
    private String comment;

    public Book(String title, Date publishDate, String comment) {
        this.title = title;
        this.publishDate = publishDate;
        this.comment = comment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Book book)) return false;

        Date targetPublishDate = book.getPublishDate();

        String year = publishDate.toString();

        return Objects.equals(title, book.title) &&
                publishDate.getYear() == targetPublishDate.getYear() &&
                publishDate.getMonth() == targetPublishDate.getMonth() &&
                publishDate.getDate() == targetPublishDate.getDate();
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(title);
        result = 31 * result + publishDate.getYear();
        result = 31 * result + publishDate.getMonth();
        result = 31 * result + publishDate.getDate();
        return result;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", publishDate=" + publishDate +
                ", comment='" + comment + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NotNull Book o) {
        return publishDate.compareTo(o.publishDate) * -1;
    }

    @Override
    protected Book clone() {
        Book book = new Book(title, (Date) publishDate.clone(), comment);
        return book;
    }
}
