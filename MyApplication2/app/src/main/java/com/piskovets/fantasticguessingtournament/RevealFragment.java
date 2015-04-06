package com.piskovets.fantasticguessingtournament;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;


public class RevealFragment extends Fragment {
    TextView tv;
    View rootView;
    View shape;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_reveal, container, false);
        tv=(TextView)rootView.findViewById(R.id.theme_name_tv);
        shape=rootView.findViewById(R.id.reveal);

        return rootView;
    }
    public  void changeText(String text){
        if(tv!=null)
            tv.setText(text);

    }

    public void hideShape(int startX,int startY){
        //final View shape=rootView.findViewById(R.id.reveal_container);


        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(
                shape,
                startX,
                startY,
                (float) Math.hypot(rootView.getWidth(), rootView.getHeight()), 0
        );


        // Set a natural ease-in/ease-out interpolator.
        //animator.setInterpolator(new DecelerateInterpolator());
        //animator.setStartDelay(200);
        animator.setDuration(1000);
        /*animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                shape.setVisibility(View.INVISIBLE);
            }

        });*/
        animator.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {
                shape.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel() {

            }

            @Override
            public void onAnimationRepeat() {

            }
        });

        // Finally start the animation
        animator.start();

    }
    public void hideProgressBar(){
        View progressbar=rootView.findViewById(R.id.progressBar2);
        progressbar.setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.textView2).setVisibility(View.INVISIBLE);
    }


    public int getWidth(){

        return shape.getWidth();
    }
    public void setProgressColor(){
        //ProgressBarCircularIndeterminate progressbar=(ProgressBarCircularIndeterminate)rootView.findViewById(R.id.progressBar2);
        //progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#388E3C"), PorterDuff.Mode.SRC_IN);
    }





    public void setColor(int color){
        shape.setBackgroundColor(color);
    }
    public boolean isShapeVisible(){
        return shape.getVisibility() == View.VISIBLE;
    }

    public int getHeight(){
        //View shape=rootView.findViewById(R.id.reveal_container);
        return shape.getHeight();
    }
}
