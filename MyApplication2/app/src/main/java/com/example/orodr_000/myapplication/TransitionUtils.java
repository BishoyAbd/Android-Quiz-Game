package com.example.orodr_000.myapplication;

import android.transition.ChangeBounds;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.animation.DecelerateInterpolator;

public final class TransitionUtils {

    /**
     * Returns a modified enter transition that excludes the navigation bar and status
     * bar as targets during the animation. This ensures that the navigation bar and
     * status bar won't appear to "blink" as they fade in/out during the transition.
     */
    public static Transition makeEnterTransition() {
        TransitionSet set=new TransitionSet();
        set.setOrdering(TransitionSet.ORDERING_TOGETHER);
        Transition slide = new Slide();
        slide.setStartDelay(900);
        slide.setDuration(1000);
        slide.addTarget(R.id.button2);
        slide.addTarget(R.id.button8);
        set.addTransition(slide);

        Transition slide1 = new Slide(Gravity.TOP);
        slide1.addTarget(R.id.highscore_tv);
        slide1.addTarget(R.id.category_image_view);
        slide1.setStartDelay(900);
        slide1.setDuration(1000);
        set.addTransition(slide1);

        Transition slide2=new Slide(Gravity.LEFT);
        slide2.addTarget(R.id.textView);
        slide2.setDuration(500);
        set.addTransition(slide2);

        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Transition makeExitTransition(){
        Transition explode=new Explode();
        explode.setStartDelay(900);
        explode.setDuration(1000);
        return explode;
    }

    /**
     * Returns a transition that will (1) move the shared element to its correct size
     * and location on screen, (2) gradually increase/decrease the shared element's
     * text size, and (3) gradually alters the shared element's text color through out
     * the transition.
     */
    public static Transition makeSharedElementEnterTransition() {
        TransitionSet set = new TransitionSet();
        set.setOrdering(TransitionSet.ORDERING_TOGETHER);

        Transition changeBounds = new ChangeBounds();
        changeBounds.addTarget(R.id.activity_my_points_tv);
        //changeBounds.addTarget(R.id.category_image_view);
        changeBounds.setDuration(500);
        set.addTransition(changeBounds);

        /*Transition changeBounds1 = new ChangeBounds();
        changeBounds1.addTarget(R.id.category_image_view);
        changeBounds1.setDuration(1000);
        set.addTransition(changeBounds1);*/


        Transition textSize = new TextSizeTransition();
        textSize.addTarget(R.id.activity_my_points_tv);
        textSize.setDuration(500);
        //textSize.addTarget(context.getString(R.string.hello_world));
        set.addTransition(textSize);
        
       
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    private TransitionUtils() {
    }
}
