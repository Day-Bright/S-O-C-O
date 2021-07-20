package Function;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Ngram {

    public LinkedList<String> NgramList(String QueryStr, int N) throws IOException {
        WordBreak wordBreak = new WordBreak();
        List<String> QueryList = wordBreak.StandardList(QueryStr);
        LinkedList<String> new_QueryList = new LinkedList<>();
        for(int i=0;i<QueryList.size();i++){
            String words = "";
            if(QueryList.size()-i<N){
                break;
            }else{
                for(int j=0;j<N;j++){
                    words+=QueryList.get(i+j);
                }
                new_QueryList.add(words);
            }
        }
        return new_QueryList;
    }

    public static String NgramStr(String QueryStr, int N) throws IOException {
        WordBreak wordBreak = new WordBreak();
        List<String> QueryList = wordBreak.StandardList(QueryStr);
        String NgramString = "";
        for(int i=0;i<QueryList.size();i++){
            String words = "";
            if(QueryList.size()-i<N){
                break;
            }else{
                for(int j=0;j<N;j++){
                    words=words+QueryList.get(i+j)+"_";
                }
                words = words.substring(0,words.length()- 1);
                NgramString=NgramString+words+" ";
            }
        }
        return NgramString.substring(0,NgramString.length()-1);
    }

    public static String Ngram_all(String QueryStr, int N) throws Exception{
        String all = "";
        for(int i=N;i>0;i--){
            String s = NgramStr(QueryStr, i);
            all=all+s+" ";
        }
        return all;
    }

    public static void main(String[] args) throws Exception {
        String s = Ngram_all("qewwe rtrt yuyu iii oo pp ii yyy tt", 3);
        System.out.println(s);

    }
}
