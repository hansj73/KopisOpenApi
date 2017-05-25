

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

public class KopisApiBatch {
	static Connection con;
	static Statement stmt;
    static ResultSet rs;
    
    //static final String propFile = "G:/study/workspace/KopisOpenApi/config/config.properties";
//    static final String propFile = "/data/script/kopisApi/config";
    
    /** 
     * config 파일 load 
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
//            System.out.println(e.toString());
        }
            return value;
    }
    

    public static void  DbDriverLoad()
    {
    	
    	try{
            
            Class.forName("oracle.jdbc.driver.OracleDriver");
//            	System.out.println("드라이버 로딩 성공");
			} catch (ClassNotFoundException e) {
//				System.out.println("드라이버 로딩 실패");
			}
    }
    
    
    public static void getConnection()
    {
    	 
        try{
            //커넥션을 가져온다.
        	
        	   
        	 	String driverClass = getProperty("driverClassName") ;
	            String url = getProperty("url") ;
	            String username =getProperty("username") ;
	            String password= getProperty("password") ;
	            
	            // 콘솔 출력
//	            System.out.println(driverClass+":"+url+":"+username+":"+password) ;
	            con = DriverManager.getConnection(url,username, password);
	            System.out.println("커넥션 성공");
	           
        	
        	}catch(Exception e){
        		e.printStackTrace();
        	}
    }
    
    
        
    public  static void getData(String mt20id,String shprfnmfct) throws IOException
    {
    	  String Detail_apiXml="";
    	  StringBuffer sb = new StringBuffer();
    	  
    	  try{
           	stmt = con.createStatement();
              //데이터를 가져온다.
           	/**
           	 * type 06  공연
           	 */
           	rs = stmt.executeQuery("select count(*) count from OPENAPI_METADATA where sub_title='"+mt20id+"' and type='06'");
              
              int cnt=(rs.next()==true)?rs.getInt("count"):-1;
              
//              System.out.println(":::cnt:::"+cnt);
              
              if(cnt==0)
              {
              	Detail_apiXml= KopisApiExplorer.apiDataDetail(mt20id); // 리스트 조회
              	
              	ArrayList<KopisApiDto> Api_detail = (ArrayList<KopisApiDto>) KopisApiExplorer.processDocumentDetail(Detail_apiXml);
              	
              	/** prfnm 공연 제목 특수 문자 unicode로 치환**/
              	String title = KopisApiExplorer.getUnicodeChage(Api_detail.get(0).getPrfnm());
              	
              	/** DESCRIPTION  이미지 url 추가하기 ***/
              	String description=KopisApiExplorer.TagImageSrc(Api_detail.get(0).getStyurl1());
              	
              	/** 공연 기간 ***/
              	String period=Api_detail.get(0).getPrfpdfrom()+"~"+Api_detail.get(0).getPrfpdto();
              	
    		    /** post url 변경 에 입력***/
              	
              	String cultureUrl=getProperty("cultureSaveUrl");
              	String kopisPosterUrl=getProperty("kopisPosterUrl");
              	
              	String posterUrl=Api_detail.get(0).getPoster().replaceAll(kopisPosterUrl, cultureUrl);
              	
              	/** 장르 코드변환***/
              	String genreCode=KopisApiExplorer.getGenreCode(Api_detail.get(0).getGenrenm()); // 장르 코드 분류추가 
              			
              	sb.append("INSERT INTO OPENAPI_METADATA");
              	sb.append("(");
              	sb.append("SEQ,TITLE,SUB_TITLE");
              	sb.append(",REG_DATE,CREATED_DATE,TYPE");
              	sb.append(",EXTENT,DESCRIPTION,RIGHTS");
              	sb.append(",GRADE,CHARGE,VENUE");
              	sb.append(",PERIOD,TIME,REFERENCE_IDENTIFIER_ORG");
              	sb.append(",APPROVAL,GENRE");
              	sb.append(")");
              	sb.append("VALUES");
              	sb.append("(");
              	sb.append("OPENAPI_METADATA_SEQUENCE.NEXTVAL,'"+title+"','"+mt20id+"'");
              	sb.append(",sysdate,sysdate,'06'");
              	sb.append(",'"+Api_detail.get(0).getPrfruntime()+"','"+description+"','"+Api_detail.get(0).getEntrpsnm()+"'");
              	sb.append(",'"+Api_detail.get(0).getPrfage()+"','"+Api_detail.get(0).getPcseguidance()+"','"+Api_detail.get(0).getFcltynm()+"'");
              	sb.append(",'"+period+"','"+Api_detail.get(0).getDtguidance()+"','"+posterUrl+"'");
              	sb.append(",'W','"+genreCode+"')");
                          	  
              	/*System.out.println("::SQL:::"+sb.toString());*/
              	stmt.executeUpdate(sb.toString());
              	
              	try {
					KopisApiExplorer.ImageRead(Api_detail.get(0).getPoster());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
              	
              	System.out.println("파일 입력 및 이미지 저장 완료");
              }
            	else if(cnt>0)
            	{
            		System.out.println("등록되어 있습니다.");
            	}
            	else
            	{
            		System.out.println(":::error_msg::"+cnt);
            	}
              
            
             }catch(SQLException e){
              e.printStackTrace();
              System.out.println("::e::"+e);
          }
        
    }
     
    public static void closeConnection()
    {
    	try{
                //자원 반환
                rs.close();
                stmt.close();
                con.close();
//                System.out.println("::::dbClose():::");
            }catch(Exception e){
                e.printStackTrace();
            }
    }
   
    
    public static int  ApiMain(int s_num,String shprfnmfct,String state) 
    {
		// TODO Auto-generated method stub
		KopisApiExplorer kopisEx = new KopisApiExplorer();
		
		ArrayList<KopisApiDto> Api_list = (ArrayList<KopisApiDto>) kopisEx.getKopisList(s_num,shprfnmfct,state);
		 
		 int list_cnt=Api_list.size();
		
		 if(list_cnt >0 ){
			  getConnection(); //** 디비접속**
		 }
		 
		 for(int i=0; i<Api_list.size(); i++){
			 try {
				getData(Api_list.get(i).getMt20id(),shprfnmfct);
			} catch (IOException e) {
				e.printStackTrace();
			}
		 }
		 
		 if(list_cnt >0 ){
			  closeConnection();
		 }
		 
		 return list_cnt;
				
	}

}
