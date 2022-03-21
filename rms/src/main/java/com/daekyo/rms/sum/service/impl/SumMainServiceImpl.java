package com.daekyo.rms.sum.service.impl;

import java.io.BufferedReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.omg.CORBA.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daekyo.rms.sum.dao.SumMainDAO;
import com.daekyo.rms.sum.service.SumMainService;
import com.daekyo.rms.sum.vo.SumMainVO;

@Service("sumMainService")
@Transactional(readOnly = true)
public class SumMainServiceImpl implements SumMainService {

	private static final Logger logger = LoggerFactory.getLogger(SumMainServiceImpl.class);

	@Autowired
	private SumMainDAO sumMainMapper;
	
	/*******************************************
	 * 금액 세자리 콤마 적용 				   *
	 *******************************************/
	public String getFormatForAmt(String amt) {
		DecimalFormat fommater = new DecimalFormat("###,###");
		
		if(amt.equals("-")) return "-";
		
		return fommater.format(Integer.parseInt(amt));
	}

	@Override
	public String getSumMoni(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		
		String line = "";
		String zreq_ym = "";
		String active_bcg_id = "";
		String brch_gb = "";
		String order = "";
		String cnt_gb = "";
		
		String session_zregion = (String) session.getAttribute("zregion");
		String session_zdept_id = (String) session.getAttribute("zdept_id");
		String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
		String session_zemp_id = (String) session.getAttribute("zemp_id");
		String session_zauth_grp = (String) session.getAttribute("zauth_grp");
		String session_zregion_gb = (String) session.getAttribute("zregion_gb");
		
		// Json으로 변수 받음
		BufferedReader reader = request.getReader();
		while ((line = reader.readLine()) != null) {
			JSONObject jsonObj = (JSONObject) JSONValue.parse(line);

			zreq_ym = (String) jsonObj.get("zreq_ym");
			active_bcg_id = (String) jsonObj.get("active_bcg_id");
			brch_gb = (String) jsonObj.get("brch_gb");
			order = (String) jsonObj.get("order");
			cnt_gb = (String) jsonObj.get("cnt_gb"); // 수치/지수 구분
		}
		
		// 파라미터
		HashMap<String, String> paramMap = new HashMap();
		paramMap.put("session_zregion", session_zregion);
		paramMap.put("session_zdept_id", session_zdept_id);
		paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
		paramMap.put("session_zemp_id", session_zemp_id);
		paramMap.put("session_zregion_gb", session_zregion_gb);
		paramMap.put("zreq_ym", zreq_ym);
		paramMap.put("brch_gb", brch_gb);
		paramMap.put("order", order);
		paramMap.put("active_bcg_id", active_bcg_id);
		paramMap.put("session_zauth_grp", session_zauth_grp);
		
		if(cnt_gb.equals("ind")) {
			return getResultHtml(sumMainMapper.getSumMoni(paramMap), active_bcg_id, brch_gb, request);
		} else {
			return getResultHtmlForCount(sumMainMapper.getSumMoniForCount(paramMap), active_bcg_id, brch_gb, request);
		}
	}

	@Override
	public String getSumMoniDtl(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		
		String line = "";
		String zreq_ym = "";
		String active_bcg_id = "";
		
		String zregion = "";
		String zdept_id = "";
		String zdept_brch_id = "";
		String cnt_gb = "";
		
		String session_zregion = (String) session.getAttribute("zregion");
		String session_zdept_id = (String) session.getAttribute("zdept_id");
		String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
		String session_zemp_id = (String) session.getAttribute("zemp_id");
		String session_zauth_grp = (String) session.getAttribute("zauth_grp");
		String session_zregion_gb = (String) session.getAttribute("zregion_gb");
		
		// Json으로 변수 받음
		BufferedReader reader = request.getReader();
		while ((line = reader.readLine()) != null) {
			JSONObject jsonObj = (JSONObject) JSONValue.parse(line);

			zregion = (String) jsonObj.get("zregion");
			zdept_id = (String) jsonObj.get("zdept_id");
			zdept_brch_id = (String) jsonObj.get("zdept_brch_id");
			zreq_ym = (String) jsonObj.get("zreq_ym");
			active_bcg_id = (String) jsonObj.get("active_bcg_id");
			cnt_gb = (String) jsonObj.get("cnt_gb");
		}
		
		// 파라미터
		HashMap<String, String> paramMap = new HashMap();
		paramMap.put("zregion", zregion);
		paramMap.put("zdept_id",zdept_id);
		paramMap.put("zdept_brch_id", zdept_brch_id);
		paramMap.put("session_zregion", session_zregion);
		paramMap.put("session_zdept_id", session_zdept_id);
		paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
		paramMap.put("session_zemp_id", session_zemp_id);
		paramMap.put("session_zregion_gb", session_zregion_gb);
		paramMap.put("zreq_ym", zreq_ym);
		paramMap.put("active_bcg_id", active_bcg_id);
		paramMap.put("session_zauth_grp", session_zauth_grp);
		
		if(cnt_gb.equals("ind")) {
			return getResultDtlHtml(sumMainMapper.getSumMoniDtl(paramMap), active_bcg_id);
		} else {
			return getResultDtlHtmlForCount(sumMainMapper.getSumMoniDtlForCount(paramMap), active_bcg_id);
		}
	}
	
