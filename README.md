Example performance - Gatling plugin based performance tests
==============================================================

Example performance tests using Gatling. Refer to the plugin documentation
[on the Gatling website](https://gatling.io/docs/current/extensions/maven_plugin/) for further information.

Parameters can be passed from the command line via java vars in order to set simulation preferences:

| Parameter                     |          Description           |         Default value |
|-------------------------------|:------------------------------:|----------------------:|
| baseUrl                       |            Base url            | http://localhost:8080 |
| rampUsers                     |      Number of ramp users      |                     1 |
| rampTime                      |      Ramp time in seconds      |                     1 |
| constantUsersPerSec           |    Number of constant users    |                     1 |
| constantUsersTime             | Constant users time in seconds |                     1 |

Example execution:

```console
mvn  -DrampUsers=10 -DrampTime=10 -DconstantUsersPerSec=5 -DconstantUsersTime=10  clean gatling:test
```


