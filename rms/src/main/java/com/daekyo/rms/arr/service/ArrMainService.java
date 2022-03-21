package com.daekyo.rms.arr.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.daekyo.rms.com.vo.MainVO;

public interface ArrMainService {
	
	public List getArrPayment(HttpServletRequest request) throws Exception;
	
}


