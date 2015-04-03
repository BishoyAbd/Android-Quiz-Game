package com.piskovets.fantasticguessingtournament;

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



    public MenuItem getMenuItem(int i,String locale){
        switch (locale){
            /*case "українська":
                MenuItem_TABLE_COLUMNS[0]=MyDatabase.ITEM_NAME_UA;
                break;
            /*case "français":
                MenuItem_TABLE_COLUMNS[0]=MyDatabase.ITEM_NAME_FR;
                break;
            case "русский":
                MenuItem_TABLE_COLUMNS[0]=MyDatabase.ITEM_NAME_RU;
                break;*/
            case "English":
            default:
                MenuItem_TABLE_COLUMNS[0]=MyDatabase.ITEM_NAME;
                break;
        }
        Cursor cursor = database.query(MyDatabase.MENU_ITEMS,
                MenuItem_TABLE_COLUMNS, MyDatabase.ITEM_ID + " = "
                        + i, null, null, null, null);

        cursor.moveToFirst();

        MenuItem newComment = parseMenuItem(cursor);
        cursor.close();
        return newComment;
    }
    public String getDefaultTableName(String locale,String Name){
        String result=null;
        Cursor c;

        switch (locale){
            /*case "українська":
                c=database.rawQuery("SELECT Name FROM menu WHERE Name_ua=?",new String[]{Name});
                break;
            /*case "français":
                c=database.rawQuery("SELECT Name FROM menu WHERE Name_fr=?",new String[]{Name});

                break;
            case "русский":
                c=database.rawQuery("SELECT Name FROM menu WHERE Name_ru=?",new String[]{Name});

                break;*/
            case "English":
            default:
                //c=database.rawQuery("SELECT Name FROM menu WHERE Name=?",new String[]{Name});
            return Name;

        }
        //if(c.getCount()==0)
        //{
            //Log.d("Error", "No records found");
            //return null;
        //}
        //while(c.moveToNext()){
          //  result=c.getString(0);
        //}
        //c.close();
        //return result;

    }

    public Cursor getEmployees() {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {"Name", "Theme","Image"};
        String sqlTables = "menu";


        qb.setTables(sqlTables);
        Cursor c = qb.query(database, sqlSelect, null, null,
                null, null, null);

        c.moveToFirst();
        c.close();
        return c;

    }



    public List<String> getAllVariants(String locale,String file){
        List<String> strVariants=new ArrayList<>();
        Cursor c;
        switch (locale){
            /*case "українська":
                c=database.rawQuery("SELECT Name_ua FROM "+file, null);
                break;
            /*case "français":
                c=database.rawQuery("SELECT Name_fr FROM "+file, null);
                break;
            case "русский":
                c=database.rawQuery("SELECT Name_ru FROM "+file, null);
                break;*/
            case "English":
            default:
                c=database.rawQuery("SELECT Name FROM "+file, null);
                break;
        }

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
    public String getUrls(String locale,String file,String Name){
        String result=null;
        Cursor c;
        switch (locale){
            /*case "українська":
                c=database.rawQuery("SELECT Urls FROM "+file+" WHERE Name_ua=?",new String[]{Name});
                break;
            /*case "français":
                c=database.rawQuery("SELECT Urls FROM "+file+" WHERE Name_fr=?",new String[]{Name});
                break;
            case "русский":
                c=database.rawQuery("SELECT Urls FROM "+file+" WHERE Name_ru=?",new String[]{Name});
                break;*/
            case "English":
            default:
                c=database.rawQuery("SELECT Urls FROM "+file+" WHERE Name=?",new String[]{Name});
                break;
        }

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


