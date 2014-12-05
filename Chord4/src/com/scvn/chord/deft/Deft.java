package com.scvn.chord.deft;

import com.scvn.chord.audio.GuitarPlayer;
import com.scvn.chord.*;

import javax.swing.*;
import javax.swing.event.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;


public class Deft extends JFrame implements ChordConstants, ActionListener, ListSelectionListener {

    /* 0   1   2   3   4   5   6   7   8   9   10  11 */
    /* A   A#  B   C   C#  D   D#  E   F   F#  G   G# */

    static final int BoardLength = 15;
    static final int EMPTY = -1;
    static final int GRID_LEN = 4;

    static final int NB_HALFTONE = 12;
    static final int NB_STRING = 6;

    static final int MAXFING = 4;
    static final int HAND = 4;
    static final int MINSTRING = 4;

    Chord4 chord4;
    String chordName;		/* points to arguments */
    boolean allowInversions = false;
//    boolean verboseAnalysis = false;
    int dfret;
    int tuning[] = new int[NB_STRING];
    int board[][] = new int[NB_STRING][BoardLength];

    int maxfing = MAXFING;
    int handReach = HAND;
    int minstring = MINSTRING;
    int blen = BoardLength;

    Vector foundPos;
    ChordList chordList;

    KnownChord currentVoicing;
    ChordParser chordParser;

    File chordrcFile;

    final static String help_help = "This text";
    final static String req_help = "Prints the required notes for this chord";
    final static String tune_help = "set tuning for the instrument";
    final static String genpos_help = "Generates all possible ways of playing the chord";
    final static String board_help = "Prints the position on the board of the required notes";
    final static String scale_help = "Prints the scale of the root of the chord";
    final static String chord_help = "Sets the chord to work on";
    final static String blen_help = "Sets the lentgh of the guitar board";
    final static String hand_help = "Sets the span of your handReach, in frets";
    final static String minstring_help = "Sets the minimum number of strings required in a chord";
    final static String init_help = "Reinitializes all variables to defaults";
    final static String show_help = "Displays operating parameters";
    final static String inversions_help = "Toggles the generation of positions with inversions";
    final static String quit_help = "Quits";
    final static String fing_help = "Sets the maximum number non-zero frets in a position";
    final static String display_help = "Display the full board for a given position";
    final static String write_help = "Writes a define statement to the output file";


    // GUI
    JTextField tfChord;
    JTextArea taWork;
    JCheckBox cbAllowInversions;
    JCheckBox cbVerboseAnalysis;
    JList voicings;
    JButton storeButton, goButton;
    JComboBox cbMinStrings, cbMaxFingers, cbHandReach;
    JList inputList;
/*------------------------------------------------------------*/
    private Deft() {
        super("Deft: the DEFine Tool for Chord");
        chordList = new ChordList();
        initGUI();
        init();
        chordrcFile = Params.locateChordrcFile();
    }

    public Deft(Chord4 c4) {
        this();
        this.chord4 = c4;
        addWindowListener(chord4.getViewMenu());
    }


