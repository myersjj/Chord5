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

package com.scvn.chord.render;

import java.net.*;
import java.awt.geom.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.print.*;
import java.text.*;
import java.util.*;
import java.util.logging.Logger;
import java.awt.font.*;
import java.awt.event.*;
import java.io.*;

import com.scvn.chord.grop.*;
import com.scvn.chord.*;


public class C4Renderer implements Printable, Pageable, ChordConstants {
    //, ComponentListener  {
	private static final Logger LOGGER =
	        Logger.getLogger(Chord4.class.getName());
    static final int MAXNOTE = 8;
    static final double SCREEN_RES = 72.0;
    BufferedImage bufferedImage;
    Chord4 c4;
    Settings settings;
    FontRenderContext fontRC;
    PageFormat userPageFormat = null;
    PrinterJob pj;

    KnownChordVector knownChords;
    Producer producer;
    OptFrame optFrame;
    RenderingFrame rFrame;
    Graphics2D cgc;
    Page currentPage;
    ChordVector usedChords;
    TreeMap toc;
    PageSet pageSet;
    Font textFont, chordFont, monoFont;
    Context context;
    TocElement currentTocElement;

    // Strings
    String chord = ""; // Buffer for the name of the chord
    String title1; // Holds the first title line
    String source;
    String directive; // Buffer for the directive
    String mesgbuf;
    String mesg, currentFile;
    String commandName;
    String sigText = null;

    Image sigImage;
    Color commentBgColor;

    // ints
    int c; // Current character in input file
    int iChord; // Index to 'chord'
    int iDirective; // Index to 'directive'
    int iInput; // Input line pointer
    /* 1 for even page numbers on the left */
    int pageLabel = 0; // physical page number
    int lpageLabel = 1; // logical page number
    int vpos; // Current PostScript position, in points
    int colVpos; // Beginning height of column, in points
    int minColVpos; // lowest colums ending
    int hpos;
    int hOffset = 0; // horizontal offset for multi-col output
    int startOfChorus; // Vertical positions of the chorus
    int endOfChorus;
    int curTextSize = 0;
    int nPages = 0; // total physical page counter
    int vPages = 0; // virtual pages
    int nLines = 1; // line number in input file
    int maxColumns = 1; // number of columns
    int nColumns = 0; // counter of columns
    int songPages = 0; // song page counter
    int margin; // distance between the left edge of the paper and the first text position
    int actualChordHpos;
    int desiredChordHpos;
    int minChordHpos;
    int clipx; // distance betwwen th eleft edge of the paper and the first useable position
    int transpose = 0;
    int top;
    int bottom;
    int width;
    int height;
    int titleHeight = 36;
    int footerHeight = 15;
    int noGrid = NOT_SET;

    // other numericals
    double chordInc;
    double rotation = 0.0; // Current rotation

    // booleans
    boolean numberAll = true; // number the first page (set with -p 1) false in prod version !!!*/
    boolean numberLogical = false;

    //boolean lyricsOnly = false;
    boolean dumpOnly = false;
    boolean inTab = false;
    boolean postscriptDump = false;
    boolean autoSpace = false; // applies lyricsOnly when no chords
    boolean noEasyGrids = false;
    boolean title1Found = false;
    boolean leftFootEven = false; // 0 for even page numbers on the right
    boolean debugMode = false;
    boolean renderingDone = false;

/* --------------------------------------------------------------------------------*/
    public C4Renderer(OptFrame optFrame, Producer prod, Chord4 c4) {
        producer = prod;
        this.optFrame = optFrame;
        this.c4 = c4;
        knownChords = c4.getKnownChords();
        settings = c4.getSettings();

        rFrame = new RenderingFrame(c4, this);

        // init userPageFormat
        pj = PrinterJob.getPrinterJob();
        userPageFormat = pj.defaultPage();

        userPageFormat.setOrientation(userPageFormat.PORTRAIT);
        Paper paper = new Paper();
        paper.setImageableArea(36, 0, userPageFormat.getWidth() - 36, userPageFormat.getHeight() - 18);
        userPageFormat.setPaper(paper);
        setUserPageFormat(userPageFormat, false);

        commentBgColor = Color.lightGray;
    }
/* --------------------------------------------------------------------------------*/
    public void addUsedChord(KnownChord kc) {
        usedChords.add(kc);
    }
/* --------------------------------------------------------------------------------*/
    void advance(int amount) {
        vpos += amount;
        /* Affect text positionning ! */
        if (vpos > bottom) {
            doEndOfColumn();
        }
    }

