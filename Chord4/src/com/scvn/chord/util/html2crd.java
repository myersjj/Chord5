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


package com.scvn.chord.util;

//import com.oroinc.text.regex.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class html2crd {

    //<H1 ALIGN=CENTER>25 or 6 to 4</H1>
    String reTitle = "<H1 ALIGN=CENTER>(.*)</H1>";
    //<H2 ALIGN=CENTER>Chicago</H2>
    String reSubtitle = "<H2 ALIGN=CENTER>(.*)</H2>";
    //Staring blindly into space<BR>
    String reTextOnlyLine = ">?([^>]*)<BR>";
    String reMixedLine = "<P><TABLE BORDER=0 CELLSPACING=0 CELLPADDING=0>";
//    <TR><TD><I></I><TD><I>Am</I><TD><I>F</I></TR>
    String reChordLine = "><I>([^<>]*)</I><";
//    <TR><TD>W<TD>aiting for the break of day<IMG SRC="space/space2.gif"><TD><IMG SRC="space/space1.gif"></TR>
    String reTextLine = "TD>([^<>]*)<";
    Pattern patTitle;
    Pattern patSubtitle;
    Pattern patMixedLine;
    Pattern patTextOnlyLine;
    Pattern patChordLine;
    Pattern patTextLine;

    Matcher matcher;
    Matcher textMatcher;
    Matcher chordMatcher;

    public static void main(String arg[]) {
        new html2crd(arg);
    }

    public html2crd(String arg[]) {

            patTitle = Pattern.compile(reTitle);
            patSubtitle = Pattern.compile(reSubtitle);
            patMixedLine = Pattern.compile(reMixedLine);
            patTextOnlyLine = Pattern.compile(reTextOnlyLine);
            patTextLine = Pattern.compile(reTextLine);
            patChordLine = Pattern.compile(reChordLine);

        for (int i = 0; i < arg.length; i++) {
            convert(arg[i]);
        }
    }

    public void convert(String fname) {
        try {
            FileReader fr = new FileReader(fname);
            BufferedReader br = new BufferedReader(fr);
            String newName = fname.substring(0, fname.lastIndexOf('.')) + ".crd";
            System.out.println("Converting ["+fname+"] to ["+newName+"]");
            PrintWriter pw = new PrintWriter(new FileOutputStream(newName));
            String line;
            String text, chord;
            int cInput, tInput;
            while ((line = br.readLine()) != null) {
                matcher = patTitle.matcher(line);
                if (matcher.matches()) {
                    pw.println("{title: " + matcher.group(1) + "}");
                }
                matcher = patSubtitle.matcher(line);
                if (matcher.matches()) {
                    pw.println("{subtitle: " + matcher.group(1) + "}");
                }
                matcher = patTextOnlyLine.matcher(line);
                if (matcher.matches()) {
                    pw.println(matcher.group(1));
                }

                matcher = patMixedLine.matcher(line);
                if (matcher.matches()) {
                    String cLine = br.readLine();
                    String tLine = br.readLine();
                    cInput = 0;
                    tInput = 0;
                    chordMatcher = patChordLine.matcher(cLine);
                    textMatcher = patTextLine.matcher(tLine);
                    while (chordMatcher.find(cInput) && textMatcher.find(tInput)) {
                        tInput = textMatcher.end();
                        cInput = chordMatcher.end();
                        // number of groups should match
                        text = textMatcher.group(1);
                        chord = chordMatcher.group(1);
                        if (chord != null && chord.length() > 0) {
                            pw.print("[" + chord + "]");
                        }
                        if (text != null && text.length() > 0) {
                        pw.print(text);
                        }
                    }
                    pw.println();
                }
            }
            pw.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
