package com.daekyo.rms.crd.service;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.daekyo.rms.crd.vo.CrdMainVO;

public interface CrdMainService {

	public List getCardPayment(HttpServletRequest request) throws Exception;

}
