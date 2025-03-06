/**
 * 
 */
package com.ppc.mm.nickprodmessaging.util;

import java.util.List;

/**
 * @author gujarach
 *
 */
public class Keyword {

	private String removeAll;
	private List<String> add;
	private List<String> remove;
	
	public String getRemoveAll() {
		return removeAll;
	}
	public void setRemoveAll(String removeAll) {
		this.removeAll = removeAll;
	}
	public List<String> getAdd() {
		return add;
	}
	public void setAdd(List<String> add) {
		this.add = add;
	}
	public List<String> getRemove() {
		return remove;
	}
	public void setRemove(List<String> remove) {
		this.remove = remove;
	}
	@Override
	public String toString() {
		return "Keyword [removeAll=" + removeAll + ", add=" + add + ", remove=" + remove + "]";
	}
}
