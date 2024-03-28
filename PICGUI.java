import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class PICGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private JTable table_1;
    private JTable table_2;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PICGUI frame = new PICGUI();
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

        JTextArea state_area = new JTextArea();
        state_area.setBounds(10, 0, 1192, 200);
        simulator_state_panel.add(state_area);

        JPanel console_panel = new JPanel();
        tabbedPane.addTab("Console", null, console_panel, null);
        console_panel.setLayout(null);

        JTextArea console_area = new JTextArea();
        console_area.setText("console");
        console_area.setBounds(10, 0, 1192, 200);
        console_panel.add(console_area);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBounds(10, 11, 1217, 23);
        contentPane.add(toolBar);

        JButton btnFile = new JButton("File");
        btnFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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

        JButton btnRun = new JButton("Run");
        btnRun.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnRun.setBounds(79, 61, 89, 23);
        contentPane.add(btnRun);

        JButton btnStop = new JButton("Stop");
        btnStop.setBounds(205, 61, 89, 23);
        contentPane.add(btnStop);

        JButton btnStep = new JButton("Step");
        btnStep.setBounds(340, 61, 89, 23);
        contentPane.add(btnStep);

        JButton btnReset = new JButton("Reset");
        btnReset.setBounds(467, 61, 89, 23);
        contentPane.add(btnReset);

        JSlider slider = new JSlider();
        slider.setBounds(609, 61, 174, 26);
        contentPane.add(slider);

        JLabel lblNewLabel = new JLabel("New label");
        lblNewLabel.setBounds(837, 65, 100, 14);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Hz");
        lblNewLabel_1.setBounds(947, 65, 46, 14);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("New label");
        lblNewLabel_2.setBounds(1072, 65, 99, 14);
        contentPane.add(lblNewLabel_2);

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

        JScrollPane scrollPane_2 = new JScrollPane();
        scrollPane_2.setBounds(39, 41, 576, 416);
        panel.add(scrollPane_2);

        table_1 = new JTable();
        table_1.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "BP", "PC", "LST" }) {
            Class[] columnTypes = new Class[] { String.class, String.class, String.class };

            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }
        });
        scrollPane_2.setViewportView(table_1);

        TableColumnModel colmod = table_1.getColumnModel();
        TableColumn TC_lst = colmod.getColumn(2);
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

        JLabel lbw = new JLabel("0x00");
        lbw.setBounds(704, 41, 46, 14);
        panel.add(lbw);

        JLabel lbpc = new JLabel("0x0000");
        lbpc.setBounds(704, 66, 46, 14);
        panel.add(lbpc);

        JLabel lbpcl = new JLabel("0x00");
        lbpcl.setBounds(704, 91, 36, 14);
        panel.add(lbpcl);

        JLabel lbpclath = new JLabel("0x00");
        lbpclath.setBounds(704, 116, 46, 14);
        panel.add(lbpclath);

        JLabel lbc = new JLabel("0");
        lbc.setBounds(704, 141, 17, 14);
        panel.add(lbc);

        JLabel lbdc = new JLabel("0");
        lbdc.setBounds(704, 166, 14, 14);
        panel.add(lbdc);

        JLabel lbz = new JLabel("0");
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

        table_2 = new JTable();
        table_2.setModel(
                new DefaultTableModel(new Object[][] {}, new String[] { "Value (Binary)", "Value (HEX)", "Address" }) {
                    Class[] columnTypes = new Class[] { String.class, String.class, String.class };

                    public Class getColumnClass(int columnIndex) {
                        return columnTypes[columnIndex];
                    }
                });
        scrollPane_3.setViewportView(table_2);

        JPanel panel_sfr = new JPanel();
        tabbedPane_1.addTab("SFR", null, panel_sfr, null);

        JPanel panel_stack = new JPanel();
        tabbedPane_1.addTab("Stack", null, panel_stack, null);

        JPanel panel_io = new JPanel();
        tabbedPane_1.addTab("I/O-Pins", null, panel_io, null);
    }

    // input Dateipfad der LST Datei durch den FileBtn in der GUI
    private void displayDataInTable(String filePath) {
        readDataFromFile(filePath);
        parse(filePath);

        System.out.println("Filepath in the display to table method: " + filePath);
        // List<String[]> data = readDataFromFile(filePath);
        // String[] columnNames = { "Address", "Instruction" }; /spalte
        // DefaultTableModel model = new DefaultTableModel(data.toArray(new
        // Object[0][]), columnNames);
        // table.setModel(model);

    }

    private List<String[]> readDataFromFile(String filePath) {
        List<String[]> data = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String lineStr = br.readLine(); // Read the first line from the file
            System.out.println("lineStr: " + lineStr); // Print the line read from the file

            // Split the line using a regular expression that matches one or more whitespace
            // characters
            String[] line = lineStr.trim().split("\\s+");
            System.out.println("Number of elements in line array: " + line.length);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle file reading errors
        }
        return data;
    }

    public List<String> parse(String lstFile) {
        List<String> codeLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(lstFile))) {
            String currentLine;

            while ((currentLine = br.readLine()) != null) {
                codeLines.add(currentLine);
                // TODO: System.out.println(sCurrentLine);
            }

        } catch (IOException e) {
            // TODO: Log
            e.printStackTrace();
        }

        return codeLines;
    }
}
