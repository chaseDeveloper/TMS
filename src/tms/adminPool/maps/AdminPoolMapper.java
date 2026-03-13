package tms.adminPool.maps;

import java.util.List;
import java.util.Map;

import tms.main.param.FileParam;
import tms.main.param.UnivParam;

public interface AdminPoolMapper {	
	
	public List<Map<String,Object>> getAdminPoolList(Map<String,Object> param) throws Exception;
		
	public Map<String,Object> getAdminPoolInfo(Map<String,Object> param) throws Exception;
	
	public List<Map<String,Object>> selectPoolDegreeList(Map<String,Object> param) throws Exception;	
	
	public List<Map<String,Object>> selectPoolFulltimeTeacherList(Map<String,Object> param) throws Exception;
	
	public List<Map<String,Object>> selectPoolEvaExpList(Map<String,Object> param) throws Exception;
	
	public List<Map<String,Object>> selectPoolDisqUnivList(Map<String,Object> param) throws Exception;
	
	public List<Map<String, Object>> getConsentRequired() throws Exception;

	public void deleteExpiredRows() throws Exception;
	
	public List<Map<String, Object>> selectEncryptedRows() throws Exception;
	
	public void insertDeletedMember(Map<String,Object> param) throws Exception;
	
	public void deletePoolApplByUsrId(Map<String,Object> param) throws Exception;
	public void deletePoolDisqUnivByUsrId(Map<String,Object> param) throws Exception;
	public void deletePoolEvaExpByUsrId(Map<String,Object> param) throws Exception;
	public void deletePoolFulltimeTeacherByUsrId(Map<String,Object> param) throws Exception;
	public void deletePoolDegreeByUsrId(Map<String,Object> param) throws Exception;
}
