package com.roomorama.caldroid;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class SquareTextView extends TextView {

	int myheight = 0;

	public SquareTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public SquareTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public SquareTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (myheight == 0) {
			myheight = CaldroidFragment.getheights();
		}
		if (myheight == 0) {
			//Log.v ("onMeasure status","my height is still 0");
			myheight = 600;
		}
		this.setMeasuredDimension(widthMeasureSpec, myheight / 6);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// Log.v("my current width", "" + w);
		// Log.v("my current height", "" + h);
		// Log.v("my old width", "" + oldw);
		// Log.v("my old height", "" + oldh);
		//if (myheight == 0) {
		//Log.v ("status","my height is 0");
		myheight = CaldroidFragment.getheights();
		//}
		//Log.v ("onSizeChanged status","my height is now" + myheight);
		if (myheight == 0) {
			//Log.v ("onSizeChanged status","my height is still 0");
			myheight = 600;
		}
		super.onSizeChanged(w, myheight / 6, oldw, oldh);
	}
}
