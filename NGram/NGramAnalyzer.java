package NGram;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ngram.NGramTokenizer;


public class NGramAnalyzer extends Analyzer {


    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        return new TokenStreamComponents(new NGramTokenizer(15,15));
    }

}