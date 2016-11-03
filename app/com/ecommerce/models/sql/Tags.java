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
public class Tags extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;

	@JsonIgnore
	@Column(columnDefinition = "TIMESTAMP")
	private Date createdTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public static Tags findById(String tagId) throws MyException {
		Tags tag = Ebean.find(Tags.class).where().eq("id", tagId).findUnique();
		if (tag == null) {
			throw new MyException(FailureMessages.TAG_DOESNT_EXIST);
		}
		return tag;
	}

	public static List<SqlRow> searchByText(String searchText) {

		searchText = searchText.replace(" ", "").toLowerCase();
		String searchTagQuery = "SELECT id FROM tags WHERE REPLACE(LOWER(name), ' ', '') LIKE '%" + searchText + "%'";
		SqlQuery rawSqlQuery = Ebean.createSqlQuery(searchTagQuery);
		List<SqlRow> rowList = rawSqlQuery.findList();
		return rowList;
	}

	public static List<SqlRow> findByTagName(String tagName) {

		tagName = tagName.replace(" ", "").toLowerCase();
		String searchTagQuery = "SELECT id FROM tags WHERE REPLACE(LOWER(name), ' ', '') =:tagName";
		SqlQuery rawSqlQuery = Ebean.createSqlQuery(searchTagQuery).setParameter("tagName", tagName);
		List<SqlRow> rowList = rawSqlQuery.findList();
		return rowList;
	}

	public static Long add(String tagName) throws MyException {

		List<SqlRow> tagList = findByTagName(tagName);
		if (tagList.isEmpty()) {
			Tags newTag = new Tags();
			// TODO format with upper case and lower case
			newTag.setName(tagName);
			newTag.setCreatedTime(new Date());
			newTag.save();
			newTag.refresh();
			return newTag.getId();
		} else {
			return tagList.get(0).getLong("id");
		}
	}

}