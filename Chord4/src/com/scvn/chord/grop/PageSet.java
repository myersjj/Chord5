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

import java.util.logging.Logger;

import com.scvn.chord.Chord4;

/**
 * Insert the type's description here.
 * Creation date: (99-07-17 17:07:55)
 * @author
 */
public class PageSet extends java.util.Vector {
	private static final Logger LOGGER =
	        Logger.getLogger(Chord4.class.getName());
    /**
     * PageSet constructor comment.
     */
    public PageSet() {
        super();
        LOGGER.fine("New page set created");
    }
    /**
     * PageSet constructor comment.
     * @param initialCapacity int
     */
    public PageSet(int initialCapacity) {
        super(initialCapacity);
        LOGGER.fine("New page set created");
    }
    /**
     * PageSet constructor comment.
     * @param initialCapacity int
     * @param capacityIncrement int
     */
    public PageSet(int initialCapacity, int capacityIncrement) {
        super(initialCapacity, capacityIncrement);
        LOGGER.fine("New page set created");
    }
    /**
     * PageSet constructor comment.
     * @param c java.util.Collection
     */
    public PageSet(java.util.Collection c) {
        super(c);
        LOGGER.fine("New page set created");
    }
    /**
     * Insert the method's description here.
     * Creation date: (99-07-17 17:11:22)
     * @param page com.scvn.chord4.grop.Page
     */
    public void addPage(Page page) {
        addElement(page);
    }
    /**
     * Insert the method's description here.
     * Creation date: (99-07-17 17:09:22)
     * @return com.scvn.chord4.grop.Page
     * @param pageNum int
     */
    public Page getPage(int pageNum) {
        if (pageNum < size()) {
            return (Page)(elementAt(pageNum));
        }
        return null;
    }
    
    public void removeAllElements()
    {
        super.removeAllElements();
        LOGGER.fine("Page set flushed");
    }
}
