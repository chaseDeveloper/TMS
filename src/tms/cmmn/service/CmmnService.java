package tms.cmmn.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.ibm.icu.text.SimpleDateFormat;

import tms.cmmn.maps.CmmnMapper;
import tms.main.exception.ResMsgException;
import tms.main.util.Code;

/*import kedi.necte.eva.cmmn.exception.ResMsgException;
import kedi.necte.eva.cmmn.maps.CmmnMapper;
import kedi.necte.eva.cmmn.maps.PeriodMapper;
import kedi.necte.eva.cmmn.util.Code;
import kedi.necte.eva.login.maps.LoginMapper;*/


@Service("cmmnService")
public class CmmnService {	
	private Log log = LogFactory.getLog(this.getClass());
	
	@Autowired CmmnMapper cmmnMapper;
/*	@Autowired LoginMapper loginMapper;
	@Autowired PeriodMapper periodMapper;*/
	List<Map<String,Object>> codeList;
	
	public static final String FILE_EXT_CM =  "pdf,jpg,jpeg,png,gif,hwp,hwpx,xls,xlsx,doc,docx,ppt,pptx,zip";
	
	public static final String MOV_FILE_EXT_CM =  "mp4,mov,wmv,avi";
	
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
	
	public String fileUpload(HttpServletRequest request, MultipartFile file) throws ResMsgException, Exception {
		List<MultipartFile> paramList = new ArrayList<MultipartFile>();
		paramList.add(file);
		
		return fileUpload(request, paramList);
	}
	
	public String fileUpload(HttpServletRequest request, MultipartFile file, String allowExtension) throws ResMsgException, Exception {
		List<MultipartFile> paramList = new ArrayList<MultipartFile>();
		paramList.add(file);
		
		return fileUpload(request, paramList, allowExtension);
	}
	
	public String fileUpload(HttpServletRequest request, List<MultipartFile> fileList) throws ResMsgException, Exception {
		return fileUpload(request, fileList, FILE_EXT_CM);
		
	}
	
	public String movFileUpload(HttpServletRequest request, MultipartFile file) throws ResMsgException, Exception {
		List<MultipartFile> paramList = new ArrayList<MultipartFile>();
		paramList.add(file);
		
		return movFileUpload(request, paramList);
	}
	
	public String movFileUpload(HttpServletRequest request, List<MultipartFile> fileList) throws ResMsgException, Exception {
		return fileUpload(request, fileList, MOV_FILE_EXT_CM);
	}	
	
	public String fileUpload(HttpServletRequest request, List<MultipartFile> fileList, String allowExtension) throws ResMsgException, Exception {
		if(fileList == null) {
			throw new ResMsgException("업로드 할 파일이 없습니다.");
		}
		
		if(fileList.size() == 0) {
			throw new ResMsgException("업로드 할 파일이 없습니다.");
		}
		
		HttpSession session = request.getSession();
		String usrId = (String) session.getAttribute("USR_ID");
		String usrPerm = (String) session.getAttribute("USR_PERM");
		
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		for(int index = 0; index < fileList.size();index++) {
			MultipartFile file = fileList.get(index);
			if(file == null) {
				throw new ResMsgException("업로드 할 파일이 없습니다.");
			}
			
			String ext;
			if(file.getOriginalFilename().lastIndexOf(".") > -1) {
				String[] extensions = allowExtension.split(",");
				ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
				boolean flag = false;
				for(int i=0;i<extensions.length;i++) {
					if(ext.equalsIgnoreCase(extensions[i])) {
						flag = true;					
					}
				}
				
				if(!flag) {
					throw new ResMsgException("허용된 확장자 (" + allowExtension + ") 파일만 업로드 가능합니다.");	
				}
			} else {
				throw new ResMsgException("확장자가 없는 파일은 업로드 할 수 없습니다.");
			}
			
			String fileSeq = String.format("%04d", (index+1));
			String uploadRootPath = Code.instance().getUploadRootPath();
			int curYear = Calendar.getInstance().get(Calendar.YEAR);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String curTime = sf.format(new Date());
			long size = file.getSize();
			
			String fileSize = String.valueOf(size / 1024) + "KB";		
	    	
	    	String filePath = uploadRootPath + "/" + curYear ;
	    	String fileName = fileSeq + "_" + curTime + "." + ext ;

			Map<String,Object> fileParam = new HashMap<String,Object>();
			fileParam.put("fileSeq", fileSeq);
			fileParam.put("filePath", filePath );
			fileParam.put("fileName", fileName );
			fileParam.put("orgFileName", file.getOriginalFilename() );
			fileParam.put("fileSize", fileSize );
			fileParam.put("regstUsrId", usrId);
			fileParam.put("regstUsrPerm", usrPerm);
	
			resultList.add(fileParam);
		}
		
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("list", resultList);
		
		cmmnMapper.insertFile(param);
		
		String fileId = (String) param.get("fileId");
		
		for(int i=0;i<resultList.size();i++) {
			Map<String,Object> fileParam = resultList.get(i);
			MultipartFile file = fileList.get(i);
			
			String orgFileName = (String) fileParam.get("orgFileName");
			if(!file.getOriginalFilename().equals(orgFileName)) {
				throw new ResMsgException("파일이 일치하지 않습니다. 프로그램에 오류가 발생하였으니 관리자에게 문의하여 주십시오.");
			}
			String filePath = (String) fileParam.get("filePath");
			String fileName = (String) fileParam.get("fileName");
			String fullPath = filePath + "/" + fileId + "_" + fileName;
			
			fileCopy(fullPath, file.getInputStream());
		}

		
		return fileId;
	}
	
	public String fileUpload(HttpServletRequest request, String fileId, Integer iFileSeq, MultipartFile file) throws ResMsgException, Exception {

		if(file == null) {
			throw new ResMsgException("업로드 할 파일이 없습니다.");
		}
		
		if(fileId== null) {
			throw new ResMsgException("fileId가 없습니다.");
		}
		
		if(iFileSeq== null) {
			throw new ResMsgException("fileSeq가 없습니다.");
		}
		
		HttpSession session = request.getSession();
		String usrId = (String) session.getAttribute("USR_ID");
		String usrPerm = (String) session.getAttribute("USR_PERM");
		
		String allowExtension = FILE_EXT_CM;
		
		String ext;
		if(file.getOriginalFilename().lastIndexOf(".") > -1) {
			String[] extensions = allowExtension.split(",");
			ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
			boolean flag = false;
			for(int i=0;i<extensions.length;i++) {
				if(ext.equalsIgnoreCase(extensions[i])) {
					flag = true;					
				}
			}
			
			if(!flag) {
				throw new ResMsgException("허용된 확장자 (" + allowExtension + ") 파일만 업로드 가능합니다.");	
			}
		} else {
			throw new ResMsgException("확장자가 없는 파일은 업로드 할 수 없습니다.");
		}
		
		String fileSeqStr = String.format("%04d", iFileSeq);
		String uploadRootPath = Code.instance().getUploadRootPath();
		int curYear = Calendar.getInstance().get(Calendar.YEAR);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String curTime = sf.format(new Date());
		long size = file.getSize();
		
		String fileSize = String.valueOf(size / 1024) + "KB";		
    	
    	String filePath = uploadRootPath + "/" + curYear ;
    	String fileName = fileSeqStr + "_" + curTime + "." + ext ;

		Map<String,Object> fileParam = new HashMap<String,Object>();
		fileParam.put("fileId", fileId);
		fileParam.put("fileSeq", fileSeqStr);
		fileParam.put("filePath", filePath );
		fileParam.put("fileName", fileName );
		fileParam.put("orgFileName", file.getOriginalFilename() );
		fileParam.put("fileSize", fileSize );
		fileParam.put("regstUsrId", usrId);
		fileParam.put("regstUsrPerm", usrPerm);
		
		cmmnMapper.insertFileExistFileId(fileParam);
		

		String fullPath = filePath + "/" + fileId + "_" + fileName;
		
		fileCopy(fullPath, file.getInputStream());

		
		return fileId;
	}
	
