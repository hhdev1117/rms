package com.daekyo.rms.smi.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface SmiMainService {

	public List getSmiRisk(HttpServletRequest request) throws Exception;

}
