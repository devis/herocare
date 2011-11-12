import com.devis.*

import org.springframework.orm.hibernate3.SessionFactoryUtils
import org.springframework.orm.hibernate3.SessionHolder

def importGeoData = { ctx ->
	
	// without this you'll get a lazy initialization exception when using a many-to-many relationship
	def sessionFactory = ctx.getBean("sessionFactory")
	def session = SessionFactoryUtils.getSession(sessionFactory, true)
		
	def zipcodeFile = new File("${grails.util.BuildSettingsHolder.getSettings().baseDir}/datamodel/csv/zip-codes-database-03252010.csv");
	def stateFile = new File("${grails.util.BuildSettingsHolder.getSettings().baseDir}/datamodel/csv/states.csv")
	
	stateFile.eachLine { line -> 
		def fields = line.split(",")
		def stateName = fields[0].trim()
		def stateAbbr = fields[1].trim()

		def existingState = State.findByAbbreviation(stateAbbr)
		if (!existingState) {
			println "Adding new state: ${stateName}"
			def newState = new State(name: stateName, abbreviation: stateAbbr)
			if (!newState.save(failOnError: true, flush: true)) {
				println "   Could not save new state : " + newState.errors
			}
		}
	}

	zipcodeFile.eachLine { line ->
		def fields = line.split(",")

		def zipcode = fields[0].trim()
		def stateAbbr = fields[1].trim()
		def latitude = fields[3].trim()
		def longitude = fields[4].trim()
		def cityName = fields[7].trim()
		def existingState = State.findByAbbreviation(stateAbbr)
		if (existingState) {
			def existingCity = City.findByNameAndState(cityName, existingState)
			if (!existingCity) {
				println "Adding new city: ${cityName}, ${existingState.abbreviation}"
				existingCity = new City(name: cityName, state: existingState)
				if (!existingCity.save(failOnError: true, flush: true)) {
					println "   Could not save new city : " + existingCity.errors
				}
			}
			if (existingCity) {
				def geoData = GeoData.findByZipcode(zipcode)
				if (!geoData) {
					geoData = new GeoData(zipcode: zipcode, latitude:latitude, longitude:longitude, city: existingCity, state: existingState)
					if(!geoData.save(failOnError: true, flush:true)) {
						println "   Could not save new geo data: " + geoData.errors
					}
				}
			}
		}
		session.flush()
	}	
}.call(ctx)