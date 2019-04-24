package gr.auebhci.hcilauncher.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

/**
 * This class connects to the contacts database.
 */
public class ContactsDatabase{

    private ContactsDatabaseHelper helper;
    private SQLiteDatabase database;
    private String LOGCAT_TAG = "ContactDatabase";
    private Context context;
    private String DATABASE_TABLE = "contacts";
    private boolean isOpened = false;

    public ContactsDatabase(Context context) {
        this.context = context;
    }

    /**
     * Opens database
     */
    public void open(){
        if(isOpened)
            Log.d(LOGCAT_TAG, "Database is already opened.");
        else {
            helper = new ContactsDatabaseHelper();
            database = helper.getWritableDatabase();
            isOpened = true;
            Log.d(LOGCAT_TAG, "Database successfully opened.");
        }
    }

    /**
     * Closes database.
     */
    public void close(){
        if(!isOpened)
            Log.d(LOGCAT_TAG, "Database is closed.");
        else {
            database.close();
            helper.close();
            isOpened = false;
            Log.d(LOGCAT_TAG, "Database successfully closed.");
        }
    }

    public void addContact(String name, String phone){
        if(isOpened)
        database.execSQL("INSERT INTO " + DATABASE_TABLE + " (photopath,name,number) values ('nav','" + name + "','" + phone + "')");
    }

    /**
     * Gets all contacts from database
     * @return ArrayList String[] with contacts
     */
    public ArrayList<String[]> getContacts(){
        if(!isOpened)open(); //open database to prevent crash.
        Cursor cursor = database.rawQuery("select name, number, id from " + DATABASE_TABLE, null);
        ArrayList<String[]> result = new ArrayList<>(cursor.getCount());
        String[] temp;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            temp = new String[3];
            temp[0] = cursor.getString(cursor.getColumnIndex("name"));
            temp[1] = cursor.getString(cursor.getColumnIndex("number"));
            temp[2] = cursor.getString(cursor.getColumnIndex("id"));
            result.add(temp);
        }
        return result;
    }

    public void delete(String id){
        database.execSQL("DELETE FROM " + DATABASE_TABLE + " WHERE id=" + id);
    }

    /**
     * Database Helper class.
     */
    private class ContactsDatabaseHelper extends SQLiteOpenHelper {


        private String DATABASE_PATH = "";
        private static final String DATABASE_NAME = "contacts.db";
        private static final int DATABASE_VERSION = 1;



        public ContactsDatabaseHelper() {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

            //since AP1>17
            this.DATABASE_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("CREATE TABLE " + DATABASE_TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "photopath TEXT, " +
                    "name TEXT, " +
                    "number TEXT);");
            Log.d(LOGCAT_TAG, "Database successfully created");


        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
}
