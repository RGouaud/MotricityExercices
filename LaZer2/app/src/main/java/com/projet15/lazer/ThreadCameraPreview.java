package com.projet15.lazer;


import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import java.io.IOException;

import static android.content.ContentValues.TAG;
public class ThreadCameraPreview implements Runnable {

    private CameraPreview _cameraPreview;
    private FrameLayout _layoutPreview;


    /** constructeurs */
    public ThreadCameraPreview(CameraPreview cameraPreview, FrameLayout layoutPreview) //avec uniquement cameraPreview et FrameLyout
    {
        this._cameraPreview = cameraPreview;
        this._layoutPreview = layoutPreview;
    }


    /** run */
    public void run()
    {
        _layoutPreview.addView(_cameraPreview);
    }
    
}
