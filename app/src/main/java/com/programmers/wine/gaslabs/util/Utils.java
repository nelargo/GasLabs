package com.programmers.wine.gaslabs.util;


import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.Button;

public class Utils {

    /**
     * This method set programmatically the color filter to a button.
     *
     * @param context Context to can access to resources.
     * @param btn     View of the button.
     * @param colorId Resource color id (id in file colors.xml)
     * @param shapeId Resource of the shape (can be a selector, id in drawable folder)
     */
    public static void setColorButton(Context context, Button btn, int colorId, int shapeId) {
        Drawable drawable = ContextCompat.getDrawable(context, shapeId);
        int color = ContextCompat.getColor(context, colorId);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        btn.setBackground(drawable);
    }

}
