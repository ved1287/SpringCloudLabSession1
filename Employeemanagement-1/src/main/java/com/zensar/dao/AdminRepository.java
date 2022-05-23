package com.zensar.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zensar.bean.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

}
