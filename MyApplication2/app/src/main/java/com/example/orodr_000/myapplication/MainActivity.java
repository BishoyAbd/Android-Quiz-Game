package com.example.orodr_000.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;


public class MainActivity extends ActionBarActivity {
    private List<Integer> randList=new ArrayList<>();
    private List<ImageData> myData=new ArrayList<>();
    private List<String> strList=new ArrayList<>();
    private String[] strVariantArray=new String[4];
    private Activity activity;

    private Button var1;
    private Button var2;
    private Button var3;
    private Button var4;
    private TextView pnts;
    private RoundCornerProgressBar progress;
    private String strFile;
    private String str_File;
    private CountDownTimer countDownTimer;
    private long remainingTime;
    private int points;
    private TickPlusDrawable tickPlusDrawable;
    private boolean gameStart=false;
    private View animView;
    private TextView timer_tv;


    private ImageView image;
    private static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedpreferences;
    private android.app.Fragment pauseFragment;
    private AnimationDrawable imageAnimation;
    private boolean isOverLimit=false;
    private RevealFragment revealFragment1;
    private View myView;
    private boolean imageIsLoaded=true;
    private MenuItemOperations menuDBoperation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);
        Intent i = getIntent();
        Log.d("Image",i.getStringExtra("Image"));
        strFile = i.getStringExtra("File");
        //getActionBar().setTitle(strFile);
        str_File=strFile.replaceAll(" ","_");
        //startX=(int)i.getFloatExtra("startX",0);
        //startY=(int)i.getFloatExtra("startY",0);

        myView = findViewById(R.id.transitionElement2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);

        ImageView menuImage=(ImageView)findViewById(R.id.category_image_view);
        menuImage.setImageResource(getResources().getIdentifier("@drawable/" + i.getStringExtra("Image"), "drawable", getApplicationContext().getPackageName()));
        TextView menuTitle=(TextView) findViewById(R.id.menu_title);
        menuTitle.setText(strFile);


        animView = findViewById(R.id.animView);
        animView.setEnabled(false);
        View backgroundView = findViewById(R.id.animView1);
        tickPlusDrawable = new TickPlusDrawable(getResources().getDimensionPixelSize(R.dimen.stroke_width),getResources().getColor(R.color.background),Color.parseColor("#FF5722"),Color.parseColor("#4CAF50"));
        animView.setBackground(tickPlusDrawable);
        backgroundView.setBackground(new TickBackgroundDrawable(getResources().getColor(R.color.background)));

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        image =(ImageView)findViewById(R.id.view);
        var1 = (FontFitButton) findViewById(R.id.activity_my_variant1_btn);
        var2 = (FontFitButton) findViewById(R.id.activity_my_variant2_btn);
        var3 = (FontFitButton) findViewById(R.id.activity_my_variant3_btn);
        var4 = (FontFitButton) findViewById(R.id.activity_my_variant4_btn);
        var1.setEnabled(false);
        var2.setEnabled(false);
        var3.setEnabled(false);
        var4.setEnabled(false);
        var1.setText("");
        var2.setText("");
        var3.setText("");
        var4.setText("");
        pnts = (TextView) findViewById(R.id.activity_my_points_tv);

        progress = (RoundCornerProgressBar) findViewById(R.id.progressBar);



        progress.setMinimumHeight(80);
        progress.setMax(45);
        progress.setProgress(45);
        progress.setRotation(180);
        progress.setBackgroundColor(getResources().getColor(R.color.background));
        progress.setProgressColor(Color.parseColor("#4CAF50"));

        timer_tv=(TextView)findViewById(R.id.timer_tv);
        //progress.getProgressDrawable().setColorFilter(Color.parseColor("#4CAF50"), PorterDuff.Mode.SRC_ATOP);

        android.app.FragmentManager fm = getFragmentManager();
        pauseFragment=fm.findFragmentById(R.id.fragment);
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.hide(pauseFragment);
        ft.commit();

        /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        revealFragment1 = new RevealFragment();
        transaction.replace(R.id.fragment3, revealFragment1);
        transaction.commit();*/
        FragmentManager fragmentManager=getSupportFragmentManager();
        revealFragment1=(RevealFragment)fragmentManager.findFragmentById(R.id.fragment3);
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.show(revealFragment1);
        transaction.commit();



        /*try {
            strTextArray = LoadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        menuDBoperation=new MenuItemOperations(this);
        menuDBoperation.open();
        strList=menuDBoperation.getAllVariants(str_File);
        points = 0;
        activity = this;

        for(int j = 0; j < strList.size(); j++) randList.add(j);
        new getImageTask().execute();


    }

    public void addTime(long addedTime) {
        remainingTime=remainingTime + addedTime;
        showTimer(remainingTime);
    }

    public void decreaseTime(long decTime) {
        remainingTime=remainingTime - decTime;
        showTimer(remainingTime);
    }

    public void showTimer(final long millisecondsToCountDown) {

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(millisecondsToCountDown, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;
                progress.setProgress((int) millisUntilFinished / 1000);
                timer_tv.setText(String.valueOf((int) millisUntilFinished / 1000));
                if(remainingTime<31000){
                    if (remainingTime < 16000) {
                        progress.setProgressColor(Color.parseColor("#FF5722"));
                        //progress.getProgressDrawable().setColorFilter(Color.parseColor("#FF5252"), PorterDuff.Mode.SRC_IN);
                    } else {
                        //progress.getProgressDrawable().setColorFilter(Color.parseColor("#FFEB3B"), PorterDuff.Mode.SRC_IN);
                        progress.setProgressColor(Color.parseColor("#FFd600"));
                    }
                }else {
                    //progress.getProgressDrawable().setColorFilter(Color.parseColor("#4CAF50"), PorterDuff.Mode.SRC_IN);
                    progress.setProgressColor(Color.parseColor("#4CAF50"));
                }
                if(remainingTime<23500){
                    timer_tv.setTextColor(Color.BLACK);
                }

            }


            @Override
            public void onFinish() {
                Log.d("Timer","Timer Finished");
                Glide.clear(image);
                image.setBackground(null);
                image.setImageBitmap(null);
                var1.setText("");
                var2.setText("");
                var3.setText("");
                var4.setText("");
                var1.setEnabled(false);
                var2.setEnabled(false);
                var3.setEnabled(false);
                var4.setEnabled(false);

                int highscore=Integer.parseInt(sharedpreferences.getString(strFile,"0"));
                getWindow().setExitTransition(TransitionUtils.makeExitTransition());
                myData.clear();
                ChildActivity.launch(MainActivity.this, findViewById(R.id.activity_my_points_tv), String.valueOf(points),highscore,strFile);


                /*AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                builder.setTitle("Results");

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        activity.finish();
                        Intent intent = new Intent(MainActivity.this, HListViewTest.class);
                        startActivity(intent);
                        //dialog.dismiss();
                    }
                });


                int highscore=Integer.parseInt(sharedpreferences.getString(strFile,"0"));
                if(points>highscore){
                    SharedPreferences.Editor editor=sharedpreferences.edit();
                    editor.putString(strFile,String.valueOf(points));
                    editor.apply();
                    String alert1 = "New High Score";
                    String alert2 = "Your Result: " + String.valueOf(points);
                    builder.setMessage(alert1 +"\n"+ alert2);

                }else{
                    String alert1 = "High Score: "+String.valueOf(highscore);
                    String alert2 = "Your Result: " + String.valueOf(points);
                    builder.setMessage(alert1 +"\n"+ alert2);
                }


                Log.d("Editor", "Theme " + strFile);
                AlertDialog alert11 = builder.create();
                alert11.setCanceledOnTouchOutside(false);
                alert11.setOnCancelListener(new DialogInterface.OnCancelListener()
                {
                    @Override
                    public void onCancel(DialogInterface dialog)
                    {
                        MainActivity.this.finish();
                        Intent intent = new Intent(MainActivity.this, HListViewTest.class);
                        startActivity(intent);
                    }
                });
                alert11.show();*/

             }
        }.start();

    }

    /*public String getImage(JsonArray resultArray) {
        String strImage_url;
        int randInt = new Random().nextInt(resultArray.size());
        strImage_url = resultArray.get(randInt).getAsJsonObject().get("unescapedUrl").getAsString();
        Log.d("Ok","Got Url");
        return strImage_url;
    }*/

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }*/

    public void buttonClick(View v) {
        //Glide.clear(image);
        //loadImage();

    }

    public void onPauseClick(View v){
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_top,
                R.anim.slide_out_top);
        if (pauseFragment.isHidden()) {
            ft.show(pauseFragment);

            tickPlusDrawable.animatePlay();
            countDownTimer.cancel();
            var1.setEnabled(false);
            var2.setEnabled(false);
            var3.setEnabled(false);
            var4.setEnabled(false);
            image.setEnabled(false);

        } else {
            ft.hide(pauseFragment);
            tickPlusDrawable.animatePause();
            showTimer(remainingTime);
            var1.setEnabled(true);
            var2.setEnabled(true);
            var3.setEnabled(true);
            var4.setEnabled(true);
            image.setEnabled(true);
        }
        ft.commit();
    }

    public boolean CheckAnswer(String a, String b) {
        return a.equalsIgnoreCase(b);
    }

    public int AddPoints() {
        if (remainingTime > 40000) {
            addTime(45000 - remainingTime);
        } else {
            addTime(5000);
        }
        if (points < 1000) {
            return 100;

        } else {
            if (points < 5000) {
                return 250;
            } else {
                if (points < 20000) {
                    return 500;
                }else{
                    return 1000;
                }
            }
        }
    }

    public int DecreasePoints() {
        //decreaseTime(3000);
        if (points < 1000) {
            return -50;
        } else {
            if (points < 5000) {
                return -150;
            } else {
                if (points < 20000) {
                    return -350;
                }else{
                    return -750;
                }
            }
        }

    }

    public void AnswerClick(View v) {

        Button btn=(Button) v;
        String answ;
        var1.setEnabled(false);
        var2.setEnabled(false);
        var3.setEnabled(false);
        var4.setEnabled(false);
        int changePoints;
        int counter = 0;
        answ = myData.get(counter).getStrAnswer();

        if (CheckAnswer(btn.getText().toString(), answ)) {

            changePoints=AddPoints();
            tickPlusDrawable.animateTick();


        } else {

            changePoints=DecreasePoints();
            tickPlusDrawable.animateCross();

        }

        AnimateText(pnts,points,points+changePoints);

        //pnts.setText(String.valueOf(points));



        try{
            myData.remove(0);
        }catch(IndexOutOfBoundsException e){
            new showImageTask().execute();
        }
        if(myData.size()>1){
            Log.d("Animation", "size is more than 1");
            image.setBackgroundResource(R.drawable.imageanim);
            imageAnimation = (AnimationDrawable) image.getBackground();
            image.setImageBitmap(null);
            imageAnimation.run();
            checkIfAnimationDone(imageAnimation);
        }else{
            progress.setProgress((int) remainingTime / 1000);
            countDownTimer.cancel();
            Log.d("Animation","size is less than 1");
            image.setBackgroundResource(R.drawable.loadanim);
            imageAnimation = (AnimationDrawable) image.getBackground();
            image.setImageBitmap(null);
            imageAnimation.start();
            imageIsLoaded=false;
        }

        Log.d("Ok","");
        if(isOverLimit){
            isOverLimit=false;
            new showImageTask().execute();}
    }
    public void AnimateText(final TextView view,final int start, final int end){
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(start,end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                int value=start+(int)animation.getAnimatedValue();
                view.setText(String.valueOf(value));
            }
        });
        animator.setEvaluator(new TypeEvaluator<Integer>() {
            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                return Math.round((endValue - startValue) * fraction);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                points=end;

            }
        });
        animator.setDuration(500);
        animator.start();
    }

    private void checkIfAnimationDone(AnimationDrawable anim){
        final AnimationDrawable a = anim;
        int timeBetweenChecks = 300;
        Handler h = new Handler();
        h.postDelayed(new Runnable(){
            public void run(){
                if (a.getCurrent() != a.getFrame(a.getNumberOfFrames() - 1)){
                    checkIfAnimationDone(a);
                } else{
                    loadImage();
                }
            }
        }, timeBetweenChecks);
    }

    public void loadImage(){
        try {
            if(pauseFragment.isHidden()) {
                tickPlusDrawable.animatePause();
            }
        }catch(IllegalStateException e){
            Log.d("Error","Cannot start Animator");
        }

        try{
        if (imageAnimation!=null && imageAnimation.isRunning()) {
            imageAnimation.stop();
            if(countDownTimer!=null && pauseFragment.isHidden()){
                showTimer(remainingTime);
            }
        }
        image.setImageBitmap(myData.get(0).getImage());

        var1.setText(String.valueOf(myData.get(0).getVar1()));
        var2.setText(String.valueOf(myData.get(0).getVar2()));
        var3.setText(String.valueOf(myData.get(0).getVar3()));
        var4.setText(String.valueOf(myData.get(0).getVar4()));
        Log.d("Variant",var1.getText().toString());
        var1.setEnabled(true);
        var2.setEnabled(true);
        var3.setEnabled(true);
        var4.setEnabled(true);
        }catch (IndexOutOfBoundsException e){
            Log.d("Error","Out of Bounds");
            new showImageTask().execute();
        }
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

    public void loadingAnimation(final View myView){
        myView.postDelayed(new Runnable() {

            @Override
            public void run() {
                /*Animator anim =
                        ViewAnimationUtils.createCircularReveal(myView, myView.getWidth() / 2, myView.getHeight() / 2, myView.getWidth(), 0);

                anim.addListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        myView.setVisibility(View.INVISIBLE);

                    }
                });
                //anim.setInterpolator(new DecelerateInterpolator());
                anim.setDuration(2000);
                anim.start();*/
                //revealFragment1.changeText(strFile);
                revealFragment1.hideShape(myView.getWidth() / 2, myView.getHeight() / 2);
            }
        }, 10);

    }

    public void showFragment(){

        //new Handler().postDelayed(new Runnable() {

            //@Override
          //  public void run() {
                revealFragment1.changeText(strFile);
          //  }
        //},10);

    }

    public class getImageTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /*image.setBackgroundResource(R.drawable.loadanim);
            imageAnimation = (AnimationDrawable) image.getBackground();
            imageAnimation.start();*/

            showFragment();

        }

        @Override
        protected Void doInBackground(Void... params) {



            //showFragment();
            NextImage();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //imageAnimation.stop();


        }

    }

    public class showImageTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            NextImage();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
       }

    }

    public void NextImage() {


        int[] randArray = new int[4];

        Collections.shuffle(randList, new Random());
        for (int i = 0; i < 4; i++) {
            //int randInt = new Random().nextInt(strTextArray.length);
            randArray[i] = randList.get(i);
            //strVariantArray[i]=strTextArray[randArray[i]];
            strVariantArray[i]=strList.get(randArray[i]);
        }


        int randInt = new Random().nextInt(randArray.length);
        String strSearchText = strVariantArray[randInt];
        //String strAnswer = strSearchText;
        //String fileName = strTheme;
        //strSearchText = fileName + " " + strSearchText;
        //strSearchText = Uri.encode(strSearchText);
        myData.add(new ImageData(strSearchText, strVariantArray[0], strVariantArray[1], strVariantArray[2], strVariantArray[3]));
        /*String url;
        url = "https://ajax.googleapis.com/ajax/services/search/images?" +
                "v=1.0&q=" + strSearchText + "&rsz=8&imgsz=medium|large&imgtype=photo";

        Log.d("Next Image","Search string => "+ strSearchText);
        getJsonRequest(url);*/
        Log.d("Next Image","Search string => "+ strSearchText);
        getImageFromDB(str_File, strSearchText);


    }
    public void getImageFromDB(String file,String name){
        String returnImage= menuDBoperation.getUrls(file,name);
        String[] returnArray=returnImage.split(",");
        int randInt = new Random().nextInt(returnArray.length);
        final String mImage_url=returnArray[randInt];
        activity.runOnUiThread(new Runnable() {
            public void run() {
                //Toast.makeText(activity, "Hello", Toast.LENGTH_SHORT).show();
                Glide.with(activity).load(mImage_url).asBitmap().into(new SimpleTarget<Bitmap>(500, 500) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        try{

                            myData.get(myData.size()-1).setImage(bitmap);
                            Log.d("Ok","Image Added; Array Size: "+myData.size());
                            if(!gameStart){
                                loadingAnimation(myView);
                                gameStart=true;
                                try {
                                    long startTime = 46 * 1000;
                                    showTimer(startTime);
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                animView.setEnabled(true);
                                loadImage();
                            }
                            if(!imageIsLoaded) {
                                imageIsLoaded = true;
                                loadImage();
                            }
                            if(myData.size()<20){
                                NextImage();}
                            else {
                                isOverLimit=true;
                            }
                        }
                        catch(ArrayIndexOutOfBoundsException e){
                            Log.d("Error","Out of Bounds");
                            NextImage();
                        }
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        try {
                            myData.remove(myData.size() - 1);
                            Log.d("Error", "Image Load Error; Array Size: " + myData.size());
                            NextImage();
                        }catch(ArrayIndexOutOfBoundsException ex){
                            Log.d("Error","Out of Bounds");
                            NextImage();
                        }

                    }
                });
            }
        });


    }

    /*public void getJsonRequest(final String url){
        /*Ion.with(activity)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                         try {
                            if (e != null){
                                Log.d("Error",e.toString());
                                throw e;}
                            JsonArray results = result.getAsJsonObject("responseData").getAsJsonArray("results");
                            Log.d("Ok","Results Loaded");
                            mImage_url = getImage(results);

                            Glide.with(activity).load(mImage_url).asBitmap().into(new SimpleTarget<Bitmap>(250, 250) {
                                @Override
                                public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                    myData.get(myData.size()-1).setImage(bitmap);
                                    myData.get(myData.size()-1).setStrSearchText(url);
                                    Log.d("Ok","Image Added; Array Size: "+myData.size());
                                    if(!gameStart){
                                        gameStart=true;
                                        try {
                                            long startTime = 46 * 1000;
                                            showTimer(startTime);
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                        }
                                        animView.setEnabled(true);
                                        loadImage();
                                    }

                                    if(myData.size()<20){
                                        NextImage();}
                                    else {
                                        isOverLimit=true;
                                    }
                                }

                                @Override
                                public void onLoadFailed(Exception e, Drawable errorDrawable) {

                                    myData.remove(myData.size()-1);
                                    Log.d("Error","Image Load Error; Array Size: "+myData.size());
                                    NextImage();

                                }
                            });

                        }
                        catch (Exception ex) {
                          //Toast.makeText(activity, ex.toString(), Toast.LENGTH_LONG).show();
                            Log.d("Error",ex.toString());
                            myData.remove(myData.size()-1);
                            NextImage();
                        }

                    }
                });*/
        /*final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("JsonObject",response.toString());
                            JSONObject jObject = response.getJSONObject("responseData");
                            //Log.d("JsonObject",jObject.toString());
                            JSONArray jsonArray = jObject.getJSONArray("results");
                            mImage_url=getVolleyImage(jsonArray);
                            Glide.with(activity).load(mImage_url).asBitmap().into(new SimpleTarget<Bitmap>(500, 500) {
                                @Override
                                public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                    try{

                                        myData.get(myData.size()-1).setImage(bitmap);
                                        Log.d("Ok","Image Added; Array Size: "+myData.size());
                                        if(!gameStart){
                                            loadingAnimation(myView);
                                            gameStart=true;
                                            try {
                                                long startTime = 46 * 1000;
                                                showTimer(startTime);
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                            }
                                            animView.setEnabled(true);
                                            loadImage();
                                        }
                                        if(!imageIsLoaded) {
                                            imageIsLoaded = true;
                                            loadImage();
                                        }



                                        if(myData.size()<20){
                                            NextImage();}
                                        else {
                                            isOverLimit=true;
                                        }
                                    }
                                    catch(ArrayIndexOutOfBoundsException e){
                                        Log.d("Error","Out of Bounds");
                                        NextImage();
                                    }
                                }

                                @Override
                                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                    try {
                                        myData.remove(myData.size() - 1);
                                        Log.d("Error", "Image Load Error; Array Size: " + myData.size());
                                        NextImage();
                                    }catch(ArrayIndexOutOfBoundsException ex){
                                        Log.d("Error","Out of Bounds");
                                        NextImage();
                                    }

                                }
                            });

                        } catch (JSONException e) {
                            //e.printStackTrace();
                            /*try {
                                myData.remove(myData.size() - 1);
                                Log.d("JsonException", "JSONEXCEPTION");
                                Log.d("Error", "Image Load Error; Array Size: " + myData.size());
                                NextImage();
                            }catch(ArrayIndexOutOfBoundsException eex){
                                NextImage();
                            }*/
                            /*Log.d("Error", "Image Load Error; Array Size: " + myData.size());
                            e.printStackTrace();
                            /*myData.clear();
                            if(countDownTimer!=null)
                                countDownTimer.cancel();
                            Intent intent = new Intent(MainActivity.this, HListViewTest.class);
                            startActivity(intent);
                            activity.finish();*/
                           /* throw new RuntimeException(e);
                        }
                        catch(IllegalArgumentException ex){
                            Log.d("Error","Cannot Load Activity");
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(activity).addToRequestQueue(jsonObjReq);
    }*/

    /*private String getVolleyImage(JSONArray resultArray) {
        String strImage_url = null;
        int randInt = new Random().nextInt(resultArray.length());

        try {
            JSONObject obj;
            obj = resultArray.getJSONObject(randInt);
            strImage_url = obj.getString("unescapedUrl");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Ok","Got Url");
        return strImage_url;
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myData.clear();
        if(countDownTimer!=null)
            countDownTimer.cancel();
        Intent intent = new Intent(MainActivity.this, HListViewTest.class);
        startActivity(intent);
        activity.finish();
    }

    public class TickPlusDrawable extends Drawable {

        private static final long ANIMATION_DURATION = 200;
        private final Interpolator ANIMATION_INTERPOLATOR = new DecelerateInterpolator();

        private Paint mLinePaint;
        private Paint mBackgroundPaint;

        private float[] mPoints = new float[8];
        private final RectF mBounds = new RectF();

        private ArgbEvaluator mArgbEvaluator = new ArgbEvaluator();

        private float mRotation;

        private int mStrokeWidth = 10;
        private int mTickColor = Color.BLUE;
        private int mPlusColor = Color.WHITE;
        private int mCrossColor= Color.RED;

        public TickPlusDrawable(int strokeWidth, int tickColor, int crossColor, int plusColor) {
            mStrokeWidth = strokeWidth;
            mTickColor = tickColor;
            mPlusColor = plusColor;
            mCrossColor = crossColor;
            setupPaints();
        }

        private void setupPaints() {
            mLinePaint = new Paint(ANTI_ALIAS_FLAG);
            mLinePaint.setStyle(Paint.Style.STROKE);
            mLinePaint.setColor(Color.parseColor("#3F51B5"));
            mLinePaint.setStrokeWidth(mStrokeWidth);
            mLinePaint.setStrokeCap(Paint.Cap.ROUND);

            mBackgroundPaint = new Paint(ANTI_ALIAS_FLAG);
            mBackgroundPaint.setStyle(Paint.Style.FILL);
            mBackgroundPaint.setColor(mTickColor);
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);

            int padding = bounds.centerX()/2;

            mBounds.left = bounds.left + padding;
            mBounds.right = bounds.right - padding;
            mBounds.top = bounds.top + padding;
            mBounds.bottom = bounds.bottom - padding;

            setupPauseMode();
        }

        private void setupPauseMode() {
            mPoints[0] = mBounds.centerX()-mBounds.centerX()/4;
            mPoints[1] = mBounds.top;
            mPoints[2] = mBounds.centerX()-mBounds.centerX()/4;
            mPoints[3] = mBounds.bottom;
            mPoints[4] = mBounds.centerX()+mBounds.centerX()/4;
            mPoints[5] = mBounds.top;
            mPoints[6] = mBounds.centerX()+mBounds.centerX()/4;
            mPoints[7] = mBounds.bottom;
        }

        private float x(int pointIndex) {
            return mPoints[xPosition(pointIndex)];
        }

        private float y(int pointIndex) {
            return mPoints[yPosition(pointIndex)];
        }

        private int xPosition(int pointIndex) {
            return pointIndex*2;
        }

        private int yPosition(int pointIndex) {
            return xPosition(pointIndex) + 1;
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawCircle(mBounds.centerX(), mBounds.centerY(), mBounds.centerX(), mBackgroundPaint);

            canvas.save();
            canvas.rotate(180 * mRotation, (x(0) + x(1))/2, (y(0) + y(1))/2);
            canvas.drawLine(x(0), y(0), x(1), y(1), mLinePaint);
            canvas.restore();

            canvas.save();
            canvas.rotate(180 * mRotation, (x(2) + x(3)) / 2, (y(2) + y(3)) / 2);
            canvas.drawLine(x(2), y(2), x(3), y(3), mLinePaint);
            canvas.restore();
        }


        public void animateTick() {
            AnimatorSet set = new AnimatorSet();
            int cx;
            int cy;
            cx=animView.getWidth()/2;
            cy=0;
            int initialRadius =  Math.max(animView.getWidth(), animView.getHeight())*2;

            Animator anim;
            anim = ViewAnimationUtils.createCircularReveal(animView, cx, cy, 0, initialRadius);
            set.playTogether(
                    anim,
                    ObjectAnimator.ofFloat(this, mPropertyPointAX, mBounds.left),
                    ObjectAnimator.ofFloat(this, mPropertyPointAY, mBounds.centerY()),

                    ObjectAnimator.ofFloat(this, mPropertyPointBX, mBounds.centerX()),
                    ObjectAnimator.ofFloat(this, mPropertyPointBY, mBounds.bottom),

                    ObjectAnimator.ofFloat(this, mPropertyPointCX, mBounds.right),
                    ObjectAnimator.ofFloat(this, mPropertyPointCY, mBounds.centerX() / 2),

                    ObjectAnimator.ofFloat(this, mPropertyPointDX, mBounds.centerX()),
                    ObjectAnimator.ofFloat(this, mPropertyPointDY, mBounds.bottom),

                    ObjectAnimator.ofFloat(this, mRotationProperty, 0f, 1f),
                    ObjectAnimator.ofObject(this, mLineColorProperty, mArgbEvaluator, mPlusColor),
                    ObjectAnimator.ofObject(this, mBackgroundColorProperty, mArgbEvaluator, mTickColor)
            );

            set.setDuration(ANIMATION_DURATION);
            set.setInterpolator(ANIMATION_INTERPOLATOR);
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    var1.setText("");
                    var2.setText("");
                    var3.setText("");
                    var4.setText("");



                }
            });
            set.start();
        }


        public void animateCross() {
            AnimatorSet set = new AnimatorSet();
            int cx;
            int cy;
            cx=animView.getWidth()/2;
            cy=0;
            int initialRadius =  Math.max(animView.getWidth(), animView.getHeight())*2;

            Animator anim;
            anim = ViewAnimationUtils.createCircularReveal(animView, cx, cy, 0, initialRadius);
            set.playTogether(
                    anim,
                    ObjectAnimator.ofFloat(this, mPropertyPointAX,(mBounds.centerX())-mBounds.centerX()/2),
                    ObjectAnimator.ofFloat(this, mPropertyPointAY, (mBounds.centerY())-mBounds.centerY()/2),

                    ObjectAnimator.ofFloat(this, mPropertyPointBX,mBounds.centerX()+mBounds.centerX()/2),
                    ObjectAnimator.ofFloat(this, mPropertyPointBY, mBounds.centerY()+mBounds.centerY()/2),

                    ObjectAnimator.ofFloat(this, mPropertyPointCX, mBounds.centerX()+mBounds.centerX()/2),
                    ObjectAnimator.ofFloat(this, mPropertyPointCY, mBounds.centerY()-mBounds.centerY()/2),

                    ObjectAnimator.ofFloat(this, mPropertyPointDX, mBounds.centerX()-mBounds.centerX()/2),
                    ObjectAnimator.ofFloat(this, mPropertyPointDY, mBounds.centerY()+mBounds.centerY()/2),

                    ObjectAnimator.ofFloat(this, mRotationProperty, 0f, 1f),
                    ObjectAnimator.ofObject(this, mLineColorProperty, mArgbEvaluator, mCrossColor),
                    ObjectAnimator.ofObject(this, mBackgroundColorProperty, mArgbEvaluator, mTickColor)
            );
            set.setDuration(ANIMATION_DURATION);
            set.setInterpolator(ANIMATION_INTERPOLATOR);
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    var1.setText("");
                    var2.setText("");
                    var3.setText("");
                    var4.setText("");
                    /*image.setBackgroundResource(R.drawable.imageanim);
                    imageAnimation = (AnimationDrawable) image.getBackground();
                    image.setImageBitmap(null);
                    imageAnimation.run();
                    checkIfAnimationDone(imageAnimation);*/

                }
            });
            set.start();
        }


        public void animatePause() {
            AnimatorSet set = new AnimatorSet();
            int cx;
            int cy;
            cx=animView.getWidth()/2;
            cy=animView.getHeight();
            int initialRadius =  Math.max(animView.getWidth(), animView.getHeight())*2;

            Animator anim;
            anim = ViewAnimationUtils.createCircularReveal(animView, cx, cy, 0,initialRadius );
            set.playTogether(

                    ObjectAnimator.ofFloat(this, mPropertyPointAX, mBounds.centerX()-mBounds.centerX()/4),
                    ObjectAnimator.ofFloat(this, mPropertyPointAY, mBounds.top),

                    ObjectAnimator.ofFloat(this, mPropertyPointBX, mBounds.centerX()-mBounds.centerX()/4),
                    ObjectAnimator.ofFloat(this, mPropertyPointBY, mBounds.bottom),

                    ObjectAnimator.ofFloat(this, mPropertyPointCX, mBounds.centerX()+mBounds.centerX()/4),
                    ObjectAnimator.ofFloat(this, mPropertyPointCY, mBounds.top),

                    ObjectAnimator.ofFloat(this, mPropertyPointDX, mBounds.centerX()+mBounds.centerX()/4),
                    ObjectAnimator.ofFloat(this, mPropertyPointDY, mBounds.bottom),

                    ObjectAnimator.ofFloat(this, mRotationProperty, 0f, 1f),
                    anim,
                    ObjectAnimator.ofObject(this, mLineColorProperty, mArgbEvaluator, Color.parseColor("#3F51B5")),
                    ObjectAnimator.ofObject(this, mBackgroundColorProperty, mArgbEvaluator, mTickColor)
            );
            set.setDuration(ANIMATION_DURATION);
            //set.setStartDelay(500);
            set.setInterpolator(ANIMATION_INTERPOLATOR);
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    if(image!=null)
                    image.setBackground(null);
                }
            });
            set.start();
        }


        public void animatePlay() {
            AnimatorSet set = new AnimatorSet();
            int cx;
            int cy;
            cx=animView.getWidth()/2;
            cy=animView.getHeight();
            int initialRadius =  Math.max(animView.getWidth(), animView.getHeight())*2;

            Animator anim;
            anim = ViewAnimationUtils.createCircularReveal(animView, cx, cy, 0,initialRadius );
            set.playTogether(

                    anim,
                    ObjectAnimator.ofFloat(this, mPropertyPointAX, mBounds.centerX()-mBounds.centerX()/4),
                    ObjectAnimator.ofFloat(this, mPropertyPointAY, mBounds.top),

                    ObjectAnimator.ofFloat(this, mPropertyPointBX, mBounds.right),
                    ObjectAnimator.ofFloat(this, mPropertyPointBY, mBounds.centerX()),

                    ObjectAnimator.ofFloat(this, mPropertyPointCX, mBounds.right),
                    ObjectAnimator.ofFloat(this, mPropertyPointCY, mBounds.centerX()),

                    ObjectAnimator.ofFloat(this, mPropertyPointDX, mBounds.centerX()-mBounds.centerX()/4),
                    ObjectAnimator.ofFloat(this, mPropertyPointDY, mBounds.bottom),

                    ObjectAnimator.ofFloat(this, mRotationProperty, 0f, 1f),

                    ObjectAnimator.ofObject(this, mLineColorProperty, mArgbEvaluator,Color.parseColor("#ffd600") ),
                    ObjectAnimator.ofObject(this, mBackgroundColorProperty, mArgbEvaluator, mTickColor)
            );
            set.setDuration(ANIMATION_DURATION);
            set.setInterpolator(ANIMATION_INTERPOLATOR);
            set.start();
        }

        @Override
        public void setAlpha(int alpha) {}

        @Override
        public void setColorFilter(ColorFilter cf) {}

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

        private Property<TickPlusDrawable, Integer> mBackgroundColorProperty = new Property<TickPlusDrawable, Integer>(Integer.class, "bg_color") {
            @Override
            public Integer get(TickPlusDrawable object) {
                return object.mBackgroundPaint.getColor();
            }

            @Override
            public void set(TickPlusDrawable object, Integer value) {
                object.mBackgroundPaint.setColor(value);
            }
        };

        private Property<TickPlusDrawable, Integer> mLineColorProperty = new Property<TickPlusDrawable, Integer>(Integer.class, "line_color") {
            @Override
            public Integer get(TickPlusDrawable object) {
                return object.mLinePaint.getColor();
            }

            @Override
            public void set(TickPlusDrawable object, Integer value) {
                object.mLinePaint.setColor(value);
            }
        };

        private Property<TickPlusDrawable, Float> mRotationProperty = new Property<TickPlusDrawable, Float>(Float.class, "rotation") {
            @Override
            public Float get(TickPlusDrawable object) {
                return object.mRotation;
            }

            @Override
            public void set(TickPlusDrawable object, Float value) {
                object.mRotation = value;
            }
        };

        private PointProperty mPropertyPointAX = new XPointProperty(0);
        private PointProperty mPropertyPointAY = new YPointProperty(0);
        private PointProperty mPropertyPointBX = new XPointProperty(1);
        private PointProperty mPropertyPointBY = new YPointProperty(1);
        private PointProperty mPropertyPointCX = new XPointProperty(2);
        private PointProperty mPropertyPointCY = new YPointProperty(2);
        private PointProperty mPropertyPointDX = new XPointProperty(3);
        private PointProperty mPropertyPointDY = new YPointProperty(3);

        private abstract class PointProperty extends Property<TickPlusDrawable, Float> {

            protected int mPointIndex;

            private PointProperty(int pointIndex) {
                super(Float.class, "point_" + pointIndex);
                mPointIndex = pointIndex;
            }
        }

        private class XPointProperty extends PointProperty {

            private XPointProperty(int pointIndex) {
                super(pointIndex);
            }

            @Override
            public Float get(TickPlusDrawable object) {
                return object.x(mPointIndex);
            }

            @Override
            public void set(TickPlusDrawable object, Float value) {
                object.mPoints[object.xPosition(mPointIndex)] = value;
                invalidateSelf();
            }
        }

        private class YPointProperty extends PointProperty {

            private YPointProperty(int pointIndex) {
                super(pointIndex);
            }

            @Override
            public Float get(TickPlusDrawable object) {
                return object.y(mPointIndex);
            }

            @Override
            public void set(TickPlusDrawable object, Float value) {
                object.mPoints[object.yPosition(mPointIndex)] = value;
                invalidateSelf();
            }
        }
    }
}