	public String getResultHtml(List resultList, String active_bcg_id, String brch_gb, HttpServletRequest request) {
		StringBuilder stb = new StringBuilder();
		if(resultList.size() < 3) {
			return "N";
		}
	    for(int i = 0; i < resultList.size(); i++) {
	    	SumMainVO vo = (SumMainVO) resultList.get(i);
	    	if(vo.getZlevel().equals("3")) {
	    		stb.append("<tr class='sum-node-brch ");
	    		stb.append(vo.getZregion());
	    		stb.append(" ");
	    		stb.append(vo.getZdept_id());
	    		stb.append(" ");
	    		stb.append(vo.getZdept_brch_id());
	    		stb.append(" zlevel");
	    		stb.append(vo.getZlevel());
	    		stb.append("' zregion='");
	    		stb.append(vo.getZregion());
	    		stb.append("' zdept_id='");
	    		stb.append(vo.getZdept_id());
	    		stb.append("' zdept_brch_id='");
	    		stb.append(vo.getZdept_brch_id());
	    		stb.append("' zreq_ym='");
	    		stb.append(vo.getZrms_ym());
	    		stb.append("' zlevel='");
	    		stb.append(vo.getZlevel());
	    		if(vo.getCheck_gb().equals("Y")) {
	    			if(request.getSession().getAttribute("zauth_grp").equals("NC") || request.getSession().getAttribute("zauth_grp").equals("NP")) {
		    			if(request.getSession().getAttribute("zdept_id").equals(vo.getZdept_id())) {
		    				stb.append("'><td class='left'>&emsp;&emsp;&emsp;&emsp;<i class='fa fa-plus-square' style='color:#91a9ca; cursor: pointer;' onclick='getAddData(this)'></i>&emsp;&emsp;");
		    			} else {
		    				stb.append("' style='display:none;'><td class='left'>&emsp;&emsp;&emsp;&emsp;<i class='fa fa-plus-square' style='color:#91a9ca; cursor: pointer;' onclick='getAddData(this)'></i>&emsp;&emsp;");
		    			}
	    			} else {
	    				stb.append("' style='display:none;'><td class='left'>&emsp;&emsp;&emsp;&emsp;<i class='fa fa-plus-square' style='color:#91a9ca; cursor: pointer;' onclick='getAddData(this)'></i>&emsp;&emsp;");
	    			}
	    		} else {
	    			stb.append("' style='display:none;'><td class='left'>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;");
	    		}
	    		if(vo.getZdept_brch_nm().trim().equals("")) {
	    			stb.append(vo.getZdept_brch_id());
	    			if(vo.getZbrch_emp_nm() != null) {
		    			stb.append(" [");
		    			stb.append(vo.getZbrch_emp_nm());
		    			stb.append("]");
	    			}
	    		} else {
	    			stb.append(vo.getZdept_brch_id());
	    			if(vo.getZbrch_emp_nm() != null) {
		    			stb.append(" [");
		    			stb.append(vo.getZbrch_emp_nm());
		    			stb.append("]");
	    			}
	    			stb.append(" (");
	    			stb.append(vo.getZdept_brch_nm());
	    			stb.append(")");
	    		}
	    		if(vo.getCheck_gb().equals("Y")) {
		    		stb.append("</td><td class='sum-item1'>");
		    		stb.append(vo.getZitem1_p());
		    		stb.append("%</td><td class='sum-item2'>");
		    		stb.append(vo.getZitem2_p());
		    		stb.append("%</td><td class='sum-item3'>");
		    		stb.append(vo.getZitem3_p());
		    		stb.append("%</td><td class='sum-item4'>");
		    		stb.append(vo.getZitem4_p());
		    		stb.append("%</td><td class='sum-item5'>");
		    		stb.append(vo.getZitem5_p());
		    		stb.append("%</td>");
		    		if(active_bcg_id.equals("N")) {
			    		stb.append("<td class='sum-item6'>");
			    		stb.append(vo.getZitem6_p());
			    		stb.append("%</td><td class='sum-item7'>");
			    		stb.append(vo.getZitem7_p());
			    		stb.append("%</td>");
		    		}
	    		} else {
		    		stb.append("</td><td>");
		    		stb.append(vo.getZitem1_p());
		    		stb.append("%</td><td>");
		    		stb.append(vo.getZitem2_p());
		    		stb.append("%</td><td>");
		    		stb.append(vo.getZitem3_p());
		    		stb.append("%</td><td>");
		    		stb.append(vo.getZitem4_p());
		    		stb.append("%</td><td>");
		    		stb.append(vo.getZitem5_p());
		    		stb.append("%</td>");
		    		if(active_bcg_id.equals("N")) {
			    		stb.append("<td>");
			    		stb.append(vo.getZitem6_p());
			    		stb.append("%</td><td>");
			    		stb.append(vo.getZitem7_p());
			    		stb.append("%</td>");
		    		}
	    		}
	    		if(!brch_gb.equals("") && active_bcg_id.equals("N")) {
					if(brch_gb.equals("BL")) {
						stb.append("<td>-");
						stb.append("</td>");
						
						/*
						stb.append("<td>-");
						stb.append("</td><td>");
						stb.append("-");
						stb.append("</td>");
						 */
					} else {
						stb.append("<td style='color:#47C83E;'>");
						stb.append(vo.getZadmin_rank1());
						stb.append("</td>");
						
						/*
						stb.append("<td style='color:#47C83E;'>");
						stb.append(vo.getZadmin_rank1());
						stb.append("</td><td style='color:#47C83E;'>");
						stb.append(vo.getZadmin_rank2());
						stb.append("</td>");
						 */ 
					}
	    		} else if(active_bcg_id.equals("C") || active_bcg_id.equals("S")){
	    			stb.append("<td style='color:#47C83E;'>");
	    			stb.append(vo.getZadmin_rank1());
	    			stb.append("</td>");
	    			
	    			/*
	    			stb.append("<td style='color:#47C83E;'>");
	    			stb.append(vo.getZadmin_rank1());
	    			stb.append("</td><td style='color:#47C83E;'>");
	    			stb.append(vo.getZadmin_rank2());
	    			stb.append("</td>");
	    			 */
	    		}
				stb.append("</tr>");
	    	} else if(vo.getZlevel().equals("2")) {
	    		stb.append("<tr class='sum-node-dept ");
	    		stb.append(vo.getZregion());
	    		stb.append(" ");
	    		stb.append(vo.getZdept_id());
	    		stb.append(" zlevel");
	    		stb.append(vo.getZlevel());
	    		stb.append("' zregion='");
	    		stb.append(vo.getZregion());
	    		stb.append("' zdept_id='");
	    		stb.append(vo.getZdept_id());
	    		stb.append("' zreq_ym='");
	    		stb.append(vo.getZrms_ym());
	    		stb.append("' zlevel='");
	    		stb.append(vo.getZlevel());
	    		if(vo.getCheck_gb().equals("Y")) {
	    			if(request.getSession().getAttribute("zauth_grp").equals("NC") || request.getSession().getAttribute("zauth_grp").equals("NP")) {
		    			if(request.getSession().getAttribute("zdept_id").equals(vo.getZdept_id())) {
		    				stb.append("'><td class='left'>&emsp;&emsp;<i class='fa fa-minus-square' style='cursor: pointer;' onclick='getAddData(this)'></i>&emsp;&emsp;");
		    			} else {
		    				stb.append("' style='display:none;'><td class='left'>&emsp;&emsp;<i class='fa fa-plus-square' style='color:#91a9ca; cursor: pointer;' onclick='getAddData(this)'></i>&emsp;&emsp;");
		    			}
	    			} else {
	    				stb.append("' style='display:none;'><td class='left'>&emsp;&emsp;<i class='fa fa-plus-square' style='color:#91a9ca; cursor: pointer;' onclick='getAddData(this)'></i>&emsp;&emsp;");
	    			}
	    		} else {
	    			stb.append("' style='display:none;'><td class='left'>&emsp;&emsp;&emsp;&emsp;");
	    		}
	    		stb.append(vo.getZdept_nm());
	    		if(vo.getZdept_emp_nm() != null) {
		    		stb.append(" [");
		    		stb.append(vo.getZdept_emp_nm());
		    		stb.append("]");
	    		}
	    		if(vo.getCheck_gb().equals("Y")) {
		    		stb.append("</td><td class='sum-item1'>");
		    		stb.append(vo.getZitem1_p());
		    		stb.append("%</td><td class='sum-item2'>");
		    		stb.append(vo.getZitem2_p());
		    		stb.append("%</td><td class='sum-item3'>");
		    		stb.append(vo.getZitem3_p());
		    		stb.append("%</td><td class='sum-item4'>");
		    		stb.append(vo.getZitem4_p());
		    		stb.append("%</td><td class='sum-item5'>");
		    		stb.append(vo.getZitem5_p());
		    		stb.append("%</td>");
		    		if(active_bcg_id.equals("N")) {
			    		stb.append("<td class='sum-item6'>");
			    		stb.append(vo.getZitem6_p());
			    		stb.append("%</td><td class='sum-item7'>");
			    		stb.append(vo.getZitem7_p());
			    		stb.append("%</td>");
		    		}
	    		} else {
		    		stb.append("</td><td>");
		    		stb.append(vo.getZitem1_p());
		    		stb.append("%</td><td>");
		    		stb.append(vo.getZitem2_p());
		    		stb.append("%</td><td>");
		    		stb.append(vo.getZitem3_p());
		    		stb.append("%</td><td>");
		    		stb.append(vo.getZitem4_p());
		    		stb.append("%</td><td>");
		    		stb.append(vo.getZitem5_p());
		    		stb.append("%</td>");
		    		if(active_bcg_id.equals("N")) {
			    		stb.append("<td>");
			    		stb.append(vo.getZitem6_p());
			    		stb.append("%</td><td>");
			    		stb.append(vo.getZitem7_p());
			    		stb.append("%</td>");
		    		}
	    		}
	    		if(!brch_gb.equals("") && active_bcg_id.equals("N")) {
					if(brch_gb.equals("LC")) {
						stb.append("<td>-");
						stb.append("</td>");
						/*
						stb.append("<td>-");
						stb.append("</td><td>");
						stb.append("-");
						stb.append("</td>");
						 */
					} else {
						stb.append("<td style='color:#6799FF;'>");
						stb.append(vo.getZadmin_rank1());
						stb.append("</td>");
						
						/*
						stb.append("<td style='color:#6799FF;'>");
						stb.append(vo.getZadmin_rank1());
						stb.append("</td><td style='color:#6799FF;'>");
						stb.append(vo.getZadmin_rank2());
						stb.append("</td>");
						 */ 
					}
	    		} else if(active_bcg_id.equals("C") || active_bcg_id.equals("S")){
	    			stb.append("<td style='color:#6799FF;'>");
	    			stb.append(vo.getZadmin_rank1());
	    			stb.append("</td>");
	    			
	    			/*
					stb.append("<td style='color:#6799FF;'>");
	    			stb.append(vo.getZadmin_rank1());
	    			stb.append("</td><td style='color:#6799FF;'>");
	    			stb.append(vo.getZadmin_rank2());
	    			stb.append("</td>");
	    			 */
	    		}
				stb.append("</tr>");
	    	} else if(vo.getZlevel().equals("1")) {
	    		stb.append("<tr class='sum-node-bcg ");
	    		stb.append(vo.getZregion());
	    		stb.append(" zlevel");
	    		stb.append(vo.getZlevel());
	    		stb.append("' zregion='");
	    		stb.append(vo.getZregion());
	    		stb.append("' zreq_ym='");
	    		stb.append(vo.getZrms_ym());
	    		stb.append("' zlevel='");
	    		stb.append(vo.getZlevel());
	    		if(vo.getCheck_gb().equals("Y")) {
	    			if(request.getSession().getAttribute("zauth_grp").equals("NC") || request.getSession().getAttribute("zauth_grp").equals("NP")) {
		    			if(request.getSession().getAttribute("zregion").equals(vo.getZregion())) {
		    				stb.append("'><td class='left' onClick='openChart()'><i class='fa fa-minus-square' style='cursor: pointer;' onclick='getAddData(this)'></i>&emsp;&emsp;");
		    			} else {
		    				stb.append("'><td class='left' onClick='openChart()'><i class='fa fa-plus-square' style='color:#91a9ca; cursor: pointer;' onclick='getAddData(this)'></i>&emsp;&emsp;");
		    			}
	    			} else {
	    				stb.append("'><td class='left' onClick='openChart()'><i class='fa fa-plus-square' style='color:#91a9ca; cursor: pointer;' onclick='getAddData(this)'></i>&emsp;&emsp;");
	    			}
	    		} else {
	    			stb.append("'><td class='left' onClick='openChart()'>&emsp;&emsp;&emsp;");
	    		}
	    		stb.append(vo.getZregion_nm());
	    		if(vo.getCheck_gb().equals("Y")) {
		    		stb.append("</td><td class='sum-item1'>");
		    		stb.append(vo.getZitem1_p());
		    		stb.append("%</td><td class='sum-item2'>");
		    		stb.append(vo.getZitem2_p());
		    		stb.append("%</td><td class='sum-item3'>");
		    		stb.append(vo.getZitem3_p());
		    		stb.append("%</td><td class='sum-item4'>");
		    		stb.append(vo.getZitem4_p());
		    		stb.append("%</td><td class='sum-item5'>");
		    		stb.append(vo.getZitem5_p());
		    		stb.append("%</td>");
		    		if(active_bcg_id.equals("N")) {
			    		stb.append("<td class='sum-item6'>");
			    		stb.append(vo.getZitem6_p());
			    		stb.append("%</td><td class='sum-item7'>");
			    		stb.append(vo.getZitem7_p());
			    		stb.append("%</td>");
		    		}
	    		} else {
		    		stb.append("</td><td>");
		    		stb.append(vo.getZitem1_p());
		    		stb.append("%</td><td>");
		    		stb.append(vo.getZitem2_p());
		    		stb.append("%</td><td>");
		    		stb.append(vo.getZitem3_p());
		    		stb.append("%</td><td>");
		    		stb.append(vo.getZitem4_p());
		    		stb.append("%</td><td>");
		    		stb.append(vo.getZitem5_p());
		    		stb.append("%</td>");
		    		if(active_bcg_id.equals("N")) {
			    		stb.append("<td>");
			    		stb.append(vo.getZitem6_p());
			    		stb.append("%</td><td>");
			    		stb.append(vo.getZitem7_p());
			    		stb.append("%</td>");
		    		}
	    		}
	    		if(!brch_gb.equals("") && active_bcg_id.equals("N")) {
					stb.append("<td style='color:#F15F5F;'>");
					stb.append(vo.getZadmin_rank1());
					stb.append("</td>");
					/*
					stb.append("<td style='color:#F15F5F;'>");
					stb.append(vo.getZadmin_rank1());
					stb.append("</td><td style='color:#F15F5F;'>");
					stb.append(vo.getZadmin_rank2());
					stb.append("</td>");
					*/
	    		} else if(active_bcg_id.equals("C") || active_bcg_id.equals("S")){
	    			stb.append("<td style='color:#F15F5F;'>");
	    			stb.append(vo.getZadmin_rank1());
	    			stb.append("</td>");
	    			
	    			/*
	    			stb.append("<td style='color:#F15F5F;'>");
	    			stb.append(vo.getZadmin_rank1());
	    			stb.append("</td><td style='color:#F15F5F;'>");
	    			stb.append(vo.getZadmin_rank2());
	    			stb.append("</td>");
	    			*/ 
	    		}
				stb.append("</tr>");
	    	} else if(vo.getZlevel().equals("A") && (!brch_gb.equals("") || !active_bcg_id.equals("N"))) {
				stb.append("<tr class='bg-blue'><td>상위20%기준</td><td>");
				if(vo.getZitem1_p() == null) {
				stb.append("-</td><td>");
				} else {
					stb.append(vo.getZitem1_p());
					stb.append("%</td><td>");
				}
				if(vo.getZitem2_p() == null) {
				stb.append("-</td><td>");
				} else {
					stb.append(vo.getZitem2_p());
					stb.append("%</td><td>");
				}
				if(vo.getZitem3_p() == null) {
				stb.append("-</td><td>");
				} else {
					stb.append(vo.getZitem3_p());
					stb.append("%</td><td>");
				}
				if(vo.getZitem4_p() == null) {
				stb.append("-</td><td>");
				} else {
					stb.append(vo.getZitem4_p());
					stb.append("%</td><td>");
				}
				if(vo.getZitem5_p() == null) {
				stb.append("-</td>");
				} else {
					stb.append(vo.getZitem5_p());
					stb.append("%</td>");
				}
				if(active_bcg_id.equals("N")) {
					stb.append("<td>");
					if(vo.getZitem6_p() == null) {
						stb.append("-</td><td>");
					} else {
						stb.append(vo.getZitem6_p());
						stb.append("%</td><td>");
					}
					if(vo.getZitem7_p() == null) {
						stb.append("-</td>");
					} else {
						stb.append(vo.getZitem7_p());
						stb.append("%</td>");
					}
		    		if(!brch_gb.equals("")) {
						stb.append("<td>-</td>");
						//stb.append("<td>-</td><td>-</td>");
		    		}
				} else {
					stb.append("<td>-</td>");
					//stb.append("<td>-</td><td>-</td>");
				}
				stb.append("</tr>");
	    	} else if(vo.getZlevel().equals("B")) {
				stb.append("<tr class='bg-grey'><td>평균</td><td>");
				stb.append(vo.getZitem1_p());
				stb.append("%</td><td>");
				stb.append(vo.getZitem2_p());
				stb.append("%</td><td>");
				stb.append(vo.getZitem3_p());
				stb.append("%</td><td>");
				stb.append(vo.getZitem4_p());
				stb.append("%</td><td>");
				stb.append(vo.getZitem5_p());
				stb.append("%</td>");
				if(active_bcg_id.equals("N")) {
					stb.append("<td>");
					stb.append(vo.getZitem6_p());
					stb.append("%</td><td>");
					stb.append(vo.getZitem7_p());
					stb.append("%</td>");
					
		    		if(!brch_gb.equals("")) {
		    			stb.append("<td>-</td>");
						//stb.append("<td>-</td><td>-</td>");
		    		}
				} else {
					stb.append("<td>-</td>");
					//stb.append("<td>-</td><td>-</td>");
				}
				stb.append("</tr>");
	    	}
	    }
	    
		return stb.toString();
	}
	
