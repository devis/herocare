package com.devis

class VeteranPopulation {
	
	static belongsTo = [state: State]

	Long total
	Long male
	Long female
	
    static constraints = {
    }
}