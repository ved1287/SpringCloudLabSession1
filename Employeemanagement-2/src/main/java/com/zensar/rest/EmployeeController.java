package com.zensar.rest;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;







import com.zensar.bean.Employee;
import com.zensar.dao.EmployeeRepository;
import com.zensar.service.EmployeeService;

@RestController
@RequestMapping("/Admin")
public class EmployeeController {
	
	@Autowired
	EmployeeService employeeService;
	
	
	
	
	@PostMapping("/add-employee")
	private Employee saveEmployee(@RequestBody Employee employee) {
		employeeService.create(employee);
		
		return  employee;
	}
	
	
	@GetMapping("/get-employees")
	private List<Employee> getAllEmployee(){
		return employeeService.read();
	}
	
	
	@GetMapping("/get-employee/{id}")
	private Employee getEmployeeById(@PathVariable("id") long id) {
		return employeeService.readById(id);
	}
	
	
	@PostMapping("/update-employee/{id}")
	private Employee update(@PathVariable Long id,@RequestBody Employee employeeDetails) {
		Employee employee = employeeService.readById(id);
//				.orElseThrow(()->new ResourceNotFoundException("Employee not exist with id :" + id));
		
		employee.setFisrtName(employeeDetails.getFisrtName());
		employee.setLastName(employeeDetails.getLastName());
		employee.setDesignation(employeeDetails.getDesignation());
		employee.setRole(employeeDetails.getRole());
		employee.setPincode(employeeDetails.getPincode());
		employee.setAddress(employeeDetails.getAddress());
		employee.setCity(employeeDetails.getCity());
		employee.setCountry(employeeDetails.getCountry());
		employee.setMobileNumber(employeeDetails.getMobileNumber());
		employee.setEmail(employeeDetails.getEmail());
		 employeeService.update(employee);
		 return employee;
		
		
	}
	
	
	@DeleteMapping("/delete-employee/{id}")
	private String deleteEmployee(@PathVariable("id") long id) {
		employeeService.delete(id);
		return "Successfully Deleted";
	}
	
	
	

}
