package com.example.orodr_000.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.Property;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;


public class ButtonActivity extends FragmentActivity {
    int[] randArray;
    List<Integer> randList=new ArrayList<>();
    String[] strTextArray = {""};
    private Activity activity;

    private Button var1;
    private Button var2;
    private Button var3;
    private Button var4;
    private TextView pnts;
    private ProgressBar progress;
    private String strSearchText;
    private String strAnswer;
    private String strTheme;
    private String strFile;
    private CountDownTimer countDownTimer;
    private long remainingTime;
    private boolean timerHasStarted = false;
    private int points;
    private TickPlusDrawable tickPlusDrawable;
    private boolean gameStart=false;
    private View animView;
    private Chronometer stopWatch;
    private int es=0;
    private ImageView image;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    private Fragment pauseFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();


        setContentView(R.layout.activity_button);

        animView = findViewById(R.id.animView);
        View backgroundView = findViewById(R.id.animView1);
        tickPlusDrawable = new TickPlusDrawable(getResources().getDimensionPixelSize(R.dimen.stroke_width),  Color.parseColor("#37474F"),Color.parseColor("#FF5722"),Color.parseColor("#4CAF50"));
        animView.setBackground(tickPlusDrawable);
        backgroundView.setBackground(new TickBackgroundDrawable(Color.parseColor("#37474F")));

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //stopWatch=(Chronometer)findViewById(R.id.chronometer1);
        /*stopWatch.setBase(SystemClock.elapsedRealtime());
        stopWatch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){

                                                   @Override
                                                   public void onChronometerTick(Chronometer chronometer) {
                                                       // TODO Auto-generated method stub
                                                       es++;
                                                       if(es>4){
                                                           Log.d("timer","image reloaded "+es);
                                                           es=0;
                                                           stopWatch.stop();
                                                           Glide.clear(image);
                                                           //showImage(imageArray);
                                                           NextImage();
                                                           new getImageTask().execute();

                                                       }

                                                   }}
        );*/

        var1 = (Button) findViewById(R.id.activity_my_variant1_btn);
        var2 = (Button) findViewById(R.id.activity_my_variant2_btn);
        var3 = (Button) findViewById(R.id.activity_my_variant3_btn);
        var4 = (Button) findViewById(R.id.activity_my_variant4_btn);
        var1.setEnabled(false);
        var2.setEnabled(false);
        var3.setEnabled(false);
        var4.setEnabled(false);

        pnts = (TextView) findViewById(R.id.activity_my_points_tv);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        FragmentManager fm = getFragmentManager();
        pauseFragment=fm.findFragmentById(R.id.fragment);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.hide(pauseFragment);
        ft.commit();

        progress.setMax(45);
        progress.setProgress(45);
        progress.setRotation(180);
        progress.getProgressDrawable().setColorFilter(Color.parseColor("#4CAF50"), PorterDuff.Mode.SRC_ATOP);

        Intent i = getIntent();
        strTheme = i.getStringExtra("Theme");
        strFile = i.getStringExtra("File");

        try {
            strTextArray = LoadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        points = 0;
        activity = this;


        for(int j = 0; j < strTextArray.length; j++) randList.add(j);
            NextImage();
            new getImageTask().execute();
        long startTime = 46 * 1000;
        try {
            showTimer(startTime);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (!timerHasStarted) {
            countDownTimer.start();
            timerHasStarted = true;
        } else {
            countDownTimer.cancel();
            timerHasStarted = false;
        }

    }

    public void addTime(long addedTime) {
        showTimer(remainingTime + addedTime);
    }

    public void decreaseTime(long decTime) {
        showTimer(remainingTime - decTime);
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
                if(remainingTime<31000){
                    if (remainingTime < 16000) {

                        progress.getProgressDrawable().setColorFilter(Color.parseColor("#FF5252"), PorterDuff.Mode.SRC_IN);
                    } else {
                        progress.getProgressDrawable().setColorFilter(Color.parseColor("#FFEB3B"), PorterDuff.Mode.SRC_IN);

                    }
                }else {
                    progress.getProgressDrawable().setColorFilter(Color.parseColor("#4CAF50"), PorterDuff.Mode.SRC_IN);
                }

            }

            @Override
            public void onFinish() {
                Glide.clear(image);
                stopWatch.stop();
                image.setBackground(null);
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                builder.setTitle("Results");

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                activity.finish();
                                Intent intent = new Intent(ButtonActivity.this, HListViewTest.class);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });

