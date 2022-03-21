package com.daekyo.rms.sum.control;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.daekyo.rms.sum.service.SumMainService;
import com.daekyo.rms.sum.vo.SumMainVO;
import com.daekyo.rms.util.Util;
import com.daekyo.rms.util.MenuControllInterceptor.MenuCheckInterceptor;

@Controller
@RequestMapping("/sum")
public class SumMainController {

	private static final Logger logger = LoggerFactory.getLogger(SumMainController.class);

	@Autowired
	MessageSource messageSource;

	@Resource(name = "sumMainService")
	private SumMainService sumMainService;

	/**
	 * [눈높이] 주요지표 화면 처리
	 */
	@MenuCheckInterceptor
	@RequestMapping("/sumNoonMoni.do")
	public ModelAndView sumNoonMoniMain(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "sum/sumNoonMoni", "N");
	}
	
	/**
	 * [차이홍] 주요지표 화면 처리
	 */
	@MenuCheckInterceptor
	@RequestMapping("/sumChiMoni.do")
	public ModelAndView sumChiMoni(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "sum/sumChiMoni", "C");
	}
	
	/**
	 * [솔루니] 주요지표 화면 처리
	 */
	@MenuCheckInterceptor
	@RequestMapping("/sumSolMoni.do")
	public ModelAndView sumSolMoni(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "sum/sumSolMoni", "S");
	}
	
	/**
	 * [눈높이] 주요지표 순위 화면 처리
	 */
	@MenuCheckInterceptor
	@RequestMapping("/sumNoonRank.do")
	public ModelAndView sumNoonRankMain(HttpServletRequest request, HttpServletResponse response) {
		return Util.customMav(request, "sum/sumNoonRank", "N");
	}
	
	/**
	 * 주요지표 조회
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "SEARCH")
	@RequestMapping("/getSumMoni.do")
	public List getSumMoni(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[SUM_CONTROLLER] : "  + request.getServletPath());
		
		List resultList = new ArrayList();
		
		try {
			resultList.add(sumMainService.getSumMoni(request));
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}

		return resultList;
	}
	
	/**
	 * 주요지표 팀 이하 조회
	 */
	@ResponseBody
	@RequestMapping("/getSumMoniDtl.do")
	public List getSumMoniDtl(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[SUM_CONTROLLER] : "  + request.getServletPath());
		
		List resultList = new ArrayList();
		
		try {
			resultList.add(sumMainService.getSumMoniDtl(request));
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}

		return resultList;
	}
	
	/**
	 * 주요지표 순위 조회
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "SEARCH")
	@RequestMapping("/getSumRank.do")
	public List getSumRank(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[SUM_CONTROLLER] : "  + request.getServletPath());
		
		List resultList = new ArrayList();
		
		try {
			resultList = sumMainService.getSumRank(request);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}

		return resultList;
	}
}
