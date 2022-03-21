package com.daekyo.rms.com.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.RequestMapping;

import com.daekyo.rms.com.dao.ComCommDAO;
import com.daekyo.rms.com.service.ComCommService;
import com.daekyo.rms.util.ExcelUtil;
import com.daekyo.rms.util.Util;

@Service("ComCommService")
public class ComCommServiceImpl implements ComCommService {
	
	 private static final Logger logger = LoggerFactory.getLogger(ComCommServiceImpl.class);

     @Autowired
     private ComCommDAO comCommMapper;
     
     Util utils = new Util();
     
 	@Override
 	public List getCommCode(HttpServletRequest request) throws Exception {
 		return comCommMapper.getCommCode();

 	}

	@Override
	public List selectMenuList(HashMap paramMap) {
		return comCommMapper.selectMenuList(paramMap);
	}

	@Override
	public int menuAuthCheck(HashMap paramMap) {
		return comCommMapper.menuAuthCheck(paramMap);
	}

	@Override
	public List getMenu(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		
		String line = "";
		String active_bcg_id = "";
		String zauth_grp = (String) session.getAttribute("zauth_grp");
		
		// Json으로 변수 받음
		BufferedReader reader = request.getReader();
		while ((line = reader.readLine()) != null) {
			JSONObject jsonObj = (JSONObject) JSONValue.parse(line);

			active_bcg_id = (String) jsonObj.get("active_bcg_id");
		}
		
		if(active_bcg_id.equals("")) {
			if(!session.getAttribute("zregion_gb").equals("A")) {
				active_bcg_id = (String) session.getAttribute("zregion_gb");
			} else {
				active_bcg_id = "N";
			}
		}
		
		// 파라미터
		HashMap paramMap = new HashMap();
		paramMap.put("active_bcg_id", active_bcg_id);
		paramMap.put("zauth_grp", zauth_grp);
		
		return comCommMapper.getMenu(paramMap);
	}
}


