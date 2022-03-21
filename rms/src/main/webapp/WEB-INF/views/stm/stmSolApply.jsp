<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<%@ include file="../taglibs.jsp"%>

<script>
	var active_bcg_id = "${active_bcg_id}";
	var param_zregion = "${param_zregion}";
	var param_zdept_id = "${param_zdept_id}";
	var param_zdept_brch_id = "${param_zdept_brch_id}";
	var param_zemp_id = "${param_zemp_id}";
	var param_zreq_ym = "${param_zreq_ym}";
	
	$(document).ready(function(e) {
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
			url : "${ctx}/arr/getArrPayment.do",
			contentType : 'application/json',
			data : JSON.stringify(obj),
			success : function(result) {
				// 하단 데이터 기준일 보이게하기
				$("#data-dt").css("visibility", "visible");
				$("#data-dt-sum").html(yyyymm.substring(0, 4) + "-" + yyyymm.substring(4, 6));
				
				$("#crd-main-tbody").empty();

				var html = "";
				
				if(result.length == 0){
		        	html += '<tr>';
		        	html += '<td colspan="10" style="font-size:14px;">조회결과가 없습니다.</td>';
		        	html += '</tr>';

		        	$(".page_nation").css("display", "none");
			    } else {
				    // 페이지를 셋팅한다
				    setPage(result[0].totalcnt);
				    
					for ( var i in result) {
						html += "<tr>";
						html += "<td>" + result[i].rnum + "</td>";
						html += "<td>" + result[i].zregion_nm + "</td>";
						html += "<td>" + result[i].zdept_nm + "</td>";
						if(isEmpty(result[i].zlc_nm)) {
							html += "<td>" + result[i].zdept_brch_id + "</td>";
						} else {
							html += "<td>" + result[i].zdept_brch_id + "<br/>[" + result[i].zlc_nm + "]</td>";
						}
						html += "<td>" + result[i].zemp_nm + "<br/>[" + result[i].zemp_id + "]</td>";
						html += "<td>" + result[i].zmbr_nm + "<br/>[" + result[i].kunnr + "]</td>";
						html += "<td>" + result[i].zgrade_cde + "</td>";
						html += "<td>" + result[i].matnr + "<br/>[" + result[i].matnr_nm + "]</td>";
						html += "<td>" + numberWithCommas(result[i].zrcpt_amt) + "</td>";
						html += "<td>" + changeDateToView2(result[i].zlastfee_ym) + "</td>";
						html += "</tr>";
					}
			    }
				
				$("#crd-main-tbody").append(html);

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
			}, 
			complete:function(){
				closeLoading();
		    }
		});
	}
</script>
<h2>2개월 체납</h2>
<div class="filter-area">
	<div class="left">
		<%@ include file="../com/calendarEx.jsp"%>
		<!-- 조직선택 input -->
		<%@ include file="../com/group.jsp"%>
		<button class="btn-tb-top search" onclick="btnSearch(1);">검색</button>
	</div>
	<div class="right">
		<button class="btn-tb-top excel-1" onclick="openExcelPopup(zregion, zdept, zdept_brch, zemp, yyyymm, active_bcg_id, 'excelArrPayment_d.do');">엑셀다운</button>
		<c:if test="${zregion_gb eq 'A'}">
			<button class="btn-tb-top excel-2" onclick="openExcelPopup(zregion, zdept, zdept_brch, zemp, yyyymm, active_bcg_id, 'excelArrPayment_da.do');">전사 엑셀다운</button>
		</c:if>
	</div>
</div>

<div class="tbl">
	<table>
		<colgroup>
			<col style="width:6%;">
			<col style="width:10%;">
			<col style="width:10%;">
			<col style="width:10%;">
			<col style="width:12%;">
			<col style="width:12%;">
			<col style="width:8%;">
			<col style="width:12%;">							
			<col style="width:8%;">
			<col style="width:12%;">
		</colgroup>
		<thead>
			<tr>
				<th>No.</th>
				<th>본부</th>
				<th>사업국</th>
				<th>팀</th>
				<th>교사</th>
				<th>회원</th>
				<th>학년</th>
				<th>코드</th>
				<th>체납액</th>
				<th>완납년월</th>
			</tr>
		</thead>
		<tbody id="crd-main-tbody">
			<tr>
				<td colspan="10" style="font-size:14px;">조회결과가 없습니다.</td>
			</tr>
		</tbody>
	</table>
	<%@ include file="../com/page.jsp"%>
	
</div>
<div class="left-info" id="data-dt" style="visibility: hidden;">* Data 기준 : <span id="data-dt-sum"></span></div>