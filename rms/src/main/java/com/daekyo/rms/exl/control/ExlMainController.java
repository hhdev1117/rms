package com.daekyo.rms.exl.control;

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

import com.daekyo.rms.exl.service.ExlMainService;
import com.daekyo.rms.util.MenuControllInterceptor.MenuCheckInterceptor;

@Controller
@RequestMapping("/exl")
public class ExlMainController {

	private static final Logger logger = LoggerFactory.getLogger(ExlMainController.class);

	@Autowired
	MessageSource messageSource;

	@Resource(name = "exlMainService")
	private ExlMainService exlMainService;
	
	/**
	 * 엑셀다운 SESSION 체크용
	 * SESSION 체크 뿐만 아니라
	 * 해당권한을 막으면 엑셀다운 전체가 불가능해진다. 
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "EXCEL")
	@RequestMapping("/sessionCheck.do")
	public void sessionCheck(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[EXL_CONTROLLER] : "  + request.getServletPath());
	}
	
	/**
	 * [현장용] 주요지표 엑셀다운
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "EXCEL")
	@RequestMapping("/excelMoniSum_d.do")
	public void excelMoniSum_d(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[EXL_CONTROLLER] : "  + request.getServletPath());
		
		try {
			if(request.getParameter("cnt_gb").equals("ind")) exlMainService.excelMoniSum_d(request, response);
			else exlMainService.excelMoniSum_d_cnt(request, response);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}
	}
	
	/**
	 * [관리용] 주요지표 엑셀다운
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "EXCEL")
	@RequestMapping("/excelMoniSum_ex_d.do")
	public void excelMoniSum_da(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[EXL_CONTROLLER] : "  + request.getServletPath());
		
		try {
			if(request.getParameter("cnt_gb").equals("ind")) exlMainService.excelMoniSum_ex_d(request, response);
			else exlMainService.excelMoniSum_ex_d_cnt(request, response);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}
	}
	
	/**
	 * [현장용] 카드결제 현황 엑셀 다운로드
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "EXCEL")
	@RequestMapping("/excelCardPayment_d.do")
	public void excelCardPayment_d(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[EXL_CONTROLLER] : "  + request.getServletPath());
		
		try {
			exlMainService.excelCardPayment_d(request, response);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}
	}
	
	/**
	 * [관리용] 카드결제 현황 엑셀 다운로드
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "EXCEL")
	@RequestMapping("/excelCardPayment_da.do")
	public void excelCardPayment_da(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[EXL_CONTROLLER] : "  + request.getServletPath());
		
		try {
			exlMainService.excelCardPayment_da(request, response);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}
	}
	
	/**
	 * [현장용] 이체제외 후 학습 엑셀 다운로드
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "EXCEL")
	@RequestMapping("/excelTranStudy_d.do")
	public void excelTranStudy_d(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[EXL_CONTROLLER] : "  + request.getServletPath());
		
		try {
			exlMainService.excelTranStudy_d(request, response);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}
	}
	
	/**
	 * [관리용] 이체제외 후 학습 엑셀 다운로드
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "EXCEL")
	@RequestMapping("/excelTranStudy_da.do")
	public void excelTranStudy_da(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[EXL_CONTROLLER] : "  + request.getServletPath());
		
		try {
			exlMainService.excelTranStudy_da(request, response);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}
	}
	
	/**
	 * [현장용] 개인정보 인증 엑셀 다운로드
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "EXCEL")
	@RequestMapping("/excelIndAuth_d.do")
	public void excelIndAuth_d(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[EXL_CONTROLLER] : "  + request.getServletPath());
		
		try {
			exlMainService.excelIndAuth_d(request, response);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}
	}
	
	/**
	 * [관리용] 개인정보 인증 엑셀 다운로드
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "EXCEL")
	@RequestMapping("/excelIndAuth_da.do")
	public void excelIndAuth_da(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[EXL_CONTROLLER] : "  + request.getServletPath());
		
		try {
			exlMainService.excelIndAuth_da(request, response);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}
	}
	
	/**
	 * [현장용] 2개월 체납 엑셀 다운로드
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "EXCEL")
	@RequestMapping("/excelArrPayment_d.do")
	public void excelArrPayment_d(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[EXL_CONTROLLER] : "  + request.getServletPath());
		
		try {
			exlMainService.excelArrPayment_d(request, response);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}
	}
	
	/**
	 * [관리용] 2개월 체납 엑셀 다운로드
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "EXCEL")
	@RequestMapping("/excelArrPayment_da.do")
	public void excelArrPayment_da(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[EXL_CONTROLLER] : "  + request.getServletPath());
		
		try {
			exlMainService.excelArrPayment_da(request, response);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}
	}
	
	/**
	 * [현장용] 직접결제 중복발송 엑셀 다운로드
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "EXCEL")
	@RequestMapping("/excelPayDirect_d.do")
	public void excelPayDirect_d(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[EXL_CONTROLLER] : "  + request.getServletPath());
		
		try {
			exlMainService.excelPayDirect_d(request, response);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}
	}
	
	/**
	 * [관리용] 직접결제 중복발송 엑셀 다운로드
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "EXCEL")
	@RequestMapping("/excelPayDirect_da.do")
	public void excelPayDirect_da(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[EXL_CONTROLLER] : "  + request.getServletPath());
		
		try {
			exlMainService.excelPayDirect_da(request, response);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}
	}
	
	/**
	 * [현장용] 긴급교재 엑셀 다운로드
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "EXCEL")
	@RequestMapping("/excelStmApply_d.do")
	public void excelStmApply_d(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[EXL_CONTROLLER] : "  + request.getServletPath());
		
		try {
			exlMainService.excelStmApply_d(request, response);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}
	}
	
	/**
	 * [관리용] 긴급교재 엑셀 다운로드
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "EXCEL")
	@RequestMapping("/excelStmApply_da.do")
	public void excelStmApply_da(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[EXL_CONTROLLER] : "  + request.getServletPath());
		
		try {
			exlMainService.excelStmApply_da(request, response);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}
	}
	
	/**
	 * [현장용] 써밋제품미학습Risk 엑셀 다운로드
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "EXCEL")
	@RequestMapping("/excelSmiRisk_d.do")
	public void excelSmiRisk_d(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[EXL_CONTROLLER] : "  + request.getServletPath());
		
		try {
			exlMainService.excelSmiRisk_d(request, response);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}
	}
	
	/**
	 * [관리용] 써밋제품미학습Risk 엑셀 다운로드
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "EXCEL")
	@RequestMapping("/excelSmiRisk_da.do")
	public void excelSmiRisk_da(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[EXL_CONTROLLER] : "  + request.getServletPath());
		
		try {
			exlMainService.excelSmiRisk_da(request, response);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}
	}
	
	/**
	 * [현장용] 월별 모니터링 엑셀 다운로드
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "EXCEL")
	@RequestMapping("/excelMonMoni_d.do")
	public void excelMonMoni_d(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[EXL_CONTROLLER] : "  + request.getServletPath());
		
		try {
			if(request.getParameter("cnt_gb").equals("ind")) exlMainService.excelMonMoni_d(request, response);
			else exlMainService.excelMonMoni_d_cnt(request, response);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}
	}
	
	/**
	 * [현장용] 0개월학습 엑셀 다운로드
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "EXCEL")
	@RequestMapping("/excelNstZeroStudy_d.do")
	public void excelNstZeroStudy_d(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[EXL_CONTROLLER] : "  + request.getServletPath());
		
		try {
			exlMainService.excelNstZeroStudy_d(request, response);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}
	}
	
	/**
	 * [관리용] 0개월학습 엑셀 다운로드
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "EXCEL")
	@RequestMapping("/excelNstZeroStudy_da.do")
	public void excelNstZeroStudy_da(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[EXL_CONTROLLER] : "  + request.getServletPath());
		
		try {
			exlMainService.excelNstZeroStudy_da(request, response);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}
	}
	
	/**
	 * 주요지표 랭킹 엑셀다운
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "EXCEL")
	@RequestMapping("/excelMoniSum_r.do")
	public void excelMoniSum_r(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[EXL_CONTROLLER] : "  + request.getServletPath());
		
		try {
			exlMainService.excelMoniSum_r(request, response);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}
	}
	
	/**
	 * [현장용] 자동이체중복 엑셀다운
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "EXCEL")
	@RequestMapping("/excelTrsDup_d.do")
	public void excelTrsDup_d(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[EXL_CONTROLLER] : "  + request.getServletPath());
		
		try {
			exlMainService.excelTrsDup_d(request, response);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}
	}
	
	/**
	 * [관리용] 자동이체중복 엑셀다운
	 */
	@ResponseBody
	@MenuCheckInterceptor(value = true, flag = "EXCEL")
	@RequestMapping("/excelTrsDup_da.do")
	public void excelTrsDup_da(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("[EXL_CONTROLLER] : "  + request.getServletPath());
		
		try {
			exlMainService.excelTrsDup_da(request, response);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}
	}

}
