package tms.poolResult.maps;

import java.util.List;
import java.util.Map;

import tms.main.param.FileParam;
import tms.main.param.UnivParam;

public interface PoolResultMapper {	
	
	public List<Map<String,Object>> selectPoolResultList(Map<String,Object> param) throws Exception;
	
	boolean saveSign(Map<String, Object> param);	
		
	public Map<String,Object> getpoolCd(Map<String,Object> param) throws Exception;
	
}
