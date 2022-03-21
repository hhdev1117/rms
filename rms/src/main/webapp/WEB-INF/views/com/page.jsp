<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<script type="text/javascript">
	$(document).ready(function(e){
		setPageInit(1);
	});

	/*=============================================================
	페이지 검색
	=============================================================*/
	var page_totalcnt = 0;
	var row_cnt = 20; <%-- 로우 카운트 --%>
	function btnSearchPage(page) {
		// 페이지 입력이 안들어온경우
		if(isEmpty(page)) {
			page = "1";
			$(".input-num").val(page);
		}

		var page_cnt = Math.floor(page_totalcnt/row_cnt);
		if(page_totalcnt%row_cnt > 0) page_cnt = page_cnt + 1;
		
		// 페이지입력이 큰경우
		// 다음버튼으로 큰수가 들어올수 있다.
		if(page > page_cnt) {
			page = page_cnt;
			$(".input-num").val(page);
		}
		// 페이지 입력이 음수인경우
		// 다음버튼으로 -가 들어올수 있다.
		if(page < 1) {
			page = 1;
			$(".input-num").val(page);
		}

		btnSearch(page);
	}

	/*=============================================================
	다음 페이지 버튼
	=============================================================*/
	function btnNext(page) {
		var nowPage = $(".input-num").val();

		var nextPage = Number(nowPage) + Number(page);
		
		// 널값 체크
		if(isEmpty(nextPage)) {
			nextPage = "1";
			$(".input-num").val(nextPage);
		}
		var page_cnt = Math.floor(page_totalcnt/20);
		if(page_totalcnt%20 > 0) page_cnt = page_cnt + 1;
		
		// 큰경우
		if(nextPage > page_cnt) {
			nextPage = page_cnt;
			$(".input-num").val(nextPage);
		}
		// 음수인경우
		if(nextPage < 1) {
			nextPage = 1;
			$(".input-num").val(nextPage);
		}

		// 페이지 숫자 변경
		$(".input-num").val(nextPage);
		btnSearch(nextPage);
	}

	/*=============================================================
	디폴트 셋팅
	새로 조회할때 적용, 처음불러올때 적용
	=============================================================*/
	function setPageInit(page) {
		page_totalcnt = 0;
		$("#span-totalcnt").html = 0;
		$(".input-num").val(page);
	}
		
	
	/*=============================================================
	페이지를 셋팅한다.
	조회 결과에따라 css를 다르게 하기위한 처리이다.
	호출되는 시점은 조회 이후
	=============================================================*/
	function setPage(result_totalcnt) {
		// 총 페이지 수가 20보다 많으면 아래 페이징처리를 보이게한다.
		page_totalcnt = result_totalcnt;
		
		if(page_totalcnt > 20) {
			$(".page_nation").css("display", "");
		} else {
			$(".page_nation").css("display", "none");
		}

		var page_cnt = Math.floor(page_totalcnt/20);
		if(page_totalcnt%20 > 0) page_cnt = page_cnt + 1;
			
		// 아래 총 페이지수를 표시
		$("#span-totalcnt").html(page_cnt);

		// input 값 1로 설정
		$(".input").val(1);
	}
	
</script>
<div class="page_nation" style="display:none;">
    <a class="arrow pprev" href="javascript:void(0);" onclick="btnNext(-99999)"><img src="${webPath}/images/page_pprev.png" alt=""></a>
    <a class="arrow prev" href="javascript:void(0);" onclick="btnNext(-1)"><img src="${webPath}/images/page_prev.png" alt=""></a>
    <input type="text" class="input-num" value="1" onchange="btnSearchPage(this.value);" onKeyup="this.value=this.value.replace(/[^0-9]/g,'');">
    <span class="num">/&nbsp;<span id="span-totalcnt">1</span></span>
    <a class="arrow next" href="javascript:void(0);" onclick="btnNext(1)"><img src="${webPath}/images/page_next.png" alt=""></a>
    <a class="arrow nnext" href="javascript:void(0);" onclick="btnNext(99999)"><img src="${webPath}/images/page_nnext.png" alt=""></a>
</div>