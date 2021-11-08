package com.example.detectcolor;


public class PixelObject implements Comparable<PixelObject> {
    public float[] hsv;
    public int pixel;
    public int pixelCount;

    public PixelObject(int pixel, int pixelCount, float[] hsv) {
        this.hsv = hsv;
        this.pixel = pixel;
        this.pixelCount = pixelCount;
    }
    public int compareTo(PixelObject o){
        return o.pixelCount - this.pixelCount;
    }
}
