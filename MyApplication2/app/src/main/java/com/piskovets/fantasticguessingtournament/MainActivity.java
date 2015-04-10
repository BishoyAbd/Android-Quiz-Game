package com.piskovets.fantasticguessingtournament;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
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

    public enum ActivityStates{
        PAUSED,UNPAUSED,GAME_OVER,RETURN,STARTED

    }
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
    private View animView;
    private TextView timer_tv;
    private ImageView menuImage;


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
    private String locale;
    private ActivityStates activityStates;
    private boolean clicked;
    private TextView level;
    private TextView levelNumber;
    private int level_number;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        Intent i = getIntent();
        Log.d("Image",i.getStringExtra("Image"));
        clicked=false;
        activityStates=ActivityStates.STARTED;
        myView = findViewById(R.id.transitionElement2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getIntent().getIntExtra("Color",getResources().getColor(R.color.transition)));
        if(Build.VERSION.SDK_INT==Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(darkerColor(getIntent().getIntExtra("Color",getResources().getColor(R.color.transition))));
            getWindow().setNavigationBarColor(darkerColor(getIntent().getIntExtra("Color",getResources().getColor(R.color.transition))));
        }
        toolbar.setTitle("");
        level=(TextView)findViewById(R.id.level_tv);
        levelNumber=(TextView)findViewById(R.id.level_number_tv);
        level.setVisibility(View.INVISIBLE);
        levelNumber.setVisibility(View.INVISIBLE);
        level_number=0;
        levelNumber.setText(String.valueOf(level_number));
        setSupportActionBar(toolbar);

        menuImage=(ImageView)findViewById(R.id.category_image_view);

        menuImage.setImageResource(getResources().getIdentifier("@drawable/" + i.getStringExtra("Image"), "drawable", getApplicationContext().getPackageName()));
        TextView menuTitle=(TextView) findViewById(R.id.menu_title);
        menuImage.setTag(i.getStringExtra("Image"));

        animView = findViewById(R.id.animView);
        animView.setEnabled(false);
        tickPlusDrawable = new TickPlusDrawable(getResources().getDimensionPixelSize(R.dimen.stroke_width),getResources().getColor(R.color.background),Color.parseColor("#FF5722"),Color.parseColor("#4CAF50"));
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.JELLY_BEAN){
            animView.setBackgroundDrawable(tickPlusDrawable);
        }else {
            animView.setBackground(tickPlusDrawable);
        }

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        image =(ImageView)findViewById(R.id.view);
        var1 = (Button) findViewById(R.id.activity_my_variant1_btn);
        var2 = (Button) findViewById(R.id.activity_my_variant2_btn);
        var3 = (Button) findViewById(R.id.activity_my_variant3_btn);
        var4 = (Button) findViewById(R.id.activity_my_variant4_btn);
        var1.setEnabled(false);
        var2.setEnabled(false);
        var3.setEnabled(false);
        var4.setEnabled(false);
        image.setEnabled(false);
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
        timer_tv.setTextColor(Color.BLACK);

        android.app.FragmentManager fm = getFragmentManager();
        pauseFragment=fm.findFragmentById(R.id.fragment);
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.hide(pauseFragment);
        ft.commit();

        FragmentManager fragmentManager=getFragmentManager();
        revealFragment1=(RevealFragment)fragmentManager.findFragmentById(R.id.fragment3);
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        revealFragment1.setColor(getIntent().getIntExtra("Color",getResources().getColor(R.color.transition)));
        revealFragment1.setProgressColor();
        transaction.show(revealFragment1);
        transaction.commit();

        if(Build.VERSION.SDK_INT==Build.VERSION_CODES.LOLLIPOP) {
            Bitmap icon = ((BitmapDrawable) getDrawable(R.drawable.ic_launcher)).getBitmap();
            ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(getString(R.string.app_name), icon, darkerColor(getIntent().getIntExtra("Color", getResources().getColor(R.color.transition))));
            this.setTaskDescription(taskDescription);
        }

        locale = getResources().getConfiguration().locale.getDisplayLanguage();
        menuDBoperation=new MenuItemOperations(this);
        menuDBoperation.open();
        strFile =menuDBoperation.getDefaultTableName(locale, i.getStringExtra("File"));
        menuTitle.setText(i.getStringExtra("File"));
        str_File=strFile.replaceAll(" ","_");
        strList=menuDBoperation.getAllVariants(locale,str_File);
        points = 0;
        activity = this;

        for(int j = 0; j < strList.size(); j++) randList.add(j);
        new getImageTask().execute();
    }

    public int darkerColor(int color){
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f; // value component
        color = Color.HSVToColor(hsv);
        return color;
    }

    public void addTime() {
        long addedTime;
        if(points>0) {
            addedTime = (long) (10000/Math.log10(points*10));
        }
        else{
            addedTime=5000;
        }
        remainingTime=remainingTime + addedTime;
        if(remainingTime>45000) remainingTime=45000;
        showTimer(remainingTime);
    }

    public void decreaseTime() {
        long decTime;
        if(points>0) {
            decTime = (long) (Math.log10(points*10)*1500);
        }
        else{
            decTime=3000;
        }
        remainingTime=remainingTime - decTime;
        if(remainingTime<0) remainingTime=0;
        showTimer(remainingTime);
    }

    public void onGameFinish(){
        Log.d("Timer", "Timer Finished");
        var1.setEnabled(false);
        var2.setEnabled(false);
        var3.setEnabled(false);
        var4.setEnabled(false);
        image.setEnabled(false);
        activityStates = ActivityStates.GAME_OVER;
        Glide.clear(image);
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.JELLY_BEAN) {
            image.setBackgroundDrawable(null);
        }
        else{
            image.setBackground(null);}
        image.setImageBitmap(null);
        var1.setText("");
        var2.setText("");
        var3.setText("");
        var4.setText("");

        int highscore = Integer.parseInt(sharedpreferences.getString(strFile, "0"));
        myData.clear();

        final Intent intent = new Intent(activity, ChildActivity.class);
        intent.putExtra("ChildActivity:points", String.valueOf(points));
        intent.putExtra("Highscore", highscore);
        intent.putExtra("Theme", strFile);
        intent.putExtra("Name", getIntent().getStringExtra("File"));
        intent.putExtra("Image", String.valueOf(menuImage.getTag()));
        intent.putExtra("Color", getIntent().getIntExtra("Color", getResources().getColor(R.color.transition)));
        /*if(Build.VERSION.SDK_INT==Build.VERSION_CODES.LOLLIPOP) {
            final ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, findViewById(R.id.activity_my_points_tv), findViewById(R.id.activity_my_points_tv).getTransitionName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                startActivity(intent, options.toBundle());
                activity.finishAfterTransition();
            } catch (IllegalArgumentException e) {
                Log.d("Error", "Illegal Argument");
            }
        }else{*/
        //final ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, findViewById(R.id.activity_my_points_tv), ViewCompat.getTransitionName(findViewById(R.id.activity_my_points_tv)));
        //try {
        //  ActivityCompat.startActivity(activity, intent, options.toBundle());
        startActivity(intent);
        overridePendingTransition(0,0);
        activity.finish();

        //ActivityCompat.finishAfterTransition(activity);
        //} catch (IllegalArgumentException e) {
        Log.d("Error", "Illegal Argument");
        //}
    }


    public void showTimer(final long millisecondsToCountDown) {

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(millisecondsToCountDown, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;
                progress.setProgress((int) millisUntilFinished / 1000);
                timer_tv.setText(String.valueOf((int) millisUntilFinished / 1000));
                if (remainingTime < 31000) {
                    if (remainingTime < 16000) {
                        progress.setProgressColor(Color.parseColor("#FF5722"));
                    } else {
                        progress.setProgressColor(Color.parseColor("#FFd600"));
                    }
                } else {
                    progress.setProgressColor(Color.parseColor("#4CAF50"));
                }
                if (remainingTime < 1000) {
                    image.setEnabled(false);
                    var1.setEnabled(false);
                    var2.setEnabled(false);
                    var3.setEnabled(false);
                    var4.setEnabled(false);

                }
            }


            @Override
            public void onFinish() {
                if (!clicked) {
                    Log.d("Not Animation", String.valueOf(tickPlusDrawable.isCross));
                    onGameFinish();
                }
            }

        }.start();
    }

    public void buttonClick(View v) {
        //decreaseTime();
        var1.setText("");
        var2.setText("");
        var3.setText("");
        var4.setText("");
        var1.setEnabled(false);
        var2.setEnabled(false);
        var3.setEnabled(false);
        var4.setEnabled(false);
        image.setEnabled(false);

        int changePoints=DecreasePoints();
        AnimateText(pnts,points,points+changePoints);
        try{
            myData.remove(0);
        }catch(IndexOutOfBoundsException e){
            //new showImageTask().execute();
        }
        if(remainingTime>=1000) {
            level_number++;
            level.setVisibility(View.VISIBLE);
            levelNumber.setText(String.valueOf(level_number));
            levelNumber.setVisibility(View.VISIBLE);
            if (myData.size() > 1) {
                Log.d("Animation", "size is more than 1");
                image.setBackgroundResource(R.drawable.image_anim);
                imageAnimation = (AnimationDrawable) image.getBackground();
                image.setImageBitmap(null);
                imageAnimation.run();
                progress.setProgress((int) remainingTime / 1000);
                timer_tv.setText(String.valueOf((int) remainingTime / 1000));
                animView.setEnabled(false);
                checkIfAnimationDone(imageAnimation);
            } else {
                progress.setProgress((int) remainingTime / 1000);
                timer_tv.setText(String.valueOf((int) remainingTime / 1000));
                countDownTimer.cancel();
                Log.d("Animation", "size is less than 1");
                image.setBackgroundResource(R.drawable.load_anim);
                imageAnimation = (AnimationDrawable) image.getBackground();
                animView.setEnabled(false);
                image.setImageBitmap(null);
                imageAnimation.start();
                imageIsLoaded = false;
            }

            if (isOverLimit) {
                isOverLimit = false;
                new showImageTask().execute();
            }
        }else{Log.d("Clicked","Pause should be false by now");
            tickPlusDrawable.isPause=false;
        }


    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("Pause", activityStates.toString());
        switch(activityStates){
            case UNPAUSED:
                if(!tickPlusDrawable.isPause() && !tickPlusDrawable.isPlay()){
                    imageAnimation.stop();
                    //imageAnimation=null;
                    Log.d("Pause","Animation Stopped");
                }
                if(!revealFragment1.isShapeVisible()){
                    onPauseClick(pauseFragment.getView());
                }
                Log.d("Pause","On Pause Click");
                activityStates=ActivityStates.PAUSED;
                break;
            case STARTED:
                if(!tickPlusDrawable.isPause() && !tickPlusDrawable.isPlay()){
                    imageAnimation.stop();
                    Log.d("Pause","Animation Stopped");
                }
                if(!revealFragment1.isShapeVisible()){
                    onPauseClick(pauseFragment.getView());
                }
                Log.d("Pause", "On Pause Click");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(imageAnimation!=null ) {
            imageAnimation=null;
            Log.d("Pause","Animation Resumed");
            animView.setEnabled(true);
            var1.setText("");
            var2.setText("");
            var3.setText("");
            var4.setText("");
        }
        Log.d("Resume",activityStates.toString());
        switch(activityStates){
            case STARTED:
                break;
            case PAUSED:
                //new showImageTask().execute();
                break;
        }
    }

    public void onPauseClick(View v){
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        if(getResources().getBoolean(R.bool.portrait_only)){
            ft.setCustomAnimations(R.anim.slide_in_top,
                    R.anim.slide_out_top);}
        else{
            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
        }
        if (pauseFragment.isHidden()) {
            ft.show(pauseFragment);
            activityStates=ActivityStates.PAUSED;
            var1.setText("");
            var2.setText("");
            var3.setText("");
            var4.setText("");
            tickPlusDrawable.animatePlay();
            if(countDownTimer!=null)
                countDownTimer.cancel();
            var1.setEnabled(false);
            var2.setEnabled(false);
            Log.d("Pause","is Shown");
            var3.setEnabled(false);
            var4.setEnabled(false);
            image.setEnabled(false);
        } else {
            activityStates=ActivityStates.UNPAUSED;
            loadImage();
            ft.hide(pauseFragment);
            Log.d("Pause","is Hidden");
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
        addTime();
        if (points < 100) {
            return 10;
        } else {
            if (points < 500) {
                return 25;
            } else {
                if (points < 2000) {
                    return 50;
                }else{
                    return 100;
                }
            }
        }
    }

    public int DecreasePoints() {
        decreaseTime();
        if(points<=0){
            return 0;
        }
        if (points < 100) {
            return -5;
        } else {
            if (points < 500) {
                return -15;
            } else {
                if (points < 2000) {
                    return -35;
                }else{
                    return -75;
                }
            }
        }
    }

    public void AnswerClick(View v) {
        clicked=true;
        Button btn=(Button) v;
        String answ;
        var1.setEnabled(false);
        var2.setEnabled(false);
        var3.setEnabled(false);
        var4.setEnabled(false);
        image.setEnabled(false);
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

        try{
            myData.remove(0);
        }catch(IndexOutOfBoundsException e){
            new showImageTask().execute();
        }
        if(remainingTime>=1000) {
            level_number++;
            level.setVisibility(View.VISIBLE);
            levelNumber.setText(String.valueOf(level_number));
            levelNumber.setVisibility(View.VISIBLE);
            if (myData.size() > 1) {
                Log.d("Animation", "size is more than 1");
                image.setBackgroundResource(R.drawable.image_anim);
                imageAnimation = (AnimationDrawable) image.getBackground();
                image.setImageBitmap(null);
                imageAnimation.run();
                //countDownTimer.cancel();
                progress.setProgress((int) remainingTime / 1000);
                timer_tv.setText(String.valueOf((int) remainingTime / 1000));
                animView.setEnabled(false);
                checkIfAnimationDone(imageAnimation);
            } else {
                progress.setProgress((int) remainingTime / 1000);
                timer_tv.setText(String.valueOf((int) remainingTime / 1000));
                countDownTimer.cancel();
                Log.d("Animation", "size is less than 1");
                image.setBackgroundResource(R.drawable.load_anim);
                imageAnimation = (AnimationDrawable) image.getBackground();
                animView.setEnabled(false);
                image.setImageBitmap(null);
                imageAnimation.start();
                imageIsLoaded = false;
            }

            if (isOverLimit) {
                isOverLimit = false;
                new showImageTask().execute();
            }
        }else{Log.d("Clicked","Pause should be false by now");
            tickPlusDrawable.isPause=false;
        }
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
                points = end;

            }
        });
        if(remainingTime<1000){
            animator.setDuration(150);
        }else{
            animator.setDuration(500);
        }
        animator.start();
    }

    private void checkIfAnimationDone(AnimationDrawable anim){
        final AnimationDrawable a = anim;
        int timeBetweenChecks = 200;
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                if (a.getCurrent() != a.getFrame(a.getNumberOfFrames() - 1)) {
                    checkIfAnimationDone(a);
                } else {
                    animView.setEnabled(true);
                    showTimer(remainingTime);
                    imageAnimation=null;
                    loadImage();
                }

            }
        }, timeBetweenChecks);
    }

    private void checkIfEndAnimationDone(AnimationDrawable anim){
        final AnimationDrawable a = anim;
        int timeBetweenChecks = 200;
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                if (a.getCurrent() != a.getFrame(a.getNumberOfFrames() - 1)) {
                    checkIfEndAnimationDone(a);
                } else {
                    animView.setEnabled(true);
                    showTimer(remainingTime);
                    imageAnimation=null;
                    loadImage();
                }

            }
        }, timeBetweenChecks);
    }

    private void checkIfStartingAnimationDone(AnimationDrawable anim){
        final AnimationDrawable a = anim;
        int timeBetweenChecks = 200;
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                if (a.getCurrent() != a.getFrame(a.getNumberOfFrames() - 1)) {
                    checkIfStartingAnimationDone(a);
                } else {
                    animView.setEnabled(true);
                    try {
                        long startTime = 46 * 1000;
                        showTimer(startTime);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    imageAnimation.stop();
                    imageAnimation=null;
                    loadImage();
                }

            }
        }, timeBetweenChecks);
    }

    public void loadImage(){
        try {
            if (imageAnimation != null ) {
                //image.clearAnimation();
                //imageAnimation.stop();
                image.setBackgroundResource(R.drawable.image_anim);
                imageAnimation = (AnimationDrawable) image.getBackground();
                image.setImageBitmap(null);
                imageAnimation.run();
                Log.d("End Animation","Added");
                //animView.setEnabled(true);

                //if (countDownTimer != null && activityStates==ActivityStates.UNPAUSED) {
                //  showTimer(remainingTime);
                //}
                checkIfEndAnimationDone(imageAnimation);
            }else {
                try {
                    if(activityStates==ActivityStates.UNPAUSED && !tickPlusDrawable.isPause()) {
                        tickPlusDrawable.animatePause();
                        Log.d("Animating Pause","true");
                    }
                }catch(IllegalStateException e){
                    Log.d("Error","Cannot start Animator");
                }
                level.setVisibility(View.INVISIBLE);
                levelNumber.setVisibility(View.INVISIBLE);
                clicked = false;
                if(Build.VERSION.SDK_INT<Build.VERSION_CODES.JELLY_BEAN) {
                    image.setBackgroundDrawable(null);
                }
                else{
                    image.setBackground(null);}
                image.setImageBitmap(myData.get(0).getImage());
                var1.setText(String.valueOf(myData.get(0).getVar1()));
                var2.setText(String.valueOf(myData.get(0).getVar2()));
                var3.setText(String.valueOf(myData.get(0).getVar3()));
                var4.setText(String.valueOf(myData.get(0).getVar4()));
                Log.d("Variant", var1.getText().toString());
                var1.setEnabled(true);
                var2.setEnabled(true);
                var3.setEnabled(true);
                var4.setEnabled(true);
                image.setEnabled(true);
            }
        }catch(IndexOutOfBoundsException e){
            Log.d("Error", "Out of Bounds");
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
        return dir.delete();
    }

    public void loadingAnimation(final View myView){
        myView.postDelayed(new Runnable() {
            @Override
            public void run() {
                revealFragment1.hideProgressBar();
                revealFragment1.hideShape(myView.getWidth() / 2, myView.getHeight() / 2);
            }
        }, 10);

    }

    public void showFragment(){
        revealFragment1.changeText(getIntent().getStringExtra("File"));
    }

    public class getImageTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showFragment();
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
            randArray[i] = randList.get(i);
            strVariantArray[i]=strList.get(randArray[i]);
        }
        int randInt = new Random().nextInt(randArray.length);
        String strSearchText = strVariantArray[randInt];
        Log.d("Next Image","Search string => "+ strSearchText);
        getImageFromDB(str_File, strSearchText, strVariantArray);
    }
    public void getImageFromDB(String file, final String name, final String[] variant){
        String returnImage= menuDBoperation.getUrls(locale,file, name);
        String[] returnArray=returnImage.split(",");
        int randInt = new Random().nextInt(returnArray.length);
        final String mImage_url=returnArray[randInt];
        activity.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    Glide.with(activity).load(mImage_url).asBitmap().into(new SimpleTarget<Bitmap>(500, 500) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                            try {
                                if(myData.size()<20) {
                                    myData.add(new ImageData(name, variant[0], variant[1], variant[2], variant[3], bitmap));
                                    Log.d("Ok", "Image Added; Array Size: " + myData.size());
                                    if (activityStates == ActivityStates.STARTED) {
                                        loadingAnimation(myView);
                                        //gameStart = true;
                                        activityStates = ActivityStates.UNPAUSED;

                                        animView.setEnabled(true);
                                        //loadImage();
                                        level_number++;
                                        level.setVisibility(View.VISIBLE);
                                        levelNumber.setText(String.valueOf(level_number));
                                        levelNumber.setVisibility(View.VISIBLE);


                                        image.setBackgroundResource(R.drawable.image_anim);
                                        imageAnimation = (AnimationDrawable) image.getBackground();
                                        image.setImageBitmap(null);
                                        imageAnimation.run();

                                        animView.setEnabled(false);
                                        checkIfStartingAnimationDone(imageAnimation);


                                    }
                                    if (!imageIsLoaded) {
                                        imageIsLoaded = true;
                                        loadImage();
                                    }
                                    if (myData.size() < 20) {
                                        NextImage();
                                    } else {
                                        isOverLimit = true;
                                    }
                                }

                            } catch (ArrayIndexOutOfBoundsException e) {
                                Log.d("Error", "Out of Bounds");
                                NextImage();
                            }
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            try {
                                Log.d("Error", "Image Load Error; Array Size: " + myData.size());
                                NextImage();
                            } catch (ArrayIndexOutOfBoundsException ex) {
                                Log.d("Error", "Out of Bounds");
                                NextImage();
                            }

                        }
                    });
                }catch(IllegalArgumentException e){
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        myData.clear();
        switch(activityStates){
            case STARTED:
                if(imageAnimation!=null && imageAnimation.isRunning()){
                    imageAnimation.stop();
                }
                Log.d("OnBackPressed","Animation Stopped "+ activityStates);
                break;
            case PAUSED:
                break;
            default:
                if(imageAnimation!=null && imageAnimation.isRunning()){
                    imageAnimation.stop();
                }else{
                    countDownTimer.cancel();}
                Log.d("OnBackPressed","Timer Canceled");
                break;
        }
        //if(countDownTimer!=null){

        //}
        activityStates=ActivityStates.RETURN;
        Intent intent = new Intent(MainActivity.this, HListViewTest.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(0,0);
        activity.finish();
    }

    public class TickPlusDrawable extends Drawable {

        private static final long ANIMATION_DURATION = 200;
        private final Interpolator ANIMATION_INTERPOLATOR = new DecelerateInterpolator();

        private Paint mLinePaint;
        private Paint mBackgroundPaint;

        private boolean isTick=false;
        private boolean isCross=false;
        private boolean isPause=true;
        private boolean isPlay=false;

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
            isTick=true;
            isCross=false;
            isPause=false;
            isPlay=false;
            set.playTogether(
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
                    if(remainingTime<1000){
                        onGameFinish();
                    }else{
                        var1.setText("");
                        var2.setText("");
                        var3.setText("");
                        var4.setText("");
                    }
                }
            });
            set.start();
        }


        public void animateCross() {
            AnimatorSet set = new AnimatorSet();
            isTick=false;
            isCross=true;
            isPause=false;
            isPlay=false;
            set.playTogether(
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
                    if(remainingTime<1000){
                        Log.d("Animation","Cross");
                        onGameFinish();
                    }else
                    {
                        var1.setText("");
                        var2.setText("");
                        var3.setText("");
                        var4.setText("");
                    }
                }
            });
            set.start();
        }


        public void animatePause() {
            AnimatorSet set = new AnimatorSet();
            isTick=false;
            isCross=false;
            isPause=true;
            isPlay=false;
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
                    ObjectAnimator.ofObject(this, mLineColorProperty, mArgbEvaluator, Color.parseColor("#3F51B5")),
                    ObjectAnimator.ofObject(this, mBackgroundColorProperty, mArgbEvaluator, mTickColor)
            );
            set.setDuration(ANIMATION_DURATION);
            set.setInterpolator(ANIMATION_INTERPOLATOR);
            set.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    if(image!=null)
                        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.JELLY_BEAN) {
                            image.setBackgroundDrawable(null);
                        }
                        else{
                            image.setBackground(null);}
                }
            });
            set.start();
        }


        public boolean isPause() {
            return isPause;
        }

        public boolean isPlay() {
            return isPlay;
        }

        public void animatePlay() {
            AnimatorSet set = new AnimatorSet();
            isTick=false;
            isCross=false;
            isPause=false;
            isPlay=true;
            set.playTogether(

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

        //public boolean isTick() {
           // return isTick;
        //}



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
