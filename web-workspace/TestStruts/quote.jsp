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
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>
        <bean:message key="app.price" />: <%= request.getAttribute("PRICE") %>
      </td>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
  </table>
</body>
</html>