<%@ page contentType="text/html; charset=UTF-8" language="java" errorPage="" %>
<html>
  <body>
    <form method="POST" action="login.do">
      <p align="center">登陆</p><br>
         用户名(jiawei): <input type="text" name="username"> <br>
         密码(1234): <input type="password" name="password"> <br>
      <p>
        <input type="submit" value="提交" name="B1">
        <input type="reset" value="重置" name="B2">
        <a href="register.do">注册验证</a>
      </p>
    </form>
  </body>
</html>
