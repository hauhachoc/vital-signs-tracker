This text document provides short information about the "Vital Signs Tracker"
Android application classes.
Detail information about the design architecture can be found on:
https://code.google.com/p/vital-signs-tracker/wiki/AppDesignDoc

Note:
* The central of this application is the "welcome.java". This class provides
  two options to the user, whether the user is a health care provider or a 
  patient. If the user is a health care provider, he/she has to click the 
  'Provider' button to bring him/her to the provider sign-in page. On the other
  hand, if the user is a patient, he/she has to click the 'Patient' button to
  bring him/her to the patient sign-in page.

* Each of the sign-in page (either the provider or patient one) has several 
  UI, such as retrieve password, activate account, register account, and sign-in.

* Once the user (either the provider or patient) successfully sign-in to the system,
  they will go to their main lobby. Provider and patient's main lobby are different.
  Each of them has different features.
 
--------------
PATIENT
-------------- 
* Patient main lobby has several features, such as:
  - enter the vital signs information manually.
  - turn on blue-tooth to listen and get information from the blue-tooth device.
  - send emergency request to the doctor via server.
  - send emergency request using text messaging (SMS).
  - retrieve health care provider information.
  - display his/her vital signs report (table). data will be fed by server.
  - sign-out.

* Patient can enter the vital signs information manually. There are two classes
  that take care this features: PatientEnterManually1.java and PatientEnterManually2.java.
  The reason we provide this feature are:
  - the blue-tooth device can only give the heart-rate measurement 
    however, the vital signs information consists of heart-rate, body temp.,
    and blood pressure.
  - the application does not depend on the blue-tooth device that read the heart-rate
    measurement. 
  - The bluetooth heart rate is read from an external heart rate monitor from Zephyr Hxm BT.
    This is handled in the class BlueToothData.java which in turn uses the NewConnectedListener.java
    class. This class handles the processing of the input packets from the HxM and is responsible for 
    creating objects that parse the input data stream and other objects and methods that display the 
    data on the phone screen. 
      
  
  Once the patient enter the vital signs measurement, he/she has to submit it to 
  the server in order to be stored in the remote server. Once the information 
  are successfully stored on server, it will return to the patient main lobby screen.

* Patient can send emergency request to the doctor via server or SMS.
  The class that takes care this feature is PatientMainLobby.java and the method is
  emergencyRequestOnClick(View v).
  Once the patient trigger the emergency request, this method will collect the geopoints
  (latitude and longitude) of the patient, get the address from the geopoints using 
  Geocoder object, add user information and message. These data will be stored in 
  JSON object and then sent to the server.
  If the health care provider (doctor) is online, these emergency alert will be sent
  to the doctor via server using 'push notification'. The patient will receive Toast message
  that his/her emergency request has been sent to the doctor.
  If the health care provider (doctor) is offline, the server will respond to the client
  with 'fail' respond. Based on this 'fail' respond, this method will also trigger the
  SMS message to the doctor's phone with the geopoints, address, name, and message info.

* Patient can see his/her vital signs record that are stored in the remote server using
  the feature in the main lobby. The report is in landscape orientation. Patient can choose
  the length of the report (1 week, 2 weeks, 1 month, 3 months, and 1 year).


--------------
PROVIDER
--------------
* Health care provider main lobby has several features, such as:
  - subscribe a new patient to the system.
  - display all patients report.
  - display all patients line chart.
  - open persistence client server socket connection for push notification.
  - sign-out.

* To subscribe a new patient to the system, the health care provider has to provide
  the patient's basic information, such as name, email, et cetera. The class that takes
  care this feature is SubscribePatient.java

* Health care provider can retrieve the patients' vital signs data that are stored in server.
  Provider can choose the patient and the length of the report from the drop-down menu 
  (spinner). The patient's names are fed from the server, they are not hard-coded. 
  Any time the doctor adds a new patient to the system, the spinner will display the last
  up-to-date patients' names list.

* Health care provider also can view the patients' vital signs report in line chart. The line
  chart is an open source library. The package for the line chart (graph) is 
  com.vitalsigntracker.android.Graph.
  The class that provides the data and call the Graph class is PresentLineChart.java.

* To get the real time notification we create a push notification from the server.
  By using the push notification, information from the patient can be received right away;
  moreover, it also minimizes the battery consumption.
  Actually, Google App Engine provides a push notification using C2DM. However, I do not
  receive the permission from Google for the C2DM key (I applied for the C2DM key about 2-3
  months ago). Therefore, we have to create our own push notification.
  My first method was using 'pull notification' where the provider keeps sending request to
  the server to check for any incoming emergency notification. This can be done easily using
  AlarmManager and Service. However, this method drains the battery easily.
  The final modification is using 'push notification' where the provider (doctor) just open 
  a persistence socket connection (client-server). Once there is an emergency request from 
  a patient, the server will push this emergency request to the provider (doctor) right away
  via this persistence socket connection.
  The class that take cares this feature is ProviderConnectionThread.java. This class is called
  once the provider (doctor) sign-in to the system using service. The class that calls the
  ProviderConnectionThread class is ProviderLogin.java.

* The background service thread that keeps the persistence socket connection will be disconnected
  once the provider (doctor) sign-out from the system. The class that disconnect the socket 
  connection is ProviderMainLobby.java with the 'signOutOnClick()' method.
  
 
* Accelerometer study project generates data for decision making by recording sensor values and sending
an email to gmail as my SD card had issues with permissions

* Weka study project is an attempt to understand machine learning algorithms to apply for motion
detection





