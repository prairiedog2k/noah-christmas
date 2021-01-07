package com.hunchback.noahchristmas;

import android.graphics.Bitmap;
import androidx.arch.core.util.Function;

public class Functions {
    public static final Function<Bitmap, Integer> LEFT = new Function<Bitmap, Integer>() {
        @Override
        public Integer apply(Bitmap input) {
            return 0;
        }
    };

    public static final Function<Bitmap, Integer> TOP = LEFT;

    public static final Function<Bitmap, Integer> RIGHT = new Function<Bitmap, Integer>() {
        @Override
        public Integer apply(Bitmap input) {
            return input.getWidth();
        }
    };

    public static final Function<Bitmap, Integer> BOTTOM = new Function<Bitmap, Integer>() {
        @Override
        public Integer apply(Bitmap input) {
            return input.getHeight();
        }
    };
}
