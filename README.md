# MainQuest

### Warehouse Management Software

<div align="center">
<img src="https://gitlab.com/psw2122/a03/cepejoma/-/wikis/uploads/logo.png" alt="logo;" >
</div>

#### Installation Guides
<div >
Before you can run the program, please open the stockAlote project file with IntelliJ. Next, go to the Main file located under stockAlote/src/GUI/.

Run the Main class using IntelliJ. An error should occur: “JavaFX runtime components are missing, and are required to run this application”. 
To fix this, go on the top menu of the IDE, click on Run, then Edit Configurations. Once that’s done, you should see a blue text on the right of the windows saying ”Modify options”. 
Click on it and a menu will show up. Select the option “Add VM options”. Now, a new text box will appear next to the java SDK drop down menu. 
For the error to vanish, insert the following code in this text box: 

--module-path "javafx-sdk-17.0.1\lib" --add-modules javafx.controls,javafx.fxml

Now you may run Main again and everything should work.

</div>