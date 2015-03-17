package com.example.orodr_000.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;


public class ChildActivity extends ActionBarActivity {
    public static final String EXTRA_IMAGE = "ChildActivity:points";
    private static final String MyPREFERENCES = "MyPrefs" ;
    private TransitionFragment transitionFragment;
    View activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        activity=findViewById(R.id.transitionElement);
        //getActionBar().hide();
        setContentView(R.layout.activity_child);

        //final Toolbar toolbar = (Toolbar) findViewById(R.id.category_image_view);
        //toolbar.setBackgroundColor(getIntent().getIntExtra("Color",getResources().getColor(R.color.transition)));
        //toolbar.setBackgroundResource(getResources().getIdentifier("@drawable/" + getIntent().getStringExtra("Image"), "drawable", getApplicationContext().getPackageName()));
        //toolbar.setTitle("");

        //setSupportActionBar(toolbar);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        final int width = metrics.widthPixels;
        final int height = metrics.heightPixels;
        transitionFragment = new TransitionFragment();
        getFragmentManager().beginTransaction().replace(R.id.sample_content_fragment, transitionFragment).commit();
        findViewById(R.id.transitionElement3).setVisibility(View.INVISIBLE);

        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        TextView points=(TextView)findViewById(R.id.activity_my_points_tv);
        getWindow().setEnterTransition(TransitionUtils.makeEnterTransition());
        getWindow().setSharedElementEnterTransition(TransitionUtils.makeSharedElementEnterTransition());
        setEnterSharedElementCallback(new EnterSharedElementCallback(this));


        points.setText(getIntent().getStringExtra(EXTRA_IMAGE));
        TextView highscore=(TextView)findViewById(R.id.highscore_tv);

        final String theme=getIntent().getStringExtra("Theme");
        final ImageView imageView=(ImageView)findViewById(R.id.category_image_view);
        Log.d("Image Tag:",getIntent().getStringExtra("Image"));
        imageView.setImageResource(getResources().getIdentifier("@drawable/" + getIntent().getStringExtra("Image"), "drawable", getApplicationContext().getPackageName()));
        imageView.setTag(getIntent().getStringExtra("Image"));

        if(Integer.valueOf(getIntent().getStringExtra(EXTRA_IMAGE))>getIntent().getIntExtra("Highscore",0)){
            SharedPreferences.Editor editor= sharedpreferences.edit();
            editor.putString(theme,String.valueOf(points.getText()));
            editor.apply();
            highscore.setText("New Highscore");
        }else{
            highscore.setText("Highscore: "+String.valueOf(getIntent().getIntExtra("Highscore",0)));
        }
        final Button okButton=(Button)findViewById(R.id.button2);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChildActivity.this, HListViewTest.class);
                startActivity(intent);
                ChildActivity.this.finish();

            }
        });
        Button replayButton=(Button)findViewById(R.id.button8);
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChildActivity.this,MainActivity.class);
                intent.putExtra("File", theme);
                intent.putExtra("Image", String.valueOf(imageView.getTag()));
                intent.putExtra("Color",getIntent().getIntExtra("Color",getResources().getColor(R.color.transition)));
                okButton.setVisibility(View.INVISIBLE);
                v.setVisibility(View.INVISIBLE);
                //toolbar.setVisibility(View.INVISIBLE);
                transitionFragment.setColor(getIntent().getIntExtra("Color",getResources().getColor(R.color.transition)));
                transitionFragment.changeText(theme);
                transitionFragment.revealShape(width/2, height/2);
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
