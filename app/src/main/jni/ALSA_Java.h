#include <jni.h>

#ifndef ALSA_Java
#define ALSA_Java
#ifdef __cplusplus
extern "C" {
#endif
JNIEXPORT jcharArray JNICALL Java_com_pa_eric_lightsapp_TCPClient_ConvertBytesToArray(JNIEnv *env, jbyteArray *bytes);
#ifdef __cplusplus
}
#endif
#endif
