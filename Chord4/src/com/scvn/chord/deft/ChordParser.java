package com.scvn.chord.deft;

import java.util.*;

public class ChordParser {

    String types[] = {"UNDEF", "MAJOR", "MINOR", "SUS", "AUG", "DIM", "DOM"};

    static final int UNDEF = 0;
    static final int MAJOR = 1;
    static final int MINOR = 2;
    static final int SUS = 3;
    static final int AUG = 4;
    static final int DIM = 5;
    static final int DOM = 6;

    static final int NB_HALF_TONES = 12;


    int type;
    int idx;
    int afterQualifier;			/* num value after qualifier */
    int afterRoot;			/* num value after root */

    int rootHalfTone;
    String rootName;
    Note rootNote;
    int baseHalfToneAbs = -1;
    String baseName;
    Note baseNote;
    String chord_name;
    int clen;
    Vector requiredNotes, requiredHT;
    StringBuffer work;

    Scale scale;

    public ChordParser(String s) throws MalformedChordException {
        work = new StringBuffer("Analyzing \"" + s + "\"\n");
        chord_name = s;
        clen = s.length();
        requiredNotes = new Vector();
        requiredHT = new Vector();
        idx = 0;
//        Note.setScale(null);
        parse(chord_name);
        work.append(scale.toString());
        printRequiredNotes();
        printRequiredHTAbs();
        printRequiredHTRel();
    }

