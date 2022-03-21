package com.daekyo.rms.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;


public class Util {
	
	private static final Logger logger = LoggerFactory.getLogger(Util.class);

	DecimalFormat NumFrm0 = new DecimalFormat("###,###,###,##0;-###,###,###,##0");
	DecimalFormat NumFrm1 = new DecimalFormat("###,###,###,##0.0;-###,###,###,##0.0");
	DecimalFormat NumFrm2 = new DecimalFormat("###,###,###,##0.00;-###,###,###,##0.00");
	DecimalFormat NumFrm3 = new DecimalFormat("###,###,###,##0.000;-###,###,###,##0.000");
	DecimalFormat NumFrm4 = new DecimalFormat("###,###,###,##0.0000;-###,###,###,##0.0000");
	DecimalFormat NumFrm5 = new DecimalFormat("###,###,###,##0.00000;-###,###,###,##0.00000");
	DecimalFormat NumFrm6 = new DecimalFormat("###,###,###,##0.000000;-###,###,###,##0.000000");

	public Util() {
	}

//	======================== Aion Properties File open ===========================
	public synchronized String getProp(String inProperty) {
		InputStream is = getClass().getResourceAsStream("/fed.properties");
		Properties aionProps = new Properties();
		String strValue = "";
		try {
			aionProps.load(is);
			strValue = aionProps.getProperty(inProperty);
		} 
		catch (NullPointerException e) {
			logger.debug("[NullPointerException] getProp -" + e );
			
			strValue = "";
		}
		
		catch (Exception e) {
			System.err.println("Can't read the property. " + inProperty);
			strValue = "";
		} finally {
			try {
				if(is != null) {
					is.close();
				}
				
			} 
			catch (NullPointerException e) {
				logger.debug("[NullPointerException] getProp -" + e );
			}
			catch (Exception e) {
				logger.debug("[Exception] getProp -" + e );
			}
		}

		return strValue;
	}

	// ===================== 널변환================================================
	public String isnull(String inStr, String conStr) {
		String retValue = "";

		if (inStr == null || inStr.equals(""))
			retValue = conStr;
		else
			retValue = inStr;

		// 2003/01/14 ( .001 을 0.001 로 교체)=======================
		if (!(inStr == null || inStr.equals(""))) {
			int index = 0;
			index = inStr.indexOf(".");
			if (index == 0) {
				inStr = "0" + inStr;
				retValue = inStr;
			}
		}
		return retValue;
	}

	public String isnull(Object inStr, String conStr) {
		
		String chkValue = String.valueOf(inStr); 

		return isnull(chkValue, conStr);
	}

	public String isnull(String inStr) {

		return isnull(inStr, "");
	}

	public String isnull(Object inStr) {

		return isnull(String.valueOf(inStr), "");
	}


	/**
	 * JSON 스트링을 HashMap으로 컨버트합니다.
	 * @param strJson 컨버트할 JSON
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> jsonToMap(String strJson){
		ObjectMapper mapper   = null;  
		HashMap<String, Object> map = null;
		 
		try {
			mapper  = new ObjectMapper();
			map  = new HashMap<String, Object>();			
			map = mapper.readValue(strJson, HashMap.class);
		} 
		catch (NullPointerException e) {
			logger.debug("[NullPointerException] jsonToMap -" + e );
		}
		catch (Exception e) {
			logger.debug("[Exception] jsonToMap -" + e );
		}
		 
		return map;
	}
	
	/**
	 * 오브젝트를 JSON 스트링으로 컨버트합니다.
	 * @param object 컨버트할 오브젝트(직렬화 가능한 객체)
	 * @return JSON 스트링
	 */
	public static String objectToJson(Object object){
		ObjectMapper mapper   = null;
		String strJson = null;
		 
		try {
			mapper  = new ObjectMapper();
			strJson = mapper.writeValueAsString(object);
		} 
		catch (NullPointerException e) {
			logger.debug("[NullPointerException] objectToJson -" + e );
		}		
		catch (Exception e) {
			logger.debug("[Exception] objectToJson -" + e );
		}
		 
		return strJson;
	}
	
	
	
