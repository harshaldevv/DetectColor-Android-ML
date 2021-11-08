package com.example.detectcolor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;



public class RealtimeColor extends SurfaceView implements SurfaceHolder.Callback , Camera.PreviewCallback {

    RealtimeColor realtimeColor;
    SurfaceHolder surfaceHolder;
    Camera  mycamera=null;
    Camera.Size prevsize;

    public RealtimeColor(Context context) {
        super(context);
        surfaceHolder.getSurface();
        surfaceHolder.addCallback(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mycamera=Camera.open();
        mycamera.setPreviewCallback(this);

        Camera.Parameters parameters=mycamera.getParameters();
        prevsize=parameters.getPreviewSize();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            mycamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            mycamera.stopPreview();
            mycamera.release();
            mycamera=null;

    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        YuvImage yuvImage = new YuvImage(bytes, parameters.getPreviewFormat(), parameters.getPreviewSize().width, parameters.getPreviewSize().height, null);
        yuvImage.compressToJpeg(new Rect(0, 0, parameters.getPreviewSize().width, parameters.getPreviewSize().height), 90, out);
        byte[] imageBytes = out.toByteArray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        Toast.makeText(getContext(), "bitmap formed " , Toast.LENGTH_LONG).show();

        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
