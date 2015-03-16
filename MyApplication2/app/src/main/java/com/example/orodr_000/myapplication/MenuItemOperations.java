package com.example.orodr_000.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class MenuItemOperations {
    private MyDatabase dbHelper;
    private String[] MenuItem_TABLE_COLUMNS = { MyDatabase.ITEM_NAME, MyDatabase.ITEM_THEME,MyDatabase.ITEM_IMAGE};
    private SQLiteDatabase database;

    public MenuItemOperations(Context context) {
        dbHelper = new MyDatabase(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getReadableDatabase();
    }

    public void close() {
        dbHelper.close();
    }



    public MenuItem getMenuItem(int i){
        Cursor cursor = database.query(MyDatabase.MENU_ITEMS,
                MenuItem_TABLE_COLUMNS, MyDatabase.ITEM_ID + " = "
                        + i, null, null, null, null);

        cursor.moveToFirst();

        MenuItem newComment = parseMenuItem(cursor);
        cursor.close();
        return newComment;
    }

    public Cursor getEmployees() {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {"Name", "Theme","Image"};
        String sqlTables = "menu";


        qb.setTables(sqlTables);
        Cursor c = qb.query(database, sqlSelect, null, null,
                null, null, null);

        c.moveToFirst();
        return c;

    }



    public List<String> getAllVariants(String file){
        List<String> strVariants=new ArrayList<>();
        Cursor c=database.rawQuery("SELECT Name FROM "+file, null);
        Log.d("",String.valueOf(database.getMaximumSize()));
        if(c.getCount()==0)
        {
            Log.d("Error", "No records found");
            return null;
        }
        while(c.moveToNext())
        {
            strVariants.add(c.getString(0));
            //buffer.append("Rollno: "+c.getString(0)+"\n");

        }
        //Log.d("Student Details", buffer.toString());
        c.close();
        return strVariants;
    }
    public String getUrls(String file,String Name){
        String result=null;
        Cursor c=database.rawQuery("SELECT Urls FROM "+file+" WHERE Name=?",new String[]{Name});
        if(c.getCount()==0)
        {
            Log.d("Error", "No records found");
            return null;
        }
        while(c.moveToNext()){
            result=c.getString(0);
        }
        c.close();
        return result;

    }

    private MenuItem parseMenuItem(Cursor cursor) {
        MenuItem MenuItem = new MenuItem();
        //MenuItem.setId(cursor.getInt());
        MenuItem.setName(cursor.getString(0));
        MenuItem.setTheme(cursor.getString(1));
        MenuItem.setImage(cursor.getString(2));
        return MenuItem;
    }
}


