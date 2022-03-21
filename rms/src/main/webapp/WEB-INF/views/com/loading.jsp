<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<script type="text/javascript">
	<%--=============================================================
	로딩팝업 오픈
	=============================================================--%>
	function openLoading() {
		$("#loading-popup").css("display", "block");
	}
	
	<%--=============================================================
	로딩팝업 클로즈
	=============================================================--%>
	function closeLoading() {
		$("#loading-popup").css("display", "none");
	}

</script>
<div id="loading-popup" class="modal" style="z-index: 1001;">
	<div class="loading">
		<div class="loader"></div>
	</div>
</div>