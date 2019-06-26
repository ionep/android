package hex.floodproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBhelper extends SQLiteOpenHelper {
    public static final String dbName="Hexflood.db";
    public static final String tableName="RiverLevel";
    public static final String col1="sn";
    public static final String col2="data";
    public static final String col3="dd";
    public static final String col4="mm";
    public static final String col5="yy";
    public static final String col6="location";  //state 0-enter 1-out 2-in

    public DBhelper(Context context) {
        super(context, dbName, null, 1);
        SQLiteDatabase db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+tableName+" ("+col1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+col2+" VARCHAR(50) NOT NULL, "+col3+" VARCHAR(50) NOT NULL,"+col4+" VARCHAR(50) NOT NULL,"+col5+" VARCHAR(50) NOT NULL,"+col6+" VARCHAR(50) NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+tableName);
        onCreate(db);
    }

    public boolean insertData(String data, String dd, String mm, String yy, String location)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(col2,data);
        contentValues.put(col3,dd);
        contentValues.put(col4,mm);
        contentValues.put(col5,yy);
        contentValues.put(col6,location);
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
    /*
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
    */

}
