package com.example.orodr_000.myapplication;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class MyDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "asset.db";
    private static final int DATABASE_VERSION = 1;

    public static final String MENU_ITEMS = "menu";
    public static final String ITEM_NAME = "Name";
    public static final String ITEM_THEME = "Theme";
    public static final String ITEM_IMAGE = "Image";
    public static final String ITEM_ID = "id";


    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


}
