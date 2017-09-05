package com.mark.demo.crawler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
*hxp(hxpwangyi@126.com)
*2017年9月5日
*
*/
public class CSDNCrawler {
	private static String baseUrl="http://blog.csdn.net";
	private static String blogUrl="http://blog.csdn.net/hxpjava1";
	private static List<String> articleUrls=new ArrayList<String>();
	private static Integer totalIndexPageNum;
	private static String saveDir="d://tmp/";
	
	public static void main(String[] args) throws IOException {
		Document doc=Jsoup.connect(blogUrl).userAgent("Mozilla").post();
		Element element=doc.getElementById("papelist");
		Elements as=element.getElementsByTag("a");
		Element lastPage=as.get(as.size()-1);
		String lastPageUrl=lastPage.attr("href");
		String[] urlSplited=lastPageUrl.split("/");
		totalIndexPageNum=Integer.parseInt(urlSplited[urlSplited.length-1]);
		
		getArticleUrls();
		
		savePages();
	}
	
	public static void printUrls(){
		for(int i=0;i<articleUrls.size();i++){
			System.out.println(articleUrls.get(i));
		}
	}
	
	public  static  void getArticleUrls() throws IOException{
		for(int i=1;i<=totalIndexPageNum;i++){
			Document doc=Jsoup.connect(baseUrl+"/hxpjava1/article/list/"+i).userAgent("Mozilla").post();
			Elements elements=doc.getElementsByClass("link_title");
			for(int j=0;j<elements.size();j++){
				Element element=elements.get(j);
				Elements es=element.getElementsByTag("a");
				articleUrls.add(es.get(0).attr("href"));
			}
		}
	}
	
	public static void savePages(){
		for(int i=0;i<articleUrls.size();i++){
			String articleUrl=articleUrls.get(i);
			BufferedInputStream bis=null;
			BufferedOutputStream bos =null;
			try{
				Document doc=Jsoup.connect(baseUrl+articleUrl).post();
				String name=doc.title();
				
				File dest = new File(saveDir +name+".html");
	            InputStream is;
	            FileOutputStream fos = new FileOutputStream(dest);
	            
				URL url = new URL(baseUrl+articleUrl);
		        is = url.openStream();
		    
		        //为字节输入流加缓冲
		         bis= new BufferedInputStream(is);
		        //为字节输出流加缓冲
		        bos = new BufferedOutputStream(fos);
	
		        int length;
	
		        byte[] bytes = new byte[1024*20];
		        while((length = bis.read(bytes, 0, bytes.length)) != -1){
		            fos.write(bytes, 0, length);
		        }
			}catch(Exception e){
				e.printStackTrace();
				try {
					bis.close();
					bos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
					
		}
		
	}
}
