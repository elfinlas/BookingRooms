package com.mhlab.br.jpa.persistence;

import com.mhlab.br.jpa.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Room Entity를 처리하는 리포지토리 객체
 *
 * Created by MHLab on 18/10/2018.
 */

@Repository
public interface RoomRepo extends JpaRepository<Room, Integer> {
}
