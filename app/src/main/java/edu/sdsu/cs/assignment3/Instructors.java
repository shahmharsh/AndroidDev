package edu.sdsu.cs.assignment3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Horsie on 3/10/15.
 */
public class Instructors {
    private static Instructors mInstance = null;
    private ArrayList<Instructor> mInstructors;
    private InstructorDbHelper mDbHelper;

    private Instructors(Context pContext) {
        mInstructors = new ArrayList<>();
        mDbHelper = new InstructorDbHelper(pContext);
    }

    public static synchronized Instructors getInstance(Context pContext) {
        if (mInstance == null) {
            mInstance = new Instructors(pContext);
        }
        return mInstance;
    }

    public boolean add(Instructor instructor) {
        return mInstructors.add(instructor);
    }

    public Instructor[] getAllInstructors() {
        Instructor[] instructorArray = new Instructor[mInstructors.size()];
        mInstructors.toArray(instructorArray);
        return instructorArray;
    }

    public void updateAllInstructorsInDB() {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.delete(InstructorSchema.InstructorEntry.TABLE_NAME, null, null);
        ContentValues values = new ContentValues();

        for (Instructor instructor : mInstructors) {
            values.clear();
            values.put(InstructorSchema.InstructorEntry.COLUMN_NAME_INSTRUCTOR_ID,instructor.getId());
            values.put(InstructorSchema.InstructorEntry.COLUMN_NAME_FIRST_NAME,instructor.getFirstName());
            values.put(InstructorSchema.InstructorEntry.COLUMN_NAME_LAST_NAME,instructor.getLastName());

            db.insert(InstructorSchema.InstructorEntry.TABLE_NAME, null, values);
        }
    }

    public void getInstructorsFromDB() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                InstructorSchema.InstructorEntry.COLUMN_NAME_INSTRUCTOR_ID,
                InstructorSchema.InstructorEntry.COLUMN_NAME_FIRST_NAME,
                InstructorSchema.InstructorEntry.COLUMN_NAME_LAST_NAME,
                InstructorSchema.InstructorEntry.COLUMN_NAME_EMAIL
        };

        String sortOrder = InstructorSchema.InstructorEntry.COLUMN_NAME_INSTRUCTOR_ID + " ASC";

        Cursor cursor = db.query(InstructorSchema.InstructorEntry.TABLE_NAME, projection, null, null, null, null, sortOrder);

        int columnFirstNameIndex = cursor.getColumnIndex(InstructorSchema.InstructorEntry.COLUMN_NAME_FIRST_NAME);
        int columnLastNameIndex = cursor.getColumnIndex(InstructorSchema.InstructorEntry.COLUMN_NAME_LAST_NAME);
        int columnInstructorIdIndex = cursor.getColumnIndex(InstructorSchema.InstructorEntry.COLUMN_NAME_INSTRUCTOR_ID);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String firstName = cursor.getString(columnFirstNameIndex);
            String lastName = cursor.getString(columnLastNameIndex);
            int id = cursor.getInt(columnInstructorIdIndex);
            Instructor instructor = new Instructor(id, firstName, lastName);
            this.add(instructor);
            cursor.moveToNext();
        }
    }
}