	/**
	 * 주요지표 수치용
	 **/
	public String getResultHtmlForCount(List resultList, String active_bcg_id, String brch_gb, HttpServletRequest request) {
		StringBuilder stb = new StringBuilder();
		if(resultList.size() < 3) {
			return "N";
		}
	    for(int i = 0; i < resultList.size(); i++) {
	    	SumMainVO vo = (SumMainVO) resultList.get(i);
	    	if(vo.getZlevel().equals("3")) {
	    		stb.append("<tr class='sum-node-brch ");
	    		stb.append(vo.getZregion());
	    		stb.append(" ");
	    		stb.append(vo.getZdept_id());
	    		stb.append(" ");
	    		stb.append(vo.getZdept_brch_id());
	    		stb.append(" zlevel");
	    		stb.append(vo.getZlevel());
	    		stb.append("' zregion='");
	    		stb.append(vo.getZregion());
	    		stb.append("' zdept_id='");
	    		stb.append(vo.getZdept_id());
	    		stb.append("' zdept_brch_id='");
	    		stb.append(vo.getZdept_brch_id());
	    		stb.append("' zreq_ym='");
	    		stb.append(vo.getZrms_ym());
	    		stb.append("' zlevel='");
	    		stb.append(vo.getZlevel());
	    		if(vo.getCheck_gb().equals("Y")) {
	    			if(request.getSession().getAttribute("zauth_grp").equals("NC") || request.getSession().getAttribute("zauth_grp").equals("NP")) {
		    			if(request.getSession().getAttribute("zdept_id").equals(vo.getZdept_id())) {
		    				stb.append("'><td class='left'>&emsp;&emsp;&emsp;&emsp;<i class='fa fa-plus-square' style='color:#91a9ca; cursor: pointer;' onclick='getAddData(this)'></i>&emsp;&emsp;");
		    			} else {
		    				stb.append("' style='display:none;'><td class='left'>&emsp;&emsp;&emsp;&emsp;<i class='fa fa-plus-square' style='color:#91a9ca; cursor: pointer;' onclick='getAddData(this)'></i>&emsp;&emsp;");
		    			}
	    			} else {
	    				stb.append("' style='display:none;'><td class='left'>&emsp;&emsp;&emsp;&emsp;<i class='fa fa-plus-square' style='color:#91a9ca; cursor: pointer;' onclick='getAddData(this)'></i>&emsp;&emsp;");
	    			}
	    		} else {
	    			stb.append("' style='display:none;'><td class='left'>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;");
	    		}
	    		if(vo.getZdept_brch_nm().trim().equals("")) {
	    			stb.append(vo.getZdept_brch_id());
	    			if(vo.getZbrch_emp_nm() != null) {
		    			stb.append(" [");
		    			stb.append(vo.getZbrch_emp_nm());
		    			stb.append("]");
	    			}
	    		} else {
	    			stb.append(vo.getZdept_brch_id());
	    			if(vo.getZbrch_emp_nm() != null) {
		    			stb.append(" [");
		    			stb.append(vo.getZbrch_emp_nm());
		    			stb.append("]");
	    			}
	    			stb.append(" (");
	    			stb.append(vo.getZdept_brch_nm());
	    			stb.append(")");
	    		}
	    		if(vo.getCheck_gb().equals("Y")) {
		    		stb.append("</td><td class='sum-item1'>");
		    		stb.append(vo.getZitem1_s());
		    		stb.append("</td><td class='sum-item2'>");
		    		stb.append(getFormatForAmt(vo.getZitem2_s()));
		    		stb.append("</td><td class='sum-item3'>");
		    		stb.append(vo.getZitem3_s());
		    		stb.append("</td><td class='sum-item4'>");
		    		stb.append(vo.getZitem4_s());
		    		stb.append("</td><td class='sum-item5'>");
		    		stb.append(vo.getZitem5_s());
		    		stb.append("</td>");
		    		if(active_bcg_id.equals("N")) {
			    		stb.append("<td class='sum-item6'>");
			    		stb.append(vo.getZitem6_s());
			    		stb.append("</td><td class='sum-item7'>");
			    		stb.append(vo.getZitem7_s());
			    		stb.append("</td>");
		    		}
	    		} else {
		    		stb.append("</td><td>");
		    		stb.append(vo.getZitem1_s());
		    		stb.append("</td><td>");
		    		stb.append(getFormatForAmt(vo.getZitem2_s()));
		    		stb.append("</td><td>");
		    		stb.append(vo.getZitem3_s());
		    		stb.append("</td><td>");
		    		stb.append(vo.getZitem4_s());
		    		stb.append("</td><td>");
		    		stb.append(vo.getZitem5_s());
		    		stb.append("</td>");
		    		if(active_bcg_id.equals("N")) {
			    		stb.append("<td>");
			    		stb.append(vo.getZitem6_s());
			    		stb.append("</td><td>");
			    		stb.append(vo.getZitem7_s());
			    		stb.append("</td>");
		    		}
	    		}
	    		if(!brch_gb.equals("") && active_bcg_id.equals("N")) {
					if(brch_gb.equals("BL")) {
						stb.append("<td>-");
						stb.append("</td>");
						
						/*
						stb.append("<td>-");
						stb.append("</td><td>");
						stb.append("-");
						stb.append("</td>");
						 */
					} else {
						stb.append("<td style='color:#47C83E;'>");
						stb.append(vo.getZadmin_rank1());
						stb.append("</td>");
						
						/*
						stb.append("<td style='color:#47C83E;'>");
						stb.append(vo.getZadmin_rank1());
						stb.append("</td><td style='color:#47C83E;'>");
						stb.append(vo.getZadmin_rank2());
						stb.append("</td>");
						 */
					}
	    		} else if(active_bcg_id.equals("C") || active_bcg_id.equals("S")){
	    			stb.append("<td style='color:#47C83E;'>");
	    			stb.append(vo.getZadmin_rank1());
	    			stb.append("</td>");
	    			
	    			/*
	    			stb.append("<td style='color:#47C83E;'>");
	    			stb.append(vo.getZadmin_rank1());
	    			stb.append("</td><td style='color:#47C83E;'>");
	    			stb.append(vo.getZadmin_rank2());
	    			stb.append("</td>");
	    			 */ 
	    		}
				stb.append("</tr>");
	    	} else if(vo.getZlevel().equals("2")) {
	    		stb.append("<tr class='sum-node-dept ");
	    		stb.append(vo.getZregion());
	    		stb.append(" ");
	    		stb.append(vo.getZdept_id());
	    		stb.append(" zlevel");
	    		stb.append(vo.getZlevel());
	    		stb.append("' zregion='");
	    		stb.append(vo.getZregion());
	    		stb.append("' zdept_id='");
	    		stb.append(vo.getZdept_id());
	    		stb.append("' zreq_ym='");
	    		stb.append(vo.getZrms_ym());
	    		stb.append("' zlevel='");
	    		stb.append(vo.getZlevel());
	    		if(vo.getCheck_gb().equals("Y")) {
	    			if(request.getSession().getAttribute("zauth_grp").equals("NC") || request.getSession().getAttribute("zauth_grp").equals("NP")) {
		    			if(request.getSession().getAttribute("zdept_id").equals(vo.getZdept_id())) {
		    				stb.append("'><td class='left'>&emsp;&emsp;<i class='fa fa-minus-square' style='cursor: pointer;' onclick='getAddData(this)'></i>&emsp;&emsp;");
		    			} else {
		    				stb.append("' style='display:none;'><td class='left'>&emsp;&emsp;<i class='fa fa-plus-square' style='color:#91a9ca; cursor: pointer;' onclick='getAddData(this)'></i>&emsp;&emsp;");
		    			}
	    			} else {
	    				stb.append("' style='display:none;'><td class='left'>&emsp;&emsp;<i class='fa fa-plus-square' style='color:#91a9ca; cursor: pointer;' onclick='getAddData(this)'></i>&emsp;&emsp;");
	    			}
	    		} else {
	    			stb.append("' style='display:none;'><td class='left'>&emsp;&emsp;&emsp;&emsp;");
	    		}
	    		stb.append(vo.getZdept_nm());
	    		if(vo.getZdept_emp_nm() != null) {
		    		stb.append(" [");
		    		stb.append(vo.getZdept_emp_nm());
		    		stb.append("]");
	    		}
	    		if(vo.getCheck_gb().equals("Y")) {
		    		stb.append("</td><td class='sum-item1'>");
		    		stb.append(vo.getZitem1_s());
		    		stb.append("</td><td class='sum-item2'>");
		    		stb.append(getFormatForAmt(vo.getZitem2_s()));
		    		stb.append("</td><td class='sum-item3'>");
		    		stb.append(vo.getZitem3_s());
		    		stb.append("</td><td class='sum-item4'>");
		    		stb.append(vo.getZitem4_s());
		    		stb.append("</td><td class='sum-item5'>");
		    		stb.append(vo.getZitem5_s());
		    		stb.append("</td>");
		    		if(active_bcg_id.equals("N")) {
			    		stb.append("<td class='sum-item6'>");
			    		stb.append(vo.getZitem6_s());
			    		stb.append("</td><td class='sum-item7'>");
			    		stb.append(vo.getZitem7_s());
			    		stb.append("</td>");
		    		}
	    		} else {
		    		stb.append("</td><td>");
		    		stb.append(vo.getZitem1_s());
		    		stb.append("</td><td>");
		    		stb.append(getFormatForAmt(vo.getZitem2_s()));
		    		stb.append("</td><td>");
		    		stb.append(vo.getZitem3_s());
		    		stb.append("</td><td>");
		    		stb.append(vo.getZitem4_s());
		    		stb.append("</td><td>");
		    		stb.append(vo.getZitem5_s());
		    		stb.append("</td>");
		    		if(active_bcg_id.equals("N")) {
			    		stb.append("<td>");
			    		stb.append(vo.getZitem6_s());
			    		stb.append("</td><td>");
			    		stb.append(vo.getZitem7_s());
			    		stb.append("</td>");
		    		}
	    		}
	    		if(!brch_gb.equals("") && active_bcg_id.equals("N")) {
					if(brch_gb.equals("LC")) {
						stb.append("<td>-");
						stb.append("</td>");
						
						/*
						stb.append("<td>-");
						stb.append("</td><td>");
						stb.append("-");
						stb.append("</td>");
						 */ 
					} else {
						stb.append("<td style='color:#6799FF;'>");
						stb.append(vo.getZadmin_rank1());
						stb.append("</td>");
						
						/*
						stb.append("<td style='color:#6799FF;'>");
						stb.append(vo.getZadmin_rank1());
						stb.append("</td><td style='color:#6799FF;'>");
						stb.append(vo.getZadmin_rank2());
						stb.append("</td>");
						 */ 
					}
	    		} else if(active_bcg_id.equals("C") || active_bcg_id.equals("S")){
	    			stb.append("<td style='color:#6799FF;'>");
	    			stb.append(vo.getZadmin_rank1());
	    			stb.append("</td>");
	    			
	    			/*
	    			stb.append("<td style='color:#6799FF;'>");
	    			stb.append(vo.getZadmin_rank1());
	    			stb.append("</td><td style='color:#6799FF;'>");
	    			stb.append(vo.getZadmin_rank2());
	    			stb.append("</td>"); 
	    			 */
	    		}
				stb.append("</tr>");
	    	} else if(vo.getZlevel().equals("1")) {
	    		stb.append("<tr class='sum-node-bcg ");
	    		stb.append(vo.getZregion());
	    		stb.append(" zlevel");
	    		stb.append(vo.getZlevel());
	    		stb.append("' zregion='");
	    		stb.append(vo.getZregion());
	    		stb.append("' zreq_ym='");
	    		stb.append(vo.getZrms_ym());
	    		stb.append("' zlevel='");
	    		stb.append(vo.getZlevel());
	    		if(vo.getCheck_gb().equals("Y")) {
	    			if(request.getSession().getAttribute("zauth_grp").equals("NC") || request.getSession().getAttribute("zauth_grp").equals("NP")) {
		    			if(request.getSession().getAttribute("zregion").equals(vo.getZregion())) {
		    				stb.append("'><td class='left'><i class='fa fa-minus-square' style='cursor: pointer;' onclick='getAddData(this)'></i>&emsp;&emsp;");
		    			} else {
		    				stb.append("'><td class='left'><i class='fa fa-plus-square' style='color:#91a9ca; cursor: pointer;' onclick='getAddData(this)'></i>&emsp;&emsp;");
		    			}
	    			} else {
	    				stb.append("'><td class='left'><i class='fa fa-plus-square' style='color:#91a9ca; cursor: pointer;' onclick='getAddData(this)'></i>&emsp;&emsp;");
	    			}
	    		} else {
	    			stb.append("'><td class='left'>&emsp;&emsp;&emsp;");
	    		}
	    		stb.append(vo.getZregion_nm());
	    		if(vo.getCheck_gb().equals("Y")) {
		    		stb.append("</td><td class='sum-item1'>");
		    		stb.append(vo.getZitem1_s());
		    		stb.append("</td><td class='sum-item2'>");
		    		stb.append(getFormatForAmt(vo.getZitem2_s()));
		    		stb.append("</td><td class='sum-item3'>");
		    		stb.append(vo.getZitem3_s());
		    		stb.append("</td><td class='sum-item4'>");
		    		stb.append(vo.getZitem4_s());
		    		stb.append("</td><td class='sum-item5'>");
		    		stb.append(vo.getZitem5_s());
		    		stb.append("</td>");
		    		if(active_bcg_id.equals("N")) {
			    		stb.append("<td class='sum-item6'>");
			    		stb.append(vo.getZitem6_s());
			    		stb.append("</td><td class='sum-item7'>");
			    		stb.append(vo.getZitem7_s());
			    		stb.append("</td>");
		    		}
	    		} else {
		    		stb.append("</td><td>");
		    		stb.append(vo.getZitem1_s());
		    		stb.append("</td><td>");
		    		stb.append(getFormatForAmt(vo.getZitem2_s()));
		    		stb.append("</td><td>");
		    		stb.append(vo.getZitem3_s());
		    		stb.append("</td><td>");
		    		stb.append(vo.getZitem4_s());
		    		stb.append("</td><td>");
		    		stb.append(vo.getZitem5_s());
		    		stb.append("</td>");
		    		if(active_bcg_id.equals("N")) {
			    		stb.append("<td>");
			    		stb.append(vo.getZitem6_s());
			    		stb.append("</td><td>");
			    		stb.append(vo.getZitem7_s());
			    		stb.append("</td>");
		    		}
	    		}
	    		if(!brch_gb.equals("") && active_bcg_id.equals("N")) {
					stb.append("<td style='color:#F15F5F;'>");
					stb.append(vo.getZadmin_rank1());
					stb.append("</td>");
					
					/*
					stb.append("<td style='color:#F15F5F;'>");
					stb.append(vo.getZadmin_rank1());
					stb.append("</td><td style='color:#F15F5F;'>");
					stb.append(vo.getZadmin_rank2());
					stb.append("</td>");
					 */ 
	    		} else if(active_bcg_id.equals("C") || active_bcg_id.equals("S")){
	    			stb.append("<td style='color:#F15F5F;'>");
	    			stb.append(vo.getZadmin_rank1());
	    			stb.append("</td>");
	    			
					/*
	    			stb.append("<td style='color:#F15F5F;'>");
	    			stb.append(vo.getZadmin_rank1());
	    			stb.append("</td><td style='color:#F15F5F;'>");
	    			stb.append(vo.getZadmin_rank2());
	    			stb.append("</td>");
					 */ 
	    		}
				stb.append("</tr>");
	    	} else if(vo.getZlevel().equals("A") && (!brch_gb.equals("") || !active_bcg_id.equals("N"))) {
				stb.append("<tr class='bg-blue'><td>상위20%기준</td><td>");
				if(vo.getZitem1_s() == null) {
				stb.append("-</td><td>");
				} else {
		    		stb.append(vo.getZitem1_s());
					stb.append("</td><td>");
				}
				if(vo.getZitem2_s() == null) {
				stb.append("-</td><td>");
				} else {
		    		stb.append(getFormatForAmt(vo.getZitem2_s()));
					stb.append("</td><td>");
				}
				if(vo.getZitem3_s() == null) {
				stb.append("-</td><td>");
				} else {
		    		stb.append(vo.getZitem3_s());
					stb.append("</td><td>");
				}
				if(vo.getZitem4_s() == null) {
				stb.append("-</td><td>");
				} else {
		    		stb.append(vo.getZitem4_s());
					stb.append("</td><td>");
				}
				if(vo.getZitem5_s() == null) {
				stb.append("-</td>");
				} else {
		    		stb.append(vo.getZitem5_s());
					stb.append("</td>");
				}
				if(active_bcg_id.equals("N")) {
					stb.append("<td>");
					if(vo.getZitem6_s() == null) {
						stb.append("-</td><td>");
					} else {
			    		stb.append(vo.getZitem6_s());
						stb.append("</td><td>");
					}
					if(vo.getZitem7_s() == null) {
						stb.append("-</td>");
					} else {
						stb.append(vo.getZitem7_s());
						stb.append("</td>");
					}
		    		if(!brch_gb.equals("")) {
						stb.append("<td>-</td>");
						//stb.append("<td>-</td><td>-</td>");
		    		}
				} else {
					stb.append("<td>-</td>");
					//stb.append("<td>-</td><td>-</td>");
				}
				stb.append("</tr>");
	    	} else if(vo.getZlevel().equals("B")) {
				stb.append("<tr class='bg-grey'><td>평균</td><td>");
				stb.append(vo.getZitem1_s());
				stb.append("</td><td>");
				stb.append(getFormatForAmt(vo.getZitem2_s()));
				stb.append("</td><td>");
				stb.append(vo.getZitem3_s());
				stb.append("</td><td>");
				stb.append(vo.getZitem4_s());
				stb.append("</td><td>");
				stb.append(vo.getZitem5_s());
				stb.append("</td>");
				if(active_bcg_id.equals("N")) {
					stb.append("<td>");
					stb.append(vo.getZitem6_s());
					stb.append("</td><td>");
					stb.append(vo.getZitem7_s());
					stb.append("</td>");
					
		    		if(!brch_gb.equals("")) {
						stb.append("<td>-</td>");
						//stb.append("<td>-</td><td>-</td>");
		    		}
				} else {
					stb.append("<td>-</td>");
					//stb.append("<td>-</td><td>-</td>");
				}
				stb.append("</tr>");
	    	}
	    }
	    
		return stb.toString();
	}
	
