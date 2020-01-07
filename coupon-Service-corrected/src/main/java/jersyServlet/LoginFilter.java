///**
// * 
// */
//package jersyServlet;
//
//import java.io.IOException;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
///**
// * @author scary
// *
// */
//
//public class LoginFilter implements Filter {
//
//	@Override
//	public void init(FilterConfig filterConfig) throws ServletException {
//
//	}
//
//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//			throws IOException, ServletException {
//		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//		String path = httpServletRequest.getPathInfo();
//		HttpSession session = httpServletRequest.getSession(false);
//		if (path.contains("/login/")) {
//			chain.doFilter(request, response);
//			return;
//		}
//		if (path.contains("/logout/")) {
//			chain.doFilter(request, response);
//			return;
//		}
//		if (session == null) {
//			throw new ServletException("you are not logged in");
//		}
//		if (path.startsWith("/admin")) {
//			if (session.getAttribute("admin") != null) {
//				chain.doFilter(request, response);
//				return;
//			}
//			throw new ServletException("access denied");
//		}
//		if (path.startsWith("/company")) {
//			if (session.getAttribute("company") != null) {
//				chain.doFilter(request, response);
//				return;
//			}
//			throw new ServletException("access denied");
//		}
//		if (path.startsWith("/customer")) {
//			if (session.getAttribute("customer") != null) {
//				chain.doFilter(request, response);
//				return;
//			}
//			throw new ServletException("access denied");
//		}
//
//	}
//
//	@Override
//	public void destroy() {
//
//	}
//
//}
