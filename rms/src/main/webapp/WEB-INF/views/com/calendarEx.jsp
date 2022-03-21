<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<script type="text/javascript">

	$(document).ready(function(e){
		<%-- 주요지표에서 넘어온 경우와 아닌경우 분기 --%>

		if(isEmpty(param_zreq_ym)) {
			<%-- 달력 초기화 --%>
			//setBeforeCalendar();
			if(calendarEx != "T") {
				setAfterCalendar();
			} else {
				setMonCalendar();
			}
		} else {
			setParamCalendar(param_zreq_ym);
		}
	});
	
	<%--=============================================================
	년도에서 좌우 클릭했을때 변경
	=============================================================--%>
	function setBeforeYear(flag) {
		var year = $("#calendar-before-year").html();
		// true == 증가
		if(flag == true) {
			$("#calendar-before-year").html(year * 1 + 1);
		} else {
			$("#calendar-before-year").html(year * 1 - 1);
		}
	}

	function setAfterYear(flag) {
		var year = $("#calendar-after-year").html();
		// true == 증가
		if(flag == true) {
			$("#calendar-after-year").html(year * 1 + 1);
		} else {
			$("#calendar-after-year").html(year * 1 - 1);
		}
	}

	<%--=============================================================
	월을 선택했을때 설정
	=============================================================--%>
	var before_yyyymm = "";
	function setBeforeDate() {
		var yyyymm = $("#calendar-before-year").html() + $('input[name="calendar-before-rdo"]:checked').val();
		<%--12개월 체크용--%>
		var plusyyyymm = (after_yyyymm.substr(0, 4)*1-1) + after_yyyymm.substr(4, 2);
		if(yyyymm*1 > after_yyyymm*1) {
			alert("시작월이 종료월보다 큽니다.");
			$("#calendar-before-year").html(before_yyyymm.substr(0, 4));
			$("#option-before" + (before_yyyymm.substr(4, 2) * 1)).trigger("click");
			$("#calBeforeFilterBox").css("visibility", "hidden");
			return;
		} else if(yyyymm*1 < '202007'*1) {
			alert("2020년 7월 이전 데이터는 조회되지 않습니다.");
			$("#calendar-before-year").html(before_yyyymm.substr(0, 4));
			$("#option-before" + (before_yyyymm.substr(4, 2) * 1)).trigger("click");
			$("#calBeforeFilterBox").css("visibility", "hidden");
			return;
		} else if(yyyymm*1 <= plusyyyymm*1) {
			alert("최대 12개월까지 조회가 가능합니다.");
			var setyear = plusyyyymm.substr(0, 4);
			var setmonth =  plusyyyymm.substr(4, 2)*1 + 1;
			if(setmonth > 12) {
				setmonth = "1";
				setyear = setyear*1+1;
			}
			$("#calendar-before-year").html(setyear);
			$("#option-before" + setmonth).trigger("click");
			$("#calBeforeFilterBox").css("visibility", "hidden");
			return;
		} else {
			before_yyyymm = yyyymm;
			$("#calendar-before-date").attr("placeholder", $("#calendar-before-year").html() + "/" + $('input[name="calendar-before-rdo"]:checked').val());
			$("#calBeforeFilterBox").css("visibility", "hidden");
		}
	}

	var after_yyyymm = "";
	function setAfterDate() {
		var yyyymm = $("#calendar-after-year").html() + $('input[name="calendar-after-rdo"]:checked').val();
		<%--12개월 체크용--%>
		var plusyyyymm = (before_yyyymm.substr(0, 4)*1+1) + before_yyyymm.substr(4, 2);
		
		if(yyyymm*1 < before_yyyymm*1) {
			alert("종료월이 시작월보다 작습니다.");
			$("#calendar-after-year").html(after_yyyymm.substr(0, 4));
			$("#option-after" + (after_yyyymm.substr(4, 2) * 1)).trigger("click");
		} else if(yyyymm*1 < '202007'*1) {
			alert("2020년 7월 이전 데이터는 조회되지 않습니다.");
			$("#calendar-after-year").html(before_yyyymm.substr(0, 4));
			$("#option-after" + (before_yyyymm.substr(4, 2) * 1)).trigger("click");
		} else if(yyyymm*1 >= plusyyyymm*1) {
			alert("최대 12개월까지 조회가 가능합니다.");
			var setyear = plusyyyymm.substr(0, 4);
			var setmonth =  plusyyyymm.substr(4, 2)*1 - 1 ;
			if(setmonth < 1) {
				setmonth = "12";
				setyear = setyear*1-1;
			}
			$("#calendar-after-year").html(setyear);
			$("#option-after" + setmonth).trigger("click");
			$("#calAfterFilterBox").css("visibility", "hidden");
			return;
		} else {
			after_yyyymm = yyyymm;
			$("#calendar-after-date").attr("placeholder", $("#calendar-after-year").html() + "/" + $('input[name="calendar-after-rdo"]:checked').val());
		}

		$("#calAfterFilterBox").css("visibility", "hidden");
	}

	<%--=============================================================
	달력 초기화
	=============================================================--%>

	<%--=============================================================
	월별 모니터링 달력 초기화
	월별 모니터링은 1년단위 조회
	=============================================================--%>
	function setMonCalendar() {
		var today = new Date();

		var before_year = today.getFullYear() - 1; // 전 날짜는 1년전으로 설정
		//var before_mon = today.getMonth() + 1;
		var before_mon = today.getMonth();

		var after_year = today.getFullYear();
		var after_mon = today.getMonth();

		<%-- 1월인 경우 작년 12월로 날짜 설정. --%>
		if(before_mon == "0") {
			before_year -= 1;
			before_mon = 12;
		}

		<%-- 1월인 경우 작년 12월로 날짜 설정. --%>
		if(after_mon == "0") {
			after_year -= 1;
			after_mon = 12;
		}

		var nyear = before_year + before_mon;
		if(nyear*1 < "202007"*1) {
			before_year = "2020";
			before_mon = "7";
		}

		<%-- 달력 안에 연도 변경 --%>
		$("#calendar-after-year").html(after_year);
		$("#calendar-before-year").html(before_year);

		<%-- 현재 월을 선택으로 한다. --%>
		$("input[id='option-after" + after_mon + "']").attr("checked", "checked");
		$("input[id='option-before" + before_mon + "']").attr("checked", "checked");

		<%-- 한자릿수 월은 앞에 0붙여줌 --%>
		if(after_mon < 10) after_mon = "0" + after_mon;
		if(before_mon < 10) before_mon = "0" + before_mon;

		after_yyyymm = (after_year + "") + (after_mon + "");
		before_yyyymm = (before_year + "") + (before_mon + "");
			
		<%-- calendar 텍스트 변경 --%>
		$("#calendar-after-date").attr("placeholder", after_year + "/" + after_mon);
		$("#calendar-before-date").attr("placeholder", before_year + "/" + before_mon);
	}

	function setAfterCalendar() {
		var today = new Date();

		var year = today.getFullYear();
		//var mon = today.getMonth() + 1;
		var mon = today.getMonth();

		<%-- 1월인 경우 작년 12월로 날짜 설정. --%>
		if(mon == "0") {
			year -= 1;
			mon = 12;
		}

		<%-- 달력 안에 연도 변경 --%>
		$("#calendar-after-year").html(year);
		$("#calendar-before-year").html(year);

		<%-- 현재 월을 선택으로 한다. --%>
		$("input[id='option-after" + mon + "']").attr("checked", "checked");
		$("input[id='option-before" + mon + "']").attr("checked", "checked");

		<%-- 한자릿수 월은 앞에 0붙여줌 --%>
		if(mon < 10) mon = "0" + mon;

		after_yyyymm = (year + "") + (mon + "");
		before_yyyymm = (year + "") + (mon + "");
			
		<%-- calendar 텍스트 변경 --%>
		$("#calendar-before-date").attr("placeholder", year + "/" + mon);
		$("#calendar-after-date").attr("placeholder", year + "/" + mon);
	}
	
	<%--=============================================================
	주요지표에서 넘어온 경우 초기화
	=============================================================--%>
	function setParamCalendar(param) {
		var year = param.substring(0, 4) * 1;
		var mon = param.substring(4, 6) * 1;

		var nyear = param;
		if(nyear*1 < "202007"*1) {
			year = "2020";
			mon = "7";
		}

		<%-- 달력 안에 연도 변경 --%>
		$("#calendar-before-year, #calendar-after-year").html(year);

		<%-- 현재 월을 선택으로 한다. --%>
		$("input[id='option-before" + mon + "'], input[id='option-after" + mon + "']").attr("checked", "checked");
		
		<%-- 한자릿수 월은 앞에 0붙여줌 --%>
		if(mon < 10) mon = "0" + mon;
		
		before_yyyymm = (year + "") + (mon + "");
		after_yyyymm = (year + "") + (mon + "");
		
		<%-- calendar 텍스트 변경 --%>
		$("#calendar-before-date, #calendar-after-date").attr("placeholder", year + "/" + mon);
	}
	
	<%--=============================================================
	달력 눌렀을때 열려있으면 닫고, 닫혀있으면 연다.
	=============================================================--%>
	function btnBeforeCalendarOpen() {
		if($("#calBeforeFilterBox").css("visibility") == "visible") {
			$("#calBeforeFilterBox").css("visibility", "hidden");
		} else {
			$("#calBeforeFilterBox").css("visibility", "visible");

			<%-- 후 날짜 닫기 --%>
			$("#calAfterFilterBox").css("visibility", "hidden");
		}
	}

	function btnAfterCalendarOpen() {
		if($("#calAfterFilterBox").css("visibility") == "visible") {
			$("#calAfterFilterBox").css("visibility", "hidden");
		} else {
			$("#calAfterFilterBox").css("visibility", "visible");

			<%-- 선 날짜 닫기 --%>
			$("#calBeforeFilterBox").css("visibility", "hidden");
		}
	}
	
