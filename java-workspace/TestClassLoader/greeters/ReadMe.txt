
Read Me for linking/ex7/greeters directory

The six classes in this directory won't compile here unless
you tell the compiler to include the next directory up in its
path of directories to search for referenced types. To do this
with the JDK 1.2 FCS release, just say:

javac -sourcepath .. *.java

Alternatively, you can move the .java files from this directory
up one directory to linking/ex7. Compile them there, then move
then and the resulting .class files back down to this directory.

bv
