package com.zensar.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.zensar.bean.Admin;
import com.zensar.bean.Employee;
import com.zensar.bean.WorkDetails;
import com.zensar.service.AdminService;


@RestController
@RequestMapping("/Manger")
public class AdminController {
	@Autowired
	AdminService adminService;
	
	
	
	
	@PostMapping("/add-Admin")
	private Admin saveAdmin(@RequestBody Admin admin) {
		adminService.create(admin);
		
		return  admin;
	}
	
	
	@GetMapping("/get-Admin")
	private List<Admin> getAllAdmin(){
		return adminService.read();
	}
	
	
	@GetMapping("/get-admin/{id}")
	private Admin getAdminById(@PathVariable("id") long id) {
		return adminService.readById(id);
	}
	
	//creating put mapping that updates the employee detail
	@PostMapping("/update-admin/{id}")
	private Admin updateAdmin(@PathVariable Long id,@RequestBody Admin adminDetails) {
		Admin admin = adminService.readById(id);
//				.orElseThrow(()->new ResourceNotFoundException("Employee not exist with id :" + id));
		
		admin.setFisrtName(adminDetails.getFisrtName());
		admin.setLastName(adminDetails.getLastName());
		admin.setDesignation(adminDetails.getDesignation());
		admin.setRole(adminDetails.getRole());
		admin.setPincode(adminDetails.getPincode());
		admin.setAddress(adminDetails.getAddress());
		admin.setCity(adminDetails.getCity());
		admin.setCountry(adminDetails.getCountry());
		admin.setMobileNumber(adminDetails.getMobileNumber());
		admin.setEmail(adminDetails.getEmail());
		adminService.update(admin);
		 return admin;
		
		
	}
	
	
	@DeleteMapping("/delete-admin/{id}")
	private String deleteAdmin(@PathVariable("id") long id) {
		adminService.delete(id);
		return "Successfully Deleted";
	}
	
	@PostMapping("/get-employee/{id}")
	private Employee saveEmployee(@RequestBody Employee employee) {
		RestTemplate restTemplate = new RestTemplate();
		Employee employee1;

		String url = "http://Admin-service/get-employee/"+employee.getId();
		employee1 = restTemplate.getForObject(url, Employee.class);
		return  employee1;
	}
	
	
	@DeleteMapping("/delete-employee/{id}")
	private Employee deleteEmployee(@PathVariable("id") long id) {
		RestTemplate restTemplate = new RestTemplate();
		Employee employee1;

		String url = "http://Admin-service/delete-employee/"+id;
		employee1 = restTemplate.getForObject(url, Employee.class);
		return  employee1;
	}
	
	@GetMapping("/get-WorkDetails/{id}")
	private Employee getWorkDetailsById(@PathVariable("id") long id) {
		RestTemplate restTemplate = new RestTemplate();
		Employee employee1;

		String url = "http://Employee-service/get-WorkDetails/"+id;
		employee1 = restTemplate.getForObject(url, Employee.class);
		return  employee1;
		
	}
	
	@DeleteMapping("/delete-workDetails/{id}")
	private Employee deleteworkDetails(@PathVariable("id") long id) {
		RestTemplate restTemplate = new RestTemplate();
		Employee employee1;

		String url = "http://Employee-service/delete-workDetails/"+id;
		employee1 = restTemplate.getForObject(url, Employee.class);
		
		return employee1;
	}
}
