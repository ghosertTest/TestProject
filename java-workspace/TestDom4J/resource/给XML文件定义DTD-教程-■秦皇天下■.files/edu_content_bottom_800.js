<!--
function qhtx(id){
var vlu = document.f1.word.value
if (vlu == ""){alert("请输入关键字！");return false;}
if (id==0){window.open("http://www.google.com/search?q="+vlu+"+site:qhtx.net","w0");}
if (id==1){window.open("http://www.baidu.com/baidu?wd="+vlu+"+site:qhtx.net","w1");}
if (id==2){window.open("http://www.yahoo.com.cn/search?p="+vlu+" ","w2");}
if (id==3){window.open("http://www.sogou.com/web?query="+vlu+"+site:qhtx.net","w3");}

if (id==5){window.open("http://www.qhtx.net/Cha/ip.asp?str="+vlu+"","w5");}
if (id==6){window.open("http://www.qhtx.net/Cha/sjh.asp?str="+vlu+"","w6");}
}//-->

document.write('<form name="f1" method="post">');
document.write('<font color="red" size="3"><b>查看更多相关内容：</b></font><input name="word" value="'+key+'" size="25"> <input type="button" value="Google" onclick="qhtx(0);"> <input type="button" value="百度" onclick="qhtx(1);"> <input type="button" value="Yahoo!" onclick="qhtx(2);"> <input type="button" value="搜狗" onclick="qhtx(3);">');
document.write(' <input type="button" value="IP地址" onclick="qhtx(5);"> <input type="button" value="手机号" onclick="qhtx(6);">');
document.write('</form>');