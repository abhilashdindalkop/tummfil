package com.ecommerce.dao;

import java.util.Date;
import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.ebean.SqlUpdate;
import com.ecommerce.models.sql.Cart;
import com.ecommerce.models.sql.Products;
import com.ecommerce.models.sql.Users;

import utils.MyException;
import utils.MyConstants.FailureMessages;

public class CartDAO {

	public Cart findById(long cartProductId) throws MyException {
		Cart cartProduct = Ebean.find(Cart.class).where().eq("id", cartProductId).findUnique();
		if (cartProduct == null) {
			throw new MyException(FailureMessages.PRODUCT_NOT_ADDED_TO_CART);
		}
		return cartProduct;
	}

	public List<Cart> findUserCartProducts(Users user, boolean isOrderByVendor, boolean isOrderByTime)
			throws MyException {

		Query<Cart> query = Ebean.find(Cart.class);
		query.where().eq("user", user);

		if (isOrderByVendor) {
			query.order().asc("vendor_id");
		}
		if (isOrderByTime) {
			query.order().desc("created_time");
		}
		List<Cart> cartList = query.findList();
		return cartList;
	}

	public Cart findByUserAndProduct(Users user, Products product) throws MyException {
		Cart cartProduct = Ebean.find(Cart.class).where().eq("user", user).eq("product", product).findUnique();
		return cartProduct;
	}

	public Cart findCartVendor(Users user) throws MyException {
		Cart cartProduct = Ebean.find(Cart.class).where().eq("user", user).setMaxRows(1).findUnique();
		return cartProduct;
	}

	public boolean isDifferentVendor(Users user, Products product) throws MyException {
		Cart cartProduct = findCartVendor(user);
		if (cartProduct == null) {
			return false;
		}
		return !cartProduct.getVendor().getId().equals(product.getVendor().getId());
	}

	public void add(Cart cartProduct, Users user, Products product) throws MyException {
		cartProduct.setUser(user);
		cartProduct.setVendor(product.getVendor());
		cartProduct.setProduct(product);
		cartProduct.setQuantity(1);
		cartProduct.setCreatedTime(new Date());
		cartProduct.save();
	}

	public void updateProduct(Cart cartProduct, int quantity) throws MyException {
		cartProduct.setQuantity(quantity);
		cartProduct.update();
	}

	public void deleteByCartProductId(long cartProductId) {
		String deleteSql = "delete from cart where id=:cartProductId";
		SqlUpdate deleteCartProduct = Ebean.createSqlUpdate(deleteSql);
		deleteCartProduct.setParameter("cartProductId", cartProductId);
		Ebean.execute(deleteCartProduct);
	}

	public void deleteUserCart(Users user) {
		String deleteSql = "delete from cart where user_id=:userId";
		SqlUpdate deleteCartProduct = Ebean.createSqlUpdate(deleteSql);
		deleteCartProduct.setParameter("userId", user.getId());
		Ebean.execute(deleteCartProduct);
	}

}
