package tms.cmmn.maps;

import java.util.List;
import java.util.Map;

import tms.main.param.FileParam;
import tms.main.param.UnivParam;

public interface CmmnMapper {	
	
	public void insertFile(Map<String,Object> param);
	public void insertFileExistFileId(Map<String,Object> param);
	
	public Map<String,Object> getFileInfo(Map<String,Object> param);
	public void deleteFile(Map<String,Object> param);
	public void updateFileSeq(Map<String,Object> param);
	public void deleteGreaterFileSeq(Map<String,Object> param);
	public void insertHis(Map<String,Object> param);
	public void updateHis(Map<String,Object> param);
	public Map<String,Object> getHisInfo(String hisNo);
	
	public List<Map<String,Object>> getUnivList();
	public List<Map<String,Object>> getUnivListParamUnivCd(String univCd);
	
	public List<Map<String,Object>> getUnivTranCorsCdList(String univCd);
	public String getUnivNm(String univCd);
	public Map<String,Object> getUnivInfo(String univCd);
	public String getUsrNm(String usrNo);
	public List<Map<String,Object>> getFilterTranCorsCdList(Map<String,Object> param);
	public List<Map<String,Object>> getFilterScsbjtList(Map<String,Object> param);
	public List<Map<String,Object>> getUnivScsbjtList(Map<String,Object> param);
	public boolean isExistScsbjtInUniv(Map<String,Object> param);
	public boolean isExistScsbjt2InUniv(Map<String,Object> param);
	public void updateDelFlag(String fileId);
	public void insertDataMap(Map<String,Object> param);
	public void deleteDataMapBySubCode(Map<String,Object> param);
	
	public String getMenuNm(String menuId);
	
	public List<Map<String,Object>> getTargetTranCorsCdList(Map<String,Object> param);
	public List<Map<String,Object>> getTargetScsbjtList(Map<String,Object> param);
	
	public List<Map<String,Object>> getFileList(String fileId);
	
	public Boolean isExistCurLoginUsr(Map<String,Object> param);
	public void insertCurLoginUsr(Map<String,Object> param);
	public void updateCurLoginUsr(Map<String,Object> param);
	public void deleteCurLoginUsr();
	public void deleteCurLoginUsrImmediateSession(String sessId);
	public void deleteCurLoginUsrImmediateUsr(Map<String,Object> param);
	public Map<String,Object> getCurLoginInfo(Map<String,Object> param);
	
	public List<String> getEvaYearList(Map<String,Object> param) throws Exception;
	public List<String> getEvaCalcYearList(Map<String,Object> param) throws Exception;
	
	public List<Map<String,Object>> getEvaTargetList(Map<String,Object> param);
	public List<Map<String,Object>> getEvaUnivList(Map<String,Object> param);
	public List<Map<String,Object>> getEvaTranCorsList(Map<String,Object> param);
	public List<Map<String,Object>> getEvaScsbjtList(Map<String,Object> param);
	public List<Map<String,Object>> getEvaGradTranCorsList(Map<String,Object> param);
	
	public List<Map<String,Object>> getEvaTranCorsListForTch(Map<String,Object> param);
	public List<Map<String,Object>> getEvaScsbjtListForTch(Map<String,Object> param);
	
	public String getEvaTranCorsCd(Map<String,Object> param);
	
}
