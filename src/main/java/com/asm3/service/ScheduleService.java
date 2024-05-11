package com.asm3.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asm3.entity.Doctor;
import com.asm3.entity.Schedule;
import com.asm3.repo.ScheduleRepo;

@Service
@Transactional
public class ScheduleService {
	@Autowired ScheduleRepo repo;
	@PersistenceContext private EntityManager entityManager;
	
	public Schedule save(Schedule schedule) {
		return repo.save(schedule);
	}
	
	public Schedule findById(int id) {
		return repo.findById(id).get();
	}
	
	public long countUserSchedule(int doctorId) {
		return repo.countUserSchedule(doctorId);
	}
	
	@SuppressWarnings("unchecked")
	public List<Schedule> getUserScheduleByDoctorId(int doctorId, int pageNumber, int pageSize) {
		String sql = "SELECT s FROM Schedule s "
				+ "LEFT JOIN FETCH s.user u "
				+ "LEFT JOIN FETCH s.doctor d "
				+ "WHERE d.id = :doctorId "
				+ "ORDER BY s.id DESC";
	    return entityManager.createQuery(sql)
	    					.setParameter("doctorId", doctorId)
	                        .setFirstResult(pageNumber * pageSize)
	                        .setMaxResults(pageSize)
	                        .getResultList();
	}
	
	public boolean checkScheduleByUserIdAndDoctorId(int userId, int doctorId) {
		Long sc = repo.checkScheduleByUserIdAndDoctorId(userId, doctorId);
		return sc > 0 ? true : false;
	}
	
	public List<Object[]> getSpecialzationOutStanding() {
	    String jpql = "SELECT sp, COUNT(sp.id) FROM Schedule s "
	                + "LEFT JOIN s.specialization sp "
	                + "GROUP BY sp, sp.id ";
	    TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
	    query.setMaxResults(4);
	    return query.getResultList();
	}
	
	public List<Object[]> getDoctorOutStanding() {
	    String jpql = "SELECT d, COUNT(d.id), c, u, sp FROM Schedule s "
	                + "LEFT JOIN s.doctor d "
	                + "LEFT JOIN d.clinic c "
	                + "LEFT JOIN d.user u "
	                + "LEFT JOIN d.specialization sp "
	                + "GROUP BY d, c, u, sp";
	    TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
	    query.setMaxResults(5);
	    return query.getResultList();
	}
	
	public long countScheduleByUser(int userId) {
	    String sql = "SELECT COUNT(s) FROM Schedule s "
	            + "LEFT JOIN s.user u "
	            + "WHERE u.id = :userId";
	    return entityManager.createQuery(sql, Long.class)
	                        .setParameter("userId", userId)
	                        .getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Schedule> getAllScheduleByUser(int userId, int pageNumber, int pageSize) {
		String sql = "SELECT s FROM Schedule s "
				+ "LEFT JOIN FETCH s.user u "
				+ "LEFT JOIN FETCH s.doctor d "
				+ "LEFT JOIN FETCH d.clinic c "
				+ "LEFT JOIN FETCH s.patient p "
				+ "WHERE u.id = :userId "
				+ "ORDER BY s.id DESC";
	    return entityManager.createQuery(sql)
	    					.setParameter("userId", userId)
	                        .setFirstResult(pageNumber * pageSize)
	                        .setMaxResults(pageSize)
	                        .getResultList();
	}
	
	public void delete(int id) {
		repo.deleteById(id);
	}
	
	public Schedule findJoinAllById(int id) {
		String sql = "SELECT s FROM Schedule s "
				+ "LEFT JOIN FETCH s.user u "
				+ "LEFT JOIN FETCH s.doctor d "
				+ "LEFT JOIN FETCH d.specialization sp "
				+ "LEFT JOIN FETCH d.clinic c "
				+ "LEFT JOIN FETCH s.patient p "
				+ "WHERE s.id = :id ";
	    return (Schedule) entityManager.createQuery(sql)
	    					.setParameter("id", id)
	                        .getSingleResult();
	}
	
	
	
	
	
}
