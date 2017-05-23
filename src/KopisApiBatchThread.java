

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Properties;

public class KopisApiBatchThread extends KopisApiBatch{
         
	//static final String propFile = "G:/study/workspace/KopisOpenApi/config/config.properties";
//	static final String propFile = "/data/script/kopisApi/config";
	/**
	 * config 파일 load
	 */
	
	private static String OS = System.getProperty("os.name").toLowerCase();

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
	        }
	            return value;
	    }

    static void threadMessage(String message) {
        String threadName =Thread.currentThread().getName();
        System.out.format("%s: %s%n",threadName,message);
        
    }
    
    /**
     * @desc state01 공연예정 
     */
  
    private static class MessageLoop implements Runnable {
        public void run() {
        	
        	 String[] importantInfo = null;
        	 try {
        		 if(OS.indexOf("win") >= 0){
        			 importantInfo = getProperty("artOrgName").split(",");
        		 }else{
        			 importantInfo = new String(getProperty("artOrgName").getBytes("ISO-8859-1"), "UTF-8").split(",");
        		 }
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
        	
        	try {
            	int j=1;
            	
            	 for (String shprfnmfct :importantInfo) 
            	 {
            		 for (;;) 
            		 {// 무한루푸
            			//for (int i=0; i<3; i++) {// 무한루푸*/                    
            			 Thread.sleep(4000);//4초
            			 int list_size= KopisApiBatch.ApiMain(j,shprfnmfct,"01");
            			 /*DbConnTest.ApiMain(j++);*/
            			 /*threadMessage("DbConnTest.getConnection()");*/
            			 System.out.println("::pageCnt::"+j+"::::::"+shprfnmfct);
            			 if(list_size ==0){System.out.println(":::list_size_break::");break;}// if --end
            			 j++;
            		 }// for end
            			j=1;
            	 }//for end
          
            } catch (InterruptedException e) {
            	System.out.println("::::SimpleThreads::run::error::"+e);
            }
        	
        }
    }
    
    /**
     *@desc state 02 공연중
     */
    
    private static class MessageLoop1 implements Runnable {
        public void run() {
        	
        	 String[] importantInfo = null;
        	 try {
        		 if(OS.indexOf("win") >= 0){
        			 importantInfo = getProperty("artOrgName").split(",");
        		 }else{
        			 importantInfo = new String(getProperty("artOrgName").getBytes("ISO-8859-1"), "UTF-8").split(",");
        		 }
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
        	
        	try {
            	int j=1;
            	
            	 for (String shprfnmfct :importantInfo) 
            	 {
            		 for (;;) 
            		 {// 무한루푸
            			//for (int i=0; i<3; i++) {// 무한루푸*/                    
            			 Thread.sleep(4000);//4초
            			 int list_size= KopisApiBatch.ApiMain(j,shprfnmfct,"02");
            			 /*DbConnTest.ApiMain(j++);*/
            			 /*threadMessage("DbConnTest.getConnection()");*/
            			 System.out.println("::pageCnt::"+j+"::::::"+shprfnmfct);
            			 if(list_size ==0){System.out.println(":::list_size_break::");break;}// if --end
            			 j++;
            		 }// for end
            			j=1;
            	 }//for end
          
            } catch (InterruptedException e) {
            	System.out.println("::::SimpleThreads::run::error::"+e);
            }
        	
        }
    }

    public static void main(String args[]) throws InterruptedException {

        long patience = 1000 * 60 * 60;
      
        KopisApiBatch.DbDriverLoad();// 디비 드라이버 로딩

        threadMessage("Starting  thread");

        long startTime = System.currentTimeMillis();
        Thread t = new Thread(new MessageLoop()); // 공영예정 thread
        Thread t1 = new Thread(new MessageLoop1()); // 공영예정 thread
        t.start();
        //t1.start();
       
        System.out.println("t.isAli ve()::"+t.isAlive());
        while (t.isAlive()) {
            threadMessage("Still waiting...t.thread.. ");
            t.join(5000); // 5초
            if (((System.currentTimeMillis() - startTime) > patience) && t.isAlive()) {
                threadMessage("Tired of waiting!");
                t.interrupt();
                t.join();
            }
            
           if(t.isAlive()==false) {
        	   t1.start();
        	   System.out.println("t1.start");
        	   }
        }
        
        while (t1.isAlive()) {
            threadMessage("Still waiting...t1.thread.. ");
            t1.join(5000); // 5초
            if (((System.currentTimeMillis() - startTime) > patience) && t1.isAlive()) {
                threadMessage("Tired of waiting!");
                t1.interrupt();
                t1.join();
            }
        }
		long end = System.currentTimeMillis();
		System.out.println( "실행 시간 : " + ( end - startTime )/1000.0 +" 초");
        threadMessage("Finally!");
    }
}
