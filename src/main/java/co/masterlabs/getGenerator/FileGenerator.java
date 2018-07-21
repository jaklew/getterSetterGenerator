package co.masterlabs.getGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;

public class FileGenerator{
    public static void generateFile(Map<String,Object> inputList, String inputFilePath){

    }

    private static String prepareOutputFilePath(String inputFilePath){
        int lastSlashLocation = inputFilePath.lastIndexOf('\\');
        String folder = inputFilePath.substring(0,lastSlashLocation);
        System.out.println(folder);
        return folder + "\\wynikGeneracji.java";
    }
}