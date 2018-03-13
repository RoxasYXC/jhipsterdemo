package com.yxc.repository;

import com.yxc.domain.Userinfo;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Userinfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserinfoRepository extends JpaRepository<Userinfo, Long> {

}
