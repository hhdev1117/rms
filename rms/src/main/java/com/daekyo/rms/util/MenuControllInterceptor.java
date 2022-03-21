package com.daekyo.rms.util;

import java.io.PrintWriter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.daekyo.rms.com.service.ComCommService;

// 메뉴이동시 권한체크용 인터셉터
public class MenuControllInterceptor extends HandlerInterceptorAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(MenuControllInterceptor.class);

	/*
	 * 해당 어노테이션은 메뉴인터셉터를 안타게 하려고 만든것이다.
	 * servlet-context에서 처리할수 있지만
	 * 일일이 처리 안하는 메뉴 찾아서 하기 힘드므로 아래와 같이 처리한다.
	 */
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface MenuCheckInterceptor {
		boolean value() default true;
		String flag() default "MENU";
	}
	
	@Resource(name = "ComCommService")
	private ComCommService comCommService;
	
	/*
	 * 권한 체크하는 function
	 */
	@SuppressWarnings("unchecked")
	private boolean menuAuthCheck(HttpSession session, String zmenu_url) {
		
		String zauth_grp = (String) session.getAttribute("zauth_grp");
		String zregion_gb = (String) session.getAttribute("zregion_gb");
		
		HashMap paramMap = new HashMap();
		paramMap.put("zauth_grp", zauth_grp);
		paramMap.put("zregion_gb", zregion_gb);
		paramMap.put("zmenu_url", zmenu_url);
		
		if(comCommService.menuAuthCheck(paramMap) > 0) {
			return true; // 권한 있음
		} else {
			return false; // 권한 없음
		}
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		MenuCheckInterceptor usingAuth = ((HandlerMethod) handler).getMethodAnnotation(MenuCheckInterceptor.class);
		
		if(usingAuth == null || usingAuth.value() == false) {
			return true;
		}
		// 인터셉터 적용되는 펑션일 경우 권한체크
		else {
			// 체크하는 권한이 메뉴인경우
			if(usingAuth.flag().equals("MENU")) {
				if(menuAuthCheck(request.getSession(), request.getServletPath())) {
					return true;
				} else {
					response.setContentType("text/html; charset=utf-8");
					PrintWriter out = response.getWriter();
					out.print("<script>");
					out.print("alert('접근 권한이 없습니다.');");
					out.print("window.history.back();");
					out.print("</script>");
					out.flush();
					return false;
				}
			}
			else if(usingAuth.flag().equals("EXCEL")) {
				if(menuAuthCheck(request.getSession(), request.getServletPath())) {
					return true;
				} else {
					response.sendError(401);
					return false;
				}
			}
			// 메뉴 이외에 기타등등
			else {
				if(menuAuthCheck(request.getSession(), request.getServletPath())) {
					return true;
				} else {
					// 권한부족 에러코드
					response.sendError(401);
					return false;
				}
			}
		}
	}
}