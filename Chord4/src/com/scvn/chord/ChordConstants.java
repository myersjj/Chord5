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
/*
 * ChordConstants.java
 *
 * Created on December 8, 2001, 9:43 AM
 */

package com.scvn.chord;

/**
 *
 * @author  martin
 * @version
 */
public interface ChordConstants {


    public static final int NOT_SET = -1;
    public static final int YES = 1;
    public static final int NO = 0;
    
    public static final String SYSTEM = "system";
    public static final String RC = "rc";
    public static final String RT = "rt";
    public static final String CURRENT = "current";
    
    public static final String TEXTSIZE = "TextSize";
    public static final String MONOSIZE = "MonoSize";
    public static final String CHORDSIZE = "ChordSize";
    public static final String TEXTFONTNAME = "TextFontName";
    public static final String CHORDFONTNAME = "ChordFontName";
    public static final String MONOFONTNAME = "MonoFontName";

    public static final String GRIDSIZE = "GridSize";
    public static final String NOGRIDS = "NoGrids";
    public static final String SHOWSPLASH = "ShowSplash";
    public static final String NOEASYGRIDS = "NoEasyGrids";

    public static final String TRANSPOSITION = "Transposition";
    public static final String PAGINATION = "Pagination";
    public static final String SIGNATUREOBJECT = "SignatureObject";
    public static final String DOTOC = "DoToc";
    public static final String DOSUBTOC = "DoSubToc";
    public static final String ISLEFTFOOTEVEN = "IsLeftFootEven";
    public static final String ISLISTOFFILES= "IsListOfFiles";
    public static final String ISLYRICSONLY = "IsLyricsOnly";
    public static final String NUMBERLOGICAL = "NumberLogical";
    public static final String FIRSTPAGELABEL = "FirstPageLabel";

    public static final String  DefTextSize= "12";
    public static final String  DefMonoSize= "12";
    public static final String  DefChordSize= "9";
    public static final String  DefTextFontName= "Serif";
    public static final String  DefChordFontName= "SansSerif";
    public static final String  DefMonoFontName= "Monospaced";

    public static final String  DefGridSize= "25";
    public static final String  DefNoGrids= "false";
    public static final String  DefNoEasyGrids= "false";
    public static final String  DefShowSplash = "false";

    public static final String  DefTransposition= "0";
    public static final String  DefPagination= "1";
    public static final String  DefSignatureObject= "";
    public static final String  DefDoToc= "true";
    public static final String  DefDoSubToc= "true";
    public static final String  DefIsLeftFootEven= "false";
    public static final String  DefIsListOfFiles= "false";
    public static final String  DefIsLyricsOnly= "false";
    public static final String  DefNumberLogical= "false";
    public static final String  DefFirstPageLabel= "1";

    public static final int FRET_OPEN = -2;
    public static final int FRET_X	= -1;
    public static final int CHORD_BUILTIN = 0;
    public static final int CHORD_DEFINED = 1;
    public static final int CHORD_IN_CHORDRC = 2;
    public static final int CHORD_FROM_DEFT = 3;

    static final String FRET_NONE_STR = "-";		/* fret value for unplayed strings */
    static final String FRET_X_STR = "x";		/* fret value for muted strings */
    static final String BASE_FRET_STR = "base-fret";
    static final String FRETS_STR = "frets";
    static final int LONG_FINGERS = 4;

    static final int CHORD_EASY = 0;
    static final int CHORD_HARD = 1;

    static final int ERR_UNDEF_CHORD                  = 1;  // "chord '" + chord + "' has never been defined"
    static final int ERR_CHORD_WITHIN_CHORD           = 2;    // "Opening a chord within a chord!"
    static final int ERR_UNMATCHED_BRACKET            = 3;   // "']' found with no matching '['"
    static final int ERR_UNMATCHED_BRACE              = 4; //"'}' found with no matching '{'"
    static final int ERR_TRANSPOSING                  = 5; // "Don't know how to transpose [" + chord + "]"
    static final int ERR_INV_TEXTSIZE                 = 6; // "invalid value for textSize"
    static final int ERR_INV_CHORDSIZE                = 7; // "invalid value for chordSize"
    static final int ERR_NOT_IN_CHORUS                = 8; // "Not in a chorus."
    static final int ERR_NOT_IN_TAB                   = 9; // "Not in a tablature !"
    static final int ERR_ALREADY_IN_TAB               = 10; // "Already in a tablature !"
    static final int ERR_INV_COL_NUM                  = 11; // "invalid value for number of columns"
    static final int ERR_UNKNOWN_CMD                  = 12; // "Unknown Directive : {" + command + "}"
    static final int ERR_EOL_IN_CHORD                 = 13; // "Line ends while in a chord !"
    static final int ERR_EOL_IN_DIR                   = 14; // "Line ends while in a directive !"
    static final int ERR_NULL_COMMENT                 = 15 ;// "Null comment."
    static final int ERR_MISSIGN_BASE_FRET_WORD       = 16 ;// "syntax error in chord definition: keyword <"+BASE_FRET_STR+"> missing"
    static final int ERR_MISSING_BASE_FRET_VAL        = 17 ;// "syntax error in chord definition: no base fret value"
    static final int ERR_MISSING_NAME                 = 18 ;// "syntax error in chord definition: no chord name"
    static final int ERR_MISSING_FRETS_WORD           = 19 ;// "syntax error in chord definition: keyword <frets> missing"
    static final int ERR_MISSING_FRETS_VAL            = 20 ;// "syntax error in chord definition : too few arguments"
    static final int ERR_EXTRA_FRETS_VAL              = 21 ;// "syntax error in chord definition: too many arguments"
//    static final int ERR_UNKNOWN_CMD                  = 12; // "Unknown Directive : {" + command + "}"

    static final String[] ERR_MSGS= {
        "chord not defined",
        "Opening a chord within a chord!",
        "']' found with no matching '['",
        "'}' found with no matching '{'",
        "Don't know how to transpose this",
        "invalid value for textSize",
        "invalid value for chordSize",
        "Not in a chorus.",
        "Not in a tablature !",
        "Already in a tablature !",
        "invalid value for number of columns",
        "Unknown Directive",
        "Line ends while in a chord !",
        "Line ends while in a directive !",
        "Null comment.",
        "syntax error in chord definition: keyword \'base-fret\' missing",
        "syntax error in chord definition: no base fret value",
        "syntax error in chord definition: no chord name",
        "syntax error in chord definition: keyword \'frets\' missing",
        "syntax error in chord definition : too few arguments",
        "syntax error in chord definition: too many arguments"
    };
}