	/*
	public String fileUpload(MultipartFile file, String allowExtension, String usrId) throws ResMsgException, Exception {
		if(file == null) {
			throw new ResMsgException("업로드 할 파일이 없습니다.");
		}
		List<MultipartFile> paramList = new ArrayList<MultipartFile>();
		paramList.add(file);
		
		String fileId = fileUpload(paramList, allowExtension, usrId);

		return fileId;
	}
	
	public String fileUpload(List<MultipartFile> fileList, String allowExtension, String usrId) throws ResMsgException, Exception {
		if(fileList == null) {
			throw new ResMsgException("업로드 할 파일이 없습니다.");
		}
		
		if(fileList.size() == 0) {
			throw new ResMsgException("업로드 할 파일이 없습니다.");
		}
		
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		for(int index = 0; index < fileList.size();index++) {
			MultipartFile file = fileList.get(index);
			if(file == null) {
				throw new ResMsgException("업로드 할 파일이 없습니다.");
			}
			
			String ext;
			if(file.getOriginalFilename().lastIndexOf(".") > -1) {
				String[] extensions = allowExtension.split(",");
				ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
				boolean flag = false;
				for(int i=0;i<extensions.length;i++) {
					if(ext.equalsIgnoreCase(extensions[i])) {
						flag = true;					
					}
				}
				
				if(!flag) {
					throw new ResMsgException("허용된 확장자 (" + allowExtension + ") 파일만 업로드 가능합니다.");	
				}
			} else {
				throw new ResMsgException("확장자가 없는 파일은 업로드 할 수 없습니다.");
			}
			
			String fileSeq = String.format("%04d", (index+1));
			String uploadRootPath = Code.instance().getUploadRootPath();
			int curYear = Calendar.getInstance().get(Calendar.YEAR);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String curTime = sf.format(new Date());
			long size = file.getSize();
			
			String fileSize = String.valueOf(size / 1024) + "KB";		
	    	
	    	String filePath = uploadRootPath + "/" + curYear ;
	    	String fileName = fileSeq + "_" + curTime + "." + ext ;

			Map<String,Object> fileParam = new HashMap<String,Object>();
			fileParam.put("fileSeq", fileSeq);
			fileParam.put("filePath", filePath );
			fileParam.put("fileName", fileName );
			fileParam.put("orgFileName", file.getOriginalFilename() );
			fileParam.put("fileSize", fileSize );
			fileParam.put("regstUsrId", usrId);

	
			resultList.add(fileParam);
		}
		
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("list", resultList);
		
		cmmnMapper.insertFile(param);
		
		String fileId = (String) param.get("fileId");
		
		for(int i=0;i<resultList.size();i++) {
			Map<String,Object> fileParam = resultList.get(i);
			MultipartFile file = fileList.get(i);
			
			String orgFileName = (String) fileParam.get("orgFileName");
			if(!file.getOriginalFilename().equals(orgFileName)) {
				throw new ResMsgException("파일이 일치하지 않습니다. 프로그램에 오류가 발생하였으니 관리자에게 문의하여 주십시오.");
			}
			String filePath = (String) fileParam.get("filePath");
			String fileName = (String) fileParam.get("fileName");
			String fullPath = filePath + "/" + fileId + "_" + fileName;
			
			fileCopy(fullPath, file.getInputStream());
		}

		
		return fileId;
	}
	
	public String fileUpload(String fileName, long fileSize,  byte[] fileBinary,  String allowExtension, String usrId) throws ResMsgException, Exception {
		if(fileName == null) {
			throw new ResMsgException("업로드 할 파일이 없습니다.");
		}
		
		if(fileBinary == null) {
			throw new ResMsgException("업로드 할 파일이 없습니다.");
		}
		
		if(fileBinary.length == 0) {
			throw new ResMsgException("업로드 할 파일이 없습니다.");
		}
		
		String ext;
		if(fileName.lastIndexOf(".") > -1) {
			String[] extensions = allowExtension.split(",");
			ext = fileName.substring(fileName.lastIndexOf(".") + 1);
			boolean flag = false;
			for(int i=0;i<extensions.length;i++) {
				if(ext.equalsIgnoreCase(extensions[i])) {
					flag = true;					
				}
			}
			
			if(!flag) {
				throw new ResMsgException("허용된 확장자 (" + allowExtension + ") 파일만 업로드 가능합니다.");	
			}
		} else {
			throw new ResMsgException("확장자가 없는 파일은 업로드 할 수 없습니다.");
		}
		
		String fileSeq = String.format("%04d", 1);
		String uploadRootPath = Code.instance().getUploadRootPath();
		int curYear = Calendar.getInstance().get(Calendar.YEAR);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String curTime = sf.format(new Date());
		
		String size =  String.valueOf(fileSize / 1024)  + "KB";		
    	
    	String filePath = uploadRootPath + "/" + curYear ;
    	String reaFileName = fileSeq + "_" + curTime + "." + ext ;

    	List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		Map<String,Object> fileParam = new HashMap<String,Object>();
		fileParam.put("fileSeq", fileSeq);
		fileParam.put("filePath", filePath );
		fileParam.put("fileName", reaFileName );
		fileParam.put("orgFileName", fileName );
		fileParam.put("fileSize", size );
		fileParam.put("regstUsrId", usrId);

		resultList.add(fileParam);
		
		
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("list", resultList);
		
		cmmnMapper.insertFile(param);
		
		String fileId = (String) param.get("fileId");
		String fullPath = filePath + "/" + fileId + "_" + reaFileName;
		fileParam.put("fullPath", fullPath);
		
		fileCopy(fullPath, new ByteArrayInputStream(fileBinary));
		return fileId;
	}
	
	public String fileUpload(String fileId, Integer iFileSeq, MultipartFile file, String allowExtension, String usrId) throws ResMsgException, Exception {
		if(file == null) {
			throw new ResMsgException("업로드 할 파일이 없습니다.");
		}
		
		if(fileId== null) {
			throw new ResMsgException("fileId가 없습니다.");
		}
		
		if(iFileSeq== null) {
			throw new ResMsgException("fileSeq가 없습니다.");
		}
		
		String ext;
		if(file.getOriginalFilename().lastIndexOf(".") > -1) {
			String[] extensions = allowExtension.split(",");
			ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
			boolean flag = false;
			for(int i=0;i<extensions.length;i++) {
				if(ext.equalsIgnoreCase(extensions[i])) {
					flag = true;					
				}
			}
			
			if(!flag) {
				throw new ResMsgException("허용된 확장자 (" + allowExtension + ") 파일만 업로드 가능합니다.");	
			}
		} else {
			throw new ResMsgException("확장자가 없는 파일은 업로드 할 수 없습니다.");
		}
		
		String fileSeqStr = String.format("%04d", iFileSeq);
		String uploadRootPath = Code.instance().getUploadRootPath();
		int curYear = Calendar.getInstance().get(Calendar.YEAR);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String curTime = sf.format(new Date());
		long size = file.getSize();
		
		String fileSize = String.valueOf(size / 1024) + "KB";		
    	
    	String filePath = uploadRootPath + "/" + curYear ;
    	String fileName = fileSeqStr + "_" + curTime + "." + ext ;

		Map<String,Object> fileParam = new HashMap<String,Object>();
		fileParam.put("fileId", fileId);
		fileParam.put("fileSeq", fileSeqStr);
		fileParam.put("filePath", filePath );
		fileParam.put("fileName", fileName );
		fileParam.put("orgFileName", file.getOriginalFilename() );
		fileParam.put("fileSize", fileSize );
		fileParam.put("regstUsrId", usrId);
		
		cmmnMapper.insertFileExistFileId(fileParam);
		

		String fullPath = filePath + "/" + fileId + "_" + fileName;
		
		fileCopy(fullPath, file.getInputStream());

		
		return fileId;
	}
	*/
	
	public void updateDelFlag(String fileId)  throws ResMsgException, Exception {
		if(fileId == null) throw new ResMsgException("fileId가 없습니다.");
		cmmnMapper.updateDelFlag(fileId);
	}
	
	public void changeFileSeq(String fileId, Integer fileSeq, String originFileSeq) throws ResMsgException, Exception {
		if(fileId == null) throw new ResMsgException("fileId가 없습니다.");
		if(fileSeq == null) throw new ResMsgException("fileSeq가 없습니다.");
		if(originFileSeq == null) throw new ResMsgException("originFileSeq가 없습니다.");
		
		if(String.format("%04d", fileSeq).equals(originFileSeq)) {
			return;
		}
		
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("fileId", fileId);
		param.put("fileSeq", String.format("%04d", fileSeq));
		param.put("originFileSeq", originFileSeq);
		cmmnMapper.updateFileSeq(param);
	}
	
