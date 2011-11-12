package com.devis

class State {
	
	static hasMany = [cities: City, geoData: GeoData]
	
	String abbreviation
	String name

    static constraints = {
    }
}
