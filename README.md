# Micronaut sample project

* First start the app by either running the Application class directly or via maven.
* Check the [routes endpoint](http://localhost:8080/routes) to see all available routes
* Run `curl -v "http://localhost:8080/login"  -H "Content-Type: application/json" -d '{"username":"sherlock", "password":"password", "secret":"password", "identity":"sherlock"}'` to obtain an oauth token