	public void deleteGreaterFileSeq(String fileId, Integer fileSeq) throws ResMsgException, Exception {
		if(fileId == null) throw new ResMsgException("fileId가 없습니다.");
		if(fileSeq == null) throw new ResMsgException("fileSeq가 없습니다.");

		Map<String,Object> param = new HashMap<String,Object>();
		param.put("fileId", fileId);
		param.put("fileSeq", String.format("%04d", fileSeq));
		cmmnMapper.deleteGreaterFileSeq(param);
	}
	
	/*
	public Integer fileUpload(MultipartFile file, String allowExtension, String bizCd, String regstUsrId) throws ResMsgException, Exception {
		Integer fileId = cmmnMapper.getNextFileId();
		return fileUpload(file, fileId, 1, allowExtension, bizCd, regstUsrId);
	}
	
	public Integer fileUpload(MultipartFile file, Integer fileId, Integer fileSeq,  String allowExtension, String bizCd, String regstUsrId) throws ResMsgException, Exception {
		if(file == null) {
			throw new ResMsgException("업로드 할 파일이 없습니다.");
		}
		
		String ext;
		if(file.getOriginalFilename().lastIndexOf(".") > -1) {
			String[] extensions = allowExtension.split(",");
			ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
			boolean flag = false;
			for(int i=0;i<extensions.length;i++) {
				if(ext.equalsIgnoreCase(extensions[i])) {
					flag = true;					
				}
			}
			
			if(!flag) {
				throw new ResMsgException("허용된 확장자 (" + allowExtension + ") 파일만 업로드 가능합니다.");	
			}
		} else {
			throw new ResMsgException("확장자가 없는 파일은 업로드 할 수 없습니다.");
		}
		String uploadRootPath = Code.instance().getUploadRootPath();
		int curYear = Calendar.getInstance().get(Calendar.YEAR);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String curTime = sf.format(new Date());
		long size = file.getSize();
		String fileSize = new Long(size / 1024).toString() + "KB";		
    	
    	String filePath = uploadRootPath + "/" + curYear + "/" + bizCd ;
    	String fileName = bizCd + "_" + curTime + "." + ext ;

		Map<String,Object> fileParam = new HashMap<String,Object>();
		fileParam.put("fileId", fileId);
		fileParam.put("fileSeq", fileSeq);
		fileParam.put("bizCd", bizCd);
		fileParam.put("filePath", filePath );
		fileParam.put("fileName", fileName );
		fileParam.put("orgFileName", file.getOriginalFilename() );
		fileParam.put("fileSize", fileSize );
		fileParam.put("regstUsrId", regstUsrId );

		String fullPath = filePath + "/" + fileName;
		
		
		cmmnMapper.insertFile(fileParam);
		
		fileCopy(fullPath, file.getInputStream());
		
		return fileId;
	}
	*/
	
	public void mp4FileDownload(HttpServletRequest request, HttpServletResponse response, Map<String,Object> fileMap)  throws Exception  {
		String file_path = (String) fileMap.get("FILE_PATH");
		String file_name = (String) fileMap.get("FILE_NAME");
		String full_path = file_path + "/" + file_name;
		log.debug("fullPath: " + full_path);
		File fFilePath = new File(full_path);
		if(fFilePath.exists()) {
			
			String downloadFileName = (String) fileMap.get("ORG_FILE_NAME");
			String userAgent = request.getHeader("User-Agent"); 
			boolean ie = (userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("Trident") > -1 ); 
			if ( ie ) { 
				downloadFileName = new String(downloadFileName.getBytes("windows-949"), "8859_1"); 
			} else if(userAgent.indexOf("Edge") > -1) {
				downloadFileName = new String( URLEncoder.encode(downloadFileName, "UTF-8")); 
			} else { 
				downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "8859_1");  
			}
			
			String range = request.getHeader("Range");
			int i = range.indexOf("=");
			int j = range.indexOf("-");

			long start = Long.parseLong(range.substring(i + 1, j));
			long end = 0;
			if (j < range.length() - 1) {
			  end = Long.parseLong(range.substring(j + 1));
			}

	
			if (end == 0) {
			  end = fFilePath.length() - 1; 
			}

			if (end > fFilePath.length() - 1) {
			  end = fFilePath.length() - 1; 
			}

			response.setHeader("Content-Type" , "application/octet-stream");
			response.setHeader("Content-Transfer-Encoding" , "binary");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + downloadFileName + "\";");
			response.setHeader("Pragma", "no-cache;") ; 
			response.setHeader("Expires", "-1") ; 
			response.setContentLength((int) (end - start + 1));
			//response.setHeader("Content-Range" , "bytes 0-" + (fFilePath.length() -1) + "/" + fFilePath.length());
			response.setHeader("Content-Range" , "bytes " + start + "-" + end + "/" + fFilePath.length());
			response.setHeader("Accept-Ranges" , "bytes");
			response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
			
			String ext = file_name.substring(file_name.lastIndexOf(".") + 1);
			if(ext.equals("ogg")) {
				response.setHeader("Content-Type" , "video/ogg");
			} else {
				response.setHeader("Content-Type" , "video/mp4");
			}
			
			/*
			BufferedInputStream fin = new BufferedInputStream(new FileInputStream(fFilePath));
			BufferedOutputStream outs = new BufferedOutputStream(response.getOutputStream());
			byte[] buffer = new byte[8192];
			int readCnt = 0;
			while((readCnt = fin.read(buffer)) != -1) {
				outs.write(buffer, 0, readCnt);
			}
			outs.close();
			fin.close();
			*/
			
			RandomAccessFile rf = new RandomAccessFile(fFilePath, "r");
			rf.seek(start);
			byte[] buffer = new byte[1024];
			int num = 0;
			ServletOutputStream out = response.getOutputStream();
			
