package sbdgray;

import java.io.*;
import java.util.*;

public class SBD {

    static String[] eksekusiQuery (String[] query, String[][] dictionary){
        //Mengeksekusi query
        
        String[] result;   
        String lastword = query[query.length - 1];
        char semicolon = lastword.charAt(lastword.length() - 1);
        lastword = lastword.substring(0,lastword.length()-1);
        if(query[0].equals("select")){
            if(query[2].equals("from")){
                if(semicolon == ';'){                                        
                    String[] columnNames = cariTable(dictionary, lastword);
                    result = new String[columnNames.length-1];
                    if(query[1].equals("*")){
                        result = columnNames;
                    }else{                        
                        String[] columns = getKolom(columnNames, pemisah(query[1], ","));                          
                        result = new String[columns.length+1];
                        result[0] = columnNames[0];
                        for (int i = 0; i < columns.length; i++) {
                            result[i+1] = columns[i];                             
                        }
                    }
                }else{
                  result = new String[1];
                  result[0] = "SQL Error (Missing ;)";
                }
            }else{
                result = new String[1];
                result[0]= "SQL Error (Syntax Error)";
            }
        }
        else{
            result = new String[1];
            result[0]= "SQL Error (Syntax Error)";
        }            
        return result;
    }
    
    static String[] getKolom (String[] columnNames, String[] queryColumnNames){
        //Mencari kolom pada query di dalam tabel
        
        String[] result = new String[queryColumnNames.length];
        int j = 0;        
        for (int i = 0; i < queryColumnNames.length; i++) {
            j = 0;
            while(j <= columnNames.length){
                if(j < columnNames.length){
                    if(queryColumnNames[i].equals(columnNames[j])){
                        result[i] = columnNames[j];
                        j = columnNames.length+1;
                    }
                    else
                        j++;
                }else{
                    result = new String[1];
                    result[0]= "SQL Error (Syntax Error)";
                    j++;
                }
            }
        }        
        return result;
    }
    
    static String[] cariTable (String[][] dictionary, String tableName){
        // Mencari nama tabel dan nama-nama kolom tabel pada query di dictionary
                
        String[] output = {"Data Tidak Ditemukan"};                 
        for (int i = 0; i < dictionary.length; i++) {
            if(dictionary[i][0].equals(tableName)){
                output = new String[dictionary[i].length];
                for (int j = 0; j < dictionary[i].length; j++) {
                    output[j] = dictionary[i][j];
                }
            }
        }
        return output;
    }
    
    static String[] pemisah (String kalimat, String separator){
        // Memisah setiap kata menggunakan pemisah tertentu        
        
        String[] kata = kalimat.split(separator);
        return kata;
    }
    
    static String[][] konversiKeArray (File file) throws FileNotFoundException{
        //Mengubah text didalam file menjadi array String
        
        Scanner sc = new Scanner(file);
        String[][] dictionary = new String[3][];        
        int i = 0;        
        while(sc.hasNextLine()){
            dictionary[i] = pemisah(sc.nextLine(), ";");
            i++;
        }
        return dictionary;
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException{
        File file = new File("src/sbdgray/Dictionary.txt");        
        String[][] dictionary = konversiKeArray(file);                      
        System.out.println("Masukkan Query Statement : ");
        Scanner sc = new Scanner(System.in);
        String query = sc.nextLine();       
        String[] word = pemisah(query, " ");        
        String[] output = eksekusiQuery(word, dictionary); 
        System.out.println();
        System.out.println("Nama Tabel : " + output[0]);
        System.out.print("List Kolom : ");
        for (int i = 1; i < output.length; i++) {            
            System.out.print(output[i]);
            if(i != output.length - 1){
                System.out.print(", ");
            }
        }
        System.out.println();
    }
}