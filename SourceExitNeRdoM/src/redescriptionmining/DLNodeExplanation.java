/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redescriptionmining;

import clus.data.rows.DataTuple;
import clus.data.type.ClusAttrType;
import clus.data.type.NominalAttrType;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import org.javatuples.Pair;

/**
 *
 * @author matej
 */
public class DLNodeExplanation {
    static public void main(String []args){
        
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

        Mappings fid=new Mappings();
        
        DataSetCreator datJ=new DataSetCreator(appset.viewInputPaths, appset.outFolderPath,appset);

        DataSetCreator datJFull=null;
        DataSetCreator datJTest=null;
        NHMCDistanceMatrix nclMatInit=null;
       
           if(appset.system.equals("windows"))
            fid.createIndex(appset.outFolderPath+"\\Jinput.arff");
        else
            fid.createIndex(appset.outFolderPath+"/Jinput.arff");
        
         Random r=new Random();
        //RedescriptionSet rs=new RedescriptionSet();
         RedescriptionSet rtmp = new RedescriptionSet();
        RuleReader rr=new RuleReader();
        RuleReader rr1=new RuleReader();
         RuleReader rrFA=new RuleReader();
         boolean oom[]= new boolean[1];
         Jacard jsN[]=new Jacard[3];
           for(int i=0;i<jsN.length;i++)
            jsN[i]=new Jacard();
         
        int elemFreq[]=null;
        int attrFreq[]=null;
        ArrayList<Double> redScores=null;
        ArrayList<Double> redScoresAtt=null;
        ArrayList<Double> targetAtScore=null;
        ArrayList<Double> redDistCoverage=null;
        ArrayList<Double> redDistCoverageAt=null;
         double Statistics[]={0.0,0.0,0.0};//previousMedian - 0, numberIterationsStable - 1, minDifference - 2
         ArrayList<Double> maxDiffScoreDistribution = null;
          ArrayList<Double> redDistNetwork=null;
      
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
            if(appset.attributeImportance!=0)
                targetAtScore.add(Double.NaN);
        }
      }
        
         DataSetCreator datJInit=null;
          
       if(!appset.useSplitTesting){ 
        if(appset.initClusteringFileName.equals("")){
            if(appset.system.equals("windows"))
                datJInit = new DataSetCreator(appset.outFolderPath+"\\Jinput.arff");
            else
                datJInit = new DataSetCreator(appset.outFolderPath+"/Jinput.arff");
        }
       }
        
                try{
        datJInit.readDataset();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        
        datJInit.W2indexs.addAll(datJ.W2indexs);
      
        System.out.println("Number of attributes: "+datJInit.schema.getNbAttributes());
        
        int numAttr = datJInit.schema.getNbAttributes()-1;
        //add blank target attribute, modify values in each iteration
        datJInit.schema.addAttrType(new NominalAttrType("target"));
         int lastNominal=datJInit.schema.getNominalAttrUse(ClusAttrType.ATTR_USE_ALL).length;
        datJInit.schema.getAttrType(datJInit.schema.getNbAttributes()-1).setArrayIndex(lastNominal++);
    
          ArrayList<DataTuple> dataList=datJInit.data.toArrayList(); 
          
          for(int el = 0;el<dataList.size();el++){
              int arow[];
              if(datJInit.schema.getNominalAttrUse(ClusAttrType.ATTR_USE_ALL).length>0)
                arow=new int[dataList.get(el).m_Ints.length+1];
              else
                  arow=new int[1];
                if(datJInit.schema.getNominalAttrUse(ClusAttrType.ATTR_USE_ALL).length>0)
                arow[dataList.get(el).m_Ints.length]=0;
              else
                  arow[0]=0;
                dataList.get(el).m_Ints=arow;
          }
        
        int sView = 0;  
        HashMap<Integer,Pair<Double,Double>> bins = null;
          
        for(int at=0;at<numAttr;at++){
            
            if(at>0){
                if(!appset.useSplitTesting){ 
        if(appset.initClusteringFileName.equals("")){
            if(appset.system.equals("windows"))
                datJInit = new DataSetCreator(appset.outFolderPath+"\\Jinput.arff");
            else
                datJInit = new DataSetCreator(appset.outFolderPath+"/Jinput.arff");
        }
       }
        
                try{
        datJInit.readDataset();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        
        datJInit.W2indexs.addAll(datJ.W2indexs);
        
        datJInit.schema.addAttrType(new NominalAttrType("target"));
         lastNominal=datJInit.schema.getNominalAttrUse(ClusAttrType.ATTR_USE_ALL).length;
        datJInit.schema.getAttrType(datJInit.schema.getNbAttributes()-1).setArrayIndex(lastNominal++);
    
          dataList=datJInit.data.toArrayList(); 
          
          for(int el = 0;el<dataList.size();el++){
              int arow[];
              if(datJInit.schema.getNominalAttrUse(ClusAttrType.ATTR_USE_ALL).length>0)
                arow=new int[dataList.get(el).m_Ints.length+1];
              else
                  arow=new int[1];
                if(datJInit.schema.getNominalAttrUse(ClusAttrType.ATTR_USE_ALL).length>0)
                arow[dataList.get(el).m_Ints.length]=0;
              else
                  arow[0]=0;
                dataList.get(el).m_Ints=arow;
          }
      
            }
            
            RedescriptionSet rttmp = new RedescriptionSet();
             rr=new RuleReader();
             rr1=new RuleReader();
             rrFA=new RuleReader();
           
            System.out.println(datJInit.schema.getAttrType(at+1).getName()+" "+datJInit.schema.getAttrType(at+1).getTypeName());
            ClusAttrType t=datJInit.schema.getAttrType(at+1);
        //can be generalized to mw by using sView = datJInit.W2indexs.get(i)
          System.out.println("Attribute t name: "+t.getName());
         if((at+1)<(datJInit.W2indexs.get(0)-2)) 
             sView = 0;
         else sView = 1;
            
        int test = 1;
        int clustMode = 1;
        
          System.out.println("Indeks view2: "+datJInit.W2indexs.get(0));
          System.out.println("Selected view: "+sView);

        if(appset.initClusteringFileName.equals(""))//zamijeniti sa initialClusteringGenAttribute
            if(clustMode == 1)
             bins = datJInit.initialClusteringGenAttribute2Bins(appset.outFolderPath,appset,datJ.schema.getNbDescriptiveAttributes(), at+1, r, fid);
        else   
            bins = datJInit.initialClusteringGenAttribute(appset.outFolderPath,appset,datJ.schema.getNbDescriptiveAttributes(), at+1, r, fid);
            //datJInit.initialClusteringGen1(appset.outFolderPath,appset,datJ.schema.getNbDescriptiveAttributes(),r);
       
        double min = Double.MAX_VALUE;
        
        for(int z=0;z<bins.keySet().size();z++){
            double bm = bins.get(z).getValue0();
            
            if(bm<min)
                min = bm;
        }
        
        int first = -1;
        for(int z=0;z<bins.keySet().size();z++){
            if(t.getTypeName().toLowerCase().contains("numeric")){
                String rule = t.getName()+" > "+bins.get(z).getValue0();
                if(bins.get(z).getValue0() == min)
                    first = 1;
                 Rule rn = new Rule(rule, fid);
                 System.out.println("f "+first+" il "+bins.get(z).getValue0()+" ih "+bins.get(z).getValue1());
                 rn.computeEntities(datJInit, fid, at, bins.get(z).getValue0(), bins.get(z).getValue1(), first, 1);
                 rn.closeInterval(datJ, fid);
                 System.out.println("Rule test support: "+rn.elements.size());
                if(sView == 0)
                    rr1.rules.add(rn);
                else rr.rules.add(rn);
            }
            else{
               
                String rule = t.getName()+" = "+fid.cattAtt.get(at).getValue1().get(z);
                Rule rn = new Rule(rule, fid);
                rn.computeEntities(datJInit, fid, at, bins.get(z).getValue0(), bins.get(z).getValue1(), first, 0);
                rn.closeInterval(datJ, fid);
                if(sView == 0)
                    rr1.rules.add(rn);
                else rr.rules.add(rn);
            }
        }
        
        //create rules from bins or target categories
        //rr->view1, rr1->view2
        //gornja granica uključena [] kod binova
        
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
                initSettings.setDataFilePath(appset.initClusteringFileName);
        }
        
        System.out.println("WIndexes size: "+datJ.W2indexs.size());
     
      if(sView == 1){  
        if(appset.system.equals("windows"))
             initSettings.setPath(appset.outFolderPath+"\\view1.s");
        else
             initSettings.setPath(appset.outFolderPath+"/view1.s");
        initSettings.createInitialSettings1(1, datJ.W2indexs.get(0), datJInit.schema.getNbAttributes(), appset);
        System.out.println("distance file size: "+appset.distanceFilePaths.size()+"");
        System.out.println("use nc: "+appset.useNC.size());
        if(appset.useNC.get(0) == false)
             initSettings.createInitialSettingsGen(0, 3, datJ.W2indexs.get(0), datJ.schema.getNbAttributes(), appset,1);
        else
             initSettings.createInitialSettingsGen(0, 4, datJ.W2indexs.get(0), datJ.schema.getNbAttributes(), appset,1);
      }
      
        ClusProcessExecutor exec=new ClusProcessExecutor();

        if(sView == 1){ 
        //RunInitW1S1
        exec.run(appset.javaPath,appset.clusPath ,appset.outFolderPath,"view1.s",0, appset.clusteringMemory);//was 1 before for rules
        System.out.println("Process 1 side 1 finished!");
        } 
        //read the rules obtained from first attribute set
          String input1="";
           String inputFa="";
       if(sView == 1){ 
          if(appset.system.equals("windows"))
             input1=appset.outFolderPath+"\\view1.out";
          else
              input1=appset.outFolderPath+"/view1.out"; 
           
           rr1.extractRules(input1,fid,datJInit,appset);
      }
      
        SettingsReader set=null;
        SettingsReader set1=null;
        SettingsReader setF=null;
        SettingsReader setF1=null;
   
      if(sView == 0){   
        //RunInitW1S2
        if(appset.system.equals("windows")){
            initSettings.setPath(appset.outFolderPath+"\\view2.s");
        }
        else{
           initSettings.setPath(appset.outFolderPath+"/view2.s"); 
        }
       
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
      }
      
      
       if(appset.numSupplementTrees>0){
            if(sView == 0){
                 if(appset.system.equals("windows")){
                     setF=new SettingsReader(appset.outFolderPath+"\\view2tmpF.s",appset.outFolderPath+"\\view2.s");
                     setF.setDataFilePath(appset.outFolderPath+"\\JinputInitial.arff");
                 }
                 else{
                     setF=new SettingsReader(appset.outFolderPath+"/view2tmpF.s",appset.outFolderPath+"/view2.s");
                     setF.setDataFilePath(appset.outFolderPath+"/JinputInitial.arff");
                 }
            }
            else{
              if(appset.system.equals("windows")){
                     setF=new SettingsReader(appset.outFolderPath+"\\view1tmpF.s",appset.outFolderPath+"\\view1.s");
                     setF.setDataFilePath(appset.outFolderPath+"\\JinputInitial.arff");
                 }
                 else{
                     setF=new SettingsReader(appset.outFolderPath+"/view1tmpF.s",appset.outFolderPath+"/view1.s");
                     setF.setDataFilePath(appset.outFolderPath+"/JinputInitial.arff");
                 }  
            }
            
             }
       
       if(appset.numSupplementTrees>0)
                    setF.ModifySettingsF(0,datJ.schema.getNbAttributes(),appset);
          
      
      
       if(appset.numSupplementTrees>0){
           if(sView == 0)
                exec.run(appset.javaPath, appset.clusPath, appset.outFolderPath, "view2tmpF.s", 0,appset.clusteringMemory);//was 1 for rules before
           else{
                exec.run(appset.javaPath, appset.clusPath, appset.outFolderPath, "view1tmpF.s", 0,appset.clusteringMemory);//was 1 for rules before
           } 
       }
       
        if(appset.numSupplementTrees>0){ 
            if(sView ==0){
                if(appset.system.equals("windows"))
                        inputFa=appset.outFolderPath+"\\view2tmpF.out";
                else inputFa=appset.outFolderPath+"/view2tmpF.out";
            }
            else{
                 if(appset.system.equals("windows"))
                     inputFa=appset.outFolderPath+"\\view1tmpF.out";
                 else  inputFa=appset.outFolderPath+"/view1tmpF.out";
            }
        }
       
       //load rules into rule reader
        
        if(appset.numSupplementTrees>0){
                rrFA.extractRules(inputFa,fid,datJ,appset);
                rrFA.setSize();
            }
        
          if(appset.numSupplementTrees>0){
              if(sView == 0)
                rr.addnewRulesCF(rrFA, appset.numnewRAttr); 
              else rr1.addnewRulesCF(rrFA, appset.numnewRAttr); 
          }
        
       /*FileDeleter delTmp=new FileDeleter();
           if(appset.system.equals("windows"))
                delTmp.setPath(appset.outFolderPath+"\\JinputInitial.arff");
           else
               delTmp.setPath(appset.outFolderPath+"/JinputInitial.arff");
           delTmp.delete();
           */
       
           System.out.println("Rule set sizes: "+rr1.rules.size()+" "+rr.rules.size());      
          double minJG  = appset.minJS;
          double minAJG = appset.minAddRedJS;
          appset.minJS = 0.05;
          appset.minAddRedJS = 0.04;
           
            if(appset.useJoin){ //add duplicate check with negations/disjunctions
            rttmp.createGuidedJoinExt(rr1, rr, jsN, appset, 0, 0, 0, oom,fid,datJ);
            rr.removeElements(rr.newRuleIndex);
            rr1.removeElements(rr1.newRuleIndex);
        }
        else if(!appset.useJoin){
            rttmp.createGuidedNoJoinExt(rr1, rr, jsN, appset, 0, 0,0,oom,fid,datJ);
            rr.removeElements(rr.newRuleIndex);
            rr1.removeElements(rr1.newRuleIndex);
        }

            rttmp.adaptSet(datJ, fid, 1);
             rttmp.removePVal(appset);
             
              //napraviti kombinacije binova???
              //rafinirati redeskripcije koristeći binove
              //pravilo mora sadrzavati samo taj atribut da bi rafiniranje krenulo
             if(clustMode == 0)
                rttmp.refineRedsBins(bins, jsN, appset, fid, datJ, t, at, min, sView);
             rttmp.adaptSet(datJ, fid, 1);
             /*System.out.println("Supports: ");
             for(int zz=0;zz<rttmp.redescriptions.size();zz++){
                 System.out.println(rttmp.redescriptions.get(zz).viewElementsLists.size());
                 for(int zz1=0;zz1<rttmp.redescriptions.get(zz).supportsSides.size();zz1++)
                     System.out.print(rttmp.redescriptions.get(zz).supportsSides.get(zz1)+" ");
                 System.out.print(" "+rttmp.redescriptions.get(zz).elements.size());
                 System.out.println();
             }*/
             
             double ResultsScore = 0.0; 
             double coverage[] = new double[2];
             
             appset.minJS = minJG;
             appset.minAddRedJS = minAJG;
             
             for(int k = rttmp.redescriptions.size()-1;k>=0;k--)
                 if(rttmp.redescriptions.get(k).JS<appset.minJS)
                     rttmp.redescriptions.remove(k);
             
             Jacard js1 = new Jacard();
             
             for(int k=rttmp.redescriptions.size()-1;k>=0;k--)
                 for(int k1 = k-1;k1>=0;k1--){
                     if((js1.computeAttributeJacard(rttmp.redescriptions.get(k), rttmp.redescriptions.get(k1), datJ) == 1) && js1.computeRedescriptionElementJacard(rttmp.redescriptions.get(k), rttmp.redescriptions.get(k1)) == 1){
                         rttmp.redescriptions.remove(k);
                         break;
                     }
                 }
                         
             
              if(appset.system.equals("windows"))
                    rttmp.writeToFile(appset.outFolderPath+"\\"+appset.outputName+"DLInterpretability "+t.getName()+" "+appset.minJS+" JSType "+0+"minSupp "+appset.minSupport+".rr", datJ, fid, startTime,rttmp.redescriptions.size(),appset, ResultsScore, coverage,oom);
              else
                    rttmp.writeToFile(appset.outFolderPath+"/"+appset.outputName+"DLInterpretability "+t.getName()+" "+appset.minJS+" JSType "+0+"minSupp "+appset.minSupport+".rr", datJ, fid, startTime,rttmp.redescriptions.size(),appset, ResultsScore, coverage,oom);
    
             /*  if(test == 1)
            continue;*/
              
             // rtmp.redescriptions.addAll(rttmp.redescriptions);
            //add code for multi-view  
              
          
           
        //kod multi-view dodaj pravila drugih pogleda
          
     //combine rules into reds      
 
           datJInit=null;        
           
        int leftSide=1, rightSide=0;//set left to 1 when computing lf, otherwise right
        int leftSide1=0, rightSide1=1; //left, right side for Side 2
        int it=0;
        Jacard js=new Jacard();
      //  Jacard jsN[]=new Jacard[3];
        
        for(int i=0;i<jsN.length;i++)
            jsN[i]=new Jacard();
       
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
      
        int gl = appset.numSupplementTrees;
        
        while(newRedescriptions!=0 && RunInd<appset.numIterations){
            appset.numSupplementTrees = 2;
       DataSetCreator dsc=null;
       DataSetCreator dsc1=null;
       
       rr.setSize();
       rr1.setSize();
       
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
        
        for(int z=0;z<numBins;z++){

            nARules=0; nARules1=0;
            double startPerc=0;
            double endPerc=1;
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
                    }
                     else if(appset.computeDMfromRules==true){
                             nclMat1.computeDistanceMatrix(rr, fid, appset.maxDistance, datJ.numExamples);
                     }
                  }
                }
            }
  
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
               
         else if(appset.treeTypes.get(1)==0)
             if(appset.system.equals("windows")){ 
                 dsc.modifyDatasetCat(startIndexRR1,endTmp,rr1,appset.outFolderPath+"\\Jinputnew.arff",fid,appset);
             }
             else{
                dsc.modifyDatasetCat(startIndexRR1,endTmp,rr1,appset.outFolderPath+"/Jinputnew.arff",fid,appset); 
             }
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
             if(appset.treeTypes.get(0)==1)
                 if(appset.system.equals("windows")){ 
                    dsc.modifyDatasetS(startIndexRR,endTmp,rr,appset.outFolderPath+"\\Jinputnew.arff",fid,appset);
                 }
                 else{
                     dsc.modifyDatasetS(startIndexRR,endTmp,rr,appset.outFolderPath+"/Jinputnew.arff",fid,appset); 
                 }
             else if(appset.treeTypes.get(0)==0/*appset.typeOfLSTrees==0*/)
                  if(appset.system.equals("windows")){ 
                    dsc.modifyDatasetCat(startIndexRR,endTmp ,rr,appset.outFolderPath+"\\Jinputnew.arff",fid,appset);
                  }
                  else{
                     dsc.modifyDatasetCat(startIndexRR,endTmp ,rr,appset.outFolderPath+"/Jinputnew.arff",fid,appset); 
                  }
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
            else if(appset.treeTypes.get(1)==0)
               if(appset.system.equals("windows"))
                     dsc1.modifyDatasetCat(startIndexRR1,endTmp, rr1,appset.outFolderPath+"\\Jinputnew1.arff",fid,appset);
                else
                     dsc1.modifyDatasetCat(startIndexRR1,endTmp, rr1,appset.outFolderPath+"/Jinputnew1.arff",fid,appset);
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
             if(appset.treeTypes.get(0)==1)
                 if(appset.system.equals("windows"))
                    dsc1.modifyDatasetS(startIndexRR,endTmp,rr,appset.outFolderPath+"\\Jinputnew1.arff",fid,appset);
                 else
                    dsc1.modifyDatasetS(startIndexRR,endTmp,rr,appset.outFolderPath+"/Jinputnew1.arff",fid,appset); 
             else if(appset.treeTypes.get(0)==0)
                 if(appset.system.equals("windows"))
                 dsc1.modifyDatasetCat(startIndexRR,endTmp,rr,appset.outFolderPath+"\\Jinputnew1.arff",fid,appset);
             else
                   dsc1.modifyDatasetCat(startIndexRR,endTmp,rr,appset.outFolderPath+"/Jinputnew1.arff",fid,appset);   
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
             exec.run(appset.javaPath, appset.clusPath, appset.outFolderPath, "view2tmp.s", 0,appset.clusteringMemory);//was 1 for rules before
             if(appset.numSupplementTrees>0)
                exec.run(appset.javaPath, appset.clusPath, appset.outFolderPath, "view2tmpF.s", 0,appset.clusteringMemory);//was 1 for rules before
             System.out.println("Process 2 side 1 finished!");
         }
         else if(rightSide==1 && (endIndexRR-oldIndexRR)>z*appset.numTargets){
             exec.run(appset.javaPath, appset.clusPath, appset.outFolderPath, "view1tmp.s", 0,appset.clusteringMemory);
             if(appset.numSupplementTrees>0)
                 exec.run(appset.javaPath, appset.clusPath, appset.outFolderPath, "view1tmpF.s", 0,appset.clusteringMemory);
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
             exec.run(appset.javaPath, appset.clusPath, appset.outFolderPath,"view2tmp1.s", 0,appset.clusteringMemory);
             if(appset.numSupplementTrees>0)
                exec.run(appset.javaPath, appset.clusPath, appset.outFolderPath,"view2tmpF1.s", 0,appset.clusteringMemory);
             System.out.println("Process 2 side 2 finished!");
         }
         else if(rightSide1==1 && (endIndexRR-oldIndexRR)>z*appset.numTargets){
             exec.run(appset.javaPath, appset.clusPath, appset.outFolderPath,"view1tmp1.s", 0,appset.clusteringMemory);
             if(appset.numSupplementTrees>0)
             exec.run(appset.javaPath, appset.clusPath, appset.outFolderPath,"view1tmpF1.s" , 0,appset.clusteringMemory);
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
        }
        else if(rightSide==1 && (endIndexRR-oldIndexRR)>z*appset.numTargets){
            if(z==0)
                newRules=rr1.addnewRulesC(ItRules, appset.numnewRAttr,1);
            else
                newRules=rr1.addnewRulesC(ItRules, appset.numnewRAttr, 0);
            if(appset.numSupplementTrees>0)
                rr1.addnewRulesCF(ItRulesF, appset.numnewRAttr); 
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
        }
        else if(rightSide1==1 && (endIndexRR-oldIndexRR)>z*appset.numTargets){
            if(z==0)
                    newRules1=rr1.addnewRulesC(ItRules1, appset.numnewRAttr,1);
            else
                    newRules1=rr1.addnewRulesC(ItRules1, appset.numnewRAttr,0);
            if(appset.numSupplementTrees>0)
             rr1.addnewRulesCF(ItRulesF1, appset.numnewRAttr);
        }

        System.out.println("New rules cicle 1: "+newRules);
        System.out.println("New rules cicle 2: "+newRules1);
       }
        //add the redescription creaton code
       
       if(appset.optimizationType==0){ 
        if(appset.useJoin){
            //add computation of rule support if bagging
            newRedescriptions=rtmp.createGuidedJoinBasic(rr1, rr, jsN, appset, oldIndexRR1, oldIndexRR, RunInd,oom,fid,datJ, elemFreq, attrFreq, redScores,redScoresAtt,redDistCoverage,redDistCoverageAt, redDistNetwork, targetAtScore, Statistics, maxDiffScoreDistribution,nclMatInit,0);
            rr.removeElements(rr.newRuleIndex);
            rr1.removeElements(rr1.newRuleIndex);
            if(appset.numSupplementTrees>0){
                 rr.removeRulesCF();
                 rr1.removeRulesCF();
            }
        }
        else if(!appset.useJoin){
            newRedescriptions=rtmp.createGuidedNoJoinBasic(rr1, rr, jsN, appset, oldIndexRR1, oldIndexRR, RunInd,oom,fid,datJ, elemFreq, attrFreq, redScores,redScoresAtt,redDistCoverage,redDistCoverageAt, redDistNetwork, targetAtScore, Statistics, maxDiffScoreDistribution,nclMatInit,0);
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
            newRedescriptions=rtmp.createGuidedJoinExt(rr1, rr, jsN, appset, oldIndexRR1, oldIndexRR, RunInd, oom,fid,datJ);//sqitch sides of rules
            rr.removeElements(rr.newRuleIndex);
            rr1.removeElements(rr1.newRuleIndex);
        }
        else if(!appset.useJoin){
            newRedescriptions=rtmp.createGuidedNoJoinExt(rr1, rr, jsN, appset, oldIndexRR1, oldIndexRR, RunInd,oom,fid,datJ);
            rr.removeElements(rr.newRuleIndex);
            rr1.removeElements(rr1.newRuleIndex);
        }
       }
       
         it++;
        
        System.out.println("New redescriptions: "+newRedescriptions);
         
        //if more than two viewes get guided search in here for further views...
        //should be modified for redescription addition
        //new join procedures should be created 
        System.out.println("Number of viewes: "+datJ.W2indexs.size());
        for(int nws=2;nws<datJ.W2indexs.size()+1;nws++){
            if(readers.size()<(datJ.W2indexs.size()-2)+1)
            readers.add(new RuleReader());
            int oldIndW=readers.get(nws-2).newRuleIndex, endIndW=0;
            
            SettingsReader setMW=new SettingsReader();
           if(appset.system.equals("windows")){ 
            setMW.setPath(appset.outFolderPath+"\\view3tmp.s");
            setMW.setStaticFilePath=appset.outFolderPath+"\\view3tmp.s";
            setMW.setDataFilePath(appset.outFolderPath+"\\Jinputnew.arff");
           }
           else{
              setMW.setPath(appset.outFolderPath+"/view3tmp.s");
              setMW.setStaticFilePath=appset.outFolderPath+"/view3tmp.s";
              setMW.setDataFilePath(appset.outFolderPath+"/Jinputnew.arff"); 
           }
            if((nws-1)<(datJ.W2indexs.size()-2+1))
                setMW.createInitialSettingsGen(nws, datJ.W2indexs.get(nws-1)+1 ,datJ.W2indexs.get(nws)+1,datJ.schema.getNbAttributes() , appset,0);
            else
                setMW.createInitialSettingsGen(nws, datJ.W2indexs.get(nws-1)+1 ,datJ.schema.getNbAttributes()+1,datJ.schema.getNbAttributes() , appset,0);
            
              numBins=0;
        Size=rtmp.redescriptions.size();
        if(Size%appset.numTargets==0)
            numBins=Size/appset.numTargets;
        else numBins=Size/appset.numTargets+1;
            
        for(int z=0;z<numBins;z++){
       
            if(z==0){//should create network from redescriptions!
               if(appset.useNC.size()>nws && appset.networkInit==false){ 
                if(appset.useNC.get(nws)==true && readers.get(nws-2).rules.size()>0){
                    nclMat.reset(appset);
                    if(appset.distanceFilePaths.size()>=nws && appset.networkInit==false && appset.useNetworkAsBackground==false){
                             nclMat.loadDistance(new File(appset.distanceFilePaths.get(nws)), fid);
                              if(appset.system.equals("windows")){ 
                                     nclMat.writeToFile(new File(appset.outFolderPath+"\\distance.csv"), fid,appset);
                              }
                              else{
                                  nclMat.writeToFile(new File(appset.outFolderPath+"/distance.csv"), fid,appset);
                              }
                    }
                     else if(appset.computeDMfromRules==true){
                             nclMat.computeDistanceMatrix(rtmp.redescriptions, fid, appset.maxDistance, datJ.numExamples,oldRIndex);
                             if(appset.system.equals("windows")){ 
                                    nclMat.resetFile(new File(appset.outFolderPath+"\\distances.csv"));
                                    nclMat.writeToFile(new File(appset.outFolderPath+"\\distances.csv"), fid,appset);
                             }
                             else{
                                 nclMat.resetFile(new File(appset.outFolderPath+"/distances.csv"));
                                 nclMat.writeToFile(new File(appset.outFolderPath+"/distances.csv"), fid,appset);
                             }
                     }
                   }
               }
                endIndW=readers.get(nws-2).rules.size();
            }
            
            nARules=0; nARules1=0;
            double startPerc=0;//percentage[z];
            double endPerc=0;//percentage[z+1];
            int minCovElements[]=new int[]{0};
            int maxCovElements[]=new int[]{0};
            int cuttof=0;
            
           /* cuttof=rs.findCutoff(naex, startPerc, endPerc, minCovElements,maxCovElements,oldRIndex, appset.minSupport,appset.maxSupport,appset.numTargets);
             System.out.println("minCovElements: "+minCovElements[0]);
             System.out.println("maxCovElements: "+maxCovElements[0]);
             System.out.println("cuttof: "+cuttof);
             
           if(cuttof==-1)
                continue;*/
            
           if(appset.system.equals("windows")) 
                dsc=new DataSetCreator(appset.outFolderPath+"\\Jinput.arff");
           else
               dsc=new DataSetCreator(appset.outFolderPath+"/Jinput.arff");
            
             try{
        dsc.readDataset();
        }
        catch(IOException e){
            e.printStackTrace();
        }
            
            System.out.println("startPerc: "+startPerc);
            System.out.println("endPerc: "+endPerc);
            
            int endTmp=0;
             if((z+1)*appset.numTargets>rtmp.redescriptions.size())
                 endTmp=rtmp.redescriptions.size();
             else endTmp=(z+1)*appset.numTargets;
              //add conditions in the this part of the code...!!!
             int startIndexRR=oldRIndex[0]+z*appset.numTargets;
            
            for(int i=startIndexRR;i<endTmp;i++)//oldRIndex[0];i<rs.redescriptions.size();i++) //do on the fly when reading rules
                   // if(rs.redescriptions.get(i).elements.size()<=naex*endPerc && rs.redescriptions.get(i).elements.size()>=naex*startPerc && rs.redescriptions.get(i).elements.size()>=minCovElements[0] && rs.redescriptions.get(i).elements.size()<=maxCovElements[0]) //do parameters analysis in this step
                        nARules++;
             setMW.ModifySettings(nARules,dsc.schema.getNbAttributes());
             try{
                 if(appset.treeTypes.get(nws)==1/*appset.typeOfRSTrees==1*/){ 
                     if(appset.system.equals("windows")) 
                         dsc.modifyDatasetS(startIndexRR,endTmp, rtmp.redescriptions,appset.outFolderPath+"\\Jinputnew.arff",fid,appset);
                     else 
                         dsc.modifyDatasetS(startIndexRR,endTmp, rtmp.redescriptions,appset.outFolderPath+"/Jinputnew.arff",fid,appset);
                 }
         else if(appset.treeTypes.get(nws)==0/*appset.typeOfRSTrees==0*/){
             if(appset.system.equals("windows")) 
                dsc.modifyDatasetCat(startIndexRR,endTmp, rtmp.redescriptions,appset.outFolderPath+"\\Jinputnew.arff",fid,appset);
             else
                dsc.modifyDatasetCat(startIndexRR,endTmp, rtmp.redescriptions,appset.outFolderPath+"/Jinputnew.arff",fid,appset); 
         }
         //if(appset.treeTypes.get(nws)==1/*appset.typeOfRSTrees==1*/)   
               // dsc.modifyDatasetS(nARules, startPerc, endPerc, oldRIndex[0], rs.redescriptions.size(), minCovElements[0],maxCovElements[0], rs.redescriptions,appset.outFolderPath+"\\Jinputnew.arff",fid);
        // else if(appset.treeTypes.get(nws)==0/*appset.typeOfRSTrees==0*/)
          //      dsc.modifyDatasetCat(nARules, startPerc, endPerc, oldRIndex[0], rs.redescriptions.size(), minCovElements[0],maxCovElements[0], rs.redescriptions,appset.outFolderPath+"\\Jinputnew.arff",fid);
        }
        catch(IOException e){
            e.printStackTrace();
        }
             
             exec.run(appset.javaPath, appset.clusPath, appset.outFolderPath, "view3tmp.s"/*"wbtmp.s"*/, 0,appset.clusteringMemory);//was 1 for rules before
             System.out.println("Process 1 side "+nws+" finished!");
             
             String input;
             if(appset.system.equals("windows")) 
              input=appset.outFolderPath+"\\view3tmp.out";
             else
                 input=appset.outFolderPath+"/view3tmp.out";
             
              int newRules=0;
              RuleReader ItRules=new RuleReader();
              
             ItRules.extractRules(input,fid,datJ,appset);
        ItRules.setSize();
            if(z==0)
                newRules=readers.get(nws-2).addnewRulesC(ItRules, appset.numnewRAttr,1);
            else
                newRules=readers.get(nws-2).addnewRulesC(ItRules, appset.numnewRAttr,0);
           }
        
        if(appset.useJoin){//do redescription construction
            rtmp.combineViewRulesJoin(readers.get(nws-2), jsN, appset, oldIndW, RunInd, oom, fid, datJ, oldRIndex ,nws); 
        }
        else{//(rr, rr1, jsN, appset, oldIndexRR, oldIndexRR1, RunInd, oom,fid,datJ);
            rtmp.combineViewRules(readers.get(nws-2), jsN, appset, oldIndW, RunInd, oom, fid, datJ, oldRIndex ,nws);
        }
           
        //rs.combineViewRules(readers.get(nws-2), jsN, appset, oldIndW, RunInd, oom, fid, datJ, oldRIndex ,nws);
        }
        
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
        
        //write general reds connected with this node's subsets
        
         System.out.println("Redescription size main: "+rtmp.redescriptions.size());
        
        //removing all redescriptions with inadequate minSupport and minJS
        rtmp.remove(appset);
         
        System.out.println("Redescription size main after remove: "+rtmp.redescriptions.size());
        
        //filtering
      // rs.filter(appset, rr, rr1,fid,datJ); // think about what we want and if we need it
        
        System.out.println("Redescription size main after filter: "+rtmp.redescriptions.size());
       
      int numFullRed=0;
        //computing pVal...
        numFullRed=rtmp.computePVal(datJ,fid);
        rtmp.removePVal(appset);
        
        for(int i=0;i<rtmp.redescriptions.size();i++){
            rtmp.redescriptions.get(i).closeInterval(datJ, fid);
            rtmp.redescriptions.get(i).createRuleString(fid);
        }
        
        if(appset.system.equals("windows"))
                    rtmp.writeToFile(appset.outFolderPath+"\\"+appset.outputName+"DLInterpretabilityGeneral"+t.getName()+" "+appset.minJS+" JSType "+0+"minSupp "+appset.minSupport+".rr", datJ, fid, startTime,rtmp.redescriptions.size(),appset, 0, new double[2] ,oom);
              else
                    rtmp.writeToFile(appset.outFolderPath+"/"+appset.outputName+"DLInterpretabilityGeneral"+t.getName()+" "+appset.minJS+" JSType "+0+"minSupp "+appset.minSupport+".rr", datJ, fid, startTime,rtmp.redescriptions.size(),appset, 0, new double[2],oom);
      
        rtmp.redescriptions.clear();
       /* if(test == 1)
            break;*/
         appset.numSupplementTrees = gl;
      }
   // }
       
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
