package a2dv606_aa223de.assignment3.Call_History;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * reference
 * http://www.vogella.com/tutorials/AndroidSQLite/article.html
 */

public class CallHistoryDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_COMMENT };

    public CallHistoryDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Call createCall(String call) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_COMMENT, call);
        long insertId = database.insert(MySQLiteHelper.TABLE_COMMENTS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Call newCall = cursorToCall(cursor);
        cursor.close();
        return newCall;
    }

    public void deleteCall(Call call) {
        long id = call.getId();
        System.out.println("Call deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Call> getAllCalls() {
        List<Call> calls = new ArrayList<Call>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Call comment = cursorToCall(cursor);
            calls.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return calls;
    }

    private Call cursorToCall(Cursor cursor) {
        Call call = new Call();
        call.setId(cursor.getLong(0));
        call.setCall(cursor.getString(1));
        return call;
    }
}