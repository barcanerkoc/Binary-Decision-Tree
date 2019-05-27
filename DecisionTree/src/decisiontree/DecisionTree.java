package decisiontree;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DecisionTree{
    
    private static Node root = new Node("Root", "Root");
    
    public static void main(String[] args){
        
        init("/Data/DTData3.txt");
        
    }
    
    public static void init(String path){
        
        List<String> data = readData(path);
        root.setTrueNode(rootSelection(data));
        System.out.println("");
        
    }
    
    public static Node rootSelection(List<String> data){
        
        int dataSize = Integer.valueOf(data.get(0).split("\t")[0]);
        int classSize = Integer.valueOf(data.get(0).split("\t")[1]);
        
        int[] trues = new int[classSize + 1];
        int[] trueTrueCounts = new int[classSize]; // count of a class where the class is T and output is T
        int[] falseTrueCounts = new int[classSize]; // count of a class where the class is F and output is T
        for(int x = 2; x < data.size(); x++){
            
            String[] lineContent = data.get(x).split("\t");
            
            if(lineContent[lineContent.length - 1].equals("T")){
                
                trues[lineContent.length - 1]++;
                
                for(int y = 0; y < lineContent.length - 1; y++){
                    
                    if(lineContent[y].equals("T")){
                        trues[y]++;
                        trueTrueCounts[y]++;
                    }else if(lineContent[y].equals("F")){
                        falseTrueCounts[y]++;
                    }
                    
                }
                
            }else{
                
                for(int y = 0; y < lineContent.length - 1; y++){
                    
                    if(lineContent[y].equals("T")){
                        trues[y]++;
                    }
                    
                }
                
            }
            
        }
        
        double mainEntropy = calculateEntropy(dataSize, trues[trues.length - 1]);
        // 3/5 * (2/3, 1/3)
        
        double highestIG = 0;
        int index = 0;
        for(int x = 0; x < classSize; x++){
            
            double IG = informationGain(mainEntropy, dataSize, trues[x], trueTrueCounts[x], falseTrueCounts[x]);
            if(IG > highestIG){
                highestIG = IG;
                index = x;
            }
            
            System.out.println(informationGain(mainEntropy, dataSize, trues[x], trueTrueCounts[x], falseTrueCounts[x]));
            
        }
        
        //node eklendi (node (isim, değer, output veya sonraki node))
        Node node = new Node(data.get(1).split("\t")[index]);
        
        if(trues[index] > 0){
            
            if(trueTrueCounts[index] == 0){
                // node true'dan false verir
                node.setTrueNode(new Node("Leaf", "F"));
            }else if(trues[index] == trueTrueCounts[index]){
                // node true'dan true verir
                node.setTrueNode(new Node("Leaf", "T"));
            }else{
                // node node a gider
                List<String> newData = changeData(data, index, false);
                node.setTrueNode(rootSelection(newData));
            }
            
        }
        
        if(dataSize - trues[index] > 0){
            
            if(falseTrueCounts[index] == 0){
                // node false'dan false verir
                node.setFalseNode(new Node("Leaf", "F"));
            }else if((dataSize - trues[index]) == falseTrueCounts[index]){
                // node false'dan true verir
                node.setFalseNode(new Node("Leaf", "T"));
            }else{
                // node node a gider
                List<String> newData = changeData(data, index, true);
                node.setFalseNode(rootSelection(newData));
            }
            
        }
        
        System.out.println("");
        
        return node;
        
    }
    
    public static double informationGain(double mainEntropy, double dataSize, int trueCount, int trueTrueCount, int falseTrueCount){
        
        return mainEntropy - (((trueCount / dataSize) * calculateEntropy(trueCount, trueTrueCount)) + (((dataSize - trueCount) / dataSize) * calculateEntropy((dataSize - trueCount), falseTrueCount)));
        
    }
    
    public static double calculateEntropy(double dataSize, int trueCount){
        
        return -(trueCount / dataSize) * log2(trueCount / dataSize) -((dataSize - trueCount) / dataSize) * log2((dataSize - trueCount) / dataSize);
        
    }
    
    public static double log2(double num){
        
        if(num == 0){
            return 0;
        }
        
        return (Math.log10(num) / Math.log10(2));
        
    }
    
    public static List<String> changeData(List<String> data, int index, boolean binary){
        
        List<String> newData = new ArrayList<>();
        int subDataSize = 0;
        
        String temp = "";
        for(int x = 0; x < data.get(1).split("\t").length; x++){
            
            if(x == index){
                continue;
            }
            
            if(x < data.get(1).split("\t").length - 1){
                temp += data.get(1).split("\t")[x] + "\t";
            }else{
                temp += data.get(1).split("\t")[x];
            }
            
        }
        
        newData.add(temp);
        
        if(binary){
            
            for(int x = 2; x < data.size(); x++){
                
                String[] lineContent = data.get(x).split("\t");
                
                if(lineContent[index].equals("T")){
                    subDataSize++;
                    continue;
                }
                
                temp = "";
                for(int y = 0; y < lineContent.length; y++){
                    
                    if(y == index){
                        continue;
                    }
                    
                    if(y < lineContent.length - 1){
                        temp += lineContent[y] + "\t";
                    }else{
                        temp += lineContent[y];
                    }
                    
                }
                
                newData.add(temp);
                
            }
            
        }else{
            
           for(int x = 2; x < data.size(); x++){
                
                String[] lineContent = data.get(x).split("\t");
                
                if(lineContent[index].equals("F")){
                    subDataSize++;
                    continue;
                }
                
                temp = "";
                for(int y = 0; y < lineContent.length; y++){
                    
                    if(y == index){
                        continue;
                    }
                    
                    if(y < lineContent.length - 1){
                        temp += lineContent[y] + "\t";
                    }else{
                        temp += lineContent[y];
                    }
                    
                }
                
                newData.add(temp);
                
            } 
            
        }
        
        int dataSize = Integer.valueOf(data.get(0).split("\t")[0]);
        int classSize = Integer.valueOf(data.get(0).split("\t")[1]);
        
        newData.add(0, (dataSize - subDataSize) + "\t" + (classSize - 1));
        
        return newData;
        
    }
    
    public static List<String> readData(String path){
        
        List<String> data = new ArrayList<>();
        try {
            
            Stream<String> stream = new BufferedReader(new InputStreamReader(DecisionTree.class.getResourceAsStream(path))).lines();
            data = stream.collect(Collectors.toList());
            //Stream<String> stream = Files.lines(Paths.get(path));
            //stream.forEach(System.out::println);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return data;
        
    }
    
}
