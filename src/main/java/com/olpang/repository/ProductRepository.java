package com.olpang.repository;

import com.olpang.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 제품 DB 접근 Repository
 * JpaRepository 기본 CRUD, 제품 커스텀 Repository 상속
 */
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
}
