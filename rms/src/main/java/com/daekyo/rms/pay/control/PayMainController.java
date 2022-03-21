package com.daekyo.rms.pay.control;

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

import com.daekyo.rms.pay.service.PayMainService;
import com.daekyo.rms.util.Util;
import com.daekyo.rms.util.MenuControllInterceptor.MenuCheckInterceptor;

@Controller
@RequestMapping("/pay")
public class PayMainController {

	private static final Logger logger = LoggerFactory.getLogger(PayMainController.class);

	@Autowired
	MessageSource messageSource;

	@Resource(name = "payMainService")
	private PayMainService payMainService;
	
	/**
	 * [눈높이] 직접결제 중복발송 화면처리
	 */
	@MenuCheckInterceptor
	@RequestMapping("/payNoonDirect.do")
	public ModelAndView payNoonDirect(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "pay/payNoonDirect", "N");
	}
	
	/**
	 * [차이홍] 직접결제 중복발송 화면처리
	 */
	@MenuCheckInterceptor
	@RequestMapping("/payChiDirect.do")
	public ModelAndView payChiDirect(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "pay/payChiDirect", "C");
	}
	
	/**
	 * [솔루니] 직접결제 중복발송 화면처리
	 */
	@MenuCheckInterceptor
	@RequestMapping("/paySolDirect.do")
	public ModelAndView paySolDirect(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "pay/paySolDirect", "S");
	}
	
	/**
	 * 직접결제 중복발송 조회
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "SEARCH")
	@RequestMapping("/getDirectPayment.do")
	public List getDirectPayment(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[PAY_CONTROLLER] : " + request.getServletPath());
		
		List resultList = null;
		
		try {
			resultList = payMainService.getDirectPayment(request);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}

		return resultList;
	}

}
