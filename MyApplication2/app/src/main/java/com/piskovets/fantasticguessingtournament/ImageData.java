package com.piskovets.fantasticguessingtournament;

import android.graphics.Bitmap;



public class ImageData {
    private Bitmap image;
    private String strAnswer;
    private String var1;
    private String var2;
    private String var3;
    private String var4;

    public String getVar1() {
        return var1;
    }

    public String getVar2() {
        return var2;
    }

    public String getVar3() {
        return var3;
    }

    public String getVar4() {
        return var4;
    }

    public ImageData(String strAnswer, String var1, String var2, String var3, String var4) {
        this.strAnswer = strAnswer;
        this.var1 = var1;
        this.var2 = var2;
        this.var3 = var3;
        this.var4 = var4;
    }

    public ImageData( String strAnswer, String var1, String var2, String var3, String var4,Bitmap image) {
        this.image = image;
        this.strAnswer = strAnswer;
        this.var1 = var1;
        this.var2 = var2;
        this.var3 = var3;
        this.var4 = var4;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getStrAnswer() {
        return strAnswer;
    }


}
