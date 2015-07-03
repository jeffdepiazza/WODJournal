package com.floridaseabee.wodjournal;

import android.view.View;
import android.widget.TextView;

public class Personal_Record_View_Holder {
	TextView movement;
	TextView weight;
	TextView weight_units;
	TextView date;
	TextView comments;
	
	Personal_Record_View_Holder(View row) {
		this.movement = (TextView) row.findViewById(R.id.pr_movement);
		this.weight = (TextView) row.findViewById(R.id.pr_weight);
		this.weight_units = (TextView) row.findViewById(R.id.pr_weight_units);
		this.date = (TextView) row.findViewById(R.id.pr_date);
		this.comments= (TextView) row.findViewById(R.id.pr_comments);
		
	}
}
