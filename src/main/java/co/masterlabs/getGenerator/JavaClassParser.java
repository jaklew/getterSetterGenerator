package co.masterlabs.getGenerator;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.directory.InvalidAttributesException;

public class JavaClassParser{
    private static final String _modifier = "public ";
    private static final String _getter   = " get";
    private static final String _setter   = " set";

    private static String generateGetterString(String type, String item){
        StringBuilder getterBuilder = new StringBuilder();
        getterBuilder.append(_modifier);
        getterBuilder.append(type);
        getterBuilder.append(_getter);
        getterBuilder.append(item.substring(0,2).toUpperCase() + item.substring(2,item.length()) + "()"); //getitem() -> getItem() (trzymanie się konwencji nazywania getterów)
        getterBuilder.append("{\n");
        getterBuilder.append(String.format("\treturn %s;",item));
        getterBuilder.append("\n}");
        return getterBuilder.toString();
    }

    private static String generateSetterString(String type, String item){
        StringBuilder getterBuilder = new StringBuilder();
        getterBuilder.append(_modifier);
        getterBuilder.append("void");
        getterBuilder.append(_setter);
        getterBuilder.append(item.substring(0,1).toUpperCase() + item.substring(1,item.length()) + String.format("(%s setTo)", type)); //setitem() -> setItem() (trzymanie się konwencji nazywania setterów)
        getterBuilder.append("{\n");
        getterBuilder.append(String.format("\tthis.%s = setTo;",item));
        getterBuilder.append("\n}");
        return getterBuilder.toString();
    }

    private static String generateGetterString(String type, String item, int no){
        StringBuilder getterBuilder = new StringBuilder();
        getterBuilder.append(_modifier);
        getterBuilder.append(type);
        getterBuilder.append(_getter);
        getterBuilder.append(item.substring(0,1).toUpperCase() + item.substring(1,item.length()) + no); //getlista_itemowNo -> getLista_itemowNo (trzymanie się konwencji nazywania getterów)
        getterBuilder.append("{\n");
        getterBuilder.append(String.format("\treturn %s.get(%d)",item.trim(),no));
        getterBuilder.append("\n}");
        return getterBuilder.toString();
    }

    public static List<String> parseClassFile(File f) throws InvalidAttributesException{
        if(!f.getName().endsWith(".java")) throw new InvalidAttributesException("Invalid filename");
        
        List<String> retVal = new ArrayList<String>();
        
        Pattern p = Pattern.compile("\\<(.*?)\\>");

        String line;
        String[] splitLine;

        try{
            BufferedReader bReader = new BufferedReader(new FileReader(f));
            while(! ((line = bReader.readLine())==null)){
                
                if(!line.contains("{") && !line.contains("}")){
                    
                    if(line.contains("List")){
                        
                        splitLine = line.split("\\:");
                        int howManyGetters = Integer.valueOf(splitLine[1].substring(1, 2));

                        Matcher m = p.matcher(splitLine[0]);
                        if(m.find()){
                            splitLine = splitLine[0].split(String.format("\\<%s\\>",m.group(1)));
                            for(int i = 0; i < howManyGetters; i++){
                                retVal.add(generateGetterString(m.group(1), splitLine[1].trim(), i));
                            }
                        }
                        else{
                            bReader.close();
                            throw new InvalidAttributesException("List type not specified");
                        } 
                    }
                    else{
                        int l = line.trim().length()-1;
                        System.out.println(l);
                        line = line.trim().substring(0,l);
                        System.out.println(line);
                        splitLine = line.split(" ");
                        retVal.add(generateGetterString(splitLine[0], splitLine[1]));
                        retVal.add(generateSetterString(splitLine[0], splitLine[1]));
                    }
                }
            }
            bReader.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return retVal;
    }

    public static File generujPlik(List<String> inputList){
        Map<String, String> envMap = System.getenv();
            for(String s : inputList){
                System.out.println(s + "\n");
            }
        return null;
    }
}