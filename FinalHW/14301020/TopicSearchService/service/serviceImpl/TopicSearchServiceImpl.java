package cn.edu.bjtu.weibo.service;

import cn.edu.bjtu.weibo.model.BaseContent;
import cn.edu.bjtu.weibo.model.BaseContentSR;
import cn.edu.bjtu.weibo.util.TopicIndexManager;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryTermScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.List;

/**
 * Created by lovejoy on 2016/12/23.
 */
public class SearchTopicServiceImpl implements SearchTopicService {
    /**
     * Given the keyword, search the keyword only in topic, then return all the contents contain the topic
     * @param keyword
     * @param pageIndex
     * @param numberPerPage
     * @return
     */
    public List<BaseContentSR> getSearchedContentWithTopicList(String keyword, int pageIndex, int numberPerPage)
    {
        //if index haven't created, then getDirectory() will get null
        RAMDirectory directory= TopicIndexManager.getDirectory();
        try {
            DirectoryReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            Term term = new Term("content", keyword);
            TermQuery termQuery = new TermQuery(term);
            TopDocs topDocs = searcher.search(new TermQuery(term), pageIndex*numberPerPage);
            SimpleHTMLFormatter simpleHTMLFormatter=new SimpleHTMLFormatter("[","]");
            Highlighter highlighter=new Highlighter(simpleHTMLFormatter,new QueryTermScorer(termQuery));
            for(int i=0;i<topDocs.scoreDocs.length;i++)
            {
                ScoreDoc scoreDoc = topDocs.scoreDocs[i];
                Document doc=searcher.doc(scoreDoc.doc);


                //I don't know which attribute should be added into BaseContent,
                //so I add content, date, owner into BaseContent in my opinion.
                BaseContent bc=new BaseContent();
                bc.setContent(doc.get("content"));
                bc.setDate(doc.get("date"));
                bc.setUserId(doc.get("owner"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //you should call TopicIndexManager().initiate() before run()
    //you can see how I use TopicIndexManager() in TopicIndexTask and Test()
    public void updateSearchIndex(){
        new TopicIndexManager().run();
    }
}