</script>
<div class="filtering">
	<div class="custom-input date" onclick="btnBeforeCalendarOpen();">
		<input type="text" id="calendar-before-date" placeholder="" style="cursor: pointer;" disabled>
		<a href="javascript:void(0);" class="icon_calender"><img src="${webPath}/images/icon_calender.png" alt=""></a>
	</div>
	<div class="filterBox calender-box" id="calBeforeFilterBox">
		<div class="year">
			<span><img src="${webPath}/images/icon_arrow_left.png" onclick="setBeforeYear(false);" style="cursor: pointer;"></span>
			<span class="txt-year" id="calendar-before-year"></span><span class="txt-year">년</span>
			<span><img src="${webPath}/images/icon_arrow_right.png" onclick="setBeforeYear(true);" style="cursor: pointer;"></span>
		</div>
		<div>
			<input type="radio" name="calendar-before-rdo" id="option-before1" class="month" value="01" onclick="setBeforeDate();" >
				<label for="option-before1">1월</label>
			<input type="radio" name="calendar-before-rdo" id="option-before2" class="month" value="02" onclick="setBeforeDate();">
				<label for="option-before2">2월</label>
			<input type="radio" name="calendar-before-rdo" id="option-before3" class="month" value="03" onclick="setBeforeDate();">
				<label for="option-before3">3월</label>
			<input type="radio" name="calendar-before-rdo" id="option-before4" class="month" value="04" onclick="setBeforeDate();">
				<label for="option-before4">4월</label>
			<input type="radio" name="calendar-before-rdo" id="option-before5" class="month" value="05" onclick="setBeforeDate();">
				<label for="option-before5">5월</label>
			<input type="radio" name="calendar-before-rdo" id="option-before6" class="month" value="06" onclick="setBeforeDate();">
				<label for="option-before6">6월</label>
			<input type="radio" name="calendar-before-rdo" id="option-before7" class="month" value="07" onclick="setBeforeDate();">
				<label for="option-before7">7월</label>
			<input type="radio" name="calendar-before-rdo" id="option-before8" class="month" value="08" onclick="setBeforeDate();">
				<label for="option-before8">8월</label>
			<input type="radio" name="calendar-before-rdo" id="option-before9" class="month" value="09" onclick="setBeforeDate();">
				<label for="option-before9">9월</label>
			<input type="radio" name="calendar-before-rdo" id="option-before10" class="month" value="10" onclick="setBeforeDate();">
				<label for="option-before10">10월</label>
			<input type="radio" name="calendar-before-rdo" id="option-before11" class="month" value="11" onclick="setBeforeDate();">
				<label for="option-before11">11월</label>
			<input type="radio" name="calendar-before-rdo" id="option-before12" class="month" value="12" onclick="setBeforeDate();">
				<label for="option-before12">12월</label>
		</div>
	</div>							
