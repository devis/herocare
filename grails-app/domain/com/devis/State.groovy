package com.devis

class State {
	
	static hasMany = [cities: City, geoData: GeoData, facilities: HospitalFacility]
	
	String abbreviation
	String name
	VeteranPopulation population

    static constraints = {
		population (nullable: true)
    }
}