    /* --------------------------------------------------------------------------------*/
    public void printGrids() {
        settings.setDoToc(false);
        start();
        usedChords = knownChords;
        renderingDone = true;
//        rFrame.setCurrentPage(0);
        setGraphics(rFrame.getRenderingPane().buildGraphics());
        doStartOfPage();
        setTextFont(settings.getTextSize());
        useTextFont();
        doTitle("Known Chords as Grids");
//        drawChords(); remove duplicate chord list -ml
        doEndOfSong();
        setVisible(true);
        rFrame.setCurrentPage(0);

    }
/* --------------------------------------------------------------------------------*/
    public void printDefines() {
        /*
         doStartOfPage();
        setTextFont(textSize);
        useTextFont();
        usedChords = knownChords;
        doTitle("Known Chords as Grids");
        drawChords();
        usedChords = new ChordVector();
        doEndOfSong();
         */
    }

/*--------------------------------------------------------------------------------*/
    void cleanUsedChords() {
        usedChords.removeAllElements();
    }
/* --------------------------------------------------------------------------------*/
    public void close() {
        /* generate index  pages */
         if (settings.getDoToc()) {
            doToc(toc);
            doEndOfPage(false);
        }
        if (settings.getDoSubToc()) {
            doSubToc(toc);
            doEndOfPage(false);
        }
        if (vPages != 0) {
            doEndOfPhysPage();
        }
    }
/* --------------------------------------------------------------------------------*/
    public void componentHidden(ComponentEvent e) {
    }
/* --------------------------------------------------------------------------------*/
    public void componentMoved(ComponentEvent e) {
    }
/* --------------------------------------------------------------------------------*/
    public void componentResized(ComponentEvent e) {
        // System.out.println("new size: " + getSize());
    }
/* --------------------------------------------------------------------------------*/
    public void componentShown(ComponentEvent e) {
    }
/* --------------------------------------------------------------------------------*/
    public void doChorusLine() {
        currentPage.drawLine(clipx, startOfChorus, clipx, endOfChorus);
    }
/* --------------------------------------------------------------------------------*/
    public void doComment(String comment, int style) {
        int w, h;
        if (comment == null) {
            ParsingErrorSet.addSyntaxError(ERR_NULL_COMMENT, null, producer);
            return;
        }
        comment.trim();
        /* strip leading blanks */
        useTextFont();
        Rectangle2D bounds = cgc.getFont().getStringBounds(comment, fontRC);
        w = (int) bounds.getWidth() + 2;
        // not bad, but does it scale ??
        h = (int) bounds.getHeight();
        // new atempt...
        //	LineMetrics lm = cgc.getFont().getLineMetrics(comment, fontRC);
        //	h = lm.getHeight();

        // box tweaks
        int boxY = vpos + 4;
        int boxHeight = h - 1;

        advance(h); // leave room for boxes
        if (style == 1) { // in a gray box
            currentPage.setColor(commentBgColor);
            // tune this
            currentPage.fillRect(hpos, boxY, w, boxHeight);
            currentPage.setColor(Color.black);
            currentPage.drawString(comment, hpos, vpos);
        } else if (style == 2) { // in Italic ??
            currentPage.setFont(settings.getTextFontName(), Font.ITALIC, settings.getTextSize());
            currentPage.drawString(comment, hpos, vpos);
            useTextFont();
        } else if (style == 3) { // in a box
            currentPage.drawRect(hpos, boxY, w, boxHeight);
            currentPage.drawString(comment, hpos, vpos);
        }
    }
/* --------------------------------------------------------------------------------*/
    public void doEndOfColumn() {
        if (nColumns == (maxColumns - 1)) {
            doEndOfPage(false);
            doStartOfPage();
        } else {
            nColumns++;
            if (vpos > minColVpos)
                minColVpos = vpos;
            vpos = colVpos;
            hpos = margin + (nColumns * hOffset);
        }
    }
/* --------------------------------------------------------------------------------*/
    public void doEndOfPage(boolean forcePhysical)
/* Logical */ {
        int oTextSize;
        if ((songPages > 1) && title1Found) {
            oTextSize = settings.getTextSize();
            setTextFont(settings.getTextSize() - 2);
            useTextFont();
            currentPage.drawString(title1, (width - stringWidth(textFont, title1)) / 2, bottom + footerHeight);
            setTextFont(settings.getTextSize());
            setTextFont(oTextSize);
            useTextFont();
        }
        // signature handling
        if (sigImage != null) {
            currentPage.drawImage(sigImage, Math.max(width - sigImage.getWidth(optFrame), 0), top);
        } else if (sigText != null) {
            useTextFont();
            currentPage.drawString(sigText, Math.max((width - stringWidth(textFont, sigText)), 0), top);
        }

        if (numberLogical) {
            if (numberAll) {
                doPageNumbering(lpageLabel);
            } else if (songPages > 1) {
                doPageNumbering(songPages);
            }
        }
        if (context.inChorus) {
            endOfChorus = vpos;
            doChorusLine();
        }
        if ((vPages == settings.getPagination()) || forcePhysical) {
            doEndOfPhysPage();
            vPages = 0;
        }
        nColumns = 0;
        minColVpos = top;
        colVpos = top;
    }
/* --------------------------------------------------------------------------------*/
    public void doEndOfPhysPage()
/* physical */ {
        if (!numberLogical) {
            if (numberAll) {
                doPageNumbering(pageLabel);
            } else if (songPages > 1) {
                doPageNumbering(songPages);
            }
        }
        lpageLabel += settings.getPagination() - vPages;
    }
/* --------------------------------------------------------------------------------*/
    public void doEndOfSong() {
        useTextFont();
        if ((!settings.getLyricsOnly() && !settings.getNoGrids())) {
            if (vpos < minColVpos) {
                vpos = minColVpos;
            }
            drawChords();
        }
        doEndOfPage(false);
        songPages = 0;
        iInput = 0;
        inTab = false;
        title1 = "";

    }
/* --------------------------------------------------------------------------------*/
    public void doNewSong() {
        doEndOfSong();
        doStartOfSong();
    }
/* --------------------------------------------------------------------------------*/
    public void doPageNumbering(int pnum) {
        String s;
        s = "Page " + pageLabel;
        currentPage.drawLine(margin, bottom + footerHeight - 10, width, bottom + footerHeight - 10);
        useTextFont();
        if (((pageLabel % 2) == 0) && leftFootEven) {
            currentPage.drawString(s, margin, bottom + footerHeight);
        } else {
            currentPage.drawString(s, width - stringWidth(textFont, s), bottom + footerHeight);
        }
    }
/* --------------------------------------------------------------------------------*/
    public void doStartOfPage()
/*logical page ! */ {
        // System.out.println("SO doStartOfPage " + pj.toString());
        vPages++;
        lpageLabel++;
        initPage();
        vpos = top;
        minColVpos = top;
        hpos = margin;
        nColumns = 0;
        songPages++;
        setTextFont(settings.getTextSize());
        setChordFont(settings.getChordSize());
        /*28/4/94 ML */
        if (context.inChorus) {
            startOfChorus = vpos;
        }
    }
/* --------------------------------------------------------------------------------*/
    public void doStartOfSong() {
        /* reset default */
        settings.reset(CURRENT);
        doStartOfPage();
        knownChords.clean();
        cleanUsedChords();
        nLines = 1;
        setTextFont(settings.getTextSize());
        setChordFont(settings.getChordSize());
        nColumns = 0;
        maxColumns = 1;
        minColVpos = top;
    }
/* --------------------------------------------------------------------------------*/
    public void doSubtitle(String subTitle) {
        int x, w;

        //	setTextFont (curTextSize);
        useTextFont();
        w = stringWidth(textFont, subTitle);
        // x = ((width - margin - w) / 2 )  + margin;
        x = (width - w) / 2;
        currentPage.drawString(subTitle, x, vpos);
        vpos = vpos + settings.getTextSize();
        if (settings.getDoToc() && songPages == 1) {
            currentTocElement.getSubtitles().addElement(subTitle);
        }
    }
/* --------------------------------------------------------------------------------*/
    public void doTitle(String title) {
        int x, w;
        setTextFont(settings.getTextSize() + 5);
        useTextFont();
        w = stringWidth(textFont, title);
        // x = ((width - margin - w) / 2 )  + margin;
        x = (width - w) / 2;
        currentPage.drawString(title, x, vpos);
        vpos = vpos + settings.getTextSize() + 5;
        /* skip blanks */
        title = title.trim();
        title1 = new String(title);
        title1Found = true;
        if (settings.getDoToc() && songPages == 1) {

            /* generate index entry */
            currentTocElement = new TocElement(title, numberLogical ? lpageLabel : pageLabel);
            toc.put(title, currentTocElement);
        }
        setTextFont(settings.getTextSize() - 5);
        useTextFont();
    }
/* --------------------------------------------------------------------------------*/
    void doSubToc(TreeMap toc) {
        TreeMap subToc = new TreeMap();
        Iterator it = toc.keySet().iterator();
        String key;
        TocElement tocTe;
        Vector subs, tocElements;
        String aSub;


        while(it.hasNext()) {
            key = (String) it.next();
            tocTe = (TocElement) (toc.get(key));
            // for each subtitle
            subs = tocTe.getSubtitles();
            for (int i=0; i<subs.size(); i++) {
                aSub = (String)subs.elementAt(i);

            // fetch the subToc entry, create if necessary
                tocElements = (Vector)subToc.get(aSub);
                if (tocElements == null) {
                    tocElements = new Vector();
                    subToc.put(aSub, tocElements);
                }
                // add the tocElement as a sub
                tocElements.add(tocTe);
            }
        }

        // testing
//        it = subToc.keySet().iterator();
//        while (it.hasNext()) {
//            key = (String) it.next();
//            System.out.println(key);
//            tocElements = (Vector)subToc.get(key);
//            for (int i = 0; i< tocElements.size(); i++) {
//                tocTe = (TocElement)tocElements.elementAt(i);
//                System.out.println("   " + tocTe.getTitle() + "@" + tocTe.getLabel());
//            }
//        }
        // end of testing

        // Start of print
        int textSize = settings.getTextSize();

        if (vPages % settings.getPagination() != 0)
            doEndOfPage(true);
        if (settings.getPagination() == 4) {
            settings.setPagination(1);
        }
        vPages = 0;
        doStartOfPage();
        numberAll = false;
        songPages = 0;
        setTextFont(textSize + 10);
        useTextFont();
        currentPage.drawString("Subtitle Index", (width - stringWidth(textFont, "Subtitle Index")) / 2, top + 10);
        advance(textSize);
        it = subToc.keySet().iterator();
        while (it.hasNext()) {
            key = (String) it.next();
            tocElements = (Vector)subToc.get(key);

            if (vpos > bottom - 3 * textSize) {
                advance(vpos);
                songPages = 0;
            }
            advance(textSize + 8);
            setTextFont(textSize + 5);
            useTextFont();
            currentPage.drawString(key, 72, vpos);
            /* sub titles */
            tocElements = (Vector)subToc.get(key);
            for (int j = 0; j< tocElements.size(); j++) {
                tocTe = (TocElement)tocElements.elementAt(j);
                advance(textSize + 2);
                setTextFont(textSize);
                useTextFont();
                currentPage.drawString(tocTe.getTitle(), 108, vpos);
                currentPage.drawString(Integer.toString(tocTe.getLabel()), width - 50, vpos);
            }
        }
        vpos = vpos - textSize - 5;
        setTextFont(textSize);
    }
/* --------------------------------------------------------------------------------*/
    public void doToc(TreeMap tm) {

        int textSize = settings.getTextSize();

        TocElement te;
        int j;

        setSignatureObject(null);

        if (vPages % settings.getPagination() != 0)
            doEndOfPage(true);
        if (settings.getPagination() == 4) {
            settings.setPagination(1);
        }
        vPages = 0;
        doStartOfPage();
        numberAll = false;
        songPages = 0;
        setTextFont(textSize + 10);
        useTextFont();
        currentPage.drawString("Index", (width - stringWidth(textFont, "Index")) / 2, top + 10);
        advance(textSize);
        Iterator iter = tm.keySet().iterator();
        String key;
        while (iter.hasNext()) {
            key = (String) iter.next();
            te = (TocElement) (tm.get(key));

            /* title */
            if (vpos > bottom - 3 * textSize) {
                advance(vpos);
                songPages = 0;
            }
            advance(textSize + 8);
            setTextFont(textSize + 5);
            useTextFont();
            currentPage.drawString(te.getTitle(), 72, vpos);
            currentPage.drawString(Integer.toString(te.getLabel()), width - 50, vpos);

            /* sub titles */

            for (j = 0; j < te.getSubtitles().size(); j++) {
                advance(textSize + 2);
                setTextFont(textSize);
                useTextFont();
                currentPage.drawString((String) (te.getSubtitles().elementAt(j)), 108, vpos);
            }
        }
        vpos = vpos - textSize - 5;
        setTextFont(textSize);
    }
/* --------------------------------------------------------------------------------*/
    public void doTranslate(double vert, double horiz) {
        LOGGER.fine("changing translation");
    }
/*--------------------------------------------------------------------------------*/
    void drawChords() {
        KnownChord kc;
        useChordFont();

        moveto(width - 2 * settings.getGridSize() - margin, vpos);

        for (int i = 0; i < usedChords.size(); i++) {
            kc = (KnownChord) (usedChords.elementAt(i));
            if ((!noEasyGrids) || (noEasyGrids && (kc.getDifficulty() == 1))) {
                moveto(hpos + 2 * settings.getGridSize(), vpos);
                currentPage.drawChord(kc, hpos, vpos);
                //			kc.paint(cgc, hpos, vpos);
            }
        }
        useTextFont();
    }
/*--------------------------------------------------------------------------------*/
    /**
     * getNumberOfPages method comment.
     */
    public int getNumberOfPages() {
        return getPageSet().size();
    }
/*--------------------------------------------------------------------------------*/
    /**
     * getPageFormat method comment.
     */
    public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
        if (pageIndex >= getPageSet().size()) {
            throw new IndexOutOfBoundsException("Page " + pageIndex + " does not exists.");
        }
        return userPageFormat;
    }
