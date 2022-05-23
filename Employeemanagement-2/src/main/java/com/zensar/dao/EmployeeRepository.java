package com.zensar.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zensar.bean.Employee;



@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	

}
