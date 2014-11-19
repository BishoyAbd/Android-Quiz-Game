package com.example.orodr_000.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.lucasr.twowayview.TwoWayView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class HListViewTest extends Activity {

    private static final String TAG =HListViewTest.class.getSimpleName();

    private static final String url="C:/Users/orodr_000/Desktop/test.json";
    private List<Quiz_Button> buttonList=new ArrayList<Quiz_Button>();
    private TwoWayView listView;
    private CustomListAdapter adapter;
    private String[] title;
    private String[] theme;
    private String[] image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_hlist_view_test);

        listView=(TwoWayView) findViewById(R.id.list);
        listView.setItemMargin(10);
        try {
            title=LoadFile("text_title.txt");
            theme=LoadFile("text_theme.txt");
            image=LoadFile("text_image.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i=0;i<title.length;i++){
            Quiz_Button b=new Quiz_Button();
            b.setTitle(title[i]);
            b.setTheme(theme[i]);
            b.setThumbnailUrl(image[i]);
            buttonList.add(b);
        }
        adapter=new CustomListAdapter(this,buttonList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
                                    long id) {


                // We know the View is a <extView so we can cast it
                RelativeLayout clickedView = (RelativeLayout) view;
                Intent intent = new Intent(HListViewTest.this, MyActivity.class);
                TextView fileview=(TextView)clickedView.findViewById(R.id.quiz_title);
                String file = fileview.getText().toString();
                String theme=fileview.getHint().toString();
                intent.putExtra("Theme", theme);
                intent.putExtra("File",file);
                startActivity(intent);


            }
        });
    }

    public String[] LoadFile(String name) throws IOException {
        AssetManager am = getAssets();
        am.list("");
        InputStream is = am.open(name);


        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        String[] strArray = new String[0];


        while ((line = br.readLine()) != null) {
            strArray = line.split(",");
        }

        br.close();

        return strArray;
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hlist_view_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
