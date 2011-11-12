package com.devis

class GeoData {
	
	static belongsTo = [city: City, state: State]
	
	String zipcode
	Double latitude
	Double longitude

    static constraints = {
    }
}
