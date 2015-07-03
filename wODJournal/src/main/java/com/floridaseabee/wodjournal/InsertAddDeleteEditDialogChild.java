package com.floridaseabee.wodjournal;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class InsertAddDeleteEditDialogChild extends DialogFragment implements
		OnClickListener {
	Button add_entry;
	Button edit_entry;
	Button delete_entry;
	Button insert_entry;
	Child_Return_Clicks listener;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.insert_add_edit_delete_dialog,
				container, false);
		getDialog().setTitle("Movement Options");
		add_entry = (Button) v.findViewById(R.id.add_entry);
		add_entry.setText(getResources().getString(R.string.add_movement));
		add_entry.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				send_back_add_event();
				Log.v("insert add edit dialog child", "add entry");
			
			}
		});
		
		insert_entry = (Button) v.findViewById(R.id.insert_entry);
		insert_entry.setText(getResources().getString(R.string.insert_movement));
		insert_entry.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				send_back_insert_event();
				Log.v("insert add edit dialog child", "insert entry");
			}
		});
		edit_entry = (Button) v.findViewById(R.id.edit_entry);
		edit_entry.setText(getResources().getString(R.string.edit_movement));
		edit_entry.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				send_back_edit_event();
				Log.v("insert add edit dialog child", "edit entry");
			}

		});
		delete_entry = (Button) v.findViewById(R.id.delete_entry);
		delete_entry.setText(getResources().getString(R.string.delete_movement));
		delete_entry.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.v("insert add edit dialog child", "delete entry");
				send_back_delete_event();
				dismiss();
			}

		});
		return v;

	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub

	}

	interface Child_Return_Clicks {
		public void return_child_edit();

		public void return_child_delete();

		public void return_child_add();
		
		public void return_child_insert();

	}

	public static InsertAddDeleteEditDialogChild newInstance() {
		InsertAddDeleteEditDialogChild f = new InsertAddDeleteEditDialogChild();
		return f;
	}

	public void send_back_add_event() {
		listener.return_child_add();
	}

	public void send_back_insert_event() {
		listener.return_child_insert();
	}
	
	public void send_back_delete_event() {
		listener.return_child_delete();
	}
	
	public void send_back_edit_event() {
		listener.return_child_edit();
	}
	public void setListener(Child_Return_Clicks listener) {
		
		this.listener = listener;
		Log.v("child dialog", "set listener");
	}
}
