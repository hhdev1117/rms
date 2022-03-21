package com.daekyo.rms.sum.dao;

import java.util.HashMap;
import java.util.List;

public interface SumMainDAO {
	
     public List getSumMoni(HashMap paramMap);
     
     public List getSumMoniForCount(HashMap paramMap);
     
     public List getSumMoniDtl(HashMap paramMap);
     
     public List getSumMoniDtlForCount(HashMap paramMap);
     
     public List getSumRank(HashMap paramMap);
     
     public List getSumRankCnt(HashMap paramMap);
     
     public List getSumRankInd(HashMap paramMap);

}