package com.daekyo.rms.com.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.daekyo.rms.com.service.ComDataService;

@Controller
public class ComDataController {

	private static final Logger logger = LoggerFactory.getLogger(ComDataController.class);

	@Resource(name = "comDataService")
	private ComDataService comDataService;
	
	/**
	 * 본부 조회
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ServletRequestBindingException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("/getZregion.do")
	public Object getZregion(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws ServletRequestBindingException, IOException {

		logger.debug("CONTROLL : /getZregion.do");
		
		List resultList = null;
		try {
			resultList = comDataService.getZregion(request);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}

		return resultList;
	}

	/**
	 * 사업국 조회
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ServletRequestBindingException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("/getVkbur.do")
	public Object getVkbur(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws ServletRequestBindingException, IOException {

		logger.debug("CONTROLL : /getVkbur.do");

		List resultList = null;
		try {
			resultList = comDataService.getVkbur(request);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}

		return resultList;
	}
	
	/**
	 * 팀 조회
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ServletRequestBindingException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("/getVkgrp.do")
	public Object getVkgrp(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws ServletRequestBindingException, IOException {

		logger.debug("CONTROLL : /getVkgrp.do");
		
		List resultList = null;
		try {
			resultList = comDataService.getVkgrp(request);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}

		return resultList;
	}
	
	/**
	 * 선생님 조회
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ServletRequestBindingException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("/getZemp.do")
	public Object getZemp(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws ServletRequestBindingException, IOException {

		logger.debug("CONTROLL : /getZemp.do");
		
		List resultList = null;
		try {
			resultList = comDataService.getZemp(request);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}

		return resultList;
	}

	/**
	 * 공통 코드 조회
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ServletRequestBindingException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("/com/getCommonCode")
	public Object getCode(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws ServletRequestBindingException, IOException {

		String line = null;
		
		String zgrcode = "";
		List resultList = null;
		
		try {

			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				JSONObject jsonObj = (JSONObject) JSONValue.parse(line);

				zgrcode = (String) jsonObj.get("zgrcode");
			}
			
			resultList = comDataService.selectCodeList(zgrcode);
			
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}

		return resultList;
	}
}
