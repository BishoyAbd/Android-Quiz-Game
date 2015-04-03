package com.piskovets.fantasticguessingtournament;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Fragment;
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
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.piskovets.fantasticguessingtournament.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;


public class MainFragment extends Fragment {

    Activity fa;
    RelativeLayout rellayout;
    public enum ActivityStates{
        PAUSED,UNPAUSED,GAME_OVER,RETURN,STARTED

    }
    private List<Integer> randList=new ArrayList<>();
    public List<ImageData> myData=new ArrayList<>();
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
    public  CountDownTimer countDownTimer;
    private long remainingTime;
    private int points;
    private TickPlusDrawable tickPlusDrawable;
    private boolean gameStart=false;
    private View animView;
    private TextView timer_tv;
    private ImageView menuImage;


    private ImageView image;
    private static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedpreferences;
    private Fragment pauseFragment;
    private AnimationDrawable imageAnimation;
    private boolean isOverLimit=false;
    private RevealFragment revealFragment2;
    private View myView;
    private boolean imageIsLoaded=true;
    private MenuItemOperations menuDBoperation;
    private String locale;
    public ActivityStates activityStates;
    private boolean clicked;
    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fa= super.getActivity();
        //setExitTransition(new Explode());
        rellayout= (RelativeLayout) inflater.inflate(R.layout.fragment_main, container, false);
        Initialize();
        return rellayout;
    }

    public void Initialize(){
        if(getResources().getBoolean(R.bool.portrait_only)){
            fa.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            fa.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        //setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.explode));
        Intent i = fa.getIntent();
        Log.d("Image", i.getStringExtra("Image"));
        clicked=false;
        activityStates= ActivityStates.STARTED;
        myView = rellayout.findViewById(R.id.transitionElement2);
        Toolbar toolbar = (Toolbar) rellayout.findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(fa.getIntent().getIntExtra("Color",getResources().getColor(R.color.transition)));
        //fa.getWindow().setStatusBarColor(darkerColor(fa.getIntent().getIntExtra("Color",getResources().getColor(R.color.transition))));
        //fa.getWindow().setNavigationBarColor(darkerColor(fa.getIntent().getIntExtra("Color",getResources().getColor(R.color.transition))));
        toolbar.setTitle("");

        //fa.setSupportActionBar(toolbar);

        menuImage=(ImageView)rellayout.findViewById(R.id.category_image_view);

        menuImage.setImageResource(getResources().getIdentifier("@drawable/" + i.getStringExtra("Image"), "drawable", fa.getApplicationContext().getPackageName()));
        TextView menuTitle=(TextView) rellayout.findViewById(R.id.menu_title);
        menuImage.setTag(i.getStringExtra("Image"));



        animView = rellayout.findViewById(R.id.animView);
        animView.setEnabled(false);
        View backgroundView = rellayout.findViewById(R.id.animView1);
        tickPlusDrawable = new TickPlusDrawable(getResources().getDimensionPixelSize(R.dimen.stroke_width),getResources().getColor(R.color.background), Color.parseColor("#FF5722"),Color.parseColor("#4CAF50"));
        animView.setBackground(tickPlusDrawable);
        backgroundView.setBackground(new TickBackgroundDrawable(getResources().getColor(R.color.background)));

        sharedpreferences = fa.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        image =(ImageView)rellayout.findViewById(R.id.view);
        var1 = (Button) rellayout.findViewById(R.id.activity_my_variant1_btn);
        var2 = (Button) rellayout.findViewById(R.id.activity_my_variant2_btn);
        var3 = (Button) rellayout.findViewById(R.id.activity_my_variant3_btn);
        var4 = (Button) rellayout.findViewById(R.id.activity_my_variant4_btn);
        var1.setEnabled(false);
        var2.setEnabled(false);
        var3.setEnabled(false);
        var4.setEnabled(false);
        image.setEnabled(false);
        var1.setText("");
        var2.setText("");
        var3.setText("");
        var4.setText("");
        pnts = (TextView) rellayout.findViewById(R.id.activity_my_points_tv);

        progress = (RoundCornerProgressBar) rellayout.findViewById(R.id.progressBar);

        progress.setMinimumHeight(80);
        progress.setMax(45);
        progress.setProgress(45);
        progress.setRotation(180);
        progress.setBackgroundColor(getResources().getColor(R.color.background));
        progress.setProgressColor(Color.parseColor("#4CAF50"));

        timer_tv=(TextView)rellayout.findViewById(R.id.timer_tv);
        timer_tv.setTextColor(Color.BLACK);

        FragmentManager fm = getChildFragmentManager();
        pauseFragment=fm.findFragmentById(R.id.fragment);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.hide(pauseFragment);
        ft.commit();

        FragmentManager fragmentManager=getChildFragmentManager();
        revealFragment2 =(RevealFragment)fragmentManager.findFragmentById(R.id.fragment3);
        FragmentTransaction transaction=getChildFragmentManager().beginTransaction();
        transaction.show(revealFragment2);
        transaction.commit();


        //Bitmap icon=((BitmapDrawable) fa.getDrawable(R.drawable.ic_launcher)).getBitmap();
        //ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(getString(R.string.app_name),icon,darkerColor(fa.getIntent().getIntExtra("Color", getResources().getColor(R.color.transition))));
        //fa.setTaskDescription(taskDescription);

        locale = getResources().getConfiguration().locale.getDisplayLanguage();
        menuDBoperation=new MenuItemOperations(fa);
        menuDBoperation.open();
        strFile =menuDBoperation.getDefaultTableName(locale, i.getStringExtra("File"));
        menuTitle.setText(i.getStringExtra("File"));
        str_File=strFile.replaceAll(" ","_");
        strList=menuDBoperation.getAllVariants(locale,str_File);
        points = 0;
        activity = fa;

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
            addedTime = (long) (10000/Math.log10(points));
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
            decTime = (long) (Math.log10(points)*1500);
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
        image.setBackground(null);
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
        intent.putExtra("Name", fa.getIntent().getStringExtra("File"));
        intent.putExtra("Image", String.valueOf(menuImage.getTag()));
        intent.putExtra("Color", fa.getIntent().getIntExtra("Color", getResources().getColor(R.color.transition)));

        /*ChildFragment childFragment=new ChildFragment();
        childFragment.setColor(fa.getIntent().getIntExtra("Color", getResources().getColor(R.color.transition)));
        childFragment.setHigh_score(highscore);
        childFragment.setTheme(strFile);
        childFragment.setImage(String.valueOf(menuImage.getTag()));
        childFragment.setName(fa.getIntent().getStringExtra("File"));
        childFragment.setPoints(String.valueOf(points));
        childFragment.setEnterTransition(TransitionUtils.makeEnterTransition());
        childFragment.setSharedElementEnterTransition(TransitionUtils.makeSharedElementEnterTransition());
        childFragment.setEnterSharedElementCallback(new EnterSharedElementCallback(getActivity()));*/

        //childFragment.setSharedElementEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.trans_move));
        //childFragment.setEnterTransition(TransitionUtils.makeEnterTransition());

        /*FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.replace(android.R.id.content, childFragment);
        trans.addSharedElement(rellayout.findViewById(R.id.activity_my_points_tv), rellayout.findViewById(R.id.activity_my_points_tv).getTransitionName());
        trans.addToBackStack(null);
        trans.commit();*/

        //final ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, rellayout.findViewById(R.id.activity_my_points_tv), rellayout.findViewById(R.id.activity_my_points_tv).getTransitionName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            //ActivityCompat.startActivity(activity, intent, options.toBundle());
            //ActivityCompat.finishAfterTransition(activity);
        } catch (IllegalArgumentException e) {
            Log.d("Error", "Illegal Argument");
        }
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
        decreaseTime();

    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d("Pause",activityStates.toString());
        switch(activityStates){
            case UNPAUSED:
                if(!tickPlusDrawable.isPause() && !tickPlusDrawable.isPlay()){
                    imageAnimation.stop();
                    Log.d("Pause","Animation Stopped");
                }
                if(!revealFragment2.isShapeVisible()){
                    onPauseClick(pauseFragment.getView());
                }
                Log.d("Pause", "On Pause Click");
                activityStates=ActivityStates.PAUSED;
                break;
            case GAME_OVER:
                break;
            case RETURN:
                break;
            case STARTED:
                if(!tickPlusDrawable.isPause() && !tickPlusDrawable.isPlay()){
                    imageAnimation.stop();
                    Log.d("Pause","Animation Stopped");
                }
                if(!revealFragment2.isShapeVisible()){
                    onPauseClick(pauseFragment.getView());
                }
                Log.d("Pause","On Pause Click");
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(imageAnimation!=null ) {
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
                new showImageTask().execute();
                break;
        }
    }

    public void onPauseClick(View v){
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_top,
                R.anim.slide_out_top);
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
        decreaseTime();
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
            if (myData.size() > 1) {
                Log.d("Animation", "size is more than 1");
                image.setBackgroundResource(R.drawable.imageanim);
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
                image.setBackgroundResource(R.drawable.loadanim);
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
                    loadImage();
                }

            }
        }, timeBetweenChecks);
    }


    public void loadImage(){
        clicked=false;
        try {
            if(pauseFragment.isHidden()) {
                tickPlusDrawable.animatePause();
                Log.d("Animating Pause","true");
            }
        }catch(IllegalStateException e){
            Log.d("Error","Cannot start Animator");
        }
        try {
            if (imageAnimation != null && imageAnimation.isRunning()) {
                animView.setEnabled(true);
                imageAnimation.stop();
                if (countDownTimer != null  && pauseFragment.isHidden()) {
                    showTimer(remainingTime);
                }
            }

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
        }catch(IndexOutOfBoundsException e){
            Log.d("Error", "Out of Bounds");
            new showImageTask().execute();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            trimCache(fa.getApplicationContext()); //if trimCache is static
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
                revealFragment2.hideProgressBar();
                revealFragment2.hideShape(myView.getWidth() / 2, myView.getHeight() / 2);
            }
        }, 10);

    }

    public void showFragment(){
        revealFragment2.setColor(fa.getIntent().getIntExtra("Color", getResources().getColor(R.color.transition)));
        revealFragment2.setProgressColor();
        revealFragment2.changeText(fa.getIntent().getStringExtra("File"));
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
        getImageFromDB(str_File, strSearchText,strVariantArray);
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
                                myData.add(new ImageData(name, variant[0], variant[1], variant[2], variant[3],bitmap));
                                Log.d("Ok", "Image Added; Array Size: " + myData.size());
                                if (!gameStart) {
                                    loadingAnimation(myView);
                                    gameStart = true;
                                    try {
                                        long startTime = 46 * 1000;
                                        showTimer(startTime);
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                    animView.setEnabled(true);
                                    loadImage();
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

                            } catch (ArrayIndexOutOfBoundsException e) {
                                Log.d("Error", "Out of Bounds");
                                NextImage();
                            }
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            try {
                                myData.remove(myData.size() - 1);
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


    /*public void onBackPressed() {

        activityStates=ActivityStates.RETURN;
        myData.clear();
        if(countDownTimer!=null)
            countDownTimer.cancel();
        Intent intent = new Intent(MainActivity.this, HListViewTest.class);
        startActivity(intent);
        activity.finish();
    }*/

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
            int cx;
            int cy;
            cx=animView.getWidth()/2;
            cy=0;
            int initialRadius =  Math.max(animView.getWidth(), animView.getHeight())*2;
            isTick=true;
            isCross=false;
            isPause=false;
            isPlay=false;
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
            int cx;
            int cy;
            cx=animView.getWidth()/2;
            cy=0;
            isTick=false;
            isCross=true;
            isPause=false;
            isPlay=false;
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
            int cx;
            int cy;
            cx=animView.getWidth()/2;
            cy=animView.getHeight();
            isTick=false;
            isCross=false;
            isPause=true;
            isPlay=false;
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


        public boolean isPause() {
            return isPause;
        }

        public boolean isPlay() {
            return isPlay;
        }

        public void animatePlay() {
            AnimatorSet set = new AnimatorSet();
            int cx;
            int cy;
            cx=animView.getWidth()/2;
            cy=animView.getHeight();
            isTick=false;
            isCross=false;
            isPause=false;
            isPlay=true;
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

        public boolean isTick() {
            return isTick;
        }



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

