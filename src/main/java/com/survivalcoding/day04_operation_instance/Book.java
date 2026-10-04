package com.survivalcoding.day04_operation_instance;

import java.util.Date;
import java.util.Objects;

public final class Book implements Comparable<Book>, Cloneable {

    // field
    private String title;
    private Date publishDate;
    private String comment;

    // constructor
    public Book(String title, Date publishDate, String comment) {
        setTitle(title);
        setPublishDate(publishDate);
        setComment(comment);
    }

    @Override
    public String toString() {
        return "Book [title=" + title
                + ", publishDate=" + DateUtil.toString(publishDate)
                + ", comment=" + comment + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((publishDate == null) ? 0 : publishDate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Book book)) {
            return false;
        }
        return Objects.equals(this.title, book.title) && Objects.equals(this.publishDate, book.publishDate);
    }

    @Override
    public int compareTo(Book obj) {
        int result = obj.publishDate.compareTo(this.publishDate);
        if (result != 0) {
            return result;
        }
        return this.title.compareTo(obj.title);
    }

    @Override
    public Book clone() {
        Book result = new Book(
                this.title,
                (Date) this.publishDate.clone(),
                this.comment);
        return result;
    }

    // getter
    public String getTitle() {
        return title;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public String getComment() {
        return comment;
    }

    // setter
    public void setTitle(String title) {
        if (title == null) {
            throw new IllegalArgumentException("title(제목)은 null이 아니어야 합니다");
        }
        this.title = title;
    }

    public void setPublishDate(Date publishDate) {
        if (publishDate == null) {
            throw new IllegalArgumentException("publishDate(출판일)은 null이 아니어야 합니다");
        }
        this.publishDate = publishDate;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
