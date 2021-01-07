package com.hunchback.noahchristmas;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import androidx.arch.core.util.Function;
import static com.hunchback.noahchristmas.Functions.*;

public enum RotateType {
    UPPER_LEFT(LEFT, TOP),
    UPPER_RIGHT(RIGHT, TOP),
    BOTTOM_LEFT(LEFT, BOTTOM),
    BOTTOM_RIGHT(RIGHT, BOTTOM);

    private final Function<Bitmap, Integer> xFunction;
    private final Function<Bitmap, Integer> yFunction;

    private RotateType(Function<Bitmap, Integer> xFunction, Function<Bitmap, Integer> yFunction) {
        this.xFunction = xFunction;
        this.yFunction = yFunction;
    }

    public Matrix rotate(Bitmap bmp, int degrees) {
        Matrix transform = new Matrix();
        transform.postRotate(degrees, xFunction.apply(bmp), yFunction.apply(bmp));
        return transform;
    }

    public Matrix rotateAt(Bitmap bmp, int canvasX, int canvasY, int degrees) {
        Matrix transform = rotate(bmp, degrees);
        transform.postTranslate(canvasX, canvasY);
        return transform;
    }
}
