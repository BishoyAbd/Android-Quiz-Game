package com.example.orodr_000.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
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
import android.support.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;


public class ButtonActivity extends Activity {
    static ImageLoader imageLoader = ImageLoader.getInstance();
    int[] randArray;
    String[] strTextArray = {""};
    private Activity activity;

    private String mImage_url;
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
    private FloatingActionButton button;
    private FloatingActionButton yesButton;
    private FloatingActionButton noButton;
    public TickPlusDrawable tickPlusDrawable;
    public boolean gameStart=false;
    private View animView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        //cardView.setPreventCornerOverlap(false);
        // aq = new AQuery(this);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        setContentView(R.layout.activity_button);

        animView = findViewById(R.id.animView);
        View backgroundView = findViewById(R.id.animView1);
        tickPlusDrawable = new TickPlusDrawable(getResources().getDimensionPixelSize(R.dimen.stroke_width),  Color.parseColor("#37474F"),Color.parseColor("#FF5722"),Color.parseColor("#4CAF50"));
        animView.setBackground(tickPlusDrawable);
        backgroundView.setBackground(new TickBackgroundDrawable(Color.parseColor("#37474F")));

        /*button = (FloatingActionButton) findViewById(R.id.answerButton2);
        button.setSize(FloatingActionButton.SIZE_MINI);
        yesButton=(FloatingActionButton)findViewById(R.id.answerButtonYes);
        yesButton.setSize(FloatingActionButton.SIZE_MINI);
        noButton=(FloatingActionButton)findViewById(R.id.answerButtonNo);
        noButton.setSize(FloatingActionButton.SIZE_MINI);*/


        var1 = (Button) findViewById(R.id.activity_my_variant1_btn);
        var2 = (Button) findViewById(R.id.activity_my_variant2_btn);
        var3 = (Button) findViewById(R.id.activity_my_variant3_btn);
        var4 = (Button) findViewById(R.id.activity_my_variant4_btn);
        pnts = (TextView) findViewById(R.id.activity_my_points_tv);
        progress = (ProgressBar) findViewById(R.id.progressBar);

        /*DisplayImageOptions defaultOptions =
                new DisplayImageOptions.Builder()
                        .cacheInMemory(true)
                                //.showImageOnLoading(R.drawable.loading)
                        .showImageForEmptyUri(R.drawable.loading)
                        .showImageOnFail(R.drawable.loading)
                        .cacheOnDisc(true)
                        .displayer(new FadeInBitmapDisplayer(500))
                        .build();
        ImageLoaderConfiguration mImageLoaderConfig =
                new ImageLoaderConfiguration.Builder(getApplicationContext())
                        .defaultDisplayImageOptions(defaultOptions)
                        .build();
        imageLoader.init(mImageLoaderConfig);*/

