package com.automobile.service.comman;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class EdittextOpenSanssemibold extends android.support.v7.widget.AppCompatEditText {

    public EdittextOpenSanssemibold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode())
            init();
    }

    public EdittextOpenSanssemibold(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init();

    }

    public EdittextOpenSanssemibold(Context context) {
        super(context);
        if (!isInEditMode())
            init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans-Semibold.ttf");
        setTypeface(tf, 0);

    }

}