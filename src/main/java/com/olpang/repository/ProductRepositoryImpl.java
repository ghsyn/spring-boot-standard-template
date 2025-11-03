package com.olpang.repository;

import com.olpang.domain.Product;
import com.olpang.domain.QProduct;
import com.olpang.request.ProductPageRequest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
