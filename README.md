# highlightTest

## Installation
Eclipse project
1) add -Djava.library.path="${workspace_loc}\highlightIDE\Debug" to VM settings
2) update JDK folder includes in Project properties -> C/C++ build -> Settings -> Tool Settings -> C++ Compiler -> includes
```bash
jdk_folder\include
jdk_folder\include\win32
```
3) Linker flags should include (Project properties -> C/C++ build -> Settings -> Tool Settings -> C++ Linker -> Miscellaneous)
```bash
-Wl,--add-stdcall-alias -static-libgcc -static-libstdc++
```
4) for lib compilation use this commands in src folder:
```bash
javac -d ..\bin -cp . .\cppImp\ColorJNI.java
javac -h ..\jni  -d ..\bin -cp . .\cppImp\highlightIDE.java
```
5) if used MinGW in highlight.h must be used #include "mingw.thread.h" instead of #include <thread>
6) possible issue with MinGW: wrong _WIN32_WINNT, should be redefined in cppImp_highlightIDE.cpp (#define _WIN32_WINNT 0x0A00)

## Usage
1) Run from eclipse
2) copy library to export folder and run from it:
```bash
java -jar ide.jar
```
3) from export folder run:
```bash
java -Djava.library.path="..\Debug" -jar ide.jar 
```


## License
[MIT](https://choosealicense.com/licenses/mit/)