        long startTime = 71 * 1000;
        try {
            showTimer(startTime);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        //timer.setText(String.valueOf(startTime / 1000));

        progress.setMax(70);
        progress.setProgress(70);
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
        //layout.setBackgroundResource(R.drawable.image);

        if(savedInstanceState==null){
            NextImage();
            new getImageTask().execute();}
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
                if(remainingTime<51000){
                    if (remainingTime < 21000) {
                        // timer.setTextColor(Color.RED);
                        progress.getProgressDrawable().setColorFilter(Color.parseColor("#FF5252"), PorterDuff.Mode.SRC_IN);
                    } else {
                        progress.getProgressDrawable().setColorFilter(Color.parseColor("#FFEB3B"), PorterDuff.Mode.SRC_IN);
                        // timer.setTextColor(Color.BLACK);
                    }
                }else {
                    progress.getProgressDrawable().setColorFilter(Color.parseColor("#4CAF50"), PorterDuff.Mode.SRC_IN);
                }
                //  timer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                Toast toast = Toast.makeText(getApplicationContext(), "GAME OVER", Toast.LENGTH_LONG);
                TextView t = (TextView) toast.getView().findViewById(android.R.id.message);
                t.setTextColor(Color.RED);
                toast.show();
                activity.finish();
            }
        }.start();

    }


    public void timerResume() {
        showTimer(remainingTime);
    }


    /*@Override
    public void onWindowFocusChanged (boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        AnimationDrawable frameAnimation =
                (AnimationDrawable) layout.getBackground();
        if(hasFocus) {
            frameAnimation.start();
        } else {
            frameAnimation.stop();
        }
    }*/
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
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putString("Variant1", var1.getText().toString());
        savedInstanceState.putString("Variant2", var2.getText().toString());
        savedInstanceState.putString("Variant3", var3.getText().toString());
        savedInstanceState.putString("Variant4", var4.getText().toString());
        savedInstanceState.putLong("Time", remainingTime);
        savedInstanceState.putString("Points", pnts.getText().toString());
        savedInstanceState.putString("Url",mImage_url);
        savedInstanceState.putString("Answer",strAnswer);
        savedInstanceState.putDouble("myDouble", 1.9);
        savedInstanceState.putInt("MyInt", 1);
        savedInstanceState.putString("MyString", "Welcome back to Android");
        // etc.
    }
    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        remainingTime=savedInstanceState.getLong("Time");
        var1.setText(savedInstanceState.getString("Variant1"));
        var2.setText(savedInstanceState.getString("Variant2"));
        var3.setText(savedInstanceState.getString("Variant3"));
        var4.setText(savedInstanceState.getString("Variant4"));
        pnts.setText(savedInstanceState.getString(("Points")));
        strAnswer=savedInstanceState.getString("Answer");
        mImage_url = savedInstanceState.getString("Url");
        points= Integer.valueOf(pnts.getText().toString());
        //aq.id(R.id.view).image(mImage_url, true, true, 0, R.drawable.loading,null,AQuery.FADE_IN);
        final ImageView image =(ImageView)findViewById(R.id.view);
        //imageLoader.displayImage(mImage_url, image);
        Animation anim1 = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);

        Glide.with(this).load(mImage_url).animate(anim1).fitCenter().into(new GlideDrawableImageViewTarget(image) {
            AnimationDrawable imageAnimation;
            @Override
            public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                super.onResourceReady(drawable, anim);

                imageAnimation.stop();
                image.setBackground(null);
            }

            @Override
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
                image.setBackgroundResource(R.drawable.imageanim);

                imageAnimation=(AnimationDrawable) image.getBackground();
                imageAnimation.start();
            }

        });

        try {
            showTimer(remainingTime);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        // timer.setText(String.valueOf(remainingTime / 1000));

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public void buttonClick(View v) {
        NextImage();

        new getImageTask().execute();

    }



    public void NextImage() {if (gameStart){
        tickPlusDrawable.animatePause();}
        gameStart=true;

        /*if(noButton.getVisibility() == View.VISIBLE){
            animateHideAnswer(noButton);
            //noButton.setVisibility(View.INVISIBLE);
            Log.d("Button","Wrong Answer");
        }else{
        if(yesButton.getVisibility() == View.VISIBLE){
            animateHideAnswer(yesButton);
            //yesButton.setVisibility(View.INVISIBLE);
            Log.d("Button","Right Answer");
        }}*/
        randArray = new int[4];
        for (int i = 0; i < 4; i++) {
            int randInt = new Random().nextInt(strTextArray.length);
            randArray[i] = randInt;
        }


        int randInt = new Random().nextInt(randArray.length);
        strSearchText = strTextArray[randArray[randInt]];
        strAnswer = strSearchText;
        String fileName = strTheme;
        strSearchText = fileName + " " + strSearchText;
        strSearchText = Uri.encode(strSearchText);
        System.out.println("Search string =>" + strSearchText);

    }

    /*private static String removeLastChar(String str) {
        return str.substring(0,str.length()-1);
    }*/
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

        if (remainingTime > 65000) {
            addTime(70000 - remainingTime);
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
        //TextView tv = (TextView) v;
        Button btn=(Button) v;
        String answ;
        var1.setEnabled(false);
        var2.setEnabled(false);
        var3.setEnabled(false);
        var4.setEnabled(false);
        answ = strAnswer;

        if (CheckAnswer(btn.getText().toString(), answ)) {

            AddPoints();
            tickPlusDrawable.animateTick();
            /*if(tickPlusDrawable.animEnd){
                NextImage();
                new getImageTask().execute();}*/
            //Toast toast = Toast.makeText(getApplicationContext(), "Right Answer", Toast.LENGTH_SHORT);

            /*int cx;
            int cy;
            cx=yesButton.getWidth()/2;
            cy=0;
            int initialRadius =  Math.max(yesButton.getWidth(), yesButton.getHeight())*2;

            Animator anim;
            anim = ViewAnimationUtils.createCircularReveal(yesButton, cx, cy, 0, initialRadius);
            anim.setDuration(1000);
            button.setVisibility(View.INVISIBLE);
            anim.setInterpolator(new DecelerateInterpolator(2f));
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    yesButton.setVisibility(View.VISIBLE);
                    NextImage();
                    new getImageTask().execute();
                }
            });


            anim.start();*/

            //TextView t = (TextView) toast.getView().findViewById(android.R.id.message);
            //t.setTextColor(Color.GREEN);
            //toast.show();
        } else {

            DecreasePoints();
            tickPlusDrawable.animateCross();

            /*if(tickPlusDrawable.animEnd){
                NextImage();
                new getImageTask().execute();}*/
           /* int cx =noButton.getWidth()/2;
            int cy =0;
            int initialRadius = noButton.getHeight()*2;

            final Animator anim = ViewAnimationUtils.createCircularReveal(noButton, cx, cy, 0,initialRadius);

            anim.setDuration(1000);
            button.setVisibility(View.INVISIBLE);
            anim.setInterpolator(new DecelerateInterpolator(2f));
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    //yesButton.setVisibility(View.INVISIBLE);
                    noButton.setVisibility(View.VISIBLE);

                    NextImage();
                    new getImageTask().execute();
                }
            });


            anim.start();*/
            //Toast toast = Toast.makeText(getApplicationContext(), "Wrong Answer", Toast.LENGTH_SHORT);
            //TextView t = (TextView) toast.getView().findViewById(android.R.id.message);
            // button.setIcon(R.drawable.ic_action_cancel_dark);
            // button.setColorNormal(Color.RED);
            //t.setTextColor(Color.RED);
            //toast.show();
        }
        pnts.setText(String.valueOf(points));


        /*AlertDialog alertDialog = new AlertDialog.Builder(
                MyActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle("Alert Dialog");

        // Setting Dialog Message
        alertDialog.setMessage("Right Answer: "+strAnswer+". Your Answer: "+tv.getText().toString());


        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                //Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                NextImage();
            }
        });

        // Showing Alert Message
        alertDialog.show();
        */
    }

    public class getImageTask extends AsyncTask<Void, Void, Void> {
        //ProgressDialog dialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            //dialog = ProgressDialog.show(MyActivity.this, "", "Please wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {


            String url;

            url = "https://ajax.googleapis.com/ajax/services/search/images?" +
                    "v=1.0&q=" + strSearchText + "&rsz=8&imgsz=large&as_filetype=jpg";

            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                JSONObject jObject = response.getJSONObject("responseData");
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
            /*aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {

                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {


                    if(json != null){

                        //successful ajax call, show status code and json content
                        JSONObject jObject;
                        try {
                            jObject = json.getJSONObject("responseData");
                            JSONArray jsonArray = jObject.getJSONArray("results");
                            showImage(jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Toast.makeText(aq.getContext(), status.getCode() + ":" + json.toString(), Toast.LENGTH_LONG).show();

                    }else{

                        //ajax error, show error code
                        Toast.makeText(aq.getContext(), "Error:" + status.getCode(), Toast.LENGTH_LONG).show();
                    }
                }
            });*/




            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            /*if (dialog.isShowing()) {
                dialog.dismiss();
            }*/

        }
    }
    public void showImage(JSONArray json){
        if (countDownTimer == null) {
            timerResume();
        }

        mImage_url = getImage(json);
        final ImageView image =(ImageView)findViewById(R.id.view);

        /*imageLoader.displayImage(mImage_url, image, new
                SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        super.onLoadingFailed(imageUri, view, failReason);
                        Log.d("Image Loading Failed","Failed to load image "+failReason);
                    }

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        super.onLoadingStarted(imageUri, view);
                    }

                });*/
        Animation anim1 = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);

        Glide.with(this).load(mImage_url).animate(anim1).fitCenter().into(new GlideDrawableImageViewTarget(image) {
            AnimationDrawable imageAnimation;
            @Override
            public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                super.onResourceReady(drawable, anim);

                imageAnimation.stop();
                image.setBackground(null);
                var1.setEnabled(true);
                var2.setEnabled(true);
                var3.setEnabled(true);
                var4.setEnabled(true);
            }

            @Override
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
                image.setBackgroundResource(R.drawable.imageanim);

                imageAnimation=(AnimationDrawable) image.getBackground();
                imageAnimation.start();
            }

        });
       /* Picasso.with(this).load(mImage_url).fit().into(image,new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });*/

        //aq.id(R.id.view).image(mImage_url, true, true, 0, R.drawable.loading,null,AQuery.FADE_IN);
        var1.setText(String.valueOf(strTextArray[randArray[0]]));
        var2.setText(String.valueOf(strTextArray[randArray[1]]));
        var3.setText(String.valueOf(strTextArray[randArray[2]]));
        var4.setText(String.valueOf(strTextArray[randArray[3]]));
    }

    public class TickPlusDrawable extends Drawable {

        private static final long ANIMATION_DURATION = 200;
        private final Interpolator ANIMATION_INTERPOLATOR = new DecelerateInterpolator();

        private Paint mLinePaint;
        private Paint mBackgroundPaint;

        private float[] mPoints = new float[8];
        private final RectF mBounds = new RectF();

        private boolean mTickMode;
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

        private void setupPlusMode() {
            mPoints[0] = mBounds.left;
            mPoints[1] = mBounds.centerY();
            mPoints[2] = mBounds.right;
            mPoints[3] = mBounds.centerY();
            mPoints[4] = mBounds.centerX();
            mPoints[5] = mBounds.top;
            mPoints[6] = mBounds.centerX();
            mPoints[7] = mBounds.bottom;
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

        public void toggle() {
            if(mTickMode) {
                animatePlus();
            } else {
                animateCross();
            }
            mTickMode = !mTickMode;
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

        public void animatePlus() {
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(this, mPropertyPointAX, mBounds.left),
                    ObjectAnimator.ofFloat(this, mPropertyPointAY, mBounds.centerY()),

                    ObjectAnimator.ofFloat(this, mPropertyPointBX, mBounds.right),
                    ObjectAnimator.ofFloat(this, mPropertyPointBY, mBounds.centerY()),

                    ObjectAnimator.ofFloat(this, mPropertyPointCX, mBounds.centerX()),
                    ObjectAnimator.ofFloat(this, mPropertyPointCY, mBounds.top),

                    ObjectAnimator.ofFloat(this, mPropertyPointDX, mBounds.centerX()),
                    ObjectAnimator.ofFloat(this, mPropertyPointDY, mBounds.bottom),

                    ObjectAnimator.ofFloat(this, mRotationProperty, 0f, 1f),
                    ObjectAnimator.ofObject(this, mLineColorProperty, mArgbEvaluator, Color.parseColor("#3F51B5")),
                    ObjectAnimator.ofObject(this, mBackgroundColorProperty, mArgbEvaluator, mTickColor)
            );
            set.setDuration(ANIMATION_DURATION);
            set.setStartDelay(500);
            set.setInterpolator(ANIMATION_INTERPOLATOR);
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
