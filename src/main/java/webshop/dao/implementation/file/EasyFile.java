package webshop.dao.implementation.file;

import org.json.JSONArray;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class EasyFile{
    public static List<List<String>> importFromCSV(String filePath){
        if (filePath == null || filePath.equals("")) throw new IllegalArgumentException("You have to give filePath!");

        return csvToList(loadFromFile(filePath));
    }
    public static void exportToCSV(String filePath, List<List<String>> data){
        if (filePath == null || filePath.equals("")) throw new IllegalArgumentException("You have to give filePath!");

        writeToFile(filePath, listToCsv(data));
    }
    public static void exportToCSV(String filePath, List<List<String>> data, boolean append){
        if (filePath == null || filePath.equals("")) throw new IllegalArgumentException("You have to give filePath!");

        if (append) appendToFile(filePath, listToCsv(data));
        else writeToFile(filePath, listToCsv(data));
    }

    public static JSONArray importFromJSON(String filePath){
        if (filePath == null || filePath.equals("")) throw new IllegalArgumentException("You have to give filePath!");

        return new JSONArray(loadFromFile(filePath).findFirst().orElse("[]"));
    }
    public static void exportToJSON(String filePath, JSONArray data){
        if (filePath == null || filePath.equals("")) throw new IllegalArgumentException("You have to give filePath!");

        writeToFile(filePath, data.toString());
    }

    public static Stream<String> loadFromFile(String filePath){
        try{
            return Files.lines(Paths.get(filePath));
        }catch (IOException e){
            createNewEmptyFile(filePath);
        }
        return Stream.empty();
    }
    public static void writeToFile(String filePath, String data){
        try{
            Files.write(Paths.get(filePath), data.getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void appendToFile(String filePath, String data){
        try{
            Files.write(Paths.get(filePath), data.getBytes(),
                    Files.exists(Paths.get(filePath))
                            ? StandardOpenOption.APPEND
                            : StandardOpenOption.CREATE
            );
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void createNewEmptyFile(String filePath){
        try{
            Files.write(Paths.get(filePath), "".getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void deleteFile(String filePath){
        try{
            Files.deleteIfExists(Paths.get(filePath));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static String listToCsv(List<List<String>> data){
        return data.stream()
                .map(EasyFile::encodeCsvLine)
                .collect(Collectors.joining("\n"))
                .concat("\n");
    }
    private static List<List<String>> csvToList(Stream<String> data){
        return data
                .filter(line -> !line.equals(""))
                .map(EasyFile::decodeCsvLine)
                .collect(Collectors.toList());
    }

    private static String encodeCsvLine(List<String> values){
        return values.stream()
                .map(string -> string.contains("\"") || string.contains(",")
                        ? "\"" + string.replaceAll("\"", "\"\"") + "\""
                        : string)
                .collect(Collectors.joining(","));
    }

    private static List<String> decodeCsvLine(String csvLine){
        boolean inBlock = false;
        List<String> values = new ArrayList<>();
        StringBuilder value = new StringBuilder();

        for (int i = 0; i < csvLine.length(); i++){
            if (csvLine.charAt(i) == '"'){
                if (inBlock && i + 1 < csvLine.length() && csvLine.charAt(i + 1) == '"'){
                    value.append("\"");
                    i++;
                }else{
                    inBlock = !inBlock;
                }
            }else if (csvLine.charAt(i) == ',' && !inBlock){
                values.add(value.toString());
                value = new StringBuilder();
            }else{
                value.append(csvLine.charAt(i));
            }
        }
        if (!value.equals("")) values.add(value.toString());

        return values;
    }
}