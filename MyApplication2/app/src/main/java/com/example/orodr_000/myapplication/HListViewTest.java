package com.example.orodr_000.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class HListViewTest extends FragmentActivity {

    //private static final String TAG =HListViewTest.class.getSimpleName();

    private List<MenuItem> buttonList=new ArrayList<>();
    private Activity activity;
    private CustomListAdapter adapter;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedPreferences;
    private TransitionFragment transitionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        //getActionBar().hide();
        setContentView(R.layout.activity_hlist_view_test);
        activity=this;
        final GridView listView=(GridView)findViewById(R.id.list);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //FragmentManager fm = getFragmentManager();
        //android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transitionFragment = new TransitionFragment();
        getFragmentManager().beginTransaction().replace(R.id.sample_content_fragment, transitionFragment).commit();
        //transaction.replace(R.id.sample_content_fragment, transitionFragment);
        //transaction.commit();


        findViewById(R.id.transitionElement).setVisibility(View.INVISIBLE);
        /*try {
            title=LoadFile("text_title.txt");
            theme=LoadFile("text_theme.txt");
            image=LoadFile("text_image.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i=0;i<title.length;i++){
            MenuItem b=new MenuItem();
            b.setName(title[i]);
            b.setTheme(theme[i]);
            //if(sharedPreferences.contains(title[i])){
                b.setPoints(sharedPreferences.getString(title[i],"0"));
            //}
            //else{
              //  b.setPoints("0");
            //}
            b.setImage(image[i]);
            buttonList.add(b);
        }*/
        MenuItemOperations menuDBoperation = new MenuItemOperations(this);
        menuDBoperation.open();

        for(int i=0;i< menuDBoperation.getEmployees().getCount();i++){
            MenuItem b=new MenuItem();
            b.setName(menuDBoperation.getMenuItem(i + 1).getName());
            b.setPoints(sharedPreferences.getString(menuDBoperation.getMenuItem(i + 1).getName(),"0"));
            b.setTheme(menuDBoperation.getMenuItem(i + 1).getTheme());
            b.setImage(menuDBoperation.getMenuItem(i + 1).getImage());
            buttonList.add(b);
        }
        adapter = new CustomListAdapter(this, buttonList);
        listView.setAdapter(adapter);
        final float[] currentXPosition = new float[1];
        final float[] currentYPosition = new float[1];


        listView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent me) {

                currentXPosition[0] = me.getX();
                currentYPosition[0] = me.getY();

                // Access text in the cell, or the object itself

              /*  try{
                    if(action==MotionEvent.ACTION_MOVE){

                    }else {
                        Quiz_Button s = (Quiz_Button) listView.getItemAtPosition(position);
                        //TextView tv = (TextView) listView.getChildAt(position);
                        Log.d("Item at position:", "Position: " + currentYPosition + " , " + "Item: " + s.getTitle());
                        //Log.d("Child at position:","Position: "+position+" , "+"Child: "+tv.getText());
                        Intent intent = new Intent(HListViewTest.this, MainActivity.class);
                        String file = s.getTitle();
                        String theme = s.getTheme();
                        intent.putExtra("Theme", theme);
                        intent.putExtra("File", file);
                        intent.putExtra("startX", currentXPosition);
                        intent.putExtra("startY", currentYPosition);
                        View myView = findViewById(R.id.transitionElement);
                        int finalRadius = Math.max(myView.getWidth(), myView.getHeight());
                        if (action == MotionEvent.ACTION_UP) {
                            Animator anim =
                                    ViewAnimationUtils.createCircularReveal(myView, (int) currentXPosition, (int) currentYPosition, 0, finalRadius);


                            myView.setVisibility(View.VISIBLE);
                            anim.setDuration(1000);
                            anim.start();

                            startActivity(intent);//, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                            activity.finish();
                        }
                    }
                 }
                catch (NullPointerException e){
                    Log.d("Touched in a bad place",e.getLocalizedMessage());
                }*/
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
                                    long id) {


                // We know the View is a <extView so we can cast it
                RelativeLayout clickedView = (RelativeLayout) view;
                final Intent intent = new Intent(HListViewTest.this, MainActivity.class);
                TextView fileview = (TextView) clickedView.findViewById(R.id.quiz_title);
                ImageView imageview=(ImageView)clickedView.findViewById(R.id.quiz_image);
                Bitmap myBitmap=((BitmapDrawable) imageview.getDrawable()).getBitmap();
                Palette palette = Palette.generate(myBitmap);
                int vibrant = palette.getVibrantColor(getResources().getColor(R.color.transition));
                Log.d("Vibrant Color", String.valueOf(vibrant));

                transitionFragment.setColor(vibrant);
                final String file = fileview.getText().toString();
                String theme = fileview.getHint().toString();
                intent.putExtra("Theme", theme);
                intent.putExtra("File", file);
                intent.putExtra("startX", currentXPosition[0]);
                intent.putExtra("startY", currentYPosition[0]);
                intent.putExtra("Image", String.valueOf(imageview.getTag()));
                intent.putExtra("Color",vibrant);
                ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (mWifi.isConnected()) {
                    // Do whatever
                    /*Animator anim =
                            ViewAnimationUtils.createCircularReveal(myView, (int) currentXPosition[0], (int) currentYPosition[0], 0, finalRadius);

                    anim.setInterpolator(new DecelerateInterpolator());
                    //myView.setVisibility(View.VISIBLE);
                    anim.addListener(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            FragmentManager fm = getFragmentManager();
                            transitionFragment=(TransitionFragment)fm.findFragmentById(R.id.fragment2);
                            transitionFragment.changeText(file);
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.show(transitionFragment);
                            ft.commit();
                        }
                    });

                    anim.setDuration(2000);
                    anim.start();*/

                    transitionFragment.changeText(file);
                    transitionFragment.revealShape((int) currentXPosition[0],(int) currentYPosition[0]);
                    startActivity(intent);//, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                    activity.finish();



                }else{
                    Toast.makeText(activity,"Connect to wifi",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }







  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hlist_view_test, menu);
        return true;
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
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
            e.printStackTrace();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
