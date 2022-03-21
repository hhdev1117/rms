<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<script type="text/javascript">
	function openRankPopup() {
        $('#layerbox-rank').css("position", "absolute");
        $('#layerbox-rank').css("top",(($(window).height() - $('#layerbox-rank').outerHeight()) / 2) + $(window).scrollTop());
        $('#layerbox-rank').css("left",(($(window).width() - $('#layerbox-rank').outerWidth()) / 2) + $(window).scrollLeft());
        //$('#layerbox-rank').draggable();
        $('#layerbox-rank').show();

        setRankCalendar();
        btnRankSearch();
        
        wrapWindowByMaskRank();

        // 정렬 초기화
        $(".th-order").attr("order", "");

        // 방문, 러닝 초기화
		$('input:checkbox[id="chkbx5"]').prop("checked", true);
		$('input:checkbox[id="chkbx6"]').prop("checked", false);

		// 비중, 건수 초기화
		$('input:checkbox[id="chkbx7"]').prop("checked", true);
		$('input:checkbox[id="chkbx8"]').prop("checked", false);
		$('input:checkbox[id="chkbx9"]').prop("checked", false);
		$('#th-crd-rank').html('카드결제</br>현황');

		// 달력 초기화
		setRankCalendar();
	}

	function wrapWindowByMaskRank() {
        var maskHeight = $(document).height(); 
        var maskWidth = $(window).width();
      
        $('#mask-rank').css({
            'width' : maskWidth,
            'height' : maskHeight
        });
        $('#mask-rank').fadeTo("slow", 0.5);
    }

    function closeRankPopup() {
    	$('#layerbox-rank').hide();
        $('#mask-rank').hide();
    }

	<%--=============================================================
	조회
	page : 조회할 페이지
	=============================================================--%>
	// 정렬을 위한 Array
	// 상위20%, 평균은 정렬하면 안되므로 별도로 관리
	var searchRankArray = new Array(); // 데이터용
	var searchRankTotalArray = new Array(); // 상위20%, 평균용

	// 정렬을 위한 변수 // 엑셀다운 할때도 param으로 보내야합니다.
	var rank_search_cnt_gb = "";
	var rank_search_brch_gb = "";
	var rank_search_yyyymm_rank = "";
	function btnRankSearch() {
		var rank_brch_gb = "";
		if(active_bcg_id == "N") {
			if($("input:checkbox[id='chkbx5']").prop("checked") == false && $("input:checkbox[id='chkbx6']").prop("checked") == false) {
				rank_brch_gb = "";
			} else if($("input:checkbox[id='chkbx5']").prop("checked") == true && $("input:checkbox[id='chkbx6']").prop("checked") == false) {
				rank_brch_gb = "BL";
			} else if($("input:checkbox[id='chkbx5']").prop("checked") == false && $("input:checkbox[id='chkbx6']").prop("checked") == true) {
				rank_brch_gb = "LC";
			} else {
				clickBrchCheck();
				return;
			}
		} else { // 솔루니, 차이홍은 BL만 조회
			rank_brch_gb = "BL";
		}
		
		var rank_cnt_gb = "ind";
		if($("input:checkbox[id='chkbx7']").prop("checked") == true) {
			rank_cnt_gb = "ind";
		} else if($("input:checkbox[id='chkbx8']").prop("checked") == true) {
			rank_cnt_gb = "cnt";
		} else if($("input:checkbox[id='chkbx9']").prop("checked") == true) {
			rank_cnt_gb = "ran";
		} else {
			cnt_gb = "ind";
		}
		
		$("#rank-calFilterBox, #sumOpenSearchRankFilter").css("visibility", "hidden");
		$('input:checkbox[id="rdo3"], input:checkbox[id="rdo4"]').prop("checked", false);

		search_rms_ym = yyyymm_rank;
		search_cnt_gb = rank_cnt_gb;
		
		var obj = new Object();
		obj.zreq_ym = yyyymm_rank;
		obj.brch_gb = rank_brch_gb;
		obj.active_bcg_id = active_bcg_id;
		obj.cnt_gb = rank_cnt_gb;
		
		$.ajax({
			type : "POST",
			dataType : "json",
			url : "${ctx}/sum/getSumRank.do",
			contentType : 'application/json',
			data : JSON.stringify(obj),
			success : function(result) {
				// 정렬 초기화
		        $(".th-order").attr("order", "");
		        
				// 정렬에 사용하기 위한 구분 
				rank_search_cnt_gb = rank_cnt_gb;
				rank_search_brch_gb = rank_brch_gb;
				rank_search_yyyymm_rank = yyyymm_rank;
				
				// 정렬을 위한 Array
				searchRankArray = new Array();
				searchRankTotalArray = new Array();
				  
				// 하단 데이터 기준일 보이게하기
				$("#data-rank-dt").css("visibility", "visible");
				$("#data-rank-dt-sum").html(yyyymm_rank.substring(0, 4) + "-" + yyyymm_rank.substring(4, 6));
				
				$("#ran-main-tbody").empty();

				var html = "";

				if(result.length == 0){
		        	html += '<tr>';
		        	html += '<td colspan="11" style="font-size:14px;">조회결과가 없습니다.</td>';
		        	html += '</tr>';
			    } else {

				    if(result.length > 17) {
						$("#rank-tbody-div").css("width", "1173px");
				    } else {
				    	$("#rank-tbody-div").css("width", "auto");
				    }

			    	if(rank_cnt_gb == "cnt") { // 건수
			    		if(rank_brch_gb == "BL") {
							for ( var i in result) {
								$(".notall").css("display", "none");

								if(result[i].zlevel == '2') { // 교육국만
									html += "<tr zreq_ym='" + result[i].zrms_ym + "' zregion='" + result[i].zregion + "' zdept_id='" + result[i].zdept_id + "' zdept_brch_id='" + result[i].zdept_brch_id + "'>";
									html += "<td style='color:#6799FF;'>" + result[i].zsub_rank + "</td>";
									html += "<td>" + result[i].zregion_nm  + "</td>";
									html += "<td>" + result[i].zdept_nm;
									if(isEmpty(result[i].zdept_emp_nm)) {
										html += "</td>";
									} else {
										html += "[" + result[i].zdept_emp_nm + "]" + "</td>";
									}
									html += "<td >" + result[i].rms_info_r + "</td>";
									html += "<td >" + numberWithCommas(result[i].rms_card_r) + "</td>";
									html += "<td >" + result[i].rms_tran_r + "</td>";
									html += "<td >" + result[i].rms_arre_r + "</td>";
									html += "<td >" + result[i].rms_pay_r + "</td>";
									html += "<td >" + result[i].rms_summ_r + "</td>";
									html += "<td >" + result[i].rms_stmng_r + "</td>";
									//html += "<td>" + result[i].zup_rank + "</td>";
									html += "</tr>";

									// 정렬에 사용하기위한 array 에 저장
									searchRankArray.push(result[i]);
								} else if(result[i].zlevel == 'A' || result[i].zlevel == 'B'){
									if(result[i].zlevel == 'A') {
										html += "<tr class='bg-blue'>";
										html += "<td colspan='3'>상위20%기준</td>";
									} else if(result[i].zlevel == 'B') {
										html += "<tr class='bg-grey'>";
										html += "<td colspan='3'>평균</td>";
									}
									
									html += "<td >" + result[i].rms_info_r + "</td>";
									html += "<td >" + result[i].rms_card_r + "</td>";
									html += "<td >" + result[i].rms_tran_r + "</td>";
									html += "<td >" + result[i].rms_arre_r + "</td>";
									html += "<td >" + result[i].rms_pay_r + "</td>";
									html += "<td >" + result[i].rms_summ_r + "</td>";
									html += "<td >" + result[i].rms_stmng_r + "</td>";
									//html += "<td>" + result[i].zup_rank + "</td>";
									html += "</tr>";
									
									// 정렬에 사용하기위한 array 에 저장
									searchRankTotalArray.push(result[i]);
								}
									
							}
				    	} else {

					    	$(".notall").css("display", "");
							for ( var i in result) {
								if(result[i].zlevel == '3') { // 센터만
									html += "<tr zreq_ym='" + result[i].zrms_ym + "' zregion='" + result[i].zregion + "' zdept_id='" + result[i].zdept_id + "' zdept_brch_id='" + result[i].zdept_brch_id + "'>";
									html += "<td style='color:#47C83E;'>" + result[i].zsub_rank + "</td>";
									html += "<td>" + result[i].zregion_nm  + "</td>";
									html += "<td>" + result[i].zdept_nm;
									if(isEmpty(result[i].zdept_emp_nm)) {
										html += "</td>";
									} else {
										html += "[" + result[i].zdept_emp_nm + "]" + "</td>";
									}

									html += "<td>" + result[i].zdept_brch_nm;
									if(isEmpty(result[i].zbrch_emp_nm)) {
										html += "</td>";
									} else {
										html += "[" + result[i].zbrch_emp_nm + "]" + "</td>";
									}
									
									html += "<td >" + result[i].rms_info_r + "</td>";
									html += "<td >" + numberWithCommas(result[i].rms_card_r) + "</td>";
									html += "<td >" + result[i].rms_tran_r + "</td>";
									html += "<td >" + result[i].rms_arre_r + "</td>";
									html += "<td >" + result[i].rms_pay_r + "</td>";
									html += "<td >" + result[i].rms_summ_r + "</td>";
									html += "<td >" + result[i].rms_stmng_r + "</td>";
									//html += "<td>" + result[i].zup_rank + "</td>";
									html += "</tr>";

									// 정렬에 사용하기위한 array 에 저장
									searchRankArray.push(result[i]);
								} else if(result[i].zlevel == 'A' || result[i].zlevel == 'B'){
									if(result[i].zlevel == 'A') {
										html += "<tr class='bg-blue'>";
										html += "<td colspan='4'>상위20%기준</td>";
									} else if(result[i].zlevel == 'B') {
										html += "<tr class='bg-grey'>";
										html += "<td colspan='4'>평균</td>";
									}
									html += "<td >" + result[i].rms_info_r + "</td>";
									html += "<td >" + result[i].rms_card_r + "</td>";
									html += "<td >" + result[i].rms_tran_r + "</td>";
									html += "<td >" + result[i].rms_arre_r + "</td>";
									html += "<td >" + result[i].rms_pay_r + "</td>";
									html += "<td >" + result[i].rms_summ_r + "</td>";
									html += "<td >" + result[i].rms_stmng_r + "</td>";
									//html += "<td>" + result[i].zup_rank + "</td>";
									html += "</tr>";
									
									// 정렬에 사용하기위한 array 에 저장
									searchRankTotalArray.push(result[i]);
								}
							}
				    	}
			    	} else if (rank_cnt_gb == "ind") { // 비중
			    		if(rank_brch_gb == "BL") {
							for ( var i in result) {
								$(".notall").css("display", "none");

								if(result[i].zlevel == '2') { // 교육국만
									html += "<tr zreq_ym='" + result[i].zrms_ym + "' zregion='" + result[i].zregion + "' zdept_id='" + result[i].zdept_id + "' zdept_brch_id='" + result[i].zdept_brch_id + "'>";
									html += "<td style='color:#6799FF;'>" + result[i].zsub_rank + "</td>";
									html += "<td>" + result[i].zregion_nm  + "</td>";
									html += "<td>" + result[i].zdept_nm;
									if(isEmpty(result[i].zdept_emp_nm)) {
										html += "</td>";
									} else {
										html += "[" + result[i].zdept_emp_nm + "]" + "</td>";
									}
									html += "<td >" + result[i].rms_info_r + "%</td>";
									html += "<td >" + result[i].rms_card_r + "%</td>";
									html += "<td >" + result[i].rms_tran_r + "%</td>";
									html += "<td >" + result[i].rms_arre_r + "%</td>";
									html += "<td >" + result[i].rms_pay_r + "%</td>";
									html += "<td >" + result[i].rms_summ_r + "%</td>";
									html += "<td >" + result[i].rms_stmng_r + "%</td>";
									//html += "<td>" + result[i].zup_rank + "</td>";
									html += "</tr>";

									// 정렬에 사용하기위한 array 에 저장
									searchRankArray.push(result[i]);
								} else if(result[i].zlevel == 'A' || result[i].zlevel == 'B'){
									if(result[i].zlevel == 'A') {
										html += "<tr class='bg-blue'>";
										html += "<td colspan='3'>상위20%기준</td>";
									} else if(result[i].zlevel == 'B') {
										html += "<tr class='bg-grey'>";
										html += "<td colspan='3'>평균</td>";
									}
									
									html += "<td >" + result[i].rms_info_r + "%</td>";
									html += "<td >" + result[i].rms_card_r + "%</td>";
									html += "<td >" + result[i].rms_tran_r + "%</td>";
									html += "<td >" + result[i].rms_arre_r + "%</td>";
									html += "<td >" + result[i].rms_pay_r + "%</td>";
									html += "<td >" + result[i].rms_summ_r + "%</td>";
									html += "<td >" + result[i].rms_stmng_r + "%</td>";
									//html += "<td>" + result[i].zup_rank + "</td>";
									html += "</tr>";
									
									// 정렬에 사용하기위한 array 에 저장
									searchRankTotalArray.push(result[i]);
								}
									
							}
				    	} else {

					    	$(".notall").css("display", "");
							for ( var i in result) {
								if(result[i].zlevel == '3') { // 센터만
									html += "<tr zreq_ym='" + result[i].zrms_ym + "' zregion='" + result[i].zregion + "' zdept_id='" + result[i].zdept_id + "' zdept_brch_id='" + result[i].zdept_brch_id + "'>";
									html += "<td style='color:#47C83E;'>" + result[i].zsub_rank + "</td>";
									html += "<td>" + result[i].zregion_nm  + "</td>";
									html += "<td>" + result[i].zdept_nm;
									if(isEmpty(result[i].zdept_emp_nm)) {
										html += "</td>";
									} else {
										html += "[" + result[i].zdept_emp_nm + "]" + "</td>";
									}

									html += "<td>" + result[i].zdept_brch_nm;
									if(isEmpty(result[i].zbrch_emp_nm)) {
										html += "</td>";
									} else {
										html += "[" + result[i].zbrch_emp_nm + "]" + "</td>";
									}
									html += "<td >" + result[i].rms_info_r + "%</td>";
									html += "<td >" + result[i].rms_card_r + "%</td>";
									html += "<td >" + result[i].rms_tran_r + "%</td>";
									html += "<td >" + result[i].rms_arre_r + "%</td>";
									html += "<td >" + result[i].rms_pay_r + "%</td>";
									html += "<td >" + result[i].rms_summ_r + "%</td>";
									html += "<td >" + result[i].rms_stmng_r + "%</td>";
									//html += "<td>" + result[i].zup_rank + "</td>";
									html += "</tr>";

									// 정렬에 사용하기위한 array 에 저장
									searchRankArray.push(result[i]);
								} else if(result[i].zlevel == 'A' || result[i].zlevel == 'B'){
									if(result[i].zlevel == 'A') {
										html += "<tr class='bg-blue'>";
										html += "<td colspan='4'>상위20%기준</td>";
									} else if(result[i].zlevel == 'B') {
										html += "<tr class='bg-grey'>";
										html += "<td colspan='4'>평균</td>";
									}
									html += "<td >" + result[i].rms_info_r + "%</td>";
									html += "<td >" + result[i].rms_card_r + "%</td>";
									html += "<td >" + result[i].rms_tran_r + "%</td>";
									html += "<td >" + result[i].rms_arre_r + "%</td>";
									html += "<td >" + result[i].rms_pay_r + "%</td>";
									html += "<td >" + result[i].rms_summ_r + "%</td>";
									html += "<td >" + result[i].rms_stmng_r + "%</td>";
									//html += "<td>" + result[i].zup_rank + "</td>";
									html += "</tr>";
									
									// 정렬에 사용하기위한 array 에 저장
									searchRankTotalArray.push(result[i]);
								}
							}
				    	}
			    	} else if (rank_cnt_gb == "ran") { // 순위
				    	
			    		if(rank_brch_gb == "BL") {
			    			$(".notall").css("display", "none");
			    			
							for ( var i in result) {
								if(result[i].zlevel == 'A' || result[i].zlevel == 'B') {
									if(result[i].zlevel == 'A') {
										html += "<tr class='bg-blue'>";
										html += "<td colspan='3'>상위20%기준</td>";
									} else if(result[i].zlevel == 'B') {
										html += "<tr class='bg-grey'>";
										html += "<td colspan='3'>평균</td>";
									}
									html += "<td>" + result[i].rms_info_r + "</td>";
									html += "<td>" + result[i].rms_card_r + "</td>";
									html += "<td>" + result[i].rms_tran_r + "</td>";
									html += "<td>" + result[i].rms_arre_r + "</td>";
									html += "<td>" + result[i].rms_pay_r + "</td>";
									html += "<td>" + result[i].rms_summ_r + "</td>";
									html += "<td>" + result[i].rms_stmng_r + "</td>";
									//html += "<td>" + result[i].zup_rank + "</td>";
									html += "</tr>";
									
									// 정렬에 사용하기위한 array 에 저장
									searchRankTotalArray.push(result[i]);
								} else {
									
									html += "<tr zreq_ym='" + result[i].zrms_ym + "' zregion='" + result[i].zregion + "' zdept_id='" + result[i].zdept_id + "' zdept_brch_id='" + result[i].zdept_brch_id + "'>";
									html += "<td style='color:#6799FF;'>" + result[i].zsub_rank + "</td>";
									html += "<td>" + result[i].zregion_nm  + "</td>";
									html += "<td>" + result[i].zdept_nm;
									if(isEmpty(result[i].zdept_emp_nm)) {
										html += "</td>";
									} else {
										html += "[" + result[i].zdept_emp_nm + "]" + "</td>";
									}
									html += "<td >" + result[i].rms_info_r + "</td>";
									html += "<td >" + result[i].rms_card_r + "</td>";
									html += "<td >" + result[i].rms_tran_r + "</td>";
									html += "<td >" + result[i].rms_arre_r + "</td>";
									html += "<td >" + result[i].rms_pay_r + "</td>";
									html += "<td >" + result[i].rms_summ_r + "</td>";
									html += "<td >" + result[i].rms_stmng_r + "</td>";
									//html += "<td>" + result[i].zup_rank + "</td>";
									html += "</tr>";

									// 정렬에 사용하기위한 array 에 저장
									searchRankArray.push(result[i]);
								}
							}
				    	} else {

					    	$(".notall").css("display", "");
							for ( var i in result) {
								if(result[i].zlevel == 'A' || result[i].zlevel == 'B') {
									if(result[i].zlevel == 'A') {
										html += "<tr class='bg-blue'>";
										html += "<td colspan='4'>상위20%기준</td>";
									} else if(result[i].zlevel == 'B') {
										html += "<tr class='bg-grey'>";
										html += "<td colspan='4'>평균</td>";
									}

									html += "<td>" + result[i].rms_info_r + "</td>";
									html += "<td>" + result[i].rms_card_r + "</td>";
									html += "<td>" + result[i].rms_tran_r + "</td>";
									html += "<td>" + result[i].rms_arre_r + "</td>";
									html += "<td>" + result[i].rms_pay_r + "</td>";
									html += "<td>" + result[i].rms_summ_r + "</td>";
									html += "<td>" + result[i].rms_stmng_r + "</td>";
									//html += "<td>" + result[i].zup_rank + "</td>";
									html += "</tr>";

									// 정렬에 사용하기위한 array 에 저장
									searchRankTotalArray.push(result[i]);
								} else {
									html += "<tr zreq_ym='" + result[i].zrms_ym + "' zregion='" + result[i].zregion + "' zdept_id='" + result[i].zdept_id + "' zdept_brch_id='" + result[i].zdept_brch_id + "'>";
									html += "<td style='color:#6799FF;'>" + result[i].zsub_rank + "</td>";
									html += "<td>" + result[i].zregion_nm  + "</td>";
									html += "<td>" + result[i].zdept_nm;
									if(isEmpty(result[i].zdept_emp_nm)) {
										html += "</td>";
									} else {
										html += "[" + result[i].zdept_emp_nm + "]" + "</td>";
									}

									html += "<td>" + result[i].zdept_brch_nm;
									if(isEmpty(result[i].zbrch_emp_nm)) {
										html += "</td>";
									} else {
										html += "[" + result[i].zbrch_emp_nm + "]" + "</td>";
									}
									html += "<td >" + result[i].rms_info_r + "</td>";
									html += "<td >" + result[i].rms_card_r + "</td>";
									html += "<td >" + result[i].rms_tran_r + "</td>";
									html += "<td >" + result[i].rms_arre_r + "</td>";
									html += "<td >" + result[i].rms_pay_r + "</td>";
									html += "<td >" + result[i].rms_summ_r + "</td>";
									html += "<td >" + result[i].rms_stmng_r + "</td>";
									//html += "<td>" + result[i].zup_rank + "</td>";
									html += "</tr>";
									
									// 정렬에 사용하기위한 array 에 저장
									searchRankArray.push(result[i]);
								}
							}
				    	}
			    	}

			    	
			    }
				
				$("#ran-main-tbody").append(html);

				$("#grpFilterBox").css("visibility", "hidden");
				//setClickEventRank();
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
		// 조회가 먼저 안된경우 동작안함
		if(searchRankArray.length < 1) {
			return;
		}
		
		var newArray = searchRankArray;

		var param = $(e).attr("value");
		//$(".th-order").attr("order", "");
		
		if($(e).attr("order") == "" || $(e).attr("order") == "desc") { // 오름차순 정렬시작
			newArray = searchRankArray.sort(function (a, b) {
				$(".th-order").attr("order", "");
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
				} else if(param == "zregion_nm") {
					return a.zregion_nm < b.zregion_nm ? -1 : a.zregion_nm > b.zregion_nm ? 1 : 0;
				} else if(param == "zdept_nm") {
					return a.zdept_nm < b.zdept_nm ? -1 : a.zdept_nm > b.zdept_nm ? 1 : 0;
				} else if(param == "zdept_brch_nm") {
					return a.zdept_brch_nm < b.zdept_brch_nm ? -1 : a.zdept_brch_nm > b.zdept_brch_nm ? 1 : 0;
				}

				/* else if(param == "zup_rank") {
					return a.zup_rank - b.zup_rank;
				}*/
			});
			
		} else if($(e).attr("order") == "asc") { // 내림차순 정렬시작
			newArray = searchRankArray.sort(function (b, a) {
				$(".th-order").attr("order", "");
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
				} else if(param == "zregion_nm") {
					return a.zregion_nm < b.zregion_nm ? -1 : a.zregion_nm > b.zregion_nm ? 1 : 0;
				} else if(param == "zdept_nm") {
					return a.zdept_nm < b.zdept_nm ? -1 : a.zdept_nm > b.zdept_nm ? 1 : 0;
				} else if(param == "zdept_brch_nm") {
					return a.zdept_brch_nm < b.zdept_brch_nm ? -1 : a.zdept_brch_nm > b.zdept_brch_nm ? 1 : 0;
				}

				/* else if(param == "zup_rank") {
					return a.zup_rank - b.zup_rank;
				}*/
			});
		}

		// 상위20% 기준, 평균 순으로 정렬
		searchRankTotalArray = searchRankTotalArray.sort(function (a, b) {
			return a.zlevel < b.zlevel ? -1 : a.zlevel > b.zlevel ? 1 : 0;
		});
		
		$("#ran-main-tbody").empty();
		
		var html = "";

		// rank_search_cnt_gb 는 조회시마다 변경된다.
		if(rank_search_cnt_gb == 'ind') { // 비중
			if(rank_search_brch_gb == "BL") { // 비중, BL

				// 평균, 상위20% 그리기
				for ( var i in searchRankTotalArray) {
					<!-- 정렬한 newArray를 다시 그린다. -->
					if(searchRankTotalArray[i].zlevel == 'A') {
						html += "<tr class='bg-blue'>";
						html += "<td colspan='3'>상위20%기준</td>";						
					} else {
						html += "<tr class='bg-grey'>";
						html += "<td colspan='3'>평균</td>";
					} 
					html += "<td>" + searchRankTotalArray[i].rms_info_r + "%</td>";
					html += "<td>" + searchRankTotalArray[i].rms_card_r + "%</td>";
					html += "<td>" + searchRankTotalArray[i].rms_tran_r + "%</td>";
					html += "<td>" + searchRankTotalArray[i].rms_arre_r + "%</td>";
					html += "<td>" + searchRankTotalArray[i].rms_pay_r + "%</td>";
					html += "<td>" + searchRankTotalArray[i].rms_summ_r + "%</td>";
					html += "<td>" + searchRankTotalArray[i].rms_stmng_r + "%</td>";
					//html += "<td>" + searchRankTotalArray[i].zup_rank + "</td>";
					html += "</tr>";
				}

				// 정렬된 항목 그리기
				for ( var i in newArray) {
					<!-- 정렬한 newArray를 다시 그린다. -->
					html += "<tr zreq_ym='" + newArray[i].zrms_ym + "' zregion='" + newArray[i].zregion + "' zdept_id='" + newArray[i].zdept_id + "' zdept_brch_id='" + newArray[i].zdept_brch_id + "'>";
					html += "<td style='color:#6799FF;'>" + newArray[i].zsub_rank + "</td>";
					html += "<td>" + newArray[i].zregion_nm  + "</td>";
					html += "<td>" + newArray[i].zdept_nm;
					if(isEmpty(newArray[i].zdept_emp_nm)) {
						html += "</td>";
					} else {
						html += "[" + newArray[i].zdept_emp_nm + "]" + "</td>";
					}
					html += "<td >" + newArray[i].rms_info_r + "%</td>";
					html += "<td >" + newArray[i].rms_card_r + "%</td>";
					html += "<td >" + newArray[i].rms_tran_r + "%</td>";
					html += "<td >" + newArray[i].rms_arre_r + "%</td>";
					html += "<td >" + newArray[i].rms_pay_r + "%</td>";
					html += "<td >" + newArray[i].rms_summ_r + "%</td>";
					html += "<td >" + newArray[i].rms_stmng_r + "%</td>";
					//html += "<td>" + newArray[i].zup_rank + "</td>";
					html += "</tr>";
				}
			} else { // 비중, LC

				// 평균, 상위20% 그리기
				for ( var i in searchRankTotalArray) {
					<!-- 정렬한 newArray를 다시 그린다. -->
					if(searchRankTotalArray[i].zlevel == 'A') {
						html += "<tr class='bg-blue'>";
						html += "<td colspan='4'>상위20%기준</td>";						
					} else {
						html += "<tr class='bg-grey'>";
						html += "<td colspan='4'>평균</td>";
					} 
					html += "<td>" + searchRankTotalArray[i].rms_info_r + "%</td>";
					html += "<td>" + searchRankTotalArray[i].rms_card_r + "%</td>";
					html += "<td>" + searchRankTotalArray[i].rms_tran_r + "%</td>";
					html += "<td>" + searchRankTotalArray[i].rms_arre_r + "%</td>";
					html += "<td>" + searchRankTotalArray[i].rms_pay_r + "%</td>";
					html += "<td>" + searchRankTotalArray[i].rms_summ_r + "%</td>";
					html += "<td>" + searchRankTotalArray[i].rms_stmng_r + "%</td>";
					//html += "<td>" + searchRankTotalArray[i].zup_rank + "</td>";
					html += "</tr>";
				}
				
				for ( var i in newArray) {
					<!-- 정렬한 newArray를 다시 그린다. -->
					html += "<tr zreq_ym='" + newArray[i].zrms_ym + "' zregion='" + newArray[i].zregion + "' zdept_id='" + newArray[i].zdept_id + "' zdept_brch_id='" + newArray[i].zdept_brch_id + "'>";
					html += "<td style='color:#47C83E;'>" + newArray[i].zsub_rank + "</td>";
					html += "<td>" + newArray[i].zregion_nm  + "</td>";
					html += "<td>" + newArray[i].zdept_nm;
					if(isEmpty(newArray[i].zdept_emp_nm)) {
						html += "</td>";
					} else {
						html += "[" + newArray[i].zdept_emp_nm + "]" + "</td>";
					}

					html += "<td>" + newArray[i].zdept_brch_nm;
					if(isEmpty(newArray[i].zbrch_emp_nm)) {
						html += "</td>";
					} else {
						html += "[" + newArray[i].zbrch_emp_nm + "]" + "</td>";
					}
					html += "<td >" + newArray[i].rms_info_r + "%</td>";
					html += "<td >" + newArray[i].rms_card_r + "%</td>";
					html += "<td >" + newArray[i].rms_tran_r + "%</td>";
					html += "<td >" + newArray[i].rms_arre_r + "%</td>";
					html += "<td >" + newArray[i].rms_pay_r + "%</td>";
					html += "<td >" + newArray[i].rms_summ_r + "%</td>";
					html += "<td >" + newArray[i].rms_stmng_r + "%</td>";
					//html += "<td>" + newArray[i].zup_rank + "</td>";
					html += "</tr>";
				}
			}
		} else if(rank_search_cnt_gb == 'ran') {
			if(rank_search_brch_gb == "BL") {
				// 평균, 상위20% 그리기
				for ( var i in searchRankTotalArray) {
					<!-- 정렬한 newArray를 다시 그린다. -->
					if(searchRankTotalArray[i].zlevel == 'A') {
						html += "<tr class='bg-blue'>";
						html += "<td colspan='3'>상위20%기준</td>";
					} else if(searchRankTotalArray[i].zlevel == 'B') {
						html += "<tr class='bg-grey'>";
						html += "<td colspan='3'>평균</td>";
					}
					html += "<td>" + searchRankTotalArray[i].rms_info_r + "</td>";
					html += "<td>" + searchRankTotalArray[i].rms_card_r + "</td>";
					html += "<td>" + searchRankTotalArray[i].rms_tran_r + "</td>";
					html += "<td>" + searchRankTotalArray[i].rms_arre_r + "</td>";
					html += "<td>" + searchRankTotalArray[i].rms_pay_r + "</td>";
					html += "<td>" + searchRankTotalArray[i].rms_summ_r + "</td>";
					html += "<td>" + searchRankTotalArray[i].rms_stmng_r + "</td>";
					//html += "<td>" + searchRankTotalArray[i].zup_rank + "</td>";
					html += "</tr>";
				}
				
				for ( var i in newArray) {
					<!-- 정렬한 newArray를 다시 그린다. -->
					html += "<tr zreq_ym='" + newArray[i].zrms_ym + "' zregion='" + newArray[i].zregion + "' zdept_id='" + newArray[i].zdept_id + "' zdept_brch_id='" + newArray[i].zdept_brch_id + "'>";
					html += "<td style='color:#6799FF;'>" + newArray[i].zsub_rank + "</td>";
					html += "<td>" + newArray[i].zregion_nm  + "</td>";
					html += "<td>" + newArray[i].zdept_nm;
					if(isEmpty(newArray[i].zdept_emp_nm)) {
						html += "</td>";
					} else {
						html += "[" + newArray[i].zdept_emp_nm + "]" + "</td>";
					}
					html += "<td >" + newArray[i].rms_info_r + "</td>";
					html += "<td >" + newArray[i].rms_card_r + "</td>";
					html += "<td >" + newArray[i].rms_tran_r + "</td>";
					html += "<td >" + newArray[i].rms_arre_r + "</td>";
					html += "<td >" + newArray[i].rms_pay_r + "</td>";
					html += "<td >" + newArray[i].rms_summ_r + "</td>";
					html += "<td >" + newArray[i].rms_stmng_r + "</td>";
					//html += "<td>" + newArray[i].zup_rank + "</td>";
					html += "</tr>";
				}
			} else {
				// 평균, 상위20% 그리기
				for ( var i in searchRankTotalArray) {
					<!-- 정렬한 newArray를 다시 그린다. -->
					if(searchRankTotalArray[i].zlevel == 'A') {
						html += "<tr class='bg-blue'>";
						html += "<td colspan='4'>상위20%기준</td>";
					} else if(searchRankTotalArray[i].zlevel == 'B') {
						html += "<tr class='bg-grey'>";
						html += "<td colspan='4'>평균</td>";
					}
					html += "<td>" + searchRankTotalArray[i].rms_info_r + "</td>";
					html += "<td>" + searchRankTotalArray[i].rms_card_r + "</td>";
					html += "<td>" + searchRankTotalArray[i].rms_tran_r + "</td>";
					html += "<td>" + searchRankTotalArray[i].rms_arre_r + "</td>";
					html += "<td>" + searchRankTotalArray[i].rms_pay_r + "</td>";
					html += "<td>" + searchRankTotalArray[i].rms_summ_r + "</td>";
					html += "<td>" + searchRankTotalArray[i].rms_stmng_r + "</td>";
					//html += "<td>" + searchRankTotalArray[i].zup_rank + "</td>";
					html += "</tr>";
				}
				
				for ( var i in newArray) {
					<!-- 정렬한 newArray를 다시 그린다. -->
					html += "<tr zreq_ym='" + newArray[i].zrms_ym + "' zregion='" + newArray[i].zregion + "' zdept_id='" + newArray[i].zdept_id + "' zdept_brch_id='" + newArray[i].zdept_brch_id + "'>";
					html += "<td style='color:#47C83E;'>" + newArray[i].zsub_rank + "</td>";
					html += "<td>" + newArray[i].zregion_nm  + "</td>";
					html += "<td>" + newArray[i].zdept_nm;
					if(isEmpty(newArray[i].zdept_emp_nm)) {
						html += "</td>";
					} else {
						html += "[" + newArray[i].zdept_emp_nm + "]" + "</td>";
					}

					html += "<td>" + newArray[i].zdept_brch_nm;
					if(isEmpty(newArray[i].zbrch_emp_nm)) {
						html += "</td>";
					} else {
						html += "[" + newArray[i].zbrch_emp_nm + "]" + "</td>";
					}
					html += "<td >" + newArray[i].rms_info_r + "</td>";
					html += "<td >" + newArray[i].rms_card_r + "</td>";
					html += "<td >" + newArray[i].rms_tran_r + "</td>";
					html += "<td >" + newArray[i].rms_arre_r + "</td>";
					html += "<td >" + newArray[i].rms_pay_r + "</td>";
					html += "<td >" + newArray[i].rms_summ_r + "</td>";
					html += "<td >" + newArray[i].rms_stmng_r + "</td>";
					//html += "<td>" + newArray[i].zup_rank + "</td>";
					html += "</tr>";
				}
			}
		} else {
			if(rank_search_brch_gb == "BL") { // 건수, BL

				// 평균, 상위20% 그리기
				for ( var i in searchRankTotalArray) {
					<!-- 정렬한 newArray를 다시 그린다. -->
					if(searchRankTotalArray[i].zlevel == 'A') {
						html += "<tr class='bg-blue'>";
						html += "<td colspan='3'>상위20%기준</td>";						
					} else {
						html += "<tr class='bg-grey'>";
						html += "<td colspan='3'>평균</td>";
					} 
					html += "<td>" + searchRankTotalArray[i].rms_info_r + "</td>";
					html += "<td>" + numberWithCommas(searchRankTotalArray[i].rms_card_r) + "</td>";
					html += "<td>" + searchRankTotalArray[i].rms_tran_r + "</td>";
					html += "<td>" + searchRankTotalArray[i].rms_arre_r + "</td>";
					html += "<td>" + searchRankTotalArray[i].rms_pay_r + "</td>";
					html += "<td>" + searchRankTotalArray[i].rms_summ_r + "</td>";
					html += "<td>" + searchRankTotalArray[i].rms_stmng_r + "</td>";
					//html += "<td>" + searchRankTotalArray[i].zup_rank + "</td>";
					html += "</tr>";
				}

				// 정렬된 항목 그리기
				for ( var i in newArray) {
					<!-- 정렬한 newArray를 다시 그린다. -->
					html += "<tr zreq_ym='" + newArray[i].zrms_ym + "' zregion='" + newArray[i].zregion + "' zdept_id='" + newArray[i].zdept_id + "' zdept_brch_id='" + newArray[i].zdept_brch_id + "'>";
					html += "<td style='color:#6799FF;'>" + newArray[i].zsub_rank + "</td>";
					html += "<td>" + newArray[i].zregion_nm  + "</td>";
					html += "<td>" + newArray[i].zdept_nm;
					if(isEmpty(newArray[i].zdept_emp_nm)) {
						html += "</td>";
					} else {
						html += "[" + newArray[i].zdept_emp_nm + "]" + "</td>";
					}
					html += "<td >" + newArray[i].rms_info_r + "</td>";
					html += "<td >" + numberWithCommas(newArray[i].rms_card_r) + "</td>";
					html += "<td >" + newArray[i].rms_tran_r + "</td>";
					html += "<td >" + newArray[i].rms_arre_r + "</td>";
					html += "<td >" + newArray[i].rms_pay_r + "</td>";
					html += "<td >" + newArray[i].rms_summ_r + "</td>";
					html += "<td >" + newArray[i].rms_stmng_r + "</td>";
					//html += "<td>" + newArray[i].zup_rank + "</td>";
					html += "</tr>";
				}
			} else { // 건수, LC

				// 평균, 상위20% 그리기
				for ( var i in searchRankTotalArray) {
					<!-- 정렬한 newArray를 다시 그린다. -->
					if(searchRankTotalArray[i].zlevel == 'A') {
						html += "<tr class='bg-blue'>";
						html += "<td colspan='4'>상위20%기준</td>";						
					} else {
						html += "<tr class='bg-grey'>";
						html += "<td colspan='4'>평균</td>";
					} 
					html += "<td>" + searchRankTotalArray[i].rms_info_r + "</td>";
					html += "<td>" + numberWithCommas(searchRankTotalArray[i].rms_card_r) + "</td>";
					html += "<td>" + searchRankTotalArray[i].rms_tran_r + "</td>";
					html += "<td>" + searchRankTotalArray[i].rms_arre_r + "</td>";
					html += "<td>" + searchRankTotalArray[i].rms_pay_r + "</td>";
					html += "<td>" + searchRankTotalArray[i].rms_summ_r + "</td>";
					html += "<td>" + searchRankTotalArray[i].rms_stmng_r + "</td>";
					//html += "<td>" + searchRankTotalArray[i].zup_rank + "</td>";
					html += "</tr>";
				}
				
				for ( var i in newArray) {
					<!-- 정렬한 newArray를 다시 그린다. -->
					html += "<tr zreq_ym='" + newArray[i].zrms_ym + "' zregion='" + newArray[i].zregion + "' zdept_id='" + newArray[i].zdept_id + "' zdept_brch_id='" + newArray[i].zdept_brch_id + "'>";
					html += "<td style='color:#47C83E;'>" + newArray[i].zsub_rank + "</td>";
					html += "<td>" + newArray[i].zregion_nm  + "</td>";
					html += "<td>" + newArray[i].zdept_nm;
					if(isEmpty(newArray[i].zdept_emp_nm)) {
						html += "</td>";
					} else {
						html += "[" + newArray[i].zdept_emp_nm + "]" + "</td>";
					}

					html += "<td>" + newArray[i].zdept_brch_nm;
					if(isEmpty(newArray[i].zbrch_emp_nm)) {
						html += "</td>";
					} else {
						html += "[" + newArray[i].zbrch_emp_nm + "]" + "</td>";
					}
					html += "<td >" + newArray[i].rms_info_r + "</td>";
					html += "<td >" + numberWithCommas(newArray[i].rms_card_r) + "</td>";
					html += "<td >" + newArray[i].rms_tran_r + "</td>";
					html += "<td >" + newArray[i].rms_arre_r + "</td>";
					html += "<td >" + newArray[i].rms_pay_r + "</td>";
					html += "<td >" + newArray[i].rms_summ_r + "</td>";
					html += "<td >" + newArray[i].rms_stmng_r + "</td>";
					//html += "<td>" + newArray[i].zup_rank + "</td>";
					html += "</tr>";
				}
			}
		}
		

		$("#ran-main-tbody").append(html);
		//setClickEventRank();
	}

	function changeGrp3(e) {
		if(e.id == "chkbx5") {
			$('input:checkbox[id="chkbx5"]').prop("checked", true);
			$('input:checkbox[id="chkbx6"]').prop("checked", false);
		} else {
			$('input:checkbox[id="chkbx5"]').prop("checked", false);
			$('input:checkbox[id="chkbx6"]').prop("checked", true);
		}
	}

	function changeGrp4(e) {
		if(e.id == "chkbx7") {
			$('input:checkbox[id="chkbx7"]').prop("checked", true);
			$('input:checkbox[id="chkbx8"]').prop("checked", false);
			$('input:checkbox[id="chkbx9"]').prop("checked", false);
			$('#th-crd-rank').html('카드결제</br>현황');
		} else if(e.id == "chkbx8") {
			$('input:checkbox[id="chkbx7"]').prop("checked", false);
			$('input:checkbox[id="chkbx8"]').prop("checked", true);
			$('input:checkbox[id="chkbx9"]').prop("checked", false);
			$('#th-crd-rank').html('카드결제<br>금액');
		} else if(e.id == "chkbx9") {
			$('input:checkbox[id="chkbx7"]').prop("checked", false);
			$('input:checkbox[id="chkbx8"]').prop("checked", false);
			$('input:checkbox[id="chkbx9"]').prop("checked", true);
			$('#th-crd-rank').html('카드결제<br>현황');
		}
	}

	function btnRankSearchFilterOpen() {
		if($("#sumOpenSearchRankFilter").css("visibility") == "visible") {
			$("#sumOpenSearchRankFilter").css("visibility", "hidden");
		} else {
			$("#sumOpenSearchRankFilter").css("visibility", "visible");
			$("#rank-calFilterBox").css("visibility", "hidden");
		}
	}

	function numberWithCommas(price) {
	    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}

	<!-- 안씀 2022.02.21 시은과장님 요청-->
	function setClickEventRank() {
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

			if(active_bcg_id == "N") {
				if(cls == "sum-item1") getContentTab("/ind/indNoonAuth.do", data);
				else if(cls == "sum-item2") getContentTab("/crd/crdNoonCard.do", data);
				else if(cls == "sum-item3") getContentTab("/trn/trnNoonStd.do", data);
				else if(cls == "sum-item4") getContentTab("/arr/arrNoonPay.do", data);
				else if(cls == "sum-item5") getContentTab("/pay/payNoonDirect.do", data);
				else if(cls == "sum-item6") getContentTab("/smi/smiNoonRisk.do", data);
				else if(cls == "sum-item7") getContentTab("/stm/stmNoonApply.do", data);
			} else if(active_bcg_id == "S") {
				if(cls == "sum-item1") getContentTab("/ind/indSolAuth.do", data);
				else if(cls == "sum-item2") getContentTab("/crd/crdSolCard.do", data);
				else if(cls == "sum-item3") getContentTab("/trn/trnSolStd.do", data);
				else if(cls == "sum-item4") getContentTab("/arr/arrSolPay.do", data);
				else if(cls == "sum-item5") getContentTab("/pay/paySolDirect.do", data);
				else if(cls == "sum-item6") getContentTab("/pay/paySolDirect.do", data);
				else if(cls == "sum-item7") getContentTab("/stm/stmSolApply.do", data);
			} else if(active_bcg_id == "C") {
				if(cls == "sum-item1") getContentTab("/ind/indChiAuth.do", data);
				else if(cls == "sum-item2") getContentTab("/crd/crdChiCard.do", data);
				else if(cls == "sum-item3") getContentTab("/trn/trnChiStd.do", data);
				else if(cls == "sum-item4") getContentTab("/arr/arrChiPay.do", data);
				else if(cls == "sum-item5") getContentTab("/pay/payChiDirect.do", data);
				else if(cls == "sum-item6") getContentTab("/pay/payChiDirect.do", data);
				else if(cls == "sum-item7") getContentTab("/stm/stmChiApply.do", data);
			}
			
		});
	}

