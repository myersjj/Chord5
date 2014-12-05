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
import java.util.logging.Logger;

import com.scvn.chord.render.*;

public class Parser implements ChordConstants {
	private static final Logger LOGGER =
	        Logger.getLogger(Chord4.class.getName());
    private Producer producer;
    private C4Renderer renderer;
    //	private OptFrame optFrame;
    private Settings settings;
    private Chord4 chord4;
    private KnownChordVector knownChords;


    String textLine = "";
    /* Lyrics Buffer */
    String chord;
    String directive;
    String noChordStr = "N.C.";
//    String msg;
    Vector chordLine;
    int iInput = 0;
    int transpose = 0;
    /* transposition value */
    
    
    Context context;
    public Parser(Chord4 c4) {
        chord4 = c4;
        context = new Context();
        chordLine = new Vector(10, 10);
        knownChords = chord4.getKnownChords();
        //optFrame = c4.getOptFrame();
        settings = c4.getSettings();
        Music.setSettings(settings);
    }
    /* --------------------------------------------------------------------------------*/
    void doChord(String chord) {
        int k;
        KnownChord kc;
        for (k = chordLine.size(); k < textLine.length(); k++) {
            chordLine.addElement(null);
        }
        if (!chord.toUpperCase().equals(noChordStr)) {
            String c = Music.doTranspose(chord);
            if (c == null) {
                ParsingErrorSet.addSyntaxError(ERR_TRANSPOSING, chord, producer);
                return;
            }
            chord = c;
            if ((kc = knownChords.getChord(chord)) == null) {
                ParsingErrorSet.addSyntaxError(ERR_UNDEF_CHORD, chord, producer);
                kc = knownChords.add(chord, FRET_OPEN, FRET_OPEN, FRET_OPEN, FRET_OPEN, FRET_OPEN, FRET_OPEN,
                                        0, KnownChord.CHORD_DEFINED, KnownChord.CHORD_EASY);
            }
            //  	usedChords.add(kc);
            renderer.addUsedChord(kc);
            chordLine.addElement(kc);
        }
        else {
            chordLine.addElement(noChordStr);
        }
    }
    /* --------------------------------------------------------------------------------*/
    boolean isArgOK(String a, boolean required)
    {
        boolean present = (a != null && a.trim().length() != 0);
        if (required && ! present)
        {
//                msg += ParsingErrorSet.error("Argument is required and should not be an empty string", producer);
                return false;
        }
        if (!required && present)
        {
//                msg += ParsingErrorSet.error("No argument required", producer);
                return false;
        }
        return true;
    }
    
