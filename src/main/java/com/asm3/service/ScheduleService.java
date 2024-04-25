package com.asm3.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asm3.entity.Schedule;
import com.asm3.repo.ScheduleRepo;

@Service
@Transactional
public class ScheduleService {
	@Autowired ScheduleRepo repo;
	@PersistenceContext private EntityManager entityManager;
	
	public long countUserSchedule(int doctorId) {
		return repo.countUserSchedule(doctorId);
	}
	
	@SuppressWarnings("unchecked")
	public List<Schedule> getUserScheduleByDoctorId(int doctorId, int pageNumber, int pageSize) {
		String sql = "SELECT s FROM Schedule s "
				+ "LEFT JOIN FETCH s.user u "
				+ "LEFT JOIN FETCH s.doctor d "
				+ "ORDER BY s.id DESC";
	    return entityManager.createQuery(sql)
	                        .setFirstResult(pageNumber * pageSize)
	                        .setMaxResults(pageSize)
	                        .getResultList();
	}
}
