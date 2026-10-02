package com.survivalcoding.day10.exam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Book implements Comparable<Book>, Cloneable {
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-ss");
    private final String title;
    private final Date publishDate;
    private final String comment;

    // 생성
    public Book(String title, String publishDate, String comment) {
        formatter.setLenient(false);

        try {
            this.publishDate = formatter.parse(publishDate);
        } catch (ParseException e) {
            throw new IllegalArgumentException("올바른 날짜 포맷을 넣어주세요");
        }

        this.title = title;
        this.comment = comment;
    }

    // 클론 전용
    private Book(String title, Date publishDate, String comment) {
        this.title = title;
        this.publishDate = publishDate;
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Book book)) return false;

        Date targetPublishDate = book.getPublishDate();

        return Objects.equals(title, book.title) &&
                publishDate.getYear() == targetPublishDate.getYear() &&
                publishDate.getMonth() == targetPublishDate.getMonth() &&
                publishDate.getDate() == targetPublishDate.getDate();
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(title);
        result = 31 * result * publishDate.getYear();
        result = 31 * result * publishDate.getMonth();
        result = 31 * result * publishDate.getDate();

        return result;
    }

    // region getter setter

    public String getTitle() {
        return title;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "Book{" +
                "formatter=" + formatter +
                ", title='" + title + '\'' +
                ", publishDate=" + publishDate +
                ", comment='" + comment + '\'' +
                '}';
    }

    // endregion getter setter

    @Override
    public int compareTo(Book o) {
        return publishDate.compareTo(o.publishDate);
    }

    @Override
    public Book clone() {
        return new Book(title, (Date) publishDate.clone(), comment);
    }
}
