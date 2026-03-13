package tms.adminDelete.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;

import tms.adminDelete.maps.AdminDeleteMapper;
import tms.main.maps.MainMapper;
import tms.main.util.KediAES256Util;

@Controller
@EnableScheduling
@Configuration
@RequestMapping("/adminDelete")
public class AdminDeleteController {
	private Log log = LogFactory.getLog(this.getClass());

	@Autowired
	MainMapper mainMapper;

	@Autowired
	MessageSource messageSource;

	@Autowired
	AdminDeleteMapper adminDeleteMapper;

	@Resource(name = "txManager")
	private PlatformTransactionManager txManager;

	@RequestMapping("/adminDeleteList.do")
	public String adminDeleteList(HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
		HttpSession session = request.getSession();

		String di = (String) session.getAttribute("DI");
		String clientIp = (String) session.getAttribute("clientIp");

		if (di == null || di.length() != 64) {
			String errorMessage = messageSource.getMessage("msg_102", null, Locale.getDefault());
			redirectAttributes.addFlashAttribute("message", errorMessage);
			return "redirect:/main/main.do";
		} else {
			if (mainMapper.isExistPoolDi(di)) {

				Map<String, Object> loginInfo = mainMapper.getPoolInfo(di);
				String usrId = (String) loginInfo.get("USR_ID");
				String role = (String) loginInfo.get("ROLE");
				String ip = (String) loginInfo.get("IP");

				if (role == null || !"A".equals(role)) {
					String errorMessage = messageSource.getMessage("msg_108", null, Locale.getDefault());
					redirectAttributes.addFlashAttribute("message", errorMessage);
					request.getSession().invalidate();
					return "redirect:/main/main.do";
				}

				if (ip == null || clientIp == null) {
					String errorMessage = "접속 실패: 등록된 IP가 없습니다.";
					redirectAttributes.addFlashAttribute("message", errorMessage);
					request.getSession().invalidate(); // 세션 무효화
					return "redirect:/main/main.do";
				}

				if (!clientIp.equals(ip)) {
					String errorMessage = "접속 실패: 현재 접속한 IP (" + clientIp + ")가 등록된 ip와 일치하지 않습니다.";
					redirectAttributes.addFlashAttribute("message", errorMessage);
					request.getSession().invalidate(); // 세션 무효화
					return "redirect:/main/main.do";
				}

			} else {
				String errorMessage = messageSource.getMessage("msg_105", null, Locale.getDefault());
				redirectAttributes.addFlashAttribute("message", errorMessage);
				request.getSession().invalidate();
				return "redirect:/join/joinStep01.do";
			}
		}

		return "/adminDelete/adminDeleteView";
	}

	@RequestMapping(value = "/getAdminDeleteList.do")
	public void getAdminDeleteList(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    String sStartDate = request.getParameter("startDate");
	    String sEndDate = request.getParameter("endDate");
	    String sNameEmail = request.getParameter("nameEmail");

	    HashMap<String, Object> resultMap = new HashMap<>();
	    HashMap<String, Object> paramMap = new HashMap<>();

	    if (sStartDate != null && !sStartDate.isEmpty()) {
	        sStartDate += " 00:00:00";
	        paramMap.put("sStartDate", sStartDate);
	    }

	    if (sEndDate != null && !sEndDate.isEmpty()) {
	        sEndDate += " 23:59:59";
	        paramMap.put("sEndDate", sEndDate);
	    }

	    if (sNameEmail != null && !sNameEmail.isEmpty()) {
	        paramMap.put("sNameEmail", sNameEmail);
	    }

	    List<Map<String, Object>> list = adminDeleteMapper.getAdminDeleteList(paramMap);

	    resultMap.put("result", "S");
	    resultMap.put("list", list);

	    Gson gson = new Gson();
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(gson.toJson(resultMap));
	}

}
