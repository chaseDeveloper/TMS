package tms.adminSchedule.maps;

import java.util.List;
import java.util.Map;

import tms.main.param.FileParam;
import tms.main.param.UnivParam;

public interface AdminScheduleMapper {	
	
	public List<Map<String,Object>> getAdminScheList() throws Exception;
	
	public void insertPoolSchedule(Map<String,Object> param);
	
	public void updatePoolSchedule(Map<String,Object> param);
	
	public void deletePoolSchedule(Map<String,Object> param);

	public List<Map<String, Object>> poolCdList();	
	
	public boolean isExistPoolSchedule(Map<String,Object> param) throws Exception;	
	
	public List<Map<String,Object>> getAdminPoolList(Map<String,Object> param) throws Exception;
	
	public void updataYNSelect(Map<String,Object> param);
	
	public Map<String,Object> getAdminPoolPrint(Map<String,Object> param) throws Exception;
	
}
