package com.sliar.sliarblog.dao;

import com.sliar.sliarblog.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameAndPassword(String username, String password);

}