/*--------------------------------------------------------------------------------*/
    /**
     * Insert the method's description here.
     * Creation date: (99-07-18 08:55:13)
     * @return com.scvn.chord4.grop.PageSet
     */
    public PageSet getPageSet() {
        if (pageSet == null) {
            pageSet = new PageSet();
        }

        return pageSet;
    }
/*--------------------------------------------------------------------------------*/
    /**
     * getPrintable method comment.
     */
    public Printable getPrintable(int pageIndex) throws java.lang.IndexOutOfBoundsException {
        if (pageIndex >= getPageSet().size()) {
            throw new IndexOutOfBoundsException("Page " + pageIndex + " does not exists.");
        }
        return this;
    }
/*--------------------------------------------------------------------------------*/
    /**
     * Insert the method's description here.
     * Creation date: (00-05-14 08:29:21)
     * @return java.awt.print.PrinterJob
     */
    public PrinterJob getPrintJob() {
        return pj;
    }
/*--------------------------------------------------------------------------------*/
    /**
     * Insert the method's description here.
     * Creation date: (00-05-14 08:22:39)
     * @return java.awt.print.PageFormat
     */
    public PageFormat getUserPageFormat() {
        return userPageFormat;
    }
/* --------------------------------------------------------------------------------*/
    void initPage()
