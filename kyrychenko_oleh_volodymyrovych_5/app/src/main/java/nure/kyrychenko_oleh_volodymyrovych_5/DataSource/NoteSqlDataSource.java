package nure.kyrychenko_oleh_volodymyrovych_5.DataSource;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Date;

import nure.kyrychenko_oleh_volodymyrovych_5.Listeners.OnAsyncNoteQueryDoneListener;
import nure.kyrychenko_oleh_volodymyrovych_5.Model.AsyncNoteQueryInfo;
import nure.kyrychenko_oleh_volodymyrovych_5.Model.AsyncQueryType;
import nure.kyrychenko_oleh_volodymyrovych_5.Model.FilterSettings;
import nure.kyrychenko_oleh_volodymyrovych_5.Model.INote;
import nure.kyrychenko_oleh_volodymyrovych_5.Model.Importance;
import nure.kyrychenko_oleh_volodymyrovych_5.Model.Note;
import nure.kyrychenko_oleh_volodymyrovych_5.Util.Util;

public class NoteSqlDataSource implements ISyncNoteDataSource, IAsyncNoteDataSource {

    private NoteSqlHelper sqlHelper;

    public NoteSqlDataSource(NoteSqlHelper helper) {
        sqlHelper = helper;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public INote[] getAllNotes(FilterSettings filters) {
        SQLiteDatabase db = sqlHelper.getReadableDatabase();

        String where = getWhere(filters);
        Cursor cursor = db.rawQuery(
                "SELECT " + String.join(", ", NoteSqlHelper.COLUMNS) +
                    " FROM " + NoteSqlHelper.NOTES_TABLE_NAME + " " + where + ";",
                null
        );

        INote[] notes = new INote[cursor.getCount()];

        for(int i = 0; cursor.moveToNext(); ++i) {
            notes[i] = readNote(cursor);
        }

        cursor.close();

        return notes;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public INote getById(String id) {
        SQLiteDatabase db = sqlHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT " + String.join(", ", NoteSqlHelper.COLUMNS) +
                    " FROM " + NoteSqlHelper.NOTES_TABLE_NAME +
                    " WHERE " + NoteSqlHelper.NOTE_ID_COLUMN_NAME + " = '" + id + "';",
                null
        );

        INote note = null;

        if(cursor.getCount() != 0) {
            cursor.moveToNext();
            note = readNote(cursor);
        }

        cursor.close();

        return note;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getWhere(FilterSettings settings) {
        if(settings == null) {
            return "";
        }

        String contentFilter = settings.getContentFilter();
        Importance[] importanceFilter = settings.getImportanceFilter();

        String contentWhere = contentFilter == null || contentFilter.isEmpty() ? "" :
                "INSTR(" + NoteSqlHelper.NOTE_TITLE_COLUMN_NAME + ", '" + contentFilter + "') OR " +
                "INSTR(" + NoteSqlHelper.NOTE_DESCRIPTION_COLUMN_NAME + ", '" + contentFilter + "')";

        String[] importanceWhereItems = new String[importanceFilter.length + 1];
        for(int i = 0; i < importanceFilter.length; ++i) {
            importanceWhereItems[i] = NoteSqlHelper.NOTE_IMPORTANCE_COLUMN_NAME + " = " + Integer.toString(
                    Util.importanceToInt(importanceFilter[i])
            );
        }
        importanceWhereItems[importanceWhereItems.length - 1] = NoteSqlHelper.NOTE_IMPORTANCE_COLUMN_NAME + " = -1";

        ArrayList<String> wheres = new ArrayList<>();

        if(!contentWhere.isEmpty()) {
            wheres.add("(" + contentWhere + ")");
        }
        if(importanceWhereItems.length != 0) {
            String importanceWhere = String.join(
                    " OR ", importanceWhereItems
            );
            wheres.add("(" + importanceWhere + ")");
        }

        return wheres.size() == 0 ? "" : "WHERE " + String.join(" AND ", wheres);
    }

    private INote readNote(Cursor cursor) {
        Note note = new Note();

        note.setId(cursor.getString(cursor.getColumnIndex(NoteSqlHelper.NOTE_ID_COLUMN_NAME)));
        note.setTitle(cursor.getString(cursor.getColumnIndex(NoteSqlHelper.NOTE_TITLE_COLUMN_NAME)));
        note.setDescription(cursor.getString(cursor.getColumnIndex(NoteSqlHelper.NOTE_DESCRIPTION_COLUMN_NAME)));

        Date destDate = Util.ParseDateTime(
                cursor.getString(cursor.getColumnIndex(NoteSqlHelper.NOTE_DESTINATION_DATE_TIME_COLUMN_NAME))
        );
        note.setDestinationDate(destDate);
        note.setDestinationTime(destDate);

        note.setCreationDateTime(
                Util.ParseDateTime(
                        cursor.getString(cursor.getColumnIndex(NoteSqlHelper.NOTE_CREATION_DATE_TIME_COLUMN_NAME))
                )
        );
        note.setImportance(
                Util.intToImportance(cursor.getInt(cursor.getColumnIndex(NoteSqlHelper.NOTE_IMPORTANCE_COLUMN_NAME)))
        );
        note.setImageUri(cursor.getString(cursor.getColumnIndex(NoteSqlHelper.NOTE_IMAGE_PATH_URI)));

        return note;
    }

    private void putNoteData(ContentValues content, INote note) {
        content.put(NoteSqlHelper.NOTE_TITLE_COLUMN_NAME, note.getTitle());
        content.put(NoteSqlHelper.NOTE_DESCRIPTION_COLUMN_NAME, note.getDescription());
        content.put(
                NoteSqlHelper.NOTE_DESTINATION_DATE_TIME_COLUMN_NAME,
                Util.FormatDateTimeFormDb(
                        note.getDestinationDate(),
                        note.getDestinationTime()
                )
        );
        content.put(
                NoteSqlHelper.NOTE_CREATION_DATE_TIME_COLUMN_NAME,
                Util.FormatDate(note.getCreationDateTime())
        );

        int imp = Util.importanceToInt(note.getImportance());
        content.put(NoteSqlHelper.NOTE_IMPORTANCE_COLUMN_NAME, imp);
        content.put(NoteSqlHelper.NOTE_IMAGE_PATH_URI, note.getImageUri());
    }

    @Override
    public void addNote(INote note) {
        SQLiteDatabase db = sqlHelper.getWritableDatabase();

        ContentValues content = new ContentValues();

        content.put(NoteSqlHelper.NOTE_ID_COLUMN_NAME, note.getId());
        putNoteData(content, note);

        db.insert(NoteSqlHelper.NOTES_TABLE_NAME, null, content);
    }

    @Override
    public void updateNote(INote note) {
        SQLiteDatabase db = sqlHelper.getWritableDatabase();

        ContentValues content = new ContentValues();
        putNoteData(content, note);

        String where = NoteSqlHelper.NOTE_ID_COLUMN_NAME + " = '" + note.getId() + "'";
        db.update(NoteSqlHelper.NOTES_TABLE_NAME, content, where, null);
    }

    @Override
    public void deleteNote(INote note) {
        SQLiteDatabase db = sqlHelper.getWritableDatabase();

        String where = NoteSqlHelper.NOTE_ID_COLUMN_NAME + " = '" + note.getId() + "'";
        db.delete(NoteSqlHelper.NOTES_TABLE_NAME, where, null);
    }

    private void executeAsyncQuery(AsyncNoteQueryInfo info, OnAsyncNoteQueryDoneListener listener)  {
        AsyncNoteRepoTask task = new AsyncNoteRepoTask(this, listener);
        task.execute(info);
    }

    @Override
    public void getAllNotesAsync(FilterSettings filters, OnAsyncNoteQueryDoneListener listener) {
        AsyncNoteQueryInfo queryInfo = new AsyncNoteQueryInfo();
        queryInfo.setQueryType(AsyncQueryType.GET);
        queryInfo.setFilters(filters);

        executeAsyncQuery(queryInfo, listener);
    }

    @Override
    public void getByIdAsync(String id, OnAsyncNoteQueryDoneListener listener) {
        AsyncNoteQueryInfo queryInfo = new AsyncNoteQueryInfo();
        queryInfo.setQueryType(AsyncQueryType.GET_BY_ID);
        queryInfo.setGetId(id);

        executeAsyncQuery(queryInfo, listener);
    }
}
