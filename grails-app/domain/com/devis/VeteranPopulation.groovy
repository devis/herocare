package com.devis

class VetaranPopulation {
	
	static belongsTo = [state: State]

	Long total
	Long male
	Long female
	
    static constraints = {
    }
}