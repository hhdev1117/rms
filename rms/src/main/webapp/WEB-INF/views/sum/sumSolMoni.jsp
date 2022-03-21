<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<%@ include file="../taglibs.jsp"%>
<%--
	[솔루니] 주요지표
 --%>
<script>
	var active_bcg_id = "${active_bcg_id}";

	var zregion = "${zregion}";
	var zdept = "${zdept_id}";
	var zdept_brch = "${zdept_brch_id}";
	var zemp = "${zemp_id}";

	var search_rms_ym = "";
	var search_cnt_gb = "";
	
	$(document).ready(function(e){
		setActiveMenu("menu1");
		setCalendar();
		btnSearch();

		openPopup();
	});

	<%--=============================================================
	펼쳐보기 기능(확장)
	=============================================================--%>
	function expandGrid(level) {
		if(level == "1") {
			$(".zlevel2").css("display", "");
			$(".zlevel3").css("display", "none");
			$(".zlevel4, .zlevel5").remove();
			
			$(".zlevel1").find("i:eq(0)").attr("class", "fa fa-minus-square").css("color", "");
			$(".zlevel2, .zlevel3").find("i:eq(0)").attr("class", "fa fa-plus-square").css("color", "#91a9ca");
		} else {
			$(".zlevel2, .zlevel3").css("display", "");
			$(".zlevel4, .zlevel5").remove();

			$(".zlevel1, .zlevel2").find("i:eq(0)").attr("class", "fa fa-minus-square").css("color", "");
			$(".zlevel3").find("i:eq(0)").attr("class", "fa fa-plus-square").css("color", "#91a9ca");
		}

		<%-- IE에서는 좌측메뉴가 container에 먹히는 현상이 발생하는데 수정이불가능해서 조회시 동적으로 변경 --%>
		$(".left-bg").css("height", $("#container").css("height"));
	}

	<%--=============================================================
	펼쳐보기 기능(축소)
	=============================================================--%>
	function contractGrid(level) {
		if(level == "1") {
			$(".zlevel2, .zlevel3").css("display", "none");
			$(".zlevel4, .zlevel5").remove();

			$(".zlevel1, .zlevel2, .zlevel3, .zlevel4, .zlevel5").find("i:eq(0)").attr("class", "fa fa-plus-square").css("color", "#91a9ca");
		} else {
			$(".zlevel3").css("display", "none");
			$(".zlevel4, .zlevel5").remove();

			$(".zlevel2, .zlevel3, .zlevel4, .zlevel5").find("i:eq(0)").attr("class", "fa fa-plus-square").css("color", "#91a9ca");
		}

		<%-- IE에서는 좌측메뉴가 container에 먹히는 현상이 발생하는데 수정이불가능해서 조회시 동적으로 변경 --%>
		$(".left-bg").css("height", $("#container").css("height"));
	}

	<%--=============================================================
	주요지표 클릭 시 화면이동 / 항목 추가 시 수정필요
	=============================================================--%>
	function setClickEvent() {
		var node = "td.sum-item1, td.sum-item2, td.sum-item3, td.sum-item4, td.sum-item5, td.sum-item6, td.sum-item7";
		
		$(node).hover(function() {
			$(this).css("font-weight", "bold");
		}, function(){
			$(this).css("font-weight", "");
		});
		
		$(node).on("click", function () {
			var param_zregion 		= $(this).parents("tr").attr("zregion");
			var param_zdept_id 		= $(this).parents("tr").attr("zdept_id");
			var param_zdept_brch_id = $(this).parents("tr").attr("zdept_brch_id");
			var param_zemp_id 		= $(this).parents("tr").attr("zemp_id");
			var param_zreq_ym 		= $(this).parents("tr").attr("zreq_ym");
			
			var data = new Object();
			data.param_zregion 				= param_zregion;
			data.param_zdept_id 			= param_zdept_id;
			data.param_zdept_brch_id 		= param_zdept_brch_id;
			data.param_zemp_id 				= param_zemp_id;
			data.param_zreq_ym 				= param_zreq_ym;

			var cls = $(this).attr("class");
			if(cls == "sum-item1") getContentTab("/ind/indSolAuth.do", data);
			else if(cls == "sum-item2") getContentTab("/crd/crdSolCard.do", data);
			else if(cls == "sum-item3") getContentTab("/trn/trnSolStd.do", data);
			else if(cls == "sum-item4") getContentTab("/arr/arrSolPay.do", data);
			else if(cls == "sum-item5") getContentTab("/pay/paySolDirect.do", data);
		});
	}

	<%--=============================================================
	체크박스 이벤트
	=============================================================--%>
	function setTreeTable(e) {
		if($("#" + e.id).prop("checked") == false) {
			if(e.id == "rdo1") {
				contractGrid(1);
				$('input:checkbox[id="rdo2"]').prop("checked", false);
			} else if(e.id == "rdo2") {
				contractGrid(2);
			}
		} else {
			if(e.id == "rdo1"){
				expandGrid(1);
			} else if(e.id == "rdo2") {
				expandGrid(2);
				$('input:checkbox[id="rdo1"]').prop("checked", true);
			} 
		}

		<%-- IE에서는 좌측메뉴가 container에 먹히는 현상이 발생하는데 수정이불가능해서 조회시 동적으로 변경 --%>
		$(".left-bg").css("height", $("#container").css("height"));
	}

	<%--=============================================================
	펼쳐보기 열기 / 닫기.
	=============================================================--%>
	function btnSumOpen() {
		if($("#sumOpenGrp").css("visibility") == "visible") {
			$("#sumOpenGrp").css("visibility", "hidden");
		} else {
			$("#sumOpenGrp").css("visibility", "visible");
		}

		<%-- IE에서는 좌측메뉴가 container에 먹히는 현상이 발생하는데 수정이불가능해서 조회시 동적으로 변경 --%>
		$(".left-bg").css("height", $("#container").css("height"));
	}
	
	<%--=============================================================
	조회
	=============================================================--%>
	function btnSearch() {

		var cnt_gb = "ind";
		if($("input:checkbox[id='chkbx3']").prop("checked") == true) {
			cnt_gb = "ind";
		} else if($("input:checkbox[id='chkbx4']").prop("checked") == true) {
			cnt_gb = "cnt";
		} else {
			cnt_gb = "ind";
		}
		
		<%-- 
			달력, 펼쳐보기 닫기
		--%>
		$("#calFilterBox, #sumOpenGrp, #sumOpenSearchFilter").css("visibility", "hidden");
		$("#calFilterBox, #sumOpenGrp").css("visibility", "hidden");
		$('input:checkbox[id="rdo1"], input:checkbox[id="rdo2"]').prop("checked", false);
		<%-- 
			조회했으면 펼쳐보기를 위해 조회한 연월을 저장해야된다.
			팀 이하 확장시 해당 ym을 param으로 조회하기 때문이다.
			yyyymm은 변동가능
		--%>
		search_rms_ym = yyyymm;
		search_cnt_gb = cnt_gb;
		
		var obj = new Object();
		obj.zreq_ym = yyyymm;
		obj.brch_gb = "";
		obj.active_bcg_id = active_bcg_id;
		obj.cnt_gb = cnt_gb;
		
		$.ajax({
			type : "POST",
			dataType : "json",
			url : "${ctx}/sum/getSumMoni.do",
			contentType : 'application/json',
			data : JSON.stringify(obj),
			success : function(result) {
				$("#sum-main-tbody").empty();
				    <%--
				    var zlevel1 = 0;
				    var zlevel2 = 0;
				    var zlevel3 = 0;
				    var zlevel4 = 0;
				    var zlevel5 = 0;

					for (var i in result) {
						eval("zlevel" + result[i].zlevel + "+= 1");
						if(result[i].zlevel == 1) {
							html += "<tr class='sum-node-bcg' data-node-id='" + zlevel1 + "'>";
							html += "<td class='left'>" + result[i].zregion_nm + "</td>";
							html += "<td>" + result[i].zitem1_p + "</td>";
							html += "<td>" + result[i].zitem2_p + "</td>";
							html += "<td>" + result[i].zitem3_p + "</td>";
							html += "<td>" + result[i].zitem4_p + "</td>";
							html += "<td>" + result[i].zitem5_p + "</td>";
							html += "<td>" + result[i].zitem6_p + "</td>";
							html += "<td>" + result[i].zitem7_p + "</td>";
							html += "</tr>";

							zlevel2 = 0;
							zlevel3 = 0;
							zlevel4 = 0;
							zlevel5 = 0;
						} else if(result[i].zlevel == 2) {
							html += "<tr class='sum-node-dept' data-node-id='" + zlevel1 + "." + zlevel2 + "' data-node-pid='" + zlevel1 + "'>";
							html += "<td class='left'>" + result[i].zdept_nm + "</td>";
							html += "<td>" + result[i].zitem1_p + "</td>";
							html += "<td>" + result[i].zitem2_p + "</td>";
							html += "<td>" + result[i].zitem3_p + "</td>";
							html += "<td>" + result[i].zitem4_p + "</td>";
							html += "<td>" + result[i].zitem5_p + "</td>";
							html += "<td>" + result[i].zitem6_p + "</td>";
							html += "<td>" + result[i].zitem7_p + "</td>";
							html += "</tr>";

							zlevel3 = 0;
							zlevel4 = 0;
							zlevel5 = 0;
						} else if(result[i].zlevel == 3) {
							html += "<tr class='sum-node-brch' data-node-id='" + zlevel1 + "." + zlevel2 + "." + zlevel3 + "' data-node-pid='" + zlevel1 + "." + zlevel2 + "'>";
							html += "<td class='left'>" + result[i].zdept_brch_nm + "</td>";
							html += "<td>" + result[i].zitem1_p + "</td>";
							html += "<td>" + result[i].zitem2_p + "</td>";
							html += "<td>" + result[i].zitem3_p + "</td>";
							html += "<td>" + result[i].zitem4_p + "</td>";
							html += "<td>" + result[i].zitem5_p + "</td>";
							html += "<td>" + result[i].zitem6_p + "</td>";
							html += "<td>" + result[i].zitem7_p + "</td>";
							html += "</tr>";

							zlevel4 = 0;
							zlevel5 = 0;
						} else if(result[i].zlevel == 4) {
							html += "<tr class='sum-node-emp' data-node-id='" + zlevel1 + "." + zlevel2 + "." + zlevel3 + "." + zlevel4 + "' data-node-pid='" + zlevel1 + "." + zlevel2 + "." + zlevel3 + "'>";
							html += "<td class='left'>" + result[i].zemp_nm + "[" + result[i].zemp_id + "]</td>";
							html += "<td>" + result[i].zitem1_p + "</td>";
							html += "<td>" + result[i].zitem2_p + "</td>";
							html += "<td>" + result[i].zitem3_p + "</td>";
							html += "<td>" + result[i].zitem4_p + "</td>";
							html += "<td>" + result[i].zitem5_p + "</td>";
							html += "<td>" + result[i].zitem6_p + "</td>";
							html += "<td>" + result[i].zitem7_p + "</td>";
							html += "</tr>";

							zlevel5 = 0;
						} else if(result[i].zlevel == 5) {
							html += "<tr class='sum-node-cls' data-node-id='" + zlevel1 + "." + zlevel2 + "." + zlevel3 + "." + zlevel4 + "." + zlevel5 + "' data-node-pid='" + zlevel1 + "." + zlevel2 + "." + zlevel3 + "." + zlevel4 + "'>";
							html += "<td class='left'>" + result[i].zcls_nm + "</td>";
							html += "<td>" + result[i].zitem1_p + "</td>";
							html += "<td>" + result[i].zitem2_p + "</td>";
							html += "<td>" + result[i].zitem3_p + "</td>";
							html += "<td>" + result[i].zitem4_p + "</td>";
							html += "<td>" + result[i].zitem5_p + "</td>";
							html += "<td>" + result[i].zitem6_p + "</td>";
							html += "<td>" + result[i].zitem7_p + "</td>";
							html += "</tr>";
						}
					}

					 
						조회건수가 대량이므로 Client Side Randering을 하지않고
					 	Server Side Rendering으로 처리한다.
					 	Client Side Randering 을 할경우 Controller에서 렌더링하는부분을
					 	List그대로 반환시키고 위 주석을 풀어주면 된다. 
					 --%>
						if(result == "N") {
							var html = "";
				        	html += '<tr>';
				        	html += '<td colspan="10" style="font-size:14px;">조회결과가 없습니다.</td>';
				        	html += '</tr>';

				        	$("#sum-main-tbody").append(html);
						} else {
							$("#sum-main-tbody").append(result);
						}

					<%--=============================================================
					테이블 클릭시
					=============================================================--%>
					setClickEvent();

					<%-- IE에서는 좌측메뉴가 container에 먹히는 현상이 발생하는데 수정이불가능해서 조회시 동적으로 변경 --%>
					$(".left-bg").css("height", $("#container").css("height"));
					
					<%--
					$('#basic').simpleTreeTable({
						expander : $('#expander'),
						collapser : $('#collapser'),
						storeKey : 'simple-tree-table-basic',
						opened: [0] // 그리드 오픈 안함 [0] ==> 'all' 변경시 모두 오픈
					}).on('node:open', function(e, $node) {
						if($node.attr("zlevel") == "3") {
							getAddData($node);
						}
					}).on('node:close', function(e, $node) {
						//팀 아래부터는 닫을때 삭제한다.
						if($node.attr("zlevel") == "3") {
							var nodeArr = new Array();
							nodeArr = $('#basic').data('simple-tree-table').findDescendants($node, nodeArr);
							for(var i = 0; i < nodeArr.length; i++) {
								nodeArr[i].remove();
							}
						}
					})
					--%>

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

	function getAddData(node) {
		var level = $(node).parents("tr").attr("zlevel");
		var opened = "";
		if($(node).attr("class") == "fa fa-minus-square") opened = "closed";
		else opened = "opened";

		<%-- 오픈 할 경우 --%>
		if(opened == "opened") {
			if(level == "1") {
				$(".zlevel2." + $(node).parents("tr").attr("zregion")).css("display", "");
				$(node).attr("class", "fa fa-minus-square").css("color", "");
			}
			else if(level == "2") {
				$(".zlevel3." + $(node).parents("tr").attr("zdept_id")).css("display", "");
				$(node).attr("class", "fa fa-minus-square").css("color", "");
			} else if(level == "3") {
				$(node).attr("class", "fa fa-minus-square").css("color", "");
				
				var obj = new Object();
				obj.zreq_ym = search_rms_ym;
				obj.zregion = $(node).parents("tr").attr("zregion");
				obj.zdept_id = $(node).parents("tr").attr("zdept_id");
				obj.zdept_brch_id = $(node).parents("tr").attr("zdept_brch_id");
				obj.active_bcg_id = active_bcg_id;
				obj.cnt_gb = search_cnt_gb;
				
				$.ajax({
					type : "POST",
					dataType : "json",
					async: false,
					url : "${ctx}/sum/getSumMoniDtl.do",
					contentType : 'application/json',
					data : JSON.stringify(obj),
					success : function(result) {
						$(node).parents("tr").after(result);

						<%--=============================================================
						테이블 클릭시
						=============================================================--%>
						setClickEvent();
							
						//$('#basic').data('simple-tree-table').build();
						//$(".sum-node-cls").attr("class", "sum-node-cls simple-tree-table-empty");
					},
					beforeSend:function(xmlHttpRequest){
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
				    }
				});

				$(node).attr("class", "fa fa-minus-square").css("color", "");
			} else if(level == "4") {
				$(".zlevel5." + $(node).parents("tr").attr("zemp_id")).css("display", "");
				$(node).attr("class", "fa fa-minus-square").css("color", "");
			}
		} else {
			if(level == "1") {
				var c_zregion = $(node).parents("tr").attr("zregion");
				$(".zlevel2." + c_zregion + ", .zlevel3." + c_zregion).css("display", "none");
				$(".zlevel4." + c_zregion + ", .zlevel5." + c_zregion).remove();


				$(node).attr("class", "fa fa-plus-square").css("color", "#91a9ca");
				$(".zlevel2." + c_zregion + ", .zlevel3." + c_zregion + ", .zlevel4." + c_zregion + ", .zlevel5." + c_zregion).find('i:eq(0)').attr("class", "fa fa-plus-square").css("color", "#91a9ca");
			} else if(level == "2") {
				var c_zdept_id = $(node).parents("tr").attr("zdept_id");
				$(".zlevel3." + c_zdept_id).css("display", "none");
				$(".zlevel4." + c_zdept_id + ", .zlevel5." + c_zdept_id).remove();

				$(node).attr("class", "fa fa-plus-square").css("color", "#91a9ca");
				$(".zlevel3." + c_zdept_id + ", .zlevel4." + c_zdept_id + ", .zlevel5." + c_zdept_id).find('i:eq(0)').attr("class", "fa fa-plus-square").css("color", "#91a9ca");
			} else if(level == "3") {
				var c_zdept_id = $(node).parents("tr").attr("zdept_id");
				var c_zdept_brch_id = $(node).parents("tr").attr("zdept_brch_id");
				$(".zlevel4." + c_zdept_brch_id + "." + c_zdept_id + ", .zlevel5." + c_zdept_brch_id + "." + c_zdept_id).remove();

				$(node).attr("class", "fa fa-plus-square").css("color", "#91a9ca");
				$(".zlevel4." + c_zdept_brch_id + ", .zlevel5." + c_zdept_brch_id).find('i:eq(0)').attr("class", "fa fa-plus-square").css("color", "#91a9ca");
			} else if(level == "4") {
				var c_zemp_id = $(node).parents("tr").attr("zemp_id");
				$(".zlevel5." + c_zemp_id).css("display", "none");

				$(node).attr("class", "fa fa-plus-square").css("color", "#91a9ca");
				$(".zlevel5." + c_zemp_id).find('i:eq(0)').attr("class", "fa fa-plus-square").css("color", "#91a9ca");
			} 
		}

		<%-- IE에서는 좌측메뉴가 container에 먹히는 현상이 발생하는데 수정이불가능해서 조회시 동적으로 변경 --%>
		$(".left-bg").css("height", $("#container").css("height"));
	}

	function btnSumSearchFilterOpen() {
		if($("#sumOpenSearchFilter").css("visibility") == "visible") {
			$("#sumOpenSearchFilter").css("visibility", "hidden");
		} else {
			$("#sumOpenSearchFilter").css("visibility", "visible");
			$("#calFilterBox").css("visibility", "hidden");
		}

		<%-- IE에서는 좌측메뉴가 container에 먹히는 현상이 발생하는데 수정이불가능해서 조회시 동적으로 변경 --%>
		$(".left-bg").css("height", $("#container").css("height"));
	}

	function excelDownloadForSum() {
		var chkbx3 = $("input:checkbox[id='chkbx3']").is(":checked");
		var chkbx4 = $("input:checkbox[id='chkbx4']").is(":checked");

		var cnt_gb = "ind";
		if(chkbx3 == true) cnt_gb = "ind";
		else cnt_gb = "cnt";

		openExcelPopupForSum(zregion, zdept, zdept_brch, zemp, yyyymm, yyyymm, active_bcg_id, 'excelMoniSum_ex_d.do', '주요지표.xlsx', ' ', cnt_gb);
	}

	function changeGrp2(e) {
		if(e.id == "chkbx3") {
			$('input:checkbox[id="chkbx3"]').prop("checked", true);
			$('input:checkbox[id="chkbx4"]').prop("checked", false);
			$('#th-crd').html('카드결제<br>현황');
		} else {
			$('input:checkbox[id="chkbx3"]').prop("checked", false);
			$('input:checkbox[id="chkbx4"]').prop("checked", true);
			$('#th-crd').html('카드결제<br>금액');
		}
	}
</script>
<%@ include file="../com/menual.jsp"%>
<%@ include file="../com/popup.jsp"%>
<%@ include file="../com/rank.jsp"%>
<h2>주요지표 <button class="btn-menual" id="menual" onClick="openMenualPopup();">주요지표 정의</button></h2>
<div class="filter-area">
	<div class="left">
		<%@ include file="../com/calendar.jsp"%>
		<!-- 선택보기 수정(210604) -->
		<div class="filtering select">
		    <div class="custom-input select-box" onclick="btnSumSearchFilterOpen()">
		    	<span>선택보기</span>
		    	<i class="fa fa-sort-amount-desc"></i>
		    	<!-- <i class="fa fa-caret-down"></i> -->
		    </div>
		    <div class="filterBox option-box" id="sumOpenSearchFilter" style="height:80px;">
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
		<button class="btn-tb-top search" onClick="btnSearch();">검색</button>
	</div>
	<div class="right">
		<button class="btn-tb-top excel-1" id="btn-rank" onClick="openRankPopup();">랭킹</button>
		<div class="custom-input ext-tbl">
			<span>펼쳐보기 선택</span>
			<div class="chkbx">
				<input type="checkbox" id="rdo1" name="radio-btn" onclick="setTreeTable(this);">
				<label for="rdo1" style="font-size: 13px;">교육국</label> <%-- "사업국" -> "교육국" 수정 2021.07.29 --%>
			</div>
			<div class="chkbx">
				<input type="checkbox" id="rdo2" name="radio-btn" onclick="setTreeTable(this);">
				<label for="rdo2" style="font-size: 13px;">팀[센터]</label>
			</div>
			<!--
			<div class="rdo">
				<input type="radio" id="rdo1" name="radio-btn" onclick="setTreeTable(this);">
				<label for="rdo1">사업국</label>
			</div>
			<div class="rdo">
				<input type="radio" id="rdo2" name="radio-btn" onclick="setTreeTable(this);">
				<label for="rdo2">팀[센터]</label>
			</div>
			 -->
		</div>
		<button class="btn-tb-top excel-1" onclick="excelDownloadForSum();">엑셀다운</button>
	</div>
</div>
<!-- // filter area -->
<!-- table -->
<div class="tbl">
	<table id="basic">
		<colgroup>
			<col style="width: 34%;">
			<col style="width: 7%;">
			<col style="width: 8%;">
			<col style="width: 7%;">
			<col style="width: 7%;">
			<col style="width: 10%;">
			<col style="width: 8%;">
			<!-- <col style="width: 6%;">  -->
		</colgroup>
		<thead>
			<tr>
				<th>조직</th>
				<th>개인정보</br>인증</th>
				<th id="th-crd">카드결제</br>현황</th>
				<th>이체제외 후</br>학습</th>
				<th>2개월 체납</th>
				<th>고객직접결제</br>중복발송</th>
				<th>관리순위</th>
				<!-- <th>개선순위</th> -->
			</tr>
		</thead>
		<tbody id="sum-main-tbody">
			<tr>
				<td colspan="10" style="font-size:14px;">조회결과가 없습니다.</td>
			</tr>
		</tbody>
	</table>
</div>
</body>
</html>

<!-- // table -->