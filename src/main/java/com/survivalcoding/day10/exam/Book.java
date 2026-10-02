package com.survivalcoding.day10.exam;

import java.util.Date;
import java.util.Objects;

public class Book {
    private String title;
    private Date publishDate;
    private String comment;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Book book)) return false;

        var targetPublishDate = book.getPublishDate();

        return publishDate.getYear() == targetPublishDate.getYear() &&
                publishDate.getMonth() == targetPublishDate.getMonth() &&
                publishDate.getDay() == targetPublishDate.getDay();
    }

    @Override
    public int hashCode() {
        int result = 31 * publishDate.getYear();
        result = 31 * result * publishDate.getMonth();
        result = 31 * result * publishDate.getDay();

        return result;
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
}
