package com.example.orodr_000.myapplication;


public class Quiz_Button
{
    private String title,theme,thumbnailUrl;
    public Quiz_Button(){

    }

    public Quiz_Button(String title, String thumbnailUrl, String theme) {

        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.theme = theme;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
