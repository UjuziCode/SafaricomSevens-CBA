
package com.cbagroup.safaricom.sevens.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CBAdbUtils {
	
	/**
     *  Constants that specify the different columns on the Tablet Database
     *  and corresponding column indexes
     */
	
	public static final String REGISTRATION_ID = "id";

    public static final String REGISTRATION_FNAME = "fname";
    
    public static final String REGISTRATION_LNAME = "lname";
    
    public static final String REGISTRATION_AGE_GROUP = "age_group";
    
    public static final String REGISTRATION_EMAIL = "email";
    
    public static final String REGISTRATION_GENDER = "gender";
    
    public static final String REGISTRATION_PHONE= "phone";
    
    public static final String REGISTRATION_ADDRESS= "address";
    
    public static final String REGISTRATION_STATE= "state";
    
    public static final String REGISTRATION_ACCOUNTNUMBER_STATE= "accountnumberstate";
    
    public static final String REGISTRATION_ACCOUNTNUMBER= "accountnumber";
    
    public static final String[] TBL_REGISTRATION_COLUMNS = new String[] {REGISTRATION_ID, REGISTRATION_FNAME, REGISTRATION_LNAME,
    	REGISTRATION_AGE_GROUP,REGISTRATION_EMAIL,REGISTRATION_GENDER,REGISTRATION_PHONE,REGISTRATION_ADDRESS,REGISTRATION_STATE,REGISTRATION_ACCOUNTNUMBER_STATE,REGISTRATION_ACCOUNTNUMBER};
    
    private DatabaseHelper mDbHelper;

    private SQLiteDatabase mDb;
    
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "CBA_GROUP_db";

    private static final String REGISTRATION_TABLE = "registrations";
    
    private static final String CREATE_REGISTRATION_TABLE = 
        	"CREATE TABLE IF NOT EXISTS  "
            + REGISTRATION_TABLE 
            + " (" + REGISTRATION_ID 
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + REGISTRATION_FNAME 
            + " VARCHAR(50) NOT NULL, "
            + REGISTRATION_LNAME 
            + " VARCHAR(50) NOT NULL, "
            + REGISTRATION_AGE_GROUP 
            + " VARCHAR(15) NOT NULL, "
            + REGISTRATION_EMAIL 
            + " VARCHAR(150) NOT NULL, "
            + REGISTRATION_GENDER 
            + " VARCHAR(20) NOT NULL, "
            + REGISTRATION_PHONE 
            + " VARCHAR(20) NOT NULL, "
            + REGISTRATION_ADDRESS 
            + " VARCHAR(50) , "
            + REGISTRATION_STATE 
            + " VARCHAR(10) NOT NULL, "
            + REGISTRATION_ACCOUNTNUMBER_STATE 
            + " VARCHAR(10) NOT NULL, "
            +REGISTRATION_ACCOUNTNUMBER
            +" VARCHAR(100) )"; 
    
    private final Context mContext;
    
    private static final String TAG = "CBA_GROUP_db";
    
    public CBAdbUtils(Context context) {
        this.mContext = context;
    }

    public CBAdbUtils open() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();

        return this;
    }

    public void close() {
        mDbHelper.close();
    }
    
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        
        @Override
		public void onCreate(SQLiteDatabase db) {
        	Log.v("Creating Database", CREATE_REGISTRATION_TABLE);
			db.execSQL(CREATE_REGISTRATION_TABLE);
			
		}
        
        @Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + " which does not destroy all old data");
			
			List<String> registrationColumns;
            db.execSQL(CREATE_REGISTRATION_TABLE);
            registrationColumns = CBAdbUtils.getColumns(db, REGISTRATION_TABLE);
            db.execSQL("ALTER TABLE " + REGISTRATION_TABLE + " RENAME TO temp_" + REGISTRATION_TABLE);
            db.execSQL(CREATE_REGISTRATION_TABLE);
            registrationColumns.retainAll(CBAdbUtils.getColumns(db, REGISTRATION_TABLE));
            String cols = CBAdbUtils.join(registrationColumns, ",");
            db.execSQL(String.format("INSERT INTO %s (%s) SELECT %s FROM temp_%s", REGISTRATION_TABLE, cols, cols, REGISTRATION_TABLE));   
            db.execSQL("DROP TABLE IF EXISTS temp_" + REGISTRATION_TABLE);
            onCreate(db);
		}
        
        
    }
    
    /**
     * Credits http://goo.gl/7kOpU
     * @param db
     * @param tableName
     * @return
     */
    public static List<String> getColumns(SQLiteDatabase db, String tableName) {
    	Log.v("List<String>", tableName);
        List<String> ar = null;
        Cursor c = null;

        try {
            c = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 1", null);

            if (c != null) {
                ar = new ArrayList<String>(Arrays.asList(c.getColumnNames()));
            }

        } catch (Exception e) {
            Log.v(tableName, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (c != null)
                c.close();
        }
        return ar;
    }

    public static String join(List<String> list, String delim) {
        StringBuilder buf = new StringBuilder();
        int num = list.size();
        for (int i = 0; i < num; i++) {
            if (i != 0)
                buf.append(delim);
            buf.append((String)list.get(i));
        }
        return buf.toString();
    }
 
    
    /**
     * The Below code will ...Insert into @REGISTRATION_TABLE content to  DB 
     * @param  ContentValues values
     * @return
     */
    public void insertDB(ContentValues values) {
        mDb.insertOrThrow(REGISTRATION_TABLE, null, values);

    }
    
    /**
     * The Below code will ...check if @REGISTRATION_TABLE content   DB  is empty
     * @param  
     * @return Boolean rowExists
     */ 
	public boolean chkDB() {
		String sql = "SELECT  * FROM " + REGISTRATION_TABLE;
		Cursor mCursor = mDb.rawQuery(sql, null);

		Boolean rowExists;

		if (mCursor.moveToFirst()) {
			rowExists = true;

		} else {

			rowExists = false;
		}
		return rowExists;

	}
    
    
    /**
    * The Below code will Delete  table Products 
    * @param 
    * @return
    */
   public void deleteDB() {
       mDb.delete(REGISTRATION_TABLE, null, null);

   }
    
    
    /**
     * The Below code will ...Export a List Array from @REGISTRATION_TABLE
     * @param
     * @return List<String[]>
     */
    
    public List<String[]> selectAll()
    {
    List<String[]> list = new ArrayList<String[]>();
    String sql = "SELECT  * FROM " + REGISTRATION_TABLE ;
    Cursor cursor = mDb.rawQuery(sql, null); 
    int x=0;
    if (cursor.moveToFirst()) {
       do {
        String[] b1=new String[]{cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),
        		cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10)}; 
        list.add(b1);
        x=x+1;
       } while (cursor.moveToNext());
    }
    if (cursor != null && !cursor.isClosed()) {
       cursor.close();
    } 
    cursor.close();
    
    return list;
   }
}