			try {
				while (start < end && (num = rf.read(buffer)) != -1) {
				  out.write(buffer, 0, num);
				  out.flush();
				  start += 1024;
				}
			} catch(IOException e) {
				if(!e.getCause().toString().equals("java.net.SocketException: Connection reset")) {
					throw e;					
				} 				
			} finally {
				if(out != null) {
					out.close();
				}
				if(rf != null) {
					rf.close();
				}
			}
			
		} else {
			ServletOutputStream out =response.getOutputStream();
			out.println("<script language=javascript>");
			String msg = new String("alert('파일이 존재하지 않습니다.');");
			out.write(msg.getBytes("UTF-8"));
			out.println("</script>");
		}
		
	}
	
	public void fileDownload(HttpServletRequest request, HttpServletResponse response, Map<String,Object> fileMap)  throws Exception  {
		String file_path = (String) fileMap.get("FILE_PATH");
		String file_name = (String) fileMap.get("FILE_NAME");
		String full_path = file_path + "/" + file_name;
		String downloadFileName = (String) fileMap.get("ORG_FILE_NAME");
		
		fileDownload(request, response, downloadFileName, full_path);
	}
	
	public void fileDownload(HttpServletRequest request, HttpServletResponse response, String downloadFileName, String fullPath)  throws Exception  {
		if(fullPath == null) throw new ResMsgException("파일 다운로드 PATH 정보가 없습니다.");
		log.info("fullPath: " + fullPath);
		File fFilePath = new File(fullPath);
		if(fFilePath.exists()) {
			
			String userAgent = request.getHeader("User-Agent"); 
			boolean ie = (userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("Trident") > -1 ); 
			if ( ie ) { 
				downloadFileName = new String(downloadFileName.getBytes("windows-949"), "8859_1"); 
			} else if(userAgent.indexOf("Edge") > -1) {
				downloadFileName = new String( URLEncoder.encode(downloadFileName, "UTF-8")); 
			} else { 
				downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "8859_1");  
			}
			String ext = "";
			if(downloadFileName.lastIndexOf(".") != -1) {
				ext = downloadFileName.substring(downloadFileName.lastIndexOf(".") + 1);
			}
			/*
			if(ext.equalsIgnoreCase("pdf")) {
				response.setHeader("Content-Type" , "application/pdf");
				response.setHeader("Content-Disposition", "inline;filename=\"" + downloadFileName + "\";");				
				response.setHeader("Content-Transfer-Encoding" , "binary");	
			} else if(ext.equalsIgnoreCase("hwp")
					|| ext.equalsIgnoreCase("hwpx")) {
				response.setHeader("Content-Type" , "application/vnd.hancom.hwp");
				response.setHeader("Content-Disposition", "inline;filename=\"" + downloadFileName + "\";");				
				response.setHeader("Content-Transfer-Encoding" , "binary");	
			} else {					
				response.setHeader("Content-Type" , "application/octet-stream");
				response.setHeader("Content-Disposition", "attachment;filename=\"" + downloadFileName + "\";");
				response.setHeader("Content-Transfer-Encoding" , "binary");				
			}
			*/
			response.setHeader("Content-Type" , "application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + downloadFileName + "\";");
			response.setHeader("Content-Transfer-Encoding" , "binary");		
			
			response.setHeader("Content-Length", String.valueOf(fFilePath.length()));
			response.setHeader("Pragma", "no-cache;") ; 
			response.setHeader("Expires", "-1") ; 
			//response.setContentLength((int)fFilePath.length());
			
			BufferedInputStream fin = null;
			BufferedOutputStream outs = null;
			try {
				fin = new BufferedInputStream(new FileInputStream(fFilePath));
				outs = new BufferedOutputStream(response.getOutputStream());
				byte[] buffer = new byte[8192];
				int readCnt = 0;
				while((readCnt = fin.read(buffer)) != -1) {
					outs.write(buffer, 0, readCnt);
				}
			} catch(ClientAbortException e) {
		
			} catch(Exception e) {
				throw e;
			} finally {			
				try {
					if(outs != null)
						outs.close();
					if(fin != null)
						fin.close();	
				} catch(IOException e) {
					log.error("클라이언트에 의한 취소");
				}
			}
			
		} else {
			ServletOutputStream out =response.getOutputStream();
			out.println("<script language=javascript>");
			String msg = new String("alert('파일이 존재하지 않습니다.');");
			out.write(msg.getBytes("UTF-8"));
			out.println("</script>");
		}
	}
	
	public void fileDownload(HttpServletRequest request, HttpServletResponse response, ByteArrayOutputStream bout, String downloadFileName)  throws Exception  {
		String userAgent = request.getHeader("User-Agent"); 
		boolean ie = (userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("Trident") > -1 ); 
		if ( ie ) { 
			downloadFileName = new String(downloadFileName.getBytes("windows-949"), "8859_1"); 
		} else if(userAgent.indexOf("Edge") > -1) {
			downloadFileName = new String( URLEncoder.encode(downloadFileName, "UTF-8")); 
		} else { 
			downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "8859_1");  
		}
		
		response.setHeader("Content-Type" , "application/octet-stream");
		response.setHeader("Content-Transfer-Encoding" , "binary");
		response.setHeader("Content-Disposition", "attachment;filename=\"" + downloadFileName + "\";");
		response.setHeader("Pragma", "no-cache;") ; 
		response.setHeader("Expires", "-1") ; 
		response.setContentLength(bout.size());
	
		BufferedOutputStream outs = null;
		try {
			outs = new BufferedOutputStream(response.getOutputStream());
			outs.write(bout.toByteArray());
		} finally {
			if(outs != null)
				outs.close();
		}				
	}
	
	public void pdfDownload(HttpServletRequest request, HttpServletResponse response, String downloadFileName, String fullPath)  throws Exception  {
		if(fullPath == null) throw new ResMsgException("파일 다운로드 PATH 정보가 없습니다.");
		log.info("fullPath: " + fullPath);
		File fFilePath = new File(fullPath);
		if(fFilePath.exists()) {
			
			String userAgent = request.getHeader("User-Agent"); 
			boolean ie = (userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("Trident") > -1 ); 
			if ( ie ) { 
				downloadFileName = new String(downloadFileName.getBytes("windows-949"), "8859_1"); 
			} else if(userAgent.indexOf("Edge") > -1) {
				downloadFileName = new String( URLEncoder.encode(downloadFileName, "UTF-8")); 
			} else { 
				downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "8859_1");  
			}
			String ext = "";
			if(downloadFileName.lastIndexOf(".") != -1) {
				ext = downloadFileName.substring(downloadFileName.lastIndexOf(".") + 1);
			}
			
			if(ext.equalsIgnoreCase("pdf")) {
				response.setHeader("Content-Type" , "application/pdf");
				response.setHeader("Content-Disposition", "inline;filename=\"" + downloadFileName + "\";");				
				response.setHeader("Content-Transfer-Encoding" , "binary");	
			} else {					
				response.setHeader("Content-Type" , "application/octet-stream");
				response.setHeader("Content-Disposition", "attachment;filename=\"" + downloadFileName + "\";");
				response.setHeader("Content-Transfer-Encoding" , "binary");				
			}
			
			
			response.setHeader("Content-Length", String.valueOf(fFilePath.length()));
			response.setHeader("Pragma", "no-cache;") ; 
			response.setHeader("Expires", "-1") ; 
			//response.setContentLength((int)fFilePath.length());
			
			BufferedInputStream fin = null;
			BufferedOutputStream outs = null;
			try {
				fin = new BufferedInputStream(new FileInputStream(fFilePath));
				outs = new BufferedOutputStream(response.getOutputStream());
				byte[] buffer = new byte[8192];
				int readCnt = 0;
				while((readCnt = fin.read(buffer)) != -1) {
					outs.write(buffer, 0, readCnt);
				}
			} catch(ClientAbortException e) {
		
			} catch(Exception e) {
				throw e;
			} finally {			
				try {
					if(outs != null)
						outs.close();
					if(fin != null)
						fin.close();	
				} catch(IOException e) {
					log.error("클라이언트에 의한 취소");
				}
			}
			
		} else {
			/*			
			ServletOutputStream out =response.getOutputStream();
			
			out.println("<script language=javascript>");
			String msg = new String("alert('파일이 존재하지 않습니다.');");
			out.write(msg.getBytes("UTF-8"));
			out.println("</script>");
			*/
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/cmmn/blank.jsp");
			request.setAttribute("msg", "파일이 존재하지 않습니다.");
			rd.forward(request, response);
		}
	}
	
	public void fileDelete(String fileId) throws ResMsgException, Exception {
		fileDelete(fileId, "0001");
	}
	
	public void fileDelete(String fileId, String fileSeq) throws ResMsgException, Exception {
		if(fileId == null) return;
		if(fileSeq == null) return;
		
		Map<String,Object> fileParam = new HashMap<String,Object>();
		fileParam.put("fileId", fileId);
		fileParam.put("fileSeq", fileSeq);

		Map<String,Object> fileInfo = cmmnMapper.getFileInfo(fileParam);
		fileDelete(fileId, fileSeq, fileInfo);				
	}
	
	public void fileDelete(String fileId, String fileSeq, Map<String,Object> fileInfo) throws ResMsgException, Exception {
		if(fileId == null) return;
		if(fileSeq == null) return;
		if(fileInfo == null) return;
		
		Map<String,Object> fileParam = new HashMap<String,Object>();
		fileParam.put("fileId", fileId);
		fileParam.put("fileSeq", fileSeq);
		
		String filePath = (String) fileInfo.get("FILE_PATH");
		String fileName = (String) fileInfo.get("FILE_NAME");
		String fullPath = filePath + "/" + fileName;
		File f = new File(fullPath);
		if(f.exists()) {
			f.delete();
		}
		
		cmmnMapper.deleteFile(fileParam);
	}
	
	public String getWebSocketFullContextPath(HttpServletRequest request) {
		String schemeHost = "ws://";

		String host = null;
		String xForwardedHost = request.getHeader("X-Forwarded-Host");
		if(xForwardedHost != null && xForwardedHost.equals("necte.kedi.re.kr")) {
			host = xForwardedHost;
		} else {
			host = request.getHeader("Host");
		}
		schemeHost += host + request.getContextPath();
		
		return schemeHost;
	}
	
	public String getSchemeHost(HttpServletRequest request) {
		String schemeHost = "";
		if(request.isSecure()) {
			schemeHost += "https://";
		} else {
			schemeHost += "http://";
		}
		
		String host = null;
		String xForwardedHost = request.getHeader("X-Forwarded-Host");
		if(xForwardedHost != null && xForwardedHost.equals("necte.kedi.re.kr")) {
			host = xForwardedHost;
		} else {
			host = request.getHeader("Host");
		}
		schemeHost += host;
		
		return schemeHost;
	}
	
	public String getFullContextPath(HttpServletRequest request) {
		String contextPath = getSchemeHost(request) + request.getContextPath();
		return contextPath;
	}
	
	public String uriToFullURL(HttpServletRequest request, String uri) {
		String url = getFullContextPath(request);
		
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
		
		if(ip.indexOf(",") > -1) {
			String[] ipArr = ip.split(",");
			ip = ipArr[0];
		}
		
		return ip;
	}
	
	public Map<String,Object> fileCopy(String fileFullPath, InputStream in)
            throws IOException {
		
		String fullPath = "";
		String filePath = "";
		String realFileName = "";
		
		File f = new File(fileFullPath);
		
		//디렉토리가 없으면 디렉토리 생성		
		File path = new File(f.getParent());
		if(!path.exists()) {
			path.mkdirs();
		}
		
		if(f.exists()) {
			//기존에 동일한 이름의 파일이 있는 경우 숫자를 붙인다.
			String fileName = f.getAbsolutePath().replace(path.getAbsolutePath(), "").substring(1);
			log.info("upload FileName: " + fileName);
			String ext = "";
			
			String parentPath = fileFullPath.substring(0, fileFullPath.lastIndexOf("/"));
			String preFileName = fileName;
			if(fileName.lastIndexOf(".") > -1) {
				ext = fileName.substring(fileName.lastIndexOf(".") + 1);
				preFileName = fileName.substring(0, fileName.lastIndexOf("."));
			} 
			for(int i=1;i<=1000;i++) {
				fileName = preFileName + "(" + i + ")." + ext;
				File tempFile = new File(parentPath + "/" +  fileName);
				if(tempFile.exists()) {
					continue;
				} else {
					fullPath = parentPath + "/" +  fileName;
					realFileName = fileName;
					filePath = parentPath;
					break;
				}
			}
		} else {
			String parentPath = fileFullPath.substring(0, fileFullPath.lastIndexOf("/") );
			String fileName = fileFullPath.substring(fileFullPath.lastIndexOf("/") + 1);
			
			fullPath = parentPath + "/" +  fileName;
			realFileName = fileName;
			filePath = parentPath;
		}

		Files.copy(in, Paths.get(fullPath));
		
		Map<String,Object> fileMap = new HashMap<String,Object>();
		fileMap.put("fullPath" , fullPath);
		fileMap.put("realFileName" , realFileName);
		fileMap.put("filePath" , filePath);
		return fileMap;
    }
	
	
	public void addExcelDataConstraint(XSSFSheet sheet, XSSFSheet validSheet, CellStyle headerStyle, CellStyle contentStyle
			, String validHeaderName, int validColumnIndex 
			, List<Map<String,Object>> dataList, String dataColumnName
			, int applyStartRow, int applyEndRow, int applyColumnIndex )
            throws Exception {
		
		XSSFRow validRow0 = validSheet.getRow(0);
		if(validRow0 == null) {
			validRow0 = validSheet.createRow(0);
		}
		
    	XSSFCell validHeader = validRow0.createCell(validColumnIndex);
    	validHeader.setCellStyle(headerStyle);
    	validHeader.setCellValue(validHeaderName);
    	
    	int startRow = 1;
    	    	
    	int dataIndex = 0;
    	for(Map<String,Object> tranCorsCdMap: dataList) {
    		String data = (String) tranCorsCdMap.get(dataColumnName);
    		XSSFRow validRow = validSheet.getRow(dataIndex + startRow);
    		if(validRow == null) {
    			validRow = validSheet.createRow(dataIndex + startRow);
    		}
    		
    		XSSFCell validCell = validRow.createCell(validColumnIndex);
    		validCell.setCellStyle(contentStyle);
    		validCell.setCellValue(data);
    		dataIndex++;
    	}
    	
    	if(validColumnIndex > 16384) {
    		throw new ResMsgException("최대컬럼수보다 큽니다");
    	}
    	
    	StringBuilder columnLetterBuilder = new StringBuilder();
    	int vc = validColumnIndex + 1;
    	if (vc > 26 + (26*26)) {
    		int first =  vc / (26*26);
    		int remain = vc - ((26*26) * first);
    		int sec =  remain / 26;
    		remain = remain - (26 * sec);
    		int third =  remain;
    		columnLetterBuilder.append((char) (65+first-1)).append((char) (65+sec-1)).append((char) (65+third-1));
    	} else if(vc > 26 ) {
    		int sec =  vc / 26;
    		int remain = vc - (26 * sec);
    		int third =  remain;
    		columnLetterBuilder.append((char) (65+sec-1)).append((char) (65+third-1));
    	} else {
    		columnLetterBuilder.append((char) (65+vc-1));
    	}
    	String columnLetter = columnLetterBuilder.toString();
    	
    	
		DataValidationHelper validationHelper=new XSSFDataValidationHelper(sheet);
		DataValidationConstraint constraint1 = validationHelper.createFormulaListConstraint("데이터제약조건!$" + columnLetter + "$" + (startRow + 1) + ":$" + columnLetter + "$" + (dataIndex+ startRow));
    	CellRangeAddressList addressList1 = new CellRangeAddressList(applyStartRow, applyEndRow, applyColumnIndex, applyColumnIndex);
    	
    	
    	DataValidation dataValidation1 = validationHelper.createValidation(constraint1, addressList1);
        	    	
    	dataValidation1.setEmptyCellAllowed(false);      
    	dataValidation1.setShowPromptBox(true);         //설명 표시
    	dataValidation1.setShowErrorBox(true);
    	
    	sheet.addValidationData(dataValidation1);
    }
	
	public void excelDownload(HttpServletRequest request, HttpServletResponse response, 
			String colModel
			) throws Exception {
		ServletOutputStream out = response.getOutputStream();
		SXSSFWorkbook wb = null;
		SXSSFWorkbook sxssfWorkbook = null;
		try {
		
			//엑셀파일 생성
			wb = new SXSSFWorkbook();
			SXSSFSheet originsheet = wb.createSheet("테스트");
			
			
			//스타일 생성
			CellStyle cellstyle = wb.createCellStyle();
			cellstyle.setBorderBottom(BorderStyle.THIN);
			cellstyle.setBorderLeft(BorderStyle.THIN);
			cellstyle.setBorderTop(BorderStyle.THIN);
			cellstyle.setBorderRight(BorderStyle.THIN);
			Font font = wb.createFont();
			font.setFontHeightInPoints((short) 10);
			font.setFontName("나눔고딕");
			cellstyle.setFont(font);
			cellstyle.setVerticalAlignment(VerticalAlignment.CENTER);
			
			CellStyle cellstyleLeft = wb.createCellStyle();
			cellstyleLeft.setBorderBottom(BorderStyle.THIN);
			cellstyleLeft.setBorderLeft(BorderStyle.THIN);
			cellstyleLeft.setBorderTop(BorderStyle.THIN);
			cellstyleLeft.setBorderRight(BorderStyle.THIN);
			cellstyleLeft.setFont(font);
			cellstyleLeft.setVerticalAlignment(VerticalAlignment.CENTER);
			cellstyleLeft.setAlignment(HorizontalAlignment.LEFT);
			
			CellStyle cellstyleRight = wb.createCellStyle();
			cellstyleRight.setBorderBottom(BorderStyle.THIN);
			cellstyleRight.setBorderLeft(BorderStyle.THIN);
			cellstyleRight.setBorderTop(BorderStyle.THIN);
			cellstyleRight.setBorderRight(BorderStyle.THIN);
			cellstyleRight.setFont(font);
			cellstyleRight.setVerticalAlignment(VerticalAlignment.CENTER);
			cellstyleRight.setAlignment(HorizontalAlignment.RIGHT);
			
			CellStyle cellstyleWrapText = wb.createCellStyle();
			cellstyleWrapText.setBorderBottom(BorderStyle.THIN);
			cellstyleWrapText.setBorderLeft(BorderStyle.THIN);
			cellstyleWrapText.setBorderTop(BorderStyle.THIN);
			cellstyleWrapText.setBorderRight(BorderStyle.THIN);
			cellstyleWrapText.setFont(font);
			cellstyleWrapText.setWrapText(true);
			cellstyleWrapText.setVerticalAlignment(VerticalAlignment.CENTER);
			cellstyleWrapText.setAlignment(HorizontalAlignment.CENTER);
			
			CellStyle cellstyleWrapTextLeft = wb.createCellStyle();
			cellstyleWrapTextLeft.setBorderBottom(BorderStyle.THIN);
			cellstyleWrapTextLeft.setBorderLeft(BorderStyle.THIN);
			cellstyleWrapTextLeft.setBorderTop(BorderStyle.THIN);
			cellstyleWrapTextLeft.setBorderRight(BorderStyle.THIN);
			cellstyleWrapTextLeft.setFont(font);
			cellstyleWrapTextLeft.setWrapText(true);
			cellstyleWrapTextLeft.setVerticalAlignment(VerticalAlignment.CENTER);
			cellstyleWrapTextLeft.setAlignment(HorizontalAlignment.LEFT);
			
			CellStyle cellstyleWrapTextRight = wb.createCellStyle();
			cellstyleWrapTextRight.setBorderBottom(BorderStyle.THIN);
			cellstyleWrapTextRight.setBorderLeft(BorderStyle.THIN);
			cellstyleWrapTextRight.setBorderTop(BorderStyle.THIN);
			cellstyleWrapTextRight.setBorderRight(BorderStyle.THIN);
			cellstyleWrapTextRight.setFont(font);
			cellstyleWrapTextRight.setWrapText(true);
			cellstyleWrapTextRight.setVerticalAlignment(VerticalAlignment.CENTER);
			cellstyleWrapTextRight.setAlignment(HorizontalAlignment.RIGHT);
			
			CellStyle headerstyle = wb.createCellStyle();
			headerstyle.setBorderBottom(BorderStyle.THIN);
			headerstyle.setBorderLeft(BorderStyle.THIN);
			headerstyle.setBorderTop(BorderStyle.THIN);
			headerstyle.setBorderRight(BorderStyle.THIN);
			headerstyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
			headerstyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerstyle.setFont(font);
			headerstyle.setWrapText(true);
			headerstyle.setAlignment(HorizontalAlignment.CENTER);
			headerstyle.setVerticalAlignment(VerticalAlignment.CENTER);
			
			String sheetName = "테스트";
			/*
			if(excelVOList == null
					|| excelVOList.size()== 0) {
				throw new Exception("엑셀 컬럼정보가 없습니다");
			}
			
			int columnCnt = excelVOList.size();
			
			XSSFRow firstrow = null;
			if(groupHeaderList == null) {
				firstrow = originsheet.createRow(0);
			} else {
				firstrow = originsheet.createRow(0 + groupHeaderList.size());
			}			
			
			XSSFCell[] firstrowCell = new XSSFCell[columnCnt];
			for(int i=0;i<columnCnt;i++) {
				ExcelVO vo = excelVOList.get(i);
				firstrowCell[i] = firstrow.createCell(i);
				firstrowCell[i].setCellStyle(headerstyle);
				originsheet.setColumnWidth(i, (vo.getColWidth() * 256) + 163);
				firstrowCell[i].setCellValue(vo.getDisplayName());
			}
			
			if(groupHeaderList != null) {
				int index = 1;
				for(List<Map<String,String>> groupHeaderColumnList : groupHeaderList) {
					int groupHeaderRowNum = groupHeaderList.size() - index;
					XSSFRow groupHeaderRow = originsheet.createRow(groupHeaderRowNum);
					for(int i=0;i<columnCnt;i++) {
						groupHeaderRow.createCell(i);
					}
					
					for(int i=0;i<columnCnt;i++) {
						
						XSSFCell headerCell = firstrow.getCell(i);
						boolean flag = false;
						int colSpanCnt = 0;
						for(Map<String,String> groupHeaderColumnMap: groupHeaderColumnList) {
							if( i == Integer.parseInt(groupHeaderColumnMap.get("startColumnIndex")) ) {
								Integer colSpan = Integer.parseInt(groupHeaderColumnMap.get("numberOfColumns"));
								String titleText = groupHeaderColumnMap.get("titleText");
								originsheet.addMergedRegion(new CellRangeAddress(groupHeaderRowNum, groupHeaderRowNum, i, i+ colSpan - 1));
								XSSFCell groupHeaderCell = groupHeaderRow.getCell(i);
								groupHeaderCell.setCellValue(titleText);
								groupHeaderCell.setCellStyle(headerstyle);
								colSpanCnt = colSpan;
								flag = true;
							}
						}
						
						if(flag) {
							i = i + colSpanCnt -1;
							continue;
						} else {
							String cellValue = headerCell.getStringCellValue();						
							originsheet.addMergedRegion(new CellRangeAddress(groupHeaderRowNum, groupHeaderList.size(), i, i));
							groupHeaderRow.getCell(i).setCellValue(cellValue);
							groupHeaderRow.getCell(i).setCellStyle(headerstyle);
						}
					}
					
					index++;
				}
			}
					
			//sxssfWorkbook = new SXSSFWorkbook(wb, 1200);
		   // SXSSFSheet sheet = (SXSSFSheet) sxssfWorkbook.getSheetAt(0);

		    int firstIndex = 0;
		    if(groupHeaderList != null) {
		    	firstIndex = groupHeaderList.size();
		    }
		    
		    int rowsize = list.size();
	        int startrow = firstIndex + 1;
			       
			for(int row=startrow + 0;row <rowsize + startrow; row++) {
				Map<String,Object> rowMap = list.get(row - startrow);
				XSSFRow xssfrow = (XSSFRow) originsheet.createRow(row);
				
				for(int col=0;col<columnCnt;col++) {
					ExcelVO vo = excelVOList.get(col);
					Object obj = rowMap.get(vo.getColName());
					XSSFCell cell = (XSSFCell) xssfrow.createCell(col);
					if(obj != null) {
						if(obj instanceof java.lang.String) {
							String temp = (String) obj;							
							cell.setCellValue(temp);
							
						} else if(obj instanceof java.math.BigDecimal) {
							BigDecimal b_obj = (BigDecimal) obj;
							if(b_obj.doubleValue() == b_obj.intValue()) {
								cell.setCellValue(b_obj.intValue() );
							} else {
								cell.setCellValue(b_obj.doubleValue() );
							}
							
						} else if(obj instanceof java.lang.Integer) {
							Integer b_obj = (Integer) obj;
							cell.setCellValue(b_obj);
						}
					} 
					if(vo.isWrapText()) {
						if(vo.getAlign().equals("left")) {
							cell.setCellStyle(cellstyleWrapTextLeft);
						} else if(vo.getAlign().equals("right")) {
							cell.setCellStyle(cellstyleWrapTextRight);
						} else {
							cell.setCellStyle(cellstyleWrapText);
						}
						
					} else {
						if(vo.getAlign().equals("left")) {
							cell.setCellStyle(cellstyleLeft);
						} else if(vo.getAlign().equals("right")) {
							cell.setCellStyle(cellstyleRight);
						} else {
							cell.setCellStyle(cellstyle);
						}
					}
					
					
				}
				
			}	
			*/
			
			SXSSFRow row = originsheet.createRow(0);
			SXSSFCell cell = row.createCell(0);
			cell.setCellStyle(headerstyle);
			cell.setCellValue("테스트");;
			
			
//			
//			Gson gson = new Gson();
//			
//			Type listType = new TypeToken<List<Map<String,Object>>>() {}.getType();		
//        	List<Map<String,Object>> colModelList = gson.fromJson(colModel, listType);
        	
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
			String today = sf.format(new Date());
	
			String downloadFileName = sheetName + "_" + today + ".xlsx";
			String userAgent = request.getHeader("User-Agent"); 
			boolean ie = (userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("Trident") > -1 ); 
			if ( ie ) { 
				log.debug("ie download");
				downloadFileName = new String(downloadFileName.getBytes("windows-949"), "8859_1"); 
			} else if(userAgent.indexOf("Edge") > -1) {
				downloadFileName = new String( URLEncoder.encode(downloadFileName, "UTF-8")); 
			} else { 
				downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "8859_1");  
			}
					
					
			response.setHeader("Content-Type" , "application/octet-stream");
			response.setHeader("Content-Transfer-Encoding" , "binary");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + downloadFileName + "\";");
			response.setHeader("Pragma", "no-cache;") ; 
			response.setHeader("Expires", "-1") ;
			
			Cookie[] cookies = request.getCookies();
			if(cookies != null && cookies.length > 0) {
				for(int i=0;i<cookies.length;i++) {
					log.debug("cookieName : " + cookies[i].getName());
					if(cookies[i].getName().equals("downloadpopup")) {
						  cookies[i].setMaxAge(0);
						  response.addCookie(cookies[i]);
						  break;
					}
				}
			}
			
			wb.write(out);
		} catch(Exception e) {
			log.error(e.getMessage());
			response.setContentType("text/html;charset=UTF-8");
			String msg = "<script>alert(\"" + e.getMessage() + "\");";		
			msg += "</script>";			
			out.write(msg.getBytes("UTF-8"));			
		} finally {
			Cookie[] cookies = request.getCookies();
			if(cookies != null && cookies.length > 0) {
				for(int i=0;i<cookies.length;i++) {
					log.debug("cookieName : " + cookies[i].getName());
					if(cookies[i].getName().equals("downloadpopup")) {
						  cookies[i].setMaxAge(0);
						  response.addCookie(cookies[i]);
						  break;
					}
				}
			}
			
			if(sxssfWorkbook != null) 
				sxssfWorkbook.close();
			if(wb != null)
				wb.close();
			if(out != null)
				out.close();
			out.close();
		}
	}
	
	public String getHash(String curPw) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(curPw.getBytes());
        byte[] encCurPwdArr = md.digest();
        String encCurPw = "";
        for(int i=0;i<encCurPwdArr.length;i++) {
        	encCurPw += String.format("%02x", encCurPwdArr[i]);
        }
        log.info("encCurPw: " + encCurPw);
        
        return encCurPw;
	}
	
	public Integer getIntNumberFromMap(Map<String,Object> map , String paramName) throws ResMsgException {
		
		if(map == null ) {
			return null;
		}
		
		if( map.get(paramName) == null || map.get(paramName).equals("")) {
			return null;
		}
		
		Integer iAdmCnt1 = null;
		if(map.get(paramName) instanceof String) {
			String admCnt1 = (String) map.get(paramName);
			if(!admCnt1.matches("^\\d{1,4}$")) {
				throw new ResMsgException("4자리 이하 숫자만 입력 가능합니다.");
			}
			iAdmCnt1 = Integer.parseInt(admCnt1);
		} else if(map.get(paramName) instanceof Integer) {
			iAdmCnt1 = (Integer) map.get(paramName); 
		} else if(map.get(paramName) instanceof Double) {
			Double admCnt1 = (Double) map.get(paramName);
			iAdmCnt1 = admCnt1.intValue();
		}
		

		return iAdmCnt1;
	}
	
	public Map<String, Object> paging(int curPage, int totalCnt) {
		int pageRecordSize = 10;
		int pageSize = 10;
		
		int totalPage = ((totalCnt -1) / pageSize) + 1;
		int beginPage = ((curPage - 1) / pageSize) * pageSize + 1;
		int endPage = (beginPage + pageSize -1) < totalPage ? (beginPage + pageSize -1) : totalPage ;
		int nextPage = (beginPage + pageSize) < totalPage ? (beginPage + pageSize ) : totalPage ;
		int prevPage = beginPage == 1 ? 1 : beginPage -1;
		int beginRecord = (curPage -1) * pageRecordSize + 1;
		int endRecord = (beginRecord + pageRecordSize -1) < totalCnt ? (beginRecord + pageRecordSize -1) : totalCnt;
		
		Map<String,Object> pageMap = new HashMap<String,Object>();
		pageMap.put("totalPage", totalPage);
		pageMap.put("beginPage", beginPage);
		pageMap.put("endPage", endPage);
		pageMap.put("prevPage", prevPage);
		pageMap.put("nextPage", nextPage);
		pageMap.put("beginRecord", beginRecord);
		pageMap.put("endRecord", endRecord);
		pageMap.put("curPage", curPage);
		
		return pageMap;
		
	}
	
	public String insertHis(HttpServletRequest req, String resultType, String menuNo, String menuNm, String workType, String funcNm) {
		return insertHis(req, resultType,menuNo, menuNm, workType, funcNm, null, null);
	}
	
	public String insertHis(HttpServletRequest req, String resultType, String menuNo, String menuNm, String workType, String funcNm, String rmrk) {
		return insertHis(req, resultType,menuNo, menuNm, workType, funcNm, rmrk, null);
	}
	
	public String insertHis(HttpServletRequest req, String resultType, String menuNo, String menuNm, String workType, String funcNm, String rmrk, String link) {
		return insertHis(req, resultType,menuNo, menuNm, workType, funcNm, rmrk, link, null);
	}
	
	public String insertHis(HttpServletRequest req, String resultType, String menuNo, String menuNm, String workType, String funcNm, String rmrk, String link, String pravacyInfo) {
		HttpSession session = req.getSession();
		return insertHis(req, resultType,menuNo, menuNm, workType, funcNm, rmrk
				, link , pravacyInfo , (String) session.getAttribute("USR_NO")
				, (String) session.getAttribute("USR_ID")
				, (String) session.getAttribute("USR_NM"));
	}
	
	public String insertHis(HttpServletRequest req, String resultType, String menuNo, String menuNm, String workType
			, String funcNm, String rmrk, String link, String pravacyInfo, String usrNo, String usrId, String usrNm) {
		HttpSession session = req.getSession();
		String univNm = (String) session.getAttribute("UNIV_NM");
		
		Map<String,Object> hisParam = new HashMap<String,Object>();
		hisParam.put("RESULT_TYPE"	, resultType);
		hisParam.put("MENU_NO"	, menuNo);
		hisParam.put("MENU_NM"	, menuNm);
		hisParam.put("WORK_TYPE"	, workType);
		hisParam.put("FUNC_NM"	, funcNm);
		hisParam.put("UNIV_NM"	, univNm);
		hisParam.put("RMRK"	, rmrk);
		if(pravacyInfo == null || pravacyInfo.equals("")) {
			hisParam.put("PRIVACY_YN"	, "N");
		} else {
			hisParam.put("PRIVACY_YN"	, "Y");
		}
		hisParam.put("LINK"	, link);
		hisParam.put("PRIVACY_INFO"	, pravacyInfo);
		hisParam.put("sessUsrNo", usrNo);	
		hisParam.put("sessUsrId", usrId);	
		hisParam.put("sessUsrNm", usrNm);
		hisParam.put("usrIp", getUsrIp(req));
		hisParam.put("sessionId", session.getId());
		hisParam.put("uri", req.getRequestURI());
	
		
		cmmnMapper.insertHis(hisParam);
		
		return (String) hisParam.get("HIS_NO");
	}
	
	public void updateHis(HttpServletRequest request, String resultType) {
		updateHis(request, resultType, null);
	}
	
	public void updateHis(HttpServletRequest request, String resultType, String rmrk) {
		updateHis(request, resultType, rmrk, null);
	}
			
	public void updateHis(HttpServletRequest request, String resultType, String rmrk, String resultValue) {
		String hisNo = (String) request.getAttribute("HIS_NO");
		if(hisNo != null) {
			HttpSession session = request.getSession();
			String usrId = (String) session.getAttribute("USR_ID");
			String usrNo = (String) session.getAttribute("USR_NO");
			String usrNm = (String) session.getAttribute("USR_NM");
			
			Map<String,Object> hisParam = new HashMap<String,Object>();
			hisParam.put("HIS_NO"	, hisNo);
			hisParam.put("RESULT_TYPE"	, resultType);
			Map<String, String[]> parameters = request.getParameterMap();
			if(parameters != null) {
				Gson gson = new Gson();
				hisParam.put("PARAM"	, gson.toJson(parameters));
			}
			hisParam.put("RMRK"	, rmrk);
			hisParam.put("RESULT"	, resultValue);
			hisParam.put("usrId"	, usrId);
			hisParam.put("usrNm"	, usrNm);
			hisParam.put("usrNo"	, usrNo);
			cmmnMapper.updateHis(hisParam);
		}
	}
	
	
	/*
	public String insertNewHis(HttpServletRequest req) {
		String hisNo = (String) req.getAttribute("HIS_NO");
		if(hisNo != null) {
			if(req instanceof MultipartHttpServletRequest) {
				MultipartHttpServletRequest mreq = (MultipartHttpServletRequest) req;
				mreq.getFileNames();
			}
			HttpSession session = req.getSession();				
			
			String univNm = (String) session.getAttribute("UNIV_NM");
			String usrNo = (String) session.getAttribute("USR_NO");
			String usrId = (String) session.getAttribute("USR_ID");
			String usrNm = (String) session.getAttribute("USR_NM");
			
			Map<String, String[]> parameters = req.getParameterMap();
			
	
			if(param != null && param.getBytes().length > 4000) {
				byte[] arrParam = param.getBytes();
				byte[] nParam = new byte[4000];
				System.arraycopy(arrParam, 0, nParam, 0, 4000);
				param = new String(nParam);
			}
	
			
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
			String curDate = sf.format(new Date());
			String curYear = curDate.substring(0,4);
			String curMonth = curDate.substring(4,6);
			String curDay = curDate.substring(6,8);
			String uploadPath = Code.instance().getUploadRootPath() + "/hisfile/" + curYear + "/" + curMonth + "/" + curDay;

		}
		
		
		Map<String,Object> hisParam = new HashMap<String,Object>();
		hisParam.put("RESULT_TYPE"	, resultType);
		hisParam.put("MENU_NO"	, menuNo);
		hisParam.put("MENU_NM"	, menuNm);
		hisParam.put("WORK_TYPE"	, workType);
		hisParam.put("FUNC_NM"	, funcNm);
		hisParam.put("UNIV_NM"	, univNm);
		hisParam.put("RMRK"	, rmrk);
		hisParam.put("PARAM", param);
		if(paramFile != null) {
			hisParam.put("PARAM_FILE_NM", paramFile.getOriginalFilename());
			hisParam.put("PARAM_REAL_FILE_NM", uploadPath + "");
		}
		
		hisParam.put("PARAM_DATA_CNT", param);
		hisParam.put("RESULT", param);
		hisParam.put("RESULT_CNT", param);
		hisParam.put("RESULT_FILE_NM", param);
		hisParam.put("RESULT_REAL_FILE_NM", param);
		if(privacyYn == null || privacyYn.equals("")) {
			hisParam.put("PRIVACY_YN"	, "N");
		} else {
			hisParam.put("PRIVACY_YN"	, "Y");
		}		
		hisParam.put("PRIVACY_INFO"	, privacyInfo);
		hisParam.put("sessUsrNo", usrNo);	
		hisParam.put("sessUsrId", usrId);	
		hisParam.put("sessUsrNm", usrNm);
		hisParam.put("usrIp", getUsrIp(req));
		hisParam.put("sessionId", session.getId());
	
		
		cmmnMapper.insertHis(hisParam);
		
		return (String) hisParam.get("HIS_NO");
	}
	*/
	
	
	public void inserCurLoginUsr(HttpServletRequest request) throws ResMsgException {
		HttpSession session = request.getSession();
    	String sessUsrNo = (String) session.getAttribute("USR_NO");
    	String sessUsrId = (String) session.getAttribute("USR_ID");
    	String sessUsrNm = (String) session.getAttribute("USR_NM");
    	String sessUnivNm = (String) session.getAttribute("UNIV_NM");
    	String sessUsrPerm = (String) session.getAttribute("USR_PERM");
    	String sessPicNo = (String) session.getAttribute("PIC_NO");
    	
    	String usrPermNm = "";
    	if(sessUsrPerm.equals("U")) {
    		usrPermNm = "대학관계자";
    	} else if(sessUsrPerm.equals("E")) {
    		usrPermNm = "교육부";
    	} else if(sessUsrPerm.equals("A")) {
    		usrPermNm = "관리자";
    	} else if(sessUsrPerm.equals("Q")) {
    		usrPermNm = "정량평가 위원";
    	} else if(sessUsrPerm.equals("L")) {
    		usrPermNm = "정성평가 위원";
    	}
    	
    	Map<String,Object> param = new HashMap<String,Object>();
    	param.put("sessId", session.getId());
    	param.put("usrNo", sessUsrNo);
    	param.put("usrId", sessUsrId);
    	param.put("usrNm", sessUsrNm);
    	param.put("univNm", sessUnivNm);
    	param.put("usrPermNm", usrPermNm);
    	param.put("picNo", sessPicNo);
    	
		if(cmmnMapper.isExistCurLoginUsr(param)) {
			cmmnMapper.updateCurLoginUsr(param);
    	} else {
    		session.invalidate();
    		throw new ResMsgException("현재 계정으로 다른 곳에서 로그인을 하였습니다. 현재 세션을 종료합니다.", "login");
    	}
	}
	
	public void delteCurLoginUsr() {
		cmmnMapper.deleteCurLoginUsr();
	}
	
