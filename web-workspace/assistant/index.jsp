<%@ page contentType="text/html; charset=gb2312" language="java" errorPage="" %>
<jsp:useBean id="sp" scope="page" class="assis.ShowPages"/>

<%@ include file="title.jsp"%>

<%
    String url = "";
	url = contextPath+"/ViewSugServlet?initCurrentPage=";
    sp = (assis.ShowPages)request.getAttribute("sp");
    if (sp == null) {
		String initCurrentPage = "1";
		response.sendRedirect(url+initCurrentPage);
%>
<%	} else {%>

<table width="991" border="0">
  <tr>
    <td width="150" rowspan="2"><p>&nbsp;</p>    </td>
    <td width="677" align="left" valign="top" bgcolor="#669999"><p class="style7">���ȱ��棺</p>    </td>
    <td width="150" rowspan="2">&nbsp;</td>
  </tr>
  <tr>
    <td height="91" align="left" valign="top"><p class="style1"><br>
      1 2004-12-15 �����𺽣�Ŀǰ����������<span style="color: #FF0000"><%=assis.OnlineCounter.getCounter()%>��</span></p>    </td>
  </tr>
</table>
<table width="991" border="0">
  <tr>
    <td width="150" rowspan="2" valign="top">&nbsp;</td>
      <td width="677" bgcolor="#6699CC" class="style1"><span class="style8">�û����ԣ�</span>
	    <span class="style8">�ܹ�<%=sp.getTotalRecorders()%>�����Լ�¼</span>&nbsp;
	    <A HREF=<%=url+"1"%>>��1ҳ</A>&nbsp;
	    <A HREF=<%=url+String.valueOf(sp.getMinAvailablePage()-1)%>>��<%=sp.getAvailablePages()%>ҳ</A>&nbsp;
	    <%for(int i = 0; (i + sp.getMinAvailablePage()) <= sp.getMaxAvailablePage(); i++) {%>
            <%  if(sp.getCurrentPage() == sp.getMinAvailablePage()+i) { %>
                    <A HREF=<%=url+String.valueOf(sp.getMinAvailablePage()+i)%>><FONT COLOR="#FFCC99"><%=sp.getMinAvailablePage()+i%></FONT></A>&nbsp;
            <%} else {%>
	                <A HREF=<%=url+String.valueOf(sp.getMinAvailablePage()+i)%>><%=sp.getMinAvailablePage()+i%></A>&nbsp;
	        <%}%>
	    <%}%>
	    <A HREF=<%=url+String.valueOf(sp.getMaxAvailablePage()+1)%>>��<%=sp.getAvailablePages()%>ҳ</A>&nbsp;
        <A HREF=<%=url+String.valueOf(sp.getTotalPages())%>>��<%=sp.getTotalPages()%>ҳ</A>&nbsp;
        &nbsp;&nbsp;&nbsp;
        <A HREF=<%=url+String.valueOf(sp.getCurrentPage()-1)%>>��һҳ</A>&nbsp;
        <A HREF=<%=url+String.valueOf(sp.getCurrentPage()+1)%>>��һҳ</A>&nbsp;
	  </td>
    <td width="150" rowspan="2">&nbsp;</td>
  </tr>
  <tr>
    <td height="115" valign="top" class="style1">
	  <table width="677" border="0" class="style1">
	  <%
	    ArrayList al = sp.getContent();
	    Iterator it = al.iterator();
		assis.Suggestion sug = null;
	    while (it.hasNext()) {
			sug = (assis.Suggestion)it.next();
	  %>
        <tr>
          <td bgcolor="#CCCCCC"><%=sug.getSugTime()%>&nbsp;<%=convert(sug.getSugName())%>&nbsp;��������&nbsp;<%=sug.getSugIp()%></td>
        </tr>
        <tr>
          <td><%=convert(sug.getSuggestion())%>&nbsp;
		    <A HREF="mailto:<%=convert(sug.getSugEmail())%>"><%=convert(sug.getSugEmail())%></A>
			<br><br>
		  </td>
        </tr>
	  <%
	    }
	  %>
      </table>
	</td>
  </tr>
</table>
<table width="990" border="0">
  <tr>
    <td width="150" height="44">&nbsp;</td>
    <td width="676">
	  <form name="subsug" method="post" action="<%=contextPath%>/SubSugServlet" onSubmit="return isSugValid()">
      <p align="center">
        <textarea name="suggestion" cols="100" rows="5" class="style1" id="suggestion"></textarea>
      </p>
      <p align="left">
        &nbsp;&nbsp;&nbsp; 
	    <span class="style1">�����ߣ�
          <input name="sugname" type="text" id="sugname" size="15">�ظ����䣺
          <input name="sugemail" type="text" id="sugemail" size="15">&nbsp;(�ɿ�ȱ)&nbsp;
          <input name="submitsug" type="submit" class="style1" id="submitsug4" value=" �����ύ ">
        </span>
	  </p>
      </form>
	</td>
    <td width="150">&nbsp;</td>
  </tr>
</table>

<script language ="javascript">
function isSugValid() {
	if (subsug.suggestion.value  =="") {
		alert("���Բ���Ϊ��!");
		return false;
	}
	return true;
}
</script>

</body>
</html>

<%	}%>