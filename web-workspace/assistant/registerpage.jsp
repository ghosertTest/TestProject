<%@ page contentType="text/html; charset=gb2312" language="java" errorPage="" %>
<%@ include file="title.jsp"%>

<table width="991" border="0">
  <tr>
    <td width="150" rowspan="2"><p>&nbsp;</p>    </td>
    <td width="677" align="left" valign="top" bgcolor="#669999" class="style1"><p class="style7">新用户注册：</p>    </td>
    <td width="150" rowspan="2">&nbsp;</td>
  </tr>
  <tr>
    <td height="91" align="center" valign="middle" class="style1"><p>&nbsp;</p>      
	
	<form name="register" method="post" action="<%=contextPath%>/RegistServlet" onSubmit="return isValidRegister()">
      <table width="494" height="193" border="0">
        <tr class="style1">
          <td width="125" class="style1"><span style="color: #FF0000">*</span>用户名：</td>
          <td width="174" class="style4"><input name="username" type="text" class="style1" id="username" size="25"></td>
          <td width="181" class="style1"><font size="2">(4-16位 不能单独使用数字)</font></td>
        </tr>
        <tr class="style1">
          <td class="style1"><span style="color: #FF0000">*</span>密码：</td>
          <td class="style4"><input name="password" type="password" class="style1" id="password" size="25"></td>
          <td class="style1"><font size="2">(4—16位，可使用英文，数字)</font></td>
        </tr>
        <tr class="style1">
          <td class="style1"><span style="color: #FF0000">*</span>重复密码：</td>
          <td class="style4"><input name="password1" type="password" class="style1" id="password1" size="25"></td>
          <td class="style1">&nbsp;</td>
        </tr>
        <tr class="style1">
          <td class="style1"> 真实姓名：</td>
          <td class="style4"><input name="name" type="text" class="style1" id="name" size="25"></td>
          <td class="style1">&nbsp;</td>
        </tr>
        <tr class="style1">
          <td class="style1">电子信箱：</td>
          <td class="style4"><input name="email" type="text" class="style1" id="email" size="25"></td>
          <td class="style1">&nbsp;</td>
        </tr>
        <tr class="style1">
          <td class="style1">手机号码：</td>
          <td class="style4"><input name="mobile" type="text" class="style1" id="mobile" size="25"></td>
          <td class="style1">&nbsp;</td>
        </tr>
      </table>      
	  <div align="left"><br>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		  <input type="submit" value=" 注 册 ">&nbsp;&nbsp;&nbsp;<input type="reset" value=" 重 填 ">
      </div>
	</form>
	  <p class="style1"><br></p>
	  
	</td>
  </tr>
</table>

<script language ="javascript">
				function isValidRegister() {
					if (register.username.value  =="") {
						alert("用户名不能为空!");
						register.username.focus();
						return false;
					}
					if (isNaN(register.username.value)==false) {
						alert("用户名不能全为数字!");
						register.username.value ="";
						register.username.focus();
						return false;
					}
                    if ((register.username.value).length<4 || (register.username.value).length>15) {
						alert("用户名长度必须大于4位小于15位");
						register.username.value ="";
						register.username.focus();
						return false;
					}
					if (register.password.value =="" || register.password1.value =="") {
						alert("密码不能为空!");
						register.password.value ="";
						register.password1.value ="";
						register.password.focus();
						return false;
					}
					if (register.password.value != register.password1.value) {
						alert("两次密码输入不一致");
						register.password.value ="";
						register.password1.value ="";
						register.password.focus();
						return false;
					}
					if ((register.password.value).length<4 || (register.password.value).length>15) {
						alert("密码长度必须大于4位小于15位");
						register.password.value ="";
						register.password1.value ="";
						register.password.focus();
						return false;
					}
					if ((register.email.value).length>0 && (register.email.value.indexOf("@")<0 || register.email.value.indexOf(".")<0)) {
						alert("无效email地址");
						register.email.value ="";
						register.email.focus();
						return false;
					}
					return true;
				}
</script>
</body>
</html>
