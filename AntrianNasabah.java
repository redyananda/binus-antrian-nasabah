public class AntrianNasabah {

    // class Nasabah
    static class Nasabah {
        String nomor;
        String nama;
        String kategori;

        Nasabah(String nomor, String nama, String kategori){
            this.nomor = nomor;
            this.nama = nama;
            this.kategori = kategori;   // Kategori "VIP" atau "Reguler"
        }
    }

    // Class AntrianArray --> antrian nasabah circular queue
    static class AntrianArray {
        Nasabah[] data;         // array untuk menyimpan data nasabah
        int posPertama;         // posisi nasabah pertama
        int posKosong;          // slot kosong untuk nasabah baru
        int jumlahNasabah;      // jumlah nasabah di antrian

        AntrianArray(int maksNasabah) {
            data = new Nasabah[maksNasabah];

            // kondisi awal, jadi semua di set 0
            posPertama = 0;
            posKosong = 0;
            jumlahNasabah = 0;
        }

        // untuk pengecekan, jika itu kosong, maka di return nilai jumlah nasabah 0
        boolean isEmpty() {
            return jumlahNasabah == 0;
        }

        // untuk pengecekan, jika itu penuh, maka di return nilai jumlah nasabah = jumlah size data
        boolean isFull() {
            return jumlahNasabah == data.length;
        }

        // ini fungsi untuk memasukkan nasabah baru di antrian
        void enqueue(Nasabah n) {
            // disini di cek dulu, apakah masih ada slot kosong untuk antrian
            if(isFull()) {
                System.out.println("Antrian sudah penuh");
                return;
            }

            // Jika slot masih ada yang kosong
            data[posKosong] = n;    // taruh nasabah n di slot kosong

            // geser ke slot berikutnya, jadi ini untuk memberitahu slot kosong ke berapa yang kosong
            // di modulo ke data.length, supaya jika sudah penuh, posisi balik ke 0
            posKosong = (posKosong+1) % data.length;    
            jumlahNasabah++;    // jumlah nasabah di antrian bertambah 1
        }

        // ini fungsi memanggil nasabah untuk dilayani
        // jadi ini untuk mengurangi nasabah yang ada di antrian
        Nasabah dequeue() {
            // di cek dulu, apakah ada antrian, jika antrian kosong, maka return null
            if(isEmpty()) {
                return null;
            }

            // ambil nasabah di posPertama, lalu simpan ke variabel n
            Nasabah n = data[posPertama];
            data[posPertama] = null;    // lalu slot di pos pertama sekarang dibikin kosong
            
            // geser pos pertama ke nasabah berikutnya
            // dimodulo dengan data.length, supaya jika sudah sampai di ujung array, posisinya balik ke 0
            posPertama = (posPertama+1) % data.length;
            jumlahNasabah--;    // jumlah nasabah di antrian di kurangin 1
            return n;
        }

        // ini fungsi untuk menampilkan daftar nasabah yang ada di antrian
        void tampilkan() {

            // dicek dulu apakah antrian kosong, kalau kosong akan keluar tulisan Tidak ada data antrian
            if(isEmpty()) {
                System.out.println("Tidak ada data antrian");
                return;
            }

            // for loop untuk print semua data list nasabah pada list antrian
            for(int i = 0; i < jumlahNasabah; i++){
                // hitung posisi nasabah ke-i di array
                // dimodulo dengan data.length, supaya jika sudah sampai di ujung array, posisinya balik ke 0
                int posisi = (posPertama + i) % data.length;
                Nasabah n = data[posisi];   // nasabah pada posisi i di assign ke variabel n 
                System.out.println(n.nomor + " - " + n.nama);
            }
            System.out.println();
        }
    }

    static AntrianArray antrianVIP = new AntrianArray(10);      // Ini untuk jumlah maksimal Nasabah VIP dalam Antrian
    static AntrianArray antrianReguler = new AntrianArray(10);  // Ini untuk jumlah maksimal Nasabah Reguler dalam Antrian

    static int vipBerturut = 0; // ini untuk bikin aturan awal, VIP berturut 0
    static int nomorVIP = 1;    // aturan, nomor vip dimulai 1
    static int nomorReguler = 1;    // aturan nomor reguler dimulai 1

    // ini fungsi untuk menambahkan nasabah VIP baru ke antrian VIP
    static void enqueueVIP(String nama) {

        // buat data nasabah baru dengan nomor, nama, dan kategori VIP
        // "" + nomorVIP dipakai untuk mengubah angka jadi teks (String)
        Nasabah n = new Nasabah(""+ nomorVIP, nama, "VIP");
        nomorVIP++; // nomor VIP bertambah 1 untuk nasabah VIP berikutnya
        antrianVIP.enqueue(n);  // masukkan nasabah ke antrian VIP
        System.out.println("Nasabah baru : " + n.nomor + "-" + n.nama + " (" + n.kategori + ")");
    }

    // ini fungsi untuk menambahkan nasabah reguler baru ke antrian reguler
    static void enqueueReguler(String nama) {
        Nasabah n = new Nasabah(""+ nomorReguler, nama, "Reguler");
        nomorReguler++;
        antrianReguler.enqueue(n);
        System.out.println("Nasabah baru : " + n.nomor + "-" + n.nama + " (" + n.kategori + ")");
    }

    // ini fungsi untuk memanggil nasabah berikutnya yang akan dilayani
    // di sini aturan anti-starvation diterapkan, supaya nasabah reguler tidak menunggu terus
    static void dequeue() {
        Nasabah n;  // untuk menyimpan nasabah yang dipanggil

        // Aturan 1: kalau sudah 4 VIP dipanggil berturut-turut dan ada nasabah reguler yang menunggu (4 VIP lalu 1 reguler dan diulang lagi)
        // sekarang giliran nasabah reguler
        if (vipBerturut >= 4 && !antrianReguler.isEmpty()){
            n = antrianReguler.dequeue();
            vipBerturut = 0;    // reset penghitung VIP, mulai hitung dari 0 lagi

        // Aturan 2: kalau belum giliran reguler dan antrian VIP masih ada, VIP didahulukan
        } else if (!antrianVIP.isEmpty()){
            n = antrianVIP.dequeue();
            vipBerturut++;  // penghitung VIP berturut-turut bertambah 1

        // Aturan 3: kalau antrian VIP kosong, langsung panggil nasabah reguler    
        } else if(!antrianReguler.isEmpty()){
            n = antrianReguler.dequeue();
            vipBerturut = 0;    // reset penghitung VIP

        // kalau kedua antrian kosong, tidak ada nasabah yang bisa dipanggil
        } else {
            System.out.println("Tidak ada antrian");
            return;
        }

        // tampilkan nasabah yang sedang dilayani
        System.out.println(n.nama + " (" + n.kategori + ") sedang dilayani");
    }

    // ini fungsi untuk menampilkan isi antrian VIP dan antrian reguler
    static void displayQueue() {
        System.out.print("Antrian VIP     : ");
        System.out.println();
        antrianVIP.tampilkan();     // tampilkan semua nasabah di antrian VIP
        System.out.print("Antrian Reguler : ");
        System.out.println();
        antrianReguler.tampilkan();     // tampilkan semua nasabah di antrian Reguler
    }

    // simulasinya
    public static void main(String[] args) {
        // Kondisi awal: ada 2 nasabah reguler dan 3 nasabah VIP yang sudah mengantri
        System.out.println("Kondisi awal:");
        enqueueReguler("Andi");
        enqueueReguler("Dewi");
        enqueueVIP("Budi");
        enqueueVIP("Citra");
        enqueueVIP("Eko");
        System.out.println();
        displayQueue(); // tampilkan isi antrian sebelum ada yang dipanggil
 
        // Simulasi: setiap putaran ada 1 VIP baru datang, lalu 1 nasabah dipanggil
        System.out.println();
        System.out.println("VIP terus datang setiap putaran:");

        // data nasabah VIP yang datang di setiap putaran
        String[] namaVIP = {"Fajar", "Gita", "Hadi", "Indra", "Joko", "Kiki", "Lina", "Maya", "Nadia", "Omar"};
 

        // perulangan sebanyak jumlah nama VIP, yaitu 10 putaran
        for (int i = 0; i < namaVIP.length; i++) {
            System.out.println("Putaran " + (i + 1));
            enqueueVIP(namaVIP[i]); // ada VIP baru datang
            dequeue();              // panggil 1 nasabah
            System.out.println();
        }
        
        // tampilkan nasabah yang masih tersisa di antrian setelah simulasi selesai
        System.out.println();
        System.out.println("Sisa antrian");
        displayQueue();
    }

}
