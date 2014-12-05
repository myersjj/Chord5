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

import java.util.*;



public class ChordVector extends Vector  implements ChordConstants {

    /*--------------------------------------------------------------------------------*/
    public ChordVector() {
        super(100, 50);
    }
    /*--------------------------------------------------------------------------------*/
    
    public KnownChord add(KnownChord kc) {
        
        int n, i;
        
        for (i=0; i<size(); i++) {
            n = getChord(i).compareTo(kc);
            if (n == 0) {
                return kc;
            }
            if (n > 0) {
                break;
            }
        }
        insertElementAt(kc, i);
        return (kc);
    }
    /*--------------------------------------------------------------------------------*/
    
    public KnownChord add(String chord_name, int s1, int s2, int s3, int s4, int s5, int s6,
    int displ, int origin, int difficult) {
        
        KnownChord kc;
        kc=add(new KnownChord(chord_name, s1, s2, s3, s4, s5, s6,
        displ, origin, difficult));
        return (kc);
    }
    /*--------------------------------------------------------------------------------*/
    public KnownChord getChord(int i) {
        return((KnownChord)(elementAt(i)));
    }
    /*--------------------------------------------------------------------------------*/
    public KnownChord getChord(String chordName) {
        KnownChord kc = null;
        
        for (int i=0; i<size(); i++)  {
            kc=getChord(i);
            if (kc.getChordName().equals(chordName)) {
                return (kc);
            }
        }
        return(null);
    }
}
