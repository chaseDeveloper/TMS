package tms.join.maps;

import java.util.List;
import java.util.Map;

import tms.main.param.FileParam;
import tms.main.param.UnivParam;

public interface JoinMapper {	
	
	public List<Map<String,Object>> getPoolList(Map<String,Object> param) throws Exception;
	
	public Map<String,Object> selectPoolInfo(Map<String,Object> param) throws Exception;
	
	public void insertPoolInfo(Map<String,Object> param);
	
	public void updatePoolInfo(Map<String,Object> param);
	
	public void updatePoolDegree(Map<String,Object> param);
	
	public void updatePoolFulltimeTeacher(Map<String,Object> param);
	
	public void updatePoolEvaExp(Map<String,Object> param);	
	
	public boolean isExistPoolTelHp(Map<String,Object> param) throws Exception;
	
		
	
	
	
	
}
