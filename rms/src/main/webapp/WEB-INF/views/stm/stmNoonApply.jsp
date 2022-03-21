<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<%@ include file="../taglibs.jsp"%>
<%--
	[눈높이] 긴급교재
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
		setActiveMenu("menu3", "menu3-7");
		$('#menu3-sub').slideDown(300);
	});

	$('.tooltip').hover(function(){
		document.onmousemove = function(e){
    		var divLeft = e.pageX + 10;
    		var divTop = e.pageY + 5;
    		$(".tootip-box").css("top", divTop);
    		$(".tootip-box").css("left", divLeft);
			$(".tootip-box").css("visibility", "visible");
		}
    }, function() {
    	document.onmousemove = function(e){
    		$(".tootip-box").css("visibility", "hidden");
    	    //alert(e.pageX + "\n" + e.pageY);
    	}
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
			url : "${ctx}/stm/getStmApply.do",
			contentType : 'application/json',
			data : JSON.stringify(obj),
			success : function(result) {
				// 하단 데이터 기준일 보이게하기
				$("#data-dt").css("visibility", "visible");
				$("#data-dt-sum").html(before_yyyymm.substring(0, 4) + "-" + before_yyyymm.substring(4, 6) + " ~ " + after_yyyymm.substring(0, 4) + "-" + after_yyyymm.substring(4, 6));
				
				$("#stm-main-tbody").empty();

				var html = "";
				
				if(result.length == 0){
		        	html += '<tr>';
		        	html += '<td colspan="18" style="font-size:14px;">조회결과가 없습니다.</td>';
		        	html += '</tr>';

		        	$(".page_nation").css("display", "none");
			    } else {
				    // 페이지를 셋팅한다
				    setPage(result[0].totalcnt);
				    
					for ( var i in result) {
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
						html += "<td>" + result[i].zreq_cnt + "</td>";
						html += "<td>" + result[i].zreq_vol + "</td>";
						html += "<td>" + result[i].zmon_cnt_p + "%</td>";
						html += "<td>" + result[i].zreq_non_vol + "</td>";
						html += "<td>" + result[i].zgrund_01_c + " <br>[ " + result[i].zgrund_01 + "% ]</td>";
						html += "<td>" + result[i].zgrund_04_c + " <br>[ " + result[i].zgrund_04 + "% ]</td>";
						html += "<td>" + result[i].zgrund_06_c + " <br>[ " + result[i].zgrund_06 + "% ]</td>";
						html += "<td>" + result[i].zgrund_03_c + " <br>[ " + result[i].zgrund_03 + "% ]</td>";
						html += "<td>" + result[i].zgrund_05_c + " <br>[ " + result[i].zgrund_05 + "% ]</td>";
						html += "<td>" + result[i].zgrund_07_c + " <br>[ " + result[i].zgrund_07 + "% ]</td>";
						html += "<td>" + result[i].zgrund_etc_c + " <br>[ " + result[i].zgrund_etc + "% ]</td>";
						html += "</tr>";
					}
			    }
				
				$("#stm-main-tbody").append(html);

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
<h2>긴급교재 신청 <button class="btn-menual" id="menual" onClick="openMenualPopup();">주요지표 정의</button></h2>
<div class="filter-area">
	<div class="left">
		<%@ include file="../com/calendarEx.jsp"%>
		<!-- 조직선택 input -->
		<%@ include file="../com/group.jsp"%>
		<button class="btn-tb-top search" onclick="btnSearch(1);">검색</button>
	</div>
	<div class="right">
		<button class="btn-tb-top excel-1" onclick="openExcelPopup(zregion, zdept, zdept_brch, zemp, before_yyyymm, after_yyyymm, active_bcg_id, 'excelStmApply_d.do', '[현장용]긴급교재_리스트.xlsx');">엑셀다운</button>
		<c:if test="${zregion_gb eq 'A'}">
			<button class="btn-tb-top excel-2" onclick="openExcelPopup(zregion, zdept, zdept_brch, zemp, before_yyyymm, after_yyyymm, active_bcg_id, 'excelStmApply_da.do', '[관리용]긴급교재_리스트.xlsx');">전사 엑셀다운</button>
		</c:if>
	</div>
</div>

<div class="tootip-box" style="text-align: left"> · 전입<br> · 물류배송오류<br> · 교실분리·인수인계<br> · SP교재신청<br> · 기타(파손,분실 등)<br> · 교육용<br> · MCM<br> · 해외이민</div>

<div class="tbl">
	<table>
		<colgroup>
			<col style="width:3%;">
			<col style="width:6%;">
			<col style="width:6%;">
			<col style="width:7%;">
			<col style="width:auto;">
			<col style="width:8%;">
			<col style="width:4%;">
			<col style="width:4%;">							
			<col style="width:6%;">
			<col style="width:6%;">
			<col style="width:6%;">
			<col style="width:6%;">
			<col style="width:6%;">
			<col style="width:6%;">
			<col style="width:6%;">
			<col style="width:6%;">
			<col style="width:6%;">
		</colgroup>
		<thead>
			<tr>
				<th rowspan="2">No.</th>
				<th rowspan="2">기준<br>년월</th>
				<th rowspan="2">본부</th>
				<th rowspan="2">교육국</th> <%-- "사업국" -> "교육국" 수정 2021.07.29 --%>
				<th rowspan="2">팀</th>
				<th rowspan="2">교사</th>
				<th rowspan="2">신청<br>건수</th>
				<th rowspan="2">신청<br>권수</th>
				<th rowspan="2">신청권수<br>비중<br>(총원)</th>
				<th rowspan="2">미지정<br>회원<br>신청권수</th>
				<th colspan="7">사유별 신청권수 및 비중</th>

			</tr>
			<tr>
				<th rowspan="1">신입<br>(복회)</th>
				<th rowspan="1">진도수정<br>(오류)</th>
				<th rowspan="1">교재추가<br>불출</th>
				<th rowspan="1">전환</th>
				<th rowspan="1">입회<br>유도샘플</th>
				<th rowspan="1">진도<br>미입력</th>
				<th class="tooltip" rowspan="1">기타</th>
			</tr>
		</thead>
		<tbody id="stm-main-tbody">
			<tr>
				<td colspan="18" style="font-size:14px;">조회결과가 없습니다.</td>
			</tr>
		</tbody>
	</table>
	<%@ include file="../com/page.jsp"%>
	
</div>
<div class="left-info" id="data-dt" style="visibility: hidden;">* Data 기준 : <span id="data-dt-sum"></span></div>