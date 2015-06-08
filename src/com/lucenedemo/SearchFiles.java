/**
* TODO
* @Project: luceneDemo
* @Title: SearchFiles.java
* @Package com.lucenedemo
* @author jason.liu
* @Date 2014-9-9 下午2:34:15
* @Version v1.0
*/
package com.lucenedemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * TODO
 * @ClassName: SearchFiles
 * @author jason.liu
 */
public class SearchFiles {
	
	
	public static String indexPath = "F:/aostarit/1533质量体系/index";

	/**
	 * TODO
	 * @Title: main
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String field = "path";
		
		try {
			IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexPath)));
			IndexSearcher searcher = new IndexSearcher(reader);
			
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_9);
			
			QueryParser parser = new QueryParser(Version.LUCENE_4_9,field,analyzer);
		
			Query query = parser.parse("软件开发");
			
			System.out.println("Searching for: " + query.toString(field));
			
			Date start = new Date();
			searcher.search(query,null,100);
			Date end = new Date();
			System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
			
//			doPagingSearch(reader, searcher, query, 10, true, false);
			
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query,
			int hitsPerPage, boolean raw, boolean interactive) throws IOException{
		
		//collect enough docs to show 5 pages
		TopDocs results = searcher.search(query, 5*hitsPerPage);
		ScoreDoc[] hits = results.scoreDocs;
		
		int numTotalHits = results.totalHits;
		System.out.println(numTotalHits + " total matching documents");
		
		int start = 0;
		//return the smaller of two int values;
		int end = Math.min(numTotalHits, hitsPerPage);
		
	}

}
