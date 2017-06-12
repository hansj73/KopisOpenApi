import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.imageio.ImageIO;


public class DataUtil {

	public DataUtil(){}
	
	 public static String getProperty(String keyName) {
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
	 
	 public static String getLogProperty() {
	        String value = "";
	        try {
	        	ClassLoader cl=ClassLoader.getSystemClassLoader();
	            URL url = cl.getResource( "log4j.xml" );
	            value=url.getPath();
	        }catch(Exception e){}
	            return value;
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
	 
	 public static void ImageRead(String imageUrl) throws Exception{
			
			BufferedImage image=null;
			int width = 0;
			int height = 0;
		
			
			 String localPath=DataUtil.MkFileDir();
			 
			
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
				EtcImageWrite(imageUrl,localPath);
			}
		}
		
		 public static void EtcImageWrite(String targetUrl,String localPath)throws Exception {
			 
			    File file = new File(targetUrl);
			    
//				String localPath=DataUtil.getProperty("ImageSaveDir");

				System.out.println("::::EtcImageWrite::"+targetUrl);
				System.out.println("::::localPath::"+localPath);
				System.out.println("::::localPath_fullpath::"+localPath+"/"+file.getName());
				
				
			    FileOutputStream in = new FileOutputStream(localPath+"/"+file.getName());		  
			    URL url = new URL(targetUrl);
			    InputStream fi = url.openStream();
			    int size;
			    while((size=fi.read())>-1){
			    	in.write(size);
			    }
			    in.close();
			 
		 }
		 
public static String  TagImageSrc(String overview,String imgUrl,String contentTypeId) {
		 	 
		    /* String cultureUrl=DataUtil.getProperty("cultureSaveUrl");
		     String firstImageUrl=DataUtil.getProperty("firstImageUrl")+"/"+contentTypeId+"/";*/
		     
		     String imgUrl1=imgUrl;
/*		     imgUrl1=imgUrl1.replaceAll(firstImageUrl,cultureUrl);*/
		     
		     String localPath=DataUtil.MkFileDir();
		     
		 	 imgUrl1=DataUtil.ImageRename(imgUrl1);
		     
		     System.out.println("::::TagImageSrc::"+imgUrl1);
		    		 
		 	 StringBuffer sb = new StringBuffer();
		     sb.append("<p>");
		 	 try
		 	 {
//		 		iUrl=imgUrl.split("@");
	        	
//	        	for(String i: iUrl){
//	        		if(!"".equals(i)){
	        			EtcImageWriteConent(imgUrl1,localPath); // 이미지 저장하기
	        			sb.append("<img src=\""+imgUrl1+"\"/><br/>");
	        			sb.append(overview);
//	        		}
//	        	}
	        	sb.append("</p>");
	        	/*System.out.println(sb.toString());*/
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(":::e::"+e.getMessage());
			}
		 
		 return sb.toString();
	}
	 

public static void EtcImageWriteConent(String targetUrl,String localPath)throws Exception {
	 
//	    String targetUrl="http://www.kopis.or.kr/upload/pfmPoster/PF_PF136954_170414_100633.jpg";
	    File file = new File(targetUrl);
//	    System.out.println("::ffa::"+file.getName());
	    
	    
//		String localPath=DataUtil.getProperty("ImageSaveDir");
//		String localPath=ImagSaveDir;
		
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

   
/*
 * 이미지 파일 다운로드 경로변경
 */
	public static String MkFileDir(){
	    
	      SimpleDateFormat sdf = new SimpleDateFormat("yyMM");
	      Calendar c1 = Calendar.getInstance();
		  String strToday = sdf.format(c1.getTime());
		  
		  String year=strToday.substring(0,2);
		  String month=strToday.substring(2,4);
		  
		  /*String imageSubDir="/"+year+"/"+month+"/"+DataUtil.getProperty("cat2");*/
		  String imageSubDir="/"+year+"/"+month+"/";
		  
		  String ImageSaveDir=DataUtil.getProperty("ImageSaveDir")+imageSubDir; //
			 
		     //파일 객체 생성
		     File file = new File(ImageSaveDir);
		     //!표를 붙여주어 파일이 존재하지 않는 경우의 조건을 걸어줌
		     if(!file.exists()){
		         //디렉토리 생성 메서드
		         file.mkdirs();
		         System.out.println("created directory successfully!");
		     }
		     
		     return ImageSaveDir;
	}
	
	public static String cultureSaveUrl(){
	    
	      SimpleDateFormat sdf = new SimpleDateFormat("yyMM");
	      Calendar c1 = Calendar.getInstance();
		  String strToday = sdf.format(c1.getTime());
		  
		  String year=strToday.substring(0,2);
		  String month=strToday.substring(2,4);
		  
		  /*String imageSubDir="/"+year+"/"+month+"/"+DataUtil.getProperty("cat2");*/
		  String imageSubDir="/"+year+"/"+month+"/";
		  String cultureSaveUrl=DataUtil.getProperty("cultureSaveUrl")+imageSubDir; //
		  return cultureSaveUrl;
	}
	
	/*
	    * 이미지 url 변경
	    */

	public static String ImageRename(String firstImageUrlThum){
	    
	      SimpleDateFormat sdf = new SimpleDateFormat("yyMM");
	      Calendar c1 = Calendar.getInstance();
		  String strToday = sdf.format(c1.getTime());
		  
		  String year=strToday.substring(0,2);
		  String month=strToday.substring(2,4);
		  
		  String imageSubDir="/"+year+"/"+month+"/"+DataUtil.getProperty("cat2");
		  
		  String[] ff=firstImageUrlThum.replaceAll(DataUtil.getProperty("firstImageUrl2")+"/", "").split("/");
		  
		  String firstImagerUrl2=DataUtil.getProperty("firstImageUrl2"); // 썸네일이미지
		  String cultureUrl=DataUtil.getProperty("cultureSaveUrl")+imageSubDir;
		  	
		  firstImageUrlThum=firstImageUrlThum.replaceAll(firstImagerUrl2+"/"+ff[0], cultureUrl);
		  	
		     
		     return firstImageUrlThum;
	}
	
	public static String SEdate() {
		
		  Calendar cal = Calendar.getInstance();
	      	 
	      //현재 년도, 월, 일
	      int year = cal.get ( cal.YEAR );
	      int month = cal.get ( cal.MONTH ) + 1 ;
	      int date = cal.get ( cal.DATE ) ;
	      	
	      int startDay = cal.get(cal.DAY_OF_MONTH);
	      int endDay = cal.getActualMaximum(cal.DAY_OF_MONTH); 
	      
	      
	      String smonth=(month<10)?"0"+month:month+"";
	      String fromYMD = year+""+smonth+""+"01";
	      String toYMD = year+""+smonth+""+endDay;
	      
	      return fromYMD+":"+toYMD;
	}

}
