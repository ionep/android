package examprojectapp.ionep.com.examprojectapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBhelper extends SQLiteOpenHelper {
    public static final String dbName="Attendance.db";
    public static final String tableName="students_attend";
    public static final String col1="ID";
    public static final String col2="Roll";
    public static final String col3="Hour";
    public static final String col4="Minute";
    public static final String col5="Second";
    public static final String col6="State";  //state 0-enter 1-out 2-in

    public DBhelper(Context context) {
        super(context, dbName, null, 1);
        SQLiteDatabase db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+tableName+" ("+col1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+col2+" TEXT NOT NULL, "+col3+" INTEGER NOT NULL,"+col4+" INTEGER NOT NULL,"+col5+" INTEGER NOT NULL,"+col6+" INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+tableName);
        onCreate(db);
    }

    public boolean insertData(String roll,int hour,int minute,int second,int state)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(col2,roll);
        contentValues.put(col3,hour);
        contentValues.put(col4,minute);
        contentValues.put(col5,second);
        contentValues.put(col6,state);
        long res=db.insert(tableName,null,contentValues);
        if(res==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public Cursor readData()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor crs=db.rawQuery("SELECT * FROM "+tableName,null);
        return crs;
    }

    public Cursor readData(String roll)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor crs=db.rawQuery("SELECT * FROM "+tableName+" WHERE Roll='"+roll+"' ORDER BY ID DESC LIMIT 0,1",null);
        return crs;
    }

    public Cursor readData(int state)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor crs=db.rawQuery("SELECT * FROM "+tableName+" WHERE State='"+state+"' ORDER BY ID DESC LIMIT 0,1",null);
        return crs;
    }
    public Cursor readData(String roll,int state)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor crs=db.rawQuery("SELECT * FROM "+tableName+" WHERE Roll='"+roll+"' AND State='"+state+"' ORDER BY ID DESC LIMIT 0,1",null);
        return crs;
    }

}
