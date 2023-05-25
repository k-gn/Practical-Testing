package sample.cafekiosk.spring.api.service.product.request;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingType;
import sample.cafekiosk.spring.domain.product.ProductType;

@Getter
@NoArgsConstructor
public class ProductCreateServiceRequest {

	private ProductType type;
	private ProductSellingType sellingType;
	private String name;
	private int price;

	@Builder
	private ProductCreateServiceRequest(
		ProductType type,
		ProductSellingType sellingType,
		String name,
		int price
	) {
		this.type = type;
		this.sellingType = sellingType;
		this.name = name;
		this.price = price;
	}

	public Product toEntity(String nextProductNumber) {
		return Product.builder()
			.productNumber(nextProductNumber)
			.type(type)
			.sellingType(sellingType)
			.name(name)
			.price(price)
			.build();
	}
}
