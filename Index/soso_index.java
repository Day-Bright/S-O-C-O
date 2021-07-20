package Index;

import com.alibaba.fastjson.JSONObject;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class soso_index {



    public static HashMap<String,String>  csv() throws Exception{

        File csv = new File("C:\\Users\\管理员\\Documents\\Tencent Files\\1520207872\\FileRecv\\AI-SOCO-master\\data_dir\\train.csv"); // CSV文件路径
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(csv));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = "";
        String everyLine = "";
//        HashSet<String> map = new HashSet<>();
        HashMap<String,String> map=new HashMap<>();
        try {
            while ((line = br.readLine()) != null) // 读取到的内容给line变量
            {
                everyLine = line;
                String[] split = everyLine.split(",");
//                String x = null;
//                x='"'+split[0]+'"';
//                map.add(x);
                map.put(split[1],split[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }


    public static void BuildIndex(HashMap<String, String> csv) throws Exception {

        String creat_path = "C:\\Users\\管理员\\Desktop\\sox";
        Directory directory = FSDirectory.open(Paths.get(creat_path));
        Analyzer analyzer = new StandardAnalyzer();
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


        try {
            File file = new File("C:\\Users\\管理员\\Documents\\Tencent Files\\1520207872\\FileRecv\\AI-SOCO-master\\data_dir\\train");
            File[] tempList = file.listFiles();
            if (tempList == null) {
                System.out.println("路径错误or文件夹无文件");
            } else {
                for (File file_list : tempList) {
                    File f = new File(file_list.getPath());
                    BufferedReader br = new BufferedReader(new FileReader(f));
                    StringBuilder stringBuffer = new StringBuilder();
                    String s;
                    while ((s=br.readLine())!=null){
                        stringBuffer.append(s);
                    }
                    String new_String = stringBuffer.toString();
                    Document document = new Document();
                    String Fname = file_list.getName();
                    String writter = csv.get(Fname);
                    System.out.println(writter);
                    document.add(new Field(writter,new_String,type1));
                    indexWriter.addDocument(document);
                    br.close();
                }
                indexWriter.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) throws Exception {
        HashMap<String,String>  csv = csv();
//        System.out.println(csv);
        BuildIndex(csv);
    }
}