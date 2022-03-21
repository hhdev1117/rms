package com.daekyo.rms.com.dao;

import java.util.HashMap;
import java.util.List;


public interface ComCommDAO {
	
	public List getCommCode() throws Exception;
	
	public List selectMenuList(HashMap paramMap);
	
	public int menuAuthCheck(HashMap paramMap);
	
	public List getMenu(HashMap paramMap);
    
}