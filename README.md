# 4) socioregister
A Springboot REST application to register and add Socios, the final/ full Springboot REST-application

1) General Info About the Socio-Micro-Service-Demo

2) Specific Info Concerning Each Single Application



## General Info ====

The Socio Micro Services Project will consist of about 10 small (backend) Springboot applications, deployed in a Docker Container/ Linux Oracle Virtual Box. SocioRegister is the principal part of a series of four applications called: starter, mock, jpa, socioregister. Together they show a stepwise buildup to a Springboot REST application, which contains use-cases for registering and adding Socios (similar to Facebook). This line of applications goes from an almost empty Springboot shell (starter: one controller method only) to a small but full-fledged REST application: SocioRegister which will be used as a component of our micro-services.

Next you`ll find four other serving applications. The simple SocioWeather, provides a weather-report by city by consulting an external REST-service called Open Weather. SocioBank, permits money transaction between Socios, also consulting an external service for exchange rates. The SocioSecurity, a Cookie/ Token based SpringSecurity (OAUTH2), still has to be written still. Finally the SocioDbBatch application is interesting because it will update, on a daily bases, the databases of SocioRegister (socio_db) and SocioBank (soicio_bank_db). The DBs run on MySQL or Postgres.

From SocioRegister-jpa one finds backend-Validation (javax) and REST-Exception Handeling of Spring (RestControllerAdvice).

Testing, in general, will have an important focus and since we are dealing with Spring(boot) there will systematically testing based on five mayor strategies:

	-@ExtendWith(MockitoExtension.class)

	-@ExtendWith(SpringExtension.class) standalone setup (two ways)

	-@ExtendWith(SpringExtension.class) server tests (@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

	-@DataJpaTest wich is database testing on H2

	-Spring Batch testing

Testing is still "work in progress"



## Specific Info SocioRegister ====

The previous three applications (starter, mock, jpa) form the prelude for the final SocioRegister application to be deployed at a Docker Container. Maybe it is a good idea to start first with a small recap?

	1) SocioRegister-starter shows how to begin a Micro-Service setup beginning with an empty shell of a Springboot (WEB/REST) application. Stressing the importance of the Maven pom (or Gradle file) and the Springboot Starter Dependencies.
	
	2) SocioRegister-mock is already more personalized, adding several Socio-layers to the mix: first the principal layers, the controller, service (interface and implemention), and the Repository; second, from DTO via Modelmapper convertion to Model (Entity) and visa versa; third, the use of the ResponseEntity class at the controller's returns.
	
	3) SocioRegister-jpa adds the Spring-data layer in the form of JPA-persistence (Postgres DB). It further goes into the implementation of validation messages. And finally one finds an implementaion of a Spring Global Exception Handeling (amung others the ResourceNotFoundException) based on the @RestControllerAdvice and the ResponseEntityExceptionHandler.

Like I said before at this part I would like to elaborate on testing and DB initialization.

### Spring Profiles

But first note a new feature called Spring Profiles present at the resources folder and the DBConfig class at the root. The property spring.profiles.active=dev, which sets the different profiles, you will find at application.properties. There are three profile-options present: test, dev, and pro. Each option points at a different DB. You may simply change the spring.profiles.active= into test, dev, or prod and see for yourself the results. To see it work correctly you first have to install and initialize each single db (H2, Postgres and MySQL).


### DB Initialization

Db initialization is a tricky thing but an importanty feature when changing frequently to different environments. Next read what I know about it which I learned very much by trail and error:

	1) each different DB-type, H2, Postgres etc, has their own query dialects. At the src/main/resources you`ll find a schema of MySQL.
	
	2) See to it that all model/ entity classes have the correct hibernate/jpa annotations concerning each table and their relations: very important!
	
	3) Write the insert-data into a data.sql file at src/main/resources, using the correct query langauge for each dialect (Springboot auto-config will pickup that file);
	
	4) Use the following properties for initialization (see application(-prod/dev/test).properties): 
		-spring.jpa.hibernate.ddl-auto=create   / none
		-spring.datasource.initialization-mode=always   / never
		-spring.datasource.initialize=true   / false
	The first option create/always/true I use only at the first time starting the app when there is no DB present (of course after installing the servers for each DB-type). I noted that these props work best with H2 and Postgres. (There is also the issue of declaring the DB as Schema or Catalog at each Model/ Entity Java class, which MySQL likes most.....)
	
### Spring Testing

Spring is many things, but principally it is dependency injection of classes/ beans into other classes. Key to dependency injection is the Spring Application Context, a namespace/ file-tree where Spring scans for @Component, @Service, @Repository annotated classes, to be injected (@Autowired) into other classes. 

All this is NOT available at the Test-directory, so what to do? Either we need to mock all these dependencies needed, or we have to invoke a complete or partial Spring Context. But first we add the Mockito5 spring-boot-starter-test to the pom (the exclude  tag results in excluding Mockito4) There are three strategies concerning Spring Testing focused on a Spring-REST-app:

