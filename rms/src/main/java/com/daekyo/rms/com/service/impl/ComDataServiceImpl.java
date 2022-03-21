package com.daekyo.rms.com.service.impl;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daekyo.rms.com.dao.ComDataDAO;
import com.daekyo.rms.com.service.ComDataService;

@Service("comDataService")
public class ComDataServiceImpl implements ComDataService {

	private static final Logger logger = LoggerFactory.getLogger(ComDataServiceImpl.class);

	@Autowired
	private ComDataDAO dataMapper;

	/**
	 * 본부 조회
	 */
	@Override
	public List getZregion(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		
		String line = "";
		String active_bcg_id = "";
		String zregion = (String) session.getAttribute("zregion");
		String zauth_grp = (String) session.getAttribute("zauth_grp");
		String session_zregion_gb = (String) session.getAttribute("zregion_gb");
		
		// Json으로 변수 받음
		BufferedReader reader = request.getReader();
		while ((line = reader.readLine()) != null) {
			JSONObject jsonObj = (JSONObject) JSONValue.parse(line);

			active_bcg_id = (String) jsonObj.get("active_bcg_id");
		}
		
		HashMap paramMap = new HashMap();
		paramMap.put("active_bcg_id", active_bcg_id);
		paramMap.put("zregion", zregion);
		paramMap.put("zauth_grp", zauth_grp);
		paramMap.put("session_zregion_gb", session_zregion_gb);
		
		return dataMapper.getZregion(paramMap);
	}

	/**
	 * 사업국 조회
	 */
	@Override
	public List getVkbur(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		
		String line = "";
		String zregion = "";
		String zdept_id = (String) session.getAttribute("zdept_id");
		String zauth_grp = (String) session.getAttribute("zauth_grp");
		
		// Json으로 변수 받음
		BufferedReader reader = request.getReader();
		while ((line = reader.readLine()) != null) {
			JSONObject jsonObj = (JSONObject) JSONValue.parse(line);

			zregion = (String) jsonObj.get("zregion");
		}
		
		// 파라미터
		HashMap paramMap = new HashMap();
		paramMap.put("zregion", zregion);
		paramMap.put("zdept_id", zdept_id);
		paramMap.put("zauth_grp", zauth_grp);
		
		return dataMapper.getVkbur(paramMap);
	}

	/**
	 * 팀 조회
	 */
	@Override
	public List getVkgrp(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		
		String line = "";
		String vkbur = "";
		String zdept_id = (String) session.getAttribute("zdept_id");
		String zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
		String zauth_grp = (String) session.getAttribute("zauth_grp");
		
		// Json으로 변수 받음
		BufferedReader reader = request.getReader();
		while ((line = reader.readLine()) != null) {
			JSONObject jsonObj = (JSONObject) JSONValue.parse(line);

			vkbur = (String) jsonObj.get("vkbur");
		}
		
		// 파라미터
		HashMap paramMap = new HashMap();
		paramMap.put("vkbur", vkbur);
		paramMap.put("zdept_id", zdept_id);
		paramMap.put("zdept_brch_id", zdept_brch_id);
		paramMap.put("zauth_grp", zauth_grp);
		
		return dataMapper.getVkgrp(paramMap);
	}

	/**
	 * 선생님 조회
	 */
	@Override
	public List getZemp(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		
		String line = "";
		String zregion = "";
		String vkbur = "";
		String vkgrp = "";
		String zemp_id = (String) session.getAttribute("zemp_id");
		String zauth_grp = (String) session.getAttribute("zauth_grp");
		
		// Json으로 변수 받음
		BufferedReader reader = request.getReader();
		while ((line = reader.readLine()) != null) {
			JSONObject jsonObj = (JSONObject) JSONValue.parse(line);

			zregion = (String) jsonObj.get("zregion");
			vkbur = (String) jsonObj.get("vkbur");
			vkgrp = (String) jsonObj.get("vkgrp");
		}
		
		// 파라미터
		HashMap paramMap = new HashMap();
		paramMap.put("zregion", zregion);
		paramMap.put("vkbur", vkbur);
		paramMap.put("vkgrp", vkgrp);
		paramMap.put("zemp_id", zemp_id);
		paramMap.put("zauth_grp", zauth_grp);
		
		return dataMapper.getZemp(paramMap);
	}

	/**
	 * 공통코드 조회
	 */
	@Override
	public List selectCodeList(String zgrcode) throws Exception {
		return dataMapper.selectCodeList(zgrcode);
	}
	
}
