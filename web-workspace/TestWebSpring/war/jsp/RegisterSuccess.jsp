<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page pageEncoding="gb2312" contentType="text/html;charset=gb2312" isELIgnored="false"%>
<html>
    <body>
        <p align="center">
             用户名：<c:out value="${registerinfo.username}" /> <br>
             密码：<c:out value="${registerinfo.password1}" /> <br>
                      注册成功！
        </p>
    </body>
</html>
