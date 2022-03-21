<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<%-- 세션을 변경하기 위한 팝업입니다. --%>
<%-- 사용에 주의바랍니다. --%>
<script type="text/javascript">
	function openSessionPopup() {
	    $('#layerbox-session').css("position", "absolute");
	    $('#layerbox-session').css("top",(($(window).height() - $('#layerbox-session').outerHeight()) / 2) + $(window).scrollTop());
	    $('#layerbox-session').css("left",(($(window).width() - $('#layerbox-session').outerWidth()) / 2) + $(window).scrollLeft());
	    $('#layerbox-session').draggable();
	    $('#layerbox-session').show();
	    
		wrapWindowByMaskSession();
	}
	
	function wrapWindowByMaskSession() {
	    var maskHeight = $(document).height(); 
	    var maskWidth = $(window).width();
	  
	    $('#mask-session').css({
	        'width' : maskWidth,
	        'height' : maskHeight
	    });
	    $('#mask-session').fadeTo("slow", 0.5);
	}
	
	function closeSessionPopup() {
		$('#layerbox-session').hide();
	    $('#mask-session').hide();
	}

	function chkToRadio(e) {
		if(e.id == "chkbx1") {
			if($("#" + e.id).prop("checked") == true) {
				$('#chkbx2').prop("checked", "");
			}
		} else if(e.id == "chkbx2") {
			if($("#" + e.id).prop("checked") == true) {
				$('#chkbx1').prop("checked", "");
			}
		}
	}

	function changeSession() {

		var obj = new Object();
		obj.gb = "";
		obj.zregion = "";
		obj.zregion_nm = "";
		obj.zdept_id = "";
		obj.zdept_nm = "";
		obj.zdept_brch_id = "";
		obj.zauth_grp = "";
		obj.zregion_gb = "";
		
		if($("#chkbx1").prop("checked") == true) {
			obj.gb = "1";
		} else if($("#chkbx2").prop("checked") == true) {
			obj.gb = "2";
		}

		if(isEmpty(obj.gb)) {
			alert("변경 권한을 선택해주세요.");
			return;
		}

		$.ajax({
			type : "POST",
			dataType : "json",
			url : "${ctx}/changeSession.do",
			contentType : 'application/json',
			data : JSON.stringify(obj),
			success : function(result) {
				location.reload();
			},
			beforeSend:function(xmlHttpRequest){
	        	xmlHttpRequest.setRequestHeader("AJAX","true");
	        },
			error : function(e) {
				if (e.status == "400") { // 세션만료 에러코드  
					location.href = "${ctx}/logout";
				}
				if (e.status == "401") { // 권한없음 에러코드  
					alert("권한이 없습니다. 관리자에게 문의바랍니다.");
					return;
				}
			}, 
			complete:function(){
				location.reload();
		    }
		});
	}
</script>
<div id="mask-session" class="mask"></div>   
<div id="layerbox-session" class="layerpop">
	<div class="layerpop_area" style="width:500px; height:310px;">
		<div>
	       <div class="title">권한 변경 팝업</div>
	       <div class="layerpop_close" onclick="javascript:closeSessionPopup();">&#10799;</div>
	    </div>
	    <div class="popup-content" style="padding: 22px;">
	    	<div class="tbl">
				<table id="table-menual" style="border-collapse: collapse;">
					<colgroup>
						<col style="width:55%;">
						<col style="width:20%;">
						<col style="width:25%;">
					</colgroup>
					<thead>
						<tr>
							<th>소 속</th>
							<th>구 분</th>
							<th>권 한</th>
						</tr>
					</thead>
					
					<c:choose>
						<c:when test="${zlogin_id eq '10000232'}">
							<tr>
								<td>
									<div class="filterBox option-box" align="left">
										<div class="chkbx">
											<input type="checkbox" id="chkbx1" name="checkbox-btn" onchange="chkToRadio(this);">
											<label for="chkbx1">호남본부</label>
										</div>
								    </div>
								</td>
								<td>본인</td>
								<td>임원</td>
							</tr>
							
							<tr>
								<td>
									<div class="filterBox option-box" align="left">
										<div class="chkbx">
											<input type="checkbox" id="chkbx2" name="checkbox-btn" onchange="chkToRadio(this);">
											<label for="chkbx2">제주본부 > 제주지원팀</label>
										</div>
								    </div>
								</td>
								<td>위임</td>
								<td>임원</td>
							</tr>
						</c:when>
						<c:otherwise>
							<tr>
								<td>
									<div class="filterBox option-box" align="left">
										<div class="chkbx">
											<input type="checkbox" id="chkbx1" name="checkbox-btn" onchange="chkToRadio(this);">
											<label for="chkbx1">호남본부 > 호남지원팀</label>
										</div>
								    </div>
								</td>
								<td>본인</td>
								<td>사업본부</td>
							</tr>
							
							<tr>
								<td>
									<div class="filterBox option-box" align="left">
										<div class="chkbx">
											<input type="checkbox" id="chkbx2" name="checkbox-btn" onchange="chkToRadio(this);">
											<label for="chkbx2">제주본부 > 제주지원팀</label>
										</div>
								    </div>
								</td>
								<td>위임</td>
								<td>사업본부</td>
							</tr>
						</c:otherwise>
				    </c:choose>
				</table>
			</div>
			<div class="pop-btn">
	      		<a href="javascript:changeSession();" class="btn-right">확인</a>
      		</div>
		</div>
	</div>
</div>