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

import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Logger;

public class ParsingErrorSet implements ChordConstants {

//    TreeSet undefChords = new TreeSet();
    private static Vector syntaxErrors = new Vector();
    private static Vector undefErrors = new Vector();
    private static final Logger LOGGER =
	        Logger.getLogger(Chord4.class.getName());


    public static void init() {
        syntaxErrors.clear();
        undefErrors.clear();
    }


/* --------------------------------------------------------------------------------*/
    public static void addSyntaxError(int errType, String errMsg, Producer p) {
    	LOGGER.fine("syntax error: " + errType + " msg=" + errMsg + " p=" + p);
        switch (errType) {
            case ERR_UNDEF_CHORD:
                undefErrors.add(new ParsingError(errType, errMsg, p.getFileName(), p.getLineNumber()));
                break;
            default:
                syntaxErrors.add(new ParsingError(errType, errMsg, p.getFileName(), p.getLineNumber()));
                break;
        }
    }

    public static ParsingError[] getChordUndefErrors() {
        ParsingError[] ce = new ParsingError[undefErrors.size()];
        undefErrors.copyInto(ce);
        return ce;
    }

    public static ParsingError[] getSyntaxErrors() {
        ParsingError[] ce = new ParsingError[syntaxErrors.size()];
        syntaxErrors.copyInto(ce);
        return ce;
    }

    public static String getSyntaxMessages() {
        ParsingError[] ces = new ParsingError[syntaxErrors.size()];
        syntaxErrors.copyInto(ces);
        StringBuffer sb = new StringBuffer();
        ParsingError ce;
        for (int i = 0; i < ces.length; i++) {
            ce = ces[i];
            sb.append("File '" + ce.getFileName() + "' line " + ce.getLineNumber() + " " + ERR_MSGS[ce.getErrType()]);
            if (ce.getErrDetail() != null) {
                sb.append(": " + ce.getErrDetail());
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String getNUndefMessages(int num) {
        ParsingError[] ces = new ParsingError[undefErrors.size()];
        undefErrors.copyInto(ces);
        StringBuffer sb = new StringBuffer();
        ParsingError ce;
        for (int i = 0; i < Math.min(ces.length, num); i++) {
            ce = ces[i];
            sb.append("File '" + ce.getFileName() + "' line " + ce.getLineNumber() + ": " + ce.getErrDetail() + "\n");
        }
        if (ces.length > num) {
            sb.append("... and " + (ces.length - num) + " others.");
        }
        return sb.toString();
    }

    public static Vector getUndefList() {
        ParsingError[] ces = new ParsingError[undefErrors.size()];
        undefErrors.copyInto(ces);
        Vector v;
        TreeSet ts = new TreeSet();
        ParsingError ce;
        for (int i = 0; i < ces.length; i++) {
            ce = ces[i];
            if (!ts.contains(ce.getErrDetail())) {
                ts.add(ce.getErrDetail());
            }
        }
        // now put the sorted list in the vector
        v = new Vector(ts);
        return v;
    }
}
