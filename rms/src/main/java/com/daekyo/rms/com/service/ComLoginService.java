package com.daekyo.rms.com.service;

import java.util.HashMap;

public interface ComLoginService {
	
	public HashMap<String, String> checkLoginInfo(HashMap<String,String> params) throws Exception;	
	public String getLinkLoginid(String zlink_key) throws Exception;
	
	public String checkAuthInfo(String zlogin_id) throws Exception;
	public String checkAuthCntInfo(String zlogin_id) throws Exception;
	
}


