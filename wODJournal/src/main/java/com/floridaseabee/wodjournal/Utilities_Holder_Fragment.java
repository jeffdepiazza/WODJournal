package com.floridaseabee.wodjournal;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.floridaseabee.wodjournal.DatabaseHelper.create_xml_async;
import com.floridaseabee.wodjournal.DatabaseHelper.restore_xml_async;

public class Utilities_Holder_Fragment extends Fragment implements DatabaseHelper.Utilities_Listener {

	private create_xml_async create_xml_task;
	private restore_xml_async restore_xml_task;
	private Boolean task_running = false;
	private Utilities_Dialog_Listener listener;
	private ProgressDialog dialog_to_save = null;

	public static Utilities_Holder_Fragment newInstance() {

		Utilities_Holder_Fragment f = new Utilities_Holder_Fragment();
		return f;

	}

	interface Dialog_Dismiss {
		public void dismiss_dialog();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// We are saving the instance so it not only holds onto the AsyncTask
		// to get our data out of the database, but we are also using it to hold
		// on to our data member that we are passing to the activity. This
		// allows us to save it during rotation and then pass it back to the
		// activity
		// during its onResume to reset the state of the Activity.
	
		// It also looks like it is holding onto the listeners as well, as the
		// onCreate isn't called again upon rotation and the restting of the listeners
		// does not occur.

		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		Log.v("utility holder fragment", "onCreate");
		if (task_running != null) {
			Log.v("utility holder fragment", "task running");
			if (create_xml_task != null) {
				Log.v("utility holder fragment", "resetting  create xml listener via on create");
				create_xml_task.reset_listener(this);
			} else if (restore_xml_task != null) {
				Log.v("utility holder fragment", "resetting restore xml listener via on create");
				restore_xml_task.reset_listener(this);
			}
		}
	}

	public void backup_to_xml(Context ctxt, String DATASUBDIRECTORY, String EXPORT_FILE_NAME) {
		task_running = true;
		DatabaseHelper.getInstance(getActivity()).createXmlAsync(this, ctxt, DATASUBDIRECTORY, EXPORT_FILE_NAME);
	}

	public void backup_to_file(Context ctxt, String DATASUBDIRECTORY, String EXPORT_FILE_NAME) {
		task_running = true;
		DatabaseHelper.getInstance(getActivity()).createFileAsync(this, ctxt, DATASUBDIRECTORY, EXPORT_FILE_NAME);
	}

	public void restore_from_xml(Context ctxt, String filename) {
		task_running = true;
		DatabaseHelper.getInstance(getActivity()).restoreXmlAsync(this, ctxt, filename);
	}

	public void restore_from_file(Context ctxt, String filename) {
		task_running = true;
		DatabaseHelper.getInstance(getActivity()).restoreFileAsync(this, ctxt, filename);
	}

	/**
	 * taken out of the EMPUB lite as it allows the ability to execute in
	 * parallel for different API versions . while we are not going on anything
	 * else but Jellybean, i left this in there as it should help out with
	 * keeping with keeping parallel tasks in addition
	 * 
	 * @param task
	 * @param params
	 */
	static public <T> void executeAsyncTask(AsyncTask<T, ?, ?> task, T... params) {
		if (Build.VERSION.SDK_INT > +Build.VERSION_CODES.HONEYCOMB) {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);

		} else {
			task.execute(params);
		}
	}

	@Override
	public void return_task_complete() {
		if (task_running) {
			task_running = false;
			Log.v("utility holder fragment", "async done, going back to activity ");
			listener.dismiss_progress_dialog();
		}
	}

	@Override
	public void return_progress(Integer progress) {
		//Log.v("Utilities Holder - return progress" , "returning " + progress + "%");
		listener.update_dialog(progress);
		// TODO Auto-generated method stub

	}

	@Override
	public void return_create_xml_asynctask(create_xml_async mytask) {
		Log.v("utility holder fragment", "getting a hook into the async task");
		this.create_xml_task = mytask;
	}

	public void return_restore_xml_asynctask(restore_xml_async mytask) {
		Log.v("utility holder fragment", "getting a hook into the async task");
		this.restore_xml_task = mytask;
	}

	public void setListener(Utilities_Dialog_Listener listener) {
		this.listener = listener;
	}

	public void reset_xml_export_asynctask_listener() {
		Log.v("utility holder fragment", "resetting xml listener");
		create_xml_task.reset_listener(this);
	}

	public void reset_xml_import_asynctask_listener() {
		Log.v("utility holder fragment", "resetting xml listener");
		restore_xml_task.reset_listener(this);
	}

	public void set_progress_dialog(ProgressDialog progress_dialog) {
		dialog_to_save = progress_dialog;
	}
	
	public ProgressDialog get_progress_dialog() {
		return dialog_to_save;
	}
	
	
	public void remove_progress_dialog() {
		dialog_to_save = null;	
	}
}
