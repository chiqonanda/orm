package main;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;
import java.util.Scanner;
import model.dao.BukuDao;
import model.entitas.Buku;

public class Belajarbersama {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("perpustakaanPU");
        EntityManager em = emf.createEntityManager();
        BukuDao bukuDao = new BukuDao(em);
        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        while (running) {
            System.out.println("\n=== MENU CRUD BUKU (ORM) ===");
            System.out.println("1. Tambah Buku (Create)");
            System.out.println("2. Tampilkan Semua Buku (Read)");
            System.out.println("3. Cari Buku by ID (Read)");
            System.out.println("4. Ubah Data Buku (Update)");
            System.out.println("5. Hapus Buku (Delete)");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu: ");

            int pilihan = scanner.nextInt();
            scanner.nextLine(); // Konsumsi sisa newline

            switch (pilihan) {
                case 1:
                    System.out.println("\n--- Tambah Buku ---");
                    System.out.print("Masukkan judul buku : ");
                    String judul = scanner.nextLine();
                    System.out.print("Masukkan nama penulis: ");
                    String penulis = scanner.nextLine();

                    Buku bukuBaru = new Buku(judul, penulis);
                    bukuDao.save(bukuBaru);
                    System.out.println("Buku berhasil disimpan! Generated ID: " + bukuBaru.getId());
                    break;

                case 2:
                    System.out.println("\n--- Daftar Semua Buku ---");
                    List<Buku> listBuku = bukuDao.findAll();
                    if (listBuku.isEmpty()) {
                        System.out.println("Belum ada data buku di database.");
                    } else {
                        for (Buku b : listBuku) {
                            System.out.println(b);
                        }
                    }
                    break;

                case 3:
                    System.out.println("\n--- Cari Buku Berdasarkan ID ---");
                    System.out.print("Masukkan ID buku: ");
                    Long idCari = scanner.nextLong();

                    Buku bukuKetemu = bukuDao.findById(idCari);
                    if (bukuKetemu != null) {
                        System.out.println("Data ditemukan: " + bukuKetemu);
                    } else {
                        System.out.println("Buku dengan ID " + idCari + " tidak ditemukan.");
                    }
                    break;

                case 4:
                    System.out.println("\n--- Update Data Buku ---");
                    System.out.print("Masukkan ID buku yang ingin diubah: ");
                    Long idUpdate = scanner.nextLong();
                    scanner.nextLine();

                    Buku targetUpdate = bukuDao.findById(idUpdate);
                    if (targetUpdate != null) {
                        System.out.println("Data saat ini: " + targetUpdate);
                        System.out.print("Masukkan judul baru: ");
                        targetUpdate.setJudul(scanner.nextLine());
                        System.out.print("Masukkan penulis baru: ");
                        targetUpdate.setPenulis(scanner.nextLine());

                        bukuDao.update(targetUpdate);
                        System.out.println("Data buku berhasil diperbarui!");
                    } else {
                        System.out.println("Buku dengan ID " + idUpdate + " tidak ditemukan.");
                    }
                    break;

                case 5:
                    System.out.println("\n--- Hapus Buku ---");
                    System.out.print("Masukkan ID buku yang ingin dihapus: ");
                    Long idHapus = scanner.nextLong();

                    Buku targetHapus = bukuDao.findById(idHapus);
                    if (targetHapus != null) {
                        bukuDao.delete(idHapus);
                        System.out.println("Buku ID " + idHapus + " berhasil dihapus dari database.");
                    } else {
                        System.out.println("Buku dengan ID " + idHapus + " tidak ditemukan.");
                    }
                    break;

                case 0:
                    running = false;
                    System.out.println("Program selesai. Menutup koneksi database...");
                    break;

                default:
                    System.out.println("Pilihan tidak valid, silakan coba lagi.");
            }
        }

        scanner.close();
        em.close();
        emf.close();
    }
}