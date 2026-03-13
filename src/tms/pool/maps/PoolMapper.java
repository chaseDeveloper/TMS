package tms.pool.maps;

import java.util.List;
import java.util.Map;

import tms.main.param.FileParam;
import tms.main.param.UnivParam;

public interface PoolMapper {	
	
	public Map<String,Object> getPoolInfo(Map<String,Object> param) throws Exception;	

	public List<Map<String,Object>> selectPoolDegreeList(Map<String,Object> param) throws Exception;	
	
	public List<Map<String,Object>> selectPoolFulltimeTeacherList(Map<String,Object> param) throws Exception;
	
	public List<Map<String,Object>> selectPoolEvaExpList(Map<String,Object> param) throws Exception;	
	
	public List<Map<String, Object>> selectSchlList(Map<String, Object> param);
	
	public List<Map<String, Object>> selectSchlList2(Map<String, Object> param);
	
	public void updatePoolInfo(Map<String,Object> param);
	
	public void insertPoolDegreeInfo(Map<String,Object> param);
	
	public void deletePoolDegreeList(Map<String,Object> param);
	
    public void insertPoolFulltimeTeacherInfo(Map<String,Object> param);
	
	public void deletePoolFulltimeTeacherList(Map<String,Object> param);
	
	public void insertPoolEvaExpInfo(Map<String,Object> param);
		
	public void deletePoolEvaExpList(Map<String,Object> param);	
		
	public boolean isExistEmail(Map<String,Object> checkparam) throws Exception;
	
	public boolean isChangeEmail(Map<String,Object> checkparam) throws Exception;
	
	public void updatePoolApplInfo(Map<String,Object> param);
	
	public void updatePoolDisqUnivInfo(Map<String,Object> param);

	
}
