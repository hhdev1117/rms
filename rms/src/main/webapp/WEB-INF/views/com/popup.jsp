<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<script type="text/javascript">
	function openPopup() {
        $('#layerbox-popup').css("position", "absolute");
        $('#layerbox-popup').css("top",(($(window).height() - $('#layerbox-popup').outerHeight()) / 2) + $(window).scrollTop()  - 200);
        $('#layerbox-popup').css("left",(($(window).width() - $('#layerbox-popup').outerWidth()) / 2) + $(window).scrollLeft() - 150);
        $('#layerbox-popup').draggable();
        $('#layerbox-popup').show();
	}

	function wrapWindowByMaskPopup() {
        var maskHeight = $(document).height(); 
        var maskWidth = $(window).width();
      
        $('#mask-popup').css({
            'width' : maskWidth,
            'height' : maskHeight
        });
        $('#mask-popup').fadeTo("slow", 0.5);
    }

    function closePopup() {
    	$('#layerbox-popup').hide();
    }
</script>   
<div id="layerbox-popup" class="layerpop" style="box-shadow: 2px 2px 2px grey;">
	<div class="layerpop_area" style="width:650px; height:330px;">
		<div>
	       <div class="title">안내</div>
	       <div class="layerpop_close" onclick="javascript:closePopup();">&#10799;</div>
	    </div>
	    <div class="popup-content" style="padding: 22px;">
	    	<div class="tbl">
	    	
					    	
				<div style="padding-bottom: 9px;"><span style="font-size: 16px;">1. 모니터링 신규 지표 추가 : 자동이체 중복계좌</span><br></div>
				<div style="padding-bottom: 9px;"><span style="font-size: 16px;">- 은행 및 카드 자동이체 회원 중 성이 다른 2명 이상의 회원이 한계좌에 등록되어 있는 경우</span><br></div>
				<div style="padding-bottom: 9px;"><span style="font-size: 16px;">※ 공공사업, 단체, B2B 회원 수치 제외, 조직원 및 조직원 자녀 제외</span><br></div>
				<div style="padding-bottom: 9px;"><span style="font-size: 16px;">※ 해당 지표는 7개 주요지표 外 세부지표에 해당 됩니다.</span><br><br></div>
				
				<div style="padding-bottom: 9px;"><span style="font-size: 16px;">2. 문의</span><br></div>
				<div style="padding-bottom: 9px;"><span style="font-size: 16px;">- 사업운영모니터링 시스템 운영 : 눈높이지원팀 장지원 (829-0543), 전한성 (829-0545)</span><br></div>
				<div style="padding-bottom: 9px;"><span style="font-size: 16px;">- 사업운영모니터링 시스템 문의 : IT사업지원팀 김시은 (829-0356)</span><br></div>	
	    	
	    		<%--
	    		<div style="padding-bottom: 9px;"><span style="font-size: 16px;">1. 모니터링 신규 지표 추가 : 0개월 학습</span><br></div>
				<div style="padding-bottom: 9px;"><span style="font-size: 16px;">- 전 월에 학습횟수 0으로 입회한 후, 해당 월에 학습횟수 0으로 퇴회한 과목</span><br><br></div>
				
				<div style="padding-bottom: 9px;"><span style="font-size: 16px;">2. 단체 등록 회원 시스템 반영</span><br></div>
				<div style="padding-bottom: 9px;"><span style="font-size: 16px;">- 단체회원으로 신청/등록된 회원 : 개인정보인증 수치 제외</span><br></div>
				<div style="padding-bottom: 9px;"><span style="font-size: 16px;">- 단체회원 학습으로 등록된 과목 : 카드결제현황 및 고객직접결제 중복발송 수치 제외</span><br><br></div>
				
				<div style="padding-bottom: 9px;"><span style="font-size: 16px;">3. 문의</span><br></div>
				<div style="padding-bottom: 9px;"><span style="font-size: 16px;">- 사업운영모니터링 시스템 운영 : 사업지원팀 장지원 (829-0543)</span><br></div>
				<div style="padding-bottom: 9px;"><span style="font-size: 16px;">- 사업운영모니터링 시스템 문의 : IT사업지원팀 김시은 (829-0356)</span><br></div>
				 --%>
			</div>
			<div class="pop-btn">
	      		<a href="javascript:closePopup();" class="btn-right">확인</a>
      		</div>
		</div>
	</div>
</div>