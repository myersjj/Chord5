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
 
package com.scvn.chord.grop;

import com.scvn.chord.*;
import java.awt.*;

/**
 * Insert the type's description here.
 * Creation date: (99-07-17 16:44:27)
 * @author Martin Leclerc
 */
public class Page extends java.util.Vector {
    /**
     * Page constructor comment.
     */
    public Page() {
        super();
    }
    /**
     * Page constructor comment.
     * @param initialCapacity int
     */
    public Page(int initialCapacity) {
        super(initialCapacity);
    }
    /**
     * Page constructor comment.
     * @param initialCapacity int
     * @param capacityIncrement int
     */
    public Page(int initialCapacity, int capacityIncrement) {
        super(initialCapacity, capacityIncrement);
    }
    /**
     * Page constructor comment.
     * @param c java.util.Collection
     */
    public Page(java.util.Collection c) {
        super(c);
    }
    /**
     * Insert the method's description here.
     * Creation date: (99-07-17 17:59:15)
     * @param kc KnownChord
     * @param x int
     * @param y int
     */
    public void drawChord(KnownChord kc, int x, int y) {
        add(new ChordDrawer(kc, x, y));
    }

    /**
     * Insert the method's description here.
     * Creation date: (99-07-17 17:53:39)
     * @param x1 int
     * @param y1 int
     * @param x2 int
     * @param y2 int
     */
    public void drawImage(int x1, int y1, int x2, int y2) {
        add(new LineDrawer(x1, y1, x2, y2));
    }
    /**
     * Insert the method's description here.
     * Creation date: (99-07-17 17:53:39)
    */
    public void drawImage(Image im, int x, int y) {
        add(new ImageDrawer(im, x, y));
    }
    /**
     * Insert the method's description here.
     * Creation date: (99-07-17 17:53:39)
     * @param x1 int
     * @param y1 int
     * @param x2 int
     * @param y2 int
     */
    public void drawLine(int x1, int y1, int x2, int y2) {
        add(new LineDrawer(x1, y1, x2, y2));
    }
    /**
     * Insert the method's description here.
     * Creation date: (99-07-17 17:53:39)
     */
    public void drawRect(int x, int y, int w, int h) {
        add(new RectDrawer(x, y, w, h));
    }
    /**
     * Insert the method's description here.
     * Creation date: (99-07-17 17:59:15)
     * @param s java.lang.String
     * @param x int
     * @param y int
     */
    public void drawString(String s, int x, int y) {
        // Mac 1.3 JVM seems to have a problem with zero length String
        // when the StringDrawer.apply() is called
        if (s != null && s.length() > 0) {
          add(new StringDrawer(s, x, y));
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (99-07-17 18:36:33)
     * @param x int
     * @param y int
     * @param w int
     * @param h int
     */
    public void fillRect(int x, int y, int w, int h) {
        add(new RectFiller(x, y, w, h));
    }
    /**
     * Insert the method's description here.
     * Creation date: (99-07-17 16:47:24)
     * @param g java.awt.Graphics
     */
    public void render(java.awt.Graphics g) {
        //Logger.debug("Page rendering itself");
        for (int i = 0; i < size(); i++) {
            ((GraphicsOperation) (elementAt(i))).apply(g);
            //Logger.debug("grop:"+elementAt(i));
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (99-07-17 18:35:03)
     * @param c java.awt.Color
     */
    public void setColor(java.awt.Color c) {
        add(new ColorSetter(c));
    }
    /**
     * Insert the method's description here.
     * Creation date: (99-07-17 18:35:03)
     */
    public void setFont(Font f) {
        add(new FontSetter(f));
    }
    /**
     * Insert the method's description here.
     * Creation date: (99-07-17 18:35:03)
     */
    public void setFont(String name, int style, int size) {
        add(new FontSetter(name, style, size));
    }
}
