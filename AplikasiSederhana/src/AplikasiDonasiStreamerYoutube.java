import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class AplikasiDonasiStreamerYoutube {
    // Konstanta untuk judul aplikasi
    private static final String APP_TITLE = "Aplikasi Donasi Streamer";
    
    // Konstanta untuk pilihan nominal donasi
    private static final String[] PILIHAN_DONASI = {
        "Rp10.000", "Rp25.000", "Rp50.000", "Rp100.000", "Rp250.000", "Rp500.000"
    };

    // Collection (ArrayList) untuk menyimpan riwayat donasi
    private static ArrayList<HashMap<String, String>> riwayatDonasi = new ArrayList<>();

    public static void main(String[] args) {
        // Membuat jendela utama aplikasi
        JFrame frame = new JFrame(APP_TITLE);
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label dan input untuk nama donatur
        JLabel labelNama = new JLabel("Masukkan Nama Anda:");
        JTextField fieldNama = new JTextField(20);

        // Label dan pilihan nominal donasi
        JLabel labelNominal = new JLabel("Pilih Nominal Donasi:");
        JComboBox<String> comboDonasi = new JComboBox<>(PILIHAN_DONASI);

        // Label dan input pesan dukungan
        JLabel labelPesan = new JLabel("Masukkan Pesan Dukungan:");
        JTextField fieldPesan = new JTextField(20);

        // Tombol untuk mengirim donasi
        JButton tombolDonasi = new JButton("Donasi Sekarang");

        // Tombol untuk melihat riwayat donasi
        JButton tombolRiwayat = new JButton("Lihat Riwayat Donasi");

        // Menambahkan elemen ke dalam frame dengan GridBagLayout
        gbc.gridx = 0; gbc.gridy = 0;
        frame.add(labelNama, gbc);
        gbc.gridx = 1;
        frame.add(fieldNama, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        frame.add(labelNominal, gbc);
        gbc.gridx = 1;
        frame.add(comboDonasi, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        frame.add(labelPesan, gbc);
        gbc.gridx = 1;
        frame.add(fieldPesan, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        frame.add(tombolDonasi, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        frame.add(tombolRiwayat, gbc);

        // Event listener untuk tombol donasi
        tombolDonasi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String namaPengirim = fieldNama.getText().trim();
                String nominal = (String) comboDonasi.getSelectedItem();
                String pesan = fieldPesan.getText().trim();

                // Validasi input
                if (namaPengirim.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Nama tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (pesan.length() > 100) {
                    JOptionPane.showMessageDialog(frame, "Pesan terlalu panjang! (Maksimal 100 karakter)", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Simpan ke riwayat donasi (Collection)
                HashMap<String, String> donasiBaru = new HashMap<>();
                donasiBaru.put("Nama", namaPengirim);
                donasiBaru.put("Nominal", nominal);
                donasiBaru.put("Pesan", pesan);
                riwayatDonasi.add(donasiBaru);

                // Tampilkan notifikasi sukses
                JOptionPane.showMessageDialog(frame,
                        "💰 DONASI TERKIRIM! 💰\n" +
                        namaPengirim + " baru saja mengirim " + nominal + "\n" +
                        "Pesan: " + pesan + "\n\n" +
                        "Terima kasih atas dukungannya! 🎉",
                        "Donasi Berhasil", JOptionPane.INFORMATION_MESSAGE);

                // Kosongkan field setelah donasi
                fieldNama.setText("");
                fieldPesan.setText("");
            }
        });

        // Event listener untuk tombol riwayat donasi
        tombolRiwayat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (riwayatDonasi.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Belum ada donasi yang tercatat!", "Riwayat Donasi", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                StringBuilder riwayat = new StringBuilder("📜 RIWAYAT DONASI 📜\n\n");
                for (HashMap<String, String> donasi : riwayatDonasi) {
                    riwayat.append("Nama: ").append(donasi.get("Nama")).append("\n")
                           .append("Nominal: ").append(donasi.get("Nominal")).append("\n")
                           .append("Pesan: ").append(donasi.get("Pesan")).append("\n")
                           .append("----------------------\n");
                }

                JOptionPane.showMessageDialog(frame, riwayat.toString(), "Riwayat Donasi", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Menampilkan jendela aplikasi
        frame.setVisible(true);
    }
}
