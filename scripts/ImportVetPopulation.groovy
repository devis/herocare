import com.devis.*

import org.springframework.orm.hibernate3.SessionFactoryUtils
import org.springframework.orm.hibernate3.SessionHolder

def importVetPopulation = {ctx ->
	
	// without this you'll get a lazy initialization exception when using a many-to-many relationship
	def sessionFactory = ctx.getBean("sessionFactory")
	def session = SessionFactoryUtils.getSession(sessionFactory, true)
	
	def inFile = new File("${grails.util.BuildSettingsHolder.getSettings().baseDir}/datamodel/csv/statab2008_0505_VeteransBySexPeriodOfServiceAndBySt_0_Data.csv");
	
	inFile.eachLine { line ->
		def fields = line.split(',')
		def stateName = fields[0].trim()
		def total = fields[1].trim()
		def male = fields[2].trim()
		def female = fields[3].trim()
		
		def existingState = State.findByName(stateName)
		if (existingState && !existingState.population) {
			println "Adding new population"
			def population = new VeteranPopulation(total: total, male: male, female: female, state: existingState)
			if (!population.save(failOnError: true, flush: true)) {
				println "   Could not save new population : " + population.errors
			}
		}
		
	}
	
}.call(ctx)