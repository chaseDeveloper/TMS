package tms.main.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter(urlPatterns = "/*")
public class ResourceFilter implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String uri = req.getRequestURI();
		System.out.println("filter uri:" + uri);
		
		if (uri.indexOf(".jsp") > -1) {
			 chain.doFilter(request, response);
		}else if(uri.indexOf(".css") > -1
				|| uri.indexOf(".gif") > -1
				|| uri.indexOf(".png") > -1
				|| uri.indexOf(".jpg") > -1
				|| uri.indexOf(".GIF") > -1
				|| uri.indexOf(".PNG") > -1
				|| uri.indexOf(".JPG") > -1
				|| uri.indexOf(".js") > -1				
				|| uri.indexOf(".map") > -1
				|| uri.indexOf(".txt") > -1
				|| uri.indexOf(".woff") > -1
				|| uri.indexOf(".eot") > -1
				|| uri.indexOf(".ttf") > -1
				|| uri.indexOf(".woff2") > -1) {
			request.getServletContext().getNamedDispatcher("default").forward(request, response);
		} else {
			chain.doFilter(req, response);
		}
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
}
