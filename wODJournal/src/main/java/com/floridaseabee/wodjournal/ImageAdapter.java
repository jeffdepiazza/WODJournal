package com.floridaseabee.wodjournal;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private Integer[] mThumbIds = {R.drawable.ic_timer, R.drawable.ic_analytics, R.drawable.ic_calendar_light, R.drawable.ic_prs, R.drawable.ic_search, R.drawable.ic_launcher
    };
    
    private String[] Caption = {"Timers", "Analytics", "WOD Calendar", "Personal Records", "Workout Search", "Expansion2"
    };
    public ImageAdapter(Context c) {
        mContext = c;
    }

    @Override
	public int getCount() {
        return mThumbIds.length;
    }

    @Override
	public Object getItem(int position) {
        return null;
    }

    @Override
	public long getItemId(int position) {
        return 0;
    }

    @Override
	public View getView(int position, View convertView, ViewGroup parent) {


        View myView  = null;

        if(convertView==null)
        {
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            myView = li.inflate(R.layout.grid_cell_items, null);
        }else{
            myView = convertView;
        }


        TextView tv = myView.findViewById(R.id.grid_item_text);
        /*Log.v("D:<",String.valueOf(Caption.length) +" y: "+ String.valueOf(position)); */
        tv.setText(Caption[position]);

        ImageView iv = myView.findViewById(R.id.grid_item_image);
        /*Log.v("D:<",String.valueOf(mThumbIds.length) +" y: "+ String.valueOf(position)); */
        iv.setImageResource(mThumbIds[position]);

        return myView;
    }
}
