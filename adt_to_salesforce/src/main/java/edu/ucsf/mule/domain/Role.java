package edu.ucsf.mule.domain;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
	private String startDateString;
	private String endDateString;
	private Date startDate;
	private Date endDate;
	final Log logger = LogFactory.getLog(getClass());
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
	
	public String getStartDateString() {
		return startDateString;
	}
	public void setStartDateString(String startDateString) {
		this.startDateString = startDateString;
	}
	public String getEndDateString() {
		return endDateString;
	}
	public void setEndDateString(String endDateString) {
		this.endDateString = endDateString;
	}

	public Date getStartDate() {
		
		return parseDate(startDateString);
		
	}


	
	public Date getEndDate() {
		return  parseDate(endDateString);
	}
	
	
	public String getExternatlId(){
		return (patientId == null ? "" : patientId.trim()) 
				+  (patientVisitId == null ? "" :  "~"+ patientVisitId.trim()) 
				+ (providerId == null ? "" : "~" + providerId.trim())
				;
	}
	
	private Date parseDate(String dateString) {
		DateFormat format1 = new SimpleDateFormat("yyyyMMddHHmm"); // datetime
		DateFormat format2 = new SimpleDateFormat("yyyyMMdd"); // date only

		if (dateString != null && dateString.length() >= 8) {
			if (dateString.length() == 8) {
				return parseDateByFormat(dateString, format2);
			} else if (dateString.length() == 12) {
				return parseDateByFormat(dateString, format1);
			} else {// don't recognize date taht is greater that 12
				logger.error("Date String Greater than 12 char " +dateString);
				
				return null;
			}
		} else {
			return null;
		}
	}

	private Date parseDateByFormat(String dateString, DateFormat format2) {
		Date returnDate = null;
		try {
			returnDate = format2.parse( dateString );
		} catch (ParseException e) {
			logger.error("Can not parse Date String " +dateString, e);
			
		}
		return returnDate;
	}
	@Override
	public String toString() {
		return "Role [roleid=" + roleid + ", roletype=" + roletype
				+ ", firstname=" + firstname + ", lastname=" + lastname
				+ ", middlename=" + middlename + ", ucsfid=" + ucsfid
				+ ", mso=" + mso + ", patientId=" + patientId
				+ ", patientVisitId=" + patientVisitId + ", providerId="
				+ providerId + ", active=" + active + ", startDate="
				+ startDate + ", endDate=" + endDate + "]";
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
