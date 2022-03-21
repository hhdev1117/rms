package com.daekyo.rms.mon.control;

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

import com.daekyo.rms.mon.service.MonMainService;
import com.daekyo.rms.util.Util;
import com.daekyo.rms.util.MenuControllInterceptor.MenuCheckInterceptor;

@Controller
@RequestMapping("/mon")
public class MonMainController {

	private static final Logger logger = LoggerFactory.getLogger(MonMainController.class);

	@Autowired
	MessageSource messageSource;

	@Resource(name = "monMainService")
	private MonMainService monMainService;
	
	/**
	 * [눈높이] 월별 모니터링 화면처리
	 */
	@MenuCheckInterceptor
	@RequestMapping("/monNoonMoni.do")
	public ModelAndView monNoonMoni(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "mon/monNoonMoni", "N");
	}
	
	/**
	 * [차이홍] 월별 모니터링 화면처리
	 */
	@MenuCheckInterceptor
	@RequestMapping("/monChiMoni.do")
	public ModelAndView monChiMoni(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "mon/monChiMoni", "C");
	}
	
	/**
	 * [솔루니] 월별 모니터링 화면처리
	 */
	@MenuCheckInterceptor
	@RequestMapping("/monSolMoni.do")
	public ModelAndView monSolMoni(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "mon/monSolMoni", "S");
	}
	
	/**
	 * 월별 모니터링 조회
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "SEARCH")
	@RequestMapping("/getMonMoni.do")
	public List getMonMoni(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[PAY_CONTROLLER] : " + request.getServletPath());
		
		List resultList = null;
		
		try {
			resultList = monMainService.getMonMoni(request);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}

		return resultList;
	}

}
