package com.example.bankcards.repository;

import com.example.bankcards.entity.CardEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, Long> {

    Optional<CardEntity> findByCardNumber(String cardNumber);

    Page<CardEntity> findByUserEntityId(Long userId, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE) // SELECT FOR UPDATE
    @Query("SELECT c FROM CardEntity c WHERE c.id = :id")
    Optional<CardEntity> findByIdWithLock(@Param("id") Long id);

    @Query("SELECT c FROM CardEntity c WHERE c.userEntity.id = :userId AND c.cardNumber LIKE %:search%")
    Page<CardEntity> findByUserEntityIdAndCardNumberContaining(
            @Param("userId") Long userId,
            @Param("search") String search,
            Pageable pageable);

}
