<!-- 页面中使用了JSTL Core taglib 和Spring lib-->
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!-- 设定页面编译时采用gb2312编码，同时指定浏览器显示时采取gb2312解码-->
<%@ page pageEncoding="gb2312" contentType="text/html;charset=gb2312" isELIgnored="false" %>
<html>
<head>
<title>用户注册</title>
</head>
<body style="text-align: center">
<form method="POST" action="register.do">

    <!--
        spring.bind标记通过path参数与CommandClass对象相绑定。之后我们就可以对绑定的
        CommandClass对象的状态信息进行访问。上面的片断中，我们通过通配符“*”将当前
        spring.bind语义与command对象的所有属性相绑定，用于集中的错误信息显示.

         这里的"command"是Spring中的默认CommandClass名称，用于引用当前页面对应的
        CommandClass实例（当前语境下，也就是com.loadtrend.info.RegisterInfo）.
        
        spring:bind 语义内，可以通过status.*访问对应的状态属性。status.* 对应的实际是类
        org.springframework.web.servlet.support.BindStatus
        BindStatus 类提供了与当前CommandClass对象绑定的状态信息，如：
        status.errorMessages 对应绑定对象属性的错误信息。forEach显示
        status.errorMessage  对应绑定对象属性的错误信息。直接显示
        status.expression 对应绑定对象属性的名称。
        status.value 对应绑定对象属性当前值。
        具体描述可参见BindStatus类的Java Doc 文档。  
    -->
	<spring:bind path="command.*">
		<font color="#FF0000">
		    <c:forEach items="${status.errorMessages}" var="error">
	             错误: <c:out value="${error}" />
	            <br>
		    </c:forEach>
		</font>
	</spring:bind>
	<table border="0" width="450" height="101" cellspacing="0" cellpadding="0">
		<tr>
			<td height="27" width="408" colspan="2">
			    <p align="center">
			        <b>
			             <!-- 用户注册 -->
			             <spring:message code="register_title" arguments="args1,args2" />
			        </b>
			</td>
		</tr>
		<tr>
			<td height="23" width="104">用户名：</td>
			<td height="23" width="450">
				<spring:bind path="command.username">
					<input type="text" value="<c:out value="${status.value}"/>" name="<c:out value="${status.expression}"/>">
		                 （必须大于等于4个字符）
		                <br>
						<c:if test="${status.error}">
							<font color="#FF0000"> 错误: 
							    <c:out value="${status.errorMessage}" />
						    </font>
						</c:if>
				</spring:bind>
			</td>
		</tr>
		<tr>
			<td height="23" width="104">密码：</td>
			<td height="23" width="450">
				<spring:bind path="command.password1">
					<input type="password" value="<c:out value="${status.value}"/>" name="<c:out value="${status.expression}"/>">
		                 （必须大于等于6个字符）
		                <br>
						<c:if test="${status.error}">
							<font color="#FF0000"> 错误: 
								<c:out value="${status.errorMessage}" />
							</font>
						</c:if>
				</spring:bind>
		    </td>
		</tr>
		<tr>
			<td height="23" width="104">重复密码：</td>
			<td height="23" width="450">
				<spring:bind path="command.password2">
					<input type="password" value="<c:out value="${status.value}"/>" name="<c:out value="${status.expression}"/>">
					<br>
					<c:if test="${status.error}">
						<font color="#FF0000"> 错误: 
							<c:out value="${status.errorMessage}" />
                        </font>
					</c:if>
				</spring:bind>
			</td>
		</tr>
	</table>
    <p>
    <input type="submit" value="提交" name="B1"> <input type="reset" value="重置" name="B2"></p>
</form>
</body>
</html>
