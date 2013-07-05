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
				}

			} else {
				logger.info("userId not found  for " + role)
				warnings = warnings + "userId not found  for " + role + "\n"
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
			warnings = warnings + " no ucsfid for " + role + "\n";
				
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

	static getFinalRolesToUpsert(mappedrolesForUpsertList,saveRoles , PCPNO1, PCPNO2){

		//hashset of Role external key which should be PVCareRoleKey__c of existing record if present.
		def roleUpsertSet = new HashSet<String>()
		def rolesToDelete = new ArrayList<>()
		for (mappedRolesToUpsert in mappedrolesForUpsertList){
			roleUpsertSet.add(mappedRolesToUpsert.ExternalKey__c)
		}
		for (role in saveRoles){
			// if upsertRoles doesn't have the role then add to upsertMap as delete.
			if(!roleUpsertSet.contains(role.ExternalKey__c)){
				def newRole = HelperUtils.deepClone(role)
				//set role isactive flag to false
				def clonedRole = (Map) HelperUtils.deepClone(newRole)
				clonedRole.Active__c = false;
				rolesToDelete.add(clonedRole)
			}


		}
		if (rolesToDelete.size() > 0){
			mappedrolesForUpsertList.addAll(rolesToDelete);
		}
		//setup PCP
		for (mappedRolesToUpsert in mappedrolesForUpsertList){
			if (mappedRolesToUpsert['ucsfid'] == PCPNO1 || mappedRolesToUpsert['ucsfid'] == PCPNO2){
				mappedRolesToUpsert['Primary_Care_Provider__c'] = true;
			} else {
				mappedRolesToUpsert['Primary_Care_Provider__c']= false;
			}
			
		}
		return mappedrolesForUpsertList;
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
					if (!it.value){
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