package com.myapp.camel.routes;

import com.myapp.camel.avro.Order;
import org.apache.camel.builder.RouteBuilder;
 // This is the class in question

import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.dataformat.avro.AvroDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

//@Component
public class JsonToAvroToJmsRouter extends RouteBuilder {
    @Autowired
    private JacksonDataFormat jacksonDataFormatForOrder;

    @Override
    public void configure() throws Exception {
        AvroDataFormat avroDataFormat = new AvroDataFormat(Order.SCHEMA$);

        from("file:data/avroorders?include=.*\\.json$&noop=true&autoCreate=false&directoryMustExist=true")
                .routeId("avro-json-route")

                .log("File ${header:CamelFileName} contents:\n ${bodyAs(String)}")

                .unmarshal(jacksonDataFormatForOrder)

                .marshal(avroDataFormat)
                .to("myJms:queue:orderavro");

        from("myJms:queue:orderavro")
                .unmarshal(avroDataFormat) // Convert Avro binary to Java object
                .log("✅ Received Order: ${body}");

    }
}


// Primitive Types
// boolean
// int
// long
// float
// double
// bytes
// string


// record
// enum
// array
// map
// union ("null":"int")
// date -> int
// time-millis



// SimpleRegistry
// ApplicationContextRegistry
// OsgiServiceRegistry
// CdiBeanRegistry

// Data Formats provided with camel

// XML
// JSON
// CSV
// Bindy
// Avro -> Binary Format
// ProtoBuf
// Gzip
// JAXB
// Crypto
// HL7