/* common to doStartOfPage and to  initPs */ {
        if (vPages == 1) {
            openPage();
            nPages++;
            pageLabel++;
            if (settings.getPagination() > 1) {
            }
        }
        /*
        if (pagination == 4)
        {
                if (vPages == 1)
                        doTranslate(margin / 2, height + 3 * (bottom) / 4);
                else
                        if (vPages == 2)
                                doTranslate(width, 0.0);
                        else
                                if (vPages == 3)
                                        doTranslate(-width, -height);
                                else
                                        if (vPages == 4)
                                                doTranslate(width, 0.0);
        }
        if (pagination == 2)
                if (vPages == 1)
                {
                        doTranslate((bottom) / 2, - (height + (width - height) / 2));
                }
                else
                        if (vPages == 2)
                                doTranslate(width, 0.0);
         */
    }

/*--------------------------------------------------------------------------------*/
    /**
     * Insert the method's description here.
     * Creation date: (99-07-25 18:34:42)
     */
    public void invalidateRendering() {
        renderingDone = false;
    }
/*--------------------------------------------------------------------------------*/
    /**
     * Insert the method's description here.
     * Creation date: (99-07-25 18:33:57)
     */
    public void validateRendering(Graphics2D g) {
        setGraphics(g);
        if (!isRenderingDone()) {
            c4.runParser();
        }
        renderingDone = true;
    }

