package com.test.tracking.util;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Formatter;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.test.tracking.uscis.Record;

public class FileUtil {

	public static String readFromFile(String fileName) {
		File file = new File(fileName);
		try {
			if (!file.exists())
				file.createNewFile();
			return FileUtils.readFileToString(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		file = null;
		return null;
	}

	public static void writeFile(String fileName, String text) {
		File file = new File(fileName);
		try {
			if (!file.exists())
				file.createNewFile();
			FileUtils.writeStringToFile(file, text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void appendFile(String fileName, String text) {
		File file = new File(fileName);
		try {
			if (!file.exists())
				file.createNewFile();
			FileUtils.writeStringToFile(file, text, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void appendFile(String fileName, List<Record> recordList) {
		File file = new File(fileName);
		try {
			if (!file.exists())
				file.createNewFile();
			for (Record record : recordList) {
				FileUtils.writeStringToFile(file, formatRecord(record), true);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static String formatRecord(Record record) {
		Formatter formatter = new Formatter();
		String recordStr = formatter
				.format("%-20s %-20s %-10s %s", DateFormat.getDateInstance().format(record.getDate()), record.getCaseNumber(), record.getCaseType(), record.getStatus() + "\r\n")
				.toString();
		formatter.close();
		return recordStr;
	}
	

}
