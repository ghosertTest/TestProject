<!--
function qhtx(id){
var vlu = document.f1.word.value
if (vlu == ""){alert("������ؼ��֣�");return false;}
if (id==0){window.open("http://www.google.com/search?q="+vlu+"+site:qhtx.net","w0");}
if (id==1){window.open("http://www.baidu.com/baidu?wd="+vlu+"+site:qhtx.net","w1");}
if (id==2){window.open("http://www.yahoo.com.cn/search?p="+vlu+" ","w2");}
if (id==3){window.open("http://www.sogou.com/web?query="+vlu+"+site:qhtx.net","w3");}

if (id==5){window.open("http://www.qhtx.net/Cha/ip.asp?str="+vlu+"","w5");}
if (id==6){window.open("http://www.qhtx.net/Cha/sjh.asp?str="+vlu+"","w6");}
}//-->

document.write('<form name="f1" method="post">');
document.write('<font color="red" size="3"><b>�鿴����������ݣ�</b></font><input name="word" value="'+key+'" size="25"> <input type="button" value="Google" onclick="qhtx(0);"> <input type="button" value="�ٶ�" onclick="qhtx(1);"> <input type="button" value="Yahoo!" onclick="qhtx(2);"> <input type="button" value="�ѹ�" onclick="qhtx(3);">');
document.write(' <input type="button" value="IP��ַ" onclick="qhtx(5);"> <input type="button" value="�ֻ���" onclick="qhtx(6);">');
document.write('</form>');