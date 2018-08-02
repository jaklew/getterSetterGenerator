package co.masterlabs.getGenerator;

import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.directory.InvalidAttributesException;

public class JavaClassParser{
    private static final String _modifier = "public ";
    private static final String _voidType = "void";
    private static final String _getter   = " get";
    private static final String _setter   = " set";

    private static String generateGetterString(String type, String item){
        StringBuilder getterBuilder = new StringBuilder();
        getterBuilder.append(_modifier);
        getterBuilder.append(type);
        getterBuilder.append(_getter);
        getterBuilder.append(item.substring(0,1).toUpperCase() + item.substring(1,item.length()) + "()"); //getitem() -> getItem() (trzymanie się konwencji nazywania getterów)
        getterBuilder.append("{\n");
        getterBuilder.append(String.format("\treturn this.%s;",item));
        getterBuilder.append("\n}");
        return getterBuilder.toString();
    }

    private static String generateSetterString(String type, String item){
        StringBuilder setterBuilder = new StringBuilder();
        setterBuilder.append(_modifier);
        setterBuilder.append(_voidType);
        setterBuilder.append(_setter);
        setterBuilder.append(item.substring(0,1).toUpperCase() + item.substring(1,item.length()) + String.format("(%s setTo)", type)); //setitem() -> setItem() (trzymanie się konwencji nazywania setterów)
        setterBuilder.append("{\n");
        setterBuilder.append(String.format("\tthis.%s = setTo;",item));
        setterBuilder.append("\n}");
        return setterBuilder.toString();
    }

    private static String generateGetterString(String type, String item, int no){
        StringBuilder getterBuilder = new StringBuilder();
        getterBuilder.append(_modifier);
        getterBuilder.append(type);
        getterBuilder.append(_getter);
        getterBuilder.append(item.substring(0,1).toUpperCase() + item.substring(1,item.length()) + (no+1)); //getlista_itemowNo -> getLista_itemowNo (trzymanie się konwencji nazywania getterów)
        getterBuilder.append("{\n");
        getterBuilder.append(String.format("\treturn %s.get(%d)",item.trim(),no));
        getterBuilder.append("\n}");
        return getterBuilder.toString();
    }

    public static TreeMap<String,Object> parseClassFile(File f) throws InvalidAttributesException{
        if(!f.getName().endsWith(".java")) throw new InvalidAttributesException("Invalid filename");
        
        TreeMap<String,Object> retVal = new TreeMap<String,Object>(); //mapa z polami: Constructor (String), Getters(List<String>), Setters(List<String>) 
        String constructor = "";
        List<String> gettersList = new ArrayList<String>();
        List<String> settersList = new ArrayList<String>();


        Pattern p = Pattern.compile("\\<(.*?)\\>");

        String line;

        String[] splitLine;

        try{
            BufferedReader bReader = new BufferedReader(new FileReader(f));
            while(! ((line = bReader.readLine())==null)){
                
                if(!line.contains("{") && !line.contains("}")){
                    
                    if(line.contains("List")){
                        
                        splitLine = line.split("\\:");
                        int howManyGetters = Integer.valueOf(splitLine[1].substring(1, splitLine[1].length()-1));

                        Matcher m = p.matcher(splitLine[0]);
                        if(m.find()){
                            splitLine = splitLine[0].split(String.format("\\<%s\\>",m.group(1)));
                            for(int i = 0; i < howManyGetters; i++){
                                gettersList.add(generateGetterString(m.group(1), splitLine[1].trim(), i));
                            }
                            constructor += String.format("\t%s = new List<%s>();\n", splitLine[1], m.group(1));
                        }
                        else{
                            bReader.close();
                            throw new InvalidAttributesException("List type not specified");
                        }
                        
                    }
                    else if (line.contains("class")){
                        line = line.trim();
                        splitLine = line.split("\\s");
                        try{
                            String className = splitLine[2];
                            constructor = "public " + className + "(Document doc){\n";
                        }   
                        catch(Exception e){
                            System.out.println("Blad w generacji konstruktora");
                            e.printStackTrace();
                            System.out.println("Koniec komunikatu o bledzie.");
                        }
                    }

                    else{
                        
                        line = line.trim();
                        splitLine = line.split("\\s");

                        if(splitLine.length == 2){
                            int l = splitLine[1].trim().length()-1;
                            splitLine[1] = splitLine[1].substring(0, l);
                            gettersList.add(generateGetterString(splitLine[0], splitLine[1]));
                            settersList.add(generateSetterString(splitLine[0], splitLine[1]));
                            constructor += String.format("\tthis.%s = doc.getItemValue%s(\"%s\");", splitLine[1], getItemValueTypeString(splitLine[0]), splitLine[1]);
                        }
                        
                    }
                }


            }
            constructor += "}";
            retVal.put("Constructor",constructor);
            retVal.put("Getters",gettersList);
            retVal.put("Setters", settersList);
            bReader.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return retVal;
    }

    public static void generujPlik(List<String> inputList, String outputFileLocation){

        try{
            File outputFile = new File(outputFileLocation);
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

    private static String getItemValueTypeString(String typeString){
        if(typeString.equalsIgnoreCase("int")) return "Integer";
        else if(typeString.equalsIgnoreCase("double")) return "Double";
        else if(typeString.equalsIgnoreCase("String")) return typeString;
        return "";
    }
}