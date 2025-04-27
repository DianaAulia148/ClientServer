package Client_Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

import java.sql.Timestamp;
import java.util.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ChatServer {
    //Port tujuan
    private static final int PORT = 9001;
    private static HashSet<String> names = new HashSet<String>();
    /**
     * Pengaturan untuk perintah 'print writers' untuk semua clients.
     * Digunakan untuk membroadcast pesan.
     */
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

    /**
     * Method utama dari program,
     * yang mana hanya listen port yang dipilih dan membuat thread handler baru.
     */
    public static void main(String[] args) throws Exception {
        System.out.println("The chat server is running.");
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }

    /**
     * Class handler yang merupakan ekstensi dari Thread.
     * Handler dibuat dari loop pendengar dan
     * digunakan untuk membroadcast pesan dari suatu client.
     */
    private static class Handler extends Thread {
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        public String printTimeStamp;

        //Socket
        public Handler(Socket socket) {
            this.socket = socket;
        }

        /*
         * Memproses thread client ketika nama yang dimasukan sedang digunakan
         * oleh client lain dengan terus menerus meminta inputan nama
         * hingga nama yang dimasukan berbeda
         * dari nama yang pernah digunakan oleh client lain.
         */
   
        public void run() {
            try {
                // Membuat Alamat Socket
                in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                /** 
                 * Meminta nama client. 
                 * tetap meminta sampai nama sudah diisikan dan belum digunakan
                 * Nama yang sudah diisikan akan dikunci 
                 * dan ditampilkan sebagai identitas pengirim pesan.
                 */
                while (true) {
                    out.println("SUBMITNAME");
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (names) {
                        if (!names.contains(name)) {
                            names.add(name);
                            break;
                        }
                    }
                }

                /* Server mingirimkan pesan 'Name Accepted' ke setiap client 
                ketika nama yang dimasukan adalah nama baru*/
                out.println("NAMEACCEPTED");
                writers.add(out);
                
                // Menerima pesan dari client ini untuk kemudian membroadcastnya.
                while (true) {
                    String input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    for (PrintWriter writer : writers) {
                        DateFormat df = new SimpleDateFormat ("HH:mm");
                        Date dateobj = new Date();
                        writer.println("MESSAGE " + name + ": " + input + " " + "(" + df.format(dateobj) + ")");
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                /* 
                 * Jika client terputus, program akan menghapus nama client
                 * dan menutup socketnya.
                 */
                if (name != null) {
                    names.remove(name);
                }
                if (out != null) {
                    writers.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
        
    }
}
