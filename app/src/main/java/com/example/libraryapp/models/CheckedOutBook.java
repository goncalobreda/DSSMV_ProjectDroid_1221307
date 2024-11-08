package com.example.libraryapp.models;

import java.util.List;

public class CheckedOutBook {
    private String id;
    private String bookId;
    private String libraryId;
    private String userId;
    private boolean active;
    private String dueDate;
    private String createTimestamp;
    private String updateTimestamp;
    private String libraryName;
    private String libraryAddress;
    private String libraryOpenTime;
    private String libraryCloseTime;
    private Book book;

    // Getters e setters para todos os atributos
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(String createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public String getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(String updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public String getLibraryAddress() {
        return libraryAddress;
    }

    public void setLibraryAddress(String libraryAddress) {
        this.libraryAddress = libraryAddress;
    }

    public String getLibraryOpenTime() {
        return libraryOpenTime;
    }

    public void setLibraryOpenTime(String libraryOpenTime) {
        this.libraryOpenTime = libraryOpenTime;
    }

    public String getLibraryCloseTime() {
        return libraryCloseTime;
    }

    public void setLibraryCloseTime(String libraryCloseTime) {
        this.libraryCloseTime = libraryCloseTime;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
