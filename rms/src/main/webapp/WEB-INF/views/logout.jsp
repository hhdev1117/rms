<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<%@ include file="taglibs.jsp" %>
<html>
<head>	
	<title>사업운영모니터링 시스템</title>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
	
	<link rel="stylesheet" type="text/css" href="${webPath}/css/font.css">
	<link rel="stylesheet" type="text/css" href="${webPath}/css/default.css">
	<link rel="stylesheet" type="text/css" href="${webPath}/css/layout.css">
	
	<script src="${designPath}/js/vendors/jquery-2.2.4.min.js""></script>
	<script src="${designPath}/js/vendors/jquery-ui.min.js"></script>
	
	<script type="text/javascript">


		$(document).ready(function(e){
			//alert("로그아웃 되셨습니다.");
			$('#modalLogout').show();

			// 3초후 자동으로 로그인화면
			//setTimeout("goIndex()", 3000);
			
		});

		function onload(){
			popupOpen(); 
	        wrapWindowByMask();
			
			//setTimeout("goIndex()", 3000);
		}

		function enterkey() { 
			if (window.event.keyCode == 13) {
				goIndex();
			} 
		}

		function goIndex(){
			//$('#modalLogout').hide();
			location.href="${ctx}/";
		}

		function popupOpen() {
	        $('.layerpop').css("position", "absolute");
	        $('.layerpop').css("top",(($(window).height() - $('.layerpop').outerHeight()) / 2) + $(window).scrollTop());
	        $('.layerpop').css("left",(($(window).width() - $('.layerpop').outerWidth()) / 2) + $(window).scrollLeft());
	        $('.layerpop').draggable();
	        $('#layerbox_logout').show();
	    }

	    function popupClose() {
	        $('#layerbox_logout').hide();
	        $('#mask').hide();
	    }
	    function wrapWindowByMask() {
	        var maskHeight = $(document).height(); 
	        var maskWidth = $(window).width();

	        console.log( "document 사이즈:"+ $(document).width() + "*" + $(document).height()); 
	        console.log( "window 사이즈:"+ $(window).width() + "*" + $(window).height());        
	        $('#mask').css({
	            'width' : maskWidth,
	            'height' : maskHeight
	        });
	        $('#mask').fadeTo("slow", 0.5);
	    }

	</script>
</head>

<body onload="onload();" onkeyup="enterkey();">
<!-- layer popup  -->  
<div id="mask" class="mask"></div> 
<div id="layerbox_logout" class="layerpop">
  	<!-- popup 로그아웃 -->
	<div class="layerpop_area logout">
		<div class="popup-content">
	      	<h1 class="title" style="background-color: #FFFFFF"><span class="bold" style="color: #586885; font-size: 23px;">로그아웃 되셨습니다.</span></h1>
		   	<div class="pop-btn">
		   		<a href="javascript:goIndex();" id="layerbox_close" class="btn-right">확인</a>
		   		
		   	</div>
		</div>
	</div>
</div>
</body>
</html>
