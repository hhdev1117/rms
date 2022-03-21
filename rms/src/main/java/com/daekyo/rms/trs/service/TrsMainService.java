package com.daekyo.rms.trs.service;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

public interface TrsMainService {

	public List getTrsDup(HttpServletRequest request) throws Exception;

}
