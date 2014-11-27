package com.example.orodr_000.myapplication;

import org.json.JSONObject;

/**
 * Created by orodr_000 on 21.11.2014.
 */
public class ImageData {
    JSONObject json;
    String strAnswer;
    String strSearchText;
    int[] randArray;

    public JSONObject getJson() {
        return json;
    }

    public String getStrAnswer() {
        return strAnswer;
    }

    public String getStrSearchText() {
        return strSearchText;
    }

    public String[] getStrTextArray() {
        return strTextArray;
    }

    public int[] getRandArray() {
        return randArray;
    }

    public ImageData(JSONObject json, String strAnswer, String strSearchText, String[] strTextArray,int[] randArray) {

        this.json = json;
        this.strAnswer = strAnswer;
        this.strSearchText = strSearchText;
        this.strTextArray = strTextArray;
        this.randArray=randArray;

    }

    String[] strTextArray;

}
