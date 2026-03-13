package tms.main.exception;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.google.gson.Gson;

import tms.main.service.MainService;

@ControllerAdvice
public class ExceptionHandlingController {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
    MainService mainService;
	
	
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public void MissingServletRequestParameterException(HttpServletRequest req, HttpServletResponse res, MissingServletRequestParameterException ex) throws ServletException, IOException {

		String xRquestWithHeader = req.getHeader("X-Requested-With");
//		String originHeader = req.getHeader("Origin");
		if(xRquestWithHeader == null || xRquestWithHeader.equals("")) {
			req.setAttribute("msg", ex.getMessage());
			req.getRequestDispatcher("/WEB-INF/jsp/common/hisBack.jsp").forward(req, res);
			
		} else {
			//ajax 요청
			HashMap<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("result", "F");
			resultMap.put("cmd", "msg");
			resultMap.put("msg", ex.getMessage() );
			
			Gson gson = new Gson();
			res.setCharacterEncoding("UTF-8");	
			res.setStatus(590);			
			res.setContentType("application/json;charset=UTF-8");			
			res.getWriter().write(gson.toJson(resultMap));
		}
	}
	
	
	@ExceptionHandler(ResMsgException.class)
	public void ResMsgException(HttpServletRequest req, HttpServletResponse res, ResMsgException ex) throws ServletException, IOException {

		String xRquestWithHeader = req.getHeader("X-Requested-With");
//		String originHeader = req.getHeader("Origin");
		if(xRquestWithHeader == null || xRquestWithHeader.equals("")) {
			req.setAttribute("msg", ex.getMsg());
			if(ex.getUri() == null) {
				req.getRequestDispatcher("/WEB-INF/jsp/common/hisBack.jsp").forward(req, res);
			} else if(ex.getUri().equals("blank")) {
				req.getRequestDispatcher("/WEB-INF/jsp/common/blank.jsp").forward(req, res);
			} else if(ex.getUri().equals("close")) {
				req.getRequestDispatcher("/WEB-INF/jsp/common/selfClose.jsp").forward(req, res);
			}  else {
				req.setAttribute("url", ex.getUri() == null? null :mainService.uriToFullURL(req, ex.getUri()));
				req.getRequestDispatcher("/WEB-INF/jsp/common/hisBack.jsp").forward(req, res);
			}
			
		} else {
			//ajax 요청
			HashMap<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("result", "F");
			resultMap.put("cmd", "msg");
			resultMap.put("msg", ex.getMsg() == null ? ex.getMessage() : ex.getMsg() );
			resultMap.put("url", ex.getUri() == null? null : mainService.uriToFullURL(req, ex.getUri()));
			
			Gson gson = new Gson();
			res.setCharacterEncoding("UTF-8");	
			res.setStatus(590);			
			res.setContentType("application/json;charset=UTF-8");			
			res.getWriter().write(gson.toJson(resultMap));
		}
	}
	
