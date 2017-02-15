package com.ecommerce.models.sql;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.fasterxml.jackson.annotation.JsonIgnore;

import utils.MyConstants.FailureMessages;
import utils.MyException;

@Entity
public class Category extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private Long id;

	@Column(nullable = false, length = 50)
	private String type;

	private String imageUrl;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@JsonIgnore
	private Date createdTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public static Category add(String name, String imageUrl) throws MyException {
		Category newCategory = new Category();
		newCategory.setType(name);
		newCategory.setImageUrl(imageUrl);
		newCategory.setCreatedTime(new Date());
		newCategory.save();
		return newCategory;
	}

	public static Category findById(long id) throws MyException {
		Category category = Ebean.find(Category.class).where().eq("id", id).findUnique();
		if (category == null) {
			throw new MyException(FailureMessages.CATEGORY_DOESNT_EXIST);
		}
		return category;
	}

	public static boolean isCategoriesUpdated(Date updatedTime) {
		List<Category> categoryList = Ebean.find(Category.class).where().ge("createdTime", updatedTime).findList();
		return !categoryList.isEmpty();
	}

	public static List<Category> findAllCategories() {
		List<Category> categoryList = Ebean.find(Category.class).findList();
		return categoryList;
	}

	public static List<SqlRow> searchByText(String searchText) {

		String searchCategoryQuery;
		SqlQuery rawSqlQuery;

		if (searchText == null) {
			searchCategoryQuery = "SELECT id, type FROM category";
		} else {
			searchText = searchText.replace(" ", "").toLowerCase();
			searchCategoryQuery = "SELECT id, type FROM category WHERE REPLACE(LOWER(type), ' ', '') LIKE '%"
					+ searchText + "%'";
		}

		/* Search category */
		rawSqlQuery = Ebean.createSqlQuery(searchCategoryQuery);

		List<SqlRow> rowList = rawSqlQuery.findList();
		return rowList;
	}

}
