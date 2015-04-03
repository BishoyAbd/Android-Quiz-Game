package com.piskovets.fantasticguessingtournament;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by orodr_000 on 27.03.2015.
 */
public class AnimationSound {
    MediaPlayer mpsound;

    AnimationSound(Context context,int id){
        mpsound=MediaPlayer.create(context,id);
    }

    public void startSound(boolean isLooped){
        mpsound.start();
        mpsound.setLooping(isLooped);
    }

    public void stopSound(){
        if(mpsound!=null){
            if(mpsound.isPlaying()){
                mpsound.stop();
                mpsound.setLooping(false);
            }
            mpsound.release();
            mpsound=null;
        }
    }
}
