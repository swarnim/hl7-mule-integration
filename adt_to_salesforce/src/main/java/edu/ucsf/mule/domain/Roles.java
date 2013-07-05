package edu.ucsf.mule.domain;

import java.util.ArrayList;
import java.util.List;

public class Roles {
	List<Role> roleList;

	public Roles(List<Role> roleList) {
		super();
		this.roleList = roleList;
	}

	public Roles() {
		roleList = new ArrayList<Role>();
	}

}