                //final Dialog dialog = new Dialog(ButtonActivity.this);

                //dialog.setContentView(R.layout.resultdialog);
                //dialog.setCanceledOnTouchOutside(false);

                //dialog.setTitle("Results");
                //TextView text = (TextView) dialog.findViewById(R.id.textDialog);
               // TextView text1 = (TextView) dialog.findViewById(R.id.textDialog2);
                int highscore=Integer.parseInt(sharedpreferences.getString(strFile,"0"));
                if(points>highscore){
                    SharedPreferences.Editor editor=sharedpreferences.edit();
                    editor.putString(strFile,String.valueOf(points));
                    editor.apply();
                    //text.setText("New High Score");
                    String alert1 = "New High Score";
                    String alert2 = "Your Result: " + String.valueOf(points);
                    builder.setMessage(alert1 +"\n"+ alert2);

                }else{


                    //text.setText("High Score: "+String.valueOf(highscore));
                    String alert1 = "High Score: "+String.valueOf(highscore);
                    String alert2 = "Your Result: " + String.valueOf(points);
                    builder.setMessage(alert1 +"\n"+ alert2);
                }


                Log.d("Editor", "Theme " + strFile);
                AlertDialog alert11 = builder.create();
                alert11.show();
                //dialog.show();
                /*dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
                {
                    @Override
                    public void onCancel(DialogInterface dialog)
                    {
                        ButtonActivity.this.finish();
                        Intent intent = new Intent(ButtonActivity.this, HListViewTest.class);
                        startActivity(intent);
                    }
                });

                Button declineButton = (Button) dialog.findViewById(R.id.declineButton);
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.finish();
                        Intent intent = new Intent(ButtonActivity.this, HListViewTest.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });*/

            }
        }.start();

    }


    public void timerResume() {
        showTimer(remainingTime);
    }






    public String[] LoadFile() throws IOException {
        AssetManager am = getAssets();
        am.list("");
        InputStream is = am.open(strFile + ".txt");

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        String[] strArray = new String[0];

        while ((line = br.readLine()) != null) {
            strArray = line.split(",");
        }

        br.close();

        return strArray;
    }

    public String getImage(JSONArray resultArray) {
        String strImage_url;
        try {
            int randInt = new Random().nextInt(resultArray.length());
            JSONObject obj;
            obj = resultArray.getJSONObject(randInt);
            strImage_url = obj.getString("unescapedUrl");
            System.out.println(strImage_url);
            return strImage_url;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public void buttonClick(View v) {
        Glide.clear(image);
        NextImage();
        new getImageTask().execute();
    }



    public void NextImage() {if (gameStart){
        tickPlusDrawable.animatePause();}
        gameStart=true;
        randArray = new int[4];

        Collections.shuffle(randList,new Random());
        for (int i = 0; i < 4; i++) {

            randArray[i] = randList.get(i);
        }


        int randInt = new Random().nextInt(randArray.length);
        strSearchText = strTextArray[randArray[randInt]];
        strAnswer = strSearchText;
        String fileName = strTheme;
        strSearchText = fileName + " " + strSearchText;
        strSearchText = Uri.encode(strSearchText);
        Log.d("Next Image","Search string =>"+strSearchText);

    }


    public boolean CheckAnswer(String a, String b) {
        return a.equalsIgnoreCase(b);
    }

    public void AddPoints() {
        if (points < 1000) {
            points += 100;
        } else {
            if (points < 5000) {
                points += 250;
            } else {
                if (points < 20000) {
                    points += 500;
                }else{
                    points+=1000;
                }
            }
        }

        if (remainingTime > 40000) {
            addTime(45000 - remainingTime);
        } else {
            addTime(5000);
        }
    }

    public void DecreasePoints() {
        if (points < 1000) {
            points -= 50;
        } else {
            if (points < 5000) {
                points -= 150;
            } else {
                if (points < 20000) {
                    points -= 350;
                }
            }
        }
        decreaseTime(3000);
    }


    public void AnswerClick(View v) {

        Button btn=(Button) v;
        String answ;
        var1.setEnabled(false);
        var2.setEnabled(false);
        var3.setEnabled(false);
        var4.setEnabled(false);
        image.setEnabled(false);
        answ = strAnswer;

        if (CheckAnswer(btn.getText().toString(), answ)) {

            AddPoints();
            tickPlusDrawable.animateTick();

        } else {

            DecreasePoints();
            tickPlusDrawable.animateCross();
        }
        pnts.setText(String.valueOf(points));

    }

    public class getImageTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {


            String url;

            url = "https://ajax.googleapis.com/ajax/services/search/images?" +
                    "v=1.0&q=" + strSearchText + "&rsz=8&as_filetype=jpg";

            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                JSONObject jObject = response.getJSONObject("responseData");
                                Log.d("",jObject.toString());
                                JSONArray jsonArray = jObject.getJSONArray("results");
                                showImage(jsonArray);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            MySingleton.getInstance(activity).addToRequestQueue(jsonObjReq);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
        }
    }

    public void onPauseClick(View v){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_top,
                R.anim.slide_out_top);
        if (pauseFragment.isHidden()) {
            ft.show(pauseFragment);
            tickPlusDrawable.animatePlay();
            stopWatch.stop();
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


    public void showImage(JSONArray json){
        if (countDownTimer == null) {
            timerResume();
        }

        String mImage_url = getImage(json);
        image =(ImageView)findViewById(R.id.view);



        Animation anim1 = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);

        Glide.with(this).load(mImage_url).animate(anim1).fitCenter().into(new GlideDrawableImageViewTarget(image) {
            AnimationDrawable imageAnimation;

            @Override
            public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                super.onResourceReady(drawable, anim);
                es=0;
                imageAnimation.stop();
                stopWatch.stop();
                showTimer(remainingTime);
                image.setBackground(null);
                var1.setEnabled(true);
                var2.setEnabled(true);
                var3.setEnabled(true);
                var4.setEnabled(true);
                image.setEnabled(true);
                var1.setText(String.valueOf(strTextArray[randArray[0]]));
                var2.setText(String.valueOf(strTextArray[randArray[1]]));
                var3.setText(String.valueOf(strTextArray[randArray[2]]));
                var4.setText(String.valueOf(strTextArray[randArray[3]]));

            }

            @Override
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);

                image.setBackgroundResource(R.drawable.imageanim);
                imageAnimation = (AnimationDrawable) image.getBackground();
                var1.setText("");
                var2.setText("");
                var3.setText("");
                var4.setText("");
                if(remainingTime<1500){
                    imageAnimation.stop();
                    stopWatch.stop();
                }else{
                imageAnimation.start();
                stopWatch.start();
                countDownTimer.cancel();}

            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);

            }
        });


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
                    ObjectAnimator.ofObject(this, mLineColorProperty, mArgbEvaluator, mTickColor),
                    ObjectAnimator.ofObject(this, mBackgroundColorProperty, mArgbEvaluator, mPlusColor)
            );

            set.setDuration(ANIMATION_DURATION);
            set.setInterpolator(ANIMATION_INTERPOLATOR);
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    NextImage();
                    new getImageTask().execute();

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
                    ObjectAnimator.ofFloat(this, mPropertyPointAX,(mBounds.centerX())-mBounds.centerX()/3),
                    ObjectAnimator.ofFloat(this, mPropertyPointAY, (mBounds.centerY())-mBounds.centerY()/3),

                    ObjectAnimator.ofFloat(this, mPropertyPointBX,mBounds.centerX()+mBounds.centerX()/3),
                    ObjectAnimator.ofFloat(this, mPropertyPointBY, mBounds.centerY()+mBounds.centerY()/3),

                    ObjectAnimator.ofFloat(this, mPropertyPointCX, mBounds.centerX()+mBounds.centerX()/3),
                    ObjectAnimator.ofFloat(this, mPropertyPointCY, mBounds.centerY()-mBounds.centerY()/3),

                    ObjectAnimator.ofFloat(this, mPropertyPointDX, mBounds.centerX()-mBounds.centerX()/3),
                    ObjectAnimator.ofFloat(this, mPropertyPointDY, mBounds.centerY()+mBounds.centerY()/3),

                    ObjectAnimator.ofFloat(this, mRotationProperty, 0f, 1f),
                    ObjectAnimator.ofObject(this, mLineColorProperty, mArgbEvaluator, mTickColor),
                    ObjectAnimator.ofObject(this, mBackgroundColorProperty, mArgbEvaluator, mCrossColor)
            );
            set.setDuration(ANIMATION_DURATION);
            set.setInterpolator(ANIMATION_INTERPOLATOR);
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    NextImage();
                    new getImageTask().execute();

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
            set.setStartDelay(500);
            set.setInterpolator(ANIMATION_INTERPOLATOR);
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
