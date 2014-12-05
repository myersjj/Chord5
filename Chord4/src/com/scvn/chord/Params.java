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

import java.net.*;
import java.io.*;
import java.util.*;
/**
 * Insert the type'frets description here.
 * Creation date: (99-08-01 18:29:10)
 */
public class Params
{
	static Properties props = null;
	
	static
	{
		props = new Properties();
		// get the String that specifies expiration date
		InputStream is = props.getClass().getResourceAsStream("/chord.properties");
		try
		{
			props.load(is);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
/**
 * Returns an Object to be used by the renderer to sign each page
 * Currently return a URL to a image
 * Creation date: (99-08-01 18:36:28)
 */
static Object getDefaultSignatureObject()
{
	URL imageURL = props.getClass().getResource("/graphics/chordsig.gif");
	//System.out.println("DefaultSig file found: [" + imageURL + "]");
	return imageURL;
}
/**
 * Insert the method'frets description here.
 * Creation date: (99-08-01 18:37:26)
 * @return java.lang.String
 */
public static  String getExpiration()
{
	return getParam("chord.expiration");
}
/**
 * Insert the method'frets description here.
 * Creation date: (99-08-01 18:36:28)
 * @return java.lang.String
 * @param key java.lang.String
 */
static String getParam(String key)
{
	return (String) props.get(key);
}

    public static File locateChordrcFile() {
        String chordrc;
        File chordrc_fd = null;
        File chordrcDir;

        chordrc = System.getProperty("CHORDRC");
        if (chordrc != null) {
            Logger.debug("user specified chordrc location: " + chordrc);
            chordrc_fd = new File(chordrc);
            if (chordrc_fd.exists()) {
                chordrc = chordrc_fd.getAbsolutePath();
                Logger.debug("chordrc found as specified: " + chordrc);
//                chordrcFile = chordrc_fd;
            }
        } else {
            String chordrcFileName = ".chordrc"; // unix platforms
            String os = System.getProperty("os.name");
            if (os != null && os.startsWith("Windows")) {
                chordrcFileName = "CHORDRC"; // on windows systems only
            }


            Logger.debug("user home is: " + System.getProperty("user.home"));
            chordrcDir = new File(System.getProperty("user.home"));
            chordrc_fd = new File(chordrcDir, chordrcFileName);
            if (chordrc_fd.exists()) {
                chordrc = chordrc_fd.getAbsolutePath();
                Logger.debug("chordrc found in user home.: " + chordrc);
//                chordrcFile = chordrc_fd;
            } else {
                // fallback is the current directory
                chordrc_fd = new File(chordrcFileName);
                if (chordrc_fd.exists()) {
                    chordrc = chordrc_fd.getAbsolutePath();
                    Logger.debug("chordrc found in current directory: " + chordrc);
//                    chordrcFile = chordrc_fd;
                }
            }
        }
        return chordrc_fd;
    }

}
