// file derived from sources containing below copyrights

/**
 * \file seq/seq_midi_event.c
 * \brief MIDI byte <-> sequencer event coder
 * \author Takashi Iwai <tiwai@suse.de>
 * \author Jaroslav Kysela <perex@perex.cz>
 * \date 2000-2001
 */

/*
 *  MIDI byte <-> sequencer event coder
 *
 *  Copyright (C) 1998,99,2000 Takashi Iwai <tiwai@suse.de>,
 *			       Jaroslav Kysela <perex@perex.cz>
 *
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as
 *   published by the Free Software Foundation; either version 2.1 of
 *   the License, or (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 */

#include <malloc.h>

#ifndef DOC_HIDDEN

/* midi status */
struct snd_midi_event {
    size_t qlen;                // queue length
    size_t read;                // chars read
    int type;                   // current event type
    unsigned char lastcmd;
    unsigned char nostat;
    size_t bufsize;
    unsigned char *buf;         // input buffer
}

/* event type, index into status_event[] */
/* from 0 to 6 are normal commands (note off, on, etc.) for 0x8?-0xe? */
#define ST_INVALID  7
#define ST_SPECIAL  8
#define ST_SYSEX    ST_SPECIAL
/* from 8 to 15 are events for 0xf0-0xf7 */

/* status event types */
typedef void (*event_encode_t)(snd_midi_event_t *dev, snd_seq_event_t *ev);
typedef void (*event_decode_t)(const snd_seq_event_t *ev, unsigned char *buf);

#endif /* DOC_HIDDEN */

/*
 * prototypes
 */
static void note_event(snd_midi_event_t *dev, snd_seq_event_t *ev);
static void one_param_crtl_event(snd_midi_event_t *dev, snd_seq_event_t *ev);
static void pitchbend_crtl_event(snd_midi_event_t *dev, snd_seq_event_t *ev);
static void two_param_crtl_event(snd_midi_event_t *dev, snd_seq_event_t *ev);
static void one_param_event(snd_midi_event_t *dev, snd_seq_event_t *ev);
