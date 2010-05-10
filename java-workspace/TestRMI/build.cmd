@echo off

@setlocal
echo ----------------------
echo Build TestRMI Project
echo ----------------------

cd serverclasses
rmic examples.hello.HelloImpl

copy examples\hello\Hello.class ..\clientclasses\examples\hello\Hello.class
copy examples\hello\HelloApplet.class ..\clientclasses\examples\hello\HelloApplet.class
copy examples\hello\HelloImpl_Stub.class ..\clientclasses\examples\hello\HelloImpl_Stub.class

start rmiregistry 1099
java -Djava.security.policy=policy examples.hello.HelloImpl

goto end

:error

echo ERROR: Something wrong !

:end