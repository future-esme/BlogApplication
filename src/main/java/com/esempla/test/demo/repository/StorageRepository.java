package com.esempla.test.demo.repository;

import com.esempla.test.demo.domain.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {
    Storage findStorageByName(String name);
}
