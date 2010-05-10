从JavaScript中访问Java Applet小程序中的方法和变量 
通过JavaScript提供的Applet对象，JavaScript代码可以访问Java的变量和方法，具体步骤和用法示例如下。需要注意的是，JavaScript提供的applet对象具有name属性，而没有方法和事件处理器。 
步骤： 
1） 将要访问的Java Applet小程序的方法和变量声明为Public，且必须在Public类中声明； 
2） Java Applet小程序要先装入才能访问，尽管applet对象没有定义onLoad事件，但可以在HTML文档的〈body〉标志中，指定使用Window对象的onLoad事件处理器； 
3） Java Applet小程序可以用JavaScript的applet对象来访问。 
示例： 
“Testtext.java” 
import java.applet.*; 
…… 
public class Testtext extends Applet 
{ …… 
public void setText(String s) //setText（）必须声明为“public” 
{ 
text=s; 
repaint(); 
} 
} 


在Java Applet小程序中使用JavaScript 
Live Connect提供了Java与JavaScript的接口，可以允许在Java Applet小程序中使用JavaScript。具体步骤如下： 
1） 在HTML脚本中使用〈APPLET〉标志的MAYSCRIPT属性，以允许Java Applet小程序访问脚本； 
2） 将netscape. JavaScript包导入Java Applet小程序； 
3） 在Java Applet小程序中用JSObject类的getWindow( )方法创建JavaScript窗口的句柄； 
4） 在Java Applet小程序中用JSObject类的getMember( )方法访问JavaScript对象； 
5） 在Java Applet小程序中用JSObject类的eval( )方法调用JavaScript方法。 
示例： 
“ReadForm. Java” 
import netscape.javascript.JSObject； 
import netscape.javascript.JSException； //可允许在小程序中处理异常事件 
…… 
win=JSObject.getWindow(this)； // 获取JavaScript窗口句柄，引用当前文档窗口 
doc=(JSObject)win.getMember("document")； // 访问JavaScript对象 
form=(JSObject)doc.getMember("textForm")； 
textField=(JSObject)form.getMember("textField")； 
text=(String) textField.getMember("value"); //获取文本区的值 
…… 
win.eval("alert(\"This alert comes from Java!\")")； 
// 调用JavaScript的alert（）方法 