    /* --------------------------------------------------------------------------------*/
    void doDirective(String directive) {
        int i;
        String command, arg = null;
        
        command = directive;
        LOGGER.fine("Command=" + command);
        int endOfCmd = directive.indexOf(':');
        if (endOfCmd >= 0) {
            command = directive.substring(0, endOfCmd).trim().toLowerCase();
            arg = directive.substring(endOfCmd+1).trim();
        }
        
        if (command.equals("start_of_chorus") || command.equals("soc") && isArgOK(arg, false)) {
            context.needSOC = true;
            context.inChorus = true;
        }
        else if (command.equals("end_of_chorus") || command.equals("eoc") && isArgOK(arg, false)) {
            if ( context.inChorus ) {
                renderer.setEndOfChorus();
            }
            else {
                ParsingErrorSet.addSyntaxError(ERR_NOT_IN_CHORUS, null, producer);
            }
        }
        else if (command.equals("textfont") || command.equals("tf") && isArgOK(arg, true)) {
            settings.set(context.inChordrc ? RC : CURRENT, TEXTFONTNAME, arg, true);
        }
        else if (command.equals("chordfont") || command.equals("cf") && isArgOK(arg, true)) {
            settings.set(context.inChordrc ? RC : CURRENT, CHORDFONTNAME, arg, true);
        }
        else if (command.equals("chordsize") || command.equals("cs") && isArgOK(arg, true)) {
            i = Integer.parseInt(arg);
            if ( i == 0 ) {
                ParsingErrorSet.addSyntaxError(ERR_INV_CHORDSIZE, arg, producer);
            }
            else {
                settings.set(context.inChordrc ? RC : CURRENT, CHORDSIZE, arg, true);
            }
        }
        else if (command.equals("textsize") || command.equals("ts") && isArgOK(arg, true)) {
            i = Integer.parseInt(arg);
            if ( i == 0 ) {
                ParsingErrorSet.addSyntaxError(ERR_INV_TEXTSIZE, arg, producer);
            }
            else {
                settings.set(context.inChordrc ? RC : CURRENT, TEXTSIZE, arg, true);
            }
        }
        else if (command.equals("comment") || command.equals("c") && isArgOK(arg, true)) {
            renderer.doComment(arg, 1);
        }
        else if (command.equals("comment_italic") || command.equals("ci") && isArgOK(arg, true)) {
            renderer.doComment(arg, 2);
        }
        else if (command.equals("comment_box") || command.equals("cb") && isArgOK(arg, true)) {
            renderer.doComment(arg, 3);
        }
        else if (command.equals("new_song") || command.equals("ns") && isArgOK(arg, false)) {
            renderer.doNewSong();
        }
        else if (command.equals("title") || command.equals("t") && isArgOK(arg, true)) {
            renderer.doTitle(arg);
        }
        else if (command.equals("subtitle") || command.equals("st") && isArgOK(arg, true)) {
            renderer.doSubtitle(arg);
        }
        else if (command.equals("signature") || command.equals("sig") && isArgOK(arg, true)) {
            settings.set(context.inChordrc ? RC : CURRENT, SIGNATUREOBJECT, arg, true);
        }
        else if (command.equals("define") || command.equals("d") && isArgOK(arg, true)) {
            chord4.getKnownChords().doDefineChord(arg, context.inChordrc, producer);
        }
        else if (command.equals("no_toc") || command.equals("nt") && isArgOK(arg, false)) {
            settings.set(context.inChordrc ? RC : CURRENT, DOTOC, "false", true);
            settings.set(context.inChordrc ? RC : CURRENT, DOSUBTOC, "false", true); // normal side effect
        }
        else if (command.equals("no_subtoc") || command.equals("nst") && isArgOK(arg, false)) {
            settings.set(context.inChordrc ? RC : CURRENT, DOSUBTOC, "false", true);
        }
        else if (command.equals("no_grid") || command.equals("ng") && isArgOK(arg, false)) {
            settings.set(context.inChordrc ? RC : CURRENT, NOGRIDS, "true", true);
        }
        else if (command.equals("grid") || command.equals("g") && isArgOK(arg, false)) {
            settings.set(context.inChordrc ? RC : CURRENT, NOGRIDS, "false", true);
        }
        else if (command.equals("new_page") || command.equals("np") && isArgOK(arg, false)) {
            renderer.doEndOfPage(false);
            renderer.doStartOfPage();
        }
        else if (command.equals("start_of_tab") || command.equals("sot") && isArgOK(arg, false)) {
            if ( context.inTab ) {
                ParsingErrorSet.addSyntaxError(ERR_ALREADY_IN_TAB, null, producer);
            }
            else {
                context.inTab = true;
            }
        }
        else if (command.equals("end_of_tab") || command.equals("eot") && isArgOK(arg, false)) {
            if (! context.inTab ) {
                ParsingErrorSet.addSyntaxError(ERR_NOT_IN_TAB, null, producer);	;
            }
            else {
                context.inTab = false;
            }
        }
        else if (command.equals("column_break") || command.equals("colb") && isArgOK(arg, false)) {
            renderer.doEndOfColumn();
        }
        else if (command.equals("columns") || command.equals("col") && isArgOK(arg, true)) {
            i = Integer.parseInt(arg);
            if ( i <= 0 ) {
                ParsingErrorSet.addSyntaxError(ERR_INV_COL_NUM, null, producer);
            }
            else {
                renderer.setColumns(i);
            }
        }
        else if (command.equals("new_physical_page") || command.equals("npp") && isArgOK(arg, false)) {
            renderer.doEndOfPage(true);
            renderer.doStartOfPage();
        }
        else {
            ParsingErrorSet.addSyntaxError(ERR_UNKNOWN_CMD, command, producer);
            LOGGER.fine("Unknown Command=" + command);
            context.hasDirective = false;
        }
    }
    /* --------------------------------------------------------------------------------*/
    void doEol(Context context) {
        textLine = textLine.trim();
        //LOGGER.fine("doEol:<"+textLine+">");
        if (context.inDirective)
            ParsingErrorSet.addSyntaxError(ERR_EOL_IN_DIR, null, producer);
        if (context.inChord)
            ParsingErrorSet.addSyntaxError(ERR_EOL_IN_CHORD, null, producer);
        if (!context.hasDirective) {
            if (renderer == null) {
                if (textLine.length() == 0) {
                    // ParsingErrorSet.addSyntaxError("line is NOT a directive", producer);
                }
            }
            else
                if (context.inTab) {
                    renderer.printTabLine(textLine);
                }
                else {
                    renderer.renderTextAndChords(textLine, chordLine);
                }
        }
        else {
            context.hasDirective = false;
        }
        iInput = 0;
        context.inDirective = false;
        context.inChord = false;
        textLine = "";
        chordLine.removeAllElements();
    }
    /* --------------------------------------------------------------------------------*/
    public Context getContext() {
        return context;
    }
    /* --------------------------------------------------------------------------------*/
    
