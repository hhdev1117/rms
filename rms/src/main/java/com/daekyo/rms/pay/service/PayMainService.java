package com.daekyo.rms.pay.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.daekyo.rms.com.vo.MainVO;

public interface PayMainService {
	
	public List getDirectPayment(HttpServletRequest request) throws Exception;
	
}


