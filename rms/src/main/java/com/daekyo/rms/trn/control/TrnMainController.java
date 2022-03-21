package com.daekyo.rms.trn.control;

import java.io.BufferedReader;
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

import com.daekyo.rms.trn.service.TrnMainService;
import com.daekyo.rms.util.Util;
import com.daekyo.rms.util.MenuControllInterceptor.MenuCheckInterceptor;

@Controller
@RequestMapping("/trn")
public class TrnMainController {

	private static final Logger logger = LoggerFactory.getLogger(TrnMainController.class);

	@Autowired
	MessageSource messageSource;

	@Resource(name = "trnMainService")
	private TrnMainService trnMainService;
	
	/**
	 * [눈높이] 이체제외 후 학습 메인화면
	 */
	@MenuCheckInterceptor
	@RequestMapping("/trnNoonStd.do")
	public ModelAndView trnNoonStudy(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "trn/trnNoonStd", "N");
	}
	
	/**
	 * [차이홍] 이체제외 후 학습 메인화면
	 */
	@MenuCheckInterceptor
	@RequestMapping("/trnChiStd.do")
	public ModelAndView trnChiStudy(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "trn/trnChiStd", "C");
	}
	
	/**
	 * [솔루니] 이체제외 후 학습 메인화면
	 */
	@MenuCheckInterceptor
	@RequestMapping("/trnSolStd.do")
	public ModelAndView trnSolStudy(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "trn/trnSolStd", "S");
	}
	
	/**
	 * 이체제외 후 학습 조회
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "SEARCH")
	@RequestMapping("/getTrnStudy.do")
	public List getTrnStudy(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[TRN_CONTROLLER] : "  + request.getServletPath());
		
		List resultList = null;
		
		try {
			resultList = trnMainService.getTrnStudy(request);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}

		return resultList;
	}

}
