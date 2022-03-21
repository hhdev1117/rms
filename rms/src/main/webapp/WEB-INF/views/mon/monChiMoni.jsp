<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<%@ include file="../taglibs.jsp"%>
<%--
	[차이홍] 월별 모니터링
 --%>
<script>
	var active_bcg_id = "${active_bcg_id}";
	var param_zregion = "${param_zregion}";
	var param_zdept_id = "${param_zdept_id}";
	var param_zdept_brch_id = "${param_zdept_brch_id}";
	var param_zemp_id = "${param_zemp_id}";
	var param_zreq_ym = "${param_zreq_ym}";
	var param_search_gb = "${search_gb}";

	var search_rms_ym = "";

	var calendarEx = "T";
	
	$(document).ready(function(e){
		setActiveMenu("menu2");
	});

	<%--=============================================================
	수치 클릭 시 화면이동 / 항목 추가 시 수정필요
	=============================================================--%>
	function setClickEvent(param_zregion, param_zdept_id, param_zdept_brch_id, param_zemp_id) {
		var node = "td.sum-item1, td.sum-item2, td.sum-item3, td.sum-item4, td.sum-item5, td.sum-item6, td.sum-item7";
		
		$(node).hover(function() {
			$(this).css("font-weight", "bold");
		}, function(){
				$(this).css("font-weight", "");
		});
		
		$(node).on("click", function () {
			var data = new Object();
			data.param_zregion 				= param_zregion;
			data.param_zdept_id 			= param_zdept_id;
			data.param_zdept_brch_id 		= param_zdept_brch_id;
			data.param_zemp_id 				= param_zemp_id;
			data.param_zreq_ym 				= $(this).attr("zrms_ym");
			
			var cls = $(this).attr("class");
			if(cls == "sum-item1") getContentTab("/ind/indChiAuth.do", data);
			else if(cls == "sum-item2") getContentTab("/crd/crdChiCard.do", data);
			else if(cls == "sum-item3") getContentTab("/trn/trnChiStd.do", data);
			else if(cls == "sum-item4") getContentTab("/arr/arrChiPay.do", data);
			else if(cls == "sum-item5") getContentTab("/pay/payChiDirect.do", data);
			else if(cls == "sum-item6") getContentTab("/pay/payChiDirect.do", data);
			else if(cls == "sum-item7") getContentTab("/stm/stmChiApply.do", data);
		});
	}
	
	function btnSearch() {
		if(isEmpty(zregion)) {
			alert("본부를 선택해주세요.");
			return;
		}

		$("#calBeforeFilterBox, #calAfterFilterBox, #sumOpenGrp, #sumOpenSearchFilter").css("visibility", "hidden");

		var cnt_gb = "ind";
		if($("input:checkbox[id='chkbx3']").prop("checked") == true) {
			cnt_gb = "ind";
		} else if($("input:checkbox[id='chkbx4']").prop("checked") == true) {
			cnt_gb = "cnt";
		} else {
			cnt_gb = "ind";
		}
		
		var obj = new Object();
		obj.zregion = zregion;
		obj.zdept_id = zdept;
		obj.zdept_brch_id = zdept_brch;
		obj.zemp_id = zemp;
		obj.zreq_ym_b = before_yyyymm;
		obj.zreq_ym_a = after_yyyymm;
		obj.active_bcg_id = active_bcg_id;
		obj.cnt_gb = cnt_gb;

		$.ajax({
			type : "POST",
			dataType : "json",
			url : "${ctx}/mon/getMonMoni.do",
			contentType : 'application/json',
			data : JSON.stringify(obj),
			success : function(result) {
				// 하단 데이터 기준일 보이게하기
				$("#data-dt").css("visibility", "visible");
				$("#data-dt-sum").html(before_yyyymm.substring(0, 4) + "-" + before_yyyymm.substring(4, 6) + " ~ " + after_yyyymm.substring(0, 4) + "-" + after_yyyymm.substring(4, 6));
				
				$("#mon-main-tbody").empty();

				var html = "";
				
				if(result.length == 0){
		        	html += '<tr>';
		        	html += '<td colspan="8" style="font-size:14px;">조회결과가 없습니다.</td>';
		        	html += '</tr>';

		        	$(".page_nation").css("display", "none");
			    } else {

			    	if(cnt_gb == "ind") {
				    	<%--평균--%>
						html += "<tr class='bg-grey'>";
						html += "<td>" + result[0].zrms_ym + "</td>";
						html += "<td>" + result[0].zitem1_p + "%</td>";
						html += "<td>" + result[0].zitem2_p + "%</td>";
						html += "<td>" + result[0].zitem3_p + "%</td>";
						html += "<td>" + result[0].zitem4_p + "%</td>";
						html += "<td>" + result[0].zitem5_p + "%</td>";
						html += "<td></td>";
						//html += "<td></td>";
						html += "</tr>";
					    
						for (var i = 1; i < result.length; i++) {
							html += "<tr>";
							html += "<td>" + changeDateToView2(result[i].zrms_ym) + "</td>";
							html += "<td class='sum-item1' zrms_ym='" + result[i].zrms_ym + "'>" + result[i].zitem1_p + "%</td>";
							html += "<td class='sum-item2' zrms_ym='" + result[i].zrms_ym + "'>" + result[i].zitem2_p + "%</td>";
							html += "<td class='sum-item3' zrms_ym='" + result[i].zrms_ym + "'>" + result[i].zitem3_p + "%</td>";
							html += "<td class='sum-item4' zrms_ym='" + result[i].zrms_ym + "'>" + result[i].zitem4_p + "%</td>";
							html += "<td class='sum-item5' zrms_ym='" + result[i].zrms_ym + "'>" + result[i].zitem5_p + "%</td>";
							html += "<td>" + result[i].zadmin_rank + "</td>";
							//html += "<td>" + result[i].zup_rank + "</td>";
							html += "</tr>";
						}
			    	} else {
						for (var i = 0; i < result.length; i++) {
							html += "<tr>";
							html += "<td>" + changeDateToView2(result[i].zrms_ym) + "</td>";
							html += "<td class='sum-item1' zrms_ym='" + result[i].zrms_ym + "'>" + result[i].zitem1_p + "</td>";
							html += "<td class='sum-item2' zrms_ym='" + result[i].zrms_ym + "'>" + numberWithCommas(result[i].zitem2_p) + "</td>";
							html += "<td class='sum-item3' zrms_ym='" + result[i].zrms_ym + "'>" + result[i].zitem3_p + "</td>";
							html += "<td class='sum-item4' zrms_ym='" + result[i].zrms_ym + "'>" + result[i].zitem4_p + "</td>";
							html += "<td class='sum-item5' zrms_ym='" + result[i].zrms_ym + "'>" + result[i].zitem5_p + "</td>";
							html += "<td>" + result[i].zadmin_rank + "</td>";
							//html += "<td>" + result[i].zup_rank + "</td>";
							html += "</tr>";
						}
			    	}
			    }
				
				$("#mon-main-tbody").append(html);

				<%-- IE에서는 좌측메뉴가 container에 먹히는 현상이 발생하는데 수정이불가능해서 조회시 동적으로 변경 --%>
				$(".left-bg").css("height", $("#container").css("height"));
				$("#grpFilterBox").css("visibility", "hidden");

				setClickEvent(zregion, zdept, zdept_brch, zemp);
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

	function excelDownloadForMon() {
		if(isEmpty(zregion)) {
			alert("본부를 선택해주세요.");
			return;
		} 
		openExcelPopup(zregion, zdept, zdept_brch, zemp, before_yyyymm, after_yyyymm, active_bcg_id, 'excelMonMoni_d.do', '월별_모니터링.xlsx');
	}

	function changeGrp2(e) {
		if(e.id == "chkbx3") {
			$('input:checkbox[id="chkbx3"]').prop("checked", true);
			$('input:checkbox[id="chkbx4"]').prop("checked", false);
			$('#th-crd').html('카드결제 현황');
		} else {
			$('input:checkbox[id="chkbx3"]').prop("checked", false);
			$('input:checkbox[id="chkbx4"]').prop("checked", true);
			$('#th-crd').html('카드결제 금액');
		}
	}

	function btnSumSearchFilterOpen() {
		if($("#sumOpenSearchFilter").css("visibility") == "visible") {
			$("#sumOpenSearchFilter").css("visibility", "hidden");
		} else {
			$("#sumOpenSearchFilter").css("visibility", "visible");
			$("#calFilterBox").css("visibility", "hidden");
			$("#grpFilterBox").css("visibility", "hidden");
		}

		<%-- IE에서는 좌측메뉴가 container에 먹히는 현상이 발생하는데 수정이불가능해서 조회시 동적으로 변경 --%>
		$(".left-bg").css("height", $("#container").css("height"));
	}
	
</script>
<%@ include file="../com/menual.jsp"%>
<h2>월별 모니터링 <button class="btn-menual" id="menual" onClick="openMenualPopup();">주요지표 정의</button></h2>
<div class="filter-area">
	<div class="left">
		<%@ include file="../com/calendarEx.jsp"%>
		<!-- 조직선택 input -->
		<%@ include file="../com/group.jsp"%>
		<div class="filtering select">
		    <div class="custom-input select-box" onclick="btnSumSearchFilterOpen()">
		    	<span>선택보기</span>
		    	<i class="fa fa-sort-amount-desc"></i>
		    	<!-- <i class="fa fa-caret-down"></i> -->
		    </div>
		    <div class="filterBox option-box" id="sumOpenSearchFilter" style="height: 80px;">
		    	<div style="text-align: left;">
					<div class="chkbx">
						<input type="checkbox" id="chkbx3" name="checkbox-btn" onchange="changeGrp2(this);" checked="checked">
						<label for="chkbx3">비중</label>
					</div>
					<div class="chkbx">
						<input type="checkbox" id="chkbx4" name="checkbox-btn" onchange="changeGrp2(this);">
						<label for="chkbx4">대상건수</label>
					</div>						    		
		    	</div>
		    </div>
		</div>
		<button class="btn-tb-top search" onclick="btnSearch();">검색</button>
	</div>
	<div class="right">
		<button class="btn-tb-top excel-1" onclick="excelDownloadForMon();">엑셀다운</button>
	</div>
</div>
<!-- // filter area -->
<!-- table -->
<div class="tbl">
	<table>
		<colgroup>
			<col style="width:auto%;">
			<col style="width:12%;">
			<col style="width:11%;">
			<col style="width:11%;">
			<col style="width:11%;">
			<col style="width:15%;">
			<col style="width:10%;">
			<!-- <col style="width:10%;"> -->
		</colgroup>
		<thead>
			<tr>
				<th>년-월</th>
				<th>개인정보 인증</th>
				<th id="th-crd">카드결제 현황</th>
				<th>이체제외 후 학습</th>
				<th>2개월 체납</th>
				<th>고객직접결제중복발송</th>
				<th>관리순위</th>
				<!-- <th>개선순위</th> -->
			</tr>
		</thead>
		<tbody id="mon-main-tbody">
			<tr>
				<td colspan="8" style="font-size:14px;">조회결과가 없습니다.</td>
			</tr>
		</tbody>
	</table>
</div>
<div class="left-info" id="data-dt" style="visibility: hidden;">* Data 기준 : <span id="data-dt-sum"></span></div>
</body>
</html>

<!-- // table -->