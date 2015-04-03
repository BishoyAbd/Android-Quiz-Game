package com.piskovets.fantasticguessingtournament;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;


public class ButtonActivity extends Activity {
    private MainFragment mainFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        //getActionBar().hide();
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }


        setContentView(R.layout.test);
        mainFragment = (MainFragment) getFragmentManager().findFragmentById(R.id.main_fragment);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.main_fragment, mainFragment);
        ft.commit();
    }

    public void ButtonAnswerClick(View v) {
       mainFragment.AnswerClick(v);
    }
    public void ButtonOnPauseClick(View v){
        mainFragment.onPauseClick(v);
    }
    public void ButtonClick(View v){mainFragment.buttonClick(v);}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mainFragment.activityStates= MainFragment.ActivityStates.RETURN;
        mainFragment.myData.clear();
        if(mainFragment.countDownTimer!=null)
            mainFragment.countDownTimer.cancel();
        Intent intent = new Intent(ButtonActivity.this, HListViewTest.class);
        startActivity(intent);
        this.finish();
    }
}
