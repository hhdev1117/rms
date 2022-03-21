
$(function(){
	daekyo.init();
});

var daekyo = {

	/* 팝업 열기 */
	openModalPopup:function(url, w, h, num){

		var resizeFn = function(){};

		$("body").append("<div class='modalBg'>");
		$("body").append("<div class='popupCon'>");
		$(".popupCon").append("<div id='ldSet'>");
		
		$(".modalBg").show();

		$(".popupCon").css("opacity", 0).css("position", "absolute");

		$(".modalBg").on('click', function(){

	        daekyo.closeModalPopup();
	    });

		var ifr = $("<iframe/>", {
	        width: w, 
	        height: h,
	        src: url
	    });

	    ifr.on("load", function(e){

	    	$(".popupCon").css( "opacity", 1 );

            resizeFn = function(){
                $(".popupCon").css("left", ($(window).width() - w ) / 2 );
                $(".popupCon").css("top", ($(window).height() - h ) / 2 );

                if( $(window).width() <= 800 ){

                	if( $(".popupCon").width() > 800 ){
                		$(".popupCon").css("left", 0);	
                	}
	            }
            }

            resizeFn();
            
            /* 발급하기 테스트 */
    //         if( num == 1 ){
    //         	$(".popupCon iframe").contents().find(".reportTab .tab").removeClass("on");
				// $(".popupCon iframe").contents().find(".reportTab .tab").eq(num).addClass("on");
    //         	$(".popupCon iframe").contents().find(".popDefBox").hide();
				// $(".popupCon iframe").contents().find(".popDefBox").eq(num).show();
    //         }
           
	    });

	    $("#ldSet").append(ifr);

        $(window).on("resize", function(){

		    resizeFn();
		});
	},

	/* 팝업 닫기 */
	closeModalPopup:function (){
	    $(".modalBg").remove();
	    $(".popupCon").remove();
	}
		
}