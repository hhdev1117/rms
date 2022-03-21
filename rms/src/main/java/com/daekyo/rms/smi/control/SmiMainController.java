package com.daekyo.rms.smi.control;

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

import com.daekyo.rms.smi.service.SmiMainService;
import com.daekyo.rms.util.Util;
import com.daekyo.rms.util.MenuControllInterceptor.MenuCheckInterceptor;

@Controller
@RequestMapping("/smi")
public class SmiMainController {

	private static final Logger logger = LoggerFactory.getLogger(SmiMainController.class);

	@Autowired
	MessageSource messageSource;

	@Resource(name = "smiMainService")
	private SmiMainService smiMainService;
	
	/**
	 * [눈높이] 써밋제품 학습 메인화면
	 */
	@MenuCheckInterceptor
	@RequestMapping("/smiNoonRisk.do")
	public ModelAndView smiNoonRisk(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "smi/smiNoonRisk", "N");
	}
	
	/**
	 * 써밋제품 학습 조회
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "SEARCH")
	@RequestMapping("/getSmiRisk.do")
	public List getSmiRisk(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[SMI_CONTROLLER] : "  + request.getServletPath());
		
		List resultList = null;
		
		try {
			resultList = smiMainService.getSmiRisk(request);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}

		return resultList;
	}

}
