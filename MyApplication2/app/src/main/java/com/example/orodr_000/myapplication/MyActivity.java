package com.example.orodr_000.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;


public class MyActivity extends Activity {
    private Activity activity;
    private NetworkImageView mNetworkImageView;
    private TextView var1;
    private TextView var2;
    private TextView var3;
    private TextView var4;
    private TextView timer;
    private TextView pnts;
    private ProgressBar progress;

    int[] randArray = new int[4];
    String[] strTextArray = {""};

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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my);
        var1 = (TextView) findViewById(R.id.activity_my_variant1_tv);
        var2 = (TextView) findViewById(R.id.quiz_points);
        var3 = (TextView) findViewById(R.id.textView3);
        var4 = (TextView) findViewById(R.id.textView4);
        pnts = (TextView) findViewById(R.id.points);
        timer= (TextView) findViewById(R.id.timer);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.rellayout);
        progress=(ProgressBar)findViewById(R.id.progressBar);

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
        //progress.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.LIGHTEN);

        Intent i = getIntent();
        strTheme=i.getStringExtra("Theme");
        strFile=i.getStringExtra("File");

        try {
            strTextArray = LoadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        points = 0;
        activity = this;
        layout.setBackgroundResource(R.drawable.image);
        mNetworkImageView = (NetworkImageView) findViewById(R.id.view);
        NextImage();
        if(!timerHasStarted){
            countDownTimer.start();
            timerHasStarted=true;
        }else{
            countDownTimer.cancel();
            timerHasStarted=false;
        }

    }

    public void addTime(long addedTime)
    {
        showTimer(remainingTime+addedTime);
    }

    public void decreaseTime(long decTime){showTimer(remainingTime-decTime);}

    public void showTimer(final long millisecondsToCountDown)
    {

        if(countDownTimer != null) { countDownTimer.cancel(); }
        countDownTimer = new CountDownTimer(millisecondsToCountDown, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime=millisUntilFinished;
                progress.setProgress((int)millisUntilFinished/1000);
                if(remainingTime<21000){
                    timer.setTextColor(Color.RED);
                    //progress.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                }
                else
                {
                    //progress.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                    timer.setTextColor(Color.BLACK);
                }
                timer.setText(String.valueOf(millisUntilFinished/1000));
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
    public void timerPause(){

        countDownTimer.cancel();
    }


    public void timerResume(){
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
        InputStream is = am.open(strFile+".txt");

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
    }


    public void NextImage() {
        for (int i = 0; i < 4; i++) {
            int randInt = new Random().nextInt(strTextArray.length);
            randArray[i] = randInt;
        }


        int randInt = new Random().nextInt(randArray.length);
        strSearchText = strTextArray[randArray[randInt]];
        strAnswer = strSearchText;
        String fileName =strTheme;
        strSearchText = fileName + " " + strSearchText;
        strSearchText = Uri.encode(strSearchText);
        System.out.println("Search string =>" + strSearchText);
        new getImageTask().execute();
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

        if(remainingTime>65000){
            addTime(71000-remainingTime);
        }else
        {
            addTime(5000);
        }
    }

    public void DecreasePoints(){
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
        TextView tv = (TextView) v;
        if (CheckAnswer(tv.getText().toString(), strAnswer)) {

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
        JSONObject json;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            dialog = ProgressDialog.show(MyActivity.this, "", "Please wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            URL url;
            try {
                url = new URL("https://ajax.googleapis.com/ajax/services/search/images?" +
                        "v=1.0&q=" + strSearchText + "&rsz=8&imgtype=photo");

                URLConnection connection = url.openConnection();

                connection.addRequestProperty("Referer", "http://www.google.com");

                String line;
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                System.out.println("Builder string => " + builder.toString());

                json = new JSONObject(builder.toString());
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if(countDownTimer==null){
                timerResume();
            }

            try {
                JSONObject responseObject = json.getJSONObject("responseData");
                JSONArray resultArray = responseObject.getJSONArray("results");
                String mImage_url = getImage(resultArray);
                                   ImageLoader mImageLoader = MySingleton.getInstance(activity).getImageLoader();

                    mNetworkImageView.setImageUrl(mImage_url, mImageLoader);

                    var1.setText(String.valueOf(strTextArray[randArray[0]]));
                    var2.setText(String.valueOf(strTextArray[randArray[1]]));
                    var3.setText(String.valueOf(strTextArray[randArray[2]]));
                    var4.setText(String.valueOf(strTextArray[randArray[3]]));
               } catch (JSONException e) {
                // TODO Auto-generated catch block

                timerPause();
                NextImage();

                e.printStackTrace();
            }
        }
    }
}
