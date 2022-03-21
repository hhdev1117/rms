package com.daekyo.rms.crd.control;

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
import com.daekyo.rms.crd.service.CrdMainService;
import com.daekyo.rms.util.MenuControllInterceptor.MenuCheckInterceptor;
import com.daekyo.rms.util.Util;

@Controller
@RequestMapping("/crd")
public class CrdMainController {

	private static final Logger logger = LoggerFactory.getLogger(CrdMainController.class);

	@Autowired
	MessageSource messageSource;

	@Resource(name = "crdMainService")
	private CrdMainService crdMainService;

	/**
	 * [눈높이] 카드결제 현황 화면처리
	 */
	@MenuCheckInterceptor
	@RequestMapping("/crdNoonCard.do")
	public ModelAndView crdNoonCardPayment(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "crd/crdNoonCard", "N");
	}

	/**
	 * [차이홍] 카드결제 현황 화면처리
	 */
	@MenuCheckInterceptor
	@RequestMapping("/crdChiCard.do")
	public ModelAndView crdChiCardPayment(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "crd/crdChiCard", "C");
	}

	/**
	 * [솔루니] 카드결제 현황 화면처리
	 */
	@MenuCheckInterceptor
	@RequestMapping("/crdSolCard.do")
	public ModelAndView crdSolCardPayment(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "crd/crdSolCard", "S");
	}

	/**
	 * 카드결제 현황 조회
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "SEARCH")
	@RequestMapping("/getCardPayment.do")
	public List getCardPayment(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[CRD_CONTROLLER] : " + request.getServletPath());

		List resultList = null;

		try {
			resultList = crdMainService.getCardPayment(request);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}

		return resultList;
	}

}
