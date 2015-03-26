package edu.sdsu.cs.assignment3;

/**
 * Created by Horsie on 3/12/15.
 */
public class Comment {
    private String date, text;

    public Comment(String date, String text) {
        this.date = date;
        this.text = text;
    }

    @Override
    public String toString() {
        return date + " : " + text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
