package com.zensar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zensar.bean.Employee;
import com.zensar.dao.EmployeeRepository;



@Service
public class EmployeeService {

	@Autowired
	EmployeeRepository employeeRepository;

	
	public Employee create(Employee employee) {
		return employeeRepository.save(employee);
	}


	public List<Employee> read() {
		return employeeRepository.findAll();
	}

	
	public Employee readById(Long id) {
		return employeeRepository.findById(id).get();
	}

	
	public Employee update(Employee employee) {
		return employeeRepository.save(employee);
	}

	
	public void delete(Long id) {
		employeeRepository.delete(readById(id));
	}

}
