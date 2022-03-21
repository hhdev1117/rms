<!-- 
	엑셀다운하는 페이지
	모달로 비밀번호를 입력하도록 한다.
 -->
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<script type="text/javascript">
	<%--=============================================================
	Param값으로 엑셀팝업이 오픈될때 받아야 되는 변수들
	=============================================================--%>
	var excel_zregion = ""; 
	var excel_zdept_id = "";
	var excel_zdept_brch_id = "";
	var excel_zemp_id = "";
	var excel_zemp_id = "";
	var excel_before_zreq_ym = "";
	var excel_after_zreq_ym = "";
	var excel_active_bcg_id = "";
	var excel_url = "";
	var excel_filename = "";
	var excel_brch_gb = "";
	var excel_cnt_gb = "";

    function wrapWindowByMask() {
        var maskHeight = $(document).height(); 
        var maskWidth = $(window).width();
      
        $('#mask-excel').css({
            'width' : maskWidth,
            'height' : maskHeight
        });
        $('#mask-excel').fadeTo("slow", 0.5);
    }

	<%--=============================================================
	엑셀 팝업 오픈
	Param : 
		p_zregion 			==> 본부
		p_zdept_id			==> 사업국
		p_zdept_brch_id 	==> 팀
		p_zemp_id 			==> 교사(사번)
		p_yyyymmdd 			==> 조회년월
		p_active_bcg_id 	==> 활성화ID / N, C, S
		p_url 				==> 요청 URL
	=============================================================--%>
	function openExcelPopup(p_zregion, p_zdept_id, p_zdept_brch_id, p_zemp_id, p_before_yyyymmdd, p_after_yyyymmdd, p_active_bcg_id, p_url, p_filename, p_brch_gb) {

		
		excel_zregion = p_zregion;
		excel_zdept_id = p_zdept_id;
		excel_zdept_brch_id = p_zdept_brch_id;
		excel_zemp_id = p_zemp_id;
		excel_before_zreq_ym = p_before_yyyymmdd;
		excel_after_zreq_ym = p_after_yyyymmdd;
		excel_active_bcg_id = p_active_bcg_id;
		excel_url = p_url;
		excel_filename = p_filename;
		excel_brch_gb = p_brch_gb;

		$("#exl-pw").val("");
		
        $('#layerbox-excel').css("position", "absolute");
        $('#layerbox-excel').css("top",(($(window).height() - $('#layerbox-excel').outerHeight()) / 2) + $(window).scrollTop());
        $('#layerbox-excel').css("left",(($(window).width() - $('#layerbox-excel').outerWidth()) / 2) + $(window).scrollLeft());
        $('#layerbox-excel').show();

        wrapWindowByMask();
	}

	<%--=============================================================
		엑셀 팝업 오픈 주요지표용
		Param : 
			p_zregion 			==> 본부
			p_zdept_id			==> 사업국
			p_zdept_brch_id 	==> 팀
			p_zemp_id 			==> 교사(사번)
			p_yyyymmdd 			==> 조회년월
			p_active_bcg_id 	==> 활성화ID / N, C, S
			p_url 				==> 요청 URL
		=============================================================--%>
		function openExcelPopupForSum(p_zregion, p_zdept_id, p_zdept_brch_id, p_zemp_id, p_before_yyyymmdd, p_after_yyyymmdd, p_active_bcg_id, p_url, p_filename, p_brch_gb, p_cnt_gb) {

			
			excel_zregion = p_zregion;
			excel_zdept_id = p_zdept_id;
			excel_zdept_brch_id = p_zdept_brch_id;
			excel_zemp_id = p_zemp_id;
			excel_before_zreq_ym = p_before_yyyymmdd;
			excel_after_zreq_ym = p_after_yyyymmdd;
			excel_active_bcg_id = p_active_bcg_id;
			excel_url = p_url;
			excel_filename = p_filename;
			excel_brch_gb = p_brch_gb;
			excel_cnt_gb = p_cnt_gb;

			$("#exl-pw").val("");
			
	        $('#layerbox-excel').css("position", "absolute");
	        $('#layerbox-excel').css("top",(($(window).height() - $('#layerbox-excel').outerHeight()) / 2) + $(window).scrollTop());
	        $('#layerbox-excel').css("left",(($(window).width() - $('#layerbox-excel').outerWidth()) / 2) + $(window).scrollLeft());
	        $('#layerbox-excel').show();

	        wrapWindowByMask();
		}
	<%--=============================================================
	엑셀 팝업 클로즈
	=============================================================--%>
	function closeExcelPopup() {
        $('#layerbox-excel').hide();
        $('#mask-excel').hide();
	}

	<%--=============================================================
	엔터키 입력 이벤트
	=============================================================--%>
	function enterkey() { 
		if (window.event.keyCode == 13) {
			excelDownload();
		} 
	}

	<%--=============================================================
	엑셀 다운로드
	=============================================================--%>
	function excelDownload() {
		if(isEmpty($("#exl-pw").val())) {
			alert("엑셀 비밀번호를 입력해주세요.");
			return;
		}

		closeExcelPopup();
		
		<%--=============================================================
			엑셀다운시 조회조건 파라미터값
		=============================================================--%>
		var obj = new Object();
		obj.passwd = $("#exl-pw").val();
		obj.zregion = excel_zregion;
		obj.zdept_id = excel_zdept_id;
		obj.zdept_brch_id = excel_zdept_brch_id;
		obj.zemp_id = excel_zemp_id;
		obj.zreq_ym_b = excel_before_zreq_ym;
		obj.zreq_ym_a = excel_after_zreq_ym;
		obj.active_bcg_id = excel_active_bcg_id;
		obj.brch_gb = excel_brch_gb;
		obj.cnt_gb = excel_cnt_gb;

		<%--=============================================================
		동작방식
			파일다운로드는 에러시 에러코드값 전달이 안됨.
			따라서, AJAX호출로 세션체크를 하기위한
			임시 컨트롤러를 호출하고 성공값을 받으면
			JQUERY 파일다운로더를 통해 파일다운을 한다. 
		=============================================================--%>

		$.ajax({ 
	        type : "POST",
	        dataType : "text",
	        url : "${ctx}/exl/sessionCheck.do",
	        success : function(result){
	        	openLoading();
	        	closeExcelPopup();
				$.fileDownload("${ctx}/exl/" + excel_url, {
					httpMethod : "POST",
					data : $.param(obj),
					successCallback : function(url) {
						closeLoading();
					},
					failCallback : function(responesHtml, url) {
						closeLoading();
						if(responesHtml == "401") {
							alert("해당 엑셀다운 권한이 없습니다. 관리자에게 문의바랍니다.");
						} else {
							alert("엑셀다운 에러가 발생하였습니다.");
						}
						return;
					}
				});
		    },beforeSend:function(xmlHttpRequest){
	        	xmlHttpRequest.setRequestHeader("AJAX","true");
	        },error:function(e){
				if(e.status == "400"){ // 세션만료 에러코드 
					location.href="${ctx}/logout";
				} else if(e.status == "401"){ // 권한없음 에러코드 
					alert("엑셀다운 권한이 없습니다. 관리자에게 문의바랍니다.");
					closeExcelPopup();
					return;
				} else {
					alert("에러가 발생했습니다.");
					closeExcelPopup();
					return;
				}
				closeLoading();
		     }
		});
	}

