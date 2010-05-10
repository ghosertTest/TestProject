<!-- 页面中使用了JSTL Core taglib 和Spring lib-->
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!-- 设定页面编译时采用UTF-8编码，同时指定浏览器显示时采取UTF-8解码-->
<%@ page pageEncoding="utf-8" contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>讨论列表:</title> 

<link href="css.css" rel="stylesheet" type="text/css">
<style type="text/css">
<!--
.style2 {
	color: #009900;
	font-weight: bold;
}
.style3 {color: #009900}

.TABLE_beijing                    
{                                 
	BACKGROUND-COLOR: #497847;
}                               
.tr_jiange1 {
	background-color: #FFFFFF;
	color: #666666;
}
.tr_jiange2 {                     
	background-color: #F4FFF4;
	color: #666666;           
}               

.TR_biaotou 
{
	BACKGROUND-COLOR: #97C580;
	color: #003300;
	font-weight: bold;
}    

.width3 {
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 12px;
	width: 100px;
}          
  -->
</style>
</head>

<body>
  
<div align="center">     
  
  <br>
  
  <table width="90%" border="0" cellspacing="0" bgcolor="#FFFFFF" >
    <tr>
    <td align="left"><spring:message code="BBS_REQUEST_LIST_TITLE"/></td>
    <td align="right"><a href="postNote.do"><spring:message code="BBS_NEW_REQUEST_TITLE"/></a></td>
    </tr>
  </table>
  
  <br>
  
  <table width="90%" border="0" cellpadding="5" cellspacing="1" class="TABLE_beijing">
   <tr valign="top" class="TR_biaotou">
      <td width="65%"><spring:message code="BBS_NOTE_TITLE"/></td>
      <td width="10%"><spring:message code="BBS_NOTE_AUTHOR"/></td>
      <td width="15%"><spring:message code="BBS_TIME_TITLE"/></td>
      <td width="10%"><spring:message code="BBS_STATUS_TITLE"/></td>
   </tr>
   <c:if test="${notes != null}" >
   <c:forEach items="${notes}" var="note">      
    <tr valign="top" class="tr_jiange1">
      <td width="65%"><div align="left"><a href="listReplyNote.do?noteid=<c:out value="${note.id}" escapeXml="false"/>">
        <c:out value="${note.title}" escapeXml="false"/>
      </a></div></td>
      <td width="10%"><div align="right"><c:out value="${note.author}" escapeXml="false"/></div></td>
      <td width="15%"><div align="center"><c:out value="${note.postTime}" escapeXml="false"/></div></td>
      <td width="10%"><div align="center"><c:out value="${note.status}" escapeXml="false"/></div></td>
    </tr>
   </c:forEach>
   </c:if>
  </table>          

 </div>

 </body>
 </html>