    public int roleToHT(int role) {
        switch (role) {
            case 0:
                return Scale.ROOT;
            case 2:
                return Scale.MAJOR_SECOND;
            case 4:
                return Scale.FOURTH;
            case 5:
                return Scale.EVEN_FIFTH;
            case 6:
                return Scale.SIXTH;
            case 7:
                return Scale.DOM_SEVENTH;
            case 9:
                return Scale.NINTH;
            case 11:
                return Scale.ELEVENTH;
            default :
                work.append("trouble in roleToHT: role=" + role + "\n");
                return -1;
        }

    }
/*------------------------------------------------------------*/
    void doAdd(int role) {
                has(roleToHT(role));
    }
/*------------------------------------------------------------*/
    void doAug(int num1, int num2) {
        has(Scale.ROOT);
        has(Scale.MAJOR_THIRD);
        has(Scale.AUG_FIFTH);
        if (num2 != 0) {
            work.append("Unexpected numeric value.\n");
            return;
        }

        switch (num1) {
            case 0:
                break;
            case 5:
                has_no(Scale.MAJOR_THIRD);
                break;
            case 6:
                has(Scale.SIXTH);
                break;
            case 7:
                has_no(Scale.SEVENTH);
                has(Scale.DOM_SEVENTH);
                break;
            case 9:
                has_no(Scale.SEVENTH);
                has(Scale.DOM_SEVENTH);
                has(Scale.NINTH);
                break;
            case 11:
                has_no(Scale.SEVENTH);
                has(Scale.DOM_SEVENTH);
                has(Scale.NINTH);
                has(Scale.ELEVENTH);
                break;
            case 13:
                has_no(Scale.SEVENTH);
                has(Scale.DOM_SEVENTH);
                has(Scale.NINTH);
                has(Scale.ELEVENTH);
                has(Scale.THIRTEENTH);
                break;
            default :
                work.append("trouble with afterQualifier in do_aug\n");
        }
    }
/*------------------------------------------------------------*/
    void doDim(int num1, int num2) {
        has(Scale.ROOT);
        switch (num1) {
            case 0:
                break;
            case 5:
                has_no(Scale.MAJOR_THIRD);
                break;
            case 6:
                has(Scale.SIXTH);
                break;
            case 7:
                has_no(Scale.SEVENTH);
                has(Scale.DOM_SEVENTH);
                break;
            case 9:
                has_no(Scale.SEVENTH);
                has(Scale.DOM_SEVENTH);
                has(Scale.NINTH);
                break;
            case 11:
                has_no(Scale.SEVENTH);
                has(Scale.DOM_SEVENTH);
                has(Scale.NINTH);
                has(Scale.ELEVENTH);
                break;
            case 13:
                has_no(Scale.SEVENTH);
                has(Scale.DOM_SEVENTH);
                has(Scale.NINTH);
                has(Scale.ELEVENTH);
                has(Scale.THIRTEENTH);
                break;
            default :
                work.append("trouble with afterQualifier in do_dim\n");
        }

        switch (num2) {
            case 0:
                has(Scale.MINOR_THIRD);
                has(Scale.DIM_FIFTH);
                has(Scale.DIM_SEVENTH);
                break;
            case 7:
                has(Scale.MINOR_THIRD);
                has(Scale.DIM_FIFTH);
                has(Scale.SIXTH);
                break;
            case 9:
                has(Scale.MAJOR_THIRD);
                has(Scale.EVEN_FIFTH);
                has(Scale.DOM_SEVENTH);
                has(Scale.DIM_NINTH);
                break;
            default :
                work.append("trouble in do_dim\n");
        }
    }
/*------------------------------------------------------------*/
    void doFlat(int num) {
        work.append("Flatting the " + num + "th.\n");
        switch (num) {
            case 0:
                work.append("Expected numeric value.\n");
                break;
            case 2:
                has_no(Scale.MAJOR_SECOND);
                has(Scale.MAJOR_SECOND - 1);
                break;
            case 5:
                has_no(Scale.EVEN_FIFTH);
                has(Scale.EVEN_FIFTH - 1);
                break;
            case 7:
                has_no(Scale.SEVENTH);
                has(Scale.SEVENTH - 1);
                break;
            case 9:
                has_no(Scale.NINTH);
                has(Scale.NINTH - 1);
                break;
            default :
                work.append("trouble in do_flat: num=" + num + "\n");
        }
    }
/*------------------------------------------------------------*/
    void doMajor(int num1, int num2) {
        has(Scale.ROOT);
        has(Scale.MAJOR_THIRD);
        has(Scale.EVEN_FIFTH);

        switch (num1) {
            case 0:
                break;
            case 4:
                has_no(Scale.MAJOR_THIRD);
                has(Scale.FOURTH);
                break;
            case 5:
                has_no(Scale.MAJOR_THIRD);
                break;
            case 6:
                has(Scale.SIXTH);
                break;
            case 7:
//                has_no(Scale.SEVENTH);
                has(Scale.DOM_SEVENTH);
                break;
            case 9:
//                has_no(Scale.SEVENTH);
                has(Scale.DOM_SEVENTH);
                has(Scale.NINTH);
                break;
            case 11:
//                has_no(Scale.SEVENTH);
                has(Scale.DOM_SEVENTH); /* has(NINTH);*/
                has(Scale.ELEVENTH);
                break;
            case 13:
//                has_no(Scale.SEVENTH);
                has(Scale.DOM_SEVENTH);
                has(Scale.NINTH);
                has(Scale.ELEVENTH);
                has(Scale.THIRTEENTH);
                break;
            default :
                work.append("trouble with afterQualifier in do_dom\n");
        }

        switch (num2) {
            case 0:
                break;
            case 5:
                has_no(Scale.MAJOR_THIRD);
                break;
            case 6:
                has(Scale.SIXTH);
                break;
            case 7:
                has(Scale.SEVENTH);
                break;
            case 9:
                has(Scale.NINTH);
                break;
            default :
                work.append("trouble with afterRoot in do_major\n");
        }
    }
/*------------------------------------------------------------*/
    void doMinor(int num1, int num2) {
        int num;

        has(Scale.ROOT);
        has(Scale.MINOR_THIRD);
        has(Scale.EVEN_FIFTH);
        if (num1 != 0 && num2 != 0) {
            work.append("Both afterQualifier and afterRoot != 0\n");
            return;
        }

        num = num1 + num2;
        switch (num) {
            case 0:
                break;
            case 6:
                has(Scale.SIXTH);
                break;
            case 7:
                has(Scale.DOM_SEVENTH);
                break;
            case 9:
                has(Scale.DOM_SEVENTH);
                has(Scale.NINTH);
                break;
            default :
                work.append("trouble in do_minor\n");
        }
    }
/*------------------------------------------------------------*/
    void doSharp(int num) {
        work.append("Sharping the " + num + "th\n");
        switch (num) {
            case 0:
                work.append("Expected numeric value.\n");
                break;
            case 2:
                has_no(Scale.MAJOR_SECOND);
                has(Scale.MAJOR_SECOND + 1);
                break;
            case 5:
                has_no(Scale.EVEN_FIFTH);
                has(Scale.EVEN_FIFTH + 1);
                break;
            case 7:
                has_no(Scale.SEVENTH);
                has(Scale.SEVENTH + 1);
                break;
            case 9:
                has_no(Scale.NINTH);
                has(Scale.NINTH + 1);
                break;
            default :
                work.append("trouble in do_sharp: num=" + num + "\n");
        }
    }
/*------------------------------------------------------------*/
    void doSus(int num1, int num2) {
        has(Scale.ROOT);
        has(Scale.FOURTH);
        has(Scale.EVEN_FIFTH);
        switch (num1) {
            case 0:
                break;
            case 5:
                has_no(Scale.MAJOR_THIRD);
                break;
            case 6:
                has(Scale.SIXTH);
                break;
            case 7:
                has_no(Scale.SEVENTH);
                has(Scale.DOM_SEVENTH);
                break;
            case 9:
                has_no(Scale.SEVENTH);
                has(Scale.DOM_SEVENTH);
                has(Scale.NINTH);
                break;
            case 11:
                has_no(Scale.SEVENTH);
                has(Scale.DOM_SEVENTH);
                has(Scale.NINTH);
                has(Scale.ELEVENTH);
                break;
            case 13:
                has_no(Scale.SEVENTH);
                has(Scale.DOM_SEVENTH);
                has(Scale.NINTH);
                has(Scale.ELEVENTH);
                has(Scale.THIRTEENTH);
                break;
            default :
                work.append("trouble with afterQualifier in do_dom\n");
        }

        switch (num2) {
            case 0:
                break;
            case 4:
                break;
            case 2:
                has_no(Scale.FOURTH);
                has(Scale.MAJOR_SECOND);
                break;
            case 9:
                has_no(Scale.FOURTH);
                has(Scale.NINTH);
                break;
            default :
                work.append("trouble in do_sus\n");
        }
    }

