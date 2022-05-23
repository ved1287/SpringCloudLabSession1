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

import com.zensar.bean.WorkDetails;
import com.zensar.service.WorkDeetailService;

@RestController
@RequestMapping("/Employee")
public class WorkController {
	@Autowired
	WorkDeetailService workDeetailService;
	
	@PostMapping("/add-WorkDetails")
	private WorkDetails saveWorkDetails(@RequestBody WorkDetails workDetails) {
		workDeetailService.create(workDetails);
		
		return  workDetails;
	}
	
	
	@GetMapping("/get-WorkDetails")
	private List<WorkDetails> getAllWorkDetails(){
		return workDeetailService.read();
	}
	
	
	@GetMapping("/get-WorkDetails/{id}")
	private WorkDetails getWorkDetailsById(@PathVariable("id") long id) {
		return workDeetailService.readById(id);
	}
	
	
	@PostMapping("/update-workDetails/{id}")
	private WorkDetails updateWorkDetails(@PathVariable Long id,@RequestBody WorkDetails WorkDetailsDetails) {
		WorkDetails workDetails = workDeetailService.readById(id);
//				.orElseThrow(()->new ResourceNotFoundException("Employee not exist with id :" + id));
		
		workDetails.setProjectName(WorkDetailsDetails.getProjectName());
		workDetails.setLeadName(WorkDetailsDetails.getLeadName());
		workDetails.setDesignation(WorkDetailsDetails.getDesignation());
		workDetails.setRole(WorkDetailsDetails.getRole());
		workDetails.setTimeGiven(WorkDetailsDetails.getTimeGiven());
		
		workDeetailService.update(workDetails);
		 return workDetails;
		
		
	}
	
	
	@DeleteMapping("/delete-workDetails/{id}")
	private String deleteworkDetails(@PathVariable("id") long id) {
		workDeetailService.delete(id);
		return "Successfully Deleted";
	}
}
