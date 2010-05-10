1 To show XP look like interface in SWT program:
copy lib\javaw.exe.manifest file to the path where javaw.exe exists. ( javaw.exe is the one which start the program or c:\windows\system32 and you should rename the file javaw.exe.manifest with yourownappname.exe.manifest if you don't start the program with javaw.exe)

2 command line to start program:
SWT Examples>javaw -Djava.library.path="lib" -cp .;lib\swt.jar;c:\j2sdk1.4.2_04\jre\lib org.eclipse.swt.examples.addressbook.AddressBook



See more detail in Eclipse help document.


