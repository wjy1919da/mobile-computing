package edu.coass.utils;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils {
    public static String readFileByLines(AssetManager assetManager,String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        String tempString = null;
        try {

            InputStreamReader isr = new InputStreamReader(assetManager.open(fileName),"UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while((line = br.readLine()) != null){
                builder.append(line);
            }
            tempString = builder.toString();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return tempString ;
    }


}
