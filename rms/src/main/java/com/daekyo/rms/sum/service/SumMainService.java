package com.daekyo.rms.sum.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface SumMainService {
	
	public String getSumMoni(HttpServletRequest request) throws Exception;
	
	public String getSumMoniDtl(HttpServletRequest request) throws Exception;
	
	public List getSumRank(HttpServletRequest request) throws Exception;
	
}


