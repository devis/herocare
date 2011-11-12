package com.devis

class HospitalFacility {
	
	static belongsTo = [network: HospitalNetwork, city: City, state: State]

	String name
	SatisfactionRating rating
	WaitTime waitTime
	
    static constraints = {
		SatisfactionRating (nullable: true)
		WaitTime (nullable: true)
    }
}