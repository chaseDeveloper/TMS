package tms.main.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tms.main.exception.ResMsgException;

public class Code {
	private Code(){
		codeList = new ArrayList<Map<String,Object>>();
	}
	
	private List<Map<String,Object>> codeList; 
	private String uploadRootPath;

	public static Code instance() {
		return LazyHolder.INSTANCE;
	}

	private static class LazyHolder {
		private static final Code INSTANCE = new Code();
	}
	
	public void add(Map<String,Object> map) {
		codeList.add(map);
	}
	
	public List<Map<String,Object>> getCodeList(String groupCd) throws Exception {		
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> map : codeList) {
			if(map.get("GROUP_CD") != null) {
				if(map.get("GROUP_CD").equals(groupCd)) {
					resultList.add(map);
				}
			}
		}
		
		return resultList;
	}
	
	public Map<String,Object> getCodeMap(String groupCd) throws Exception {		
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> map : codeList) {
			if(map.get("GROUP_CD") != null) {
				if(map.get("GROUP_CD").equals(groupCd)) {
					resultList.add(map);
				}
			}
		}
		
		if(resultList.size() > 1) {
			throw new ResMsgException(groupCd + "는 단일 개체의 코드가 아닙니다.");
		} if(resultList.size() == 0) {
			throw new ResMsgException(groupCd + "는 없는 코드 입니다.");
		}
		
		return resultList.get(0);
	}
	
	public String getCode(String groupCd, String codeNm) throws Exception {		
		List<Map<String,Object>> groupCdList = getCodeList(groupCd);
		String code = null;
		for(Map<String,Object> codeMap : groupCdList) {			
			String mapCodeNm = (String) codeMap.get("CODE_NM");
			if(codeNm.equals(mapCodeNm)) {
				code = (String) codeMap.get("CODE");
				break;
			}
		}
		
		return code;
	}
	
	public boolean isValidCode(String groupCd, String code) throws Exception {		
		List<Map<String,Object>> groupCdList = getCodeList(groupCd);
		for(Map<String,Object> codeMap : groupCdList) {			
			String mapCode = (String) codeMap.get("CODE");
			if(code.equals(mapCode)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isValidCodeNm(String groupCd, String codeNm) throws Exception {		
		List<Map<String,Object>> groupCdList = getCodeList(groupCd);
		for(Map<String,Object> codeMap : groupCdList) {			
			String mapCodeNm = (String) codeMap.get("CODE_NM");
			if(codeNm.equals(mapCodeNm)) {
				return true;
			}
		}
		
		return false;
	}

	public String getUploadRootPath() {
		return uploadRootPath;
	}

	public void setUploadRootPath(String uploadRootPath) {
		this.uploadRootPath = uploadRootPath;
	}
	
	
}
