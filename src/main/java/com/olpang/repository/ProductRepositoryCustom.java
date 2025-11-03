package com.olpang.repository;

import com.olpang.domain.Product;
import com.olpang.request.ProductPageRequest;

import java.util.List;

public interface ProductRepositoryCustom {

    List<Product> getList(ProductPageRequest productPageRequest);
}
