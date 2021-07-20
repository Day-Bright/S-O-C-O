package Function;

import com.huaban.analysis.jieba.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


public class WordBreak {

    //中文分词
    public List<String> CWordBreak(String CNstr){
        JiebaSegmenter segmenter = new JiebaSegmenter();
        return segmenter.sentenceProcess(CNstr);
    }

    //英文分词
    public List<String> EWordBreak(String ENstr) throws IOException {
        List<String> EList = new ArrayList<>();
        Analyzer analyzer=new IKAnalyzer(true);
        StringReader reader=new StringReader(ENstr);
        TokenStream tokenStream=analyzer.tokenStream("", reader);
        CharTermAttribute term=tokenStream.getAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while(tokenStream.incrementToken()){
            EList.add(term.toString());
        }
        reader.close();
        analyzer.close();
        return EList;
    }

    //中英文分词
    public List<String> CEWordBreak(String CEstr){
        StringBuilder CN_str = new StringBuilder();
        StringBuilder EN_str = new StringBuilder();
        char[] char_str =CEstr.toCharArray();
        for(char i_char:char_str){
            if(Character.toString(i_char).matches("[\\u4E00-\\u9FA5]+")){
                CN_str.append(i_char);
            }else{
                EN_str.append(i_char);
            }
        }
        List<String> CList = CWordBreak(CN_str.toString());
        List<String> EList = CWordBreak(EN_str.toString());
        CList.addAll(EList);
        return CList;
//        Pattern pattern = Pattern.compile("[^\u4E00-\u9FA5]");
//        Matcher matcher = pattern.matcher(CEstr);
//        String CN_str = matcher.replaceAll("");
//        List<String> CList = CWordBreak(CN_str);
    }


    public String StandardStr(String QueryStr) throws IOException {
//        QueryStr = QueryStr.replaceAll("[1234567890]"," ");

        Analyzer analyzer=new StandardAnalyzer(
                new FileReader(new File("src\\use\\stopwords.txt"))
        );
        StringReader reader=new StringReader(QueryStr);
        TokenStream tokenStream=analyzer.tokenStream("", reader);
        CharTermAttribute term=tokenStream.getAttribute(CharTermAttribute.class);
        tokenStream.reset();
        String QueryString="";
        while(tokenStream.incrementToken()){
            QueryString=term.toString()+" "+QueryString;
        }
        reader.close();
        analyzer.close();
        return QueryString;
    }


    public List<String> StandardList(String QueryStr) throws IOException {
//        QueryStr = QueryStr.replaceAll("[1234567890]"," ");
        List<String> QueryList = new ArrayList<>();
        Analyzer analyzer=new StandardAnalyzer(
                new FileReader(new File("src\\use\\stopwords.txt"))
        );
        StringReader reader=new StringReader(QueryStr);
        TokenStream tokenStream=analyzer.tokenStream("", reader);
        CharTermAttribute term=tokenStream.getAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while(tokenStream.incrementToken()){
            QueryList.add(term.toString());
        }
        reader.close();
        analyzer.close();
        return QueryList;
    }

    public static void main(String[] args) throws IOException {
        String test_str = "IK Analyzer也支持自定义词典，在IKAnalyzer.cfg.xml同一目录新建ext.dic，把新的词语按行写入文件，编辑IKAnalyzer.cfg.xml把新增的停用词字典写入配置文件，多个字典用空格隔开";
        WordBreak wordBreak = new WordBreak();
//        System.out.println(wordBreak.CWordBreak("也支持自定义词典在同一目录新建把新的词语按行写入文件编辑把新增的停用词字典写入配置文件多个字典用空格隔开"));
//        System.out.println(wordBreak.EWordBreak("If the day is done, if birds sing no more, if the wind has flagged tired, then draw the veil of darkness thick upon me, even as thou hast wrapt the earth with the coverlet of sleep and tenderly closed the petals of the drooping lotus at dusk.From the traveller, whose sack of provisions is empty before the voyage is ended, whose garment is torn and dustladen, whose strength is exhausted, remove shame and poverty, and renew his life like a flower under the cover of thy kindly night."));
        System.out.println(wordBreak.CEWordBreak(test_str).toString());
    }

}
