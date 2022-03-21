package com.daekyo.rms.trn.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.daekyo.rms.com.vo.MainVO;

public interface TrnMainService {

	public List getTrnStudy(HttpServletRequest request) throws Exception;

}
