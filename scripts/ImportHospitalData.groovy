import com.devis.*

import org.springframework.orm.hibernate3.SessionFactoryUtils
import org.springframework.orm.hibernate3.SessionHolder

def importHospitalData = {ctx ->
	
	// without this you'll get a lazy initialization exception when using a many-to-many relationship
	def sessionFactory = ctx.getBean("sessionFactory")
	def session = SessionFactoryUtils.getSession(sessionFactory, true)
	
	def waitFile = new File("${grails.util.BuildSettingsHolder.getSettings().baseDir}/datamodel/csv/DataGov_VHA_Hospital_Wait_Times.csv");
	def ratingFile = new File("${grails.util.BuildSettingsHolder.getSettings().baseDir}/datamodel/csv/DataGov_VHA_Hospital_Patient_Satisfaction.csv")
	
	ratingFile.eachLine { line ->
		def fields = line.split(',')
		
		def network = fields[1].trim().replace('"', '')
		def facility = fields[2].trim().replace('"', '')
		def cityName = fields[3].trim()
		def stateAbbr = fields[4].trim()
		def whiteIn = fields[5].trim().replace('*', '')
		def africanIn = fields[6].trim().replace('*', '')
		def otherIn = fields[7].trim().replace('*', '')
		def whiteOut = fields[8].trim().replace('*', '')
		def africanOut = fields[9].trim().replace('*', '')
		def otherOut = fields[10].trim().replace('*', '')
		
		if (fields[0].toUpperCase() != "VISN") {
			def existingState = State.findByAbbreviation(stateAbbr)
			def existingCity = City.findByNameAndState(cityName, existingState)
			if (!existingCity) {
				println "Couldn't find: ${cityName}, ${stateAbbr}"
				existingCity = new City(name: cityName, state: existingState)
				if (!existingCity.save(failOnError: true, flush: true)) {
					println "   Could not save new city : " + existingCity.errors
				}
			}
			def existingNetwork = HospitalNetwork.findByName(network)
			if (!existingNetwork) {
				println "Adding new network: ${network}"
				existingNetwork = new HospitalNetwork(name: network)
				if (!existingNetwork.save(failOnError: true, flush: true)) {
					println "   Could not save new network : " + existingNetwork.errors
				}
			}
			def existingFacility = HospitalFacility.findByNameAndNetwork(facility, existingNetwork)
			if (!existingFacility) {
				println "Adding new facility: ${existingNetwork}:${facility}"
				existingFacility = new HospitalFacility(name: facility, network: existingNetwork, city:existingCity, state:existingState)
				if (!existingFacility.save(failOnError: true, flush: true)) {
					println "   Could not save new facility : " + existingNetwork.errors
				}
			}
			if (existingFacility && !existingFacility.rating) {
				println "Adding new rating"
				def rating = new SatisfactionRating(whiteOutpatient: whiteOut, whiteInpatient: whiteIn,
					africanAmericanOutpatient: africanOut, africanAmericanInpatient, africanIn,
					otherOutpatient: otherOut, otherInpatient: otherIn, facility: existingFacility)
				if (!rating.save(failOnError: true, flush: true)) {
					println "   Could not save new rating : " + rating.errors
				}
			}
		}
		session.flush()
	}
	
	waitFile.eachLine { line ->
		
		def fields = line.split(',')
		def network = fields[1].trim().replace('"', '')
		def facility = fields[2].trim().replace('"', '')
		def cityName = fields[3].trim()
		def stateAbbr = fields[4].trim()
		def primaryCare = fields[5].trim()
		def specialtyCare = fields[6].trim()
		
		if (fields[0].toUpperCase() != "VISN") {
			def existingState = State.findByAbbreviation(stateAbbr)
			def existingCity = City.findByNameAndState(cityName, existingState)
			if (!existingCity) {
				println "Couldn't find: ${cityName}, ${stateAbbr}"
				existingCity = new City(name: cityName, state: existingState)
				if (!existingCity.save(failOnError: true, flush: true)) {
					println "   Could not save new city : " + existingCity.errors
				}
			}
			def existingNetwork = HospitalNetwork.findByName(network)
			if (!existingNetwork) {
				println "Adding new network: ${network}"
				existingNetwork = new HospitalNetwork(name: network)
				if (!existingNetwork.save(failOnError: true, flush: true)) {
					println "   Could not save new network : " + existingNetwork.errors
				}
			}
			def existingFacility = HospitalFacility.findByNameAndNetwork(facility, existingNetwork)
			if (!existingFacility) {
				println "Adding new facility: ${existingNetwork}:${facility}"
				existingFacility = new HospitalFacility(name: facility, network: existingNetwork, city: existingCity, state: existingState)
				if (!existingFacility.save(failOnError: true, flush: true)) {
					println "   Could not save new facility : " + existingNetwork.errors
				}
			}
			if (existingFacility && !existingFacility.waitTime) {
				println "Adding new wait time"
				def waitTime = new WaitTime(primaryCare: primaryCare, specialtyCare: specialtyCare, facility: existingFacility)
				if (!waitTime.save(failOnError: true, flush: true)) {
					println "   Could not save new waitTime : " + waitTime.errors
				}
			}			
		}
		session.flush()
	}
	
}.call(ctx)