package com.daekyo.rms.nst.control;

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

import com.daekyo.rms.nst.service.NstMainService;
import com.daekyo.rms.util.Util;
import com.daekyo.rms.util.MenuControllInterceptor.MenuCheckInterceptor;

@Controller
@RequestMapping("/nst")
public class NstMainController {

	private static final Logger logger = LoggerFactory.getLogger(NstMainController.class);

	@Autowired
	MessageSource messageSource;

	@Resource(name = "nstMainService")
	private NstMainService nstMainService;
	
	/**
	 * [눈높이] 0개월 학습
	 */
	@MenuCheckInterceptor
	@RequestMapping("/nstNoonZeroStudy.do")
	public ModelAndView nstNoonZeroStudy(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "nst/nstNoonZeroStudy", "N");
	}
	
	/**
	 * [차이홍] 0개월 학습
	 */
	@MenuCheckInterceptor
	@RequestMapping("/nstChiZeroStudy.do")
	public ModelAndView nstChiZeroStudy(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "nst/nstChiZeroStudy", "C");
	}
	
	/**
	 * [솔루니] 0개월 학습
	 */
	@MenuCheckInterceptor
	@RequestMapping("/nstSolZeroStudy.do")
	public ModelAndView nstSolZeroStudy(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "nst/nstSolZeroStudy", "S");
	}
	
	/**
	 * 0개월 학습 조회
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "SEARCH")
	@RequestMapping("/getZeroStudy.do")
	public List getZeroStudy(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[NST_CONTROLLER] : "  + request.getServletPath());
		
		List resultList = null;
		
		try {
			resultList = nstMainService.getZeroStudy(request);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}

		return resultList;
	}

}
