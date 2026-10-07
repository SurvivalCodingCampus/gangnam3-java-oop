package com.survivalcoding.day10_class_instance_book;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;

public class Book implements Comparable<Book>, Cloneable {

    private String title;
    private LocalDate publishDate;
    private String comment;

    public Book() {
    }

    public Book(String title, LocalDate publishDate, String comment) {
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

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object obj) {
        // 제목과 출간일이 같으면 같은 책
        if (this == obj) { // 같은 인스턴스
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) { // null이거나 다른 클래스
            return false;
        }
        Book other = (Book) obj;

        return Objects.equals(title, other.title)
                && Objects.equals(publishDate, other.publishDate);
    }

    @Override
    public int hashCode() {
        // equals랑 같은 기준으로 계산해야 Set, Map에서도 같은 책으로 판단
        // Set, Map에 넣은 뒤에는 제목이나 출간일을 바꾸지 않기
        return Objects.hash(title, publishDate);
    }

    @Override
    public int compareTo(Book other) {
        // 음수면 앞, 0이면 같음, 양수면 뒤
        Objects.requireNonNull(other, "비교할 책은 null일 수 없습니다.");

        // 최신순 정렬, 출간일이 없으면 마지막
        Comparator<LocalDate> dateOrder = Comparator.nullsLast(Comparator.reverseOrder());
        int result = dateOrder.compare(publishDate, other.publishDate);
        if (result != 0) {
            return result;
        }

        // 출간일이 같으면 제목순
        Comparator<String> titleOrder = Comparator.nullsFirst(Comparator.naturalOrder());
        return titleOrder.compare(title, other.title);
    }

    @Override
    public Book clone() {
        try {
            // String과 LocalDate는 불변이라 참조를 공유해도 서로 영향 없음
            return (Book) super.clone();
        } catch (CloneNotSupportedException e) {
            // Cloneable을 구현했으므로 발생하지 않는 예외
            throw new AssertionError("Book은 Cloneable을 구현합니다.", e);
        }
    }

    @Override
    public String toString() {
        // 책 정보 출력
        return "Book{title='" + title + "', publishDate=" + publishDate
                + ", comment='" + comment + "'}";
    }
}
