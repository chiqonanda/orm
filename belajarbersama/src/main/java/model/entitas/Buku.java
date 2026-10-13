/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entitas;

/**
 *
 * @author USER
 */

import jakarta.persistence.*;

@Entity
@Table(name = "buku")
public class Buku {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "judul", nullable = false)
    private String judul;

    @Column(name = "penulis", nullable = false)
    private String penulis;

    // Constructor kosong wajib untuk JPA
    public Buku() {}

    public Buku(String judul, String penulis) {
        this.judul = judul;
        this.penulis = penulis;
    }

    // Getter dan Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }

    public String getPenulis() { return penulis; }
    public void setPenulis(String penulis) { this.penulis = penulis; }

    @Override
    public String toString() {
        return "Buku [id=" + id + ", judul=" + judul + ", penulis=" + penulis + "]";
    }
}