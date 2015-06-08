/**
 * TODO
 * @Project: luceneDemo
 * @Title: Demo.java
 * @Package com.lucenedemo
 * @author jason.liu
 * @Date 2014-9-9 下午3:27:21
 * @Version v1.0
 */
package com.lucenedemo;

import java.io.IOException;

import junit.framework.Assert;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 * TODO
 * 
 * @ClassName: Demo
 * @author jason.liu
 */
public class Demo {

	/**
	 * TODO
	 * 
	 * @Title: main
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);

		// Store the index in memory:
		Directory directory = new RAMDirectory();
		// To store an index on disk, use this instead:
		// Directory directory = FSDirectory.open("/tmp/testindex");
		IndexWriterConfig config = new IndexWriterConfig(
				Version.LUCENE_CURRENT, analyzer);
		IndexWriter iwriter = new IndexWriter(directory, config);
		Document doc = new Document();
		String text = "This is the text to be indexed.";
		doc.add(new Field("fieldname", text, TextField.TYPE_STORED));
		iwriter.addDocument(doc);
		iwriter.close();

		// Now search the index:
		DirectoryReader ireader = DirectoryReader.open(directory);
		IndexSearcher isearcher = new IndexSearcher(ireader);
		// Parse a simple query that searches for "text":
		QueryParser parser = new QueryParser(Version.LUCENE_CURRENT,
				"fieldname", analyzer);
		Query query = parser.parse("text2");
		ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
		Assert.assertEquals(1, hits.length);
		System.out.println("hits:" + hits.length);
		// Iterate through the results:
		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = isearcher.doc(hits[i].doc);
			Assert.assertEquals("This is the text to be indexed.",
					hitDoc.get("fieldname"));
			System.out.println("This is the text to be indexed:"
					+ hitDoc.get("fieldname"));

		}
		ireader.close();
		directory.close();
	}

}
