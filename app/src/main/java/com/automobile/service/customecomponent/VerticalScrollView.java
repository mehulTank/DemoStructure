package com.automobile.service.customecomponent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class VerticalScrollView extends ScrollView
{
   private GestureDetector mGestureDetector;
    OnTouchListener mGestureListener;

    public VerticalScrollView(Context context)
    {
        super(context);
        init(context);
    }

    public VerticalScrollView(Context context, AttributeSet attrs)
    {
       super(context, attrs);
       init(context);
    }

    public VerticalScrollView(Context context, AttributeSet attrs, int defStyle) 
    {
       super(context, attrs, defStyle);
       init(context);
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
       return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }

    class YScrollDetector extends GestureDetector.SimpleOnGestureListener
    {
       @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) 
       {             
          return Math.abs(distanceY) > Math.abs(distanceX);
        }
    }

    private void init(Context context)
    {
       mGestureDetector = new GestureDetector(context, new YScrollDetector());
        setFadingEdgeLength(0);
   }
}