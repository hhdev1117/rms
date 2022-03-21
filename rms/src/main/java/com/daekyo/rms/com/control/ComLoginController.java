package com.daekyo.rms.com.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.daekyo.rms.com.dao.ComLoginDAO;
import com.daekyo.rms.com.service.ComLoginService;
import com.daekyo.rms.com.vo.MainVO;
import com.daekyo.rms.util.MessageUtils;
import com.daekyo.rms.util.Util;
import com.google.gson.Gson;

@Controller
public class ComLoginController {

	private static final Logger logger = LoggerFactory.getLogger(ComLoginController.class);

	@Resource(name = "comLoginService")
	private ComLoginService comLoginService;

	@Autowired
	private ComLoginDAO loginMapper;

	Util utils = new Util();

	MessageUtils msgUtil = new MessageUtils();

	/**
	 * 로그인 처리
	 * 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/com/checkLogin.do", method = RequestMethod.POST)
	public void checkLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletRequestBindingException, IOException {

		logger.debug("[Login_Controll] : /com/checkLogin.do");

		String zloginId = ServletRequestUtils.getRequiredStringParameter(request, "zhdloginId");
		String zpasswd = ServletRequestUtils.getRequiredStringParameter(request, "zhdpasswd");
		// 로그인 리턴값 생성
		Gson gson = new Gson();
		HashMap rtn_loginInfo = new HashMap();

		HashMap paramMap = new HashMap();
		paramMap.put("zloginId", zloginId);
		paramMap.put("zpasswd", zpasswd);
		paramMap.put("type", "0"); // type : 0-DMS 로그인페이지, 1-드림스 로그인링크

		if (zloginId.length() > 0 && zpasswd.length() > 0) {

			rtn_loginInfo = login_process(request, paramMap);

		} else {

			rtn_loginInfo.put("isLogin", false);
			rtn_loginInfo.put("msg", "아이디와 암호를 확인해주세요.");

		}

		response.getOutputStream().write(gson.toJson(rtn_loginInfo).getBytes("UTF-8"));
	}

	// 로그인 프로세스
	public HashMap login_process(HttpServletRequest request, HashMap paramMap) {

		HashMap rtn_loginInfo = new HashMap();

		try {
			rtn_loginInfo = comLoginService.checkLoginInfo(paramMap);

			if (!rtn_loginInfo.isEmpty()) {

				if (rtn_loginInfo.get("isLogin").equals(true)) {

					// 세션값 세팅
					HttpSession session = request.getSession();

					// 로그인 공통 세션
					session.setAttribute("zlogin_id", rtn_loginInfo.get("zlogin_id"));

					session.setAttribute("zregion", utils.isnull(rtn_loginInfo.get("zregion"), ""));
					session.setAttribute("zregion_nm", utils.isnull(rtn_loginInfo.get("zregion_nm"), ""));
					session.setAttribute("zdept_id", utils.isnull(rtn_loginInfo.get("zdept_id"), ""));
					session.setAttribute("zdept_nm", utils.isnull(rtn_loginInfo.get("zdept_nm"), ""));
					session.setAttribute("zdept_brch_id", utils.isnull(rtn_loginInfo.get("zdept_brch_id"), ""));

					session.setAttribute("zemp_id", utils.isnull(rtn_loginInfo.get("zemp_id"), ""));
					session.setAttribute("zemp_nm", utils.isnull(rtn_loginInfo.get("zemp_nm"), ""));

					session.setAttribute("zauth_grp", utils.isnull(rtn_loginInfo.get("zauth_grp"), ""));
					
					// 권한을 위해 사업구분을 세션에 설정해준다.
					if(session.getAttribute("zauth_grp").equals("NX") || session.getAttribute("zauth_grp").equals("NI")
						|| session.getAttribute("zauth_grp").equals("NP") || session.getAttribute("zauth_grp").equals("NC")
						|| session.getAttribute("zauth_grp").equals("NE") || session.getAttribute("zauth_grp").equals("NT")) {
						
						
						
						// 본사 NI인 경우
						if(session.getAttribute("zauth_grp").equals("NI")) {
							if(session.getAttribute("zdept_id").equals("A210")) {
								session.setAttribute("zregion_gb", "A");
							} else {
								if(session.getAttribute("zregion").equals("T880")) session.setAttribute("zregion_gb", "C");
								else if(session.getAttribute("zregion").equals("T885")) session.setAttribute("zregion_gb", "S");
								else session.setAttribute("zregion_gb", "N");
							}
						} else {
							if(session.getAttribute("zregion").equals("T880")) session.setAttribute("zregion_gb", "C");
							else if(session.getAttribute("zregion").equals("T885")) session.setAttribute("zregion_gb", "S");
							else session.setAttribute("zregion_gb", "N");
						}
					} else {
						session.setAttribute("zregion_gb", "A");
					}

					// 타임아웃 설정 (sec) 30분
					session.setMaxInactiveInterval(1800);

				} else {
					rtn_loginInfo.put("isLogin", false);
					rtn_loginInfo.put("msg", rtn_loginInfo.get("msg"));

				}

			} else {
				rtn_loginInfo.put("isLogin", false);
				rtn_loginInfo.put("msg", "로그인 정보 확인이 불가능합니다");
			}

		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] login_process -" + e);
		} catch (Exception e) {
			logger.debug("[Exception] login_process -" + e);
		}

		return rtn_loginInfo;

	}

	/**
	 * 드림스 링크 - 로그인 연동
	 * 
	 */
	@RequestMapping(value = "/com/login_link_check.do", method = { RequestMethod.POST })
	public void dmsLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletRequestBindingException, IOException {

		logger.info("[Controll] :/com/login_link_check.do");

		// 받은 파라미터 값 첫글자는 페이지 구분값
		String zlink_key = (String) ServletRequestUtils.getRequiredStringParameter(request, "zlink_key");
		logger.info("1. zlink_key : " + zlink_key);

		// 전달된 키값으로 로그인 아이디 추출
		String zloginId = "";

		try {

			zloginId = comLoginService.getLinkLoginid(zlink_key);

		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] login_link_check -" + e);
		}

