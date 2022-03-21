package com.daekyo.rms.com.service.impl;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daekyo.rms.com.dao.ComLoginDAO;
import com.daekyo.rms.com.service.ComLoginService;
import com.daekyo.rms.com.vo.LoginVO;
import com.daekyo.rms.util.Util;




@Service("comLoginService")
public class ComLoginServiceImpl implements ComLoginService {
	
	 private static final Logger logger = LoggerFactory.getLogger(ComLoginServiceImpl.class);

     @Autowired
     private ComLoginDAO loginMapper;
     
     Util utils = new Util();
     

     @Override
     @Transactional
     public HashMap<String,String> checkLoginInfo(HashMap<String,String> params) throws Exception {
    	 
    	 HashMap rtnMap = new HashMap();
    	 HashMap loginInfoMap = new HashMap();
    	 
    	 List loginChecklist = loginMapper.checkLoginInfo(params);
    	 
    	 List authChecklist = loginMapper.checkAuthInfo(params);
    	 
    	 if(loginChecklist.size() == 0) {
    		 
    		 rtnMap.put("isLogin", false);
    		 rtnMap.put("msg", 	"로그인 정보 확인이 불가능합니다");
    		 
    	 }else if(loginChecklist.size() > 1) {
    			 
    		rtnMap.put("isLogin", false);
        	rtnMap.put("msg", 	"로그인 정보 확인이 불가능합니다");
    	 }else {
    		 if(authChecklist.size() == 0){ 
    			rtnMap.put("isLogin", false);
 	     		rtnMap.put("msg", 	"접근 권한이 없습니다.");
 	   	 	 
 	     		return rtnMap;
    		 }
    		 
    		 LoginVO vo  = null;
    		 
    		 for(int i = 0; i < loginChecklist.size(); i++ ) {
    			 vo  = (LoginVO) loginChecklist.get(i);
    		 }
    		 
    		 if(vo != null) {
    			 logger.debug("로그인 조회 - 1.zlogin_id : " + vo.getZlogin_id());
	    		 // 세션 공통 값 세팅    		 
	    		 rtnMap.put("zlogin_id", vo.getZlogin_id());
	    		 rtnMap.put("isLogin", true); //true 시작해 로직에 의해 재계산
    		 }
    		 
    		// RMS사이트에서 로그인시에만 암호 체크				 
			 if(params.get("type").equals("0")) {
				 // 패스워드가 다를때
				 if(!params.get("zpasswd").equals(vo.getZpasswd())) {    					 
					rtnMap.put("isLogin", false);
			        rtnMap.put("msg", 	"로그인 정보 확인이 불가능합니다");    					 
				 }
			 } 
				 
			 if(!vo.getUse_yn().equals("Y")) {
				 rtnMap.put("isLogin", false);
				 rtnMap.put("msg", 	"로그인 정보 확인이 불가능합니다");
			 }
				 
			 List login_info_list = null;
				 
				// 정상 로그인일 경우 정보 추출
				 if(!rtnMap.get("isLogin").equals(false)){
					 
					 loginInfoMap.put("zlogin_id", vo.getZlogin_id().toString());
					 
					 login_info_list = loginMapper.getLoginInfo(loginInfoMap);
					 
					 if(login_info_list.size() > 0) {
						 
						 LoginVO info_vo  = null;					 
						 for(int i = 0; i < login_info_list.size(); i++) {
							 info_vo  = (LoginVO) login_info_list.get(i);
						 }
						 
						 
						 if(info_vo != null) {
						 
							 // 52시간 로그인 접속 확인
							 String time_gb = loginMapper.getLoginTime(vo.getZlogin_id().toString());
							 							
							if(time_gb.equals("N1")) {// 휴무팝업
								logger.debug("유연근무제로 사내인트라넷 접속을 제한합니다.");
								
								rtnMap.put("isLogin", false);
							    rtnMap.put("msg", 	"유연근무제로 사내인트라넷 접속을 제한합니다.");
							     
							}else if(time_gb.equals("N2")) { //접속가능시간 아님
								logger.debug("유연근무제로 사내인트라넷 접속을 제한합니다.");
								
								rtnMap.put("isLogin", false);
							    rtnMap.put("msg", 	"유연근무제로 사내인트라넷 접속을 제한합니다.");
							
							}else {
								
								 rtnMap.put("isLogin", true);
								 rtnMap.put("msg", 	"정상");	
								 
								 // 세션값 세팅
								 //rtnMap.put("gubun", 		"4");
								 rtnMap.put("zcenter_id", 	info_vo.getZcenter_id());
								 rtnMap.put("zcenter_nm", 	info_vo.getZcenter_nm());
								 rtnMap.put("zcenter_gb", 	info_vo.getZcenter_gb());
								 rtnMap.put("zregion", 		info_vo.getZregion());
								 rtnMap.put("zregion_nm", 	info_vo.getZregion_nm());
								 rtnMap.put("zdept_id", 	info_vo.getZdept_id());
								 rtnMap.put("zdept_nm", 	info_vo.getZdept_nm());
								 rtnMap.put("zemp_id", 		info_vo.getZemp_id());
								 rtnMap.put("zemp_nm", 		info_vo.getZemp_nm());
								 rtnMap.put("zdept_brch_id",info_vo.getZdept_brch_id());
								 
								 rtnMap.put("vtweg", 		info_vo.getVtweg());
								 rtnMap.put("kvgr2", 		info_vo.getKvgr2());
								 rtnMap.put("zauth_grp", 	info_vo.getZauth_grp());
								 
								 rtnMap.put("zlink_gb", 	params.get("zlink_gb"));
								 
								
								 rtnMap.put("forwardUrl", "/main.do");
								 
								 
							}
						 }else {
							 
							 rtnMap.put("isLogin", false);
						     rtnMap.put("msg", 	"로그인 정보 확인이 불가능합니다");
							 
						 }
							 
					 }else {
						 rtnMap.put("isLogin", false);
					     rtnMap.put("msg", 	"로그인 정보 확인이 불가능합니다");
					 }
				 }else {
	    			 rtnMap.put("isLogin", false);
				     rtnMap.put("msg", 	"로그인 정보 확인이 불가능합니다");
	    		 }
			 }
    		 
    	 
         return rtnMap;
     }
     
