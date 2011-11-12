package com.devis

class VeteranPopulation {

	static belongsTo = [state: State]

	Float total
	Float male
	Float female
	
    static constraints = {
    }
}