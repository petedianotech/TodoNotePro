#include <jni.h>
#include <string>
#include <vector>
#include "core/TodoEngine.h"
#include "core/SearchEngine.h"

static TodoEngine* g_engine = nullptr;
static SearchEngine* g_search = nullptr;

extern "C" {

JNIEXPORT void JNICALL
Java_com_todonotepro_app_native_NativeCore_init(JNIEnv* env, jobject /* this */) {
    if (!g_engine) g_engine = new TodoEngine();
    if (!g_search) g_search = new SearchEngine();
}

JNIEXPORT void JNICALL
Java_com_todonotepro_app_native_NativeCore_shutdown(JNIEnv* env, jobject /* this */) {
    delete g_engine; g_engine = nullptr;
    delete g_search; g_search = nullptr;
}

JNIEXPORT jstring JNICALL
Java_com_todonotepro_app_native_NativeCore_fastSearch(
        JNIEnv* env, jobject /* this */,
        jobjectArray titles, jobjectArray contents, jstring query) {

    if (!g_search) return env->NewStringUTF("[]");

    const char* q = env->GetStringUTFChars(query, nullptr);
    jsize count = env->GetArrayLength(titles);

    std::vector<std::string> titleVec, contentVec;
    titleVec.reserve(count);
    contentVec.reserve(count);

    for (jsize i = 0; i < count; ++i) {
        auto t = (jstring) env->GetObjectArrayElement(titles, i);
        auto c = (jstring) env->GetObjectArrayElement(contents, i);
        const char* tStr = env->GetStringUTFChars(t, nullptr);
        const char* cStr = env->GetStringUTFChars(c, nullptr);
        titleVec.emplace_back(tStr ? tStr : "");
        contentVec.emplace_back(cStr ? cStr : "");
        env->ReleaseStringUTFChars(t, tStr);
        env->ReleaseStringUTFChars(c, cStr);
        env->DeleteLocalRef(t);
        env->DeleteLocalRef(c);
    }

    std::string result = g_search->search(titleVec, contentVec, q ? q : "");
    env->ReleaseStringUTFChars(query, q);

    return env->NewStringUTF(result.c_str());
}

JNIEXPORT jlong JNICALL
Java_com_todonotepro_app_native_NativeCore_sortByPriority(
        JNIEnv* env, jobject /* this */,
        jintArray priorities, jlongArray timestamps) {

    // Placeholder: returns number of items processed (for future expansion)
    jsize len = env->GetArrayLength(priorities);
    return static_cast<jlong>(len);
}

} // extern "C"
