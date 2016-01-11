package com.rabbitframework.jadb.mapping.param;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库参数组合实例类 提供对数据库条件解析的接口 oredCriteria条件集体合，一般情况下使用createCriteria()方法
 * 特殊情况下，调用createCriteria(criteria)或addCreateCriteria()方法
 * 
 * 
 */
public class WhereParamType {

	protected List<Criteria> oredCriteria;

	public WhereParamType() {
		oredCriteria = new ArrayList<Criteria>();
	}

	public List<Criteria> getOredCriteria() {
		return oredCriteria;
	}

	/**
	 * 根据参数，增加条件集体合
	 * 
	 * @param criteria
	 */
	public void createCriteria(Criteria criteria) {
		oredCriteria.add(criteria);
	}

	/**
	 * 实例化条件类，增加条件集合
	 * 
	 * @return
	 */
	public Criteria addCreateCriteria() {
		Criteria criteria = createCriteriaInternal();
		oredCriteria.add(criteria);
		return criteria;
	}

	/**
	 * 限定条件集体合，size==1
	 * 
	 * @return
	 */
	public Criteria createCriteria() {
		Criteria criteria = createCriteriaInternal();
		if (oredCriteria.size() == 0) {
			oredCriteria.add(criteria);
		}
		return criteria;
	}

	protected Criteria createCriteriaInternal() {
		Criteria criteria = new Criteria();
		return criteria;
	}

	public void clear() {
		oredCriteria.clear();
	}

}
