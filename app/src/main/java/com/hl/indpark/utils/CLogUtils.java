package com.hl.indpark.utils;

import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


 /**
  * Created by yjl on 2021/3/30 15:40
  * Function：
  * Desc：
  */
public class CLogUtils {

	public static void write(Context context, String log) {
		try {
			File file = new File("/mnt/sdcard/" + context.getPackageName() + ".test.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			fw.write(log);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void write(String path, String log) {
		try {
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(path);
			fw.write(log);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void write(String log) {
		write("/mnt/sdcard/test.txt", log);
	}
}
