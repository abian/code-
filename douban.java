package com.jsoup;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Douban {
  private static String path = "E:/photo.html";
	private static String url = "http://www.douban.com/photos/album/64180843/";
	private static String headerName = "User-Agent";
	private static String headerValue = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/534.3 (KHTML, like Gecko) Chrome/6.0.472.63 Safari/534.3";
	private static String div_photo_wrap = ".photo_wrap";
	private static String divp_photolst_photop = ".photolst_photo";
	private static final int TOTAL_PAGE_SIZE = 29;
	private static final int PAGE_SIZE = 18;

	public static void select(String url, int start, Writer writer)
			throws IOException {
		DefaultHttpClient client = new DefaultHttpClient();
		url = url + "?start=" + start;
		HttpGet get = new HttpGet(url);
		get.setHeader(headerName, headerValue);
		HttpResponse response = client.execute(get);
		InputStream stream = response.getEntity().getContent();
		String html = IOUtils.toString(stream);
		Document doc = Jsoup.parse(html);
		Elements tweets = doc.body().select(div_photo_wrap)
				.select(divp_photolst_photop);

		for (Element tweet : tweets) {
			String href = tweet.attr("href");
			// String src=tweet.children().attr("src");
			String id = tweet.attr("title");
			// System.out.println("photo\t"+photo);
			// System.out.println(tweet.outerHtml().replace(href, id));
			writer.append(tweet.outerHtml().replace(href, id));
			writer.flush();
			// System.out.println("id\t"+id);
			// System.out.println("src\t"+src);
		}

	}

	public static void main(String[] args) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(path));
		String html = "<html><body>";
		writer.append(html);
		for (int i = 0; i < TOTAL_PAGE_SIZE; i++) {
			int start = PAGE_SIZE * i;
			Douban.select(url, start, writer);
		}
		writer.append("</body></html>");
		writer.close();
		System.out.println("*****************Finish*****************");

	}
}
