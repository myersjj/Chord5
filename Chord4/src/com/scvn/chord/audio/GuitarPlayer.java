/*
* Created by IntelliJ IDEA.
* User: martin
* Date: 02-04-14
* Time: 19:10:29
* To change template for new class use
* Code Style | Class Templates options (Tools | IDE Options).
*/
package com.scvn.chord.audio;


import com.scvn.chord.KnownChord;
import com.scvn.chord.KnownChordVector;
import com.scvn.chord.ChordConstants;

import javax.sound.midi.Receiver;
import javax.sound.midi.*;


public class GuitarPlayer implements ChordConstants {

    public static final int CHANNEL = 4;
    public static final int INSTRUMENT_ID = 24; //24
    public static final int VELOCITY = 100;
    public static final int NB_STRINGS = 6;
    public static final int SHORT_DELAY = 1;
    public static final int LONG_DELAY = 10;

    Sequencer sequencer;
    Synthesizer synth;
    ShortMessage onMsg[];
    ShortMessage offMsg[];
    ShortMessage chProg;
    int tuning[];
    Object sem = new Object();

    public GuitarPlayer() {
        onMsg = new ShortMessage[NB_STRINGS];
        offMsg = new ShortMessage[NB_STRINGS];
        for (int i = 0; i < NB_STRINGS; i++) {
            onMsg[i] = new ShortMessage();
            offMsg[i] = new ShortMessage();
        }
        chProg = new ShortMessage();

        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            synth = MidiSystem.getSynthesizer();
            synth.open();
            chProg.setMessage(ShortMessage.PROGRAM_CHANGE+CHANNEL,  INSTRUMENT_ID, VELOCITY);
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (InvalidMidiDataException ie) {
            ie.printStackTrace();
            System.exit(1);
        }
        /*
		 *	There is a bug in the Sun jdk1.3.
		 *	It prevents correct termination of the VM.
		 *	So we have to exit ourselves.
		 *	To accomplish this, we register a Listener to the Sequencer.
		 *	It is called when there are "meta" events. Meta event
		 *	47 is end of track.
		 *
		 *	Thanks to Espen Riskedal for finding this trick.
		 */

		sequencer.addMetaEventListener(new MetaEventListener()
			{
				public  void meta(MetaMessage event)
				{
					if (event.getType() == 47)
					{
						synchronized (sem) {
                        sem.notifyAll();
                        }
					}
				}
			});

//        tuning = new int[] { 52, 57, 62, 67, 71, 76 };
        tuning = new int[] { 40, 45, 50, 55, 59, 64 };
    }

    public void playChord(KnownChord kc) {
        int frets[] = kc.getFrets();
        int f[] = new int[frets.length];
        int b = kc.getBaseFret();
        int afret;
        for (int i=0; i< f.length; i++) {
            afret = frets[i];
            if (afret == FRET_OPEN)
            {
                f[i]=0;
            } else if (afret == FRET_X) {
                f[i]=FRET_X;
            } else {
                f[i]=b+afret - 1; //afret == 1 means on current baseFret
            }
        }
        for (int i=0; i< f.length; i++) {
            System.out.print(f[i]+ " ");
        }
        System.out.println();
        playChord(f);
    }

    public  void playChord(int s[]) {
        try {

            Sequence seq = new Sequence(Sequence.PPQ, 10);
            Track track = seq.createTrack();
            track.add(new MidiEvent(chProg, 0));

            int nbNotes = 0;
            int toneID;

            // build messages and single note track
            for (int i = 0; i < s.length; i++) {
                if (s[i] != FRET_X)
                {
                    int fret = (s[i] == FRET_OPEN ? 0 : s[i]);
                    toneID = tuning[i] + fret;
                    //System.out.println(toneID);
                    onMsg[i].setMessage(ShortMessage.NOTE_ON, CHANNEL, toneID, VELOCITY);
                    offMsg[i].setMessage(ShortMessage.NOTE_OFF, CHANNEL, toneID);
                    track.add(new MidiEvent(onMsg[i], nbNotes * LONG_DELAY));
                    nbNotes++;
                }
            }
            // add strummed chord to track
            for (int i = 0; i < s.length; i++) {
                if (s[i] != FRET_X)
                {
                    track.add(new MidiEvent(onMsg[i] , nbNotes * LONG_DELAY + i * SHORT_DELAY));
                }
            }

            // add note_off
            for (int i = 0; i < s.length; i++) {
                if (s[i] != FRET_X)
                {
                    track.add(new MidiEvent(offMsg[i] , (nbNotes+3) * (LONG_DELAY + SHORT_DELAY)));
                }
            }

            System.out.println("Track is " + track.ticks());

            sequencer.setSequence(seq);

            Transmitter tran = sequencer.getTransmitter();
            Receiver synthRcvr = synth.getReceiver();
            tran.setReceiver(synthRcvr);

            sequencer.start();
//            System.out.println("SOW");
            synchronized (sem) {
                sem.wait();
            }
//            System.out.println("EOW");
        } catch (InvalidMidiDataException e1) {
            e1.printStackTrace();
        } catch (MidiUnavailableException e2) {
            e2.printStackTrace();
        } catch (InterruptedException e3) {
            e3.printStackTrace();
        }
    }

    public void close() {
        sequencer.close();
        synth.close();
    }

    public void listInstruments() {
    Instrument list[] = synth.getAvailableInstruments();
    for (int i = 0; i < list.length; i++) {
        System.out.println(i+":"+ list[i].toString());
    }
}

    public static void main (String args[]) {

    GuitarPlayer gp = new GuitarPlayer();
        //gp.listInstruments();


        if (args.length != 1) {
            System.out.println("Usage: GuitarPlayer <chord_name>");
            System.exit(1);
        }
        KnownChordVector kcv = new KnownChordVector();
        KnownChord kc = kcv.getChord(args[0]);
        gp.playChord(kc);
        gp.close();
        System.exit(0);

//        int frets[] = new int[] {-1, 3, 2, 0, 1 , 0};
//        gp.playChord(frets);
    }
}

