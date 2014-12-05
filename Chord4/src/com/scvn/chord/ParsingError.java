/*
 * Created by IntelliJ IDEA.
 * User: martin
 * Date: 02-05-18
 * Time: 14:46:28
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package com.scvn.chord;

public class ParsingError {
    private int errType;
    private String errDetail;
    private String fileName;
    private int lineNumber;

        public ParsingError(int errType, String errDetail, String fileName, int lineNumber) {
        this.errType = errType;
        this.errDetail = errDetail;
        this.fileName = fileName;
        this.lineNumber = lineNumber;

    }

    public int getErrType() {
        return errType;
    }

    public String getErrDetail() {
        return errDetail;
    }

    public String getFileName() {
        return fileName;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
