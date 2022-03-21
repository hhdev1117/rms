package com.daekyo.rms.stm.control;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.daekyo.rms.stm.service.StmMainService;
import com.daekyo.rms.util.Util;
import com.daekyo.rms.util.MenuControllInterceptor.MenuCheckInterceptor;

@Controller
@RequestMapping("/stm")
public class StmMainController {

	private static final Logger logger = LoggerFactory.getLogger(StmMainController.class);

	@Autowired
	MessageSource messageSource;

	@Resource(name = "stmMainService")
	private StmMainService stmMainService;
	
	/**
	 * [눈높이] 긴급교재 메인화면
	 */
	@MenuCheckInterceptor
	@RequestMapping("/stmNoonApply.do")
	public ModelAndView stmNoonApply(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "stm/stmNoonApply", "N");
	}
	
	/**
	 * 긴급교재 조회
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "SEARCH")
	@RequestMapping("/getStmApply.do")
	public List getStmApply(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[TRN_CONTROLLER] : "  + request.getServletPath());
		
		List resultList = null;
		
		try {
			resultList = stmMainService.getStmApply(request);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}

		return resultList;
	}

}
