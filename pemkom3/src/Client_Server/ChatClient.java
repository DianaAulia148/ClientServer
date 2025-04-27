package Client_Server;

//mengimport class-class yang akan digunakan
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient {
    BufferedReader in;
    PrintWriter out;
        JFrame frame = new JFrame("CHAT");
    JTextField textField = new JTextField(40);
    JTextArea messageArea = new JTextArea(8, 40);

    public ChatClient() {
        // Interface
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.pack();

        // Listeners
        textField.addActionListener(new ActionListener() {
            /*
             * Menanggapi kirim pesan dengan menekan enter
             */
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
            }
        });
    }

    /**
     * Meminta ip address server.
     */
    private String getServerAddress() {
        return JOptionPane.showInputDialog(
            frame,
            "Masukan IP Address server :",
            "Selamat datang di chat room",
            JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Meminta inputan nama pengguna.
     */
    private String getName() {
        return JOptionPane.showInputDialog(
            frame,
            "Masukan nama kamu :",
            " ",
            JOptionPane.PLAIN_MESSAGE);
    }
    
    private void run() throws IOException {

        /* 
         * Membuat koneksi ke ip address server
         * pada port yang telah ditentukan.
         */
        String serverAddress = getServerAddress();
        Socket socket = new Socket(serverAddress, 9001);
        in = new BufferedReader(new InputStreamReader(
            socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Memproses semua pesan dari server sesuai protokol.
        while (true) {
            String line = in.readLine();
            if (line.startsWith("SUBMITNAME")) {
                out.println(getName());
            } else if (line.startsWith("NAMEACCEPTED")) {
                textField.setEditable(true);
            } else if (line.startsWith("MESSAGE")) {
                messageArea.append(line.substring(8) + "\n");
            }
        }
    }

    /**
     * Menjalankan Client sebagai aplikasi.
     * Dengan frame yang dapat diclose.
     */
    public static void main(String[] args) throws Exception {
        ChatClient client = new ChatClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
}
