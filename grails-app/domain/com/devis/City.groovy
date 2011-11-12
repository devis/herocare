package com.devis

class City {

	static belongsTo = [state: State]
	static hasMany = [geoData: GeoData]

	String name
	
    static constraints = {
    }
}
