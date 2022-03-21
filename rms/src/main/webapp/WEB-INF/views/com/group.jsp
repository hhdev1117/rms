<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<script type="text/javascript">
	$(document).ready(function(e) {
		<%--
		 파라미터가 없이 넘어온경우는
		 세션값으로 세팅해준다.
		--%>
		if(isEmpty(param_zregion) && isEmpty(param_zdept_id) && isEmpty(param_zdept_brch_id) && isEmpty(param_zemp_id)) {
			if(param_search_gb != "0") {
				setDefaultGroup();
				setSessionGroup();
			} else {
				setDefaultGroup();
				setCustomGroup();
			}
		<%--
		 파라미터를 가지고 넘어온경우는
		 파라미터값으로 세팅해준다.
		--%>
		} else {
			
			setDefaultGroup();
			setCustomGroup();
		}
	});

	$.fn.hasScrollBar = function() {
	    return (this.prop("scrollHeight") == 0 && this.prop("clientHeight") == 0)
	            || (this.prop("scrollHeight") > this.prop("clientHeight"));
	};
	
	<%--
		temp는 조직이 선택됬을때 동작을 위한 변수
		원코드는 확인을 눌렀을때 적용된다.
	--%>
	var temp_zregion = "";
	var temp_zdept = "";
	var temp_zdept_brch = "";
	var temp_zemp = "";

	var zregion = "";
	var zdept = "";
	var zdept_brch = "";
	var zemp = "";

	var zregion_nm = "";
	var zdept_nm = "";
	var zdept_brch_nm = "";
	var zemp_nm = "";

	<%--=============================================================
	조직 초기화
	=============================================================--%>
	function setDefaultGroup(){
		$("#grpFilterBox").css("width", "139px");

		var temp_zregion = "";
		var temp_zdept = "";
		var temp_zdept_brch = "";
		var temp_zemp = "";

		var zregion = "";
		var zdept = "";
		var zdept_brch = "";
		var zemp = "";
	}

	<%--=============================================================
	해당세션값을 자동으로 세팅한다.
	=============================================================--%>
	function setSessionGroup(){
		getZregion();
		
		if(!isEmpty("${zregion}") && (("${zauth_grp}" == "NI") || ("${zauth_grp}" == "NX") || ("${zauth_grp}" == "NC") || ("${zauth_grp}" == "NP"))) {
			$("#${zregion}").trigger("click");
			
			if(!isEmpty("${zdept_id}") && ("${zauth_grp}" == "NC") || ("${zauth_grp}" == "NP")) {
				$("#${zdept_id}").trigger("click");
				
				if(!isEmpty("${zdept_brch_id}") && ("${zauth_grp}" == "NP")) {
					$("#${zdept_brch_id}").trigger("click");
				}
			}
		}

		btnGroupConf();
	}

	<%--=============================================================
	주요지표에서 넘어왔을경우
	세션에서 자동으로 체크할때도 JQUERY 트리거를 이용해서 작업한다.
	=============================================================--%>
	function setCustomGroup(){
		getZregion();

		if(!isEmpty(param_zregion)) {
			$("#" + param_zregion).trigger("click");
			
			if(!isEmpty(param_zdept_id)) {
				$("#" + param_zdept_id).trigger("click");
				
				if(!isEmpty(param_zdept_brch_id)) {
					$("#" + param_zdept_brch_id).trigger("click");
					
					if(!isEmpty(param_zemp_id)) {
						$("#" + param_zemp_id).trigger("click");
					}
				}
			}
		}

		btnGroupConf();
		btnSearch(1);
	}

	<%--=============================================================
	본부 조회
	=============================================================--%>
	function getZregion() {

		<%-- active_bcg_id는 컨트롤러에서 화면이동마다 따라서 가져오게한다. --%>
		var obj = new Object();
		obj.active_bcg_id = active_bcg_id;

		$.ajax({                                                        
			type : "POST",
			dataType : "json",
			url : "${ctx}/getZregion.do",
			contentType : 'application/json',
			async: false,
			data : JSON.stringify(obj),
			success : function(result) {
				$("#zregion").empty();
 
				var html = "";
				html += "<ul>";
				html += "<li>본부 선택</li>";
				html += "</ul>";

				html += "<ul id='ul-zregion' class='slct-branch' style='height:280px; overflow:auto; -ms-overflow-style: none;'>";
				for ( var i in result) {
					html += "<li class='has-sub check'><a href='javascript:void(0);' class='list-zregion' val='zregion' id='" + result[i].code + "' onclick='getVkbur(this.id); groupClickCss(this);'>" + result[i].name + "</a></li>";
				}
				html += "</ul>";
				
				$("#zregion").append(html);

				if(result.length > 9) {
					html = "";
					html += "<button id='group-more-zregion' class='group-more'></button>";
					
					$("#zregion").append(html);
					
					$("#ul-zregion").scroll(function () {
						if($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight) {
							$("#group-more-zregion").css("display", "none");
						} else {
							$("#group-more-zregion").css("display", "");
						}
					});

					$("#group-more-zregion").click(function(){
						$("#ul-zregion").animate({scrollTop : $("#ul-zregion").scrollTop() + 200}, 200);
					});
				}
			},
			beforeSend:function(xmlHttpRequest){
        		xmlHttpRequest.setRequestHeader("AJAX","true");
        	}
			,error:function(e){
				if(e.status == "400"){ <%-- 세션만료 에러코드 --%>
					location.href="${ctx}/logout";
				}
		    }
		});
	}

	<%--=============================================================
	사업국 조회
	=============================================================--%>
	function getVkbur(param) {
		temp_zregion = param;
		var obj = new Object();
		obj.zregion = temp_zregion;

		$.ajax({                                                        
			type : "POST",
			dataType : "json",
			url : "${ctx}/getVkbur.do",
			contentType : 'application/json',
			async: false,
			data : JSON.stringify(obj),
			success : function(result) {
				$("#zdept").empty();
 
				var html = "";
				html += "<ul>";
				html += "<li>교육국 선택</li>"; <%-- "사업국" -> "교육국" 수정 2021.07.29 --%>
				html += "</ul>";

				html += "<ul id='ul-zdept' class='slct-branch' style='height:280px; overflow:auto; -ms-overflow-style: none;'>";
				for ( var i in result) {
					html += "<li class='has-sub check'><a href='javascript:void(0);' class='list-zdept' val='zdept' id='" + result[i].code + "' onclick='getVkgrp(this.id); groupClickCss(this);'>" + result[i].name + "</a></li>";
				}
				html += "</ul>";

				$("#zdept").append(html);

				if(result.length > 9) {
					html = "";
					html += "<button id='group-more-zdept' class='group-more'></button>";

					$("#zdept").append(html);
					
					$("#ul-zdept").scroll(function () {
						if($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight) {
							$("#group-more-zdept").css("display", "none");
						} else {
							$("#group-more-zdept").css("display", "");
						}
					});

					$("#group-more-zdept").click(function(){
						$("#ul-zdept").animate({scrollTop : $("#ul-zdept").scrollTop() + 200}, 200);
					});
				}
			},
			beforeSend:function(xmlHttpRequest){
        		xmlHttpRequest.setRequestHeader("AJAX","true");
        	},
			error:function(e){
				if(e.status == "400"){ <%-- 세션만료 에러코드 --%>
					location.href="${ctx}/logout.do";
				}
		    }
		});
	}

	<%--=============================================================
	팀 조회
	=============================================================--%>
	function getVkgrp(param) {
		temp_zdept = param;
		var obj = new Object();
		obj.zregion = temp_zregion;
		obj.vkbur = temp_zdept;

		$.ajax({                                                        
			type : "POST",
			dataType : "json",
			url : "${ctx}/getVkgrp.do",
			contentType : 'application/json',
			async: false,
			data : JSON.stringify(obj),
			success : function(result) {
				$("#zdept_brch").empty();
 
				var html = "";

				html += "<ul>";
				html += "<li>팀[센터] 선택</li>";
				html += "</ul>";

				html += "<ul id='ul-zdept_brch' class='slct-branch' style='height:280px; overflow:auto; -ms-overflow-style: none;'>";
				for ( var i in result) {
					html += "<li class='has-sub check'><a href='javascript:void(0);' class='list-zdept_brch' val='zdept_brch' id='" + result[i].code + "' onclick='getZemp(this.id); groupClickCss(this);'>" + result[i].code;
					if(isEmpty(result[i].name)) {
						html += "</a></li>";
					} else {
						html += "["+  result[i].name + "]</a></li>";
					}
				}
				html += "</ul>";
				
				$("#zdept_brch").append(html);

				if(result.length > 9) {
					html = "";
					html += "<button id='group-more-zdept_brch' class='group-more'></button>";

					$("#zdept_brch").append(html);
					
					$("#ul-zdept_brch").scroll(function () {
						if($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight) {
							$("#group-more-zdept_brch").css("display", "none");
						} else {
							$("#group-more-zdept_brch").css("display", "");
						}
					});

					$("#group-more-zdept_brch").click(function(){
						$("#ul-zdept_brch").animate({scrollTop : $("#ul-zdept_brch").scrollTop() + 200}, 200);
					});
				}
			},
			beforeSend:function(xmlHttpRequest){
        		xmlHttpRequest.setRequestHeader("AJAX","true");
        	},
			error:function(e){
				if(e.status == "400"){ <%-- 세션만료 에러코드 --%> 
					location.href="${ctx}/logout";
				}
		    }
		});
	}

	<%--=============================================================
	교사 조회
	=============================================================--%>
	function getZemp(param) {
		temp_zdept_brch = param;
		var obj = new Object();
		obj.zregion = temp_zregion;
		obj.vkbur = temp_zdept;
		obj.vkgrp = temp_zdept_brch;

		$.ajax({                                                        
			type : "POST",
			dataType : "json",
			url : "${ctx}/getZemp.do",
			contentType : 'application/json',
			async: false,
			data : JSON.stringify(obj),
			success : function(result) {
				$("#zemp").empty();
 
				var html = "";

				html += "<ul>";
				html += "<li>교사 선택</li>";
				html += "</ul>";

				html += "<ul id='ul-zemp' class='slct-branch' style='height:280px; overflow:auto; -ms-overflow-style: none;'>";
				for ( var i in result) {
					if(result[i].name.length < 5) {
						html += "<li class='has-sub check'><a href='javascript:void(0);' class='list-zemp' val='zemp' id='" + result[i].code + "' onclick='groupClickCss(this);'>" + result[i].name + "[" + (result[i].code).substring(2) + "]</a></li>";
					} else {
						html += "<li class='has-sub check' style='font-size:10px;'><a href='javascript:void(0);' class='list-zemp' val='zemp' id='" + result[i].code + "' onclick='groupClickCss(this);'>" + result[i].name + "[" + (result[i].code).substring(2) + "]</a></li>";
					}
				}
				html += "</ul>";
				
				$("#zemp").append(html);

				if(result.length > 9) {
					html = "";
					html += "<button id='group-more-zemp' class='group-more'></button>";

					$("#zemp").append(html);
					
					$("#ul-zemp").scroll(function () {
						if($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight) {
							$("#group-more-zemp").css("display", "none");
						} else {
							$("#group-more-zemp").css("display", "");
						}
					});

					$("#group-more-zemp").click(function(){
						$("#ul-zemp").animate({scrollTop : $("#ul-zemp").scrollTop() + 200}, 200);
					});
				}
			},
			beforeSend:function(xmlHttpRequest){
        		xmlHttpRequest.setRequestHeader("AJAX","true");
        	},
			error:function(e){
				if(e.status == "400"){ <%-- 세션만료 에러코드 --%>
					location.href="${ctx}/logout";
				}
		    }
		});
	}

	<%--=============================================================
	클릭을했을때 CSS를 변경한다.
	=============================================================--%>
	function groupClickCss(e) {
		var value = $("#" + e.id).attr("val");
		var name = $("#" + e.id).html();
		if($("#" + e.id).css("background-color") != "rgb(240, 241, 243)") {
			groupOpen(value, name, e.id);
			$(".list-" + value).css("background-color", "");
			$("#" + e.id).css("background-color", "#f0f1f3");
		} else {
			groupClose(value, name, e.id);
		}
	}

	function groupCustomClickCss(value, name, id) {
		if($("#" + e.id).css("background-color") != "rgb(240, 241, 243)") {
			groupOpen(value, name, id);
			$(".list-" + value).css("background-color", "");
			$("#" + e.id).css("background-color", "#f0f1f3");
		} else {
			groupClose(value, name, id);
			$("#" + e.id).css("background-color", "");
		}
	}

	<%--=============================================================
	클릭을했을때 하위조직 오픈.
	=============================================================--%>
	function groupOpen(value, name, id) {
		if(value == "zregion") {
			$("#zdept").css("display", "");
			$("#zdept_brch").css("display", "none");
			$("#zemp").css("display", "none");
			
			$("#btnConf").css("margin-right", "200px");
			$("#grpFilterBox").css("width", "270px");

			zregion_nm = name;
			zdept_nm = "";
			zdept_brch_nm = "";
			zemp_nm = "";

			temp_zdept = "";
			temp_zdept_brch = "";
			temp_zemp = "";
			
		} else if(value == "zdept") {
			$("#zdept_brch").css("display", "");
			$("#zemp").css("display", "none");
			
			$("#btnConf").css("margin-right", "50px");
			$("#grpFilterBox").css("width", "399px");

			zdept_nm = name;
			zdept_brch_nm = "";
			zemp_nm = "";

			temp_zdept_brch = "";
			temp_zemp = "";
			
		} else if(value == "zdept_brch") {
			$("#zemp").css("display", "");
			$("#btnConf").css("margin-right", "");
			$("#grpFilterBox").css("width", "528px");

			zdept_brch_nm = name;
			zemp_nm = "";

			temp_zemp = "";
		} else if(value == "zemp") {
			zemp_nm = name;
			
			temp_zemp = id;
		}
	}

	<%--=============================================================
	클릭을했을때 하위조직 클로즈.
	=============================================================--%>
	function groupClose(value, name, id) {
		if(value == "zregion") {
			if("${zauth_grp}" == "NI" || "${zauth_grp}" == "NX" || "${zauth_grp}" == "NC" || "${zauth_grp}" == "NP") {
				if("${zauth_grp}" == "NI" || "${zauth_grp}" == "NX") {
					$("#zemp").css("display", "none");
					$("#zdept_brch").css("display", "none");
					$("#btnConf").css("margin-right", "100");
					$("#grpFilterBox").css("width", "270px");
					$("#" + id).css("background-color", "#f0f1f3");

					zdept_nm = "";
					zdept_brch_nm = "";
					zemp_nm = "";

					temp_zdept = "";
					temp_zdept_brch = "";
					temp_zemp = "";
				} else if("${zauth_grp}" == "NC" || "${zauth_grp}" == "NP") {
					$("#zemp").css("display", "none");
					$("#btnConf").css("margin-right", "200px");
					$("#grpFilterBox").css("width", "399px");
					$("#" + id).css("background-color", "#f0f1f3");
					$(".list-zdept").css("background-color", "#f0f1f3");
					$(".list-zdept_brch").css("background-color", "");

					zdept_brch_nm = "";
					zemp_nm = "";

					temp_zdept_brch = "";
					temp_zemp = "";
				}
				return;
			}
			$("#zdept").css("display", "none");
			$("#zdept_brch").css("display", "none");
			$("#zemp").css("display", "none");

			zregion_nm = "";
			zdept_nm = "";
			zdept_brch_nm = "";
			zemp_nm = "";

			temp_zregion = "";
			temp_zdept = "";
			temp_zdept_brch = "";
			temp_zemp = "";
			
			$("#btnConf").css("margin-right", "100");
			$("#grpFilterBox").css("width", "139px");
			$("#" + id).css("background-color", "");
		} else if(value == "zdept") {
			if("${zauth_grp}" == "NC" || "${zauth_grp}" == "NP") {
				$("#zemp").css("display", "none");
				$("#btnConf").css("margin-right", "200px");
				$("#grpFilterBox").css("width", "399px");
				$("#" + id).css("background-color", "#f0f1f3");

				zdept_brch_nm = "";
				zemp_nm = "";

				temp_zdept_brch = "";
				temp_zemp = "";

				return;
			}
			$("#zdept_brch").css("display", "none");
			$("#zemp").css("display", "none");
			
			zdept_nm = "";
			zdept_brch_nm = "";
			zemp_nm = "";

			temp_zdept = "";
			temp_zdept_brch = "";
			temp_zemp = "";
			
			$("#btnConf").css("margin-right", "200px");
			$("#grpFilterBox").css("width", "270px");
			$("#" + id).css("background-color", "");
		} else if(value == "zdept_brch") {
			$("#zemp").css("display", "none");

			zdept_brch_nm = "";
			zemp_nm = "";

			temp_zdept_brch = "";
			temp_zemp = "";
			
			$("#btnConf").css("margin-right", "50");
			$("#grpFilterBox").css("width", "399px");
			$("#" + id).css("background-color", "");
		} else if(value == "zemp") {
			zemp_nm = "";

			temp_zemp = "";
			$("#" + id).css("background-color", "");
		}
	}

	<%--=============================================================
	조직선택 눌렀을때 열려있으면 닫고, 닫혀있으면 연다.
	=============================================================--%>
	function btnGroupOpen() {
		if($("#grpFilterBox").css("visibility") == "visible") {
			$("#grpFilterBox").css("visibility", "hidden");
		} else {
			$("#grpFilterBox").css("visibility", "visible");
			$("#sumOpenSearchFilter").css("visibility", "hidden");
		}
	}

	<%--=============================================================
	조직선택 확인버튼 눌렀을때
	=============================================================--%>
	function btnGroupConf() {
		var group_nm = "";
		if(!isEmpty(zregion_nm)) {
			group_nm += zregion_nm;
			if(!isEmpty(zdept_nm)) {
				group_nm += " > ";
				group_nm += zdept_nm;
				if(!isEmpty(zdept_brch_nm)) {
					group_nm += " > ";
					group_nm += zdept_brch_nm;
					if(!isEmpty(zemp_nm)) {
						group_nm += " > ";
						group_nm += zemp_nm;
					}
				}
			}
		}

		zregion = temp_zregion;
		zdept = temp_zdept;
		zdept_brch = temp_zdept_brch;
		zemp = temp_zemp;

		if(isEmpty(group_nm)) group_nm = "전체";
		$("#organization").attr("placeholder", group_nm);
		$("#grpFilterBox").css("visibility", "hidden");
	}


</script>
<div class="custom-input org">
	<div class="filtering org-select">
		<div class="custom-input selt-Chk" onclick="btnGroupOpen();">
			<input type="text" placeholder="조직선택" disabled>
			<a href="javascript:void(0);" class="icon_filter"><img src="${webPath}/images/icon_filter.png" alt=""></a>
		</div>
		<div class="filterBox" id="grpFilterBox">
			<div>
				<div id="zregion" class="slct-branch" style="overflow:auto;">
					<ul style="height:300px">
						<li>본부 선택</li>
					</ul>									
				</div>
				<div id="zdept" class="slct-branch" style="display:none;">
					<ul>
						<li>사업국 선택</li>
					</ul>									
				</div>							
				<div id="zdept_brch" class="slct-branch" style="display:none;">
					<ul>
						<li>팀[센터] 선택</li>
					</ul>						
				</div>
				<div id="zemp" class="slct-branch" style="display:none;">
					<ul>
						<li>교사 선택</li>
					</ul>									
				</div>										
			</div>
			<div>
				<button class="btnConf" onclick="btnGroupConf();">확인</button>
			</div>
		</div>				    	
	</div>
    <input type="text" id="organization" class="org-input" placeholder="" disabled>
</div>