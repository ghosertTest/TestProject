<!-- ҳ����ʹ����JSTL Core taglib ��Spring lib-->
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!-- �趨ҳ�����ʱ����gb2312���룬ͬʱָ���������ʾʱ��ȡgb2312����-->
<%@ page pageEncoding="gb2312" contentType="text/html;charset=gb2312" isELIgnored="false" %>
<html>
<head>
<title>�û�ע��</title>
</head>
<body style="text-align: center">
<form method="POST" action="register.do">

    <!--
        spring.bind���ͨ��path������CommandClass������󶨡�֮�����ǾͿ��Զ԰󶨵�
        CommandClass�����״̬��Ϣ���з��ʡ������Ƭ���У�����ͨ��ͨ�����*������ǰ
        spring.bind������command���������������󶨣����ڼ��еĴ�����Ϣ��ʾ.

         �����"command"��Spring�е�Ĭ��CommandClass���ƣ��������õ�ǰҳ���Ӧ��
        CommandClassʵ������ǰ�ﾳ�£�Ҳ����com.loadtrend.info.RegisterInfo��.
        
        spring:bind �����ڣ�����ͨ��status.*���ʶ�Ӧ��״̬���ԡ�status.* ��Ӧ��ʵ������
        org.springframework.web.servlet.support.BindStatus
        BindStatus ���ṩ���뵱ǰCommandClass����󶨵�״̬��Ϣ���磺
        status.errorMessages ��Ӧ�󶨶������ԵĴ�����Ϣ��forEach��ʾ
        status.errorMessage  ��Ӧ�󶨶������ԵĴ�����Ϣ��ֱ����ʾ
        status.expression ��Ӧ�󶨶������Ե����ơ�
        status.value ��Ӧ�󶨶������Ե�ǰֵ��
        ���������ɲμ�BindStatus���Java Doc �ĵ���  
    -->
	<spring:bind path="command.*">
		<font color="#FF0000">
		    <c:forEach items="${status.errorMessages}" var="error">
	             ����: <c:out value="${error}" />
	            <br>
		    </c:forEach>
		</font>
	</spring:bind>
	<table border="0" width="450" height="101" cellspacing="0" cellpadding="0">
		<tr>
			<td height="27" width="408" colspan="2">
			    <p align="center">
			        <b>
			             <!-- �û�ע�� -->
			             <spring:message code="register_title" arguments="args1,args2" />
			        </b>
			</td>
		</tr>
		<tr>
			<td height="23" width="104">�û�����</td>
			<td height="23" width="450">
				<spring:bind path="command.username">
					<input type="text" value="<c:out value="${status.value}"/>" name="<c:out value="${status.expression}"/>">
		                 ��������ڵ���4���ַ���
		                <br>
						<c:if test="${status.error}">
							<font color="#FF0000"> ����: 
							    <c:out value="${status.errorMessage}" />
						    </font>
						</c:if>
				</spring:bind>
			</td>
		</tr>
		<tr>
			<td height="23" width="104">���룺</td>
			<td height="23" width="450">
				<spring:bind path="command.password1">
					<input type="password" value="<c:out value="${status.value}"/>" name="<c:out value="${status.expression}"/>">
		                 ��������ڵ���6���ַ���
		                <br>
						<c:if test="${status.error}">
							<font color="#FF0000"> ����: 
								<c:out value="${status.errorMessage}" />
							</font>
						</c:if>
				</spring:bind>
		    </td>
		</tr>
		<tr>
			<td height="23" width="104">�ظ����룺</td>
			<td height="23" width="450">
				<spring:bind path="command.password2">
					<input type="password" value="<c:out value="${status.value}"/>" name="<c:out value="${status.expression}"/>">
					<br>
					<c:if test="${status.error}">
						<font color="#FF0000"> ����: 
							<c:out value="${status.errorMessage}" />
                        </font>
					</c:if>
				</spring:bind>
			</td>
		</tr>
	</table>
    <p>
    <input type="submit" value="�ύ" name="B1"> <input type="reset" value="����" name="B2"></p>
</form>
</body>
</html>
