package edu.sdsu.cs.assignment3;

import android.provider.BaseColumns;

/**
 * Created by Horsie on 3/15/15.
 */
public final class InstructorSchema  {
    public InstructorSchema() {}

    public static abstract class InstructorEntry implements BaseColumns {
        public static final String TABLE_NAME = "instructors";
        public static final String COLUMN_NAME_INSTRUCTOR_ID = "instructorId";
        public static final String COLUMN_NAME_FIRST_NAME = "firstName";
        public static final String COLUMN_NAME_LAST_NAME = "lastName";
        public static final String COLUMN_NAME_OFFICE = "office";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_NAME_TOTAL_RATINGS = "totalRatings";
    }

    public static abstract class CommentEntry implements BaseColumns {
        public static final String TABLE_NAME = "comments";
        public static final String COLUMN_NAME_INSTRUCTOR_ID = "instructorId";
        public static final String COLUMN_NAME_TEXT = "text";
        public static final String COLUMN_NAME_DATE = "date";
    }
}
