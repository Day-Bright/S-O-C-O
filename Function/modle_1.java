package Function;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import Search.Search;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;



public class modle_1 {

    private final static String[] fieldname = new String[]{"content"};
    private final static String path = "C:\\Users\\管理员\\Desktop\\good";

    //获取C的分词数量   分母
    public static long getC() throws IOException {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(path)));
        long sumTF = 0;
        for (String fn : fieldname) {
            long TermFreq = reader.getSumTotalTermFreq(fn);
            sumTF += TermFreq;
        }
//        System.out.println(sumTF);
        return sumTF;
    }


    //Q分词
    public List<String> R_List(String Q) throws IOException {
        WordBreak wordBreak = new WordBreak();
        return wordBreak.StandardList(Q);
    }


    //获取term在C中的数量   分子
    public Double XinC(String Q) throws IOException {
//        Map<String,Double> QinC = new HashMap<>();
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(path)));
        long all_tf = 0;
        for (String fn : fieldname) {
            long tf = reader.totalTermFreq(new Term(fn, Q));
            all_tf += tf;
        }
        return (double) all_tf;
//        QinC.put(Q,All_TF);
    }


    public Map<String, Double> QinC(String Q) throws IOException {
        Map<String, Double> QinC = new HashMap<>();
        long c = getC();
        double C = (double) c;
        List<String> r_list = R_List(Q);
        for (String x : r_list) {
            Double xinc = XinC(x) / C;
            QinC.put(x, xinc);
        }
        return QinC;
    }


//    //获取Q中每个X在D中的比例
//    public Map<Integer, Double> XinD(String Q) throws Exception {
//        Map<Integer, Double> XinQ = new HashMap<>();
//        Search search = new Search();
//        IndexSearcher indexSearch = search.getIndexSearch();
//        ScoreDoc[] test = search.test(Q, 1000);//？
//        for(ScoreDoc scoreDoc:test){
//            int i = scoreDoc.doc;
//            Fields fields = indexSearch.getIndexReader().getTermVectors(i);
//            double  D_count = 0.0;
//            double X=0.0;
//            double XcD=0.0;
////            Iterator<String> fieldsIterator = fields.iterator();
//            for(String f: fields){
////                String field = fieldsIterator.next();
//                Terms terms = fields.terms(f);
//                double F_count = terms.size();
//                D_count+=F_count;
//                TermsEnum termsEnums = terms.iterator();
//                BytesRef byteRef = null;
//                while((byteRef = termsEnums.next()) != null) {
//                    String term = new String(byteRef.bytes, byteRef.offset, byteRef.length);
//                    if(Q.equals(term)){
//                        X+=1.0;
//                    }
//                }
//            }
//            XcD=X/D_count;
//            XinQ.put(i,XcD);
//        }
//        return XinQ;
//    }
//
//
//    //遍历Q获取每个X在文档中的比例
//    public Map<String,Map<Integer, Double>> QinD(String Q) throws Exception {
//        List<String> r_list = R_List(Q);
//        Map<String,Map<Integer, Double>> kid = new HashMap<>();
//        for(String j : r_list){
//            Map<Integer, Double> fuck = XinD(j);
//            kid.put(j,fuck);
////            System.out.println(fuck);
//        }
//        return kid;
//    }


