package hk.com.a4gtsstask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "NotesManager";

    // Contacts table name
    private static final String TABLE_NOTES = "notes";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_BODY = "body";
    private static final String KEY_TYPE = "type";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_BODY+ " TEXT,"
                + KEY_TYPE + " TEXT"
                + ")";
        Log.v("create", CREATE_NOTES_TABLE);
        sqLiteDatabase.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    // Adding new contact
    public void addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_BODY, note.getBody());
        values.put(KEY_TYPE, "text");

        // Inserting Row
        int id = (int) db.insert(TABLE_NOTES, null, values);

        db.close(); // Closing database connection
        note.setId(id);

    }

    // Getting single note
    public Note getNote(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTES, new String[] { KEY_ID, KEY_TITLE, KEY_BODY, KEY_TYPE}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Note note = new Note(cursor.getString(1), cursor.getString(2));


        return note;
    }

    // Getting notes Count
    public int getNotesCount() {
        SQLiteDatabase db = this.getReadableDatabase();

        return (int) DatabaseUtils.queryNumEntries(db, TABLE_NOTES);
    }

    // Updating single note
    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_BODY, note.getBody());
        values.put(KEY_TYPE, "text");

        // updating row
        return db.update(TABLE_NOTES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(note.getId()) });
    }

    // Deleting single note
    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, KEY_ID + " = ?"
                , new String[] { String.valueOf(note.getId()) });

        db.close();
    }


    //Getting all Notes
    public ArrayList<Note> getAllNotes(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM notes",null);
        ArrayList<Note> notes = new ArrayList<>();

        if (cursor != null&&cursor.getCount()!=0) {
            cursor.moveToFirst();

            do {
                notes.add(new Note(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
            } while (cursor.moveToNext());
        }
        return notes;
    }

    public void deleteAllNotes()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES,null,null);
    }
}
