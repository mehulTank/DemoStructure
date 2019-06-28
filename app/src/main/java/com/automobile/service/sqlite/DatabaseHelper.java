package com.dyrct.app.sqlite.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.dyrct.app.DyrctApplication;
import com.dyrct.app.R;
import com.dyrct.app.model.BlockUserModel;
import com.dyrct.app.model.CategoryModel;
import com.dyrct.app.model.DeviceModel;
import com.dyrct.app.model.FolderContacsPoiModel;
import com.dyrct.app.model.FolderModel;
import com.dyrct.app.model.GLCContactsModel;
import com.dyrct.app.model.GroupHistoryModel;
import com.dyrct.app.model.GroupMemberModel;
import com.dyrct.app.model.GroupModel;
import com.dyrct.app.model.HistoryModel;
import com.dyrct.app.model.IceUserModel;
import com.dyrct.app.model.ItemDetail;
import com.dyrct.app.model.LogBookModel;
import com.dyrct.app.model.NotificationModel;
import com.dyrct.app.model.PoiModel;
import com.dyrct.app.model.PublicPrivateAddressModel;
import com.dyrct.app.model.SubFolderCidPoiModel;
import com.dyrct.app.util.Constans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

/****************************************************************************
 * @ClassdName:DatabaseHelper
 * @CreatedDate:
 * @ModifiedBy: not yet
 * @ModifiedDate: not yet
 * @purpose:This Class is use to Create database with Table and insert,update,delete Method with functionlity.
 ***************************************************************************/

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static Context mContext;
    private SQLiteDatabase DataBase;

    // DATABASE NAME
    private static String DATABASE_NAME = "DYRCT";
    private static String TABLE_NAME = "";

    // LIST TABLE NAME
    private String TABLE_NAME_MYGLC = "myglc";
    private String TABLE_NAME_MYGLC_PUBLIC_PRIVATE_ADD = "myglc_public_private_address";
    private String TABLE_NAME_CONTACTS_PUBLIC_PRIVATE_ADD = "contacts_public_private_address";
    private String TABLE_NAME_CONTACTS = "contacts";
    private String TABLE_NAME_POI = "poi";
    private String TABLE_NAME_HISTORY = "history";
    private String TABLE_NAME_GROUP = "groups";
    private String TABLE_NAME_GROUP_HISTORY = "groups_history";
    private String TABLE_NAME_GROUP_MEMBER = "groups_member";
    private String TABLE_NAME_BLOCK_USER = "blockuser";
    private String TABLE_NAME_ME_BLOCK_USER = "Meblockuser";
    private String TABLE_NAME_PREFRED_DEVICE = "prefred_device";
    private String TABLE_NAME_CATEGORY = "category";
    private String TABLE_NAME_LOG_BOOK = "log_book";
    private String TABLE_NAME_FOLDER = "folder";
    private String TABLE_NAME_SUB_FOLDER = "sub_folder";
    private String TABLE_NAME_FOLDER_CONTACTS_POI = "folder_contacts_poi";
    private String TABLE_NAME_NOTIFICATION = "notification";


    private ArrayList<GLCContactsModel> modelGlcContactModels;
    private ArrayList<PoiModel> modelPoi;
    private ArrayList<CategoryModel> modelCategory;
    private ArrayList<LogBookModel> modelLogBook;
    private ArrayList<GroupModel> modelGroup;
    private ArrayList<GroupHistoryModel> modelGroupHistory;
    private ArrayList<PublicPrivateAddressModel> modelPublicPrivateList;
    private ArrayList<HistoryModel> modelHistory;
    private ArrayList<BlockUserModel> modelBlockUserList;
    private ArrayList<DeviceModel> modelDeviceList;
    private ArrayList<GroupMemberModel> groupMemberModelList;
    private ArrayList<FolderModel> modelFolder;
    private ArrayList<SubFolderCidPoiModel> modelSubFolderCidPoi;
    private ArrayList<FolderContacsPoiModel> modelFoldersCidPoi;


    public DatabaseHelper(Context context, String data_name, String tab_name, Hashtable<String, String> column_pairs) {

        super(context, data_name, null, Constans.SQLITE_VERSION);
        this.mContext = context;
        TABLE_NAME = tab_name;

    }

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, Constans.SQLITE_VERSION);
        //Log.d("Database", "====DatabaseHelper");
    }

    /**
     * Open Databases
     * *
     */
    public void openDataBase() throws SQLException
    {
        this.getWritableDatabase();
        //Log.d("Database", "====openDataBase");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.d("DatabaseHelper", "====Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");

        if (oldVersion != newVersion)
        {
            try {

                DyrctApplication.getmInstance().savePreferenceDataBoolean("isLogin", false);
                DyrctApplication.getmInstance().clearePreferenceData();

                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MYGLC);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CONTACTS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MYGLC_PUBLIC_PRIVATE_ADD);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CONTACTS_PUBLIC_PRIVATE_ADD);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_POI);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_HISTORY);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_GROUP);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_BLOCK_USER);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ME_BLOCK_USER);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_GROUP_HISTORY);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CATEGORY);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LOG_BOOK);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FOLDER);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FOLDER_CONTACTS_POI);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SUB_FOLDER);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NOTIFICATION);

                //DyrctApplication.getmInstance().savePreferenceDataBoolean(mContext.getString((R.string.IsTotorial)), true);
                DyrctApplication.getmInstance().savePreferenceDataBoolean("IsDisclaimer", true);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        create_table_myGlc(database);
        create_table_myGlc_public_private(database);
        create_table_contacs(database);
        create_table_contacts_public_private(database);
        create_table_group(database);
        create_table_poi(database);
        create_table_history(database);
        create_table_blockUser(database);
        create_table_me_blockUser(database);
        create_table_group_member(database);
        create_table_groupHistory(database);
        create_table_prefredDevice(database);
        create_table_category(database);
        create_table_logbook(database);
        create_table_folder(database);
        create_table_sub_folder(database);
        create_table_folder_contacts_poi(database);
        create_table_notification(database);
    }

    // Create Table MyGlc
    public void create_table_myGlc(SQLiteDatabase database) {
        try {

            Hashtable<String, String> tmp_poeple = new Hashtable<String, String>();
            tmp_poeple.put(ContactKey.ID, "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL");
            tmp_poeple.put(ContactKey.FIRST_NAME, "TEXT");
            tmp_poeple.put(ContactKey.LAST_NAME, "TEXT");
            tmp_poeple.put(ContactKey.COMPANY_NAME, "TEXT");
            tmp_poeple.put(ContactKey.DESIGNATION, "TEXT");
            tmp_poeple.put(ContactKey.EMAIL, "TEXT");
            tmp_poeple.put(ContactKey.PHONE, "TEXT");
            tmp_poeple.put(ContactKey.URL, "TEXT");
            tmp_poeple.put(ContactKey.ADDRESS, "TEXT");
            tmp_poeple.put(ContactKey.ZIPCODE, "INTEGER");
            tmp_poeple.put(ContactKey.COUNTRY, "TEXT");
            tmp_poeple.put(ContactKey.STATE, "TEXT");
            tmp_poeple.put(ContactKey.CITY, "TEXT");
            tmp_poeple.put(ContactKey.LAT, "TEXT");
            tmp_poeple.put(ContactKey.LONG, "TEXT");
            tmp_poeple.put(ContactKey.CATEGORY, "TEXT");
            tmp_poeple.put(ContactKey.PROFILE_IMAGE, "TEXT");
            tmp_poeple.put(ContactKey.PROFILE_IMAGE_ID, "TEXT");
            tmp_poeple.put(ContactKey.TIMESTAMP, "TEXT");
            tmp_poeple.put(ContactKey.DEFAULT_ADDRESS, "TEXT");
            tmp_poeple.put(ContactKey.PROFILE_VISIBILTY_FLAG, "TEXT");
            tmp_poeple.put(ContactKey.ALTERNATE_PHONE, "TEXT");

            createtable(database, TABLE_NAME_MYGLC, tmp_poeple);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Create Table MyGlc public private
    public void create_table_myGlc_public_private(SQLiteDatabase database) {
        try {

            Hashtable<String, String> tmp_myglc = new Hashtable<String, String>();
            tmp_myglc.put(ContactKey.ID, "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL");
            tmp_myglc.put(ContactKey.USER_ID, "TEXT");
            tmp_myglc.put(ContactKey.ADDRESS_ID, "TEXT");
            tmp_myglc.put(ContactKey.COMPANY_NAME, "TEXT");
            tmp_myglc.put(ContactKey.EMAIL, "TEXT");
            tmp_myglc.put(ContactKey.PHONE, "TEXT");
            tmp_myglc.put(ContactKey.ADDRESS, "TEXT");
            tmp_myglc.put(ContactKey.COUNTRY, "TEXT");
            tmp_myglc.put(ContactKey.STATE, "TEXT");
            tmp_myglc.put(ContactKey.CITY, "TEXT");
            tmp_myglc.put(ContactKey.LAT, "TEXT");
            tmp_myglc.put(ContactKey.LONG, "TEXT");
            tmp_myglc.put(ContactKey.TYPE_ADDRESS, "TEXT");
            createtable(database, TABLE_NAME_MYGLC_PUBLIC_PRIVATE_ADD, tmp_myglc);

        } catch (Exception e) {
            e.printStackTrace();
        }
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


    // Create Table Contacts public private
    public void create_table_contacts_public_private(SQLiteDatabase database) {
        try {

            Hashtable<String, String> tmp_contacts = new Hashtable<String, String>();
            tmp_contacts.put(ContactKey.ID, "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL");
            tmp_contacts.put(ContactKey.USER_ID, "TEXT");
            tmp_contacts.put(ContactKey.ADDRESS_ID, "TEXT");
            tmp_contacts.put(ContactKey.COMPANY_NAME, "TEXT");
            tmp_contacts.put(ContactKey.EMAIL, "TEXT");
            tmp_contacts.put(ContactKey.PHONE, "TEXT");
            tmp_contacts.put(ContactKey.ADDRESS, "TEXT");
            tmp_contacts.put(ContactKey.COUNTRY, "TEXT");
            tmp_contacts.put(ContactKey.STATE, "TEXT");
            tmp_contacts.put(ContactKey.CITY, "TEXT");
            tmp_contacts.put(ContactKey.LAT, "TEXT");
            tmp_contacts.put(ContactKey.LONG, "TEXT");
            tmp_contacts.put(ContactKey.TYPE_ADDRESS, "TEXT");
            tmp_contacts.put(ContactKey.ZIPCODE, "TEXT");


            createtable(database, TABLE_NAME_CONTACTS_PUBLIC_PRIVATE_ADD, tmp_contacts);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Create Table POI
    public void create_table_poi(SQLiteDatabase database) {
        try {

            Hashtable<String, String> tmp_poi = new Hashtable<String, String>();
            tmp_poi.put(PoiKey.ID, "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL");
            tmp_poi.put(PoiKey.POI_ID, "TEXT");
            tmp_poi.put(PoiKey.TITLE, "TEXT");
            tmp_poi.put(PoiKey.ADDRESS, "TEXT");
            tmp_poi.put(PoiKey.LAT, "TEXT");
            tmp_poi.put(PoiKey.LONG, "TEXT");
            tmp_poi.put(PoiKey.NOTE, "TEXT");
            tmp_poi.put(PoiKey.NOTE, "TEXT");
            tmp_poi.put(PoiKey.NOTE_PUBLIC, "TEXT");
            tmp_poi.put(PoiKey.POI_TYPE, "TEXT");
            tmp_poi.put(PoiKey.PHONE, "TEXT");
            tmp_poi.put(PoiKey.IMAGE, "TEXT");
            tmp_poi.put(PoiKey.URL, "TEXT");
            tmp_poi.put(PoiKey.ICE_FLAG, "TEXT");
            tmp_poi.put(PoiKey.ICE_MESSAGE, "TEXT");
            tmp_poi.put(PoiKey.CREATED_DATE, "TEXT");
            createtable(database, TABLE_NAME_POI, tmp_poi);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Create Table History
    public void create_table_history(SQLiteDatabase database) {
        try {

            Hashtable<String, String> tmp_history = new Hashtable<String, String>();
            tmp_history.put(HistoryKey.ID_HISTORY, "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL");
            tmp_history.put(HistoryKey.TRIP_ID, "TEXT");
            tmp_history.put(HistoryKey.TYPE_ID, "TEXT");
            tmp_history.put(HistoryKey.TYPE, "TEXT");
            tmp_history.put(HistoryKey.NAME, "TEXT");
            tmp_history.put(HistoryKey.ADDRESS, "TEXT");
            tmp_history.put(HistoryKey.LAT, "TEXT");
            tmp_history.put(HistoryKey.IMAGE, "TEXT");
            tmp_history.put(HistoryKey.LONGI, "TEXT");
            tmp_history.put(HistoryKey.DATE_TIME, "TEXT");

            createtable(database, TABLE_NAME_HISTORY, tmp_history);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Create Table GROUP
    public void create_table_group(SQLiteDatabase database) {
        try {

            Hashtable<String, String> tmp_group = new Hashtable<String, String>();
            tmp_group.put(GroupKey.ID, "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL");
            tmp_group.put(GroupKey.GROUP_ID, "TEXT");
            tmp_group.put(GroupKey.GROUP_NAME, "TEXT");
            tmp_group.put(GroupKey.CREATED_BY_ID, "TEXT");
            tmp_group.put(GroupKey.CREATED_DATE, "TEXT");
            tmp_group.put(GroupKey.MODIFIED_DATE, "TEXT");
            tmp_group.put(GroupKey.TOTAL_NUMBER, "TEXT");
            tmp_group.put(GroupKey.ISCREATEDBYEUSER, "TEXT");
            createtable(database, TABLE_NAME_GROUP, tmp_group);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Create Table GROUP
    public void create_table_groupHistory(SQLiteDatabase database) {
        try {

            Hashtable<String, String> tmp_group = new Hashtable<String, String>();
            tmp_group.put(GroupHistoryKey.ID, "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL");
            tmp_group.put(GroupHistoryKey.GROUP_ID, "TEXT");
            tmp_group.put(GroupHistoryKey.GROUP_HISTORY_ID, "TEXT");
            tmp_group.put(GroupHistoryKey.USER_ID, "TEXT");
            tmp_group.put(GroupHistoryKey.GROUP_NAME, "TEXT");
            tmp_group.put(GroupHistoryKey.MESSGAE, "TEXT");
            tmp_group.put(GroupHistoryKey.MESSGAE_ID, "TEXT");
            tmp_group.put(GroupHistoryKey.MESSAGE_DATETIME, "TEXT");
            tmp_group.put(GroupHistoryKey.TITLE, "TEXT");
            tmp_group.put(GroupHistoryKey.CREATED_DATE, "TEXT");
            tmp_group.put(GroupHistoryKey.CREATED_TIMESTAMP, "TEXT");
            tmp_group.put(GroupHistoryKey.TYPE, "TEXT");
            tmp_group.put(GroupHistoryKey.TYPE_ID, "TEXT");
            tmp_group.put(GroupHistoryKey.CITY, "TEXT");
            tmp_group.put(GroupHistoryKey.STATE, "TEXT");
            tmp_group.put(GroupHistoryKey.COUNTRY, "TEXT");
            tmp_group.put(GroupHistoryKey.ADDRESS, "TEXT");
            tmp_group.put(GroupHistoryKey.LOCATION, "TEXT");
            tmp_group.put(GroupHistoryKey.STATUS, "TEXT");
            tmp_group.put(GroupHistoryKey.SENDER_ID, "TEXT");
            tmp_group.put(GroupHistoryKey.SENDER_NAME, "TEXT");
            tmp_group.put(GroupHistoryKey.PHONE, "TEXT");
            createtable(database, TABLE_NAME_GROUP_HISTORY, tmp_group);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Create Table GROUP MEMBER
    public void create_table_group_member(SQLiteDatabase database) {
        try {

            Hashtable<String, String> tmp_group = new Hashtable<String, String>();
            tmp_group.put(GroupMemberKey.ID, "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL");
            tmp_group.put(GroupMemberKey.GROUP_ID, "TEXT");
            tmp_group.put(GroupMemberKey.MEMBER_NAME, "TEXT");
            tmp_group.put(GroupMemberKey.MEMBER_ID, "TEXT");
            tmp_group.put(GroupMemberKey.MEMBER_PROFILE_PIC, "TEXT");
            createtable(database, TABLE_NAME_GROUP_MEMBER, tmp_group);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Create Table Block User
    public void create_table_blockUser(SQLiteDatabase database) {
        try {

            Hashtable<String, String> tmp_blockuser = new Hashtable<String, String>();
            tmp_blockuser.put(BlockUserKey.ID, "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL");
            tmp_blockuser.put(BlockUserKey.BLOCK_USER_ID, "TEXT");
            tmp_blockuser.put(BlockUserKey.BLOCK_USER_NAME, "TEXT");
            tmp_blockuser.put(BlockUserKey.BLOCK_USER_IMAGE, "TEXT");
            createtable(database, TABLE_NAME_BLOCK_USER, tmp_blockuser);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Create Table Me Block User
    public void create_table_me_blockUser(SQLiteDatabase database) {
        try {

            Hashtable<String, String> tmp_blockuser = new Hashtable<String, String>();
            tmp_blockuser.put(MeBlockUserKey.ID, "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL");
            tmp_blockuser.put(MeBlockUserKey.BLOCK_USER_ID, "TEXT");
            createtable(database, TABLE_NAME_ME_BLOCK_USER, tmp_blockuser);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Create Table Prefred device

    public void create_table_prefredDevice(SQLiteDatabase database) {
        try {

            Hashtable<String, String> tmp_device = new Hashtable<String, String>();
            tmp_device.put(PrefredDeviceKey.ID, "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL");
            tmp_device.put(PrefredDeviceKey.DEVICE_NAME, "TEXT");
            tmp_device.put(PrefredDeviceKey.ADDRESS, "TEXT");
            tmp_device.put(PrefredDeviceKey.IS_PREFRED, "TEXT");
            createtable(database, TABLE_NAME_PREFRED_DEVICE, tmp_device);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Create Table category for log book

    public void create_table_category(SQLiteDatabase database) {
        try {

            Hashtable<String, String> tmp_category = new Hashtable<String, String>();
            tmp_category.put(CategoryKey.ID, "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL");
            tmp_category.put(CategoryKey.CATEGORY_ID, "TEXT");
            tmp_category.put(CategoryKey.CATEGORY_NAME, "TEXT");
            tmp_category.put(CategoryKey.CREATED_BY_USER, "TEXT");
            tmp_category.put(CategoryKey.CREATED_DATE, "TEXT");
            tmp_category.put(CategoryKey.MODIFIED_DATE, "TEXT");
            createtable(database, TABLE_NAME_CATEGORY, tmp_category);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Create Table logbook
    public void create_table_logbook(SQLiteDatabase database) {
        try {

            Hashtable<String, String> tmp_logbook = new Hashtable<String, String>();
            tmp_logbook.put(LogBookKey.ID, "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL");
            tmp_logbook.put(LogBookKey.CATEGORY_ID, "TEXT");
            tmp_logbook.put(LogBookKey.SHARE_TYPE_ID, "TEXT");
            tmp_logbook.put(LogBookKey.SHARE_TYPE, "TEXT");
            tmp_logbook.put(LogBookKey.LOG_BOOK_ID, "TEXT");
            tmp_logbook.put(LogBookKey.START_DATE_TIME, "TEXT");
            tmp_logbook.put(LogBookKey.END_DATE_TIME, "TEXT");
            tmp_logbook.put(LogBookKey.START_ADDRESS, "TEXT");
            tmp_logbook.put(LogBookKey.END_ADDRESS, "TEXT");
            tmp_logbook.put(LogBookKey.START_LATLONG, "TEXT");
            tmp_logbook.put(LogBookKey.END_LATLONG, "TEXT");
            tmp_logbook.put(LogBookKey.KILOMETER, "TEXT");
            tmp_logbook.put(LogBookKey.DURATION, "TEXT");
            tmp_logbook.put(LogBookKey.TIMESTAMP, "TEXT");
            tmp_logbook.put(LogBookKey.TITLE, "TEXT");
            tmp_logbook.put(LogBookKey.TRIP_STATUS, "TEXT");
            tmp_logbook.put(LogBookKey.EXPECTED_ADDRESS, "TEXT");
            tmp_logbook.put(LogBookKey.EXPECTED_DISTANCE, "TEXT");
            tmp_logbook.put(LogBookKey.EXPECTED_DURATION, "TEXT");
            tmp_logbook.put(LogBookKey.EXPECTED_LATLONG, "TEXT");

            createtable(database, TABLE_NAME_LOG_BOOK, tmp_logbook);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Create Table Folder
    public void create_table_folder(SQLiteDatabase database) {
        try {

            Hashtable<String, String> tmp_folder = new Hashtable<String, String>();
            tmp_folder.put(FolderKey.ID, "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL");
            tmp_folder.put(FolderKey.FOLDER_ID, "TEXT");
            tmp_folder.put(FolderKey.FOLDER_NAME, "TEXT");
            tmp_folder.put(FolderKey.CREATED_BY_USER, "TEXT");
            tmp_folder.put(FolderKey.CREATED_DATE, "TEXT");
            tmp_folder.put(FolderKey.MODIFIED_DATE, "TEXT");
            tmp_folder.put(FolderKey.IS_SUB_FOLDER, "TEXT");
            tmp_folder.put(FolderKey.NO_OF_SUBFOLDER, "TEXT");
            tmp_folder.put(FolderKey.NO_OF_CIDPOI, "TEXT");
            tmp_folder.put(FolderKey.FOLDER_LOCK, "TEXT");
            tmp_folder.put(FolderKey.FOLDER_LOCK_USERID, "TEXT");
            tmp_folder.put(FolderKey.FOLDER_TAGS, "TEXT");
            tmp_folder.put(FolderKey.CREATED_BY_USER_ID, "TEXT");
            createtable(database, TABLE_NAME_FOLDER, tmp_folder);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Create Table Sub Folder
    public void create_table_sub_folder(SQLiteDatabase database) {
        try {

            Hashtable<String, String> tmp_folder = new Hashtable<String, String>();
            tmp_folder.put(SubFolderKey.ID, "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL");
            tmp_folder.put(SubFolderKey.FOLDER_ID, "TEXT");
            tmp_folder.put(SubFolderKey.SUB_FOLDER_ID, "TEXT");
            tmp_folder.put(SubFolderKey.SUB_FOLDER_NAME, "TEXT");
            tmp_folder.put(SubFolderKey.CREATED_DATE, "TEXT");
            tmp_folder.put(SubFolderKey.MODIFIED_DATE, "TEXT");
            tmp_folder.put(SubFolderKey.SUB_FOLDER_LOCK, "TEXT");
            tmp_folder.put(SubFolderKey.SUB_FOLDER_LOCK_USERID, "TEXT");
            tmp_folder.put(SubFolderKey.CREATED_BY_USER_ID, "TEXT");
            tmp_folder.put(SubFolderKey.CREATED_BY_USER, "TEXT");
            tmp_folder.put(SubFolderKey.SUB_FOLDER_TAGS, "TEXT");

            createtable(database, TABLE_NAME_SUB_FOLDER, tmp_folder);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Create Table  Folder contacts and poi
    public void create_table_folder_contacts_poi(SQLiteDatabase database) {
        try {

            Hashtable<String, String> tmp_folder = new Hashtable<String, String>();
            tmp_folder.put(FolderContactsPoiKey.ID, "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL");
            tmp_folder.put(FolderContactsPoiKey.FOLDER_ID, "TEXT");
            tmp_folder.put(FolderContactsPoiKey.SUB_FOLDER_ID, "TEXT");
            tmp_folder.put(FolderContactsPoiKey.FOLDER_CONTACT_POI_ID, "TEXT");
            tmp_folder.put(FolderContactsPoiKey.SHARE_TYPE, "TEXT");
            tmp_folder.put(FolderContactsPoiKey.SHARE_TYPE_ID, "TEXT");
            tmp_folder.put(FolderContactsPoiKey.TIMESTAMP, "TEXT");
            tmp_folder.put(FolderContactsPoiKey.TITLE, "TEXT");
            tmp_folder.put(FolderContactsPoiKey.LATLONG, "TEXT");
            tmp_folder.put(FolderContactsPoiKey.ADDRESS, "TEXT");
            tmp_folder.put(FolderContactsPoiKey.NOTE, "TEXT");
            tmp_folder.put(FolderContactsPoiKey.NOTE_PUBLIC, "TEXT");
            tmp_folder.put(FolderContactsPoiKey.Phone, "TEXT");
            tmp_folder.put(FolderContactsPoiKey.URL, "TEXT");
            tmp_folder.put(FolderContactsPoiKey.PIC, "TEXT");
            tmp_folder.put(FolderContactsPoiKey.CONTACT_ID, "TEXT");
            createtable(database, TABLE_NAME_FOLDER_CONTACTS_POI, tmp_folder);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Create Table  Notification
    public void create_table_notification(SQLiteDatabase database) {
        try {

            Hashtable<String, String> tmp_notification = new Hashtable<String, String>();
            tmp_notification.put(NotificationKey.ID, "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL");
            tmp_notification.put(NotificationKey.NOTIFICATION_ID, "TEXT");
            tmp_notification.put(NotificationKey.MESSAGE, "TEXT");
            tmp_notification.put(NotificationKey.SHARE_TYPE_ID, "TEXT");
            tmp_notification.put(NotificationKey.SHARE_TYPE, "TEXT");
            tmp_notification.put(NotificationKey.STATUS, "TEXT");
            tmp_notification.put(NotificationKey.TIMESTAMP, "TEXT");
            tmp_notification.put(NotificationKey.METHOD_TYPE, "TEXT");
            tmp_notification.put(NotificationKey.SENDER_NAME, "TEXT");
            tmp_notification.put(NotificationKey.CREATED_BY_USER, "TEXT");
            tmp_notification.put(NotificationKey.USER_ID, "TEXT");
            tmp_notification.put(NotificationKey.ADDRESS, "TEXT");
            tmp_notification.put(NotificationKey.LALITUDE, "TEXT");
            tmp_notification.put(NotificationKey.LONGITUDE, "TEXT");
            tmp_notification.put(NotificationKey.TITLE, "TEXT");
            tmp_notification.put(NotificationKey.FOLDER_ID, "TEXT");
            tmp_notification.put(NotificationKey.GROUP_ID, "TEXT");
            tmp_notification.put(NotificationKey.GROUP_HISTORY_ID, "TEXT");
            tmp_notification.put(NotificationKey.GROUP_NAME, "TEXT");
            createtable(database, TABLE_NAME_NOTIFICATION, tmp_notification);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void TruncateAllTables()
    {
        deleteAll(TABLE_NAME_MYGLC);
        deleteAll(TABLE_NAME_CONTACTS);
        deleteAll(TABLE_NAME_MYGLC_PUBLIC_PRIVATE_ADD);
        deleteAll(TABLE_NAME_CONTACTS_PUBLIC_PRIVATE_ADD);
        deleteAll(TABLE_NAME_POI);
        deleteAll(TABLE_NAME_HISTORY);
        deleteAll(TABLE_NAME_GROUP);
        deleteAll(TABLE_NAME_BLOCK_USER);
        deleteAll(TABLE_NAME_ME_BLOCK_USER);
        deleteAll(TABLE_NAME_PREFRED_DEVICE);
        deleteAll(TABLE_NAME_GROUP_HISTORY);
        deleteAll(TABLE_NAME_GROUP_MEMBER);
        deleteAll(TABLE_NAME_CATEGORY);
        deleteAll(TABLE_NAME_LOG_BOOK);
        deleteAll(TABLE_NAME_FOLDER);
        deleteAll(TABLE_NAME_FOLDER_CONTACTS_POI);
        deleteAll(TABLE_NAME_SUB_FOLDER);
        deleteAll(TABLE_NAME_NOTIFICATION);


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


    public void insertUpdateRecord(Hashtable<String, String> queryValues, String tableName, String rowName, String Id)
    {
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


    public void insertUpdateRecordSubFolerPoiCid(Hashtable<String, String> queryValues, String poiCidId, String subFolderId) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = null;
        ContentValues values = new ContentValues();
        String whereClues = FolderContactsPoiKey.SUB_FOLDER_ID + " =? AND " + FolderContactsPoiKey.FOLDER_CONTACT_POI_ID + " =? ";
        cursor = database.query(TABLE_NAME_FOLDER_CONTACTS_POI, null, whereClues, new String[]{subFolderId, poiCidId}, null, null, null);

        database.beginTransaction();

        if (cursor.getCount() > 0) {

            for (String key : queryValues.keySet()) {

                values.put(key, queryValues.get(key));
            }
            database.update(TABLE_NAME_FOLDER_CONTACTS_POI, values, whereClues, new String[]{subFolderId, poiCidId});

        } else {

            for (String key : queryValues.keySet()) {
                values.put(key, queryValues.get(key));
            }

            database.insert(TABLE_NAME_FOLDER_CONTACTS_POI, null, values);
        }
        database.setTransactionSuccessful();
        database.endTransaction();
        cursor.close();
        database.close();
    }


    public void insertUpdateRecordFolderPoiCid(Hashtable<String, String> queryValues, String poiCidId, String folderID, String subFolderId) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = null;
        ContentValues values = new ContentValues();
        String whereClues = FolderContactsPoiKey.FOLDER_ID + " =? AND " + FolderContactsPoiKey.SUB_FOLDER_ID + " =? AND " + FolderContactsPoiKey.FOLDER_CONTACT_POI_ID + " =? ";
        cursor = database.query(TABLE_NAME_FOLDER_CONTACTS_POI, null, whereClues, new String[]{folderID, subFolderId, poiCidId}, null, null, null);

        database.beginTransaction();
        if (cursor.getCount() > 0) {

            for (String key : queryValues.keySet()) {

                values.put(key, queryValues.get(key));
            }
            database.update(TABLE_NAME_FOLDER_CONTACTS_POI, values, whereClues, new String[]{folderID, subFolderId, poiCidId});

        } else {


            for (String key : queryValues.keySet()) {
                values.put(key, queryValues.get(key));
            }

            database.insert(TABLE_NAME_FOLDER_CONTACTS_POI, null, values);
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


    public void UpdateRecordPairedDevice(String addressId, String isPrefreed) {


        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = null;
        ContentValues values = new ContentValues();
        cursor = database.query(TABLE_NAME_PREFRED_DEVICE, null, PrefredDeviceKey.ADDRESS + "=?", new String[]{addressId}, null, null, null);

        database.beginTransaction();
        if (cursor.getCount() > 0) {


            String[] args = new String[]{addressId};
            values.put(PrefredDeviceKey.IS_PREFRED, isPrefreed);
            database.update(TABLE_NAME_PREFRED_DEVICE, values, PrefredDeviceKey.ADDRESS + "=?", args);

        }
        database.setTransactionSuccessful();
        database.endTransaction();
        cursor.close();
        database.close();
    }


    public void insertUpdateDeleteRecordGroup(String groupId) {


        groupId = groupId.replace("[", "");
        groupId = groupId.replace("]", "");

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = null;
        String query;



        if (groupId.equalsIgnoreCase("")) {
            query = "SELECT * FROM " + TABLE_NAME_GROUP;
        } else {
            query = "SELECT * FROM " + TABLE_NAME_GROUP + " WHERE " + GroupKey.GROUP_ID + " NOT IN(" + groupId + ")";
        }


        cursor = database.rawQuery(query, null);


        if (cursor.getCount() > 0) {

            if (cursor.moveToFirst()) {
                do {

                    String deleteId = cursor.getString(cursor.getColumnIndex(GroupKey.GROUP_ID));
                    deleteById(TABLE_NAME_GROUP, GroupKey.GROUP_ID, deleteId);
                    deleteById(TABLE_NAME_GROUP_HISTORY, GroupHistoryKey.GROUP_ID, deleteId);
                    deleteById(TABLE_NAME_GROUP_MEMBER, GroupMemberKey.GROUP_ID, deleteId);


                } while (cursor.moveToNext());

            }
        }
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


    /**
     * *************************************************************************
     *
     * @ClassdName:FirstNameComparator
     * @CreatedDate:
     * @ModifiedBy: not yet
     * @ModifiedDate: not yet
     * @purpose:This class is use to get Sorting order ContactName
     * <p/>
     * *************************************************************************
     */

    public class FirstNameComparator implements Comparator<GLCContactsModel>
    {
        @Override
        public int compare(GLCContactsModel lhs, GLCContactsModel rhs)
        {
            return lhs.getFistname().compareToIgnoreCase(rhs.getFistname());
        }
    }


    public ArrayList<GLCContactsModel> selectGlcContacts(String userId, boolean isDyrctFilter) {

        modelGlcContactModels = new ArrayList<GLCContactsModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_NAME_CONTACTS, null, null, null, null, null, ContactKey.FIRST_NAME + " COLLATE NOCASE ASC;");

        // Log.d("", "Cursor size==" + cursor.getCount() + "==userId==" + userId);

        if (cursor.moveToFirst()) {
            do {
                GLCContactsModel glcConatacts = new GLCContactsModel();
                String name = "";

                if (!userId.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(ContactKey.USER_ID)))) {

                    if (Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ContactKey.IS_DYRCT_USER)))) {
                        Boolean prifleVisibility = Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_VISIBILTY_FLAG)));

                        if (prifleVisibility)
                        {

                            glcConatacts.setId(cursor.getString(cursor.getColumnIndex(ContactKey.ID)));
                            glcConatacts.setUserId(cursor.getString(cursor.getColumnIndex(ContactKey.USER_ID)));
                            glcConatacts.setContactId(cursor.getString(cursor.getColumnIndex(ContactKey.CONTACT_ID)));
                            name = cursor.getString(cursor.getColumnIndex(ContactKey.FIRST_NAME)) + " " + cursor.getString(cursor.getColumnIndex(ContactKey.LAST_NAME));
                            glcConatacts.setFistname(firstCharUprt(name));
                            glcConatacts.setLastname(cursor.getString(cursor.getColumnIndex(ContactKey.LAST_NAME)));
                            glcConatacts.setCompanyname(cursor.getString(cursor.getColumnIndex(ContactKey.COMPANY_NAME)));
                            glcConatacts.setDesignation(cursor.getString(cursor.getColumnIndex(ContactKey.DESIGNATION)));
                            glcConatacts.setEmail(cursor.getString(cursor.getColumnIndex(ContactKey.EMAIL)));
                            glcConatacts.setPhone(cursor.getString(cursor.getColumnIndex(ContactKey.PHONE)));
                            glcConatacts.setUrl(cursor.getString(cursor.getColumnIndex(ContactKey.URL)));
                            glcConatacts.setAddress(cursor.getString(cursor.getColumnIndex(ContactKey.ADDRESS)));
                            glcConatacts.setZipcode(cursor.getString(cursor.getColumnIndex(ContactKey.ZIPCODE)));
                            glcConatacts.setCountry(cursor.getString(cursor.getColumnIndex(ContactKey.COUNTRY)));
                            glcConatacts.setState(cursor.getString(cursor.getColumnIndex(ContactKey.STATE)));
                            glcConatacts.setCity(cursor.getString(cursor.getColumnIndex(ContactKey.CITY)));

                            String location=cursor.getString(cursor.getColumnIndex(ContactKey.LAT));

                            if((location !=null && !location.equalsIgnoreCase("") && !location.equalsIgnoreCase("null")))
                            {
                                glcConatacts.setLat(cursor.getString(cursor.getColumnIndex(ContactKey.LAT)));
                            }
                            else
                            {
                                glcConatacts.setLat("");
                            }

                            //glcConatacts.setLat(cursor.getString(cursor.getColumnIndex(ContactKey.LAT)));
                            glcConatacts.setLongi(cursor.getString(cursor.getColumnIndex(ContactKey.LONG)));
                            glcConatacts.setCategory(cursor.getString(cursor.getColumnIndex(ContactKey.CATEGORY)));
                            glcConatacts.setProfileImage(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_IMAGE)));
                            glcConatacts.setProfileImageId(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_IMAGE_ID)));
                            glcConatacts.setTimestamp(cursor.getString(cursor.getColumnIndex(ContactKey.TIMESTAMP)));
                            glcConatacts.setNote(cursor.getString(cursor.getColumnIndex(ContactKey.NOTE)));
                            glcConatacts.setAlternatePhone(cursor.getString(cursor.getColumnIndex(ContactKey.ALTERNATE_PHONE)));
                            glcConatacts.setIsDyrctUser(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ContactKey.IS_DYRCT_USER))));
                            glcConatacts.setProfileVisibiltyFlag(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_VISIBILTY_FLAG))));
                            glcConatacts.setDefault_address(cursor.getString(cursor.getColumnIndex(ContactKey.DEFAULT_ADDRESS)));


                            modelGlcContactModels.add(glcConatacts);


                        }

                    } else {


                        glcConatacts.setId(cursor.getString(cursor.getColumnIndex(ContactKey.ID)));
                        glcConatacts.setUserId(cursor.getString(cursor.getColumnIndex(ContactKey.USER_ID)));
                        glcConatacts.setContactId(cursor.getString(cursor.getColumnIndex(ContactKey.CONTACT_ID)));
                        name = cursor.getString(cursor.getColumnIndex(ContactKey.FIRST_NAME)).trim();
                        glcConatacts.setFistname(firstCharUprt(name));
                        glcConatacts.setLastname(cursor.getString(cursor.getColumnIndex(ContactKey.LAST_NAME)));
                        glcConatacts.setCompanyname(cursor.getString(cursor.getColumnIndex(ContactKey.COMPANY_NAME)));
                        glcConatacts.setDesignation(cursor.getString(cursor.getColumnIndex(ContactKey.DESIGNATION)));
                        glcConatacts.setEmail(cursor.getString(cursor.getColumnIndex(ContactKey.EMAIL)));
                        glcConatacts.setPhone(cursor.getString(cursor.getColumnIndex(ContactKey.PHONE)));
                        glcConatacts.setUrl(cursor.getString(cursor.getColumnIndex(ContactKey.URL)));
                        glcConatacts.setAddress(cursor.getString(cursor.getColumnIndex(ContactKey.ADDRESS)));
                        glcConatacts.setZipcode(cursor.getString(cursor.getColumnIndex(ContactKey.ZIPCODE)));
                        glcConatacts.setCountry(cursor.getString(cursor.getColumnIndex(ContactKey.COUNTRY)));
                        glcConatacts.setState(cursor.getString(cursor.getColumnIndex(ContactKey.STATE)));
                        glcConatacts.setCity(cursor.getString(cursor.getColumnIndex(ContactKey.CITY)));
                        glcConatacts.setLat(cursor.getString(cursor.getColumnIndex(ContactKey.LAT)));
                        glcConatacts.setLongi(cursor.getString(cursor.getColumnIndex(ContactKey.LONG)));
                        glcConatacts.setCategory(cursor.getString(cursor.getColumnIndex(ContactKey.CATEGORY)));
                        glcConatacts.setProfileImage(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_IMAGE)));
                        glcConatacts.setProfileImageId(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_IMAGE_ID)));
                        glcConatacts.setTimestamp(cursor.getString(cursor.getColumnIndex(ContactKey.TIMESTAMP)));
                        glcConatacts.setNote(cursor.getString(cursor.getColumnIndex(ContactKey.NOTE)));
                        glcConatacts.setAlternatePhone(cursor.getString(cursor.getColumnIndex(ContactKey.ALTERNATE_PHONE)));
                        glcConatacts.setIsDyrctUser(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ContactKey.IS_DYRCT_USER))));
                        glcConatacts.setProfileVisibiltyFlag(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_VISIBILTY_FLAG))));
                        glcConatacts.setDefault_address(cursor.getString(cursor.getColumnIndex(ContactKey.DEFAULT_ADDRESS)));

                        if (!isDyrctFilter) {
                            modelGlcContactModels.add(glcConatacts);
                        }

                    }


                }

            } while (cursor.moveToNext());

        }

        final ArrayList<GLCContactsModel> tempList = new ArrayList<GLCContactsModel>();
        tempList.addAll(modelGlcContactModels);

//        if (tempList != null && tempList.size() > 0) {
//            // Sorting
//            Collections.sort(modelGlcContactModels, new FirstNameComparator());
//
//        }

        //After Sorting order then get myCID data
        cursor = null;
        cursor = db.query(TABLE_NAME_MYGLC, null, null, null, null, null, null);
        GLCContactsModel glcConatacts = null;

        if (cursor.moveToFirst())
        {
            do {

                glcConatacts = new GLCContactsModel();
                glcConatacts.setId(cursor.getString(cursor.getColumnIndex(ContactKey.ID)));
                glcConatacts.setUserId(userId);
                glcConatacts.setContactId(userId);
                glcConatacts.setFistname("ME");
                glcConatacts.setLastname(cursor.getString(cursor.getColumnIndex(ContactKey.LAST_NAME)));
                glcConatacts.setCompanyname(cursor.getString(cursor.getColumnIndex(ContactKey.COMPANY_NAME)));
                glcConatacts.setDesignation(cursor.getString(cursor.getColumnIndex(ContactKey.DESIGNATION)));
                glcConatacts.setEmail(cursor.getString(cursor.getColumnIndex(ContactKey.EMAIL)));
                glcConatacts.setPhone(cursor.getString(cursor.getColumnIndex(ContactKey.PHONE)));
                glcConatacts.setUrl(cursor.getString(cursor.getColumnIndex(ContactKey.URL)));
                glcConatacts.setAddress(cursor.getString(cursor.getColumnIndex(ContactKey.ADDRESS)));
                glcConatacts.setZipcode(cursor.getString(cursor.getColumnIndex(ContactKey.ZIPCODE)));
                glcConatacts.setCountry(cursor.getString(cursor.getColumnIndex(ContactKey.COUNTRY)));
                glcConatacts.setState(cursor.getString(cursor.getColumnIndex(ContactKey.STATE)));
                glcConatacts.setCity(cursor.getString(cursor.getColumnIndex(ContactKey.CITY)));

                String location=cursor.getString(cursor.getColumnIndex(ContactKey.LAT));

                if((location !=null && !location.equalsIgnoreCase("") && !location.equalsIgnoreCase("null")))
                {
                    glcConatacts.setLat(cursor.getString(cursor.getColumnIndex(ContactKey.LAT)));
                }
                else
                {
                    glcConatacts.setLat("");
                }


                glcConatacts.setLongi(cursor.getString(cursor.getColumnIndex(ContactKey.LONG)));
                glcConatacts.setCategory(cursor.getString(cursor.getColumnIndex(ContactKey.CATEGORY)));
                glcConatacts.setAlternatePhone(cursor.getString(cursor.getColumnIndex(ContactKey.ALTERNATE_PHONE)));
                glcConatacts.setProfileImage(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_IMAGE)));
                glcConatacts.setProfileImageId(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_IMAGE_ID)));
                glcConatacts.setTimestamp(cursor.getString(cursor.getColumnIndex(ContactKey.TIMESTAMP)));
                glcConatacts.setNote("");
                glcConatacts.setIsDyrctUser(Boolean.valueOf("true"));
                glcConatacts.setProfileVisibiltyFlag(Boolean.valueOf("true"));
                glcConatacts.setDefault_address(cursor.getString(cursor.getColumnIndex(ContactKey.DEFAULT_ADDRESS)));


            } while (cursor.moveToNext());

        }


        //after sorting and add myglc data then merge two
        final ArrayList<GLCContactsModel> allList = new ArrayList<GLCContactsModel>();
        allList.add(glcConatacts);
        allList.addAll(modelGlcContactModels);

        cursor.close();
        db.close();

        return allList;
    }


    public String firstCharUprt(String name) {
        String output = "";

        if (name != null && !name.isEmpty())
        {
            output = name.substring(0, 1).toUpperCase() + name.substring(1);
        } else {
            output = name;
        }

        return output;
    }

    public ArrayList<GLCContactsModel> selectDyrctMyContacts(String userId)
    {

        modelGlcContactModels = new ArrayList<GLCContactsModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String name = "";
        cursor = db.query(TABLE_NAME_CONTACTS, null, null, null, null, null, ContactKey.FIRST_NAME + " COLLATE NOCASE ASC;");

        if (cursor.moveToFirst()) {
            do {
                GLCContactsModel glcConatacts = new GLCContactsModel();

                if (!userId.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(ContactKey.USER_ID)))) {

                    if (Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ContactKey.IS_DYRCT_USER)))) {

                        Boolean prifleVisibility = Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_VISIBILTY_FLAG)));

                        if (prifleVisibility) {

                            glcConatacts.setId(cursor.getString(cursor.getColumnIndex(ContactKey.ID)));
                            glcConatacts.setUserId(cursor.getString(cursor.getColumnIndex(ContactKey.USER_ID)));
                            glcConatacts.setContactId(cursor.getString(cursor.getColumnIndex(ContactKey.CONTACT_ID)));
                            name = cursor.getString(cursor.getColumnIndex(ContactKey.FIRST_NAME));
                            glcConatacts.setFistname(firstCharUprt(name));
                            glcConatacts.setLastname(cursor.getString(cursor.getColumnIndex(ContactKey.LAST_NAME)));
                            glcConatacts.setCompanyname(cursor.getString(cursor.getColumnIndex(ContactKey.COMPANY_NAME)));
                            glcConatacts.setDesignation(cursor.getString(cursor.getColumnIndex(ContactKey.DESIGNATION)));
                            glcConatacts.setEmail(cursor.getString(cursor.getColumnIndex(ContactKey.EMAIL)));
                            glcConatacts.setPhone(cursor.getString(cursor.getColumnIndex(ContactKey.PHONE)));
                            glcConatacts.setUrl(cursor.getString(cursor.getColumnIndex(ContactKey.URL)));
                            glcConatacts.setAddress(cursor.getString(cursor.getColumnIndex(ContactKey.ADDRESS)));
                            glcConatacts.setZipcode(cursor.getString(cursor.getColumnIndex(ContactKey.ZIPCODE)));
                            glcConatacts.setAlternatePhone(cursor.getString(cursor.getColumnIndex(ContactKey.ALTERNATE_PHONE)));
                            glcConatacts.setCountry(cursor.getString(cursor.getColumnIndex(ContactKey.COUNTRY)));
                            glcConatacts.setState(cursor.getString(cursor.getColumnIndex(ContactKey.STATE)));
                            glcConatacts.setCity(cursor.getString(cursor.getColumnIndex(ContactKey.CITY)));
                            glcConatacts.setLat(cursor.getString(cursor.getColumnIndex(ContactKey.LAT)));
                            glcConatacts.setLongi(cursor.getString(cursor.getColumnIndex(ContactKey.LONG)));
                            glcConatacts.setProfileImage(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_IMAGE)));
                            glcConatacts.setIsVisible(false);

                            modelGlcContactModels.add(glcConatacts);
                        }

                    }

                }


            } while (cursor.moveToNext());

        }


        cursor.close();
        db.close();

        return modelGlcContactModels;

    }

    public ArrayList<GLCContactsModel> selectDyrctMyContactsICE(final String userId,final ArrayList<IceUserModel> iceUserModelArr)
    {

        modelGlcContactModels = new ArrayList<GLCContactsModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        final ArrayList arrUserIds=new ArrayList();
        cursor = db.query(TABLE_NAME_CONTACTS, null, null, null, null, null, ContactKey.FIRST_NAME + " COLLATE NOCASE ASC;");


        for (int i=0; i<iceUserModelArr.size();i++)
        {
            arrUserIds.add(iceUserModelArr.get(i).getUserId());
        }

        if (cursor.moveToFirst())
        {
            do
            {
                GLCContactsModel glcConatacts = new GLCContactsModel();

                if (!userId.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(ContactKey.USER_ID))))
                {

                    if (Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ContactKey.IS_DYRCT_USER))))
                    {

                        Boolean prifleVisibility = Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_VISIBILTY_FLAG)));

                        if (prifleVisibility)
                        {

                            glcConatacts.setId(cursor.getString(cursor.getColumnIndex(ContactKey.ID)));
                            glcConatacts.setUserId(cursor.getString(cursor.getColumnIndex(ContactKey.USER_ID)));
                            glcConatacts.setContactId(cursor.getString(cursor.getColumnIndex(ContactKey.CONTACT_ID)));
                            glcConatacts.setFistname(cursor.getString(cursor.getColumnIndex(ContactKey.FIRST_NAME)));
                            glcConatacts.setLastname(cursor.getString(cursor.getColumnIndex(ContactKey.LAST_NAME)));
                            glcConatacts.setCompanyname(cursor.getString(cursor.getColumnIndex(ContactKey.COMPANY_NAME)));
                            glcConatacts.setDesignation(cursor.getString(cursor.getColumnIndex(ContactKey.DESIGNATION)));
                            glcConatacts.setEmail(cursor.getString(cursor.getColumnIndex(ContactKey.EMAIL)));
                            glcConatacts.setPhone(cursor.getString(cursor.getColumnIndex(ContactKey.PHONE)));
                            glcConatacts.setUrl(cursor.getString(cursor.getColumnIndex(ContactKey.URL)));
                            glcConatacts.setAddress(cursor.getString(cursor.getColumnIndex(ContactKey.ADDRESS)));
                            glcConatacts.setZipcode(cursor.getString(cursor.getColumnIndex(ContactKey.ZIPCODE)));
                            glcConatacts.setAlternatePhone(cursor.getString(cursor.getColumnIndex(ContactKey.ALTERNATE_PHONE)));
                            glcConatacts.setCountry(cursor.getString(cursor.getColumnIndex(ContactKey.COUNTRY)));
                            glcConatacts.setState(cursor.getString(cursor.getColumnIndex(ContactKey.STATE)));
                            glcConatacts.setCity(cursor.getString(cursor.getColumnIndex(ContactKey.CITY)));
                            glcConatacts.setLat(cursor.getString(cursor.getColumnIndex(ContactKey.LAT)));
                            glcConatacts.setLongi(cursor.getString(cursor.getColumnIndex(ContactKey.LONG)));
                            glcConatacts.setProfileImage(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_IMAGE)));



                            if(arrUserIds.contains(cursor.getString(cursor.getColumnIndex(ContactKey.USER_ID))))
                            {
                                glcConatacts.setIsVisible(true);
                            }
                            else
                            {
                                glcConatacts.setIsVisible(false);
                            }


                            if(cursor.getString(cursor.getColumnIndex(ContactKey.APP_VERSION)).equalsIgnoreCase("v2"))
                            {
                                modelGlcContactModels.add(glcConatacts);
                            }

                        }

                    }

                }


            } while (cursor.moveToNext());




        }


        cursor.close();
        db.close();

        return modelGlcContactModels;

    }

    public ArrayList<GLCContactsModel> selectNoneDyrctMyContacts(final String userId)
    {

        modelGlcContactModels = new ArrayList<GLCContactsModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        final ArrayList arrUserIds=new ArrayList();
        cursor = db.query(TABLE_NAME_CONTACTS, null, null, null, null, null, ContactKey.FIRST_NAME + " COLLATE NOCASE ASC;");



        if (cursor.moveToFirst()) {
            do {
                GLCContactsModel glcConatacts = new GLCContactsModel();

                if (!userId.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(ContactKey.USER_ID))))
                {

                    if (!Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ContactKey.IS_DYRCT_USER))))
                    {

                            glcConatacts.setId(cursor.getString(cursor.getColumnIndex(ContactKey.ID)));
                            glcConatacts.setUserId(cursor.getString(cursor.getColumnIndex(ContactKey.USER_ID)));
                            glcConatacts.setContactId(cursor.getString(cursor.getColumnIndex(ContactKey.CONTACT_ID)));
                            glcConatacts.setFistname(cursor.getString(cursor.getColumnIndex(ContactKey.FIRST_NAME)));
                            glcConatacts.setLastname(cursor.getString(cursor.getColumnIndex(ContactKey.LAST_NAME)));
                            glcConatacts.setCompanyname(cursor.getString(cursor.getColumnIndex(ContactKey.COMPANY_NAME)));
                            glcConatacts.setDesignation(cursor.getString(cursor.getColumnIndex(ContactKey.DESIGNATION)));
                            glcConatacts.setEmail(cursor.getString(cursor.getColumnIndex(ContactKey.EMAIL)));
                            glcConatacts.setPhone(cursor.getString(cursor.getColumnIndex(ContactKey.PHONE)));
                            glcConatacts.setUrl(cursor.getString(cursor.getColumnIndex(ContactKey.URL)));
                            glcConatacts.setAddress(cursor.getString(cursor.getColumnIndex(ContactKey.ADDRESS)));
                            glcConatacts.setZipcode(cursor.getString(cursor.getColumnIndex(ContactKey.ZIPCODE)));
                            glcConatacts.setAlternatePhone(cursor.getString(cursor.getColumnIndex(ContactKey.ALTERNATE_PHONE)));
                            glcConatacts.setCountry(cursor.getString(cursor.getColumnIndex(ContactKey.COUNTRY)));
                            glcConatacts.setState(cursor.getString(cursor.getColumnIndex(ContactKey.STATE)));
                            glcConatacts.setCity(cursor.getString(cursor.getColumnIndex(ContactKey.CITY)));
                            glcConatacts.setLat(cursor.getString(cursor.getColumnIndex(ContactKey.LAT)));
                            glcConatacts.setLongi(cursor.getString(cursor.getColumnIndex(ContactKey.LONG)));
                            glcConatacts.setProfileImage(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_IMAGE)));
                            glcConatacts.setIsVisible(false);


                            modelGlcContactModels.add(glcConatacts);


                    }

                }


            } while (cursor.moveToNext());

        }


        cursor.close();
        db.close();

        return modelGlcContactModels;

    }


    public ArrayList<GLCContactsModel> selectDyrctMyContactsWithSelected(final String userId, final String userIds) {

        modelGlcContactModels = new ArrayList<GLCContactsModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_NAME_CONTACTS, null, null, null, null, null, ContactKey.FIRST_NAME + " COLLATE NOCASE ASC;");

        if (cursor.moveToFirst()) {
            do {
                GLCContactsModel glcConatacts = new GLCContactsModel();

                if (!userId.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(ContactKey.USER_ID)))) {

                    if (Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ContactKey.IS_DYRCT_USER)))) {

                        Boolean prifleVisibility = Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_VISIBILTY_FLAG)));

                        if (prifleVisibility) {

                            glcConatacts.setId(cursor.getString(cursor.getColumnIndex(ContactKey.ID)));
                            glcConatacts.setUserId(cursor.getString(cursor.getColumnIndex(ContactKey.USER_ID)));
                            glcConatacts.setContactId(cursor.getString(cursor.getColumnIndex(ContactKey.CONTACT_ID)));
                            glcConatacts.setFistname(cursor.getString(cursor.getColumnIndex(ContactKey.FIRST_NAME)));
                            glcConatacts.setLastname(cursor.getString(cursor.getColumnIndex(ContactKey.LAST_NAME)));
                            glcConatacts.setCompanyname(cursor.getString(cursor.getColumnIndex(ContactKey.COMPANY_NAME)));
                            glcConatacts.setDesignation(cursor.getString(cursor.getColumnIndex(ContactKey.DESIGNATION)));
                            glcConatacts.setEmail(cursor.getString(cursor.getColumnIndex(ContactKey.EMAIL)));
                            glcConatacts.setPhone(cursor.getString(cursor.getColumnIndex(ContactKey.PHONE)));
                            glcConatacts.setUrl(cursor.getString(cursor.getColumnIndex(ContactKey.URL)));
                            glcConatacts.setAddress(cursor.getString(cursor.getColumnIndex(ContactKey.ADDRESS)));
                            glcConatacts.setZipcode(cursor.getString(cursor.getColumnIndex(ContactKey.ZIPCODE)));
                            glcConatacts.setAlternatePhone(cursor.getString(cursor.getColumnIndex(ContactKey.ALTERNATE_PHONE)));
                            glcConatacts.setCountry(cursor.getString(cursor.getColumnIndex(ContactKey.COUNTRY)));
                            glcConatacts.setState(cursor.getString(cursor.getColumnIndex(ContactKey.STATE)));
                            glcConatacts.setCity(cursor.getString(cursor.getColumnIndex(ContactKey.CITY)));
                            glcConatacts.setLat(cursor.getString(cursor.getColumnIndex(ContactKey.LAT)));
                            glcConatacts.setLongi(cursor.getString(cursor.getColumnIndex(ContactKey.LONG)));
                            glcConatacts.setProfileImage(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_IMAGE)));

                            if (userIds.contains(cursor.getString(cursor.getColumnIndex(ContactKey.USER_ID)))) {
                                glcConatacts.setIsVisible(true);
                            } else {
                                glcConatacts.setIsVisible(false);
                            }


                            modelGlcContactModels.add(glcConatacts);
                        }

                    }

                }


            } while (cursor.moveToNext());

        }


        cursor.close();
        db.close();

        return modelGlcContactModels;

    }

    public ArrayList<GLCContactsModel> selectDyrctMyContactsWithMyCID(String userId) {

        modelGlcContactModels = new ArrayList<GLCContactsModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();

        cursor = db.query(TABLE_NAME_CONTACTS, null, null, null, null, null, ContactKey.FIRST_NAME + " COLLATE NOCASE ASC;");

        if (cursor.moveToFirst()) {
            do {
                GLCContactsModel glcConatacts = new GLCContactsModel();

                if (!userId.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(ContactKey.USER_ID)))) {

                    if (Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ContactKey.IS_DYRCT_USER)))) {

                        Boolean prifleVisibility = Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_VISIBILTY_FLAG)));

                        if (prifleVisibility) {

                            glcConatacts.setId(cursor.getString(cursor.getColumnIndex(ContactKey.ID)));
                            glcConatacts.setUserId(cursor.getString(cursor.getColumnIndex(ContactKey.USER_ID)));
                            glcConatacts.setContactId(cursor.getString(cursor.getColumnIndex(ContactKey.CONTACT_ID)));
                            glcConatacts.setFistname(cursor.getString(cursor.getColumnIndex(ContactKey.FIRST_NAME)));
                            glcConatacts.setLastname(cursor.getString(cursor.getColumnIndex(ContactKey.LAST_NAME)));
                            glcConatacts.setCompanyname(cursor.getString(cursor.getColumnIndex(ContactKey.COMPANY_NAME)));
                            glcConatacts.setDesignation(cursor.getString(cursor.getColumnIndex(ContactKey.DESIGNATION)));
                            glcConatacts.setEmail(cursor.getString(cursor.getColumnIndex(ContactKey.EMAIL)));
                            glcConatacts.setPhone(cursor.getString(cursor.getColumnIndex(ContactKey.PHONE)));
                            glcConatacts.setUrl(cursor.getString(cursor.getColumnIndex(ContactKey.URL)));
                            glcConatacts.setAddress(cursor.getString(cursor.getColumnIndex(ContactKey.ADDRESS)));
                            glcConatacts.setZipcode(cursor.getString(cursor.getColumnIndex(ContactKey.ZIPCODE)));
                            glcConatacts.setAlternatePhone(cursor.getString(cursor.getColumnIndex(ContactKey.ALTERNATE_PHONE)));
                            glcConatacts.setCountry(cursor.getString(cursor.getColumnIndex(ContactKey.COUNTRY)));
                            glcConatacts.setState(cursor.getString(cursor.getColumnIndex(ContactKey.STATE)));
                            glcConatacts.setCity(cursor.getString(cursor.getColumnIndex(ContactKey.CITY)));
                            glcConatacts.setLat(cursor.getString(cursor.getColumnIndex(ContactKey.LAT)));
                            glcConatacts.setLongi(cursor.getString(cursor.getColumnIndex(ContactKey.LONG)));
                            glcConatacts.setProfileImage(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_IMAGE)));
                            glcConatacts.setIsVisible(false);

                            modelGlcContactModels.add(glcConatacts);
                        }

                    }

                }

            } while (cursor.moveToNext());

        }

        //After Sorting order then get myCID data
        cursor = null;
        cursor = db.query(TABLE_NAME_MYGLC, null, null, null, null, null, null);
        GLCContactsModel glcConatacts = null;

        if (cursor.moveToFirst()) {
            do {

                glcConatacts = new GLCContactsModel();
                glcConatacts.setId(cursor.getString(cursor.getColumnIndex(ContactKey.ID)));
                glcConatacts.setUserId(userId);
                glcConatacts.setContactId(userId);
                glcConatacts.setFistname(cursor.getString(cursor.getColumnIndex(ContactKey.FIRST_NAME)));
                glcConatacts.setLastname(cursor.getString(cursor.getColumnIndex(ContactKey.LAST_NAME)));
                glcConatacts.setCompanyname(cursor.getString(cursor.getColumnIndex(ContactKey.COMPANY_NAME)));
                glcConatacts.setDesignation(cursor.getString(cursor.getColumnIndex(ContactKey.DESIGNATION)));
                glcConatacts.setEmail(cursor.getString(cursor.getColumnIndex(ContactKey.EMAIL)));
                glcConatacts.setPhone(cursor.getString(cursor.getColumnIndex(ContactKey.PHONE)));
                glcConatacts.setUrl(cursor.getString(cursor.getColumnIndex(ContactKey.URL)));
                glcConatacts.setAddress(cursor.getString(cursor.getColumnIndex(ContactKey.ADDRESS)));
                glcConatacts.setZipcode(cursor.getString(cursor.getColumnIndex(ContactKey.ZIPCODE)));
                glcConatacts.setAlternatePhone(cursor.getString(cursor.getColumnIndex(ContactKey.ALTERNATE_PHONE)));
                glcConatacts.setCountry(cursor.getString(cursor.getColumnIndex(ContactKey.COUNTRY)));
                glcConatacts.setState(cursor.getString(cursor.getColumnIndex(ContactKey.STATE)));
                glcConatacts.setCity(cursor.getString(cursor.getColumnIndex(ContactKey.CITY)));
                glcConatacts.setLat(cursor.getString(cursor.getColumnIndex(ContactKey.LAT)));
                glcConatacts.setLongi(cursor.getString(cursor.getColumnIndex(ContactKey.LONG)));
                glcConatacts.setProfileImage(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_IMAGE)));
                glcConatacts.setIsVisible(false);

            } while (cursor.moveToNext());

        }


        //after sorting and add myglc data then merge two
        final ArrayList<GLCContactsModel> allList = new ArrayList<GLCContactsModel>();
        allList.add(glcConatacts);
        allList.addAll(modelGlcContactModels);
        cursor.close();
        db.close();

        return allList;

    }


    public ArrayList<GLCContactsModel> selectDyrctMyContactsWithShare(String userId, String contactID, String shareType) {

        modelGlcContactModels = new ArrayList<GLCContactsModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String name = "";
        cursor = db.query(TABLE_NAME_CONTACTS, null, null, null, null, null, ContactKey.FIRST_NAME + " COLLATE NOCASE ASC;");

        if (cursor.moveToFirst()) {
            do {
                GLCContactsModel glcConatacts = new GLCContactsModel();

                if (!userId.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(ContactKey.USER_ID)))) {

                    if (Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ContactKey.IS_DYRCT_USER)))) {

                        Boolean prifleVisibility = Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_VISIBILTY_FLAG)));

                        if (prifleVisibility) {

                            glcConatacts.setId(cursor.getString(cursor.getColumnIndex(ContactKey.ID)));
                            glcConatacts.setUserId(cursor.getString(cursor.getColumnIndex(ContactKey.USER_ID)));
                            glcConatacts.setContactId(cursor.getString(cursor.getColumnIndex(ContactKey.CONTACT_ID)));
                            glcConatacts.setFistname(cursor.getString(cursor.getColumnIndex(ContactKey.FIRST_NAME)));
                            name = cursor.getString(cursor.getColumnIndex(ContactKey.FIRST_NAME));
                            glcConatacts.setFistname(firstCharUprt(name));
                            glcConatacts.setLastname(cursor.getString(cursor.getColumnIndex(ContactKey.LAST_NAME)));
                            glcConatacts.setCompanyname(cursor.getString(cursor.getColumnIndex(ContactKey.COMPANY_NAME)));
                            glcConatacts.setDesignation(cursor.getString(cursor.getColumnIndex(ContactKey.DESIGNATION)));
                            glcConatacts.setEmail(cursor.getString(cursor.getColumnIndex(ContactKey.EMAIL)));
                            glcConatacts.setPhone(cursor.getString(cursor.getColumnIndex(ContactKey.PHONE)));
                            glcConatacts.setUrl(cursor.getString(cursor.getColumnIndex(ContactKey.URL)));
                            glcConatacts.setAddress(cursor.getString(cursor.getColumnIndex(ContactKey.ADDRESS)));
                            glcConatacts.setZipcode(cursor.getString(cursor.getColumnIndex(ContactKey.ZIPCODE)));
                            glcConatacts.setCountry(cursor.getString(cursor.getColumnIndex(ContactKey.COUNTRY)));
                            glcConatacts.setState(cursor.getString(cursor.getColumnIndex(ContactKey.STATE)));
                            glcConatacts.setCity(cursor.getString(cursor.getColumnIndex(ContactKey.CITY)));
                            glcConatacts.setLat(cursor.getString(cursor.getColumnIndex(ContactKey.LAT)));
                            glcConatacts.setLongi(cursor.getString(cursor.getColumnIndex(ContactKey.LONG)));
                            glcConatacts.setAlternatePhone(cursor.getString(cursor.getColumnIndex(ContactKey.ALTERNATE_PHONE)));
                            glcConatacts.setProfileImage(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_IMAGE)));
                            glcConatacts.setIsVisible(false);


                            if (shareType.equalsIgnoreCase(Constans.SHARE_TYPE_CID)) {
                                if (!cursor.getString(cursor.getColumnIndex(ContactKey.USER_ID)).equalsIgnoreCase(contactID)) {
                                    modelGlcContactModels.add(glcConatacts);
                                }
                            } else {
                                if (!cursor.getString(cursor.getColumnIndex(ContactKey.CONTACT_ID)).equalsIgnoreCase(contactID)) {
                                    modelGlcContactModels.add(glcConatacts);
                                }
                            }


                        }

                    }

                }


            } while (cursor.moveToNext());

        }


        cursor.close();
        db.close();

        return modelGlcContactModels;

    }


    public ArrayList<GLCContactsModel> selectDyrctMyContactsUpdatemember(String userId, String groupId) {

        //get Glc contacts Data
        modelGlcContactModels = new ArrayList<GLCContactsModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_NAME_CONTACTS, null, null, null, null, null, ContactKey.FIRST_NAME + " COLLATE NOCASE ASC;");
        String name = "";

        if (cursor.moveToFirst()) {
            do {
                GLCContactsModel glcConatacts = new GLCContactsModel();

                if (!userId.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(ContactKey.USER_ID)))) {

                    if (Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ContactKey.IS_DYRCT_USER)))) {

                        Boolean prifleVisibility = Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_VISIBILTY_FLAG)));

                        if (prifleVisibility) {

                            //get Group member id
                            Cursor cursorInr = null;
                            cursorInr = db.query(TABLE_NAME_GROUP_MEMBER, null, GroupMemberKey.GROUP_ID + "=?", new String[]{groupId}, null, null, null);
                            String memberId = "";

                            if (cursorInr.moveToFirst()) {
                                do {


                                    if (cursor.getString(cursor.getColumnIndex(ContactKey.USER_ID)).equals(cursorInr.getString(cursorInr.getColumnIndex(GroupMemberKey.MEMBER_ID)))) {

                                        memberId = cursorInr.getString(cursorInr.getColumnIndex(GroupMemberKey.MEMBER_ID));
                                        break;

                                    }


                                } while (cursorInr.moveToNext());

                            }


                            if (!memberId.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(ContactKey.USER_ID)))) {
                                glcConatacts.setId(cursor.getString(cursor.getColumnIndex(ContactKey.ID)));
                                glcConatacts.setUserId(cursor.getString(cursor.getColumnIndex(ContactKey.USER_ID)));
                                glcConatacts.setContactId(cursor.getString(cursor.getColumnIndex(ContactKey.CONTACT_ID)));
                                name = cursor.getString(cursor.getColumnIndex(ContactKey.FIRST_NAME));
                                glcConatacts.setFistname(firstCharUprt(name));
                                glcConatacts.setLastname(cursor.getString(cursor.getColumnIndex(ContactKey.LAST_NAME)));
                                glcConatacts.setCompanyname(cursor.getString(cursor.getColumnIndex(ContactKey.COMPANY_NAME)));
                                glcConatacts.setDesignation(cursor.getString(cursor.getColumnIndex(ContactKey.DESIGNATION)));
                                glcConatacts.setEmail(cursor.getString(cursor.getColumnIndex(ContactKey.EMAIL)));
                                glcConatacts.setPhone(cursor.getString(cursor.getColumnIndex(ContactKey.PHONE)));
                                glcConatacts.setUrl(cursor.getString(cursor.getColumnIndex(ContactKey.URL)));
                                glcConatacts.setAddress(cursor.getString(cursor.getColumnIndex(ContactKey.ADDRESS)));
                                glcConatacts.setZipcode(cursor.getString(cursor.getColumnIndex(ContactKey.ZIPCODE)));
                                glcConatacts.setCountry(cursor.getString(cursor.getColumnIndex(ContactKey.COUNTRY)));
                                glcConatacts.setState(cursor.getString(cursor.getColumnIndex(ContactKey.STATE)));
                                glcConatacts.setCity(cursor.getString(cursor.getColumnIndex(ContactKey.CITY)));
                                glcConatacts.setLat(cursor.getString(cursor.getColumnIndex(ContactKey.LAT)));
                                glcConatacts.setAlternatePhone(cursor.getString(cursor.getColumnIndex(ContactKey.ALTERNATE_PHONE)));
                                glcConatacts.setLongi(cursor.getString(cursor.getColumnIndex(ContactKey.LONG)));
                                glcConatacts.setProfileImage(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_IMAGE)));
                                glcConatacts.setIsVisible(false);
                                modelGlcContactModels.add(glcConatacts);
                            }


                        }

                    }

                }


            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();

        return modelGlcContactModels;

    }

    public ArrayList<BlockUserModel> selectBlockUserList() {

        modelBlockUserList = new ArrayList<BlockUserModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();

        cursor = db.query(TABLE_NAME_BLOCK_USER, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                BlockUserModel blockList = new BlockUserModel();
                blockList.setId(cursor.getString(cursor.getColumnIndex(BlockUserKey.ID)));
                blockList.setBlock_user_id(cursor.getString(cursor.getColumnIndex(BlockUserKey.BLOCK_USER_ID)));
                blockList.setBlock_user_name(cursor.getString(cursor.getColumnIndex(BlockUserKey.BLOCK_USER_NAME)));
                blockList.setBlock_user_image(cursor.getString(cursor.getColumnIndex(BlockUserKey.BLOCK_USER_IMAGE)));
                modelBlockUserList.add(blockList);


            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();

        return modelBlockUserList;

    }

    public GLCContactsModel selectMyGlcContacts() {
        modelGlcContactModels = new ArrayList<GLCContactsModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_NAME_MYGLC, null, null, null, null, null, null);
        GLCContactsModel glcConatacts = null;

        if (cursor.moveToFirst()) {
            do {
                glcConatacts = new GLCContactsModel();

                glcConatacts.setId(cursor.getString(cursor.getColumnIndex(ContactKey.ID)));
                glcConatacts.setFistname(cursor.getString(cursor.getColumnIndex(ContactKey.FIRST_NAME)));

                //Log.d("GLCContactsModel", "NAME==" + cursor.getString(cursor.getColumnIndex(ContactKey.FIRST_NAME)));

                glcConatacts.setLastname(cursor.getString(cursor.getColumnIndex(ContactKey.LAST_NAME)));
                glcConatacts.setCompanyname(cursor.getString(cursor.getColumnIndex(ContactKey.COMPANY_NAME)));
                glcConatacts.setDesignation(cursor.getString(cursor.getColumnIndex(ContactKey.DESIGNATION)));
                glcConatacts.setEmail(cursor.getString(cursor.getColumnIndex(ContactKey.EMAIL)));
                glcConatacts.setPhone(cursor.getString(cursor.getColumnIndex(ContactKey.PHONE)));
                glcConatacts.setUrl(cursor.getString(cursor.getColumnIndex(ContactKey.URL)));
                glcConatacts.setAddress(cursor.getString(cursor.getColumnIndex(ContactKey.ADDRESS)));
                glcConatacts.setZipcode(cursor.getString(cursor.getColumnIndex(ContactKey.ZIPCODE)));
                glcConatacts.setCountry(cursor.getString(cursor.getColumnIndex(ContactKey.COUNTRY)));
                glcConatacts.setState(cursor.getString(cursor.getColumnIndex(ContactKey.STATE)));
                glcConatacts.setCity(cursor.getString(cursor.getColumnIndex(ContactKey.CITY)));
                glcConatacts.setLat(cursor.getString(cursor.getColumnIndex(ContactKey.LAT)));
                glcConatacts.setLongi(cursor.getString(cursor.getColumnIndex(ContactKey.LONG)));
                glcConatacts.setCategory(cursor.getString(cursor.getColumnIndex(ContactKey.CATEGORY)));
                glcConatacts.setProfileImage(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_IMAGE)));
                glcConatacts.setProfileImageId(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_IMAGE_ID)));
                glcConatacts.setAlternatePhone(cursor.getString(cursor.getColumnIndex(ContactKey.ALTERNATE_PHONE)));
                glcConatacts.setTimestamp(cursor.getString(cursor.getColumnIndex(ContactKey.TIMESTAMP)));
                glcConatacts.setDefault_address(cursor.getString(cursor.getColumnIndex(ContactKey.DEFAULT_ADDRESS)));
                glcConatacts.setProfileVisibiltyFlag(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_VISIBILTY_FLAG))));


            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();

        return glcConatacts;

    }


    public GLCContactsModel selectedGlcContactInfoByContactId(String contactId) {

        modelGlcContactModels = new ArrayList<GLCContactsModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_NAME_CONTACTS, null, ContactKey.CONTACT_ID + "=?", new String[]{contactId}, null, null, null);
        GLCContactsModel glcConatacts = null;

        if (cursor.moveToFirst()) {
            do {
                glcConatacts = new GLCContactsModel();

                glcConatacts.setId(cursor.getString(cursor.getColumnIndex(ContactKey.ID)));
                glcConatacts.setContactId(cursor.getString(cursor.getColumnIndex(ContactKey.CONTACT_ID)));
                glcConatacts.setFistname(cursor.getString(cursor.getColumnIndex(ContactKey.FIRST_NAME)));
                glcConatacts.setLastname(cursor.getString(cursor.getColumnIndex(ContactKey.LAST_NAME)));
                glcConatacts.setCompanyname(cursor.getString(cursor.getColumnIndex(ContactKey.COMPANY_NAME)));
                glcConatacts.setDesignation(cursor.getString(cursor.getColumnIndex(ContactKey.DESIGNATION)));
                glcConatacts.setEmail(cursor.getString(cursor.getColumnIndex(ContactKey.EMAIL)));
                glcConatacts.setPhone(cursor.getString(cursor.getColumnIndex(ContactKey.PHONE)));
                glcConatacts.setUrl(cursor.getString(cursor.getColumnIndex(ContactKey.URL)));
                glcConatacts.setAddress(cursor.getString(cursor.getColumnIndex(ContactKey.ADDRESS)));
                glcConatacts.setZipcode(cursor.getString(cursor.getColumnIndex(ContactKey.ZIPCODE)));
                glcConatacts.setCountry(cursor.getString(cursor.getColumnIndex(ContactKey.COUNTRY)));
                glcConatacts.setState(cursor.getString(cursor.getColumnIndex(ContactKey.STATE)));
                glcConatacts.setCity(cursor.getString(cursor.getColumnIndex(ContactKey.CITY)));
                glcConatacts.setLat(cursor.getString(cursor.getColumnIndex(ContactKey.LAT)));
                glcConatacts.setLongi(cursor.getString(cursor.getColumnIndex(ContactKey.LONG)));
                glcConatacts.setCategory(cursor.getString(cursor.getColumnIndex(ContactKey.CATEGORY)));
                glcConatacts.setAlternatePhone(cursor.getString(cursor.getColumnIndex(ContactKey.ALTERNATE_PHONE)));
                glcConatacts.setProfileImage(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_IMAGE)));
                glcConatacts.setProfileImageId(cursor.getString(cursor.getColumnIndex(ContactKey.PROFILE_IMAGE_ID)));
                glcConatacts.setTimestamp(cursor.getString(cursor.getColumnIndex(ContactKey.TIMESTAMP)));
                glcConatacts.setNote(cursor.getString(cursor.getColumnIndex(ContactKey.NOTE)));
                glcConatacts.setNote_public(cursor.getString(cursor.getColumnIndex(ContactKey.NOTE_PUBLIC)));
                glcConatacts.setDefault_address(cursor.getString(cursor.getColumnIndex(ContactKey.DEFAULT_ADDRESS)));

            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return glcConatacts;

    }


    public PoiModel SelectPoi(String poiId) {

        PoiModel poiModel = new PoiModel();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        //cursor = db.query(TABLE_NAME_POI, null, null, null, null, null, null, null);
        cursor = db.query(TABLE_NAME_POI, null, PoiKey.POI_ID + "=?", new String[]{poiId}, null, null, null);


        if (cursor.moveToFirst()) {
            do {

                poiModel.setId(cursor.getString(cursor.getColumnIndex(PoiKey.ID)));
                poiModel.setPoi_id(cursor.getString(cursor.getColumnIndex(PoiKey.POI_ID)));
                poiModel.setTitle(cursor.getString(cursor.getColumnIndex(PoiKey.TITLE)));
                poiModel.setAddress(cursor.getString(cursor.getColumnIndex(PoiKey.ADDRESS)));
                poiModel.setLat(cursor.getString(cursor.getColumnIndex(PoiKey.LAT)));
                poiModel.setLng(cursor.getString(cursor.getColumnIndex(PoiKey.LONG)));
                poiModel.setImage(cursor.getString(cursor.getColumnIndex(PoiKey.IMAGE)));
                poiModel.setNote(cursor.getString(cursor.getColumnIndex(PoiKey.NOTE)));
                poiModel.setNote_public(cursor.getString(cursor.getColumnIndex(PoiKey.NOTE_PUBLIC)));
                poiModel.setPhone(cursor.getString(cursor.getColumnIndex(PoiKey.PHONE)));
                poiModel.setUrl(cursor.getString(cursor.getColumnIndex(PoiKey.URL)));
                poiModel.setIce_flag(cursor.getString(cursor.getColumnIndex(PoiKey.ICE_FLAG)));
                poiModel.setIce_message(cursor.getString(cursor.getColumnIndex(PoiKey.ICE_MESSAGE)));

            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return poiModel;

    }


    public SubFolderCidPoiModel selectSubFoldersCidPoiModel(final String folder_id, final String folder_type, final String folderPoiCidId) {
        SubFolderCidPoiModel folderModel = new SubFolderCidPoiModel();
        Cursor cursorCidPoi = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClues;

        if (folder_type.equalsIgnoreCase(Constans.FOLDER)) {
            whereClues = FolderContactsPoiKey.SUB_FOLDER_ID + " =? AND " + FolderContactsPoiKey.FOLDER_CONTACT_POI_ID + " =? ";
        } else {
            whereClues = FolderContactsPoiKey.SUB_FOLDER_ID + " =? AND " + FolderContactsPoiKey.FOLDER_CONTACT_POI_ID + " =? ";
        }


        cursorCidPoi = db.query(TABLE_NAME_FOLDER_CONTACTS_POI, null, whereClues, new String[]{folder_id, folderPoiCidId}, null, null, null);


        if (cursorCidPoi.moveToFirst()) {
            do {

                folderModel.setId(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.ID)));
                folderModel.setFolder_id(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.FOLDER_ID)));
                folderModel.setCid_poi_id(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.FOLDER_CONTACT_POI_ID)));
                folderModel.setType(Constans.CIDPOI);
                folderModel.setSub_folder_id(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.SUB_FOLDER_ID)));
                folderModel.setTitle(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.TITLE)));
                folderModel.setLatlong(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.LATLONG)));
                folderModel.setAddress(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.ADDRESS)));
                folderModel.setShare_type(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.SHARE_TYPE)));
                folderModel.setShare_type_id(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.SHARE_TYPE_ID)));
                folderModel.setNote(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.NOTE)));
                folderModel.setNote_public(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.NOTE_PUBLIC)));
                folderModel.setPhone(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.Phone)));
                folderModel.setUrl(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.URL)));
                folderModel.setPic(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.PIC)));
                folderModel.setContacts_id(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.CONTACT_ID)));

            } while (cursorCidPoi.moveToNext());

        }

        db.close();
        return folderModel;

    }


    /****************************************************************************
     * @Method:- selectedGlcContactInfoWithMultipleAddressById
     * @purpose: this method use to get selected userData and multiple public and private address (get data in  Two tables)
     ***************************************************************************/


    public ArrayList<GLCContactsModel> selectedGlcContactInfoWithMultipleAddressById(String userId, String defulatAddress, Context context) {

        final String loginUserid = DyrctApplication.getmInstance().getSharedPreferences().getString(context.getString(R.string.User_id), "");
        modelGlcContactModels = new ArrayList<GLCContactsModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_NAME_CONTACTS, null, ContactKey.USER_ID + "=?", new String[]{userId}, null, null, null);

        GLCContactsModel glcConatactsMain = null;

        if (cursor.moveToFirst()) {
            do {
                glcConatactsMain = new GLCContactsModel();
                glcConatactsMain.setId(cursor.getString(cursor.getColumnIndex(ContactKey.CONTACT_ID)));
                glcConatactsMain.setCompanyname(cursor.getString(cursor.getColumnIndex(ContactKey.COMPANY_NAME)));
                glcConatactsMain.setEmail(cursor.getString(cursor.getColumnIndex(ContactKey.EMAIL)));
                glcConatactsMain.setPhone(cursor.getString(cursor.getColumnIndex(ContactKey.PHONE)));
                glcConatactsMain.setAddress(cursor.getString(cursor.getColumnIndex(ContactKey.ADDRESS)));
                glcConatactsMain.setCountry(cursor.getString(cursor.getColumnIndex(ContactKey.COUNTRY)));
                glcConatactsMain.setState(cursor.getString(cursor.getColumnIndex(ContactKey.STATE)));
                glcConatactsMain.setCity(cursor.getString(cursor.getColumnIndex(ContactKey.CITY)));
                glcConatactsMain.setAlternatePhone(cursor.getString(cursor.getColumnIndex(ContactKey.ALTERNATE_PHONE)));
                glcConatactsMain.setLat(cursor.getString(cursor.getColumnIndex(ContactKey.LAT)));
                glcConatactsMain.setLongi(cursor.getString(cursor.getColumnIndex(ContactKey.LONG)));


                if (cursor.getString(cursor.getColumnIndex(ContactKey.LAT)) != null) {
                    modelGlcContactModels.add(glcConatactsMain);
                }

                /*
                Cursor cursorIn = null;

                if (loginUserid.equalsIgnoreCase(userId))
                {
                    cursorIn = db.query(TABLE_NAME_CONTACTS_PUBLIC_PRIVATE_ADD, null, ContactKey.USER_ID + "=?", new String[]{userId}, null, null, null);
                }
                else
                {
                    if (defulatAddress.equals(context.getString(R.string.Both)))
                    {
                        cursorIn = db.query(TABLE_NAME_CONTACTS_PUBLIC_PRIVATE_ADD, null, ContactKey.USER_ID + "=?", new String[]{userId}, null, null, null);

                    } else {
                        cursorIn = db.query(TABLE_NAME_CONTACTS_PUBLIC_PRIVATE_ADD, null, ContactKey.USER_ID + " =? AND " + ContactKey.TYPE_ADDRESS + " =? ", new String[]{userId, defulatAddress}, null, null, null);
                    }
                }


                if (cursorIn.moveToFirst())
                {
                    do {

                        //get Defualt multiple address publicprivate table
                        GLCContactsModel glcConatacts = new GLCContactsModel();
                        glcConatacts.setId(cursorIn.getString(cursorIn.getColumnIndex(ContactKey.ADDRESS_ID)));
                        glcConatacts.setCompanyname(cursorIn.getString(cursorIn.getColumnIndex(ContactKey.COMPANY_NAME)));
                        glcConatacts.setEmail(cursorIn.getString(cursorIn.getColumnIndex(ContactKey.EMAIL)));
                        glcConatacts.setPhone(cursorIn.getString(cursorIn.getColumnIndex(ContactKey.PHONE)));
                        glcConatacts.setAddress(cursorIn.getString(cursorIn.getColumnIndex(ContactKey.ADDRESS)));
                        glcConatacts.setCountry(cursorIn.getString(cursorIn.getColumnIndex(ContactKey.COUNTRY)));
                        glcConatacts.setState(cursorIn.getString(cursorIn.getColumnIndex(ContactKey.STATE)));
                        glcConatacts.setCity(cursorIn.getString(cursorIn.getColumnIndex(ContactKey.CITY)));
                        glcConatacts.setLat(cursorIn.getString(cursorIn.getColumnIndex(ContactKey.LAT)));
                        glcConatacts.setLongi(cursorIn.getString(cursorIn.getColumnIndex(ContactKey.LONG)));

                        if (cursorIn.getString(cursorIn.getColumnIndex(ContactKey.LAT)) != null) {
                            modelGlcContactModels.add(glcConatacts);
                        }
                    }
                    while (cursorIn.moveToNext());

                }
               */

            }
            while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return modelGlcContactModels;

    }


    /****************************************************************************
     * @Method:- selectedGlcContactInfoWithMultipleAddressById
     * @purpose: this method use to get selected userData and multiple public and private address (get data in  Two tables)
     ***************************************************************************/


    public ArrayList<GLCContactsModel> selectedMyGlcContactInfoWithMultipleAddressById(String userId, Context context) {

        modelGlcContactModels = new ArrayList<GLCContactsModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_NAME_MYGLC, null, null, null, null, null, null);
        GLCContactsModel glcConatactsMain = null;


        if (cursor.moveToFirst()) {
            do {
                glcConatactsMain = new GLCContactsModel();
                glcConatactsMain.setId(userId);
                glcConatactsMain.setCompanyname(cursor.getString(cursor.getColumnIndex(ContactKey.COMPANY_NAME)));
                glcConatactsMain.setEmail(cursor.getString(cursor.getColumnIndex(ContactKey.EMAIL)));
                glcConatactsMain.setPhone(cursor.getString(cursor.getColumnIndex(ContactKey.PHONE)));
                glcConatactsMain.setAddress(cursor.getString(cursor.getColumnIndex(ContactKey.ADDRESS)));
                glcConatactsMain.setCountry(cursor.getString(cursor.getColumnIndex(ContactKey.COUNTRY)));
                glcConatactsMain.setState(cursor.getString(cursor.getColumnIndex(ContactKey.STATE)));
                glcConatactsMain.setCity(cursor.getString(cursor.getColumnIndex(ContactKey.CITY)));
                glcConatactsMain.setLat(cursor.getString(cursor.getColumnIndex(ContactKey.LAT)));
                glcConatactsMain.setAlternatePhone(cursor.getString(cursor.getColumnIndex(ContactKey.ALTERNATE_PHONE)));
                glcConatactsMain.setLongi(cursor.getString(cursor.getColumnIndex(ContactKey.LONG)));

                if (cursor.getString(cursor.getColumnIndex(ContactKey.LAT)) != null) {
                    modelGlcContactModels.add(glcConatactsMain);
                }

                /*

                Cursor cursorIn = null;
                cursorIn = db.query(TABLE_NAME_MYGLC_PUBLIC_PRIVATE_ADD, null, ContactKey.USER_ID + "=?", new String[]{userId}, null, null, null);

                if (cursorIn.moveToFirst()) {
                    do {

                        //get Defualt multiple address publicprivate table
                        GLCContactsModel glcConatacts = new GLCContactsModel();
                        glcConatacts.setId(cursorIn.getString(cursorIn.getColumnIndex(ContactKey.ADDRESS_ID)));
                        glcConatacts.setCompanyname(cursorIn.getString(cursorIn.getColumnIndex(ContactKey.COMPANY_NAME)));
                        glcConatacts.setEmail(cursorIn.getString(cursorIn.getColumnIndex(ContactKey.EMAIL)));
                        glcConatacts.setPhone(cursorIn.getString(cursorIn.getColumnIndex(ContactKey.PHONE)));
                        glcConatacts.setAddress(cursorIn.getString(cursorIn.getColumnIndex(ContactKey.ADDRESS)));
                        glcConatacts.setCountry(cursorIn.getString(cursorIn.getColumnIndex(ContactKey.COUNTRY)));
                        glcConatacts.setState(cursorIn.getString(cursorIn.getColumnIndex(ContactKey.STATE)));
                        glcConatacts.setCity(cursorIn.getString(cursorIn.getColumnIndex(ContactKey.CITY)));
                        glcConatacts.setLat(cursorIn.getString(cursorIn.getColumnIndex(ContactKey.LAT)));
                        glcConatacts.setLongi(cursorIn.getString(cursorIn.getColumnIndex(ContactKey.LONG)));

                        if (cursorIn.getString(cursorIn.getColumnIndex(ContactKey.LAT)) != null) {
                            modelGlcContactModels.add(glcConatacts);
                        }


                    }
                    while (cursorIn.moveToNext());

                }
                */

            }
            while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return modelGlcContactModels;
    }

    public ArrayList<PoiModel> SelectIce()
    {

        modelPoi = new ArrayList<PoiModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_NAME_POI, null, null, null, null, null, PoiKey.CREATED_DATE + " DESC");


        if (cursor.moveToFirst())
        {
            do
            {
                PoiModel poiModel = new PoiModel();
                poiModel.setId(cursor.getString(cursor.getColumnIndex(PoiKey.ID)));
                poiModel.setPoi_id(cursor.getString(cursor.getColumnIndex(PoiKey.POI_ID)));
                poiModel.setTitle(cursor.getString(cursor.getColumnIndex(PoiKey.TITLE)));
                poiModel.setAddress(cursor.getString(cursor.getColumnIndex(PoiKey.ADDRESS)));
                poiModel.setLat(cursor.getString(cursor.getColumnIndex(PoiKey.LAT)));
                poiModel.setLng(cursor.getString(cursor.getColumnIndex(PoiKey.LONG)));
                poiModel.setNote(cursor.getString(cursor.getColumnIndex(PoiKey.NOTE)));
                poiModel.setImage(cursor.getString(cursor.getColumnIndex(PoiKey.IMAGE)));
                poiModel.setPhone(cursor.getString(cursor.getColumnIndex(PoiKey.PHONE)));
                poiModel.setUrl(cursor.getString(cursor.getColumnIndex(PoiKey.URL)));
                poiModel.setIce_flag(cursor.getString(cursor.getColumnIndex(PoiKey.ICE_FLAG)));
                poiModel.setIce_flag(cursor.getString(cursor.getColumnIndex(PoiKey.ICE_FLAG)));
                poiModel.setIce_message(cursor.getString(cursor.getColumnIndex(PoiKey.ICE_MESSAGE)));
                poiModel.setCreated_date(cursor.getString(cursor.getColumnIndex(PoiKey.CREATED_DATE)));
                poiModel.setSetSelected(false);

                if(cursor.getString(cursor.getColumnIndex(PoiKey.ICE_FLAG)).equalsIgnoreCase("true"))
                {
                    modelPoi.add(poiModel);
                }


            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();

        Collections.sort(modelPoi, new ICEComparator());
        return modelPoi;

    }

    public ArrayList<PoiModel> SelectPoi()
    {

        modelPoi = new ArrayList<PoiModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_NAME_POI, null, null, null, null, null, PoiKey.TITLE + " COLLATE NOCASE ASC;");


        if (cursor.moveToFirst())
        {
            do {


                PoiModel poiModel = new PoiModel();
                poiModel.setId(cursor.getString(cursor.getColumnIndex(PoiKey.ID)));
                poiModel.setPoi_id(cursor.getString(cursor.getColumnIndex(PoiKey.POI_ID)));
                poiModel.setTitle(cursor.getString(cursor.getColumnIndex(PoiKey.TITLE)));
                poiModel.setAddress(cursor.getString(cursor.getColumnIndex(PoiKey.ADDRESS)));
                poiModel.setLat(cursor.getString(cursor.getColumnIndex(PoiKey.LAT)));
                poiModel.setLng(cursor.getString(cursor.getColumnIndex(PoiKey.LONG)));
                poiModel.setNote(cursor.getString(cursor.getColumnIndex(PoiKey.NOTE)));
                poiModel.setNote_public(cursor.getString(cursor.getColumnIndex(PoiKey.NOTE_PUBLIC)));
                poiModel.setImage(cursor.getString(cursor.getColumnIndex(PoiKey.IMAGE)));
                poiModel.setPhone(cursor.getString(cursor.getColumnIndex(PoiKey.PHONE)));
                poiModel.setUrl(cursor.getString(cursor.getColumnIndex(PoiKey.URL)));
                poiModel.setPoi_type(cursor.getString(cursor.getColumnIndex(PoiKey.POI_TYPE)));
                poiModel.setIce_flag(cursor.getString(cursor.getColumnIndex(PoiKey.ICE_FLAG)));
                poiModel.setIce_message(cursor.getString(cursor.getColumnIndex(PoiKey.ICE_MESSAGE)));
                poiModel.setSetSelected(false);


                if(cursor.getString(cursor.getColumnIndex(PoiKey.ICE_FLAG)).equalsIgnoreCase("false"))
                {
                    modelPoi.add(poiModel);
                }



            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();

        return modelPoi;

    }

    public class ICEComparator implements Comparator<PoiModel>
    {
        @Override
        public int compare(PoiModel lhs, PoiModel rhs)
        {
            return rhs.getIce_flag().compareToIgnoreCase(lhs.getIce_flag());
        }
    }


    public ArrayList<PoiModel> SelectPoiWithSelected(final String poiIds) {

        modelPoi = new ArrayList<PoiModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_NAME_POI, null, null, null, null, null, PoiKey.TITLE + " COLLATE NOCASE ASC;");


        if (cursor.moveToFirst()) {
            do {


                PoiModel poiModel = new PoiModel();
                poiModel.setId(cursor.getString(cursor.getColumnIndex(PoiKey.ID)));
                poiModel.setPoi_id(cursor.getString(cursor.getColumnIndex(PoiKey.POI_ID)));
                poiModel.setTitle(cursor.getString(cursor.getColumnIndex(PoiKey.TITLE)));
                poiModel.setAddress(cursor.getString(cursor.getColumnIndex(PoiKey.ADDRESS)));
                poiModel.setLat(cursor.getString(cursor.getColumnIndex(PoiKey.LAT)));
                poiModel.setLng(cursor.getString(cursor.getColumnIndex(PoiKey.LONG)));
                poiModel.setNote(cursor.getString(cursor.getColumnIndex(PoiKey.NOTE)));
                poiModel.setNote_public(cursor.getString(cursor.getColumnIndex(PoiKey.NOTE_PUBLIC)));
                poiModel.setImage(cursor.getString(cursor.getColumnIndex(PoiKey.IMAGE)));
                poiModel.setPhone(cursor.getString(cursor.getColumnIndex(PoiKey.PHONE)));
                poiModel.setUrl(cursor.getString(cursor.getColumnIndex(PoiKey.URL)));
                poiModel.setIce_flag(cursor.getString(cursor.getColumnIndex(PoiKey.ICE_FLAG)));
                poiModel.setIce_message(cursor.getString(cursor.getColumnIndex(PoiKey.ICE_MESSAGE)));

                if (poiIds.contains(cursor.getString(cursor.getColumnIndex(PoiKey.POI_ID)))) {
                    poiModel.setSetSelected(true);
                } else {
                    poiModel.setSetSelected(false);
                }

                modelPoi.add(poiModel);

            } while (cursor.moveToNext());

        }


        //Log.d("Helper", "poi size==" + modelPoi.size());
        cursor.close();
        db.close();
        return modelPoi;

    }

    public ArrayList<GroupModel> SelectGroup() {

        modelGroup = new ArrayList<GroupModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_NAME_GROUP, null, null, null, null, null, GroupKey.MODIFIED_DATE + " DESC");

        //Log.e("", "Count:-" + cursor.getCount());

        if (cursor.moveToFirst()) {
            do {


                GroupModel groupModel = new GroupModel();
                groupModel.setId(cursor.getString(cursor.getColumnIndex(GroupKey.ID)));
                groupModel.setGroup_id(cursor.getString(cursor.getColumnIndex(GroupKey.GROUP_ID)));
                groupModel.setGroup_name(cursor.getString(cursor.getColumnIndex(GroupKey.GROUP_NAME)));
                groupModel.setCreated_by_id(cursor.getString(cursor.getColumnIndex(GroupKey.CREATED_BY_ID)));
                groupModel.setCreated_date(cursor.getString(cursor.getColumnIndex(GroupKey.CREATED_DATE)));
                groupModel.setModified_date(cursor.getString(cursor.getColumnIndex(GroupKey.MODIFIED_DATE)));
                groupModel.setTotal_number(cursor.getString(cursor.getColumnIndex(GroupKey.TOTAL_NUMBER)));
                groupModel.setCreated_by_user(cursor.getString(cursor.getColumnIndex(GroupKey.ISCREATEDBYEUSER)));

                modelGroup.add(groupModel);

            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return modelGroup;

    }


    public ArrayList<NotificationModel> SelectNotification() {

        final ArrayList<NotificationModel> arrNotificationModel = new ArrayList<NotificationModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_NAME_NOTIFICATION, null, null, null, null, null, NotificationKey.TIMESTAMP + " DESC");

        //Log.e("", "Count:-" + cursor.getCount());

        if (cursor.moveToFirst()) {
            do {


                NotificationModel notificationModel = new NotificationModel();
                notificationModel.setId(cursor.getString(cursor.getColumnIndex(NotificationKey.ID)));
                notificationModel.setNotification_id(cursor.getString(cursor.getColumnIndex(NotificationKey.NOTIFICATION_ID)));
                notificationModel.setMessage(cursor.getString(cursor.getColumnIndex(NotificationKey.MESSAGE)));
                notificationModel.setSender_name(cursor.getString(cursor.getColumnIndex(NotificationKey.SENDER_NAME)));
                notificationModel.setStatus(cursor.getString(cursor.getColumnIndex(NotificationKey.STATUS)));
                notificationModel.setShare_type(cursor.getString(cursor.getColumnIndex(NotificationKey.SHARE_TYPE)));
                notificationModel.setShare_type_id(cursor.getString(cursor.getColumnIndex(NotificationKey.SHARE_TYPE_ID)));
                notificationModel.setCreated_by_user(cursor.getString(cursor.getColumnIndex(NotificationKey.CREATED_BY_USER)));
                notificationModel.setMethod_type(cursor.getString(cursor.getColumnIndex(NotificationKey.METHOD_TYPE)));
                notificationModel.setTimestamp(cursor.getString(cursor.getColumnIndex(NotificationKey.TIMESTAMP)));
                notificationModel.setUser_id(cursor.getString(cursor.getColumnIndex(NotificationKey.USER_ID)));
                notificationModel.setFolderId(cursor.getString(cursor.getColumnIndex(NotificationKey.FOLDER_ID)));
                notificationModel.setLatitude(cursor.getString(cursor.getColumnIndex(NotificationKey.LALITUDE)));
                notificationModel.setLongitude(cursor.getString(cursor.getColumnIndex(NotificationKey.LONGITUDE)));
                notificationModel.setAddress(cursor.getString(cursor.getColumnIndex(NotificationKey.ADDRESS)));
                notificationModel.setTitle(cursor.getString(cursor.getColumnIndex(NotificationKey.TITLE)));
                notificationModel.setGroupID(cursor.getString(cursor.getColumnIndex(NotificationKey.GROUP_ID)));
                notificationModel.setGroupHistoryID(cursor.getString(cursor.getColumnIndex(NotificationKey.GROUP_HISTORY_ID)));
                notificationModel.setGroupName(cursor.getString(cursor.getColumnIndex(NotificationKey.GROUP_NAME)));

                arrNotificationModel.add(notificationModel);

            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return arrNotificationModel;

    }


    public ArrayList<CategoryModel> SelectCategory() {

        modelCategory = new ArrayList<CategoryModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_NAME_CATEGORY, null, null, null, null, null, CategoryKey.CATEGORY_NAME + " COLLATE NOCASE ASC;");

        //Log.e("", "Count:-" + cursor.getCount());

        if (cursor.moveToFirst()) {
            do {
                CategoryModel categoryModel = new CategoryModel();
                categoryModel.setId(cursor.getString(cursor.getColumnIndex(CategoryKey.ID)));
                categoryModel.setCategory_id(cursor.getString(cursor.getColumnIndex(CategoryKey.CATEGORY_ID)));
                categoryModel.setCategory_name(cursor.getString(cursor.getColumnIndex(CategoryKey.CATEGORY_NAME)));
                categoryModel.setCreated_by_user(cursor.getString(cursor.getColumnIndex(CategoryKey.CREATED_BY_USER)));
                categoryModel.setCreated_date(cursor.getString(cursor.getColumnIndex(CategoryKey.CREATED_DATE)));
                categoryModel.setModified_date(cursor.getString(cursor.getColumnIndex(CategoryKey.MODIFIED_DATE)));
                categoryModel.setSelected(false);
                modelCategory.add(categoryModel);

            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return modelCategory;

    }


    public String SelectCategoryExportCSVById(String cateId) {
        String categoryName = "";

        if (cateId.equalsIgnoreCase("0") || cateId.equalsIgnoreCase("")) {
            categoryName = "Recent Trips";
        } else {
            Cursor cursor = null;
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.query(TABLE_NAME_CATEGORY, null, CategoryKey.CATEGORY_ID + "=?", new String[]{cateId}, null, null, null);

            if (cursor.moveToFirst()) {
                categoryName = cursor.getString(cursor.getColumnIndex(CategoryKey.CATEGORY_NAME));

            }
            cursor.close();
            db.close();
        }


        //Log.d("categoryName","categoryName=="+categoryName+"==cateId=="+cateId);
        return categoryName;

    }


    public ArrayList<CategoryModel> selectCategoryExportCSV() {

        modelCategory = new ArrayList<CategoryModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_NAME_CATEGORY, null, null, null, null, null, CategoryKey.CATEGORY_NAME + " COLLATE NOCASE ASC;");

        CategoryModel categoryModelAll = new CategoryModel();
        categoryModelAll.setId("0");
        categoryModelAll.setCategory_id("0");
        categoryModelAll.setCategory_name("Recent Trips");
        categoryModelAll.setCreated_by_user("Me");
        categoryModelAll.setCreated_date("");
        categoryModelAll.setModified_date("");
        categoryModelAll.setSelected(false);
        categoryModelAll.setTripsTotal("0");
        modelCategory.add(categoryModelAll);

        if (cursor.moveToFirst())
        {
            do
            {
                CategoryModel categoryModel = new CategoryModel();
                categoryModel.setId(cursor.getString(cursor.getColumnIndex(CategoryKey.ID)));
                categoryModel.setCategory_id(cursor.getString(cursor.getColumnIndex(CategoryKey.CATEGORY_ID)));
                categoryModel.setCategory_name(cursor.getString(cursor.getColumnIndex(CategoryKey.CATEGORY_NAME)));
                categoryModel.setCreated_by_user(cursor.getString(cursor.getColumnIndex(CategoryKey.CREATED_BY_USER)));
                categoryModel.setCreated_date(cursor.getString(cursor.getColumnIndex(CategoryKey.CREATED_DATE)));
                categoryModel.setModified_date(cursor.getString(cursor.getColumnIndex(CategoryKey.MODIFIED_DATE)));
                categoryModel.setTripsTotal("0");
                categoryModel.setSelected(false);
                modelCategory.add(categoryModel);

            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return modelCategory;

    }

    public ArrayList<CategoryModel> SelectCategoryMove(boolean iscategory, String categoryId) {

        modelCategory = new ArrayList<CategoryModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        if (TextUtils.isEmpty(categoryId)) {
            cursor = db.query(TABLE_NAME_CATEGORY, null, null, null, null, null, CategoryKey.CATEGORY_NAME + " COLLATE NOCASE ASC;");
        } else {
            cursor = db.query(TABLE_NAME_CATEGORY, null, CategoryKey.CATEGORY_ID + "!=?", new String[]{categoryId}, null, null, CategoryKey.CATEGORY_NAME + " COLLATE NOCASE ASC;");
        }
        //Log.e("", "Count:-" + cursor.getCount());

        if (iscategory) {
            CategoryModel categoryModelAll = new CategoryModel();
            categoryModelAll.setId("0");
            categoryModelAll.setCategory_id("0");
            categoryModelAll.setCategory_name("Recent Trips");
            categoryModelAll.setCreated_by_user("Me");
            categoryModelAll.setCreated_date("");
            categoryModelAll.setModified_date("");
            modelCategory.add(categoryModelAll);
        }


        if (cursor.moveToFirst()) {
            do {
                CategoryModel categoryModel = new CategoryModel();
                categoryModel.setId(cursor.getString(cursor.getColumnIndex(CategoryKey.ID)));
                categoryModel.setCategory_id(cursor.getString(cursor.getColumnIndex(CategoryKey.CATEGORY_ID)));
                categoryModel.setCategory_name(cursor.getString(cursor.getColumnIndex(CategoryKey.CATEGORY_NAME)));
                categoryModel.setCreated_by_user(cursor.getString(cursor.getColumnIndex(CategoryKey.CREATED_BY_USER)));
                categoryModel.setCreated_date(cursor.getString(cursor.getColumnIndex(CategoryKey.CREATED_DATE)));
                categoryModel.setModified_date(cursor.getString(cursor.getColumnIndex(CategoryKey.MODIFIED_DATE)));
                modelCategory.add(categoryModel);

            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return modelCategory;

    }


    public ArrayList<LogBookModel> SelectLogBook()
    {

        modelLogBook = new ArrayList<LogBookModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();

        cursor = db.query(TABLE_NAME_LOG_BOOK, null, LogBookKey.CATEGORY_ID + "=? OR " + LogBookKey.CATEGORY_ID + "=? ", new String[]{"0", ""}, null, null, LogBookKey.TIMESTAMP + " DESC");


        if (cursor.moveToFirst())
        {
            do {
                LogBookModel logBookModel = new LogBookModel();
                logBookModel.setId(cursor.getString(cursor.getColumnIndex(LogBookKey.ID)));
                logBookModel.setCategory_id(cursor.getString(cursor.getColumnIndex(LogBookKey.CATEGORY_ID)));
                logBookModel.setLogbook_id(cursor.getString(cursor.getColumnIndex(LogBookKey.LOG_BOOK_ID)));
                logBookModel.setShare_type_id(cursor.getString(cursor.getColumnIndex(LogBookKey.SHARE_TYPE_ID)));
                logBookModel.setShare_type(cursor.getString(cursor.getColumnIndex(LogBookKey.SHARE_TYPE)));
                logBookModel.setDuration(cursor.getString(cursor.getColumnIndex(LogBookKey.DURATION)));
                logBookModel.setStart_date_time(cursor.getString(cursor.getColumnIndex(LogBookKey.START_DATE_TIME)));
                logBookModel.setEnd_date_time(cursor.getString(cursor.getColumnIndex(LogBookKey.END_DATE_TIME)));
                logBookModel.setStart_address(cursor.getString(cursor.getColumnIndex(LogBookKey.START_ADDRESS)));
                logBookModel.setEnd_address(cursor.getString(cursor.getColumnIndex(LogBookKey.END_ADDRESS)));
                logBookModel.setStart_latlong(cursor.getString(cursor.getColumnIndex(LogBookKey.START_LATLONG)));
                logBookModel.setEnd_latlong(cursor.getString(cursor.getColumnIndex(LogBookKey.END_LATLONG)));
                logBookModel.setKilometer(cursor.getString(cursor.getColumnIndex(LogBookKey.KILOMETER)));
                logBookModel.setTimestamp(cursor.getString(cursor.getColumnIndex(LogBookKey.TIMESTAMP)));
                logBookModel.setTitle(cursor.getString(cursor.getColumnIndex(LogBookKey.TITLE)));
                logBookModel.setIsSelected(false);
                logBookModel.setTrip_status(cursor.getString(cursor.getColumnIndex(LogBookKey.TRIP_STATUS)));
                logBookModel.setExpected_latlong(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_LATLONG)));
                logBookModel.setExpected_address(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_ADDRESS)));
                logBookModel.setExpected_distance(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_DISTANCE)));
                logBookModel.setExpected_duration(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_DURATION)));

                modelLogBook.add(logBookModel);

            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return modelLogBook;

    }


    public ArrayList<LogBookModel> selectTripEndedLogBook()
    {

        modelLogBook = new ArrayList<LogBookModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();

        cursor = db.query(TABLE_NAME_LOG_BOOK, null, LogBookKey.CATEGORY_ID + "=? OR " + LogBookKey.CATEGORY_ID + "=? ", new String[]{"0", ""}, null, null, LogBookKey.TIMESTAMP + " DESC");


        if (cursor.moveToFirst()) {
            do {
                LogBookModel logBookModel = new LogBookModel();
                logBookModel.setId(cursor.getString(cursor.getColumnIndex(LogBookKey.ID)));
                logBookModel.setCategory_id(cursor.getString(cursor.getColumnIndex(LogBookKey.CATEGORY_ID)));
                logBookModel.setLogbook_id(cursor.getString(cursor.getColumnIndex(LogBookKey.LOG_BOOK_ID)));
                logBookModel.setShare_type_id(cursor.getString(cursor.getColumnIndex(LogBookKey.SHARE_TYPE_ID)));
                logBookModel.setShare_type(cursor.getString(cursor.getColumnIndex(LogBookKey.SHARE_TYPE)));
                logBookModel.setDuration(cursor.getString(cursor.getColumnIndex(LogBookKey.DURATION)));
                logBookModel.setStart_date_time(cursor.getString(cursor.getColumnIndex(LogBookKey.START_DATE_TIME)));
                logBookModel.setEnd_date_time(cursor.getString(cursor.getColumnIndex(LogBookKey.END_DATE_TIME)));
                logBookModel.setStart_address(cursor.getString(cursor.getColumnIndex(LogBookKey.START_ADDRESS)));
                logBookModel.setEnd_address(cursor.getString(cursor.getColumnIndex(LogBookKey.END_ADDRESS)));
                logBookModel.setStart_latlong(cursor.getString(cursor.getColumnIndex(LogBookKey.START_LATLONG)));
                logBookModel.setEnd_latlong(cursor.getString(cursor.getColumnIndex(LogBookKey.END_LATLONG)));
                logBookModel.setKilometer(cursor.getString(cursor.getColumnIndex(LogBookKey.KILOMETER)));
                logBookModel.setTimestamp(cursor.getString(cursor.getColumnIndex(LogBookKey.TIMESTAMP)));
                logBookModel.setTitle(cursor.getString(cursor.getColumnIndex(LogBookKey.TITLE)));
                logBookModel.setIsSelected(false);
                logBookModel.setTrip_status(cursor.getString(cursor.getColumnIndex(LogBookKey.TRIP_STATUS)));
                logBookModel.setExpected_latlong(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_LATLONG)));
                logBookModel.setExpected_address(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_ADDRESS)));
                logBookModel.setExpected_distance(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_DISTANCE)));
                logBookModel.setExpected_duration(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_DURATION)));

                if(cursor.getString(cursor.getColumnIndex(LogBookKey.TRIP_STATUS)).equalsIgnoreCase("end"))
                {
                    modelLogBook.add(logBookModel);
                }


            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return modelLogBook;

    }


    public ArrayList<SubFolderCidPoiModel> subFoldersCidPoi(final String folder_id, final String subFolderId,final String userid)
    {

        modelSubFolderCidPoi = new ArrayList<SubFolderCidPoiModel>();
        Cursor cursor = null;
        Cursor cursorCidPoi = null;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_NAME_SUB_FOLDER, null, SubFolderKey.FOLDER_ID + "=?", new String[]{folder_id.trim()}, null, null, SubFolderKey.SUB_FOLDER_NAME + " COLLATE NOCASE ASC;");
        String whereClues = FolderContactsPoiKey.FOLDER_ID + " =? AND " + FolderContactsPoiKey.SUB_FOLDER_ID + " =? ";
        cursorCidPoi = db.query(TABLE_NAME_FOLDER_CONTACTS_POI, null, whereClues, new String[]{folder_id, subFolderId.trim()}, null, null, FolderContactsPoiKey.TITLE + " COLLATE NOCASE ASC;");



        if (cursor.moveToFirst()) {
            do {
                SubFolderCidPoiModel folderModel = new SubFolderCidPoiModel();
                folderModel.setId(cursor.getString(cursor.getColumnIndex(SubFolderKey.ID)));
                folderModel.setFolder_id(cursor.getString(cursor.getColumnIndex(SubFolderKey.FOLDER_ID)));
                folderModel.setType(Constans.FOLDER);
                folderModel.setSub_folder_id(cursor.getString(cursor.getColumnIndex(SubFolderKey.SUB_FOLDER_ID)));
                folderModel.setSub_folder_name(cursor.getString(cursor.getColumnIndex(SubFolderKey.SUB_FOLDER_NAME)));
                folderModel.setSub_folderLock(cursor.getString(cursor.getColumnIndex(SubFolderKey.SUB_FOLDER_LOCK)));
                folderModel.setSub_folderTags(cursor.getString(cursor.getColumnIndex(SubFolderKey.SUB_FOLDER_TAGS)));
                folderModel.setCreated_by_user_id(cursor.getString(cursor.getColumnIndex(SubFolderKey.CREATED_BY_USER_ID)));
                folderModel.setCreated_by_user(cursor.getString(cursor.getColumnIndex(SubFolderKey.CREATED_BY_USER)));
                folderModel.setSub_folderLockUserId(cursor.getString(cursor.getColumnIndex(SubFolderKey.SUB_FOLDER_LOCK_USERID)));


                if (userid.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(SubFolderKey.SUB_FOLDER_LOCK_USERID)))) {
                    modelSubFolderCidPoi.add(folderModel);
                } else {
                    if (!Boolean.valueOf(cursor.getString(cursor.getColumnIndex(SubFolderKey.SUB_FOLDER_LOCK)))) {
                        modelSubFolderCidPoi.add(folderModel);
                    }
                }


            } while (cursor.moveToNext());

        }


        if (cursorCidPoi.moveToFirst()) {
            do {
                SubFolderCidPoiModel folderModel = new SubFolderCidPoiModel();
                folderModel.setId(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.ID)));
                folderModel.setFolder_id(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.FOLDER_ID)));
                folderModel.setCid_poi_id(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.FOLDER_CONTACT_POI_ID)));
                folderModel.setType(Constans.CIDPOI);
                folderModel.setSub_folder_id(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.SUB_FOLDER_ID)));
                folderModel.setTitle(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.TITLE)));
                folderModel.setLatlong(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.LATLONG)));
                folderModel.setAddress(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.ADDRESS)));
                folderModel.setShare_type(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.SHARE_TYPE)));
                folderModel.setShare_type_id(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.SHARE_TYPE_ID)));
                folderModel.setNote(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.NOTE)));
                folderModel.setNote_public(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.NOTE_PUBLIC)));
                folderModel.setPhone(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.Phone)));
                folderModel.setUrl(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.URL)));
                folderModel.setPic(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.PIC)));
                folderModel.setContacts_id(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.CONTACT_ID)));
                modelSubFolderCidPoi.add(folderModel);

            } while (cursorCidPoi.moveToNext());

        }
        cursorCidPoi.close();
        cursor.close();
        db.close();
        return modelSubFolderCidPoi;

    }


    public ArrayList<SubFolderCidPoiModel> selectSubFoldersCidPoi(final String folder_id, final String subFolderId)
    {

        modelSubFolderCidPoi = new ArrayList<SubFolderCidPoiModel>();
        Cursor cursor = null;
        Cursor cursorCidPoi = null;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_NAME_SUB_FOLDER, null, SubFolderKey.FOLDER_ID + "=?", new String[]{folder_id.trim()}, null, null, SubFolderKey.SUB_FOLDER_NAME + " COLLATE NOCASE ASC;");
        String whereClues = FolderContactsPoiKey.FOLDER_ID + " =? AND " + FolderContactsPoiKey.SUB_FOLDER_ID + " =? ";
        cursorCidPoi = db.query(TABLE_NAME_FOLDER_CONTACTS_POI, null, whereClues, new String[]{folder_id, subFolderId.trim()}, null, null, FolderContactsPoiKey.TITLE + " COLLATE NOCASE ASC;");

        //Log.d("TABLE_NAME_SUB_FOLDER", "getCount==" + cursor.getCount());

        if (cursor.moveToFirst()) {
            do {
                SubFolderCidPoiModel folderModel = new SubFolderCidPoiModel();
                folderModel.setId(cursor.getString(cursor.getColumnIndex(SubFolderKey.ID)));
                folderModel.setFolder_id(cursor.getString(cursor.getColumnIndex(SubFolderKey.FOLDER_ID)));
                folderModel.setType(Constans.FOLDER);
                folderModel.setSub_folder_id(cursor.getString(cursor.getColumnIndex(SubFolderKey.SUB_FOLDER_ID)));
                folderModel.setSub_folder_name(cursor.getString(cursor.getColumnIndex(SubFolderKey.SUB_FOLDER_NAME)));
                folderModel.setSub_folderLock(cursor.getString(cursor.getColumnIndex(SubFolderKey.SUB_FOLDER_LOCK)));
                folderModel.setSub_folderTags(cursor.getString(cursor.getColumnIndex(SubFolderKey.SUB_FOLDER_TAGS)));
                folderModel.setCreated_by_user_id(cursor.getString(cursor.getColumnIndex(SubFolderKey.CREATED_BY_USER_ID)));
                folderModel.setCreated_by_user(cursor.getString(cursor.getColumnIndex(SubFolderKey.CREATED_BY_USER)));
                folderModel.setSub_folderLockUserId(cursor.getString(cursor.getColumnIndex(SubFolderKey.SUB_FOLDER_LOCK_USERID)));


                Log.d("selectSubFoldersCidPoi", "selectSubFoldersCidPoi==" + cursor.getString(cursor.getColumnIndex(SubFolderKey.SUB_FOLDER_LOCK)));

                modelSubFolderCidPoi.add(folderModel);

            } while (cursor.moveToNext());

        }


        if (cursorCidPoi.moveToFirst()) {
            do {
                SubFolderCidPoiModel folderModel = new SubFolderCidPoiModel();
                folderModel.setId(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.ID)));
                folderModel.setFolder_id(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.FOLDER_ID)));
                folderModel.setCid_poi_id(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.FOLDER_CONTACT_POI_ID)));
                folderModel.setType(Constans.CIDPOI);
                folderModel.setSub_folder_id(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.SUB_FOLDER_ID)));
                folderModel.setTitle(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.TITLE)));
                folderModel.setLatlong(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.LATLONG)));
                folderModel.setAddress(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.ADDRESS)));
                folderModel.setShare_type(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.SHARE_TYPE)));
                folderModel.setShare_type_id(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.SHARE_TYPE_ID)));
                folderModel.setNote(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.NOTE)));
                folderModel.setNote_public(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.NOTE_PUBLIC)));
                folderModel.setPhone(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.Phone)));
                folderModel.setUrl(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.URL)));
                folderModel.setPic(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.PIC)));
                folderModel.setContacts_id(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.CONTACT_ID)));
                modelSubFolderCidPoi.add(folderModel);

            } while (cursorCidPoi.moveToNext());

        }
        cursorCidPoi.close();
        cursor.close();
        db.close();
        return modelSubFolderCidPoi;

    }


    public ArrayList<SubFolderCidPoiModel> selectSubFoldersCidPoiMove(final String folder_id, final String subFolderId, String cidpoiId, String moveSubFolderId, String userid) {

        modelSubFolderCidPoi = new ArrayList<SubFolderCidPoiModel>();
        Cursor cursor = null;
        Cursor cursorCidPoi = null;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_NAME_SUB_FOLDER, null, SubFolderKey.FOLDER_ID + "=?", new String[]{folder_id.trim()}, null, null, SubFolderKey.SUB_FOLDER_NAME + " COLLATE NOCASE ASC;");
        String whereClues = FolderContactsPoiKey.FOLDER_ID + " =? AND " + FolderContactsPoiKey.SUB_FOLDER_ID + " =? ";
        cursorCidPoi = db.query(TABLE_NAME_FOLDER_CONTACTS_POI, null, whereClues, new String[]{folder_id, subFolderId.trim()}, null, null, FolderContactsPoiKey.TITLE + " COLLATE NOCASE ASC;");


        if (cursor.moveToFirst()) {
            do {
                SubFolderCidPoiModel folderModel = new SubFolderCidPoiModel();
                folderModel.setId(cursor.getString(cursor.getColumnIndex(SubFolderKey.ID)));
                folderModel.setFolder_id(cursor.getString(cursor.getColumnIndex(SubFolderKey.FOLDER_ID)));
                folderModel.setType(Constans.FOLDER);
                folderModel.setSub_folder_id(cursor.getString(cursor.getColumnIndex(SubFolderKey.SUB_FOLDER_ID)));
                folderModel.setSub_folder_name(cursor.getString(cursor.getColumnIndex(SubFolderKey.SUB_FOLDER_NAME)));

                if (!moveSubFolderId.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(SubFolderKey.SUB_FOLDER_ID))))
                {

                    if (userid.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(SubFolderKey.SUB_FOLDER_LOCK_USERID)))) {
                        modelSubFolderCidPoi.add(folderModel);
                    } else {
                        if (!Boolean.valueOf(cursor.getString(cursor.getColumnIndex(SubFolderKey.SUB_FOLDER_LOCK)))) {
                            modelSubFolderCidPoi.add(folderModel);
                        }
                    }


                }

            } while (cursor.moveToNext());

        }


        if (cursorCidPoi.moveToFirst()) {
            do {
                SubFolderCidPoiModel folderModel = new SubFolderCidPoiModel();
                folderModel.setId(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.ID)));
                folderModel.setFolder_id(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.FOLDER_ID)));
                folderModel.setCid_poi_id(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.FOLDER_CONTACT_POI_ID)));
                folderModel.setType(Constans.CIDPOI);
                folderModel.setSub_folder_id(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.SUB_FOLDER_ID)));
                folderModel.setTitle(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.TITLE)));
                folderModel.setLatlong(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.LATLONG)));
                folderModel.setAddress(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.ADDRESS)));
                folderModel.setShare_type(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.SHARE_TYPE)));
                folderModel.setShare_type_id(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.SHARE_TYPE_ID)));
                folderModel.setNote(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.NOTE)));
                folderModel.setNote_public(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.NOTE_PUBLIC)));
                folderModel.setPhone(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.Phone)));
                folderModel.setUrl(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.URL)));
                folderModel.setPic(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.PIC)));
                folderModel.setContacts_id(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.CONTACT_ID)));


                if (!cidpoiId.equalsIgnoreCase(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.SHARE_TYPE_ID)))) {
                    modelSubFolderCidPoi.add(folderModel);
                }


            } while (cursorCidPoi.moveToNext());

        }
        cursorCidPoi.close();
        cursor.close();
        db.close();
        return modelSubFolderCidPoi;

    }

    public ArrayList<FolderContacsPoiModel> selectFoldersCidPoi(final String folder_id) {

        modelFoldersCidPoi = new ArrayList<FolderContacsPoiModel>();
        //Cursor cursor = null;
        Cursor cursorCidPoi = null;
        SQLiteDatabase db = this.getWritableDatabase();
        //cursor = db.query(TABLE_NAME_SUB_FOLDER, null, SubFolderKey.FOLDER_ID + "=?", new String[]{folder_id}, null, null, null);
        cursorCidPoi = db.query(TABLE_NAME_FOLDER_CONTACTS_POI, null, FolderContactsPoiKey.SUB_FOLDER_ID + "=?", new String[]{folder_id}, null, null, FolderContactsPoiKey.TITLE + " COLLATE NOCASE ASC;");

//        if (cursor.moveToFirst())
//        {
//            do
//            {
//                SubFolderCidPoiModel folderModel = new SubFolderCidPoiModel();
//                folderModel.setId(cursor.getString(cursor.getColumnIndex(SubFolderKey.ID)));
//                folderModel.setFolder_id(cursor.getString(cursor.getColumnIndex(SubFolderKey.FOLDER_ID)));
//                folderModel.setType("folder");
//                folderModel.setSub_folder_id(cursor.getString(cursor.getColumnIndex(SubFolderKey.SUB_FOLDER_ID)));
//                folderModel.setSub_folder_name(cursor.getString(cursor.getColumnIndex(SubFolderKey.SUB_FOLDER_NAME)));
//                modelSubFolderCidPoi.add(folderModel);
//
//            } while (cursor.moveToNext());
//
//        }


        if (cursorCidPoi.moveToFirst()) {
            do {
                FolderContacsPoiModel folderModel = new FolderContacsPoiModel();
                folderModel.setId(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.ID)));
                folderModel.setFolder_contacs_poi_id(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.FOLDER_CONTACT_POI_ID)));
                folderModel.setFolder_id(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.FOLDER_ID)));
                folderModel.setSub_folder_id(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.SUB_FOLDER_ID)));
                folderModel.setTitle(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.TITLE)));
                folderModel.setLatlong(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.LATLONG)));
                folderModel.setAddress(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.ADDRESS)));
                folderModel.setShare_type(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.SHARE_TYPE)));
                folderModel.setShare_type_id(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.SHARE_TYPE_ID)));
                folderModel.setNote(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.NOTE)));
                folderModel.setNote_public(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.NOTE_PUBLIC)));
                folderModel.setPhone(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.Phone)));
                folderModel.setUrl(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.URL)));
                folderModel.setPic(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.PIC)));
                folderModel.setContacts_id(cursorCidPoi.getString(cursorCidPoi.getColumnIndex(FolderContactsPoiKey.CONTACT_ID)));
                modelFoldersCidPoi.add(folderModel);

            } while (cursorCidPoi.moveToNext());

        }
        cursorCidPoi.close();
        db.close();
        return modelFoldersCidPoi;

    }


    public String getRootFolderLock(final String sub_folder_id) {

        String isFolderLock = "false";

        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_NAME_SUB_FOLDER, null, SubFolderKey.SUB_FOLDER_ID + "=?", new String[]{sub_folder_id}, null, null, null);

        Log.d("getRootFolderLock", "cursor.getCount()==" + cursor.getCount());

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                isFolderLock = cursor.getString(cursor.getColumnIndex(SubFolderKey.SUB_FOLDER_LOCK));
                Log.d("getRootFolderLock", "cursor.getCount()==" + cursor.getString(cursor.getColumnIndex(SubFolderKey.SUB_FOLDER_NAME)) + "===LOCK==" + cursor.getString(cursor.getColumnIndex(SubFolderKey.SUB_FOLDER_LOCK)));
            }

        }

        cursor.close();
        db.close();
        return isFolderLock;

    }

    public ArrayList<FolderModel> selectFolders() {

        modelFolder = new ArrayList<FolderModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_NAME_FOLDER, null, null, null, null, null, FolderKey.FOLDER_NAME + " COLLATE NOCASE ASC;");
        //cursor = db.query(TABLE_NAME_FOLDER, null, LogBookKey.CATEGORY_ID + "=?", new String[]{categoryId}, null, null, null);

        //Log.e("", "Count:-" + cursor.getCount());

        if (cursor.moveToFirst()) {
            do {
                FolderModel folderModel = new FolderModel();
                folderModel.setId(cursor.getString(cursor.getColumnIndex(FolderKey.ID)));
                folderModel.setFolder_id(cursor.getString(cursor.getColumnIndex(FolderKey.FOLDER_ID)));
                folderModel.setFolder_name(cursor.getString(cursor.getColumnIndex(FolderKey.FOLDER_NAME)));
                folderModel.setCreated_by_user(cursor.getString(cursor.getColumnIndex(FolderKey.CREATED_BY_USER)));
                folderModel.setCreated_date(cursor.getString(cursor.getColumnIndex(FolderKey.CREATED_DATE)));
                folderModel.setModified_date(cursor.getString(cursor.getColumnIndex(FolderKey.MODIFIED_DATE)));
                folderModel.setNo_of_poi(cursor.getString(cursor.getColumnIndex(FolderKey.NO_OF_CIDPOI)));
                folderModel.setFolderLock(cursor.getString(cursor.getColumnIndex(FolderKey.FOLDER_LOCK)));
                folderModel.setFolderTags(cursor.getString(cursor.getColumnIndex(FolderKey.FOLDER_TAGS)));
                folderModel.setCreated_by_user_id(cursor.getString(cursor.getColumnIndex(FolderKey.CREATED_BY_USER_ID)));
                folderModel.setFolderLockUserId(cursor.getString(cursor.getColumnIndex(FolderKey.FOLDER_LOCK_USERID)));
                folderModel.setNo_of_subfolder(cursor.getString(cursor.getColumnIndex(FolderKey.NO_OF_SUBFOLDER)));
                modelFolder.add(folderModel);

            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return modelFolder;

    }

    public ArrayList<FolderModel> selectFoldersMoveCidPoi(String userId) {

        modelFolder = new ArrayList<FolderModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_NAME_FOLDER, null, null, null, null, null, FolderKey.FOLDER_NAME + " COLLATE NOCASE ASC;");
        //cursor = db.query(TABLE_NAME_FOLDER, null, LogBookKey.CATEGORY_ID + "=?", new String[]{categoryId}, null, null, null);

        //Log.e("", "Count:-" + cursor.getCount());

        if (cursor.moveToFirst()) {
            do {
                FolderModel folderModel = new FolderModel();
                folderModel.setId(cursor.getString(cursor.getColumnIndex(FolderKey.ID)));
                folderModel.setFolder_id(cursor.getString(cursor.getColumnIndex(FolderKey.FOLDER_ID)));
                folderModel.setFolder_name(cursor.getString(cursor.getColumnIndex(FolderKey.FOLDER_NAME)));
                folderModel.setCreated_by_user(cursor.getString(cursor.getColumnIndex(FolderKey.CREATED_BY_USER)));
                folderModel.setCreated_date(cursor.getString(cursor.getColumnIndex(FolderKey.CREATED_DATE)));
                folderModel.setModified_date(cursor.getString(cursor.getColumnIndex(FolderKey.MODIFIED_DATE)));
                folderModel.setNo_of_poi(cursor.getString(cursor.getColumnIndex(FolderKey.NO_OF_CIDPOI)));
                folderModel.setFolderLock(cursor.getString(cursor.getColumnIndex(FolderKey.FOLDER_LOCK)));
                folderModel.setFolderTags(cursor.getString(cursor.getColumnIndex(FolderKey.FOLDER_TAGS)));
                folderModel.setCreated_by_user_id(cursor.getString(cursor.getColumnIndex(FolderKey.CREATED_BY_USER_ID)));
                folderModel.setFolderLockUserId(cursor.getString(cursor.getColumnIndex(FolderKey.FOLDER_LOCK_USERID)));
                folderModel.setNo_of_subfolder(cursor.getString(cursor.getColumnIndex(FolderKey.NO_OF_SUBFOLDER)));


                if (userId.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(FolderKey.FOLDER_LOCK_USERID)))) {
                    modelFolder.add(folderModel);
                } else {
                    if (!Boolean.valueOf(cursor.getString(cursor.getColumnIndex(FolderKey.FOLDER_LOCK)))) {
                        modelFolder.add(folderModel);
                    }
                }


            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return modelFolder;

    }

    public ArrayList<FolderModel> selectFoldersMove(final String folderId, final String userId, final String start) {

        modelFolder = new ArrayList<FolderModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();

        cursor = db.query(TABLE_NAME_FOLDER, null, null, null, null, null, FolderKey.FOLDER_NAME + " COLLATE NOCASE ASC;");


        if (cursor.moveToFirst()) {
            do {
                FolderModel folderModel = new FolderModel();
                folderModel.setId(cursor.getString(cursor.getColumnIndex(FolderKey.ID)));
                folderModel.setFolder_id(cursor.getString(cursor.getColumnIndex(FolderKey.FOLDER_ID)));
                folderModel.setFolder_name(cursor.getString(cursor.getColumnIndex(FolderKey.FOLDER_NAME)));
                folderModel.setCreated_by_user(cursor.getString(cursor.getColumnIndex(FolderKey.CREATED_BY_USER)));
                folderModel.setCreated_date(cursor.getString(cursor.getColumnIndex(FolderKey.CREATED_DATE)));
                folderModel.setModified_date(cursor.getString(cursor.getColumnIndex(FolderKey.MODIFIED_DATE)));
                folderModel.setNo_of_poi(cursor.getString(cursor.getColumnIndex(FolderKey.NO_OF_CIDPOI)));
                folderModel.setNo_of_subfolder(cursor.getString(cursor.getColumnIndex(FolderKey.NO_OF_SUBFOLDER)));

                if (start.equalsIgnoreCase(Constans.FOLDER) && folderId.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(FolderKey.FOLDER_ID)))) {

                }
                else
                {
                    if (!userId.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(FolderKey.FOLDER_LOCK_USERID)))) {
                        if (!Boolean.valueOf(cursor.getString(cursor.getColumnIndex(FolderKey.FOLDER_LOCK)))) {
                            modelFolder.add(folderModel);
                        }
                    } else {
                        modelFolder.add(folderModel);
                    }

                }


            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return modelFolder;

    }


    public ArrayList<LogBookModel> SelectLogBookByCategoryID(String categoryId)
    {

        modelLogBook = new ArrayList<LogBookModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();

        if(categoryId.equalsIgnoreCase("0"))
        {
            cursor = db.query(TABLE_NAME_LOG_BOOK, null, LogBookKey.CATEGORY_ID + "=? OR " + LogBookKey.CATEGORY_ID + "=? ", new String[]{"0", ""}, null, null, LogBookKey.TIMESTAMP + " DESC");
        }
        else
        {
            // cursor = db.query(TABLE_NAME_LOG_BOOK, null, null, null, null, null, LogBookKey.TITLE + " ASC");
            cursor = db.query(TABLE_NAME_LOG_BOOK, null, LogBookKey.CATEGORY_ID + "=?", new String[]{categoryId}, null, null, LogBookKey.TIMESTAMP + " DESC");
        }


        //Log.e("", "Count:-" + cursor.getCount());

        if (cursor.moveToFirst()) {
            do {
                LogBookModel logBookModel = new LogBookModel();
                logBookModel.setId(cursor.getString(cursor.getColumnIndex(LogBookKey.ID)));
                logBookModel.setCategory_id(cursor.getString(cursor.getColumnIndex(LogBookKey.CATEGORY_ID)));
                logBookModel.setLogbook_id(cursor.getString(cursor.getColumnIndex(LogBookKey.LOG_BOOK_ID)));
                logBookModel.setShare_type_id(cursor.getString(cursor.getColumnIndex(LogBookKey.SHARE_TYPE_ID)));
                logBookModel.setShare_type(cursor.getString(cursor.getColumnIndex(LogBookKey.SHARE_TYPE)));
                logBookModel.setDuration(cursor.getString(cursor.getColumnIndex(LogBookKey.DURATION)));
                logBookModel.setStart_date_time(cursor.getString(cursor.getColumnIndex(LogBookKey.START_DATE_TIME)));
                logBookModel.setEnd_date_time(cursor.getString(cursor.getColumnIndex(LogBookKey.END_DATE_TIME)));
                logBookModel.setStart_address(cursor.getString(cursor.getColumnIndex(LogBookKey.START_ADDRESS)));
                logBookModel.setEnd_address(cursor.getString(cursor.getColumnIndex(LogBookKey.END_ADDRESS)));
                logBookModel.setStart_latlong(cursor.getString(cursor.getColumnIndex(LogBookKey.START_LATLONG)));
                logBookModel.setEnd_latlong(cursor.getString(cursor.getColumnIndex(LogBookKey.END_LATLONG)));
                logBookModel.setKilometer(cursor.getString(cursor.getColumnIndex(LogBookKey.KILOMETER)));
                logBookModel.setTimestamp(cursor.getString(cursor.getColumnIndex(LogBookKey.TIMESTAMP)));
                logBookModel.setTitle(cursor.getString(cursor.getColumnIndex(LogBookKey.TITLE)));
                logBookModel.setIsSelected(false);
                logBookModel.setTrip_status(cursor.getString(cursor.getColumnIndex(LogBookKey.TRIP_STATUS)));
                logBookModel.setExpected_latlong(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_LATLONG)));
                logBookModel.setExpected_address(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_ADDRESS)));
                logBookModel.setExpected_distance(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_DISTANCE)));
                logBookModel.setExpected_duration(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_DURATION)));

                modelLogBook.add(logBookModel);

            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return modelLogBook;

    }


    public ArrayList<LogBookModel> selectLogBookByCategoryIDLogBookIds(final String categoryId,final String logbookIds)
    {

        modelLogBook = new ArrayList<LogBookModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();

        if(categoryId.equalsIgnoreCase("0"))
        {
            cursor = db.query(TABLE_NAME_LOG_BOOK, null, LogBookKey.CATEGORY_ID + "=? OR " + LogBookKey.CATEGORY_ID + "=? ", new String[]{"0", ""}, null, null, LogBookKey.TIMESTAMP + " DESC");
        }
        else
        {
            // cursor = db.query(TABLE_NAME_LOG_BOOK, null, null, null, null, null, LogBookKey.TITLE + " ASC");
            cursor = db.query(TABLE_NAME_LOG_BOOK, null, LogBookKey.CATEGORY_ID + "=?", new String[]{categoryId}, null, null, LogBookKey.TIMESTAMP + " DESC");
        }


        //Log.e("", "Count:-" + cursor.getCount());

        if (cursor.moveToFirst()) {
            do {
                LogBookModel logBookModel = new LogBookModel();
                logBookModel.setId(cursor.getString(cursor.getColumnIndex(LogBookKey.ID)));
                logBookModel.setCategory_id(cursor.getString(cursor.getColumnIndex(LogBookKey.CATEGORY_ID)));
                logBookModel.setLogbook_id(cursor.getString(cursor.getColumnIndex(LogBookKey.LOG_BOOK_ID)));
                logBookModel.setShare_type_id(cursor.getString(cursor.getColumnIndex(LogBookKey.SHARE_TYPE_ID)));
                logBookModel.setShare_type(cursor.getString(cursor.getColumnIndex(LogBookKey.SHARE_TYPE)));
                logBookModel.setDuration(cursor.getString(cursor.getColumnIndex(LogBookKey.DURATION)));
                logBookModel.setStart_date_time(cursor.getString(cursor.getColumnIndex(LogBookKey.START_DATE_TIME)));
                logBookModel.setEnd_date_time(cursor.getString(cursor.getColumnIndex(LogBookKey.END_DATE_TIME)));
                logBookModel.setStart_address(cursor.getString(cursor.getColumnIndex(LogBookKey.START_ADDRESS)));
                logBookModel.setEnd_address(cursor.getString(cursor.getColumnIndex(LogBookKey.END_ADDRESS)));
                logBookModel.setStart_latlong(cursor.getString(cursor.getColumnIndex(LogBookKey.START_LATLONG)));
                logBookModel.setEnd_latlong(cursor.getString(cursor.getColumnIndex(LogBookKey.END_LATLONG)));
                logBookModel.setKilometer(cursor.getString(cursor.getColumnIndex(LogBookKey.KILOMETER)));
                logBookModel.setTimestamp(cursor.getString(cursor.getColumnIndex(LogBookKey.TIMESTAMP)));
                logBookModel.setTitle(cursor.getString(cursor.getColumnIndex(LogBookKey.TITLE)));

                if(!logbookIds.isEmpty() && logbookIds.contains(cursor.getString(cursor.getColumnIndex(LogBookKey.LOG_BOOK_ID))))
                {
                    logBookModel.setIsSelected(true);
                }
                else
                {
                    logBookModel.setIsSelected(false);
                }


                logBookModel.setTrip_status(cursor.getString(cursor.getColumnIndex(LogBookKey.TRIP_STATUS)));
                logBookModel.setExpected_latlong(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_LATLONG)));
                logBookModel.setExpected_address(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_ADDRESS)));
                logBookModel.setExpected_distance(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_DISTANCE)));
                logBookModel.setExpected_duration(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_DURATION)));

                if(cursor.getString(cursor.getColumnIndex(LogBookKey.TRIP_STATUS)).equalsIgnoreCase("end"))
                {
                    modelLogBook.add(logBookModel);
                }


            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return modelLogBook;

    }


    public LogBookModel SelectLogBookDetals(String logbookId) {

        LogBookModel logBookModel = new LogBookModel();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_NAME_LOG_BOOK, null, LogBookKey.LOG_BOOK_ID + "=?", new String[]{logbookId}, null, null, null);


        if (cursor.moveToFirst()) {
            do {

                logBookModel.setId(cursor.getString(cursor.getColumnIndex(LogBookKey.ID)));
                logBookModel.setCategory_id(cursor.getString(cursor.getColumnIndex(LogBookKey.CATEGORY_ID)));
                logBookModel.setLogbook_id(cursor.getString(cursor.getColumnIndex(LogBookKey.LOG_BOOK_ID)));
                logBookModel.setShare_type_id(cursor.getString(cursor.getColumnIndex(LogBookKey.SHARE_TYPE_ID)));
                logBookModel.setShare_type(cursor.getString(cursor.getColumnIndex(LogBookKey.SHARE_TYPE)));
                logBookModel.setDuration(cursor.getString(cursor.getColumnIndex(LogBookKey.DURATION)));
                logBookModel.setStart_date_time(cursor.getString(cursor.getColumnIndex(LogBookKey.START_DATE_TIME)));
                logBookModel.setEnd_date_time(cursor.getString(cursor.getColumnIndex(LogBookKey.END_DATE_TIME)));
                logBookModel.setStart_address(cursor.getString(cursor.getColumnIndex(LogBookKey.START_ADDRESS)));
                logBookModel.setEnd_address(cursor.getString(cursor.getColumnIndex(LogBookKey.END_ADDRESS)));
                logBookModel.setStart_latlong(cursor.getString(cursor.getColumnIndex(LogBookKey.START_LATLONG)));
                logBookModel.setEnd_latlong(cursor.getString(cursor.getColumnIndex(LogBookKey.END_LATLONG)));
                logBookModel.setKilometer(cursor.getString(cursor.getColumnIndex(LogBookKey.KILOMETER)));
                logBookModel.setTimestamp(cursor.getString(cursor.getColumnIndex(LogBookKey.TIMESTAMP)));
                logBookModel.setTitle(cursor.getString(cursor.getColumnIndex(LogBookKey.TITLE)));
                logBookModel.setTrip_status(cursor.getString(cursor.getColumnIndex(LogBookKey.TRIP_STATUS)));
                logBookModel.setExpected_latlong(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_LATLONG)));
                logBookModel.setExpected_address(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_ADDRESS)));
                logBookModel.setExpected_distance(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_DISTANCE)));
                logBookModel.setExpected_duration(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_DURATION)));

                logBookModel.setIsSelected(false);


            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return logBookModel;

    }

    public ArrayList<LogBookModel> selectLogBooExportCSV(final String fromdate,final String toDate,String categoryIds,boolean isDateSelecte)
    {

        modelLogBook = new ArrayList<LogBookModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        categoryIds = categoryIds.replace("[", "");
        categoryIds = categoryIds.replace("]", "");
        categoryIds = categoryIds.replace(" ", "");
        String qry="";

        if(isDateSelecte)
        {
            qry = "SELECT * FROM " + TABLE_NAME_LOG_BOOK + " WHERE " + LogBookKey.CATEGORY_ID + " IN (" + categoryIds + ") AND " + LogBookKey.START_DATE_TIME + ">=" + getTimestamp(fromdate + " 00:00:00") + " AND " + LogBookKey.START_DATE_TIME + "<=" + getTimestampTo(toDate + " 23:11:59");

        }
        else
        {
            qry = "SELECT * FROM " + TABLE_NAME_LOG_BOOK + " WHERE " + LogBookKey.CATEGORY_ID + " IN (" + categoryIds + ")" ;
        }

        cursor = db.rawQuery(qry, null);

        if (cursor.moveToFirst())
        {
            do {

                LogBookModel logBookModel = new LogBookModel();
                logBookModel.setId(cursor.getString(cursor.getColumnIndex(LogBookKey.ID)));
                logBookModel.setCategory_id(cursor.getString(cursor.getColumnIndex(LogBookKey.CATEGORY_ID)));
                logBookModel.setLogbook_id(cursor.getString(cursor.getColumnIndex(LogBookKey.LOG_BOOK_ID)));
                logBookModel.setShare_type_id(cursor.getString(cursor.getColumnIndex(LogBookKey.SHARE_TYPE_ID)));
                logBookModel.setShare_type(cursor.getString(cursor.getColumnIndex(LogBookKey.SHARE_TYPE)));
                logBookModel.setDuration(cursor.getString(cursor.getColumnIndex(LogBookKey.DURATION)));
                logBookModel.setStart_date_time(cursor.getString(cursor.getColumnIndex(LogBookKey.START_DATE_TIME)));
                logBookModel.setEnd_date_time(cursor.getString(cursor.getColumnIndex(LogBookKey.END_DATE_TIME)));
                logBookModel.setStart_address(cursor.getString(cursor.getColumnIndex(LogBookKey.START_ADDRESS)));
                logBookModel.setEnd_address(cursor.getString(cursor.getColumnIndex(LogBookKey.END_ADDRESS)));
                logBookModel.setStart_latlong(cursor.getString(cursor.getColumnIndex(LogBookKey.START_LATLONG)));
                logBookModel.setEnd_latlong(cursor.getString(cursor.getColumnIndex(LogBookKey.END_LATLONG)));
                logBookModel.setKilometer(cursor.getString(cursor.getColumnIndex(LogBookKey.KILOMETER)));
                logBookModel.setTimestamp(cursor.getString(cursor.getColumnIndex(LogBookKey.TIMESTAMP)));
                logBookModel.setTitle(cursor.getString(cursor.getColumnIndex(LogBookKey.TITLE)));
                logBookModel.setTrip_status(cursor.getString(cursor.getColumnIndex(LogBookKey.TRIP_STATUS)));
                logBookModel.setExpected_latlong(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_LATLONG)));
                logBookModel.setExpected_address(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_ADDRESS)));
                logBookModel.setExpected_distance(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_DISTANCE)));
                logBookModel.setExpected_duration(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_DURATION)));
                logBookModel.setIsSelected(false);

                if(cursor.getString(cursor.getColumnIndex(LogBookKey.TRIP_STATUS)).equalsIgnoreCase("end"))
                {
                    modelLogBook.add(logBookModel);
                }

            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return modelLogBook;

    }


    public ArrayList<LogBookModel> selectLogBooExportCSVRecentTrip(final String fromdate, final String toDate, String logIds,final boolean isDateSelecte)
    {
        modelLogBook = new ArrayList<LogBookModel>();
        try
        {
            Cursor cursor = null;
            SQLiteDatabase db = this.getWritableDatabase();
            logIds = logIds.replace("[", "");
            logIds = logIds.replace("]", "");
            logIds = logIds.replace(" ", "");
            String qry="";


            Log.d("RecentTrip","logIds=="+logIds);

            if(!isDateSelecte)
            {
                 qry = "SELECT * FROM " + TABLE_NAME_LOG_BOOK + " WHERE " + LogBookKey.LOG_BOOK_ID + " IN (" + logIds + ")" ;
            }
            else
            {
                qry = "SELECT * FROM " + TABLE_NAME_LOG_BOOK + " WHERE " + LogBookKey.LOG_BOOK_ID + " IN (" + logIds + ") AND " + LogBookKey.START_DATE_TIME + ">=" + getTimestamp(fromdate + " 00:00:00") + " AND " + LogBookKey.START_DATE_TIME + "<=" + getTimestampTo(toDate + " 23:11:59");
            }


            cursor = db.rawQuery(qry, null);

            if (cursor.moveToFirst())
            {
                do {

                    LogBookModel logBookModel = new LogBookModel();
                    logBookModel.setId(cursor.getString(cursor.getColumnIndex(LogBookKey.ID)));
                    logBookModel.setCategory_id(cursor.getString(cursor.getColumnIndex(LogBookKey.CATEGORY_ID)));
                    logBookModel.setLogbook_id(cursor.getString(cursor.getColumnIndex(LogBookKey.LOG_BOOK_ID)));
                    logBookModel.setShare_type_id(cursor.getString(cursor.getColumnIndex(LogBookKey.SHARE_TYPE_ID)));
                    logBookModel.setShare_type(cursor.getString(cursor.getColumnIndex(LogBookKey.SHARE_TYPE)));
                    logBookModel.setDuration(cursor.getString(cursor.getColumnIndex(LogBookKey.DURATION)));
                    logBookModel.setStart_date_time(cursor.getString(cursor.getColumnIndex(LogBookKey.START_DATE_TIME)));
                    logBookModel.setEnd_date_time(cursor.getString(cursor.getColumnIndex(LogBookKey.END_DATE_TIME)));
                    logBookModel.setStart_address(cursor.getString(cursor.getColumnIndex(LogBookKey.START_ADDRESS)));
                    logBookModel.setEnd_address(cursor.getString(cursor.getColumnIndex(LogBookKey.END_ADDRESS)));
                    logBookModel.setStart_latlong(cursor.getString(cursor.getColumnIndex(LogBookKey.START_LATLONG)));
                    logBookModel.setEnd_latlong(cursor.getString(cursor.getColumnIndex(LogBookKey.END_LATLONG)));
                    logBookModel.setKilometer(cursor.getString(cursor.getColumnIndex(LogBookKey.KILOMETER)));
                    logBookModel.setTimestamp(cursor.getString(cursor.getColumnIndex(LogBookKey.TIMESTAMP)));
                    logBookModel.setTitle(cursor.getString(cursor.getColumnIndex(LogBookKey.TITLE)));
                    logBookModel.setTrip_status(cursor.getString(cursor.getColumnIndex(LogBookKey.TRIP_STATUS)));
                    logBookModel.setExpected_latlong(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_LATLONG)));
                    logBookModel.setExpected_address(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_ADDRESS)));
                    logBookModel.setExpected_distance(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_DISTANCE)));
                    logBookModel.setExpected_duration(cursor.getString(cursor.getColumnIndex(LogBookKey.EXPECTED_DURATION)));
                    logBookModel.setIsSelected(false);

                    if(cursor.getString(cursor.getColumnIndex(LogBookKey.TRIP_STATUS)).equalsIgnoreCase("end"))
                    {
                        modelLogBook.add(logBookModel);
                    }

                } while (cursor.moveToNext());

            }

            cursor.close();
            db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



        return modelLogBook;

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


    public ArrayList<GroupHistoryModel> SelectGroupHistory(String groupId) {

        modelGroupHistory = new ArrayList<GroupHistoryModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        // cursor = db.query(TABLE_NAME_GROUP_HISTORY, null, null, null, null, null, null, null);
        cursor = db.query(TABLE_NAME_GROUP_HISTORY, null, GroupHistoryKey.GROUP_ID + "=?", new String[]{groupId}, null, null, GroupHistoryKey.CREATED_DATE + " DESC");

        ////Log.e("", "Count:-" + cursor.getCount());

        if (cursor.moveToFirst()) {
            do {


                GroupHistoryModel groupModel = new GroupHistoryModel();
                groupModel.setId(cursor.getString(cursor.getColumnIndex(GroupHistoryKey.ID)));
                groupModel.setGroup_id(cursor.getString(cursor.getColumnIndex(GroupHistoryKey.GROUP_ID)));
                groupModel.setUser_id(cursor.getString(cursor.getColumnIndex(GroupHistoryKey.USER_ID)));
                groupModel.setHistory_id(cursor.getString(cursor.getColumnIndex(GroupHistoryKey.GROUP_HISTORY_ID)));
                groupModel.setGroup_name(cursor.getString(cursor.getColumnIndex(GroupHistoryKey.GROUP_NAME)));
                groupModel.setTitle(cursor.getString(cursor.getColumnIndex(GroupHistoryKey.TITLE)));
                groupModel.setCreated_date(cursor.getString(cursor.getColumnIndex(GroupHistoryKey.CREATED_DATE)));
                groupModel.setCreated_timestamp(cursor.getString(cursor.getColumnIndex(GroupHistoryKey.CREATED_TIMESTAMP)));
                groupModel.setType(cursor.getString(cursor.getColumnIndex(GroupHistoryKey.TYPE)));
                groupModel.setType_id(cursor.getString(cursor.getColumnIndex(GroupHistoryKey.TYPE_ID)));
                groupModel.setCity(cursor.getString(cursor.getColumnIndex(GroupHistoryKey.CITY)));
                groupModel.setState(cursor.getString(cursor.getColumnIndex(GroupHistoryKey.STATE)));
                groupModel.setCountry(cursor.getString(cursor.getColumnIndex(GroupHistoryKey.COUNTRY)));
                groupModel.setAddress(cursor.getString(cursor.getColumnIndex(GroupHistoryKey.ADDRESS)));
                groupModel.setLocation(cursor.getString(cursor.getColumnIndex(GroupHistoryKey.LOCATION)));
                groupModel.setMessage(cursor.getString(cursor.getColumnIndex(GroupHistoryKey.MESSGAE)));
                groupModel.setMessageId(cursor.getString(cursor.getColumnIndex(GroupHistoryKey.MESSGAE_ID)));
                groupModel.setStatus(cursor.getString(cursor.getColumnIndex(GroupHistoryKey.STATUS)));
                groupModel.setSenderId(cursor.getString(cursor.getColumnIndex(GroupHistoryKey.SENDER_ID)));
                groupModel.setSenderName(cursor.getString(cursor.getColumnIndex(GroupHistoryKey.SENDER_NAME)));
                groupModel.setMessage_datetime(cursor.getString(cursor.getColumnIndex(GroupHistoryKey.MESSAGE_DATETIME)));
                groupModel.setPhone(cursor.getString(cursor.getColumnIndex(GroupHistoryKey.PHONE)));

                modelGroupHistory.add(groupModel);

            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return modelGroupHistory;

    }


    public String SelectGroupHistoryMaxTimeStamp(String groupId) {
        String timestamp = "0";

        modelGroupHistory = new ArrayList<GroupHistoryModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_NAME_GROUP_HISTORY, null, GroupHistoryKey.GROUP_ID + "=?", new String[]{groupId}, null, null, GroupHistoryKey.CREATED_DATE + " DESC");

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                timestamp = cursor.getString(cursor.getColumnIndex(GroupHistoryKey.CREATED_DATE));
            }
            cursor.close();
        }

        db.close();
        return timestamp;

    }

    public ArrayList<PublicPrivateAddressModel> SelectPublicPrivateAddress(String userId, String type) {

        ////Log.e("SelectAddress", "public private type:" + type);

        modelPublicPrivateList = new ArrayList<PublicPrivateAddressModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        //cursor = db.query(TABLE_NAME_MYGLC_PUBLIC_PRIVATE_ADD, null, null, null, null, null, null, null);
        cursor = db.query(TABLE_NAME_MYGLC_PUBLIC_PRIVATE_ADD, null, ContactKey.TYPE_ADDRESS + "=?", new String[]{type}, null, null, null);
        //cursor = db.query(TABLE_NAME_CONTACTS_PUBLIC_PRIVATE_ADD, null, ContactKey.USER_ID + " =? AND " + ContactKey.TYPE_ADDRESS + " =? ", new String[]{userId, type}, null, null, null)
        //final String whereClues = ContactKey.USER_ID + " =? AND " + ContactKey.TYPE_ADDRESS + " =? ";
        //cursor = db.query(TABLE_NAME_MYGLC_PUBLIC_PRIVATE_ADD, null, whereClues, new String[]{userId, type}, null, null, null);
        //////Log.e("SelectAddress", "public private Count:" + cursor.getCount());

        if (cursor.moveToFirst()) {
            do {

                PublicPrivateAddressModel addressModel = new PublicPrivateAddressModel();
                //Log.d("SelectAddress", "USER_ID:" + cursor.getString(cursor.getColumnIndex(ContactKey.USER_ID))+"==userId=="+userId);
                addressModel.setAddress_id(cursor.getString(cursor.getColumnIndex(ContactKey.ADDRESS_ID)));
                addressModel.setUser_id(cursor.getString(cursor.getColumnIndex(ContactKey.USER_ID)));
                addressModel.setPhone(cursor.getString(cursor.getColumnIndex(ContactKey.PHONE)));
                addressModel.setAddress(cursor.getString(cursor.getColumnIndex(ContactKey.ADDRESS)));
                addressModel.setCity(cursor.getString(cursor.getColumnIndex(ContactKey.CITY)));
                addressModel.setCompany_name(cursor.getString(cursor.getColumnIndex(ContactKey.COMPANY_NAME)));
                addressModel.setCountry(cursor.getString(cursor.getColumnIndex(ContactKey.COUNTRY)));
                addressModel.setEmail(cursor.getString(cursor.getColumnIndex(ContactKey.EMAIL)));
                addressModel.setState(cursor.getString(cursor.getColumnIndex(ContactKey.STATE)));
                addressModel.setLat(cursor.getString(cursor.getColumnIndex(ContactKey.LAT)));
                addressModel.setLng(cursor.getString(cursor.getColumnIndex(ContactKey.LONG)));
                addressModel.setItemList(createItems(cursor.getString(cursor.getColumnIndex(ContactKey.TYPE_ADDRESS))));

                modelPublicPrivateList.add(addressModel);

            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return modelPublicPrivateList;

    }


    public ArrayList<GroupMemberModel> SelectMember(String groupId, String userId) {

        groupMemberModelList = new ArrayList<>();
        GroupMemberModel memberModelAdmin = new GroupMemberModel();

        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_NAME_GROUP_MEMBER, null, GroupMemberKey.GROUP_ID + "=?", new String[]{groupId}, null, null, GroupMemberKey.MEMBER_NAME + " COLLATE NOCASE ASC;");

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(GroupMemberKey.MEMBER_ID)).equalsIgnoreCase(userId)) {
                    memberModelAdmin.setId(cursor.getString(cursor.getColumnIndex(GroupMemberKey.ID)));
                    memberModelAdmin.setMember_id(cursor.getString(cursor.getColumnIndex(GroupMemberKey.MEMBER_ID)));
                    memberModelAdmin.setMember_name(cursor.getString(cursor.getColumnIndex(GroupMemberKey.MEMBER_NAME)));
                    memberModelAdmin.setMember_profile_pic(cursor.getString(cursor.getColumnIndex(GroupMemberKey.MEMBER_PROFILE_PIC)));
                } else {
                    GroupMemberModel memberModel = new GroupMemberModel();
                    memberModel.setId(cursor.getString(cursor.getColumnIndex(GroupMemberKey.ID)));
                    memberModel.setMember_id(cursor.getString(cursor.getColumnIndex(GroupMemberKey.MEMBER_ID)));
                    memberModel.setMember_name(cursor.getString(cursor.getColumnIndex(GroupMemberKey.MEMBER_NAME)));
                    memberModel.setMember_profile_pic(cursor.getString(cursor.getColumnIndex(GroupMemberKey.MEMBER_PROFILE_PIC)));
                    groupMemberModelList.add(memberModel);
                }

            } while (cursor.moveToNext());

        }

        //admin First after sorting and add Member
        final ArrayList<GroupMemberModel> allList = new ArrayList<GroupMemberModel>();
        allList.add(memberModelAdmin);
        allList.addAll(groupMemberModelList);

        cursor.close();
        db.close();
        return allList;

    }


    public ArrayList<DeviceModel> selectDeviceList() {

        modelDeviceList = new ArrayList<DeviceModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();

        cursor = db.query(TABLE_NAME_PREFRED_DEVICE, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                DeviceModel deviceList = new DeviceModel();
                deviceList.setId(cursor.getString(cursor.getColumnIndex(PrefredDeviceKey.ID)));
                deviceList.setName(cursor.getString(cursor.getColumnIndex(PrefredDeviceKey.DEVICE_NAME)));
                deviceList.setAddress(cursor.getString(cursor.getColumnIndex(PrefredDeviceKey.ADDRESS)));
                deviceList.setIsPrefered(cursor.getString(cursor.getColumnIndex(PrefredDeviceKey.IS_PREFRED)));
                modelDeviceList.add(deviceList);


            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return modelDeviceList;

    }


    public ArrayList<DeviceModel> selectPreferredDeviceList() {

        modelDeviceList = new ArrayList<DeviceModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();

        cursor = db.query(TABLE_NAME_PREFRED_DEVICE, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {

                boolean isPrefred = Boolean.valueOf(cursor.getString(cursor.getColumnIndex(PrefredDeviceKey.IS_PREFRED)));

                if (isPrefred) {
                    DeviceModel deviceList = new DeviceModel();
                    deviceList.setId(cursor.getString(cursor.getColumnIndex(PrefredDeviceKey.ID)));
                    deviceList.setName(cursor.getString(cursor.getColumnIndex(PrefredDeviceKey.DEVICE_NAME)));
                    deviceList.setAddress(cursor.getString(cursor.getColumnIndex(PrefredDeviceKey.ADDRESS)));
                    deviceList.setIsPrefered(cursor.getString(cursor.getColumnIndex(PrefredDeviceKey.IS_PREFRED)));
                    modelDeviceList.add(deviceList);
                }


            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();

        return modelDeviceList;

    }


    private List<ItemDetail> createItems(String name) {
        List<ItemDetail> result = new ArrayList<ItemDetail>();
        ItemDetail item = new ItemDetail(name);
        result.add(item);
        return result;
    }


    public ArrayList<HistoryModel> SelectHistory() {

        modelHistory = new ArrayList<HistoryModel>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.query(TABLE_NAME_HISTORY, null, null, null, null, null, HistoryKey.DATE_TIME + " DESC");

        ////Log.e("", "Count:-" + cursor.getCount());

        if (cursor.moveToFirst()) {
            do {

                HistoryModel historyModel = new HistoryModel();
                historyModel.setId(cursor.getString(cursor.getColumnIndex(HistoryKey.ID_HISTORY)));
                historyModel.setTrip_id(cursor.getString(cursor.getColumnIndex(HistoryKey.TRIP_ID)));
                historyModel.setType_id(cursor.getString(cursor.getColumnIndex(HistoryKey.TYPE_ID)));
                historyModel.setType(cursor.getString(cursor.getColumnIndex(HistoryKey.TYPE)));
                historyModel.setName(cursor.getString(cursor.getColumnIndex(HistoryKey.NAME)));
                historyModel.setAddress(cursor.getString(cursor.getColumnIndex(HistoryKey.ADDRESS)));
                historyModel.setLat(cursor.getString(cursor.getColumnIndex(HistoryKey.LAT)));
                historyModel.setLng(cursor.getString(cursor.getColumnIndex(HistoryKey.LONGI)));
                historyModel.setType(cursor.getString(cursor.getColumnIndex(HistoryKey.TYPE)));
                historyModel.setImage(cursor.getString(cursor.getColumnIndex(HistoryKey.IMAGE)));
                historyModel.setDateAndTime(cursor.getString(cursor.getColumnIndex(HistoryKey.DATE_TIME)));

                modelHistory.add(historyModel);

            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return modelHistory;

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
