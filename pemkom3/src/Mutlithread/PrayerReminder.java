package Mutlithread;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PrayerReminder extends JFrame {

    private JLabel lblClock, lblPrayerTimes;
    private JComboBox<String> cmbCity;
    private JButton btnFetch;
    private static final String API_KEY = "2676dd1d0a89f0b06841760994926eb0";
    private static final String API_URL = "https://api.aladhan.com/v1/timingsByCity";

    public PrayerReminder() {
        setTitle("Sholat Reminder Indonesia");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(true);
        getContentPane().setBackground(new Color(44, 62, 80));

        lblClock = new JLabel("Loading time...", SwingConstants.CENTER);
        lblClock.setFont(new Font("Arial", Font.BOLD, 24));
        lblClock.setForeground(Color.WHITE);
        add(lblClock, BorderLayout.NORTH);
        startClock();

        JPanel panelInput = new JPanel();
        panelInput.setBackground(new Color(44, 62, 80));

        String[] cities = {"Sabang", "Banda Aceh", "Medan", "Padang", "Pekanbaru", "Jambi", "Palembang", "Bengkulu", "Bandar Lampung", "Jakarta", "Bandung", "Semarang", "Yogyakarta", "Surabaya", "Denpasar", "Mataram", "Kupang", "Pontianak", "Samarinda", "Banjarmasin", "Palu", "Makassar", "Manado", "Gorontalo", "Ambon", "Ternate", "Sorong", "Jayapura"};
        cmbCity = new JComboBox<>(cities);
        cmbCity.setFont(new Font("Arial", Font.BOLD, 16));
        cmbCity.setPreferredSize(new Dimension(200, 30));

        btnFetch = new JButton("Dapatkan Waktu Sholat");
        btnFetch.setBackground(new Color(46, 204, 113));
        btnFetch.setForeground(Color.WHITE);
        btnFetch.setFont(new Font("Arial", Font.BOLD, 16));
        btnFetch.setPreferredSize(new Dimension(250, 40));
        btnFetch.addActionListener(this::fetchPrayerTimes);

        JLabel lblCity = new JLabel("Pilih Kota:");
        lblCity.setForeground(Color.WHITE); // Mengatur warna teks menjadi putih
        panelInput.add(lblCity);
        panelInput.add(cmbCity);
        cmbCity.setFont(new Font("Arial", Font.BOLD, 16));
        panelInput.add(btnFetch);
        btnFetch.setForeground(Color.WHITE);

        add(panelInput, BorderLayout.CENTER);

        lblPrayerTimes = new JLabel("<html><center>Silakan pilih kota...</center></html>", SwingConstants.CENTER);
        lblPrayerTimes.setFont(new Font("Arial", Font.PLAIN, 16));
        lblPrayerTimes.setForeground(Color.WHITE);
        add(lblPrayerTimes, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void startClock() {
        Timer timer = new Timer("Clock-Thread");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String time = sdf.format(calendar.getTime());
                lblClock.setText(time);
            }
        }, 0, 1000);
    }

    private void fetchPrayerTimes(ActionEvent e) {
        String city = cmbCity.getSelectedItem().toString();

        new Thread(() -> {
            try {
                String urlStr = API_URL + "?city=" + city + "&country=Indonesia&method=2&key=" + API_KEY;
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JsonParser parser = new JsonParser();
                JsonElement jsonElement = parser.parse(response.toString());
                JsonObject jsonResponse = jsonElement.getAsJsonObject();
                JsonObject timings = jsonResponse.getAsJsonObject("data").getAsJsonObject("timings");

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String currentTime = sdf.format(Calendar.getInstance().getTime());

                String prayerTimes = "<html><center>"
                        + "<h2>🕌 Jadwal Sholat</h2>"
                        + "<p>Waktu Saat Ini: <b>" + currentTime + "</b></p>"
                        + "<table border='1' cellspacing='5' cellpadding='5' style='font-size:16px;'>"
                        + "<tr><td>⏳ <b>Imsak</b></td><td>:</td><td><b>" + timings.get("Imsak").getAsString() + "</b></td></tr>"
                        + "<tr><td>🌅 <b>Subuh (Fajr)</b></td><td>:</td><td><b>" + timings.get("Fajr").getAsString() + "</b></td></tr>"
                        + "<tr><td>🌞 <b>Dzuhur (Dhuhr)</b></td><td>:</td><td><b>" + timings.get("Dhuhr").getAsString() + "</b></td></tr>"
                        + "<tr><td>☀️ <b>Ashar (Asr)</b></td><td>:</td><td><b>" + timings.get("Asr").getAsString() + "</b></td></tr>"
                        + "<tr><td>🌇 <b>Maghrib (Buka Puasa)</b></td><td>:</td><td><b>" + timings.get("Maghrib").getAsString() + "</b></td></tr>"
                        + "<tr><td>🌙 <b>Isya (Isha)</b></td><td>:</td><td><b>" + timings.get("Isha").getAsString() + "</b></td></tr>"
                        + "</table>"
                        + "<br><i>⏰ Pastikan waktu sesuai dengan zona lokal Anda</i>"
                        + "</center></html>";

                SwingUtilities.invokeLater(() -> lblPrayerTimes.setText(prayerTimes));

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> lblPrayerTimes.setText("<html><center>❌ Error fetching prayer times</center></html>"));
                ex.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PrayerReminder::new);
    }
}
