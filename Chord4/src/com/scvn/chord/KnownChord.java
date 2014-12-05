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

import javax.swing.*;
import java.awt.*;

public class KnownChord extends JComponent implements ChordConstants {

    static int gridSize; // should be a multiple of (nb_strings - 1) and of
    static int nb_frets = 5;    // (nb_frets - 1)
    static int nb_strings = 6;
    static int f_space, s_space;

    static int dot_size;
    static int dot_rad;

    private String chordName;
    int baseFret;
    int frets[];

    int origin;
    int difficult;
    String rootNote;
    private boolean drawName = true;

    static {
        init(40);
    }

    /* --------------------------------------------------------------------------------*/
    public KnownChord(String chordName, int s0, int s1, int s2, int s3, int s4, int s5, int baseFret, int origin, int difficult) {
        this(chordName, new int[]{s0, s1, s2, s3, s4, s5}, baseFret, origin, difficult);
    }

    /* --------------------------------------------------------------------------------*/
    public KnownChord(String chordName, int pos[], int baseFret, int origin, int difficult) {
        super();

        setSize(gridSize, gridSize);
        setPreferredSize(new Dimension(gridSize, gridSize));
        this.frets = new int[pos.length];
        this.chordName = chordName;
        this.baseFret = baseFret;
        for (int i = 0; i < pos.length; i++) {
            frets[i] = pos[i];
        }
        this.origin = origin;
        this.difficult = difficult;
    }

    public static int getGridSize() {
        return gridSize;
    }

    public int getOrigin() {
        return origin;
    }


    /*------------------------------------------------------------*/
    public String toString() {
        return chordName + " (" + getDefine() + ")";
    }

/*------------------------------------------------------------*/
    public String getDefine() {
        int j;
        StringBuffer sb = new StringBuffer();

        /* in chord format... */
        // convert from 0-based to 1-based so that only open
        // strings are denoted as 0
        sb.append("{define: " + chordName + " base-fret " + baseFret + " frets ");
        for (j = 0; j < frets.length; j++) {
            if (frets[j] == FRET_OPEN) {
                sb.append("0");
            } else if (frets[j] == FRET_X) {
                sb.append("x");
            } else {
                sb.append((frets[j]));
            }
            if (j < frets.length - 1) {
                sb.append(" ");
            }
        }
        sb.append("}");
//        if (nbfing+inbarre-1 > maxfing )
//        {
//            System.out.print (" (barre required)");
//        }
        return sb.toString();
    }

    /*--------------------------------------------------------------------------------*/
    int compareTo(KnownChord kc2) {

        // A.compareTo(A#) should return -1
        // A.compareTo(Ab) should return +1
        char c1, c2;
        int i = 0;
        String chord2 = kc2.getChordName();

        // if all equals, return 0
        // respect definition priorities
        if (chordName.equals(chord2)) {
            if (kc2.getOrigin() > getOrigin()) {
                return 1;
            } else if (kc2.getOrigin() < getOrigin()) {
                return -1;
            }

            return (0);
        }

        // if compare of root note == 0, return compare of remainder
        String rn1 = computeRootNote(chordName);
        String rn2 = computeRootNote(chord2);
        if (rn1.equals(rn2)) {
            return chordName.compareTo(chord2);
        }

        // compare root notes
        while (true) {

            c1 = i >= rn1.length() ? ' ' : rn1.charAt(i);
            c2 = i >= rn2.length() ? ' ' : rn2.charAt(i);

//System.out.println("i, c1, c2 :[" + i + "," + c1 + "," + c2 + "]");

/* cases
 *    c2  b      -        #
 *  c1 b  skip   def      def
 *     -  ret1b  skip     def
 *     #  ret1a  ret1a   skip
 */

// skip
            if (c1 == c2) {
                i++;
                continue;
            }

            if (Character.isLetter(c1) && Character.isLetter(c2)) {
                return c1 < c2 ? -1 : 1;
            }
// ret 1a
            if (c1 == '#') { // since c1 != c2, if c1 == #, it is the largest
                return (1);
            }
// ret 1b
            if (c2 == 'b') {
                return (1);
            }
// def
            return -1;
        }
    }

    public int getBaseFret() {
        return baseFret;
    }
    /* --------------------------------------------------------------------------------*/

    /* --------------------------------------------------------------------------------*/
    public int[] getFrets() {
        return frets;
    }

    /* --------------------------------------------------------------------------------*/
    static public void init(int gs) {
    	Logger.debug("KnownChord.init gs=" + gs);
        gridSize = gs;
        f_space = gridSize / (nb_frets - 1);
        s_space = gridSize / (nb_strings - 1);
        dot_rad = (s_space - 1) / 2;
        Logger.debug("KnownChord.init done");
    }

    public void paint(Graphics g) {
        paint((Graphics2D) g, 0, 0);
    }

