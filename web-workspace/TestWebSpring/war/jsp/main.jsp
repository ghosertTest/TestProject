<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
  <body>
    <p>Login Success!!!</p>
    <p>Current User: <c:out value="${logininfo.username}"/><br></p>
    <p>Your current messages:</p>
    <c:forEach items="${messages}" var="item" begin="0" end="9" step="1" varStatus="var">
	  <c:if test="${var.index % 2 == 0}">
	    *
      </c:if>
      item: ${item}, 1+2: ${1+2}, var.index: ${var.index}, var.count: ${var.count}, var.first: ${var.first}, var.last: ${ var.last }
      <br>
    </c:forEach>
  </body>
</html>

<!-- 
      < c : if >并没有提供else子句，使用的时候可能有些不便，此时我们可以通过 < c : choose >
      tag来达到类似的目的：
      <c:choose>
        <c:when test="${var.index % 2 == 0}">
          *
        </c:when>
        <c:otherwise>
          !
        </c:otherwise>
      </c:choose>
 -->