#### 1) MockMvc

One may instantiate it when using the @ExtendWith(MockitoExtension.class) annotation at a controller class (non Spring approach) in two ways:
	
	1) MockMvc mockMvcPartial = MockMvcBuilders.standaloneSetup(socioController).build();
	2) MockMvc mockMvcComplete =  MockMvcBuilders.webAppContextSetup(webApplicationContext).build(); 

To invoke a partial context (stand alone) in Spring, one adds the following annotations to the controller class: @ExtendWith(SpringExtension.class), @AutoConfigureJsonTesters, @WebMvcTest(SocioController.class) and next, one injects the MockMvc by the @Autowired annotation.

To invoke a complete context use the following two annotations: @ExtendWith(SpringExtension.class) @SpringBootTest.

All these approaches have in common that they do not start a real server! Note: starting a complete context is very time consuming!

#### 2) TestRestTemplate

TestRestTemplate is very similar to the RestTemplate it starts a real server for testing (private TestRestTemplate restTemplate;). Together with the following three annotations: @ExtendWith(SpringExtension.class), @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT), @AutoConfigureJsonTesters the test methods are able to use the powerfull, previous explained ResponseEntity.

#### 3) @DataJpaTest

A class annotated with DataJpaTest invokes a fully functional JPA persistence context to be executed for a H2 internal db. The next two annotations are the setup of this test environment: @TestPropertySource({"classpath:application-test.properties”}), @DataJpaTest. As a matter of exercise I have tested all service method's, which have no business logic, db transactions. Normally one would not test the standard implemented JPA methods such as findById, but only the ones that run on native-queries done by the developer, right!

#### Test Class, Mocks, Injected Dependencies and Pure Mockito Testing

The class under tested should be mocked by using the @InjectMocks annotation. The services and repositories mocks should be annotated with @MockBean and within a non-Spring Mockito context (@ExtendWith(MockitoExtension.class)) with  @Mock. The JacksonTester, which handles the json-objects conversion, should be instantiated outside Spring. In Spring  the class can be injected by @Autowired.

About the Test Methods in General

Before any test method you declare a method called setup() annotated with @BeforeEach to prepare things before each test. A test method is a test method when annotated with @Test. The code uses BDDMockito (Behavior Driven Development) and AssertJ with the general structure of: given -> when -> than resulting in easy readable test. The BDD test-paradigma will be noted especialy at the Mockito-test-version of the service implemtation classes like SocioServiceMockitoTest at the test package.


### Use-cases

The use-cases of socioregister are more extend since there are 8 tables present now:

	-http://localhost:8081/socio

	-http://localhost:8081/socio/2

	-http://localhost:8081/socio/username/js
	
	-http://localhost:8081/address
	
	-http://localhost:8081/address/2
	
	-http://localhost:8081/address/socioId/2
	
	-http://localhost:8081/country
	
	-http://localhost:8081/language
	
	-http://localhost:8081/role

And by using Postman:

	-post http://localhost:8081/socio  
		{
			"username": "rs",
			"password": "secret",
			"firstName": "Richard",
			"lastName": "Strauss",
			"email": "strauss@gmail.com",
			"active": true,
			"socioLanguages": [
				{"id": "2"},
				{"id": "3"}
			] 
		} 
  
	-post http://localhost:8081/address
		{ 
			"street":"Bachstraat 24",
			"city":"Haarlem",
			"province":"Noord Holland",
			"postalcode":"4365KL",
			"description":"some comment",
			"country": {"id": 5},
			"socioId":3,
			"addressType": "HOME"
		}
	
	-put http://localhost:8081/socio  <- no path variable here!
		{
			"id": 7, <- note the id of the existing socio!
			"username": "rsxxx",
			"password": "secret",
			"firstName": "Richard",
			"lastName": "Strauss",
			"email": "strauss@gmail.com",
			"active": true,
			"socioLanguages": [
				{"id": "2"},
				{"id": "3"}
			] 
		} 
	Note pls that different from the two previous applications (mock and jpa) here one finds no @PathVariable (/{id}) at the url. Incase of an update the id field has to be present at the json object as shown in the above example!
	
    -add an associated socio: post http://localhost:8081/associatedSocio/1/2    (/{socioId}/{associatedSocioId})
	-change state: put http://localhost:8081/associatedSocio/1/2/true    (/{socioId}/{associatedSocioId}/{boolean} for ACCEPTED/ DENIED)
	-delete an associated socio: delete http://localhost:8081/associatedSocio/1/2    (/{socioId}/{associatedSocioId})
	
### Appendix Use-cases for the PaginateSortSocioController class

	paginate with default: http://localhost:8081/paginateSocio
	paginate with row/ offset params: http://localhost:8081/paginateSocio?rows=2&offset=0
	idem plus language filter: http://localhost:8081/paginateSocio?rows=2&offset=0&language=ES
	idem plus country filter: http://localhost:8081/paginateSocio?rows=2&offset=0&country=NL
