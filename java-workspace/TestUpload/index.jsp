<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>

<form name="main" action="<%=request.getContextPath()%>/MainServlet" enctype="MULTIPART/FORM-DATA" method="post"> 

<br /> 

Select the file to upload: <input type="file" name="file" /> 

<br /> 

<input type="submit" value="Upload" name="submit" /> 

<br /> 

<%
    String filename = (String) request.getAttribute("filename");
    if ( filename != null) { %>
        File Name: <%=filename%>
<% } %>

<br /> 

<%
    String urlname = (String) request.getAttribute("urlname");
    if ( urlname != null) { %>
        Url Name: <a href="<%=urlname%>"><%=urlname%></a>
<% } %>

</form> 


</body>
</html>