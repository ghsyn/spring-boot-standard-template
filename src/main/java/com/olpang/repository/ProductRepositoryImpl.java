package com.olpang.repository;

import com.olpang.domain.Product;
import com.olpang.domain.QProduct;
import com.olpang.request.ProductPageRequest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 제품 커스텀 Repository 구현체
 * ::Spring 컨테이너에 Bean 등록된 jpaQueryFactory를 생성자 주입하여 QueryDSL 실행
 */
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Product> getList(ProductPageRequest productPageRequest) {
        return jpaQueryFactory.selectFrom(QProduct.product)
                .limit(productPageRequest.getSize())
                .offset(productPageRequest.getOffset())
                .orderBy(QProduct.product.id.desc())
                .fetch();
    }
}
