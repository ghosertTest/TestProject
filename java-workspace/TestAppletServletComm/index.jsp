<meta http-equiv="Content-Type" content="text/html; charset=gb2312">

<html>
<body>

<FORM METHOD=POST name="fmTest">

  <input type="text" name="tbLabel" value="Value from tbLabel.">
  <INPUT TYPE="checkbox" NAME="cbBox" value="Value from cbBox0">
  <INPUT TYPE="checkbox" NAME="cbBox" value="Value from cbBox1">

  <SELECT NAME="stTest">
  <option> SHANGHAI </option>
  <option> BEIJING </option>
  <option> GUANGZHOU </option>
  <option> NEWYORK </option>
  </SELECT>

  <applet name="HelloApplet" codebase="appclasses/" code=applet.HelloApplet.class width="200" height="200" mayscript>
  <!--
    <param name = "labelInfo" value = "Value from labelInfo." >
  -->
  </applet>

</FORM>

submit value to Applet for processing, applet invoke Servlet to store the value to database, at last page submit a request to refresh page
<input type="button" name="btSubmit" value="submit" onclick="fnHelloApplet(fmTest)">
<br><br>

<script language="javascript">
  function fnHelloApplet(fm)
  {
	  var comm = fm.tbLabel.value;
	  var blueTooth = fm.cbBox[0].checked;
	  var array = new Array( 3 );
	  array[0] = "I";
	  array[1] = "LOVE";
	  array[2] = "YOU";
	  var selectedIndex = fm.stTest.selectedIndex;
	  var selectText = fm.stTest.options[selectedIndex].text;
	  var evalStatement = "fmTest.action='result.jsp';fmTest.submit()";
	  HelloApplet.startWithParam( comm, blueTooth, array, selectText, evalStatement );
  }
</script>


</body>
</html>