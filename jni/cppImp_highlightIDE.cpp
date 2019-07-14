#include <iostream>
#include <iterator>
#include <functional>
#define _WIN32_WINNT 0x0A00 // hack for mingw.thread.h
#include "cppImp_highlightIDE.h"  // Generated

#include "highlight.h"

bool isCanceledChecker(bool value) {
	return value;
}

JNIEXPORT jobjectArray JNICALL Java_cppImp_highlightIDE_highlightCPP(
		JNIEnv *env, jclass thisObj, jstring javaString, jboolean isCanceled) {
	const char *nativeString = env->GetStringUTFChars(javaString, 0);
	using namespace std;

	vector<char> text;
	text.assign(nativeString, nativeString + strlen(nativeString));

	Color colorArr[strlen(nativeString)] = { };

	function<bool()> f = bind(isCanceledChecker, (bool) (isCanceled == JNI_TRUE));

	highlight(text, f, colorArr);
	int arrColorSize = (sizeof(colorArr) / sizeof(*colorArr));

	jclass javaColorClass = env->FindClass("cppImp/ColorJNI");
    if (NULL == javaColorClass) {
    	std::cout << "Class not found " << std::endl;
    	return NULL;
    }

	jobjectArray resColorArr = (jobjectArray) env->NewObjectArray(arrColorSize, javaColorClass, 0);
	for (int i = 0; i < arrColorSize; i++) {
		jobject newColorObj = env->AllocObject(javaColorClass);

		jfieldID rField = env->GetFieldID(javaColorClass , "r", "S");
	    jfieldID gField = env->GetFieldID(javaColorClass , "g", "S");
	    jfieldID bField = env->GetFieldID(javaColorClass , "b", "S");

	    env->SetShortField(newColorObj, rField, colorArr[i].r);
	    env->SetShortField(newColorObj, gField, colorArr[i].g);
	    env->SetShortField(newColorObj, bField, colorArr[i].b);

		env->SetObjectArrayElement(resColorArr, i, newColorObj);
	}

	// release resources
	env->ReleaseStringUTFChars(javaString, 0);
	return resColorArr;
}
