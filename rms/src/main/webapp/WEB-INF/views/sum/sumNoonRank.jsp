<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<%@ include file="../taglibs.jsp"%>
<%--
	[눈높이] 주요지표 순위
 --%>
<script>
	var active_bcg_id = "${active_bcg_id}";
	
	var zregion = "${zregion}";
	var zdept = "${zdept_id}";
	var zdept_brch = "${zdept_brch_id}";
	var zemp = "${zemp_id}";
	
	var search_rms_ym = "";
	
	$(document).ready(function(e){
		setActiveMenu("menu1");
		setCalendar();
		btnSearch();
	});

	<%--=============================================================
	조회
	page : 조회할 페이지
	=============================================================--%>
	var searchArray = new Array();
	function btnSearch() {
		var brch_gb = "";
		
		if($("input:checkbox[name=ckbSelect3]").prop("checked") == false && $("input:checkbox[name=ckbSelect4]").prop("checked") == false) {
			brch_gb = "";
		} else if($("input:checkbox[name=ckbSelect3]").prop("checked") == true && $("input:checkbox[name=ckbSelect4]").prop("checked") == false) {
			brch_gb = "BL";
		} else if($("input:checkbox[name=ckbSelect3]").prop("checked") == false && $("input:checkbox[name=ckbSelect4]").prop("checked") == true) {
			brch_gb = "LC";
		} else {
			clickBrchCheck();
			return;
		}
		
		var obj = new Object();
		obj.zreq_ym = yyyymm;
		obj.active_bcg_id = active_bcg_id;
		obj.brch_gb = brch_gb;

		$.ajax({
			type : "POST",
			dataType : "json",
			url : "${ctx}/sum/getSumRank.do",
			contentType : 'application/json',
			data : JSON.stringify(obj),
			success : function(result) {
				// 하단 데이터 기준일 보이게하기
				$("#data-dt").css("visibility", "visible");
				$("#data-dt-sum").html(yyyymm.substring(0, 4) + "-" + yyyymm.substring(4, 6));
				
				$("#ran-main-tbody").empty();

				var html = "";

				if(result.length == 0){
		        	html += '<tr>';
		        	html += '<td colspan="11" style="font-size:14px;">조회결과가 없습니다.</td>';
		        	html += '</tr>';
			    } else {
			    	searchArray = result;
					for ( var i in searchArray) {
						html += "<tr>";
						html += "<td>" + (i * 1 + 1) + "</td>";
						html += "<td style='text-align: left;'>" + searchArray[i].zregion_nm;
						if(!isEmpty(searchArray[i].zdept_nm)) {
							html += " > " + searchArray[i].zdept_nm;
						}
						if(!isEmpty(searchArray[i].zdept_brch_nm)) {
							html += " > " + searchArray[i].zdept_brch_nm;
						}
						html += "</td>";
						html += "<td>" + searchArray[i].rms_info_r + "</td>";
						html += "<td>" + searchArray[i].rms_card_r + "</td>";
						html += "<td>" + searchArray[i].rms_tran_r + "</td>";
						html += "<td>" + searchArray[i].rms_arre_r + "</td>";
						html += "<td>" + searchArray[i].rms_pay_r + "</td>";
						html += "<td>" + searchArray[i].rms_summ_r + "</td>";
						html += "<td>" + searchArray[i].rms_stmng_r + "</td>";
						html += "<td>" + searchArray[i].zsub_rank + "</td>";
						html += "<td>" + searchArray[i].zup_rank + "</td>";
						html += "</tr>";
					}
			    }
				
				$("#ran-main-tbody").append(html);

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

	function clickBrchCheck() {
		var check3 = $("input:checkbox[name=ckbSelect3]").is(":checked");
		var check4 = $("input:checkbox[name=ckbSelect4]").is(":checked");
		 
		if(check3 == true && check4 == true) {
			 alert("구분을 한개만 선택해주세요.");
		}
	}

	function sortSearch(e) {
		var newArray = searchArray;

		var param = $(e).attr("value");
		$(".th-order").attr("order", "");
		
		if($(e).attr("order") == "") {
			newArray = searchArray.sort(function (a, b) {
				$(e).attr("order", "asc");
				if(param == "rms_info_r") {
			    	return a.rms_info_r - b.rms_info_r;
				} else if(param == "rms_card_r") {
					return a.rms_card_r - b.rms_card_r;
				} else if(param == "rms_tran_r") {
					return a.rms_tran_r - b.rms_tran_r;
				} else if(param == "rms_arre_r") {
					return a.rms_arre_r - b.rms_arre_r;
				} else if(param == "rms_pay_r") {
					return a.rms_pay_r - b.rms_pay_r;
				} else if(param == "rms_summ_r") {
					return a.rms_summ_r - b.rms_summ_r;
				} else if(param == "rms_stmng_r") {
					return a.rms_stmng_r - b.rms_stmng_r;
				} else if(param == "zsub_rank") {
					return a.zsub_rank - b.zsub_rank;
				} else if(param == "zup_rank") {
					return a.zup_rank - b.zup_rank;
				}
			});
		} else if($(e).attr("order") == "asc") {
			newArray = searchArray.sort(function (a, b) {
				$(e).attr("order", "desc");
				if(param == "rms_info_r") {
			    	return a.rms_info_r - b.rms_info_r;
				} else if(param == "rms_card_r") {
					return a.rms_card_r - b.rms_card_r;
				} else if(param == "rms_tran_r") {
					return a.rms_tran_r - b.rms_tran_r;
				} else if(param == "rms_arre_r") {
					return a.rms_arre_r - b.rms_arre_r;
				} else if(param == "rms_pay_r") {
					return a.rms_pay_r - b.rms_pay_r;
				} else if(param == "rms_summ_r") {
					return a.rms_summ_r - b.rms_summ_r;
				} else if(param == "rms_stmng_r") {
					return a.rms_stmng_r - b.rms_stmng_r;
				} else if(param == "zsub_rank") {
					return a.zsub_rank - b.zsub_rank;
				} else if(param == "zup_rank") {
					return a.zup_rank - b.zup_rank;
				}
			});
		}

		$("#ran-main-tbody").empty();
		
		var html = "";
		
		for ( var i in newArray) {
			html += "<tr>";
			html += "<td>" + (i * 1 + 1) + "</td>";
			html += "<td style='text-align: left;'>" + newArray[i].zregion_nm;
			if(!isEmpty(newArray[i].zdept_nm)) {
				html += " > " + newArray[i].zdept_nm;
			}
			if(!isEmpty(newArray[i].zdept_brch_nm)) {
				html += " > " + newArray[i].zdept_brch_nm;
			}
			html += "</td>";
			html += "<td>" + newArray[i].rms_info_r + "</td>";
			html += "<td>" + newArray[i].rms_card_r + "</td>";
			html += "<td>" + newArray[i].rms_tran_r + "</td>";
			html += "<td>" + newArray[i].rms_arre_r + "</td>";
			html += "<td>" + newArray[i].rms_pay_r + "</td>";
			html += "<td>" + newArray[i].rms_summ_r + "</td>";
			html += "<td>" + newArray[i].rms_stmng_r + "</td>";
			html += "<td>" + newArray[i].zsub_rank + "</td>";
			html += "<td>" + newArray[i].zup_rank + "</td>";
			html += "</tr>";
		}

		$("#ran-main-tbody").append(html);
	}
</script>
<h2>주요지표[순위]</h2>
<div class="filter-area">
	<div class="left">
		<%@ include file="../com/calendar.jsp"%>
		<div class="tblchbx" style="padding-top: 5px; padding-right: 20px; padding-left: 20px;">
			<input type="checkbox" id="ckb003" name="ckbSelect3"><label for="ckb003">홈러닝&nbsp;&nbsp;&nbsp;&nbsp;</label>
			<input type="checkbox" id="ckb004" name="ckbSelect4"><label for="ckb004">러닝</label>										
		</div>
		<button class="btn-tb-top search" onclick="btnSearch();">검색</button>
	</div>
	<div class="right">
		<button class="btn-tb-top excel-1" onclick="openExcelPopup(zregion, zdept, zdept_brch, zemp, before_yyyymm, after_yyyymm, active_bcg_id, 'excelStmApply_d.do', '[현장용]긴급교재_리스트.xlsx');">엑셀다운</button>
	</div>
</div>

<div class="tbl">
	<table class="sortable">
		<colgroup>
			<col style="width: 3%;">
			<col style="width: auto;">
			<col style="width: 7%;">
			<col style="width: 7%;">
			<col style="width: 7%;">
			<col style="width: 7%;">
			<col style="width: 7%;">
			<col style="width: 8%;">
			<col style="width: 7%;">
			<col style="width: 7%;">
			<col style="width: 7%;">
		</colgroup>
		<thead>
			<tr>
				<th>No.</th>
				<th>조직</th>
				<th class="th-order" onClick="sortSearch(this)" order="" value="rms_info_r" style="cursor: pointer;">개인정보</br>인증</th>
				<th class="th-order" onClick="sortSearch(this)" order="" value="rms_card_r" style="cursor: pointer;">카드결제 현황</th>
				<th class="th-order" onClick="sortSearch(this)" order="" value="rms_tran_r" style="cursor: pointer;">이체제외 후</br>학습</th>
				<th class="th-order" onClick="sortSearch(this)" order="" value="rms_arre_r" style="cursor: pointer;">2개월 체납</th>
				<th class="th-order" onClick="sortSearch(this)" order="" value="rms_pay_r" style="cursor: pointer;">직접결제</br>중복발송</th>
				<th class="th-order" onClick="sortSearch(this)" order="" value="rms_summ_r" style="cursor: pointer;">써밋제품 학습</th>
				<th class="th-order" onClick="sortSearch(this)" order="" value="rms_stmng_r" style="cursor: pointer;">긴급교재</th>
				<th class="th-order" onClick="sortSearch(this)" order="" value="zsub_rank" style="cursor: pointer;">관리순위</th>
				<th class="th-order" onClick="sortSearch(this)" order="" value="zup_rank" style="cursor: pointer;">개선순위</th>
			</tr>
		</thead>
		<tbody id="ran-main-tbody">
			<tr>
				<td colspan="11" style="font-size:14px;">조회결과가 없습니다.</td>
			</tr>
		</tbody>
	</table>
	<%@ include file="../com/page.jsp"%>
	
</div>
<div class="left-info" id="data-dt" style="visibility: hidden;">* Data 기준 : <span id="data-dt-sum"></span></div>