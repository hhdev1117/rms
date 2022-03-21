package com.daekyo.rms.com.dao;

import java.util.HashMap;
import java.util.List;

public interface ComLoginDAO {

	public List checkLoginInfo(HashMap<String, String> params) throws Exception;
	
	public List checkAuthInfo(HashMap<String, String> params) throws Exception;

	public List getLoginInfo(HashMap<String, String> params) throws Exception;

	public String getLoginTime(String zlogin_id) throws Exception;

	public String getLinkLoginid(String zlink_key) throws Exception;

	public int delLinkKey(String zlink_key) throws Exception;

	public String checkAuthInfo(String zlogin_id) throws Exception;

	public String checkAuthCntInfo(String zlogin_id) throws Exception;

}