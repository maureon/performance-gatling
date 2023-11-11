package dummy;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.csv;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.jmesPath;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

/**
 * This sample is based on our official tutorials:
 * <ul>
 *   <li><a href="https://gatling.io/docs/gatling/tutorials/quickstart">Gatling quickstart tutorial</a>
 *   <li><a href="https://gatling.io/docs/gatling/tutorials/advanced">Gatling advanced tutorial</a>
 *   <li><a href="https://www.baeldung.com/gatling-load-testing-rest-endpoint">Load testing rest endpoint</a>
 *
 * </ul>
 */
public class DummySimulation extends Simulation {

    FeederBuilder.Batchable<String> feeder = csv("employees.csv").random();

    ChainBuilder createEmployee =

        exec(
                http("Create employee")
                        .post("/create")
                        .body(StringBody("{\"name\": #{name},"
                            + " \"salary\": #{salary}"
                            + " \"age\": #{age} }")).
                        check(
                                status().is(200)
                                //Note that you can store response values for further usage
                                //,jmesPath("id").saveAs("user-id")
                        )
            );


    String baseUrl = System.getProperty("baseUrl", "https://dummy.restapiexample.com");

    HttpProtocolBuilder httpProtocol =
        http.baseUrl(baseUrl)
            .acceptHeader("application/json")
            .contentTypeHeader("application/json")
            .acceptLanguageHeader("es-ES,es;q=0.9")
            .acceptEncodingHeader("gzip, deflate, br")
            .userAgentHeader(
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0"
            );

    ScenarioBuilder users = scenario("Users")
        .feed(feeder)
        .exec(createEmployee);

    // Ramp users config
    int rampUsers = Integer.getInteger("rampUsers", 1);
    long rampTime = Long.getLong("rampTime", 1);

    // At once users
    double constantUsersPerSec = Double.valueOf(Integer.getInteger("constantUsersPerSec", 1));
    long constantUsersTime = Long.getLong("constantUsersTime", 1);

    {
        setUp(
                users.injectOpen(
                        constantUsersPerSec(constantUsersPerSec).during(constantUsersTime),
                        rampUsers(rampUsers).during(rampTime))
        ).protocols(httpProtocol);
    }
}
