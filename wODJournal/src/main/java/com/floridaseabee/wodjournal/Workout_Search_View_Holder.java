package com.floridaseabee.wodjournal;

import android.view.View;
import android.widget.TextView;

public class Workout_Search_View_Holder {

	TextView bigger_text;
	TextView smaller_text;
	TextView date;
	TextView medium_text;

	Workout_Search_View_Holder(View row) {

        this.bigger_text = row.findViewById(R.id.search_bigger_text);
        this.smaller_text = row.findViewById(R.id.search_smaller_text);
        this.date = row.findViewById(R.id.search_date);
        this.medium_text = row.findViewById(R.id.search_medium_text);

	}
}
