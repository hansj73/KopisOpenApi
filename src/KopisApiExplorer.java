
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class KopisApiExplorer {
	
//	static final String propFile = "G:/study/workspace/KopisOpenApi/config/config.properties";
//	static final String propFile = "/data/script/kopisApi/config";
//	static final String ImageSaveDir = "/data/nas/upload/openimg";
    /**
     * config  파일 laad
     * @param keyName
     * @return
     */
    
	 private static String getProperty(String keyName) {
	        String value = null;
	       
	        try {
	        	
	        	ClassLoader cl=ClassLoader.getSystemClassLoader();
	            URL url = cl.getResource( "config.properties" );
	            
	            Properties props = new Properties();
	            FileInputStream fis = new FileInputStream(url.getPath());
	            props.load(new java.io.BufferedInputStream(fis));
	            value = props.getProperty(keyName).trim();
	            fis.close();        } catch (java.lang.Exception e) {
//	            System.out.println(e.toString());
	        }
	            return value;
	    }
	 
    
	 public  ArrayList<KopisApiDto> getKopisList(int c_page,String shprfnmfct,String state)
	 {
		
		  String List_apiXml="";
		  try {
			  /** 리스트조회***/
			 List_apiXml= apiData(c_page,shprfnmfct,state); // 리스트 조회
			 /*System.out.println(":::apiXml::"+List_apiXml);*/
			 ArrayList<KopisApiDto> list=processDocument(List_apiXml);
			 /*System.out.println(":::list_size::"+list.size());*/
		        return list;
			 } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  return null;
    }
	
	/**
	 * @desc kopsi 공연예술 api 리스트 형태로 데이터 가지고 오기
	 * @return
	 * @throws IOException
	 * @param prfstate  : 01 공연예정 02공연중
	 */
	public static String apiData(int c_page,String shprfnmfct,String state) throws IOException{
		       
//			System.out.println("::::c_page:::"+c_page+":::shprfnmfct:::"+shprfnmfct);
		
			String serviceKey=getProperty("serviceKey");// serviceKey 인증키
			
			StringBuilder urlBuilder = new StringBuilder("http://www.kopis.or.kr/openApi/restful/pblprfr"); 
	        urlBuilder.append("?service="+serviceKey+""); /*Service Key*/
	        urlBuilder.append("&stdate=20170501&eddate=20170531&cpage="+c_page+"&rows=20&prfstate="+state+"");
	        urlBuilder.append("&shprfnmfct="+ URLEncoder.encode(shprfnmfct,"UTF-8") );
	        URL url = new URL(urlBuilder.toString());
	        System.out.println(":::xml:::"+urlBuilder.toString());
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Content-type", "application/xml");
//	        System.out.println("Response code: " + conn.getResponseCode());
	        BufferedReader rd;
	        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
	            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        } else {
	            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
	        }
	        StringBuilder sb = new StringBuilder();
	        String line;
	        while ((line = rd.readLine()) != null) {
	            sb.append(line);
	        }
	        rd.close();
	        conn.disconnect();
	        //System.out.println(sb.toString());
	        return sb.toString();
	}// apiData end
	
	/**
	 * @desc 리스트 에서 상세보기호출 
	 * @param mt20id
	 * @return
	 * @throws IOException
	 */
	public  static  String apiDataDetail(String mt20id) throws IOException{
		
		String serviceKey=getProperty("serviceKey");// serviceKey 인증키
		
		StringBuilder urlBuilder = new StringBuilder("http://www.kopis.or.kr/openApi/restful/pblprfr/"); 
        urlBuilder.append(mt20id+"?service="+serviceKey+""); /*Service Key*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/xml");
        /*System.out.println("Response code: " + conn.getResponseCode());*/
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        //System.out.println(sb.toString());
        return sb.toString();
}// apiData end
	
	/**
	 * @desc 리스트 xml parser
	 * @param apiXml
	 * @return
	 */
	
	private static ArrayList<KopisApiDto>  processDocument(String apiXml)  {
	    
	    // xmlPullParser
		ArrayList<KopisApiDto> arrayList = new ArrayList<KopisApiDto>();
	    try {
	        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	        XmlPullParser parser = factory.newPullParser();
	        parser.setInput(new StringReader(apiXml));
	        int eventType = parser.getEventType();
	        KopisApiDto openData = null;
	
	        while(eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String startTag = parser.getName();
                        if(startTag.equals("db")) {
                        	openData = new KopisApiDto();
                        }
                        if(startTag.equals("mt20id")) {
                        	openData.setMt20id(parser.nextText());
                        }
                  /*      if(startTag.equals("prfnm")) {
                        	openData.setPrfnm(parser.nextText());
                        }
                        if(startTag.equals("prfpdfrom")) {
                        	openData.setPrfpdfrom(parser.nextText());
                        }
                        if(startTag.equals("prfpdto")) {
                        	openData.setPrfpdto(parser.nextText());
                        }
                        if(startTag.equals("poster")) {
                        	openData.setPoster(parser.nextText());
                        }
                        if(startTag.equals("genrenm")) {
                        	openData.setGenrenm(parser.nextText());
                        }
                        if(startTag.equals("prfstate")) {
                        	openData.setPrfstate(parser.nextText());
                        }*/
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if(endTag.equals("db")) {
                            arrayList.add(openData);
                        }
                        break;
                }
                eventType = parser.next();
            }
	        
	   }catch(XmlPullParserException e) {
	        e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return arrayList;
	}// list end
	
	/**
	 * @desc 상세보기 xml parser
	 * @param apiXml
	 * @return
	 */
	public static  ArrayList<KopisApiDto>  processDocumentDetail(String apiXml)  {
	    // xmlPullParser
		ArrayList<KopisApiDto> arrayList = new ArrayList<KopisApiDto>();
	    try {
	        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	        XmlPullParser parser = factory.newPullParser();
	        
	        parser.setInput(new StringReader(apiXml));
	        
	        int eventType = parser.getEventType();
	        KopisApiDto openData = null;
	
	        while(eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String startTag = parser.getName();
                        if(startTag.equals("db")) {
                        	openData = new KopisApiDto();
                        }
                        if(startTag.equals("mt20id")) {
                        	openData.setMt20id(parser.nextText());
                        }
                        if(startTag.equals("prfnm")) {
                        	openData.setPrfnm(parser.nextText());
                        }
                        if(startTag.equals("prfpdfrom")) {
                        	openData.setPrfpdfrom(parser.nextText());
                        }
                        if(startTag.equals("prfpdto")) {
                        	openData.setPrfpdto(parser.nextText());
                        }
                        if(startTag.equals("fcltynm")) {
                        	openData.setFcltynm(parser.nextText());
                        }
                        if(startTag.equals("prfcrew")) {
                        	openData.setPrfcrew(parser.nextText());
                        }
                        if(startTag.equals("prfruntime")) {
                        	openData.setPrfruntime(parser.nextText());
                        }
                        if(startTag.equals("prfage")) {
                        	openData.setPrfage(parser.nextText());
                        }
                        if(startTag.equals("entrpsnm")) {
                        	openData.setEntrpsnm(parser.nextText());
                        }
                        if(startTag.equals("pcseguidance")) {
                        	openData.setPcseguidance(parser.nextText());
                        }
                        if(startTag.equals("poster")) {
                        	openData.setPoster(parser.nextText());
                        }
                        if(startTag.equals("genrenm")) {
                        	openData.setGenrenm(parser.nextText());
                        }
                        if(startTag.equals("prfstate")) {
                        	openData.setPrfstate(parser.nextText());
                        }
                        if(startTag.equals("dtguidance")) {
                        	openData.setDtguidance(parser.nextText());
                        }
                        if(startTag.equals("styurls")) {
                        	String styurl_img=processImageElement(parser);
                        	openData.setStyurl1(styurl_img);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if(endTag.equals("db")) {
                            arrayList.add(openData);
                        }
                        break;
                }
                eventType = parser.next();
            }
	        
	   }catch(XmlPullParserException e) {
	        e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return arrayList;
	}//detail end
	
	/**
	 * @desc image xml parse
	 * @param xpp
	 * @return 이미지 값들을 ':' 구분자로 합쳐서 리턴한다
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static String processImageElement (XmlPullParser xpp) throws XmlPullParserException, IOException
    {
		int eventType = xpp.getEventType();
		String img_url="";
        while (eventType != xpp.END_DOCUMENT) {
         	if(eventType == xpp.END_TAG) {
                if(xpp.getName().equals("styurls")){
                	break;
                }
            } else if(eventType == xpp.TEXT) {
            	/*else if 문 삭제하지 말것 에러발생*/
                /*System.out.println("styurl"+xpp.getText());
            	img_url=xpp.getText()+":"+img_url;*/
            } else if(xpp.getName().equals("styurl")){
            	/*System.out.println("styurl"+xpp.nextText());*/
            	img_url=xpp.nextText()+"@"+img_url;
            }
            eventType = xpp.next();
        }
        /*System.out.println(":::img_url::"+img_url);*/
        return img_url;
    }
	
	public static void ImageRead(String imageUrl) throws Exception{
		
		BufferedImage image=null;
		int width = 0;
		int height = 0;
		//String imageUrl="";
		//imageUrl=PosterImgUrl+"/"+;
//		System.out.println(imageUrl);
		
		 
//		 String localPath=ImagSaveDir;
		 String localPath=getProperty("ImageSaveDir");
		 
		 System.out.println(":::::::::localPath_ImageRead:::::"+localPath);
		
		try{
	
    	image = ImageIO.read(new URL(imageUrl));
    	BufferedImage bufferdImage = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_BGR);
    	Graphics2D graphics = (Graphics2D)bufferdImage.getGraphics();
    	graphics.setBackground(Color.WHITE);
    	graphics.drawImage(image,0,0,null);
        String fileNm = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        String file_ext = fileNm.substring(fileNm.lastIndexOf('.')+1,fileNm.length());
        /*System.out.println("::::fileNm::"+fileNm+"::file_ext:::"+file_ext);*/
        ImageIO.write(bufferdImage, file_ext, new File(localPath+"/"+fileNm));
        /*System.out.println(fileNm+"다운완료");*/
        
		}catch(Exception e){
			EtcImageWrite(imageUrl);
		}
	}
	
	 public static void EtcImageWrite(String targetUrl)throws Exception {
		 
//		    String targetUrl="http://www.kopis.or.kr/upload/pfmPoster/PF_PF136954_170414_100633.jpg";
		    File file = new File(targetUrl);
//		    System.out.println("::ffa::"+file.getName());
		    
		    
//			String localPath=ImagSaveDir;
			String localPath=getProperty("ImageSaveDir");
			
			System.out.println(":::::::::localPath_EtcImageWrite:::::"+localPath);
			
		    FileOutputStream in = new FileOutputStream(localPath+"/"+file.getName());		  
		    URL url = new URL(targetUrl);
		    InputStream fi = url.openStream();
		    int size;
		    while((size=fi.read())>-1){
		    	in.write(size);
		    }
		    in.close();
		 
		 /*System.out.println("::::EtcImageWrite::"+targetUrl);*/
	 }
	 
	 /*
	  * content 안에 있는 이미지 파일 가지고 오기
	  */
	 public static void EtcImageWriteConent(String targetUrl)throws Exception {
		 
//		    String targetUrl="http://www.kopis.or.kr/upload/pfmPoster/PF_PF136954_170414_100633.jpg";
		    File file = new File(targetUrl);
//		    System.out.println("::ffa::"+file.getName());
		    
		    
			String localPath=getProperty("ImageSaveDir");
//			String localPath=ImagSaveDir;
			
			System.out.println(":::::::::localPath_EtcImageWriteConent:::::"+localPath);
			
			
		    FileOutputStream in = new FileOutputStream(localPath+"/"+file.getName());
		  
		    URL url = new URL(targetUrl);
		    InputStream fi = url.openStream();
		    int size;
		    while((size=fi.read())>-1){
		    	in.write(size);
		    }
		    in.close();
		 
		 System.out.println("::::EtcImageWriteConent::"+targetUrl);
	 }
	 
	 public static String  TagImageSrc(String imgUrl) {
		 	 
		     String cultureUrl=getProperty("cultureSaveUrl");
		     String kopisPosterstyurl=getProperty("kopisPosterstyurl");
		    		 
		 	 String[] iUrl=null;
		 	 StringBuffer sb = new StringBuffer();
		     sb.append("<p>");
		 	 try
		 	 {
		 		iUrl=imgUrl.split("@");
	        	
	        	for(String i: iUrl){
	        		if(!"".equals(i)){
	        			EtcImageWriteConent(i); // 이미지 저장하기
	        			i=i.replaceAll(kopisPosterstyurl, cultureUrl);
	        			sb.append("<img src=\""+i+"\"/><br/>");
	        		}
	        	}
	        	sb.append("</p>");
	        	/*System.out.println(sb.toString());*/
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(":::e::"+e.getMessage());
			}
		 
		 return sb.toString();
	}
	 
	 public static String  getGenreCode(String genrenm) {
		 
		 String codeValue="";
		 
		 if(genrenm.equals("연극")||genrenm.equals("뮤지컬")){
			 codeValue="1";
		 }else if(genrenm.equals("무용")||genrenm.equals("발레")){
			 codeValue="5";
		 }else if(genrenm.equals("오페라")||genrenm.equals("클래식")||genrenm.equals("국악")){
			 codeValue="2";
		  }else{
			 codeValue="1";
		 }	 
		 return codeValue;
	}
	 
	 public static String getUnicodeChage(String str){
			
		 	String	chTitle=str.replaceAll("'","&#39");
		 			/*chTitle=chTitle.replaceAll("!","&#33");
					chTitle=chTitle.replaceAll("\"","&#34");
					chTitle=chTitle.replaceAll("#","&#35");
					chTitle=chTitle.replaceAll("$","&#36");
					chTitle=chTitle.replaceAll("%","&#37");
					chTitle=chTitle.replaceAll("&","&#38");
					chTitle=chTitle.replaceAll("\\(","&#40");
					chTitle=chTitle.replaceAll("\\)","&#41");
					chTitle=chTitle.replaceAll("\\*","&#42");
					chTitle=chTitle.replaceAll("\\+","&#43");
					chTitle=chTitle.replaceAll(",","&#44");
					chTitle=chTitle.replaceAll("-","&#45");
					chTitle=chTitle.replaceAll("/","&#47");
					chTitle=chTitle.replaceAll(":","&#58");
					chTitle=chTitle.replaceAll(";","&#59");
					chTitle=chTitle.replaceAll("<","&#60");
					chTitle=chTitle.replaceAll("=","&#61");
					chTitle=chTitle.replaceAll(">","&#62");
					chTitle=chTitle.replaceAll("?","&#63");*/
			return chTitle;
		}
		
		
	
}// class end