/*	public void deleteCertNum() throws Exception {
		loginMapper.deleteCertNumAuto();
	}*/
	
/*	public void executeReserveTask() throws Exception {
		List<Map<String,Object>> periodList = periodMapper.getList();
		
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH");
		for(Map<String,Object> periodMap: periodList) {
			String periodCd = (String) periodMap.get("PERIOD_CD");
			String resvStartDate = (String) periodMap.get("RESV_START_DATE");
			String resvStartHour = (String) periodMap.get("RESV_START_HOUR");
			String resvEndDate = (String) periodMap.get("RESV_END_DATE");
			String resvEndHour = (String) periodMap.get("RESV_END_HOUR");
			
			if(resvStartDate != null && resvEndDate != null) {
				String startDate = resvStartDate + " " + resvStartHour;
				String endDate = resvEndDate + " " + resvEndHour;
				log.info("startDate: " + startDate);
				log.info("endDate: " + endDate);
				
				String curVal = (String) periodMap.get("CUR_VAL");
				String dftVal = (String) periodMap.get("DFT_VAL");
				
				String curDate = sf.format(new Date());
				String neCurVal = curVal;
				if(curDate.compareTo(startDate) >= 0) {
					//현재시간이  시작일시보다 같거나 크고
					if(curDate.compareTo(endDate) < 0) {
						//현재시간이  종료일시보다 작을때
						neCurVal = dftVal;
					} else {
						//현재시간이  종료일시보다 작을때
						neCurVal = (dftVal.equals("Y")) ? "N" : "Y";
					}
				}
				
				if(!curVal.equals(neCurVal)) {
					Map<String,Object> updateMap = new HashMap<String,Object>();
					updateMap.put("startDate", startDate);
					updateMap.put("endDate", endDate);
					updateMap.put("curVal", neCurVal);
					updateMap.put("dftVal", dftVal);
					updateMap.put("sessUsrId", "system");
					updateMap.put("periodCd", periodCd);
					periodMapper.update(updateMap);
				}
				
			}
			
		}
	}*/
	
	
	//Y,N 선택을 Map List로 
	public List<Map<String,Object>> getYnMapList() { 
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> mapY = new  HashMap<String,Object>();
		mapY.put("CODE", "Y");
		mapY.put("CODE_NM", "Y");
		
		Map<String,Object> mapN = new  HashMap<String,Object>();
		mapN.put("CODE", "N");
		mapN.put("CODE_NM", "N");
		
		list.add(mapY);
		list.add(mapN);
		
		return list;
	}
	
	public String getDefaultCellValue(Cell cell) {
		if(cell == null) return "";
		if(cell.getCellType() == CellType.NUMERIC) {
			if(DateUtil.isCellDateFormatted(cell)) {
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				return sf.format(cell.getDateCellValue());
			} else {
				BigDecimal b = new BigDecimal(cell.getNumericCellValue());
				System.out.println("scale: " + b.scale());
				if(b.scale() == 0) {
					return String.valueOf(b.intValue());
				} else {
					return String.valueOf(b.doubleValue());
				}
				
			}
		} else if(cell.getCellType() == CellType.BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue());
		} else if(cell.getCellType() == CellType.STRING) {
			return cell.getStringCellValue();
		} else if(cell.getCellType() == CellType._NONE) {
			return "";
		} else if(cell.getCellType() == CellType.BLANK) {
			return "";
		} else if(cell.getCellType() == CellType.ERROR) {
			return String.valueOf(cell.getErrorCellValue());
		} else if(cell.getCellType() == CellType.FORMULA) {
			return cell.getCellFormula();
		} else {
			log.info("타입없음");
			return "";
		}
	}
	
	public Map<String,Object> getManualFaqState(String menuId){
		String uploadRootPath = Code.instance().getUploadRootPath();
		String manualAbsolutePath = uploadRootPath + "/manual/"+menuId+".pdf";
		String faqAbsolutePath = uploadRootPath + "/faq/"+menuId+".pdf";

	    File manualFile = new File(manualAbsolutePath);
	    File faqFile = new File(faqAbsolutePath);

	    boolean isManualNew = false;
	    boolean isFaqNew = false;

	    long currentTime = System.currentTimeMillis();
	    long sevenDaysInMillis = 7L * 24 * 60 * 60 * 1000;  // 30일을 밀리초로 환산	
	    
	    if(manualFile.exists()) {
	        long lastModified = manualFile.lastModified();
	        
	        if((currentTime - lastModified) <= sevenDaysInMillis) {
	        	System.out.println(currentTime - lastModified +"");
	            isManualNew = true;
	        }   
	    }
	    
	    if(faqFile.exists()) {
	        long lastModified = faqFile.lastModified();
	        
	        if((currentTime - lastModified) <= sevenDaysInMillis) {
	            isFaqNew = true;
	           
	        }
	    }
	    
	    Map<String,Object> map = new HashMap<>();
	    map.put("isManualNew",isManualNew);
	    map.put("isFaqNew",isFaqNew);
	    map.put("menuId",menuId);
	    
	    return map;
	}
}