/*--------------------------------------------------------------------------------*/
    /**
     * Insert the method's description here.
     * Creation date: (99-07-25 18:35:22)
     * @return boolean
     */
    public boolean isRenderingDone() {
        return renderingDone;
    }
/*--------------------------------------------------------------------------------*/
    void moveto(int newHpos, int newVpos) {
        if (newHpos + settings.getGridSize() + margin > width) {
            newHpos = margin + 3;
            newVpos += 2 * settings.getGridSize();
        }

        if (newVpos + 1 * settings.getGridSize() > bottom) {
            doEndOfPage(false);
            doStartOfPage();
            newVpos = top;
        }

        hpos = newHpos;
        vpos = newVpos;
    }
/* --------------------------------------------------------------------------------*/
    void openPage() {
        //LOGGER.fine("renderer Creating page");
        currentPage = new Page();
        // PrintGraphics do not have a preset font
        useTextFont();
        getPageSet().addPage(currentPage);
        currentPage.setColor(Color.black);
        useTextFont();
    }
/*--------------------------------------------------------------------------------*/
    /**
     * Insert the method's description here.
     * Creation date: (99-07-17 17:05:58)
     * @param g java.awt.Graphics
     * @param pf java.awt.print.PageFormat
     * @param pagenum int
     */
    public int print(Graphics g, PageFormat pf, int pagenum) {
        // not on screen
        //        LOGGER.fine("Renderer.print:" +pagenum);
        Graphics2D g2d = (Graphics2D) g;
        Page p = getPageSet().getPage(pagenum);
        if (p == null) {
            return Printable.NO_SUCH_PAGE;
        } else {
            // workaround for color init problem in jdk 1.2.2
            g2d.setColor(Color.white);
            g2d.drawLine(0, 0, 0, 0);
            // end of workaround
            g2d.setColor(Color.black);
            p.render(g2d);
            return Printable.PAGE_EXISTS;
        }
    }

    void drawTextAndChords(String textLine, Vector chordLine) {
        int nextChord = 0;
        Object o;
        int chordVpos = vpos + settings.getChordSize();
        int textVpos = chordVpos + settings.getTextSize();
        int chordWidth;
        int currentHpos = hpos;
        int prevChord = -1;
        int i;
        String aChord, subText;

        // ensure equality of length
        while (textLine.length() < chordLine.size()) {
            textLine += " ";
        }
        while (chordLine.size() <  textLine.length()) {
            chordLine.addElement(null);
        }
        // done !

        for (i = 0; i < chordLine.size(); i++) {
            o = chordLine.elementAt(i);

            if (o == null) {
                continue;
            }

            if ((o instanceof KnownChord)) {
                aChord = ((KnownChord) o).getChordName();
            } else {
                aChord = (String) o;
            }
            aChord += " ";

            // if first chord found, flush text first
            if (prevChord == -1) {
                prevChord = i;
                useTextFont();
                subText = textLine.substring(0, i);
                currentPage.drawString(subText, currentHpos, textVpos);
                currentHpos += stringWidth(textFont, subText);
            }

            // find next chord
            for (nextChord=i+1;
                 (nextChord < chordLine.size() && chordLine.elementAt(nextChord) == null);
                 nextChord++) {
                // just loop
            }
            chordWidth = stringWidth(chordFont, aChord);
            subText = textLine.substring(prevChord, nextChord);
            while (stringWidth(textFont, subText) < chordWidth) {
                subText += " ";
            }
            // draw both
            useChordFont();
            currentPage.drawString(aChord, currentHpos, chordVpos);
            useTextFont();
            currentPage.drawString(subText, currentHpos, textVpos);

            // set new hpos
            currentHpos += stringWidth(textFont, subText);
            prevChord = nextChord;
        }
        // flush remaining text
        if (prevChord == -1) { // no chord at all on this line !
            prevChord = 0;
        }

        useTextFont();
        currentPage.drawString(textLine.substring(prevChord), currentHpos, textVpos);

    }

