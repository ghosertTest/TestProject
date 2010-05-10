#summary BlueCove documentation.
#labels Featured

= BlueCove =

== About ==

BlueCove is a LGPL licensed JSR-82 implementation that currently interfaces with
the Microsoft Bluetooth stack. Originally developed by Intel Research and
currently maintained by volunteers.

== Requirements ==

  * Microsoft Bluetooth stack (currently this means Windows XP SP2 or newer)
  * A Bluetooth device supported by the Microsoft bluetooth stack
  * Java 1.4 or newer (for the binary, might compile on older)
  * Another Bluetooth device to communicate with

== Limitations ==

Due to the Microsoft Bluetooth stack only supporting RFCOMM connections,
BlueCove also only supports RFCOMM connections. The operating system support is
currently limited to Windows XP SP2 and newer, because the Microsoft Bluetooth
stack is not available on other operating systems. If someone writes code to
support another stack and/or operating system, it will be considered for
inclusion.  BlueCove does also not support OBEX, but there are other projects
that can (possibly) be used to archieve OBEX functionality with BlueCove.

== Installation ==

Installation of the binary (already compiled) version of BlueCove is as follows:

  # Download BlueCove binary release
  # Unzip the archive
  # Add `bluecove.jar` to your classpath

== Compilation ==

You need a C++ compiler and JDK. Tested on Visual C++ 2005 Express Edition,
which is available for free download from MSDN. Ant is used as the build tool,
and the compilation is much easier using it.

  # Download BlueCove source release
  # Unzip the source
  # Run `ant`
  # Go into `intelbth`
  # Run `makeheaders.cmd`
  # Open `intelbth.sln`
  # Compile the project for your platform (e.g. 'Release' for 'Win32')
  # Place `intelbth.dll` in `resources`
  # Run `ant jar`

== Source ==

Available as downloadable packages or at the Subversion repository. Organized in:

  * *docs* -  Documentation
  * *intelbth* - The native windows JNI dll
  * *src* - The implementation of JSR-82 with calls to intelbth
  * *test* - Some test programs