	@ExceptionHandler(LoginException.class)
	public void LoginException(HttpServletRequest req, HttpServletResponse res, LoginException ex) throws ServletException, IOException {
		
		String xRquestWithHeader = req.getHeader("X-Requested-With");
		
		if(xRquestWithHeader == null || xRquestWithHeader.equals("")) {
			req.setAttribute("url", mainService.uriToFullURL(req, "/login/loginPage.do"));
			req.getRequestDispatcher("/WEB-INF/jsp/common/gotoLogin.jsp").forward(req, res);
			
		} else {
			//ajax 요청
			HashMap<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("result", "F");
			resultMap.put("msg", "로그아웃 되었습니다. 다시 로그인하여 주십시오.");
			resultMap.put("url", mainService.uriToFullURL(req, "/login/loginPage.do"));
			
			Gson gson = new Gson();
			res.setCharacterEncoding("UTF-8");	
			res.setStatus(591);			
			res.setContentType("application/json;charset=UTF-8");			
			res.getWriter().write(gson.toJson(resultMap));
		}
	}
	/*
	@ExceptionHandler(BindException.class)
	public void BindException(HttpServletRequest req, HttpServletResponse res, BindException ex) throws ServletException, IOException {
		
		String xRquestWithHeader = req.getHeader("X-Requested-With");
		BindingResult br = ex.getBindingResult();
		String msg = br.getFieldError().getDefaultMessage();
		
		if(xRquestWithHeader == null || xRquestWithHeader.equals("")) {
			req.setAttribute("msg", msg);
			req.getRequestDispatcher("/WEB-INF/jsp/common/hisBack.jsp").forward(req, res);
			
		} else {
			//ajax 요청
			HashMap<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("result", "F");
			resultMap.put("cmd", "msg");
			resultMap.put("msg", msg == null ? ex.getMessage() : msg );
			resultMap.put("url",null);
			
			Gson gson = new Gson();
			res.setCharacterEncoding("UTF-8");	
			res.setStatus(590);			
			res.setContentType("application/json;charset=UTF-8");			
			res.getWriter().write(gson.toJson(resultMap));
		}
	}
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public void MissingServletRequestParameterException(HttpServletRequest req, HttpServletResponse res, MissingServletRequestParameterException ex) throws ServletException, IOException {
		
		String xRquestWithHeader = req.getHeader("X-Requested-With");
		String parameterName = ex.getParameterName();
		String msg = parameterName + "은 필수 파라미터입니다.";
		if(xRquestWithHeader == null || xRquestWithHeader.equals("")) {
			req.setAttribute("msg", msg);
			req.getRequestDispatcher("/WEB-INF/jsp/common/hisBack.jsp").forward(req, res);
			
		} else {
			//ajax 요청
			HashMap<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("result", "F");
			resultMap.put("cmd", "msg");
			resultMap.put("msg", msg );
			resultMap.put("url",null);
			
			Gson gson = new Gson();
			res.setCharacterEncoding("UTF-8");	
			res.setStatus(590);			
			res.setContentType("application/json;charset=UTF-8");			
			res.getWriter().write(gson.toJson(resultMap));
		}
	}
	
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public void HttpRequestMethodNotSupportedException(HttpServletRequest req, HttpServletResponse res, HttpRequestMethodNotSupportedException ex) throws ServletException, IOException {
		String xRquestWithHeader = req.getHeader("X-Requested-With");
//		String originHeader = req.getHeader("Origin");
		if(xRquestWithHeader == null || xRquestWithHeader.equals("")) {
			req.setAttribute("title", "ERROR");
			req.setAttribute("msg", "지원하지 않는 METHOD 입니다.");
			req.getRequestDispatcher("/WEB-INF/jsp/common/errorPage.jsp").forward(req, res);
		} else {
			//ajax 요청
			HashMap<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("result", "F");
			resultMap.put("cmd", "msg");
			resultMap.put("msg",  "지원하지 않는 METHOD 입니다." );
			
			Gson gson = new Gson();
			res.setStatus(590);		
			res.setCharacterEncoding("UTF-8");
			res.setContentType("application/json;charset=UTF-8");			
			res.getWriter().write(gson.toJson(resultMap));
		}
	}
	
	
	
	@ExceptionHandler(PersistenceException.class)
	public void PersistenceException(HttpServletRequest req,  HttpServletResponse res, PersistenceException ex) throws ServletException, IOException {
		String xRquestWithHeader = req.getHeader("X-Requested-With");
//		String originHeader = req.getHeader("Origin");
		log.error("PersistenceException!!", ex);
		if(xRquestWithHeader == null || xRquestWithHeader.equals("")) {
			req.setAttribute("title", "데이터베이스 에러");
			req.setAttribute("msg", "데이터베이스 에러가 발생하였습니다. 관리자에게 문의하여 주십시오.");
			req.getRequestDispatcher("/WEB-INF/jsp/common/errorPage.jsp").forward(req, res);
		} else {
			//ajax 요청
			HashMap<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("result", "F");
			resultMap.put("cmd", "msg");
			resultMap.put("msg",  "데이터베이스 에러가 발생하였습니다. 관리자에게 문의하여 주십시오." );
			
			Gson gson = new Gson();
			res.setStatus(590);		
			res.setCharacterEncoding("UTF-8");
			res.setContentType("application/json;charset=UTF-8");			
			res.getWriter().write(gson.toJson(resultMap));
		}
	}
	
	@ExceptionHandler(SQLException.class)
	public void SQLException(HttpServletRequest req,  HttpServletResponse res, SQLException ex) throws ServletException, IOException {
		String xRquestWithHeader = req.getHeader("X-Requested-With");
//		String originHeader = req.getHeader("Origin");
		log.error("SQLException!!", ex);
		if(xRquestWithHeader == null || xRquestWithHeader.equals("")) {
			req.setAttribute("title", "데이터베이스 에러");
			req.setAttribute("msg", "데이터베이스 에러가 발생하였습니다. 관리자에게 문의하여 주십시오.");
			req.getRequestDispatcher("/WEB-INF/jsp/common/errorPage.jsp").forward(req, res);
		} else {
			//ajax 요청
			HashMap<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("result", "F");
			resultMap.put("cmd", "msg");
			resultMap.put("msg",  "데이터베이스 에러가 발생하였습니다. 관리자에게 문의하여 주십시오." );
			
			Gson gson = new Gson();
			res.setStatus(590);		
			res.setCharacterEncoding("UTF-8");
			res.setContentType("application/json;charset=UTF-8");			
			res.getWriter().write(gson.toJson(resultMap));
		}
	}
	
	@ExceptionHandler(Exception.class)
	public void exception(HttpServletRequest req,  HttpServletResponse res, Exception ex) throws ServletException, IOException {
		String xRquestWithHeader = req.getHeader("X-Requested-With");
//		String originHeader = req.getHeader("Origin");
		log.error("Exception!!", ex);
		if(xRquestWithHeader == null || xRquestWithHeader.equals("")) {
			req.setAttribute("title", "에러발생");
			req.setAttribute("msg", "알수없는 에러가 발생하였습니다. 관리자에게 문의하여 주십시오.");
			req.getRequestDispatcher("/WEB-INF/jsp/common/errorPage.jsp").forward(req, res);
		} else {
			//ajax 요청
			HashMap<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("result", "F");
			resultMap.put("cmd", "msg");
			resultMap.put("msg",  "알수없는 에러가 발생하였습니다. 관리자에게 문의하여 주십시오." );
			
			Gson gson = new Gson();
			res.setStatus(590);		
			res.setCharacterEncoding("UTF-8");
			res.setContentType("application/json;charset=UTF-8");			
			res.getWriter().write(gson.toJson(resultMap));
		}
	}
	*/
	
}
