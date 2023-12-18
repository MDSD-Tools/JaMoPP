# clnr-demo
Initial demo of a data streaming application using the CLNR dataset.

This demo will show a number of containers processing data streams of smart meter readings. 
The data was generated by the [Customer Led Network Revoultion](http://www.networkrevolution.co.uk/project-data-download/?dl=TC1a.zip#) project. 
Initially the demo will use the TC1a dataset which has domestic smart meter readings on 30 minute intervals.

## Ingest API

The IngestAPI module exposes a REST endpoint which will accept CSV or JSON objects representing smart meter readings.
These messages are sent downstream as JSON objects on a Kafka Topic. The CSV represents one sample of smart meter containing the customerId, timestamp and kWh reading since the last sample.

## Persist to DB

The Persist to DB module will store the JSON objects it receives as Readings in a MySQL database. In a future version of the demo this will be monitored by Debezium.

## Running the example

In order to run this example you need to deploy a Postgres database into your project.
This can be achieved with the following command

```bash
oc new-app -e POSTGRESQL_USER=luke -ePOSTGRESQL_PASSWORD=secret -ePOSTGRESQL_DATABASE=my_data openshift/postgresql-92-centos7 --name=my-database
```  

Once the Postgres has started the IngestAPI and PersistToDB containers can be deployed using the command `mvn clean package fabric8:deploy` from the module directories.

There is a script in the smart-meter-simulator project which will send sample data to the application. 
Set the `INGEST_URL` environment variable to point to your Ingest API application and run the `start.sh` script with a parameter of one fo the files in the resources directory.

```bash
$ oc get routes
NAME                  HOST/PORT                                             PATH      SERVICES              PORT      TERMINATION   WILDCARD
ingest-api            ingest-api-hardcoded-test.127.0.0.1.nip.io                      ingest-api            8080                    None
persist-to-db         persist-to-db-hardcoded-test.127.0.0.1.nip.io                   persist-to-db         8080                    None

$ export INGEST_URL=ingest-api-hardcoded-test.127.0.0.1.nip.io

$ ./start.sh small.csv
Sending data to ingest-api-hardcoded-test.127.0.0.1.nip.io
2011.05.01,00:00:00,1077,0.019
201
2011.05.01,00:00:00,1083,0.113
201
...
^C
$
```