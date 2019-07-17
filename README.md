# highlightTest

## Installation
###Eclipse project
1) add `-Djava.library.path="${workspace_loc}\highlightIDE\Debug"` to VM settings
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
5) if used MinGW in _highlight.h_ must be used `#include "mingw.thread.h"` instead of `#include <thread>`
6) possible issue with MinGW: wrong `_WIN32_WINNT`, should be redefined in _cppImp_highlightIDE.cpp_ (`#define _WIN32_WINNT 0x0A00`)

###Bash
It's possible to build lib with console using this commands in _Debug_ folder. Please note to change path to jdk in your system.
```bash
g++ -std=c++1y "-Ijdk_path\\include" "-Ijdk_path\\include\\win32" -O0 -g3 -Wall -c -fmessage-length=0 -o "jni\\cppImp_highlightIDE.o" "..\\jni\\cppImp_highlightIDE.cpp" 
g++ -Wl,--add-stdcall-alias -static-libgcc -static-libstdc++ -shared -o libhighlightTest.dll "jni\\cppImp_highlightIDE.o" 
```

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