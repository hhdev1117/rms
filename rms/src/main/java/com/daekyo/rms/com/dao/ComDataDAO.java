package com.daekyo.rms.com.dao;

import java.util.HashMap;
import java.util.List;

public interface ComDataDAO {
	public List getZregion(HashMap paramMap) throws Exception;

	public List getVkbur(HashMap paramMap) throws Exception;

	public List getVkgrp(HashMap paramMap) throws Exception;

	public List getZemp(HashMap paramMap) throws Exception;

	public List selectCodeList(String zgrcode) throws Exception;
}