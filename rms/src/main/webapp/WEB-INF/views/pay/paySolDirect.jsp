<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<%@ include file="../taglibs.jsp"%>
<%--
	[솔루니] 직접결제 중복발송
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
		setActiveMenu("menu3", "menu3-5");
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
			url : "${ctx}/pay/getDirectPayment.do",
			contentType : 'application/json',
			data : JSON.stringify(obj),
			success : function(result) {
				// 하단 데이터 기준일 보이게하기
				$("#data-dt").css("visibility", "visible");
				$("#data-dt-sum").html(before_yyyymm.substring(0, 4) + "-" + before_yyyymm.substring(4, 6) + " ~ " + after_yyyymm.substring(0, 4) + "-" + after_yyyymm.substring(4, 6));
				
				$("#pay-main-tbody").empty();

				var html = "";
				
				if(result.length == 0){
		        	html += '<tr>';
		        	html += '<td colspan="17" style="font-size:14px;">조회결과가 없습니다.</td>';
		        	html += '</tr>';

		        	$(".page_nation").css("display", "none");
			    } else {
				    // 페이지를 셋팅한다
				    setPage(result[0].totalcnt);
				    
					for ( var i in result) {
						if(result[i].zrms_ym == (result[i].zsms_send_dt).substring(0, 6)) {
							html += "<tr class='now-row'>";
							html += "<td>" + result[i].rnum + "</td>";
							html += "<td>" + changeDateToView2(result[i].zrms_ym) + "</td>";
							html += "<td>" + result[i].zregion_nm + "</td>";
							html += "<td>" + result[i].zdept_nm + "</td>";
							if(isEmpty(result[i].zlc_nm)) {
								html += "<td>" + result[i].zdept_brch_id + "</td>";
							} else {
								html += "<td>" + result[i].zdept_brch_id + "<br/>[" + result[i].zlc_nm + "]</td>";
							}
							html += "<td>" + result[i].zemp_nm + "<br/>[" + changeEmpIdToKunnr(result[i].zemp_id) + "]</td>";
							html += "<td>" + result[i].zmbr_nm + "<br/>[" + result[i].kunnr + "]</td>";
							html += "<td>" + result[i].zreq_gb + "</td>";
							html += "<td class='now-year'>" + changeDateToView(result[i].zsms_send_dt) + "</td>";
							html += "<td>" + result[i].zrec_name + "</td>";
							html += "<td>" + result[i].zrec_conn + "</td>";
							if('${zregion_gb}' == 'A') {
								html += "<td >" + result[i].zrec_tel + "</td>";
							} else {
								html += "<td>" + result[i].zrec_tel + "</td>";
							}
							html += "<td>" + result[i].zappr_type + "</td>";
							html += "<td>" + result[i].zappr_no + "</td>";
							html += "<td>" + changeDateToView(result[i].zappr_dt) + "</td>";
							html += "<td>" + result[i].zappr_state + "</td>";
							html += "<td>" + numberWithCommas(result[i].zappr_amt) + "</td>";
							html += "</tr>";
						} else {
							html += "<tr>";
							html += "<td>" + result[i].rnum + "</td>";
							html += "<td>" + changeDateToView2(result[i].zrms_ym) + "</td>";
							html += "<td>" + result[i].zregion_nm + "</td>";
							html += "<td>" + result[i].zdept_nm + "</td>";
							if(isEmpty(result[i].zlc_nm)) {
								html += "<td>" + result[i].zdept_brch_id + "</td>";
							} else {
								html += "<td>" + result[i].zdept_brch_id + "<br/>[" + result[i].zlc_nm + "]</td>";
							}
							html += "<td>" + result[i].zemp_nm + "<br/>[" + changeEmpIdToKunnr(result[i].zemp_id) + "]</td>";
							html += "<td>" + result[i].zmbr_nm + "<br/>[" + result[i].kunnr + "]</td>";
							html += "<td>" + result[i].zreq_gb + "</td>";
							html += "<td>" + changeDateToView(result[i].zsms_send_dt) + "</td>";
							html += "<td>" + result[i].zrec_name + "</td>";
							html += "<td>" + result[i].zrec_conn + "</td>";
							if('${zregion_gb}' == 'A') {
								html += "<td >" + result[i].zrec_tel + "</td>";
							} else {
								html += "<td>" + result[i].zrec_tel + "</td>";
							}
							html += "<td>" + result[i].zappr_type + "</td>";
							html += "<td>" + result[i].zappr_no + "</td>";
							html += "<td>" + changeDateToView(result[i].zappr_dt) + "</td>";
							html += "<td>" + result[i].zappr_state + "</td>";
							html += "<td>" + numberWithCommas(result[i].zappr_amt) + "</td>";
							html += "</tr>";
						}
					}
			    }
				
				$("#pay-main-tbody").append(html);

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
<h2>고객직접결제 중복발송 <button class="btn-menual" id="menual" onClick="openMenualPopup();">주요지표 정의</button></h2>
<div class="filter-area">
	<div class="left">
		<%@ include file="../com/calendarEx.jsp"%>
		<!-- 조직선택 input -->
		<%@ include file="../com/group.jsp"%>
		<button class="btn-tb-top search" onclick="btnSearch(1);">검색</button>
	</div>
	<div class="right">
		<button class="btn-tb-top excel-1" onclick="openExcelPopup(zregion, zdept, zdept_brch, zemp, before_yyyymm, after_yyyymm, active_bcg_id, 'excelPayDirect_d.do', '[현장용]직접결제중복발송_리스트.xlsx');">엑셀다운</button>
		<c:if test="${zregion_gb eq 'A'}">
			<button class="btn-tb-top excel-2" onclick="openExcelPopup(zregion, zdept, zdept_brch, zemp, before_yyyymm, after_yyyymm, active_bcg_id, 'excelPayDirect_da.do', '[관리용]직접결제중복발송_리스트.xlsx');">전사 엑셀다운</button>
		</c:if>
	</div>
</div>

<div class="tbl">
	<table>
		<colgroup>
			<col style="width: 2%;"> <!-- No. -->
			<col style="width: 5%;"> <!-- 기준년월 -->
			<col style="width: 5%;"> <!-- 본부 -->
			<col style="width: 7%;"> <!-- 사업국 -->
			<col style="width: 5%;"> <!-- 팀 -->
			<col style="width: 8%;"> <!-- 교사 -->
			<col style="width: 8%;"> <!-- 회원 -->
			<col style="width: 4%;"> <!-- 발송구분 -->
			<col style="width: 7%;"> <!-- 발송일자 -->
			<col style="width: 5%;"> <!-- 수신인명 -->
			<col style="width: 5%;"> <!-- 수신인관계 -->
			<col style="width: 8%;"> <!-- 수신연락처 -->
			<col style="width: 5%;"> <!-- 결제구분 -->
			<col style="width: 7%;"> <!-- 승인번호 -->
			<col style="width: 7%;"> <!-- 결제일자 -->
			<col style="width: 6%;"> <!-- 결제완료여부 -->
			<col style="width: auto;"> <!-- 결제금액 -->
		</colgroup>
		<thead>
			<tr>
				<th>No.</th>
				<th>기준<br>년월</th>
				<th>본부</th>
				<th>교육국</th> <%-- "사업국" -> "교육국" 수정 2021.07.29 --%>
				<th>팀</th>
				<th>교사</th>
				<th>회원</th>
				<th>발송<br>구분</th>
				<th>발송<br>일자</th>
				<th>수신인<br>명</th>
				<th>수신인<br>관계</th>
				<th>수신<br>연락처</th>
				<th>결제<br>구분</th>
				<th>승인<br>번호</th>
				<th>결제<br>일자</th>
				<th>결제<br>완료여부</th>
				<th>결제<br>금액</th>
			</tr>
		</thead>
		<tbody id="pay-main-tbody">
			<tr>
				<td colspan="17" style="font-size:14px;">조회결과가 없습니다.</td>
			</tr>
		</tbody>
	</table>
	<%@ include file="../com/page.jsp"%>
	
</div>
<div class="left-info" id="data-dt" style="visibility: hidden;">* Data 기준 : <span id="data-dt-sum"></span></div>