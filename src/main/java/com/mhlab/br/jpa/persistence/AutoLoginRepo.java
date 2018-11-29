package com.mhlab.br.jpa.persistence;

import com.mhlab.br.jpa.entity.AutoLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 자동 로그인 데이터베이스를 처리하는 Repo
 * Created by MHLab on 30/11/2018..
 */

@Repository
public interface AutoLoginRepo extends JpaRepository<AutoLogin, Integer> {
    Optional<AutoLogin> findByToken(String token);
}
