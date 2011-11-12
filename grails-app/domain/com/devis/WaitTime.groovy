package com.devis

class WaitTime {
	
	static belongsTo = [facility: HospitalFacility]

	Integer primaryCare
	Integer specialtyCare
	
    static constraints = {
    }
}