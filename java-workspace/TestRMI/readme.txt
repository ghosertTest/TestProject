1 see the detail in the sun api document
2 create implement "Hello.java", class "HelloImpl.java", "HelloApplet.java" and complier them to "serverclasses" folder
3 cd E:\Project\Eclipse\TestWorkspace\TestRMI\serverclasses
4 rmic example.hello.HelloImpl     [get stub and skel class of HelloImpl]
5 create a policy file under "serverclasses" with content
grant {
	// Allow everything for now
	permission java.security.AllPermission;
};
copy examples.hello.Hello.class examples\hello\HelloApplet.class HelloImpl_Stub.class
from path serverclasses to path clientclasses
6 under the path "serverclasses"
  start rmiregistry
  java -Djava.security.policy=policy examples.hello.HelloImpl
7 click hello.html to test applet RMI result, show "hello world" if it works.
8 copy TestRMI.xml to D:\Tomcat 5.0\conf\Catalina\www.ghosert.3322.org to build Tomcat
9 if you set hello.html path in tomcat, you can't visit the page on your own machine like:     http://localhost/TestAppletRMIServlet/hello.html
  to solve this problem,you can use the compute name instead of "localhost" on your machine.
  http://compute name/TestAppletRMIServlet/hello.html
  Or you can band other domain on your machine like:
  http://www.ghosert.3322.org/TestAppletRMIServlet/hello.html
  see the detail in tomcat
