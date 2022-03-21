<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<script type="text/javascript">
	function openMenualPopup() {
        $('#layerbox-menual').css("position", "absolute");
        $('#layerbox-menual').css("top",(($(window).height() - $('#layerbox-menual').outerHeight()) / 2) + $(window).scrollTop());
        $('#layerbox-menual').css("left",(($(window).width() - $('#layerbox-menual').outerWidth()) / 2) + $(window).scrollLeft());
        $('#layerbox-menual').draggable();
        $('#layerbox-menual').show();
        
		wrapWindowByMaskMenual();
	}

	function wrapWindowByMaskMenual() {
        var maskHeight = $(document).height(); 
        var maskWidth = $(window).width();
      
        $('#mask-menual').css({
            'width' : maskWidth,
            'height' : maskHeight
        });
        $('#mask-menual').fadeTo("slow", 0.5);
    }

    function closeMenualPopup() {
    	$('#layerbox-menual').hide();
        $('#mask-menual').hide();
    }
</script>
<div id="mask-menual" class="mask"></div>   
<div id="layerbox-menual" class="layerpop">
	<div class="layerpop_area" style="width:840px; height:600px;">
		<div>
	       <div class="title">주요지표 정의</div>
	       <div class="layerpop_close" onclick="javascript:closeMenualPopup();">&#10799;</div>
	    </div>
	    <div class="popup-content" style="padding: 22px;">
	    	<div class="tbl">
				<table id="table-menual" style="border-collapse: collapse;">
					<colgroup>
						<col style="width:25%;">
						<col style="width:75%;">
					</colgroup>
					<thead>
						<tr>
							<th>구   분</th>
							<th>내   용</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>개인정보인증</td>
							<td class="menual-content">최근 1년 內 동일한 법정대리인 전화번호로 성이 다른 회원 3명 이상 인증 된 건수</td>
						</tr>
						<tr>
							<td>카드결제현황</td>
							<td class="menual-content">최근 1년 內 동일한 카드번호로 성이 다른 3명 이상 회원을 결제 한 과목 및 회비</td>
						</tr>
						<tr>
							<td>이체제외 후 학습</td>
							<td class="menual-content">자동이체 (은행,카드) 제외 후 당월 학습 중인 과목수</td>
						</tr>
						<tr>
							<td>2개월 체납</td>
							<td class="menual-content">당월 학습 과목 중에서 2개월 이상 체납 과목 (당월 포함)</td>
						</tr>
						<tr>
							<td>고객직접결제 중복발송</td>
							<td class="menual-content">해당월 발송한 URL 중 최근 1년 內 성이 다른 3명 이상 회원에게 발송 된 건수</td>
						</tr>
						<tr>
							<td>써밋제품 학습</td>
							<td class="menual-content">해당월 써밋 제품 학습 과목 중 로그인 이력이 없는 과목(입/복회, 전환, 퇴회, 체험학습 계정 제외)</td>
						</tr>
						<tr>
							<td>긴급교재 신청현황</td>
							<td class="menual-content">해당월 긴급 교재 신청 권수</td>
						</tr>
						<tr>
							<td>0개월 학습</td>
							<td class="menual-content">전월 학습횟수 0으로 입회한 후, 해당 월 학습횟수 0으로 퇴회한 과목</td>
						</tr>
						<tr>
							<td>자동이체중복</td>
							<td class="menual-content">동일 자동이체 계좌(은행, 카드)로 성이 다른 2명 이상의 회원이 등록 된 과목수</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="pop-btn">
	      		<a href="javascript:closeMenualPopup();" class="btn-right">확 인</a>
      		</div>
		</div>
	</div>
</div>