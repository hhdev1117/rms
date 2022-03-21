package com.daekyo.rms.com.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface ComDataService {

	public List getZregion(HttpServletRequest request) throws Exception;

	public List getVkbur(HttpServletRequest request) throws Exception;

	public List getVkgrp(HttpServletRequest request) throws Exception;

	public List getZemp(HttpServletRequest request) throws Exception;
	
	public List selectCodeList(String zgrcode) throws Exception;
	
}
