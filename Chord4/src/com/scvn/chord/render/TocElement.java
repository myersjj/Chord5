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

import java.util.*;
/* --------------------------------------------------------------------------------*/

/* utility clases */
class TocElement {
    private Vector subTitles;
    private String title;
    private int pageLabel;
    private int nbPages;
    public TocElement(String title, int p) {
        this.title = new String(title);
        pageLabel = p;
        subTitles = new Vector(1,1);
    }
    public int getLabel() {
        return pageLabel;
    }
    public int getNbPages() {
        return nbPages;
    }
    public void setNbPages(int nb) {
        nbPages = nb;
    }
    public Vector getSubtitles() {
        return subTitles;
    }
    public String getTitle() {
        return title;
    }
}
