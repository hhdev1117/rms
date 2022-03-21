package com.daekyo.rms.arr.control;

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

import com.daekyo.rms.arr.service.ArrMainService;
import com.daekyo.rms.util.Util;
import com.daekyo.rms.util.MenuControllInterceptor.MenuCheckInterceptor;

@Controller
@RequestMapping("/arr")
public class ArrMainController {

	private static final Logger logger = LoggerFactory.getLogger(ArrMainController.class);

	@Autowired
	MessageSource messageSource;

	@Resource(name = "arrMainService")
	private ArrMainService arrMainService;
	
	/**
	 * [눈높이] 2개월 체납 화면처리
	 */
	@MenuCheckInterceptor
	@RequestMapping("/arrNoonPay.do")
	public ModelAndView arrNoonPayment(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "arr/arrNoonPay", "N");
	}
	
	/**
	 * [차이홍] 2개월 체납 화면처리
	 */
	@MenuCheckInterceptor
	@RequestMapping("/arrChiPay.do")
	public ModelAndView arrChiPayment(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "arr/arrChiPay", "C");
	}
	
	/**
	 * [솔루니] 2개월 체납 화면처리
	 */
	@MenuCheckInterceptor
	@RequestMapping("/arrSolPay.do")
	public ModelAndView arrSolPayment(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "arr/arrSolPay", "S");
	}
	
	/**
	 * 2개월 체납 조회
	 */
	@ResponseBody
	@SuppressWarnings("cast")
	@MenuCheckInterceptor(value = true, flag = "SEARCH")
	@RequestMapping("/getArrPayment.do")
	public List getArrPayment(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[ARR_CONTROLLER] : "  + request.getServletPath());
		
		List resultList = null;
		
		try {
			resultList = arrMainService.getArrPayment(request);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}

		return resultList;
	}

}
