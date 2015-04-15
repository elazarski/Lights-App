#include <jni.h>
#include <stdlib.h>
#include "alsa.h"
#include "ALSA_Java.h"

using namespace std;

JNIEXPORT jcharArray JNICALL ConvertBytesToArray(JNIEnv *env, jbyteArray bytes) {
	snd_seq_event_t *ev;
	jbyte *data;
	jchar fill[3];

	jcharArray ret = env->NewCharArray(3);

	if (ret == NULL) return NULL;

	data = env->GetByteArrayElements(bytes, NULL);
	ev = (snd_seq_event_t*)data;

	if (ev == NULL) return NULL;

	fill[0] = (jchar)ev->type;
	fill[1] = (jchar)ev->data.control.channel;
	fill[2] = (jchar)ev->data.note.note;

	env->ReleaseByteArrayElements(bytes, data, JNI_ABORT);

	env->SetCharArrayRegion(ret, 0, 3, fill);
	return ret;
}