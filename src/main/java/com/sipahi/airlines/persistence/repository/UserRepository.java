package com.sipahi.airlines.persistence.repository;

import com.sipahi.airlines.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
