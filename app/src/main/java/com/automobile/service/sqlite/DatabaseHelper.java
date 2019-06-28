package com.automobile.service.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.automobile.service.util.Constans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Locale;

/****************************************************************************
 * @ClassdName:DatabaseHelper
 * @CreatedDate:
 * @ModifiedBy: not yet
 * @ModifiedDate: not yet
 * @purpose:This Class is use to Create database with Table and insert,update,delete Method with functionlity.
 ***************************************************************************/

public class DatabaseHelper extends SQLiteOpenHelper {
    private static Context mContext;
    private SQLiteDatabase DataBase;

    // DATABASE NAME
    private static String DATABASE_NAME = "DYRCT";
    private static String TABLE_NAME = "";

    // LIST TABLE NAME
    private String TABLE_NAME_CONTACTS = "contacts";


    public DatabaseHelper(Context context, String data_name, String tab_name, Hashtable<String, String> column_pairs) {

        super(context, data_name, null, Constans.SQLITE_VERSION);
        this.mContext = context;
        TABLE_NAME = tab_name;

    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, Constans.SQLITE_VERSION);
        //Log.d("Database", "====DatabaseHelper");
    }

    /**
     * Open Databases
     * *
     */
    public void openDataBase() throws SQLException {
        this.getWritableDatabase();
        //Log.d("Database", "====openDataBase");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DatabaseHelper", "====Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");

        if (oldVersion != newVersion) {
            try {


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        create_table_contacs(database);


    }


    // Create Table Conatcts
    public void create_table_contacs(SQLiteDatabase database) {
        try {

            Hashtable<String, String> tmp_contacts = new Hashtable<String, String>();
            tmp_contacts.put(ContactKey.ID, "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL");
            tmp_contacts.put(ContactKey.USER_ID, "TEXT");
            tmp_contacts.put(ContactKey.CONTACT_ID, "TEXT");
            tmp_contacts.put(ContactKey.FIRST_NAME, "TEXT");
            tmp_contacts.put(ContactKey.LAST_NAME, "TEXT");
            tmp_contacts.put(ContactKey.COMPANY_NAME, "TEXT");
            tmp_contacts.put(ContactKey.DESIGNATION, "TEXT");
            tmp_contacts.put(ContactKey.EMAIL, "TEXT");
            tmp_contacts.put(ContactKey.PHONE, "TEXT");
            tmp_contacts.put(ContactKey.URL, "TEXT");
            tmp_contacts.put(ContactKey.ADDRESS, "TEXT");
            tmp_contacts.put(ContactKey.ZIPCODE, "INTEGER");
            tmp_contacts.put(ContactKey.COUNTRY, "TEXT");
            tmp_contacts.put(ContactKey.STATE, "TEXT");
            tmp_contacts.put(ContactKey.CITY, "TEXT");
            tmp_contacts.put(ContactKey.LAT, "TEXT");
            tmp_contacts.put(ContactKey.LONG, "TEXT");
            tmp_contacts.put(ContactKey.CATEGORY, "TEXT");
            tmp_contacts.put(ContactKey.PROFILE_IMAGE, "TEXT");
            tmp_contacts.put(ContactKey.PROFILE_IMAGE_ID, "TEXT");
            tmp_contacts.put(ContactKey.IS_DYRCT_USER, "BOOLEAN");
            tmp_contacts.put(ContactKey.TIMESTAMP, "TEXT");
            tmp_contacts.put(ContactKey.NOTE, "TEXT");
            tmp_contacts.put(ContactKey.NOTE_PUBLIC, "TEXT");
            tmp_contacts.put(ContactKey.APP_VERSION, "TEXT");
            tmp_contacts.put(ContactKey.PROFILE_VISIBILTY_FLAG, "BOOLEAN");
            tmp_contacts.put(ContactKey.DEFAULT_ADDRESS, "TEXT");
            tmp_contacts.put(ContactKey.ALTERNATE_PHONE, "TEXT");

            createtable(database, TABLE_NAME_CONTACTS, tmp_contacts);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void TruncateAllTables() {

        deleteAll(TABLE_NAME_CONTACTS);


    }


    public void deleteAll(String TableName) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TableName, null, null);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public DatabaseHelper(Context context, String data_name, String tab_name) {
//        super(context, data_name, null, Constans.SQLITE_VERSION);
//        this.mContext = context;
//        TABLE_NAME = tab_name;
//
//    }

//    public DatabaseHelper(Context context, Hashtable<String, String> column_pairs, String data_name, String tab_name) {
//        super(context, data_name, null,Constans.SQLITE_VERSION);
//        this.mContext = context;
//        TABLE_NAME = tab_name;
//
//    }

//    public DatabaseHelper(Context context, String name, CursorFactory factory, int version, SQLiteDatabase dataBase) {
//        super(context, name, factory, version);
//        DataBase = dataBase;
//    }

    public void createtable(SQLiteDatabase database, String tableName, Hashtable<String, String> tmp1) {

        String CREATE_TABLE = "create table " + tableName + "(";

        for (String key : tmp1.keySet()) {
            CREATE_TABLE = CREATE_TABLE + key + " " + tmp1.get(key) + ",";
        }

        int len = CREATE_TABLE.length();
        CREATE_TABLE = CREATE_TABLE.substring(0, len - 1) + ")";
        database.execSQL(CREATE_TABLE);
    }

    public void dropCreatetable(SQLiteDatabase database, String tableName, Hashtable<String, String> tmp1) {

        database.execSQL("DROP TABLE IF EXISTS '" + tableName + "'");

        String CREATE_TABLE = "create table " + tableName + "(";
        for (String key : tmp1.keySet()) {
            CREATE_TABLE = CREATE_TABLE + key + " " + tmp1.get(key) + ",";
        }

        int len = CREATE_TABLE.length();
        CREATE_TABLE = CREATE_TABLE.substring(0, len - 1) + ")";

        database.execSQL(CREATE_TABLE);
    }


    public void insertRecord(Hashtable<String, String> queryValues, String TableName) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (String key : queryValues.keySet()) {
            values.put(key, queryValues.get(key));
        }

        database.insert(TableName, null, values);
        database.close();
    }


    public void insertUpdateRecord(Hashtable<String, String> queryValues, String tableName, String rowName, String Id) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = null;
        ContentValues values = new ContentValues();
        cursor = database.query(tableName, null, rowName + "=?", new String[]{Id}, null, null, null);

        database.beginTransaction();

        if (cursor.getCount() > 0) {

            for (String key : queryValues.keySet()) {

                values.put(key, queryValues.get(key));
            }
            database.update(tableName, values, rowName + "='" + Id + "'", null);

        } else {


            for (String key : queryValues.keySet()) {
                values.put(key, queryValues.get(key));
            }

            database.insert(tableName, null, values);
        }

        database.setTransactionSuccessful();
        database.endTransaction();
        cursor.close();
        database.close();
    }


    public void insertUpdateRecordFolderSubFolder(Hashtable<String, String> queryValues, String folderId, String keyValue, String tablename) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = null;
        ContentValues values = new ContentValues();
        cursor = database.query(tablename, null, keyValue + "=?", new String[]{folderId}, null, null, null);
        database.beginTransaction();

        if (cursor.getCount() > 0) {

            for (String key : queryValues.keySet()) {
                values.put(key, queryValues.get(key));
            }
            database.update(tablename, values, keyValue + "='" + folderId + "'", null);

        } else {


            for (String key : queryValues.keySet()) {
                values.put(key, queryValues.get(key));
            }

            database.insert(tablename, null, values);
        }

        database.setTransactionSuccessful();
        database.endTransaction();
        cursor.close();
        database.close();
    }


    public void deleteRecordWithIdsNotInSubFolder(String tableName, String rowName, String logIds, String childRowName, String childId) {


        logIds = logIds.replace("[", "");
        logIds = logIds.replace("]", "");

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = null;
        String query;


        if (logIds.equalsIgnoreCase("")) {
            query = "SELECT * FROM " + tableName;
        } else {
            query = "SELECT * FROM " + tableName + " WHERE " + rowName + " NOT IN(" + logIds + ")";
        }


        cursor = database.rawQuery(query, null);


        if (cursor.getCount() > 0) {

            if (cursor.moveToFirst()) {
                do {

                    String deleteId = cursor.getString(cursor.getColumnIndex(rowName));
                    deleteByTwoId(tableName, rowName, deleteId, childRowName, childId);
                    //deleteById(tableName, rowName, deleteId);
                } while (cursor.moveToNext());

            }
        }
        cursor.close();
        database.close();
    }


    public void deleteRecordWithIdsNotIn(String tableName, String rowName, String logIds, String childRowName, String childId) {


        logIds = logIds.replace("[", "");
        logIds = logIds.replace("]", "");

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = null;
        String query;


        if (logIds.equalsIgnoreCase("")) {
            query = "SELECT * FROM " + tableName;
        } else {
            query = "SELECT * FROM " + tableName + " WHERE " + rowName + " NOT IN(" + logIds + ")";
        }


        cursor = database.rawQuery(query, null);


        if (cursor.getCount() > 0) {

            if (cursor.moveToFirst()) {
                do {

                    String deleteId = cursor.getString(cursor.getColumnIndex(rowName));
                    deleteByTwoId(tableName, rowName, deleteId, childRowName, childId);
                } while (cursor.moveToNext());

            }
        }
        cursor.close();
        database.close();
    }


    public void insertUpdateRecordConatcts(Hashtable<String, String> queryValues, String phone) {

        try {

            SQLiteDatabase database = this.getWritableDatabase();
            Cursor cursor = null;
            ContentValues values = new ContentValues();
            cursor = database.query(TABLE_NAME_CONTACTS, null, ContactKey.PHONE + "=?", new String[]{phone}, null, null, null);

            database.beginTransaction();

            if (cursor.getCount() > 0) {


                for (String key : queryValues.keySet()) {
                    values.put(key, queryValues.get(key));
                }
                database.update(TABLE_NAME_CONTACTS, values, ContactKey.PHONE + "='" + phone + "'", null);

            } else {


                for (String key : queryValues.keySet()) {
                    values.put(key, queryValues.get(key));
                }

                database.insert(TABLE_NAME_CONTACTS, null, values);
            }

            database.setTransactionSuccessful();
            database.endTransaction();
            cursor.close();
            database.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String Column, String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_NAME, Column + "=?", new String[]{key});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public void deleteById(String tablename, String Column, String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(tablename, Column + "=?", new String[]{key});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }


    public void deleteByTwoId(String tablename, String ColumnGroup, String keyGroup, String ColumnChild, String keyChild) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {

            //db.delete(tablename, ColumnGroup + "=?", new String[]{keyGroup});
            db.delete(tablename, ColumnGroup + " = ? AND " + ColumnChild + " = ?", new String[]{keyGroup, keyChild + ""});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public void edit(Hashtable<String, String> queryValues, String Key, String value, String TABLE_NAME) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        for (String key : queryValues.keySet()) {
            values.put(key, queryValues.get(key));
        }
        database.update(TABLE_NAME, values, Key + "='" + value + "'", null);
    }


    public String firstCharUprt(String name) {
        String output = "";

        if (name != null && !name.isEmpty()) {
            output = name.substring(0, 1).toUpperCase() + name.substring(1);
        } else {
            output = name;
        }

        return output;
    }


    public String getTimestamp(String dateTime) {
        Date dateFrom = null;
        String dateTimeStamp = "";
        try {
            dateFrom = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calFrom = Calendar.getInstance();
        calFrom.setTime(dateFrom);
        GregorianCalendar calFromDate = new GregorianCalendar();
        calFromDate.set(calFrom.get(Calendar.YEAR), calFrom.get(Calendar.MONTH), calFrom.get(Calendar.DAY_OF_MONTH), calFrom.get(Calendar.HOUR), calFrom.get(Calendar.MINUTE), calFrom.get(Calendar.SECOND));
        dateTimeStamp = String.valueOf(calFromDate.getTimeInMillis() / 1000);
        return dateTimeStamp;

    }

    public String getTimestampTo(String dateTime) {
        Date dateFrom = null;
        String dateTimeStamp = "";
        try {
            dateFrom = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calFrom = Calendar.getInstance();
        calFrom.setTime(dateFrom);
        calFrom.add(calFrom.DATE, +1);
        GregorianCalendar calFromDate = new GregorianCalendar();
        calFromDate.set(calFrom.get(Calendar.YEAR), calFrom.get(Calendar.MONTH), calFrom.get(Calendar.DAY_OF_MONTH), calFrom.get(Calendar.HOUR), calFrom.get(Calendar.MINUTE), calFrom.get(Calendar.SECOND));
        dateTimeStamp = String.valueOf(calFromDate.getTimeInMillis() / 1000);
        return dateTimeStamp;

    }


    /**
     * This method close database connection and released occupied memory
     **/
    @Override
    public synchronized void close() {
        if (DataBase != null) {
            DataBase.close();
        }
        SQLiteDatabase.releaseMemory();
        super.close();
    }

}
