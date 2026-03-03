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
 * @author Matej
 */
public class CountBaseilneTargetDist {
    
    private static float significance(int[] pop, int[] obs, float pCond) {
		float sig = 0f;
		for (int i = 0; i < pop.length; i++) {
			if (pop[i]==0 || obs[i]==0 || pCond==0)
				continue;
			sig += obs[i] * Math.log(obs[i]/(pop[i]*pCond));
		}
		sig = sig * 2;
		return sig;
	}
	
    
    public static void main(String args[]){
         Mappings map=new Mappings();
        map.createIndex("F:\\Matej Dokumenti\\Redescription mining with CLUS\\Experimenti metali\\Jinput.arff");
        HashMap<String,String> targetLabel=new HashMap<>();
        HashMap<Integer,HashMap<String,Integer>> distribution=new HashMap<>();
        DataSetCreator dat=new DataSetCreator("F:\\Matej Dokumenti\\Redescription mining with CLUS\\Experimenti metali\\Jinput.arff");
        try{
        dat.readDataset();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        int countT=0;
        File targ=new File("F:\\Matej Dokumenti\\Redescription mining with CLUS\\Experimenti metali\\dijagnozaLikvor.txt");
        Path path =Paths.get(targ.getAbsolutePath());
       BufferedReader reader=null;
       try{
       reader=Files.newBufferedReader(path,ENCODING);
       
       
       String line="";
       
       while((line=reader.readLine())!=null){
           String tmp[] = line.split(",");
           countT++;
           String num="\""+countT+"\"";
           targetLabel.put(tmp[0].trim(), tmp[1].trim());
       }
        }
       catch(Exception e){}
       
         ReadCLUSRMReds readerReds=new ReadCLUSRMReds();
         readerReds.inputFile=new File("F:\\Matej Dokumenti\\Redescription mining with CLUS\\Experimenti metali\\redescriptionsMetalsLikvorAssociationsConstrainedTeskiAD1.rr");
         readerReds.readReds(dat, map);
        
        ArrayList<String> posTVal=new ArrayList<>();
        posTVal.add("KONT"); posTVal.add("MCI"); posTVal.add("AD");
        
        
        int kont = 0, mci = 0, ad = 0;
        for(int i:map.idExample.keySet()){
            if(targetLabel.get(map.idExample.get(i)).equals("KONT"))
                kont++;
            else if(targetLabel.get(map.idExample.get(i)).equals("MCI"))
                mci++;
            else if(targetLabel.get(map.idExample.get(i)).equals("AD"))
                ad++;
        }
        
        System.out.println("Num examples: "+map.idExample.keySet().size());
        System.out.println(kont+" "+mci+" "+ad);
        
        int pop[] = new int[3];
        pop[0] = kont; pop[1] = mci; pop[2] = ad;
        
        int obs[] = new int[3];
        int numExamples = 193;
        
        File redInput = new File("F:\\Matej Dokumenti\\Redescription mining with CLUS\\Experimenti metali\\RedsWithDistTargetedMetals0.5LikvorEsencijalniAll.txt");
        
        Path p = Paths.get(redInput.getAbsolutePath());
        BufferedReader read = null;
        
        try{
        read = Files.newBufferedReader(p,StandardCharsets.UTF_8);
        
        
        File output = new File("F:\\Matej Dokumenti\\Redescription mining with CLUS\\Experimenti metali\\HiCHPvalsMetals0.5LikvorEsencijalniAll.txt");
        FileWriter fw = new FileWriter(output.getAbsoluteFile());
        String line = "";
        while((line = read.readLine())!=null){
            if(!line.contains("Dist:")) continue;
            
            if(line.contains("Perc.")) continue;
            
            String tmp[] = line.split(":");
            String vals[] = tmp[1].trim().split(" ");
            
            float sum = 0.0f;
            for(int i=0;i<vals.length;i++){
                obs[i] = Integer.parseInt(vals[i]);
                sum+=obs[i];
            }
           
            float pc = sum/numExamples; 
            double s = CountBaseilneTargetDist.significance(pop,obs,pc);
            fw.write(s+"\n");
        }
        fw.close();
        read.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}


