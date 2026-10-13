# Sistem Manajemen Data Buku: Implementasi CRUD Berbasis ORM dan Arsitektur DAO

Aplikasi berbasis konsol (*Command Line Interface*) untuk mengelola data buku secara persisten ke database MySQL `perpas`. Program ini menerapkan konsep **Object-Relational Mapping (ORM)** menggunakan **Jakarta Persistence API (JPA)** dengan *engine* **Hibernate**, serta menggunakan arsitektur **Data Access Object (DAO)** untuk memisahkan logika akses data dari logika antarmuka pengguna.

---

## 1. Struktur Proyek

```text
src/
├── config/
│   └── Koneksi.java            # Konfigurasi koneksi berbasis JDBC murni
├── model/
│   ├── entitas/
│   │   └── Buku.java           # Entity class yang dipetakan ke tabel basis data
│   └── dao/
│       └── BukuDao.java        # Abstraksi operasi CRUD (Data Access Object)
└── main/
    └── Belajarbersama.java     # Entry point aplikasi & interaksi antarmuka CLI
## 2. Paradigma Akses Data: JDBC, PreparedStatement, dan ORM

Aplikasi ini mendemonstrasikan perbandingan antara pengelolaan basis data tingkat rendah berbasis JDBC murni dengan pendekatan berbasis objek menggunakan ORM.

### A. Java Database Connectivity (JDBC) & PreparedStatement
Class `Koneksi.java` merepresentasikan pendekatan konvensional JDBC:
* **Mekanisme JDBC:** Mengandalkan driver MySQL untuk membuka saluran komunikasi langsung menggunakan antarmuka `java.sql.Connection` dan `DriverManager.getConnection()`.
* **Kebutuhan PreparedStatement:** Pada JDBC murni, pengembang wajib merancang kueri SQL mentah dan membungkusnya ke dalam objek `PreparedStatement`. Pendekatan ini memetakan parameter secara manual, misalnya `pstmt.setString(1, judul)`, guna mencegah celah keamanan SQL Injection serta meningkatkan efisiensi pra-kompilasi kueri di server basis data.
* **Tantangan JDBC:** Setiap baris data dari database kembali dalam bentuk `ResultSet`, sehingga pengembang harus memetakan data secara manual baris per baris ke variabel objek Java.

### B. Object-Relational Mapping (ORM) dengan JPA & Hibernate
Pada class `Belajarbersama.java` dan `BukuDao.java`, sistem beralih menggunakan pendekatan ORM:
* **Konsep Pemetaan:** ORM menjembatani perbedaan antara skema relasional tabel database dengan pemrograman berorientasi objek di Java.
* **Otomatisasi Query:** Pengembang tidak lagi menyusun string query SQL mentah seperti `INSERT`, `UPDATE`, atau `DELETE`. Engine Hibernate secara otomatis mengonversi method objek menjadi kueri SQL yang sesuai dengan dialek MySQL.
* **Manajemen Siklus Objek:** Siklus hidup entitas dikontrol melalui `EntityManager` dan `EntityTransaction`. Perubahan status objek Java secara otomatis disinkronkan ke dalam tabel database.

### C. Matriks Perbandingan Teknis

| Fitur / Aspek | Pendekatan JDBC & PreparedStatement | Pendekatan ORM (JPA / Hibernate) |
| :--- | :--- | :--- |
| **Penyusunan Kueri** | Manual menulis perintah SQL mentah di kode program | Otomatis dibuatkan oleh engine ORM |
| **Keamanan Kueri** | Bergantung pada kedisiplinan memakai `PreparedStatement` | Terlindungi secara terpusat lewat abstraksi ORM dan JPQL |
| **Konversi Data** | Manual mengambil nilai dari `ResultSet` ke variabel objek | Otomatis dipetakan langsung ke instance class Entity |
| **Penyimpanan Objek** | Menulis parameter kolom satu per satu ke tanda tanya `?` | Cukup memanggil `em.persist(object)` |
| **Portabilitas DB** | Rentan bentrok jika dialek SQL antar-vendor basis data berbeda | Cukup memperbarui konfigurasi dialek di file XML |

---

## 3. Penerapan Pola Desain Data Access Object (DAO)

Class `BukuDao.java` berfungsi sebagai lapisan perantara yang mengisolasi seluruh detail teknis transaksi database dari kode program utama:

* **Pemisahan Tanggung Jawab:** Modul antarmuka `Belajarbersama.java` tidak perlu memproses pembukaan transaksi database, penyusunan kueri, maupun penanganan pembatalan transaksi. Modul antarmuka cukup memanggil method seperti `save()`, `findById()`, `findAll()`, `update()`, dan `delete()`.
* **Pengelolaan Transaksi:** Operasi yang mengubah data dibungkus di dalam blok `EntityTransaction`:
  * `tx.begin()`: Membuka sesi transaksi baru.
  * `tx.commit()`: Menerapkan perubahan secara permanen ke database jika eksekusi berhasil.
  * `tx.rollback()`: Membatalkan seluruh operasi bila terjadi kendala atau exception agar integritas data tetap konsisten.
* **Operasi CRUD pada DAO:**
  * **Create:** Memanggil `em.persist(buku)` untuk mendaftarkan objek baru ke unit persistensi.
  * **Read (by ID):** Memanggil `em.find(Buku.class, id)` untuk mencari entitas berdasarkan primary key.
  * **Read (All):** Menjalankan kueri JPQL `SELECT b FROM Buku b` yang berorientasi pada class entitas Java, bukan nama tabel fisik.
  * **Update:** Memanggil `em.merge(buku)` untuk menyinkronkan data objek yang telah diperbarui ke database.
  * **Delete:** Memanggil `em.remove(buku)` setelah memastikan keberadaan objek di database.

---

## 4. Pemetaan Entitas (JPA Mapping)

Class `Buku.java` merupakan model data yang menggunakan anotasi JPA agar dikenali oleh Hibernate:

* `@Entity`: Menandai class `Buku` sebagai entitas yang dipetakan ke tabel basis data.
* `@Table(name = "buku")`: Menentukan tabel target di database `perpas`.
* `@Id`: Menetapkan atribut `id` sebagai primary key unik.
* `@GeneratedValue(strategy = GenerationType.IDENTITY)`: Menyerahkan penomoran ID otomatis ke mekanisme auto increment MySQL.
* `@Column(...)`: Mengatur detail kolom, seperti aturan `nullable = false` untuk mencegah nilai kosong pada database.
* **Konstruktor Tanpa Parameter:** Disediakan `public Buku() {}` sebagai syarat wajib refleksi internal JPA saat membuat instansiasi objek.

---

## 5. Alur Kerja Aplikasi (Belajarbersama.java)

1. **Inisialisasi:** Program memanggil `Persistence.createEntityManagerFactory("perpustakaanPU")` untuk membaca konfigurasi `persistence.xml`, lalu membuat `EntityManager`.
2. **Injeksi DAO:** Objek `EntityManager` diteruskan ke dalam konstruktor `BukuDao`.
3. **Perulangan Menu:** Menampilkan menu pilihan Create, Read, Update, Delete, dan Keluar menggunakan loop `while` serta percabangan `switch-case`.
4. **Penutupan Koneksi:** Ketika opsi 0 dipilih, koneksi database ditutup melalui `em.close()` dan `emf.close()` guna mencegah kebocoran alokasi memori.

---

## 6. Prasyarat Sistem & Dependensi

* **Java Development Kit (JDK):** Versi 17 atau yang lebih baru.
* **Database Engine:** MySQL Server (aktif pada port default 3306 dengan basis data `perpas`).
* **Maven Dependencies:**
  * `org.hibernate.orm:hibernate-core`
  * `com.mysql:mysql-connector-j`
  * `jakarta.persistence:jakarta.persistence-api`
