package com.scvn.chord.deft;

public class Scale {
    static String roles[] = {
        "Fundamental     ",
        "Minor Second    ",
        "Major Second    ",
        "Minor Third     ",
        "Major Third     ",
        "Fourth          ",
        "Minor Fifth     ",
        "Even Fifth      ",
        "Augmented Fifth ",
        "Sixth           ",
        "Dom Seventh     ",
        "Major Seventh   ",
        "Octave          ",
        "Minor Ninth     ",
        "Ninth           ",
        "Minor Tenth     ",
        "Tenth           ",
        "Eleventh        ",
        "Minor Twelveth  ",
        "Even Twelveth   ",
        "Minor Thirteenth",
        "Thirteenth      "};

    static final int ROOT = 0;
    static final int MINOR_SECOND = 1;
    static final int MAJOR_SECOND = 2;
    static final int MINOR_THIRD = 3;
    static final int MAJOR_THIRD = 4;
    static final int FOURTH = 5;
    static final int DIM_FIFTH = 6;
    static final int EVEN_FIFTH = 7;
    static final int AUG_FIFTH = 8;
    static final int SIXTH = 9;
    static final int DIM_SEVENTH = 9;
    static final int DOM_SEVENTH = 10;
    static final int SEVENTH = 11;
    static final int OCTAVE = 12;
    static final int DIM_NINTH = 13;
    static final int NINTH = 14;
    static final int MINOR_TENTH = 15;
    static final int TENTH = 16;
    static final int ELEVENTH = 17;
    static final int MINOR_TWELVETH = 18;
    static final int EVEN_TWELVETH = 19;
    static final int MINOR_THIRTEENTH = 20;
    static final int THIRTEENTH = 21;
    static final int SCALE_SIZE = 22;

    static final boolean NO_CHANGE = false;
    static final boolean CHANGE = true;
    static final int DOWN = -1;
    static final int UP = 1;


    Note[] scale;
    int rootAbsHalfTone = -1;  // control value: unset

    public Scale(Note n) {
        scale = new Note[SCALE_SIZE];
        rootAbsHalfTone = n.getHalftone();
        generateScale(n.getName());

    }

    /*------------------------------------------------------------*/
    void generateScale(String rootName) {
        /* System.out.println ("Generating scale of %s\n", root_ptr); */
        scale[ROOT] = new Note(rootAbsHalfTone + ROOT, rootName);
        scale[MINOR_SECOND] = new Note(rootAbsHalfTone + MINOR_SECOND, next_half_tone(scale[ROOT], CHANGE, UP));
        scale[MAJOR_SECOND] = new Note(rootAbsHalfTone + MAJOR_SECOND, next_half_tone(scale[MINOR_SECOND], NO_CHANGE, UP));
        scale[MINOR_THIRD] = new Note(rootAbsHalfTone + MINOR_THIRD, next_half_tone(scale[MAJOR_SECOND], CHANGE, UP));
        scale[MAJOR_THIRD] = new Note(rootAbsHalfTone + MAJOR_THIRD, next_half_tone(scale[MINOR_THIRD], NO_CHANGE, UP));
        scale[FOURTH] = new Note(rootAbsHalfTone + FOURTH, next_half_tone(scale[MAJOR_THIRD], CHANGE, UP));
        scale[DIM_FIFTH] = new Note(rootAbsHalfTone + DIM_FIFTH, next_half_tone(scale[FOURTH], CHANGE, UP));
        scale[EVEN_FIFTH] = new Note(rootAbsHalfTone + EVEN_FIFTH, next_half_tone(scale[DIM_FIFTH], NO_CHANGE, UP));
        scale[AUG_FIFTH] = new Note(rootAbsHalfTone + AUG_FIFTH, next_half_tone(scale[EVEN_FIFTH], NO_CHANGE, UP));
        scale[SIXTH] = new Note(rootAbsHalfTone + SIXTH, next_half_tone(scale[AUG_FIFTH], CHANGE, UP));
        scale[DOM_SEVENTH] = new Note(rootAbsHalfTone + DOM_SEVENTH, next_half_tone(scale[SIXTH], CHANGE, UP));
        scale[SEVENTH] = new Note(rootAbsHalfTone + SEVENTH, next_half_tone(scale[DOM_SEVENTH], NO_CHANGE, UP));
        scale[OCTAVE] = new Note(rootAbsHalfTone + OCTAVE, next_half_tone(scale[SEVENTH], CHANGE, UP));

        scale[DIM_NINTH] = new Note(rootAbsHalfTone + DIM_NINTH, next_half_tone(scale[OCTAVE], CHANGE, UP));
        scale[NINTH] = new Note(rootAbsHalfTone + NINTH, next_half_tone(scale[DIM_NINTH], NO_CHANGE, UP));
        scale[MINOR_TENTH] = new Note(rootAbsHalfTone + MINOR_TENTH, next_half_tone(scale[NINTH], CHANGE, UP));
        scale[TENTH] = new Note(rootAbsHalfTone + TENTH, next_half_tone(scale[MINOR_TENTH], NO_CHANGE, UP));
        scale[ELEVENTH] = new Note(rootAbsHalfTone + ELEVENTH, next_half_tone(scale[TENTH], CHANGE, UP));
        scale[MINOR_TWELVETH] = new Note(rootAbsHalfTone + MINOR_TWELVETH, next_half_tone(scale[ELEVENTH], CHANGE, UP));
        scale[EVEN_TWELVETH] = new Note(rootAbsHalfTone + EVEN_TWELVETH, next_half_tone(scale[MINOR_TWELVETH], NO_CHANGE, UP));
        scale[MINOR_THIRTEENTH] = new Note(rootAbsHalfTone + MINOR_THIRTEENTH, next_half_tone(scale[EVEN_TWELVETH], NO_CHANGE, UP));
        scale[THIRTEENTH] = new Note(rootAbsHalfTone + THIRTEENTH, next_half_tone(scale[MINOR_THIRTEENTH], CHANGE, UP));

        //Note.setScale(this);
    }
/*------------------------------------------------------------*/

