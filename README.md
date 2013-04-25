Vital Signs Tracker
===================

The Android application project for CSC 780 at San Francisco State University

Introduction
------------

The expensive cost of health care service is one of the important issues in the US. Many factors that contribute to the cost of the health care service, such as insurance, facility, pharmaceutical company, doctor, et cetera. If some of the factors could be eliminated then the cost of health care service can significantly decrease. Based on that idea, we try to create an application, that might be one of the solutions in reducing the health care cost, which gives the health care provider (doctor) to provide a remote monitoring system to patients. While it might reduce the health care cost, at the same time it can also increase the service ratio of doctors, which means more patients could be served. Moreover, we also try to utilize the current technology (internet and mobile devices) for the benefit in health care field.

The "Vital Signs Tracker" application can be used to record patients' vital signs like blood pressure, heart rate, and body temperature. This application will help the users to assess the most basic body functions.

The features of this app will help a user to create a history of the vital signs measurements and share it with the health care professionals thus helping in maintenance of a brief medical history.

The application is designed to help the users track their vital signs and maintain a history of the data. The data will be stored in the server. Hence, at the very least with the help of this app basic bodily functions can be monitored by the nurse or the doctor without being present at the patient's location.

The benefits for health care providers:

* Provide remote monitoring system to the patients.
* Serve more patients.
* Retrieve patients' information electronically at anytime.
* Provide less cost service.

The benefits for patients:

* Stay at home and do regular daily activities (do not have to stay at the hospital).
* Decrease health care cost.
* Save travelling time to the hospital.
Get emergency help right away.
Convenience.

Design Architecture
-------------------

![Design Architecture](https://raw.github.com/jaykhopale/vital-signs-tracker/master/system_design.png)


Explanation of Design Architecture
----------------------------------

* The client side (Android Application) consists of two different user interfaces; one user interface for the health-care provider and the other one for the patient.
* The server side, that listen and respond to clients' requests, uses Java socket programming language.
* The database, that stores all related information, uses PostgreSQL.


How to Setup the System?
------------------------

* Download and setup the Java program (server code) on the server side. Set the port number that will be use for the listening port, eg. port 8080.
* Create database and tables in PostgreSQL based on the Database schema below.
* Download and install the client side application on any Android phones (minimum SDK: Android Google API 2.2).


Database Schema
---------------

![Database Schema](https://raw.github.com/jaykhopale/vital-signs-tracker/master/db_schema.png) 


Features of The Application (Version 1.0.0)
-------------------------------------------

### For Patient ###

* Enter the vital signs data manually.
* Enter the heart rate information using bluetooth device. Bluetooth device has to be purchased separately.
* Upload vital signs data to the remote server (to be stored in the database).
* View the vital signs record in table format.
* Request emergency request manually.
* Request emergency request based on motion.

### For Health Care Provider/Doctor ###

* Subscribe a patient to the remote monitoring system.
* View the vital signs record of each patient in 'table' format.
* View the vital signs record of each patient in 'chart' format.
* Receive real time emergency request from the patient in Android notification bar (if the doctor is online) or in SMS format (if the doctor is offline).
* The emergency notification also provides the patient's location in Google Maps and the detail address (text).

