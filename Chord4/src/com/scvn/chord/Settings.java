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
 * RcOptions.java
 *
 * Created on December 8, 2001, 8:51 AM
 */

package com.scvn.chord;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
/**
 *
 * @author  martin
 * @version
 */
public class Settings extends Observable implements ChordConstants {
    
    private Properties optSets, defaultProps;
    private static final Logger LOGGER =
	        Logger.getLogger(Chord4.class.getName());
    
    public Settings() {
    	optSets = new Properties();
    	FileInputStream in = null;
    	// create application properties with default
        setSystemSettings();
        defaultProps = optSets;
        optSets = new Properties(defaultProps);
        
     // now load properties from last invocation
    	try {
			in = new FileInputStream("chordProperties");
			optSets.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("SYSTEM:"+optSets.get(SYSTEM));
        sb.append("\nRC:"+optSets.get(RC));
        sb.append("\nRT:"+optSets.get(RT));
        sb.append("\nCURRENT:"+optSets.get(CURRENT));
        return sb.toString();
    }
    
    void setSystemSettings() {
        set(SYSTEM,  TEXTSIZE, DefTextSize, false);
        set(SYSTEM,  MONOSIZE, DefMonoSize, false);
        set(SYSTEM,  CHORDSIZE, DefChordSize, false);
        set(SYSTEM,  TEXTFONTNAME, DefTextFontName, false);
        set(SYSTEM,  CHORDFONTNAME, DefChordFontName, false);
        set(SYSTEM,  MONOFONTNAME, DefMonoFontName, false);
        
        set(SYSTEM,  GRIDSIZE, DefGridSize, false);
        set(SYSTEM,  NOGRIDS, DefNoGrids, false);
        set(SYSTEM,  SHOWSPLASH, DefShowSplash, false);
        set(SYSTEM,  NOEASYGRIDS, DefNoEasyGrids, false);
        
        set(SYSTEM,  TRANSPOSITION, DefTransposition, false);
        set(SYSTEM,  PAGINATION, DefPagination, false);
        set(SYSTEM,  SIGNATUREOBJECT, DefSignatureObject, false);
        set(SYSTEM,  DOTOC, DefDoToc, false);
        set(SYSTEM,  DOSUBTOC, DefDoSubToc, false);
        set(SYSTEM,  ISLEFTFOOTEVEN, DefIsLeftFootEven, false);
        set(SYSTEM,  ISLISTOFFILES, DefIsListOfFiles, false);
        set(SYSTEM,  ISLYRICSONLY, DefIsLyricsOnly, false);
        set(SYSTEM,  NUMBERLOGICAL, DefNumberLogical, false);
        set(SYSTEM,  FIRSTPAGELABEL, DefFirstPageLabel, false);
        
    }
    
    private String get(String src, String key) {
        //Hashtable srcHash = (Hashtable)(optSets.get(src));
        //return (String)(srcHash.get(key));
    	return (optSets.getProperty(src + "." + key));
    }
    public void save() {
    	FileOutputStream out;
		try {
			out = new FileOutputStream("chordProperties");
			optSets.store(out, "---No Comment---");
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
//    public void set(String src, String key, String val) {
//        set(src, key, val, true);
//    }
    public void set(String src, String key, String val, boolean notify) {
        LOGGER.fine("seetings.set hash="+src+"  key="+key + " val="+val);
        //Hashtable srcHash = (Hashtable)(optSets.get(src));
        optSets.setProperty(src + '.' + key, val);
        //srcHash.put(key, val);
        if (notify && src != CURRENT) { // Changes in settings local to a song need not be propagated
            setChanged();
            notifyObservers(key);
            clearChanged();
        }
    }
    
    public void set(String key, String val) {
        set(CURRENT, key, val, true);
    }
    
    public String get(String key) {
        // go through the tree
        String res = null;
        res = get(CURRENT, key);
        if (res != null) {
            return res;
        }
        res = get(RT, key);
        if (res != null) {
            return res;
        }
        res = get(RC, key);
        if (res != null) {
            return res;
        }
        return get(SYSTEM, key);
    }
    
    public void reset(String src) {
        //Hashtable srcHash = (Hashtable)(optSets.get(src));
        //srcHash.clear();
    }
    public String getTextFontName() {
        return get(TEXTFONTNAME);
    }
    public String getChordFontName() {
        return get(CHORDFONTNAME);
    }
    public String getMonoFontName() {
        return get(MONOFONTNAME);
    }
    
    public int getTextSize() {
        return Integer.parseInt(get(TEXTSIZE));
    }
    public int getChordSize() {
        return Integer.parseInt(get(CHORDSIZE));
    }
    public int getMonoSize() {
        return Integer.parseInt(get(MONOSIZE));
    }
    public int getGridSize() {
    	//LOGGER.fine("enter getGridSize");
    	//LOGGER.fine("GRIDSIZE=" + get(GRIDSIZE));
        return Integer.parseInt(get(GRIDSIZE));
    }
    public boolean getNoGrids() {
        return Boolean.valueOf(get(NOGRIDS)).booleanValue();
    }
    public boolean getShowSplash() {
    	return Boolean.valueOf(get(SHOWSPLASH)).booleanValue();
    }
    public boolean getNoEasyGrids() {
        return Boolean.valueOf(get(NOEASYGRIDS)).booleanValue();
    }
    public boolean getDoToc() {
        return Boolean.valueOf(get(DOTOC)).booleanValue();
    }
    public boolean getDoSubToc() {
        return Boolean.valueOf(get(DOSUBTOC)).booleanValue();
    }
    public boolean getIsLeftFootEven() {
        return Boolean.valueOf(get(ISLEFTFOOTEVEN)).booleanValue();
    }
    public boolean getLyricsOnly() {
        return Boolean.valueOf(get(ISLYRICSONLY)).booleanValue();
    }
    public boolean getNumberLogical() {
        return Boolean.valueOf(get(NUMBERLOGICAL)).booleanValue();
    }
    public boolean getIsListOfFiles() {
        return Boolean.valueOf(get(ISLISTOFFILES)).booleanValue();
    }
    public int getFirstPageLabel() {
        return Integer.parseInt(get(FIRSTPAGELABEL));
    }
    public int getTransposition() {
        return Integer.parseInt(get(TRANSPOSITION));
    }
    public int getPagination() {
        return Integer.parseInt(get(PAGINATION));
    }
    public void setTextSize(int size) {
        set(TEXTSIZE, Integer.toString(size));
    }
    public void setMonoSize(int size) {
        set(MONOSIZE, Integer.toString(size));
    }
    public void setChordSize(int size) {
        set(CHORDSIZE, Integer.toString(size));
    }
    public void setGridSize(int size) {
        set(GRIDSIZE, Integer.toString(size));
    }
    public void setPagination(int size) {
        set(PAGINATION, Integer.toString(size));
    }
    public void setNoGrids(boolean b) {
        set(NOGRIDS, (new Boolean(b)).toString());
    }
    public void setIsListOfFiles(boolean b) {
        set(ISLISTOFFILES, (new Boolean(b)).toString());
    }
    public void setDoToc(boolean b) {
        set(DOTOC, (new Boolean(b)).toString());
    }
    public void setTextFontName(String name) {
        set(TEXTFONTNAME, name);
    }
    public void setChordFontName(String name) {
        set(CHORDFONTNAME, name);
    }
    public void setShowSplash(boolean b) {
    	set(SHOWSPLASH, (new Boolean(b)).toString());
    }
}