    public void initGUI() {
// work list
        // input field
        // list of inputs
// analysis

// voicings
        // voicelist
        // action
        //save to chortdrc

//worklist
        Container mainPanel = getContentPane();
        mainPanel.setLayout(new BorderLayout());
//        JPanel centerPanel = new JPanel(new BorderLayout());

        JPanel workList = new JPanel(new BorderLayout());
        JPanel controls = new JPanel(new GridLayout(0, 2));
        tfChord = new JTextField(6);
        tfChord.addActionListener(this);
        controls.add(new JLabel("ChordName"));
        controls.add(tfChord);
        cbMinStrings = new JComboBox(new String[]{"3", "4", "5"});
        cbMinStrings.setSelectedItem(Integer.toString(minstring));
        cbMinStrings.addActionListener(this);
        cbMinStrings.setToolTipText(minstring_help);
        cbMaxFingers = new JComboBox(new String[]{"2", "3", "4", "5"});
        cbMaxFingers.setSelectedItem(Integer.toString(maxfing));
        cbMaxFingers.addActionListener(this);
        cbMaxFingers.setToolTipText(fing_help);
        cbHandReach = new JComboBox(new String[]{"3", "4", "5"});
        cbHandReach.setSelectedItem(Integer.toString(handReach));
        cbHandReach.addActionListener(this);
        cbHandReach.setToolTipText(hand_help);
        cbAllowInversions = new JCheckBox();
        cbAllowInversions.setSelected(allowInversions);
        cbAllowInversions.addActionListener(this);
        cbAllowInversions.setToolTipText(inversions_help);
        cbVerboseAnalysis = new JCheckBox();
        cbVerboseAnalysis.setSelected(Logger.isInfo());
        cbVerboseAnalysis.addActionListener(this);
//        cbVerboseAnalysis.setToolTipText(inversions_help);
        goButton = new JButton("Go!");
        goButton.addActionListener(this);
        controls.add(new JLabel("MinStrings"));
        controls.add(cbMinStrings);
        controls.add(new JLabel("MaxFingers"));
        controls.add(cbMaxFingers);
        controls.add(new JLabel("HandReach"));
        controls.add(cbHandReach);
        controls.add(new JLabel("Allow inversions"));
        controls.add(cbAllowInversions);
        controls.add(new JLabel("Verbose Analysis"));
        controls.add(cbVerboseAnalysis);
        controls.add(new JLabel());
        controls.add(goButton);

        JPanel inputPanel = new JPanel(new BorderLayout());
        workList.add(controls, BorderLayout.NORTH);
        inputList = new JList(chordList);
        inputList.addListSelectionListener(this);
        inputPanel.add(new JLabel("List of unknown chords"), BorderLayout.NORTH);
        inputPanel.add(new JScrollPane(inputList), BorderLayout.CENTER);
        workList.add(inputPanel, BorderLayout.SOUTH);
        mainPanel.add(workList, BorderLayout.WEST);
// done worklist


// voicings
        JPanel voicingPanel = new JPanel(new BorderLayout());
        voicingPanel.add(new JLabel("Click a voicing to hear/select"), BorderLayout.NORTH);
        voicings = new JList(new Vector());
        voicings.setCellRenderer(new KnownChordCellRenderer());
        voicings.addListSelectionListener(this);
        JScrollPane voicingScrollPane = new JScrollPane(voicings);
        voicingPanel.add(voicingScrollPane, BorderLayout.CENTER);
        storeButton = new JButton("Store in chordrc");
        storeButton.addActionListener(this);
        storeButton.setEnabled(false);
        voicingPanel.add(storeButton, BorderLayout.SOUTH);
        mainPanel.add(voicingPanel, BorderLayout.CENTER);
// done voicings

// analysis
        JPanel analysis = new JPanel(new BorderLayout());
        analysis.add(new JLabel("Detailed musical analysis"), BorderLayout.NORTH);
        taWork = new JTextArea(10,10);
        taWork.setEditable(false);
        taWork.setFont(new Font("MonoSpaced", Font.PLAIN, 14));
        analysis.add(new JScrollPane(taWork), BorderLayout.CENTER);
        mainPanel.add(analysis, BorderLayout.SOUTH);
// done analysis

        setSize(600, 600);
        //pack();
//        setVisible(true);
    }
/*------------------------------------------------------------*/
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == goButton || e.getSource() == tfChord) {
            try {
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                setChordName(tfChord.getText());
                init();
                chordParser = new ChordParser(chordName);
                taWork.append(chordParser.getWork());
                populate(chordParser);
                taWork.append(printBoard(chordParser.getScale(), chordParser.getRequiredHalftonesRel()));
                genpos(chordParser);
                showVoicings(foundPos);
            } catch (Exception e2) {
                taWork.append(e2.getMessage());
                e2.printStackTrace();
            } finally {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                return;
            }
        }
        if (e.getSource() == cbMinStrings) {
            setMinstring(Integer.parseInt((String) cbMinStrings.getSelectedItem()));
            return;
        }
        if (e.getSource() == cbMaxFingers) {
            setMaxfing(Integer.parseInt((String) cbMaxFingers.getSelectedItem()));
            return;
        }
        if (e.getSource() == cbHandReach) {
            setHandReach(Integer.parseInt((String) cbHandReach.getSelectedItem()));
            return;
        }
        if (e.getSource() == cbAllowInversions) {
            allowInversions = cbAllowInversions.isSelected();
            return;
        }
        if (e.getSource() == cbVerboseAnalysis) {
            Logger.setInfo(cbAllowInversions.isSelected());
            return;
        }
        if (e.getSource() == storeButton) {
            appendToChordrc(chordrcFile, currentVoicing);
            if (chord4 != null) {
                chord4.readChordrc();
            }
            storeButton.setEnabled(false);
            chordList.remove(chordName);
        }
    }

