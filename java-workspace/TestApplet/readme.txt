��JavaScript�з���Java AppletС�����еķ����ͱ��� 
ͨ��JavaScript�ṩ��Applet����JavaScript������Է���Java�ı����ͷ��������岽����÷�ʾ�����¡���Ҫע����ǣ�JavaScript�ṩ��applet�������name���ԣ���û�з������¼��������� 
���裺 
1�� ��Ҫ���ʵ�Java AppletС����ķ����ͱ�������ΪPublic���ұ�����Public���������� 
2�� Java AppletС����Ҫ��װ����ܷ��ʣ�����applet����û�ж���onLoad�¼�����������HTML�ĵ��ġ�body����־�У�ָ��ʹ��Window�����onLoad�¼��������� 
3�� Java AppletС���������JavaScript��applet���������ʡ� 
ʾ���� 
��Testtext.java�� 
import java.applet.*; 
���� 
public class Testtext extends Applet 
{ ���� 
public void setText(String s) //setText������������Ϊ��public�� 
{ 
text=s; 
repaint(); 
} 
} 


��Java AppletС������ʹ��JavaScript 
Live Connect�ṩ��Java��JavaScript�Ľӿڣ�����������Java AppletС������ʹ��JavaScript�����岽�����£� 
1�� ��HTML�ű���ʹ�á�APPLET����־��MAYSCRIPT���ԣ�������Java AppletС������ʽű��� 
2�� ��netscape. JavaScript������Java AppletС���� 
3�� ��Java AppletС��������JSObject���getWindow( )��������JavaScript���ڵľ���� 
4�� ��Java AppletС��������JSObject���getMember( )��������JavaScript���� 
5�� ��Java AppletС��������JSObject���eval( )��������JavaScript������ 
ʾ���� 
��ReadForm. Java�� 
import netscape.javascript.JSObject�� 
import netscape.javascript.JSException�� //��������С�����д����쳣�¼� 
���� 
win=JSObject.getWindow(this)�� // ��ȡJavaScript���ھ�������õ�ǰ�ĵ����� 
doc=(JSObject)win.getMember("document")�� // ����JavaScript���� 
form=(JSObject)doc.getMember("textForm")�� 
textField=(JSObject)form.getMember("textField")�� 
text=(String) textField.getMember("value"); //��ȡ�ı�����ֵ 
���� 
win.eval("alert(\"This alert comes from Java!\")")�� 
// ����JavaScript��alert�������� 
