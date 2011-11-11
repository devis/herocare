import org.codehaus.groovy.grails.commons.GrailsApplication
import grails.util.GrailsUtil
import grails.util.Environment

import com.devis.*

class BootStrap {

    def init = { servletContext ->

        if (GrailsUtil.environment == GrailsApplication.ENV_DEVELOPMENT) {
			String[] databaseManagerOptions = ['--url', 'jdbc:hsqldb:file:databases/devDB'];
	        org.hsqldb.util.DatabaseManagerSwing.main(databaseManagerOptions)
			importData()
        }

        if (GrailsUtil.environment == GrailsApplication.ENV_TEST) {  
        }
     }

     def destroy = {
     }



	def importData() {
		def zipcodeFile = new File("${grails.util.BuildSettingsHolder.getSettings().baseDir}/datamodel/csv/zip-codes-database-03252010.csv");
		def stateFile = new File("${grails.util.BuildSettingsHolder.getSettings().baseDir}/datamodel/csv/states.csv")
		
		stateFile.eachLine { line -> 
			def fields = line.split(",")
			def stateName = fields[0]
			def stateAbbr = fields[1]

			def existingState = State.findByName(stateName)
			if (!existingState) {
				println "Adding new state: ${stateName}"
				def newState = new State(name: stateName, abbreviation: stateAbbr)
				if (!newState.save(flush: true)) {
					println "   Could not save new state : " + newState.errors
				}
			}
		}

		zipcodeFile.eachLine { line ->
			def fields = line.split(",")

			def zipcode = fields[0]
			def stateAbbr = fields[1]
			def latitude = fields[3]
			def longitude = fields[4]
			def cityName = fields[7]
			def existingState = State.findByAbbreviation(stateAbbr)
			if (existingState) {
				def existingCity = City.findByNameAndState(cityName, existingState)
				if (!existingCity) {
					existingCity = new City(name: cityName, state: existingState)
					if (!existingCity.save(flush: true)) {
						println "   Could not save new city : " + existingCity.errors
					}
				}
				if (existingCity) {
					def geoData = GeoData.findByZipcode(zipcode)
					if (!geoData) {
						geoData = new GeoData(zipcode: zipcode, latitude:latitude, longitude:longitude, city: existingCity, state: existingState)
						if(!geoData.save(flush:true)) {
							println "   Could not save new geo data: " + geoData.errors
						}
					}
				}
			}
		}	
	}
}

