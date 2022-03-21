package com.daekyo.rms.com.service.impl;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daekyo.rms.com.dao.ComLoginDAO;
import com.daekyo.rms.com.dao.ComMainDAO;
import com.daekyo.rms.com.service.ComMainService;
import com.daekyo.rms.com.vo.MainVO;

@Service("ComMainService")
public class ComMainServiceImpl implements ComMainService {
	
	 private static final Logger logger = LoggerFactory.getLogger(ComMainServiceImpl.class);
     
	 
	 @Autowired
     private ComMainDAO mainMapper;
	 
	 @Override
     @Transactional
     public MainVO selectMain(HashMap paramMap) {
    	 return mainMapper.selectMain(paramMap);
     }
}