    public Note getNoteByHT(int pos) {
        return scale[pos];
    }
/*------------------------------------------------------------*/
    public Note getNoteAbs(int pos) {
        return scale[(pos - rootAbsHalfTone + 12) % 12];
    }

/*------------------------------------------------------------*/
    Note[] getNoteArray() {
        return scale;
    }
/*------------------------------------------------------------*/
    public String getNoteName(int pos) {
        return scale[pos].getName();
    }
/*------------------------------------------------------------*/

    public int getRootAbsHalfTone() {
        return rootAbsHalfTone;
    }
/*------------------------------------------------------------*/
    public static String getRole(int r) {
        return roles[r];
    }
/*------------------------------------------------------------*/
    String next_half_tone(Note n, boolean change_name, int dir) {
        String ht_name = n.getName();
        int j;
        int sharp = 0;
        StringBuffer tmp_name = new StringBuffer();
        char tmp_root;

        /* parse the old one */

        tmp_root = ht_name.charAt(0);
        ;

        for (j = 1; j < ht_name.length(); j++) {
            if (ht_name.charAt(j) == 'b') {
                sharp--;
            } else if (ht_name.charAt(j) == '#') {
                sharp++;
            }
        }
        /* generate the new root */
        if (change_name) {
            tmp_root = (char) ((int) tmp_root + dir);
            if (tmp_root > 'G') {
                tmp_root = 'A';
            }
            if (tmp_root < 'A') {
                tmp_root = 'G';
            }
            sharp -= dir;
            if ((tmp_root != 'C') && (tmp_root != 'F')) {
                sharp -= dir;
            }
        }

        sharp += dir;

        tmp_name.append(tmp_root);

        /* print the sharps and flats */
        for (j = 0; j < sharp; j++) {
            tmp_name.append('#');
        }

        for (j = 0; j > sharp; j--) {
            tmp_name.append('b');
        }

        return (tmp_name.toString());
    }

    public String toString() {
        StringBuffer work = new StringBuffer();
        work.append("Printing scale of " + getNoteName(Scale.ROOT) + "\n");
        work.append("\t" + roles[0] + " : " + getNoteName(Scale.ROOT) + "(" + getRootAbsHalfTone() + " half tones above C)\n");

        for (int r = 1; r < Scale.SCALE_SIZE; r++) {
            work.append("\t" + roles[r] + " : " + getNoteName(r) + "\n");
        }
        return work.toString();
    }
}
