package Function;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class test {

    public static List<String> Seg(String sentence) throws IOException {
        List<String> EList = new ArrayList<>();
        Analyzer analyzer=new IKAnalyzer(true);
        StringReader reader=new StringReader(sentence);
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

    public static void main(String[] arg0) throws Exception{
        String str ="There are some people who think love is sex and marriage and six o’clock-kisses and children, and perhaps it is, Miss Lester.";
        System.out.println(Seg(str).toString());
    }

}

