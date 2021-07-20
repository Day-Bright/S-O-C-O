package Function;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.apache.lucene.analysis.ngram.NGramTokenizer;
import org.apache.lucene.analysis.tokenattributes.*;

import java.io.IOException;

public class TestNGram {

    public static void tokenizer1() throws Exception {
        try (Analyzer an = new Analyzer() {
            protected TokenStreamComponents createComponents(String fieldName) {
                NGramTokenizer tokenizer = new NGramTokenizer(1, 2);
                return new TokenStreamComponents(tokenizer, tokenizer);
            }
        })
        {
            TokenStream stream = an.tokenStream(null,  "i love qw eat aer");
            stream.reset();
            CharTermAttribute t = stream.getAttribute(CharTermAttribute.class);
            OffsetAttribute o = stream.getAttribute(OffsetAttribute.class);
            System.out.println("\nNGramTokenizer");
            while (stream.incrementToken()) {
//                System.out.println(t + ",offset " + o.startOffset() + ":" + o.endOffset());
                System.out.println(t.toString());
            }

        }
    }

    public static String tokenizer2(String qs) throws Exception {
        try (Analyzer an = new Analyzer() {
            protected TokenStreamComponents createComponents(String fieldName) {
                Tokenizer tokenizer = new WhitespaceTokenizer();
                NGramTokenFilter filter = new NGramTokenFilter(tokenizer, 2, 5, false);
                return new TokenStreamComponents(tokenizer, filter);
            }
        }) {
            TokenStream stream = an.tokenStream(null,  qs);
            stream.reset();
            CharTermAttribute t = stream.getAttribute(CharTermAttribute.class);
            OffsetAttribute o = stream.getAttribute(OffsetAttribute.class);
//            System.out.println("\nWhitespaceTokenizer");
            String s = "";
            while (stream.incrementToken()) {
//                System.out.println(t + ",offset " + o.startOffset() + ":" + o.endOffset());
//                System.out.println(t.toString());
                s=s+t.toString()+" ";
            }
//            System.out.println(s);
            return s;
        }

    }

    public static void tokenizer3() throws Exception {
        try (Analyzer an = new Analyzer() {
            protected TokenStreamComponents createComponents(String fieldName) {
                Tokenizer tokenizer = new WhitespaceTokenizer();
                NGramTokenFilter filter = new NGramTokenFilter(tokenizer, 1, 2, false);
                return new TokenStreamComponents(tokenizer, filter);
            }
        }) {
            TokenStream stream = an.tokenStream(null,  "i love qw eat aer");
            stream.reset();
            CharTermAttribute t = stream.getAttribute(CharTermAttribute.class);
            OffsetAttribute o = stream.getAttribute(OffsetAttribute.class);
//            System.out.println("\nWhitespaceTokenizer");
            while (stream.incrementToken()) {
//                System.out.println(t + ",offset " + o.startOffset() + ":" + o.endOffset());
                System.out.println(o.toString()+"   "+t.toString());
            }
        }
    }


    public static void main(String[] args) throws Exception {
        System.out.println(tokenizer2("qwqwqwqwqwq"));
//        String s = "捡 白色 iphone6 手机 壳 透明 失主 方式 15659119418  ";
//        String[] str = s.split(" ");
//        StringBuilder sb = new StringBuilder();
//        for(int i = 0; i < str.length; i++){
//            sb.append(str[i]);
//        }
//        System.out.println(sb.toString());
//        StringReader sr = new StringReader(sb.toString());
//        //N-gram模型分词器
//        Tokenizer tokenizer = new NGramTokenizer(1,2);
//        testtokenizer(tokenizer);
    }

    private static void testtokenizer(Tokenizer tokenizer) {

        try {

            tokenizer.reset();
            while(tokenizer.incrementToken()) {
                    CharTermAttribute charTermAttribute=tokenizer.addAttribute(CharTermAttribute.class);

            System.out.print(charTermAttribute.toString()+"|");
			}
            tokenizer.end();
            tokenizer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






}
