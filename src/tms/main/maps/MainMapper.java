package tms.main.maps;

import java.util.List;
import java.util.Map;

import tms.main.param.FileParam;
import tms.main.param.UnivParam;

public interface MainMapper {
	
	public List<Map<String,Object>> selectPoolScheList(Map<String,Object> param) throws Exception;
	
	public Map<String,Object> getPoolInfo(String di) throws Exception;
	
	public List<Map<String, Object>> getPoolDegreeInfo(String di) throws Exception;
	
	public List<Map<String,Object>> getPoolFulltimeInfo(String di) throws Exception;
	
	public boolean isExistPoolDi(String di) throws Exception;
	
	public Map<String,Object> getPoolInfoAdmin(String usrId) throws Exception;
}
