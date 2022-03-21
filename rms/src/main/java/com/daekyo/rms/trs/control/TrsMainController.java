package com.daekyo.rms.trs.control;

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
import com.daekyo.rms.trs.service.TrsMainService;
import com.daekyo.rms.util.MenuControllInterceptor.MenuCheckInterceptor;
import com.daekyo.rms.util.Util;

@Controller
@RequestMapping("/trs")
public class TrsMainController {

	private static final Logger logger = LoggerFactory.getLogger(TrsMainController.class);

	@Autowired
	MessageSource messageSource;

	@Resource(name = "trsMainService")
	private TrsMainService trsMainService;

	/**
	 * [눈높이] 자동이체 중복 화면처리
	 */
	@MenuCheckInterceptor
	@RequestMapping("/trsNoonDup.do")
	public ModelAndView crdNoonCardPayment(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "trs/trsNoonDup", "N");
	}

	/**
	 * [차이홍] 자동이체 중복 화면처리
	 */
	@MenuCheckInterceptor
	@RequestMapping("/trsChiDup.do")
	public ModelAndView crdChiCardPayment(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "trs/trsChiDup", "C");
	}

	/**
	 * [솔루니] 자동이체 중복 화면처리
	 */
	@MenuCheckInterceptor
	@RequestMapping("/trsSolDup.do")
	public ModelAndView crdSolCardPayment(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "trs/trsSolDup", "S");
	}

	/**
	 * 자동이체 중복 조회
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "SEARCH")
	@RequestMapping("/getTrsDup.do")
	public List getCardPayment(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[TRS_CONTROLLER] : " + request.getServletPath());

		List resultList = null;

		try {
			resultList = trsMainService.getTrsDup(request);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}

		return resultList;
	}

}
