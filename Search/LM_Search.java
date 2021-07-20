package Search;

import Function.WordBreak;
import Function.modle_1;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class LM_Search {

    public static void main(String[] args) {

            try {
                File writeFile = new File("C:\\Users\\管理员\\Documents\\Tencent Files\\1520207872\\FileRecv\\AI-SOCO-master\\data_dir\\15.csv");
                BufferedWriter writeText = new BufferedWriter(new FileWriter(writeFile));
                writeText.write("uid,pid");
                writeText.newLine();
                File file = new File("C:\\Users\\管理员\\Documents\\Tencent Files\\1520207872\\FileRecv\\AI-SOCO-master\\data_dir\\dev");
                File[] tempList = file.listFiles();
                ArrayList<Integer> si = new ArrayList<>();
                for(File f:tempList){
                    si.add(Integer.valueOf(f.getName()));
                }
                Collections.sort(si);
                for (Integer i : si){
                    String File_path = "C:\\Users\\管理员\\Documents\\Tencent Files\\1520207872\\FileRecv\\AI-SOCO-master\\data_dir\\dev\\"+String.valueOf(i);
//                System.out.println(File_path);
                    File f = new File(File_path);
                    BufferedReader br = new BufferedReader(new FileReader(f));
                    StringBuilder stringBuffer = new StringBuilder();
                    String s;
                    while ((s=br.readLine())!=null){
                        stringBuffer.append(s);
                    }
                    String new_String = stringBuffer.toString();
                    WordBreak wordBreak = new WordBreak();
                    String s1 = wordBreak.StandardStr(new_String);
                    s1 = s1.replaceAll(" +"," ");
                    modle_1 m =new modle_1();
                    Map<Integer, Double> r = modle_1.sortDescend(m.core(s1));
                    int obj = 0;
                    for (Map.Entry<Integer, Double> entry : r.entrySet()) {
                        obj = entry.getKey();
                        if (obj != 1) {
                            break;
                        }
                    }
                    String uid = String.valueOf(obj);
                    System.out.println(uid+','+i);

                    writeText.write(uid+','+i);
                    writeText.newLine();
                    writeText.flush();
//                Ngram ngram = new Ngram();
//                String s2 = ngram.NgramStr(s1,2);
//                System.out.println(s1);
//                System.out.println(s2);
//                System.out.println(s1);
//                    Search search = new Search();
//                    IndexSearcher indexSearcher = search.getIndexSearch();
//                    ScoreDoc[] scoreDocs = search.test(s1,1);
//                    for(ScoreDoc scoreDoc : scoreDocs){
//                        int doc = scoreDoc.doc;
//                        Document document = indexSearcher.doc(doc);
//                        StringBuilder part = new StringBuilder();
////                    String uid = document.getField("uid").name();
//                        String uid = document.get("uid");
//                        System.out.println(uid+','+i);
////                    System.out.println(i+","+uid+","+scoreDoc.score);
////                    writeText.write(i+","+uid+","+scoreDoc.score);
//                        writeText.write(uid+','+i);
//                        writeText.newLine();
//                        writeText.flush();
////                    for ( IndexableField indexableField : document.getFields() )
////                    {
////
////                        String key = indexableField.name();
////                        String uid = document.get("uid");
////                        System.out.println(uid);
////
////                    }
//                    }
                }

                writeText.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

