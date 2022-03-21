<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<%@ include file="taglibs.jsp" %>
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

						
			
		});
		

		function onLogin(){
			
			if (document.all.zloginId.value == ""){
				alert("아이디가 입력되지 않았습니다.");
				document.all.zloginId.focus();
				return;
			}
	
			if (document.all.zpasswd.value == ""){
				alert("비밀번호가 입력되지 않았습니다.");
				document.all.zpasswd.focus();
				return;
			}

			$("input[name=zloginId]").val(jQuery.trim($("input[name=zloginId]").val()));

			var hashed_pw = '' + CryptoJS.SHA256(document.all.zpasswd.value);
			document.loginFrm.zhdpasswd.value = hashed_pw.toUpperCase();
		    document.loginFrm.zhdloginId.value = document.all.zloginId.value;

		    $("form[name=loginFrm]").ajaxForm({
				//control : ComLoginController
				url : "${ctx}/com/checkLogin.do",
				type: 'POST',
				dataType: 'json',
				success : function(data){
					if(data.isLogin){

						if($("#remember-id").is(":checked") == true){
							var userId = document.all.zloginId.value;
							setCookie("Cookie_userid", userId, 30);
						}else{
							setCookie("Cookie_userid", "", 0);
						}
						
						
						location.href="${ctx}"+data.forwardUrl;
						
					}else{
						setCookie("Cookie_userid", "", 0);

						if(data.msg == 'undefined'){
							alert("잠시후 다시 시도해주세요.");
							location.reload();
						}else{
							alert(data.msg);
						}
					}
				},
				error : function(){
					setCookie("Cookie_userid", "", 0);
					
					if(data.msg == 'undefined'){
						alert("잠시후 다시 시도해주세요.");
						location.reload();
					}else{
						alert(data.msg);
					}
				}
			}).submit();
			
		}
		

	</script>
</head>
<body>
	<div class="wrap">
		<div class="login">
			<div class="login-area">
				<div class="logo">
					<img src="${webPath}/images/login/login_logo.png" alt="">
				</div>
				<div class="box">
					<div class="bg">
						<h1 class="title"><span class="bold">사업운영모니터링</span> 시스템</h1>
						<div class="input-box">
							<form>
						    	<fieldset>
						    		<label for="zloginId"></label>
						    		<input id="zloginId" placeholder="아이디" name="zloginId" type="text" autocomplete="off" required="" class="form-control" onKeyDown="if(event.keyCode==13)onLogin()">
						    	</fieldset>
						    	<fieldset>
						    		<label for="zpasswd"></label>
						    		<input id="zpasswd" placeholder="비밀번호" name="zpasswd" type="password" autocomplete="off" required="" class="form-control" onKeyDown="if(event.keyCode==13)onLogin()">
						    	</fieldset>
							</form>
						    <button type="submit" class="btn-login bold" onClick="onLogin()">로그인</button>
						</div>
					</div>
				</div>
				<div class="txt-bttm">Copyright 2021 DAEKYO Co., LTD. ALL Rights Reserved.</div>
			</div>
		</div>
	</div>	
</body>
<form name="loginFrm">
	<input type="hidden" name="zhdloginId" />
	<input type="hidden" name="zhdpasswd" />
</form>
</html>