/*------------------------------------------------------------*/
    void do_quit() {
        //System.out.println ("Bye!\n");
        System.exit(0);
    }
/*------------------------------------------------------------*/
    void do_tune() {
        tuning[0] = 4;
        tuning[1] = 9;
        tuning[2] = 14;
        tuning[3] = 19;
        tuning[4] = 23;
        tuning[5] = 28;
    }

    /*------------------------------------------------------------*/
    void free_pos() {
        foundPos.removeAllElements();
    }
/*------------------------------------------------------------*/
    void genpos(ChordParser pc) {
        int baseFret;
        int pos[] = new int[NB_STRING];

        Logger.info("Number of notes required:" + pc.getRequiredHalftonesRel().length);
        if (pc.getRequiredHalftonesRel().length > NB_STRING) {
            Logger.info("Number of strings less than the number required notes !");
            return;
        }

        Logger.info("Generating positions for " + chordName + ".\n");
        // start on each fret

        // stringUses contains relative finger positions
        int stringUses[] = new int[handReach];
        stringUses[0] = FRET_OPEN; //open
        for (int i = 1; i < handReach; i++) {
            stringUses[i] = i;
        }
//        stringUses[handReach+1]=FRET_X; //mute

        for (baseFret = 1; baseFret < blen - handReach + 1; baseFret++) {
//        for (baseFret=1; baseFret<2; baseFret++) {
            Logger.info("Calling GP2 with baseFret=" + baseFret);
            genpos2(pos, stringUses, baseFret, 0);
        }
//            genpos2(pos, stringUses, 0, 0);
        Logger.info("Found " + foundPos.size() + " positions");
    }

/*------------------------------------------------------------*/
    public boolean genpos2(int pos[], int[] stringUses, int baseFret, int string) {

        // check for complete pos
        if (string == NB_STRING) {
//            System.out.print("GP2 full pos:");
//            for (int i = 0; i<pos.length; i++) {
//                System.out.print(" " + pos[i]);
//            }
//            System.out.println();
            if (posok(chordParser, baseFret, pos)) {
                valid(baseFret, pos);
                return true;
            }
            return false;
        }
        //else
        int usage;
        boolean used = false;
        StringBuffer sb;
        for (int usageIndex = 0; usageIndex < stringUses.length; usageIndex++) {
            used = false;
            sb = new StringBuffer();
            usage = stringUses[usageIndex];
            for (int i = 0; i < string; i++) {
                sb.append("--");
            }
            sb.append("s" + string + " on fret " + usage);
            if (usage == FRET_OPEN) {
                sb.append(" board=" + board[string][0]);
                if (board[string][0] != EMPTY) {
                    pos[string] = usage;
                    used = genpos2(pos, stringUses, baseFret, string + 1);
                }
            } else if (usage != FRET_OPEN) {
                sb.append(" board=" + board[string][baseFret + usage-1]); //usage == 1 means on basefret
                Logger.info(sb.toString());
                if (board[string][baseFret + usage-1] != EMPTY) {
                    pos[string] = usage;
                    used = genpos2(pos, stringUses, baseFret, string + 1);
                }
            }
            sb.append("\n");
        }
        // only try with this string muted if it could not be used otherwise
        if (!used) {
            Logger.info("Trying with string " + string + " muted");
            pos[string] = FRET_X;
            genpos2(pos, stringUses, baseFret, string + 1);
        }
        return false;
    }
/*------------------------------------------------------------*/
    void init() {
        do_tune();
        foundPos = new Vector();
    }
