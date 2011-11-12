package com.devis

class SatisfactionRating {
	
	static belongsTo = [facility: HospitalFacility]

	Integer whiteInpatient
	Integer whiteOutpatient
	Integer africanAmericanInpatient
	Integer africanAmericanOutpatient
	Integer otherInpatient
	Integer otherOutpatient
	
    static constraints = {
		whiteInpatient (nullable: true)
		whiteOutpatient (nullable: true)
		africanAmericanInpatient (nullable: true)
		africanAmericanOutpatient (nullable: true)
		otherInpatient (nullable: true)
		otherOutpatient (nullable: true)
    }
}