package tms.main.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.icu.text.SimpleDateFormat;

import tms.main.exception.ResMsgException;
import tms.main.param.FileParam;
import tms.main.util.Code;
import tms.main.maps.MainMapper;

@Service
public class MainService {	
	private Log log = LogFactory.getLog(this.getClass());
	
	@Autowired MainMapper mainMapper;
	
	List<Map<String,Object>> codeList;
	
	
	public List<Map<String,Object>> getCodeList(String cmmnBasCd) throws Exception {
		
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> map : codeList) {
			if(map.get("CMMN_BAS_CD") != null) {
				if(map.get("CMMN_BAS_CD").equals(cmmnBasCd)) {
					resultList.add(map);
				}
			}
		}
		
		return resultList;
	}	
	
	
	public String uriToFullURL(HttpServletRequest request, String uri) {
		String url = "";
		if(request.isSecure()) {
			url += "https://";
		} else {
			url += "http://";
		}
		
		String host = null;
		String xForwardedHost = request.getHeader("X-Forwarded-Host");
		if(xForwardedHost != null && xForwardedHost.equals("necte.kedi.re.kr")) {
			host = xForwardedHost;
		} else {
			host = request.getHeader("Host");
		}
		url += host;
		
		String contextPath = request.getContextPath();
		url += contextPath;
		
		url += uri;
		
		return url;
	}
	
	public String getUsrIp(HttpServletRequest request) {
		String ip = null;
		String xForwaredIp = request.getHeader("X-Forwarded-For");
		if(xForwaredIp != null ) {
			ip = xForwaredIp;
		} else {
			ip = request.getRemoteAddr();
		}
		
		return ip;
	}
	
		
}