/* --------------------------------------------------------------------------------*/
    public void printTabLine(String textLine) {
        advance(settings.getTextSize() - 1);
        if (textLine.length() != 0) {
            useMonoFont();
            currentPage.drawString(textLine, hpos, vpos);
        }
    }
/* --------------------------------------------------------------------------------*/
    public void renderTextAndChords(String textLine, Vector chordLine) {
        int textSize = settings.getTextSize();
        int chordSize = settings.getChordSize();

        // do we need a new page first ??
        int needed = 0;
        if (! settings.getLyricsOnly() && context.hasChord) { // space for chords
            needed += chordSize;
        }
        if (textLine.length() != 0) {
            needed += textSize + 2;
        }
        if (vpos + needed > bottom) {
            advance(needed);
        }
        // OK for space left on page

        if (context.needSOC) {
            startOfChorus = vpos;
            context.needSOC = false;
        }

        // case 1: lyrics and chord
        // case 2: lyrics only
        if (settings.getLyricsOnly() || (textLine.length() == 0 && chordLine.size() == 0)) {
// just spew out the line
            advance(textSize - 1);
            useTextFont();
            currentPage.drawString(textLine, hpos, vpos);
        } else {


            if (context.needSOC) {
                startOfChorus = vpos; //last
                context.needSOC = false;
            }
            drawTextAndChords(textLine, chordLine);
            advance(chordSize);
            advance(textSize + 4);
        }
        chordLine = new Vector();
        context.hasChord = false;
    }
