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

public class Music {

    private static Settings settings;
    public static String notes[] = {"C", "D", "E", "F", "G", "A", "B"};
    static int amount;

    public Music() {
    }
/* --------------------------------------------------------------------------------*/
    public static void main(String args[]) {
        Logger.debug = true;
        setAmount(+7);
        System.out.println("C/G +7 = G/D");
        System.out.println(doTranspose("C/G"));
        setAmount(+2);
        System.out.println("G/B +2 = A/C#");
        System.out.println(doTranspose("G/B"));
//        System.out.println(doTranspose("A#"));
//        System.out.println(doTranspose("A+"));
//        System.out.println(doTranspose("A7"));
    }
/* --------------------------------------------------------------------------------*/
    static String doTranspose(String chord) {

/**
 * attempting parsing
 * 1) look for a known note name at start of name
 * 2) copy literally until delimiter found
 * 3) repeat
 */
        if (amount == 0) {
            return chord;
        }

        Logger.debug("Transposing " + chord + " by " + amount);

        int i,j;
        int p_chord;
        int new_i;
        boolean sharp = false;
        boolean flat = false;
        String new_chord = "";

        p_chord = 0;

        while (true) {
            /* Find basic note */
            i = 0;
            while ((i < 7) && (!chord.substring(p_chord).startsWith(notes[i]))) {
                i++;
            }

            if (i >= 7) {
                return (null);
            }
            Logger.debug("Root found:" + notes[i]);
            /* Look for sharp or flat */
            p_chord += notes[i].length();
            if (p_chord < chord.length()) {
                flat = (chord.charAt(p_chord) == 'b');
                sharp = (chord.charAt(p_chord) == '#');
                if (sharp || flat) p_chord++;
            }
            /*compute new chord name */
            new_i = i;
            /* moving upscale ... */
            if (amount > 0)
                for (j = 0; j < amount; j++)
                    if (sharp) {
                        sharp = false;
                        new_i = (new_i + 1) % 7;
                    } else if (flat)
                        flat = false;
                    else if ((new_i == 2) || (new_i == 6))
                        new_i = (new_i + 1) % 7;
                    else
                        sharp = true;
            /* moving downscale ... */
            else
                for (j = 0; j > amount; j--)
                    if (flat) {
                        flat = false;
                        new_i = (new_i + 6) % 7;
                    } else if (sharp)
                        sharp = false;
                    else if ((new_i == 3) || (new_i == 0))
                        new_i = (new_i + 6) % 7;
                    else
                        flat = true;

            new_chord += notes[new_i];

            if (sharp) new_chord += "#";
            if (flat) new_chord += "b";

            while ((p_chord < chord.length()) && (chord.charAt(p_chord) != '/'))
                new_chord += chord.charAt(p_chord++);

            if (p_chord == chord.length()) {
                chord = new_chord;
                return (chord);
            } else {
                p_chord++;
                new_chord += '/';
            }
        }
    }
/* --------------------------------------------------------------------------------*/
    public static void init() {
        setAmount(settings.getTransposition());
    }
/* --------------------------------------------------------------------------------*/
    public static void setAmount(int i) {
        amount = i;
    }
/* --------------------------------------------------------------------------------*/
    public static void setSettings(Settings theSettings) {
        settings = theSettings;
        init();
    }
}
