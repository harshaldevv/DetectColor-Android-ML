package com.example.detectcolor;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ColorFinder {

    public static ArrayList<PixelObject> allColorList = new ArrayList<>();

    private CallbackInterface callback;

    public ColorFinder(CallbackInterface callback) {
        this.callback = callback;
    }

    /* main function which will be called from main class */
    public void findDominantColor(Bitmap bitmap) {
        /* find dominant color */
        new GetDominantColor().execute(bitmap);
    }

    private int getDominantColorFromBitmap(Bitmap bitmap) {
        int [] pixels = new int[bitmap.getWidth()*bitmap.getHeight()];
        bitmap.getPixels(pixels,0,bitmap.getWidth(),0,0, bitmap.getWidth(), bitmap.getHeight());

        /*
         pixelList is the map containing key as pixel and value as PixelObject
         It stores the frequency of each pixel.
        */
        Map<float[], PixelObject> pixelList = new HashMap<>();
        for (int pixel : pixels) {
            float[] hsv = new float[3];
            Color.colorToHSV(pixel, hsv);
            if (pixelList.containsKey(hsv)) {
                pixelList.get(hsv).pixelCount++;
            } else {
                pixelList.put(hsv, new PixelObject(pixel, 1, hsv));
            }
        }

        /* allColourList contains the unique pixels in the picture */
        allColorList = new ArrayList<>();
        for(float[] pixelhsv: pixelList.keySet()){
            allColorList.add(pixelList.get(pixelhsv));
        }

        /* returns the pixel with the maximum frequency in the pixelList map */
        return getDominantPixel(pixelList);
    }

    private int getDominantPixel(Map<float[], PixelObject> pixelList) {
        int dominantColor = 0;
        int largestCount = 0;
        for (Map.Entry<float[], PixelObject> entry : pixelList.entrySet()) {
            PixelObject pixelObject = entry.getValue();
            if (pixelObject.pixelCount > largestCount) {
                largestCount = pixelObject.pixelCount;
                dominantColor = pixelObject.pixel;
            }
        }
        return dominantColor;
    }

    private class GetDominantColor extends AsyncTask<Bitmap, Integer, Integer> {
        @Override
        protected Integer doInBackground(Bitmap... params) {
            int dominantColor = getDominantColorFromBitmap(params[0]);
            return dominantColor;
        }

        @Override
        protected void onPostExecute(Integer dominantColor) {
            String hexColor = colorToHex(dominantColor);
            if (callback != null) {
                callback.onCompleted(hexColor);
            }
        }

        private String colorToHex(int color) {
            return String.format("#%06X", (0xFFFFFF & color));
        }
    }

    public interface CallbackInterface {
        public void onCompleted(String color);
    }

}
