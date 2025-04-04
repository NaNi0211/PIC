import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/*
 * IO Pin RA4 Step
 * 
 * Tris reset?
 * retlw 71 - 73test 10
 */

//add row https://www.youtube.com/watch?v=eAJphO_PHTU&t=64s

public class PICGUI extends JFrame {

    private long start = System.nanoTime();
    // private static int[] ra_pins = new int[8];
    // private static int[] rb_pins = new int[8];

    public static int[] ioPinsDataA = new int[8];
    public static int[] ioPinsDataB = new int[8];

    // public static char[] ioTrisA = new char[8] ;
    // public static char[] ioTrisB = new char[8];
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private JTable table_1;
    private JTable gpr_table;
    public JTextArea console_area = new JTextArea();
    public JTextArea state_area = new JTextArea();
    // ArrayList<String> extracted = new ArrayList<String>(); ersetzt durch befehle
    List<String> befehle = new ArrayList<>();
    static ArrayList<Integer> befehleInteger = new ArrayList<>();
    static HashMap<Integer, Integer> newOP = new HashMap<Integer, Integer>();
    /**
     * @wbp.nonvisual location=-249,529
     */
    private final JLabel label = new JLabel("New label");
    private JTextField tf_slider;
    private JTable io_table;
    private JTable stack_table;
    private JTable sfr_table;
    private int skipFirst = 0;
    private Thread thread;
    private Thread thread2;
    private int row;
    private int firstRow;
    protected static double quartz = 4;
    private JLabel lbw;
    private JLabel lbc;
    private JLabel lbpc;
    private JLabel lbdc;
    private JLabel lbz;
    private JLabel lbpcl;
    private JLabel lbl_laufzeit;
    private boolean runCounter;
    private String filePath = "";
    private JFileChooser fileChooser = new JFileChooser();
    private int ra4;
    protected static int timer0 = 0;
    protected static int prescaler = 2;
    protected static boolean PSAone = true;
    static boolean otherPc;
    private int ra0;
    private boolean ra0Check;
    private int ra7;
    private boolean ra7Check;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PICGUI frame = new PICGUI();

                    // Test t1 = new Test();
                    // t1.digit();
                    // frame.console_area.append(Integer.toString(t1.digit()));

