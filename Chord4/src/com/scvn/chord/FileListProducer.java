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

import java.io.*;
public class FileListProducer implements Producer {
    String list;
    File currentFile;
    DataInputStream dis;
    BufferedReader bis, currentFileReader, currentListReader;
    int lineNumber = 0;
    int index = 0;
    String currentLine, currentFileName;
    String delimiter = System.getProperty("line.separator");


    public FileListProducer(String s) {
        setFileList(s);
    }
    
    public int getChar() {
        if (currentFileReader == null) {
            currentFileReader = openNextFile();
            if (currentFileReader == null) {
                return(Producer.EOJ);
            }
            else {
                lineNumber = 0;
                return (Producer.SOF);
            }
        }
        // if here, we have a file opened
        try {
            if (currentLine == null) {
                currentLine = currentFileReader.readLine();
//                Logger.debug("Producer.currentLine:<" + currentLine + ">");
                if (currentLine == null) {
                    currentFileReader.close();
                    currentFileReader = null;
                    return Producer.EOF;
                }
                // so we have a live line
                lineNumber++;
                index = 0;
                currentLine += delimiter;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        int i = (int)(currentLine.charAt(index));
//        Logger.debug("getchar:<"+(char)i+">");
        index++;
        if (index == currentLine.length()) {
            currentLine = null;
        }
        return(i);
    }
    
    public String getFileList() {
        return list;
    }
    
    public String getFileName() {
        return (currentFile.getName());
    }
    
    public int getLineNumber() {
        return lineNumber;
    }
    
    BufferedReader openNextFile() {
        // get the file name
        while (true) {
            String nextFile = null;
            try {
                nextFile = currentListReader.readLine();
//                Logger.debug("Producer.nextFile:<"+nextFile+">");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            if (nextFile == null) {
                return null;
            }
            
            if (nextFile.startsWith("#")) {
                continue;
            }
            if (nextFile.trim().length() == 0) {
                continue;
            }
            // try to open it
            currentFile = new File(nextFile);
            //					System.out.println("Trying to open file [" + nf + "]");
            //nextFile = "";
            if (! currentFile.isFile() ) {
                System.out.println("Could not open " + currentFile);
                currentFile = null;
                continue;
            }
            
            try {
                return (new BufferedReader(new FileReader(currentFile)));
            } catch (FileNotFoundException e) {
                System.out.println(e.toString());
                continue;
            }
        }
    }
    
    public void setFileList(String s) {
        lineNumber = 0;
        ByteArrayInputStream bais = new ByteArrayInputStream(s.getBytes());
        InputStreamReader isr = new InputStreamReader(bais);
        currentListReader = new BufferedReader(isr);
    }
}
