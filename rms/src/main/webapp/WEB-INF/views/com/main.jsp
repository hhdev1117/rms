<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<%@ include file="../taglibs.jsp" %>
<!DOCTYPE html>
<!-- ie 호환성 -->
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<!-- ie 호환성 -->
<title>사업운영모니터링 시스템</title>
	<link rel="stylesheet" type="text/css" href="${designPath}/css/font.css">
	<link rel="stylesheet" type="text/css" href="${designPath}/css/default.css">
	<link rel="stylesheet" type="text/css" href="${designPath}/css/jquery-ui.css">
	<link rel="stylesheet" type="text/css" href="${designPath}/css/layout.css">
	<link rel="stylesheet" type="text/css" href="${designPath}/css/font-awesome.min.css">
	<script src="${designPath}/js/vendors/jquery-2.2.4.min.js"></script>
	<script src="${designPath}/js/vendors/jquery-1.11.1.min.js"></script>
	<script src="${designPath}/js/vendors/jquery-3.5.1.min.js"></script>
	<script src="${designPath}/js/vendors/jquery-ui.min.js"></script>
	<script src="${designPath}/js/vendors/jquery-ui.js"></script>
	<script src="${designPath}/js/jquery-ui.min.js"></script>
	
	<script src="${designPath}/js/vendors/jquery.form.js"></script>
	<script src="${designPath}/js/util.js"></script>

<script type="text/javascript">

	$(document).ready(function(e){
	});
	
	function goMenu(url){
		/*
		location.href = "${ctx}/" + url;
		*/
	} 
	
</script>

<c:if test="${zlogin_id eq '10000232' or zlogin_id eq '21012108'}">
	<%@ include file="../com/sessionPopup.jsp"%>
</c:if>	
							
