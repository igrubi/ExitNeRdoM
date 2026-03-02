/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redescriptionmining;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import static redescriptionmining.DataSetCreator.writeArff;

/**
 *
 * @author matej
 * multi-view baseline. Creating multi-view redescriptions using 2W CLUS-RM and combining the resulting 
 * redescriptions
 */
public class MWBaseline {
    public static void main(String args[]){
        
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
       
       ArrayList<RedescriptionSet> allSets2W = new ArrayList<>();
       ArrayList<RedescriptionSet> allSetsIncomplete = new ArrayList<>();
       RedescriptionSet MWSet = new RedescriptionSet();
       ArrayList<DataSetCreator> dats = new ArrayList<>();
       ArrayList<Mappings> maps = new ArrayList<>();
       
       System.out.println("Preferences size: "+appset.preferences.size());
       
    for(int ww1 = 0; ww1<allViews.size()-1;ww1++){
        for(int ww2 = ww1+1; ww2< allViews.size();ww2++){
            appset.viewInputPaths = new ArrayList<>();
            
            //appset.viewInputPaths.set(0,appset.outFolderPath+sep+allViews.get(ww1));
           // appset.viewInputPaths.set(1,appset.outFolderPath+sep+allViews.get(ww2));
        
            appset.viewInputPaths.add(allViews.get(ww1));
            appset.viewInputPaths.add(allViews.get(ww2));
            
            appset.outputName+="_W"+(ww1+1)+"_"+(ww2+1);
            
        Mappings fid=new Mappings();
        Mappings fidFull=new Mappings();
         Mappings fidTest=new Mappings();
        
        DataSetCreator datJ=new DataSetCreator(appset.viewInputPaths, appset.outFolderPath,appset);

        if(ww2 == ww1+1){
            maps.add(fid);
            dats.add(datJ);
        }
        if((ww1 == allViews.size()-2) && (ww2 == ww1+1)){
             maps.add(fid);
             dats.add(datJ);
        }
        
        DataSetCreator datJFull=null;
        DataSetCreator datJTest=null;
        //DataSetCreator datJ=new DataSetCreator(appset.viewInputPaths.get(0)/*appset.outFolderPath+"\\input1.arff"*/,appset.viewInputPaths.get(1)/*appset.outFolderPath+"\\input2.arff"*/ , appset.outFolderPath);
        //fid.createIndex(appset.outFolderPath+"\\Jinput.arff");
        
           if(appset.system.equals("windows"))
            fid.createIndex(appset.outFolderPath+"\\Jinput.arff");
        else
            fid.createIndex(appset.outFolderPath+"/Jinput.arff");
           
           if(appset.useSplitTesting==true){
               if(appset.system.equals("windows")){
                        fidFull.createIndex(appset.outFolderPath+"\\Jinput.arff");
                        fidTest.createIndex(appset.outFolderPath+"\\Jinput.arff");
               }
               else{
                   fidFull.createIndex(appset.outFolderPath+"/Jinput.arff");
                   fidTest.createIndex(appset.outFolderPath+"/Jinput.arff");
               }
           }
           
           if(appset.useSplitTesting==true){
               ArrayList<DataSetCreator> rDat = new ArrayList<>();
               datJFull=datJ;
               datJTest = datJ;
               if(appset.trainFileName.equals("") || appset.testFileName.equals("")){
               rDat=datJ.createSplit(appset.percentageForTrain);
               datJ = rDat.get(0);
               datJTest = rDat.get(1);
               
               
                try{
             if(appset.system.equals("windows")){
                 writeArff(appset.outFolderPath+"\\JinputTrain.arff", datJ.data);
                 writeArff(appset.outFolderPath+"\\JinputTest.arff", datJTest.data);
             }
             else{
                writeArff(appset.outFolderPath+"/JinputTrain.arff", datJ.data);
                 writeArff(appset.outFolderPath+"/JinputTest.arff", datJTest.data); 
             }
            }
                 catch(Exception e){
                     e.printStackTrace();
                 }
        
               
               }
               else{
                   if(appset.system.equals("windows")){
                        datJ = new DataSetCreator(appset.outFolderPath+"\\"+appset.trainFileName);
                        datJTest = new DataSetCreator(appset.outFolderPath+"\\"+appset.testFileName);
                   }
                   else{
                        datJ = new DataSetCreator(appset.outFolderPath+"/"+appset.trainFileName);
                        datJTest = new DataSetCreator(appset.outFolderPath+"/"+appset.testFileName);
                   }
                   
                    try{
                             datJ.readDataset();
                             datJTest.readDataset();
                         }
                     catch(IOException e){
                              e.printStackTrace();
                          }
        
                         datJ.W2indexs.addAll(datJFull.W2indexs);
                         datJTest.W2indexs.addAll(datJFull.W2indexs);
                   
            }
               
        
        
         fid.clearMaps();
         if(appset.system.equals("windows"))
             fid.createIndex(appset.outFolderPath+"\\JinputTrain.arff");
         else fid.createIndex(appset.outFolderPath+"/JinputTrain.arff");
          System.out.println("Full: "+datJFull.numExamples);
        System.out.println("Part: "+datJ.numExamples);
        }
        
        Random r=new Random();
        RedescriptionSet rs=new RedescriptionSet();
        RuleReader rr=new RuleReader();
        RuleReader rr1=new RuleReader();
         boolean oom[]= new boolean[1];
         
        int elemFreq[]=null;
        int attrFreq[]=null;
        ArrayList<Double> redScores=null;
        ArrayList<Double> redScoresAtt=null;
        ArrayList<Double> targetAtScore=null;
        ArrayList<Double> redDistCoverage=null;
        ArrayList<Double> redDistCoverageAt=null;
        ArrayList<Double> redDistNetwork=null;
         double Statistics[]={0.0,0.0,0.0};//previousMedian - 0, numberIterationsStable - 1, minDifference - 2
         ArrayList<Double> maxDiffScoreDistribution = null;
      
       if(appset.optimizationType == 0){
        if(appset.redesSetSizeType==1 && appset.numRetRed!=Integer.MAX_VALUE)
            appset.numInitial=appset.numRetRed;
        else{
            if(appset.numRetRed!=Integer.MAX_VALUE && appset.numRetRed!=-1)
                appset.numInitial=appset.numRetRed;
            else
                appset.numInitial=20;
        }
       }
        
        
        if(appset.optimizationType==0){
            
                   
         elemFreq=new int[datJ.numExamples];
         attrFreq=new int[datJ.schema.getNbAttributes()];  
            
          System.out.println("Number of redescriptions: "+appset.numInitial);
        
        redScores=new ArrayList<>(appset.numInitial);
        redScoresAtt=new ArrayList<>(appset.numInitial);
        redDistCoverage=new ArrayList<>(appset.numInitial);
        redDistCoverageAt=new ArrayList<>(appset.numInitial);
        if(appset.useNetworkAsBackground==true)
              redDistNetwork=new ArrayList<>(appset.numInitial);
         targetAtScore=null;
        //double Statistics[]={0.0,0.0,0.0};//previousMedian - 0, numberIterationsStable - 1, minDifference - 2
        maxDiffScoreDistribution=new ArrayList<>(appset.numInitial);
        
        if(appset.attributeImportance!=0)
            targetAtScore = new ArrayList<>(appset.numInitial);
        
        for(int z=0;z<appset.numInitial;z++){
            redScores.add(Double.NaN);
            redScoresAtt.add(Double.NaN);
            redDistCoverage.add(Double.NaN);
            redDistCoverageAt.add(Double.NaN);
            maxDiffScoreDistribution.add(Double.NaN);
            if(appset.useNetworkAsBackground==true)
                 redDistNetwork.add(Double.NaN);
            if(appset.attributeImportance!=0)
                targetAtScore.add(Double.NaN);
        }   
            

            
        /*if(appset.attributeImportance!=0)
            targetAtScore = new ArrayList<>(appset.numRetRed);
        
        for(int z=0;z<appset.numRetRed;z++){
            redScores.add(Double.NaN);
            redScoresAtt.add(Double.NaN);
        }*/
      }
                    
        NHMCDistanceMatrix nclMatInit=null;
        if(appset.distanceFilePaths.size()>0){
            nclMatInit=new NHMCDistanceMatrix(datJ.numExamples,appset);
            nclMatInit.loadDistance(new File(appset.distanceFilePaths.get(0)), fid);
            if(appset.distanceFilePaths.size()>0){
             nclMatInit.resetFile(new File(appset.outFolderPath+"\\distances.csv"));
             nclMatInit.writeToFile(new File(appset.outFolderPath+"\\distances.csv"), fid,appset);
            }
            else{
                nclMatInit.resetFile(new File(appset.outFolderPath+"/distances.csv"));
                nclMatInit.writeToFile(new File(appset.outFolderPath+"/distances.csv"), fid,appset);
            }
             nclMatInit=null;
        }
        
        for(int runTest=0;runTest<appset.numRandomRestarts;runTest++){  
            
          DataSetCreator datJInit=null;
          
       if(!appset.useSplitTesting){ 
        if(appset.initClusteringFileName.equals("")){
            if(appset.system.equals("windows"))
                datJInit = new DataSetCreator(appset.outFolderPath+"\\Jinput.arff");
            else
                datJInit = new DataSetCreator(appset.outFolderPath+"/Jinput.arff");
        }
        else{
                     if(appset.system.equals("windows"))
                            datJInit = new DataSetCreator(appset.outFolderPath+"\\"+appset.initClusteringFileName);
                     else
                            datJInit = new DataSetCreator(/*appset.outFolderPath+"/"+*/appset.initClusteringFileName);
               
            }
       }
       else{
           if(appset.trainFileName.equals("") || appset.testFileName.equals("")){
            if(appset.system.equals("windows"))
                datJInit = new DataSetCreator(appset.outFolderPath+"\\JinputTrain.arff");
            else
                datJInit = new DataSetCreator(appset.outFolderPath+"/JinputTrain.arff");
           }
           else{
               if(appset.system.equals("windows"))
                datJInit = new DataSetCreator(appset.outFolderPath+"\\"+appset.trainFileName);
            else
                datJInit = new DataSetCreator(appset.outFolderPath+"/"+appset.trainFileName);
           }
       }
        
                try{
        datJInit.readDataset();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        
        datJInit.W2indexs.addAll(datJ.W2indexs);
        
        if(appset.initClusteringFileName.equals(""))
            datJInit.initialClusteringGen1(appset.outFolderPath,appset,datJ.schema.getNbDescriptiveAttributes(),r);
        
        SettingsReader initSettings=new SettingsReader();
        
        if(appset.initClusteringFileName.equals(""))
             if(appset.system.equals("windows"))
                 initSettings.setDataFilePath(appset.outFolderPath+"\\JinputInitial.arff");
             else
                  initSettings.setDataFilePath(appset.outFolderPath+"/JinputInitial.arff");
        else{
            if(appset.system.equals("windows"))
                 initSettings.setDataFilePath(appset.outFolderPath+"\\"+appset.initClusteringFileName);
            else
                initSettings.setDataFilePath(/*appset.outFolderPath+"/"+*/appset.initClusteringFileName);
        }
        
        //fid.printMapping();
        //make initial cluster analysis
        
        System.out.println("WIndexes size: "+datJ.W2indexs.size());
        
       /* DataSetCreator datJInit=new DataSetCreator(appset.outFolderPath+"\\Jinput.arff");
                try{
        datJInit.readDataset();
        }
        catch(IOException e){
            e.printStackTrace();
        }*/
        
       // datJInit.W2indexs.addAll(datJ.W2indexs);
       // datJInit.initialClusteringGen1(appset.outFolderPath,appset, datJ.schema.getNbDescriptiveAttributes(),r);
        //datJInit.initialClustering(appset.outFolderPath,appset);
        //datJInit.initialClusteringCategorical(appset.outFolderPath);
        
       /* SettingsReader initSettings=new SettingsReader();
        initSettings.setDataFilePath(appset.outFolderPath+"\\JinputInitial.arff");*/
        if(appset.system.equals("windows"))
             initSettings.setPath(appset.outFolderPath+"\\view1.s");
        else
             initSettings.setPath(appset.outFolderPath+"/view1.s");
        //initSettings.createInitialSettings1(1, datJ.W2indexs.get(0), datJInit.schema.getNbAttributes(), appset);
        System.out.println("distance file size: "+appset.distanceFilePaths.size()+"");
        System.out.println("use nc: "+appset.useNC.size());
        if(appset.useNC.get(0) == false)
             initSettings.createInitialSettingsGen(0, 3, datJ.W2indexs.get(0), datJ.schema.getNbAttributes(), appset,1);
        else
             initSettings.createInitialSettingsGen(0, 4, datJ.W2indexs.get(0), datJ.schema.getNbAttributes(), appset,1);
            
        ClusProcessExecutor exec=new ClusProcessExecutor();

        //RunInitW1S1
        exec.run(appset.javaPath,appset.clusPath ,appset.outFolderPath,"view1.s",0, appset.clusteringMemory);//was 1 before for rules
        System.out.println("Process 1 side 1 finished!");
         
        //read the rules obtained from first attribute set
         // String input1="C:\\Users\\matej\\Documents\\Redescription mining with CLUS\\unctad.out";
          String input1="";
          if(appset.system.equals("windows"))
             input1=appset.outFolderPath+"\\view1.out";
          else
              input1=appset.outFolderPath+"/view1.out"; 
           
           rr1.extractRules(input1,fid,datJInit,appset);
           
         //reading arff file
         
           if(appset.distanceFilePaths.size()>1){
            nclMatInit=new NHMCDistanceMatrix(datJ.numExamples,appset);
            nclMatInit.loadDistance(new File(appset.distanceFilePaths.get(1)), fid);
            if(appset.system.equals("windows")){
             nclMatInit.resetFile(new File(appset.outFolderPath+"\\distances.csv"));
             nclMatInit.writeToFile(new File(appset.outFolderPath+"\\distances.csv"), fid,appset);
            }
            else{
                nclMatInit.resetFile(new File(appset.outFolderPath+"/distances.csv"));
                nclMatInit.writeToFile(new File(appset.outFolderPath+"/distances.csv"), fid,appset);
            }
             nclMatInit=null;
        }
       
        SettingsReader set=null;
        SettingsReader set1=null;
        SettingsReader setF=null;
        SettingsReader setF1=null;
       
        //RunInitW1S2
        if(appset.system.equals("windows")){
            initSettings.setPath(appset.outFolderPath+"\\view2.s");
        }
        else{
           initSettings.setPath(appset.outFolderPath+"/view2.s"); 
        }
        //initSettings.createInitialSettings1(2, datJ.W2indexs.get(0), datJInit.schema.getNbAttributes(), appset);
        if(datJ.W2indexs.size()>1)
            initSettings.createInitialSettingsGen(1, datJ.W2indexs.get(0)+1, datJ.W2indexs.get(1), datJ.schema.getNbAttributes(), appset,1);
        else
            initSettings.createInitialSettingsGen(1, datJ.W2indexs.get(0)+1, datJInit.schema.getNbAttributes(), datJ.schema.getNbAttributes(), appset,1);
        
        exec.run(appset.javaPath,appset.clusPath, appset.outFolderPath, "view2.s", 0,appset.clusteringMemory);//was 1 before
        System.out.println("Process 1 side 2 finished!");

        //read the rules obtained from first attribute set
       if(appset.system.equals("windows"))
        input1=appset.outFolderPath+"\\view2.out";
       else
           input1=appset.outFolderPath+"/view2.out";
        rr.extractRules(input1,fid,datJInit,appset);
           
           FileDeleter delTmp=new FileDeleter();
           if(appset.system.equals("windows"))
                delTmp.setPath(appset.outFolderPath+"\\JinputInitial.arff");
           else
               delTmp.setPath(appset.outFolderPath+"/JinputInitial.arff");
           delTmp.delete();
        
          if(appset.system.equals("windows"))
           initSettings.setPath(appset.outFolderPath+"\\view1.s");
          else
              initSettings.setPath(appset.outFolderPath+"/view1.s");
          // initSettings.createInitialSettings(1, datJ.W2indexs.get(0), datJ.schema.getNbAttributes(), appset);
           //initSettings.createInitialSettingsGen(0, 3, datJ.W2indexs.get(0), datJ.schema.getNbAttributes(), appset);
          if(appset.system.equals("windows")) 
             initSettings.setPath(appset.outFolderPath+"\\view2.s");
          else
             initSettings.setPath(appset.outFolderPath+"/view2.s");
          // initSettings.createInitialSettings(2, datJ.W2indexs.get(0), datJ.schema.getNbAttributes(), appset);
          // initSettings.createInitialSettingsGen(0, datJ.W2indexs.get(0)+1, datJ.W2indexs.get(1), datJ.schema.getNbAttributes(), appset);
         
           datJInit=null;        
           
        int leftSide=1, rightSide=0;//set left to 1 when computing lf, otherwise right
        int leftSide1=0, rightSide1=1; //left, right side for Side 2
        int it=0;
        Jacard js=new Jacard();
        Jacard jsN[]=new Jacard[3];
        
        for(int i=0;i<jsN.length;i++)
            jsN[i]=new Jacard();
            
        //ArrayList<Redescription> redescriptions=new ArrayList<Redescription>();
       
        int newRedescriptions=1;
        int numIter=0;
        int RunInd=0;
       
        int naex=datJ.numExamples;
        
        //add arrayList of view rules
        ArrayList<RuleReader> readers=new ArrayList<>();
        int oldRIndex[]={0};
        
        NHMCDistanceMatrix nclMat=null;
        if((appset.distanceFilePaths.size()>0 || appset.useNC.get(0)==true) && appset.networkInit==false)
            nclMat=new NHMCDistanceMatrix(datJ.numExamples,appset);
        NHMCDistanceMatrix nclMat1=null;
        if((appset.distanceFilePaths.size()>1 || appset.useNC.get(1)==true) && appset.networkInit==false)
            nclMat1=new NHMCDistanceMatrix(datJ.numExamples,appset);
       
        if(appset.useNetworkAsBackground==true)
            appset.networkInit=false;
        
        if(appset.useNC.size()>=2 && appset.useNC.get(1) == true){
            if(appset.system.equals("windows")) 
                initSettings.setPath(appset.outFolderPath+"\\view2.s");
            else
                initSettings.setPath(appset.outFolderPath+"/view2.s");
         if(appset.useNC.size()>2)
            initSettings.createInitialSettingsGenN(1, datJ.W2indexs.get(0)+1, datJ.W2indexs.get(1), datJ.schema.getNbAttributes(), appset);
         else
            initSettings.createInitialSettingsGenN(1, datJ.W2indexs.get(0)+1, datJ.schema.getNbAttributes()+1, datJ.schema.getNbAttributes(), appset); 
        }
        if(appset.useNC.size()>1 && appset.useNC.get(0) == true){
             if(appset.system.equals("windows")) 
                initSettings.setPath(appset.outFolderPath+"\\view1.s");
             else
                initSettings.setPath(appset.outFolderPath+"/view1.s");
         initSettings.createInitialSettingsGenN(1, 4, datJ.W2indexs.get(0), datJ.schema.getNbAttributes(), appset);
        }
        
        /*if(appset.networkInit==true){
            appset.useNC.set(0, false);
            datJ=new DataSetCreator(appset.viewInputPaths, appset.outFolderPath,appset);
            appset.useNC.set(0, true);
        }*/
        
        while(newRedescriptions!=0 && RunInd<appset.numIterations){
            
       DataSetCreator dsc=null;//new DataSetCreator(appset.outFolderPath+"\\Jinput.arff");
       DataSetCreator dsc1=null;//new DataSetCreator(appset.outFolderPath+"\\Jinput.arff");
       
       rr.setSize();
       rr1.setSize();
       
       //read dataset for cicle 1
        /*try{
        dsc.readDataset();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        //read dataset for cicle 2
        try{
        dsc1.readDataset();
        }
        catch(IOException e){
            e.printStackTrace();
        }*/

       //dsc.data.getNbRows();
       int nARules=0, nARules1=0;
       int oldIndexRR=rr.newRuleIndex;
       int oldIndexRR1=rr1.newRuleIndex;
       System.out.println("OOIndRR: "+oldIndexRR);
       System.out.println("OOIndRR1: "+oldIndexRR1);
       int endIndexRR=0, endIndexRR1=0;
             newRedescriptions=0;
            System.out.println("Iteration: "+(++numIter));

             //do rule creation with various generality levels
        double percentage[]=new double[]{0,0.2,0.4,0.6,0.8,1.0};

         int numBins=0;
        int Size=Math.max(rr.rules.size()-oldIndexRR, rr1.rules.size()-oldIndexRR1);
        if(Size%appset.numTargets==0)
            numBins=Size/appset.numTargets;
        else numBins=Size/appset.numTargets+1;
        
        for(int z=0;z<numBins;z++){//percentage.length-1;z++){

            nARules=0; nARules1=0;
            double startPerc=0;//percentage[z];
            double endPerc=1;//percentage[z+1];
            int minCovElements[]=new int[]{0}, minCovElements1[]=new int[]{0};
            int maxCovElements[]=new int[]{0}, maxCovElements1[]=new int[]{0};

            System.out.println("startPerc: "+startPerc);
            System.out.println("endPerc: "+endPerc);

            int cuttof=0,cuttof1=0;

            if(z==0){
                endIndexRR=rr.rules.size();
                endIndexRR1=rr1.rules.size();
               
                //compute network things only here
                if(appset.useNC.get(1)==true && appset.networkInit==false && appset.useNetworkAsBackground==false){
                    nclMat.reset(appset);
                    nclMat1.reset(appset);
                if(leftSide==1){
                    if(appset.distanceFilePaths.size()>=1){
                             nclMat.loadDistance(new File(appset.distanceFilePaths.get(1)), fid);
                             //nclMat.writeToFile(new File(appset.outFolderPath+"\\distance.csv"), fid);
                    }
                     else if(appset.computeDMfromRules==true){
                             nclMat.computeDistanceMatrix(rr1, fid, appset.maxDistance, datJ.numExamples);
                     }
                   }
                }
                 if(appset.useNC.get(0)==true && appset.networkInit==false && appset.useNetworkAsBackground==false){
                 if(rightSide==1){
                    if(appset.distanceFilePaths.size()>=2){
                             nclMat.loadDistance(new File(appset.distanceFilePaths.get(0)), fid);
                            // nclMat.writeToFile(new File(appset.outFolderPath+"\\distance.csv"), fid);
                    }
                     else if(appset.computeDMfromRules==true){
                             nclMat.computeDistanceMatrix(rr, fid, appset.maxDistance, datJ.numExamples);
                     }
                   }
                 }
                  if(appset.useNC.get(1)==true && appset.networkInit==false && appset.useNetworkAsBackground==false){
                if(leftSide1==1){
                    if(appset.distanceFilePaths.size()>=1){
                             nclMat1.loadDistance(new File(appset.distanceFilePaths.get(1)), fid);
                            // nclMat1.writeToFile(new File(appset.outFolderPath+"\\distance.csv"), fid);
                    }
                     else if(appset.computeDMfromRules==true){
                             nclMat1.computeDistanceMatrix(rr1, fid, appset.maxDistance, datJ.numExamples);
                     }
                }
                  }
                   if(appset.useNC.get(0)==true && appset.networkInit==false && appset.useNetworkAsBackground==false){
                    if(rightSide1==1){
                    if(appset.distanceFilePaths.size()>=2){
                             nclMat1.loadDistance(new File(appset.distanceFilePaths.get(0)), fid);
                            // nclMat1.writeToFile(new File(appset.outFolderPath+"\\distance.csv"), fid);
                    }
                     else if(appset.computeDMfromRules==true){
                             nclMat1.computeDistanceMatrix(rr, fid, appset.maxDistance, datJ.numExamples);
                     }
                  }
                }
            }
            
            /*System.out.println("Computing cuttof");
            cuttof1=rr1.findCutoff(naex, startPerc, endPerc, oldIndexRR1, endIndexRR1, minCovElements1,maxCovElements1, appset.minSupport,appset.maxSupport,appset.numTargets);
            cuttof=rr.findCutoff(naex, startPerc, endPerc, oldIndexRR, endIndexRR, minCovElements,maxCovElements, appset.minSupport,appset.maxSupport,appset.numTargets);
   */
            
            
           // System.out.println("Cuttof: "+minCovElements[0]);
           // System.out.println("Cuttof1: "+minCovElements1[0]);

            /*if(cuttof==-1 || cuttof1==-1){
                //System.out.println("Rule count: 0");
               // return;
                continue;
            }*/
           
          
         if(!appset.useSplitTesting==true){    
          if(appset.system.equals("windows")){         
                dsc=new DataSetCreator(appset.outFolderPath+"\\Jinput.arff");
                dsc1=new DataSetCreator(appset.outFolderPath+"\\Jinput.arff");
          }
          else{
                dsc=new DataSetCreator(appset.outFolderPath+"/Jinput.arff");
                dsc1=new DataSetCreator(appset.outFolderPath+"/Jinput.arff");
          }
         }
         else{
              if(appset.trainFileName.equals("") || appset.testFileName.equals("")){
                     if(appset.system.equals("windows")){    
                             dsc=new DataSetCreator(appset.outFolderPath+"\\JinputTrain.arff");
                             dsc1=new DataSetCreator(appset.outFolderPath+"\\JinputTrain.arff");
                         }
                     else{
                             dsc=new DataSetCreator(appset.outFolderPath+"/JinputTrain.arff");
                             dsc1=new DataSetCreator(appset.outFolderPath+"/JinputTrain.arff"); 
                }
             }
              else{
                  if(appset.system.equals("windows")){    
                             dsc=new DataSetCreator(appset.outFolderPath+"\\"+appset.trainFileName);
                             dsc1=new DataSetCreator(appset.outFolderPath+"\\"+appset.trainFileName);
                         }
                     else{
                             dsc=new DataSetCreator(appset.outFolderPath+"/"+appset.trainFileName);
                             dsc1=new DataSetCreator(appset.outFolderPath+"/"+appset.trainFileName); 
                }
              }
         }
    
            try{
        dsc.readDataset();
        }
        catch(IOException e){
            e.printStackTrace();
        }
            naex=dsc.data.getNbRows();
        //read dataset for cicle 2
        try{
        dsc1.readDataset();
        }
        catch(IOException e){
            e.printStackTrace();
        }
            
            //create and modify settings for cicle 1
          if(leftSide==1 && (endIndexRR1-oldIndexRR1)>z*appset.numTargets){
              //createSettings
            if(appset.system.equals("windows")){    
             set=new SettingsReader(appset.outFolderPath+"\\view2tmp.s",appset.outFolderPath+"\\view2.s");
             set.setDataFilePath(appset.outFolderPath+"\\Jinputnew.arff");
            }
            else{
                 set=new SettingsReader(appset.outFolderPath+"/view2tmp.s",appset.outFolderPath+"/view2.s");
                 set.setDataFilePath(appset.outFolderPath+"/Jinputnew.arff"); 
            }
             if(appset.numSupplementTrees>0){
                 if(appset.system.equals("windows")){
                     setF=new SettingsReader(appset.outFolderPath+"\\view2tmpF.s",appset.outFolderPath+"\\view2.s");
                     setF.setDataFilePath(appset.outFolderPath+"\\Jinputnew.arff");
                 }
                 else{
                     setF=new SettingsReader(appset.outFolderPath+"/view2tmpF.s",appset.outFolderPath+"/view2.s");
                     setF.setDataFilePath(appset.outFolderPath+"/Jinputnew.arff");
                 }
             }
             /*if(z!=0)
                 set.changeSeed();*/
             
             int endTmp=0;
             if((z+1)*appset.numTargets>(endIndexRR1-oldIndexRR1))
                 endTmp=endIndexRR1;
             else endTmp=(z+1)*appset.numTargets+oldIndexRR1;
             int startIndexRR1=oldIndexRR1+z*appset.numTargets;
             
             for(int i=startIndexRR1;i<endTmp;i++) //do on the fly when reading rules
                    if( rr1.rules.get(i).elements.size()>=appset.minSupport) //do parameters analysis in this step
                        nARules++;
             set.ModifySettings(nARules,dsc.schema.getNbAttributes());
             if(appset.numSupplementTrees>0)
                setF.ModifySettingsF(nARules,dsc.schema.getNbAttributes(),appset);
       //modify settings
            /* for(int i=oldIndexRR1;i<endIndexRR1;i++) //do on the fly when reading rules
                    if(rr1.rules.get(i).elements.size()<=naex*endPerc && rr1.rules.get(i).elements.size()>=naex*startPerc && rr1.rules.get(i).elements.size()>=minCovElements1[0] && rr1.rules.get(i).elements.size()<=maxCovElements1[0]) //do parameters analysis in this step
                        nARules++;
             set.ModifySettings(nARules,dsc.schema.getNbAttributes());*/
          }
          else if(rightSide==1 && (endIndexRR-oldIndexRR)>z*appset.numTargets){
              if(appset.system.equals("windows")){
                 set=new SettingsReader(appset.outFolderPath+"\\view1tmp.s",appset.outFolderPath+"\\view1.s");
                 set.setDataFilePath(appset.outFolderPath+"\\Jinputnew.arff");
                    }
              else{
                 set=new SettingsReader(appset.outFolderPath+"/view1tmp.s",appset.outFolderPath+"/view1.s");
                 set.setDataFilePath(appset.outFolderPath+"/Jinputnew.arff");  
              }
                 if(appset.numSupplementTrees>0){
                     if(appset.system.equals("windows")){
                        setF=new SettingsReader(appset.outFolderPath+"\\view1tmpF.s",appset.outFolderPath+"\\view1.s");
                        setF.setDataFilePath(appset.outFolderPath+"\\Jinputnew.arff");
                     }
                     else{
                         setF=new SettingsReader(appset.outFolderPath+"/view1tmpF.s",appset.outFolderPath+"/view1.s");
                         setF.setDataFilePath(appset.outFolderPath+"/Jinputnew.arff");
                     }
                 }
                 /* if(z!=0)
                     set.changeSeed();*/

                  int endTmp=0;
             if((z+1)*appset.numTargets>(endIndexRR-oldIndexRR))
                 endTmp=endIndexRR;
             else endTmp=(z+1)*appset.numTargets+oldIndexRR;
                 
             int startIndexRR=oldIndexRR+z*appset.numTargets;
             
                 for(int i=startIndexRR;i<endTmp;i++) //do on the fly when reading rules
                        if(rr.rules.get(i).elements.size()>=appset.minSupport)
                            nARules++;
                set.ModifySettings(nARules,dsc1.schema.getNbAttributes());
                if(appset.numSupplementTrees>0)
                    setF.ModifySettingsF(nARules,dsc1.schema.getNbAttributes(),appset);
                 
                 /*for(int i=oldIndexRR;i<endIndexRR;i++) //do on the fly when reading rules
                        if(rr.rules.get(i).elements.size()<=naex*endPerc && rr.rules.get(i).elements.size()>=naex*startPerc && rr.rules.get(i).elements.size()>=minCovElements[0] && rr.rules.get(i).elements.size()<=maxCovElements[0])
                            nARules++;
                set.ModifySettings(nARules,dsc1.schema.getNbAttributes());*/
          }

            //create and modify settings for cicle 2
        if(leftSide1==1 && (endIndexRR1-oldIndexRR1)>z*appset.numTargets){
            if(appset.system.equals("windows")){
                set1=new SettingsReader(appset.outFolderPath+"\\view2tmp1.s",appset.outFolderPath+"\\view2.s");
                set1.setDataFilePath(appset.outFolderPath+"\\Jinputnew1.arff");
            }
            else{
                set1=new SettingsReader(appset.outFolderPath+"/view2tmp1.s",appset.outFolderPath+"/view2.s");
                set1.setDataFilePath(appset.outFolderPath+"/Jinputnew1.arff");
            }
            if(appset.numSupplementTrees>0){
                if(appset.system.equals("windows")){
                     setF1=new SettingsReader(appset.outFolderPath+"\\view2tmpF1.s",appset.outFolderPath+"\\view2.s");
                     setF1.setDataFilePath(appset.outFolderPath+"\\Jinputnew1.arff");
                }
                else{
                     setF1=new SettingsReader(appset.outFolderPath+"/view2tmpF1.s",appset.outFolderPath+"/view2.s");
                     setF1.setDataFilePath(appset.outFolderPath+"/Jinputnew1.arff");
                }
            }
             /* if(z!=0)
                 set1.changeSeed();*/

             int endTmp=0;
             if((z+1)*appset.numTargets>(endIndexRR1-oldIndexRR1))
                 endTmp=endIndexRR1;
             else endTmp=oldIndexRR1+(z+1)*appset.numTargets;
             
             int startIndexRR1=oldIndexRR1+z*appset.numTargets;
             
             for(int i=startIndexRR1;i<endTmp;i++)
                  if(rr1.rules.get(i).elements.size()>=appset.minSupport)
                       nARules1++;
             set1.ModifySettings(nARules1,dsc.schema.getNbAttributes());
             if(appset.numSupplementTrees>0)
                setF1.ModifySettingsF(nARules1,dsc.schema.getNbAttributes(),appset);
             
            /* for(int i=oldIndexRR1;i<endIndexRR1;i++)
                  if(rr1.rules.get(i).elements.size()<=naex*endPerc && rr1.rules.get(i).elements.size()>=naex*startPerc && rr1.rules.get(i).elements.size()>=minCovElements1[0] && rr1.rules.get(i).elements.size()<=maxCovElements1[0])
                       nARules1++;
             set1.ModifySettings(nARules1,dsc.schema.getNbAttributes());*/
          }
          else if(rightSide1==1 && (endIndexRR-oldIndexRR)>z*appset.numTargets){

            if(appset.system.equals("windows")){  
              set1=new SettingsReader(appset.outFolderPath+"\\view1tmp1.s",appset.outFolderPath+"\\view1.s");
              set1.setDataFilePath(appset.outFolderPath+"\\Jinputnew1.arff");
            }
            else{
               set1=new SettingsReader(appset.outFolderPath+"/view1tmp1.s",appset.outFolderPath+"/view1.s");
               set1.setDataFilePath(appset.outFolderPath+"/Jinputnew1.arff"); 
            }
              if(appset.numSupplementTrees>0){
                  if(appset.system.equals("windows")){ 
                        setF1=new SettingsReader(appset.outFolderPath+"\\view1tmpF1.s",appset.outFolderPath+"\\view1.s");
                        setF1.setDataFilePath(appset.outFolderPath+"\\Jinputnew1.arff");
                  }
                  else{
                        setF1=new SettingsReader(appset.outFolderPath+"/view1tmpF1.s",appset.outFolderPath+"/view1.s");
                        setF1.setDataFilePath(appset.outFolderPath+"/Jinputnew1.arff"); 
                  }
              }
              /*if(z!=0)
                    set1.changeSeed();*/

              int endTmp=0;
             if((z+1)*appset.numTargets>(endIndexRR-oldIndexRR))
                 endTmp=endIndexRR;
             else endTmp=(z+1)*appset.numTargets+oldIndexRR;
              
             int startIndexRR=oldIndexRR+z*appset.numTargets;
             
              for(int i=startIndexRR;i<endTmp;i++)
                  if(rr.rules.get(i).elements.size()>=appset.minSupport)
                        nARules1++;
              set1.ModifySettings(nARules1,dsc1.schema.getNbAttributes());
              if(appset.numSupplementTrees>0)
                    setF1.ModifySettingsF(nARules1,dsc1.schema.getNbAttributes(),appset);
              
              /*for(int i=oldIndexRR;i<endIndexRR;i++)
                  if(rr.rules.get(i).elements.size()<=naex*endPerc && rr.rules.get(i).elements.size()>=naex*startPerc && rr.rules.get(i).elements.size()>=minCovElements[0] && rr.rules.get(i).elements.size()<=maxCovElements[0])
                        nARules1++;
              set1.ModifySettings(nARules1,dsc1.schema.getNbAttributes());*/
          }

        RuleReader ItRules=new RuleReader();
        RuleReader ItRules1=new RuleReader();
        RuleReader ItRulesF=new RuleReader();
        RuleReader ItRulesF1=new RuleReader();

       //modify dataset for cicle 1
       if(leftSide==1 && (endIndexRR1-oldIndexRR1)>z*appset.numTargets ){
           int startIndexRR1=oldIndexRR1+z*appset.numTargets;
           int endTmp=0;
             if((z+1)*appset.numTargets>(endIndexRR1-oldIndexRR1))
                 endTmp=endIndexRR1;
             else endTmp=(z+1)*appset.numTargets+oldIndexRR1;
        try{
         if(appset.treeTypes.get(1)==1/*appset.typeOfRSTrees==1*/) 
             if(appset.system.equals("windows")){ 
                 dsc.modifyDatasetS(startIndexRR1,endTmp,rr1,appset.outFolderPath+"\\Jinputnew.arff",fid,appset);
             }
             else{
                dsc.modifyDatasetS(startIndexRR1,endTmp,rr1,appset.outFolderPath+"/Jinputnew.arff",fid,appset); 
             }
                //dsc.modifyDatasetS(nARules, startPerc, endPerc, oldIndexRR1, endIndexRR1, minCovElements1[0],maxCovElements1[0], rr1,appset.outFolderPath+"\\Jinputnew.arff"/*"C:\\Users\\matej\\Documents\\Redescription mining with CLUS\\UNCTADall3testnew.arff"*/,fid);
         else if(appset.treeTypes.get(1)==0/*appset.typeOfRSTrees==0*/)
             if(appset.system.equals("windows")){ 
                 dsc.modifyDatasetCat(startIndexRR1,endTmp,rr1,appset.outFolderPath+"\\Jinputnew.arff",fid,appset);
             }
             else{
                dsc.modifyDatasetCat(startIndexRR1,endTmp,rr1,appset.outFolderPath+"/Jinputnew.arff",fid,appset); 
             }
                 //dsc.modifyDatasetCat(nARules, startPerc, endPerc, oldIndexRR1, endIndexRR1, minCovElements1[0],maxCovElements1[0], rr1,appset.outFolderPath+"\\Jinputnew.arff"/*"C:\\Users\\matej\\Documents\\Redescription mining with CLUS\\UNCTADall3testnew.arff"*/,fid);
        }
        catch(IOException e){
            e.printStackTrace();
        }
       }
       else if(rightSide==1 && (endIndexRR-oldIndexRR)>z*appset.numTargets){
           int endTmp=0;
             if((z+1)*appset.numTargets>(endIndexRR-oldIndexRR))
                 endTmp=endIndexRR;
             else endTmp=(z+1)*appset.numTargets+oldIndexRR;
              
             int startIndexRR=oldIndexRR+z*appset.numTargets;
             
         try{
             if(appset.treeTypes.get(0)==1/*appset.typeOfLSTrees==1*/)
                 if(appset.system.equals("windows")){ 
                    dsc.modifyDatasetS(startIndexRR,endTmp,rr,appset.outFolderPath+"\\Jinputnew.arff",fid,appset);
                 }
                 else{
                     dsc.modifyDatasetS(startIndexRR,endTmp,rr,appset.outFolderPath+"/Jinputnew.arff",fid,appset); 
                 }
                //dsc.modifyDatasetS(nARules, startPerc, endPerc, oldIndexRR, endIndexRR, minCovElements[0],maxCovElements[0], rr,appset.outFolderPath+"\\Jinputnew.arff"/*"C:\\Users\\matej\\Documents\\Redescription mining with CLUS\\UNCTADall3testnew.arff"*/,fid);
             else if(appset.treeTypes.get(0)==0/*appset.typeOfLSTrees==0*/)
                  if(appset.system.equals("windows")){ 
                    dsc.modifyDatasetCat(startIndexRR,endTmp ,rr,appset.outFolderPath+"\\Jinputnew.arff",fid,appset);
                  }
                  else{
                     dsc.modifyDatasetCat(startIndexRR,endTmp ,rr,appset.outFolderPath+"/Jinputnew.arff",fid,appset); 
                  }
               // dsc.modifyDatasetCat(nARules, startPerc, endPerc, oldIndexRR, endIndexRR, minCovElements[0],maxCovElements[0], rr,appset.outFolderPath+"\\Jinputnew.arff"/*"C:\\Users\\matej\\Documents\\Redescription mining with CLUS\\UNCTADall3testnew.arff"*/,fid);
        }
        catch(IOException e){
            e.printStackTrace();
        }  
       }

       //modify dataset for cicle 2
       if(leftSide1==1 && (endIndexRR1-oldIndexRR1)>z*appset.numTargets){
           int startIndexRR1=oldIndexRR1+z*appset.numTargets;
           int endTmp=0;
             if((z+1)*appset.numTargets>(endIndexRR1-oldIndexRR1))
                 endTmp=endIndexRR1;
             else endTmp=(z+1)*appset.numTargets+oldIndexRR1;
             
        try{
            if(appset.treeTypes.get(1)==1)
                if(appset.system.equals("windows")){ 
                    dsc1.modifyDatasetS(startIndexRR1,endTmp, rr1,appset.outFolderPath+"\\Jinputnew1.arff",fid,appset);
                }
                else{
                   dsc1.modifyDatasetS(startIndexRR1,endTmp, rr1,appset.outFolderPath+"/Jinputnew1.arff",fid,appset); 
                }
                //dsc1.modifyDatasetS(nARules1, startPerc, endPerc, oldIndexRR1, endIndexRR1, minCovElements1[0],maxCovElements1[0], rr1,appset.outFolderPath+"\\Jinputnew1.arff"/*"C:\\Users\\matej\\Documents\\Redescription mining with CLUS\\UNCTADall3testnew1.arff"*/,fid);
            else if(appset.treeTypes.get(1)==0/*appset.typeOfRSTrees==0*/)
                if(appset.treeTypes.get(1)==1)
                     dsc1.modifyDatasetCat(startIndexRR1,endTmp, rr1,appset.outFolderPath+"\\Jinputnew1.arff",fid,appset);
                else
                     dsc1.modifyDatasetCat(startIndexRR1,endTmp, rr1,appset.outFolderPath+"/Jinputnew1.arff",fid,appset);
                //dsc1.modifyDatasetCat(nARules1, startPerc, endPerc, oldIndexRR1, endIndexRR1, minCovElements1[0],maxCovElements1[0], rr1,appset.outFolderPath+"\\Jinputnew1.arff"/*"C:\\Users\\matej\\Documents\\Redescription mining with CLUS\\UNCTADall3testnew1.arff"*/,fid);
        }
        catch(IOException e){
            e.printStackTrace();
        }
       }
       else if(rightSide1==1 && (endIndexRR-oldIndexRR)>z*appset.numTargets){
           int endTmp=0;
             if((z+1)*appset.numTargets>(endIndexRR-oldIndexRR))
                 endTmp=endIndexRR;
             else endTmp=(z+1)*appset.numTargets+oldIndexRR;
              
             int startIndexRR=oldIndexRR+z*appset.numTargets;
             
         try{
             if(appset.treeTypes.get(0)==1/*appset.typeOfLSTrees==1*/)
                 if(appset.system.equals("windows"))
                    dsc1.modifyDatasetS(startIndexRR,endTmp,rr,appset.outFolderPath+"\\Jinputnew1.arff",fid,appset);
                 else
                    dsc1.modifyDatasetS(startIndexRR,endTmp,rr,appset.outFolderPath+"/Jinputnew1.arff",fid,appset); 
                //dsc1.modifyDatasetS(nARules1, startPerc, endPerc, oldIndexRR, endIndexRR, minCovElements[0],maxCovElements[0] ,rr,appset.outFolderPath+"\\Jinputnew1.arff"/*"C:\\Users\\matej\\Documents\\Redescription mining with CLUS\\UNCTADall3testnew1.arff"*/,fid);
             else if(appset.treeTypes.get(0)==0/*appset.typeOfRSTrees==0*/)
                 if(appset.system.equals("windows"))
                 dsc1.modifyDatasetCat(startIndexRR,endTmp,rr,appset.outFolderPath+"\\Jinputnew1.arff",fid,appset);
             else
                   dsc1.modifyDatasetCat(startIndexRR,endTmp,rr,appset.outFolderPath+"/Jinputnew1.arff",fid,appset);   
                //dsc1.modifyDatasetCat(nARules1, startPerc, endPerc, oldIndexRR, endIndexRR, minCovElements[0],maxCovElements[0] ,rr,appset.outFolderPath+"\\Jinputnew1.arff"/*"C:\\Users\\matej\\Documents\\Redescription mining with CLUS\\UNCTADall3testnew1.arff"*/,fid);
        }
        catch(IOException e){
            e.printStackTrace();
        }
       }
       
       dsc=null;
       dsc1=null;
      
       if((appset.useNC.get(0)==true && rightSide==1 && appset.networkInit==false && appset.useNetworkAsBackground==false) || (appset.useNC.get(1)==true && leftSide==1 && appset.networkInit==false && appset.useNetworkAsBackground==false)){ 
           if(appset.system.equals("windows")){  
             nclMat.resetFile(new File(appset.outFolderPath+"\\distances.csv"));
             nclMat.writeToFile(new File(appset.outFolderPath+"\\distances.csv"), fid,appset);
            }
           else{
              nclMat.resetFile(new File(appset.outFolderPath+"/distances.csv"));
             nclMat.writeToFile(new File(appset.outFolderPath+"/distances.csv"), fid,appset); 
           }
       }
       
        //run the second proces on new data
        // iterate until convergence (no new rules, or very small amount obtained)
         if(leftSide==1 && (endIndexRR1-oldIndexRR1)>z*appset.numTargets){
             exec.run(appset.javaPath, appset.clusPath, appset.outFolderPath, "view2tmp.s"/*"wbtmp.s"*/, 0,appset.clusteringMemory);//was 1 for rules before
             if(appset.numSupplementTrees>0)
                exec.run(appset.javaPath, appset.clusPath, appset.outFolderPath, "view2tmpF.s"/*"wbtmp.s"*/, 0,appset.clusteringMemory);//was 1 for rules before
             System.out.println("Process 2 side 1 finished!");
         }
         else if(rightSide==1 && (endIndexRR-oldIndexRR)>z*appset.numTargets){
             exec.run(appset.javaPath, appset.clusPath, appset.outFolderPath, "view1tmp.s"/*"unctadtmp.s"*/, 0,appset.clusteringMemory);
             if(appset.numSupplementTrees>0)
                 exec.run(appset.javaPath, appset.clusPath, appset.outFolderPath, "view1tmpF.s"/*"unctadtmp.s"*/, 0,appset.clusteringMemory);
             System.out.println("Process 1 side 1 finished!");
         }

        if((appset.useNC.get(0)==true && rightSide1==1 && appset.networkInit==false && appset.useNetworkAsBackground==false) || (appset.useNC.get(1)==true && leftSide1==1 && appset.networkInit==false && appset.useNetworkAsBackground==false)){ 
              if(appset.system.equals("windows")){ 
                 nclMat1.resetFile(new File(appset.outFolderPath+"\\distances.csv"));
                 nclMat1.writeToFile(new File(appset.outFolderPath+"\\distances.csv"), fid,appset);
              }
              else{
                 nclMat1.resetFile(new File(appset.outFolderPath+"/distances.csv"));
                 nclMat1.writeToFile(new File(appset.outFolderPath+"/distances.csv"), fid,appset);
              }
        }
         
         //run the second proces for cicle 2 on new data
        // iterate until convergence (no new rules, or very small amount obtained)
         if(leftSide1==1 && (endIndexRR1-oldIndexRR1)>z*appset.numTargets){
             exec.run(appset.javaPath, appset.clusPath, appset.outFolderPath,"view2tmp1.s" /*"wbtmpS1.s"*/, 0,appset.clusteringMemory);
             if(appset.numSupplementTrees>0)
                exec.run(appset.javaPath, appset.clusPath, appset.outFolderPath,"view2tmpF1.s" /*"wbtmpS1.s"*/, 0,appset.clusteringMemory);
             System.out.println("Process 2 side 2 finished!");
         }
         else if(rightSide1==1 && (endIndexRR-oldIndexRR)>z*appset.numTargets){
             exec.run(appset.javaPath, appset.clusPath, appset.outFolderPath,"view1tmp1.s"/*"unctadtmpS1.s"*/ , 0,appset.clusteringMemory);
             if(appset.numSupplementTrees>0)
             exec.run(appset.javaPath, appset.clusPath, appset.outFolderPath,"view1tmpF1.s"/*"unctadtmpS1.s"*/ , 0,appset.clusteringMemory);
             System.out.println("Process 1 side 2 finished!");
         }

       //extract rules for cicle 1
        String input="", inputF="";
        if(leftSide==1 && (endIndexRR1-oldIndexRR1)>z*appset.numTargets){
            if(appset.system.equals("windows")){ 
                 input=appset.outFolderPath+"\\view2tmp.out";
                 inputF=appset.outFolderPath+"\\view2tmpF.out";
            }
            else{
               input=appset.outFolderPath+"/view2tmp.out";
                 inputF=appset.outFolderPath+"/view2tmpF.out"; 
            }
        }
        else if(rightSide==1 && (endIndexRR-oldIndexRR)>z*appset.numTargets){
            if(appset.system.equals("windows")){ 
                 input=appset.outFolderPath+"\\view1tmp.out";
                    if(appset.numSupplementTrees>0) 
                        inputF=appset.outFolderPath+"\\view1tmpF.out";
            }
            else{
                input=appset.outFolderPath+"/view1tmp.out";
                    if(appset.numSupplementTrees>0) 
                        inputF=appset.outFolderPath+"/view1tmpF.out";
            }
        }
        //String input1="C:\\Users\\matej\\Documents\\Redescription mining with CLUS\\unctad.out";
       int newRules=0;
       if((leftSide==1 && (endIndexRR1-oldIndexRR1)>z*appset.numTargets) || (rightSide==1 && (endIndexRR-oldIndexRR)>z*appset.numTargets)){
            ItRules.extractRules(input,fid,datJ,appset);
            ItRules.setSize();
            if(appset.numSupplementTrees>0){
                ItRulesF.extractRules(inputF,fid,datJ,appset);
                ItRulesF.setSize();
            }
       }
        if(leftSide==1 && (endIndexRR1-oldIndexRR1)>z*appset.numTargets){
            if(z==0)
                newRules=rr.addnewRulesC(ItRules, appset.numnewRAttr,1);
            else
                newRules=rr.addnewRulesC(ItRules, appset.numnewRAttr,0);
            if(appset.numSupplementTrees>0)
                rr.addnewRulesCF(ItRulesF, appset.numnewRAttr); 
       // rr.extractRules(input);
        }
        else if(rightSide==1 && (endIndexRR-oldIndexRR)>z*appset.numTargets){
            if(z==0)
                newRules=rr1.addnewRulesC(ItRules, appset.numnewRAttr,1);
            else
                newRules=rr1.addnewRulesC(ItRules, appset.numnewRAttr, 0);
            if(appset.numSupplementTrees>0)
                rr1.addnewRulesCF(ItRulesF, appset.numnewRAttr); 
            //rr1.extractRules(input);
        }     
         //extract rules for cicle 2
        input=""; inputF="";
        if(leftSide1==1 && (endIndexRR1-oldIndexRR1)>z*appset.numTargets){
            if(appset.system.equals("windows")){ 
                    input=appset.outFolderPath+"\\view2tmp1.out";
                    inputF=appset.outFolderPath+"\\view2tmpF1.out";
            }
            else{
                    input=appset.outFolderPath+"/view2tmp1.out";
                    inputF=appset.outFolderPath+"/view2tmpF1.out"; 
            }
        }
        else if(rightSide1==1 && (endIndexRR-oldIndexRR)>z*appset.numTargets){
            if(appset.system.equals("windows")){ 
                    input=appset.outFolderPath+"\\view1tmp1.out";
                    inputF=appset.outFolderPath+"\\view1tmpF1.out";
            }
            else{
                input=appset.outFolderPath+"/view1tmp1.out";
                inputF=appset.outFolderPath+"/view1tmpF1.out";
            }
        }
        //String input1="C:\\Users\\matej\\Documents\\Redescription mining with CLUS\\unctad.out";
       int newRules1=0;
       if((leftSide1==1 && (endIndexRR1-oldIndexRR1)>z*appset.numTargets) || (rightSide1==1 && (endIndexRR-oldIndexRR)>z*appset.numTargets)){
        ItRules1.extractRules(input,fid,datJ,appset);
        ItRules1.setSize();
        if(appset.numSupplementTrees>0){
             ItRulesF1.extractRules(inputF,fid,datJ,appset);
             ItRulesF1.setSize();
        }
       }
        if(leftSide1==1 && (endIndexRR1-oldIndexRR1)>z*appset.numTargets){
            if(z==0)
                    newRules1=rr.addnewRulesC(ItRules1, appset.numnewRAttr,1);
            else
                    newRules1=rr.addnewRulesC(ItRules1, appset.numnewRAttr, 0);
            if(appset.numSupplementTrees>0)
                rr.addnewRulesCF(ItRulesF1, appset.numnewRAttr); 
       // rr.extractRules(input);
        }
        else if(rightSide1==1 && (endIndexRR-oldIndexRR)>z*appset.numTargets){
            if(z==0)
                    newRules1=rr1.addnewRulesC(ItRules1, appset.numnewRAttr,1);
            else
                    newRules1=rr1.addnewRulesC(ItRules1, appset.numnewRAttr,0);
            if(appset.numSupplementTrees>0)
             rr1.addnewRulesCF(ItRulesF1, appset.numnewRAttr);
            //rr1.extractRules(input);
        }

        System.out.println("New rules cicle 1: "+newRules);
        System.out.println("New rules cicle 2: "+newRules1);
       }
        //add the redescription creaton code
       
       if(appset.optimizationType==0){ 
        if(appset.useJoin){
            //add computation of rule support if bagging
            newRedescriptions=rs.createGuidedJoinBasic(rr1, rr, jsN, appset, oldIndexRR1, oldIndexRR, RunInd,oom,fid,datJ, elemFreq, attrFreq, redScores,redScoresAtt,redDistCoverage,redDistCoverageAt, redDistNetwork, targetAtScore, Statistics, maxDiffScoreDistribution,nclMatInit,0);
            rr.removeElements(rr.newRuleIndex);
            rr1.removeElements(rr1.newRuleIndex);
            if(appset.numSupplementTrees>0){
                 rr.removeRulesCF();
                 rr1.removeRulesCF();
            }
        }
        else if(!appset.useJoin){
            newRedescriptions=rs.createGuidedNoJoinBasic(rr1, rr, jsN, appset, oldIndexRR1, oldIndexRR, RunInd,oom,fid,datJ, elemFreq, attrFreq, redScores,redScoresAtt,redDistCoverage,redDistCoverageAt, redDistNetwork, targetAtScore, Statistics, maxDiffScoreDistribution,nclMatInit,0);
            rr.removeElements(rr.newRuleIndex);
            rr1.removeElements(rr1.newRuleIndex);
            if(appset.numSupplementTrees>0){
                rr.removeRulesCF();
                rr1.removeRulesCF();
            }
        }
       }
       else{
           if(appset.useJoin){
            newRedescriptions=rs.createGuidedJoinExt(rr1, rr, jsN, appset, oldIndexRR1, oldIndexRR, RunInd, oom,fid,datJ);//sqitch sides of rules
            rr.removeElements(rr.newRuleIndex);
            rr1.removeElements(rr1.newRuleIndex);
        }
        else if(!appset.useJoin){
            newRedescriptions=rs.createGuidedNoJoinExt(rr1, rr, jsN, appset, oldIndexRR1, oldIndexRR, RunInd,oom,fid,datJ);
            rr.removeElements(rr.newRuleIndex);
            rr1.removeElements(rr1.newRuleIndex);
        }
       }
       
      /* if(rs.redescriptions.size()==2)
           return;*/

         it++;
        
        System.out.println("New redescriptions: "+newRedescriptions);
   
        
        if(leftSide==1){
            leftSide=0;
            rightSide=1;
        }
        else if(rightSide==1){
            rightSide=0;
            leftSide=1;
        }
        if(leftSide1==1){
            leftSide1=0;
            rightSide1=1;
        }
        else if(rightSide1==1){
            rightSide1=0;
            leftSide1=1;
        }
        RunInd++;
        System.out.println("Running index: "+RunInd);
        }
      }
       
        System.out.println("Redescription size main: "+rs.redescriptions.size());
        
        //removing all redescriptions with inadequate minSupport and minJS
        rs.remove(appset);
         
        System.out.println("Redescription size main after remove: "+rs.redescriptions.size());
        
        //filtering
      // rs.filter(appset, rr, rr1,fid,datJ); // think about what we want and if we need it
        
        System.out.println("Redescription size main after filter: "+rs.redescriptions.size());
       
      int numFullRed=0;
        //computing pVal...
        numFullRed=rs.computePVal(datJ,fid);
        rs.removePVal(appset);
        
         System.out.println("Redescription size main after Pvalremove: "+rs.redescriptions.size());
        
        System.out.println("Found "+numFullRed+" redescriptions with JS=1.0 and minsupport>"+appset.minSupport);
        System.out.println("Found "+rs.redescriptions.size()+" redescriptions with JS>"+appset.minJS);
        
        
        
        //add sets here
        //allSets2W.add(rs);
        
        RedescriptionSet nset = new RedescriptionSet();
        Redescription rnew = null;
                 rs.adaptSet(datJ, fid, 0);
        for(int ir=0;ir<rs.redescriptions.size();ir++){
           
            rs.redescriptions.get(ir).createRuleString(fid);
                 rnew = new Redescription(rs.redescriptions.get(ir),ww1,ww2,allViews.size());
                nset.redescriptions.add(rnew);
         }
        
      allSetsIncomplete.add(nset);
     // Result.redescriptions.clear();
      rs.redescriptions.clear();
      rr.rules.clear();
      rr1.rules.clear();
      
      FileDeleter del=new FileDeleter();
     if(appset.system.equals("windows")){  
      del.setPath(appset.outFolderPath+"\\Jinputnew.arff");
      del.delete();
      del.setPath(appset.outFolderPath+"\\Jinputnew1.arff");
      del.delete();
      del.setPath(appset.outFolderPath+"\\Jinput.arff");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view1tmp.s");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view2tmp.s");
      del.delete();
       del.setPath(appset.outFolderPath+"\\view3tmp.s");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view1tmp1.s");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view2tmp1.s");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view1tmp.out");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view1tmp.model");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view2tmp.out");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view2tmp.model");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view1tmp1.out");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view1tmp1.model");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view2tmp1.out");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view2tmp1.model");
      del.delete();
      ///
      del.setPath(appset.outFolderPath+"\\view1tmpF1.s");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view2tmpF1.s");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view1tmpF.out");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view1tmpF.model");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view2tmpF.out");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view2tmpF.model");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view1tmpF1.out");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view1tmpF1.model");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view2tmpF1.out");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view2tmpF1.model");
      del.delete();
      ///
      del.setPath(appset.outFolderPath+"\\view3tmp.out");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view3tmp.model");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view1.s");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view1.out");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view1.model");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view2.s");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view2.out");
      del.delete();
      del.setPath(appset.outFolderPath+"\\view2.model");
      del.delete();
     }
     else{
       del.setPath(appset.outFolderPath+"/Jinputnew.arff");
      del.delete();
      del.setPath(appset.outFolderPath+"/Jinputnew1.arff");
      del.delete();
      del.setPath(appset.outFolderPath+"/Jinput.arff");
      del.delete();
      del.setPath(appset.outFolderPath+"/view1tmp.s");
      del.delete();
      del.setPath(appset.outFolderPath+"/view2tmp.s");
      del.delete();
       del.setPath(appset.outFolderPath+"/view3tmp.s");
      del.delete();
      del.setPath(appset.outFolderPath+"/view1tmp1.s");
      del.delete();
      del.setPath(appset.outFolderPath+"/view2tmp1.s");
      del.delete();
      del.setPath(appset.outFolderPath+"/view1tmp.out");
      del.delete();
      del.setPath(appset.outFolderPath+"/view1tmp.model");
      del.delete();
      del.setPath(appset.outFolderPath+"/view2tmp.out");
      del.delete();
      del.setPath(appset.outFolderPath+"/view2tmp.model");
      del.delete();
      del.setPath(appset.outFolderPath+"/view1tmp1.out");
      del.delete();
      del.setPath(appset.outFolderPath+"/view1tmp1.model");
      del.delete();
      del.setPath(appset.outFolderPath+"/view2tmp1.out");
      del.delete();
      del.setPath(appset.outFolderPath+"/view2tmp1.model");
      del.delete();
      ///
      del.setPath(appset.outFolderPath+"/view1tmpF1.s");
      del.delete();
      del.setPath(appset.outFolderPath+"/view2tmpF1.s");
      del.delete();
      del.setPath(appset.outFolderPath+"/view1tmpF.out");
      del.delete();
      del.setPath(appset.outFolderPath+"/view1tmpF.model");
      del.delete();
      del.setPath(appset.outFolderPath+"/view2tmpF.out");
      del.delete();
      del.setPath(appset.outFolderPath+"/view2tmpF.model");
      del.delete();
      del.setPath(appset.outFolderPath+"/view1tmpF1.out");
      del.delete();
      del.setPath(appset.outFolderPath+"/view1tmpF1.model");
      del.delete();
      del.setPath(appset.outFolderPath+"/view2tmpF1.out");
      del.delete();
      del.setPath(appset.outFolderPath+"/view2tmpF1.model");
      del.delete();
      ///
      del.setPath(appset.outFolderPath+"/view3tmp.out");
      del.delete();
      del.setPath(appset.outFolderPath+"/view3tmp.model");
      del.delete();
      del.setPath(appset.outFolderPath+"/view1.s");
      del.delete();
      del.setPath(appset.outFolderPath+"/view1.out");
      del.delete();
      del.setPath(appset.outFolderPath+"/view1.model");
      del.delete();
      del.setPath(appset.outFolderPath+"/view2.s");
      del.delete();
      del.setPath(appset.outFolderPath+"/view2.out");
      del.delete();
      del.setPath(appset.outFolderPath+"/view2.model");
      del.delete();  
     }
    }
   }
    
    //transform all redescriptions into incomplete redescriptions
    //merge reds from all sets (and compute statistics after each merge)
    
    System.out.println("all incomplete sets size: "+allSetsIncomplete.size());
    System.out.println("maps size: "+maps.size());
    System.out.println("datasets size: "+dats.size());
    
    ArrayList<Redescription> tmp = null;
    ArrayList<Redescription> tmpRd = new ArrayList<>();
    
    for(int i = 0; i<allSetsIncomplete.size()-1; i++){
        for(int j=i+1; j<allSetsIncomplete.size();j++){
           /* if(j!=(i+1))
                continue;*/
            tmpRd = new ArrayList<>();
            System.out.println("MWSet size: "+MWSet.redescriptions.size());
            for(int z=0;z<MWSet.redescriptions.size();z++){
                for(int z1 = 0; z1<allSetsIncomplete.get(j).redescriptions.size();z1++){

                tmp= MWSet.redescriptions.get(z).merge(allSetsIncomplete.get(j).redescriptions.get(z1));//add function checkMerge to reduce the number of unecessarily created redescriptions
                for(int zz=0;zz<tmp.size();zz++){//returns 1 or 2 (how many reds possible to create)
                        tmp.get(zz).computeStatistics(dats, maps);//simulate compute statistics and create red only if OK
                                if(tmp.get(zz).JS>=appset.minJS && tmp.get(zz).elements.size()>=appset.minSupport)
                                    tmpRd.add(tmp.get(zz));
                     }
                //System.out.println("usefulComplex: "+tmpRd.size());
                }
                if(z%10000 == 0)
                System.out.println("Remaining: "+(MWSet.redescriptions.size() - z));
            }
            System.out.println("complex completed!");
            MWSet.redescriptions.addAll(tmpRd);
            System.out.println(i+" "+j);
            
            //add all from view combination
            tmpRd.clear();
           for(int z=0;z<allSetsIncomplete.get(i).redescriptions.size();z++){
                for(int z1 = 0; z1<allSetsIncomplete.get(j).redescriptions.size();z1++){

                //tmp = new Redescription(allSetsIncomplete.get(i).redescriptions.get(z));
                    //need to create less tmp redescriptions
                tmp = allSetsIncomplete.get(i).redescriptions.get(z).merge(allSetsIncomplete.get(j).redescriptions.get(z1));
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
            MWSet.redescriptions.addAll(tmpRd); 
             System.out.println(i+" "+j);
        }
    }
    
    System.out.println("Reds computed...");
    System.out.println("All reds: "+MWSet.redescriptions.size());
    //filter out all incomplete reds
    //filter out all reds not satisfying constraints
    
   // for(int i = MWSet.redescriptions.size()-1;i>=0;i--)
     //   MWSet.redescriptions.get(i).computeStatistics(dats, maps);
    
    for(int i = MWSet.redescriptions.size()-1;i>=0;i--)
        if(MWSet.redescriptions.get(i).numViews()<allViews.size() || MWSet.redescriptions.get(i).JS<appset.minJS || MWSet.redescriptions.get(i).elements.size()<appset.minSupport || MWSet.redescriptions.get(i).elements.size()>appset.maxSupport || MWSet.redescriptions.get(i).pVal>appset.maxPval)
            MWSet.redescriptions.remove(i);
    
    System.out.println("Filtering done...");
    System.out.println("Remaining reds: "+MWSet.redescriptions.size());
    
    ArrayList<Integer> toRemove = new ArrayList<>();
    Jacard js=new Jacard();
    System.out.println("Redundancy filtering started: ");
    
    int end = 0;
    
    HashSet<Integer> traversed = new HashSet<>();
    
     double percRedundancy = 0.95;
    
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
    
    MWSet.writeToFileBaseline(appset.outFolderPath+sep+appset.outputName+"Baseline.rr", startTime,appset, maps.get(0), /*ResultScore*/Double.NaN);  
      MWSet.writePlotsBaseline(appset.outFolderPath+sep+appset.outputName+"BaselineStats.csv", appset, dats.get(0));
    
      System.out.println("RSets computed and written!");
      
    /* ArrayList<RedescriptionSet> allSets2W = new ArrayList<>();
       ArrayList<RedescriptionSet> allSetsIncomplete = new ArrayList<>();
       RedescriptionSet MWSet = new RedescriptionSet();*/

     long endTime = System.currentTimeMillis();
    
     double overallTime = (endTime - startTime)/1000.0;
     
     System.out.println("Overall time: "+overallTime+" "+"s");
     
  }
}
