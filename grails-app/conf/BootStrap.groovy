import org.codehaus.groovy.grails.commons.GrailsApplication
import grails.util.GrailsUtil
import grails.util.Environment

import com.devis.*

class BootStrap {

    def init = { servletContext ->

        if (GrailsUtil.environment == GrailsApplication.ENV_DEVELOPMENT) {

        }

        if (GrailsUtil.environment == GrailsApplication.ENV_TEST) {  
        }
     }

     def destroy = {
     }

}

