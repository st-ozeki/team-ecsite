package jp.co.internous.team2409.model.form;

import java.io.Serializable;

/**
 * カートフォーム
 * @author インターノウス
 *
 */
public class CartForm implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int productId;
	private int productCount;
	
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getProductCount() {
		return productCount;
	}
	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
