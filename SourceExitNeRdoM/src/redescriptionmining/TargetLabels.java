/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package redescriptionmining;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author matmih
 */
public class TargetLabels {
    HashSet<String> allLabels;
    HashMap<String,String> entityLabel;
    
    public TargetLabels(File inputData){//read the labels into the structures
        
        allLabels = new HashSet<>();
        entityLabel = new HashMap<>();
        
        BufferedReader reader;
        Path p = Paths.get(inputData.getAbsolutePath());
        
        try{
            reader = Files.newBufferedReader(p,StandardCharsets.UTF_8);
            
            String line = "";
            int data = 0;
            while((line = reader.readLine())!=null){
                if(!line.toLowerCase().contains("@data") && data == 0)
                    continue;
                else if(line.toLowerCase().contains("@data")){
                    data = 1; continue;
                }
                
                String tmp[] = line.split(",");
                entityLabel.put(tmp[0].trim(), tmp[1].trim());
                allLabels.add(tmp[1].trim());
                
            }
            
        }
        catch(IOException e){
            e.printStackTrace();
        }
        
    }
    
}
