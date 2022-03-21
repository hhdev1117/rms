package com.daekyo.rms.pay.dao;

import java.util.HashMap;
import java.util.List;

public interface PayMainDAO {
	
     public List getDirectPayment(HashMap paramMap);
     
     public List getDirectPaymentOthers(HashMap paramMap);
 
}