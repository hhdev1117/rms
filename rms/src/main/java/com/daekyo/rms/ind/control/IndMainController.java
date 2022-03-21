package com.daekyo.rms.ind.control;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.daekyo.rms.ind.service.IndMainService;
import com.daekyo.rms.util.Util;
import com.daekyo.rms.util.MenuControllInterceptor.MenuCheckInterceptor;

@Controller
@RequestMapping("/ind")
public class IndMainController {

	private static final Logger logger = LoggerFactory.getLogger(IndMainController.class);

	@Autowired
	MessageSource messageSource;

	@Resource(name = "indMainService")
	private IndMainService indMainService;
	
	/**
	 * [눈높이] 개인정보 인증 화면처리
	 */
	@MenuCheckInterceptor
	@RequestMapping("/indNoonAuth.do")
	public ModelAndView indNoonAuthorization(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "ind/indNoonAuth", "N");
	}
	
	/**
	 * [차이홍] 개인정보 인증 화면처리
	 */
	@MenuCheckInterceptor
	@RequestMapping("/indChiAuth.do")
	public ModelAndView indChiAuthorization(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "ind/indChiAuth", "C");
	}
	
	/**
	 * [솔루니] 카드결제 현황 화면처리
	 */
	@MenuCheckInterceptor
	@RequestMapping("/indSolAuth.do")
	public ModelAndView indSolAuthorization(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "ind/indSolAuth", "S");
	}
	
	/**
	 * 개인정보 인증 조회
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "SEARCH")
	@RequestMapping("/getIndAuthorization.do")
	public List getIndAuthorization(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[IND_CONTROLLER] : "  + request.getServletPath());
		
		List resultList = null;
		
		try {
			resultList = indMainService.getIndAuthorization(request);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}

		return resultList;
	}

}