                    // Convert Hex to integer
                    // frame.console_area.append(Integer.toString(convertHexToInt("F")));
                    // Append each integer from the list to the JTextArea
                    /*
                     * for (Integer intValue : befehleInteger) {
                     * frame.console_area.append(intValue.toString() + "\n"); }
                     */
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public PICGUI() {

        setResizable(false);
        setTitle("PIC16F84 Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1268, 900);
        contentPane = new JPanel();
        // contentPane.setBackground(new Color(128, 128, 128));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(10, 611, 1217, 239);
        contentPane.add(tabbedPane);

        JPanel simulator_state_panel = new JPanel();
        tabbedPane.addTab("Simulator State", null, simulator_state_panel, null);
        simulator_state_panel.setLayout(null);

        JScrollPane scrollPane_7 = new JScrollPane();
        scrollPane_7.setBounds(10, 11, 1192, 189);
        simulator_state_panel.add(scrollPane_7);

        // JTextArea state_area = new JTextArea();
        scrollPane_7.setViewportView(state_area);

        JPanel console_panel = new JPanel();
        tabbedPane.addTab("Console", null, console_panel, null);
        console_panel.setLayout(null);

        JScrollPane scrollPane_8 = new JScrollPane();
        scrollPane_8.setBounds(10, 11, 1192, 189);
        console_panel.add(scrollPane_8);

        // JTextArea console_area = new JTextArea();
        scrollPane_8.setViewportView(console_area);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBounds(10, 11, 1217, 23);
        contentPane.add(toolBar);

        JButton btnFile = new JButton("File");
        btnFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                boolean loop;

                FileNameExtensionFilter filter = new FileNameExtensionFilter("LST-Datei", "LST");
                fileChooser.setFileFilter(filter);

                do {

                    loop = false;
                    int result = fileChooser.showOpenDialog(contentPane);
                    if (result == JFileChooser.APPROVE_OPTION) {

                        filePath = fileChooser.getSelectedFile().getAbsolutePath();

                        System.out.println("Datei wurde gefunden:" + filePath);
                        if (filePath.endsWith(".LST")) {

                            displayDataInTable(filePath);
                        } else {
                            loop = true;
                            JOptionPane.showMessageDialog(null, "Es muss eine LST-Datei geladen werden!", "Error",
                                    JOptionPane.ERROR_MESSAGE);

                        }

                    }
                } while (!filePath.endsWith(".LST") && loop);

            }

        });
        toolBar.add(btnFile);

        JButton btnHelp = new JButton("Help");
        btnHelp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        toolBar.add(btnHelp);

        // Lable has to be above mouse event otherwise it doesn't know the variable
        JLabel lbl_slider = new JLabel("4"); // default value
        lbl_slider.setBounds(837, 65, 80, 14);
        contentPane.add(lbl_slider);

        // https://www.youtube.com/watch?v=LvLFekjVJA8
        // slider min, max, steps
        JSlider slider = new JSlider(1, 20, 10);
        slider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // gets the value from out Slider as the mouse is being clicked
                long num3;
                num3 = slider.getValue();
                quartz = slider.getValue();
                lbl_slider.setText(Long.toString(num3));
            }
        });
        slider.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // increases/decreases value as mouse is dragged
                long num2;
                num2 = slider.getValue();
                quartz = slider.getValue();
                lbl_slider.setText(Long.toString(num2));

            }
        });
        slider.setBounds(609, 61, 174, 26);
        contentPane.add(slider);

        JLabel lblNewLabel_1 = new JLabel("MHz");
        lblNewLabel_1.setBounds(947, 65, 46, 14);
        contentPane.add(lblNewLabel_1);

        lbl_laufzeit = new JLabel("0");
        lbl_laufzeit.setBounds(1072, 65, 99, 14);
        contentPane.add(lbl_laufzeit);

        JLabel lblNewLabel_3 = new JLabel("μs");
        lblNewLabel_3.setBounds(1181, 65, 46, 14);
        contentPane.add(lblNewLabel_3);

        JPanel panel = new JPanel();
        panel.setBounds(10, 97, 1217, 503);
        contentPane.add(panel);
        panel.setLayout(null);

        table = new JTable();
        table.setBounds(458, 5, 0, 0);
        panel.add(table);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(212, 41, 2, 2);
        panel.add(scrollPane);

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(286, 435, 0, 0);
        panel.add(scrollPane_1);

        table_1 = new JTable();
        table_1.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "BP", "PC", "HEX_INSTR", "ROW",
                "INSTRUCTION/COMMENTS                                                   " }) {
            Class[] columnTypes = new Class[] { Boolean.class, String.class, String.class, String.class, String.class };

            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        });

        // make vertical and horizontal scroll possible
        // https://stackoverflow.com/questions/2452694/jtable-with-horizontal-scrollbar
        JScrollPane scrollPane_2 = new JScrollPane(table_1, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        table_1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        scrollPane_2.setBounds(38, 41, 576, 416);
        panel.add(scrollPane_2);
        scrollPane_2.setViewportView(table_1);
        scrollPane_2.setAutoscrolls(true);
        scrollPane_2.setViewportView(table_1);

        TableColumnModel colmod = table_1.getColumnModel();
        TableColumn TC_0st = colmod.getColumn(0);
        TC_0st.setPreferredWidth(30);
        TableColumn TC_lst = colmod.getColumn(1);
        TC_lst.setPreferredWidth(35);
        TableColumn TC_2st = colmod.getColumn(2);
        TC_2st.setPreferredWidth(70);
        TableColumn TC_3st = colmod.getColumn(3);
        TC_3st.setPreferredWidth(50);
        TableColumn TC_4st = colmod.getColumn(4);
        TC_4st.setPreferredWidth(550);

        JLabel lblNewLabel_4 = new JLabel("W:");
        lblNewLabel_4.setBounds(629, 41, 36, 14);
        panel.add(lblNewLabel_4);

        JLabel lblNewLabel_5 = new JLabel("PC:");
        lblNewLabel_5.setBounds(629, 66, 36, 14);
        panel.add(lblNewLabel_5);

        JLabel lblNewLabel_6 = new JLabel("PCL:");
        lblNewLabel_6.setBounds(629, 91, 46, 14);
        panel.add(lblNewLabel_6);

        JLabel lblNewLabel_7 = new JLabel("PCLATH:");
        lblNewLabel_7.setBounds(625, 116, 79, 14);
        panel.add(lblNewLabel_7);

        JLabel lblNewLabel_8 = new JLabel("C:");
        lblNewLabel_8.setBounds(629, 141, 14, 14);
        panel.add(lblNewLabel_8);

        JLabel lblNewLabel_9 = new JLabel("DC:");
        lblNewLabel_9.setBounds(629, 166, 26, 14);
        panel.add(lblNewLabel_9);

        JLabel lblNewLabel_10 = new JLabel("Z:");
        lblNewLabel_10.setBounds(629, 191, 14, 14);
        panel.add(lblNewLabel_10);

        lbw = new JLabel("0H");

        lbw.setBounds(704, 41, 46, 14);
        panel.add(lbw);

        lbpc = new JLabel("0");
        lbpc.setBounds(704, 66, 46, 14);
        panel.add(lbpc);

        lbpcl = new JLabel("0");
        lbpcl.setBounds(704, 91, 36, 14);
        panel.add(lbpcl);

        JLabel lbpclath = new JLabel("0");
        lbpclath.setBounds(704, 116, 46, 14);
        panel.add(lbpclath);

        lbc = new JLabel("0");
        lbc.setBounds(704, 141, 17, 14);
        panel.add(lbc);

        lbdc = new JLabel("0");
        lbdc.setBounds(704, 166, 14, 14);
        panel.add(lbdc);

        lbz = new JLabel("0");
        lbz.setBounds(704, 191, 14, 14);
        panel.add(lbz);

        JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane_1.setBounds(827, 41, 364, 416);
        panel.add(tabbedPane_1);

        JPanel panel_gpr = new JPanel();
        tabbedPane_1.addTab("GPR", null, panel_gpr, null);
        panel_gpr.setLayout(null);

        JScrollPane scrollPane_3 = new JScrollPane();
        scrollPane_3.setBounds(10, 42, 339, 335);
        panel_gpr.add(scrollPane_3);

        gpr_table = new JTable();
        gpr_table.setModel(new DefaultTableModel(new Object[68][4],
                new String[] { "Addresse", "Werte HEX", "Addresse", "Werte HEX" }));
        for (int i = 12; i < 80; i++) {
            gpr_table.setValueAt("0x" + String.format("%02X", i).toUpperCase(), i - 12, 0);
            gpr_table.setValueAt("0H", i - 12, 1);
        }
        for (int i = 140; i < 208; i++) {
            gpr_table.setValueAt("0x" + String.format("%02X", i).toUpperCase(), i - 140, 2);
            gpr_table.setValueAt("0H", i - 140, 3);
        }
        scrollPane_3.setViewportView(gpr_table);

        JLabel lblNewLabel_2 = new JLabel("Bank 0");
        lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblNewLabel_2.setBounds(74, 17, 46, 14);
        panel_gpr.add(lblNewLabel_2);

        JLabel lblNewLabel_14 = new JLabel("Bank 1");
        lblNewLabel_14.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblNewLabel_14.setBounds(231, 17, 46, 14);
        panel_gpr.add(lblNewLabel_14);
        // gpr_table.setTableHeader(null);//Header entfernen

        JPanel panel_sfr = new JPanel();
        tabbedPane_1.addTab("SFR", null, panel_sfr, null);
        panel_sfr.setLayout(null);

        JScrollPane scrollPane_6 = new JScrollPane();
        scrollPane_6.setBounds(10, 41, 339, 224);
        panel_sfr.add(scrollPane_6);

        sfr_table = new JTable();
        sfr_table
                .setModel(new DefaultTableModel(
                        new Object[][] {
                                { "0x00", "indirect", String.valueOf(DecodeDraft.ram[0][0]) + "H", "0x00", "indirect",
                                        String.valueOf(DecodeDraft.ram[1][0]) + "H" },
                                { "0x01", "TMR 0", String.valueOf(DecodeDraft.ram[0][1]) + "H", "0x01", "Option",
                                        String.format("%02X", DecodeDraft.ram[1][1] = 0b1111_1111).toUpperCase()
                                                + "H" },
                                { "0x02", "PCL", String.valueOf(DecodeDraft.ram[0][2]) + "H", "0x02", "PCL",
                                        String.valueOf(DecodeDraft.ram[1][2]) + "H" },
                                { "0x03", "Status", String.valueOf(DecodeDraft.ram[0][3]) + "H", "0x03", "Status",
                                        String.valueOf(DecodeDraft.ram[1][3]) + "H" },
                                { "0x04", "FSR", String.valueOf(DecodeDraft.ram[0][4]) + "H", "0x04", "FSR",
                                        String.valueOf(DecodeDraft.ram[1][4]) + "H" },
                                { "0x05", "Port RA", String.valueOf(DecodeDraft.ram[0][5]) + "H", "0x05", "Tris RA",
                                        String.format("%02X", DecodeDraft.ram[1][5] = 0b1111_1111).toUpperCase()
                                                + "H" },
                                { "0x06", "Port RB", String.valueOf(DecodeDraft.ram[0][6]) + "H", "0x06", "Tris RB",
                                        String.format("%02X", DecodeDraft.ram[1][6] = 0b1111_1111).toUpperCase()
                                                + "H" },
                                { "0x07", "-", String.valueOf(DecodeDraft.ram[0][7]) + "H", "0x07", "-",
                                        String.valueOf(DecodeDraft.ram[1][7]) + "H" },
                                { "0x08", "EEData", String.valueOf(DecodeDraft.ram[0][8]) + "H", "0x08", "EECon1",
                                        String.valueOf(DecodeDraft.ram[1][8]) + "H" },
                                { "0x09", "EEAdr", String.valueOf(DecodeDraft.ram[0][9]) + "H", "0x09", "EECon2",
                                        String.valueOf(DecodeDraft.ram[1][9]) + "H" },
                                { "0x0A", "PCLATH", String.valueOf(DecodeDraft.ram[0][10]) + "H", "0x0A", "PCLATCH",
                                        String.valueOf(DecodeDraft.ram[1][10]) + "H" },
                                { "0x0B", "INTCON", String.valueOf(DecodeDraft.ram[0][11]) + "H", "0x0B", "INTCON",
                                        String.valueOf(DecodeDraft.ram[1][11]) + "H" }, },
                        new String[] { "Adresse", "Bez", "Werte Binär", "Adresse", "Bez", "Werte" }));
        scrollPane_6.setViewportView(sfr_table);

        JLabel lblNewLabel = new JLabel("Bank 0");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblNewLabel.setBounds(69, 16, 46, 14);
        panel_sfr.add(lblNewLabel);

        JLabel lblNewLabel_11 = new JLabel("Bank 1");
        lblNewLabel_11.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblNewLabel_11.setBounds(236, 16, 46, 14);
        panel_sfr.add(lblNewLabel_11);

        JPanel panel_stack = new JPanel();
        tabbedPane_1.addTab("Stack", null, panel_stack, null);
        panel_stack.setLayout(null);

        JScrollPane scrollPane_5 = new JScrollPane();
        scrollPane_5.setBounds(10, 11, 339, 366);
        panel_stack.add(scrollPane_5);

        // Wir speichern programmzähler der verlassen wurde
        stack_table = new JTable();
        stack_table
                .setModel(new DefaultTableModel(
                        new Object[][] { { "0", "0" }, { "1", "0" }, { "2", "0" }, { "3", "0" }, { "4", "0" },
                                { "5", "0" }, { "6", "0" }, { "7", "0" }, },
                        new String[] { "Nummer", "Programmzähler" }));
        scrollPane_5.setViewportView(stack_table);

        JPanel panel_io = new JPanel();
        tabbedPane_1.addTab("I/O-Pins", null, panel_io, null);
        panel_io.setLayout(null);

        JScrollPane scrollPane_4 = new JScrollPane();
        scrollPane_4.setBounds(10, 11, 339, 366);
        panel_io.add(scrollPane_4);

        io_table = new JTable();
        io_table.setModel(new DefaultTableModel(
                new Object[][] { { "0", "i", ioPinsDataA[0], "0", "i", ioPinsDataB[0] },
                        { "1", "i", ioPinsDataA[1], "1", "i", ioPinsDataB[1] },
                        { "2", "i", ioPinsDataA[2], "2", "i", ioPinsDataB[2] },
                        { "3", "i", ioPinsDataA[3], "3", "i", ioPinsDataB[3] },
                        { "4", "i", ioPinsDataA[4], "4", "i", ioPinsDataB[4] },
                        { "5", "i", ioPinsDataA[5], "5", "i", ioPinsDataB[5] },
                        { "6", "i", ioPinsDataA[6], "6", "i", ioPinsDataB[6] },
                        { "7", "i", ioPinsDataA[7], "7", "i", ioPinsDataB[7] } },
                new String[] { "RA", "Tris", "Pin", "RB", "Tris", "Pin" }));
        // Set custom cell renderer and editor
        for (int i = 0; i < io_table.getColumnCount(); i++) {
            io_table.getColumnModel().getColumn(i).setCellRenderer(new ToggleCellRenderer());
            io_table.getColumnModel().getColumn(i).setCellEditor(new ToggleCellEditor(i, ioPinsDataA, ioPinsDataB));
        }
        // make ioTable visible
        scrollPane_4.setViewportView(io_table);

        // io_table.getColumnModel().getColumn(2).;

        JLabel lblNewLabel_12 = new JLabel("Quartzfrequenz");
        lblNewLabel_12.setBounds(870, 45, 89, 14);
        contentPane.add(lblNewLabel_12);

        JLabel lblNewLabel_13 = new JLabel("Laufzeit");
        lblNewLabel_13.setBounds(1111, 45, 46, 14);
        contentPane.add(lblNewLabel_13);

        // Buttons müssen unterhalb Labels ansonsten werden die nicht erkennt wenn die
        // erst unterhalb deklariert werden
        /*
         * 
         * DecodeDraft backend = new DecodeDraft();
         */
        JButton btnRun = new JButton("Run");
        btnRun.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                DecodeDraft.resetValue = false;
                /*
                 * thread2 = new Thread(() -> { if ((DecodeDraft.ram[1][1] & 0b0010_0000) ==
                 * 0b0010_0000) { if ((DecodeDraft.ram[1][1] & 0b0001_0000) == 0b0000_0000) { if
                 * ((ioPinsDataA[4] == 1) && ((DecodeDraft.ram[0][5] & 0b0001_0000) ==
                 * 0b0000_0000)) { ++timer0; }
                 * 
                 * } else { if ((ioPinsDataA[4] == 0) && ((DecodeDraft.ram[0][5] & 0b0001_0000)
                 * == 0b0001_0000)) { ++timer0; } }
                 * 
                 * }
                 * 
                 * 
                 * if ((DecodeDraft.ram[1][1] & 0b0010_0000) == 0b0010_0000) { if
                 * (((DecodeDraft.ram[1][1] & 0b0001_0000) == 0b0001_0000)) { if
                 * (((ioPinsDataA[3]) == 1) && (DecodeDraft.ram[0][5] & 0b0001_0000) == 0) { //
                 * low->high
                 * 
                 * DecodeDraft.ram[0][1] = timer0++; } if (((DecodeDraft.ram[1][1] &
                 * 0b0001_0000) == 0b0000_0000)) { if (((ioPinsDataA[3]) == 0) && (ra4 == 1)) {
                 * // low->high
                 * 
                 * DecodeDraft.ram[0][1] = timer0++; } } } }
                 * 
                 * });
                 */

                thread = new Thread(() -> {

                    while (!DecodeDraft.resetValue) {
                        run_N_Step();
                        if (isBreakpointSet(DecodeDraft.pC_Next)) {
                            runCounter = false;
                            DecodeDraft.resetValue = true;
                            break;
                        }
                        try {
                            Thread.sleep((long) (400 / slider.getValue()));
                        } catch (InterruptedException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        table_1.scrollRectToVisible(table_1.getCellRect(row + 1, 0, true));
                    }

                });
                if (runCounter != true) {
                    thread.start();
                    runCounter = true;

                }
                // WErte im Stack Speichern

                // System.out.println(decoder.getStack()[0]); -> 0

                /*
                 * do {
                 * 
                 * 
                 * int a= Integer.parseInt(extracted.get(backend.getpC()));
                 * backend.literalbefehl(a); }while(backend.getpC()!=6);
                 */
            }
        });
        btnRun.setBounds(65, 61, 89, 23);
        contentPane.add(btnRun);

        JButton btnStop = new JButton("Stop");
        btnStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DecodeDraft.resetValue = true;
                runCounter = false;
            }
        });
        btnStop.setBounds(205, 61, 89, 23);
        contentPane.add(btnStop);

        JButton btnStep = new JButton("Step");
        btnStep.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DecodeDraft.resetValue = true;
                runCounter = false;

                run_N_Step();
                table_1.scrollRectToVisible(table_1.getCellRect(row + 1, 0, true));
                // https://www.tutorialspoint.com/how-to-highlight-a-row-in-a-table-with-java-swing

                /*
                 * * thread2 = new Thread(() -> {
                 * 
                 * 
                 * // sfr_table.setValueAt(String.valueOf(DecodeDraft.ram[0][0]), 1, 3);
                 * 
                 * });
                 */

                // thread.start();

            }
        });
        btnStep.setBounds(340, 61, 89, 23);
        contentPane.add(btnStep);
        /*
         * for (ActionListener al : btnStop.getActionListeners()) {
         * al.actionPerformed(null); }
         */

        JButton btnReset = new JButton("Reset");
        btnReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // alle werte zurücksetzen

                reset();
            }
        });
        btnReset.setBounds(467, 61, 89, 23);
        contentPane.add(btnReset);

        // TF für Slider
        /*
         * tf_slider = new JTextField(); tf_slider.setFont(new Font("Tahoma", Font.BOLD,
         * 11)); tf_slider.setBounds(639, 30, 86, 20); contentPane.add(tf_slider);
         * tf_slider.setColumns(10);
         */

        /*
         * temporärer btn für Slider JButton btnSlider = new JButton("Slide");
         * btnSlider.addActionListener(new ActionListener() { public void
         * actionPerformed(ActionEvent e) { //Gets value of Slider and puts in textfield
         * long num1; num1 = slider.getValue(); tf_slider.setText(Long.toString(num1));
         * } }); btnSlider.setBounds(735, 27, 89, 23); contentPane.add(btnSlider);
         */

    }

    // input Dateipfad der LST Datei durch den FileBtn in der GUI
    private void displayDataInTable(String filePath) {
        // readDataFromFile(filePath);
        newFileReset();
        parse(filePath); // displays the lst file in the table and extracts commands
        console_area.append(filePath);// eingelesene Datei in Console Ausgeben
        System.out.println("Filepath in the display to table method: " + filePath);

        // List<String[]> data = readDataFromFile(filePath);
        // String[] columnNames = { "Address", "Instruction" }; /spalte
        // DefaultTableModel model = new DefaultTableModel(data.toArray(new
        // Object[0][]), columnNames);
        // table.setModel(model);

    }

    public void run_N_Step() {

        table_1.clearSelection();
        sfr_table.clearSelection();
        gpr_table.clearSelection();

        ra4 = ioPinsDataA[3];
        int pcCheck = DecodeDraft.pC_Next;
        try {
            int instructionCode = newOP.get(DecodeDraft.pC_Next);
            int opCode = instructionCode & 0b0011_1000_0000_0000;
            int literalCode = instructionCode & 0b0000_0111_1111_1111;
            if (DecodeDraft.ram[DecodeDraft.rb0][1] < 4 && DecodeDraft.ram[DecodeDraft.rb0][1] > 0) {
                if (opCode == 0b0010_1000_0000_0000 || opCode == 0b0010_0000_0000_0000) {
                    DecodeDraft.pC_Next = ((DecodeDraft.ram[DecodeDraft.rb0][10] & 0b11000) << 8) | literalCode;
                }
            }

            int destBit = instructionCode & 0b0000_0000_1000_0000;
            int fileCode = instructionCode & 0b0000_0000_0111_1111;
            if (fileCode == 2 && destBit == 0b0000_0000_1000_0000) {
                otherPc = true;
            }
            // DecodeDraft.decode(DecodeDraft.execute.get( DecodeDraft.pC_Next));
            DecodeDraft.decode(newOP.get(DecodeDraft.pC_Next));
            DecodeDraft.ram[1][2] = DecodeDraft.pC_Next & 0b1111_1111;
            DecodeDraft.ram[0][2] = DecodeDraft.pC_Next & 0b1111_1111;
            if (otherPc) {
                DecodeDraft.pC_Next = DecodeDraft.wRegister
                        + (((DecodeDraft.ram[DecodeDraft.rb0][10]) << 8) | DecodeDraft.ram[DecodeDraft.rb0][2]);
                DecodeDraft.runtime += (4.0 / quartz);

                timer0++;

                otherPc = false;

            }

        } catch (NullPointerException e) {
            DecodeDraft.resetValue = true;
            JOptionPane.showMessageDialog(null, "Du musst erster ein Programm laden!!!", "Error",
                    JOptionPane.ERROR_MESSAGE);

        }
        if (DecodeDraft.t0IntJumpcheck) {
            DecodeDraft.t0IntJumpcheck = false;
            DecodeDraft.stack[DecodeDraft.stackpointer++] = DecodeDraft.pC_Next;
            DecodeDraft.stackpointer %= 8;
            DecodeDraft.pC_Next = 4;
        }
        if ((DecodeDraft.ram[1][1] & 0b0010_0000) == 0b0000_0000) {
            // int prescaler = 2;

            if ((DecodeDraft.ram[1][1] & 0b0000_1000) == 0b0000_0000 && PSAone) {
                PSAone = false;
                if ((DecodeDraft.ram[1][1] & 0b0000_0001) == 0b0000_0001) {
                    prescaler *= 2;
                }
                if ((DecodeDraft.ram[1][1] & 0b0000_0010) == 0b0000_0010) {
                    prescaler *= 4;
                }
                if ((DecodeDraft.ram[1][1] & 0b0000_0100) == 0b0000_0100) {
                    prescaler *= 16;
                }
            }

            System.out.println(timer0 % prescaler);
            if (DecodeDraft.ram[DecodeDraft.rb0][11] != 0b0010_0100) {

                if (++timer0 > prescaler - 1) {
                    timer0 = timer0 % prescaler;
                    DecodeDraft.ram[0][1] = (DecodeDraft.ram[0][1] + 1) % 256;
                }
            }
        }
        for (int j = 0; j < 8; j++) {
            int ioPinValue = DecodeDraft.ram[0][5] >> j;
            ioPinValue = ioPinValue & 0b0000_0001;
            ioPinsDataA[j] = ioPinValue;
            
            if (!(j == 0 && ((DecodeDraft.ram[1][5] & 0b0000_0001) == 1))) {
                io_table.setValueAt(ioPinValue, j, 2);
            } else {
                ra0 = ioPinValue;
                ra0Check = true;

            }
            if ((j == 0 && ((DecodeDraft.ram[1][5] & 0b0000_0001) == 0) && ra0Check)) {
                io_table.setValueAt(ra0, j, 2);
                ra0Check = false;
            }

            int ioPinValue2 = DecodeDraft.ram[0][6] >> j;
            ioPinValue2 = ioPinValue2 & 0b0000_0001;
            ioPinsDataA[j] = ioPinValue2;
            
            if (!(j == 7 && (((DecodeDraft.ram[1][6] & 0b1000_0000 )>> 7) == 1))) {
                io_table.setValueAt(ioPinValue2, j, 5);
            } else {
                ra7 = ioPinValue2;
                ra7Check = true;

            }
            if ((j == 7 && (((DecodeDraft.ram[1][6] & 0b1000_0000 )>> 7) == 0) && ra7Check)) {
                io_table.setValueAt(ra7, j, 5);
                ra7Check = false;
            }
        }

        for (int j = 0; j < 8; j++) {

            stack_table.setValueAt(String.format("%02X", DecodeDraft.stack[j]).toUpperCase() + "H", j, 1);
        }

        lbw.setText(Integer.toHexString(DecodeDraft.wRegister).toUpperCase() + "H");
        if (DecodeDraft.wRegister != 0) {
            lbw.setForeground(Color.BLUE);
        } else {
            lbw.setForeground(Color.BLACK);
        }

        lbpc.setText(Integer.toHexString(DecodeDraft.pC_Next).toUpperCase() + "H");

        if (DecodeDraft.pC_Next != 0) {
            lbpc.setForeground(Color.BLUE);
        } else {
            lbpc.setForeground(Color.BLACK);
        }

        // lbpcl.setText(Integer.toHexString(timer0).toUpperCase() + "H");
        // lbpclath.setText(String.valueOf());
        lbc.setText(String.valueOf(DecodeDraft.ram[DecodeDraft.rb0][3] & 0b0000_0001));
        if ((DecodeDraft.ram[DecodeDraft.rb0][3] & 0b0000_0001) != 0) {
            lbc.setForeground(Color.BLUE);
        } else {
            lbc.setForeground(Color.BLACK);
        }

        lbdc.setText(String.valueOf((DecodeDraft.ram[DecodeDraft.rb0][3] & 0b0000_0010) >> 1));
        if (((DecodeDraft.ram[DecodeDraft.rb0][3] & 0b0000_0010) >> 1) != 0) {
            lbdc.setForeground(Color.BLUE);
        } else {
            lbdc.setForeground(Color.BLACK);
        }

        for (int p = 0; p < 8; p++) {
            int maske = 1 << p;
            if ((DecodeDraft.ram[1][5] & maske) == maske) {

                io_table.setValueAt('i', p, 1);

            } else {

                io_table.setValueAt('o', p, 1);
            }

        }

        for (int p = 0; p < 8; p++) {
            int maske = 1 << p;
            if ((DecodeDraft.ram[1][6] & maske) == maske) {

                io_table.setValueAt('i', p, 4);

            } else {

                io_table.setValueAt('o', p, 4);
            }

        }

        lbz.setText(String.valueOf((DecodeDraft.ram[DecodeDraft.rb0][3] & 0b0000_0100) >> 2));
        if ((DecodeDraft.ram[DecodeDraft.rb0][3] & 0b0000_0100) >> 2 != 0) {
            lbz.setForeground(Color.BLUE);
        } else {
            lbz.setForeground(Color.BLACK);
        }

        sfrUpdate();
        for (int j = 12; j < DecodeDraft.ram[0].length - 12; j++) {
            gpr_table.setValueAt(String.format("%02X", DecodeDraft.ram[0][j]).toUpperCase() + "H", j - 12, 1);
            if (DecodeDraft.ram[0][j] != 0) {
                // gpr_table.addRowSelectionInterval(j, j);
                gpr_table.addRowSelectionInterval(j - 12, j - 12);
            }
            gpr_table.setValueAt(String.format("%02X", DecodeDraft.ram[1][j]).toUpperCase() + "H", j - 12, 3);
            if (DecodeDraft.ram[1][j] != 0) {
                // gpr_table.addRowSelectionInterval(j, j);
                gpr_table.addRowSelectionInterval(j - 12, j - 12);
            }
        }
        row += DecodeDraft.pC_Next - pcCheck;
        row = rightRow(DecodeDraft.pC_Next);

        // Markiere die Zeile in der Tabelle
        if (row >= 0) {
            table_1.addRowSelectionInterval(row, row);

        }

        lbl_laufzeit.setText(String.valueOf(String.format("%.02f", DecodeDraft.runtime)));
    }

    public void sfrUpdate() {
        DecodeDraft.ram[0][10] %= 32;
        DecodeDraft.ram[1][10] %= 32;
        for (int j = 0; j < 12; j++) {

            sfr_table.setValueAt(String.format("%02X", DecodeDraft.ram[0][j]).toUpperCase() + "H", j, 2);
            if (DecodeDraft.ram[0][j] != 0) {
                sfr_table.addRowSelectionInterval(j, j);

            }
            sfr_table.setValueAt(String.format("%02X", DecodeDraft.ram[1][j]).toUpperCase() + "H", j, 5);
            if (DecodeDraft.ram[1][j] != 0) {
                // gpr_table.addRowSelectionInterval(j, j);
                sfr_table.addRowSelectionInterval(j, j);
            }
        }
    }

    public void reset() {
        DecodeDraft.resetValue = true;
        table_1.clearSelection();
        sfr_table.clearSelection();
        gpr_table.clearSelection();
        row = firstRow;

        lbw.setForeground(Color.BLACK);
        lbz.setForeground(Color.BLACK);
        lbpc.setForeground(Color.BLACK);
        lbc.setForeground(Color.BLACK);
        lbdc.setForeground(Color.BLACK);

        DecodeDraft.ram[1][1] = 0b1111_1111;
        runCounter = false;
        DecodeDraft.carrybit = 0;
        DecodeDraft.digitcarrybit = 0;
        DecodeDraft.pC_Current = 0;
        DecodeDraft.pC_Next = 0;
        // Arrays.fill(DecodeDraft.EEROM, 0);
        for (int i = 0; i < DecodeDraft.ram.length; i++) {
            for (int j = 0; j < DecodeDraft.ram[i].length; j++) {
                if (!(i == 0 && j == 1)) {
                    DecodeDraft.ram[i][j] = 0;
                }
            }
        }

        DecodeDraft.ram[1][5] = 0b1111_1111;
        DecodeDraft.ram[1][6] = 0b1111_1111;

        Arrays.fill(DecodeDraft.stack, 0);
        DecodeDraft.rb0 = 0;
        DecodeDraft.stackpointer = 0;
        DecodeDraft.wRegister = 0;
        DecodeDraft.zerobit = 0;
        DecodeDraft.runtime = 0;
        DecodeDraft.endOfProgrammCheck = 0;
        lbl_laufzeit.setText("0");
        console_area.setText("");
        state_area.setText("");

        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[0]) + "H", 0, 1);
        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[1]) + "H", 1, 1);
        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[2]) + "H", 2, 1);
        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[3]) + "H", 3, 1);
        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[4]) + "H", 4, 1);
        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[5]) + "H", 5, 1);
        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[6]) + "H", 6, 1);
        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[7]) + "H", 7, 1);

        DecodeDraft.ram[1][1] = 0b1111_1111;
        timer0 = 0;
        for (int j = 0; j < 12; j++) {
            sfr_table.setValueAt(String.format("%02X", DecodeDraft.ram[0][j]).toUpperCase() + "H", j, 2);
            sfr_table.setValueAt(String.format("%02X", DecodeDraft.ram[1][j]).toUpperCase() + "H", j, 5);

        }

        for (int j = 12; j < 68; j++) {
            gpr_table.setValueAt(String.format("%02X", DecodeDraft.ram[0][j]).toUpperCase() + "H", j - 12, 1);
            gpr_table.setValueAt(String.format("%02X", DecodeDraft.ram[1][j]).toUpperCase() + "H", j - 12, 3);
        }

        for (int j = 0; j < 8; j++) {
            io_table.setValueAt('i', j, 1);
            io_table.setValueAt('i', j, 4);
        }
        lbw.setText(String.valueOf(DecodeDraft.wRegister) + "H");
        lbpc.setText(String.valueOf(DecodeDraft.pC_Next));
        lbpcl.setText(String.valueOf(timer0));
        // lbpclath.setText(String.valueOf(DecodeDraft.));
        lbc.setText(String.valueOf(DecodeDraft.carrybit));
        lbdc.setText(String.valueOf(DecodeDraft.digitcarrybit));
        lbz.setText(String.valueOf(DecodeDraft.zerobit));
        try {
            table_1.addRowSelectionInterval(row, row);
            table_1.scrollRectToVisible(table_1.getCellRect(row, 0, true));
        } catch (IllegalArgumentException e) {
            DecodeDraft.resetValue = true;
            JOptionPane.showMessageDialog(null, "Du musst erster ein Programm laden!!!", "Error",
                    JOptionPane.ERROR_MESSAGE);

        }

    }

    public void newFileReset() {

        DecodeDraft.resetValue = true;
        befehleInteger.clear();
        befehle.clear();
        newOP.clear();
        table_1.clearSelection();
        sfr_table.clearSelection();
        gpr_table.clearSelection();
        row = firstRow;

        lbw.setForeground(Color.BLACK);
        lbz.setForeground(Color.BLACK);
        lbpc.setForeground(Color.BLACK);
        lbc.setForeground(Color.BLACK);
        lbdc.setForeground(Color.BLACK);

        for (int j = 0; j < 8; j++) {
            io_table.setValueAt('i', j, 1);
            io_table.setValueAt('i', j, 4);
        }

        timer0 = 0;
        runCounter = false;
        DecodeDraft.carrybit = 0;
        DecodeDraft.digitcarrybit = 0;
        DecodeDraft.pC_Current = 0;
        DecodeDraft.pC_Next = 0;
        // Arrays.fill(DecodeDraft.EEROM, 0);
        for (int i = 0; i < DecodeDraft.ram.length; i++) {
            for (int j = 0; j < DecodeDraft.ram[i].length; j++) {
                DecodeDraft.ram[i][j] = 0;
            }
        }
        DecodeDraft.ram[1][1] = 0b1111_1111;
        DecodeDraft.ram[1][5] = 0b1111_1111;
        DecodeDraft.ram[1][6] = 0b1111_1111;

        Arrays.fill(DecodeDraft.stack, 0);
        DecodeDraft.rb0 = 0;
        DecodeDraft.stackpointer = 0;
        DecodeDraft.wRegister = 0;
        DecodeDraft.zerobit = 0;
        DecodeDraft.runtime = 0;
        DecodeDraft.endOfProgrammCheck = 0;
        lbl_laufzeit.setText("0");
        console_area.setText("");
        state_area.setText("");

        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[0]) + "H", 0, 1);
        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[1]) + "H", 1, 1);
        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[2]) + "H", 2, 1);
        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[3]) + "H", 3, 1);
        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[4]) + "H", 4, 1);
        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[5]) + "H", 5, 1);
        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[6]) + "H", 6, 1);
        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[7]) + "H", 7, 1);

        for (int j = 0; j < 8; j++) {
            ioPinsDataA[j] = 0;
            ioPinsDataB[j] = 0;
        }

        for (int j = 0; j < 12; j++) {
            sfr_table.setValueAt(String.format("%02X", DecodeDraft.ram[0][j]).toUpperCase() + "H", j, 2);
            sfr_table.setValueAt(String.format("%02X", DecodeDraft.ram[1][j]).toUpperCase() + "H", j, 5);

        }

        for (int j = 12; j < 68; j++) {
            gpr_table.setValueAt(String.format("%02X", DecodeDraft.ram[0][j]).toUpperCase() + "H", j - 12, 1);
            gpr_table.setValueAt(String.format("%02X", DecodeDraft.ram[1][j]).toUpperCase() + "H", j - 12, 3);
        }

        lbw.setText(String.valueOf(DecodeDraft.wRegister) + "H");
        lbpc.setText(String.valueOf(DecodeDraft.pC_Current));
        // lbpcl.setText(String.valueOf(timer0));
        // lbpclath.setText(String.valueOf(DecodeDraft.));
        lbc.setText(String.valueOf(DecodeDraft.carrybit));
        lbdc.setText(String.valueOf(DecodeDraft.digitcarrybit));
        lbz.setText(String.valueOf(DecodeDraft.zerobit));
        // table_1.addRowSelectionInterval(row, row);
    }

    public static void updateArray(int row, int column, Object value) {
        if (column == 2) {
            ioPinsDataA[row] = Integer.parseInt(value.toString());
            System.out.println("Updated ioPinsDataA[" + row + "]: " + ioPinsDataA[row]);
        } else if (column == 5) {
            ioPinsDataB[row] = Integer.parseInt(value.toString());
            System.out.println("Updated ioPinsDataB[" + row + "]: " + ioPinsDataB[row]);
        }
    }
    /*
     * Already covered in parse
     * 
     * private List<String[]> readDataFromFile(String filePath) { List<String[]>
     * data = new ArrayList<>(); try { BufferedReader br = new BufferedReader(new
     * FileReader(filePath)); String lineStr = br.readLine(); // Read the first line
     * from the file System.out.println("lineStr: " + lineStr); // Print the line
     * read from the file
     * 
     * // Split the line using a regular expression that matches one or more
     * whitespace // characters String[] line = lineStr.trim().split("\\s+");
     * System.out.println("Number of elements in line array: " + line.length);
     * 
     * } catch (IOException e) { e.printStackTrace(); // Handle file reading errors
     * } return data; }
     */

    // Displayed die LST Datei in der Tabelle table_1 und extrahiert die Befehle aus
    // der LST Datei als String
    public List<String> parse(String lstFile) {
        DefaultTableModel model = (DefaultTableModel) table_1.getModel();
        model.setRowCount(0);
        // Wieso codeLines
        List<String> codeLines = new ArrayList<>();
        // List<String> befehle = new ArrayList<>();
        Font newFont = new Font("Bold", Font.BOLD, 10);
        table_1.setFont(newFont);

        try (BufferedReader br = new BufferedReader(new FileReader(lstFile))) {
            String currentLine;
            String pc = "";
            String hexCode = "";
            String progRow = "";
            String instr = "";

            while ((currentLine = br.readLine()) != null) {

                if (currentLine.substring(0, 4).length() > 0) {
                    pc = currentLine.substring(0, 4);

                }
                if (currentLine.substring(5, 9).length() > 0) {
                    hexCode = currentLine.substring(5, 9);
                }

                if (currentLine.substring(20, 25).length() > 0) {
                    progRow = currentLine.substring(10, 25).trim();
                }

                if (currentLine.substring(25, currentLine.length()).length() > 0) {
                    instr = currentLine.substring(25, currentLine.length());
                }

                codeLines.add(currentLine);
                befehle.add(currentLine.substring(5, 9));
                if (!currentLine.substring(0, 4).equals("    ")) {
                    System.out.println(currentLine.substring(0, 4));
                    newOP.put(Integer.parseInt(currentLine.substring(0, 4), 16),
                            Integer.parseInt(currentLine.substring(5, 9), 16));
                    // System.out.println("Current linr:" + currentLine.substring(5, 9));
                }
                model.addRow(new Object[] { Boolean.FALSE, pc, hexCode, progRow, instr });

                // TODO: System.out.println(sCurrentLine);
            }

            row = rightRow(DecodeDraft.pC_Next);
            table_1.addRowSelectionInterval(row, row);
            table_1.scrollRectToVisible(table_1.getCellRect(row, 1, true));
            convertHexToInt(befehle);
            // liest eingelesene Integer List Befehle ein und übergibt diese der internen
            // List von DecodeDraft
            DecodeDraft.execute = befehleInteger;
            System.out.println("lst");

        } catch (IOException e) {
            // TODO: Log
            e.printStackTrace();
        }

        return codeLines;
    }

    // Input List<String> zahlen als hex
    // Methode initialisiert befehleInteger variable mit den entsprechenden
    // Befewhlen. oder leerem string
    // List<Integer> befehleInteger = new ArrayList<>();
    public void convertHexToInt(List<String> hexList) {
        for (String hex : hexList) {
            // Zeilen überspring da wo kein Befehl. Leerer String kann nicht tu Zahl
            // konvertiert werden
            if (hex.trim().isEmpty()) {
                continue;
            }

            try {
                int decimalValue = Integer.parseInt(hex, 16);
                befehleInteger.add(decimalValue);
            } catch (Exception e) {

                System.err.println("Ungültiget HEX String: " + hex);
            }

        }

        ArrayList<Integer> debug = befehleInteger;

        // Elemente printen
        for (Integer element : befehleInteger) {
            System.out.println("Dezimalwert: " + element);
        }
        for (Integer intValue : befehleInteger) {
            console_area.append(intValue.toString() + "\n");
        }

    }

    class ToggleCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(DefaultTableCellRenderer.CENTER); // Center-align the cell's text
            setText(value == null ? "" : value.toString());
            return this;
        }
    }

    class ToggleCellEditor extends AbstractCellEditor implements TableCellEditor {
        private JButton button;
        private int rowIndex;
        private int columnIndex;
        private int[] ioPinsDataA;
        private int[] ioPinsDataB;

        public ToggleCellEditor(int columnIndex, int[] ioPinsDataA, int[] ioPinsDataB) {
            this.columnIndex = columnIndex;
            this.ioPinsDataA = ioPinsDataA;
            this.ioPinsDataB = ioPinsDataB;

            if (columnIndex == 2 || columnIndex == 5) {
                button = new JButton("0"); // Set default value to "0"
                button.setBorderPainted(false);
                button.addActionListener(e -> {
                    if (button.getText().equals("1")) {
                        if (columnIndex == 2) {
                            ioPinsDataA[rowIndex] = 0;
                            System.out.println("Updated ioPinsDataA[" + rowIndex + "]: 0");

                            DecodeDraft.ram[0][5] &= (ioPinsDataA[rowIndex] << rowIndex);

                            if (((DecodeDraft.ram[1][1] & 0b0010_0000) == 0b0010_0000) && rowIndex == 4) {
                                if ((DecodeDraft.ram[1][1] & 0b0001_0000) == 0b0001_0000) {
                                    if (DecodeDraft.ram[DecodeDraft.rb0][11] != 0b0010_0100) {

                                        DecodeDraft.ram[0][1] += 1;
                                    }

                                    sfrUpdate();
                                }

                            }

                        } else if (columnIndex == 5) {
                            ioPinsDataB[rowIndex] = 0;
                            DecodeDraft.ram[0][6] &= (ioPinsDataB[rowIndex] << rowIndex);
                            sfrUpdate();
                            System.out.println("Updated ioPinsDataB[" + rowIndex + "]: 0");
                        }
                        sfrUpdate();
                        button.setText("0");
                    } else {
                        if (columnIndex == 2) {
                            ioPinsDataA[rowIndex] = 1;
                            System.out.println("Updated ioPinsDataA[" + rowIndex + "]: 1");

                            DecodeDraft.ram[0][5] |= (ioPinsDataA[rowIndex] << rowIndex);
                            sfrUpdate();
                            if (((DecodeDraft.ram[1][1] & 0b0010_0000) == 0b0010_0000) && rowIndex == 4) {
                                if ((DecodeDraft.ram[1][1] & 0b0001_0000) == 0b0000_0000) {

                                    DecodeDraft.ram[0][1] += 1;
                                    sfrUpdate();
                                }

                            }

                        } else if (columnIndex == 5) {
                            ioPinsDataB[rowIndex] = 1;
                            DecodeDraft.ram[0][6] |= (ioPinsDataB[rowIndex] << rowIndex);
                            System.out.println("Updated ioPinsDataB[" + rowIndex + "]: 1");
                        }
                        button.setText("1");
                        sfrUpdate();
                    }
                    fireEditingStopped();
                });
            } else {
                System.out.println("Invalid column");
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            this.rowIndex = row;
            this.columnIndex = column;
            if (column == 2 || column == 5) {
                button.setText(value == null ? "0" : value.toString()); // Set default value to "0"
            }

            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            if (e instanceof MouseEvent) {
                return ((MouseEvent) e).getClickCount() >= 1;
            }
            return true;
        }
    }

    private boolean isBreakpointSet(int line) {
        for (int i = 0; i < table_1.getRowCount(); i++) {
            Boolean breakpoint = (Boolean) table_1.getValueAt(i, 0);
            String pcValue = (String) table_1.getValueAt(i, 1).toString().substring(0, 4);
            try {
                if (breakpoint != null && breakpoint && pcValue != null && Integer.parseInt(pcValue, 16) == line) {
                    return true;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Wähle ein Breakpoint bei dem sich auch ein Befehl befindet! ",
                        "Error", JOptionPane.ERROR_MESSAGE);
                table_1.setValueAt(false, i, 0);
                reset();
                return false;
            }
        }
        return false;
    }

    private int rightRow(int line) {
        for (int i = 0; i < table_1.getRowCount(); i++) {
            String pcValue = (String) table_1.getValueAt(i, 1).toString().substring(0, 4);
            if (pcValue.trim().length() > 0 && Integer.parseInt(pcValue, 16) == line) {
                if (line == 0) {
                    firstRow = i;
                }
                return i;
            }
        }
        return -1;
    }
}
