import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// a. Kelas Generic (Generic Class)
class InventoryItem<T> {
    private T item; // Tipe data generik untuk item
    private int quantity;
    private String condition;
    
    public InventoryItem(T item, int quantity, String condition) {
        this.item = item;
        this.quantity = quantity;
        this.condition = condition;
    }
    
    public T getItem() {
        return item;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public String getCondition() {
        return condition;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public void setCondition(String condition) {
        this.condition = condition;
    }
}

// c. Diamond Interface
interface Displayable {
    void display(); // Method untuk menampilkan informasi item
}

interface Manageable extends Displayable {}

// Implementasi kelas dengan Diamond Interface
class LaboratoryItem<T> extends InventoryItem<T> implements Manageable {
    public LaboratoryItem(T item, int quantity, String condition) {
        super(item, quantity, condition);
    }
    
    @Override
    public void display() {
        System.out.println("Item: " + getItem().toString() + ", Quantity: " + getQuantity() + ", Condition: " + getCondition());
    }
}

// b. Method Generic
class InventoryManager {
    private List<LaboratoryItem<String>> inventory = new ArrayList<>();
    private static final String FILE_NAME = "inventory.txt";

    // Method Generic untuk menambahkan item ke inventaris
    private <T> void addItem(InventoryItem<T> item) {
        inventory.add((LaboratoryItem<String>) item);
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
}

// d. Wildcard (Unbounded, Upper/Lower Bounded)
class InventoryUtils {
    // Upper Bounded Wildcard (Menerima semua turunan dari InventoryItem)
    public static void printInventory(List<? extends InventoryItem<?>> inventory) {
        for (InventoryItem<?> item : inventory) {
            System.out.println("Item: " + item.getItem() + ", QTY: " + item.getQuantity() + ", Condition: " + item.getCondition());
        }
    }

    // Lower Bounded Wildcard (Menerima InventoryItem atau superclass-nya)
    public static void updateCondition(List<? super InventoryItem<String>> inventory, String newCondition) {
        for (Object obj : inventory) {
            if (obj instanceof InventoryItem) {
                ((InventoryItem<String>) obj).setCondition(newCondition);
            }
        }
    }
}