</script>
<div id="mask-excel" class="mask"></div>   
<div id="layerbox-excel" class="layerpop">
	 <!-- popup 엑셀다운 문서 암호화 -->
    <div class="layerpop_area">
    	<div>
       <div class="title">엑셀다운 문서 암호화</div>
       <div class="layerpop_close" onclick="javascript:closeExcelPopup();">&#10799;</div>
    	</div>
      <div class="popup-content">
      	<div class="exl-pw">
      		<h3>이 파일의 내용 암호화</h3>
      		<label for="exl-pw">암호 :</label>
		<input type="password" id="exl-pw" name="exl-pw" onkeyup="enterkey();">
      	</div>
      	<div class="pop-stxt">
      		<span>* 주의 :</span><span class="txt">암호를 잊거나 잃어버리면 복구할 수 없습니다.<br/>
			암호 및 해당 암호를 사용하는 문서이름을 목록으로 <br/>
			만들어 안전한 위치에 보관하는 것이 좋습니다.<br/>
			암호는 대/소문자를 구분합니다.</span> 
      	</div>
      	<div class="pop-btn">
      		<a href="javascript:closeExcelPopup();" id="layerbox_close" class="btn-left">취소</a>
      		<a href="javascript:excelDownload();" class="btn-right">확인</a>
      	</div>
      </div>
    </div>
</div>