		catch (Exception e) {
			logger.debug("[Exception] login_link_check -" + e);
		}

		logger.info("2. zloginId : " + zloginId);

		// 로그인 리턴값 생성
		Gson gson = new Gson();
		HashMap rtn_loginInfo = new HashMap();

		// 로그인 아이디 정상 호출시 로그인 진행
		if (!zloginId.equals("")) {

			HashMap paramMap = new HashMap();
			paramMap.put("zloginId", zloginId);
			paramMap.put("zpasswd", "");
			paramMap.put("type", "1"); // type : 0-DMS 로그인페이지, 1-드림스 로그인페이지

			if (zloginId.length() > 0) {

				rtn_loginInfo = login_process(request, paramMap);

			} else {

				rtn_loginInfo.put("isLogin", false);
				rtn_loginInfo.put("msg", "아이디를 확인해주세요(서버).");

			}

		} else {

			rtn_loginInfo.put("isLogin", false);
			rtn_loginInfo.put("msg", "아이디를 확인해주세요(서버).");

		}

		response.getOutputStream().write(gson.toJson(rtn_loginInfo).getBytes("UTF-8"));
	}

	/**
	 * 로그아웃 처리
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response) {

		logger.info("[Controll] : /logout");

		HttpSession session = request.getSession();
		session.invalidate();

		return "logout";
	}

	/**
	 * 드림스 로그인 링크
	 */
	@RequestMapping(value = "/login_link", method = RequestMethod.POST)
	public String login_link(HttpServletRequest request, HttpServletResponse response) {

		logger.info("[Controll] : /login_link");

		HttpSession session = request.getSession();
		//session.invalidate();

		return "com/login_link";
	}
	
	/**
	 * 세션 강제 변경
	 */
	@RequestMapping(value = "/changeSession.do", method = RequestMethod.POST)
	public String changeSession(HttpServletRequest request, HttpServletResponse response) {

		logger.info("[Controll] : /changeSession");

		try {
			HttpSession session = request.getSession();
			// 로그인 공통 세션
			
			String line = "";
			String gb = ""; // 권한구분
			
			// Json으로 변수 받음
			BufferedReader reader = request.getReader();
	
			while ((line = reader.readLine()) != null) {
				JSONObject jsonObj = (JSONObject) JSONValue.parse(line);
	
				gb = (String) jsonObj.get("gb");
			}
			String zlogin_id = session.getAttribute("zlogin_id").toString();
			
			if(zlogin_id.equals("10000232")) {
				if(gb.equals("1")) {
					session.setAttribute("zregion", "T335");
					session.setAttribute("zregion_nm", "호남본부");
					session.setAttribute("zdept_id", "T335");
					session.setAttribute("zdept_nm", "호남본부");
					session.setAttribute("zdept_brch_id", "001");
		
					session.setAttribute("zauth_grp", "NI");
					session.setAttribute("zregion_gb", "N");
				} else {
					session.setAttribute("zregion", "T337");
					session.setAttribute("zregion_nm", "제주본부");
					session.setAttribute("zdept_id", "M014");
					session.setAttribute("zdept_nm", "제주지원팀");
					session.setAttribute("zdept_brch_id", "001");
		
					session.setAttribute("zauth_grp", "NI");
					session.setAttribute("zregion_gb", "N");
				}
			} else if(zlogin_id.equals("21012108")) {
				if(gb.equals("1")) {
					session.setAttribute("zregion", "T335");
					session.setAttribute("zregion_nm", "호남본부");
					session.setAttribute("zdept_id", "M012");
					session.setAttribute("zdept_nm", "호남지원팀");
					session.setAttribute("zdept_brch_id", "001");
		
					session.setAttribute("zauth_grp", "NX");
					session.setAttribute("zregion_gb", "N");
				} else {
					session.setAttribute("zregion", "T337");
					session.setAttribute("zregion_nm", "제주본부");
					session.setAttribute("zdept_id", "M014");
					session.setAttribute("zdept_nm", "제주지원팀");
					session.setAttribute("zdept_brch_id", "001");
		
					session.setAttribute("zauth_grp", "NX");
					session.setAttribute("zregion_gb", "N");
				}
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "com/main";
	}

}
