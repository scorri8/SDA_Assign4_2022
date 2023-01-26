package com.example.sdaassign4_2022;

public class SendOrdered {
    private String returnDate;
    private String orderDate;
    private String bookId;
    private String borrowerId;

    public SendOrdered() {
    }

    public SendOrdered(String returnDate, String orderedDate, String bookId, String borrowerId) {
        this.returnDate = returnDate;
        this.orderDate = orderedDate;
        this.bookId = bookId;
        this.borrowerId = borrowerId;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }



    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }
}
