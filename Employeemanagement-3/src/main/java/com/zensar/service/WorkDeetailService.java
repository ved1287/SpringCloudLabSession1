package com.zensar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zensar.bean.WorkDetails;
import com.zensar.dao.WorkRepository;



@Service
public class WorkDeetailService {
	@Autowired
	WorkRepository  workRepository ;

	
	public WorkDetails create(WorkDetails workDetails) {
		return workRepository .save(workDetails);
	}


	public List<WorkDetails> read() {
		return workRepository .findAll();
	}

	
	public WorkDetails readById(Long id) {
		return workRepository.findById(id).get();
	}

	
	public WorkDetails update(WorkDetails workDetails) {
		return workRepository.save(workDetails);
	}

	
	public void delete(Long id) {
		workRepository.delete(readById(id));
	}

}