	public String getResultDtlHtml(List resultList, String active_bcg_id) {
		StringBuilder stb = new StringBuilder();
		
	    for(int i = 0; i < resultList.size(); i++) {
	    	SumMainVO vo = (SumMainVO) resultList.get(i);
			if(vo.getZlevel().equals("5")) {
				stb.append("<tr class='sum-node-cls ");
	    		stb.append(vo.getZregion());
	    		stb.append(" ");
	    		stb.append(vo.getZdept_id());
	    		stb.append(" ");
	    		stb.append(vo.getZdept_brch_id());
	    		stb.append(" ");
	    		stb.append(vo.getZemp_id());
	    		stb.append(" ");
	    		stb.append(vo.getZcls_id());
	    		stb.append(" zlevel");
	    		stb.append(vo.getZlevel());
	    		stb.append("' zregion='");
	    		stb.append(vo.getZregion());
	    		stb.append("' zdept_id='");
	    		stb.append(vo.getZdept_id());
	    		stb.append("' zdept_brch_id='");
	    		stb.append(vo.getZdept_brch_id());
	    		stb.append("' zemp_id='");
	    		stb.append(vo.getZemp_id());
	    		stb.append("' zcls_id='");
	    		stb.append(vo.getZcls_id());
	    		stb.append("' zreq_ym='");
	    		stb.append(vo.getZrms_ym());
	    		stb.append("' zlevel='");
	    		stb.append(vo.getZlevel());
	    		stb.append("' style='display:none;'><td class='left'>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;");
	    		stb.append(vo.getZcls_nm());
	    		stb.append("</td><td class='sum-item1'>");
	    		stb.append(vo.getZitem1_p());
	    		stb.append("</td><td class='sum-item2'>");
	    		stb.append(vo.getZitem2_p());
	    		stb.append("</td><td class='sum-item3'>");
	    		stb.append(vo.getZitem3_p());
	    		stb.append("</td><td class='sum-item4'>");
	    		stb.append(vo.getZitem4_p());
	    		stb.append("</td><td class='sum-item5'>");
	    		stb.append(vo.getZitem5_p());
	    		stb.append("</td>");
	    		if(active_bcg_id.equals("N")) {
		    		stb.append("<td class='sum-item6'>");
		    		stb.append(vo.getZitem6_p());
		    		stb.append("</td><td class='sum-item7'>");
		    		stb.append(vo.getZitem7_p());
		    		stb.append("</td>");
	    		}
	    		stb.append("</tr>");
	    	} if(vo.getZlevel().equals("4")) {
	    		stb.append("<tr class='sum-node-emp ");
	    		stb.append(vo.getZregion());
	    		stb.append(" ");
	    		stb.append(vo.getZdept_id());
	    		stb.append(" ");
	    		stb.append(vo.getZdept_brch_id());
	    		stb.append(" ");
	    		stb.append(vo.getZemp_id());
	    		stb.append(" zlevel");
	    		stb.append(vo.getZlevel());
	    		stb.append("' zregion='");
	    		stb.append(vo.getZregion());
	    		stb.append("' zdept_id='");
	    		stb.append(vo.getZdept_id());
	    		stb.append("' zdept_brch_id='");
	    		stb.append(vo.getZdept_brch_id());
	    		stb.append("' zemp_id='");
	    		stb.append(vo.getZemp_id());
	    		stb.append("' zreq_ym='");
	    		stb.append(vo.getZrms_ym());
	    		stb.append("' zlevel='");
	    		stb.append(vo.getZlevel());
	    		stb.append("'><td class='left'>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<i class='fa fa-plus-square' style='color:#91a9ca; cursor: pointer;' onclick='getAddData(this)'></i>&emsp;&emsp;");
	    		stb.append(vo.getZemp_nm());
	    		stb.append(" [");
	    		stb.append((vo.getZemp_id()).substring(2));
	    		stb.append("]");
	    		stb.append("</td><td class='sum-item1'>");
	    		stb.append(vo.getZitem1_p());
	    		stb.append("</td><td class='sum-item2'>");
	    		stb.append(vo.getZitem2_p());
	    		stb.append("</td><td class='sum-item3'>");
	    		stb.append(vo.getZitem3_p());
	    		stb.append("</td><td class='sum-item4'>");
	    		stb.append(vo.getZitem4_p());
	    		stb.append("</td><td class='sum-item5'>");
	    		stb.append(vo.getZitem5_p());
	    		stb.append("</td>");
	    		if(active_bcg_id.equals("N")) {
		    		stb.append("<td class='sum-item6'>");
		    		stb.append(vo.getZitem6_p());
		    		stb.append("</td><td class='sum-item7'>");
		    		stb.append(vo.getZitem7_p());
		    		stb.append("</td>");
	    		}
	    		stb.append("</tr>");
	    	}
	    }
    	
		return stb.toString();
	}
	
