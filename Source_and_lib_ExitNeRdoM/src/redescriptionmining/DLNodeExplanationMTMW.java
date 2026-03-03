/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redescriptionmining;

import java.io.File;

/**
 *
 * @author matej
 */
public class DLNodeExplanationMTMW {
      public static void main(String args[]) throws InterruptedException{
            long startTime = System.currentTimeMillis();

        int numThreads = Integer.parseInt(args[1]);    
        final Object lock = new Object();
        ApplicationSettings allSettings[] = new ApplicationSettings[numThreads];
        ApplicationSettings appset=null;
        for(int i=0;i<numThreads;i++){
        appset=new ApplicationSettings();
        appset.readSettings(new File(args[0]));
             appset.readPreference(); 
             allSettings[i] = appset;
        }
        if(numThreads == 0){
            System.err.println("numThreads must be >=1");
            System.exit(1);
        }
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

        Mappings fid=new Mappings();
        
        DataSetCreator datJ=new DataSetCreator(appset.viewInputPaths, appset.outFolderPath,appset);
  //make JinputThreadID datasets and read them without synchronization!
       
           if(appset.system.equals("windows"))
            fid.createIndex(appset.outFolderPath+"\\Jinput.arff");
        else
            fid.createIndex(appset.outFolderPath+"/Jinput.arff");
           
        int numAttr = datJ.schema.getNbAttributes()-1;
        

        Thread ths[] = new Thread[numThreads];
        
        int tasksPerThread = numAttr/numThreads;
        int start, end;
        
        for(int i=0;i<numThreads;i++){
            start = i*tasksPerThread;
            /*if(i==0)
                start = 50;*/
            end = i*tasksPerThread + tasksPerThread-1;
            if(i+1 == numThreads)
                end = numAttr-1;
            AttributeBatchRedescriptionMW abr = new AttributeBatchRedescriptionMW(start,end,i,allSettings[i],fid,datJ,lock);
            ths[i] = new Thread(abr);
        }
        
        for(int i=0;i<numThreads;i++)
            ths[i].start();
        
        for(int i=0;i<numThreads;i++)
            ths[i].join();
        
        FileDeleter del=new FileDeleter();
        if(appset.system.equals("windows")){ 
            del.setPath(appset.outFolderPath+"\\Jinput.arff");
           del.delete();
        }
        else{
             del.setPath(appset.outFolderPath+"/Jinput.arff");
            del.delete();
        }

    }
}
