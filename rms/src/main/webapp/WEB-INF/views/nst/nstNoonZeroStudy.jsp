<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<%@ include file="../taglibs.jsp"%>
<%--
	[눈높이] 0개월 학습
 --%>
<script>
	var active_bcg_id = "${active_bcg_id}";
	var param_zregion = "${param_zregion}";
	var param_zdept_id = "${param_zdept_id}";
	var param_zdept_brch_id = "${param_zdept_brch_id}";
	var param_zemp_id = "${param_zemp_id}";
	var param_zreq_ym = "${param_zreq_ym}";
	var param_search_gb = "${param_search_gb}";

	var calendarEx = "F";
	
	$(document).ready(function(e) {
		setActiveMenu("menu3", "menu3-8");
		$('#menu3-sub').slideDown(300);
	});

	<%--=============================================================
	조회
	page : 조회할 페이지
	=============================================================--%>
	function btnSearch(page) {
		setPageInit(page);
		
		var obj = new Object();
		obj.zregion = zregion;
		obj.zdept_id = zdept;
		obj.zdept_brch_id = zdept_brch;
		obj.zemp_id = zemp;
		obj.zreq_ym_b = before_yyyymm;
		obj.zreq_ym_a = after_yyyymm;
		obj.active_bcg_id = active_bcg_id;
		obj.page = String(page);

		$.ajax({
			type : "POST",
			dataType : "json",
			url : "${ctx}/nst/getZeroStudy.do",
			contentType : 'application/json',
			data : JSON.stringify(obj),
			success : function(result) {
				// 하단 데이터 기준일 보이게하기
				$("#data-dt").css("visibility", "visible");
				$("#data-dt-sum").html(before_yyyymm.substring(0, 4) + "-" + before_yyyymm.substring(4, 6) + " ~ " + after_yyyymm.substring(0, 4) + "-" + after_yyyymm.substring(4, 6));
				
				$("#nst-main-tbody").empty();

				var html = "";
				
				if(result.length == 0){
		        	html += '<tr>';
		        	html += '<td colspan="16" style="font-size:14px;">조회결과가 없습니다.</td>';
		        	html += '</tr>';

		        	$(".page_nation").css("display", "none");
			    } else {
				    // 페이지를 셋팅한다
				    setPage(result[0].totalcnt);
				    
					for ( var i in result) {
						html += "<tr>";
						html += "<td>" + result[i].rnum + "</td>";
						html += "<td>" + changeDateToView2(result[i].zrms_ym) + "</td>";
						// 퇴회
						html += "<td>" + result[i].out_zregion_nm + "</td>";
						html += "<td>" + result[i].out_zdept_nm + "</td>";
						if(isEmpty(result[i].out_lc_nm)) {
							html += "<td>" + result[i].out_zdept_brch_id + "</td>";
						} else {
							html += "<td>" + result[i].out_zdept_brch_id + "<br/>[" + result[i].out_lc_nm + "]</td>";
						}
						html += "<td>" + result[i].out_zemp_nm + "<br/>[" + changeEmpIdToKunnr(result[i].out_zemp_id) + "]</td>";
						
						// 입회
						html += "<td>" + result[i].zregion_nm + "</td>";
						html += "<td>" + result[i].zdept_nm + "</td>";
						if(isEmpty(result[i].in_lc_nm)) {
							html += "<td>" + result[i].zdept_brch_id + "</td>";
						} else {
							html += "<td>" + result[i].zdept_brch_id + "<br/>[" + result[i].in_lc_nm + "]</td>";
						}
						html += "<td>" + result[i].zemp_nm + "<br/>[" + changeEmpIdToKunnr(result[i].zemp_id) + "]</td>";
						
						html += "<td>" + result[i].zmbr_nm + "<br/>[" + result[i].kunnr + "]</td>";
						html += "<td>" + result[i].matnr + "<br/>[" + result[i].matnr_nm + "]</td>";
						html += "<td>" + result[i].zgrade_cde + "</td>";
						html += "<td>" + changeDateToView(result[i].out_post_dt) + "</td>";
						html += "<td>" + changeDateToView(result[i].in_post_dt) + "</td>";
						html += "<td>" + changeDateToView2(result[i].zlastfee_ym) + "</td>";
						html += "</tr>";
					}
			    }
				
				$("#nst-main-tbody").append(html);

				<%-- IE에서는 좌측메뉴가 container에 먹히는 현상이 발생하는데 수정이불가능해서 조회시 동적으로 변경 --%>
				$(".left-bg").css("height", $("#container").css("height"));
				$("#grpFilterBox").css("visibility", "hidden");

				closeLoading();
			},
			beforeSend:function(xmlHttpRequest){
				openLoading();
	        	xmlHttpRequest.setRequestHeader("AJAX","true");
	        },
			error : function(e) {
				if (e.status == "400") { // 세션만료 에러코드  
					location.href = "${ctx}/logout";
				}
				if (e.status == "401") { // 권한없음 에러코드  
					alert("권한이 없습니다. 관리자에게 문의바랍니다.");
					return;
				}
				closeLoading();
			}, 
			complete:function(){
				closeLoading();
		    }
		});
	}
