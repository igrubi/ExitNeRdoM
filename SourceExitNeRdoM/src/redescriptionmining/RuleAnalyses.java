/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package redescriptionmining;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author matmih
 */
public class RuleAnalyses {
    static public void main(String args[]){
          ArrayList<String> trainFiles = new ArrayList<>();
          trainFiles.add("C:\\Users\\matmih\\Documents\\Experimenti\\InterpretableRM\\Redescription analyses\\CIFAR10_resnet18s111_layer_5.arff");
          trainFiles.add("C:\\Users\\matmih\\Documents\\Experimenti\\InterpretableRM\\Redescription analyses\\CIFAR10_resnet18s112_layer_5.arff");
          trainFiles.add("C:\\Users\\matmih\\Documents\\Experimenti\\InterpretableRM\\Redescription analyses\\CIFAR10_resnet18s113_layer_5.arff");
          trainFiles.add("C:\\Users\\matmih\\Documents\\Experimenti\\InterpretableRM\\Redescription analyses\\CIFAR10_resnet18s114_layer_5.arff");
          //add trainFiles
          String resultFolder = "C:\\Users\\matmih\\Documents\\Experimenti\\InterpretableRM\\Redescription analyses\\"; //add result folder
          String dataFolder = "C:\\Users\\matmih\\Documents\\Experimenti\\InterpretableRM\\ResultsForAnalyses\\DescriptionsCifarResnet111Lay5R112Lay5R113Lay5R114Lay5FinalFinal\\";
         TargetLabels trainLabels = new TargetLabels(new File("C:\\Users\\matmih\\Documents\\Experimenti\\InterpretableRM\\Redescription analyses\\LabelsTrain.arff")); //train labels
          
          Mappings fid=new Mappings();
          
           ApplicationSettings appset=new ApplicationSettings();
        appset.readSettings(new File(args[0]));
        
        DataSetCreator datJ=new DataSetCreator(trainFiles, appset.outFolderPath,appset);
          
          if(appset.system.equals("windows"))
            fid.createIndex(appset.outFolderPath+"\\Jinput.arff");
        else
            fid.createIndex(appset.outFolderPath+"/Jinput.arff");
          
          ArrayList<DataSetCreator> dats = new ArrayList<>();
          ArrayList<Mappings> mappings = new ArrayList<>();
                  
          RedescriptionSet redset1 = new RedescriptionSet();
                    
          int loaded = 0;
           File dir = new File(dataFolder);
         File[] directoryListing = dir.listFiles();
         
         double max = Double.NEGATIVE_INFINITY;
         
        if (directoryListing != null) {
            int cc = 0;
            for (File rs : directoryListing){
              //  System.out.println(rs.getAbsoluteFile());
                if(rs.getName().contains("redescription") && rs.getName().contains(".rr")){
                    RedescriptionSet redset = new RedescriptionSet();
                    redset.loadRedescriptionsFromFileSet(rs, datJ);
                    
                    if(loaded == 0){
                     if(redset.redescriptions.isEmpty()) continue;
                    for(int rind = 0;rind<redset.redescriptions.get(0).ruleStrings.size();rind++){
                        dats.add(datJ); mappings.add(fid);
                    }
                       loaded = 1;
                    }

                    cc++;
                    System.out.println("F: "+cc);
                    double hom; int cnt = 0, cnt1 = 0;
                    
                    for(int rind = 0; rind<redset.redescriptions.size();rind++){
                        cnt1++;
                        //System.out.println(redset.redescriptions.get(rind));
                        redset.redescriptions.get(rind).computeStatisticsRE(dats, mappings);
                        // System.out.println(redset.redescriptions.get(rind));
                        hom = redset.redescriptions.get(rind).computeHomogeneity(trainLabels,fid);
                        if(max<hom) max = hom;//2.9995968547489635
                        if(hom<2.4 && redset.redescriptions.get(rind).elements.size()<0.5*trainLabels.entityLabel.keySet().size() && redset.redescriptions.get(rind).JS>0.6){
                            Redescription r = redset.redescriptions.get(rind).deepCopy1(redset.redescriptions.get(rind));
                            if((++cnt%1000)==0){
                                System.out.println(rs.getAbsoluteFile());
                                System.out.println(cnt1+" "+cnt);
                            }
                            r.closeInterval(datJ, fid);
                           // System.out.println(r);
                            redset1.redescriptions.add(r);
                        }
                    }
                    
                    //compute entropy for all reds, make selection baesd on entropy and support size
                    
                    
                }
            }
        }
        
        System.out.println("max hom: "+max);
        
        redset1.writeToFileHomogeneity(resultFolder+"Selected.rr", datJ, fid, 0, 0, appset, 0, new double[2],new boolean[2],trainLabels);
    
    }
}
