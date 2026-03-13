package tms.main.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import tms.main.exception.LoginException;


public class LoginInterceptor extends HandlerInterceptorAdapter {
	private Log log = LogFactory.getLog(this.getClass());
	
	@Override
	public boolean preHandle(HttpServletRequest request,
		HttpServletResponse response, Object handler) throws Exception {
		String contextpath = request.getContextPath();
		String requestURI = request.getRequestURI().replace(contextpath, "");
		
		log.debug( requestURI + " START!!!!");
		
		HttpSession session = request.getSession();
		/*if(session.getAttribute("DI") == null) {
			log.info("fail: " + requestURI + " reasone: session timeout or not login user");
			throw new LoginException();
		}*/
		
		return true;		
	}
	
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		String contextpath = request.getContextPath();
		String requestURI = request.getRequestURI().replace(contextpath, "");

		log.debug( requestURI + " END!!!!");
	}
}