	public String getResultDtlHtmlForCount(List resultList, String active_bcg_id) {
		StringBuilder stb = new StringBuilder();
		
	    for(int i = 0; i < resultList.size(); i++) {
	    	SumMainVO vo = (SumMainVO) resultList.get(i);
			if(vo.getZlevel().equals("5")) {
				stb.append("<tr class='sum-node-cls ");
	    		stb.append(vo.getZregion());
	    		stb.append(" ");
	    		stb.append(vo.getZdept_id());
	    		stb.append(" ");
	    		stb.append(vo.getZdept_brch_id());
	    		stb.append(" ");
	    		stb.append(vo.getZemp_id());
	    		stb.append(" ");
	    		stb.append(vo.getZcls_id());
	    		stb.append(" zlevel");
	    		stb.append(vo.getZlevel());
	    		stb.append("' zregion='");
	    		stb.append(vo.getZregion());
	    		stb.append("' zdept_id='");
	    		stb.append(vo.getZdept_id());
	    		stb.append("' zdept_brch_id='");
	    		stb.append(vo.getZdept_brch_id());
	    		stb.append("' zemp_id='");
	    		stb.append(vo.getZemp_id());
	    		stb.append("' zcls_id='");
	    		stb.append(vo.getZcls_id());
	    		stb.append("' zreq_ym='");
	    		stb.append(vo.getZrms_ym());
	    		stb.append("' zlevel='");
	    		stb.append(vo.getZlevel());
	    		stb.append("' style='display:none;'><td class='left'>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;");
	    		stb.append(vo.getZcls_nm());
	    		stb.append("</td><td class='sum-item1'>");
	    		stb.append(vo.getZitem1_s());
	    		stb.append("</td><td class='sum-item2'>");
	    		stb.append(getFormatForAmt(vo.getZitem2_s()));
	    		stb.append("</td><td class='sum-item3'>");
	    		stb.append(vo.getZitem3_s());
	    		stb.append("</td><td class='sum-item4'>");
	    		stb.append(vo.getZitem4_s());
	    		stb.append("</td><td class='sum-item5'>");
	    		stb.append(vo.getZitem5_s());
	    		stb.append("</td>");
	    		if(active_bcg_id.equals("N")) {
		    		stb.append("<td class='sum-item6'>");
		    		stb.append(vo.getZitem6_s());
		    		stb.append("</td><td class='sum-item7'>");
		    		stb.append(vo.getZitem7_s());
		    		stb.append("</td>");
	    		}
	    		stb.append("</tr>");
	    	} if(vo.getZlevel().equals("4")) {
	    		stb.append("<tr class='sum-node-emp ");
	    		stb.append(vo.getZregion());
	    		stb.append(" ");
	    		stb.append(vo.getZdept_id());
	    		stb.append(" ");
	    		stb.append(vo.getZdept_brch_id());
	    		stb.append(" ");
	    		stb.append(vo.getZemp_id());
	    		stb.append(" zlevel");
	    		stb.append(vo.getZlevel());
	    		stb.append("' zregion='");
	    		stb.append(vo.getZregion());
	    		stb.append("' zdept_id='");
	    		stb.append(vo.getZdept_id());
	    		stb.append("' zdept_brch_id='");
	    		stb.append(vo.getZdept_brch_id());
	    		stb.append("' zemp_id='");
	    		stb.append(vo.getZemp_id());
	    		stb.append("' zreq_ym='");
	    		stb.append(vo.getZrms_ym());
	    		stb.append("' zlevel='");
	    		stb.append(vo.getZlevel());
	    		stb.append("'><td class='left'>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<i class='fa fa-plus-square' style='color:#91a9ca; cursor: pointer;' onclick='getAddData(this)'></i>&emsp;&emsp;");
	    		stb.append(vo.getZemp_nm());
	    		stb.append(" [");
	    		stb.append((vo.getZemp_id()).substring(2));
	    		stb.append("]");
	    		stb.append("</td><td class='sum-item1'>");
	    		stb.append(vo.getZitem1_s());
	    		stb.append("</td><td class='sum-item2'>");
	    		stb.append(getFormatForAmt(vo.getZitem2_s()));
	    		stb.append("</td><td class='sum-item3'>");
	    		stb.append(vo.getZitem3_s());
	    		stb.append("</td><td class='sum-item4'>");
	    		stb.append(vo.getZitem4_s());
	    		stb.append("</td><td class='sum-item5'>");
	    		stb.append(vo.getZitem5_s());
	    		stb.append("</td>");
	    		if(active_bcg_id.equals("N")) {
		    		stb.append("<td class='sum-item6'>");
		    		stb.append(vo.getZitem6_s());
		    		stb.append("</td><td class='sum-item7'>");
		    		stb.append(vo.getZitem7_s());
		    		stb.append("</td>");
	    		}
	    		stb.append("</tr>");
	    	}
	    }
    	
		return stb.toString();
	}

