package com.daekyo.rms.com.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ComCommService {
	
	public List getCommCode(HttpServletRequest request) throws Exception;
	
	public List selectMenuList(HashMap paramMap);
	
	public int menuAuthCheck(HashMap paramMap);
	
	public List getMenu(HttpServletRequest request) throws Exception;
	
}


