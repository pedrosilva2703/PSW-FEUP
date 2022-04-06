
[PDF](installation_guide.pdf)


<h2 align="right" id="heading">Warehouse Management Software</h3>

___


> #### Purpose of the document
 The purpose of this document is to describe the installation process of our program. We will go step by step on how you can 
 install and run the necessary files so that everything works as expected.

 There are two ways of installing the application, 
 so choose only one of the following top-ics.

> ##### 1.Running through the artifacts
 Firstly, you will need to be connected to the FEUP network, either by VPN or locally. 
 This is required for the database functionality to work. A java SDK version 17 or higher is also required.
 
 Then, you need the cepejoma folder. We assume you already downloaded it to your computer, so just find it and place it wherever you please. 
 In case you don’t have access to your file, please it from our GitLab page: https://gitlab.com/psw2122/a03/cepejoma. <br>
 After downloading go to 
 ````` stockAlote\out\artifacts\stockAlote_jar\ ````` 

 Double click the BAT file and, if prompted, grant administrator permissions to the program. You may also need to open an exception
 on the firewall, which is necessary to connect to the database.
 The program should open normally and a welcoming screen should appear.


> ##### 2.Running source code in IntelliJ IDE
Before you can run the program, please open the stockAlote project file with IntelliJ. 
Next, go to the Main file located under 
``````stockAlote/src/GUI/``````

Run the Main class using IntelliJ. An error should occur: “JavaFX runtime components are missing, and are required to run this application”. 
To fix this, go on the top menu of the IDE, click on Run, then Edit Configurations. Once that’s done, you should see a blue text on the right 
of the windows saying ”Modify options”. 

Click on it and a menu will show up. Select the option “Add VM options”. Now, a new text box will appear next to the java SDK drop down menu. 
For the error to vanish, insert the following code in this text box:
``````--module-path "javafx-sdk-17.0.1\lib" --add-modules javafx.controls,javafx.fxml``````

Now you may run Main again and everything should work.