	/**
	 * 문자열 XSS 스크립팅 방지 체크 
	 * @param 검증할 문자열
	 * @return 변환된 문자열
	 */
	public static String strFilter(String str){
		  StringBuffer sb = null;
		  String[] checkStr_arr = {
		      "<script>","</script>",
		      "javascript","vbscript",
		      "<javascript>","</javascript>",
		      "<vbscript>","</vbscript>", "onerror", "onclick","onload",
		      "<script>", "<object>", "<applet>", "<form>", "<embed>", "<iframe>", "<frame>", "<base>", "<body>", "<frameset>", "<html>", "<img>", "<layer>", "<ilayer>", "<meta>", "<p>", "<style>", "<xxx src>", "<a href>",
		      "Onmouseover", "onclick", "onblur", "onfocus", "onload", "onselect", "onsubmit", "onunload", "onabort", "onerror", "onmouseout", "onreset", "ondbclick", "ondragdrop", "onkeydown", "onkeypress", "onkeyup", "onmousedown", "onmousemove", "onmouseup", "onmove", "onresize"
		      };
		   
		  for(String checkStr : checkStr_arr){
		    while(str.indexOf(checkStr)!=-1){
		      str = str.replaceAll(checkStr, "");
		    }
		    while(str.toLowerCase().indexOf(checkStr)!=-1){
		      sb = new StringBuffer(str);
		      sb = sb.replace(str.toLowerCase().indexOf(checkStr),str.toLowerCase().indexOf(checkStr)+ checkStr.length(), "");
		      str = sb.toString();
		    }
		       
		  }
		  return str;
	}
	
	/**
	 * 화면 반환용 Custom ModelAndView
	 * 개별 화면에서 추가적으로 파라미터를 넘겨야 할경우
	 * 아래 customMav를 반환받은후 
	 * mav를 추가해서 적용한다.
	 * 소스가 너무긴데 매 화면마다 같은코드를 넣고싶지않아서 공통으로 생성함.
	 */
	public static ModelAndView customMav(HttpServletRequest request, String viewname, String active_bcg_id){
		  ModelAndView mav = new ModelAndView();
		  HttpSession session = request.getSession();
		  
		  String line = "";
		  String param_zregion = request.getParameter("param_zregion");
		  String param_zdept_id = request.getParameter("param_zdept_id");
		  String param_zdept_brch_id = request.getParameter("param_zdept_brch_id");
		  String param_zemp_id = request.getParameter("param_zemp_id");
		  String param_zreq_ym = request.getParameter("param_zreq_ym");
		  String param_search_gb = request.getParameter("param_search_gb");
		  
		  mav.addObject("zregion_gb", session.getAttribute("zregion_gb"));
		  mav.addObject("zauth_grp", session.getAttribute("zauth_grp"));
		  mav.addObject("zregion", session.getAttribute("zregion"));
		  mav.addObject("zdept_id", session.getAttribute("zdept_id"));
		  mav.addObject("zdept_brch_id", session.getAttribute("zdept_brch_id"));
		  mav.addObject("zemp_id", session.getAttribute("zemp_id"));
			
		  /*
		   * 주요지표에서 넘어가는것을 위한 데이터
		   */
		  mav.addObject("param_zregion", param_zregion);
		  mav.addObject("param_zdept_id", param_zdept_id);
		  mav.addObject("param_zdept_brch_id", param_zdept_brch_id);
		  mav.addObject("param_zemp_id", param_zemp_id);
		  mav.addObject("param_zreq_ym", param_zreq_ym);
		  mav.addObject("param_search_gb", param_search_gb);
		  
		  mav.addObject("active_bcg_id", active_bcg_id);
		  mav.setViewName(viewname);
			
		  return mav;
	}
	

}