	@Override
	public List getSumRank(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		
		String line = "";
		String zreq_ym = "";
		String active_bcg_id = "";
		String brch_gb = "";
		String cnt_gb = "";
		
		String session_zregion = (String) session.getAttribute("zregion");
		String session_zdept_id = (String) session.getAttribute("zdept_id");
		String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
		String session_zemp_id = (String) session.getAttribute("zemp_id");
		String session_zauth_grp = (String) session.getAttribute("zauth_grp");
		String session_zregion_gb = (String) session.getAttribute("zregion_gb");
		
		// Json으로 변수 받음
		BufferedReader reader = request.getReader();
		while ((line = reader.readLine()) != null) {
			JSONObject jsonObj = (JSONObject) JSONValue.parse(line);

			zreq_ym = (String) jsonObj.get("zreq_ym");
			active_bcg_id = (String) jsonObj.get("active_bcg_id");
			brch_gb = (String) jsonObj.get("brch_gb");
			cnt_gb = (String) jsonObj.get("cnt_gb");
		}
		
		// 파라미터
		HashMap<String, String> paramMap = new HashMap();
		paramMap.put("session_zregion", session_zregion);
		paramMap.put("session_zdept_id", session_zdept_id);
		paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
		paramMap.put("session_zemp_id", session_zemp_id);
		paramMap.put("session_zregion_gb", session_zregion_gb);
		paramMap.put("zreq_ym", zreq_ym);
		paramMap.put("brch_gb", brch_gb);
		paramMap.put("cnt_gb", cnt_gb);
		paramMap.put("active_bcg_id", active_bcg_id);
		paramMap.put("session_zauth_grp", session_zauth_grp);
		
		if(cnt_gb.equals("ran")) { // 순위
			
			// 평균, 상위20% 2개 말고 데이터없으면 return array 없음. 
			List resultList = sumMainMapper.getSumRank(paramMap);
			return resultList.size() > 2 ? resultList : new ArrayList();
		} else if(cnt_gb.equals("ind")){ // 비중
			//return sumMainMapper.getSumRank(paramMap);
			// return sumMainMapper.getSumRankPer(paramMap);
			
			List resultList = sumMainMapper.getSumRankInd(paramMap);
			return resultList.size() > 2 ? resultList : new ArrayList();
		} else { // 건수
			//return sumMainMapper.getSumRankCnt(paramMap);
			
			List resultList = sumMainMapper.getSumRankCnt(paramMap);
			return resultList.size() > 2 ? resultList : new ArrayList();
		}
	}
	


}
