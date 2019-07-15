package com.bacefook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bacefook.entity.Relation;

@Repository
public interface RelationsRepository extends JpaRepository<Relation, Integer> {

    Relation findBySenderIdAndReceiverId(Integer senderId, Integer receiverId);

}
