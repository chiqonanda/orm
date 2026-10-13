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
│   │   └── Buku.java           # POJO / Entity class yang dipetakan ke tabel basis data
│   └── dao/
│       └── BukuDao.java        # Abstraksi operasi CRUD (Data Access Object)
└── main/
    └── Belajarbersama.java     # Entry point aplikasi & interaksi antarmuka CLI
