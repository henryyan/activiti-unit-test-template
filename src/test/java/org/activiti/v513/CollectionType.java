package com.thams.task.varaible;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.activiti.engine.impl.variable.ValueFields;
import org.activiti.engine.impl.variable.VariableType;

/** 
 * @author  izerui.com
 * @version createtime：2013-6-28 上午11:54:43 
 */
public class CollectionType implements VariableType {
  
	public String getTypeName() {
		return "list";
	}

	public boolean isCachable() {
		return true;
	}

	public boolean isAbleToStore(Object value) {
		if (value==null) {
		      return false;
		    }
		    return Collection.class.isAssignableFrom(value.getClass())
		           || List.class.isAssignableFrom(value.getClass())||ArrayList.class.isAssignableFrom(value.getClass());
	}

	public void setValue(Object value, ValueFields valueFields) {
		Collection<String> userlist = (Collection<String>)value;
		String users = null;
		for (String usercode: userlist) {
			if(users==null){
				users = usercode;
			}else{
				users += ","+usercode;
			}
		}
 		valueFields.setTextValue(users);
	}

	public Object getValue(ValueFields valueFields) {
		String users = valueFields.getTextValue();
		if(users!=null&&!"".equals(users)){
			return Arrays.asList(users.split(","));
		}
		return null;
	}

}
