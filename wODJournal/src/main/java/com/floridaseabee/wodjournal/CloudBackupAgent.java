package com.floridaseabee.wodjournal;

import java.io.File;

import android.app.backup.BackupAgentHelper;
import android.app.backup.FileBackupHelper;

public class CloudBackupAgent extends BackupAgentHelper {

	private static final String DB_NAME = "WODs.db";

	@Override
	public void onCreate() {
		FileBackupHelper dbs = new FileBackupHelper(this, DB_NAME);
		addHelper("dbs", dbs);
	}

	@Override
	public File getFilesDir() {
		File path = getDatabasePath(DB_NAME);
		return path.getParentFile();
	}
}
