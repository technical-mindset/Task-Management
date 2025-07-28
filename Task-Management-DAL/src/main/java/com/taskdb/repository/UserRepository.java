package com.taskdb.repository;

import com.taskdb.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.enable = true  ORDER BY u.id DESC")
    Page<User> findAll(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.email = ?1 ORDER BY u.id DESC")
    List<User> findUsersByEmail(String email);

    @Query("SELECT u FROM User u ORDER BY u.id DESC")
    List<User> findAll();

    @Query("SELECT u FROM User u WHERE u.id IN :memberList")
    List<User> findByMemberList(@Param("memberList") List<Integer> memberList);

    @Query("SELECT COUNT(*) FROM User u WHERE u.id =:userId AND u.team.id =:teamId")
    long findCountByIdAndTeamId(@Param("teamId") Integer teamId, @Param("userId") Integer userId);

}