     @Override
     @Transactional
     public String getLinkLoginid(String zlink_key) throws Exception {
    	 
    	 
    	 String rtn_val = "";
    	 
    	 try {
    		 
    		 logger.debug("getLinkLoginid-zlink_key:"+zlink_key);
    		 
    		 rtn_val = loginMapper.getLinkLoginid(zlink_key);
    		 
    		 logger.debug("getLinkLoginid-rtn_val:"+rtn_val);
    		 
    		 // 로그인 계정이 호출 후 해당 키 값 삭제 처리 (1회성 키값 데이터)
    		 loginMapper.delLinkKey(zlink_key);
    		 
			
		} 
    	catch (NullPointerException e) {
 			logger.debug("[NullPointerException] getLinkLoginid -" + e );
 		} 
    	catch (Exception e) {
    		logger.debug("[Exception] getLinkLoginid -" + e );
		}
    	 
    	return rtn_val;
     }
     
     @Override
     @Transactional
     public String checkAuthInfo(String zlogin_id) throws Exception {
    	 
    	 String rtn_val = "";
    	 
    	 try {
    		 
    		 rtn_val = loginMapper.checkAuthInfo(zlogin_id);
    		 
		}catch (NullPointerException e) {
 			logger.debug("[NullPointerException] getLinkLoginid -" + e );
 		} catch (Exception e) {
    		logger.debug("[Exception] getLinkLoginid -" + e );
		}
    	 
    	return rtn_val;
     }
     
     @Override
     @Transactional
     public String checkAuthCntInfo(String zlogin_id) throws Exception {
    	 
    	 String rtn_val = "";
    	 
    	 try {
    		 
    		 rtn_val = loginMapper.checkAuthCntInfo(zlogin_id);
    		 
		}catch (NullPointerException e) {
 			logger.debug("[NullPointerException] getLinkLoginid -" + e );
 		} catch (Exception e) {
    		logger.debug("[Exception] getLinkLoginid -" + e );
		}
    	 
    	return rtn_val;
     }
}


