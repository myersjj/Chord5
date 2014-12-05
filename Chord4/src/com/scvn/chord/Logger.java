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
 * Logger.java
 *
 * Created on December 9, 2001, 4:43 PM
 */

package com.scvn.chord;

/**
 *
 * @author  martin
 * @version 
 */
public class Logger {

    static boolean debug = false;
    static boolean info = false;

    /** Creates new Logger */
    public Logger() {
    }
    
    static public void debug(String s)
    {
        if (debug) System.out.println(s);
    }
    
    static public void debug (Object o, String s)
    {
        debug (o.getClass().getName() + ": " + s);
    }
    static public void info(String s)
    {
        if (info) System.out.println(s);
    }

    static public void info (Object o, String s)
    {
        info(o.getClass().getName() + ": " + s);
    }

    public static void setDebug(boolean debug) {
        Logger.debug = debug;
    }

    public static void setInfo(boolean info) {
        Logger.info = info;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static boolean isInfo() {
        return info;
    }
}
