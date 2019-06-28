package com.automobile.service.comman;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewOpenSansReguler extends TextView {

    public TextViewOpenSansReguler(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode())
            init();
    }

    public TextViewOpenSansReguler(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init();

    }

    public TextViewOpenSansReguler(Context context) {
        super(context);
        if (!isInEditMode())
            init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans-Regular.ttf");
        setTypeface(tf, 0);

    }

}