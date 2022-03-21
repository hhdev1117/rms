<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<script type="text/javascript">
	$(document).ready(function(e){
		<%-- 달력 초기화 --%>
		setRankCalendar();
	});

	<%--=============================================================
	년도에서 좌우 클릭했을때 변경
	=============================================================--%>
	function setRankYear(flag) {
		var year = $("#rank-calendar-year").html();
		<%-- true == 증가 --%>
		if(flag == true) {
			$("#rank-calendar-year").html(year * 1 + 1);
		} else {
			$("#rank-calendar-year").html(year * 1 - 1);
		}
	}

	<%--=============================================================
	월을 선택했을때 설정
	=============================================================--%>
	var yyyymm_rank = "";
	function setRankDate() {
		yyyymm_rank = $("#rank-calendar-year").html() + $('input[name="rank-calendar-rdo"]:checked').val();
		$("#rank-calendar-date").attr("placeholder", $("#rank-calendar-year").html() + "/" + $('input[name="rank-calendar-rdo"]:checked').val());

		$("#rank-calFilterBox").css("visibility", "hidden");
	}

	<%--=============================================================
	달력 초기화
	=============================================================--%>
	function setRankCalendar() {
		var today = new Date();

		var year = today.getFullYear();
		//var mon = today.getMonth() + 1;
		var mon = today.getMonth();

		<%-- 1월인 경우 작년 12월로 날짜 설정. --%>
		if(mon == "0") {
			year -= 1;
			mon = 12;
		}

		<%-- 연도를 현재 연도로 설정 --%>
		$("#rank-calendar-year").html(year);

		<%-- 현재 월을 선택으로 한다. --%>
		$("input[class='rank-month']").prop("checked", false);
		$("input[id='rank-option" + mon + "']").attr("checked", "checked");

		$("input[id='rank-option" + mon + "']").trigger("click");

		<%-- 한자릿수 월은 앞에 0붙여줌 --%>
		if(mon < 10) mon = "0" + mon;

		yyyymm_rank = (year + "") + (mon + "");
			
		<%-- calendar 텍스트 변경 --%>
		$("#rank-calendar-date").attr("placeholder", year + "/" + mon);
	}

	<%--=============================================================
	달력 눌렀을때 열려있으면 닫고, 닫혀있으면 연다.
	=============================================================--%>
	function btnRankCalendarOpen() {
		if($("#rank-calFilterBox").css("visibility") == "visible") {
			$("#rank-calFilterBox").css("visibility", "hidden");
		} else {
			$("#sumOpenSearchRankFilter").css("visibility", "hidden");
			$("#rank-calFilterBox").css("visibility", "visible");
		}
	}
	
</script>
<div class="filtering">
	<div class="custom-input date" onclick="btnRankCalendarOpen();">
		<input type="text" id="rank-calendar-date" placeholder="" style="cursor: pointer;" disabled>
		<a href="javascript:void(0);" class="icon_calender"><img src="${webPath}/images/icon_calender.png" alt=""></a>
	</div>	
	<div class="filterBox calender-box" id="rank-calFilterBox">
		<div class="year">
			<span><img src="${webPath}/images/icon_arrow_left.png" onclick="setRankYear(false);" style="cursor: pointer;" class="calc-pointer"></span>
			<span class="txt-year" id="rank-calendar-year">2021</span><span class="txt-year">년</span>
			<span><img src="${webPath}/images/icon_arrow_right.png" onclick="setRankYear(true);" style="cursor: pointer;" class="calc-pointer"></span>
		</div>
		<div>
			<input type="radio" name="rank-calendar-rdo" id="rank-option1" class="month rank-month" value="01" onclick="setRankDate();" >
				<label for="rank-option1">1월</label>
			<input type="radio" name="rank-calendar-rdo" id="rank-option2" class="month rank-month" value="02" onclick="setRankDate();">
				<label for="rank-option2">2월</label>
			<input type="radio" name="rank-calendar-rdo" id="rank-option3" class="month rank-month" value="03" onclick="setRankDate();">
				<label for="rank-option3">3월</label>
			<input type="radio" name="rank-calendar-rdo" id="rank-option4" class="month rank-month" value="04" onclick="setRankDate();">
				<label for="rank-option4">4월</label>
			<input type="radio" name="rank-calendar-rdo" id="rank-option5" class="month rank-month" value="05" onclick="setRankDate();">
				<label for="rank-option5">5월</label>
			<input type="radio" name="rank-calendar-rdo" id="rank-option6" class="month rank-month" value="06" onclick="setRankDate();">
				<label for="rank-option6">6월</label>
			<input type="radio" name="rank-calendar-rdo" id="rank-option7" class="month rank-month" value="07" onclick="setRankDate();">
				<label for="rank-option7">7월</label>
			<input type="radio" name="rank-calendar-rdo" id="rank-option8" class="month rank-month" value="08" onclick="setRankDate();">
				<label for="rank-option8">8월</label>
			<input type="radio" name="rank-calendar-rdo" id="rank-option9" class="month rank-month" value="09" onclick="setRankDate();">
				<label for="rank-option9">9월</label>
			<input type="radio" name="rank-calendar-rdo" id="rank-option10" class="month rank-month" value="10" onclick="setRankDate();">
				<label for="rank-option10">10월</label>
			<input type="radio" name="rank-calendar-rdo" id="rank-option11" class="month rank-month" value="11" onclick="setRankDate();">
				<label for="rank-option11">11월</label>
			<input type="radio" name="rank-calendar-rdo" id="rank-option12" class="month rank-month" value="12" onclick="setRankDate();">
				<label for="rank-option12">12월</label>
		</div>
	</div>
</div>