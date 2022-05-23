package com.zensar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zensar.bean.Admin;
import com.zensar.dao.AdminRepository;




@Service
public class AdminService {
	@Autowired
	AdminRepository adminRepo;

	
	public Admin create(Admin admin) {
		return adminRepo.save(admin);
	}


	public List<Admin> read() {
		return adminRepo.findAll();
	}

	
	public Admin readById(Long id) {
		return adminRepo.findById(id).get();
	}

	
	public Admin update(Admin employee) {
		return adminRepo.save(employee);
	}

	
	public void delete(Long id) {
		adminRepo.delete(readById(id));
	}
}
