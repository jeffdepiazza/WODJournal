package com.floridaseabee.wodjournal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class Utilities_File_Picker_Dialog extends DialogFragment implements AdapterView.OnItemSelectedListener, OnClickListener {

	private static final String Title = "Title";
	private static final String Directory = "Directory";
	private static final String Type_Of_Dialog = "type_of_dialog";
	private TextView directory_view;
	private EditText filename;
	private Send_back_clicks listener;

	static Utilities_File_Picker_Dialog newInstance(String title, String directory, String type_of_dialog) {

		Utilities_File_Picker_Dialog frag = new Utilities_File_Picker_Dialog();
		Bundle args = new Bundle();
		args.putString(Title, title);
		args.putString(Directory, directory);
		args.putString(Type_Of_Dialog, type_of_dialog);
		frag.setArguments(args);
		return frag;

	}

	interface Send_back_clicks {
        void send_back_ok(String filename);

        void send_back_cancel();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		View v = getActivity().getLayoutInflater().inflate(R.layout.file_name_dialog, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		Calendar currentdate = Calendar.getInstance();

        directory_view = v.findViewById(R.id.utilities_directory);
        filename = v.findViewById(R.id.utilities_file_name);

		if (getArguments().getString(Type_Of_Dialog, "") == "xml")
			filename.setText(getResources().getString(R.string.backup_filename_beginning) + currentdate.get(Calendar.YEAR) + "-"
					+ (currentdate.get(Calendar.MONTH) +1 ) + "-" + currentdate.get(Calendar.DAY_OF_MONTH) + " " + currentdate.get(Calendar.HOUR) + ":"
					+ currentdate.get(Calendar.MINUTE) + ":" + currentdate.get(Calendar.SECOND) + ".xml");
		else
			filename.setText(getResources().getString(R.string.backup_filename_beginning) + currentdate.get(Calendar.YEAR) + "-"
					+ (currentdate.get(Calendar.MONTH) + 1) + "-" + currentdate.get(Calendar.DAY_OF_MONTH) + " " + " " + currentdate.get(Calendar.HOUR) + ":"
					+ currentdate.get(Calendar.MINUTE) + ":" + currentdate.get(Calendar.SECOND) + ".WJbak");

		directory_view.setText(getResources().getString(R.string.filelocation) + getArguments().getString(Directory));

		return (builder.setTitle(getArguments().getString(Title, "")).setView(v).setPositiveButton(android.R.string.ok, this)
				.setNegativeButton(android.R.string.cancel, null).create());

		// getResources().getString(R.string.xml_dialog_title)
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		dismiss();
		listener.send_back_ok(filename.getText().toString());

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		listener.send_back_cancel();

	}

	public void setListener(Utilities_Activity listener) {
		this.listener = listener;
	}
}
