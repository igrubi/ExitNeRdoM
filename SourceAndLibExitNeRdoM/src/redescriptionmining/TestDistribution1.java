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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author matmih
 */
public class TestDistribution1 {
    public static void main(String args[]){
       // String supp = "'9986' '9943' '9942' '9889' '9882' '9872' '9821' '9804' '9797' '9727' '9717' '9707' '9697' '9680' '9670' '9654' '9624' '9604' '9582' '9581' '9561' '9558' '9548' '9536' '9524' '9511' '9501' '9460' '9445' '9431' '9429' '9409' '9390' '9380' '9370' '9365' '9305' '9283' '9275' '9261' '9235' '9231' '9229' '9192' '9182' '9177' '9175' '9157' '9125' '9115' '9106' '9100' '9083' '9059' '9056' '9011' '8972' '8970' '8957' '8952' '8946' '8939' '8924' '8916' '8910' '8903' '8876' '8864' '8860' '8856' '8854' '8842' '8840' '8821' '8811' '8801' '8780' '8777' '8713' '8706' '8701' '8694' '8660' '8598' '8597' '8581' '8570' '8561' '8551' '8533' '8518' '8483' '8471' '8461' '8451' '8430' '8419' '8411' '8407' '8393' '8379' '8370' '8346' '8337' '8328' '8272' '8271' '8226' '8215' '8206' '8186' '8171' '8167' '8162' '8154' '8140' '8130' '8120' '8087' '8080' '8070' '8032' '8026' '8014' '7995' '7993' '7983' '7981' '7968' '7956' '7936' '7771' '7763' '7740' '7719' '7714' '7692' '7690' '7675' '7674' '7656' '7644' '7639' '7628' '7620' '7610' '7590' '7554' '7543' '7509' '7501' '7470' '7458' '7435' '7396' '7374' '7365' '7350' '7326' '7312' '7311' '7292' '7272' '7250' '7222' '7215' '7214' '7207' '7185' '7157' '7140' '7131' '7130' '7119' '7117' '7104' '7103' '7097' '7075' '7065' '7055' '7024' '7010' '7006' '6985' '6982' '6957' '6930' '6918' '6907' '6882' '6876' '6875' '6873' '6863' '6862' '6857' '6811' '6794' '6767' '6736' '6727' '6714' '6704' '6690' '6676' '6665' '6646' '6624' '6593' '6551' '6535' '6504' '6494' '6481' '6447' '6440' '6432' '6413' '6389' '6382' '6381' '6379' '6371' '6370' '6365' '6322' '6312' '6299' '6289' '6272' '6260' '6226' '6213' '6204' '6194' '6184' '6177' '6151' '6116' '6103' '6096' '6075' '6061' '6034' '5945' '5935' '5925' '5904' '5860' '5850' '5804' '5796' '5780' '5766' '5764' '5760' '5747' '5724' '5700' '5687' '5635' '5625' '5606' '5596' '5585' '5583' '5555' '5542' '5538' '5516' '5508' '5492' '5479' '5472' '5446' '5418' '5382' '5359' '5356' '5346' '5336' '5328' '5293' '5283' '5273' '5248' '5245' '5225' '5224' '5215' '5214' '5212' '5185' '5181' '5168' '5150' '5062' '5028' '5000' '4983' '4973' '4940' '4928' '4925' '4904' '4894' '4885' '4873' '4854' '4841' '4836' '4818' '4796' '4785' '4782' '4770' '4758' '4755' '4727' '4714' '4688' '4664' '4647' '4638' '4621' '4613' '4600' '4586' '4581' '4541' '4537' '4535' '4528' '4526' '4492' '4430' '4406' '4397' '4366' '4352' '4335' '4334' '4288' '4276' '4270' '4244' '4242' '4234' '4220' '4164' '4162' '4106' '4097' '4087' '4084' '4026' '4004' '3977' '3965' '3953' '3947' '3944' '3916' '3910' '3897' '3896' '3834' '3825' '3824' '3812' '3769' '3766' '3746' '3740' '3717' '3715' '3701' '3686' '3670' '3659' '3642' '3630' '3620' '3579' '3531' '3527' '3515' '3493' '3487' '3486' '3484' '3476' '3469' '3449' '3443' '3407' '3390' '3363' '3299' '3290' '3285' '3283' '3261' '3248' '3221' '3217' '3193' '3177' '3150' '3131' '3104' '3094' '3085' '3083' '3082' '3080' '3074' '3045' '3015' '3010' '2992' '2991' '2989' '2985' '2978' '2955' '2931' '2924' '2914' '2906' '2893' '2881' '2871' '2865' '2840' '2807' '2799' '2785' '2776' '2767' '2745' '2731' '2723' '2712' '2690' '2681' '2663' '2647' '2639' '2634' '2632' '2629' '2624' '2623' '2594' '2587' '2583' '2575' '2566' '2555' '2551' '2549' '2522' '2511' '2502' '2501' '2453' '2441' '2427' '2364' '2348' '2347' '2312' '2303' '2300' '2284' '2281' '2255' '2230' '2153' '2151' '2139' '2128' '2086' '2083' '2080' '2076' '2072' '2069' '2045' '2032' '2031' '2011' '2008' '1997' '1983' '1962' '1953' '1928' '1926' '1923' '1889' '1845' '1840' '1837' '1832' '1804' '1803' '1797' '1779' '1765' '1763' '1742' '1738' '1731' '1690' '1683' '1661' '1628' '1615' '1608' '1605' '1591' '1588' '1586' '1539' '1519' '1489' '1480' '1479' '1442' '1426' '1367' '1317' '1315' '1298' '1291' '1284' '1278' '1265' '1239' '1215' '1208' '1205' '1172' '1134' '1127' '1109' '1095' '1069' '1067' '1066' '1042' '1028' '1020' '976' '957' '933' '927' '925' '923' '912' '890' '887' '855' '849' '847' '819' '811' '807' '801' '792' '782' '753' '747' '734' '727' '687' '686' '683' '669' '657' '648' '614' '595' '590' '577' '576' '573' '569' '548' '526' '515' '507' '505' '500' '493' '453' '451' '442' '437' '382' '335' '334' '323' '310' '309' '270' '216' '213' '205' '142' '112' '90' '76' '68' '51' '44' '32' '30' ";
       String supp = "'227' '182' '172' '161' '159' '157' '150' '134' '119' '60' '42' '39'";
              
       
        File input;
       // String path = "C:\\Users\\matmih\\Documents\\Experimenti\\InterpretableRM\\Target labels\\test-labelsMNIST.csv";
         String path = "C:\\Users\\matmih\\Documents\\Experimenti\\InterpretableRM\\Target labels\\test-labelsADNI.csv";
        BufferedReader read;
        HashMap<String,String> labels = new HashMap<>();
        HashMap<String,Integer> labelDist = new HashMap<>();
        
        try{
            Path p  = Paths.get(path);
            read = Files.newBufferedReader(p, StandardCharsets.UTF_8);
            
            String line = "";
            int cnt = 0;
            
            
            while((line = read.readLine())!=null){
                labels.put(cnt+++"", line.trim());
            }
            read.close();
        }
        catch(IOException e){}
        
        String a[] = supp.split(" ");
        
        
        for(int i=0;i<a.length;i++){
            String tmp = a[i].replaceAll("'", "");
   
            String cl = labels.get(tmp);
            if(labelDist.keySet().contains(cl)){
                labelDist.put(cl, labelDist.get(cl)+1);
                        }
            else{
                labelDist.put(cl, 1);
            }
        }
        
        Iterator<String> it = labelDist.keySet().iterator();
        
        while(it.hasNext()){
            String l = it.next();
            System.out.println(l+" "+labelDist.get(l));
        }
        
        int test = 1;
        if(test == 1) return;
        
         HashSet<String> n219 = new HashSet<>();
        HashSet<String> n229 = new HashSet<>();
        HashSet<String> n255 = new HashSet<>();
        HashSet<String> n231 = new HashSet<>();
        HashSet<String> interW1 = new HashSet<>();
        
        path = "C:\\Users\\matmih\\Documents\\Experimenti\\InterpretableRM\\Neuron219.arff";
         HashMap<String, Integer> CompDist = new HashMap<>();
          
        try{
            Path p  = Paths.get(path);
            read = Files.newBufferedReader(p, StandardCharsets.UTF_8);
            
            String line = "";
            int cnt = 0;

            while((line = read.readLine())!=null){
                double val = Double.parseDouble(line);
                
                if(val>=-5.33063697814941 && val <= 0.2002690583467483 ){
                    n219.add(cnt+"");
                    String cl = labels.get(cnt+"");
                    if(!CompDist.containsKey(cl))
                        CompDist.put(cl, 1);
                    else CompDist.put(cl, CompDist.get(cl)+1);
                }
                cnt++;           
            }
            read.close();
        }
        catch(IOException e){}
        
        path = "C:\\Users\\matmih\\Documents\\Experimenti\\InterpretableRM\\Neuron229.arff";
          CompDist = new HashMap<>();
          
        try{
            Path p  = Paths.get(path);
            read = Files.newBufferedReader(p, StandardCharsets.UTF_8);
            
            String line = "";
            int cnt = 0;

            while((line = read.readLine())!=null){
                double val = Double.parseDouble(line);
                
                if(val>=0.833896636962890 && val <= 3.06467103958129 ){
                    n229.add(cnt+"");
                    String cl = labels.get(cnt+"");
                    if(!CompDist.containsKey(cl))
                        CompDist.put(cl, 1);
                    else CompDist.put(cl, CompDist.get(cl)+1);
                }
                cnt++;           
            }
            read.close();
        }
        catch(IOException e){}
        
            path = "C:\\Users\\matmih\\Documents\\Experimenti\\InterpretableRM\\Neuron255.arff";
          CompDist = new HashMap<>();
          
        try{
            Path p  = Paths.get(path);
            read = Files.newBufferedReader(p, StandardCharsets.UTF_8);
            
            String line = "";
            int cnt = 0;

            while((line = read.readLine())!=null){
                double val = Double.parseDouble(line);
                
                if(val>=-2.789904356002807 && val <= 0.398872286081314 ){
                    n255.add(cnt+"");
                    String cl = labels.get(cnt+"");
                    if(!CompDist.containsKey(cl))
                        CompDist.put(cl, 1);
                    else CompDist.put(cl, CompDist.get(cl)+1);
                }
                cnt++;           
            }
            read.close();
        }
        catch(IOException e){}
        
         path = "C:\\Users\\matmih\\Documents\\Experimenti\\InterpretableRM\\Neuron231.arff";
          CompDist = new HashMap<>();
          
        try{
            Path p  = Paths.get(path);
            read = Files.newBufferedReader(p, StandardCharsets.UTF_8);
            
            String line = "";
            int cnt = 0;

            while((line = read.readLine())!=null){
                double val = Double.parseDouble(line);
                
                if(val>=-5.90499162673950 && val <= -0.501962065696716 ){
                    n231.add(cnt+"");
                    String cl = labels.get(cnt+"");
                    if(!CompDist.containsKey(cl))
                        CompDist.put(cl, 1);
                    else CompDist.put(cl, CompDist.get(cl)+1);
                }
                cnt++;           
            }
            read.close();
        }
        catch(IOException e){}
        
         it = n219.iterator();
        
        while(it.hasNext()){
            String e = it.next();
            
              if(n229.contains(e) && n255.contains(e) && n231.contains(e))
                     interW1.add(e);
        }
        
        System.out.println();
        System.out.println("Inter size W1: "+interW1.size());
        
        CompDist = new HashMap<>();
        
        it = interW1.iterator();
        
        while(it.hasNext()){
            String el = it.next();
            String cl = labels.get(el);
             if(!CompDist.containsKey(cl))
                        CompDist.put(cl, 1);
                    else CompDist.put(cl, CompDist.get(cl)+1);  
        }
        
        System.out.println("Dist of intersection W1: ");
        
         it = CompDist.keySet().iterator();
        
        while(it.hasNext()){
            String l = it.next();
            System.out.println(l+" "+CompDist.get(l));
        }
        
        
        
        HashSet<String> n34 = new HashSet<>();
        HashSet<String> interW2 = new HashSet<>();
        
        path = "C:\\Users\\matmih\\Documents\\Experimenti\\InterpretableRM\\Neuron34.arff";
         CompDist = new HashMap<>();
          
        try{
            Path p  = Paths.get(path);
            read = Files.newBufferedReader(p, StandardCharsets.UTF_8);
            
            String line = "";
            int cnt = 0;

            while((line = read.readLine())!=null){
                double val = Double.parseDouble(line);
                
                if(val<-6.22682285308837 || val > 2.033157825469970 ){
                    n34.add(cnt+"");
                    String cl = labels.get(cnt+"");
                    if(!CompDist.containsKey(cl))
                        CompDist.put(cl, 1);
                    else CompDist.put(cl, CompDist.get(cl)+1);
                }
                cnt++;           
            }
            read.close();
        }
        catch(IOException e){}
        
        interW2.addAll(n34);
        
        
         System.out.println();
        System.out.println("Inter size W2: "+interW2.size());
        
        CompDist = new HashMap<>();
        
        it = interW2.iterator();
        
        while(it.hasNext()){
            String el = it.next();
            String cl = labels.get(el);
             if(!CompDist.containsKey(cl))
                        CompDist.put(cl, 1);
                    else CompDist.put(cl, CompDist.get(cl)+1);  
        }
        
        System.out.println("Dist of intersection W2: ");
        
         it = CompDist.keySet().iterator();
        
        while(it.hasNext()){
            String l = it.next();
            System.out.println(l+" "+CompDist.get(l));
        }
        
        
        HashSet<String> n232F = new HashSet<>();
        
         path = "C:\\Users\\matmih\\Documents\\Experimenti\\InterpretableRM\\Neuron232.arff";
          CompDist = new HashMap<>();
          
        try{
            Path p  = Paths.get(path);
            read = Files.newBufferedReader(p, StandardCharsets.UTF_8);
            
            String line = "";
            int cnt = 0;

            while((line = read.readLine())!=null){
                double val = Double.parseDouble(line);
                
                if(val>=2.36974644660949 && val <= 4.51115989685058 ){
                    n232F.add(cnt+"");
                    String cl = labels.get(cnt+"");
                    if(!CompDist.containsKey(cl))
                        CompDist.put(cl, 1);
                    else CompDist.put(cl, CompDist.get(cl)+1);
                }
                cnt++;           
            }
            read.close();
        }
        catch(IOException e){}
        
        
         System.out.println();
        System.out.println("n232F: "+n232F.size());
        
        CompDist = new HashMap<>();
        
        it = n232F.iterator();
        
        while(it.hasNext()){
            String el = it.next();
            String cl = labels.get(el);
             if(!CompDist.containsKey(cl))
                        CompDist.put(cl, 1);
                    else CompDist.put(cl, CompDist.get(cl)+1);  
        }
        
        System.out.println("Dist of n232F: ");
        
         it = CompDist.keySet().iterator();
        
        while(it.hasNext()){
            String l = it.next();
            System.out.println(l+" "+CompDist.get(l));
        }
        
        
        HashSet<String> n232S = new HashSet<>();
        HashSet<String> n242 = new HashSet<>();
        HashSet<String> n234 = new HashSet<>();
        HashSet<String> n139 = new HashSet<>();
         HashSet<String> nInterC = new HashSet<>();
         
         
          path = "C:\\Users\\matmih\\Documents\\Experimenti\\InterpretableRM\\Neuron232.arff";
          CompDist = new HashMap<>();
          
        try{
            Path p  = Paths.get(path);
            read = Files.newBufferedReader(p, StandardCharsets.UTF_8);
            
            String line = "";
            int cnt = 0;

            while((line = read.readLine())!=null){
                double val = Double.parseDouble(line);
                
                if(val>=1.96814739704132 && val <= 2.74164700508117 ){
                    n232S.add(cnt+"");
                    String cl = labels.get(cnt+"");
                    if(!CompDist.containsKey(cl))
                        CompDist.put(cl, 1);
                    else CompDist.put(cl, CompDist.get(cl)+1);
                }
                cnt++;           
            }
            read.close();
        }
        catch(IOException e){}
        
            path = "C:\\Users\\matmih\\Documents\\Experimenti\\InterpretableRM\\Neuron242.arff";
          CompDist = new HashMap<>();
          
        try{
            Path p  = Paths.get(path);
            read = Files.newBufferedReader(p, StandardCharsets.UTF_8);
            
            String line = "";
            int cnt = 0;

            while((line = read.readLine())!=null){
                double val = Double.parseDouble(line);
                
                if(val>=2.41797900199890 && val <= 4.0023708343505 ){
                    n242.add(cnt+"");
                    String cl = labels.get(cnt+"");
                    if(!CompDist.containsKey(cl))
                        CompDist.put(cl, 1);
                    else CompDist.put(cl, CompDist.get(cl)+1);
                }
                cnt++;           
            }
            read.close();
        }
        catch(IOException e){}
        
         path = "C:\\Users\\matmih\\Documents\\Experimenti\\InterpretableRM\\Neuron234.arff";
          CompDist = new HashMap<>();
          
        try{
            Path p  = Paths.get(path);
            read = Files.newBufferedReader(p, StandardCharsets.UTF_8);
            
            String line = "";
            int cnt = 0;

            while((line = read.readLine())!=null){
                double val = Double.parseDouble(line);
                
                if(val>=1.05585503578186 && val <= 2.65426039695739){
                    n234.add(cnt+"");
                    String cl = labels.get(cnt+"");
                    if(!CompDist.containsKey(cl))
                        CompDist.put(cl, 1);
                    else CompDist.put(cl, CompDist.get(cl)+1);
                }
                cnt++;           
            }
            read.close();
        }
        catch(IOException e){}
        
         path = "C:\\Users\\matmih\\Documents\\Experimenti\\InterpretableRM\\Neuron139.arff";
          CompDist = new HashMap<>();
          
        try{
            Path p  = Paths.get(path);
            read = Files.newBufferedReader(p, StandardCharsets.UTF_8);
            
            String line = "";
            int cnt = 0;

            while((line = read.readLine())!=null){
                double val = Double.parseDouble(line);
                
                if(val>=-0.201708748936653 && val <= 1.22893130779266){
                    n139.add(cnt+"");
                    String cl = labels.get(cnt+"");
                    if(!CompDist.containsKey(cl))
                        CompDist.put(cl, 1);
                    else CompDist.put(cl, CompDist.get(cl)+1);
                }
                cnt++;           
            }
            read.close();
        }
        catch(IOException e){}
        
         it = n232S.iterator();
        
        while(it.hasNext()){
            String e = it.next();
            
              if(n242.contains(e) && n234.contains(e) && n139.contains(e))
                     nInterC.add(e);
        }
        
        
         System.out.println();
        System.out.println("Size nInterC: "+nInterC.size());
      
        it = nInterC.iterator();
        CompDist = new HashMap<>();
        while(it.hasNext()){
            String el = it.next();
            String cl = labels.get(el);
             if(!CompDist.containsKey(cl))
                        CompDist.put(cl, 1);
                    else CompDist.put(cl, CompDist.get(cl)+1);  
        }
        
        System.out.println("Dist of nInterC: ");
        
         it = CompDist.keySet().iterator();
        
        while(it.hasNext()){
            String l = it.next();
            System.out.println(l+" "+CompDist.get(l));
        }
       
         
          HashSet<String> unionW3 = new HashSet<>();
          
          unionW3.addAll(nInterC);
          unionW3.addAll( n232F);
          
           System.out.println();
        System.out.println("Size W3: "+unionW3.size());
      
        it = unionW3.iterator();
        CompDist = new HashMap<>();
        while(it.hasNext()){
            String el = it.next();
            String cl = labels.get(el);
             if(!CompDist.containsKey(cl))
                        CompDist.put(cl, 1);
                    else CompDist.put(cl, CompDist.get(cl)+1);  
        }
        
        System.out.println("Dist of W3: ");
        
         it = CompDist.keySet().iterator();
        
        while(it.hasNext()){
            String l = it.next();
            System.out.println(l+" "+CompDist.get(l));
        }
        
        
        HashSet<String> interW12 = new HashSet<>();
        
         it = interW1.iterator();
        
        while(it.hasNext()){
            String e = it.next();
            
              if(interW2.contains(e) )
                     interW12.add(e);
        }
        
        System.out.println("InterW12 size: "+interW12.size());
          
          
        
         HashSet<String> interInter = new HashSet<>();
        
          it = interW1.iterator();
        
        while(it.hasNext()){
            String e = it.next();
            
              if(interW2.contains(e) && unionW3.contains(e))
                     interInter.add(e);
        }
        
        
          System.out.println();
        System.out.println("Size redescription support: "+interInter.size());
      
        it = interInter.iterator();
        CompDist = new HashMap<>();
        while(it.hasNext()){
            String el = it.next();
            String cl = labels.get(el);
             if(!CompDist.containsKey(cl))
                        CompDist.put(cl, 1);
                    else CompDist.put(cl, CompDist.get(cl)+1);  
        }
        
        System.out.println("Dist of redescription support: ");
        
         it = CompDist.keySet().iterator();
        
        while(it.hasNext()){
            String l = it.next();
            System.out.println(l+" "+CompDist.get(l));
        }
        
        System.out.println();
        
        ArrayList<String> ar = new ArrayList<>();
        
        it = interInter.iterator();
        
        while(it.hasNext())
            ar.add(it.next());
        
        for(int i=0;i<ar.size()-1;i++)
            for(int j=i+1;j<ar.size();j++)
                if(Integer.parseInt(ar.get(i))<Integer.parseInt(ar.get(j))){
                    String tmp = ar.get(i);
                    ar.set(i,ar.get(j));
                    ar.set(j,tmp);
                    
                }
        
        for(int i=0;i<ar.size();i++)
            System.out.print(ar.get(i)+" ");
        
        
        System.out.println(); System.out.println();
        
        String out1 = "9986 9943 9942 9889 9882 9872 9821 9804 9797 9727 9717 9707 9697 9680 9670 9654 9624 9604 9582 9581 9561 9558 9548 9536 9524 9511 9501 9460 9445 9431 9429 9409 9390 9380 9370 9365 9305 9283 9275 9261 9235 9231 9229 9192 9182 9177 9175 9157 9125 9115 9106 9100 9083 9059 9056 9011 8972 8970 8957 8952 8946 8939 8924 8916 8910 8903 8876 8864 8860 8856 8854 8842 8840 8821 8811 8801 8780 8777 8713 8706 8701 8694 8660 8598 8597 8581 8570 8561 8551 8533 8518 8483 8471 8461 8451 8430 8419 8411 8407 8393 8379 8370 8346 8337 8328 8272 8271 8226 8215 8206 8186 8171 8167 8162 8154 8140 8130 8120 8087 8080 8070 8032 8026 8014 7995 7993 7983 7981 7968 7956 7936 7771 7763 7740 7719 7714 7692 7690 7675 7674 7656 7644 7639 7628 7620 7610 7590 7554 7543 7509 7501 7470 7458 7435 7396 7374 7365 7350 7326 7312 7311 7292 7272 7250 7222 7215 7214 7207 7185 7157 7140 7131 7130 7119 7117 7104 7103 7097 7075 7065 7055 7024 7010 7006 6985 6982 6957 6930 6918 6907 6882 6876 6875 6873 6863 6862 6857 6811 6794 6767 6736 6727 6714 6704 6690 6676 6665 6646 6624 6593 6551 6535 6504 6494 6481 6447 6440 6432 6413 6389 6382 6381 6379 6371 6370 6365 6322 6312 6299 6289 6272 6260 6226 6213 6204 6194 6184 6177 6151 6116 6103 6096 6075 6061 6034 5945 5935 5925 5904 5860 5850 5804 5796 5780 5766 5764 5760 5747 5724 5700 5687 5635 5625 5606 5596 5585 5583 5555 5542 5538 5516 5508 5492 5479 5472 5446 5418 5382 5359 5356 5346 5336 5328 5293 5283 5273 5248 5245 5225 5224 5215 5214 5212 5185 5181 5168 5150 5062 5028 5000 4983 4973 4940 4928 4925 4904 4894 4885 4873 4854 4841 4836 4818 4796 4785 4782 4770 4758 4755 4727 4714 4688 4664 4647 4638 4621 4613 4600 4581 4541 4537 4535 4528 4526 4492 4430 4406 4397 4366 4352 4335 4334 4288 4276 4270 4244 4242 4234 4220 4164 4162 4106 4097 4087 4026 4004 3977 3965 3953 3947 3944 3916 3910 3897 3896 3834 3825 3824 3812 3769 3766 3746 3740 3717 3715 3701 3686 3670 3659 3642 3630 3620 3579 3531 3527 3515 3493 3487 3486 3484 3476 3469 3449 3443 3407 3390 3363 3299 3290 3285 3283 3261 3248 3221 3217 3193 3177 3150 3131 3104 3094 3085 3083 3082 3080 3074 3045 3015 3010 2992 2991 2989 2985 2978 2955 2931 2924 2914 2906 2893 2881 2871 2865 2840 2807 2799 2785 2776 2767 2745 2731 2723 2712 2690 2681 2663 2647 2639 2634 2632 2629 2624 2623 2594 2587 2583 2575 2566 2555 2551 2549 2522 2511 2502 2501 2453 2441 2427 2364 2348 2347 2312 2303 2300 2284 2281 2255 2153 2151 2128 2086 2083 2080 2076 2072 2069 2045 2032 2031 2011 2008 1997 1983 1962 1953 1928 1926 1923 1845 1840 1837 1832 1804 1803 1797 1779 1765 1763 1742 1738 1731 1690 1683 1661 1628 1615 1608 1605 1591 1588 1586 1539 1519 1489 1480 1479 1442 1426 1367 1317 1315 1298 1291 1284 1278 1265 1239 1215 1208 1205 1172 1134 1127 1109 1095 1069 1067 1066 1042 1028 1020 976 957 933 927 925 923 912 890 887 855 849 847 819 811 807 801 792 782 753 747 734 727 687 686 683 669 657 648 614 595 590 577 576 573 569 548 526 515 507 505 500 493 453 451 442 437 382 335 334 323 310 309 270 216 213 205 142 112 90 76 68 51 44 32 30";
        String supp1 = "9986 9943 9942 9889 9882 9872 9821 9804 9797 9727 9717 9707 9697 9680 9670 9654 9624 9604 9582 9581 9561 9558 9548 9536 9524 9511 9501 9460 9445 9431 9429 9409 9390 9380 9370 9365 9305 9283 9275 9261 9235 9231 9229 9192 9182 9177 9175 9157 9125 9115 9106 9100 9083 9059 9056 9011 8972 8970 8957 8952 8946 8939 8924 8916 8910 8903 8876 8864 8860 8856 8854 8842 8840 8821 8811 8801 8780 8777 8713 8706 8701 8694 8660 8598 8597 8581 8570 8561 8551 8533 8518 8483 8471 8461 8451 8430 8419 8411 8407 8393 8379 8370 8346 8337 8328 8272 8271 8226 8215 8206 8186 8171 8167 8162 8154 8140 8130 8120 8087 8080 8070 8032 8026 8014 7995 7993 7983 7981 7968 7956 7936 7771 7763 7740 7719 7714 7692 7690 7675 7674 7656 7644 7639 7628 7620 7610 7590 7554 7543 7509 7501 7470 7458 7435 7396 7374 7365 7350 7326 7312 7311 7292 7272 7250 7222 7215 7214 7207 7185 7157 7140 7131 7130 7119 7117 7104 7103 7097 7075 7065 7055 7024 7010 7006 6985 6982 6957 6930 6918 6907 6882 6876 6875 6873 6863 6862 6857 6811 6794 6767 6736 6727 6714 6704 6690 6676 6665 6646 6624 6593 6551 6535 6504 6494 6481 6447 6440 6432 6413 6389 6382 6381 6379 6371 6370 6365 6322 6312 6299 6289 6272 6260 6226 6213 6204 6194 6184 6177 6151 6116 6103 6096 6075 6061 6034 5945 5935 5925 5904 5860 5850 5804 5796 5780 5766 5764 5760 5747 5724 5700 5687 5635 5625 5606 5596 5585 5583 5555 5542 5538 5516 5508 5492 5479 5472 5446 5418 5382 5359 5356 5346 5336 5328 5293 5283 5273 5248 5245 5225 5224 5215 5214 5212 5185 5181 5168 5150 5062 5028 5000 4983 4973 4940 4928 4925 4904 4894 4885 4873 4854 4841 4836 4818 4796 4785 4782 4770 4758 4755 4727 4714 4688 4664 4647 4638 4621 4613 4600 4586 4581 4541 4537 4535 4528 4526 4492 4430 4406 4397 4366 4352 4335 4334 4288 4276 4270 4244 4242 4234 4220 4164 4162 4106 4097 4087 4084 4026 4004 3977 3965 3953 3947 3944 3916 3910 3897 3896 3834 3825 3824 3812 3769 3766 3746 3740 3717 3715 3701 3686 3670 3659 3642 3630 3620 3579 3531 3527 3515 3493 3487 3486 3484 3476 3469 3449 3443 3407 3390 3363 3299 3290 3285 3283 3261 3248 3221 3217 3193 3177 3150 3131 3104 3094 3085 3083 3082 3080 3074 3045 3015 3010 2992 2991 2989 2985 2978 2955 2931 2924 2914 2906 2893 2881 2871 2865 2840 2807 2799 2785 2776 2767 2745 2731 2723 2712 2690 2681 2663 2647 2639 2634 2632 2629 2624 2623 2594 2587 2583 2575 2566 2555 2551 2549 2522 2511 2502 2501 2453 2441 2427 2364 2348 2347 2312 2303 2300 2284 2281 2255 2230 2153 2151 2139 2128 2086 2083 2080 2076 2072 2069 2045 2032 2031 2011 2008 1997 1983 1962 1953 1928 1926 1923 1889 1845 1840 1837 1832 1804 1803 1797 1779 1765 1763 1742 1738 1731 1690 1683 1661 1628 1615 1608 1605 1591 1588 1586 1539 1519 1489 1480 1479 1442 1426 1367 1317 1315 1298 1291 1284 1278 1265 1239 1215 1208 1205 1172 1134 1127 1109 1095 1069 1067 1066 1042 1028 1020 976 957 933 927 925 923 912 890 887 855 849 847 819 811 807 801 792 782 753 747 734 727 687 686 683 669 657 648 614 595 590 577 576 573 569 548 526 515 507 505 500 493 453 451 442 437 382 335 334 323 310 309 270 216 213 205 142 112 90 76 68 51 44 32 30 ";

        String tmp1[] = out1.split(" ");
        String tmp2 []= supp1.split(" ");

        HashSet<String> s1 = new HashSet<>();
        HashSet<String> s2 = new HashSet<>();
        
        for(String s:tmp1) s1.add(s);
        for(String s:tmp2) s2.add(s);
        
        it = s2.iterator();
        
        while(it.hasNext()){
            String el = it.next();
            if(!s1.contains(el.trim()))
                System.out.println(el);
        }
        
        System.out.println(); System.out.println();
        HashSet<String> strange = new HashSet<>();
        strange.add("1889"); strange.add("2139"); strange.add("2230");
        strange.add("4084"); strange.add("4586");
        
        it = strange.iterator();
        
        while(it.hasNext()){
            String el = it.next();
            if(!interW1.contains(el))
                System.out.println("Not contained in W1: "+el);
        }
        
        System.out.println();
        
        it = strange.iterator();
        
        while(it.hasNext()){
            String el = it.next();
            if(!interW2.contains(el))
                System.out.println("Not contained in W2: "+el);
        }
        
        System.out.println();
        
        it = strange.iterator();
        
        while(it.hasNext()){
            String el = it.next();
            if(!unionW3.contains(el))
                System.out.println("Not contained in W3: "+el);
        }
        
    }
}
