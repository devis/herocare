package com.devis

class HospitalNetwork {
	
	static hasMany = [facilities: HospitalFacility]

	String name
	
    static constraints = {
    }
}