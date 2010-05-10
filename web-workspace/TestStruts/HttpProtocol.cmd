echo off

setlocal
echo ----------------------
echo Test Http Protocal
echo ----------------------

telnet 127.0.0.1 80

GET /TestStruts/index.jsp HTTP/1.0


endlocal