/*------------------------------------------------------------*/
    void insert(int dfret, int pos[]) {
        //FingerPos fp = new FingerPos(chordName, foundPos.size(), dfret, pos);
        KnownChord kc = new KnownChord(chordName, pos, dfret, ChordVector.CHORD_FROM_DEFT, ChordVector.CHORD_HARD);
        //System.out.println(kc);
        if (foundPos.contains(kc)) {
            Logger.info("Redundant kc !");
        } else {
            foundPos.addElement(kc);
        }
    }
/*------------------------------------------------------------*/
    public static void main(String[] args) {
       Deft deft = new Deft();
       deft.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       deft.setVisible(true);
    }

    /*------------------------------------------------------------*/
    /* Builds a array representing the guitar board.
    The values are either EMPTY or the index into the reqs
    array
    */

    void populate(ChordParser pc) {
        int reqnote, string, fret, offset;
        int reqs[] = pc.getRequiredHalftonesRel();
        int root_ht = pc.getRootHalfTone();

        /* init board */
        for (fret = 0; fret < BoardLength; fret++)
            for (string = 0; string < NB_STRING; string++)
                board[string][fret] = EMPTY;

        System.out.println("populating...");
        for (reqnote = 0; reqnote < pc.getRequiredHalftonesRel().length; reqnote++) {
            System.out.println("[" + (reqs[reqnote]) + "]");
            for (string = 0; string < NB_STRING; string++) {
                offset = (reqs[reqnote] - tuning[string] + root_ht + 120) % NB_HALFTONE;
                while (offset < BoardLength) {
                    board[string][offset] = reqnote;
                    offset += NB_HALFTONE;
                }
            }
        }
/*	System.out.println("\n"); */
    }
/*------------------------------------------------------------*/
    boolean posok(ChordParser pc, int fret, int pos[]) {
        int j, string;
        boolean found;
        int lowest;
        int nbstrings = 0;
        int req_base = -1;
        int apos;

        /* one the notes MUST be on the current fret to avoid redundance */
        /* and while we're there, check for barre opportunity */
        int nbfing = 0;

        boolean inbarre = false;


        for (j = pos.length - 1; j >= 0; j--) {
            if (pos[j] == FRET_OPEN) {
                //open string
                nbstrings++;
                inbarre = false;
            } else if (pos[j] == FRET_X) {
                // do nothing
            } else if (pos[j] == 1) {
                // on first fret
                nbstrings++;
                if (!inbarre) {
                    nbfing++;
                    inbarre = true;
                }
            } else {
            nbstrings++;
            nbfing++;
            }
        }

        if (nbstrings < minstring) {
            status("too few strings used:" + nbstrings, fret, pos);
            return (false);
        }

        if (nbfing > maxfing) {
            status("too many fingers used:" + nbfing, fret, pos);
            return (false);
        }


        /* check for the presence of all required notes */
        int nreqs = pc.getRequiredHalftonesRel().length;
        for (j = 0; j < nreqs; j++) {
            found = false;
            for (string = 0; ((string < NB_STRING) && (!found)); string++) {
                if (pos[string] == FRET_X) {
                    continue;
                }
                if (pos[string] == FRET_OPEN) {
                    apos = 0;
                } else {
                    apos = pos[string] + fret - 1; // pos[string] == 1 means on current fret
                }
                if (j == board[string][apos]) {
                    found = true;
                    //System.out.println(scale.getNote(j) "found on string "+string+" fret "+pos[string]+"\n");
                }
            }
            if (!found) {
                status("missing note:"+pc.getRequiredHalftonesRel()[j], fret, pos);
                return (false);
            }
        }

        /* check for a constraint on base */
        req_base = -1; // none by default
        req_base = pc.getBaseHalfToneAbs(); // possibly set

        // change to roote  root note if inversions are not allowed, and still no base set
        if (req_base < 0 && !allowInversions) {
            req_base = pc.getRootHalfTone();
        }

        /* check for the base note being at the base */
        if (req_base >= 0) {
            lowest = 1000;
            for (j = 0; j < NB_STRING; j++) {
                if (pos[j] == FRET_X) {
                    continue;
                }
                if (pos[j] == FRET_OPEN) {
                    apos = tuning[j];
                } else {
                    apos = pos[j] + tuning[j] + fret - 1; // pos is 1-based
                }
                if (apos < lowest) {
                    lowest = apos;
                }
            }

            if (req_base != lowest % NB_HALFTONE) {
                /*
                System.out.print("lowest=%d ", lowest);
                */
                status("wrong note at base.Looking for " + req_base + " but got " + lowest, fret, pos);
                return (false);
            }
        }
        /* calculate display fret */
        dfret = 1; // fret is 0-based, dfret is 1-based
        for (j = 0; j < NB_STRING; j++)
            if (pos[j] > GRID_LEN) dfret = fret;
        status(" OK.", fret, pos);

        return (true);
    }
