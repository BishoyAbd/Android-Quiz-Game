package com.example.orodr_000.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Property;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;


public class MyActivity extends Activity {
    static ImageLoader imageLoader = ImageLoader.getInstance();
    int[] randArray;
    String[] strTextArray = {""};
    private Activity activity;

    private TextView var1;
    private TextView var2;
    private TextView var3;
    private TextView var4;
    private TextView timer;
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















    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_button);
        /*var1 = (TextView) findViewById(R.id.activity_my_variant1_tv);
        var2 = (TextView) findViewById(R.id.activity_my_variant2_tv);
        var3 = (TextView) findViewById(R.id.activity_my_variant3_tv);
        var4 = (TextView) findViewById(R.id.activity_my_variant4_tv);*/
        var1 = (TextView) findViewById(R.id.activity_my_variant1_btn);
        var2 = (TextView) findViewById(R.id.activity_my_variant2_btn);
        var3 = (TextView) findViewById(R.id.activity_my_variant3_btn);
        var4 = (TextView) findViewById(R.id.activity_my_variant4_btn);
        pnts = (TextView) findViewById(R.id.activity_my_points_tv);
        timer = (TextView) findViewById(R.id.activity_my_timer_tv);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.rellayout);
        progress = (ProgressBar) findViewById(R.id.progressBar);

        DisplayImageOptions defaultOptions =
                new DisplayImageOptions.Builder()
                        .cacheInMemory(true)
                        .showImageOnLoading(R.drawable.loading)
                        .showImageForEmptyUri(R.drawable.loading)
                        .showImageOnFail(R.drawable.loading)
                                //.cacheOnDisc(true)
                        .displayer(new FadeInBitmapDisplayer(500))
                        .build();
        ImageLoaderConfiguration mImageLoaderConfig =
                new ImageLoaderConfiguration.Builder(getApplicationContext())
                        .defaultDisplayImageOptions(defaultOptions)
                        .build();
        imageLoader.init(mImageLoaderConfig);

        long startTime = 71 * 1000;
        try {
            showTimer(startTime);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        timer.setText(String.valueOf(startTime / 1000));

        progress.setMax(70);
        progress.setProgress(70);
        progress.setRotation(180);
        progress.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);

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


        NextImage();
        new getImageTask().execute();
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
                if (remainingTime < 21000) {
                    timer.setTextColor(Color.RED);
                    progress.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                } else {
                    progress.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                    timer.setTextColor(Color.BLACK);
                }
                timer.setText(String.valueOf(millisUntilFinished / 1000));
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

    public void timerPause() {

        countDownTimer.cancel();
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
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public void buttonClick(View v) {
        NextImage();

        new getImageTask().execute();

    }


    public void NextImage() {
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

        answ = strAnswer;

        if (CheckAnswer(btn.getText().toString(), answ)) {

            AddPoints();
            Toast toast = Toast.makeText(getApplicationContext(), "Right Answer", Toast.LENGTH_SHORT);
            TextView t = (TextView) toast.getView().findViewById(android.R.id.message);
            t.setTextColor(Color.GREEN);
            toast.show();
        } else {

            DecreasePoints();
            Toast toast = Toast.makeText(getApplicationContext(), "Wrong Answer", Toast.LENGTH_SHORT);
            TextView t = (TextView) toast.getView().findViewById(android.R.id.message);
            t.setTextColor(Color.RED);
            toast.show();
        }
        pnts.setText(String.valueOf(points));
        NextImage();
        //new getImageTask().execute();
        new getImageTask().execute();
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
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            //dialog = ProgressDialog.show(MyActivity.this, "", "Please wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            String url;

            url = "https://ajax.googleapis.com/ajax/services/search/images?" +
                    "v=1.0&q=" + strSearchText + "&rsz=8&imgsz=large";

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




            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
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

        String mImage_url = getImage(json);
        ImageView image = (ImageView) findViewById(R.id.view);

        imageLoader.displayImage(mImage_url, image, new
                SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    }
                });
        var1.setText(String.valueOf(strTextArray[randArray[0]]));
        var2.setText(String.valueOf(strTextArray[randArray[1]]));
        var3.setText(String.valueOf(strTextArray[randArray[2]]));
        var4.setText(String.valueOf(strTextArray[randArray[3]]));
    }


}
