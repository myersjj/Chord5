package com.scvn.chord.util;

import javax.swing.text.*;
import java.awt.*;


public class ColorView extends PlainView {
    final static int S_DEF = 0;
    Segment line;
    final static int S_CHORD = 1;
    final static int S_DIRECTIVE = 2;
    final static int S_COMMENT = 3;

    public ColorView(Element e) {
        super(e);
        line = new Segment();
    }

    protected void drawLine(int lineIndex, Graphics g, int x, int y) {
        try {
            Element lineElement = getElement().getElement(lineIndex);
            int start = lineElement.getStartOffset();
            int end = lineElement.getEndOffset();
            JTextComponent host = (JTextComponent) getContainer();

        // used to compute selected area
            int startSel  = host.getSelectionStart();
            int endSel = host.getSelectionEnd();
            int p0 = lineElement.getStartOffset();
            Color selected =  host.getSelectedTextColor();

            // this puts the entire line we are typing in into the segment
            getDocument().getText(start, end - (start + 1), line);
            int state = S_DEF;
            // store original value
            int len = line.count;
            // make all substrings of length 1
            line.count = 1;
            char c;
            for (int idx = 0; idx < len; idx++) {
                c = line.array[line.offset];
                if (c == '#' && idx == 0) {
                    state = S_COMMENT;
                } else if (c == '[' && state == S_DEF) {
                    state = S_CHORD;
                } else if (c == '{' && state == S_DEF) {
                    state = S_DIRECTIVE;
                }
                // else keep current state

                switch (state) {
                    case S_DEF:
                        g.setColor(Color.darkGray);
                        break;
                    case S_DIRECTIVE:
                        g.setColor(Color.blue);
                        break;
                    case S_CHORD:
                        g.setColor(Color.red);
                        break;
                    case S_COMMENT:
                        g.setColor(Color.magenta);
                        break;
                }

                // override if current pos is within selected block
                if (p0 + idx > startSel && p0 + idx < endSel) {
                    g.setColor(selected);
                }

                // Utilities is a standard swing class
                x = Utilities.drawTabbedText(line, x, y, g, this, idx);
                line.offset++;

                if (c == ']' && state == S_CHORD) {
                    state = S_DEF;
                } else if (c == '}' && state == S_DIRECTIVE) {
                    state = S_DEF;
                }
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
