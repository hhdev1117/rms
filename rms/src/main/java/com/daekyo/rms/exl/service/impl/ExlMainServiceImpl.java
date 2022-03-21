package com.daekyo.rms.exl.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daekyo.rms.exl.dao.ExlMainDAO;
import com.daekyo.rms.exl.service.ExlMainService;
import com.daekyo.rms.exl.vo.ExcelNstZeroStudyVO;
import com.daekyo.rms.exl.vo.ExlArrPaymentVO;
import com.daekyo.rms.exl.vo.ExlCardPaymentVO;
import com.daekyo.rms.exl.vo.ExlIndAuthVO;
import com.daekyo.rms.exl.vo.ExlMonMoniVO;
import com.daekyo.rms.exl.vo.ExlMoniSumVO;
import com.daekyo.rms.exl.vo.ExlPayDirectVO;
import com.daekyo.rms.exl.vo.ExlSmiRiskVO;
import com.daekyo.rms.exl.vo.ExlStmApplyVO;
import com.daekyo.rms.exl.vo.ExlTranStudyVO;
import com.daekyo.rms.exl.vo.ExlTrsDupVO;
import com.daekyo.rms.sum.vo.SumMainVO;
import com.daekyo.rms.util.ExcelUtil;

@Service("exlMainService")
public class ExlMainServiceImpl implements ExlMainService {

	private static final Logger logger = LoggerFactory.getLogger(ExlMainServiceImpl.class);

	@Autowired
	private ExlMainDAO exlMainMapper;
	
