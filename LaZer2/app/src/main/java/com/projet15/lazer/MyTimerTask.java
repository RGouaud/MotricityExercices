package com.projet15.lazer;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.util.Log;

import java.util.TimerTask;

/**
 * Created by edith on 26/03/2018.
 */

public class MyTimerTask extends TimerTask {
    public void run() {
        Thread t = new Thread() {

            @Override
            public void run() {
                ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                Log.e("TonGen","Biiip");
                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,150);
            }
        };
        t.start();
    }
}
