package crawler;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.stylesheets.LinkStyle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crawler {
	private static final int MAX_DEPTH = 3;
	private static final int LIMIT = 120;
	private HashSet<String> links;
	private List<List<String>> articles;
	
	public Crawler() {
		links = new HashSet<>();
		// pages = new ArrayList<>();
	}

	public void getPageLinks(String URL, int depth) {
		if ((!links.contains(URL) && URL.contains("https://www.w3schools.com/") && (depth < MAX_DEPTH))) {
			System.out.println(">> Depth: " + depth + " [" + URL + "]");
			try {
				links.add(URL);
				Document document = Jsoup.connect(URL).get();
				Elements linksOnPage = document.select("a[href]");
				depth++;
				for (Element page : linksOnPage) {
					if (links.size() > LIMIT) {
						break;
					}
					if (page.hasAttr("abs:href"))
						getPageLinks(page.attr("abs:href"), depth);
				}
			} catch (IOException e) {
				System.err.println("For '" + URL + "': " + e.getMessage());
			}
		}
	}

	public void downloadLinks() {
		links.forEach(x -> {
			Document document;
			FileWriter writer;
			try {
				document = Jsoup.connect(x).get();
			    new File("\\HTML_Files").mkdir();
			    //articles.
			    writer = new FileWriter("/HTML_Files/"+document.title());
				try {
					String temp = document.html();
					writer.write(temp);
				} catch (Exception e) {
					// TODO: handle exception
				}
				writer.close();
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		});
	}

	public static void main(String[] args) throws IOException {
		Crawler crawl = new Crawler();
		crawl.getPageLinks("https://www.w3schools.com/", 0);
		System.out.println(crawl.links.size());
		System.out.println("Downloading..");
		crawl.downloadLinks();
	}

}
