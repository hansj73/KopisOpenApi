import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Properties;



public class HelloWorld {
	
	/*static final String propFile = "/data/script/kopisApi/config/config.properties";*/

	public static void main(String[] args) throws UnsupportedEncodingException  {
		// TODO Auto-generated method stub
		
		
		
//		    artOrgName= new String(getProperty("ImageSaveDir").getBytes("ISO-8859-1"), "UTF-8"); 
//			String driverClassName=getProperty("driverClassName") ;
//			String url = getProperty("url") ;
//			
//			
//	
//		System.out.println("artOrgName::"+artOrgName+":::serviceKey1::"+serviceKey1);
//		System.out.println("authKey::"+authKey);
		
		

         String artOrgName="\uc608\uc220\uc758\uc804\ub2f9";
         
        String aa= new String(artOrgName.getBytes("ISO-8859-1"), "UTF-8");
        
        System.out.println(":::aa:::"+unicodeConvert(artOrgName));

		
		     //String poster="http://www.kopis.or.kr/upload/pfmPoster/PF_PF137198_170427_142223.gif";
		      // poster=poster.replaceAll("http://www.kopis.or.kr/upload/pfmPoster/", "http://175.125.91.124/upload/openimg/");
		       
		       //System.out.println("::::poster::"+poster);
			
		//String posterDetail="http://www.kopis.or.kr/upload/pfmIntroImage/PF_PF132236_160704_0226303.jpg";
	}
	
	/**
	 * unicode 읽기
	 * @param str
	 * @return
	 */
	
	private static String unicodeConvert(String str) {
	    StringBuilder sb = new StringBuilder();
	    char ch;
	    int len = str.length();
	    for (int i = 0; i < len; i++) {
	        ch = str.charAt(i);
	        if (ch == '\\' && str.charAt(i+1) == 'u') {
	            sb.append((char) Integer.parseInt(str.substring(i+2, i+6), 16));
	            i+=5;
	            continue;
	        }
	        sb.append(ch);
	    }
	    return sb.toString();
	}


	
	 private static String getProperty(String keyName) {
	        String value = null;
	       
	        try {
	        	
	        	ClassLoader cl=ClassLoader.getSystemClassLoader();
	            URL url = cl.getResource( "config/config.properties" );
	        
	            Properties props = new Properties();
//	            FileInputStream fis = new FileInputStream(propFile);
	            
	            System.out.println("::::::url:::"+url.getPath());
	            FileInputStream fis = new FileInputStream(url.getPath());
	            props.load(new java.io.BufferedInputStream(fis));
	            value = props.getProperty(keyName).trim();
	            fis.close();        } catch (java.lang.Exception e) {
	        }
	            return value;
	    }
	
}
