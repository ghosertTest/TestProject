<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html>
<head>
  <title>Wiley Struts Application</title>
</head>
<body>
  <table width="500" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td>&nbsp;</td>
    </tr>
    <tr bgcolor="#36566E">
      <td height="68" width="48%">
        <div align="left">
          <img src="images/hp_logo_wiley.gif" width="220" height="74">
        </div>
      </td>
    </tr>
    <tr>
      <td><A HREF=<%=request.getContextPath()+ "/Translation.do?locale=zh_CN"%>>Chinese</A></td>
      <td><A HREF=<%=request.getContextPath()+ "/Translation.do?locale=en_US"%>>English</A></td>
    </tr>
  </table>
  <html:errors/>
  <!-- delete "name" and "type" attributes are also right, DEPRECATED ! -->
  <html:form action="/Lookup" name="lookupForm" type="wiley.LookupForm" >
    <table width="45%" border="0">
      <tr>
        <td><bean:message key="app.symbol" />:</td>
        <td><html:text property="symbol" /></td>
      </tr>
      <tr>
        <td colspan="2" align="center"><html:submit /></td>
      </tr>
    </table>
  </html:form>
</body>
</html>