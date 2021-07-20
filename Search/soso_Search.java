package Search;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class soso_Search {


    public static void main(String[] args)throws Exception {
        try {
            File writeFile = new File("C:\\Users\\管理员\\Documents\\Tencent Files\\1520207872\\FileRecv\\AI-SOCO-master\\data_dir\\03.csv");
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
//                    System.out.println(new_String);
                new_String = new_String.replaceAll("[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？1234567890]+"," ");
                new_String = new_String.replaceAll("OR"," ").replaceAll("or"," ")
                        .replaceAll("NOT"," ").replaceAll("not"," ")
                        .replaceAll("AND"," ").replaceAll("and"," ");
//                    System.out.println(new_String);
//                    System.out.println("======================================");
                Search search = new Search();
                ArrayList<StringBuilder> qq = search.Search_MultiFieldQueryParser(new_String,1);

                String re=null;
                if(qq.size()==0){
//                    System.out.println('0'+','+i);
                    writeText.write('0'+','+i);
                    writeText.newLine();
                    writeText.flush();
                }else{
                    StringBuilder stringBuilder = qq.get(0);
                    System.out.println(stringBuilder.toString());
                    re = stringBuilder.toString().split(":")[0];
//                      System.out.println("=======");
//                    System.out.println(re+','+i);
                    writeText.write(re+','+i);
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
