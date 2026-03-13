package tms.main.listener;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import tms.main.util.Code;

@WebListener
public class FirstContextListener implements ServletContextListener {
	private Log log = LogFactory.getLog(this.getClass());
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		log.info("apply program start!!!");
		
		try {
			Properties properties = new Properties();
			
			FileInputStream fi = new FileInputStream(new File( sce.getServletContext().getRealPath("/WEB-INF/config/db.properties")));
			properties.load(fi);
			
			String dbUserName = properties.getProperty("db.username");
			String encDbPassword = properties.getProperty("db.password");
			String dbUrl = properties.getProperty("db.url");
			String driverName = properties.getProperty("db.driver");
			encDbPassword = encDbPassword.substring(encDbPassword.indexOf("(")+ 1,encDbPassword.lastIndexOf(")"));
			
			StandardPBEStringEncryptor enc = new StandardPBEStringEncryptor();
			enc.setAlgorithm("PBEWithMD5AndDES");
			enc.setPassword("TBM_PASSWORD");
			
			String dbPassword = enc.decrypt(encDbPassword);
			Class.forName(driverName);
			Connection conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			/*
			String codeSql = ""
					+ "SELECT CMMN_BAS_CD , CMMN_BAS_CD_NM , CMMN_DTL_CD , CMMN_DTL_CD_NM  \n" + 
					"FROM E5_CMMN_CD_M\n" + 
					"WHERE\n" + 
					"	DEL_YN  = 'N'\n" + 
					"	AND CMMN_DTL_CD != '_'\n" + 
					"ORDER BY\n" + 
					"	CMMN_BAS_CD , SORT_SRNO ";
					*/
			
			String codeSql = "SELECT \n" + 
					"		       GROUP_CD\n" + 
					"		     , GROUP_NM\n" + 
					"		     , CODE\n" + 
					"			 , CODE_NM\n" + 
					"			 , SPARE_1\n" + 
					"			 , SPARE_2\n" + 
					"			 , SPARE_3\n" + 
					"			 , SPARE_4\n" + 
					"			 , SPARE_5\n" + 
					"			 , SORT_SEQ\n" + 
					"			 , USE_YN\n" + 
					"			 , RMRK\n" + 
					"			 , APLCN_BGNG_YMD\n" + 
					"			 , APPL_END_YMD\n" + 
					"		  FROM E6_CODE_INFO\n" + 
					"		 WHERE\n" + 
					"		   GROUP_YN = 'N'\n" + 
					"		   AND USE_YN = 'Y'\n" + 
					"		   AND DEL_YN = 'N'		 \n" + 
					"		ORDER BY SORT_SEQ, CODE ";
			
			Code code = Code.instance();
			
			PreparedStatement pstmt = conn.prepareStatement(codeSql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("GROUP_CD", rs.getString("GROUP_CD"));
				map.put("GROUP_NM", rs.getString("GROUP_NM"));
				map.put("CODE", rs.getString("CODE"));
				map.put("CODE_NM", rs.getString("CODE_NM"));
				map.put("SPARE_1", rs.getString("SPARE_1"));
				map.put("SPARE_2", rs.getString("SPARE_2"));
				map.put("SPARE_3", rs.getString("SPARE_3"));
				map.put("SPARE_4", rs.getString("SPARE_4"));
				map.put("SPARE_5", rs.getString("SPARE_5"));
				map.put("SORT_SEQ", rs.getObject("SORT_SEQ") == null ? null : ((BigDecimal) rs.getObject("SORT_SEQ")).intValue());
				map.put("RMRK", rs.getString("RMRK"));
				code.add(map);
			}
			rs.close();
			pstmt.close();
			conn.close();
			
			String uploadRootPath = properties.getProperty("fileupload.path");
			if(uploadRootPath == null) {
				uploadRootPath = "/KEDI/upload";
			}
			
			code.setUploadRootPath(uploadRootPath);		
			
		} catch(Exception e) {			
			log.error("apply program start error!!!. System Will Exit.", e);
			System.exit(1);
		}
		//properties.load(inStream);
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		log.info("necte6 program destroy!!!");
	}
}
