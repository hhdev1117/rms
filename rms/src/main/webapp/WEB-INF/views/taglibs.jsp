<%@ page import="java.util.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/><c:set var="webPath" value="${pageContext.request.contextPath}/static"/><c:set var="designPath" value="${pageContext.request.contextPath}/static"/>
<c:set var="serverName" value="${pageContext.request.localName}"/>
<c:set var="serverIp" value="${pageContext.request.localAddr }"/>
<c:set var="serverPort" value="${pageContext.request.localPort }"/>