/* --------------------------------------------------------------------------------*/
    void setChordFont(int size) {
//        if ((size != settings.getChordSize()) || chordFont == null) {
          if (chordFont == null || (size != chordFont.getSize() || settings.getChordSize() != chordFont.getSize()) ) {
            chordFont = new Font(settings.getChordFontName(), Font.ITALIC, size);
            settings.setChordSize(size);
        } else {
//           LOGGER.fine("Chord size KEPT at " +size);
        }
    }
/* --------------------------------------------------------------------------------*/
    public void setColumns(int i) {
        maxColumns = i;
        nColumns = 0;
        colVpos = vpos;
        hOffset = ((width - margin) / maxColumns);
    }
/* --------------------------------------------------------------------------------*/
    public void setContext(Context context) {
        this.context = context;
    }
/* --------------------------------------------------------------------------------*/
    public void setEndOfChorus() {
        endOfChorus = vpos;
        doChorusLine();
        context.inChorus = false;
    }
/*--------------------------------------------------------------------------------*/
    /**
     * Insert the method's description here.
     * Creation date: (99-07-18 11:51:31)
     * @param g java.awt.Graphics2D
     */
    public void setGraphics(Graphics2D g) {
        cgc = g;
        fontRC = cgc.getFontRenderContext();
    }
/* --------------------------------------------------------------------------------*/
    void setMonoFont(int size) {
        if (monoFont == null || (size != monoFont.getSize() || settings.getMonoSize() != monoFont.getSize()) ) {
            monoFont = new Font(settings.getMonoFontName(), Font.PLAIN, size);
            settings.setMonoSize(size);
        }
    }
/*--------------------------------------------------------------------------------*/
    /**
     * Insert the method's description here.
     * Creation date: (00-05-14 08:29:21)
     * @param newPj java.awt.print.PrinterJob
     */
    public void setPj(java.awt.print.PrinterJob newPj) {
        pj = newPj;
    }
/*--------------------------------------------------------------------------------*/
    /**
     * Insert the method's description here.
     * Creation date: (00-05-14 11:44:00)
     * @param newRenderingDone boolean
     */
    public void setRenderingDone(boolean newRenderingDone) {
        renderingDone = newRenderingDone;
    }
