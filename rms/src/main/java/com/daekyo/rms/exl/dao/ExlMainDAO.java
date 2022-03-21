package com.daekyo.rms.exl.dao;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ExlMainDAO {
	// 로그 입력
	public int insertExcelLog(HashMap paramMap);

	// [현장용] 주요지표 엑셀다운 / 비중
	public List excelMoniSum_d(HashMap paramMap);

	// [현장용] 주요지표 엑셀다운 / 건수
	public List excelMoniSum_d_cnt(HashMap paramMap);

	// LC, BL용 주요지표 엑셀다운 / 비중
	public List excelMoniSum_ex_d(HashMap paramMap);

	// LC, BL용 주요지표 엑셀다운 / 건수
	public List excelMoniSum_ex_d_cnt(HashMap paramMap);

	// [현장용] 카드결제 현황 엑셀다운
	public List excelCardPayment_d(HashMap paramMap);

	// [관리용] 카드결제 현황 엑셀다운
	public List excelCardPayment_da(HashMap paramMap);

	// [현장용] 이체 제외 후 학습 엑셀다운
	public List excelTranStudy_d(HashMap paramMap);

	// [관리용] 이체 제외 후 학습 엑셀다운
	public List excelTranStudy_da(HashMap paramMap);

	// [현장용] 개인정보 인증 엑셀다운
	public List excelIndAuth_d(HashMap paramMap);

	// [관리용] 개인정보 인증 엑셀다운
	public List excelIndAuth_da(HashMap paramMap);

	// [현장용] 2개월 체납 엑셀다운
	public List excelArrPayment_d(HashMap paramMap);

	// [관리용] 2개월 체납 엑셀다운
	public List excelArrPayment_da(HashMap paramMap);

	// [현장용] 직접결제 중복발송 엑셀다운
	public List excelPayDirect_d(HashMap paramMap);

	// [관리용] 직접결제 중복발송 엑셀다운
	public List excelPayDirect_da(HashMap paramMap);

	// [현장용] 긴급교재 엑셀다운
	public List excelStmApply_d(HashMap paramMap);

	// [관리용] 긴급교재 엑셀다운
	public List excelStmApply_da(HashMap paramMap);

	// [현장용] 써밋제품 학습 엑셀다운
	public List excelSmiRisk_d(HashMap paramMap);

	// [현장용] 월별 모니터링 엑셀다운-비중
	public List excelMonMoni_d(HashMap paramMap);

	// [현장용] 월별 모니터링 엑셀다운-대상건수
	public List excelMonMoni_d_cnt(HashMap paramMap);

	// [현장용] 0개월학습 엑셀다운
	public List excelNstZeroStudy_d(HashMap paramMap);

	// [관리용] 0개월학습 엑셀다운
	public List excelNstZeroStudy_da(HashMap paramMap);

	// 주요지표 랭킹 엑셀다운-비중
	public List excelSumMainRank_cnt(HashMap paramMap);

	// 주요지표 랭킹 엑셀다운-비중
	public List excelSumMainRank_ind(HashMap paramMap);
	
	// [현장용] 자동이체중복 엑셀다운
	public List excelTrsDup_d(HashMap paramMap);
		
	// [관리용] 자동이체중복 엑셀다운
	public List excelTrsDup_da(HashMap paramMap);

}