package com.example.orodr_000.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.TextView;



public class RevealFragment extends Fragment {
    TextView tv;
    View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_reveal, container, false);
        tv=(TextView)rootView.findViewById(R.id.theme_name_tv);
        return rootView;
    }
    public  void changeText(String text){
        if(tv!=null)
            tv.setText(text);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void hideShape(int startX,int startY){
        final View shape=rootView.findViewById(R.id.reveal_container);

        Animator animator = ViewAnimationUtils.createCircularReveal(
                shape,
                startX,
                startY,
                (float) Math.hypot(rootView.getWidth(), rootView.getHeight()),0
        );


        // Set a natural ease-in/ease-out interpolator.
        //animator.setInterpolator(new DecelerateInterpolator());
        //animator.setStartDelay(200);
        animator.setDuration(1000);
        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                shape.setVisibility(View.INVISIBLE);
            }

        });

        // Finally start the animation
        animator.start();

    }

    public int getWidth(){
        View shape=rootView.findViewById(R.id.reveal_container);
        return shape.getWidth();
    }

    public int getHeight(){
        View shape=rootView.findViewById(R.id.reveal_container);
        return shape.getHeight();
    }
}
