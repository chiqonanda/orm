/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.entitas.Buku;
import java.util.List;

public class BukuDao {

    private EntityManager em;

    public BukuDao(EntityManager em) {
        this.em = em;
    }

    // 1. CREATE / INSERT
    public void save(Buku buku) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(buku); // ORM otomatis generate SQL "INSERT"
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }

    // 2. READ BY ID
    public Buku findById(Long id) {
        return em.find(Buku.class, id); // ORM otomatis generate SQL "SELECT ... WHERE id = ?"
    }

    // 3. READ ALL (Menggunakan JPQL)
    public List<Buku> findAll() {
        return em.createQuery("SELECT b FROM Buku b", Buku.class).getResultList();
    }

    // 4. UPDATE
    public void update(Buku buku) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(buku); // ORM otomatis generate SQL "UPDATE"
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }

    // 5. DELETE
    public void delete(Long id) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Buku buku = em.find(Buku.class, id);
            if (buku != null) {
                em.remove(buku); // ORM otomatis generate SQL "DELETE"
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }
}