package edu.sdsu.cs.assignment3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Horsie on 3/10/15.
 */
public class Instructor implements Serializable {
    private int id;
    private Double rating;
    private int numberOfRatings;
    private String firstName, lastName;
    private String office, phone, email;
    private ArrayList<Comment> comments;

    private boolean dataCached;

    public Instructor(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        dataCached = false;
        office = phone = email = null;
        rating = -1.0;
        numberOfRatings = -1;
        comments = new ArrayList<>();
    }

    @Override
    public String toString() {
        return (firstName + " " + lastName);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return toString();
    }

    public int getId() {
        return id;
    }

    public boolean isDataCached() {
        if (dataCached) {
            return true;
        } else {
            if (phone == null || office == null || email == null || rating == -1.0 || numberOfRatings == -1) {
                return false;
            } else {
                dataCached = true;
                return true;
            }
        }
    }

    public boolean addComment(Comment comment) {
        return comments.add(comment);
    }

    public Comment[] getAllComments() {
        Comment[] commentsArray = new Comment[comments.size()];
        comments.toArray(commentsArray);
        return commentsArray;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    public void clearComments() {
        comments.clear();
    }

    public void updateInDB(Context pContext) {
        InstructorDbHelper dbHelper = new InstructorDbHelper(pContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InstructorSchema.InstructorEntry.COLUMN_NAME_EMAIL, getEmail());
        values.put(InstructorSchema.InstructorEntry.COLUMN_NAME_RATING, getRating());
        values.put(InstructorSchema.InstructorEntry.COLUMN_NAME_TOTAL_RATINGS, getNumberOfRatings());
        values.put(InstructorSchema.InstructorEntry.COLUMN_NAME_PHONE, getPhone());
        values.put(InstructorSchema.InstructorEntry.COLUMN_NAME_OFFICE, getOffice());

        String whereClause = InstructorSchema.InstructorEntry.COLUMN_NAME_INSTRUCTOR_ID + " = " + getId();
        db.update(InstructorSchema.InstructorEntry.TABLE_NAME, values, whereClause, null);
    }

    public void updateCommentsInDB(Context pContext) {
        InstructorDbHelper dbHelper = new InstructorDbHelper(pContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String deleteCommentsSql = "DELETE FROM " + InstructorSchema.CommentEntry.TABLE_NAME +
                " WHERE " + InstructorSchema.CommentEntry.COLUMN_NAME_INSTRUCTOR_ID + " = " + getId();
        db.execSQL(deleteCommentsSql);

        ContentValues values = new ContentValues();
        for (Comment comment : comments) {
            values.clear();
            values.put(InstructorSchema.CommentEntry.COLUMN_NAME_INSTRUCTOR_ID, getId());
            values.put(InstructorSchema.CommentEntry.COLUMN_NAME_TEXT, comment.getText());
            values.put(InstructorSchema.CommentEntry.COLUMN_NAME_DATE, comment.getDate());

            db.insert(InstructorSchema.CommentEntry.TABLE_NAME, null, values);
        }
    }

    public void getDetailsFromDB(Context pContext) {
        InstructorDbHelper dbHelper = new InstructorDbHelper(pContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                InstructorSchema.InstructorEntry.COLUMN_NAME_EMAIL,
                InstructorSchema.InstructorEntry.COLUMN_NAME_RATING,
                InstructorSchema.InstructorEntry.COLUMN_NAME_TOTAL_RATINGS,
                InstructorSchema.InstructorEntry.COLUMN_NAME_PHONE,
                InstructorSchema.InstructorEntry.COLUMN_NAME_OFFICE,
        };

        String whereClause = InstructorSchema.InstructorEntry.COLUMN_NAME_INSTRUCTOR_ID + " = ?";
        String [] whereArgs = new String[] {String.valueOf(getId())};
        Cursor cursor = db.query(InstructorSchema.InstructorEntry.TABLE_NAME, columns, whereClause, whereArgs, null, null, null);

        cursor.moveToFirst();
        int emailIndex = cursor.getColumnIndex(InstructorSchema.InstructorEntry.COLUMN_NAME_EMAIL);
        int ratingIndex = cursor.getColumnIndex(InstructorSchema.InstructorEntry.COLUMN_NAME_RATING);
        int totalRatingIndex = cursor.getColumnIndex(InstructorSchema.InstructorEntry.COLUMN_NAME_TOTAL_RATINGS);
        int phoneIndex = cursor.getColumnIndex(InstructorSchema.InstructorEntry.COLUMN_NAME_PHONE);
        int officeIndex = cursor.getColumnIndex(InstructorSchema.InstructorEntry.COLUMN_NAME_OFFICE);

        setEmail(cursor.getString(emailIndex));
        setRating(cursor.getDouble(ratingIndex));
        setNumberOfRatings(cursor.getInt(totalRatingIndex));
        setPhone(cursor.getString(phoneIndex));
        setOffice(cursor.getString(officeIndex));

        getCommentsFromDB(pContext);
    }

    public void getCommentsFromDB(Context pContext) {
        InstructorDbHelper dbHelper = new InstructorDbHelper(pContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                InstructorSchema.CommentEntry.COLUMN_NAME_TEXT,
                InstructorSchema.CommentEntry.COLUMN_NAME_DATE
        };

        String whereClause = InstructorSchema.CommentEntry.COLUMN_NAME_INSTRUCTOR_ID + " = ?";
        String [] whereArgs = new String[] {String.valueOf(getId())};
        Cursor cursor = db.query(InstructorSchema.CommentEntry.TABLE_NAME, columns, whereClause, whereArgs, null, null, null);

        clearComments();

        int textIndex = cursor.getColumnIndex(InstructorSchema.CommentEntry.COLUMN_NAME_TEXT);
        int dateIndex = cursor.getColumnIndex(InstructorSchema.CommentEntry.COLUMN_NAME_DATE);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String text = cursor.getString(textIndex);
            String date = cursor.getString(dateIndex);
            Comment comment = new Comment(date,text);
            addComment(comment);
            cursor.moveToNext();
        }

    }

}
