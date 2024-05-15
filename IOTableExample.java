import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;


//https://www.youtube.com/watch?v=skryksKiIK0&t=418s jtable
//https://youtube.com/watch?v=Tg62AxNRir4

public class IOTableExample {
    public static int[] ioPinsDataA = new int[8];
    public static int[] ioPinsDataB = new int[8];

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("I/O Pins Table");
            JTable ioTable = new JTable();
            ioTable.setModel(new DefaultTableModel(
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
            for (int i = 0; i < ioTable.getColumnCount(); i++) {
                ioTable.getColumnModel().getColumn(i).setCellRenderer(new ToggleCellRenderer());
                ioTable.getColumnModel().getColumn(i).setCellEditor(new ToggleCellEditor(i, ioPinsDataA, ioPinsDataB));
            }

            JScrollPane scrollPane = new JScrollPane(ioTable);
            frame.add(scrollPane);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
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
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.rowIndex = row;
        button.setText(value == null ? "0" : value.toString()); // Set default value to "0"
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