//    public Map<Integer, Map<String, Double>> test(String Q) throws Exception {
//        // new空字典[返回值]
//        Map<Integer, Map<String, Double>> fuck = new HashMap<>();
//        // 对Q分词
//        modle_1 m = new modle_1();
//        List<String> r_list = m.R_List(Q);
//        // 调用Search检索前  条
//        Search search = new Search();
//        IndexSearcher indexSearch = search.getIndexSearch();
//        ScoreDoc[] scoreDocs = search.test(Q, 100);
//        // 遍历检索出来的文书
//        for (ScoreDoc scoreDoc : scoreDocs) {
//            // 再new一个空字典存放
//            Map<String, Double> XinQ = new HashMap<>();
//            int i = scoreDoc.doc;
//            Document document = indexSearch.doc(i);
//            String uid = document.get("uid");
////            System.out.println(uid);
//            int a =Integer.parseInt(uid);
//            System.out.println(a);
//            Fields fields = indexSearch.getIndexReader().getTermVectors(i);
//            for (String j : r_list) {
//                //            Iterator<String> fieldsIterator = fields.iterator();
//                double X = 0.0;
//                double XcD = 0.0;
//                long D_count = 0;
//                for (String f : fields) {
////                String field = fieldsIterator.next();
//                    Terms terms = fields.terms(f);
//                    long F_count = terms.size();
//                    D_count += F_count;
//                    TermsEnum termsEnums = terms.iterator();
//                    BytesRef byteRef = null;
//                    while ((byteRef = termsEnums.next()) != null) {
//                        String term = new String(byteRef.bytes, byteRef.offset, byteRef.length);
//                        if (j.equals(term)) {
//                            X += 1.0;
//                        }
//                    }
//                }
//                XcD = X / D_count;
//                XinQ.put(j, XcD);
//            }
//            fuck.put(a, XinQ);
//        }
//        return fuck;
//    }

    public Map<Integer, Map<String, Double>> test(String Q) throws Exception {
        // new空字典[返回值]
        Map<Integer, Map<String, Double>> fuck = new HashMap<>();
        // 对Q分词
        modle_1 m = new modle_1();
        List<String> r_list = m.R_List(Q);
        // 调用Search检索前  条
        Search search = new Search();
        IndexSearcher indexSearch = search.getIndexSearch();
        ScoreDoc[] scoreDocs = search.test(Q, 100);
        // 遍历检索出来的文书
        for (ScoreDoc scoreDoc : scoreDocs) {
            // 再new一个空字典存放
            Map<String, Double> XinQ = new HashMap<>();
            int i = scoreDoc.doc;
            Document document=indexSearch.doc(i);
            String uid = document.get("uid");
            int a =Integer.parseInt(uid);
            Fields fields = indexSearch.getIndexReader().getTermVectors(i);
//            indexSearch.getIndexReader().docFreq(new Term("",""));
            for (String j : r_list) {
                double XcD = 0.0;
                double all_l = 0;
                long D_count = 0;
                for (String f : fields) {
//                String field = fieldsIterator.next();
//                    System.out.println(f+"=================================");
                    Terms terms = fields.terms(f);
                    long F_count = terms.size();
                    D_count += F_count;
                    TermsEnum termsEnums = terms.iterator();
                    BytesRef byteRef = null;
                    while ((byteRef = termsEnums.next()) != null) {
                        String term = new String(byteRef.bytes, byteRef.offset, byteRef.length);
                        long ll = termsEnums.totalTermFreq();
//                        System.out.println(term+ll);
                        if (j.equals(term)) {
                            long l = termsEnums.totalTermFreq();
//                            System.out.println(l);
                            all_l+=l;
//                            X +=1.0;
//                            System.out.println(term+"  "+j);
                        }
                    }
                }
//                System.out.println(all_l);
                XcD = all_l / D_count;
                XinQ.put(j, XcD);
            }
            fuck.put(a, XinQ);
        }
        return fuck;
    }

    public HashMap<Integer,Double> core(String Q) throws Exception {
        //  获取P(q)
        Map<Integer, Map<String, Double>> test = test(Q);
        Map<String, Double> qinC = QinC(Q);
        List<String> r_list = R_List(Q);
        List<Double> result = new ArrayList<>();
        HashMap<Integer,Double> re = new HashMap<>();
        for (Map.Entry<Integer, Map<String, Double>> entry_test : test.entrySet()) {
            Integer uid_ = entry_test.getKey();
            Map<String, Double> value = entry_test.getValue();
            double result_x = 0.0;
            double result_Q = 1.0;
            for (String x : r_list) {
                for (Map.Entry<String, Double> entry_value : value.entrySet()) {
                    for (Map.Entry<String, Double> entry_qinC : qinC.entrySet()) {
                        if (x.equals(entry_value.getKey()) && x.equals(entry_qinC.getKey())) {
                            result_x = entry_value.getValue() + entry_qinC.getValue();
                        }
                    }
                }
                result_Q = result_Q * result_x;
            }
            result.add(result_Q);
            re.put(uid_,result_Q);
        }
        return re;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortDescend(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                int compare = (o1.getValue()).compareTo(o2.getValue());
                return -compare;
            }
        });

        Map<K, V> returnMap = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            returnMap.put(entry.getKey(), entry.getValue());
        }
        return returnMap;
    }


    public static void main(String[] args) throws Exception {
        modle_1 m = new modle_1();
        String Q = "while";
//        System.out.println(m.R_List(Q));
//        System.out.println("sinc" + m.XinC("高人"));
//        System.out.println("test" + m.test(Q));
//        System.out.println(m.QinC(Q));
        System.out.println(sortDescend(m.core(Q)));
    }

}
