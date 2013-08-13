package edu.ucsf.groovy
import edu.ucsf.mule.Util.HelperUtils;
import edu.ucsf.mule.domain.Role
import java.sql.DatabaseMetaData
import java.util.HashMap
import java.util.Map
import java.text.SimpleDateFormat
import java.text.ParseException

import org.jbpm.pvm.internal.wire.binding.FalseBinding;
class Utils {
	static final org.slf4j.Logger  logger = org.slf4j.LoggerFactory.getLogger('edu.ucsf.groovy.Utils')
	static blankMapValue(data) {
		if (data == null){
			return ""
		}
		if (data instanceof Collection){
			data.each{ blankMapValue(it) }
		} else if (data instanceof Map){
			data.each{
				if (it.value instanceof Map){
					blankMapValue(it.value)
				} else {
					it.value = (it.value == null) ? '' : it.value
				}
			}
		}
	}

	
	static dummy(data) {
		if (data == null){
			return ""
		}
	}
	static getCurrentSalesforceDatetime(){
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date())
	}
	static convertGmtDateToPst(dateString){
		if(dateString){
			def dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'")
			dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"))
			try{
				def convertedDate = (Date) dateFormat.parse(dateString)
				def dateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz")
				dateString = dateFormat2.format(convertedDate)
			} catch (ParseException e){
				println("Error parsing dataString " + dateString)
			}
		}
	}

	static  getUpsertRoleListWithOtherIds(payload, patientADTROLs, pId, pVisitId , warnings){

		def ucsfIdMap = new HashMap<String,String>()
		def returnRoleList = new ArrayList<Role> ()

		for (userRecord in payload){
			ucsfIdMap.put(userRecord.UCSF_ID__c, userRecord.Id)
		}
		for (Role role in patientADTROLs){
			if (role.ucsfid){
				if (ucsfIdMap.get(role.ucsfid)){
					role.setProviderId(ucsfIdMap.get(role.ucsfid));
					role.setPatientId(pId);
					role.setPatientVisitId(pVisitId);
					
					returnRoleList.add(role);
				} else {
					logger.info("userId not found  in salesforce " + role)
					warnings.append("userId not found in salesforce for ").append(role).append("\n");
				}

			} else {
				logger.info("userId not found  in " + role)
				warnings.append("userId not found  for ").append(role).append("\n");
			}
		}
		return returnRoleList;
	}
	static getUcsfIds(patientADTROLs, warnings){
		def ucsfIdSet = new HashSet<String>();
		def ucsfIds="";
		for (role in patientADTROLs){
			if (role.ucsfid){
				ucsfIdSet.add(role.ucsfid)
			} else {
			logger.info("no ucsfid for" + role);
			warnings.append(warnings).append(" no ucsfid for ").append(role).append("\n");
				
			}
		}
		if (ucsfIdSet.size() > 0) {

			for (ucsfId  in ucsfIdSet){
				ucsfIds = ucsfIds + "'" + ucsfId + "',"
			}
			ucsfIds = ucsfIds.substring(0, ucsfIds.length()-1)
		}
		return ucsfIds;
	}

	static getFinalRolesToUpsert(mappedrolesForUpsertList,saveRoles , PCPNO1, PCPNO2, ucsfid_salesforceid){

		//hashset of Role external key which should be PVCareRoleKey__c of existing record if present.
		def roleUpsertMap = new HashMap<String>()
		def rolesToDelete = new ArrayList<>()
		def finalUpsertList = new ArrayList<>();
		for (mappedRolesToUpsert in mappedrolesForUpsertList){
			roleUpsertMap.put(mappedRolesToUpsert.ExternalKey__c, mappedRolesToUpsert)
		}
		for (role in saveRoles){
			// if upsertRoles doesn't have the role then add to upsertMap as delete.
			if(!roleUpsertMap.get(role.ExternalKey__c)){
				def newRole = HelperUtils.deepClone(role)
				//set role isactive flag to false
				def clonedRole = (Map) HelperUtils.deepClone(newRole)
				clonedRole.Active__c = false;
				rolesToDelete.add(clonedRole)
			}


		}
		if (rolesToDelete.size() > 0){
			finalUpsertList.addAll(rolesToDelete);
		}
		 finalUpsertList.addAll(roleUpsertMap.values());
		//setup PCP
		 
		 def userIdToucsfIdMap = new HashMap<String,String>()
		
 
		 for (userRecord in ucsfid_salesforceid){
			 userIdToucsfIdMap.put(userRecord.Id, userRecord.UCSF_ID__c)
		 }
		 
		for (mappedRolesToUpsert in finalUpsertList){
			if (mappedRolesToUpsert['ADT_Patient_Visit__c'] == null){
				mappedRolesToUpsert.remove('ADT_Patient_Visit__c');
			}
		 
			if ((mappedRolesToUpsert['Provider__c']  && userIdToucsfIdMap[mappedRolesToUpsert['Provider__c']] == PCPNO1) || (mappedRolesToUpsert['Provider__c']  && userIdToucsfIdMap[mappedRolesToUpsert['Provider__c']] == PCPNO2)){
				mappedRolesToUpsert['Primary_Care_Provider__c'] = true;
			} else {
				mappedRolesToUpsert['Primary_Care_Provider__c']= false;
			}
			
		}
		return finalUpsertList;
	}

	//Salesforce objects must be a list of HashMap or Just a HashMap. Mule Salesforce connector doesn't handle when the data is null or blank. Doing here. For
	//each Sobject Mule connector expects array of feldName that is supposed to be passed as Null. If you don't explicitly specify this Mule/Salesforce will not
	//update the field.
	static addSalesforceNullField(sfData) {

		if (sfData != null){

			if (sfData instanceof Collection){
				sfData.each{ addSalesforceNullField(it) }
			} else if (sfData instanceof Map){
				def fieldsToNullList = new ArrayList()
				sfData.each{
					if (!(it.value instanceof Boolean) && !it.value ){
						fieldsToNullList.add(it.key)
					}

				}
				//add fieldsToNullList to the
				if (fieldsToNullList.size() > 0){
					sfData.put("fieldsToNull", (String [])  fieldsToNullList.toArray())
				}
			}
		}

	}


}