package com.floridaseabee.wodjournal;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class InsertAddDeleteEditDialogParent extends DialogFragment {
	Button add_entry;
	Button edit_entry;
	Button delete_entry;
	Button insert_entry;
	Button add_movement_entry;
	Parent_Return_Clicks listener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
				
		getDialog().setTitle("WOD Container Options");
		View v = inflater.inflate(R.layout.insert_add_edit_delete_dialog,
				container, false);

        add_movement_entry = v.findViewById(R.id.add_movement_entry);
		add_movement_entry.setVisibility(View.VISIBLE);
		add_movement_entry.setText(getResources().getString(R.string.add_movement_entry_text));
		add_movement_entry.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.v("insert add edit dialog", "add movement entry");
				send_back_add_movement_event();
			}

		});

        add_entry = v.findViewById(R.id.add_entry);
		add_entry.setText(getResources().getString(R.string.add_container));
		add_entry.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.v("insert add edit dialog", "add entry");
				send_back_add_event();
			}

		});
        insert_entry = v.findViewById(R.id.insert_entry);
		insert_entry.setText(getResources()
				.getString(R.string.insert_container));
		insert_entry.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {

				Log.v("insert add edit dialog", "insert entry");
				send_back_insert_event();
			}

		});
        edit_entry = v.findViewById(R.id.edit_entry);
		edit_entry.setText(getResources().getString(R.string.edit_container));
		edit_entry.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {

				Log.v("insert add edit dialog", "edit entry");
				send_back_edit_event();
			}

		});
        delete_entry = v.findViewById(R.id.delete_entry);
		delete_entry.setText(getResources()
				.getString(R.string.delete_container));
		delete_entry.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.v("insert add edit dialog", "delete entry");

				send_back_delete_event();
				dismiss();

			}

		});
		return v;

	}

	interface Parent_Return_Clicks {
        void return_parent_edit();

        void return_parent_delete();

        void return_parent_add();

        void return_parent_insert();

        void return_parent_movement_add();

	}

	public static InsertAddDeleteEditDialogParent newInstance() {
		InsertAddDeleteEditDialogParent f = new InsertAddDeleteEditDialogParent();
		return f;

	}

	public void send_back_add_event() {
		listener.return_parent_add();
	}

	public void send_back_delete_event() {
		listener.return_parent_delete();
	}

	public void send_back_insert_event() {
		listener.return_parent_insert();
	}

	public void send_back_edit_event() {
		listener.return_parent_edit();
	}

	public void send_back_add_movement_event() {
		listener.return_parent_movement_add();
	}
	
	public void setListener(Parent_Return_Clicks listener) {
		this.listener = listener;
		Log.v("parent dialog", "set listener");
	}

}