</div>						
<span>~&nbsp;</span>
<div class="filtering">
	<div class="custom-input date" onclick="btnAfterCalendarOpen();">
		<input type="text" id="calendar-after-date" placeholder="" style="cursor: pointer;" disabled>
		<a href="javascript:void(0);" class="icon_calender"><img src="${webPath}/images/icon_calender.png" alt=""></a>
	</div>
	<div class="filterBox calender-box" id="calAfterFilterBox">
		<div class="year">
			<span><img src="${webPath}/images/icon_arrow_left.png" onclick="setAfterYear(false);" style="cursor: pointer;"></span>
			<span class="txt-year" id="calendar-after-year"></span><span class="txt-year">년</span>
			<span><img src="${webPath}/images/icon_arrow_right.png" onclick="setAfterYear(true);" style="cursor: pointer;"></span>
		</div>
		<div>
			<input type="radio" name="calendar-after-rdo" id="option-after1" class="month" value="01" onclick="setAfterDate();" >
				<label for="option-after1">1월</label>
			<input type="radio" name="calendar-after-rdo" id="option-after2" class="month" value="02" onclick="setAfterDate();">
				<label for="option-after2">2월</label>
			<input type="radio" name="calendar-after-rdo" id="option-after3" class="month" value="03" onclick="setAfterDate();">
				<label for="option-after3">3월</label>
			<input type="radio" name="calendar-after-rdo" id="option-after4" class="month" value="04" onclick="setAfterDate();">
				<label for="option-after4">4월</label>
			<input type="radio" name="calendar-after-rdo" id="option-after5" class="month" value="05" onclick="setAfterDate();">
				<label for="option-after5">5월</label>
			<input type="radio" name="calendar-after-rdo" id="option-after6" class="month" value="06" onclick="setAfterDate();">
				<label for="option-after6">6월</label>
			<input type="radio" name="calendar-after-rdo" id="option-after7" class="month" value="07" onclick="setAfterDate();">
				<label for="option-after7">7월</label>
			<input type="radio" name="calendar-after-rdo" id="option-after8" class="month" value="08" onclick="setAfterDate();">
				<label for="option-after8">8월</label>
			<input type="radio" name="calendar-after-rdo" id="option-after9" class="month" value="09" onclick="setAfterDate();">
				<label for="option-after9">9월</label>
			<input type="radio" name="calendar-after-rdo" id="option-after10" class="month" value="10" onclick="setAfterDate();">
				<label for="option-after10">10월</label>
			<input type="radio" name="calendar-after-rdo" id="option-after11" class="month" value="11" onclick="setAfterDate();">
				<label for="option-after11">11월</label>
			<input type="radio" name="calendar-after-rdo" id="option-after12" class="month" value="12" onclick="setAfterDate();">
				<label for="option-after12">12월</label>
		</div>
	</div>							
</div>