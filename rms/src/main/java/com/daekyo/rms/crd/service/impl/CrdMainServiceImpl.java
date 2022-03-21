package com.daekyo.rms.crd.service.impl;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.daekyo.rms.crd.dao.CrdMainDAO;
import com.daekyo.rms.crd.service.CrdMainService;

@Service("crdMainService")
public class CrdMainServiceImpl implements CrdMainService {

	private static final Logger logger = LoggerFactory.getLogger(CrdMainServiceImpl.class);

	@Autowired
	private CrdMainDAO crdMainMapper;

	@Override
	public List getCardPayment(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		
		String line = "";
		String zregion = "";
		String zdept_id = "";
		String zdept_brch_id = "";
		String zemp_id = "";
		String zreq_ym_b = "";
		String zreq_ym_a = "";
		String active_bcg_id = "";
		String page = "1";
		
		String session_zregion = (String) session.getAttribute("zregion");
		String session_zdept_id = (String) session.getAttribute("zdept_id");
		String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
		String session_zemp_id = (String) session.getAttribute("zemp_id");
		String session_zauth_grp = (String) session.getAttribute("zauth_grp");
		String session_zregion_gb = (String) session.getAttribute("zregion_gb");
		
		// Json으로 변수 받음
		BufferedReader reader = request.getReader();
		while ((line = reader.readLine()) != null) {
			JSONObject jsonObj = (JSONObject) JSONValue.parse(line);

			zregion = (String) jsonObj.get("zregion");
			zdept_id = (String) jsonObj.get("zdept_id");
			zdept_brch_id = (String) jsonObj.get("zdept_brch_id");
			zemp_id = (String) jsonObj.get("zemp_id");
			zreq_ym_b = (String) jsonObj.get("zreq_ym_b");
			zreq_ym_a = (String) jsonObj.get("zreq_ym_a");
			active_bcg_id = (String) jsonObj.get("active_bcg_id");
			page = (String) jsonObj.get("page");
		}
		
		// 파라미터
		HashMap<String, String> paramMap = new HashMap();
		paramMap.put("zregion", zregion);
		paramMap.put("zdept_id", zdept_id);
		paramMap.put("zdept_brch_id", zdept_brch_id);
		paramMap.put("zemp_id", zemp_id);
		paramMap.put("page", page);
		paramMap.put("session_zregion", session_zregion);
		paramMap.put("session_zdept_id", session_zdept_id);
		paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
		paramMap.put("session_zemp_id", session_zemp_id);
		paramMap.put("session_zregion_gb", session_zregion_gb);
		paramMap.put("zreq_ym_b", zreq_ym_b);
		paramMap.put("zreq_ym_a", zreq_ym_a);
		paramMap.put("active_bcg_id", active_bcg_id);
		paramMap.put("session_zauth_grp", session_zauth_grp);
		
		return crdMainMapper.getCardPayment(paramMap);
	}

}
