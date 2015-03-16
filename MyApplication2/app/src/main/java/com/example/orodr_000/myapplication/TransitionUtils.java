package com.example.orodr_000.myapplication;

import android.transition.ChangeBounds;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionSet;

public final class TransitionUtils {

    /**
     * Returns a modified enter transition that excludes the navigation bar and status
     * bar as targets during the animation. This ensures that the navigation bar and
     * status bar won't appear to "blink" as they fade in/out during the transition.
     */
    public static Transition makeEnterTransition() {
        Transition slide = new Fade();
        slide.setStartDelay(300);
        return slide;
    }

    public static Transition makeExitTransition(){
        Transition explode=new Explode();
        explode.setStartDelay(500);
        explode.setDuration(2000);
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

        /*Transition recolor = new Recolor();
        recolor.addTarget(R.id.activity_my_points_tv);
        recolor.addTarget(context.getString(R.string.hello_world));
        set.addTransition(recolor);*/

        Transition changeBounds = new ChangeBounds();
        changeBounds.addTarget(R.id.activity_my_points_tv);
        //changeBounds.addTarget(context.getString(R.string.hello_world));
        changeBounds.setDuration(300);
        set.addTransition(changeBounds);

        Transition textSize = new TextSizeTransition();
        textSize.addTarget(R.id.activity_my_points_tv);
        textSize.setDuration(300);
        //textSize.addTarget(context.getString(R.string.hello_world));
        set.addTransition(textSize);

        return set;
    }

    private TransitionUtils() {
    }
}
