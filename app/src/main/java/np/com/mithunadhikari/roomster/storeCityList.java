package np.com.mithunadhikari.roomster;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class storeCityList extends SQLiteOpenHelper {
    List add_data = new ArrayList<>();
    SQLiteDatabase db;

    public storeCityList(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    public void setString(List str) {
        add_data = str;
        getString();
        //System.out.println("Received list are"+add_data);
        //onCreate(db);
        fill_data();

    }
    void fill_data(){
        for (int i = 0; i < add_data.size(); i++) {
            String ROW1 = "INSERT INTO store_city Values ("+ add_data.get(i)+" );";
            db.execSQL(ROW1);
            /*System.out.println("Data are inserted");*/
        }
    }
    public List getString() {
        System.out.println("hello yeah"+add_data);
       return add_data;

    }

    @Override
    public void onCreate(SQLiteDatabase  db) {
        String cityTable = "create table if not exists  store_city (City text not null) ";
        db.execSQL(cityTable);
        for (int i = 0; i < add_data.size(); i++) {
            String ROW1 = "INSERT INTO store_city Values ("+ add_data.get(i)+" );";
            db.execSQL(ROW1);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop = "drop table if exists store_city ";
        db.execSQL(drop);
        onCreate(db);
    }
}
