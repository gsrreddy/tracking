package com.test.tracking.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class FileUtil {
	
	
    
    public static String readFromFile(String fileName) {
        File file = new File(fileName);
        try {
            if(!file.exists())
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
            if(!file.exists())
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
            if(!file.exists())
                file.createNewFile();
            FileUtils.writeStringToFile(file, text, true);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    



}
