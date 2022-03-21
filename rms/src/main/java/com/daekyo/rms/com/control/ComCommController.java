package com.daekyo.rms.com.control;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.daekyo.rms.com.service.ComCommService;
import com.daekyo.rms.com.service.ComMainService;
import com.daekyo.rms.util.MessageUtils;
import com.daekyo.rms.util.Util;

@Controller
public class ComCommController {
	
	private static final Logger logger = LoggerFactory.getLogger(ComCommController.class);
	
	@Resource(name = "ComCommService") 
	private ComCommService comCommService;
	
	Util utils = new Util();
	
	MessageUtils msgUtil = new MessageUtils();
	
	/**
	 * 메뉴 조회
	 * 
	 */
	@ResponseBody
	@RequestMapping("/com/getMenu.do")
	public Object getMenu(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws ServletRequestBindingException, IOException {
		
		List resultList = null;
		
		try {
			resultList = comCommService.getMenu(request);
		} catch (NullPointerException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
		} catch (Exception e) {
			logger.debug("[Exception] " + request.getServletPath() + " - " + e );
		}

		return resultList;
	}
	
}
