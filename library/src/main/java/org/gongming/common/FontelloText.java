package org.gongming.common;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontelloText extends TextView {

    public FontelloText(Context context) {
        super(context);
        init(context);
    }

    public FontelloText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FontelloText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fontello.ttf");
        setTypeface(font);
    }
}
