For Run Project just use  mvn spring-boot:run

Using port set to 8080 according to Task pdf, but you can simply change it in /src/main/resources/application.properties

Sample Jason Object for pre-calculation:
{
	"loanAmount":"5000",
	"nominalRate":"5.0",
	"duration":"24",
	"startDate":"2018-01-01T00:00:00Z"
}
