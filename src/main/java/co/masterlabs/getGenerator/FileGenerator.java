package co.masterlabs.getGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.io.IOException;

public class FileGenerator{
    @SuppressWarnings("unchecked")
    public static void generateFile(TreeMap<String,Object> inputMap, String inputFilePath){
        try{          

            BufferedWriter bWriter = new BufferedWriter(new FileWriter(new File(prepareOutputFilePath(inputFilePath))));
            for(Entry<String,Object> e: inputMap.entrySet()){

                if(e.getKey().equalsIgnoreCase("Constructor")){
                    bWriter.write((String)e.getValue());
                }

                else if(e.getKey().equalsIgnoreCase("Getters") || e.getKey().equalsIgnoreCase("Setters")){
                    for(String s : (List<String>)e.getValue()){
                        bWriter.write(s);
                    }                
                }
            }
            bWriter.close();
        }
        catch(IOException e){
            System.out.println("generateFile: błąd tworzenia pliku.");
        }
    }
     

    private static String prepareOutputFilePath(String inputFilePath){
        int lastSlashLocation = inputFilePath.lastIndexOf('\\');
        String folder = inputFilePath.substring(0,lastSlashLocation);
        System.out.println(folder);
        return folder + "\\wynikGeneracji.java";
    }
}