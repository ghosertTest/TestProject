<!-- 页面中使用了JSTL Core taglib 和Spring lib-->
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!-- 设定页面编译时采用utf-8编码，同时指定浏览器显示时采取utf-8解码-->
<%@ page pageEncoding="utf-8" contentType="text/html;charset=utf-8" isELIgnored="false" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>发送新问题:</title> 

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
  <br>
  
  <form name="postNote" action="postNote.do" method="post" onSubmit="return isNoteValid()"> 
  <table width="50%" align="left">
    <tr>
      <td align="left"><spring:message code="BBS_NOTE_TITLE"/></td>
    </tr>
    <tr>
      <td align="left">
        <input name="title" type="text" size="80">
      </td>
    </tr>
    <tr>
      <td align="left"><spring:message code="BBS_NOTE_CONTENT_TITLE"/></td>
    </tr>
    <tr>
      <td align="left">
        <textarea name="content" cols="80" rows="10"></textarea>
      </td>
    </tr>
    <tr>
      <td align="left">
        <input name="submit" type="submit" value="<spring:message code="BBS_NOTE_SUBMIT_TITLE"/>">
        <input name="reset" type="reset" value="<spring:message code="BBS_NOTE_RESET_TITLE"/>">
      </td>
    </tr>
  </table>                                                       
  </form>
 </div>
 
 <script language ="javascript">
 
 String.prototype.trim = function()
 {
     return this.replace(/(^[\s]*)|([\s]*$)/g, "");
 }
 
 function isNoteValid()
 {
    var trimTitle = postNote.title.value.trim();
	if ( trimTitle=="")
	{
		alert("The title is empty!");
		return false;
	}
	if ( trimTitle.length>100)
	{
		alert("The title is more than 100 character!");
		return false;
	}
	
	var trimContent = postNote.content.value.trim();
    if ( trimContent=="")
	{
		alert("The content is empty!");
		return false;
	}
	if ( trimContent.length>1000 )
	{
		alert("The content is more than 1000 character!");
		return false;
	}
	
	return true;
 }
 </script>
 </body>
 </html>
