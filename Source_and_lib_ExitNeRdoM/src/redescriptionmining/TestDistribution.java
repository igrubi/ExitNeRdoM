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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author matmih
 */
public class TestDistribution {
    
    public static void main(String args[]){
        
        
        String supp = "'9994' '9984' '9978' '9969' '9956' '9955' '9950' '9946' '9931' '9923' '9912' '9903' '9898' '9896' '9878' '9876' '9848' '9846' '9845' '9844' '9838' '9836' '9819' '9810' '9802' '9799' '9796' '9795' '9774' '9772' '9760' '9737' '9725' '9715' '9705' '9699' '9689' '9682' '9674' '9661' '9657' '9653' '9649' '9645' '9641' '9622' '9612' '9602' '9599' '9594' '9593' '9591' '9570' '9556' '9549' '9520' '9518' '9509' '9499' '9489' '9485' '9464' '9444' '9443' '9438' '9435' '9434' '9415' '9407' '9401' '9395' '9388' '9378' '9368' '9356' '9348' '9345' '9336' '9335' '9324' '9313' '9301' '9295' '9291' '9282' '9274' '9265' '9258' '9249' '9241' '9233' '9222' '9200' '9190' '9180' '9171' '9155' '9143' '9140' '9124' '9121' '9120' '9112' '9096' '9090' '9081' '9071' '9061' '9051' '9049' '9039' '9030' '9025' '9017' '9005' '9003' '9002' '8995' '8988' '8986' '8980' '8979' '8976' '8971' '8938' '8930' '8922' '8914' '8905' '8892' '8887' '8875' '8873' '8862' '8850' '8819' '8809' '8799' '8776' '8768' '8758' '8745' '8740' '8729' '8724' '8720' '8715' '8708' '8692' '8682' '8672' '8666' '8659' '8658' '8650' '8636' '8633' '8604' '8600' '8586' '8575' '8568' '8559' '8549' '8545' '8543' '8540' '8538' '8536' '8526' '8509' '8493' '8491' '8488' '8469' '8459' '8449' '8443' '8439' '8432' '8427' '8420' '8418' '8396' '8390' '8376' '8367' '8360' '8352' '8344' '8340' '8324' '8306' '8305' '8303' '8289' '8286' '8268' '8261' '8252' '8244' '8239' '8233' '8231' '8229' '8219' '8187' '8166' '8164' '8159' '8138' '8128' '8118' '8113' '8100' '8099' '8090' '8088' '8086' '8078' '8068' '8058' '8048' '8045' '8022' '8020' '8005' '8004' '7990' '7980' '7973' '7969' '7954' '7944' '7934' '7920' '7916' '7911' '7900' '7899' '7896' '7890' '7881' '7869' '7856' '7847' '7839' '7832' '7822' '7812' '7811' '7806' '7802' '7790' '7784' '7783' '7748' '7738' '7728' '7725' '7722' '7717' '7708' '7686' '7665' '7661' '7658' '7638' '7636' '7626' '7618' '7608' '7600' '7591' '7582' '7581' '7567' '7563' '7561' '7556' '7527' '7517' '7507' '7480' '7468' '7463' '7453' '7447' '7442' '7424' '7422' '7418' '7411' '7405' '7399' '7395' '7373' '7353' '7344' '7340' '7325' '7323' '7319' '7303' '7299' '7290' '7280' '7270' '7262' '7253' '7247' '7232' '7228' '7226' '7223' '7217' '7191' '7183' '7176' '7173' '7171' '7167' '7165' '7150' '7138' '7123' '7091' '7087' '7073' '7063' '7053' '7048' '7023' '7019' '7014' '7005' '7000' '6976' '6969' '6961' '6948' '6938' '6928' '6917' '6913' '6902' '6901' '6891' '6869' '6868' '6848' '6829' '6819' '6809' '6799' '6789' '6758' '6757' '6751' '6748' '6743' '6733' '6729' '6726' '6720' '6712' '6702' '6694' '6688' '6683' '6680' '6678' '6673' '6670' '6661' '6652' '6644' '6634' '6628' '6623' '6622' '6618' '6604' '6601' '6556' '6545' '6540' '6533' '6527' '6515' '6506' '6502' '6484' '6482' '6472' '6468' '6456' '6445' '6438' '6430' '6419' '6408' '6407' '6397' '6375' '6374' '6356' '6338' '6329' '6320' '6310' '6308' '6278' '6276' '6254' '6239' '6233' '6232' '6231' '6224' '6222' '6202' '6192' '6182' '6162' '6159' '6141' '6135' '6125' '6115' '6110' '6108' '6101' '6092' '6083' '6073' '6063' '6060' '6058' '6044' '6012' '6008' '6005' '5998' '5991' '5984' '5978' '5970' '5953' '5943' '5933' '5917' '5915' '5902' '5896' '5893' '5889' '5880' '5873' '5872' '5859' '5848' '5839' '5831' '5811' '5809' '5794' '5786' '5783' '5776' '5762' '5759' '5746' '5738' '5732' '5722' '5699' '5689' '5679' '5666' '5661' '5651' '5646' '5642' '5637' '5630' '5614' '5604' '5594' '5590' '5588' '5566' '5553' '5552' '5550' '5544' '5535' '5534' '5524' '5520' '5514' '5506' '5499' '5490' '5486' '5477' '5461' '5460' '5457' '5453' '5442' '5431' '5416' '5406' '5399' '5384' '5370' '5361' '5350' '5331' '5327' '5323' '5318' '5316' '5314' '5291' '5281' '5271' '5258' '5254' '5252' '5232' '5227' '5211' '5208' '5193' '5186' '5166' '5156' '5148' '5132' '5128' '5113' '5112' '5093' '5092' '5091' '5090' '5081' '5072' '5052' '5044' '5036' '5025' '5014' '5013' '5005' '4984' '4977' '4972' '4953' '4951' '4949' '4932' '4931' '4930' '4927' '4924' '4917' '4909' '4903' '4877' '4872' '4871' '4869' '4864' '4859' '4858' '4853' '4847' '4806' '4778' '4774' '4764' '4759' '4754' '4733' '4732' '4729' '4719' '4717' '4708' '4691' '4687' '4685' '4676' '4674' '4670' '4655' '4653' '4651' '4646' '4643' '4606' '4603' '4602' '4597' '4595' '4589' '4588' '4580' '4574' '4564' '4563' '4533' '4525' '4524' '4516' '4491' '4450' '4428' '4409' '4386' '4349' '4337' '4331' '4328' '4318' '4308' '4304' '4303' '4292' '4273' '4267' '4264' '4262' '4249' '4232' '4216' '4195' '4191' '4190' '4185' '4181' '4179' '4178' '4171' '4170' '4169' '4168' '4153' '4147' '4144' '4138' '4110' '4105' '4104' '4085' '4077' '4074' '4069' '4057' '4050' '4039' '4036' '4035' '4032' '4025' '4021' '4014' '4010' '4006' '4005' '3990' '3983' '3974' '3937' '3930' '3925' '3922' '3919' '3900' '3894' '3866' '3858' '3852' '3851' '3843' '3815' '3809' '3789' '3786' '3784' '3779' '3777' '3765' '3761' '3747' '3745' '3741' '3733' '3699' '3689' '3679' '3673' '3669' '3652' '3651' '3649' '3648' '3641' '3639' '3638' '3625' '3606' '3605' '3601' '3593' '3586' '3582' '3562' '3546' '3532' '3480' '3471' '3464' '3457' '3455' '3454' '3452' '3438' '3434' '3433' '3430' '3421' '3420' '3419' '3386' '3380' '3379' '3354' '3353' '3351' '3320' '3314' '3281' '3276' '3272' '3268' '3264' '3255' '3253' '3244' '3237' '3231' '3230' '3227' '3214' '3211' '3203' '3196' '3175' '3152' '3148' '3143' '3126' '3124' '3099' '3097' '3092' '3070' '3050' '3039' '3027' '3026' '3019' '3014' '3003' '2997' '2984' '2982' '2974' '2965' '2950' '2946' '2943' '2928' '2912' '2902' '2885' '2880' '2878' '2868' '2867' '2827' '2825' '2822' '2816' '2815' '2803' '2789' '2788' '2786' '2757' '2753' '2746' '2734' '2725' '2722' '2719' '2706' '2704' '2693' '2688' '2676' '2674' '2661' '2655' '2654' '2626' '2612' '2599' '2590' '2576' '2553' '2541' '2529' '2524' '2519' '2510' '2504' '2473' '2444' '2421' '2418' '2416' '2411' '2409' '2398' '2379' '2366' '2359' '2357' '2356' '2355' '2335' '2324' '2316' '2315' '2302' '2287' '2283' '2277' '2276' '2273' '2261' '2258' '2245' '2239' '2235' '2228' '2221' '2175' '2171' '2166' '2164' '2154' '2137' '2136' '2071' '2051' '2041' '2034' '2027' '2017' '2015' '2013' '2010' '1994' '1993' '1988' '1986' '1945' '1939' '1934' '1922' '1897' '1885' '1884' '1876' '1868' '1867' '1861' '1844' '1838' '1836' '1835' '1834' '1830' '1829' '1820' '1791' '1785' '1780' '1773' '1766' '1760' '1753' '1736' '1729' '1728' '1715' '1707' '1704' '1674' '1673' '1667' '1659' '1657' '1646' '1643' '1633' '1630' '1603' '1582' '1555' '1548' '1528' '1515' '1484' '1483' '1448' '1434' '1430' '1424' '1397' '1392' '1368' '1360' '1358' '1351' '1350' '1338' '1329' '1318' '1316' '1305' '1302' '1295' '1280' '1257' '1254' '1240' '1238' '1236' '1214' '1213' '1211' '1201' '1193' '1189' '1180' '1179' '1139' '1137' '1136' '1129' '1097' '1083' '1075' '1054' '1040' '1038' '1037' '1030' '1027' '1025' '1019' '1011' '1008' '1004' '994' '988' '984' '979' '977' '967' '964' '963' '949' '948' '929' '920' '918' '907' '889' '850' '848' '840' '836' '835' '831' '826' '824' '818' '809' '798' '790' '783' '777' '772' '768' '767' '755' '754' '749' '745' '735' '725' '700' '696' '695' '682' '675' '672' '663' '652' '647' '640' '615' '584' '537' '529' '523' '506' '504' '489' '480' '476' '473' '455' '430' '427' '419' '416' '409' '393' '388' '385' '378' '377' '357' '354' '350' '348' '345' '342' '332' '330' '329' '314' '302' '288' '279' '276' '272' '267' '265' '251' '239' '228' '224' '204' '203' '202' '196' '190' '189' '180' '178' '176' '168' '154' '145' '143' '137' '135' '107' '94' '89' '74' '57' '46' '40' '39' '37' '31' '29' '14' '5' '2' ";
        
        
        File input;
        String path = "C:\\Users\\matmih\\Documents\\Experimenti\\InterpretableRM\\Target labels\\test-labelsMNIST.csv";
        
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
        
        HashSet<String> n234 = new HashSet<>();
        HashSet<String> n181 = new HashSet<>();
        HashSet<String> n157 = new HashSet<>();
        HashSet<String> n9 = new HashSet<>();
        HashSet<String> n203 = new HashSet<>();
        HashSet<String> n104 = new HashSet<>();
        HashSet<String> inter = new HashSet<>();
         HashSet<String> interInter = new HashSet<>();
        
        path = "C:\\Users\\matmih\\Documents\\Experimenti\\InterpretableRM\\Neuron140.arff";
          HashMap<String,Integer> CompDist = new HashMap<>();
          
        try{
            Path p  = Paths.get(path);
            read = Files.newBufferedReader(p, StandardCharsets.UTF_8);
            
            String line = "";
            int cnt = 0;

            while((line = read.readLine())!=null){
                double val = Double.parseDouble(line);
                
                if(val<-9.724295616149902 || val > 0.3992563486099243 ){
                     n104.add(cnt+"");
                    n203.add(cnt+"");
                    String cl = labels.get(cnt+"");
                    System.out.println("cnt: "+cnt);
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
                
                if(val>=0.7909627556800842 && val <= 2.884502410888672 ){
                    n234.add(cnt+"");
                    String cl = labels.get(cnt+"");
                    System.out.println("cnt: "+cnt);
                    if(!CompDist.containsKey(cl))
                        CompDist.put(cl, 1);
                    else CompDist.put(cl, CompDist.get(cl)+1);
                }
                cnt++;           
            }
            read.close();
        }
        catch(IOException e){}
        
        path = "C:\\Users\\matmih\\Documents\\Experimenti\\InterpretableRM\\Neuron181.arff";
         CompDist = new HashMap<>();
          
        try{
            Path p  = Paths.get(path);
            read = Files.newBufferedReader(p, StandardCharsets.UTF_8);
            
            String line = "";
            int cnt = 0;

            while((line = read.readLine())!=null){
                double val = Double.parseDouble(line);
                
                if(val>=-1.058100938796997 && val <= 1.8759573698043823  ){
                    n181.add(cnt+"");
                    String cl = labels.get(cnt+"");
                    System.out.println("cnt: "+cnt);
                    if(!CompDist.containsKey(cl))
                        CompDist.put(cl, 1);
                    else CompDist.put(cl, CompDist.get(cl)+1);
                }
                cnt++;           
            }
            read.close();
        }
        catch(IOException e){}
        
        
        path = "C:\\Users\\matmih\\Documents\\Experimenti\\InterpretableRM\\Neuron157.arff";
         CompDist = new HashMap<>();
          
        try{
            Path p  = Paths.get(path);
            read = Files.newBufferedReader(p, StandardCharsets.UTF_8);
            
            String line = "";
            int cnt = 0;

            while((line = read.readLine())!=null){
                double val = Double.parseDouble(line);
                
                if(val>=-1.9122748374938965 && val <= 0.6180122494697571  ){
                    n157.add(cnt+"");
                    String cl = labels.get(cnt+"");
                    System.out.println("cnt: "+cnt);
                    if(!CompDist.containsKey(cl))
                        CompDist.put(cl, 1);
                    else CompDist.put(cl, CompDist.get(cl)+1);
                }
                cnt++;           
            }
            read.close();
        }
        catch(IOException e){}
        
        path = "C:\\Users\\matmih\\Documents\\Experimenti\\InterpretableRM\\Neuron9.arff";
         CompDist = new HashMap<>();
          
        try{
            Path p  = Paths.get(path);
            read = Files.newBufferedReader(p, StandardCharsets.UTF_8);
            
            String line = "";
            int cnt = 0;

            while((line = read.readLine())!=null){
                double val = Double.parseDouble(line);
                
                if(val>=-1.6770859956741333 && val <= 3.5461184978485107 ){
                    n9.add(cnt+"");
                    String cl = labels.get(cnt+"");
                    System.out.println("cnt: "+cnt);
                    if(!CompDist.containsKey(cl))
                        CompDist.put(cl, 1);
                    else CompDist.put(cl, CompDist.get(cl)+1);
                }
                cnt++;           
            }
            read.close();
        }
        catch(IOException e){}
        
        it = n9.iterator();
        
        while(it.hasNext()){
            String e = it.next();
            
              if(n234.contains(e) && n181.contains(e) && n157.contains(e) && n203.contains(e) &&n104.contains(e))
                     inter.add(e);
        }
        
        
        
      
        
        System.out.println("Inter size: "+inter.size());
        
        CompDist = new HashMap<>();
        
        it = inter.iterator();
        
        while(it.hasNext()){
            String el = it.next();
            String cl = labels.get(el);
             if(!CompDist.containsKey(cl))
                        CompDist.put(cl, 1);
                    else CompDist.put(cl, CompDist.get(cl)+1);  
        }
        
        System.out.println("Dist of intersection: ");
        
         it = CompDist.keySet().iterator();
        
        while(it.hasNext()){
            String l = it.next();
            System.out.println(l+" "+CompDist.get(l));
        }
        
       System.out.println("Dist of neuron: ");
        
         it = CompDist.keySet().iterator();
        
        while(it.hasNext()){
            String l = it.next();
            System.out.println(l+" "+CompDist.get(l));
        }
        
     
    }
    
}
