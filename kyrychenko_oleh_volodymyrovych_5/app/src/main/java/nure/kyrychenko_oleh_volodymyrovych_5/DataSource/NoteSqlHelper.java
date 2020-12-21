package nure.kyrychenko_oleh_volodymyrovych_5.DataSource;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class NoteSqlHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 2;
    public static final String DB_NAME = "NotesDb";

    public static final String NOTES_TABLE_NAME = "notes";

    public static final String NOTE_ID_COLUMN_NAME = "_id";
    public static final String NOTE_TITLE_COLUMN_NAME = "title";
    public static final String NOTE_DESCRIPTION_COLUMN_NAME = "description";
    public static final String NOTE_DESTINATION_DATE_TIME_COLUMN_NAME = "destination_datetime";
    public static final String NOTE_CREATION_DATE_TIME_COLUMN_NAME = "creation_datetime";
    public static final String NOTE_IMPORTANCE_COLUMN_NAME = "importance";
    public static final String NOTE_IMAGE_PATH_URI = "image";

    public static final String[] COLUMNS = new String[] {
            NOTE_ID_COLUMN_NAME,
            NOTE_TITLE_COLUMN_NAME,
            NOTE_DESCRIPTION_COLUMN_NAME,
            NOTE_DESTINATION_DATE_TIME_COLUMN_NAME,
            NOTE_CREATION_DATE_TIME_COLUMN_NAME,
            NOTE_IMPORTANCE_COLUMN_NAME,
            NOTE_IMAGE_PATH_URI
    };

    public NoteSqlHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private void createTable(SQLiteDatabase db, String tableName, boolean isTemp) {
        db.execSQL(
                "CREATE " + (isTemp ? "TEMPORARY TABLE " + tableName + "_tmp" : "TABLE " + tableName) + "(" +
                        NOTE_ID_COLUMN_NAME + " TEXT PRIMARY KEY, " +
                        NOTE_TITLE_COLUMN_NAME + " TEXT, " +
                        NOTE_DESCRIPTION_COLUMN_NAME + " TEXT, " +
                        NOTE_DESTINATION_DATE_TIME_COLUMN_NAME + " TEXT, " +
                        NOTE_CREATION_DATE_TIME_COLUMN_NAME + " TEXT, " +
                        NOTE_IMPORTANCE_COLUMN_NAME + " INTEGER NOT NULL, " +
                        NOTE_IMAGE_PATH_URI + " TEXT" +
                        ");"
        );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db, NOTES_TABLE_NAME, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.beginTransaction();

        try {
            createTable(db, NOTES_TABLE_NAME, true);
            db.execSQL(
                    "INSERT INTO " + NOTES_TABLE_NAME + "_tmp" +
                            " SELECT " + String.join(", ", COLUMNS) +
                            " FROM " + NOTES_TABLE_NAME
            );
            db.execSQL("DROP TABLE " + NOTES_TABLE_NAME);

            createTable(db, NOTES_TABLE_NAME, false);
            db.execSQL(
                    "INSERT INTO " + NOTES_TABLE_NAME +
                            " SELECT " + String.join(", ", COLUMNS) +
                            " FROM " + NOTES_TABLE_NAME + "_tmp;"
            );

            db.execSQL("DROP TABLE " + NOTES_TABLE_NAME + "_tmp;");
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
