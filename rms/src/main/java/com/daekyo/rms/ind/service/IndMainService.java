package com.daekyo.rms.ind.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.daekyo.rms.com.vo.MainVO;

public interface IndMainService {
	
	public List getIndAuthorization(HttpServletRequest request) throws Exception;
	
}