/*--------------------------------------------------------------------------------*/
    /**
     * Insert the method's description here.
     * Creation date: (99-11-19 19:53:39)
     * @param arg java.lang.String
     */
    public void setSignatureObject(Object arg) {
        if (arg == null) {
            sigText = null;
            sigImage = null;
            return;
        }
        if (arg instanceof URL) {
            // get the image
            sigImage = Toolkit.getDefaultToolkit().getImage((URL) arg);
            sigText = null;
        } else if (arg instanceof Image) {
            sigImage = (Image) arg;
            sigText = null;
        } else if (arg instanceof String) {
            String s = (String) arg;
            if ((new File(s)).isAbsolute()) {
                sigImage = Toolkit.getDefaultToolkit().getImage(s);
                sigText = null;
            } else {
                sigImage = null;
                sigText = (String) arg;
            }
        }

        // check that the image is load is reference is not null
        if (sigImage != null && sigImage.getWidth(optFrame) < 0) {
            MediaTracker mt = new MediaTracker(optFrame);
            mt.addImage(sigImage, 1);
            try {
                mt.waitForAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

/*
        if (signature != null && arg.equals(signature))
        {
        return;
        }
        signature = arg;
        sigImage = null;
        // signature may be : URL, full path, or string.
        String url = null;
        // if path, build url
        System.out.println("Signature=[" + signature + "]");
        if ((new File(signature)).isAbsolute())
        {
        url = "file:///" + signature;
        System.out.println("is a file. now Signature=[" + signature + "]");
        }
        // attempt URL access
        try
        {
        sigImage = Toolkit.getDefaultToolkit().getImage(new URL(url));
        MediaTracker mt = new MediaTracker(optFrame);
        mt.addImage(sigImage, 1);
        mt.waitForAll();
        System.out.println("URL loaded");
        }
        catch (MalformedURLException mue)
        {
        System.out.println("Bad URL");
        // do nothing. signature will be used as a string
        // because sigImage was left null
        }
        catch (Exception e)
        {
        e.printStackTrace();
        }
 */
    }
/* --------------------------------------------------------------------------------*/
    void setTextFont(int size) {
        if (textFont == null || (size != textFont.getSize() || settings.getTextSize() != textFont.getSize()) ) {
            settings.setTextSize(size);
            textFont = new Font(settings.getTextFontName(), Font.PLAIN, size);
        }
    }
/* --------------------------------------------------------------------------------*/
    void setTextFontName(String name) {
        if ((!name.equals(settings.getTextFontName())) || textFont == null) {
            settings.setTextFontName(name);
            textFont = new Font(name, Font.PLAIN, settings.getTextSize());
        }
    }
/*--------------------------------------------------------------------------------*/
    /**
     * Insert the method's description here.
     * Creation date: (99-07-25 17:48:35)
     * @param pf java.awt.print.PageFormat
     */
    public void setUserPageFormat(PageFormat pf) {
        setUserPageFormat(pf, true);
    }

/*--------------------------------------------------------------------------------*/
    public void setUserPageFormat(PageFormat pf, boolean invalidate) {
        userPageFormat = pf;
        // leave room for title and grids
        // room for title
        top = (int) pf.getImageableY() + titleHeight;
        clipx = (int) pf.getImageableX();
        margin = clipx + 8;

        width = (int) pf.getImageableWidth();

        // derivations
        // bottom triggers the page footer. that is positioned at bottom+20
        // see doPageNumbering
        bottom = (int) (pf.getImageableY() + pf.getImageableHeight() - titleHeight - footerHeight);
        vpos = top;
        hpos = margin;
        // ML
        if (invalidate) {
            invalidateRendering();
        }
    }
/* --------------------------------------------------------------------------------*/
    public void start() {
        pageLabel = 0; // physical page number
        lpageLabel = 1; // logical page number
        hOffset = 0; // horizontal offset for multi-col output
        nPages = 0; // total physical page counter
        vPages = 0; // virtual pages
        nLines = 1; // line number in input file
        maxColumns = 1; // number of columns
        nColumns = 0; // counter of columns
        songPages = 0; // song page counter
        transpose = 0;
        titleHeight = 36;
        footerHeight = 15;
        numberAll = true;

        usedChords = new ChordVector();
        getPageSet().removeAllElements();
// setup toc
        LOGGER.fine("set up toc");
        Collator coll = Collator.getInstance();
        coll.setStrength(Collator.SECONDARY);
        toc = new TreeMap(coll);
        /* complete some derivations after parsing options */

        if (userPageFormat == null) {
            LOGGER.fine("No page format set !");
            return;
        }

        nColumns = 0;
        LOGGER.fine("init KnownChord");
        //KnownChord.init(settings.getGridSize());


        /* File Processing */
        LOGGER.fine("initPage");
        initPage();
        chordFont = null;
        textFont = null;
        monoFont = null;
        LOGGER.fine("reset settings");

        settings.reset(CURRENT);

        setChordFont(settings.getChordSize());
        setTextFont(settings.getTextSize());
        setMonoFont(settings.getTextSize());
        chordInc = settings.getChordSize() * 1.5;
        LOGGER.fine("invalidateRendering");
        invalidateRendering();
    }
/*--------------------------------------------------------------------------------*/
    public int stringWidth(Font f, String s) {
        return (int) (f.getStringBounds(s, fontRC).getWidth());
    }
/* --------------------------------------------------------------------------------*/
    void useChordFont() {
        // check integrity, since settings can be changed by directives.
        if (chordFont.getSize() != settings.getChordSize()) {
            setChordFont(settings.getChordSize());
        }
        cgc.setFont(chordFont);
        currentPage.setFont(chordFont);
    }
/* --------------------------------------------------------------------------------*/
    void useMonoFont() {
        // check integrity, since settings can be changed by directives.
        if (monoFont.getSize() != settings.getMonoSize()) {
            setMonoFont(settings.getMonoSize());
        }
        cgc.setFont(monoFont);
        currentPage.setFont(monoFont);
    }
/* --------------------------------------------------------------------------------*/
    void useTextFont() {
        // check integrity, since settings can be changed by directives.
        if (textFont.getSize() != settings.getTextSize()) {
            setTextFont(settings.getTextSize());
        }
        currentPage.setFont(textFont);
        cgc.setFont(textFont);
    }
/* --------------------------------------------------------------------------------*/
    public void setCurrentPage(int i) {
        rFrame.setCurrentPage(i);
    }
/* --------------------------------------------------------------------------------*/
    public void setVisible(boolean v) {
        rFrame.setVisible(v);
        rFrame.setCurrentPage(0);
    }

    public RenderingFrame getRenderingFrame() {
        return rFrame;
    }
}
