/*
 * TextAreaProducer.java
 *
 * Created on December 29, 2001, 7:19 PM
 */

package com.scvn.chord;

/**
 *
 * @author  martin
 */
public class StringProducer implements Producer {
    
    private String  s;
    String text;
    int marker = 0;
    int lineCounter = 1;
    String sep;
    
    /** Creates a new instance of TextAreaProducer */
    public StringProducer(String s) {
        this.s = s;
        sep = System.getProperty("line.separator");
    }
    
    void initText () {
        text = s;
    }
    public int getChar() {
        int c;
        if (text == null) {
            initText();
            if ( ! text.endsWith(sep)) {
                text += sep;
            }
            return(Producer.SOF);
        }
        if (marker < text.length()) {

            if (text.regionMatches(marker, sep, 0, sep.length())) {
                lineCounter++;
            }
            c = text.charAt(marker++);
            return c;
        } else if (marker == text.length() ) {
            marker++;
            return (Producer.EOF);
        }
        else {
            text = null;
            marker = 0;
            return (Producer.EOJ);
        }
    }
    /* --------------------------------------------------------------------------------*/
    public String getFileName() {
        return("<Editor>");
    }
    /* --------------------------------------------------------------------------------*/
    public int getLineNumber() {
        return lineCounter;
    }
    /* --------------------------------------------------------------------------------*/
    public String getFileList() {
        return("<Editor>");
    }
    /* --------------------------------------------------------------------------------*/
    public void setFileList(String s) {
    }
}
