
## Data Sources
* http://www.infochimps.com/datasets/veterans-by-sex-period-of-service-and-by-state-2005#overview_tab
* http://explore.data.gov/Health-and-Nutrition/Veterans-Health-Administration-2008-Hospital-Repor/hmjx-6c8s
* http://explore.data.gov/Health-and-Nutrition/Veterans-Health-Administration-2008-Hospital-Repor/jmdz-e9wx

## Importing data
`<environment>` is one of `dev, test, prod`
```bash
 grails <environment> shell
 load scripts/ImportGeoData.groovy
 clear
 load scripts/ImportHospitalData.groovy
 clear
 load scripts/ImportVetPopulation.groovy
```
