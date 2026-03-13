package tms.adminDelete.maps;

import java.util.List;
import java.util.Map;

public interface AdminDeleteMapper {	
	public List<Map<String,Object>> getAdminDeleteList(Map<String,Object> param) throws Exception;
		
	public Map<String,Object> getSubjectEmail(Map<String,Object> param) throws Exception;
}
