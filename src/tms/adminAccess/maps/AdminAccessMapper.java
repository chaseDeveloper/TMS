package tms.adminAccess.maps;

import java.util.List;
import java.util.Map;

import tms.main.param.FileParam;
import tms.main.param.UnivParam;

public interface AdminAccessMapper {	
	
	public List<Map<String,Object>> getAdminAccessList(Map<String,Object> param) throws Exception;
		
	public Map<String,Object> getSubjectEmail(Map<String,Object> param) throws Exception;
}
