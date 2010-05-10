<%@ page contentType="text/html; charset=gb2312" language="java" errorPage="" %>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<jsp:useBean id="user" scope="application" class="assis.User"/>

<%!
    // convert ISO-8859-1 to GB2312 for showing Chinese on the web
  	public String convert(String s) {

		try {

			s = new String(s.getBytes("ISO-8859-1"), "GB2312");
		} catch (UnsupportedEncodingException usee) {

			usee.printStackTrace();
		}

		return s;
	}
%>
<% 
    String contextPath = request.getContextPath();
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>个人助理网</title>
<style type="text/css">
<!--
.style1 {
	font-family: "宋体";
	font-size: 12px;
}
.style3 {font-family: "宋体"}
.style4 {font-size: 12px}
.style7 {font-family: "宋体"; font-size: 12px; color: #FFFFFF; }
.style8 {color: #FFFFFF}
a:link {
	text-decoration: none;
	color: #FFFFCC;
}
a:visited {
	text-decoration: none;
	color: #FFFFFF;
}
a:hover {
	text-decoration: none;
	color: #FFCC99;
}
a:active {
	text-decoration: none;
	color: #FFFFFF;
}
-->
</style>
</head>

<body>
<table width="991" height="26" border="0">
  <tr>
    <td width="150" height="22"><div align="center"></div></td>
    <td width="677" bgcolor="#996666"><div align="center"><span class="style7"><a href="index.jsp">助理网首页</a> | <a href="addressbook/index.jsp">个人通讯簿</a> | <a href="calendar/index.jsp">个人日程表</a> | <a href="finance/index.jsp">个人理财通</a> | <a href="notebook/index.jsp">个人日记本</a></span></div></td>
    <td width="150">&nbsp;</td>
  </tr>
</table>

<% 
    // show user name if there is "user" in session, if not, show username and password box to verify
	user = (assis.User)session.getAttribute("user");
	if (user != null) { %>
		<table width="991" border="0">
		  <tr>
		    <td width="150" height="23">&nbsp;</td>
			<%
				String sName = null; 
				if (user.getName().length()!=0) {
					sName = user.getName();
				} else {
					sName = user.getUsername();
				}
			%>
		    <td width="337" height="23" bgcolor="#FFFFFF" class="style1"><%= sName%>  欢迎光临！</td>	
		    <td width="338" bgcolor="#FFFFFF"><div align="center">
		      <input name="logout" type="submit" class="style1" id="logout" value=" 退 出 " onClick="location.href='LogoutServlet';">
		    </div></td>
		    <td width="150" height="23">&nbsp;</td>
		  </tr>
		</table>
<% } else { %>
		<table width="991" border="0">
		  <tr>
		    <td width="150" height="23">　</td>
		    <td width="677" height="23" bgcolor="#FFFFFF">
			<form action="<%=contextPath%>/LoginServlet" method="post" name="login" onSubmit="return isValid()" class="style3 style4">
		    	用户名：<input name="username" type="text" class="style1" size="15" maxlength="15">
				密码：<input name="password" type="password" class="style1" size="15" maxlength="15">
				<input name="login" type="submit" id="login" value=" 登 陆 ">
		    	<input name="register" type="button" id="register" value=" 注 册 " onclick="location.href='registerpage.jsp';">
		    </form></td>
		    <td width="150" height="23">　</td>
		  </tr>
		</table>
		<script language ="javascript">
		function isValid() {
			if (login.username.value  =="") {
				alert("用户名不能为空!");
				return false;
			}
			if (login.password.value =="") {
				alert("密码不能为空!");
				return false;
			}
			return true;
		}
		</script>
<% } %>
