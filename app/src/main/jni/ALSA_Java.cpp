#include <jni.h>
#include <stdlib.h>
#include <stdio.h>
#include <android/log.h>
#include "alsa.h"
#include "ALSA_Java.h"

using namespace std;

JNIEXPORT jcharArray JNICALL Java_com_pa_eric_lightsapp_TCPClient_ConvertBytesToArray(JNIEnv *env, jobject obj, jbyteArray bytes) {
	snd_seq_event_t *ev;
	jbyte *data;
	jchar fill[3];
    jsize len;
   // jbyte *buffer;

	jcharArray ret = env->NewCharArray(3);
	if (ret == NULL) return NULL;

	//len = env->GetArrayLength(*bytes);
	//if (len == 0) return NULL;

   // env->GetByteArrayRegion(*bytes, 0, len, buffer);

	data = env->GetByteArrayElements(bytes, NULL);
	//if (data == NULL) return NULL;

//    memcpy(buffer, data, len);


	ev = (snd_seq_event_t*)data;
	if (ev == NULL) return NULL;

	fill[0] = (jchar)ev->type;
	fill[1] = (jchar)ev->data.control.channel;
	fill[2] = (jchar)ev->data.note.note;

     env->ReleaseByteArrayElements(bytes, data, JNI_ABORT);

    //free(buffer);

	env->SetCharArrayRegion(ret, 0, 3, fill);
	return ret;
}