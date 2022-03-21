<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<script type="text/javascript">
	$(document).ready(function(e){
		<%-- 달력 초기화 --%>
		setCalendar();
	});

	<%--=============================================================
	년도에서 좌우 클릭했을때 변경
	=============================================================--%>
	function setYear(flag) {
		var year = $("#calendar-year").html();
		<%-- true == 증가 --%>
		if(flag == true) {
			$("#calendar-year").html(year * 1 + 1);
		} else {
			$("#calendar-year").html(year * 1 - 1);
		}
	}

	<%--=============================================================
	월을 선택했을때 설정
	=============================================================--%>
	var yyyymm = "";
	function setDate() {
		yyyymm = $("#calendar-year").html() + $('input[name="calendar-rdo"]:checked').val();
		$("#calendar-date").attr("placeholder", $("#calendar-year").html() + "/" + $('input[name="calendar-rdo"]:checked').val());

		$("#calFilterBox").css("visibility", "hidden");
	}

	<%--=============================================================
	달력 초기화
	=============================================================--%>
	function setCalendar() {
		var today = new Date();

		var year = today.getFullYear();
		//var mon = today.getMonth() + 1;
		var mon = today.getMonth();

		<%-- 1월인 경우 작년 12월로 날짜 설정. --%>
		if(mon == "0") {
			year -= 1;
			mon = 12;
		}

		$("#calendar-year").html(year);

		<%-- 현재 월을 선택으로 한다. --%>
		$("input[id='option" + mon + "']").attr("checked", "checked");

		<%-- 한자릿수 월은 앞에 0붙여줌 --%>
		if(mon < 10) mon = "0" + mon;

		yyyymm = (year + "") + (mon + "");
			
		<%-- calendar 텍스트 변경 --%>
		$("#calendar-date").attr("placeholder", year + "/" + mon);
	}

	<%--=============================================================
	달력 눌렀을때 열려있으면 닫고, 닫혀있으면 연다.
	=============================================================--%>
	function btnCalendarOpen() {
		if($("#calFilterBox").css("visibility") == "visible") {
			$("#calFilterBox").css("visibility", "hidden");
		} else {
			$("#sumOpenSearchFilter").css("visibility", "hidden");
			$("#calFilterBox").css("visibility", "visible");
		}
	}
	
</script>
<div class="filtering">
	<div class="custom-input date" onclick="btnCalendarOpen();">
		<input type="text" id="calendar-date" placeholder="" style="cursor: pointer;" disabled>
		<a href="javascript:void(0);" class="icon_calender"><img src="${webPath}/images/icon_calender.png" alt=""></a>
	</div>	
	<div class="filterBox calender-box" id="calFilterBox">
		<div class="year">
			<span><img src="${webPath}/images/icon_arrow_left.png" onclick="setYear(false);" style="cursor: pointer;"></span>
			<span class="txt-year" id="calendar-year"></span><span class="txt-year">년</span>
			<span><img src="${webPath}/images/icon_arrow_right.png" onclick="setYear(true);" style="cursor: pointer;"></span>
		</div>
		<div>
			<input type="radio" name="calendar-rdo" id="option1" class="month" value="01" onclick="setDate();" >
				<label for="option1">1월</label>
			<input type="radio" name="calendar-rdo" id="option2" class="month" value="02" onclick="setDate();">
				<label for="option2">2월</label>
			<input type="radio" name="calendar-rdo" id="option3" class="month" value="03" onclick="setDate();">
				<label for="option3">3월</label>
			<input type="radio" name="calendar-rdo" id="option4" class="month" value="04" onclick="setDate();">
				<label for="option4">4월</label>
			<input type="radio" name="calendar-rdo" id="option5" class="month" value="05" onclick="setDate();">
				<label for="option5">5월</label>
			<input type="radio" name="calendar-rdo" id="option6" class="month" value="06" onclick="setDate();">
				<label for="option6">6월</label>
			<input type="radio" name="calendar-rdo" id="option7" class="month" value="07" onclick="setDate();">
				<label for="option7">7월</label>
			<input type="radio" name="calendar-rdo" id="option8" class="month" value="08" onclick="setDate();">
				<label for="option8">8월</label>
			<input type="radio" name="calendar-rdo" id="option9" class="month" value="09" onclick="setDate();">
				<label for="option9">9월</label>
			<input type="radio" name="calendar-rdo" id="option10" class="month" value="10" onclick="setDate();">
				<label for="option10">10월</label>
			<input type="radio" name="calendar-rdo" id="option11" class="month" value="11" onclick="setDate();">
				<label for="option11">11월</label>
			<input type="radio" name="calendar-rdo" id="option12" class="month" value="12" onclick="setDate();">
				<label for="option12">12월</label>
		</div>
	</div>
</div>