    /*------------------------------------------------------------*/
    int findAbsoluteHT(String noteName) throws MalformedChordException {
        int ht = 0;

        switch (noteName.charAt(0)) {
            case 'C':
                ht = 0;
                break;
            case 'D':
                ht = 2;
                break;
            case 'E':
                ht = 4;
                break;
            case 'F':
                ht = 5;
                break;
            case 'G':
                ht = 7;
                break;
            case 'A':
                ht = 9;
                break;
            case 'B':
                ht = 11;
                break;
        }

        if (noteName.length() == 2) {
            if (noteName.charAt(1) == 'b')
                ht--;
            else
                ht++;
        }

        if (ht < 0) {
            ht += NB_HALF_TONES;
        }
        return ht;
    }
/*------------------------------------------------------------*/
    public int getRootHalfTone() {
        return rootHalfTone;
    }
/*------------------------------------------------------------*/

    public int getBaseHalfToneAbs() {
        return baseHalfToneAbs;
    }
/*------------------------------------------------------------*/


    /**
     * 1/ locates a note name in the string
     * 2/ returns a note where halftone is a value relative to C
     */
    String findNoteName(String chord_name) throws MalformedChordException {
        String name;

        if (idx >= clen || (chord_name.charAt(idx) < 'A') || (chord_name.charAt(idx) > 'G')) {
            throw new MalformedChordException("Unknown note.");
        }

        name = new String() + chord_name.charAt(idx);

        idx++;
        if (idx < clen && (chord_name.charAt(idx) == 'b' || chord_name.charAt(idx) == '#')) {
            name += chord_name.charAt(idx);
            idx++;
        }
        return name;
    }
/*------------------------------------------------------------*/

    public int[] getRequiredHalftonesAbs() {
        Integer ht;
        int reqhts[] = new int[requiredHT.size()];
        for (int i = 0; i < reqhts.length; i++) {
            ht = (Integer) requiredHT.elementAt(i);
            reqhts[i] = ht.intValue();
        }
        return reqhts;
    }

