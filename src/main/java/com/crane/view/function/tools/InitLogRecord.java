package com.crane.view.function.tools;

import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class InitLogRecord {
    public static void initLog() {
        FileInputStream fileInputStream = null;
        try {
            Properties properties = new Properties();
            fileInputStream = (FileInputStream) PathTool.getResources2InputStream("log4j.properties");
            properties.load(fileInputStream);
            PropertyConfigurator.configure(properties);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
