package com.daekyo.rms.exl.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ExlMainService {
	
	public void excelMoniSum_d(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public void excelMoniSum_ex_d(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public void excelMoniSum_d_cnt(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public void excelMoniSum_ex_d_cnt(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public void excelCardPayment_d(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public void excelCardPayment_da(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public void excelTranStudy_d(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public void excelTranStudy_da(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public void excelIndAuth_d(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public void excelIndAuth_da(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public void excelArrPayment_d(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public void excelArrPayment_da(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public void excelPayDirect_d(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public void excelPayDirect_da(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public void excelStmApply_d(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public void excelStmApply_da(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public void excelSmiRisk_d(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public void excelSmiRisk_da(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public void excelMonMoni_d(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public void excelMonMoni_d_cnt(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public void excelNstZeroStudy_d(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public void excelNstZeroStudy_da(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public void excelMoniSum_r(HttpServletRequest request, HttpServletResponse response) throws Exception;	
	public void excelTrsDup_d(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public void excelTrsDup_da(HttpServletRequest request, HttpServletResponse response) throws Exception;
}