</script>
<%@ include file="../com/menual.jsp"%>
<h2>0개월 학습 <button class="btn-menual" id="menual" onClick="openMenualPopup();">주요지표 정의</button></h2>
<div class="filter-area">
	<div class="left">
		<%@ include file="../com/calendarEx.jsp"%>
		<!-- 조직선택 input -->
		<%@ include file="../com/group.jsp"%>
		<button class="btn-tb-top search" onclick="btnSearch(1);">검색</button>
	</div>
	<div class="right">
		<button class="btn-tb-top excel-1" onclick="openExcelPopup(zregion, zdept, zdept_brch, zemp, before_yyyymm, after_yyyymm, active_bcg_id, 'excelNstZeroStudy_d.do', '[현장용]0개월학습_리스트.xlsx');">엑셀다운</button>
		<c:if test="${zregion_gb eq 'A'}">
			<button class="btn-tb-top excel-2" onclick="openExcelPopup(zregion, zdept, zdept_brch, zemp, before_yyyymm, after_yyyymm, active_bcg_id, 'excelNstZeroStudy_da.do', '[관리용]0개월학습_리스트.xlsx');">전사 엑셀다운</button>
		</c:if>
	</div>
</div>

<div class="tbl">
	<table>
		<colgroup>
			<col style="width:3%;">
			<col style="width:5%;">
			<col style="width:7%;">
			<col style="width:6%;">
			<col style="width:5%;">
			<col style="width:5%;">
			<col style="width:7%;">
			<col style="width:6%;">							
			<col style="width:5%;">
			<col style="width:5%;">
			<col style="width:5%;">
			<col style="width:10%;">
			<col style="width:5%;">
			<col style="width:8%;">
			<col style="width:8%;">
			<col style="width:7%;">
		</colgroup>
		<thead>
			<tr>
				<th rowspan="2">No.</th>
				<th rowspan="2">기준<br>년월</th>
				<th colspan="4">퇴회실적</th>
				<th colspan="4">입회실적</th>
				<th rowspan="2">회원</th>
				<th rowspan="2">과목</th> <%-- "코드" -> "과목" 수정 2021.12.28 --%>
				<th rowspan="2">학년</th>
				<th rowspan="2">퇴회일</th>
				<th rowspan="2">입회일</th>
				<th rowspan="2">완납<br>년월</th>

			</tr>
			<tr>
				<th rowspan="1">본부</th> 
				<th rowspan="1">교육국</th> 
				<th rowspan="1">팀</th>
				<th rowspan="1">교사</th>
				<th rowspan="1">본부</th> 
				<th rowspan="1">교육국</th> 
				<th rowspan="1">팀</th>
				<th rowspan="1">교사</th>
			</tr>
		</thead>
		<tbody id="nst-main-tbody">
			<tr>
				<td colspan="18" style="font-size:14px;">조회결과가 없습니다.</td>
			</tr>
		</tbody>
	</table>
	<%@ include file="../com/page.jsp"%>
	
</div>
<div class="left-info" id="data-dt" style="visibility: hidden;">* Data 기준 : <span id="data-dt-sum"></span></div>