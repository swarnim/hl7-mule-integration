package edu.ucsf.mule.domain;

import java.io.Serializable;

public class Role implements Serializable{
	private String roleid;
	private String roletype;
	private String  firstname;
	private String lastname;
	private String middlename;
	private String ucsfid;
	private String mso;
	private String patientId;
	private String patientVisitId;
	private String providerId;
	private boolean active;
	public Role() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	public String getRoletype() {
		return roletype;
	}
	public void setRoletype(String roletype) {
		this.roletype = roletype;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getMiddlename() {
		return middlename;
	}
	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}
	public String getUcsfid() {
		return ucsfid;
	}
	public void setUcsfid(String ucsfid) {
		this.ucsfid = ucsfid;
	}
	public String getMso() {
		return mso;
	}
	public void setMso(String mso) {
		this.mso = mso;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getPatientVisitId() {
		return patientVisitId;
	}
	public void setPatientVisitId(String patientVisitId) {
		this.patientVisitId = patientVisitId;
	}
	public String getExternatlId(){
		return (patientId == null ? "" : patientId.trim()) 
				+  (patientVisitId == null ? "" :  "~"+ patientVisitId.trim()) 
				+ (providerId == null ? "" : "~" + providerId.trim())
				;
	}
	
	
	@Override
	public String toString() {
		return "Role [roleid=" + roleid + ", roletype=" + roletype
				+ ", firstname=" + firstname + ", lastname=" + lastname
				+ ", middlename=" + middlename + ", ucsfid=" + ucsfid
				+ ", mso=" + mso + ", patientId=" + patientId
				+ ", patientVisitId=" + patientVisitId + ", providerId="
				+ providerId + "]";
	}
	public String getProviderId() {
		return providerId;
	}
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

}
