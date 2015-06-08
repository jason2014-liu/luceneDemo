/**
 * TODO
 * @Project: luceneDemo
 * @Title: IndexFiles.java
 * @Package com.lucenedemo
 * @author jason.liu
 * @Date 2014-9-9 上午10:02:10
 * @Version v1.0
 */
package com.lucenedemo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * TODO index all text files unders a directory
 * 
 * @ClassName: IndexFiles
 * @author jason.liu
 */
public class IndexFiles {

	public static String docDirectory = "F:/aostarit/1533质量体系";
	public static String indexPath = "F:/aostarit/1533质量体系/index";

	public static boolean create = true;

	/**
	 * TODO
	 * 
	 * @Title: main
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		final File docDir = new File(docDirectory);
		if (!docDir.exists() || !docDir.canRead()) {
			System.out
					.println("Document directory '"
							+ docDir.getAbsolutePath()
							+ "' does not exist or is not readable, please check the path");
		}

		Date start = new Date();
		System.out.println("Indexing to directory '" + indexPath + "'...");

		try {
			Directory dir = FSDirectory.open(new File(indexPath));

			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_9);
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_9,
					analyzer);

			if (create) {
				// create a new index in the directory,removing any previously
				// indexed documents
				iwc.setOpenMode(OpenMode.CREATE);
			} else {
				// add new documents to an existing index
				iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			}

			IndexWriter writer = new IndexWriter(dir, iwc);
			indexDocs(writer, docDir);
			
			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(" caught a " + e.getClass()
					+ "\n with message: " + e.getMessage());
		}

		Date end = new Date();
		System.out.println(end.getTime() - start.getTime()
				+ " total milliseconds");
	}

	static void indexDocs(IndexWriter writer, File file) throws IOException {

		// do not try to index files that cannot be read
		if (file.canRead()) {
			if (file.isDirectory()) {
				String[] files = file.list();

				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						indexDocs(writer, new File(file, files[i]));
					}
				}
			} else {
				FileInputStream fis;
				try {
					fis = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}

				try {
					// make a new, empty document
					Document doc = new Document();

					// add the path of the file as a field named "path".
					// use a field that is index,but dont tokenize the field
					// into
					// separate words
					// and dont insex term frequency or positional information
					Field pathField = new StringField("path", file.getPath(),
							Field.Store.YES);
					doc.add(pathField);

					// Add the last modified date of the file a field named
					// "modified".
					doc.add(new LongField("modified", file.lastModified(),
							Field.Store.NO));

					// Add the contents of the file to a field named "contents".
					// Specify a Reader,
					// so that the text of the file is tokenized and indexed,
					// but
					// not stored.
					// Note that FileReader expects the file to be in UTF-8
					// encoding.
					// If that's not the case searching for special characters
					// will
					// fail.
//					doc.add(new TextField("contents", new BufferedReader(
//							new InputStreamReader(fis, StandardCharsets.UTF_8))));

					if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {

						System.out.println("adding " + file);
						writer.addDocument(doc);

					} else {
						System.out.println("updating " + file);
						writer.updateDocument(new Term("path", file.getPath()),
								doc);
					}
				} finally {
					fis.close();
				}
			}

		}
	}

}
