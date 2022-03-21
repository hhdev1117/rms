package com.daekyo.rms.com.control;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.daekyo.rms.com.service.ComCommService;
import com.daekyo.rms.com.service.ComMainService;
import com.daekyo.rms.com.vo.MainVO;

@Controller
public class ComMainController {

	private static final Logger logger = LoggerFactory.getLogger(ComMainController.class);

	@Autowired
	MessageSource messageSource;

	@Resource(name = "ComMainService")
	private ComMainService comMainService;
	
	@Resource(name = "ComCommService")
	private ComCommService comCommService;

	/**
	 * 메인화면 처리
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/main.do")
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		String zregion = (String) session.getAttribute("zregion");
		String zregion_nm = (String) session.getAttribute("zregion_nm");
		String zdept_id = (String) session.getAttribute("zdept_id");
		String zdept_nm = (String) session.getAttribute("zdept_nm");
		String zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
		String zemp_id = (String) session.getAttribute("zemp_id");
		String zemp_nm = (String) session.getAttribute("zemp_nm");
		String zauth_grp = (String) session.getAttribute("zauth_grp");
		String zregion_gb = (String) session.getAttribute("zregion_gb");

		MainVO vo = null;
		List menuList = null;
		HashMap paramMap = new HashMap();
		paramMap.put("session_zregion", zregion);
		paramMap.put("session_zdept_id", zdept_id);
		paramMap.put("session_zdept_brch_id", zdept_brch_id);
		paramMap.put("session_zemp_id", zemp_id);
		paramMap.put("session_zregion_gb", zregion_gb);
		paramMap.put("session_zauth_grp", zauth_grp);

		try {
			menuList = comCommService.getMenu(request);
			vo = comMainService.selectMain(paramMap);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("com/main");
		mav.addObject("vo", vo);
		mav.addObject("zlogin_id", session.getAttribute("zlogin_id"));
		mav.addObject("menuList", menuList);
		mav.addObject("zauth_grp", zauth_grp);
		mav.addObject("zregion", zregion);
		mav.addObject("zregion_nm", zregion_nm);
		mav.addObject("zdept_id", zdept_id);
		mav.addObject("zdept_nm", zdept_nm);
		mav.addObject("zdept_brch_id", zdept_brch_id);
		mav.addObject("zemp_nm", zemp_nm);
		mav.addObject("zregion_gb", zregion_gb);
		/*
		 * 권한부여하는 부분 
		 * auth가 GOOD이 아니면 
		 * 권한이 없으므로 내보낸다.
		 */
		request.setAttribute("auth", "GOOD");

		return mav;
	}
	
	/**
	 * 인덱스 처리
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/index.do")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		
		String url = request.getParameter("url");
		String active_bcg_id = request.getParameter("active_bcg_id");
		String param_search_gb = request.getParameter("param_search_gb");
		String active_menu_id = request.getParameter("active_menu_id");
		String active_sub_menu_id = request.getParameter("active_sub_menu_id");
		
		if(session.getAttribute("zregion_gb").equals("N") || session.getAttribute("zregion_gb").equals("A")) {
			if(url == null) 				url = "/sum/sumNoonMoni.do";
			if(active_bcg_id == null)		active_bcg_id = "N";
			if(active_menu_id == null) 		active_menu_id = "menu1";
			if(active_sub_menu_id == null) 	active_sub_menu_id = "";
		} else if(session.getAttribute("zregion_gb").equals("C")) {
			if(url == null) 				url = "/sum/sumChiMoni.do";
			if(active_bcg_id == null)		active_bcg_id = "C";
			if(active_menu_id == null) 		active_menu_id = "menu1";
			if(active_sub_menu_id == null) 	active_sub_menu_id = "";
		} else {
			if(url == null) 				url = "/sum/sumSolMoni.do";
			if(active_bcg_id == null)		active_bcg_id = "S";
			if(active_menu_id == null) 		active_menu_id = "menu1";
			if(active_sub_menu_id == null) 	active_sub_menu_id = "";
		}

		ModelAndView mav = new ModelAndView();
		mav.addObject("zregion_nm", session.getAttribute("zregion_nm"));
		mav.addObject("zdept_nm", session.getAttribute("zdept_nm"));
		mav.addObject("zemp_nm", session.getAttribute("zemp_nm"));
		mav.addObject("zregion_gb", session.getAttribute("zregion_gb"));
		mav.addObject("zregion", session.getAttribute("zregion"));
		mav.addObject("zdept_id", session.getAttribute("zdept_id"));
		mav.addObject("zdept_brch_id", session.getAttribute("zdept_brch_id"));
		mav.addObject("zemp_id", session.getAttribute("zemp_id"));
		
		mav.addObject("page_id", url);
		mav.addObject("active_bcg_id", active_bcg_id);
		mav.addObject("active_menu_id", active_menu_id);
		mav.addObject("param_search_gb", param_search_gb);
		mav.addObject("active_sub_menu_id", active_sub_menu_id);
		
		request.setAttribute("auth", "GOOD");
		
		mav.setViewName("com/index");
		
		return mav;
	}
	
	/**
	 * blank 화면 처리
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/blank.do")
	public String blank(HttpServletRequest request, HttpServletResponse response) {
		
		logger.info("[Controll] : /blank");
		return "com/blank";
	}
	
	/**
	 * 로그아웃 처리
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/logout.do")
	public String logout(HttpServletRequest request, HttpServletResponse response) {

		logger.info("[Controll] : /logout");		

		HttpSession session = request.getSession();
		session.invalidate();
		
		
		return "com/logout";
	}

}
