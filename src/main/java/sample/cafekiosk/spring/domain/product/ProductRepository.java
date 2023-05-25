package sample.cafekiosk.spring.domain.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findAllBySellingTypeIn(List<ProductSellingType> sellingTypes);

	List<Product> findAllByProductNumberIn(List<String> productNumbers);

	@Query(value = "select p.product_Number from product p order by id desc limit 1", nativeQuery = true)
	String findLatestProductNumber();
}

/*
	# QueryDSL
		- 타임 체크 가능
		- 동적 쿼리 가능
		- 가독성 좋음
		- 거의 필수로 사용한다.
 */
