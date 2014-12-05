/*
 * The CHORD utility for guitar players is distributed under the terms of the
 * general GNU license.
 *
 * Copyright (C) 2001  Martin Leclerc & Mario Dorion
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.scvn.chord;

public class KnownChordVector extends ChordVector{


    /*--------------------------------------------------------------------------------*/
    public KnownChordVector() {
        init();
    }
    /*--------------------------------------------------------------------------------*/
    public void clean() {

        for (int i = 0; i < size(); ) {
            if (getChord(i).origin == CHORD_DEFINED ) {
                /* remove from known chords */
                removeElementAt(i);

            } else {
                i++;
            }
        }

    }
    /*--------------------------------------------------------------------------------*/
    void doDefineChord(String arg, boolean in_chordrc, Producer producer) {
        int dot_array[];
        String chord_name;
        String temp_str;
        int fret_displ;
        int n;
        boolean hardtoplay = false;
        int pos2 = 0;

        dot_array = new int[6];

        arg = arg.trim();
        pos2 = arg.indexOf(' ');
        chord_name= arg.substring(0, pos2);
        if (chord_name.length() == 0) {
            ParsingErrorSet.addSyntaxError(ERR_MISSING_NAME, null, producer);
            return;
        }
        arg = arg.toLowerCase();

        arg = arg.substring(pos2).trim();
        pos2 = arg.indexOf(' ');
        temp_str = arg.substring(0, pos2);
        if (! temp_str.equals(BASE_FRET_STR)) {
            ParsingErrorSet.addSyntaxError(ERR_MISSIGN_BASE_FRET_WORD, null, producer);
            return;
        }

        arg = arg.substring(pos2).trim();
        pos2 = arg.indexOf(' ');
        temp_str = arg.substring(0, pos2);
        if (temp_str.length() == 0) {
            ParsingErrorSet.addSyntaxError(ERR_MISSING_BASE_FRET_VAL,null, producer);
            return;
        }
        fret_displ= Integer.parseInt(temp_str.trim());
        if (fret_displ == 0) fret_displ = 1;

        arg = arg.substring(pos2).trim();
        pos2 = arg.indexOf(' ');
        temp_str = arg.substring(0, pos2);
        if (! temp_str.equals(FRETS_STR)) {
            ParsingErrorSet.addSyntaxError(ERR_MISSIGN_BASE_FRET_WORD, null, producer);
            return;
        }

        for (n = 0; n < 6; n++) {
            arg = arg.substring(pos2).trim();
            pos2 = arg.indexOf(' ');
            if (pos2 < 0) {
                pos2 = arg.length();
            }
            temp_str = arg.substring(0, pos2);
            //    System.out.println("  temp_str=["+temp_str+"]");

            if (temp_str.length() != 0) {
                if (temp_str.equals(FRET_NONE_STR) || temp_str.equals(FRET_X_STR)) {
                    dot_array[n]= KnownChord.FRET_X;
                }
                else {
                    dot_array[n]= Integer.parseInt(temp_str);
                    //        System.out.println("  dot_array[n]=["+dot_array[n]+"]");
                    if (dot_array[n] < 0) {
                        dot_array[n]= KnownChord.FRET_X;
                    }
                }
                hardtoplay= hardtoplay || (dot_array[n]>LONG_FINGERS);
            }
            else {
                ParsingErrorSet.addSyntaxError(ERR_MISSING_FRETS_VAL, null, producer);
                return;
            }
        }

        temp_str = arg.substring(pos2).trim();
        if (temp_str.length() != 0) {
            ParsingErrorSet.addSyntaxError(ERR_EXTRA_FRETS_VAL, null, producer);
            return;
        }

        /* CHORD is OK */
/*
        if (hardtoplay)
                {
                sprintf(mesg, "You'll need long fingers to play \"%frets\" this way!", chord_name);
                error (mesg);
                }
         */

        add(chord_name,
        dot_array[0],dot_array[1],dot_array[2],
        dot_array[3],dot_array[4],dot_array[5],
        fret_displ, in_chordrc ? KnownChord.CHORD_IN_CHORDRC : KnownChord.CHORD_DEFINED,
        CHORD_HARD);
    }
    /*--------------------------------------------------------------------------------*/

    public void init() {
        add("Ab",	 1, 3, 3, 2, 1, 1,	 4, CHORD_BUILTIN, CHORD_HARD);
        add("Ab+",	 FRET_X, FRET_X, 2, 1, 1, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Ab4",	 FRET_X, FRET_X, 1, 1, 2, 4,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Ab7",	 FRET_X, FRET_X, 1, 1, 1, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Ab11",	 1, 3, 1, 3, 1, 1,	 4, CHORD_BUILTIN, CHORD_HARD);
        add("Absus",	 FRET_X, FRET_X, 1, 1, 2, 4,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Absus4",	 FRET_X, FRET_X, 1, 1, 2, 4,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Abdim",	 FRET_X, FRET_X, FRET_OPEN, 1, FRET_OPEN, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Abmaj",	 1, 3, 3, 2, 1, 1,	 4, CHORD_BUILTIN, CHORD_HARD);
        add("Abmaj7",	 FRET_X, FRET_X, 1, 1, 1, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Abm",	 1, 3, 3, 1, 1, 1,	 4, CHORD_BUILTIN, CHORD_HARD);
        add("Abmin",	 1, 3, 3, 1, 1, 1,	 4, CHORD_BUILTIN, CHORD_HARD);
        add("Abm7",	 FRET_X, FRET_X, 1, 1, 1, 1,	 4, CHORD_BUILTIN, CHORD_HARD);

        add("A",	 FRET_X, FRET_OPEN, 2, 2, 2, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("A+",	 FRET_X, FRET_OPEN, FRET_X, 2, 2, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("A4",	 FRET_OPEN, FRET_OPEN, 2, 2, FRET_OPEN, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("A6",	 FRET_X, FRET_X, 2, 2, 2, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("A7",	 FRET_X, FRET_OPEN, 2, FRET_OPEN, 2, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("A7+",	 FRET_X, FRET_X, 3, 2, 2, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("A7(9+)",	 FRET_X, 2, 2, 2, 2, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("A9",	 FRET_X, FRET_OPEN, 2, 1, FRET_OPEN, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("A11",	 FRET_X, 4, 2, 4, 3, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("A13",	 FRET_X, FRET_OPEN, 1, 2, 3, 1,	 5, CHORD_BUILTIN, CHORD_HARD);
        add("A7sus4",	 FRET_OPEN, FRET_OPEN, 2, FRET_OPEN, 3, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("A9sus",	 FRET_X, FRET_OPEN, 2, 1, FRET_OPEN, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Asus",	 FRET_X, FRET_X, 2, 2, 3, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Asus2",	 FRET_OPEN, FRET_OPEN, 2, 2, FRET_OPEN, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Asus4",	 FRET_X, FRET_X, 2, 2, 3, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Adim",	 FRET_X, FRET_X, 1, 2, 1, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Amaj",	 FRET_X, FRET_OPEN, 2, 2, 2, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Amaj7",	 FRET_X, FRET_OPEN, 2, 1, 2, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Am",	 FRET_X, FRET_OPEN, 2, 2, 1, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Amin",	 FRET_X, FRET_OPEN, 2, 2, 1, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("A/D",	 FRET_X, FRET_X, FRET_OPEN, FRET_OPEN, 2, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("A/F#",	 2, FRET_OPEN, 2, 2, 2, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("A/G#",	 4, FRET_OPEN, 2, 2, 2, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);

        add("Am#7",	 FRET_X, FRET_X, 2, 1, 1, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Am(7#)",	 FRET_X, FRET_OPEN, 2, 2, 1, 4,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Am6",	 FRET_X, FRET_OPEN, 2, 2, 1, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Am7",	 FRET_X, FRET_OPEN, 2, 2, 1, 3,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("Am7sus4",	 FRET_OPEN, FRET_OPEN, FRET_OPEN, FRET_OPEN, 3, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Am9",	 FRET_X, FRET_OPEN, 1, 1, 1, 3,	 5, CHORD_BUILTIN, CHORD_HARD);
        add("Am/G",	 3, FRET_OPEN, 2, 2, 1, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Amadd9",	 FRET_OPEN, 2, 2, 2, 1, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Am(add9)",	 FRET_OPEN, 2, 2, 2, 1, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);

        add("A#",	 FRET_X, 1, 3, 3, 3, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("A#+",	 FRET_X, FRET_X, FRET_OPEN, 3, 3, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("A#4",	 FRET_X, FRET_X, 3, 3, 4, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("A#7",	 FRET_X, FRET_X, 1, 1, 1, 2,	 3, CHORD_BUILTIN, CHORD_HARD);
        add("A#sus",	 FRET_X, FRET_X, 3, 3, 4, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("A#sus4",	 FRET_X, FRET_X, 3, 3, 4, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("A#maj",	 FRET_X, 1, 3, 3, 3, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("A#maj7",	 FRET_X, 1, 3, 2, 3, FRET_X,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("A#dim",	 FRET_X, FRET_X, 2, 3, 2, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("A#m",	 FRET_X, 1, 3, 3, 2, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("A#min",	 FRET_X, 1, 3, 3, 2, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("A#m7",	 FRET_X, 1, 3, 1, 2, 1,	 1, CHORD_BUILTIN, CHORD_HARD);

        add("Bb",	 FRET_X, 1, 3, 3, 3, 1,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("Bb+",	 FRET_X, FRET_X, FRET_OPEN, 3, 3, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Bb4",	 FRET_X, FRET_X, 3, 3, 4, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Bb6",	 FRET_X, FRET_X, 3, 3, 3, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Bb7",	 FRET_X, FRET_X, 1, 1, 1, 2,	 3, CHORD_BUILTIN, CHORD_HARD);
        add("Bb9",	 1, 3, 1, 2, 1, 3,	 6, CHORD_BUILTIN, CHORD_HARD);
        add("Bb11",	 1, 3, 1, 3, 4, 1,	 6, CHORD_BUILTIN, CHORD_HARD);
        add("Bbsus",	 FRET_X, FRET_X, 3, 3, 4, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Bbsus4",	 FRET_X, FRET_X, 3, 3, 4, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Bbmaj",	 FRET_X, 1, 3, 3, 3, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Bbmaj7",	 FRET_X, 1, 3, 2, 3, FRET_X,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Bbdim",	 FRET_X, FRET_X, 2, 3, 2, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Bbm",	 FRET_X, 1, 3, 3, 2, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Bbmin",	 FRET_X, 1, 3, 3, 2, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Bbm7",	 FRET_X, 1, 3, 1, 2, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Bbm9",	 FRET_X, FRET_X, FRET_X, 1, 1, 3,	 6, CHORD_BUILTIN, CHORD_HARD);

        add("B",	 FRET_X, 2, 4, 4, 4, 2,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("B+",	 FRET_X, FRET_X, 1, FRET_OPEN, FRET_OPEN, 4,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("B4",	 FRET_X, FRET_X, 3, 3, 4, 1,	 2, CHORD_BUILTIN, CHORD_HARD);
        add("B7",	 FRET_OPEN, 2, 1, 2, FRET_OPEN, 2,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("B7+",	 FRET_X, 2, 1, 2, FRET_OPEN, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("B7+5",	 FRET_X, 2, 1, 2, FRET_OPEN, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("B7#9",	 FRET_X, 2, 1, 2, 3, FRET_X,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("B7(#9)",	 FRET_X, 2, 1, 2, 3, FRET_X,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("B9",	 1, 3, 1, 2, 1, 3,	 7, CHORD_BUILTIN, CHORD_HARD);
        add("B11",	 1, 3, 3, 2, FRET_OPEN, FRET_OPEN,	 7, CHORD_BUILTIN, CHORD_HARD);
        add("B11/13",	 FRET_X, 1, 1, 1, 1, 3,	 2, CHORD_BUILTIN, CHORD_HARD);
        add("B13",	 FRET_X, 2, 1, 2, FRET_OPEN, 4,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Bsus",	 FRET_X, FRET_X, 3, 3, 4, 1,	 2, CHORD_BUILTIN, CHORD_HARD);
        add("Bsus4",	 FRET_X, FRET_X, 3, 3, 4, 1,	 2, CHORD_BUILTIN, CHORD_HARD);
        add("Bmaj",	 FRET_X, 2, 4, 3, 4, FRET_X,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Bmaj7",	 FRET_X, 2, 4, 3, 4, FRET_X,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Bdim",	 FRET_X, FRET_X, FRET_OPEN, 1, FRET_OPEN, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Bm",	 FRET_X, 2, 4, 4, 3, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Bmin",	 FRET_X, 2, 4, 4, 3, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("B/F#",	 FRET_OPEN, 2, 2, 2, FRET_OPEN, FRET_OPEN,	 2, CHORD_BUILTIN, CHORD_HARD);
        add("BaddE",	 FRET_X, 2, 4, 4, FRET_OPEN, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("B(addE)",	 FRET_X, 2, 4, 4, FRET_OPEN, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("BaddE/F#",	 2, FRET_X, 4, 4, FRET_OPEN, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);

        add("Bm6",	 FRET_X, FRET_X, 4, 4, 3, 4,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Bm7",	 FRET_X, 1, 3, 1, 2, 1,	 2, CHORD_BUILTIN, CHORD_EASY);
        add("Bmmaj7",	 FRET_X, 1, 4, 4, 3, FRET_X,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Bm(maj7)",	 FRET_X, 1, 4, 4, 3, FRET_X,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Bmsus9",	 FRET_X, FRET_X, 4, 4, 2, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Bm(sus9)",	 FRET_X, FRET_X, 4, 4, 2, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Bm7b5",	 1, 2, 4, 2, 3, 1,	 1, CHORD_BUILTIN, CHORD_HARD);

        add("C",	 FRET_X, 3, 2, FRET_OPEN, 1, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("C+",	 FRET_X, FRET_X, 2, 1, 1, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("C4",	 FRET_X, FRET_X, 3, FRET_OPEN, 1, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("C6",	 FRET_X, FRET_OPEN, 2, 2, 1, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("C7",	 FRET_OPEN, 3, 2, 3, 1, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("C9",	 1, 3, 1, 2, 1, 3,	 8, CHORD_BUILTIN, CHORD_HARD);
        add("C9(11)",	 FRET_X, 3, 3, 3, 3, FRET_X,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("C11",	 FRET_X, 1, 3, 1, 4, 1,	 3, CHORD_BUILTIN, CHORD_HARD);
        add("Csus",	 FRET_X, FRET_X, 3, FRET_OPEN, 1, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Csus2",	 FRET_X, 3, FRET_OPEN, FRET_OPEN, 1, FRET_X,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Csus4",	 FRET_X, FRET_X, 3, FRET_OPEN, 1, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Csus9",	 FRET_X, FRET_X, 4, 1, 2, 4,	 7, CHORD_BUILTIN, CHORD_HARD);
        add("Cmaj",	 FRET_OPEN, 3, 2, FRET_OPEN, 1, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Cmaj7",	 FRET_X, 3, 2, FRET_OPEN, FRET_OPEN, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Cm",	 FRET_X, 1, 3, 3, 2, 1,	 3, CHORD_BUILTIN, CHORD_HARD);
        add("Cmin",	 FRET_X, 1, 3, 3, 2, 1,	 3, CHORD_BUILTIN, CHORD_HARD);
        add("Cdim",	 FRET_X, FRET_X, 1, 2, 1, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("C/B",	 FRET_X, 2, 2, FRET_OPEN, 1, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Cadd2/B",	 FRET_X, 2, FRET_OPEN, FRET_OPEN, 1, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("CaddD",	 FRET_X, 3, 2, FRET_OPEN, 3, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("C(addD)",	 FRET_X, 3, 2, FRET_OPEN, 3, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Cadd9",	 FRET_X, 3, 2, FRET_OPEN, 3, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("C(add9)",	 FRET_X, 3, 2, FRET_OPEN, 3, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);

        add("C3",	 FRET_X, 1, 3, 3, 2, 1,	 3, CHORD_BUILTIN, CHORD_EASY);
        add("Cm7",	 FRET_X, 1, 3, 1, 2, 1,	 3, CHORD_BUILTIN, CHORD_EASY);
        add("Cm11",	 FRET_X, 1, 3, 1, 4, FRET_X,	 3, CHORD_BUILTIN, CHORD_HARD);

        add("C#",	 FRET_X, FRET_X, 3, 1, 2, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("C#+",	 FRET_X, FRET_X, 3, 2, 2, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("C#4",	 FRET_X, FRET_X, 3, 3, 4, 1,	 4, CHORD_BUILTIN, CHORD_HARD);
        add("C#7",	 FRET_X, FRET_X, 3, 4, 2, 4,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("C#7(b5)",	 FRET_X, 2, 1, 2, 1, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("C#sus",	 FRET_X, FRET_X, 3, 3, 4, 1,	 4, CHORD_BUILTIN, CHORD_HARD);
        add("C#sus4",	 FRET_X, FRET_X, 3, 3, 4, 1,	 4, CHORD_BUILTIN, CHORD_HARD);
        add("C#maj",	 FRET_X, 4, 3, 1, 1, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("C#maj7",	 FRET_X, 4, 3, 1, 1, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("C#dim",	 FRET_X, FRET_X, 2, 3, 2, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("C#m",	 FRET_X, FRET_X, 2, 1, 2, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("C#min",	 FRET_X, FRET_X, 2, 1, 2, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("C#add9",	 FRET_X, 1, 3, 3, 1, 1,	 4, CHORD_BUILTIN, CHORD_HARD);
        add("C#(add9)",	 FRET_X, 1, 3, 3, 1, 1,	 4, CHORD_BUILTIN, CHORD_HARD);
        add("C#m7",	 FRET_X, FRET_X, 2, 4, 2, 4,	 1, CHORD_BUILTIN, CHORD_HARD);

        add("Db",	 FRET_X, FRET_X, 3, 1, 2, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Db+",	 FRET_X, FRET_X, 3, 2, 2, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Db7",	 FRET_X, FRET_X, 3, 4, 2, 4,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Dbsus",	 FRET_X, FRET_X, 3, 3, 4, 1,	 4, CHORD_BUILTIN, CHORD_HARD);
        add("Dbsus4",	 FRET_X, FRET_X, 3, 3, 4, 1,	 4, CHORD_BUILTIN, CHORD_HARD);
        add("Dbmaj",	 FRET_X, FRET_X, 3, 1, 2, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Dbmaj7",	 FRET_X, 4, 3, 1, 1, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Dbdim",	 FRET_X, FRET_X, 2, 3, 2, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Dbm",	 FRET_X, FRET_X, 2, 1, 2, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Dbmin",	 FRET_X, FRET_X, 2, 1, 2, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Dbm7",	 FRET_X, FRET_X, 2, 4, 2, 4,	 1, CHORD_BUILTIN, CHORD_HARD);

        add("D",	 FRET_X, FRET_X, FRET_OPEN, 2, 3, 2,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("D+",	 FRET_X, FRET_X, FRET_OPEN, 3, 3, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("D4",	 FRET_X, FRET_X, FRET_OPEN, 2, 3, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("D6",	 FRET_X, FRET_OPEN, FRET_OPEN, 2, FRET_OPEN, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("D7",	 FRET_X, FRET_X, FRET_OPEN, 2, 1, 2,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("D7#9",	 FRET_X, 2, 1, 2, 3, 3,	 4, CHORD_BUILTIN, CHORD_HARD);
        add("D7(#9)",	 FRET_X, 2, 1, 2, 3, 3,	 4, CHORD_BUILTIN, CHORD_HARD);
        add("D9",	 1, 3, 1, 2, 1, 3,	10, CHORD_BUILTIN, CHORD_HARD);
        add("D11",	 3, FRET_OPEN, FRET_OPEN, 2, 1, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Dsus",	 FRET_X, FRET_X, FRET_OPEN, 2, 3, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Dsus2",	 FRET_OPEN, FRET_OPEN, FRET_OPEN, 2, 3, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Dsus4",	 FRET_X, FRET_X, FRET_OPEN, 2, 3, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("D7sus2",	 FRET_X, FRET_OPEN, FRET_OPEN, 2, 1, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("D7sus4",	 FRET_X, FRET_OPEN, FRET_OPEN, 2, 1, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Dmaj",	 FRET_X, FRET_X, FRET_OPEN, 2, 3, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Dmaj7",	 FRET_X, FRET_X, FRET_OPEN, 2, 2, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Ddim",	 FRET_X, FRET_X, FRET_OPEN, 1, FRET_OPEN, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Dm",	 FRET_X, FRET_X, FRET_OPEN, 2, 3, 1,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("Dmin",	 FRET_X, FRET_X, FRET_OPEN, 2, 3, 1,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("D/A",	 FRET_X, FRET_OPEN, FRET_OPEN, 2, 3, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("D/B",	 FRET_X, 2, FRET_OPEN, 2, 3, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("D/C",	 FRET_X, 3, FRET_OPEN, 2, 3, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("D/C#",	 FRET_X, 4, FRET_OPEN, 2, 3, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("D/E",	 FRET_X, 1, 1, 1, 1, FRET_X,	 7, CHORD_BUILTIN, CHORD_HARD);
        add("D/G",	 3, FRET_X, FRET_OPEN, 2, 3, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("D5/E",	 FRET_OPEN, 1, 1, 1, FRET_X, FRET_X,	 7, CHORD_BUILTIN, CHORD_HARD);
        add("Dadd9",	 FRET_OPEN, FRET_OPEN, FRET_OPEN, 2, 3, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("D(add9)",	 FRET_OPEN, FRET_OPEN, FRET_OPEN, 2, 3, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("D9add6",	 1, 3, 3, 2, FRET_OPEN, FRET_OPEN,	10, CHORD_BUILTIN, CHORD_HARD);
        add("D9(add6)",	 1, 3, 3, 2, FRET_OPEN, FRET_OPEN,	10, CHORD_BUILTIN, CHORD_HARD);

        add("Dm6(5b)",	 FRET_X, FRET_X, FRET_OPEN, 1, FRET_OPEN, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Dm7",	 FRET_X, FRET_X, FRET_OPEN, 2, 1, 1,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("Dm#5",	 FRET_X, FRET_X, FRET_OPEN, 3, 3, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Dm(#5)",	 FRET_X, FRET_X, FRET_OPEN, 3, 3, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Dm#7",	 FRET_X, FRET_X, FRET_OPEN, 2, 2, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Dm(#7)",	 FRET_X, FRET_X, FRET_OPEN, 2, 2, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Dm/A",	 FRET_X, FRET_OPEN, FRET_OPEN, 2, 3, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Dm/B",	 FRET_X, 2, FRET_OPEN, 2, 3, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Dm/C",	 FRET_X, 3, FRET_OPEN, 2, 3, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Dm/C#",	 FRET_X, 4, FRET_OPEN, 2, 3, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Dm9",	 FRET_X, FRET_X, 3, 2, 1, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);

        add("D#",	 FRET_X, FRET_X, 3, 1, 2, 1,	 3, CHORD_BUILTIN, CHORD_HARD);
        add("D#+",	 FRET_X, FRET_X, 1, FRET_OPEN, FRET_OPEN, 4,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("D#4",	 FRET_X, FRET_X, 1, 3, 4, 4,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("D#7",	 FRET_X, FRET_X, 1, 3, 2, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("D#sus",	 FRET_X, FRET_X, 1, 3, 4, 4,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("D#sus4",	 FRET_X, FRET_X, 1, 3, 4, 4,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("D#maj",	 FRET_X, FRET_X, 3, 1, 2, 1,	 3, CHORD_BUILTIN, CHORD_HARD);
        add("D#maj7",	 FRET_X, FRET_X, 1, 3, 3, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("D#dim",	 FRET_X, FRET_X, 1, 2, 1, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("D#m",	 FRET_X, FRET_X, 4, 3, 4, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("D#min",	 FRET_X, FRET_X, 4, 3, 4, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("D#m7",	 FRET_X, FRET_X, 1, 3, 2, 2,	 1, CHORD_BUILTIN, CHORD_HARD);

        add("Eb",	 FRET_X, FRET_X, 3, 1, 2, 1,	 3, CHORD_BUILTIN, CHORD_HARD);
        add("Eb+",	 FRET_X, FRET_X, 1, FRET_OPEN, FRET_OPEN, 4,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Eb4",	 FRET_X, FRET_X, 1, 3, 4, 4,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Eb7",	 FRET_X, FRET_X, 1, 3, 2, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Ebsus",	 FRET_X, FRET_X, 1, 3, 4, 4,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Ebsus4",	 FRET_X, FRET_X, 1, 3, 4, 4,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Ebmaj",	 FRET_X, FRET_X, 1, 3, 3, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Ebmaj7",	 FRET_X, FRET_X, 1, 3, 3, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Ebdim",	 FRET_X, FRET_X, 1, 2, 1, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Ebadd9",	 FRET_X, 1, 1, 3, 4, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Eb(add9)",	 FRET_X, 1, 1, 3, 4, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Ebm",	 FRET_X, FRET_X, 4, 3, 4, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Ebmin",	 FRET_X, FRET_X, 4, 3, 4, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Ebm7",	 FRET_X, FRET_X, 1, 3, 2, 2,	 1, CHORD_BUILTIN, CHORD_HARD);

        add("E",	 FRET_OPEN, 2, 2, 1, FRET_OPEN, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("E+",	 FRET_X, FRET_X, 2, 1, 1, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("E5",	 FRET_OPEN, 1, 3, 3, FRET_X, FRET_X,	 7, CHORD_BUILTIN, CHORD_HARD);
        add("E6",	 FRET_X, FRET_X, 3, 3, 3, 3,	 9, CHORD_BUILTIN, CHORD_HARD);
        add("E7",	 FRET_OPEN, 2, 2, 1, 3, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("E7#9",	 FRET_OPEN, 2, 2, 1, 3, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("E7(#9)",	 FRET_OPEN, 2, 2, 1, 3, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("E7(5b)",	 FRET_X, 1, FRET_OPEN, 1, 3, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("E7b9",	 FRET_OPEN, 2, FRET_OPEN, 1, 3, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("E7(b9)",	 FRET_OPEN, 2, FRET_OPEN, 1, 3, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("E7(11)",	 FRET_OPEN, 2, 2, 2, 3, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("E9",	 1, 3, 1, 2, 1, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("E11",	 1, 1, 1, 1, 2, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Esus",	 FRET_OPEN, 2, 2, 2, FRET_OPEN, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Esus4",	 FRET_OPEN, 2, 2, 2, FRET_OPEN, FRET_OPEN,	 FRET_OPEN, CHORD_BUILTIN, CHORD_HARD);
        add("Emaj",	 FRET_OPEN, 2, 2, 1, FRET_OPEN, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Emaj7",	 FRET_OPEN, 2, 1, 1, FRET_OPEN, FRET_X,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Edim",	 FRET_X, FRET_X, 2, 3, 2, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Em",	 FRET_OPEN, 2, 2, FRET_OPEN, FRET_OPEN, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Emin",	 FRET_OPEN, 2, 2, FRET_OPEN, FRET_OPEN, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);

        add("Em6",	 FRET_OPEN, 2, 2, FRET_OPEN, 2, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Em7",	 FRET_OPEN, 2, 2, FRET_OPEN, 3, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("Em/B",	 FRET_X, 2, 2, FRET_OPEN, FRET_OPEN, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Em/D",	 FRET_X, FRET_X, FRET_OPEN, FRET_OPEN, FRET_OPEN, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Em7/D",	 FRET_X, FRET_X, FRET_OPEN, FRET_OPEN, FRET_OPEN, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Emsus4",	 FRET_OPEN, FRET_OPEN, 2, FRET_OPEN, FRET_OPEN, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Em(sus4)",	 FRET_OPEN, FRET_OPEN, 2, FRET_OPEN, FRET_OPEN, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Emadd9",	 FRET_OPEN, 2, 4, FRET_OPEN, FRET_OPEN, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Em(add9)",	 FRET_OPEN, 2, 4, FRET_OPEN, FRET_OPEN, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);

        add("F",	 1, 3, 3, 2, 1, 1,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("F+",	 FRET_X, FRET_X, 3, 2, 2, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("F+7+11",	 1, 3, 3, 2, FRET_OPEN, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("F4",	 FRET_X, FRET_X, 3, 3, 1, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("F6",	 FRET_X, 3, 3, 2, 3, FRET_X,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("F7",	 1, 3, 1, 2, 1, 1,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("F9",	 2, 4, 2, 3, 2, 4,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("F11",	 1, 3, 1, 3, 1, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Fsus",	 FRET_X, FRET_X, 3, 3, 1, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Fsus4",	 FRET_X, FRET_X, 3, 3, 1, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Fmaj",	 1, 3, 3, 2, 1, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Fmaj7",	 FRET_X, 3, 3, 2, 1, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Fdim",	 FRET_X, FRET_X, FRET_OPEN, 1, FRET_OPEN, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Fm",	 1, 3, 3, 1, 1, 1,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("Fmin",	 1, 3, 3, 1, 1, 1,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("F/A",	 FRET_X, FRET_OPEN, 3, 2, 1, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("F/C",	 FRET_X, FRET_X, 3, 2, 1, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("F/D",	 FRET_X, FRET_X, FRET_OPEN, 2, 1, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("F/G",	 3, 3, 3, 2, 1, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("F7/A",	 FRET_X, FRET_OPEN, 3, FRET_OPEN, 1, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Fmaj7/A",	 FRET_X, FRET_OPEN, 3, 2, 1, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Fmaj7/C",	 FRET_X, 3, 3, 2, 1, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Fmaj7(+5)", FRET_X, FRET_X, 3, 2, 2, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Fadd9",	 3, FRET_OPEN, 3, 2, 1, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("F(add9)",	 3, FRET_OPEN, 3, 2, 1, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("FaddG",	 1, FRET_X, 3, 2, 1, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("FaddG",	 1, FRET_X, 3, 2, 1, 3,	 1, CHORD_BUILTIN, CHORD_HARD);

        add("Fm6",	 FRET_X, FRET_X, FRET_OPEN, 1, 1, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Fm7",	 1, 3, 1, 1, 1, 1,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("Fmmaj7",	 FRET_X, 3, 3, 1, 1, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);

        add("F#",	 2, 4, 4, 3, 2, 2,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("F#+",	 FRET_X, FRET_X, 4, 3, 3, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("F#7",	 FRET_X, FRET_X, 4, 3, 2, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("F#9",	 FRET_X, 1, 2, 1, 2, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("F#11",	 2, 4, 2, 4, 2, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("F#sus",	 FRET_X, FRET_X, 4, 4, 2, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("F#sus4",	 FRET_X, FRET_X, 4, 4, 2, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("F#maj",	 2, 4, 4, 3, 2, 2,	 FRET_OPEN, CHORD_BUILTIN, CHORD_HARD);
        add("F#maj7",	 FRET_X, FRET_X, 4, 3, 2, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("F#dim",	 FRET_X, FRET_X, 1, 2, 1, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("F#m",	 2, 4, 4, 2, 2, 2,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("F#min",	 2, 4, 4, 2, 2, 2,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("F#/E",	 FRET_OPEN, 4, 4, 3, 2, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("F#4",	 FRET_X, FRET_X, 4, 4, 2, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("F#m6",	 FRET_X, FRET_X, 1, 2, 2, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("F#m7",	 FRET_X, FRET_X, 2, 2, 2, 2,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("F#m7-5",	 1, FRET_OPEN, 2, 3, 3, 3,	 2, CHORD_BUILTIN, CHORD_HARD);
        add("F#m/C#m",	 FRET_X, FRET_X, 4, 2, 2, 2,	 1, CHORD_BUILTIN, CHORD_HARD);

        add("Gb",	 2, 4, 4, 3, 2, 2,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("Gb+",	 FRET_X, FRET_X, 4, 3, 3, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Gb7",	 FRET_X, FRET_X, 4, 3, 2, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("Gb9",	 FRET_X, 1, 2, 1, 2, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Gbsus",	 FRET_X, FRET_X, 4, 4, 2, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Gbsus4",	 FRET_X, FRET_X, 4, 4, 2, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Gbmaj",	 2, 4, 4, 3, 2, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Gbmaj7",	 FRET_X, FRET_X, 4, 3, 2, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Gbdim",	 FRET_X, FRET_X, 1, 2, 1, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Gbm",	 2, 4, 4, 2, 2, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Gbmin",	 2, 4, 4, 2, 2, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Gbm7",	 FRET_X, FRET_X, 2, 2, 2, 2,	 1, CHORD_BUILTIN, CHORD_HARD);

        add("G",	 3, 2, FRET_OPEN, FRET_OPEN, FRET_OPEN, 3,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("G+",	 FRET_X, FRET_X, 1, FRET_OPEN, FRET_OPEN, 4,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("G4",	 FRET_X, FRET_X, FRET_OPEN, FRET_OPEN, 1, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("G6",	 3, FRET_X, FRET_OPEN, FRET_OPEN, FRET_OPEN, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("G7",	 3, 2, FRET_OPEN, FRET_OPEN, FRET_OPEN, 1,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("G7+",	 FRET_X, FRET_X, 4, 3, 3, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("G7b9",	 FRET_X, FRET_X, FRET_OPEN, 1, FRET_OPEN, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("G7(b9)",	 FRET_X, FRET_X, FRET_OPEN, 1, FRET_OPEN, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("G7#9",	 1, 3, FRET_X, 2, 4, 4,	 3, CHORD_BUILTIN, CHORD_HARD);
        add("G7(#9)",	 1, 3, FRET_X, 2, 4, 4,	 3, CHORD_BUILTIN, CHORD_HARD);
        add("G9",	 3, FRET_X, FRET_OPEN, 2, FRET_OPEN, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("G9(11)",	 1, 3, 1, 3, 1, 3,	 3, CHORD_BUILTIN, CHORD_HARD);
        add("G11",	 3, FRET_X, FRET_OPEN, 2, 1, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Gsus",	 FRET_X, FRET_X, FRET_OPEN, FRET_OPEN, 1, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Gsus4",	 FRET_X, FRET_X, FRET_OPEN, FRET_OPEN, 1, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("G6sus4",	 FRET_OPEN, 2, FRET_OPEN, FRET_OPEN, 1, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("G6(sus4)",	 FRET_OPEN, 2, FRET_OPEN, FRET_OPEN, 1, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("G7sus4",	 3, 3, FRET_OPEN, FRET_OPEN, 1, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("G7(sus4)",	 3, 3, FRET_OPEN, FRET_OPEN, 1, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Gmaj",	 3, 2, FRET_OPEN, FRET_OPEN, FRET_OPEN, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Gmaj7",	 FRET_X, FRET_X, 4, 3, 2, 1,	 2, CHORD_BUILTIN, CHORD_HARD);
        add("Gmaj7sus4", FRET_X, FRET_X, FRET_OPEN, FRET_OPEN, 1, 2,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Gmaj9",	 1, 1, 4, 1, 2, 1,	 2, CHORD_BUILTIN, CHORD_HARD);
        add("Gm",	 1, 3, 3, 1, 1, 1,	 3, CHORD_BUILTIN, CHORD_EASY);
        add("Gmin",	 1, 3, 3, 1, 1, 1,	 3, CHORD_BUILTIN, CHORD_EASY);
        add("Gdim",	 FRET_X, FRET_X, 2, 3, 2, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Gadd9",	 1, 3, FRET_X, 2, 1, 3,	 3, CHORD_BUILTIN, CHORD_HARD);
        add("G(add9)",	 1, 3, FRET_X, 2, 1, 3,	 3, CHORD_BUILTIN, CHORD_HARD);
        add("G/A",	 FRET_X, FRET_OPEN, FRET_OPEN, FRET_OPEN, FRET_OPEN, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("G/B",	 FRET_X, 2, FRET_OPEN, FRET_OPEN, FRET_OPEN, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("G/D",	 FRET_X, 2, 2, 1, FRET_OPEN, FRET_OPEN,	 4, CHORD_BUILTIN, CHORD_HARD);
        add("G/F#",	 2, 2, FRET_OPEN, FRET_OPEN, FRET_OPEN, 3,	 1, CHORD_BUILTIN, CHORD_HARD);

        add("Gm6",	 FRET_X, FRET_X, 2, 3, 3, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("Gm7",	 1, 3, 1, 1, 1, 1,	 3, CHORD_BUILTIN, CHORD_EASY);
        add("Gm/Bb",	 3, 2, 2, 1, FRET_X, FRET_X,	 4, CHORD_BUILTIN, CHORD_HARD);

        add("G#",	 1, 3, 3, 2, 1, 1,	 4, CHORD_BUILTIN, CHORD_EASY);
        add("G#+",	 FRET_X, FRET_X, 2, 1, 1, FRET_OPEN,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("G#4",	 1, 3, 3, 1, 1, 1,	 4, CHORD_BUILTIN, CHORD_HARD);
        add("G#7",	 FRET_X, FRET_X, 1, 1, 1, 2,	 1, CHORD_BUILTIN, CHORD_EASY);
        add("G#sus",	 FRET_X, FRET_X, 1, 1, 2, 4,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("G#sus4",	 FRET_X, FRET_X, 1, 1, 2, 4,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("G#maj",	 1, 3, 3, 2, 1, 1,	 4, CHORD_BUILTIN, CHORD_HARD);
        add("G#maj7",	 FRET_X, FRET_X, 1, 1, 1, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("G#dim",	 FRET_X, FRET_X, FRET_OPEN, 1, FRET_OPEN, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("G#m",	 1, 3, 3, 1, 1, 1,	 4, CHORD_BUILTIN, CHORD_HARD);
        add("G#min",	 1, 3, 3, 1, 1, 1,	 4, CHORD_BUILTIN, CHORD_HARD);
        add("G#m6",	 FRET_X, FRET_X, 1, 1, FRET_OPEN, 1,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("G#m7",	 FRET_X, FRET_X, 1, 1, 1, 1,	 4, CHORD_BUILTIN, CHORD_EASY);
        add("G#m9maj7",	 FRET_X, FRET_X, 1, 3, FRET_OPEN, 3,	 1, CHORD_BUILTIN, CHORD_HARD);
        add("G#m9(maj7)",FRET_X, FRET_X, 1, 3, FRET_OPEN, 3,	 1, CHORD_BUILTIN, CHORD_HARD);

    }
}
