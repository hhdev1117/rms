package com.daekyo.rms.mon.dao;

import java.util.HashMap;
import java.util.List;

public interface MonMainDAO {
	
     public List getMonMoni(HashMap paramMap);
 
     public List getMonMoniForCnt(HashMap paramMap);
}