<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<%@ include file="../taglibs.jsp" %>

<html>
<head>
<!-- ie 호환성 -->
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<!-- ie 호환성 -->	
	<title>사업운영모니터링 시스템</title>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
	<link rel="stylesheet" type="text/css" href="${designPath}/css/font.css">
	<link rel="stylesheet" type="text/css" href="${designPath}/css/default.css">
	<link rel="stylesheet" type="text/css" href="${designPath}/css/jquery-ui.css">
	<link rel="stylesheet" type="text/css" href="${designPath}/css/layout.css?ver=1">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
	<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/es6-promise@4/dist/es6-promise.min.js"></script>
	<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/es6-promise@4/dist/es6-promise.auto.min.js"></script>
	<script src="${designPath}/js/vendors/jquery-2.2.4.min.js"></script>
	<script src="${designPath}/js/vendors/jquery-1.11.1.min.js"></script>
	<script src="${designPath}/js/vendors/jquery-3.5.1.min.js"></script>
	<script src="${designPath}/js/vendors/jquery-ui.min.js"></script>
	<script src="${designPath}/js/vendors/jquery.fileDownload.js"></script>
	<script src="${designPath}/js/vendors/jquery-ui.js"></script>
	<%--<script src="${designPath}/dist/jquery-simple-tree-table.js"></script>--%>
	<%--<script src="${designPath}/js/colResizable-1.6.min.js"></script>--%>
	<%--<script src="${designPath}/js/colResizable-1.6"></script>--%>
	<script src="${designPath}/js/jquery-ui.min.js"></script>
	
	<script src="${designPath}/js/vendors/jquery.form.js"></script>
	<script src="${designPath}/js/util.js"></script>
	<script type="text/javascript">
		<%-- 
			README
			페이지가 메뉴를 통해 넘어오는경우가 있고, 
			주요지표 에서 값을 가지고 넘어오는경우가 있다.
			후자의 경우때문에 param변수를 생성했고
			모든 메뉴의 검색버튼 이벤트를 btnSearch()로 통일시켜줘야 한다.
		--%>
		
		$(document).ready(function(e){
			if("${param_search_gb}" == "0") {
				var data = new Object();
				data.param_search_gb 				= "${param_search_gb}";
			
				getContentTab("${page_id}", data);
			} else {
				getContentTab("${page_id}");
			}
			menuSetting();
			setActiveMenu("${active_menu_id}", "${active_sub_menu_id}");
			setActiveTopMenu("${active_bcg_id}");
		});

		function getContentTab(page, data, flag){
			var url = "${ctx}" + page;
			var targetDiv = ".container";

			$.post(url, data, function(result){
				window.onpopstate = function(event) {
					try{
						getContentTab(event.state.url, event.state.data, true);
					} catch(e) {
						history.back();
					}
				}

				<%-- 뒤로가기 제어용 --%>
				if(!flag) { 
					var objState = new Object();
					objState.data 			=	data 
					objState.url 			= 	url;
					
					history.pushState(objState, null, '');
				}
				
				$(targetDiv).empty();
				
				$(targetDiv).append(result).trigger("create");

				<%-- HTML 테이블 colResizeable.js 이용하기(후에 기능추가되면 사용할 것) --%>
				<%-- $(".resizeMe").colResizable({liveDrag:true, fixed:true}); --%> 

				<%-- IE에서 좌측 css background-color가 적용이 안되는 문제 해결용으로 강제 변경한 높이를 조회시 원상태로 돌려준다. --%>
				$(".left-bg").css("height", "100%");
			});
		}
		
		<%--=============================================================
		메뉴 활성화 CSS 변경
		=============================================================--%>
		function setActiveMenu(menuid, submenuid) {
			<%-- 메뉴 활성화 끄기 --%>
			$(".menu").attr("class", "menu");

			<%-- 활성화 메뉴 읽어서 활성화 --%>
			if(menuid != "") {
				$("#" + menuid).attr("class", "menu active");
			}

			<%-- 서브메뉴 활성화 끄기 --%>
			$(".subtitlemenu").attr("class", "subtitlemenu");

			<%-- 활성화 서브 메뉴 읽어서 활성화 --%>
			if(submenuid != "") {
				$("#" + submenuid).attr("class", "subtitlemenu on");
			}
		}

		<%--=============================================================
		상단 사업구분 CSS 변경
		=============================================================--%>
		function setActiveTopMenu(active_bcg_id) {
			if(active_bcg_id == "N") {
				$("#spanBcgName").html("눈높이");
			} else if (active_bcg_id == "C") {
				$("#spanBcgName").html("차이홍");
			} else {
				$("#spanBcgName").html("솔루니");
			}
		}
		
		<%--=============================================================
		메뉴 Setting
		=============================================================--%>
		function menuSetting() {
			var obj = new Object();
			obj.active_bcg_id = "${active_bcg_id}";
			obj.param_search_gb = "${param_search_gb}";

			$.ajax({
				type : "POST",
				dataType : "json",
				url : "${ctx}/com/getMenu.do",
				contentType : 'application/json',
				data : JSON.stringify(obj),
				success : function(result) {
					<%-- 상위메뉴들은 CSS상 반 하드코딩으로 작성 --%>
					$("#menu1").attr("onclick", "getContentTab('" + result[0].zmenu_url + "'); $('#menu3-sub').slideUp(300); $('#menu3').attr('value', 'F');");
					$("#menu2").attr("onclick", "getContentTab('" + result[1].zmenu_url + "'); $('#menu3-sub').slideUp(300); $('#menu3').attr('value', 'F');");
					$("#menu3").attr("onclick", "clickDetail('" + result[2].zmenu_url + "')");

					<%-- 하위메뉴 불러오기 --%>
					var html = "";
					
					for(var i = 2; i < result.length; i++) {
						var zmenu_url = result[i].zmenu_url;
						html += "<li><a herf='javascript:void(0);' class='subtitlemenu' id='menu3-" + (i - 1) + "' onclick=\"getContentTab('" + zmenu_url + "');\" style='cursor:hand;'>" + result[i].zmenu_nm + "</a></li>";
					}
					
					$("#sub-menu-list").append(html);
				},
				error : function(e) {
					if (e.status == "400") { <%-- 세션만료 에러코드 --%>
						location.href = "${ctx}/logout";
					}
				}
			});
		}

		<%--=============================================================
		모니터링 상세 클릭시 
		=============================================================--%>
		function clickDetail(url) {
			if($("#menu3").attr("value") == "T") {
				$("#menu3").attr("value", "F");
				$("#menu3").attr("class", "menu");

				getContentTab("${ctx}/blank.do");
				$('#menu3-sub').slideUp(300);
			} else {
				$("#menu3").attr("value", "T");
				
				getContentTab(url);
				$('#menu3-sub').slideDown(300);
			}
		}
	</script>