</script>


<div id="mask-rank" class="mask" style="z-index: 800;"></div>   
<div id="layerbox-rank" class="layerpop" style="z-index: 900;">
	<div class="layerpop_area" style="width:1200px; height:900px;">
		<div>
	       <div class="title">사업운영모니터링 랭킹</div>
	       <div class="layerpop_close" onclick="javascript:closeRankPopup();">&#10799;</div>
	    </div>
	    <div class="popup-content" style="padding: 22px;">
	    
			<div class="filter-area">
				<div class="left">
					<%@ include file="../com/calendarRank.jsp"%>

					<div class="filtering select">
					    <div class="custom-input select-box" onclick="btnRankSearchFilterOpen()">
					    	<span>선택보기</span>
					    	<i class="fa fa-sort-amount-desc"></i>
					    	<!-- <i class="fa fa-caret-down"></i> -->
					    </div>
					    <c:choose>
								<c:when test="${active_bcg_id eq 'N'}">
								    <div class="filterBox option-box" id="sumOpenSearchRankFilter"><!-- style="height:162px;"> --> 
								    	<div>
											<div class="chkbx">
												<input type="checkbox" id="chkbx7" name="checkbox-btn" onchange="changeGrp4(this);" checked="checked">
												<label for="chkbx7">비중</label>
											</div>
											<div class="chkbx">
												<input type="checkbox" id="chkbx8" name="checkbox-btn" onchange="changeGrp4(this);">
												<label for="chkbx8">대상건수</label>
											</div>
											<div class="chkbx" style="display: none;">
												<input type="checkbox" id="chkbx9" name="checkbox-btn" onchange="changeGrp4(this);">
												<label for="chkbx9">순위</label>
											</div>
								    	</div>
								    	<div>
											<div class="chkbx">
												<input type="checkbox" id="chkbx5" name="checkbox-btn" onchange="changeGrp3(this);" checked="checked">
												<label for="chkbx5">방문</label> 
											</div>
											<div class="chkbx">
												<input type="checkbox" id="chkbx6" name="checkbox-btn" onchange="changeGrp3(this);">
												<label for="chkbx6">러닝</label>
											</div>						    		
								    	</div>
								    </div>
								</c:when>
								<c:otherwise>
								    <div class="filterBox option-box" id="sumOpenSearchRankFilter" style="height:80px;"><!-- style="height:162px;"> --> 
		    							<div style="text-align: left;">
											<div class="chkbx">
												<input type="checkbox" id="chkbx7" name="checkbox-btn" onchange="changeGrp4(this);" checked="checked">
												<label for="chkbx7">비중</label>
											</div>
											<div class="chkbx">
												<input type="checkbox" id="chkbx8" name="checkbox-btn" onchange="changeGrp4(this);">
												<label for="chkbx8">대상건수</label>
											</div>
											<div class="chkbx" style="display: none;">
												<input type="checkbox" id="chkbx9" name="checkbox-btn" onchange="changeGrp4(this);">
												<label for="chkbx9">순위</label>
											</div>
								    	</div>
								    </div>
								</c:otherwise>
						</c:choose>
					</div>
					
					<button class="btn-tb-top search" onclick="btnRankSearch();">검색</button>
				</div>
				<div class="right">
					<button class="btn-tb-top excel-1" onclick="openExcelPopupForSum(zregion, zdept, zdept_brch, zemp, yyyymm_rank, yyyymm_rank, active_bcg_id, 'excelMoniSum_r.do', '주요지표_랭킹.xlsx', rank_search_brch_gb, rank_search_cnt_gb);">엑셀다운</button>
				</div>
			</div>
			<div class="tbl">
				<table class="sortable">
					<colgroup>
						<col style="width: 5%;"> <%-- 관리순위 --%>
						<%-- <col style="width: 7%;"> --%> <%-- 개선순위 --%>
						<col style="width: 10%;"> <%-- 본부 --%>
						<col style="width: 13%;"> <%-- 교육국 --%>
						<col class="notall" id="none-center" style="display:none; width: 13%;"> <%-- 센터 --%>
						<col style="width: 6%;"> <%-- 개인정보 인증 --%>
						<col style="width: 6%;"> <%-- 카드결제 현황 --%>
						<col style="width: 6%;"> <%-- 이체제외 후 학습 --%>
						<col style="width: 6%;"> <%-- 2개월 체납 --%>
						<col style="width: 6%;"> <%-- 고객직접결제 중복발송 --%>
						<col style="width: 6%;"> <%-- 써밋제품 학습 --%>
						<col style="width: 6%;"> <%-- 긴급교재 신청 --%>
						<%-- <col style="width: 7%;"> --%>
					</colgroup>
					<thead>
						<tr>
							<th class="th-order" onClick="sortSearch(this)" order="" value="zsub_rank" style="cursor: pointer;">관리순위</th>
							<%-- <th class="th-order" onClick="sortSearch(this)" order="" value="zup_rank" style="cursor: pointer;">개선순위</th> --%>
							<th class="th-order" onClick="sortSearch(this)" order="" value="zregion_nm" style="cursor: pointer;">본부</th>
							<th class="th-order" onClick="sortSearch(this)" order="" value="zdept_nm" style="cursor: pointer;">교육국</th>
							<th class="th-order notall" onClick="sortSearch(this)" order="" value="zdept_brch_nm" style="cursor: pointer; display:none;">센터</th>
							<th class="th-order" onClick="sortSearch(this)" order="" value="rms_info_r" style="cursor: pointer;">개인정보</br>인증</th>
							<th class="th-order" id="th-crd-rank" onClick="sortSearch(this)" order="" value="rms_card_r" style="cursor: pointer;">카드결제</br>현황</th>
							<th class="th-order" onClick="sortSearch(this)" order="" value="rms_tran_r" style="cursor: pointer;">이체제외 후</br>학습</th>
							<th class="th-order" onClick="sortSearch(this)" order="" value="rms_arre_r" style="cursor: pointer;">2개월 체납</th>
							<th class="th-order" onClick="sortSearch(this)" order="" value="rms_pay_r" style="cursor: pointer;">고객직접결제</br>중복발송</th>
							<th class="th-order" onClick="sortSearch(this)" order="" value="rms_summ_r" style="cursor: pointer;">써밋제품 학습</th>
							<th class="th-order" onClick="sortSearch(this)" order="" value="rms_stmng_r" style="cursor: pointer;">긴급교재</br>신청</th>
						</tr>
					</thead>
				</table>
			</div>
			<div class="tbl" id="rank-tbody-div" style="overflow-y:auto; height: 650px; width: 1173px;">
				<table>
					<colgroup>
						<col style="width: 5%;"> <%-- 관리순위 --%>
						<%-- <col style="width: 7%;"> --%> <%-- 개선순위 --%>
						<col style="width: 10%;"> <%-- 본부 --%>
						<col style="width: 13%;"> <%-- 교육국 --%>
						<col class="notall" style="display:none; width: 13%;"> <%-- 센터 --%>
						<col style="width: 6%;"> <%-- 개인정보 인증 --%>
						<col style="width: 6%;"> <%-- 카드결제 현황 --%>
						<col style="width: 6%;"> <%-- 이체제외 후 학습 --%>
						<col style="width: 6%;"> <%-- 2개월 체납 --%>
						<col style="width: 6%;"> <%-- 직접결제 중복발송 --%>
						<col style="width: 6%;"> <%-- 써밋제품 학습 --%>
						<col style="width: 6%;"> <%-- 긴급교재 --%>
						<%-- <col style="width: 7%;"> --%>
					</colgroup>
					<tbody id="ran-main-tbody">
						<tr>
							<td colspan="11" style="font-size:14px;">조회결과가 없습니다.</td>
						</tr>
					</tbody>
				</table>
			</div>	
			<div class="left-info" id="data-rank-dt" style="visibility: hidden;">* Data 기준 : <span id="data-rank-dt-sum"></span></div>
				
			<div class="pop-btn">
	    		<a href="javascript:closeRankPopup();" class="btn-right">확 인</a>
      		</div>
		</div>
	</div>
</div>