package com.example.orodr_000.myapplication;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;


public class ChildActivity extends Activity {
    public static final String EXTRA_IMAGE = "ChildActivity:points";
    private static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        //getActionBar().hide();
        setContentView(R.layout.activity_child);

        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        TextView points=(TextView)findViewById(R.id.activity_my_points_tv);
        getWindow().setEnterTransition(TransitionUtils.makeEnterTransition());
        getWindow().setSharedElementEnterTransition(TransitionUtils.makeSharedElementEnterTransition());
        setEnterSharedElementCallback(new EnterSharedElementCallback(this));


        points.setText(getIntent().getStringExtra(EXTRA_IMAGE));
        TextView highscore=(TextView)findViewById(R.id.highscore_tv);
        String theme=getIntent().getStringExtra("Theme");

        if(Integer.valueOf(getIntent().getStringExtra(EXTRA_IMAGE))>getIntent().getIntExtra("Highscore",0)){
            SharedPreferences.Editor editor= sharedpreferences.edit();
            editor.putString(theme,String.valueOf(points.getText()));
            editor.apply();
            highscore.setText("New Highscore");
        }else{
            highscore.setText("Highscore: "+String.valueOf(getIntent().getIntExtra("Highscore",0)));
        }
        Button okButton=(Button)findViewById(R.id.button2);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChildActivity.this, HListViewTest.class);
                startActivity(intent);
                ChildActivity.this.finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ChildActivity.this, HListViewTest.class);
        startActivity(intent);
        ChildActivity.this.finish();
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_child, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    public static void launch(Activity activity, View transitionView, String points,int highscore,String theme) {
        //ActivityOptionsCompat options =ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionView, EXTRA_IMAGE);

        Intent intent = new Intent(activity, ChildActivity.class);
        intent.putExtra(EXTRA_IMAGE, points);
        intent.putExtra("Highscore",highscore);
        intent.putExtra("Theme",theme);
        //ActivityCompat.startActivity(activity, intent, options.toBundle());
        try{ActivityCompat.startActivity(activity, intent, ActivityOptions.makeSceneTransitionAnimation(
                activity, transitionView, transitionView.getTransitionName()).toBundle());}
        catch(IllegalArgumentException e)
        {
            Log.d("Error","Illegal Argument");
        }
        activity.finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            trimCache(getApplicationContext()); //if trimCache is static
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void trimCache(Context applicationContext) {
        try {
            File dir = applicationContext.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }
}