    public void processInput() {
        /*debug("start of processFile");*/
        settings = chord4.getSettings();
        setRenderer(chord4.getRenderer());
        Music.setSettings(settings);
        ParsingErrorSet.init();
        //ParsingErrorSet ces = new ParsingErrorSet();
        int c;
        while ((c = producer.getChar()) != Producer.EOJ) {
            //LOGGER.fine("got char:["+(char)c+"]="+c);
            if (c == Producer.EOF) {
                //LOGGER.fine("Parser got EOF");
                if (! context.inChordrc)
                {
                renderer.doEndOfSong();
                }
                //iInput=0;
                //textLine="";
                continue;
            }
            if (c == Producer.SOF ) {
                //LOGGER.fine("Parser got SOF");
                if (! context.inChordrc) {
                    renderer.doStartOfSong();
                }
                continue;
            }
            
            iInput++;
            switch ((char) c) {
                case '[' :
                    if (!context.inTab) {
                        if (context.inChord)
                            ParsingErrorSet.addSyntaxError(ERR_CHORD_WITHIN_CHORD, null, producer);
                        else
                            context.inChord = true;
                        chord = "";
                    }
                    else
                        textLine += (char) c;
                    break;
                case ']' :
                    if (!context.inTab) {
                        if (context.inChord) {
                            context.inChord = false;
                            doChord(chord);
                            context.hasChord = true;
                            chord = "";
                        }
                        else
                            ParsingErrorSet.addSyntaxError(ERR_UNMATCHED_BRACKET, null, producer);
                    }
                    else
                        textLine += (char) c;
                    break;
                case '{' :
                    context.inDirective = true;
                    directive = new String();
                    context.hasDirective = true;
                    break;
                case '}' :
                    if (context.inDirective) {
                        context.inDirective = false;
                        for (;(c = producer.getChar()) != '\n';);
                        iInput = 0;
                        doDirective(directive);
                        context.hasDirective = false;
                    }
                    else
                        ParsingErrorSet.addSyntaxError(ERR_UNMATCHED_BRACE, null, producer);
                    break;
                case '\n' :
                    doEol(context);
                    break;
                case '(' :
                case ')' :
                    if (context.inDirective) {
                        directive += (char) c;
                        break;
                    }
                    else
                        if (context.inChord) {
                            chord += (char) c;
                            break;
                        }
                        else {
                            textLine += (char) c;
                            break;
                        }
                    
                    /* This case HAS to be the last before the default statement !!! */
                    
                case '#' :
                    if (iInput == 1) {
                        for (;(c = producer.getChar()) != '\n';);
                        iInput = 0;
                        break;
                    }
                default :
                    if (context.inChord) {
                        if (c != ' ') {
                            chord += (char) c;
                        }
                    }
                    else
                        if (context.inDirective) {
                            directive += (char) c;
                        }
                        else {
                            textLine += (char) c;
                        }
                    break;
            }
        }
//        return ces;
    }
    /* --------------------------------------------------------------------------------*/
    public void setProducer(Producer prod) {
        producer = prod;
    }
    /* --------------------------------------------------------------------------------*/
    public void setRenderer(C4Renderer rend) {
        renderer = rend;
        if (renderer != null) {
            renderer.setContext(context);
        }
    }
}
