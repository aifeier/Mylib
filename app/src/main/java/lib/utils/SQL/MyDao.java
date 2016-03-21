package lib.utils.SQL;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by n-240 on 2016/2/16.
 *我们再创建一个MyDao用于处理所有的数据操作方法
 * @author cwf
 */
public class MyDao {
    private static final String TAG = "OrdersDao";

    // 列定义
    private final String[] MY_COLUMNS = new String[] {"Id", "CustomName","OrderPrice","Country"};

    private Context context;
    private MySQLiteOpenHelper sqLiteOpenHelper;
    public MyDao(Context context){
        this.context = context;
        sqLiteOpenHelper = new MySQLiteOpenHelper(context);
    }

    /**
     * 判断表中是否有数据
     */
    public boolean isDataExist(){
        int count = 0;

        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try{
            sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
            cursor = sqLiteDatabase.query(MySQLiteOpenHelper.TABLE_NAME, new String[]{"COUNT(Id)"},
                    null,null, null, null, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            if (count > 0) return true;

        }catch (Exception e){
            Log.e(TAG, "", e);
        }finally {
            if(cursor != null){
                cursor.close();
            }

            if(sqLiteDatabase != null){
                sqLiteDatabase.close();
            }
        }
        return false;
    }

    /**
     * 初始化数据
     */
    public void initTable(){
        SQLiteDatabase db = null;

        try {
            db = sqLiteOpenHelper.getWritableDatabase();
            db.beginTransaction();

            db.execSQL("insert into " + MySQLiteOpenHelper.TABLE_NAME + " (Id, CustomName, OrderPrice, Country) values (1, 'Arc', 100, 'China')");
            db.execSQL("insert into " + MySQLiteOpenHelper.TABLE_NAME + " (Id, CustomName, OrderPrice, Country) values (2, 'Bor', 200, 'USA')");
            db.execSQL("insert into " + MySQLiteOpenHelper.TABLE_NAME + " (Id, CustomName, OrderPrice, Country) values (3, 'Cut', 500, 'Japan')");
            db.execSQL("insert into " + MySQLiteOpenHelper.TABLE_NAME + " (Id, CustomName, OrderPrice, Country) values (4, 'Bor', 300, 'USA')");
            db.execSQL("insert into " + MySQLiteOpenHelper.TABLE_NAME + " (Id, CustomName, OrderPrice, Country) values (5, 'Arc', 600, 'China')");
            db.execSQL("insert into " + MySQLiteOpenHelper.TABLE_NAME + " (Id, CustomName, OrderPrice, Country) values (6, 'Doom', 200, 'China')");

            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.e(TAG, "", e);
        }finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }
}
