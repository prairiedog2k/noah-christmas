package com.hunchback.noahchristmas;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameObject {
    // Velocity of game character (pixel/millisecond)
    public static final float VELOCITY = 0.1f;
    public static int SLUSH = 100;

    private final Bitmap image;
    private int x;
    private int y;
    private final int width;
    private final int height;


    private long lastDrawNanoTime =-1;
    private GameSurface gameSurface;


    public GameObject(GameSurface gameSurface, Bitmap image, int x, int y) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.gameSurface= gameSurface;
    }

    public void update()  {

    }

    public void draw(Canvas canvas)  {
        canvas.drawBitmap(image, x, y, null);
        // Last draw time.
        this.lastDrawNanoTime= System.nanoTime();
    }
}
