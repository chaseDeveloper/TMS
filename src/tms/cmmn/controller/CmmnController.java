package tms.cmmn.controller;


import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import tms.cmmn.maps.CmmnMapper;
import tms.cmmn.service.CmmnService;
import tms.main.exception.ResMsgException;


@Controller
@RequestMapping("/cmmn")
public class CmmnController {		
	
	@Autowired CmmnMapper cmmnMapper;
	@Autowired CmmnService cmmnService;
	
    private Log log = LogFactory.getLog(this.getClass());
	
    @RequestMapping(value = "/exportData.do", method = RequestMethod.GET)
    public void exportData(String pq_filename, HttpServletRequest request, HttpServletResponse response) throws IOException {
    	HttpSession ses = request.getSession(true);
        if ( ((String)ses.getAttribute("pq_filename")).equals(pq_filename) ) {
            String contents = (String) ses.getAttribute("pq_data");
            Boolean pq_decode = (Boolean) ses.getAttribute("pq_decode");
            String pq_ext = (String) ses.getAttribute("pq_ext");
            
            String paramFile = pq_filename.substring(0 ,pq_filename.lastIndexOf(pq_ext) -1 );
            String fileName = URLDecoder.decode(new String(Base64.decodeBase64(paramFile)), "UTF-8");            
            log.info("fileName: " + fileName + "." + pq_ext);
            

            byte[] bytes = pq_decode?  Base64.decodeBase64(contents): contents.getBytes(Charset.forName("UTF-8"));
            
            response.setContentType("application/octet-stream");

            response.setHeader("Content-Disposition",
                    "attachment;filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "." + pq_ext + "\"");
            response.setContentLength(bytes.length);
            ServletOutputStream out = response.getOutputStream();
            out.write(bytes);

            out.flush();
            out.close();
        }
        
    }
    
    @RequestMapping(value = "/exportData.do", method = RequestMethod.POST)
    public @ResponseBody
    String exportData(String pq_data, String pq_ext, Boolean pq_decode, String pq_filename, HttpServletRequest request) throws IOException {
        
        String[] arr = new String[] {"csv", "xlsx", "htm", "zip", "json"};        
        String filename = pq_filename + "." + pq_ext;

        if(Arrays.asList(arr).contains(pq_ext)){            
            HttpSession ses = request.getSession(true);
            ses.setAttribute("pq_data", pq_data);
            ses.setAttribute("pq_decode", pq_decode);
            ses.setAttribute("pq_filename", filename);
            ses.setAttribute("pq_ext", pq_ext);
        }
        return filename;
    }
  
    
    @RequestMapping("/fileDownload.do")
    public void fileDownload(@RequestParam String fileId, String fileSeq , HttpServletRequest request,HttpServletResponse response) throws Exception {    	
    	String univCd = (String) request.getSession().getAttribute("UNIV_CD");
    	
    	Map<String,Object> param = new HashMap<String,Object>();
    	try {
    		param.put("fileId", fileId);

    		if(fileSeq != null) {
    			param.put("fileSeq", fileSeq);
    		} else {
    			param.put("fileSeq", "0001");
    		}
    		
    	} catch(NumberFormatException e) {
    		throw new ResMsgException("fileId는 숫자형식만 가능합니다.", "blank");
    	}
    	
    	Map<String,Object> fileInfo = cmmnMapper.getFileInfo(param);
    	if(fileInfo == null) {
    		throw new ResMsgException("다운로드 할 파일이 없습니다.", "blank");
    	}
    	
    	
    	if(univCd != null) {
    		String inptUsrPerm = (String) fileInfo.get("INPT_USR_PERM");
    		if(inptUsrPerm != null && inptUsrPerm.equals("U")) {
    			String inptId = (String) fileInfo.get("INPT_ID");
    			if(!univCd.equals(inptId)) {
        			throw new ResMsgException("타 대학의 파일을 다운로드 할 수 없습니다.", "blank");
        		}
    		}
    		
    	}
    	
    	
    	cmmnService.fileDownload(request, response, fileInfo);
    }
  
}