/*------------------------------------------------------------*/
    String printBoard(Scale scale, int reqs[]) {
//        int ht;
        int req;

        StringBuffer sb = new StringBuffer();
        char noFret = ']';
        char anyFret = '|';
        char delim;
        String noteName;
        int noteLength;

        sb.append("Printing board to play " + chordName + "\n");
        System.out.println("digits in board are always relative values to C ! "+ "\n");
        for (int string = 0; string < NB_STRING; string++) {

            for (int fret = 0; fret < blen; fret++) {
                delim = (fret == 0) ? noFret : anyFret;
                req = board[string][fret];
                noteName = (req == EMPTY) ? "" : scale.getNoteByHT(reqs[req]).toString();
                noteLength = noteName.length();
                sb.append (noteName);
                sb.append("   ".substring(noteLength));
                sb.append(delim);
                }
            sb.append("\n");
        }
        return sb.toString();
    }
/*------------------------------------------------------------*/
    void status(String msg, int baseFret, int pos[]) {
        int j;
        StringBuffer sb = new StringBuffer();
        sb.append("Position baseFret=" + baseFret + " frets=");
        for (j = 0; j < pos.length; j++)
            if (pos[j] == FRET_OPEN) {
                sb.append("o ");
            } else if (pos[j] == FRET_X) {
                sb.append("x ");
            } else {
                sb.append(pos[j] + " ");
            }
        sb.append(" gets: " + msg );
        Logger.info(sb.toString());
    }

    public void appendToChordrc(File chordrcFile, KnownChord kc) {
        if (chordrcFile == null) {    // should never happen. Deft checks also
            JOptionPane.showMessageDialog(null, "No chordrc file found. Cannot save");
            return;
        }
        try {
            RandomAccessFile raf = new RandomAccessFile(chordrcFile, "rw");
            raf.seek(raf.length());
            raf.writeBytes(kc.getDefine());
            raf.writeBytes(System.getProperty("line.separator"));
            raf.close();
            // re-read
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


/*------------------------------------------------------------*/
    void valid(int fret, int pos[]) {
        insert(fret, pos);
    }

/*------------------------------------------------------------*/
    public void showVoicings(Vector foundPos) {
        voicings.setListData(foundPos);
    }

/*------------------------------------------------------------*/
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == voicings && !e.getValueIsAdjusting()) {
            JList list = (JList) e.getSource();
            currentVoicing = (KnownChord) list.getSelectedValue();
            if (currentVoicing != null) {
                GuitarPlayer gp = new GuitarPlayer();
                gp.playChord(currentVoicing);
                gp.close();
                if (chordrcFile != null) {
                    storeButton.setEnabled(true);
                }
            }
            return;
        }
        if (e.getSource() == inputList) {
            JList list = (JList) e.getSource();
            setChordName((String)list.getSelectedValue());
            tfChord.setText(chordName);
            return;
        }
    }

    public void setChordName(String chordName) {
        this.chordName = chordName;
    }

    public void setMaxfing(int maxfing) {
        this.maxfing = maxfing;
    }

    public void setHandReach(int handReach) {
        this.handReach = handReach;
    }

    public void setMinstring(int minstring) {
        this.minstring = minstring;
    }

    public void setChordList(Vector data) {
        chordList.setData(data);
        voicings.removeAll();
    }
}
