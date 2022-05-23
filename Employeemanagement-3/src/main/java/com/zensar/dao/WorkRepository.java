package com.zensar.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zensar.bean.WorkDetails;
@Repository
public interface WorkRepository extends  JpaRepository<WorkDetails, Long>{

}
