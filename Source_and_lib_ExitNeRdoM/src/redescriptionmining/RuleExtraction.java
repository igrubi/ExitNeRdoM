/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package redescriptionmining;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.set.hash.TIntHashSet;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author matmih
 */


 //add paths for train and validation set to settings file + 
        //load target class values for all entities from the train and all different values of the class label (create a class for this) +
        //create 3 Jinput datasets, containing all views, all but last and only the first one +
        //iterate over all redescription sets in the folder +
        //create functions to load redescriptions (probably already exist) +
       //redescription - compute statistics -> use +
        //create functions to compute homogeneity of redescriptions + rules and to compute rule support +
        //load the most accurate, general red describing some target label value (separately for each class)
        // do not put target values as a view but use their distribution to choose homogeneous reds (an option)
        //if use target, compute homogeneity of the original rule +
        //aim to have high coverage for all entities from the train with high precision (will always be 1 with reds) +
        //evaluate obtained rules on the test fold (compute accuracy for each class)  -> report as the competing methods
        //write the result for each fold in the file

public class RuleExtraction {
    public static void main(String args[]){
         String trainInputFolder, testInputFolder, resultFolder="";
        
       
       ApplicationSettings appset=new ApplicationSettings();
        appset.readSettings(new File(args[0]));
        
        
         
        
       /* DataSetCreator datJ=new DataSetCreator(appset.viewInputPaths, appset.outFolderPath,appset);
       
           if(appset.system.equals("windows"))
            fid.createIndex(appset.outFolderPath+"\\Jinput.arff");
        else
            fid.createIndex(appset.outFolderPath+"/Jinput.arff");*/
       
       ArrayList<String> inputPaths = new ArrayList<String>();
       ArrayList<String> inputPathsTest = new ArrayList<String>();
       
       
        for(int i=0;i<appset.numFolds;i++){//iterate over all folds
           Mappings fid=new Mappings();
           Mappings fid1=new Mappings();
           Mappings fid2=new Mappings();
           
           inputPaths = new ArrayList<String>();
           inputPathsTest = new ArrayList<String>();
           
           Mappings fidTest=new Mappings();
           Mappings fidTest1=new Mappings();
           Mappings fidTest2=new Mappings();
            //train inputs and mappings
           if(appset.system.equals("windows")){
           trainInputFolder = appset.trainPath+"\\fold"+(i+1)+"\\";
            resultFolder = appset.trainPath+"\\fold"+(i+1)+"\\Results\\";
           testInputFolder = appset.testPath+"\\fold"+(i+1)+"\\";      
           }
           else{
               trainInputFolder = appset.trainPath+"/fold"+(i+1)+"/";
               testInputFolder = appset.testPath+"/fold"+(i+1)+"/"; 
               resultFolder = appset.trainPath+"/fold"+(i+1)+"/Results/";
           }
           
           for(int j=0;j<appset.numLayers;j++)
                inputPaths.add(trainInputFolder+"layer"+j+".arff");
           if(appset.useTarget)
             inputPaths.add(trainInputFolder+"target.arff");
           
           DataSetCreator datJ=new DataSetCreator(inputPaths, appset.outFolderPath,appset);
           if(appset.useTarget)
             inputPaths.remove(inputPaths.size()-1);
           DataSetCreator datJ1=new DataSetCreator(inputPaths, appset.outFolderPath,appset,"Jinput1.arff");
           while(inputPaths.size()>1)//only the first, original attributes view
              inputPaths.remove(inputPaths.size()-1);
           DataSetCreator datJ2=new DataSetCreator(inputPaths, appset.outFolderPath,appset,"Jinput2.arff");
           
            if(appset.system.equals("windows"))
            fid.createIndex(appset.outFolderPath+"\\Jinput.arff");
        else
            fid.createIndex(appset.outFolderPath+"/Jinput.arff");
            
             if(appset.system.equals("windows"))
            fid1.createIndex(appset.outFolderPath+"\\Jinput1.arff");
        else
            fid1.createIndex(appset.outFolderPath+"/Jinput1.arff");
             
             
              if(appset.system.equals("windows"))
            fid2.createIndex(appset.outFolderPath+"\\Jinput2.arff");
        else
            fid2.createIndex(appset.outFolderPath+"/Jinput2.arff");
          
              //test inputs and mappings
               for(int j=0;j<appset.numLayers;j++)
                inputPathsTest.add(testInputFolder+"layer"+j+".arff");
               if(appset.useTarget)
                    inputPathsTest.add(testInputFolder+"target.arff");
           
           DataSetCreator datJTest=new DataSetCreator(inputPathsTest, appset.outFolderPath,appset,"JinputTest.arff");
           if(appset.useTarget)
             inputPathsTest.remove(inputPathsTest.size()-1);
           DataSetCreator datJTest1=new DataSetCreator(inputPathsTest, appset.outFolderPath,appset,"JinputTest1.arff");
            while(inputPathsTest.size()>1)
                  inputPathsTest.remove(inputPathsTest.size()-1);
           DataSetCreator datJTest2=new DataSetCreator(inputPathsTest, appset.outFolderPath,appset,"JinputTest2.arff");
           
            if(appset.system.equals("windows"))
            fidTest.createIndex(appset.outFolderPath+"\\JinputTest.arff");
        else
            fidTest.createIndex(appset.outFolderPath+"/JinputTest.arff");
            
             if(appset.system.equals("windows"))
            fidTest1.createIndex(appset.outFolderPath+"\\JinputTest1.arff");
        else
            fidTest1.createIndex(appset.outFolderPath+"/JinputTest1.arff");
             
             
              if(appset.system.equals("windows"))
            fidTest2.createIndex(appset.outFolderPath+"\\JinputTest2.arff");
        else
            fidTest2.createIndex(appset.outFolderPath+"/JinputTest2.arff");
        
        File inputLabelsTrain = new File(trainInputFolder+"target.arff");  
        TargetLabels labelsTrain = new TargetLabels(inputLabelsTrain);   
        
        HashMap<String,Integer> labelIndex = new HashMap<>();
        int count = 0;
        for(String s:labelsTrain.allLabels)
             labelIndex.put(s,count++);
        
        File inputLabelsTest = new File(testInputFolder+"target.arff");  
        TargetLabels labelsTest = new TargetLabels(inputLabelsTest); 
        
        System.out.println("Label mappings: ");
        System.out.println(labelsTrain.allLabels.size());
        System.out.println(labelsTrain.entityLabel.keySet().size());
        System.out.println(labelsTest.allLabels.size());
        System.out.println(labelsTest.entityLabel.keySet().size());
        
        ArrayList<ArrayList<Redescription>> chosenReds = new ArrayList<>();
        ArrayList<ArrayList<Rule>> chosenRules = new ArrayList<>();
        int numElems = datJ.numExamples;
        ArrayList<HashSet<Integer>> Covered = new ArrayList<>();
       ArrayList<Double> numElemsNotCovered  = new ArrayList<>();
       
       ArrayList<HashSet<Integer>> CoveredRules = new ArrayList<>();
       ArrayList<Double> numElemsNotCoveredRules  = new ArrayList<>();
       
       for(int lind=0;lind<labelsTrain.allLabels.size();lind++){
           chosenReds.add(new ArrayList<Redescription>());
           chosenRules.add(new ArrayList<Rule>());
           Covered.add(new HashSet<Integer>());
           CoveredRules.add(new HashSet<Integer>());
           
           numElemsNotCovered.add(0.0);
           numElemsNotCoveredRules.add(0.0);
       }
           
             for (String s : labelsTrain.entityLabel.keySet()) {
                 int ind = labelIndex.get(labelsTrain.entityLabel.get(s));
                 numElemsNotCovered.set(ind,numElemsNotCovered.get(ind)+1.0);
                 numElemsNotCoveredRules.set(ind,numElemsNotCoveredRules.get(ind)+1.0);
             }
       
        
       //read redescription sets       
         File dir = new File(resultFolder);
         File[] directoryListing = dir.listFiles();
 
        if (directoryListing != null) {
            for (File rs : directoryListing){
                if(rs.getName().contains("redescription") && rs.getName().contains(".rr")){
                    RedescriptionSet redset = new RedescriptionSet();
                    redset.loadRedescriptionsFromFileSet(rs, datJ);
                    ArrayList<DataSetCreator> dats = new ArrayList<>();
                    ArrayList<Mappings> mappings = new ArrayList<>();
                    if(redset.redescriptions.isEmpty()) continue;
                    for(int rind = 0;rind<redset.redescriptions.get(0).ruleStrings.size();rind++){
                        dats.add(datJ); mappings.add(fid);
                    }

                    for(int rind = 0; rind<redset.redescriptions.size();rind++){
                        redset.redescriptions.get(rind).computeStatisticsRE(dats, mappings);
                    }
                    
                    RedescriptionSet redset1 = new RedescriptionSet();
                    
                    for(int rind = 0; rind<redset.redescriptions.size();rind++){
                        Redescription r = new Redescription(datJ1);
                        if(appset.useTarget){
                        for(int rrs =0;rrs<redset.redescriptions.get(rind).ruleStrings.size()-1;rrs++)
                            r.ruleStrings.add(redset.redescriptions.get(rind).ruleStrings.get(rrs));
                        }
                        else{
                            for(int rrs =0;rrs<redset.redescriptions.get(rind).ruleStrings.size();rrs++)
                            r.ruleStrings.add(redset.redescriptions.get(rind).ruleStrings.get(rrs));
                        }
                        r.computeStatisticsRE(dats, mappings);
                        redset1.redescriptions.add(r);
                        /*System.out.println(rs.getName());
                        for(int zz=0;zz<r.ruleStrings.size();zz++)
                            System.out.println(r.ruleStrings.get(zz));
                        System.out.println();*/
                    }
                  
                    RuleReader ruleset = new RuleReader();
                    for(int rind = 0; rind<redset.redescriptions.size();rind++){
                        if(redset.redescriptions.get(rind).ruleStrings.get(0).contains("NOT ") || redset.redescriptions.get(rind).ruleStrings.get(0).contains(" OR "))
                            continue;
                        Rule r = new Rule(redset.redescriptions.get(rind).ruleStrings.get(0),fid2,0);
                        r.addAttBounds(redset.redescriptions.get(rind).ruleStrings.get(0), fid2);
                        //compute rule elements
                        r.addElements(fid2, datJ2);
                        //close interval
                        r.closeInterval(datJ2, fid2);
                        
                        ruleset.rules.add(r);
                    }  
                    
                    //should be done for every possible class value
                    for(int targetIndex = 0; targetIndex<labelsTrain.allLabels.size();targetIndex++){
                    
                    double numNCelemsCoveredByRed = 0.0;
                    double precision = 0.0, score = 0.0;
                    double accuracy = 0.0;
                    double max = -1;
                    int maxind = 0;
                    
                    while(numElemsNotCovered.get(targetIndex)>0){
                        int added=0; max = -1; maxind = 0;
                        for(int rind = 0;rind<redset1.redescriptions.size();rind++){
                            int tlind = redset1.redescriptions.get(rind).majorityClassIndex(labelsTrain, labelIndex, fid1);
                            if(tlind!=targetIndex) continue;
                            numNCelemsCoveredByRed = 0.0;
                            double numMissed = 0.0;
                            TIntIterator it= redset1.redescriptions.get(rind).elements.iterator();
                            while(it.hasNext()){
                               int  entity = it.next();
                                if(!Covered.get(targetIndex).contains(entity))
                                    numNCelemsCoveredByRed +=1;
                               /* String entl = fid.idExample.get(entity);
                                if(labelIndex.get(labelsTrain.entityLabel.get(entl))!=tlind)
                                    numMissed+=1.0;*/
                            }
                            
                            if(numNCelemsCoveredByRed == 0) continue;
                            
                            accuracy = redset1.redescriptions.get(rind).JS;
                            precision = redset1.redescriptions.get(rind).computePrecision(labelsTrain, labelIndex, fid1);
                            if(precision<0.9) continue;
                            score = ((numNCelemsCoveredByRed/numElemsNotCovered.get(targetIndex))+accuracy+2*precision)/4.0;
                            
                            if(score>max){
                                max = score;
                                maxind = rind;
                            }
                            
                        }
                        
                        if(max!=-1){
                            added = 1;
                        chosenReds.get(targetIndex).add(redset1.redescriptions.get(maxind));
                        numNCelemsCoveredByRed =0.0;
                        TIntIterator it= redset1.redescriptions.get(maxind).elements.iterator();
                            while(it.hasNext()){
                                if(!Covered.get(targetIndex).contains(it.next()))
                                    numNCelemsCoveredByRed +=1;
                            }
                            
                            numElemsNotCovered.set(targetIndex,numElemsNotCovered.get(targetIndex)-numNCelemsCoveredByRed);
                        
                       it = redset1.redescriptions.get(maxind).elements.iterator();
                        while(it.hasNext())
                            Covered.get(targetIndex).add(it.next());
                        }
                        
                        if(added == 0) break;
                        
                    } 
                    
                     //later, replace the worst candidate with better one
                    for(int rind = 0;rind<redset1.redescriptions.size();rind++){
                        int tlind = redset1.redescriptions.get(rind).majorityClassIndex(labelsTrain, labelIndex, fid1);
                        for(int cind = 0; cind< chosenReds.get(targetIndex).size();cind++){
                            if(tlind!=targetIndex) break;
                            //check if red on rind describes a superset of entities of red on cind and has > acc and prec
                            int subs = 1;
                            
                            TIntIterator it = chosenReds.get(targetIndex).get(cind).elements.iterator();
                            while(it.hasNext()){
                                if(!redset1.redescriptions.get(rind).elements.contains(it.next())){
                                    subs = 0;
                                    break;
                                }
                            }
                            
                            if(subs == 1 && redset1.redescriptions.get(rind).JS>=chosenReds.get(targetIndex).get(cind).JS && redset1.redescriptions.get(rind).computePrecision(labelsTrain, labelIndex, fid1)>=chosenReds.get(targetIndex).get(cind).computePrecision(labelsTrain, labelIndex, fid1))
                                        chosenReds.get(targetIndex).set(cind, redset1.redescriptions.get(rind));
                        }
                    }
                    
                   //do the same for rules...
                   double numNCelemsCoveredByRule = 0.0;
                   
                    while(numElemsNotCoveredRules.get(targetIndex)>0){
                        int added=0; max = -1; maxind = 0;
                        for(int rind = 0;rind<ruleset.rules.size();rind++){
                            int tlind = ruleset.rules.get(rind).majorityClassIndex(labelsTrain, labelIndex, fid1);
                            if(tlind!=targetIndex) continue;
                            numNCelemsCoveredByRule = 0.0;
                            TIntIterator it= ruleset.rules.get(rind).elements.iterator();
                            while(it.hasNext()){
                                if(!CoveredRules.get(targetIndex).contains(it.next()))
                                    numNCelemsCoveredByRule +=1;
                            }
                            
                            if(numNCelemsCoveredByRule == 0) continue;
                            
                            precision = ruleset.rules.get(rind).computePrecision(labelsTrain, labelIndex, fid1);
                             if(precision<0.9) continue;
                            score = ((numNCelemsCoveredByRule/numElemsNotCoveredRules.get(targetIndex))+2*precision)/3.0;
                            
                            if(score>max){
                                max = score;
                                maxind = rind;
                            }
                            
                        }
                        
                        if(max!=-1){
                            added = 1;
                        chosenRules.get(targetIndex).add(ruleset.rules.get(maxind));
                        numNCelemsCoveredByRule =0.0;
                        TIntIterator it= ruleset.rules.get(maxind).elements.iterator();
                            while(it.hasNext()){
                                if(!CoveredRules.get(targetIndex).contains(it.next()))
                                    numNCelemsCoveredByRule +=1;
                            }
                            
                            numElemsNotCoveredRules.set(targetIndex,numElemsNotCoveredRules.get(targetIndex)-numNCelemsCoveredByRule);
                        
                       it = ruleset.rules.get(maxind).elements.iterator();
                        while(it.hasNext())
                            CoveredRules.get(targetIndex).add(it.next());
                        }
                        
                        if(added == 0) break;
                        
                    } 
                    
                     //later, replace the worst candidate with better one
                    for(int rind = 0;rind<ruleset.rules.size();rind++){
                        int tlind = ruleset.rules.get(rind).majorityClassIndex(labelsTrain, labelIndex, fid1);
                        for(int cind = 0; cind< chosenRules.get(targetIndex).size();cind++){
                            if(tlind!=targetIndex) break;
                            //check if red on rind describes a superset of entities of red on cind and has > acc and prec
                            int subs = 1;
                            
                            TIntIterator it = chosenRules.get(targetIndex).get(cind).elements.iterator();
                            while(it.hasNext()){
                                if(!ruleset.rules.get(rind).elements.contains(it.next())){
                                    subs = 0;
                                    break;
                                }
                            }
                            
                            if(subs == 1 && ruleset.rules.get(rind).computePrecision(labelsTrain, labelIndex, fid1)>=chosenRules.get(targetIndex).get(cind).computePrecision(labelsTrain, labelIndex, fid1))
                                        chosenRules.get(targetIndex).set(cind, ruleset.rules.get(rind));
                        }
                    }
                   
                   
                    }
                    
                }
      // Do something with child
            }     
           
           }
        System.out.println("Traversing results done!");
        System.out.println("chosen reds size: ");
        for(int kk=0;kk<chosenReds.size();kk++)
            System.out.println(chosenReds.get(kk).size());
        System.out.println("chosen rules size: ");
        for(int kk=0;kk<chosenRules.size();kk++)
            System.out.println(chosenRules.get(kk).size());
        //compute accuracy of rules for each class and write to file
        ArrayList<ArrayList<Redescription>> chosenRedsTest = new ArrayList<>();
        ArrayList<ArrayList<Rule>> chosenRulesTest = new ArrayList<>();
        
        for(int cr = 0; cr<chosenReds.size();cr++)
            chosenRedsTest.add(new ArrayList<Redescription>());
        
        for(int cr = 0; cr<chosenRules.size();cr++)
            chosenRulesTest.add(new ArrayList<Rule>());
        
        ArrayList<DataSetCreator> dats = new ArrayList<>();
        ArrayList<Mappings> mappings = new ArrayList<>();
       
       if(chosenReds.size()>0){
           for(int cv=0;cv<chosenReds.size();cv++){
           if(chosenReds.get(cv).size()>0){
                   for(int rind = 0;rind<chosenReds.get(0).get(0).ruleStrings.size();rind++){
                        dats.add(datJTest1); mappings.add(fidTest1);
                    }
                   break;
              }
           }
       }
        
        if(dats.size()<=0 || mappings.size()<=0){
            System.err.println("Empty test data and size! Error with obtained rules!");
            System.exit(1);
        }
       
         for(int cr = 0; cr<chosenReds.size();cr++){
             for(int cr1=0;cr1<chosenReds.get(cr).size();cr1++){
             Redescription r = new Redescription(datJ1);
              for(int rs=0;rs<chosenReds.get(cr).get(cr1).ruleStrings.size();rs++)             
               r.ruleStrings.add(chosenReds.get(cr).get(cr1).ruleStrings.get(rs));            
              r.computeStatisticsRE(dats, mappings);
              chosenRedsTest.get(cr).add(r);
             }          
         }
         
         
                     for(int cr = 0; cr<chosenRules.size();cr++){
                         for(int cr1=0;cr1<chosenRules.get(cr).size();cr1++){
                              Rule r = new Rule(chosenRules.get(cr).get(cr1).rule,fidTest2,0);
                         r.addAttBounds(chosenRules.get(cr).get(cr1).rule, fidTest2);
                        //compute rule elements
                        r.addElements(fidTest2, datJTest2);
                        //close interval
                        r.closeInterval(datJTest2, fidTest2);
                        
                        chosenRulesTest.get(cr).add(r);
                    }  
                     }
                     
              //compute accuracy, iterate over all entities, check target and compute for every class value       
              ArrayList<ArrayList<Double>> confusionReds = new ArrayList<>();
              ArrayList<ArrayList<Double>> confusionRules = new ArrayList<>();
              //tp,fp,tn,fn
              for(int cr = 0; cr<chosenReds.size();cr++){
                  confusionReds.add(new ArrayList<Double>());
                  confusionRules.add(new ArrayList<Double>());
              }
              
              for(int cr = 0; cr<confusionReds.size();cr++){
                  for(int nn = 0;nn<4;nn++){
                  confusionReds.get(cr).add(0.0);
                  confusionRules.get(cr).add(0.0);
                  }
              }
              
              
              //create entity sets for rules
              ArrayList<TIntHashSet> supports = new ArrayList<>();
              
              for(int cc = 0;cc<chosenRules.size();cc++)
                  supports.add(new TIntHashSet());
              
              for(int index = 0;index<chosenRulesTest.size();index++){
                      for(int cr=0;cr<chosenRulesTest.get(index).size();cr++){
                          supports.get(index).addAll(chosenRulesTest.get(index).get(cr).elements);
                      }
                   }
              
              double correctRules = 0.0;
              
              Iterator<String> it = labelsTest.entityLabel.keySet().iterator();
              while(it.hasNext()){
                  String elem = it.next();
                  int id = fidTest2.exampleId.get(elem);
                  int realClassIndex = labelIndex.get(labelsTest.entityLabel.get(elem));
                  for(int index = 0;index<chosenRules.size();index++){//add test!
                     // for(int cr=0;cr<chosenRules.get(index).size();cr++){
                          if(supports.get(index).contains(id)){
                              if(index == realClassIndex){
                                  confusionRules.get(index).set(0,confusionRules.get(index).get(0)+1);
                                  correctRules++;
                              }
                              else{  
                                  confusionRules.get(index).set(1,confusionRules.get(index).get(1)+1);
                                  break;
                              }
                          }
                          else{
                              if(index!=realClassIndex)
                                  confusionRules.get(index).set(2,confusionRules.get(index).get(2)+1);
                              else confusionRules.get(index).set(3,confusionRules.get(index).get(3)+1);
                          }
                    //  }
                  }
              }
              
              
              supports.clear();
              for(int cc = 0;cc<chosenReds.size();cc++)
                  supports.add(new TIntHashSet());
              
              for(int index = 0;index<chosenReds.size();index++){
                      for(int cr=0;cr<chosenReds.get(index).size();cr++){
                          supports.get(index).addAll(chosenRedsTest.get(index).get(cr).elements);
                      }
                   }
              
              double correctReds = 0.0;
              it = labelsTest.entityLabel.keySet().iterator();
              while(it.hasNext()){
                  String elem = it.next();
                  int id = fidTest2.exampleId.get(elem);
                  int realClassIndex = labelIndex.get(labelsTest.entityLabel.get(elem));
                  for(int index = 0;index<chosenReds.size();index++){
                     // for(int cr=0;cr<chosenReds.get(index).size();cr++){
                          if(supports.get(index).contains(id)){
                              if(index == realClassIndex){
                                  confusionReds.get(index).set(0,confusionReds.get(index).get(0)+1);
                                  correctReds++;
                              }
                              else{  
                                  confusionReds.get(index).set(1,confusionReds.get(index).get(1)+1);
                                  break;
                              }
                          }
                          else{
                              if(index!=realClassIndex)
                                  confusionReds.get(index).set(2,confusionReds.get(index).get(2)+1);
                              else confusionReds.get(index).set(3,confusionReds.get(index).get(3)+1);
                          }
                    //  }
                    
                  }
                  
              }
             /* System.out.println("Stats: "+supports.get(0).size()+" "+labelsTest.entityLabel.keySet().size());
              System.out.println("Stats: "+supports.get(1).size()+" "+labelsTest.entityLabel.keySet().size());
              System.out.println("Stats: "+supports.get(2).size()+" "+labelsTest.entityLabel.keySet().size());
              System.out.println("Stats: "+supports.get(3).size()+" "+labelsTest.entityLabel.keySet().size());*/
             // System.out.println("CR:  "+correctReds);
              double fidelityReds = 0.0, fidelityRules = 0.0, all = 0.0;
              
               for(int cr = 0; cr<chosenRules.size();cr++){
                   fidelityRules+=confusionRules.get(cr).get(0);
                   fidelityRules+=confusionRules.get(cr).get(2);
                   all+=confusionRules.get(cr).get(0);
                   all+=confusionRules.get(cr).get(1);
                   all+=confusionRules.get(cr).get(2);
                   all+=confusionRules.get(cr).get(3);
               }
              fidelityRules/=all;
              all = 0.0;
               for(int cr = 0; cr<chosenReds.size();cr++){
                   fidelityReds+=confusionReds.get(cr).get(0);
                   fidelityReds+=confusionReds.get(cr).get(2);
                   all+=confusionReds.get(cr).get(0);
                   all+=confusionReds.get(cr).get(1);
                   all+=confusionReds.get(cr).get(2);
                   all+=confusionReds.get(cr).get(3);
               }
              fidelityReds/=all;
              
              fidelityRules = correctRules/labelsTest.entityLabel.keySet().size();
              fidelityReds = correctReds/labelsTest.entityLabel.keySet().size();
              
              //write reds and class and accuracy to file
              String resultFile = "";
               if(appset.system.equals("windows"))
                   resultFile = appset.outFolderPath+"\\fold_"+(i+1)+".rules.txt";
              else
                   resultFile = appset.outFolderPath+"/fold_"+(i+1)+".rules.txt";

               
               try{
                   FileWriter fw = new FileWriter(resultFile);
                   
                   for(int cr = 0; cr<chosenRules.size();cr++){
                       for(int rind = 0;rind<chosenRules.get(cr).size();rind++){
                           fw.write(chosenRules.get(cr).get(rind).rule+"\n");
                       }
                       
                       Iterator<String> ls = labelIndex.keySet().iterator();
                       
                       while(ls.hasNext()){
                           String target = ls.next();
                           if(labelIndex.get(target) == cr){
                                fw.write("Class: "+target+"\n"); 
                                break;
                           }
                       }
                       fw.write("\n\n");
                   }
                   fw.write("Fidelity: "+fidelityRules+"\n");
                   fw.close();
               }
               catch(IOException e){
                   
               }
               
               resultFile = "";
               if(appset.system.equals("windows"))
                   resultFile = appset.outFolderPath+"\\fold_"+(i+1)+".reds.txt";
              else
                   resultFile = appset.outFolderPath+"/fold_"+(i+1)+".reds.txt";

               
               try{
                   FileWriter fw = new FileWriter(resultFile);
                   
                   for(int cr = 0; cr<chosenReds.size();cr++){
                       for(int rind = 0;rind<chosenReds.get(cr).size();rind++){
                           for(int redr = 0;redr<chosenReds.get(cr).get(rind).ruleStrings.size();redr++)
                             fw.write(chosenReds.get(cr).get(rind).ruleStrings.get(redr)+"\n");
                       }
                       
                       Iterator<String> ls = labelIndex.keySet().iterator();
                       
                       while(ls.hasNext()){
                           String target = ls.next();
                           if(labelIndex.get(target) == cr){
                                fw.write("Class: "+target+"\n"); 
                                break;
                           }
                       } 
                       fw.write("\n\n");
                   }
                   fw.write("Fidelity: "+fidelityReds+"\n");
                   fw.close();
                   //return;//remove after testing
               }
               catch(IOException e){
                   e.printStackTrace();
               }
               
               
        }

    }    
}
