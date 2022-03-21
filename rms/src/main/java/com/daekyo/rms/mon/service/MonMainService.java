package com.daekyo.rms.mon.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface MonMainService {
	
	public List getMonMoni(HttpServletRequest request) throws Exception;
	
}


