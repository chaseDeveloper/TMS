package tms.poolSbmt.maps;

import java.util.List;
import java.util.Map;

import tms.main.param.FileParam;
import tms.main.param.UnivParam;

public interface PoolSbmtMapper {	
	
	
	public List<Map<String,Object>> selectPoolApplList1(Map<String,Object> param) throws Exception;

	public List<Map<String,Object>> selectPoolApplList2(Map<String,Object> param) throws Exception;

	public String selectUsrJobCd(Map<String,Object> param) throws Exception;
	
	public String selectUsrFileId(Map<String,Object> param) throws Exception;
	
	public List<Map<String,Object>> selectPoolDisqUnivList(Map<String,Object> param) throws Exception;
	
	public Map<String,Object> getScheduleInfo(Map<String,Object> param) throws Exception;	
	
	public void updatePoolDisqYNInfo(Map<String,Object> param);	
	
	public void insertPoolDisqUnivInfo(Map<String,Object> param);
		
	public void deletePoolDisqUnivList(Map<String,Object> param);	
		
	public void savePoolApplInfo(Map<String,Object> param);	
	
	public List<Map<String, Object>> selectSchlList(Map<String, Object> param);
	
	
	
}
