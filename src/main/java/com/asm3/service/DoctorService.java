package com.asm3.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asm3.entity.Doctor;
import com.asm3.entity.User;
import com.asm3.repo.DoctorRepo;

@Service
@Transactional
public class DoctorService {
	@Autowired DoctorRepo repo;
	@PersistenceContext private EntityManager entityManager;
	
	public Doctor save(Doctor doctor) {
		return repo.save(doctor);
	}
	
//	public List<Doctor> findAllDoctorsWithUser() {
//		return repo.findAllDoctorsWithUser();
//	}
	
	@SuppressWarnings("unchecked")
	public List<Doctor> findAllDoctorsWithUser(int pageNumber, int pageSize) {
		String sql = "SELECT d FROM Doctor d "
				+ "LEFT JOIN FETCH d.user u "
				+ "LEFT JOIN FETCH d.specialization s "
				+ "ORDER BY d.id DESC";
		
		int offset = pageNumber * pageSize;
	    return entityManager.createQuery(sql)
	                        .setFirstResult(offset)
	                        .setMaxResults(pageSize)
	                        .getResultList();
	}
	
	public Doctor findJoinAllById(int id) {
		return repo.findJoinAllById(id);
	}
	
	public void deleteDoctor(int id) {
		repo.deleteById(id);
	}
	
	public long countDoctor() {
		return repo.count();
	}
	
	public long countDoctorBySearchSpecial(String keySearch) {
		String sql = "SELECT COUNT(d) FROM Doctor d "
				+ "LEFT JOIN d.specialization s "
				
				+ "WHERE UPPER(s.name) LIKE UPPER(:keySearch)";
		Query query = entityManager.createQuery(sql);
		query.setParameter("keySearch", "%" + keySearch + "%");
		return (long) query.getSingleResult();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Doctor> findAllDoctorsWithUserBySearchSpecial(String keySearch, int pageNumber, int pageSize) {
		String sql = "SELECT d FROM Doctor d "
				+ "LEFT JOIN FETCH d.user u "
				+ "LEFT JOIN FETCH d.specialization s "
				+ "LEFT JOIN FETCH d.clinic c "
				
				+ "WHERE UPPER(s.name) LIKE UPPER(:keySearch) "
				+ "ORDER BY d.id DESC";
				
		int offset = pageNumber * pageSize;
		Query query = entityManager.createQuery(sql)
	                        .setFirstResult(offset)
	                        .setMaxResults(pageSize);
		
		query.setParameter("keySearch", "%" + keySearch + "%");
		
		return (List<Doctor>) query.getResultList();
		
	}
	
	public long countDoctorBySearchGeneral(String keySearch) {
		String sql = "SELECT COUNT(d) FROM Doctor d "
				+ "LEFT JOIN d.user u "
				+ "LEFT JOIN d.specialization s "
				+ "LEFT JOIN d.clinic c "
				
				+ "WHERE UPPER(s.name) LIKE UPPER(:keySearch) "
				+ "OR UPPER(u.fullName) LIKE UPPER(:keySearch)"
				+ "OR UPPER(c.name) LIKE UPPER(:keySearch)"
				+ "OR UPPER(c.address) LIKE UPPER(:keySearch)";
		Query query = entityManager.createQuery(sql);
		query.setParameter("keySearch", "%" + keySearch + "%");
		return (long) query.getSingleResult();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Doctor> findAllDoctorsWithUserBySearchGeneral(String keySearch, int pageNumber, int pageSize) {
		String sql = "SELECT d FROM Doctor d "
				+ "LEFT JOIN FETCH d.user u "
				+ "LEFT JOIN FETCH d.specialization s "
				+ "LEFT JOIN FETCH d.clinic c "
				
				+ "WHERE UPPER(s.name) LIKE UPPER(:keySearch) "
				+ "OR UPPER(u.fullName) LIKE UPPER(:keySearch)"
				+ "OR UPPER(c.name) LIKE UPPER(:keySearch)"
				+ "OR UPPER(c.address) LIKE UPPER(:keySearch)"
				+ "ORDER BY d.id DESC";
				
		int offset = pageNumber * pageSize;
		Query query = entityManager.createQuery(sql)
	                        .setFirstResult(offset)
	                        .setMaxResults(pageSize);
		
		query.setParameter("keySearch", "%" + keySearch + "%");
		
		return (List<Doctor>) query.getResultList();
		
	}
	
	public int findDoctorIdByUserId(int userId) {
		return repo.findDoctorIdByUserId(userId);
	}
}
