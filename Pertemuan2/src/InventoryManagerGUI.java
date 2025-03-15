import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

// GUI Application
public class InventoryManagerGUI extends JFrame {
    // Generic Class: Menggunakan parameter tipe generik <T>
    private List<LaboratoryItem<String>> inventory = new ArrayList<>();
    private DefaultTableModel tableModel;
    private JTable table;
    private static final String FILE_NAME = "inventory.txt";
    
    public InventoryManagerGUI() {
        setTitle("LabKOM Inventory");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Panel Input
        JPanel panel = new JPanel();
        String[] labItems = {"Monitor", "Keyboard", "Mouse", "CPU", "Meja", "Kursi", "Proyektor","Papan Tulis","WIFI","AC","Lampu"};
        JComboBox<String> itemDropdown = new JComboBox<>(labItems);
        JTextField quantityField = new JTextField(5);
        JButton addButton = new JButton("Tambah Item");
        panel.add(new JLabel("Item:"));
        panel.add(itemDropdown);
        panel.add(new JLabel("QTY:"));
        panel.add(quantityField);
        panel.add(addButton);
        
        // Tabel Item
        tableModel = new DefaultTableModel(new String[]{"Item", "QTY", "Kondisi"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Tombol Hapus & Tandai Rusak
        JButton removeButton = new JButton("Hapus Item");
        JButton markDamagedButton = new JButton("Tandai Rusak");
        
        // Event Listener
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String itemText = (String) itemDropdown.getSelectedItem();
                String quantityText = quantityField.getText().trim();
                
                if (quantityText.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Semua kolom harus di isi!");
                    return;
                }
                
                int quantity;
                try {
                    quantity = Integer.parseInt(quantityText);
                    if (quantity <= 0) {
                        JOptionPane.showMessageDialog(null, "Jumlah harus berupa bilangan positif!");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Jumlah harus berupa angka!");
                    return;
                }
                
                // Periksa apakah item sudah ada, jika ada update jumlahnya dan ubah kondisi menjadi "Good"
                boolean found = false;
                for (int i = 0; i < inventory.size(); i++) {
                    if (inventory.get(i).getItem().equals(itemText)) {
                        inventory.get(i).setQuantity(quantity);
                        inventory.get(i).setCondition("BAGUS"); // Perbarui kondisi menjadi "Good"
                        tableModel.setValueAt(quantity, i, 1);
                        tableModel.setValueAt("BAGUS", i, 2);
                        saveInventory();
                        found = true;
                        break;
                    }
                }
                
                if (!found) {
                    LaboratoryItem<String> newItem = new LaboratoryItem<>(itemText, quantity, "BAGUS");
                    addItem(newItem);
                }
                
                quantityField.setText("");
            }
        });
        
        removeButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Pilih item yang ingin di HAPUS!");
            } else {
                removeItem(selectedRow);
            }
        });
        
        markDamagedButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Pilih item yang ingin ditandai RUSAK!");
            } else {
                inventory.get(selectedRow).setCondition("RUSAK");
                tableModel.setValueAt("RUSAK", selectedRow, 2);
                saveInventory();
            }
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(removeButton);
        buttonPanel.add(markDamagedButton);
        
        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    // Generic Method: Menggunakan parameter generik <T>
    private <T> void addItem(LaboratoryItem<T> item) {
        inventory.add((LaboratoryItem<String>) item);
        tableModel.addRow(new Object[]{item.getItem(), item.getQuantity(), item.getCondition()});
        saveInventory();
    }
    
    private void removeItem(int index) {
        inventory.remove(index);
        tableModel.removeRow(index);
        saveInventory();
    }
    
    private void saveInventory() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (LaboratoryItem<String> item : inventory) {
                writer.write(item.getItem() + "," + item.getQuantity() + "," + item.getCondition());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InventoryManagerGUI().setVisible(true);
        });
    }
}
