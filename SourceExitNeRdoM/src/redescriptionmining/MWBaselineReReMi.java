/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redescriptionmining;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author matej
 */
public class MWBaselineReReMi {
    static public void main(String args[]){
        //load all sets
        
                 long startTime = System.currentTimeMillis();

        ApplicationSettings appset=new ApplicationSettings();
        appset.readSettings(new File(args[0]));
             appset.readPreference(); 
        System.out.println("Num targets: "+appset.numTargets);
        System.out.println("Num trees in RS: "+appset.numTreesinForest);
        System.out.println("Average tree depth in RS: "+appset.aTreeDepth);
        System.out.println("Allow left side rule negation: "+appset.leftNegation);
        System.out.println("Allow right side rule negation: "+appset.rightNegation);
        System.out.println("Allow left side rule disjunction: "+appset.leftDisjunction);
        System.out.println("Allow right side rule disjunction: "+appset.rightDisjunction);
        System.out.println("Types of LSTrees: "+appset.treeTypes.get(0));
        System.out.println("Types of RSTrees: "+appset.treeTypes.get(1));
        System.out.println("Use Network information: "+appset.useNC.toString());
        System.out.println("Spatial matrix: "+appset.spatialMatrix.toString());
        System.out.println("Spatial measure: "+appset.spatialMeasures.toString());
        
        
        System.out.println("Attribute importance gen: ");
        for(int i=0;i<appset.attributeImportanceGen.size();i++)
              System.out.print(appset.attributeImportanceGen.get(i)+" ");
        System.out.println();
        
        System.out.println("Important attributes: ");
        for(int i=0;i<appset.importantAttributes.size();i++){
            for(int j=0;j<appset.importantAttributes.get(i).size();j++){
                for(int k=0;k<appset.importantAttributes.get(i).get(j).size();k++){
                    if(k<appset.importantAttributes.get(i).get(j).size())
                        System.out.print(appset.importantAttributes.get(i).get(j).get(k)+" , ");
                }
                System.out.print(" + ");
            }
        System.out.println();
        }
        //appset.importantAttributes

       ArrayList<String> allViews = new ArrayList<>(); 
       //trade
       //allViews.add("input1Trade.arff"); allViews.add("input2Population.arff"); allViews.add("input3Energy.arff");
       
       for(int i=0;i<appset.viewInputPaths.size();i++)
           allViews.add(appset.viewInputPaths.get(i));
       
       
       String sep = "";
       if(appset.system != "windows")
         sep = "/";
       else sep = "\\";
       
       ArrayList<ArrayList<RedescriptionReReMiGen>> allSetsIncomplete = new ArrayList<>();
       RedescriptionSet MWSet = new RedescriptionSet();
       ArrayList<DataSetCreator> dats = new ArrayList<>();
       ArrayList<Mappings> maps = new ArrayList<>();
       
       System.out.println("Preferences size: "+appset.preferences.size());
       File inputSet = null;
       File inputQueries = null;
       String fileName = "redsReReMi";
       String fileNameQueries = "redsReReMi";
       int stNum = 0;
       
    for(int ww1 = 0; ww1<allViews.size()-1;ww1++){
        for(int ww2 = ww1+1; ww2< allViews.size();ww2++){
            
             Mappings fid=new Mappings();
        Mappings fidFull=new Mappings();
         Mappings fidTest=new Mappings();
        
        DataSetCreator datJ=new DataSetCreator(appset.viewInputPaths, appset.outFolderPath,appset);

        if(ww1==0 && ww2 == 1){
             if(appset.system.equals("windows"))
            fid.createIndex(appset.outFolderPath+"\\Jinput.arff");
        else
            fid.createIndex(appset.outFolderPath+"/Jinput.arff");
        }
        
        if(ww2 == ww1+1){
            maps.add(fid);
            dats.add(datJ);
        }
        if((ww1 == allViews.size()-2) && (ww2 == ww1+1)){
             maps.add(fid);
             dats.add(datJ);
        }
            
            ArrayList<RedescriptionReReMiGen> t = new ArrayList<>();
            allSetsIncomplete.add(t);
            //load set (ww1, ww2) from a file
              fileName = "redsReReMi";
             fileNameQueries = "redsReReMi";
             fileName = fileName + ww1 + "_"+ww2+".txt";
             fileNameQueries = fileNameQueries + ww1 + "_"+ww2+".named";
            inputSet = new File("C:\\Users\\matej\\Documents\\Redescription mining with CLUS\\Experiments ML multi-view\\Siren reds\\Country\\"+fileName);
            inputQueries = new File("C:\\Users\\matej\\Documents\\Redescription mining with CLUS\\Experiments ML multi-view\\Siren reds\\Country\\"+fileNameQueries);
            
            if(appset.system.equals("linux")){
                inputSet = new File(fileName);
               inputQueries = new File(fileNameQueries);
            
            }
            
            try{
                Path p = Paths.get(inputQueries.getAbsolutePath());
                BufferedReader read = Files.newBufferedReader(p);
                
                String line = "";
                int count = 0;
                while((line = read.readLine())!=null){
                    if(count == 0){
                        count = 1;
                        continue;
                    }
                    
                    String tmp[] = line.split("\t");
                    
                    RedescriptionReReMiGen r = new RedescriptionReReMiGen(allViews.size());
                    
                    r.queries.set(ww1,tmp[1].replaceAll("\\|", "OR").replaceAll("&", "AND").replaceAll("\\!", "NOT").trim()); r.queries.set(ww2,tmp[2].replaceAll("\\|", "OR").replaceAll("&", "AND").replaceAll("\\!", "NOT").trim());
                    allSetsIncomplete.get(stNum).add(r);
                }
                
                read.close();
                
                
                p = Paths.get(inputSet.getAbsolutePath());
                read = Files.newBufferedReader(p);
                
                line = "";
                count = 0;
                int redNum = 0;
                while((line = read.readLine())!=null){
                   
                   if(count == 0){ 
                    String tmp[] = line.split("\t");
                    
                    String sup1[] = tmp[0].split(" ");
                    String sup2[] = tmp[1].split(" ");
                    
                    for(int z=0;z<sup1.length;z++)
                        allSetsIncomplete.get(stNum).get(redNum).supports.get(ww1).add(Integer.parseInt(sup1[z].trim()));
                     
                    for(int z=0;z<sup2.length;z++)
                        allSetsIncomplete.get(stNum).get(redNum).supports.get(ww2).add(Integer.parseInt(sup2[z].trim()));
                     
                    redNum++;
                    count=1;
                    continue;
                   }
                   
                    
                    
                     if(count!= 0){
                        count++;
                        if(count == 3)
                            count = 0;
                        continue;
                    }
                }
                
                read.close();
                
               stNum++; 
            }
            catch(Exception e){
                e.printStackTrace();
            }
            
        }
    }
    
    System.out.println("stNum: "+stNum);
    System.out.println("as2W array size: "+allSetsIncomplete.size());
       //test reading of RedSets 
    
   /* for(int i=0;i<allSetsIncomplete.size();i++){
       for(int j=0;j<allSetsIncomplete.get(i).size();j++){
           System.out.println(allSetsIncomplete.get(i).get(j).queries.get(0)+" "+allSetsIncomplete.get(i).get(j).queries.get(1));
           System.out.println(allSetsIncomplete.get(i).get(j).supports.get(0).size()+" "+allSetsIncomplete.get(i).get(j).supports.get(1).size());
       }
    }*/
            
        //implement the procedure to create complete redescriptions
        //use newly constructed ReReMiRed structure
    
    ArrayList<RedescriptionReReMiGen> tmp = null;
    ArrayList<RedescriptionReReMiGen> tmpRd = new ArrayList<>();
    ArrayList<RedescriptionReReMiGen> MWReReMi = new ArrayList<>();
    
    //create merge function and computeStatistics function for RedescriptionReReMi
    
    System.out.println("ASI: "+allSetsIncomplete.size());
    
    for(int i = 0; i<allSetsIncomplete.size()-1; i++){
        for(int j=i+1; j<allSetsIncomplete.size();j++){
           /* if(j!=(i+1))
                continue;*/
            tmpRd = new ArrayList<>();
            System.out.println("MWReReMi size: "+MWReReMi.size());
            for(int z=0;z<MWReReMi.size();z++){
                for(int z1 = 0; z1<allSetsIncomplete.get(j).size();z1++){

                tmp= MWReReMi.get(z).merge(allSetsIncomplete.get(j).get(z1));//add function checkMerge to reduce the number of unecessarily created redescriptions
                for(int zz=0;zz<tmp.size();zz++){//returns 1 or 2 (how many reds possible to create)
                        tmp.get(zz).computeStatistics(dats, maps);//simulate compute statistics and create red only if OK
                                if(tmp.get(zz).JS>=appset.minJS && tmp.get(zz).elements.size()>=appset.minSupport)
                                    tmpRd.add(tmp.get(zz));
                     }
                //System.out.println("usefulComplex: "+tmpRd.size());
                }
                if(z%10000 == 0)
                 System.out.println("Remaining: "+(MWReReMi.size() - z));
            }
            System.out.println("complex completed!");
            MWReReMi.addAll(tmpRd);
            System.out.println(i+" "+j);
            
            //add all from view combination
            tmpRd.clear();
           for(int z=0;z<allSetsIncomplete.get(i).size();z++){
                for(int z1 = 0; z1<allSetsIncomplete.get(j).size();z1++){

                //tmp = new Redescription(allSetsIncomplete.get(i).redescriptions.get(z));
                    //need to create less tmp redescriptions
                tmp = allSetsIncomplete.get(i).get(z).merge(allSetsIncomplete.get(j).get(z1));
                //System.out.println("tmp size: "+tmp.size());
               // System.out.println("Merge complete");
                for(int zz=0;zz<tmp.size();zz++){
                     tmp.get(zz).computeStatistics(dats, maps);
                    if(tmp.get(zz).JS>=appset.minJS && tmp.get(zz).elements.size()>=appset.minSupport)
                        tmpRd.add(tmp.get(zz));
                     }
               // System.out.println("useful: "+tmpRd.size());
               
                }
            }
           System.out.println("Regular completed!");
           MWReReMi.addAll(tmpRd); 
             System.out.println(i+" "+j);
        }
    }
    
    System.out.println("Reds computed...");
    System.out.println("All reds: "+MWReReMi.size());
    //filter out all incomplete reds
    //filter out all reds not satisfying constraints
    
   // for(int i = MWSet.redescriptions.size()-1;i>=0;i--)
     //   MWSet.redescriptions.get(i).computeStatistics(dats, maps);
    
  //filter and return (copy to original Redescription structure)
    for(int i=0;i<MWReReMi.size();i++){
        Redescription tmpRed = new Redescription(MWReReMi.get(i));
        MWSet.redescriptions.add(tmpRed);
    }
    
    System.out.println("After copy, MW size: "+MWSet.redescriptions.size());
    
    for(int i = MWSet.redescriptions.size()-1;i>=0;i--)
        if(MWSet.redescriptions.get(i).numViews()<allViews.size() || MWSet.redescriptions.get(i).JS<appset.minJS || MWSet.redescriptions.get(i).elements.size()<appset.minSupport || MWSet.redescriptions.get(i).elements.size()>appset.maxSupport || MWSet.redescriptions.get(i).pVal>appset.maxPval)
            MWSet.redescriptions.remove(i);
    
    System.out.println("Filtering done...");
    System.out.println("Remaining reds: "+MWSet.redescriptions.size());
    
    MWReReMi.clear();
    
    ArrayList<Integer> toRemove = new ArrayList<>();
    Jacard js=new Jacard();
    System.out.println("Redundancy filtering started: ");
    
    int end = 0;
    
    double percRedundancy = 0.95;
    
    HashSet<Integer> traversed = new HashSet<>();
    
    int last = 0;
    while(true){
        end = 1;
        int i = last;
    for(;i<MWSet.redescriptions.size()-1;i++){
       // if(traversed.contains(i))
         //   continue;
        for(int j = i+1;j<MWSet.redescriptions.size();j++){
            if(((MWSet.redescriptions.get(i).elements.size()>MWSet.redescriptions.get(j).elements.size()) && (percRedundancy*MWSet.redescriptions.get(i).elements.size()>MWSet.redescriptions.get(j).elements.size())) || ((MWSet.redescriptions.get(j).elements.size()>MWSet.redescriptions.get(i).elements.size()) && percRedundancy*MWSet.redescriptions.get(j).elements.size()>MWSet.redescriptions.get(i).elements.size()))
                continue;
            if(js.computeRedescriptionElementJacard(MWSet.redescriptions.get(i), MWSet.redescriptions.get(j)) >= percRedundancy)
                if(MWSet.redescriptions.get(i).JS>MWSet.redescriptions.get(j).JS)
                    toRemove.add(j);
                else{
                    toRemove.add(i);
                    i--;
                    break;
                }
        }
    
        //traversed.add(i);
        last = i+1;
        
        for(int k=toRemove.size()-1;k>=0;k--)
            MWSet.redescriptions.remove((int)toRemove.get(k));
        System.out.println("Filtering index: "+i);
        System.out.println("Remaning: "+MWSet.redescriptions.size());
        System.out.println("ToRemove size: "+toRemove.size());
        
        if(toRemove.size()>0)
            end = 0;
        toRemove.clear();
        if(end == 0)
            break;
    }
    if(end == 1)
        break;
   }
    
    System.out.println("Remaining reds: "+MWSet.redescriptions.size());
    
    //output complete set to a file (along with corresponding measures)
    
    //create functions that do not use mappings
    //double ResultScore=MWSet.computeRedescriptionSetScoreGenBaseline(appset.preferences.get(0),0,dats.get(0),appset);
    
    //System.out.println("Rscore computed!");
    
    MWSet.writeToFileBaseline(appset.outFolderPath+sep+appset.outputName+"BaselineReReMi.rr", startTime,appset, maps.get(0), /*ResultScore*/Double.NaN);  
      MWSet.writePlotsBaseline(appset.outFolderPath+sep+appset.outputName+"BaselineStatsReReMi.csv", appset, dats.get(0));
    
      System.out.println("RSets computed and written!");
      
    /* ArrayList<RedescriptionSet> allSets2W = new ArrayList<>();
       ArrayList<RedescriptionSet> allSetsIncomplete = new ArrayList<>();
       RedescriptionSet MWSet = new RedescriptionSet();*/

     long endTime = System.currentTimeMillis();
    
     double overallTime = (endTime - startTime)/1000.0;
     
     System.out.println("Overall time: "+overallTime+" "+"s");
        
   //TO implement: computeStatistics and merge
        
    }
}
