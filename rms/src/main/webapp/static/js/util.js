function getCookieVal(offset) 
{
        var endstr = document.cookie.indexOf (";", offset);
        if (endstr == -1) endstr = document.cookie.length;
        
        return unescape(document.cookie.substring(offset, endstr));
}

function getCookie(name) 
{
        var arg = name + "=";
        var alen = arg.length;
        var clen = document.cookie.length;
        var i = 0;
        while (i < clen) {
          var j = i + alen;
          if (document.cookie.substring(i, j) == arg)
                return getCookieVal(j);
          i = document.cookie.indexOf(" ", i) + 1;
          if (i == 0) break;
        }
        return null;
}

function setCookie( name, value, expiredays ) 
{
   var todayDate = new Date();
   todayDate.setTime(todayDate.getTime() + 12*60*60*1000);
   document.cookie = name + "=" + escape( value ) + "; path=/; expires=" + todayDate.toGMTString() + ";"
}

function deleteCookie(cookieName){
    var expireDate = new Date();
    expireDate.setDate(expireDate.getDate() - 1);
    document.cookie = cookieName + "= " + "; expires=" + expireDate.toGMTString();
}

/*=============================================================
금액을 콤마찍히게 변경
=============================================================*/
function numberWithCommas(price) {
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

/*=============================================================
null 값 체크
=============================================================*/
function isEmpty(str){
	if(typeof str == "undefined" || str == null || str == "" || str == " ")
		return true;
	else
		return false;
}

/*=============================================================
날짜 VIEW용으로 변경 YYYY-MM-DD
=============================================================*/
function changeDateToView(yyyymmdd){
	if(isEmpty(yyyymmdd)) return "";
	else return yyyymmdd.substring(0, 4) + "-" + yyyymmdd.substring(4, 6) + "-" + yyyymmdd.substring(6, 8);
}

/*=============================================================
날짜 VIEW용으로 변경 YYYY-MM
=============================================================*/
function changeDateToView2(yyyymm){
	if(isEmpty(yyyymm)) return "";
	else return yyyymm.substring(0, 4) + "-" + yyyymm.substring(4, 6);
}

/*=============================================================
교사사번 8자리 변경 00XXXXXXXX -> XXXXXXXX
=============================================================*/
function changeEmpIdToKunnr(zemp_id){
	return zemp_id.substring(2);
}