<!-- main -->
<div class="wrap">
	<!-- header -->
	<div class="header">
		<div class="top-left">
				<a href="${ctx}/main.do"><img src="${webPath}/images/logo.png" class="logo" alt="로고">
				<h1 class="top-h1">사업운영모니터링 시스템</h1>
				</a>
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
					<c:choose>
						<c:when test="${zlogin_id eq '10000232' or zlogin_id eq '21012108'}">
							<span class="name" onclick="openSessionPopup()" style="cursor:pointer;">
								<c:if test="${zemp_nm ne 'null'}">
									${zemp_nm }
								</c:if>	
							</span>
							
						</c:when>
						<c:otherwise>
							<span class="name">
								<c:if test="${zemp_nm ne 'null'}">
									${zemp_nm }	 
								</c:if>	
							</span>
						</c:otherwise>
				    </c:choose>
				</a>
				<a href="${ctx}/logout" class="logout"><img src="${webPath}/images/icon_logout.png" alt="로그아웃"></a>
			</div>
		</div>
	<!-- // header -->
	<!-- contents -->
	<div class="content-bg">
		<div class="main-container">
			<div class="rank-info">
				<div class="ranking">
					<c:choose>
						<c:when test="${zregion_gb ne 'A'}">
							<div>
								<img src="${webPath}/images/main_icon1.png">
								<c:choose>
									<c:when test="${vo.rank1 eq ' '}">
										<span class="tt">관리순위&nbsp;&nbsp;-</span>
									</c:when>
									<c:otherwise>
										<span class="tt">관리순위</span>
										<span class="num">${vo.rank1}</span>
									</c:otherwise>
								</c:choose>
							</div>
							
							<!-- 개선순위 2021.07.07
							<div>
								<img src="${webPath}/images/main_icon2.png">
								<c:choose>
									<c:when test="${vo.rank2 eq ' '}">
										<span class="tt">개선순위&nbsp;&nbsp;-</span>
									</c:when>
									<c:otherwise>
										<span class="tt">개선순위</span>
										<span class="num">${vo.rank2}</span>
									</c:otherwise>
								</c:choose>
							</div>
							 -->					
				        </c:when>
						<c:otherwise>
							<div>
								<img src="${webPath}/images/main_icon1.png">
								<span class="tt">관리순위&nbsp;&nbsp;-</span>
							</div>
							<!-- 개선순위 2021.07.07
							<div>
								<img src="${webPath}/images/main_icon2.png">
								<span class="tt">개선순위&nbsp;&nbsp;-</span>
							</div>	
							-->			
						</c:otherwise>
					</c:choose>
				</div>
				<div class="info-txt">기준 <c:if test="${vo.nowdate ne 'null'}">${vo.nowdate}</c:if></div>
			</div>
			<div class="row">
				<div class="card">
					<form action="${ctx}/index.do" method="post" onclick="this.submit();" style="cursor:pointer;">
						<h2>개인정보 인증</h2>
						<c:choose>
							<c:when test="${zregion_gb eq 'N' or zregion_gb eq 'A'}">
								<input type="hidden" name="url" value="${menuList[2].zmenu_url}"/>
					            <input type="hidden" name="active_bcg_id" value="N"/>
					            <input type="hidden" name="param_search_gb" value="0"/>
							</c:when>
							<c:when test="${zregion_gb eq 'C'}">
								<input type="hidden" name="url" value="${menuList[2].zmenu_url}"/>
					            <input type="hidden" name="active_bcg_id" value="C"/>
					            <input type="hidden" name="param_search_gb" value="0"/>
							</c:when>
							<c:when test="${zregion_gb eq 'S'}">
								<input type="hidden" name="url" value="${menuList[2].zmenu_url}"/>
					            <input type="hidden" name="active_bcg_id" value="S"/>
					            <input type="hidden" name="param_search_gb" value="0"/>
							</c:when>
						</c:choose>
				        <p><span><c:if test="${vo.zitem1_p ne 'null'}">${vo.zitem1_p}</c:if></span>%</p>
					</form>
				</div>
				<div class="card">
					<form action="${ctx}/index.do" method="post" onclick="this.submit();" style="cursor:pointer;">
						<h2>카드결제 현황</h2>
						<c:choose>
							<c:when test="${zregion_gb eq 'N' or zregion_gb eq 'A'}">
								<input type="hidden" name="url" value="${menuList[3].zmenu_url}"/>
					            <input type="hidden" name="active_bcg_id" value="N"/>
					            <input type="hidden" name="param_search_gb" value="0"/>
							</c:when>
							<c:when test="${zregion_gb eq 'C'}">
								<input type="hidden" name="url" value="${menuList[3].zmenu_url}"/>
					            <input type="hidden" name="active_bcg_id" value="C"/>
					            <input type="hidden" name="param_search_gb" value="0"/>
							</c:when>
							<c:when test="${zregion_gb eq 'S'}">
								<input type="hidden" name="url" value="${menuList[3].zmenu_url}"/>
					            <input type="hidden" name="active_bcg_id" value="S"/>
					            <input type="hidden" name="param_search_gb" value="0"/>
							</c:when>
						</c:choose>
				        <p><span><c:if test="${vo.zitem2_p ne 'null'}">${vo.zitem2_p}</c:if></span>%</p>
					</form>
				</div>
				<div class="card">
					<form action="${ctx}/index.do" method="post" onclick="this.submit();" style="cursor:pointer;">
						<h2>이체제외 후 학습</h2>
						<c:choose>
							<c:when test="${zregion_gb eq 'N' or zregion_gb eq 'A'}">
								<input type="hidden" name="url" value="${menuList[4].zmenu_url}"/>
					            <input type="hidden" name="active_bcg_id" value="N"/>
					            <input type="hidden" name="param_search_gb" value="0"/>
							</c:when>
							<c:when test="${zregion_gb eq 'C'}">
								<input type="hidden" name="url" value="${menuList[4].zmenu_url}"/>
					            <input type="hidden" name="active_bcg_id" value="C"/>
					            <input type="hidden" name="param_search_gb" value="0"/>
							</c:when>
							<c:when test="${zregion_gb eq 'S'}">
								<input type="hidden" name="url" value="${menuList[4].zmenu_url}"/>
					            <input type="hidden" name="active_bcg_id" value="S"/>
					            <input type="hidden" name="param_search_gb" value="0"/>
							</c:when>
						</c:choose>
				        <p><span><c:if test="${vo.zitem3_p ne 'null'}">${vo.zitem3_p}</c:if></span>%</p>
					</form>
				</div>
				<div class="card">
					<form action="${ctx}/index.do" method="post" onclick="this.submit();" style="cursor:pointer;">
						<h2>2개월 체납</h2>
						<c:choose>
							<c:when test="${zregion_gb eq 'N' or zregion_gb eq 'A'}">
								<input type="hidden" name="url" value="${menuList[5].zmenu_url}"/>
					            <input type="hidden" name="active_bcg_id" value="N"/>
					            <input type="hidden" name="param_search_gb" value="0"/>
							</c:when>
							<c:when test="${zregion_gb eq 'C'}">
								<input type="hidden" name="url" value="${menuList[5].zmenu_url}"/>
					            <input type="hidden" name="active_bcg_id" value="C"/>
					            <input type="hidden" name="param_search_gb" value="0"/>
							</c:when>
							<c:when test="${zregion_gb eq 'S'}">
								<input type="hidden" name="url" value="${menuList[5].zmenu_url}"/>
					            <input type="hidden" name="active_bcg_id" value="S"/>
					            <input type="hidden" name="param_search_gb" value="0"/>
							</c:when>
						</c:choose>
				        <p><span><c:if test="${vo.zitem4_p ne 'null'}">${vo.zitem4_p}</c:if></span>%</p>
					</form>
				</div>
			</div>
			<div class="row">
				<div class="card">
					<form action="${ctx}/index.do" method="post" onclick="this.submit();" style="cursor:pointer;">
						<h2>고객직접결제 중복발송</h2>
						<c:choose>
							<c:when test="${zregion_gb eq 'N' or zregion_gb eq 'A'}">
								<input type="hidden" name="url" value="${menuList[6].zmenu_url}"/>
					            <input type="hidden" name="active_bcg_id" value="N"/>
					            <input type="hidden" name="param_search_gb" value="0"/>
							</c:when>
							<c:when test="${zregion_gb eq 'C'}">
								<input type="hidden" name="url" value="${menuList[6].zmenu_url}"/>
					            <input type="hidden" name="active_bcg_id" value="C"/>
					            <input type="hidden" name="param_search_gb" value="0"/>
							</c:when>
							<c:when test="${zregion_gb eq 'S'}">
								<input type="hidden" name="url" value="${menuList[6].zmenu_url}"/>
					            <input type="hidden" name="active_bcg_id" value="S"/>
					            <input type="hidden" name="param_search_gb" value="0"/>
							</c:when>
						</c:choose>
				        <p><span><c:if test="${vo.zitem5_p ne 'null'}">${vo.zitem5_p}</c:if></span>%</p>
					</form>
				</div>
				<c:if test="${zregion_gb eq 'N' or zregion_gb eq 'A'}">
					<div class="card">
						<form action="${ctx}/index.do" method="post" onclick="this.submit();" style="cursor:pointer;">
							<h2>써밋제품 학습</h2>
								<input type="hidden" name="url" value="${menuList[7].zmenu_url}"/>
							    <input type="hidden" name="active_bcg_id" value="N"/>
							    <input type="hidden" name="param_search_gb" value="0"/>
					        <p><span><c:if test="${vo.zitem6_p ne 'null'}">${vo.zitem6_p}</c:if></span>%</p>
						</form>
					</div>
				</c:if>
				<c:if test="${zregion_gb eq 'N' or zregion_gb eq 'A'}">
					<div class="card">
						<form action="${ctx}/index.do" method="post" onclick="this.submit();" style="cursor:pointer;">
							<h2>긴급교재 신청</h2>
								<input type="hidden" name="url" value="${menuList[8].zmenu_url}"/>
							    <input type="hidden" name="active_bcg_id" value="N"/>
							    <input type="hidden" name="param_search_gb" value="0"/>
					        <p><span><c:if test="${vo.zitem7_p ne 'null'}">${vo.zitem7_p}</c:if></span>%</p>
						</form>
					</div>
				</c:if>
				<div class="card sum" onclick="$('#go-sum').submit();">
					<h2>주요지표</h2>
					<form action="${ctx}/index.do" method="post" id="go-sum" onclick="this.submit();" style="cursor:pointer;">
						<c:choose>
							<c:when test="${zregion_gb eq 'N' or zregion_gb eq 'A'}">
								<input type="hidden" name="url" value="/sum/sumNoonMoni.do"/>
					            <input type="hidden" name="active_bcg_id" value="N"/>
					            <input type="hidden" name="param_search_gb" value="0"/>
					            <span>바로가기</span>
							</c:when>
							<c:when test="${zregion_gb eq 'C'}">
								<input type="hidden" name="url" value="/sum/sumChiMoni.do"/>
					            <input type="hidden" name="active_bcg_id" value="C"/>
					            <input type="hidden" name="param_search_gb" value="0"/>
					            <span>바로가기</span>
							</c:when>
							<c:when test="${zregion_gb eq 'S'}">
								<input type="hidden" name="url" value="/sum/sumSolMoni.do"/>
					            <input type="hidden" name="active_bcg_id" value="S"/>
					            <input type="hidden" name="param_search_gb" value="0"/>
					            <span>바로가기</span>
							</c:when>
						</c:choose>
			        </form>
				</div>
			</div>
		</div>
	</div>
</div>