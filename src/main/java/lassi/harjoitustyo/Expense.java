package lassi.harjoitustyo;

import java.time.*;


public class Expense {
    
    private LocalDate date;
    private String category;
    private double price;
    private String comment;

    public Expense() {
        //empty constructor
    }

    public Expense(String date, String category, double price, String comment) {
        this.date = LocalDate.parse(date);
        this.category = category;
        this.price = price;
        this.comment = comment;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public String getDateString() {
        return this.date.toString();
    }

    public void setDate(int year, int month, int day) {
        this.date = LocalDate.of(year, month, day);
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String toString() {
        return this.date + "," + this.category + "," + this.price + "," + this.comment;
    }

    public static void main(String[] args) {
	}
}
