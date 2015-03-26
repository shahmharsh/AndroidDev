package edu.sdsu.cs.assignment3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Horsie on 3/15/15.
 */
public class InstructorDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Instructors.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_INSTRUCTOR_ENTRIES = "CREATE TABLE IF NOT EXISTS " +
                    InstructorSchema.InstructorEntry.TABLE_NAME + " (" +
                    InstructorSchema.InstructorEntry.COLUMN_NAME_INSTRUCTOR_ID + " INTEGER PRIMARY KEY," +
                    InstructorSchema.InstructorEntry.COLUMN_NAME_FIRST_NAME + TEXT_TYPE + COMMA_SEP +
                    InstructorSchema.InstructorEntry.COLUMN_NAME_LAST_NAME + TEXT_TYPE + COMMA_SEP +
                    InstructorSchema.InstructorEntry.COLUMN_NAME_OFFICE + TEXT_TYPE + COMMA_SEP +
                    InstructorSchema.InstructorEntry.COLUMN_NAME_PHONE + TEXT_TYPE + COMMA_SEP +
                    InstructorSchema.InstructorEntry.COLUMN_NAME_EMAIL + TEXT_TYPE + COMMA_SEP +
                    InstructorSchema.InstructorEntry.COLUMN_NAME_RATING + REAL_TYPE + COMMA_SEP +
                    InstructorSchema.InstructorEntry.COLUMN_NAME_TOTAL_RATINGS + INT_TYPE + " )";

    private static final String SQL_DELETE_INSTRUCTOR_ENTRIES = "DROP TABLE IF EXISTS " + InstructorSchema.InstructorEntry.TABLE_NAME;

    private static final String SQL_CREATE_COMMENT_ENTRIES = "CREATE TABLE IF NOT EXISTS " +
            InstructorSchema.CommentEntry.TABLE_NAME + " (" +
            InstructorSchema.CommentEntry.COLUMN_NAME_INSTRUCTOR_ID + INT_TYPE + COMMA_SEP +
            InstructorSchema.CommentEntry.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
            InstructorSchema.CommentEntry.COLUMN_NAME_TEXT + TEXT_TYPE + " )";

    private static final String SQL_DELETE_COMMENT_ENTRIES = "DROP TABLE IF EXISTS " + InstructorSchema.CommentEntry.TABLE_NAME;

    public InstructorDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_INSTRUCTOR_ENTRIES);
        db.execSQL(SQL_CREATE_COMMENT_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_INSTRUCTOR_ENTRIES);
        db.execSQL(SQL_DELETE_COMMENT_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}