package Index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class soco_index_better {

    public static ArrayList<ArrayList<String>> csv() throws Exception{

        File csv = new File("C:\\Users\\管理员\\Documents\\Tencent Files\\1520207872\\FileRecv\\AI-SOCO-master\\data_dir\\train.csv"); // CSV文件路径
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(csv));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = "";
        String everyLine = "";
        ArrayList<ArrayList<String>> PidLists = new ArrayList<>();
        int k=1;
        try {
            while ((line = br.readLine()) != null) // 读取到的内容给line变量
            {

                everyLine = line;
                String[] split = everyLine.split(",");
                if(split[0].equals("uid")){
                    continue;
                }
                ArrayList<String> PidList = new ArrayList<>();
                PidList.add(split[1]);
                PidList.add(split[0]);
                PidLists.add(PidList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return PidLists;
    }

    public static HashMap<String,ArrayList<String>> Mark() throws Exception {
        ArrayList<ArrayList<String>> csv = csv();
        HashMap<String,ArrayList<String>> map = new HashMap<>();
        for(int i=0;i<csv.size();i=i+50){
            String uid = csv.get(i).get(1);
            ArrayList<String> L = new ArrayList<>();
            for(int j=0;j<50;j++){
                L.add(csv.get(i+j).get(0));
            }
            map.put(uid,L);
        }
        return map;
    }

    public static void BuildIndex(HashMap<String,ArrayList<String>> mark) throws Exception {

        String creat_path = "C:\\Users\\管理员\\Desktop\\so_better";
        Directory directory = FSDirectory.open(Paths.get(creat_path));
        Analyzer analyzer = new StandardAnalyzer(
                new FileReader(new File("D:\\Index\\src\\use\\stopwords.txt.txt"))
        );
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

//        config.setMaxBufferedDocs(500000);
//        config.setRAMBufferSizeMB(20000);
//        config.setRAMBufferSizeMB(64);
//        config.setUseCompoundFile(false);
//        config.setMaxBufferedDocs(1000);
//        config.setMergePolicy(new LogDocMergePolicy());

        IndexWriter indexWriter = new IndexWriter(directory, config);

        FieldType type1 = new FieldType();
        // 设置是否存储该字段
        type1.setStored(true); // 请试试不存储的结果
        // 设置是否对该字段分词
        type1.setTokenized(true); // 请试试不分词的结果
        // 设置该字段的索引选项
        type1.setIndexOptions(IndexOptions.DOCS); // 反向索引中只保存词项
        // 设置为该字段保存词项向量
        type1.setStoreTermVectors(true);
        type1.setStoreTermVectorPositions(true);
        type1.setStoreTermVectorOffsets(true);
        type1.setStoreTermVectorPayloads(true);
        type1.freeze(); // 使不可更改

        for(Map.Entry<String, ArrayList<String>> entry : mark.entrySet()){
            String mapKey = entry.getKey();
            ArrayList<String> mapValue = entry.getValue();
//            System.out.println(mapKey+":"+mapValue);
            Document document = new Document();
            for(String pid :mapValue){
                String Path = "C:\\Users\\管理员\\Documents\\Tencent Files\\1520207872\\FileRecv\\AI-SOCO-master\\data_dir\\train\\"+pid;
                File f = new File(Path);
                System.out.println(Path);
                BufferedReader br = new BufferedReader(new FileReader(f));
                StringBuilder stringBuffer = new StringBuilder();
                String s;
                while ((s=br.readLine())!=null){
                    stringBuffer.append(s);
                }
                String new_String = stringBuffer.toString();
                document.add(new Field("content",new_String,type1));
                document.add(new Field("uid",mapKey,type1));
                br.close();
            }
            indexWriter.addDocument(document);
        }
        indexWriter.close();
    }

    public static void main(String[] args) throws Exception {
        HashMap<String, ArrayList<String>> mark = Mark();
        BuildIndex(mark);
    }

}
