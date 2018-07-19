package co.masterlabs.getGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class FileGenerator{
    public static void generateFile(List<String> inputList, String inputFilePath){

        try{
            File outputFile = new File(prepareOutputFilePath(inputFilePath));
            BufferedWriter bWriter = new BufferedWriter(new FileWriter(outputFile));
            for(String s : inputList){
                bWriter.write(s + "\n");
            }
            bWriter.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private static String prepareOutputFilePath(String inputFilePath){
        int lastSlashLocation = inputFilePath.lastIndexOf('/');
        String folder = inputFilePath.substring(0,lastSlashLocation);

        return folder + "wynikGeneracji.java";
    }
}