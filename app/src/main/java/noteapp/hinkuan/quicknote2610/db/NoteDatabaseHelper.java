package noteapp.hinkuan.quicknote2610.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import noteapp.hinkuan.quicknote2610.QuickNoteApplication;
import noteapp.hinkuan.quicknote2610.config.QuickNote;
import noteapp.hinkuan.quicknote2610.entity.Note;
public class NoteDatabaseHelper {
    private static NoteDatabaseHelper sNoteDatabaseHelper;
    private final DatabaseHelper dbHelper;

    public static NoteDatabaseHelper getInstance() {
        if (sNoteDatabaseHelper == null) {
            synchronized (NoteDatabaseHelper.class) {
                if (sNoteDatabaseHelper == null) {
                    sNoteDatabaseHelper = new NoteDatabaseHelper(QuickNote.getAppContext());
                }
            }
        }
        return sNoteDatabaseHelper;
    }

    private NoteDatabaseHelper(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void insert(Note data) {
        insert(DatabaseHelper.ALL_NOTE_TABLE_NAME, data);
    }

    public void delete(int id) {
        delete(DatabaseHelper.ALL_NOTE_TABLE_NAME, id);
    }

    public void remove(int id) {
        remove(DatabaseHelper.ALL_NOTE_TABLE_NAME, id);
    }

    public void update(Note data) {
        update(DatabaseHelper.ALL_NOTE_TABLE_NAME, data);
    }

    public void recovery(int id) {
        recovery(DatabaseHelper.ALL_NOTE_TABLE_NAME, id);
    }

    public List<Note> query(String where) {
        return query(DatabaseHelper.ALL_NOTE_TABLE_NAME, where);
    }

    public void save(Note data) {
        save(DatabaseHelper.ALL_NOTE_TABLE_NAME, data);
    }

    public List<Note> getAllActiveNotes() {
        return query(" where iswasted=0");
    }

    public List<Note> getAllWastedNotes() {
        return query(" where iswasted=1");
    }

    public List<Note> getRecentlyNotes(int num) {
        return order(DatabaseHelper.ALL_NOTE_TABLE_NAME, " where iswasted=0", "lastmodify", false, num);
    }

    public List<Note> getAllNotes() {
        return query(" ");
    }

    public List<Note> getNotesByPattern(String pattern) {
        String whereCondition = " where iswasted=0 and title like '%" + pattern + "%' or " +
                "iswasted=0 and content like '%" + pattern + "%'";
        return query(whereCondition);
    }

    public void insert(String table, Note data) {
        String sql = "insert into " + table;
        sql += "(_id, title, content, date, address, timestamp,lastmodify, iswasted) values(null, ?,?, ?, ?, ?, ?, ?)";
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        sqlite.execSQL(sql, new String[]{data.getTitle(), data.getContent(), data.getDate(),
                data.getAddress(), data.getTimestamp() + "", data.getLastModify() + "", data.getIsWasted() + ""});
        sqlite.close();
    }

    public void delete(String table, int id) {
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql = ("delete from " + table + " where _id=?");
        sqlite.execSQL(sql, new Integer[]{id});
        sqlite.close();
    }

    public void remove(String table, int id) {
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql = ("update " + table + " set iswasted=1 where _id=?");
        sqlite.execSQL(sql, new Integer[]{id});
        sqlite.close();
    }

    public void update(String table, Note data) {
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql = ("update " + table +
                " set title=?, content=?, date=?, address=?, timestamp=?, lastmodify=?, iswasted=? where _id=?");
        sqlite.execSQL(sql,
                new String[]{data.getTitle(), data.getContent(), data.getDate(), data.getAddress(),
                        data.getTimestamp() + "", data.getLastModify() + "", data.getIsWasted() + "", data.getId() + ""});
        sqlite.close();
    }

    public void recovery(String table, int id) {
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql = ("update " + table + " set iswasted=0 where _id=?");
        sqlite.execSQL(sql, new Integer[]{id});
        sqlite.close();
    }


    public List<Note> query(String table, String where) {
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
        ArrayList<Note> data = new ArrayList<>();
        Cursor cursor = sqlite.rawQuery("select * from " + table + where, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Note Note = new Note();
            Note.setId(cursor.getInt(0));
            Note.setTitle(cursor.getString(1));
            Note.setContent(cursor.getString(2));
            Note.setDate(cursor.getString(3));
            Note.setAddress(cursor.getString(4));
            Note.setTimestamp(cursor.getLong(5));
            Note.setLastModify(cursor.getLong(6));
            Note.setWasted(cursor.getInt(7));
            data.add(Note);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();
        return data;
    }


    public List<Note> order(String table, String where, String fieldName, boolean isAscending, int nums) {
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql;
        if (isAscending) {
            sql = ("select * from " + table + where + " order by " + fieldName + " asc limit 0," + nums);
        } else {
            sql = ("select * from " + table + where + " order by " + fieldName + " desc limit 0," + nums);
        }
        ArrayList<Note> data = new ArrayList<>();
        Cursor cursor = sqlite.rawQuery(sql, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Note Note = new Note();
            Note.setId(cursor.getInt(0));
            Note.setTitle(cursor.getString(1));
            Note.setContent(cursor.getString(2));
            Note.setDate(cursor.getString(3));
            Note.setAddress(cursor.getString(4));
            Note.setTimestamp(cursor.getLong(5));
            Note.setLastModify(cursor.getLong(6));
            Note.setWasted(cursor.getInt(7));
            data.add(Note);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();
        return data;
    }

    public void reset(List<Note> datas) {
        if (datas != null) {
            SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
            sqlite.execSQL("delete from " + DatabaseHelper.ALL_NOTE_TABLE_NAME);
            for (Note data : datas) {
                insert(data);
            }
            sqlite.close();
        }
    }

    public void save(String table, Note data) {
        List<Note> datas = query(table, " where _id=" + data.getId());
        if (datas != null && !datas.isEmpty()) {
            update(data);
        } else {
            insert(data);
        }
    }


    public void close() {
        dbHelper.close();
    }

}
