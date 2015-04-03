package com.piskovets.fantasticguessingtournament;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;


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
    public void revealShape(int startX,int startY){
        final View shape=rootView.findViewById(R.id.transition_container);
        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(
                shape,
                startX,
                startY,
                0,
                Math.max(rootView.getWidth(), rootView.getHeight()));


        // Set a natural ease-in/ease-out interpolator.
        animator.setInterpolator(new DecelerateInterpolator(12f));
        animator.setDuration(7000);
        /*animator.addListener(new AnimatorListenerAdapter() {



            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                shape.setVisibility(View.VISIBLE);
            }
        });*/
        animator.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {
                shape.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd() {

            }

            @Override
            public void onAnimationCancel() {

            }

            @Override
            public void onAnimationRepeat() {

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
