package Search;

import NGram.NGramAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;


public class Search {

    public IndexSearcher getIndexSearch() throws Exception{
        String creat_path = "C:\\Users\\管理员\\Desktop\\15index";
        Directory directory = FSDirectory.open(Paths.get(creat_path));
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
//        indexSearcher.setSimilarity();
        return indexSearcher;
    }

    public ArrayList<StringBuilder> getResult(ScoreDoc[] scoreDocs,IndexSearcher indexSearcher) throws Exception{

        ArrayList<StringBuilder> result = new ArrayList<>();
        for(ScoreDoc scoreDoc : scoreDocs){

            int doc = scoreDoc.doc;
            Document document = indexSearcher.doc(doc);
//            System.out.println(scoreDoc.score);//检索得分
//            IndexableField uid = document.getField("uid");
            StringBuilder part = new StringBuilder();
//            List<IndexableField> fields = document.getFields();
            for ( IndexableField indexableField : document.getFields() )
            {

                String key = indexableField.name();
                String str = key + ":" + document.get(key);
                part.append(str);
                part.append("\n");
            }
            result.add(part);
        }
        return result;
    }


    public ArrayList<StringBuilder> Search_TermQuery(String fld,String text,int n) throws Exception {
        IndexSearcher indexSearcher = getIndexSearch();
        Query query = new TermQuery(new Term(fld,text));
        TopDocs topDocs = indexSearcher.search(query,n);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        ArrayList<StringBuilder> result = getResult(scoreDocs,indexSearcher);
        indexSearcher.getIndexReader().close();
        return result;
    }


//    public ScoreDoc[] NGram(String fld,String text,int n) throws Exception {
//        IndexSearcher indexSearcher = getIndexSearch();
//        PhraseQuery.Builder builder = new PhraseQuery.Builder();
//        builder.add(new Term(fld,text));
//        PhraseQuery pq = builder.build();
//        Query query = new NGramPhraseQuery(2, pq);
//        TopDocs topDocs = indexSearcher.search(query,n);
//        return topDocs.scoreDocs;
//    }

    public ScoreDoc[] Search_BooleanQuery(String text,int n) throws Exception{
        IndexSearcher indexSearcher = getIndexSearch();
        Query query1 = new TermQuery(new Term("content", text));
//		Query query2 = new TermQuery(new Term("", ""));
		BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
		booleanQueryBuilder.add(query1, BooleanClause.Occur.MUST);
//		booleanQueryBuilder.add(query2, BooleanClause.Occur.MUST);
		BooleanQuery query = booleanQueryBuilder.build();
        BooleanQuery.setMaxClauseCount(Integer.MAX_VALUE);
        TopDocs topDocs = indexSearcher.search(query,n);
        return  topDocs.scoreDocs;
    }

    public ArrayList<StringBuilder> Search_QueryParser(int n) throws Exception{
        IndexSearcher indexSearcher = getIndexSearch();
        QueryParser queryParser = new QueryParser("当事人",new IKAnalyzer());//默认检索案件类型
        Query query = queryParser.parse("当事人:孔凡军*");
        TopDocs topDocs = indexSearcher.search(query,n);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        ArrayList<StringBuilder> result = getResult(scoreDocs,indexSearcher);
        indexSearcher.getIndexReader().close();
        return result;
    }


    public ScoreDoc[] Search_QueryParserTest(String text,int n) throws Exception{
        IndexSearcher indexSearcher = getIndexSearch();
        BooleanQuery.setMaxClauseCount(Integer.MAX_VALUE);
        QueryParser queryParser = new QueryParser("content",new NGramAnalyzer());//默认检索案件类型
        Query query = queryParser.parse(text);
        TopDocs topDocs = indexSearcher.search(query,n);
        return topDocs.scoreDocs;
    }


    public ArrayList<StringBuilder> Search_MultiFieldQueryParser(String text,int n) throws Exception{
        IndexSearcher indexSearcher = getIndexSearch();
//        String [] fields = {"当事人",
//                            "审理程序",
//                            "案件类型",
//                            "legal_basis",
//                            "major_text",
//                            "案由",
//                            "审理法院",
//                            "裁判日期",
//                            "docid"};
        String[] fields = {"content"};
        MultiFieldQueryParser multiFieldQueryParser = new MultiFieldQueryParser(fields,new StandardAnalyzer(
                new FileReader(new File("src\\use\\stopwords.txt"))
        ));
        Query query = multiFieldQueryParser.parse(QueryParser.escape(text));
        BooleanQuery.setMaxClauseCount(Integer.MAX_VALUE);
        TopDocs topDocs = indexSearcher.search(query,n);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        ArrayList<StringBuilder> result = getResult(scoreDocs,indexSearcher);
        indexSearcher.getIndexReader().close();
        return result;
    }

    public ScoreDoc[] test(String text,int n) throws Exception {
        IndexSearcher indexSearcher = getIndexSearch();
//        String [] fields = {"当事人",
//                "审理程序",
//                "案件类型",
//                "legal_basis",
//                "major_text",
//                "案由",
//                "审理法院",
//                "裁判日期",
//                "docid"};
        String[] fields = {"content"};
        MultiFieldQueryParser multiFieldQueryParser = new MultiFieldQueryParser(fields,new NGramAnalyzer());
        Query query1 = multiFieldQueryParser.parse(text);
        BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
        BooleanQuery.setMaxClauseCount(Integer.MAX_VALUE);
        booleanQueryBuilder.add(query1, BooleanClause.Occur.MUST);
        BooleanQuery query = booleanQueryBuilder.build();
        TopDocs topDocs = indexSearcher.search(query,n);
        return topDocs.scoreDocs;
    }

    /**
     *通配符查询
     * '?' 代表一个字符， '*' 代表0个或多个字符
     * 查询当事人，案由等
     */
    public ArrayList<StringBuilder> Search_WildcardQuery(String fld,String text,int n) throws Exception{
        String new_text = '*' + text +'*';
        IndexSearcher indexSearcher = getIndexSearch();
        Query query = new WildcardQuery(new Term(fld,new_text));
        TopDocs topDocs = indexSearcher.search(query,n);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        ArrayList<StringBuilder> result = getResult(scoreDocs,indexSearcher);
        indexSearcher.getIndexReader().close();
        return result;
    }

    /**
     * 模糊检索不支持中文
     */
    public ArrayList<StringBuilder> Search_FuzzyQuery(String fld,String text,int n) throws Exception{
        IndexSearcher indexSearcher = getIndexSearch();
        FuzzyQuery query = new FuzzyQuery(new Term(fld,text));
        TopDocs topDocs = indexSearcher.search(query,n);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        ArrayList<StringBuilder> result = getResult(scoreDocs,indexSearcher);
        indexSearcher.getIndexReader().close();
        return result;
    }

    public static void main(String[] args) throws Exception {
        Search search = new Search();
        ArrayList<StringBuilder> qq = search.Search_MultiFieldQueryParser("++",1);
//        for(StringBuilder i :qq){
//            System.out.println(i.toString());
//            System.out.println("==================================================");
//        }
//        System.out.println(search.Search_WildcardQuery("当事人","孔",10).toString());
//        System.out.println(search.Search_MultiFieldQueryParser("民事案件",100).toString());
    }

}
