package Search;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class search_NGram {

    public static void main(String[] args)throws Exception {
        try {
            File writeFile = new File("C:\\Users\\管理员\\Documents\\Tencent Files\\1520207872\\FileRecv\\新建文件夹\\AI-SOCO-master\\data_dir\\0000015.csv");
            BufferedWriter writeText = new BufferedWriter(new FileWriter(writeFile));
            writeText.write("uid,pid");
            writeText.newLine();

            File file = new File("C:\\Users\\管理员\\Documents\\Tencent Files\\1520207872\\FileRecv\\新建文件夹\\AI-SOCO-master\\data_dir\\dev");
            File[] tempList = file.listFiles();
            ArrayList<Integer> si = new ArrayList<>();
            for(File f:tempList){
                si.add(Integer.valueOf(f.getName()));
            }
            Collections.sort(si);
            for (Integer i : si){
                String File_path = "C:\\Users\\管理员\\Documents\\Tencent Files\\1520207872\\FileRecv\\新建文件夹\\AI-SOCO-master\\data_dir\\dev\\"+String.valueOf(i);
//                System.out.println(File_path);
                File f = new File(File_path);
                BufferedReader br = new BufferedReader(new FileReader(f));
                StringBuilder stringBuffer = new StringBuilder();
                String s;
                while ((s=br.readLine())!=null){
                    stringBuffer.append(s);
                }
                String new_String = stringBuffer.toString();
                new_String = new_String.replaceAll("[^a-zA-Z]", " ");  //去除数字，英文，汉字  之外的内容

                new_String = new_String.replaceAll("OR"," ").replaceAll("or"," ")
                        .replaceAll("NOT"," ").replaceAll("not"," ")
                        .replaceAll("AND"," ").replaceAll("and"," ");
                new_String=new_String.replaceAll(" +"," ");

                Search search = new Search();
                IndexSearcher indexSearcher = search.getIndexSearch();
                ScoreDoc[] scoreDocs = search.Search_QueryParserTest(new_String,1);
                for(ScoreDoc scoreDoc : scoreDocs){
                    int doc = scoreDoc.doc;
                    Document document = indexSearcher.doc(doc);
                    String uid = document.get("uid");
                    writeText.write(uid+','+i);
                    System.out.println(uid+','+i);
                    writeText.newLine();
                    writeText.flush();
                }
            }

            writeText.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
