/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redescriptionmining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import org.javatuples.Pair;
import org.javatuples.Triplet;

/**
 *
 * @author matej
 */
public class ComputeAttributePairwiseAssociationsMW {
    

    final static Charset ENCODING = StandardCharsets.UTF_8;
    public static void main(String argv[]){
        
        // File input=new File("C:\\Users\\matej\\Documents\\Redescription mining with CLUS\\redescriptionsMetalsLikvorAssociationsADStLev_0 minjs 0.5 JSType 0.rr");
           //File input=new File("C:\\Users\\matej\\Documents\\Redescription mining with CLUS\\redescriptionsMetalsPlazmaAssociationsADStLev_0 minjs 0.5 JSType 0.rr");
        //File input=new File("C:\\Users\\matej\\Documents\\Redescription mining with CLUS\\RedsWithDistTargetedDPMetals0.5PlazmaMedSupAll.txt");
        //File input=new File("C:\\Users\\matej\\Documents\\Redescription mining with CLUS\\RedsWithDistTargetedDPMetals0.5LikvorMedSupAll.txt");
         //File input=new File("C:\\Users\\matej\\Documents\\Redescription mining with CLUS\\redescriptionsMetalsPlazmaAssociationsMWADStLev_0 minjs 0.5 JSType 0_10000_30000.rr");
        File input=new File("F:\\Matej Dokumenti\\Redescription mining with CLUS\\Experimenti metali\\RedsWithDistTargetedMetals0.5MWMedSupAll.txt");

//RedsWithDistTargetedDPMetals0.5PlazmaMedSupAll
            BufferedReader reader;
            
            String dataInput="";//add path
            //DataSetCreator dat=new DataSetCreator(dataInput);
           // ArrayList<RedescriptionReReMi> red=new ArrayList<>();
        
            HashMap<String,Integer> attrToIndex=new HashMap<>(1000);
            HashMap<Integer,String> indexToAttr = new HashMap<>(1000);
            HashMap<Integer,ArrayList<Pair<Integer,Integer>>> associations=new HashMap<>(1000000);
                 
            //HashSet<String> view1=new HashSet<>(1000);
            //HashSet<String> view2=new HashSet<>(1000);
            ArrayList<HashSet<String>> views = new ArrayList<>();
            
            int numViews = 3;
            
            for(int i=0;i<numViews;i++)
                views.add(new HashSet<String>(1000));
            
            ArrayList<String> lines = new ArrayList<>();
       
            HashMap<Integer, ArrayList<Pair<HashSet<Integer>,Integer>>> mwAssociations = new HashMap<>(1000000);//store all multi-view associations
            
            int countA=0;
            
          try {
      Path path =Paths.get(input.getAbsolutePath());
      System.out.println("Path: "+input.getAbsolutePath());
      reader = Files.newBufferedReader(path,ENCODING);
      String line = null;
      int count=0,uniqueid=0;
      
      int ElemCount=0;
      int AttrCount=0;
           
      //RedescriptionReReMi r=null;
       Redescription r=null;
       int covered=0;
       
       //HashSet<Integer> index1=new HashSet<>(1000);
       // HashSet<Integer> index2=new HashSet<>(1000);
        ArrayList<HashSet<Integer>> indexes = new ArrayList<>();
        
        for(int v=0;v<numViews;v++)
            indexes.add(new HashSet<Integer>(1000));
   
        int skip=0, skipCount=0;
        int test=1;
      while ((line = reader.readLine()) != null){
          
          if(line.contains("Dist: ") && !line.contains("Perc. Dist:")){
              String dis1[]=line.split(":");
              String entr[]=dis1[1].split(" ");//check if this catches control
              int nCN=Integer.parseInt(entr[1]);

              if(nCN>1)
                  skip=1;
          }
          
          if(skip==1 && skipCount<(13+numViews)){
              skipCount++;
              if(skipCount==(12+numViews)){
                  skip=0;
                  skipCount=0;
                 lines.remove(lines.size()-1);
                 lines.remove(lines.size()-1); 
              }
              //System.out.println("Skipped: ");
              //System.out.println(line);
              continue;
          }
          
          lines.add(line);
         
        for(int v=0;v<numViews;v++){
            String l = "W"+(v+1)+"R: ";
          if(line.contains(l)){
          //if(line.contains("W1R: ") || line.contains("W2R: ")){
          
               String ruleString=line.substring(5);
               
               String tmp[]=ruleString.split("AND");
               
               for(int k=0;k<tmp.length;k++){
                   String atT=tmp[k];
                   String aTT[]=atT.split(">=");
                  // String aTT1[]=aTT[1].split("<=");
                   String attribute=aTT[0].trim();
                   
                   views.get(v).add(attribute);
                   
                   System.out.println("views size: "+views.get(v).size());
                   
                   /*if(line.contains("W1R: "))
                       view1.add(attribute);
                   else if(line.contains("W2R: "))
                       view2.add(attribute);*/
                   
                  if(!attrToIndex.keySet().contains(attribute)){
                      attrToIndex.put(attribute, countA);
                      indexToAttr.put(countA, attribute);
                      associations.put(countA, new ArrayList<Pair<Integer,Integer>>(1000));
                      countA++;
                  }
                  
                  indexes.get(v).add(attrToIndex.get(attribute));
                  System.out.println("indexes: "+indexes.get(v).size());
                 /* if(line.contains("W1R: "))
                      index1.add(attrToIndex.get(attribute));
                  else
                      index2.add(attrToIndex.get(attribute));*/
                  
               }
               
               System.out.println(line+"\n");
               
               for(int k=0;k<tmp.length-1;k++){
                   String atT=tmp[k];
                   String aTT[]=atT.split(">=");
//                   String aTT1[]=aTT[1].split("<=");
                   String attribute=aTT[0].trim();
                  
                   ArrayList<Pair<Integer,Integer>> tmpList=associations.get(attrToIndex.get(attribute));
                    int ind1=attrToIndex.get(attribute);
                   
                    for(int z=k+1;z<tmp.length;z++){
                      String atT2=tmp[z];
                   String aTT2[]=atT2.split(">=");
                   String attribute2=aTT2[0].trim();
                   int ind2=attrToIndex.get(attribute2);
                   int found=0, findex=-1;
                   
                       for(int kIter=0;kIter<tmpList.size();kIter++){
                           if(tmpList.get(kIter).getValue0()==ind2){
                               found=1;
                               findex=kIter;
                               break;
                           }       
                       }
                       
                       if(found==0){
                           tmpList.add(new Pair<>(ind2,1));
                           associations.get(ind2).add(new Pair(attrToIndex.get(attribute),1));
                       }
                       else{
                           int ct=tmpList.get(findex).getValue1();
                           ct++;
                           Pair<Integer,Integer> tmpP=tmpList.get(findex);
                           tmpP=tmpP.setAt1(ct);
                           tmpList.set(findex, tmpP);
                           
                           ArrayList<Pair<Integer,Integer>> tmpLP1=associations.get(ind2);
                           
                             for(int kIter=0;kIter<tmpLP1.size();kIter++){
                           if(tmpLP1.get(kIter).getValue0()==ind1){
                               found=1;
                               int c2=tmpLP1.get(kIter).getValue1();
                               c2++;
                               Pair<Integer,Integer> tp2=tmpLP1.get(kIter);
                               tp2=tp2.setAt1(c2);
                               tmpLP1.set(kIter, tp2);
                               break;
                           }         
                        }   
                       }
                   }     
               } 
               
            
               
               //compute regular inter-view associations
               
               
         
              /* if(line.contains("W2R: ")){//modify to compute MW associations
                   System.out.println("Entered contains W2R");
                   System.out.println("AS: "); System.out.println(associations.keySet().size());
                  // System.out.println("I1: "+index1.size()); System.out.println("I2: "+index2.size());
                   for(Integer el:index1){
                        ArrayList<Pair<Integer,Integer>> tmpList=associations.get(el);
                       for(Integer el2:index2){
                           int found=0, findex=-1;
                   
                       for(int kIter=0;kIter<tmpList.size();kIter++){
                           if((int)tmpList.get(kIter).getValue0()==(int)el2){
                               found=1;
                               findex=kIter;
                               break;
                           }
                       } 
                           if(found==0){
                           tmpList.add(new Pair<>(el2,1));
                           associations.get(el2).add(new Pair(el,1));
                       }
                       else{
                           int ct=tmpList.get(findex).getValue1();
                           ct++;
                           Pair<Integer,Integer> tmpP=tmpList.get(findex);
                           tmpP=tmpP.setAt1(ct);
                           tmpList.set(findex, tmpP);
                           
                           ArrayList<Pair<Integer,Integer>> tmpLP1=associations.get(el2);
                           
                             for(int kIterT=0;kIterT<tmpLP1.size();kIterT++){
                           if((int)tmpLP1.get(kIterT).getValue0()==(int)el){
                               found=1;
                               int c2=tmpLP1.get(kIterT).getValue1();
                               c2++;
                               Pair<Integer,Integer> tp2=tmpLP1.get(kIterT);
                               tp2=tp2.setAt1(c2);
                               tmpLP1.set(kIterT, tp2);
                               break;
                           }         
                        }   
                       }  
                       }
                   }
                   System.out.println("Exit W2Contains");
                   //index1.clear(); index2.clear();
                  
               }*/
             
          }
        }
        
        
         
         /* else if(line.contains("W2R: ")){
              r.ruleStrings.add(line.substring(5));
              String ruleString=line.substring(5);
               
               String tmp[]=ruleString.split("AND");
               
               for(int k=0;k<tmp.length;k++){
                   String atT=tmp[k];
                   String aTT[]=atT.split(">=");
                   String aTT1[]=aTT[1].split("<=");
                   String atribute=aTT[0].trim();
                   ArrayList<Double> boundaries=new ArrayList<>();
                   boundaries.add(Double.parseDouble(aTT1[0]));
                   boundaries.add(Double.parseDouble(aTT1[1]));
                   attrsIntervals.put(uniqueid++, new Triplet(count,fid.attId.get(atribute),boundaries));
               }
               count++;
          }*/
           if(line.contains("JS: ")){
                 ArrayList<HashSet<Integer>> mwAsocTmp = new ArrayList<>();
               HashSet<Integer> w1 = indexes.get(0);
               HashSet<Integer> w2 = indexes.get(1);
               
               System.out.println("sets: "+w1.size()+" "+w2.size());
               
               for(int ent:w1){
                   for(int ent2:w2){
                       HashSet<Integer> tmpS = new HashSet<>();
                       tmpS.add(ent); tmpS.add(ent2);
                       mwAsocTmp.add(tmpS);
                   }
               }
               
               System.out.println("mwAsocTmp2W: "+mwAsocTmp.size());
               
               for(int zz=2;zz<numViews;zz++){
                   HashSet<Integer> tmpS = indexes.get(zz);
                    ArrayList<HashSet<Integer>> tmp2 = new ArrayList<>();
                    
                    for(int ent:tmpS){
                        for(int f=0;f<mwAsocTmp.size();f++){
                            HashSet<Integer> tmpS2 = new HashSet<>();
                            tmpS2.add(ent); tmpS2.addAll(mwAsocTmp.get(f));
                            tmp2.add(tmpS2);
                        }
                    }
                    
                    mwAsocTmp.clear();
                    mwAsocTmp.addAll(tmp2);
                    tmp2.clear();
                    
               }
              
        // HashMap<Integer, Pair<ArrayList<HashSet<Integer>>,Integer>> mwAssociations = new HashMap<>(1000000);//store all multi-view associations
            System.out.println("mwAsocTmp size: "+mwAsocTmp.size());
               for(int zz=0;zz<mwAsocTmp.size();zz++){
                  HashSet<Integer> tmpS = mwAsocTmp.get(zz);
                  
                  for(int ent:tmpS){
                      if(indexes.get(0).contains(ent)){
                          if(mwAssociations.containsKey(ent)){
                              ArrayList<Pair<HashSet<Integer>,Integer>> entSet = mwAssociations.get(ent);
                                int found = 0;
                              for(int ivv=0;ivv<entSet.size();ivv++){
                                  Pair<HashSet<Integer>,Integer> ss = entSet.get(ivv);
                                  if(ss.getValue0().size() == tmpS.size() && ss.getValue0().containsAll(tmpS)){
                                      int countT = ss.getValue1();
                                      countT++;
                                      System.out.println("countT: "+countT);
                                      ss=ss.setAt1(countT);
                                      entSet.set(ivv, ss);
                                      System.out.println("ent set: "+entSet.get(ivv).getValue1());
                                      found = 1;
                                      break;
                                  }
                              }
                              if(found == 0){                                  
                                  entSet.add(new Pair<>(tmpS,1)); 
                              }
                                             }
                          else{
                              mwAssociations.put(ent, new ArrayList<Pair<HashSet<Integer>,Integer>>());
                               ArrayList<Pair<HashSet<Integer>,Integer>> entSet = mwAssociations.get(ent);
                                entSet.add(new Pair<>(tmpS,1));
                          }
                      }
                  }      
               }
        
          for(int c=0;c<numViews;c++)
                       indexes.get(c).clear(); 
             continue;
          }
          else if(line.contains("p-value: ")){
              continue;
          }
          else if(line.contains("Support intersection: ") || line.contains("Support union: "))
              continue;
          else if(line.contains("Covered examples (intersection):"))
              continue;
          else if(line.contains("\""))
              continue;
             /* if(covered==1){
                  //line=line.replace("\"", "");
                  String tmp[]=line.split(" ");
                  for(int i=0;i<tmp.length;i++)
                      r.elements.add(fid.exampleId.get(tmp[i]));
                  covered=0;
              }   */
        }
    
      reader.close();
         }
         catch(Exception e){
             e.printStackTrace();
         }

          System.out.println("Done reading file!");
          
         /* int test=1;
          if(test==1)
              return;*/
          
       int k=50;
       int min=Integer.MAX_VALUE,minIndex=-1;
      // int minW1=Integer.MAX_VALUE,minIndexW1=-1;
       //int minW2=Integer.MAX_VALUE,minIndexW2=-1;
       
      //ArrayList<Triplet<Integer,Integer,Integer>> result=new ArrayList<>();
       ArrayList<Pair<HashSet<Integer>,Integer>> result=new ArrayList<>();
       //ArrayList<Triplet<Integer,Integer,Integer>> resultW1=new ArrayList<>();
       //ArrayList<Triplet<Integer,Integer,Integer>> resultW2=new ArrayList<>();
       ArrayList<ArrayList<Triplet<Integer,Integer,Integer>>> resultsIntraView = new ArrayList<>();
      
       int minW = 0, minIndexW = -1;
       for(int w=0;w<numViews;w++){
           resultsIntraView.add(new ArrayList<Triplet<Integer,Integer,Integer>>());
           minW = Integer.MAX_VALUE; minIndexW = -1;
           for(Integer at:associations.keySet()){
               ArrayList<Pair<Integer,Integer>> tmpList=associations.get(at);
             
               if(views.get(w).contains(indexToAttr.get(at))){
                   for(int i=0;i<tmpList.size();i++){
                    int duplicate=0;
                   
                   for(int z1=0;z1<resultsIntraView.get(w).size();z1++){
                       if(((int)resultsIntraView.get(w).get(z1).getValue0()==(int)tmpList.get(i).getValue0()) && ((int)resultsIntraView.get(w).get(z1).getValue1()==(int) at)){
                           duplicate=1;
                           break;
                       }
                   }
                   
                   if(duplicate==1)
                       continue;
                   
                   if(resultsIntraView.get(w).size()<k){
                    Triplet nt=new Triplet(at,tmpList.get(i).getValue0(),tmpList.get(i).getValue1());
                  resultsIntraView.get(w).add(nt);
                   if(tmpList.get(i).getValue1()<minW){
                       minW=tmpList.get(i).getValue1();
                       minIndexW=resultsIntraView.get(w).size()-1;
                   }
               }
               else{
                   if(tmpList.get(i).getValue1()>minW){
                       Triplet nt=new Triplet(at,tmpList.get(i).getValue0(),tmpList.get(i).getValue1());
                        resultsIntraView.get(w).set(minIndexW, nt);
                   }
                   
                   minW=resultsIntraView.get(w).get(0).getValue2();
                   minIndexW=0;
                   for(int j=1;j<resultsIntraView.get(w).size();j++){
                       if(resultsIntraView.get(w).get(j).getValue2()<minW){
                           minW=resultsIntraView.get(w).get(j).getValue2();
                           minIndexW=j;
                       }
                   }
                 }
               }
               }
               else continue;
               
           }
       }
       
       /*for(Integer at:associations.keySet()){
           ArrayList<Pair<Integer,Integer>> tmpList=associations.get(at);
           
           for(int i=0;i<tmpList.size();i++){
               if((view1.contains(indexToAttr.get(at)) && view2.contains(indexToAttr.get(tmpList.get(i).getValue0())))){
                   
                   if(result.size()<k){
                   Triplet nt=new Triplet(at,tmpList.get(i).getValue0(),tmpList.get(i).getValue1());
                   result.add(nt);
                   if(tmpList.get(i).getValue1()<min){
                       min=tmpList.get(i).getValue1();
                       minIndex=result.size()-1;
                   }
               }
               else{
                   if(tmpList.get(i).getValue1()>min){
                       Triplet nt=new Triplet(at,tmpList.get(i).getValue0(),tmpList.get(i).getValue1());
                       if((Integer)nt.getValue2()>result.get(minIndex).getValue2())
                        result.set(minIndex, nt);
                   }
                   
                   min=result.get(0).getValue2();
                   minIndex=0;
                   for(int j=1;j<result.size();j++){
                       if(result.get(j).getValue2()<min){
                           min=result.get(j).getValue2();
                           minIndex=j;
                       }
                   }   
               }
                   
           }
               else if(view1.contains(indexToAttr.get(at)) && view1.contains(indexToAttr.get(tmpList.get(i).getValue0()))){
                   int duplicate=0;
                   
                   for(int z1=0;z1<resultW1.size();z1++){
                       if(((int)resultW1.get(z1).getValue0()==(int)tmpList.get(i).getValue0()) && ((int)resultW1.get(z1).getValue1()==(int) at)){
                           duplicate=1;
                           break;
                       }
                   }
                   
                   if(duplicate==1)
                       continue;
                   
                   if(resultW1.size()<k){
                    Triplet nt=new Triplet(at,tmpList.get(i).getValue0(),tmpList.get(i).getValue1());
                   resultW1.add(nt);
                   if(tmpList.get(i).getValue1()<minW1){
                       minW1=tmpList.get(i).getValue1();
                       minIndexW1=resultW1.size()-1;
                   }
               }
               else{
                   if(tmpList.get(i).getValue1()>minW1){
                       Triplet nt=new Triplet(at,tmpList.get(i).getValue0(),tmpList.get(i).getValue1());
                        resultW1.set(minIndexW1, nt);
                   }
                   
                   minW1=resultW1.get(0).getValue2();
                   minIndexW1=0;
                   for(int j=1;j<resultW1.size();j++){
                       if(resultW1.get(j).getValue2()<minW1){
                           minW1=resultW1.get(j).getValue2();
                           minIndexW1=j;
                       }
                   }
                 }
               }
               else if(view2.contains(indexToAttr.get(at)) && view2.contains(indexToAttr.get(tmpList.get(i).getValue0()))){
                   
                   int duplicate=0;
                   
                   for(int z1=0;z1<resultW2.size();z1++){
                       if(((int)resultW2.get(z1).getValue0()==(int)tmpList.get(i).getValue0()) && ((int)resultW2.get(z1).getValue1()==(int) at)){
                           duplicate=1;
                           break;
                       }
                   }
                   
                   if(duplicate==1)
                       continue;
                   
                   
                   if(resultW2.size()<k){
                       Triplet nt=new Triplet(at,tmpList.get(i).getValue0(),tmpList.get(i).getValue1());
                   resultW2.add(nt);
                   //resultW2.add(tmpList.get(i));
                   if(tmpList.get(i).getValue1()<minW2){
                       minW2=tmpList.get(i).getValue1();
                       minIndexW2=resultW2.size()-1;
                   }
               }
               else{
                   if(tmpList.get(i).getValue1()>minW2){
                       Triplet nt=new Triplet(at,tmpList.get(i).getValue0(),tmpList.get(i).getValue1());
                        resultW2.set(minIndexW2, nt);
                        //resultW2.set(minIndexW2, tmpList.get(i));
                   }
                   
                   minW2=resultW2.get(0).getValue2();
                   minIndexW2=0;
                   for(int j=1;j<resultW2.size();j++){
                       if(resultW2.get(j).getValue2()<minW2){
                           minW2=resultW2.get(j).getValue2();
                           minIndexW2=j;
                       }
                   }
                 }
               }
               else
                   continue;
           }
       }*/
       
       //select top k inter-view associations
       minIndexW = -1; minW = Integer.MAX_VALUE;
       ArrayList<Pair<HashSet<Integer>,Integer>> asF = new ArrayList<>();
        for(int key:mwAssociations.keySet()){ 
             ArrayList<Pair<HashSet<Integer>,Integer>> t = mwAssociations.get(key);
             System.out.println("t size: "+t.size());
        for(int ii=0;ii<t.size();ii++){
            if(asF.size()<k){
                if(minW>t.get(ii).getValue1()){
                    minW = t.get(ii).getValue1();
                    minIndexW = asF.size();
                }
                asF.add(t.get(ii));
            }
            else{
                if(t.get(ii).getValue1()>minW){
                    asF.set(minIndexW, t.get(ii));
                    minW = asF.get(0).getValue1();
                    minIndexW = 0;
                    
                    for(int iv=1;iv<asF.size();iv++){
                        if(minW>asF.get(iv).getValue1()){
                            minW = asF.get(iv).getValue1();
                            minIndexW = iv;
                        }
                    }
                }
            }
        }     
             
        }
       
       
       //File outE1=new File("C:\\Users\\matej\\Documents\\Redescription mining with CLUS\\associationsMetali0.5.txt");
       // File outE1=new File("C:\\Users\\matej\\Documents\\Redescription mining with CLUS\\associationsMetaliPlazma0.5.txt");
       // File outE1=new File("C:\\Users\\matej\\Documents\\Redescription mining with CLUS\\associationsMetaliPlazmaFiltered0.5.txt");
       // File outE1=new File("C:\\Users\\matej\\Documents\\Redescription mining with CLUS\\associationsMetaliLikvorFiltered0.5.txt");
        //File outE1=new File("C:\\Users\\matej\\Documents\\Redescription mining with CLUS\\associationsMetaliMW0.5.txt");
        File outE1=new File("F:\\Matej Dokumenti\\Redescription mining with CLUS\\Experimenti metali\\associationsMetaliFilteredMW0.5.txt");
       
        
       try{
         PrintWriter out = new PrintWriter(outE1.getAbsolutePath());
         
       AsociationComparator c = new AsociationComparator();
       Association1Comparator c1 = new Association1Comparator();
      // Collections.sort(result,c);
      // Collections.sort(resultW1,c);
      // Collections.sort(resultW2,c);
       
       for(int i=0;i<numViews;i++)
           Collections.sort(resultsIntraView.get(i),c);
       
       Collections.sort(asF,c1);
         
         out.write("Inter-view associations:\n\n");
         //for(int i=0;i<result.size();i++){
      //   for(int key:mwAssociations.keySet()){   
      //       ArrayList<Pair<HashSet<Integer>,Integer>> dt = mwAssociations.get(key);
             for(int ii=0;ii<asF.size();ii++){
                 HashSet<Integer> ds = asF.get(ii).getValue0();
                 String atString = "";
                 for(int j:ds)
                     atString+=indexToAttr.get(j)+" ";
                 atString+=asF.get(ii).getValue1();
                 out.write(atString+"\n");
//             out.write(indexToAttr.get(result.get(i).getValue0())+" "+indexToAttr.get(result.get(i).getValue1())+" "+result.get(i).getValue2()+"\n");
             }
         // }
         
         out.write("\n\n");
         
        for(int i=0;i<numViews;i++){
             out.write("W"+(i+1)+" associations:\n\n");
              for(int j=0;j<resultsIntraView.get(i).size();j++){
                     out.write(indexToAttr.get(resultsIntraView.get(i).get(j).getValue0())+" "+indexToAttr.get(resultsIntraView.get(i).get(j).getValue1())+" "+resultsIntraView.get(i).get(j).getValue2()+"\n");
              }
              out.write("\n\n");
        } 
         
        /* out.write("W1 associations:\n\n");
         for(int i=0;i<resultW1.size();i++){
             out.write(indexToAttr.get(resultW1.get(i).getValue0())+" "+indexToAttr.get(resultW1.get(i).getValue1())+" "+resultW1.get(i).getValue2()+"\n");
         }
         
          out.write("\n\n");
         
         out.write("W2 associations:\n\n");
         for(int i=0;i<resultW2.size();i++){
             out.write(indexToAttr.get(resultW2.get(i).getValue0())+" "+indexToAttr.get(resultW2.get(i).getValue1())+" "+resultW2.get(i).getValue2()+"\n");
         }*/
         
         out.close();
         }
         catch(FileNotFoundException ex){
             ex.printStackTrace();
         }
       
       File outputRedsFiltered = new File("F:\\Matej Dokumenti\\Redescription mining with CLUS\\Experimenti metali\\RedsWithDistTargetedDPMetals0.5MWMedSupAllFiltered.txt");
       
       try{
         PrintWriter out = new PrintWriter(outputRedsFiltered.getAbsolutePath());
         
           for(int i=0;i<lines.size();i++)
               out.write(lines.get(i)+"\n");
         
         out.close();
         }
         catch(FileNotFoundException ex){
             ex.printStackTrace();
         }
          
    }
}
