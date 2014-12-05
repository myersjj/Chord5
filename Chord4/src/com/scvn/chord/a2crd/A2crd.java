package com.scvn.chord.a2crd;

import com.scvn.chord.Producer;
import com.scvn.chord.StringProducer;

import java.io.*;

public class A2crd {

    private boolean debugFlag = false;

    void merge2lines(String chordLine, String textLine, StringBuffer res)
    {
        int i, mini=0, max=0;
        int lchord = chordLine.length();
        int ltext = textLine.length();
        int minChordLineIndex = 0;

        max = Math.max(lchord, ltext);
        debug("// merge2lines text_line=["+textLine+"]");
        debug("// merge2lines chord_line=["+chordLine+"]");

        for (i = 0; i < max ; i++)
        {
            if ( (i > minChordLineIndex) && (i < lchord) && ( chordLine.charAt(i) != ' ' ))
            {
                res.append ("[");
                for (mini=i;  (mini<lchord) && (chordLine.charAt(mini) != ' '); mini++)
                {
                    res.append(chordLine.charAt(mini));
                }
                res.append("]");
                minChordLineIndex = mini;
            }
            if (i < ltext)
            {
                res.append(textLine.charAt(i));
            }
        }

        res.append("\n");
    }
/* --------------------------------------------------------------------------------*/
    public String merge(double ratio, String text)
    {
        debug ("// start of processFile");
        StringBuffer res = new StringBuffer();

        int       spaces=0;
        int     notSpace=0;

        boolean        dirtyChord=false;
        boolean        dirtyText=false;

        String     chordLine = "";
        String     textLine = "";
        boolean isChordLine;
        StringBuffer     buf = new StringBuffer();
        int c;
        int i;

        Producer producer = new StringProducer(text);

        while ((c=producer.getChar()) != Producer.EOJ)
        {
            switch (c)
            {
                case Producer.SOF:
                    debug ("// IN:Produce.SOF");
                    buf = new StringBuffer();
                    break;

                case Producer.EOF:
                    if (dirtyChord || dirtyText) {
                        merge2lines(chordLine, textLine, res);
                    }
                    break;

                case '\n': // handling of a complete line
                    debug("// IN:["+buf+"]");
                    if (notSpace != 0) {
                        debug ("// space="+spaces+" notSpace="+notSpace+"");

                        isChordLine  = ((spaces / notSpace) > ratio) || ((spaces==0) && (buf.length()<3));
                        debug ("// isChordline is \""+isChordLine+"\"");
                        if ((isChordLine && dirtyChord) || (! isChordLine && dirtyText)) {
                            merge2lines (chordLine, textLine, res);
                            dirtyText=false;
                            textLine = "";
                            dirtyChord=false;
                            chordLine = "";
                            spaces =0;
                            notSpace = 0;
                        }
                        if (isChordLine) {
                            chordLine = buf.toString();
                            dirtyChord = true;
                        } else {
                            textLine = buf.toString();
                            dirtyText = true;
                        }

                        buf = new StringBuffer();
                        spaces=0;
                        notSpace=0;
                    }
                    else // just got an empty line
                    {
                        debug("// got blank line dirtyChord="+dirtyChord +" dirtyText="+dirtyText);
                        if (dirtyChord || dirtyText) {
                            merge2lines(chordLine, textLine, res);
                            dirtyText=false;
                            textLine = "";
                            dirtyChord=false;
                            chordLine = "";
                            spaces =0;
                            notSpace = 0;
                        }
                        res.append("\n");

                    }

                    buf = new StringBuffer();
                    break; // end of complete line handling

                case '\t':
                    for (i=0; (i<8) && (buf.length() % 8 != 0); i++)
                        buf.append(' ');
                    spaces += 8;
                    break;

                case '|':
                case ' ':
                    spaces++;
                    buf.append(' ');
                    break;

                    // fall thru is important here !

                default:
                    notSpace++;
                    buf.append((char)c);
                    break;
            }
        }
        return res.toString();
    }

/* --------------------------------------------------------------------------------*/
    void debug(String msg) {
        if (debugFlag) {
            System.out.println(msg);
        }
    }
/* --------------------------------------------------------------------------------*/

    public static void main (String args[])
    {
        for (int optind = 0; optind < args.length; optind++ )
        {
//            FileListProducer flp =  new FileListProducer(source);
            A2crd a2crd = new A2crd();
            String source = new String(args[optind]);

            try {
                StringBuffer sb =new StringBuffer();
                BufferedReader br = new BufferedReader(new FileReader(source));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }

                System.out.println("============before====================");
                System.out.println(sb);
                System.out.println("============after=====================");
                String res = a2crd.merge(1.0, sb.toString());
                System.out.println(res);
                System.out.println("======================================");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
