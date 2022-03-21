<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<%@ include file="../taglibs.jsp" %>
<%
	String  zlink_key = request.getParameter("zlink_key");
%>
<!DOCTYPE html>
<head>	
	<title>사업운영모니터링 시스템</title>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
	
	<link rel="stylesheet" type="text/css" href="${webPath}/css/font.css">
	<link rel="stylesheet" type="text/css" href="${webPath}/css/default.css">
	<link rel="stylesheet" type="text/css" href="${webPath}/css/login.css">
	
	<script src="${webPath}/js/vendors/jquery-2.2.4.min.js"></script>
	<script src="${webPath}/js/vendors/jquery-ui.min.js"></script>
	<script src="${webPath}/js/vendors/jquery.form.js"></script>
	
	<script src="${webPath}/js/sha256.js"></script>
	<script src="${webPath}/js/util.js"></script>
	
	
	<script type="text/javascript">
		$(document).ready(function(e){


			$("form[name=loginFrm]").ajaxForm({
				//control : ComLoginController
				url : "${ctx}/com/login_link_check.do",
				type: 'POST',
				dataType: 'json',
				success : function(data){
					if(data.isLogin){
						
						location.href="${ctx}" + data.forwardUrl;
					}else{
						alert(data.msg);
						location.href="${ctx}/";
					}
				},
				error : function(){
					alert(data.msg);
					location.href="${ctx}/";
				} 
			}).submit();
			
		});


	</script>
</head>
<body>
<form name="loginFrm">
	<input type='hidden' name='zlink_key' id='zlink_key' value='<c:out value="<%=zlink_key %>" />' />
</form>
	
</body>
</html>
