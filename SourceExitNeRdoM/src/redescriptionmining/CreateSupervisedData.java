/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redescriptionmining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import static redescriptionmining.SettingsReader.ENCODING;

/**
 *
 * @author matej
 */
public class CreateSupervisedData {
    public static void main(String args[]){
        //for supervised experiments on metals
        //1. try using one view only
        //2. try using both
        //3. add redescriptions and rules
        
        ArrayList<ArrayList<String>> dataAttributes = new ArrayList<>();
        ArrayList<ArrayList<String>> dataValues = new ArrayList<>();
        
        ArrayList<String> dataPaths = new ArrayList<>();
        
        dataPaths.add("C:\\Users\\matej\\Documents\\MetaliAD\\biomarkerLikvorF.arff");
        
        HashMap<String,String> targetLabel=new HashMap<>();
        
        for(int i=0;i<dataPaths.size();i++){
            dataAttributes.add(new ArrayList<String>());
            dataValues.add(new ArrayList<String>());
            File input = new File(dataPaths.get(i));
            
            try{
               Path path =Paths.get(input.getAbsolutePath());
               BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
               
               String line = "";
               int dataSection = 0;
               int firstAttr = 1;
               while((line = reader.readLine())!=null){
                   if(line.toLowerCase().contains("@attribute")){
                       if(i!=0 && firstAttr == 1){
                           firstAttr = 0;
                           continue;
                       }
                       else if(firstAttr == 1)
                           firstAttr = 0;
                       
                       dataAttributes.get(i).add(line);
                           
                   }
                   if(line.toLowerCase().contains("@data")){
                       dataSection = 1;
                       continue;
                   }
                   if(dataSection == 1){
                       dataValues.get(i).add(line);
                   }
               }
               reader.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
            
        }
        
        File targ=new File("C:\\Users\\matej\\Documents\\MetaliAD\\dijagnozaLikvor.arff");
        Path path =Paths.get(targ.getAbsolutePath());
       BufferedReader reader=null;
       try{
       reader=Files.newBufferedReader(path,ENCODING);
       
        
       String line="";
       
       while((line=reader.readLine())!=null){
           String tmp[] = line.split(",");
           targetLabel.put(tmp[0].trim(), tmp[1].trim());
       }
        }
       catch(Exception e){}
        
       try{
          FileWriter fw = new FileWriter("C:\\Users\\matej\\Documents\\MetaliAD\\MetalsPredictionLikvor.arff");
          fw.write("@relation 'dataAD'\n\n");
          for(int i=0;i<dataAttributes.size();i++)
              for(int j=0;j<dataAttributes.get(i).size();j++)
                  fw.write(dataAttributes.get(i).get(j)+"\n");
          fw.write("\n");
          fw.write("@data\n");
          //write data
          String line = "", line2="";
          for(int i=0;i<dataValues.get(0).size();i++){
              line = "";
            
              for(int j=0;j<dataValues.size();j++){
                    String id = dataValues.get(0).get(i).substring(0,dataValues.get(0).get(i).indexOf(",")).trim();
                  if(j==0)
                        line+=dataValues.get(j).get(i);
                  else{
                      line2 = dataValues.get(j).get(i).substring(dataValues.get(j).get(i).indexOf(",")+1,dataValues.get(j).get(i).length());
                      line+=","+line2;
                  }
                   line+=","+targetLabel.get(id);
              }
             fw.write(line+"\n");
          }
          fw.close();
       }
       catch(IOException e){
           e.printStackTrace();
       }
    }
}
