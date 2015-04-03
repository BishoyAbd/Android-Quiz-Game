package com.piskovets.fantasticguessingtournament;

import android.transitions.everywhere.ChangeBounds;
import android.transitions.everywhere.ChangeTransform;
import android.transitions.everywhere.Explode;
import android.transitions.everywhere.Fade;
import android.transitions.everywhere.Slide;
import android.transitions.everywhere.Transition;
import android.transitions.everywhere.TransitionSet;
import android.view.Gravity;
import android.view.animation.DecelerateInterpolator;

public final class PreLollipopTransitionUtils {

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

        Transition slide2=new Fade(Fade.IN);
        slide2.addTarget(R.id.textView);
        slide2.addTarget(R.id.activity_my_points_tv);
        slide2.setDuration(500);
        set.addTransition(slide2);

        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Transition makeExitTransition(){
        Transition explode=new Explode();
        explode.setDuration(1000);
        return explode;
    }

    /**
     * Returns a transition that will (1) move the shared element to its correct size
     * and location on screen, (2) gradually increase/decrease the shared element's
     * text size, and (3) gradually alters the shared element's text color through out
     * the transition.
     */


    private PreLollipopTransitionUtils() {
    }
}
