import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

//add row https://www.youtube.com/watch?v=eAJphO_PHTU&t=64s

public class PICGUI extends JFrame {
    
    private long start = System.nanoTime();
    // private static int[] ra_pins = new int[8];
    // private static int[] rb_pins = new int[8];

    public static int[] ioPinsDataA = new int[8];
    public static int[] ioPinsDataB = new int[8];
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
            reset();
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(contentPane);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    System.out.println("Datei wurde gefunden:" + filePath);
                    displayDataInTable(filePath);
                }
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
        table_1.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "BP", "" }) {
            Class[] columnTypes = new Class[] { Boolean.class, String.class };

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
        TableColumn TC_lst = colmod.getColumn(1);
        TC_lst.setPreferredWidth(800);

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

         lbw = new JLabel("0");

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
        scrollPane_3.setBounds(10, 11, 339, 366);
        panel_gpr.add(scrollPane_3);

        gpr_table = new JTable();
        gpr_table
                .setModel(new DefaultTableModel(
                        new Object[][] { { "0x0C", "0H" }, { "0x0D", "0H" }, { "0x0E", "0H" }, { "0x0F", "0H" },

                                { "0x10", "0H" }, { "0x11", "0H" }, { "0x12", "0H" }, { "0x13", "0H" },
                                { "0x14", "0H" }, { "0x15", "0H" }, { "0x16", "0H" }, { "0x17", "0H" },
                                { "0x18", "0H" }, { "0x19", "0H" }, { "0x1A", "0H" }, { "0x1B", "0H" },
                                { "0x1C", "0H" }, { "0x1D", "0H" }, { "0x1E", "0H" }, { "0x1F", "0H" },

                                { "0x20", "0H" }, { "0x21", "0H" }, { "0x22", "0H" }, { "0x23", "0H" },
                                { "0x24", "0H" }, { "0x25", "0H" }, { "0x26", "0H" }, { "0x27", "0H" },
                                { "0x28", "0H" }, { "0x29", "0H" }, { "0x2A", "0H" }, { "0x2B", "0H" },
                                { "0x2C", "0H" }, { "0x2D", "0H" }, { "0x2E", "0H" }, { "0x2F", "0H" },

                                { "0x30", "0H" }, { "0x31", "0H" }, { "0x32", "0H" }, { "0x33", "0H" },
                                { "0x34", "0H" }, { "0x35", "0H" }, { "0x36", "0H" }, { "0x37", "0H" },
                                { "0x38", "0H" }, { "0x39", "0H" }, { "0x3A", "0H" }, { "0x3B", "0H" },
                                { "0x3C", "0H" }, { "0x3D", "0H" }, { "0x3E", "0H" }, { "0x3F", "0H" },

                                { "0x40", "0H" }, { "0x41", "0H" }, { "0x42", "0H" }, { "0x43", "0H" },
                                { "0x44", "0H" }, { "0x45", "0H" }, { "0x46", "0H" }, { "0x47", "0H" },
                                { "0x48", "0H" }, { "0x49", "0H" }, { "0x4A", "0H" }, { "0x4B", "0H" },
                                { "0x4C", "0H" }, { "0x4D", "0H" }, { "0x4E", "0H" }, { "0x4F", "0H" }, },
                        new String[] { "Addresse", "Werte HEX" }));
        scrollPane_3.setViewportView(gpr_table);
        // gpr_table.setTableHeader(null);//Header entfernen

        JPanel panel_sfr = new JPanel();
        tabbedPane_1.addTab("SFR", null, panel_sfr, null);
        panel_sfr.setLayout(null);

        JScrollPane scrollPane_6 = new JScrollPane();
        scrollPane_6.setBounds(10, 11, 339, 224);
        panel_sfr.add(scrollPane_6);

        sfr_table = new JTable();
        sfr_table.setModel(new DefaultTableModel(
                new Object[][] {
                        { "0x00", "indirect", String.valueOf(DecodeDraft.ram[0][0]), "0x00", "indirect",
                                String.valueOf(DecodeDraft.ram[1][0]) },
                        { "0x01", "TMR 0", String.valueOf(DecodeDraft.ram[0][1]), "0x01", "Option",
                                String.valueOf(DecodeDraft.ram[1][1]) },
                        { "0x02", "PCL", String.valueOf(DecodeDraft.ram[0][2]), "0x02", "PCL",
                                String.valueOf(DecodeDraft.ram[1][2]) },
                        { "0x03", "Status", String.valueOf(DecodeDraft.ram[0][3]), "0x03", "Status",
                                String.valueOf(DecodeDraft.ram[1][3]) },
                        { "0x04", "FSR", String.valueOf(DecodeDraft.ram[0][4]), "0x04", "FSR",
                                String.valueOf(DecodeDraft.ram[1][4]) },
                        { "0x05", "Port RA", String.valueOf(DecodeDraft.ram[0][5]), "0x05", "Tris RA",
                                String.valueOf(DecodeDraft.ram[1][5]) },
                        { "0x06", "Port RB", String.valueOf(DecodeDraft.ram[0][6]), "0x06", "Tris RB",
                                String.valueOf(DecodeDraft.ram[1][6]) },
                        { "0x07", "-", String.valueOf(DecodeDraft.ram[0][7]), "0x07", "-",
                                String.valueOf(DecodeDraft.ram[1][7]) },
                        { "0x08", "EEData", String.valueOf(DecodeDraft.ram[0][8]), "0x08", "EECon1",
                                String.valueOf(DecodeDraft.ram[1][8]) },
                        { "0x09", "EEAdr", String.valueOf(DecodeDraft.ram[0][9]), "0x09", "EECon2",
                                String.valueOf(DecodeDraft.ram[1][9]) },
                        { "0x0A", "PCLATH", String.valueOf(DecodeDraft.ram[0][10]), "0x0A", "PCLATCH",
                                String.valueOf(DecodeDraft.ram[1][10]) },
                        { "0x0B", "INTCON", String.valueOf(DecodeDraft.ram[0][11]), "0x0B", "INTCON",
                                String.valueOf(DecodeDraft.ram[1][11]) }, },
                new String[] { "Adresse", "Bez", "Werte Binär", "Adresse", "Bez", "Werte" }));
        scrollPane_6.setViewportView(sfr_table);

        JLabel lblNewLabel = new JLabel("Bank 0");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblNewLabel.setBounds(73, 246, 46, 14);
        panel_sfr.add(lblNewLabel);

        JLabel lblNewLabel_11 = new JLabel("Bank 1");
        lblNewLabel_11.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblNewLabel_11.setBounds(245, 246, 46, 14);
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
                DecodeDraft.resetValue = true;
                DecodeDraft.resetValue = false;

                thread = new Thread(() -> {

                    while (!DecodeDraft.resetValue) {
                        table_1.clearSelection();
                        int pcCheck = DecodeDraft.pC_Next;
                        DecodeDraft.decode(DecodeDraft.execute.get(DecodeDraft.pC_Next));
                        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[0]), 0, 1);
                        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[1]), 1, 1);
                        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[2]), 2, 1);
                        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[3]), 3, 1);
                        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[4]), 4, 1);
                        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[5]), 5, 1);
                        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[6]), 6, 1);
                        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[7]), 7, 1);

                        lbw.setText(Integer.toHexString(DecodeDraft.wRegister).toUpperCase() + "H");
                        lbpc.setText(String.valueOf(DecodeDraft.pC_Next));
                        lbpcl.setText(String.valueOf(DecodeDraft.pC_Next));
                        // lbpclath.setText(String.valueOf(DecodeDraft.));
                        lbc.setText(String.valueOf(DecodeDraft.carrybit));
                        lbdc.setText(String.valueOf(DecodeDraft.digitcarrybit));
                        lbz.setText(String.valueOf(DecodeDraft.zerobit));

                        for (int j = 0; j < 12; j++) {
                            sfr_table.setValueAt(Integer.toHexString(DecodeDraft.ram[0][j]).toUpperCase() + "H", j, 2);
                            sfr_table.setValueAt(Integer.toHexString(DecodeDraft.ram[1][j]).toUpperCase() + "H", j, 5);
                        }
                        for (int j = 12; j < 67; j++) {
                            gpr_table.setValueAt(Integer.toHexString(DecodeDraft.ram[0][j]).toUpperCase() + "H", j - 11,
                                    1);
                        }
                        if ((DecodeDraft.pC_Next == pcCheck) && (Integer.parseInt(
                                table_1.getValueAt(row, 1).toString().substring(0, 4),
                                16) != Integer.parseInt(table_1.getValueAt(row, 1).toString().substring(7, 9), 16))) {
                            row++;
                        }

                        row = rightRow(DecodeDraft.pC_Next);

                        // Markiere die Zeile in der Tabelle
                        if (row >= 0) {
                            table_1.addRowSelectionInterval(row, row);
                            // table_1.scrollRectToVisible(table_1.getCellRect(row, 1, true));
                        }
                        table_1.setBackground(Color.white);

                        lbl_laufzeit.setText(String.valueOf(String.format("%.02f", DecodeDraft.runtime)));
                        if (isBreakpointSet(DecodeDraft.pC_Next)) {
                            DecodeDraft.resetValue = true;
                            break;
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }

                    }

                });

                thread.start();

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

            }
        });
        btnStop.setBounds(205, 61, 89, 23);
        contentPane.add(btnStop);

        JButton btnStep = new JButton("Step");
        btnStep.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DecodeDraft.resetValue = true;

                thread2 = new Thread(() -> {
                    table_1.clearSelection();
                    int pcCheck = DecodeDraft.pC_Next;
                    DecodeDraft.decode(DecodeDraft.execute.get(DecodeDraft.pC_Next));
                    stack_table.setValueAt(String.valueOf(DecodeDraft.stack[0]), 0, 1);
                    stack_table.setValueAt(String.valueOf(DecodeDraft.stack[1]), 1, 1);
                    stack_table.setValueAt(String.valueOf(DecodeDraft.stack[2]), 2, 1);
                    stack_table.setValueAt(String.valueOf(DecodeDraft.stack[3]), 3, 1);
                    stack_table.setValueAt(String.valueOf(DecodeDraft.stack[4]), 4, 1);
                    stack_table.setValueAt(String.valueOf(DecodeDraft.stack[5]), 5, 1);
                    stack_table.setValueAt(String.valueOf(DecodeDraft.stack[6]), 6, 1);
                    stack_table.setValueAt(String.valueOf(DecodeDraft.stack[7]), 7, 1);

                    lbw.setText(Integer.toHexString(DecodeDraft.wRegister).toUpperCase() + "H");
                    lbpc.setText(String.valueOf(DecodeDraft.pC_Next));
                    lbpcl.setText(String.valueOf(DecodeDraft.pC_Next));
                    // lbpclath.setText(String.valueOf(DecodeDraft.));
                    lbc.setText(String.valueOf(DecodeDraft.carrybit));
                    lbdc.setText(String.valueOf(DecodeDraft.digitcarrybit));
                    lbz.setText(String.valueOf(DecodeDraft.zerobit));
                    for (int j = 0; j < 12; j++) {
                        sfr_table.setValueAt(Integer.toHexString(DecodeDraft.ram[0][j]).toUpperCase() + "H", j, 2);
                        sfr_table.setValueAt(Integer.toHexString(DecodeDraft.ram[1][j]).toUpperCase() + "H", j, 5);
                    }
                    for (int j = 12; j < 67; j++) {
                        gpr_table.setValueAt(Integer.toHexString(DecodeDraft.ram[0][j]).toUpperCase() + "H", j - 11, 1);
                    }
                    row += DecodeDraft.pC_Next - pcCheck;
                    row = rightRow(DecodeDraft.pC_Next);

                    // Markiere die Zeile in der Tabelle
                    if (row >= 0) {
                        table_1.addRowSelectionInterval(row, row);
                        // table_1.scrollRectToVisible(table_1.getCellRect(row, 1, true));
                    }

                    table_1.setBackground(Color.white);

                    lbl_laufzeit.setText(String.valueOf(String.format("%.02f", DecodeDraft.runtime)));

                    // https://www.tutorialspoint.com/how-to-highlight-a-row-in-a-table-with-java-swing
                });

                /*
                 * * thread2 = new Thread(() -> {
                 * 
                 * 
                 * // sfr_table.setValueAt(String.valueOf(DecodeDraft.ram[0][0]), 1, 3);
                 * 
                 * });
                 */

                // thread.start();
                thread2.start();

            }
        });
        btnStep.setBounds(340, 61, 89, 23);
        contentPane.add(btnStep);
        for (ActionListener al : btnStop.getActionListeners()) {
            al.actionPerformed(null);
        }

        JButton btnReset = new JButton("Reset");
        btnReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // alle werte zurücksetzen

                DecodeDraft.resetValue = true;
                table_1.clearSelection();
                row = firstRow;

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
                Arrays.fill(DecodeDraft.stack, 0);
                DecodeDraft.rb0 = 0;
                DecodeDraft.stackpointer = 0;
                DecodeDraft.wRegister = 0;
                DecodeDraft.zerobit = 0;
                DecodeDraft.runtime = 0;
                DecodeDraft.endOfProgrammCheck = 0;
                lbl_laufzeit.setText("0");
                console_area.setText("0");
                state_area.setText("0");

                stack_table.setValueAt(String.valueOf(DecodeDraft.stack[0]) + "H", 0, 1);
                stack_table.setValueAt(String.valueOf(DecodeDraft.stack[1]) + "H", 1, 1);
                stack_table.setValueAt(String.valueOf(DecodeDraft.stack[2]) + "H", 2, 1);
                stack_table.setValueAt(String.valueOf(DecodeDraft.stack[3]) + "H", 3, 1);
                stack_table.setValueAt(String.valueOf(DecodeDraft.stack[4]) + "H", 4, 1);
                stack_table.setValueAt(String.valueOf(DecodeDraft.stack[5]) + "H", 5, 1);
                stack_table.setValueAt(String.valueOf(DecodeDraft.stack[6]) + "H", 6, 1);
                stack_table.setValueAt(String.valueOf(DecodeDraft.stack[7]) + "H", 7, 1);

                for (int j = 0; j < 12; j++) {
                    sfr_table.setValueAt(String.valueOf(DecodeDraft.ram[0][j]) + "H", j, 2);
                    sfr_table.setValueAt(String.valueOf(DecodeDraft.ram[1][j]) + "H", j, 5);
                }
                for (int j = 12; j < 68; j++) {
                    gpr_table.setValueAt(String.valueOf(DecodeDraft.ram[0][j]) + "H", j - 11, 1);
                }

                lbw.setText(String.valueOf(DecodeDraft.wRegister) + "H");
                lbpc.setText(String.valueOf(DecodeDraft.pC_Current));
                lbpcl.setText(String.valueOf(DecodeDraft.pC_Current));
                // lbpclath.setText(String.valueOf(DecodeDraft.));
                lbc.setText(String.valueOf(DecodeDraft.carrybit));
                lbdc.setText(String.valueOf(DecodeDraft.digitcarrybit));
                lbz.setText(String.valueOf(DecodeDraft.zerobit));
                table_1.addRowSelectionInterval(row, row);
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
        
        
        parse(filePath); // displays the lst file in the table and extracts commands
        console_area.append(filePath);// eingelesene Datei in Console Ausgeben
        System.out.println("Filepath in the display to table method: " + filePath);

        // List<String[]> data = readDataFromFile(filePath);
        // String[] columnNames = { "Address", "Instruction" }; /spalte
        // DefaultTableModel model = new DefaultTableModel(data.toArray(new
        // Object[0][]), columnNames);
        // table.setModel(model);

    }
    public void reset() {

        DecodeDraft.resetValue = true;
        befehleInteger.clear();
        befehle.clear();
        table_1.clearSelection();
        row = firstRow;

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
        Arrays.fill(DecodeDraft.stack, 0);
        DecodeDraft.rb0 = 0;
        DecodeDraft.stackpointer = 0;
        DecodeDraft.wRegister = 0;
        DecodeDraft.zerobit = 0;
        DecodeDraft.runtime = 0;
        DecodeDraft.endOfProgrammCheck = 0;
        lbl_laufzeit.setText("0");
        console_area.setText("0");
        state_area.setText("0");

        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[0]) + "H", 0, 1);
        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[1]) + "H", 1, 1);
        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[2]) + "H", 2, 1);
        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[3]) + "H", 3, 1);
        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[4]) + "H", 4, 1);
        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[5]) + "H", 5, 1);
        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[6]) + "H", 6, 1);
        stack_table.setValueAt(String.valueOf(DecodeDraft.stack[7]) + "H", 7, 1);

        for (int j = 0; j < 12; j++) {
            sfr_table.setValueAt(String.valueOf(DecodeDraft.ram[0][j]) + "H", j, 2);
            sfr_table.setValueAt(String.valueOf(DecodeDraft.ram[1][j]) + "H", j, 5);
        }
        for (int j = 12; j < 68; j++) {
            gpr_table.setValueAt(String.valueOf(DecodeDraft.ram[0][j]) + "H", j - 11, 1);
        }

        lbw.setText(String.valueOf(DecodeDraft.wRegister) + "H");
        lbpc.setText(String.valueOf(DecodeDraft.pC_Current));
        lbpcl.setText(String.valueOf(DecodeDraft.pC_Current));
        // lbpclath.setText(String.valueOf(DecodeDraft.));
        lbc.setText(String.valueOf(DecodeDraft.carrybit));
        lbdc.setText(String.valueOf(DecodeDraft.digitcarrybit));
        lbz.setText(String.valueOf(DecodeDraft.zerobit));
      //  table_1.addRowSelectionInterval(row, row);
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
        List<String> codeLines = new ArrayList<>();
        // List<String> befehle = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(lstFile))) {
            String currentLine;

            while ((currentLine = br.readLine()) != null) {
                codeLines.add(currentLine);
                befehle.add(currentLine.substring(5, 9));
                // System.out.println("Current linr:" + currentLine.substring(5, 9));
                model.addRow(new Object[] { Boolean.FALSE, currentLine });

                // TODO: System.out.println(sCurrentLine);
            }

            row = rightRow(DecodeDraft.pC_Next);
            table_1.addRowSelectionInterval(row, row);

            convertHexToInt(befehle);
            // liest eingelesene Integer List Befehle ein und übergibt diese der internen
            // List von DecodeDraft
            DecodeDraft.execute=befehleInteger;
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
                        } else if (columnIndex == 5) {
                            ioPinsDataB[rowIndex] = 0;
                            System.out.println("Updated ioPinsDataB[" + rowIndex + "]: 0");
                        }
                        button.setText("0");
                    } else {
                        if (columnIndex == 2) {
                            ioPinsDataA[rowIndex] = 1;
                            System.out.println("Updated ioPinsDataA[" + rowIndex + "]: 1");
                        } else if (columnIndex == 5) {
                            ioPinsDataB[rowIndex] = 1;
                            System.out.println("Updated ioPinsDataB[" + rowIndex + "]: 1");
                        }
                        button.setText("1");
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

            if (breakpoint != null && breakpoint && pcValue != null && Integer.parseInt(pcValue) == line) {
                return true;
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