</head>
<body>
<%@ include file="../com/loading.jsp"%>
<%@ include file="../com/excel.jsp"%>
	<div class="wrap">
		<!-- header -->
		<div class="header">
			<div class="top-left">
				<a href="${ctx}/main.do"><img src="${webPath}/images/logo.png" class="logo" alt="로고">
				<h1 class="top-h1">사업운영모니터링 시스템</h1></a>
				<div class="dropdown">
					<c:choose>
						<c:when test="${zregion_gb eq 'A'}">
						    <button class="dropbtn">
						    	<span id="spanBcgName">눈높이 </span>
						    	<i class="fa fa-caret-down"></i>
						    </button>
							<div class="dropdown-content">
								<form id="form-noonSum" action="${ctx}/index.do" method="post" onclick="this.submit();">
							    	<input type="hidden" name="url" value="/sum/sumNoonMoni.do"/>
							    	<input type="hidden" name="active_bcg_id" value="N"/>
							        <input type="hidden" name="active_menu_id" value="menu1"/>
							        <input type="hidden" name="active_sub_menu_id" value=""/>
							        <span>눈높이</span>
							    </form>
								<form id="form-chiSum" action="${ctx}/index.do" method="post" onclick="this.submit();">
							    	<input type="hidden" name="url" value="/sum/sumChiMoni.do"/>
							    	<input type="hidden" name="active_bcg_id" value="C"/>
							        <input type="hidden" name="active_menu_id" value="menu1"/>
							        <input type="hidden" name="active_sub_menu_id" value=""/>
							        <span>차이홍</span>
							    </form>
								<form id="form-solSum" action="${ctx}/index.do" method="post" onclick="this.submit();">
							    	<input type="hidden" name="url" value="/sum/sumSolMoni.do"/>
							    	<input type="hidden" name="active_bcg_id" value="S"/>
							        <input type="hidden" name="active_menu_id" value="menu1"/>
							        <input type="hidden" name="active_sub_menu_id" value=""/>
							        <span>솔루니</span>
							    </form>
							</div>
						</c:when>
						<c:otherwise>
							<button class="dropbtn" style="cursor:text;">
						    	<span id="spanBcgName" style="padding-right:0px;">눈높이</span>
						    </button>
						</c:otherwise>
				    </c:choose>
				</div>
			</div>
			<div class="top-right">
				<a href="javascript:void(0);" style="cursor:text;">
				<!-- BCG -->
					<c:if test="${zregion_nm ne 'null'}">
						${zregion_nm}
						<!-- HL --> 		
						<c:if test="${zdept_nm ne 'null'}">
							&gt; ${zdept_nm }	 
						 </c:if>		 
					 </c:if>
				</a>
				<a href="javascript:void(0);" class="my" style="cursor:text;"><img src="${webPath}/images/icon_my.png" alt="이름">
					<span class="name">
						<c:if test="${zemp_nm ne 'null'}">
							${zemp_nm }	 
						 </c:if>	
					</span>
				</a>
				<a href="${ctx}/logout" class="logout"><img src="${webPath}/images/icon_logout.png" alt="로그아웃"></a>
			</div>
		</div>
		<!-- // header -->
		<!-- contents -->
		<div class="content-bg">
			<!-- left-menu -->
			<div class="left-bg">
				<ul class="main-menu">
					<li>
						<a href="javascript:void(0);" id="menu1" class="menu">
							<img src="${webPath}/images/menu_1.png" alt="">
							<div class="menu-tt">주요지표</div>
						</a>
					</li>
					<li>
						<a href="javascript:void(0);" id="menu2" class="menu">
							<img src="${webPath}/images/menu_2.png" alt="">
							<div class="menu-tt">월별 모니터링</div>
						</a>
					</li>
					<li>
						<a href="javascript:void(0);" id="menu3" class="menu" value="F">
							<img src="${webPath}/images/menu_3.png" alt="">
							<div class="menu-tt">모니터링 세부</div>
						</a>
						<div class="submenu" id="menu3-sub" style="display:none;">
							<ul id="sub-menu-list">
							<!-- 여기에 하위메뉴 불러오게함 -->
							</ul>
						</div>
					</li>
				</ul>
			</div>
			<!-- // left-menu -->
			<!-- container -->
			<div class="container" id="container">
			
			</div>
		</div>
	</div>
</body>