    public int[] getRequiredHalftonesRel() {
        Integer ht;
        int reqhts[] = new int[requiredHT.size()];
        for (int i = 0; i < reqhts.length; i++) {
            ht = (Integer) requiredHT.elementAt(i);
            reqhts[i] = toRole(ht.intValue());
        }
        return reqhts;
    }
/*------------------------------------------------------------*/
    public String getWork() {
        return work.toString();
    }
/*------------------------------------------------------------*/
    public int toRole(int absHT) {
        return (absHT - getRootHalfTone() + 12) % 12;
    }
/*------------------------------------------------------------*/
    void has(int role) {
        has(scale.getNoteByHT(role), true);
    }
/*------------------------------------------------------------*/
    void has(Note n, boolean mapit) {
        if (!requiredNotes.contains(n)) {
            work.append("Adding ");
            if (mapit) {
                work.append("the " + Scale.getRole(toRole(n.getHalftone())) + ": ");
            }
            work.append(n.toString() + "\n");
            requiredNotes.addElement(n);
        } else {
            work.append(n.toString() + " already in !\n");
        }
    }
/*------------------------------------------------------------*/
    void has_no(int ht) {
        has_no(scale.getNoteByHT(ht), true);
    }
/*------------------------------------------------------------*/
    void has_no(Note n, boolean mapit) {
        work.append("Removing ");
        if (mapit) {
            work.append("the " + Scale.getRole(toRole(n.getHalftone())) + ": ");
        }
        work.append(n.getName() + "\n");
        requiredNotes.removeElement(n);
    }
/*------------------------------------------------------------*/
    int look_for_num() {
        int tmp_num;

        if (idx >= clen || !Character.isDigit(chord_name.charAt(idx))) {
            return (0);
        }

        tmp_num = chord_name.charAt(idx++) - '0';

        if (tmp_num == 1 && idx < clen) {
            if (Character.isDigit(chord_name.charAt(idx))) {
                tmp_num = 10 + chord_name.charAt(idx++) - '0';
            }
        }

        return (tmp_num);
    }
/*------------------------------------------------------------*/
    void parse(String chord_name) throws MalformedChordException {

        rootName = findNoteName(chord_name);
        rootHalfTone = findAbsoluteHT(rootName);

        Note n = new Note(rootHalfTone, rootName);
        scale = new Scale(n);
//        Note.setScale(scale);

        afterQualifier = 0;
        afterRoot = 0;

        afterQualifier = look_for_num();

        /* these affect basic triad content */
        type = UNDEF;

//  work.append("["+chordName.substring(idx, idx+2)+"]\n";
        if (idx == clen) {
            type = MAJOR;
        } else if ((idx + 2) < clen && chord_name.substring(idx, idx + 3).equalsIgnoreCase("maj")) {
            type = MAJOR;
            idx += 3;
        } else if ((idx + 2) < clen && chord_name.substring(idx, idx + 3).equalsIgnoreCase("min")) {
            type = MINOR;
            idx += 3;
        } else if ((idx + 2) < clen && chord_name.substring(idx, idx + 3).equalsIgnoreCase("sus")) {
            type = SUS;
            idx += 3;
        } else if ((idx + 2) < clen && chord_name.substring(idx, idx + 3).equalsIgnoreCase("aug")) {
            type = AUG;
            idx += 3;
        } else if ((idx + 2) < clen && chord_name.substring(idx, idx + 3).equalsIgnoreCase("dim")) {
            type = DIM;
            idx += 3;
        } else if (chord_name.charAt(idx) == '+' && (idx == clen - 1 || (idx + 1 < clen && !Character.isDigit(chord_name.charAt(idx + 1))))) {
            type = AUG;
            idx += 1;
        } else if (chord_name.charAt(idx) == '-' && (idx == clen - 1 || (idx + 1 < clen && !Character.isDigit(chord_name.charAt(idx + 1))))) {
            type = MINOR;
            idx += 1;
        } else if (chord_name.charAt(idx) == 'M') {
            type = MAJOR;
            idx += 1;
        } else if (chord_name.charAt(idx) == 'm') {
            type = MINOR;
            idx += 1;
        } else {
            type = MAJOR;
        }

        if (type == UNDEF) {
            throw new MalformedChordException("Undefined type");
        }

        /* scan for numeric after a type is set */
        afterRoot = look_for_num();

        work.append("Chord is " + rootName + " " + types[type] + " (afterQualifier=" + afterQualifier + ", afterRoot=" + afterRoot + ").\n");

        switch (type) {
            case MAJOR:
                doMajor(afterQualifier, afterRoot);
                break;
            case MINOR:
                doMinor(afterQualifier, afterRoot);
                break;
            case SUS:
                doSus(afterQualifier, afterRoot);
                break;
            case AUG:
                doAug(afterQualifier, afterRoot);
                break;
            case DIM:
                doDim(afterQualifier, afterRoot);
                break;
        }

        /* look for additionnal info */
        while (idx < clen && chord_name.charAt(idx) != '/') {
            if ((idx + 2) < clen && chord_name.substring(idx, idx + 3).equalsIgnoreCase("add")) {
                idx += 3;
                doAdd(look_for_num());
            } else if ((idx + 2) < clen && chord_name.substring(idx, idx + 3).equalsIgnoreCase("maj")) {
                idx += 3;
                doAdd(look_for_num());
            } else if (chord_name.charAt(idx) == '+') {
                idx++;
                if (Character.isDigit(chord_name.charAt(idx))) {
                    doAdd(look_for_num());
                }
            } else if (chord_name.charAt(idx) == '(') {
                idx++;
            } else if (chord_name.charAt(idx) == ')') {
                idx++;
            } else if (Character.isDigit(chord_name.charAt(idx))) {
                doAdd(look_for_num());
            } else if (chord_name.charAt(idx) == '-') {
                idx++;
                doFlat(look_for_num());
            } else if (chord_name.charAt(idx) == 'b') {
                idx++;
                doFlat(look_for_num());
            } else if (chord_name.charAt(idx) == '#') {
                idx++;
                doSharp(look_for_num());
            } else {
                throw new MalformedChordException("Can't understand in context. so far\n" + work);
            }
        }

        /* look for bass note */
        /* important to set baseHalfToneAbs */
        if (idx < clen && chord_name.charAt(idx) == '/')	/* note at base or addition */ {
            idx++;
            int role = look_for_num();
            if (role != 0) {
                Note note = scale.getNoteByHT(roleToHT(role));
                has(note, false);
                baseHalfToneAbs = note.getHalftone();
                baseName = note.getName();
            } else {
                baseName = findNoteName(chord_name);
                baseHalfToneAbs = findAbsoluteHT(baseName);
                has(new Note(baseHalfToneAbs, baseName), false);
            }
        }

        // build required HT
        Integer ht;
        for (int i = 0; i < requiredNotes.size(); i++) {
            ht = new Integer((((Note) (requiredNotes.elementAt(i))).getHalftone()));
            if (!requiredHT.contains(ht)) {
                requiredHT.addElement(ht);
            }
        }
// removed to include X5
//        if (requiredHT.size() < 3) {
//            throw new MalformedChordException("Less than 3 notes don't make a chord!");
//        }
    }
/*------------------------------------------------------------*/
    void printRequiredHTAbs() {

        work.append("Required absolute halftones for  " + chord_name + ":");
        int ht[] = getRequiredHalftonesAbs();
        for (int j = 0; j < ht.length; j++)
            work.append(" " + ht[j]);
        work.append("\n");
    }

/*------------------------------------------------------------*/
    void printRequiredHTRel() {

        work.append("Required relative halftones for  " + chord_name + ":");
        int ht[] = getRequiredHalftonesRel();
        for (int j = 0; j < ht.length; j++)
            work.append(" " + ht[j]);
        work.append("\n");
    }

    /*------------------------------------------------------------*/
    void printRequiredNotes() {

        work.append("Required notes for  " + chord_name + ":");
        for (int j = 0; j < requiredNotes.size(); j++)
            work.append(" " + ((Note) requiredNotes.elementAt(j)).getName());

        if (baseName != null) {
            work.append(" with " + baseName + " at the base");
        }
        work.append("\n");
    }
/*------------------------------------------------------------*/
    public Scale getScale() {
        return scale;
    }

}
