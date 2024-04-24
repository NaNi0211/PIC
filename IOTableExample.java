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
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("I/O Pins Table");
            JTable ioTable = new JTable();
            ioTable.setModel(new DefaultTableModel(
                    new Object[][] { { "0", "i", null, "0", "i", null }, { "1", "i", null, "1", "i", null },
                            { "2", "i", null, "2", "i", null }, { "3", "i", null, "3", "i", null },
                            { "4", "i", null, "4", "i", null }, { "5", "i", null, "5", "i", null },
                            { "6", "i", null, "6", "i", null }, { "7", "i", null, "7", "i", null } },
                    new String[] { "RA", "Tris", "Pin", "RB", "Tris", "Pin" }));

            // Set custom cell renderer and editor
            for (int i = 0; i < ioTable.getColumnCount(); i++) {
                ioTable.getColumnModel().getColumn(i).setCellRenderer(new ToggleCellRenderer());
                ioTable.getColumnModel().getColumn(i).setCellEditor(new ToggleCellEditor());
            }

            JScrollPane scrollPane = new JScrollPane(ioTable);
            frame.add(scrollPane);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}

class ToggleCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setText(value == null ? "" : value.toString());
        return this;
    }
}

class ToggleCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JButton button;

    public ToggleCellEditor() {
        button = new JButton();
        button.setBorderPainted(false);
        button.addActionListener(e -> {
            if (button.getText().equals("1")) {
                button.setText("0");
            } else {
                button.setText("1");
            }
            fireEditingStopped();
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        button.setText(value == null ? "" : value.toString());
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