	/*******************************************
	 * 엑셀 상단 다운로드 시간 추출 		   *
	 *******************************************/
	public String getDownloadDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return sdf.format(cal.getTime());
	}
	
	/*******************************************
	 * 금액 세자리 콤마 적용 				   *
	 *******************************************/
	public String getFormatForAmt(String amt) {
		DecimalFormat fommater = new DecimalFormat("###,###");
		
		if(amt.equals("-")) return "-";
		
		return fommater.format(Integer.parseInt(amt));
	}
	
	/*******************************************
	 * 엑셀 로그 입력				 		   *
	 *******************************************/
	public int insertExcelLog(HttpSession session, int rowcnt, String url) {
		HashMap paramMap = new HashMap();
		paramMap.put("session_zlogin_id", session.getAttribute("zlogin_id"));
		paramMap.put("session_zauth_grp", session.getAttribute("zauth_grp"));
		paramMap.put("session_zdept_id", session.getAttribute("zdept_id"));
		paramMap.put("session_zdept_brch_id", session.getAttribute("zdept_brch_id"));
		paramMap.put("session_zemp_id", session.getAttribute("zemp_id"));
		paramMap.put("rowcnt", rowcnt + "");
		paramMap.put("zmenu_url", url);
		
		return exlMainMapper.insertExcelLog(paramMap);
	}
	
	/*******************************************
	 * [ 현장용 ] 주요지표 엑셀다운 		   *
	 *******************************************/
	@Transactional
	@Override
	public void excelMoniSum_d(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			HttpSession session = request.getSession();
			
			String zregion = request.getParameter("zregion");
			String zdept_id = request.getParameter("zdept_id");
			String zdept_brch_id = request.getParameter("zdept_brch_id");
			String zemp_id = request.getParameter("zemp_id");
			String zreq_ym_b = request.getParameter("zreq_ym_b");
			String zreq_ym_a = request.getParameter("zreq_ym_a");
			String passwd = request.getParameter("passwd");
			String active_bcg_id = request.getParameter("active_bcg_id");
			String brch_gb = request.getParameter("brch_gb");
			
			String session_zregion = (String) session.getAttribute("zregion");
			String session_zdept_id = (String) session.getAttribute("zdept_id");
			String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
			String session_zemp_id = (String) session.getAttribute("zemp_id");
			String session_zauth_grp = (String) session.getAttribute("zauth_grp");
			String session_zregion_gb = (String) session.getAttribute("zregion_gb");
			
			HashMap paramMap = new HashMap();
			paramMap.put("zregion", zregion);
			paramMap.put("zdept_id", zdept_id);
			paramMap.put("zdept_brch_id", zdept_brch_id);
			paramMap.put("zemp_id", zemp_id);
			paramMap.put("zreq_ym_b", zreq_ym_b);
			paramMap.put("zreq_ym_a", zreq_ym_a);
			paramMap.put("passwd", passwd);
			paramMap.put("active_bcg_id", active_bcg_id);
			paramMap.put("brch_gb", brch_gb);
			paramMap.put("session_zregion", session_zregion);
			paramMap.put("session_zdept_id", session_zdept_id);
			paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
			paramMap.put("session_zemp_id", session_zemp_id);
			paramMap.put("session_zauth_grp", session_zauth_grp);
			paramMap.put("session_zregion_gb", session_zregion_gb);
			
			// 데이터 조회
			List selectReportDataList = exlMainMapper.excelMoniSum_d(paramMap);
			
			ExcelUtil excelUtil = ExcelUtil.getInstance();
			excelUtil.setRequest(request);
			excelUtil.setResponse(response);
			excelUtil.setFilename("주요지표");
			excelUtil.setTitle("엑셀다운로드일시 : " + getDownloadDate());
			excelUtil.setTitleindex(1);
			
			SXSSFWorkbook workbook = excelUtil.getSxssfWorkbook("tplMoniSum_d.xlsx");
			if(!active_bcg_id.equals("N")) {
				workbook = excelUtil.getSxssfWorkbook("tplMoniSum_cs_d.xlsx");
			}
			
			workbook.setCompressTempFiles(true);
			
			// 첫번째 시트 생성
			SXSSFSheet sheet = workbook.getSheetAt(0);
			
			// 템플릿을 제외한 마지막 ROWNUM을 읽는다(해당 ROW부터 쓰기위해서)
			int rowNo = 3;
			
			if(selectReportDataList.size() > 2) {
				// 평균
				XSSFCellStyle avgStyle = (XSSFCellStyle) workbook.createCellStyle();
				Font avgFont = avgStyle.getFont();
				avgFont.setFontHeight((short) 200);
				avgFont.setBold(true);
				avgStyle.setBorderRight(BorderStyle.THIN);
				avgStyle.setBorderLeft(BorderStyle.THIN);
				avgStyle.setBorderTop(BorderStyle.THIN);
				avgStyle.setBorderBottom(BorderStyle.THIN);
				avgStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				//avgStyle.setAlignment(HorizontalAlignment.CENTER);
				avgStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(218, 238, 243))); 
				avgStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				
				sheet.addMergedRegionUnsafe(new CellRangeAddress(3, 3, 0, 7));
				
				int avgIndex = 0;
				ExlMoniSumVO avgVo = (ExlMoniSumVO) selectReportDataList.get(0);
				
				SXSSFRow avgRow = sheet.createRow(rowNo++);
				SXSSFCell avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				avgCell.setCellValue("평균");
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				avgCell.setCellValue(avgVo.getZitem1_p());
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				avgCell.setCellValue(avgVo.getZitem2_p());
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				avgCell.setCellValue(avgVo.getZitem3_p());
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				avgCell.setCellValue(avgVo.getZitem4_p());
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				avgCell.setCellValue(avgVo.getZitem5_p());
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				avgCell.setCellValue(avgVo.getZitem6_p());
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				avgCell.setCellValue(avgVo.getZitem7_p());
			}
			
			// 셀스타일 생성(상하좌우 테두리)
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			Font font = style.getFont();
			font.setBold(false);
			font.setFontHeight((short) 200);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			// 셀에 데이터를 넣는 부분
			for (int i = 1; i < selectReportDataList.size(); i++) {
				// 1열 작성
				SXSSFRow row = sheet.createRow(rowNo++);
				
				ExlMoniSumVO vo = (ExlMoniSumVO) selectReportDataList.get(i);
				int index = 0;
				
				SXSSFCell cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZregion_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_emp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_brch_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_brch_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZemp_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZemp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZcls_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem1_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem2_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem3_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem4_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem5_p());
				
				if(active_bcg_id.equals("N")) {
					cell = row.createCell(index++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getZitem6_p());
					
					cell = row.createCell(index++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getZitem7_p());
				}
				
			}
			
			insertExcelLog(session, selectReportDataList.size(), request.getServletPath());
			
			// 엑셀 다운
			excelUtil.runToExcel(workbook, passwd);
			
		} catch (IOException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );  
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );	
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
			
		} catch(Exception e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );			
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
		}
	}

	/*******************************************
	 * 주요지표 LC, BL용 엑셀다운 		  	   *
	 *******************************************/
	@Override
	public void excelMoniSum_ex_d(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			HttpSession session = request.getSession();
			
			String zregion = request.getParameter("zregion");
			String zdept_id = request.getParameter("zdept_id");
			String zdept_brch_id = request.getParameter("zdept_brch_id");
			String zemp_id = request.getParameter("zemp_id");
			String zreq_ym_b = request.getParameter("zreq_ym_b");
			String zreq_ym_a = request.getParameter("zreq_ym_a");
			String passwd = request.getParameter("passwd");
			String active_bcg_id = request.getParameter("active_bcg_id");
			String brch_gb = request.getParameter("brch_gb");
			String order = request.getParameter("order");
			
			String session_zregion = (String) session.getAttribute("zregion");
			String session_zdept_id = (String) session.getAttribute("zdept_id");
			String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
			String session_zemp_id = (String) session.getAttribute("zemp_id");
			String session_zauth_grp = (String) session.getAttribute("zauth_grp");
			String session_zregion_gb = (String) session.getAttribute("zregion_gb");
			
			HashMap paramMap = new HashMap();
			paramMap.put("zregion", zregion);
			paramMap.put("zdept_id", zdept_id);
			paramMap.put("zdept_brch_id", zdept_brch_id);
			paramMap.put("zemp_id", zemp_id);
			paramMap.put("zreq_ym_b", zreq_ym_b);
			paramMap.put("zreq_ym_a", zreq_ym_a);
			paramMap.put("passwd", passwd);
			paramMap.put("active_bcg_id", active_bcg_id);
			paramMap.put("brch_gb", brch_gb);
			paramMap.put("order", order);
			paramMap.put("session_zregion", session_zregion);
			paramMap.put("session_zdept_id", session_zdept_id);
			paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
			paramMap.put("session_zemp_id", session_zemp_id);
			paramMap.put("session_zauth_grp", session_zauth_grp);
			paramMap.put("session_zregion_gb", session_zregion_gb);
			
			// 데이터 조회
			List selectReportDataList = exlMainMapper.excelMoniSum_ex_d(paramMap);
			
			ExcelUtil excelUtil = ExcelUtil.getInstance();
			excelUtil.setRequest(request);
			excelUtil.setResponse(response);
			excelUtil.setFilename("주요지표");
			excelUtil.setTitle("엑셀다운로드일시 : " + getDownloadDate());
			excelUtil.setTitleindex(1);
			
			SXSSFWorkbook workbook = excelUtil.getSxssfWorkbook("tplMoniSum_ex_d.xlsx");
			if(!active_bcg_id.equals("N")) {
				workbook = excelUtil.getSxssfWorkbook("tplMoniSum_cs_d.xlsx");
			}
			
			workbook.setCompressTempFiles(true);
			
			// 첫번째 시트 생성
			SXSSFSheet sheet = workbook.getSheetAt(0);
			
			// 템플릿을 제외한 마지막 ROWNUM을 읽는다(해당 ROW부터 쓰기위해서)
			int rowNo = 3;
			
			if(selectReportDataList.size() > 2) {
				// 상위 20프로
				XSSFCellStyle perStyle = (XSSFCellStyle) workbook.createCellStyle();
				Font perFont = perStyle.getFont();
				perFont.setFontHeight((short) 200);
				perFont.setBold(true);
				perStyle.setBorderRight(BorderStyle.THIN);
				perStyle.setBorderLeft(BorderStyle.THIN);
				perStyle.setBorderTop(BorderStyle.THIN);
				perStyle.setBorderBottom(BorderStyle.THIN);
				perStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				//perStyle.setAlignment(HorizontalAlignment.CENTER);
				perStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(218, 238, 243))); 
				perStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				
				sheet.addMergedRegionUnsafe(new CellRangeAddress(3, 3, 0, 7));
				
				int perIndex = 0;
				ExlMoniSumVO perVo = (ExlMoniSumVO) selectReportDataList.get(0);
				
				SXSSFRow perRow = sheet.createRow(rowNo++);
				SXSSFCell perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue("상위20%");
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue(perVo.getZitem1_p());
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue(perVo.getZitem2_p());
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue(perVo.getZitem3_p());
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue(perVo.getZitem4_p());
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue(perVo.getZitem5_p());
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue(perVo.getZitem6_p());
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue(perVo.getZitem7_p());
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue(perVo.getZadmin_rank1());
				
				/* 개선순위 2021.07.07
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue(perVo.getZadmin_rank2());
				*/
				
				// 평균
				XSSFCellStyle avgStyle = (XSSFCellStyle) workbook.createCellStyle();
				Font avgFont = avgStyle.getFont();
				avgFont.setFontHeight((short) 200);
				avgFont.setBold(true);
				avgStyle.setBorderRight(BorderStyle.THIN);
				avgStyle.setBorderLeft(BorderStyle.THIN);
				avgStyle.setBorderTop(BorderStyle.THIN);
				avgStyle.setBorderBottom(BorderStyle.THIN);
				avgStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				//avgStyle.setAlignment(HorizontalAlignment.CENTER);
				avgStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(218, 238, 243))); 
				avgStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				
				sheet.addMergedRegionUnsafe(new CellRangeAddress(4, 4, 0, 7));
				
				int avgIndex = 0;
				ExlMoniSumVO avgVo = (ExlMoniSumVO) selectReportDataList.get(1);
				
				SXSSFRow avgRow = sheet.createRow(rowNo++);
				SXSSFCell avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				avgCell.setCellValue("평균");
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				avgCell.setCellValue(avgVo.getZitem1_p());
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				avgCell.setCellValue(avgVo.getZitem2_p());
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				avgCell.setCellValue(avgVo.getZitem3_p());
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				avgCell.setCellValue(avgVo.getZitem4_p());
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				avgCell.setCellValue(avgVo.getZitem5_p());
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				avgCell.setCellValue(avgVo.getZitem6_p());
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				avgCell.setCellValue(avgVo.getZitem7_p());
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				avgCell.setCellValue(avgVo.getZadmin_rank1());
				
				/* 개선순위 2021.07.07
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				avgCell.setCellValue(avgVo.getZadmin_rank2());
				*/
			}
			
			// 셀스타일 생성(상하좌우 테두리)
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			Font font = style.getFont();
			font.setFontHeight((short) 200);
			font.setBold(false);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			// 셀에 데이터를 넣는 부분
			for (int i = 2; i < selectReportDataList.size(); i++) {
				// 1열 작성
				SXSSFRow row = sheet.createRow(rowNo++);
				
				ExlMoniSumVO vo = (ExlMoniSumVO) selectReportDataList.get(i);
				int index = 0;
				
				SXSSFCell cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZregion_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_emp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_brch_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_brch_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZemp_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZemp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZcls_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem1_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem2_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem3_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem4_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem5_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem6_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem7_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				if(brch_gb.equals("LC")) {
					if(vo.getZlevel().equals("3")) cell.setCellValue(vo.getZadmin_rank1());
					else cell.setCellValue("-");
				} else if(brch_gb.equals("BL")) {
					if(vo.getZlevel().equals("2")) cell.setCellValue(vo.getZadmin_rank1());
					else cell.setCellValue("-");
				} else {
					cell.setCellValue(vo.getZadmin_rank1());
				}
				/* 개선순위 2021.07.07
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				if(brch_gb.equals("LC")) {
					if(vo.getZlevel().equals("3")) cell.setCellValue(vo.getZadmin_rank2());
					else cell.setCellValue("-");
				} else if(brch_gb.equals("BL")) {
					if(vo.getZlevel().equals("2")) cell.setCellValue(vo.getZadmin_rank2());
					else cell.setCellValue("-");
				} else {
					cell.setCellValue(vo.getZadmin_rank2());
				}
				*/
			}
			
			insertExcelLog(session, selectReportDataList.size(), request.getServletPath());
			
			// 엑셀 다운
			excelUtil.runToExcel(workbook, passwd);
			
		} catch (IOException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );  
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );	
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
			
		} catch(Exception e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );			
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
		}
	}
	
	/*******************************************
	 * [ 현장용 ] 주요지표 엑셀다운 - 대상건수 *
	 *******************************************/
	@Transactional
	@Override
	public void excelMoniSum_d_cnt(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			HttpSession session = request.getSession();
			
			String zregion = request.getParameter("zregion");
			String zdept_id = request.getParameter("zdept_id");
			String zdept_brch_id = request.getParameter("zdept_brch_id");
			String zemp_id = request.getParameter("zemp_id");
			String zreq_ym_b = request.getParameter("zreq_ym_b");
			String zreq_ym_a = request.getParameter("zreq_ym_a");
			String passwd = request.getParameter("passwd");
			String active_bcg_id = request.getParameter("active_bcg_id");
			String brch_gb = request.getParameter("brch_gb");
			
			String session_zregion = (String) session.getAttribute("zregion");
			String session_zdept_id = (String) session.getAttribute("zdept_id");
			String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
			String session_zemp_id = (String) session.getAttribute("zemp_id");
			String session_zauth_grp = (String) session.getAttribute("zauth_grp");
			String session_zregion_gb = (String) session.getAttribute("zregion_gb");
			
			HashMap paramMap = new HashMap();
			paramMap.put("zregion", zregion);
			paramMap.put("zdept_id", zdept_id);
			paramMap.put("zdept_brch_id", zdept_brch_id);
			paramMap.put("zemp_id", zemp_id);
			paramMap.put("zreq_ym_b", zreq_ym_b);
			paramMap.put("zreq_ym_a", zreq_ym_a);
			paramMap.put("passwd", passwd);
			paramMap.put("active_bcg_id", active_bcg_id);
			paramMap.put("brch_gb", brch_gb);
			paramMap.put("session_zregion", session_zregion);
			paramMap.put("session_zdept_id", session_zdept_id);
			paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
			paramMap.put("session_zemp_id", session_zemp_id);
			paramMap.put("session_zauth_grp", session_zauth_grp);
			paramMap.put("session_zregion_gb", session_zregion_gb);
			
			// 데이터 조회
			List selectReportDataList = exlMainMapper.excelMoniSum_d_cnt(paramMap);
			
			ExcelUtil excelUtil = ExcelUtil.getInstance();
			excelUtil.setRequest(request);
			excelUtil.setResponse(response);
			excelUtil.setFilename("주요지표");
			excelUtil.setTitle("엑셀다운로드일시 : " + getDownloadDate());
			excelUtil.setTitleindex(1);
			
			SXSSFWorkbook workbook = excelUtil.getSxssfWorkbook("tplMoniSum_d.xlsx");
			if(!active_bcg_id.equals("N")) {
				workbook = excelUtil.getSxssfWorkbook("tplMoniSum_cs_d.xlsx");
			}
			
			workbook.setCompressTempFiles(true);
			
			// 첫번째 시트 생성
			SXSSFSheet sheet = workbook.getSheetAt(0);
			
			// 템플릿을 제외한 마지막 ROWNUM을 읽는다(해당 ROW부터 쓰기위해서)
			int rowNo = 3;
			
			// 셀스타일 생성(상하좌우 테두리)
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			Font font = style.getFont();
			font.setBold(false);
			font.setFontHeight((short) 200);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			// 셀에 데이터를 넣는 부분
			for (int i = 1; i < selectReportDataList.size(); i++) {
				// 1열 작성
				SXSSFRow row = sheet.createRow(rowNo++);
				
				ExlMoniSumVO vo = (ExlMoniSumVO) selectReportDataList.get(i);
				int index = 0;
				
				SXSSFCell cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZregion_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_emp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_brch_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_brch_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZemp_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZemp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZcls_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem1_s());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem2_s());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem3_s());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem4_s());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem5_s());
				
				if(active_bcg_id.equals("N")) {
					cell = row.createCell(index++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getZitem6_s());
					
					cell = row.createCell(index++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getZitem7_s());
				}
				
			}
			
			insertExcelLog(session, selectReportDataList.size(), request.getServletPath());
			
			// 엑셀 다운
			excelUtil.runToExcel(workbook, passwd);
			
		} catch (IOException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );  
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );	
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
			
		} catch(Exception e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );			
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
		}
	}

	/*******************************************
	 * 주요지표 LC, BL용 엑셀다운 - 대상건수   *
	 *******************************************/
	@Override
	public void excelMoniSum_ex_d_cnt(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			HttpSession session = request.getSession();
			
			String zregion = request.getParameter("zregion");
			String zdept_id = request.getParameter("zdept_id");
			String zdept_brch_id = request.getParameter("zdept_brch_id");
			String zemp_id = request.getParameter("zemp_id");
			String zreq_ym_b = request.getParameter("zreq_ym_b");
			String zreq_ym_a = request.getParameter("zreq_ym_a");
			String passwd = request.getParameter("passwd");
			String active_bcg_id = request.getParameter("active_bcg_id");
			String brch_gb = request.getParameter("brch_gb");
			String order = request.getParameter("order");
			
			String session_zregion = (String) session.getAttribute("zregion");
			String session_zdept_id = (String) session.getAttribute("zdept_id");
			String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
			String session_zemp_id = (String) session.getAttribute("zemp_id");
			String session_zauth_grp = (String) session.getAttribute("zauth_grp");
			String session_zregion_gb = (String) session.getAttribute("zregion_gb");
			
			HashMap paramMap = new HashMap();
			paramMap.put("zregion", zregion);
			paramMap.put("zdept_id", zdept_id);
			paramMap.put("zdept_brch_id", zdept_brch_id);
			paramMap.put("zemp_id", zemp_id);
			paramMap.put("zreq_ym_b", zreq_ym_b);
			paramMap.put("zreq_ym_a", zreq_ym_a);
			paramMap.put("passwd", passwd);
			paramMap.put("active_bcg_id", active_bcg_id);
			paramMap.put("brch_gb", brch_gb);
			paramMap.put("order", order);
			paramMap.put("session_zregion", session_zregion);
			paramMap.put("session_zdept_id", session_zdept_id);
			paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
			paramMap.put("session_zemp_id", session_zemp_id);
			paramMap.put("session_zauth_grp", session_zauth_grp);
			paramMap.put("session_zregion_gb", session_zregion_gb);
			
			// 데이터 조회
			List selectReportDataList = exlMainMapper.excelMoniSum_ex_d_cnt(paramMap);
			
			ExcelUtil excelUtil = ExcelUtil.getInstance();
			excelUtil.setRequest(request);
			excelUtil.setResponse(response);
			excelUtil.setFilename("주요지표");
			excelUtil.setTitle("엑셀다운로드일시 : " + getDownloadDate());
			excelUtil.setTitleindex(1);
			
			SXSSFWorkbook workbook = excelUtil.getSxssfWorkbook("tplMoniSum_ex_d.xlsx");
			if(!active_bcg_id.equals("N")) {
				workbook = excelUtil.getSxssfWorkbook("tplMoniSum_cs_d.xlsx");
			}
			
			workbook.setCompressTempFiles(true);
			
			// 첫번째 시트 생성
			SXSSFSheet sheet = workbook.getSheetAt(0);
			
			// 템플릿을 제외한 마지막 ROWNUM을 읽는다(해당 ROW부터 쓰기위해서)
			int rowNo = 3;
			
			if(selectReportDataList.size() > 2) {
				// 상위 20프로
				XSSFCellStyle perStyle = (XSSFCellStyle) workbook.createCellStyle();
				Font perFont = perStyle.getFont();
				perFont.setFontHeight((short) 200);
				perFont.setBold(true);
				perStyle.setBorderRight(BorderStyle.THIN);
				perStyle.setBorderLeft(BorderStyle.THIN);
				perStyle.setBorderTop(BorderStyle.THIN);
				perStyle.setBorderBottom(BorderStyle.THIN);
				perStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				//perStyle.setAlignment(HorizontalAlignment.CENTER);
				perStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(218, 238, 243))); 
				perStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				
				sheet.addMergedRegionUnsafe(new CellRangeAddress(3, 3, 0, 7));
				
				int perIndex = 0;
				ExlMoniSumVO perVo = (ExlMoniSumVO) selectReportDataList.get(0);
				
				SXSSFRow perRow = sheet.createRow(rowNo++);
				SXSSFCell perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue("상위20%");
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue(perVo.getZitem1_s());
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue(perVo.getZitem2_s());
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue(perVo.getZitem3_s());
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue(perVo.getZitem4_s());
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue(perVo.getZitem5_s());
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue(perVo.getZitem6_s());
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue(perVo.getZitem7_s());
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue(perVo.getZadmin_rank1());
				
				/* 개선순위 2021.07.07
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue(perVo.getZadmin_rank2());
				*/
			}
			
			// 셀스타일 생성(상하좌우 테두리)
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			Font font = style.getFont();
			font.setFontHeight((short) 200);
			font.setBold(false);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			// 셀에 데이터를 넣는 부분
			for (int i = 2; i < selectReportDataList.size(); i++) {
				// 1열 작성
				SXSSFRow row = sheet.createRow(rowNo++);
				
				ExlMoniSumVO vo = (ExlMoniSumVO) selectReportDataList.get(i);
				int index = 0;
				
				SXSSFCell cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZregion_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_emp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_brch_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_brch_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZemp_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZemp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZcls_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem1_s());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem2_s());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem3_s());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem4_s());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem5_s());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem6_s());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem7_s());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				if(brch_gb.equals("LC")) {
					if(vo.getZlevel().equals("3")) cell.setCellValue(vo.getZadmin_rank1());
					else cell.setCellValue("-");
				} else if(brch_gb.equals("BL")) {
					if(vo.getZlevel().equals("2")) cell.setCellValue(vo.getZadmin_rank1());
					else cell.setCellValue("-");
				} else {
					cell.setCellValue(vo.getZadmin_rank1());
				}
				/* 개선순위 2021.07.07
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				if(brch_gb.equals("LC")) {
					if(vo.getZlevel().equals("3")) cell.setCellValue(vo.getZadmin_rank2());
					else cell.setCellValue("-");
				} else if(brch_gb.equals("BL")) {
					if(vo.getZlevel().equals("2")) cell.setCellValue(vo.getZadmin_rank2());
					else cell.setCellValue("-");
				} else {
					cell.setCellValue(vo.getZadmin_rank2());
				}
				*/
				
			}
			
			insertExcelLog(session, selectReportDataList.size(), request.getServletPath());
			
			// 엑셀 다운
			excelUtil.runToExcel(workbook, passwd);
			
		} catch (IOException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );  
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );	
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
			
		} catch(Exception e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );			
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
		}
	}

	/*******************************************
	 * [ 현장용 ] 카드결제 현황 엑셀다운 	   *
	 *******************************************/
	@Override
	public void excelCardPayment_d(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			HttpSession session = request.getSession();
			
			String zregion = request.getParameter("zregion");
			String zdept_id = request.getParameter("zdept_id");
			String zdept_brch_id = request.getParameter("zdept_brch_id");
			String zemp_id = request.getParameter("zemp_id");
			String zreq_ym_b = request.getParameter("zreq_ym_b");
			String zreq_ym_a = request.getParameter("zreq_ym_a");
			String passwd = request.getParameter("passwd");
			String active_bcg_id = request.getParameter("active_bcg_id");
			
			String session_zregion = (String) session.getAttribute("zregion");
			String session_zdept_id = (String) session.getAttribute("zdept_id");
			String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
			String session_zemp_id = (String) session.getAttribute("zemp_id");
			String session_zauth_grp = (String) session.getAttribute("zauth_grp");
			String session_zregion_gb = (String) session.getAttribute("zregion_gb");
			
			HashMap paramMap = new HashMap();
			paramMap.put("zregion", zregion);
			paramMap.put("zdept_id", zdept_id);
			paramMap.put("zdept_brch_id", zdept_brch_id);
			paramMap.put("zemp_id", zemp_id);
			paramMap.put("zreq_ym_b", zreq_ym_b);
			paramMap.put("zreq_ym_a", zreq_ym_a);
			paramMap.put("passwd", passwd);
			paramMap.put("active_bcg_id", active_bcg_id);
			paramMap.put("session_zregion", session_zregion);
			paramMap.put("session_zdept_id", session_zdept_id);
			paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
			paramMap.put("session_zemp_id", session_zemp_id);
			paramMap.put("session_zauth_grp", session_zauth_grp);
			paramMap.put("session_zregion_gb", session_zregion_gb);
			
			// 데이터 조회
			List selectReportDataList = exlMainMapper.excelCardPayment_d(paramMap);
			
			ExcelUtil excelUtil = ExcelUtil.getInstance();
			excelUtil.setRequest(request);
			excelUtil.setResponse(response);
			excelUtil.setFilename("[현장용]카드결제현황_리스트");
			excelUtil.setTitle("엑셀다운로드일시 : " + getDownloadDate());
			excelUtil.setTitleindex(2);
			
			SXSSFWorkbook workbook = excelUtil.getSxssfWorkbook("tplCardPayment_d.xlsx");
			workbook.setCompressTempFiles(true);
			
			// 첫번째 시트 생성
			SXSSFSheet sheet = workbook.getSheetAt(0);
			
			// 템플릿을 제외한 마지막 ROWNUM을 읽는다(해당 ROW부터 쓰기위해서)
			int rowNo = 4;
			
			// 셀스타일 생성(상하좌우 테두리)
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			Font font = style.getFont();
			font.setFontHeight((short) 200);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			// 셀에 데이터를 넣는 부분
			for (int i = 0; i < selectReportDataList.size(); i++) {
				// 1열 작성
				SXSSFRow row = sheet.createRow(rowNo++);
				
				int index = 0;
				SXSSFCell cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZrms_ym());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZregion_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZdept_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZdept_brch_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZcard_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZcard_no());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZemp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZemp_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZauth_grp());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZmbr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getKunnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZgrade_cde());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getMatnr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getMatnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZapprv_dt());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZapprv_no());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZcard_amt());
			}
			
			insertExcelLog(session, selectReportDataList.size(), request.getServletPath());
			
			// 엑셀 다운
			excelUtil.runToExcel(workbook, passwd);
			
		} catch (IOException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );  
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );	
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
			
		} catch(Exception e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );			
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
		}
	}
	
	/*******************************************
	 * [ 관리용 ] 카드결제 현황 엑셀다운   	   *
	 *******************************************/
	@Override
	public void excelCardPayment_da(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			HttpSession session = request.getSession();
			
			String zregion = request.getParameter("zregion");
			String zdept_id = request.getParameter("zdept_id");
			String zdept_brch_id = request.getParameter("zdept_brch_id");
			String zemp_id = request.getParameter("zemp_id");
			String zreq_ym_b = request.getParameter("zreq_ym_b");
			String zreq_ym_a = request.getParameter("zreq_ym_a");
			String passwd = request.getParameter("passwd");
			String active_bcg_id = request.getParameter("active_bcg_id");
			
			String session_zregion = (String) session.getAttribute("zregion");
			String session_zdept_id = (String) session.getAttribute("zdept_id");
			String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
			String session_zemp_id = (String) session.getAttribute("zemp_id");
			String session_zauth_grp = (String) session.getAttribute("zauth_grp");
			String session_zregion_gb = (String) session.getAttribute("zregion_gb");
			
			HashMap paramMap = new HashMap();
			//paramMap.put("zregion", zregion);
			//paramMap.put("zdept_id", zdept_id);
			//paramMap.put("zdept_brch_id", zdept_brch_id);
			//paramMap.put("zemp_id", zemp_id);
			paramMap.put("zreq_ym_b", zreq_ym_b);
			paramMap.put("zreq_ym_a", zreq_ym_a);
			paramMap.put("passwd", passwd);
			paramMap.put("active_bcg_id", active_bcg_id);
			paramMap.put("session_zregion", session_zregion);
			paramMap.put("session_zdept_id", session_zdept_id);
			paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
			paramMap.put("session_zemp_id", session_zemp_id);
			paramMap.put("session_zauth_grp", session_zauth_grp);
			paramMap.put("session_zregion_gb", session_zregion_gb);
			
			// 데이터 조회
			List selectReportDataList = exlMainMapper.excelCardPayment_da(paramMap);
			
			ExcelUtil excelUtil = ExcelUtil.getInstance();
			excelUtil.setRequest(request);
			excelUtil.setResponse(response);
			excelUtil.setFilename("[관리용]카드결제현황_리스트");
			excelUtil.setTitle("엑셀다운로드일시 : " + getDownloadDate());
			excelUtil.setTitleindex(2);
			
			SXSSFWorkbook workbook = excelUtil.getSxssfWorkbook("tplCardPayment_da.xlsx");
			workbook.setCompressTempFiles(true);
			
			// 첫번째 시트 생성
			SXSSFSheet sheet = workbook.getSheetAt(0);
			
			// 템플릿을 제외한 마지막 ROWNUM을 읽는다(해당 ROW부터 쓰기위해서)
			int rowNo = 4;
			
			// 셀스타일 생성(상하좌우 테두리)
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			Font font = style.getFont();
			font.setFontHeight((short) 200);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			// 셀에 데이터를 넣는 부분
			for (int i = 0; i < selectReportDataList.size(); i++) {
				// 1열 작성
				SXSSFRow row = sheet.createRow(rowNo++);
				
				int index = 0;
				
				SXSSFCell cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZrms_ym());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZregion_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZdept_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZdept_brch_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZcard_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZcard_no());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZemp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZemp_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZauth_grp());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZmbr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getKunnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZgrade_cde());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getMatnr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getMatnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZapprv_dt());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZapprv_no());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZcard_amt());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlCardPaymentVO) selectReportDataList.get(i)).getZadmin_etc());
			}
			
			insertExcelLog(session, selectReportDataList.size(), request.getServletPath());
			
			// 엑셀 다운
			excelUtil.runToExcel(workbook, passwd);
			
		} catch (IOException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );  
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );	
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
			
		} catch(Exception e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );			
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
		}
	}

	/*******************************************
	 * [ 현장용 ] 이체제외 후 학습 엑셀다운	   *
	 *******************************************/
	@Override
	public void excelTranStudy_d(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			HttpSession session = request.getSession();
			
			String zregion = request.getParameter("zregion");
			String zdept_id = request.getParameter("zdept_id");
			String zdept_brch_id = request.getParameter("zdept_brch_id");
			String zemp_id = request.getParameter("zemp_id");
			String zreq_ym_b = request.getParameter("zreq_ym_b");
			String zreq_ym_a = request.getParameter("zreq_ym_a");
			String passwd = request.getParameter("passwd");
			String active_bcg_id = request.getParameter("active_bcg_id");
			
			String session_zregion = (String) session.getAttribute("zregion");
			String session_zdept_id = (String) session.getAttribute("zdept_id");
			String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
			String session_zemp_id = (String) session.getAttribute("zemp_id");
			String session_zauth_grp = (String) session.getAttribute("zauth_grp");
			String session_zregion_gb = (String) session.getAttribute("zregion_gb");
			
			HashMap paramMap = new HashMap();
			paramMap.put("zregion", zregion);
			paramMap.put("zdept_id", zdept_id);
			paramMap.put("zdept_brch_id", zdept_brch_id);
			paramMap.put("zemp_id", zemp_id);
			paramMap.put("zreq_ym_b", zreq_ym_b);
			paramMap.put("zreq_ym_a", zreq_ym_a);
			paramMap.put("passwd", passwd);
			paramMap.put("active_bcg_id", active_bcg_id);
			paramMap.put("session_zregion", session_zregion);
			paramMap.put("session_zdept_id", session_zdept_id);
			paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
			paramMap.put("session_zemp_id", session_zemp_id);
			paramMap.put("session_zauth_grp", session_zauth_grp);
			paramMap.put("session_zregion_gb", session_zregion_gb);
			
			// 데이터 조회
			List selectReportDataList = exlMainMapper.excelTranStudy_d(paramMap);
			
			ExcelUtil excelUtil = ExcelUtil.getInstance();
			excelUtil.setRequest(request);
			excelUtil.setResponse(response);
			excelUtil.setFilename("[현장용]이체제외_후_학습_리스트");
			excelUtil.setTitle("엑셀다운로드일시 : " + getDownloadDate());
			excelUtil.setTitleindex(2);
			
			SXSSFWorkbook workbook = excelUtil.getSxssfWorkbook("tplTranStudy_d.xlsx");
			workbook.setCompressTempFiles(true);
			
			// 첫번째 시트 생성
			SXSSFSheet sheet = workbook.getSheetAt(0);
			
			// 템플릿을 제외한 마지막 ROWNUM을 읽는다(해당 ROW부터 쓰기위해서)
			int rowNo = 4;
			
			// 셀스타일 생성(상하좌우 테두리)
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			Font font = style.getFont();
			font.setFontHeight((short) 200);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			// 셀에 데이터를 넣는 부분
			for (int i = 0; i < selectReportDataList.size(); i++) {
				// 1열 작성
				SXSSFRow row = sheet.createRow(rowNo++);
				int index = 0;
				
				SXSSFCell cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlTranStudyVO) selectReportDataList.get(i)).getZrms_ym());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlTranStudyVO) selectReportDataList.get(i)).getZregion_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlTranStudyVO) selectReportDataList.get(i)).getZdept_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlTranStudyVO) selectReportDataList.get(i)).getZdept_brch_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlTranStudyVO) selectReportDataList.get(i)).getZemp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlTranStudyVO) selectReportDataList.get(i)).getZemp_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlTranStudyVO) selectReportDataList.get(i)).getZauth_grp());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlTranStudyVO) selectReportDataList.get(i)).getZmbr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlTranStudyVO) selectReportDataList.get(i)).getKunnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlTranStudyVO) selectReportDataList.get(i)).getZgrade_cde());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlTranStudyVO) selectReportDataList.get(i)).getMatnr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlTranStudyVO) selectReportDataList.get(i)).getMatnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlTranStudyVO) selectReportDataList.get(i)).getZexc_month());
			}
			
			insertExcelLog(session, selectReportDataList.size(), request.getServletPath());
			
			// 엑셀 다운
			excelUtil.runToExcel(workbook, passwd);
			
		} catch (IOException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );  
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );	
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
			
		} catch(Exception e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );			
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
		}
	}

	/*******************************************
	 * [ 관리용 ] 이체제외 후 학습 엑셀다운	   *
	 *******************************************/
	@Override
	public void excelTranStudy_da(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			HttpSession session = request.getSession();
			
			String zregion = request.getParameter("zregion");
			String zdept_id = request.getParameter("zdept_id");
			String zdept_brch_id = request.getParameter("zdept_brch_id");
			String zemp_id = request.getParameter("zemp_id");
			String zreq_ym_b = request.getParameter("zreq_ym_b");
			String zreq_ym_a = request.getParameter("zreq_ym_a");
			String passwd = request.getParameter("passwd");
			String active_bcg_id = request.getParameter("active_bcg_id");
			
			String session_zregion = (String) session.getAttribute("zregion");
			String session_zdept_id = (String) session.getAttribute("zdept_id");
			String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
			String session_zemp_id = (String) session.getAttribute("zemp_id");
			String session_zauth_grp = (String) session.getAttribute("zauth_grp");
			String session_zregion_gb = (String) session.getAttribute("zregion_gb");
			
			HashMap paramMap = new HashMap();
			//paramMap.put("zregion", zregion);
			//paramMap.put("zdept_id", zdept_id);
			//paramMap.put("zdept_brch_id", zdept_brch_id);
			//paramMap.put("zemp_id", zemp_id);
			paramMap.put("zreq_ym_b", zreq_ym_b);
			paramMap.put("zreq_ym_a", zreq_ym_a);
			paramMap.put("passwd", passwd);
			paramMap.put("active_bcg_id", active_bcg_id);
			paramMap.put("session_zregion", session_zregion);
			paramMap.put("session_zdept_id", session_zdept_id);
			paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
			paramMap.put("session_zemp_id", session_zemp_id);
			paramMap.put("session_zauth_grp", session_zauth_grp);
			paramMap.put("session_zregion_gb", session_zregion_gb);
			
			// 데이터 조회
			List selectReportDataList = exlMainMapper.excelTranStudy_da(paramMap);
			
			ExcelUtil excelUtil = ExcelUtil.getInstance();
			excelUtil.setRequest(request);
			excelUtil.setResponse(response);
			excelUtil.setFilename("[관리용]이체제외_후_학습_리스트");
			excelUtil.setTitle("엑셀다운로드일시 : " + getDownloadDate());
			excelUtil.setTitleindex(2);
			
			SXSSFWorkbook workbook = excelUtil.getSxssfWorkbook("tplTranStudy_da.xlsx");
			workbook.setCompressTempFiles(true);
			
			// 첫번째 시트 생성
			SXSSFSheet sheet = workbook.getSheetAt(0);
			
			// 템플릿을 제외한 마지막 ROWNUM을 읽는다(해당 ROW부터 쓰기위해서)
			int rowNo = 4;
			
			// 셀스타일 생성(상하좌우 테두리)
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			Font font = style.getFont();
			font.setFontHeight((short) 200);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			// 셀에 데이터를 넣는 부분
			for (int i = 0; i < selectReportDataList.size(); i++) {
				// 1열 작성
				SXSSFRow row = sheet.createRow(rowNo++);
				
				int index = 0;
				
				SXSSFCell cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlTranStudyVO) selectReportDataList.get(i)).getZrms_ym());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlTranStudyVO) selectReportDataList.get(i)).getZregion_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlTranStudyVO) selectReportDataList.get(i)).getZdept_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlTranStudyVO) selectReportDataList.get(i)).getZdept_brch_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlTranStudyVO) selectReportDataList.get(i)).getZemp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlTranStudyVO) selectReportDataList.get(i)).getZemp_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlTranStudyVO) selectReportDataList.get(i)).getZauth_grp());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlTranStudyVO) selectReportDataList.get(i)).getZmbr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlTranStudyVO) selectReportDataList.get(i)).getKunnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlTranStudyVO) selectReportDataList.get(i)).getZgrade_cde());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlTranStudyVO) selectReportDataList.get(i)).getMatnr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlTranStudyVO) selectReportDataList.get(i)).getMatnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlTranStudyVO) selectReportDataList.get(i)).getZexc_month());
			}
			
			insertExcelLog(session, selectReportDataList.size(), request.getServletPath());
			
			// 엑셀 다운
			excelUtil.runToExcel(workbook, passwd);
			
		} catch (IOException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );  
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );	
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
			
		} catch(Exception e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );			
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
		}
	}

	/*******************************************
	 * [ 현장용 ] 개인정보 인증 엑셀다운  	   *
	 *******************************************/
	@Override
	public void excelIndAuth_d(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			HttpSession session = request.getSession();
			
			String zregion = request.getParameter("zregion");
			String zdept_id = request.getParameter("zdept_id");
			String zdept_brch_id = request.getParameter("zdept_brch_id");
			String zemp_id = request.getParameter("zemp_id");
			String zreq_ym_b = request.getParameter("zreq_ym_b");
			String zreq_ym_a = request.getParameter("zreq_ym_a");
			String passwd = request.getParameter("passwd");
			String active_bcg_id = request.getParameter("active_bcg_id");
			
			String session_zregion = (String) session.getAttribute("zregion");
			String session_zdept_id = (String) session.getAttribute("zdept_id");
			String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
			String session_zemp_id = (String) session.getAttribute("zemp_id");
			String session_zauth_grp = (String) session.getAttribute("zauth_grp");
			String session_zregion_gb = (String) session.getAttribute("zregion_gb");
			
			HashMap paramMap = new HashMap();
			paramMap.put("zregion", zregion);
			paramMap.put("zdept_id", zdept_id);
			paramMap.put("zdept_brch_id", zdept_brch_id);
			paramMap.put("zemp_id", zemp_id);
			paramMap.put("zreq_ym_b", zreq_ym_b);
			paramMap.put("zreq_ym_a", zreq_ym_a);
			paramMap.put("passwd", passwd);
			paramMap.put("active_bcg_id", active_bcg_id);
			paramMap.put("session_zregion", session_zregion);
			paramMap.put("session_zdept_id", session_zdept_id);
			paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
			paramMap.put("session_zemp_id", session_zemp_id);
			paramMap.put("session_zauth_grp", session_zauth_grp);
			paramMap.put("session_zregion_gb", session_zregion_gb);
			
			// 데이터 조회
			List selectReportDataList = exlMainMapper.excelIndAuth_d(paramMap);
			
			ExcelUtil excelUtil = ExcelUtil.getInstance();
			excelUtil.setRequest(request);
			excelUtil.setResponse(response);
			excelUtil.setFilename("[현장용]개인정보_인증_리스트");
			excelUtil.setTitle("엑셀다운로드일시 : " + getDownloadDate());
			excelUtil.setTitleindex(2);
			
			SXSSFWorkbook workbook = excelUtil.getSxssfWorkbook("tplIndAuth_d.xlsx");
			workbook.setCompressTempFiles(true);
			
			// 첫번째 시트 생성
			SXSSFSheet sheet = workbook.getSheetAt(0);
			
			// 템플릿을 제외한 마지막 ROWNUM을 읽는다(해당 ROW부터 쓰기위해서)
			int rowNo = 4;
			
			// 셀스타일 생성(상하좌우 테두리)
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			Font font = style.getFont();
			font.setFontHeight((short) 200);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			// 셀에 데이터를 넣는 부분
			for (int i = 0; i < selectReportDataList.size(); i++) {
				// 1열 작성
				SXSSFRow row = sheet.createRow(rowNo++);
				
				int index = 0;
				
				SXSSFCell cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlIndAuthVO) selectReportDataList.get(i)).getZrms_ym());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlIndAuthVO) selectReportDataList.get(i)).getZregion_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlIndAuthVO) selectReportDataList.get(i)).getZdept_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlIndAuthVO) selectReportDataList.get(i)).getZdept_brch_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlIndAuthVO) selectReportDataList.get(i)).getZemp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlIndAuthVO) selectReportDataList.get(i)).getZemp_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlIndAuthVO) selectReportDataList.get(i)).getZauth_grp());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlIndAuthVO) selectReportDataList.get(i)).getZsms_send_dt());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlIndAuthVO) selectReportDataList.get(i)).getZmbr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlIndAuthVO) selectReportDataList.get(i)).getKunnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlIndAuthVO) selectReportDataList.get(i)).getZcert_gb());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlIndAuthVO) selectReportDataList.get(i)).getZrec_conn());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlIndAuthVO) selectReportDataList.get(i)).getZcert_name());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlIndAuthVO) selectReportDataList.get(i)).getZcert_tel());
			}
			
			insertExcelLog(session, selectReportDataList.size(), request.getServletPath());
			
			// 엑셀 다운
			excelUtil.runToExcel(workbook, passwd);
			
		} catch (IOException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );  
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );	
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
			
		} catch(Exception e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );			
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
		}
		
	}

	/*******************************************
	 * [ 관리용 ] 개인정보 인증 엑셀다운  	   *
	 *******************************************/
	@Override
	public void excelIndAuth_da(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			HttpSession session = request.getSession();
			
			String zregion = request.getParameter("zregion");
			String zdept_id = request.getParameter("zdept_id");
			String zdept_brch_id = request.getParameter("zdept_brch_id");
			String zemp_id = request.getParameter("zemp_id");
			String zreq_ym_b = request.getParameter("zreq_ym_b");
			String zreq_ym_a = request.getParameter("zreq_ym_a");
			String passwd = request.getParameter("passwd");
			String active_bcg_id = request.getParameter("active_bcg_id");
			
			String session_zregion = (String) session.getAttribute("zregion");
			String session_zdept_id = (String) session.getAttribute("zdept_id");
			String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
			String session_zemp_id = (String) session.getAttribute("zemp_id");
			String session_zauth_grp = (String) session.getAttribute("zauth_grp");
			String session_zregion_gb = (String) session.getAttribute("zregion_gb");
			
			HashMap paramMap = new HashMap();
			//paramMap.put("zregion", zregion);
			//paramMap.put("zdept_id", zdept_id);
			//paramMap.put("zdept_brch_id", zdept_brch_id);
			//paramMap.put("zemp_id", zemp_id);
			paramMap.put("zreq_ym_b", zreq_ym_b);
			paramMap.put("zreq_ym_a", zreq_ym_a);
			paramMap.put("passwd", passwd);
			paramMap.put("active_bcg_id", active_bcg_id);
			paramMap.put("session_zregion", session_zregion);
			paramMap.put("session_zdept_id", session_zdept_id);
			paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
			paramMap.put("session_zemp_id", session_zemp_id);
			paramMap.put("session_zauth_grp", session_zauth_grp);
			paramMap.put("session_zregion_gb", session_zregion_gb);
			
			// 데이터 조회
			List selectReportDataList = exlMainMapper.excelIndAuth_da(paramMap);
			
			ExcelUtil excelUtil = ExcelUtil.getInstance();
			excelUtil.setRequest(request);
			excelUtil.setResponse(response);
			excelUtil.setFilename("[관리용]개인정보_인증_리스트");
			excelUtil.setTitle("엑셀다운로드일시 : " + getDownloadDate());
			excelUtil.setTitleindex(2);
			
			SXSSFWorkbook workbook = excelUtil.getSxssfWorkbook("tplIndAuth_da.xlsx");
			workbook.setCompressTempFiles(true);
			
			// 첫번째 시트 생성
			SXSSFSheet sheet = workbook.getSheetAt(0);
			
			// 템플릿을 제외한 마지막 ROWNUM을 읽는다(해당 ROW부터 쓰기위해서)
			int rowNo = 4;
			
			// 셀스타일 생성(상하좌우 테두리)
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			Font font = style.getFont();
			font.setFontHeight((short) 200);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			// 셀에 데이터를 넣는 부분
			for (int i = 0; i < selectReportDataList.size(); i++) {
				// 1열 작성
				SXSSFRow row = sheet.createRow(rowNo++);
				
				int index = 0;
				ExlIndAuthVO vo = (ExlIndAuthVO) selectReportDataList.get(i);
				
				SXSSFCell cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZrms_ym());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZregion_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_brch_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZemp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZemp_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZauth_grp());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZsms_send_dt());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZmbr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getKunnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZbirth_day());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZtel());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZaddr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZcert_gb());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZrec_conn());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZcert_name());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZcert_tel());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZadmin_etc());
			}
			
			insertExcelLog(session, selectReportDataList.size(), request.getServletPath());
			
			// 엑셀 다운
			excelUtil.runToExcel(workbook, passwd);
			
		} catch (IOException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );  
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );	
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
			
		} catch(Exception e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );			
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
		}
		
	}

	/*******************************************
	 * [ 현장용 ] 2개월 체납 엑셀다운 		   *
	 *******************************************/
	@Override
	public void excelArrPayment_d(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			HttpSession session = request.getSession();
			
			String zregion = request.getParameter("zregion");
			String zdept_id = request.getParameter("zdept_id");
			String zdept_brch_id = request.getParameter("zdept_brch_id");
			String zemp_id = request.getParameter("zemp_id");
			String zreq_ym_b = request.getParameter("zreq_ym_b");
			String zreq_ym_a = request.getParameter("zreq_ym_a");
			String passwd = request.getParameter("passwd");
			String active_bcg_id = request.getParameter("active_bcg_id");
			
			String session_zregion = (String) session.getAttribute("zregion");
			String session_zdept_id = (String) session.getAttribute("zdept_id");
			String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
			String session_zemp_id = (String) session.getAttribute("zemp_id");
			String session_zauth_grp = (String) session.getAttribute("zauth_grp");
			String session_zregion_gb = (String) session.getAttribute("zregion_gb");
			
			HashMap paramMap = new HashMap();
			paramMap.put("zregion", zregion);
			paramMap.put("zdept_id", zdept_id);
			paramMap.put("zdept_brch_id", zdept_brch_id);
			paramMap.put("zemp_id", zemp_id);
			paramMap.put("zreq_ym_b", zreq_ym_b);
			paramMap.put("zreq_ym_a", zreq_ym_a);
			paramMap.put("passwd", passwd);
			paramMap.put("active_bcg_id", active_bcg_id);
			paramMap.put("session_zregion", session_zregion);
			paramMap.put("session_zdept_id", session_zdept_id);
			paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
			paramMap.put("session_zemp_id", session_zemp_id);
			paramMap.put("session_zauth_grp", session_zauth_grp);
			paramMap.put("session_zregion_gb", session_zregion_gb);
			
			// 데이터 조회
			List selectReportDataList = exlMainMapper.excelArrPayment_d(paramMap);
			
			ExcelUtil excelUtil = ExcelUtil.getInstance();
			excelUtil.setRequest(request);
			excelUtil.setResponse(response);
			excelUtil.setFilename("[현장용]2개월체납_리스트");
			excelUtil.setTitle("엑셀다운로드일시 : " + getDownloadDate());
			excelUtil.setTitleindex(2);
			
			SXSSFWorkbook workbook = excelUtil.getSxssfWorkbook("tplArrPayment_d.xlsx");
			workbook.setCompressTempFiles(true);
			
			// 첫번째 시트 생성
			SXSSFSheet sheet = workbook.getSheetAt(0);
			
			// 템플릿을 제외한 마지막 ROWNUM을 읽는다(해당 ROW부터 쓰기위해서)
			int rowNo = 4;
			
			// 셀스타일 생성(상하좌우 테두리)
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			Font font = style.getFont();
			font.setFontHeight((short) 200);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			// 셀에 데이터를 넣는 부분
			for (int i = 0; i < selectReportDataList.size(); i++) {
				// 1열 작성
				SXSSFRow row = sheet.createRow(rowNo++);
				
				int index = 0;
				
				SXSSFCell cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getZrms_ym());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getZregion_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getZdept_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getZdept_brch_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getZemp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getZemp_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getZauth_grp());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getZmbr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getKunnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getZgrade_cde());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getMatnr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getMatnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getZrcpt_amt());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getZlastfee_ym());
			}
			
			insertExcelLog(session, selectReportDataList.size(), request.getServletPath());
			
			// 엑셀 다운
			excelUtil.runToExcel(workbook, passwd);
			
		} catch (IOException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );  
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );	
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
			
		} catch(Exception e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );			
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
		}
	}

	/*******************************************
	 * [ 관리용 ] 2개월 체납 엑셀다운 		   *
	 *******************************************/
	@Override
	public void excelArrPayment_da(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			HttpSession session = request.getSession();
			
			String zregion = request.getParameter("zregion");
			String zdept_id = request.getParameter("zdept_id");
			String zdept_brch_id = request.getParameter("zdept_brch_id");
			String zemp_id = request.getParameter("zemp_id");
			String zreq_ym_b = request.getParameter("zreq_ym_b");
			String zreq_ym_a = request.getParameter("zreq_ym_a");
			String passwd = request.getParameter("passwd");
			String active_bcg_id = request.getParameter("active_bcg_id");
			
			String session_zregion = (String) session.getAttribute("zregion");
			String session_zdept_id = (String) session.getAttribute("zdept_id");
			String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
			String session_zemp_id = (String) session.getAttribute("zemp_id");
			String session_zauth_grp = (String) session.getAttribute("zauth_grp");
			String session_zregion_gb = (String) session.getAttribute("zregion_gb");
			
			HashMap paramMap = new HashMap();
			//paramMap.put("zregion", zregion);
			//paramMap.put("zdept_id", zdept_id);
			//paramMap.put("zdept_brch_id", zdept_brch_id);
			//paramMap.put("zemp_id", zemp_id);
			paramMap.put("zreq_ym_b", zreq_ym_b);
			paramMap.put("zreq_ym_a", zreq_ym_a);
			paramMap.put("passwd", passwd);
			paramMap.put("active_bcg_id", active_bcg_id);
			paramMap.put("session_zregion", session_zregion);
			paramMap.put("session_zdept_id", session_zdept_id);
			paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
			paramMap.put("session_zemp_id", session_zemp_id);
			paramMap.put("session_zauth_grp", session_zauth_grp);
			paramMap.put("session_zregion_gb", session_zregion_gb);
			
			// 데이터 조회
			List selectReportDataList = exlMainMapper.excelArrPayment_da(paramMap);
			
			ExcelUtil excelUtil = ExcelUtil.getInstance();
			excelUtil.setRequest(request);
			excelUtil.setResponse(response);
			excelUtil.setFilename("[관리용]2개월체납_리스트");
			excelUtil.setTitle("엑셀다운로드일시 : " + getDownloadDate());
			excelUtil.setTitleindex(2);
			
			SXSSFWorkbook workbook = excelUtil.getSxssfWorkbook("tplArrPayment_da.xlsx");
			workbook.setCompressTempFiles(true);
			
			// 첫번째 시트 생성
			SXSSFSheet sheet = workbook.getSheetAt(0);
			
			// 템플릿을 제외한 마지막 ROWNUM을 읽는다(해당 ROW부터 쓰기위해서)
			int rowNo = 4;
			
			// 셀스타일 생성(상하좌우 테두리)
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			Font font = style.getFont();
			font.setFontHeight((short) 200);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			// 셀에 데이터를 넣는 부분
			for (int i = 0; i < selectReportDataList.size(); i++) {
				// 1열 작성
				SXSSFRow row = sheet.createRow(rowNo++);
				
				int index = 0;
				
				SXSSFCell cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getZrms_ym());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getZregion_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getZdept_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getZdept_brch_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getZemp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getZemp_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getZauth_grp());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getZmbr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getKunnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getZgrade_cde());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getMatnr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getMatnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getZrcpt_amt());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlArrPaymentVO) selectReportDataList.get(i)).getZlastfee_ym());
			}
			
			insertExcelLog(session, selectReportDataList.size(), request.getServletPath());
			
			// 엑셀 다운
			excelUtil.runToExcel(workbook, passwd);
			
		} catch (IOException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );  
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );	
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
			
		} catch(Exception e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );			
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
		}
	}

	/*******************************************
	 * [ 현장용 ] 직접결제 중복발송 엑셀다운   *
	 *******************************************/
	@Override
	public void excelPayDirect_d(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			HttpSession session = request.getSession();
			
			String zregion = request.getParameter("zregion");
			String zdept_id = request.getParameter("zdept_id");
			String zdept_brch_id = request.getParameter("zdept_brch_id");
			String zemp_id = request.getParameter("zemp_id");
			String zreq_ym_b = request.getParameter("zreq_ym_b");
			String zreq_ym_a = request.getParameter("zreq_ym_a");
			String passwd = request.getParameter("passwd");
			String active_bcg_id = request.getParameter("active_bcg_id");
			
			String session_zregion = (String) session.getAttribute("zregion");
			String session_zdept_id = (String) session.getAttribute("zdept_id");
			String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
			String session_zemp_id = (String) session.getAttribute("zemp_id");
			String session_zauth_grp = (String) session.getAttribute("zauth_grp");
			String session_zregion_gb = (String) session.getAttribute("zregion_gb");
			
			HashMap paramMap = new HashMap();
			paramMap.put("zregion", zregion);
			paramMap.put("zdept_id", zdept_id);
			paramMap.put("zdept_brch_id", zdept_brch_id);
			paramMap.put("zemp_id", zemp_id);
			paramMap.put("zreq_ym_b", zreq_ym_b);
			paramMap.put("zreq_ym_a", zreq_ym_a);
			paramMap.put("passwd", passwd);
			paramMap.put("active_bcg_id", active_bcg_id);
			paramMap.put("session_zregion", session_zregion);
			paramMap.put("session_zdept_id", session_zdept_id);
			paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
			paramMap.put("session_zemp_id", session_zemp_id);
			paramMap.put("session_zauth_grp", session_zauth_grp);
			paramMap.put("session_zregion_gb", session_zregion_gb);
			
			// 데이터 조회
			List selectReportDataList = exlMainMapper.excelPayDirect_d(paramMap);
			
			ExcelUtil excelUtil = ExcelUtil.getInstance();
			excelUtil.setRequest(request);
			excelUtil.setResponse(response);
			excelUtil.setFilename("[현장용]고객직접결제_중복발송_리스트");
			excelUtil.setTitle("엑셀다운로드일시 : " + getDownloadDate());
			excelUtil.setTitleindex(2);
			
			SXSSFWorkbook workbook = excelUtil.getSxssfWorkbook("tplPayDirect_d.xlsx");
			workbook.setCompressTempFiles(true);
			
			// 첫번째 시트 생성
			SXSSFSheet sheet = workbook.getSheetAt(0);
			
			// 템플릿을 제외한 마지막 ROWNUM을 읽는다(해당 ROW부터 쓰기위해서)
			int rowNo = 4;
			
			// 셀스타일 생성(상하좌우 테두리)
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			Font font = style.getFont();
			font.setFontHeight((short) 200);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			// 셀에 데이터를 넣는 부분
			for (int i = 0; i < selectReportDataList.size(); i++) {
				// 1열 작성
				SXSSFRow row = sheet.createRow(rowNo++);
				
				int index = 0;
				
				SXSSFCell cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZrms_ym());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZregion_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZdept_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZdept_brch_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZemp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZemp_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZauth_grp());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZmbr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getKunnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZreq_gb());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZsms_send_dt());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZrec_name());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZrec_conn());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZrec_tel());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZappr_type());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZappr_no());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZappr_dt());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZappr_state());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZappr_amt());
			}
			
			insertExcelLog(session, selectReportDataList.size(), request.getServletPath());
			
			// 엑셀 다운
			excelUtil.runToExcel(workbook, passwd);
			
		} catch (IOException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );  
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );	
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
			
		} catch(Exception e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );			
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
		}
		
	}

	/*******************************************
	 * [ 관리용 ] 직접결제 중복발송 엑셀다운   *
	 *******************************************/
	@Override
	public void excelPayDirect_da(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			HttpSession session = request.getSession();
			
			String zregion = request.getParameter("zregion");
			String zdept_id = request.getParameter("zdept_id");
			String zdept_brch_id = request.getParameter("zdept_brch_id");
			String zemp_id = request.getParameter("zemp_id");
			String zreq_ym_b = request.getParameter("zreq_ym_b");
			String zreq_ym_a = request.getParameter("zreq_ym_a");
			String passwd = request.getParameter("passwd");
			String active_bcg_id = request.getParameter("active_bcg_id");
			
			String session_zregion = (String) session.getAttribute("zregion");
			String session_zdept_id = (String) session.getAttribute("zdept_id");
			String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
			String session_zemp_id = (String) session.getAttribute("zemp_id");
			String session_zauth_grp = (String) session.getAttribute("zauth_grp");
			String session_zregion_gb = (String) session.getAttribute("zregion_gb");
			
			HashMap paramMap = new HashMap();
			//paramMap.put("zregion", zregion);
			//paramMap.put("zdept_id", zdept_id);
			//paramMap.put("zdept_brch_id", zdept_brch_id);
			//paramMap.put("zemp_id", zemp_id);
			paramMap.put("zreq_ym_b", zreq_ym_b);
			paramMap.put("zreq_ym_a", zreq_ym_a);
			paramMap.put("passwd", passwd);
			paramMap.put("active_bcg_id", active_bcg_id);
			paramMap.put("session_zregion", session_zregion);
			paramMap.put("session_zdept_id", session_zdept_id);
			paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
			paramMap.put("session_zemp_id", session_zemp_id);
			paramMap.put("session_zauth_grp", session_zauth_grp);
			paramMap.put("session_zregion_gb", session_zregion_gb);
			
			// 데이터 조회
			List selectReportDataList = exlMainMapper.excelPayDirect_da(paramMap);
			
			ExcelUtil excelUtil = ExcelUtil.getInstance();
			excelUtil.setRequest(request);
			excelUtil.setResponse(response);
			excelUtil.setFilename("[관리용]고객직접결제_중복발송_리스트");
			excelUtil.setTitle("엑셀다운로드일시 : " + getDownloadDate());
			excelUtil.setTitleindex(2);
			
			SXSSFWorkbook workbook = excelUtil.getSxssfWorkbook("tplPayDirect_da.xlsx");
			workbook.setCompressTempFiles(true);
			
			// 첫번째 시트 생성
			SXSSFSheet sheet = workbook.getSheetAt(0);
			
			// 템플릿을 제외한 마지막 ROWNUM을 읽는다(해당 ROW부터 쓰기위해서)
			int rowNo = 4;
			
			// 셀스타일 생성(상하좌우 테두리)
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			Font font = style.getFont();
			font.setFontHeight((short) 200);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			// 셀에 데이터를 넣는 부분
			for (int i = 0; i < selectReportDataList.size(); i++) {
				// 1열 작성
				SXSSFRow row = sheet.createRow(rowNo++);
				
				int index = 0;
				
				SXSSFCell cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZrms_ym());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZregion_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZdept_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZdept_brch_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZemp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZemp_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZauth_grp());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZmbr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getKunnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getMatnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZreq_gb());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZsms_send_dt());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZrec_name());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZrec_conn());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZrec_tel());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZappr_type());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZappr_no());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZappr_dt());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZappr_state());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZappr_amt());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZdup_month());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(((ExlPayDirectVO) selectReportDataList.get(i)).getZadmin_etc());
			}
			
			insertExcelLog(session, selectReportDataList.size(), request.getServletPath());
			
			// 엑셀 다운
			excelUtil.runToExcel(workbook, passwd);
			
		} catch (IOException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );  
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );	
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
			
		} catch(Exception e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );			
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
		}
		
	}
	
	/*******************************************
	 * [ 현장용 ] 긴급교재 엑셀다운  		   *
	 *******************************************/
	@Override
	public void excelStmApply_d(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			HttpSession session = request.getSession();
			
			String zregion = request.getParameter("zregion");
			String zdept_id = request.getParameter("zdept_id");
			String zdept_brch_id = request.getParameter("zdept_brch_id");
			String zemp_id = request.getParameter("zemp_id");
			String zreq_ym_b = request.getParameter("zreq_ym_b");
			String zreq_ym_a = request.getParameter("zreq_ym_a");
			String passwd = request.getParameter("passwd");
			String active_bcg_id = request.getParameter("active_bcg_id");
			
			String session_zregion = (String) session.getAttribute("zregion");
			String session_zdept_id = (String) session.getAttribute("zdept_id");
			String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
			String session_zemp_id = (String) session.getAttribute("zemp_id");
			String session_zauth_grp = (String) session.getAttribute("zauth_grp");
			String session_zregion_gb = (String) session.getAttribute("zregion_gb");
			
			HashMap paramMap = new HashMap();
			paramMap.put("zregion", zregion);
			paramMap.put("zdept_id", zdept_id);
			paramMap.put("zdept_brch_id", zdept_brch_id);
			paramMap.put("zemp_id", zemp_id);
			paramMap.put("zreq_ym_b", zreq_ym_b);
			paramMap.put("zreq_ym_a", zreq_ym_a);
			paramMap.put("passwd", passwd);
			paramMap.put("active_bcg_id", active_bcg_id);
			paramMap.put("session_zregion", session_zregion);
			paramMap.put("session_zdept_id", session_zdept_id);
			paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
			paramMap.put("session_zemp_id", session_zemp_id);
			paramMap.put("session_zauth_grp", session_zauth_grp);
			paramMap.put("session_zregion_gb", session_zregion_gb);
			
			// 데이터 조회
			List selectReportDataList = exlMainMapper.excelStmApply_d(paramMap);
			
			ExcelUtil excelUtil = ExcelUtil.getInstance();
			excelUtil.setRequest(request);
			excelUtil.setResponse(response);
			excelUtil.setFilename("[현장용]긴급교재_리스트");
			excelUtil.setTitle("엑셀다운로드일시 : " + getDownloadDate());
			excelUtil.setTitleindex(2);
			
			SXSSFWorkbook workbook = excelUtil.getSxssfWorkbook("tplStmApply_d.xlsx");
			workbook.setCompressTempFiles(true);
			
			// 첫번째 시트 생성
			SXSSFSheet sheet = workbook.getSheetAt(0);
			
			// 템플릿을 제외한 마지막 ROWNUM을 읽는다(해당 ROW부터 쓰기위해서)
			int rowNo = 5;
			
			// 셀스타일 생성(상하좌우 테두리)
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			Font font = style.getFont();
			font.setFontHeight((short) 200);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			// 셀에 데이터를 넣는 부분
			for (int i = 0; i < selectReportDataList.size(); i++) {
				ExlStmApplyVO vo = (ExlStmApplyVO) selectReportDataList.get(i);
				
				// 1열 작성
				SXSSFRow row = sheet.createRow(rowNo++);
				
				int index = 0;
				
				SXSSFCell cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZrms_ym()); // 기준년월
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZregion_nm()); // BCG명
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_nm()); // 사업국명
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_brch_id()); // 팀명
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZemp_nm()); // 교사명
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZemp_id()); // 사번
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZauth_grp()); // 직책
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZmon_cnt()); // 월말총원
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZnew_cnt()); // 당월입회
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZnew_rate()); // 입회율
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZreq_cnt()); // 신청건수
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZreq_vol()); // 신청권수
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZmon_cnt_p()); // 신청비중
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZreq_non_vol()); // 미지정회원 신청권수
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_01());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_01_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_04());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_04_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_06());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_06_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_03());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_03_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_05());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_05_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_07());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_07_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_etc());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_etc_p());
				
			}
			
			insertExcelLog(session, selectReportDataList.size(), request.getServletPath());
			
			// 엑셀 다운
			excelUtil.runToExcel(workbook, passwd);
			
		} catch (IOException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );  
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );	
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
			
		} catch(Exception e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );			
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
		}
		
	}

	/*******************************************
	 * [ 관리용 ] 긴급교재 엑셀다운   		   *
	 *******************************************/
	@Override
	public void excelStmApply_da(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			HttpSession session = request.getSession();
			
			String zregion = request.getParameter("zregion");
			String zdept_id = request.getParameter("zdept_id");
			String zdept_brch_id = request.getParameter("zdept_brch_id");
			String zemp_id = request.getParameter("zemp_id");
			String zreq_ym_b = request.getParameter("zreq_ym_b");
			String zreq_ym_a = request.getParameter("zreq_ym_a");
			String passwd = request.getParameter("passwd");
			String active_bcg_id = request.getParameter("active_bcg_id");
			
			String session_zregion = (String) session.getAttribute("zregion");
			String session_zdept_id = (String) session.getAttribute("zdept_id");
			String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
			String session_zemp_id = (String) session.getAttribute("zemp_id");
			String session_zauth_grp = (String) session.getAttribute("zauth_grp");
			String session_zregion_gb = (String) session.getAttribute("zregion_gb");
			
			HashMap paramMap = new HashMap();
			//paramMap.put("zregion", zregion);
			//paramMap.put("zdept_id", zdept_id);
			//paramMap.put("zdept_brch_id", zdept_brch_id);
			//paramMap.put("zemp_id", zemp_id);
			paramMap.put("zreq_ym_b", zreq_ym_b);
			paramMap.put("zreq_ym_a", zreq_ym_a);
			paramMap.put("passwd", passwd);
			paramMap.put("active_bcg_id", active_bcg_id);
			paramMap.put("session_zregion", session_zregion);
			paramMap.put("session_zdept_id", session_zdept_id);
			paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
			paramMap.put("session_zemp_id", session_zemp_id);
			paramMap.put("session_zauth_grp", session_zauth_grp);
			paramMap.put("session_zregion_gb", session_zregion_gb);
			
			// 데이터 조회
			List selectReportDataList = exlMainMapper.excelStmApply_da(paramMap);
			
			ExcelUtil excelUtil = ExcelUtil.getInstance();
			excelUtil.setRequest(request);
			excelUtil.setResponse(response);
			excelUtil.setFilename("[관리용]긴급교재_리스트");
			excelUtil.setTitle("엑셀다운로드일시 : " + getDownloadDate());
			excelUtil.setTitleindex(2);
			
			SXSSFWorkbook workbook = excelUtil.getSxssfWorkbook("tplStmApply_da.xlsx");
			workbook.setCompressTempFiles(true);
			
			// 첫번째 시트 생성
			SXSSFSheet sheet = workbook.getSheetAt(0);
			
			// 템플릿을 제외한 마지막 ROWNUM을 읽는다(해당 ROW부터 쓰기위해서)
			int rowNo = 5;
			
			// 셀스타일 생성(상하좌우 테두리)
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			Font font = style.getFont();
			font.setFontHeight((short) 200);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			// 셀에 데이터를 넣는 부분
			for (int i = 0; i < selectReportDataList.size(); i++) {
				ExlStmApplyVO vo = (ExlStmApplyVO) selectReportDataList.get(i);
				
				int index = 0;
				// 1열 작성
				SXSSFRow row = sheet.createRow(rowNo++);
				
				SXSSFCell cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZrms_ym());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZregion_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_brch_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZemp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZemp_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZauth_grp());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZmon_cnt());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZnew_cnt());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZnew_rate());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZreq_cnt());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZreq_vol());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZmon_cnt_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZreq_non_vol());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_01());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_01_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_04());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_04_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_06());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_06_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_03());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_03_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_05());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_05_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_07());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_07_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_02());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_02_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_08());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_08_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_09());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_09_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_11());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_11_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_10());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_10_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_13());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_13_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_12());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_12_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_14());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrund_14_p());
			} 
			
			insertExcelLog(session, selectReportDataList.size(), request.getServletPath());
			
			// 엑셀 다운
			excelUtil.runToExcel(workbook, passwd);
			
		} catch (IOException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );  
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );	
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
			
		} catch(Exception e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );			
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
		}
		
	}
	
	/*******************************************
	 * [ 현장용 ] 써밋제품 학습 엑셀다운 	   *
	 *******************************************/
	@Override
	public void excelSmiRisk_d(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			HttpSession session = request.getSession();
			
			String zregion = request.getParameter("zregion");
			String zdept_id = request.getParameter("zdept_id");
			String zdept_brch_id = request.getParameter("zdept_brch_id");
			String zemp_id = request.getParameter("zemp_id");
			String zreq_ym_b = request.getParameter("zreq_ym_b");
			String zreq_ym_a = request.getParameter("zreq_ym_a");
			String passwd = request.getParameter("passwd");
			String active_bcg_id = request.getParameter("active_bcg_id");
			
			String session_zregion = (String) session.getAttribute("zregion");
			String session_zdept_id = (String) session.getAttribute("zdept_id");
			String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
			String session_zemp_id = (String) session.getAttribute("zemp_id");
			String session_zauth_grp = (String) session.getAttribute("zauth_grp");
			String session_zregion_gb = (String) session.getAttribute("zregion_gb");
			
			HashMap paramMap = new HashMap();
			paramMap.put("zregion", zregion);
			paramMap.put("zdept_id", zdept_id);
			paramMap.put("zdept_brch_id", zdept_brch_id);
			paramMap.put("zemp_id", zemp_id);
			paramMap.put("zreq_ym_b", zreq_ym_b);
			paramMap.put("zreq_ym_a", zreq_ym_a);
			paramMap.put("passwd", passwd);
			paramMap.put("active_bcg_id", active_bcg_id);
			paramMap.put("session_zregion", session_zregion);
			paramMap.put("session_zdept_id", session_zdept_id);
			paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
			paramMap.put("session_zemp_id", session_zemp_id);
			paramMap.put("session_zauth_grp", session_zauth_grp);
			paramMap.put("session_zregion_gb", session_zregion_gb);
			
			// 데이터 조회
			List selectReportDataList = exlMainMapper.excelSmiRisk_d(paramMap);
			
			ExcelUtil excelUtil = ExcelUtil.getInstance();
			excelUtil.setRequest(request);
			excelUtil.setResponse(response);
			excelUtil.setFilename("[현장용]써밋제품학습_리스트");
			excelUtil.setTitle("엑셀다운로드일시 : " + getDownloadDate());
			excelUtil.setTitleindex(2);
			
			SXSSFWorkbook workbook = excelUtil.getSxssfWorkbook("tplSmiRisk_d.xlsx");
			workbook.setCompressTempFiles(true);
			
			// 첫번째 시트 생성
			SXSSFSheet sheet = workbook.getSheetAt(0);
			
			// 템플릿을 제외한 마지막 ROWNUM을 읽는다(해당 ROW부터 쓰기위해서)
			int rowNo = 4;
			
			// 셀스타일 생성(상하좌우 테두리)
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			Font font = style.getFont();
			font.setFontHeight((short) 200);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			// 셀에 데이터를 넣는 부분
			for (int i = 0; i < selectReportDataList.size(); i++) {
				ExlSmiRiskVO vo = (ExlSmiRiskVO) selectReportDataList.get(i);
				int index = 0;
				// 1열 작성
				SXSSFRow row = sheet.createRow(rowNo++);
				
				SXSSFCell cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZrms_ym());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZregion_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_brch_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZemp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZemp_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZauth_grp());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZmbr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getKunnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrade_cde());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getMatnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getMatnr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZcls_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdup_month());
			}
			
			insertExcelLog(session, selectReportDataList.size(), request.getServletPath());
			
			// 엑셀 다운
			excelUtil.runToExcel(workbook, passwd);
			
		} catch (IOException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );  
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );	
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
			
		} catch(Exception e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );			
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
		}
	}
	
	/*******************************************
	 * [ 관리용 ] 써밋제품 학습 엑셀다운 	   *
	 *******************************************/
	@Override
	public void excelSmiRisk_da(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			HttpSession session = request.getSession();
			
			String zregion = request.getParameter("zregion");
			String zdept_id = request.getParameter("zdept_id");
			String zdept_brch_id = request.getParameter("zdept_brch_id");
			String zemp_id = request.getParameter("zemp_id");
			String zreq_ym_b = request.getParameter("zreq_ym_b");
			String zreq_ym_a = request.getParameter("zreq_ym_a");
			String passwd = request.getParameter("passwd");
			String active_bcg_id = request.getParameter("active_bcg_id");
			
			String session_zregion = (String) session.getAttribute("zregion");
			String session_zdept_id = (String) session.getAttribute("zdept_id");
			String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
			String session_zemp_id = (String) session.getAttribute("zemp_id");
			String session_zauth_grp = (String) session.getAttribute("zauth_grp");
			String session_zregion_gb = (String) session.getAttribute("zregion_gb");
			
			HashMap paramMap = new HashMap();
			//paramMap.put("zregion", zregion);
			//paramMap.put("zdept_id", zdept_id);
			//paramMap.put("zdept_brch_id", zdept_brch_id);
			//paramMap.put("zemp_id", zemp_id);
			paramMap.put("zreq_ym_b", zreq_ym_b);
			paramMap.put("zreq_ym_a", zreq_ym_a);
			paramMap.put("passwd", passwd);
			paramMap.put("active_bcg_id", active_bcg_id);
			paramMap.put("session_zregion", session_zregion);
			paramMap.put("session_zdept_id", session_zdept_id);
			paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
			paramMap.put("session_zemp_id", session_zemp_id);
			paramMap.put("session_zauth_grp", session_zauth_grp);
			paramMap.put("session_zregion_gb", session_zregion_gb);
			
			// 데이터 조회
			List selectReportDataList = exlMainMapper.excelSmiRisk_d(paramMap);
			
			ExcelUtil excelUtil = ExcelUtil.getInstance();
			excelUtil.setRequest(request);
			excelUtil.setResponse(response);
			excelUtil.setFilename("[관리용]써밋제품학습_리스트");
			excelUtil.setTitle("엑셀다운로드일시 : " + getDownloadDate());
			excelUtil.setTitleindex(2);
			
			SXSSFWorkbook workbook = excelUtil.getSxssfWorkbook("tplSmiRisk_da.xlsx");
			workbook.setCompressTempFiles(true);
			
			// 첫번째 시트 생성
			SXSSFSheet sheet = workbook.getSheetAt(0);
			
			// 템플릿을 제외한 마지막 ROWNUM을 읽는다(해당 ROW부터 쓰기위해서)
			int rowNo = 4;
			
			// 셀스타일 생성(상하좌우 테두리)
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			Font font = style.getFont();
			font.setFontHeight((short) 200);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			// 셀에 데이터를 넣는 부분
			for (int i = 0; i < selectReportDataList.size(); i++) {
				ExlSmiRiskVO vo = (ExlSmiRiskVO) selectReportDataList.get(i);
				int index = 0;
				// 1열 작성
				SXSSFRow row = sheet.createRow(rowNo++);
				
				SXSSFCell cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZrms_ym());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZregion_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_brch_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZemp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZemp_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZauth_grp());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZmbr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getKunnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrade_cde());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getMatnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getMatnr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZcls_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdup_month());
			}
			
			insertExcelLog(session, selectReportDataList.size(), request.getServletPath());
			
			// 엑셀 다운
			excelUtil.runToExcel(workbook, passwd);
			
		} catch (IOException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );  
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );	
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
			
		} catch(Exception e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );			
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
		}
	}

	/*******************************************
	 * [ 현장용 ] 월별모니터링 엑셀다운		   *
	 *******************************************/
	@Override
	public void excelMonMoni_d(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			HttpSession session = request.getSession();
			
			String zregion = request.getParameter("zregion");
			String zdept_id = request.getParameter("zdept_id");
			String zdept_brch_id = request.getParameter("zdept_brch_id");
			String zemp_id = request.getParameter("zemp_id");
			String zreq_ym_b = request.getParameter("zreq_ym_b");
			String zreq_ym_a = request.getParameter("zreq_ym_a");
			String passwd = request.getParameter("passwd");
			String active_bcg_id = request.getParameter("active_bcg_id");
			String zlevel = "4";
			
			String session_zregion = (String) session.getAttribute("zregion");
			String session_zdept_id = (String) session.getAttribute("zdept_id");
			String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
			String session_zemp_id = (String) session.getAttribute("zemp_id");
			String session_zauth_grp = (String) session.getAttribute("zauth_grp");
			String session_zregion_gb = (String) session.getAttribute("zregion_gb");
			
			if(!zregion.equals("") && zdept_id.equals("") && zdept_brch_id.equals("") && zemp_id.equals("")) {
				zlevel = "1";
			}
			if(!zregion.equals("") && !zdept_id.equals("") && zdept_brch_id.equals("") && zemp_id.equals("")) {
				zlevel = "2";
			}
			if(!zregion.equals("") && !zdept_id.equals("") && !zdept_brch_id.equals("") && zemp_id.equals("")) {
				zlevel = "3";
			}
			if(!zregion.equals("") && !zdept_id.equals("") && !zdept_brch_id.equals("") && !zemp_id.equals("")) {
				zlevel = "4";
			}
			
			HashMap paramMap = new HashMap();
			paramMap.put("zlevel", zlevel);
			paramMap.put("zregion", zregion);
			paramMap.put("zdept_id", zdept_id);
			paramMap.put("zdept_brch_id", zdept_brch_id);
			paramMap.put("zemp_id", zemp_id);
			paramMap.put("zreq_ym_b", zreq_ym_b);
			paramMap.put("zreq_ym_a", zreq_ym_a);
			paramMap.put("passwd", passwd);
			paramMap.put("active_bcg_id", active_bcg_id);
			paramMap.put("session_zregion", session_zregion);
			paramMap.put("session_zdept_id", session_zdept_id);
			paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
			paramMap.put("session_zemp_id", session_zemp_id);
			paramMap.put("session_zauth_grp", session_zauth_grp);
			paramMap.put("session_zregion_gb", session_zregion_gb);
			
			// 데이터 조회
			List selectReportDataList = exlMainMapper.excelMonMoni_d(paramMap);
			
			ExcelUtil excelUtil = ExcelUtil.getInstance();
			excelUtil.setRequest(request);
			excelUtil.setResponse(response);
			excelUtil.setFilename("월별_모니터링");
			excelUtil.setTitle("엑셀다운로드일시 : " + getDownloadDate());
			excelUtil.setTitleindex(1);
			SXSSFWorkbook workbook = excelUtil.getSxssfWorkbook("tplMonMoni_d.xlsx");
			if(!active_bcg_id.equals("N")) {
				workbook = excelUtil.getSxssfWorkbook("tplMonMoni_cs_d.xlsx");
			}
			
			workbook.setCompressTempFiles(true);
			
			// 첫번째 시트 생성
			SXSSFSheet sheet = workbook.getSheetAt(0);
			
			// 템플릿을 제외한 마지막 ROWNUM을 읽는다(해당 ROW부터 쓰기위해서)
			int rowNo = 3;
			
			// 평균
			XSSFCellStyle avgStyle = (XSSFCellStyle) workbook.createCellStyle();
			Font avgFont = avgStyle.getFont();
			avgFont.setFontHeight((short) 200);
			avgFont.setBold(true);
			avgStyle.setBorderRight(BorderStyle.THIN);
			avgStyle.setBorderLeft(BorderStyle.THIN);
			avgStyle.setBorderTop(BorderStyle.THIN);
			avgStyle.setBorderBottom(BorderStyle.THIN);
			avgStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			//avgStyle.setAlignment(HorizontalAlignment.CENTER);
			avgStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(218, 238, 243))); 
			avgStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			int avgIndex = 0;
			ExlMonMoniVO avgVo = (ExlMonMoniVO) selectReportDataList.get(0);
			
			SXSSFRow avgRow = sheet.createRow(rowNo++);
			SXSSFCell avgCell = avgRow.createCell(avgIndex++);
			avgCell.setCellStyle(avgStyle);
			avgCell.setCellValue("평균");
			
			avgCell = avgRow.createCell(avgIndex++);
			avgCell.setCellStyle(avgStyle);
			avgCell.setCellValue(avgVo.getZitem1_p());
			
			avgCell = avgRow.createCell(avgIndex++);
			avgCell.setCellStyle(avgStyle);
			avgCell.setCellValue(avgVo.getZitem2_p());
			
			avgCell = avgRow.createCell(avgIndex++);
			avgCell.setCellStyle(avgStyle);
			avgCell.setCellValue(avgVo.getZitem3_p());
			
			avgCell = avgRow.createCell(avgIndex++);
			avgCell.setCellStyle(avgStyle);
			avgCell.setCellValue(avgVo.getZitem4_p());
			
			avgCell = avgRow.createCell(avgIndex++);
			avgCell.setCellStyle(avgStyle);
			avgCell.setCellValue(avgVo.getZitem5_p());
			
			if(active_bcg_id.equals("N")) {
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				avgCell.setCellValue(avgVo.getZitem6_p());
				
				avgCell = avgRow.createCell(avgIndex++);
				avgCell.setCellStyle(avgStyle);
				avgCell.setCellValue(avgVo.getZitem7_p());
			}
			
			avgCell = avgRow.createCell(avgIndex++);
			avgCell.setCellStyle(avgStyle);
			avgCell.setCellValue(avgVo.getZadmin_rank());

			/* 개선순위 2021.07.07
			avgCell = avgRow.createCell(avgIndex++);
			avgCell.setCellStyle(avgStyle);
			avgCell.setCellValue("-");
			*/
			
			// 셀스타일 생성(상하좌우 테두리)
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			Font font = style.getFont();
			font.setFontHeight((short) 200);
			font.setBold(false);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			// 셀에 데이터를 넣는 부분
			for (int i = 1; i < selectReportDataList.size(); i++) {
				ExlMonMoniVO vo = (ExlMonMoniVO) selectReportDataList.get(i);
				int index = 0;
				// 1열 작성
				SXSSFRow row = sheet.createRow(rowNo++);
				
				SXSSFCell cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZrms_ym());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem1_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem2_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem3_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem4_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem5_p());
				
				if(active_bcg_id.equals("N")) {
					cell = row.createCell(index++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getZitem6_p());
					
					cell = row.createCell(index++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getZitem7_p());
				}
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZadmin_rank());

				/* 개선순위 2021.07.07
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZup_rank());
				*/
			}
			
			insertExcelLog(session, selectReportDataList.size(), request.getServletPath());
			
			// 엑셀 다운
			excelUtil.runToExcel(workbook, passwd);
			
		} catch (IOException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );  
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );	
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
			
		} catch(Exception e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );			
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
		}
	}

	/*******************************************************
	 * [ 현장용 ] 월별모니터링 엑셀다운-대상건수		   *
	 *******************************************************/
	@Override
	public void excelMonMoni_d_cnt(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			HttpSession session = request.getSession();
			
			String zregion = request.getParameter("zregion");
			String zdept_id = request.getParameter("zdept_id");
			String zdept_brch_id = request.getParameter("zdept_brch_id");
			String zemp_id = request.getParameter("zemp_id");
			String zreq_ym_b = request.getParameter("zreq_ym_b");
			String zreq_ym_a = request.getParameter("zreq_ym_a");
			String passwd = request.getParameter("passwd");
			String active_bcg_id = request.getParameter("active_bcg_id");
			String zlevel = "4";
			
			String session_zregion = (String) session.getAttribute("zregion");
			String session_zdept_id = (String) session.getAttribute("zdept_id");
			String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
			String session_zemp_id = (String) session.getAttribute("zemp_id");
			String session_zauth_grp = (String) session.getAttribute("zauth_grp");
			String session_zregion_gb = (String) session.getAttribute("zregion_gb");
			
			if(!zregion.equals("") && zdept_id.equals("") && zdept_brch_id.equals("") && zemp_id.equals("")) {
				zlevel = "1";
			}
			if(!zregion.equals("") && !zdept_id.equals("") && zdept_brch_id.equals("") && zemp_id.equals("")) {
				zlevel = "2";
			}
			if(!zregion.equals("") && !zdept_id.equals("") && !zdept_brch_id.equals("") && zemp_id.equals("")) {
				zlevel = "3";
			}
			if(!zregion.equals("") && !zdept_id.equals("") && !zdept_brch_id.equals("") && !zemp_id.equals("")) {
				zlevel = "4";
			}
			
			HashMap paramMap = new HashMap();
			paramMap.put("zlevel", zlevel);
			paramMap.put("zregion", zregion);
			paramMap.put("zdept_id", zdept_id);
			paramMap.put("zdept_brch_id", zdept_brch_id);
			paramMap.put("zemp_id", zemp_id);
			paramMap.put("zreq_ym_b", zreq_ym_b);
			paramMap.put("zreq_ym_a", zreq_ym_a);
			paramMap.put("passwd", passwd);
			paramMap.put("active_bcg_id", active_bcg_id);
			paramMap.put("session_zregion", session_zregion);
			paramMap.put("session_zdept_id", session_zdept_id);
			paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
			paramMap.put("session_zemp_id", session_zemp_id);
			paramMap.put("session_zauth_grp", session_zauth_grp);
			paramMap.put("session_zregion_gb", session_zregion_gb);
			
			// 데이터 조회
			List selectReportDataList = exlMainMapper.excelMonMoni_d_cnt(paramMap);
			
			ExcelUtil excelUtil = ExcelUtil.getInstance();
			excelUtil.setRequest(request);
			excelUtil.setResponse(response);
			excelUtil.setFilename("월별_모니터링");
			excelUtil.setTitle("엑셀다운로드일시 : " + getDownloadDate());
			excelUtil.setTitleindex(1);
			SXSSFWorkbook workbook = excelUtil.getSxssfWorkbook("tplMonMoni_d.xlsx");
			if(!active_bcg_id.equals("N")) {
				workbook = excelUtil.getSxssfWorkbook("tplMonMoni_cs_d.xlsx");
			}
			
			workbook.setCompressTempFiles(true);
			
			// 첫번째 시트 생성
			SXSSFSheet sheet = workbook.getSheetAt(0);
			
			// 템플릿을 제외한 마지막 ROWNUM을 읽는다(해당 ROW부터 쓰기위해서)
			int rowNo = 3;
			
			// 셀스타일 생성(상하좌우 테두리)
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			Font font = style.getFont();
			font.setFontHeight((short) 200);
			font.setBold(false);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			// 셀에 데이터를 넣는 부분
			for (int i = 1; i < selectReportDataList.size(); i++) {
				ExlMonMoniVO vo = (ExlMonMoniVO) selectReportDataList.get(i);
				int index = 0;
				// 1열 작성
				SXSSFRow row = sheet.createRow(rowNo++);
				
				SXSSFCell cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZrms_ym());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem1_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem2_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem3_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem4_p());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZitem5_p());
				
				if(active_bcg_id.equals("N")) {
					cell = row.createCell(index++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getZitem6_p());
					
					cell = row.createCell(index++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getZitem7_p());
				}
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZadmin_rank());

				/* 개선순위 2021.07.07
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZup_rank());
				*/
			}
			
			insertExcelLog(session, selectReportDataList.size(), request.getServletPath());
			
			// 엑셀 다운
			excelUtil.runToExcel(workbook, passwd);
			
		} catch (IOException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );  
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );	
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
			
		} catch(Exception e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );			
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
		}
	}
	
	/*******************************************
	 * [ 현장용 ] 0개월 학습 엑셀다운		   *
	 *******************************************/
	@Override
	public void excelNstZeroStudy_d(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			HttpSession session = request.getSession();
			
			String zregion = request.getParameter("zregion");
			String zdept_id = request.getParameter("zdept_id");
			String zdept_brch_id = request.getParameter("zdept_brch_id");
			String zemp_id = request.getParameter("zemp_id");
			String zreq_ym_b = request.getParameter("zreq_ym_b");
			String zreq_ym_a = request.getParameter("zreq_ym_a");
			String passwd = request.getParameter("passwd");
			String active_bcg_id = request.getParameter("active_bcg_id");
			
			String session_zregion = (String) session.getAttribute("zregion");
			String session_zdept_id = (String) session.getAttribute("zdept_id");
			String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
			String session_zemp_id = (String) session.getAttribute("zemp_id");
			String session_zauth_grp = (String) session.getAttribute("zauth_grp");
			String session_zregion_gb = (String) session.getAttribute("zregion_gb");
			
			HashMap paramMap = new HashMap();
			paramMap.put("zregion", zregion);
			paramMap.put("zdept_id", zdept_id);
			paramMap.put("zdept_brch_id", zdept_brch_id);
			paramMap.put("zemp_id", zemp_id);
			paramMap.put("zreq_ym_b", zreq_ym_b);
			paramMap.put("zreq_ym_a", zreq_ym_a);
			paramMap.put("passwd", passwd);
			paramMap.put("active_bcg_id", active_bcg_id);
			paramMap.put("session_zregion", session_zregion);
			paramMap.put("session_zdept_id", session_zdept_id);
			paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
			paramMap.put("session_zemp_id", session_zemp_id);
			paramMap.put("session_zauth_grp", session_zauth_grp);
			paramMap.put("session_zregion_gb", session_zregion_gb);
			
			// 데이터 조회
			List selectReportDataList = exlMainMapper.excelNstZeroStudy_d(paramMap);
			
			ExcelUtil excelUtil = ExcelUtil.getInstance();
			excelUtil.setRequest(request);
			excelUtil.setResponse(response);
			excelUtil.setFilename("[현장용]0개월학습_리스트");
			excelUtil.setTitle("엑셀다운로드일시 : " + getDownloadDate());
			excelUtil.setTitleindex(2);
			
			SXSSFWorkbook workbook = excelUtil.getSxssfWorkbook("tplNstZeroStudy_d.xlsx");
			workbook.setCompressTempFiles(true);
			
			// 첫번째 시트 생성
			SXSSFSheet sheet = workbook.getSheetAt(0);
			
			// 템플릿을 제외한 마지막 ROWNUM을 읽는다(해당 ROW부터 쓰기위해서)
			int rowNo = 4;
			
			// 셀스타일 생성(상하좌우 테두리)
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			Font font = style.getFont();
			font.setFontHeight((short) 200);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			// 셀에 데이터를 넣는 부분
			for (int i = 0; i < selectReportDataList.size(); i++) {
				// 1열 작성
				SXSSFRow row = sheet.createRow(rowNo++);
				
				int index = 0;
				
				ExcelNstZeroStudyVO vo = (ExcelNstZeroStudyVO) selectReportDataList.get(i);
				
				SXSSFCell cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZrms_ym());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getOut_zregion_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getOut_zdept_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getOut_zdept_brch_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getOut_team_empnm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getOut_team_empid());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getOut_zemp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getOut_zemp_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZregion_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_brch_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_team_empnm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_team_empid());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZemp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZemp_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZmbr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getKunnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrade_cde());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getMatnr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getMatnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getOut_post_dt());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getOut_gubun());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getIn_post_dt());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getIn_gubun());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZlastfee_ym());
			}
			
			insertExcelLog(session, selectReportDataList.size(), request.getServletPath());
			
			// 엑셀 다운
			excelUtil.runToExcel(workbook, passwd);
			
		} catch (IOException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );  
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );	
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
			
		} catch(Exception e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );			
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
		}
		
	}

	/*******************************************
	 * [ 관리용 ] 0개월 학습 엑셀다운   	   *
	 *******************************************/
	@Override
	public void excelNstZeroStudy_da(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			HttpSession session = request.getSession();
			
			String zregion = request.getParameter("zregion");
			String zdept_id = request.getParameter("zdept_id");
			String zdept_brch_id = request.getParameter("zdept_brch_id");
			String zemp_id = request.getParameter("zemp_id");
			String zreq_ym_b = request.getParameter("zreq_ym_b");
			String zreq_ym_a = request.getParameter("zreq_ym_a");
			String passwd = request.getParameter("passwd");
			String active_bcg_id = request.getParameter("active_bcg_id");
			
			String session_zregion = (String) session.getAttribute("zregion");
			String session_zdept_id = (String) session.getAttribute("zdept_id");
			String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
			String session_zemp_id = (String) session.getAttribute("zemp_id");
			String session_zauth_grp = (String) session.getAttribute("zauth_grp");
			String session_zregion_gb = (String) session.getAttribute("zregion_gb");
			
			HashMap paramMap = new HashMap();
			//paramMap.put("zregion", zregion);
			//paramMap.put("zdept_id", zdept_id);
			//paramMap.put("zdept_brch_id", zdept_brch_id);
			//paramMap.put("zemp_id", zemp_id);
			paramMap.put("zreq_ym_b", zreq_ym_b);
			paramMap.put("zreq_ym_a", zreq_ym_a);
			paramMap.put("passwd", passwd);
			paramMap.put("active_bcg_id", active_bcg_id);
			paramMap.put("session_zregion", session_zregion);
			paramMap.put("session_zdept_id", session_zdept_id);
			paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
			paramMap.put("session_zemp_id", session_zemp_id);
			paramMap.put("session_zauth_grp", session_zauth_grp);
			paramMap.put("session_zregion_gb", session_zregion_gb);
			
			// 데이터 조회
			List selectReportDataList = exlMainMapper.excelNstZeroStudy_da(paramMap);
			
			ExcelUtil excelUtil = ExcelUtil.getInstance();
			excelUtil.setRequest(request);
			excelUtil.setResponse(response);
			excelUtil.setFilename("[관리용]0개월학습_리스트");
			excelUtil.setTitle("엑셀다운로드일시 : " + getDownloadDate());
			excelUtil.setTitleindex(2);
			
			SXSSFWorkbook workbook = excelUtil.getSxssfWorkbook("tplNstZeroStudy_da.xlsx");
			workbook.setCompressTempFiles(true);
			
			// 첫번째 시트 생성
			SXSSFSheet sheet = workbook.getSheetAt(0);
			
			// 템플릿을 제외한 마지막 ROWNUM을 읽는다(해당 ROW부터 쓰기위해서)
			int rowNo = 4;
			
			// 셀스타일 생성(상하좌우 테두리)
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			Font font = style.getFont();
			font.setFontHeight((short) 200);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			// 셀에 데이터를 넣는 부분
			for (int i = 0; i < selectReportDataList.size(); i++) {
				// 1열 작성
				SXSSFRow row = sheet.createRow(rowNo++);
				
				int index = 0;
				
				ExcelNstZeroStudyVO vo = (ExcelNstZeroStudyVO) selectReportDataList.get(i);
				
				SXSSFCell cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZrms_ym());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getOut_zregion_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getOut_zdept_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getOut_zdept_brch_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getOut_lc_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getOut_team_empnm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getOut_team_empid());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getOut_zemp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getOut_zemp_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZregion_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_brch_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getIn_lc_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_team_empnm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_team_empid());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZemp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZemp_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZmbr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getKunnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrade_cde());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getMatnr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getMatnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getMatnr_point());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getOut_post_dt());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getOut_gubun());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getIn_post_dt());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getIn_gubun());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getIns_zregion_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getIns_zdept_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getIns_team_empnm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getIns_team_empid()); // 소개의뢰 팀장사번
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getIns_zdept_brch_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getIns_zemp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getIns_zemp_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getTrn_matnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getTrn_matnr_point());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZaddr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZlastfee_ym());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZbf_matnr_all());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getMatnr_all());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZnew_mem_chk());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZco_kunnr());
			}
			
			insertExcelLog(session, selectReportDataList.size(), request.getServletPath());
			
			// 엑셀 다운
			excelUtil.runToExcel(workbook, passwd);
			
		} catch (IOException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );  
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );	
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
			
		} catch(Exception e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );			
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
		}
		
	}

	@Override
	public void excelMoniSum_r(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			HttpSession session = request.getSession();
			
			String zregion = request.getParameter("zregion");
			String zdept_id = request.getParameter("zdept_id");
			String zdept_brch_id = request.getParameter("zdept_brch_id");
			String zemp_id = request.getParameter("zemp_id");
			String zreq_ym = request.getParameter("zreq_ym_a");
			String passwd = request.getParameter("passwd");
			String active_bcg_id = request.getParameter("active_bcg_id");
			String brch_gb = request.getParameter("brch_gb");
			String cnt_gb = request.getParameter("cnt_gb");
			String tplName = ""; // 템플릿 파일 이름
			int cellMerge = 5; // 템플릿에 셀 병합 수 // lc : 센터장명 까지, bl : 교육국장명까지
			
			String session_zregion = (String) session.getAttribute("zregion");
			String session_zdept_id = (String) session.getAttribute("zdept_id");
			String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
			String session_zemp_id = (String) session.getAttribute("zemp_id");
			String session_zauth_grp = (String) session.getAttribute("zauth_grp");
			String session_zregion_gb = (String) session.getAttribute("zregion_gb");
			
			HashMap paramMap = new HashMap();
			paramMap.put("zreq_ym", zreq_ym);
			paramMap.put("passwd", passwd);
			paramMap.put("active_bcg_id", active_bcg_id);
			paramMap.put("session_zregion", session_zregion);
			paramMap.put("session_zdept_id", session_zdept_id);
			paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
			paramMap.put("session_zemp_id", session_zemp_id);
			paramMap.put("session_zauth_grp", session_zauth_grp);
			paramMap.put("session_zregion_gb", session_zregion_gb);
			paramMap.put("brch_gb", brch_gb);
			paramMap.put("cnt_gb", cnt_gb);
			
			// 데이터 조회
			List selectReportDataList = new ArrayList();;
			if(cnt_gb.equals("ind")) {
				// 비중
				selectReportDataList = exlMainMapper.excelSumMainRank_ind(paramMap);
			} else if(cnt_gb.equals("cnt")) {
				// 대상건수
				selectReportDataList = exlMainMapper.excelSumMainRank_cnt(paramMap);
			}
			
			ExcelUtil excelUtil = ExcelUtil.getInstance();
			excelUtil.setRequest(request);
			excelUtil.setResponse(response);
			if(brch_gb.equals("LC")) {
				excelUtil.setFilename("주요지표_순위_센터");
				tplName = "tplMoniSum_r_lc.xlsx";
				cellMerge = 5;
			} else if(brch_gb.equals("BL")) {
				excelUtil.setFilename("주요지표_순위_방문");
				tplName = "tplMoniSum_r_bl.xlsx";
				cellMerge = 3;
			}
			
			excelUtil.setTitle("엑셀다운로드일시 : " + getDownloadDate());
			excelUtil.setTitleindex(1);
			
			SXSSFWorkbook workbook = excelUtil.getSxssfWorkbook(tplName);
			workbook.setCompressTempFiles(true);
			
			// 첫번째 시트 생성
			SXSSFSheet sheet = workbook.getSheetAt(0);
			
			// 템플릿을 제외한 마지막 ROWNUM을 읽는다(해당 ROW부터 쓰기위해서)
			int rowNo = 3;
			
			if(selectReportDataList.size() > 2) {
				// 상위 20프로
				XSSFCellStyle perStyle = (XSSFCellStyle) workbook.createCellStyle();
				Font perFont = perStyle.getFont();
				perFont.setFontHeight((short) 200);
				perFont.setBold(true);
				perStyle.setBorderRight(BorderStyle.THIN);
				perStyle.setBorderLeft(BorderStyle.THIN);
				perStyle.setBorderTop(BorderStyle.THIN);
				perStyle.setBorderBottom(BorderStyle.THIN);
				perStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				//perStyle.setAlignment(HorizontalAlignment.CENTER);
				perStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(230, 238, 253))); 
				perStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				
				// 방문, LC에 따라 평균과 상위20% 문구를 넣을 컬럼병합 수가 다르다
				sheet.addMergedRegionUnsafe(new CellRangeAddress(3, 3, 0, cellMerge));
				
				int perIndex = 0;
				SumMainVO perVo = (SumMainVO) selectReportDataList.get(0);
				
				SXSSFRow perRow = sheet.createRow(rowNo++);
				SXSSFCell perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue("상위20%");
				
				for(int i = 0; i < cellMerge; i++) {
					perCell = perRow.createCell(perIndex++);
					perCell.setCellStyle(perStyle);
				}
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue(perVo.getRms_info_r());
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue(perVo.getRms_card_r());
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue(perVo.getRms_tran_r());
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue(perVo.getRms_arre_r());
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue(perVo.getRms_pay_r());
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue(perVo.getRms_summ_r());
				
				perCell = perRow.createCell(perIndex++);
				perCell.setCellStyle(perStyle);
				perCell.setCellValue(perVo.getRms_stmng_r());
				
				if(cnt_gb.equals("ind")) { // 비중만 평균있음
					// 평균
					XSSFCellStyle avgStyle = (XSSFCellStyle) workbook.createCellStyle();
					Font avgFont = avgStyle.getFont();
					avgFont.setFontHeight((short) 200);
					avgFont.setBold(true);
					avgStyle.setBorderRight(BorderStyle.THIN);
					avgStyle.setBorderLeft(BorderStyle.THIN);
					avgStyle.setBorderTop(BorderStyle.THIN);
					avgStyle.setBorderBottom(BorderStyle.THIN);
					avgStyle.setVerticalAlignment(VerticalAlignment.CENTER);
					//avgStyle.setAlignment(HorizontalAlignment.CENTER);
					avgStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(245, 250, 254))); 
					avgStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					
					sheet.addMergedRegionUnsafe(new CellRangeAddress(4, 4, 0, cellMerge));
					
					int avgIndex = 0;
					SumMainVO avgVo = (SumMainVO) selectReportDataList.get(1);
					
					SXSSFRow avgRow = sheet.createRow(rowNo++);
					SXSSFCell avgCell = avgRow.createCell(avgIndex++);
					avgCell.setCellStyle(avgStyle);
					avgCell.setCellValue("평균");
					
					for(int i = 0; i < cellMerge; i++) {
						avgCell = avgRow.createCell(avgIndex++);
						avgCell.setCellStyle(avgStyle);
					}
					
					avgCell = avgRow.createCell(avgIndex++);
					avgCell.setCellStyle(avgStyle);
					avgCell.setCellValue(avgVo.getRms_info_r());
					
					avgCell = avgRow.createCell(avgIndex++);
					avgCell.setCellStyle(avgStyle);
					avgCell.setCellValue(avgVo.getRms_card_r());
					
					avgCell = avgRow.createCell(avgIndex++);
					avgCell.setCellStyle(avgStyle);
					avgCell.setCellValue(avgVo.getRms_tran_r());
					
					avgCell = avgRow.createCell(avgIndex++);
					avgCell.setCellStyle(avgStyle);
					avgCell.setCellValue(avgVo.getRms_arre_r());
					
					avgCell = avgRow.createCell(avgIndex++);
					avgCell.setCellStyle(avgStyle);
					avgCell.setCellValue(avgVo.getRms_pay_r());
					
					avgCell = avgRow.createCell(avgIndex++);
					avgCell.setCellStyle(avgStyle);
					avgCell.setCellValue(avgVo.getRms_summ_r());
					
					avgCell = avgRow.createCell(avgIndex++);
					avgCell.setCellStyle(avgStyle);
					avgCell.setCellValue(avgVo.getRms_stmng_r());
				}
			}
			
			// 셀스타일 생성(상하좌우 테두리)
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			Font font = style.getFont();
			font.setFontHeight((short) 200);
			font.setBold(false);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			// 셀에 데이터를 넣는 부분
			for (int i = 0; i < selectReportDataList.size(); i++) {
				SumMainVO vo = (SumMainVO) selectReportDataList.get(i);
				
				if((brch_gb.equals("LC") && vo.getZlevel().equals("3"))
						|| (brch_gb.equals("BL") && vo.getZlevel().equals("2"))) {
					// 1열 작성
					SXSSFRow row = sheet.createRow(rowNo++);
					
					int index = 0;
					
					SXSSFCell cell = row.createCell(index++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getZsub_rank());
					
					cell = row.createCell(index++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getZregion_nm());
					
					cell = row.createCell(index++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getZdept_nm());
					
					cell = row.createCell(index++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getZdept_emp_nm());
					
					if(brch_gb.equals("LC")) {
						cell = row.createCell(index++);
						cell.setCellStyle(style);
						cell.setCellValue(vo.getZdept_brch_nm());
						
						cell = row.createCell(index++);
						cell.setCellStyle(style);
						cell.setCellValue(vo.getZbrch_emp_nm());
					}
					
					cell = row.createCell(index++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getRms_info_r());
					
					cell = row.createCell(index++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getRms_card_r());
					
					cell = row.createCell(index++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getRms_tran_r());
					
					cell = row.createCell(index++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getRms_arre_r());
					
					cell = row.createCell(index++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getRms_pay_r());
					
					cell = row.createCell(index++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getRms_summ_r());
					
					cell = row.createCell(index++);
					cell.setCellStyle(style);
					cell.setCellValue(vo.getRms_stmng_r());
				}
			}
			
			insertExcelLog(session, selectReportDataList.size(), request.getServletPath());
			
			// 엑셀 다운
			excelUtil.runToExcel(workbook, passwd);
			
		} catch (IOException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );  
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );	
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
			
		} catch(Exception e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );			
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
		}
		
	}

	@Override
	public void excelTrsDup_d(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			HttpSession session = request.getSession();
			
			String zregion = request.getParameter("zregion");
			String zdept_id = request.getParameter("zdept_id");
			String zdept_brch_id = request.getParameter("zdept_brch_id");
			String zemp_id = request.getParameter("zemp_id");
			String zreq_ym_b = request.getParameter("zreq_ym_b");
			String zreq_ym_a = request.getParameter("zreq_ym_a");
			String passwd = request.getParameter("passwd");
			String active_bcg_id = request.getParameter("active_bcg_id");
			
			String session_zregion = (String) session.getAttribute("zregion");
			String session_zdept_id = (String) session.getAttribute("zdept_id");
			String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
			String session_zemp_id = (String) session.getAttribute("zemp_id");
			String session_zauth_grp = (String) session.getAttribute("zauth_grp");
			String session_zregion_gb = (String) session.getAttribute("zregion_gb");
			
			HashMap paramMap = new HashMap();
			paramMap.put("zreq_ym_b", zreq_ym_b);
			paramMap.put("zreq_ym_a", zreq_ym_a);
			paramMap.put("passwd", passwd);
			paramMap.put("active_bcg_id", active_bcg_id);
			paramMap.put("session_zregion", session_zregion);
			paramMap.put("session_zdept_id", session_zdept_id);
			paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
			paramMap.put("session_zemp_id", session_zemp_id);
			paramMap.put("session_zauth_grp", session_zauth_grp);
			paramMap.put("session_zregion_gb", session_zregion_gb);
			
			// 데이터 조회
			List selectReportDataList = exlMainMapper.excelTrsDup_d(paramMap);
			
			ExcelUtil excelUtil = ExcelUtil.getInstance();
			excelUtil.setRequest(request);
			excelUtil.setResponse(response);
			excelUtil.setFilename("[현장용]자동이체중복_리스트");
			excelUtil.setTitle("엑셀다운로드일시 : " + getDownloadDate());
			excelUtil.setTitleindex(2);
			
			SXSSFWorkbook workbook = excelUtil.getSxssfWorkbook("tplTrsDup_d.xlsx");
			workbook.setCompressTempFiles(true);
			
			// 첫번째 시트 생성
			SXSSFSheet sheet = workbook.getSheetAt(0);
			
			// 템플릿을 제외한 마지막 ROWNUM을 읽는다(해당 ROW부터 쓰기위해서)
			int rowNo = 4;
			
			// 셀스타일 생성(상하좌우 테두리)
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			Font font = style.getFont();
			font.setFontHeight((short) 200);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			// 셀에 데이터를 넣는 부분
			for (int i = 0; i < selectReportDataList.size(); i++) {
				// 1열 작성
				SXSSFRow row = sheet.createRow(rowNo++);
				
				int index = 0;
				
				ExlTrsDupVO vo = (ExlTrsDupVO) selectReportDataList.get(i);
				
				SXSSFCell cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZrms_ym());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZregion_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_brch_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_team_empnm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_team_empid());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZemp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZemp_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZmbr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getKunnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrade_cde());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getMatnr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getMatnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZbank_credc_gb());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZtrs_acct_dt());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZtrs_rcpt_amt());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZbank_credc_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZtrs_card_bk_no());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZtrs_rsd_nm());
				
			}
			
			insertExcelLog(session, selectReportDataList.size(), request.getServletPath());
			
			// 엑셀 다운
			excelUtil.runToExcel(workbook, passwd);
			
		} catch (IOException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );  
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );	
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
			
		} catch(Exception e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );			
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
		}
		
	}

	@Override
	public void excelTrsDup_da(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			HttpSession session = request.getSession();
			
			String zregion = request.getParameter("zregion");
			String zdept_id = request.getParameter("zdept_id");
			String zdept_brch_id = request.getParameter("zdept_brch_id");
			String zemp_id = request.getParameter("zemp_id");
			String zreq_ym_b = request.getParameter("zreq_ym_b");
			String zreq_ym_a = request.getParameter("zreq_ym_a");
			String passwd = request.getParameter("passwd");
			String active_bcg_id = request.getParameter("active_bcg_id");
			
			String session_zregion = (String) session.getAttribute("zregion");
			String session_zdept_id = (String) session.getAttribute("zdept_id");
			String session_zdept_brch_id = (String) session.getAttribute("zdept_brch_id");
			String session_zemp_id = (String) session.getAttribute("zemp_id");
			String session_zauth_grp = (String) session.getAttribute("zauth_grp");
			String session_zregion_gb = (String) session.getAttribute("zregion_gb");
			
			HashMap paramMap = new HashMap();
			paramMap.put("zreq_ym_b", zreq_ym_b);
			paramMap.put("zreq_ym_a", zreq_ym_a);
			paramMap.put("passwd", passwd);
			paramMap.put("active_bcg_id", active_bcg_id);
			paramMap.put("session_zregion", session_zregion);
			paramMap.put("session_zdept_id", session_zdept_id);
			paramMap.put("session_zdept_brch_id", session_zdept_brch_id);
			paramMap.put("session_zemp_id", session_zemp_id);
			paramMap.put("session_zauth_grp", session_zauth_grp);
			paramMap.put("session_zregion_gb", session_zregion_gb);
			
			// 데이터 조회
			List selectReportDataList = exlMainMapper.excelTrsDup_da(paramMap);
			
			ExcelUtil excelUtil = ExcelUtil.getInstance();
			excelUtil.setRequest(request);
			excelUtil.setResponse(response);
			excelUtil.setFilename("[관리용]자동이체중복_리스트");
			excelUtil.setTitle("엑셀다운로드일시 : " + getDownloadDate());
			excelUtil.setTitleindex(2);
			
			SXSSFWorkbook workbook = excelUtil.getSxssfWorkbook("tplTrsDup_da.xlsx");
			workbook.setCompressTempFiles(true);
			
			// 첫번째 시트 생성
			SXSSFSheet sheet = workbook.getSheetAt(0);
			
			// 템플릿을 제외한 마지막 ROWNUM을 읽는다(해당 ROW부터 쓰기위해서)
			int rowNo = 4;
			
			// 셀스타일 생성(상하좌우 테두리)
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			Font font = style.getFont();
			font.setFontHeight((short) 200);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			// 셀에 데이터를 넣는 부분
			for (int i = 0; i < selectReportDataList.size(); i++) {
				// 1열 작성
				SXSSFRow row = sheet.createRow(rowNo++);
				
				int index = 0;
				
				ExlTrsDupVO vo = (ExlTrsDupVO) selectReportDataList.get(i);
				
				SXSSFCell cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZrms_ym());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZregion_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_brch_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_team_empnm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZdept_team_empid());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZemp_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZemp_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZmbr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getKunnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZgrade_cde());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getMatnr_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getMatnr());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZbank_credc_gb());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZrcpt_dt_schd());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZtrs_acct_dt());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZtrs_rcpt_amt());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZbank_credc_id());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZtrs_card_bk_no());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZtrs_rsd_nm());
				
				cell = row.createCell(index++);
				cell.setCellStyle(style);
				cell.setCellValue(vo.getZbiz_gb());
				
			}
			
			insertExcelLog(session, selectReportDataList.size(), request.getServletPath());
			
			// 엑셀 다운
			excelUtil.runToExcel(workbook, passwd);
			
		} catch (IOException e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );  
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );	
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
			
		} catch(Exception e) {
			logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e );
			
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch (IOException e1) {
				logger.debug("[NullPointerException] " + request.getServletPath() + " - " + e1 );
			} catch(Exception ignore) {
				logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
			} finally {
				if(out != null) try { out.close(); } 
				catch (IOException e1) {
					logger.debug("[Exception e] " + request.getServletPath() + " - " + e1 );			
				} catch(Exception ignore) {
					logger.debug("[Exception] " + request.getServletPath() + " - " + ignore );
				}
			}
		}
		
	}
}
