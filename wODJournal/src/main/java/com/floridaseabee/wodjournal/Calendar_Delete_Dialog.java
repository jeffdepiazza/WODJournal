package com.floridaseabee.wodjournal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Dixie Normus on 7/5/2015.
 */
public class Calendar_Delete_Dialog  extends DialogFragment implements
        DialogInterface.OnClickListener {

    private static final String Year = "year";
    private static final String Month = "month";
    private static final String Day = "day";
    private Integer year;
    private Integer month;
    private Integer day;
    private TextView delete_verify_text;
    private Calendar_Delete_dialog_Listener listener;

    static Calendar_Delete_Dialog newInstance(Integer year, Integer month, Integer day) {

        Calendar_Delete_Dialog frag = new Calendar_Delete_Dialog();
        Bundle args = new Bundle();
        args.putInt(Year, year);
        args.putInt(Month, month);
        args.putInt(Day, day);
        frag.setArguments(args);
        return frag;
    }

    interface Calendar_Delete_dialog_Listener {
        void head_back_to_activity();
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = getActivity().getLayoutInflater().inflate(R.layout.calendar_delete_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        year = getArguments().getInt(Year, 0);
        month = getArguments().getInt(Month, 0);
        day = getArguments().getInt(Day, 0);
        delete_verify_text = v.findViewById(R.id.calendar_delete_dialog_textview);
        delete_verify_text.setText(getResources().getText(R.string.calendar_delete_dialog_text) + " " + day + "-" + get_month(month) + "-" + year + "?");

        return (builder.setTitle("Delete Day?").setView(v).setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel, null).create());

    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        listener.head_back_to_activity();
    }

    public void setListener(WOD_Calendar_Activity listener) {
        this.listener = listener;
    }

    public String get_month(Integer month) {
        switch (month) {

            case 1:
                return "JAN";
            case 2:
                return "FEB";
            case 3:
                return "MAR";
            case 4:
                return "APR";
            case 5:
                return "MAY";
            case 6:
                return "JUN";
            case 7:
                return "JUL";
            case 8:
                return "AUG";
            case 9:
                return "SEP";
            case 10:
                return "OCT";
            case 11:
                return "NOV";
            case 12:
                return "DEC";

        }
        return "XXX";
    }

}
