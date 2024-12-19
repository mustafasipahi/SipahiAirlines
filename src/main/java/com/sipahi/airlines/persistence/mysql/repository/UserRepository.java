package com.sipahi.airlines.persistence.mysql.repository;

import com.sipahi.airlines.persistence.mysql.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