    /*--------------------------------------------------------------------------------*/
    public void paint(Graphics2D cgc, int hpos, int vpos) {

//        System.out.println(toString());
        int fret, str;
        String cn;
        setForeground(Color.black);

        for (fret = 0; fret < nb_frets; fret++) {
            cgc.drawLine(hpos, vpos + f_space * fret, hpos + s_space * (nb_strings - 1), vpos + f_space * fret);
        }
        for (str = 0; str < nb_strings; str++) {
            cgc.drawLine(hpos + s_space * str, vpos, hpos + s_space * str, vpos + f_space * (nb_frets - 1));

            switch (frets[str]) {
                case FRET_X:
                    cgc.drawLine(hpos + s_space * str - dot_rad, vpos - dot_rad - dot_rad, hpos + s_space * str + dot_rad, vpos);
                    cgc.drawLine(hpos + s_space * str - dot_rad, vpos, hpos + s_space * str + dot_rad, vpos - dot_rad - dot_rad);
                    break;
                case FRET_OPEN:
                    cgc.drawOval(hpos + s_space * str - dot_rad, vpos - dot_rad - dot_rad, dot_rad + dot_rad, dot_rad + dot_rad);
                    break;
                default:
                    cgc.fillOval(hpos + s_space * str - dot_rad, vpos - dot_rad - dot_rad + f_space * frets[str], dot_rad + dot_rad, dot_rad + dot_rad);

            }

        }

        if (baseFret > 1) {
            cgc.drawString(Integer.toString(baseFret), hpos + gridSize + dot_rad / 2 + 2, vpos + f_space);
        }


        if (drawName) {
            cn = chordName;
            if (origin == CHORD_DEFINED) {
                cn += "**";
            } else if (origin == CHORD_IN_CHORDRC) {
                cn += "*";
            }
            cgc.drawString(cn, hpos + (gridSize - cgc.getFontMetrics().stringWidth(cn)) / 2, vpos - 10);
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof KnownChord)) {
            return false;
        }
        KnownChord kc = (KnownChord) obj;
        // compare set of absolute frets and names
        //System.out.println(chordName + ".equals()");
        if (!kc.getChordName().equals(getChordName())) {
            return false;
        }
        int kcfrets[] = kc.getFrets();
        int kcbase = kc.getBaseFret();

        int i = 0;
        for (i = 0; i < kcfrets.length; i++) {
            if (kcfrets[i] == FRET_OPEN && frets[i] == FRET_OPEN) {
                continue;
            }
            if (kcfrets[i] == FRET_X && frets[i] == FRET_X) {
                continue;
            }
            if (kcbase + kcfrets[i] != baseFret + frets[i]) {
                return false;
            }
        }

        return i == kcfrets.length;
    }

    public String getChordName() {
        return chordName;
    }

    public int getDifficulty() {
        return difficult;
    }

    public String getRootNote() {
        return rootNote;
    }

    public void setDrawName(boolean drawName) {
        this.drawName = drawName;
    }

    static String computeRootNote(String name) {
        int len = 1;
        while (len < name.length() && (name.charAt(len) == '#' || name.charAt(len) == 'b')) {
            len++;
        }
        return name.substring(0, len);
    }

    public static void main(String args[]) {
/*
        System.out.println("computeRootNote A:["+computeRootNote("A")+"]");
        System.out.println("computeRootNote Ab:["+computeRootNote("Ab")+"]");
        System.out.println("computeRootNote A7:["+computeRootNote("A7")+"]");
        System.out.println("computeRootNote A#:["+computeRootNote("A#")+"]");
 */
//        KnownChord kc1;

//        kc1 = new KnownChord("A", 1, 1, 1, 1, 1, 1, 1, 1, 1);
//        System.out.println("A.compareTo A (0):" + kc1.compareTo("A"));
//        System.out.println("A.compareTo Ab (1):" + kc1.compareTo("Ab"));
//        System.out.println("A.compareTo A# (-1):" + kc1.compareTo("A#"));
//        System.out.println("A.compareTo A7 (-1):" + kc1.compareTo("A7"));
//
//        kc1 = new KnownChord("A#", 1, 1, 1, 1, 1, 1, 1, 1, 1);
//        System.out.println("A#.compareTo A (1):" + kc1.compareTo("A"));
//        System.out.println("A#.compareTo Ab (1):" + kc1.compareTo("Ab"));
//        System.out.println("A#.compareTo A# (0):" + kc1.compareTo("A#"));
//        System.out.println("A#.compareTo A7 (1):" + kc1.compareTo("A7"));
//
//        kc1 = new KnownChord("Ab", 1, 1, 1, 1, 1, 1, 1, 1, 1);
//        System.out.println("Ab.compareTo A (-1):" + kc1.compareTo("A"));
//        System.out.println("Ab.compareTo Ab (0):" + kc1.compareTo("Ab"));
//        System.out.println("Ab.compareTo A# (-1):" + kc1.compareTo("A#"));
//        System.out.println("Ab.compareTo A7 (-1):" + kc1.compareTo("A7"));
//
//        kc1 = new KnownChord("A7", 1, 1, 1, 1, 1, 1, 1, 1, 1);
//        System.out.println("A7.compareTo A (1):" + kc1.compareTo("A"));
//        System.out.println("A7.compareTo Ab (1):" + kc1.compareTo("Ab"));
//        System.out.println("A7.compareTo A# (-1):" + kc1.compareTo("A#"));
//        System.out.println("A7.compareTo A7 (0):" + kc1.compareTo("A7"));
    }

}
