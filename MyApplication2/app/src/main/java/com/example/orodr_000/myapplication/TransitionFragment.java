package com.example.orodr_000.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;


public class TransitionFragment extends Fragment {
    TextView tv;
    View rootView;

    public TransitionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_transition, container, false);
        tv=(TextView)rootView.findViewById(R.id.theme_name_tv);

        return rootView;
    }

    public  void changeText(String text){
        tv.setText(text);

    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void revealShape(int startX,int startY){
        final View shape=rootView.findViewById(R.id.transition_container);
        Animator animator = ViewAnimationUtils.createCircularReveal(
                shape,
                startX,
                startY,
                0,
                Math.max(rootView.getWidth(), rootView.getHeight()));


        // Set a natural ease-in/ease-out interpolator.
        animator.setInterpolator(new DecelerateInterpolator(12f));
        animator.setDuration(7000);
        animator.addListener(new AnimatorListenerAdapter() {



            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                shape.setVisibility(View.VISIBLE);
            }
        });

        //shape.setVisibility(View.VISIBLE);
        // Finally start the animation
        animator.start();

    }

    public int getWidth(){
        View shape=rootView.findViewById(R.id.transition_container);
        return shape.getWidth();
    }
    public int getHeight(){
        View shape=rootView.findViewById(R.id.transition_container);
        return shape.getHeight();
    }
    public void setColor(int color){
        View shape=rootView.findViewById(R.id.transition_container);
        shape.setBackgroundColor(color);
    }


}
