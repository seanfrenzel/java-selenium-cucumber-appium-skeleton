Java-Cucumber
======
Test automation skeleton for java-cucumber

Resources
---
- [Cucumber-JVM](https://cucumber.io/docs/reference/jvm)
- [Cucumber-JVM API](http://cucumber.github.io/api/cucumber/jvm/javadoc/)
- [Appium](http://appium.io/)
- [Appium Touch Actions](http://appium.io/docs/en/writing-running-appium/touch-actions/)
- [Appium Touch Actions Through Scripts](https://github.com/appium/appium/blob/master/docs/en/writing-running-appium/ios/ios-xctest-mobile-gestures.md)
- [Appium client API](http://appium.github.io/java-client/)
- [Appium capabilities](http://appium.io/docs/en/writing-running-appium/caps/)
- [Android Emulator](https://developer.android.com/studio/run/emulator)
- [ADB](https://developer.android.com/studio/command-line/adb)
- [iOS Emulator](https://help.apple.com/simulator/mac/current/#/deve44b57b2a)
    - useful links for ios [simctl](https://www.iosdev.recipes/simctl/) commands

Setup
---
**Install** [Xcode](https://developer.apple.com/xcode/)
\
After installation, be sure to _`open and accept the license agreements`_.
Ensure the _`Xcode command line tools`_ are installed during the process

**Install** [Android SDK](https://developer.android.com/studio/#downloads)
\
`Create new Android Studio Project` with `default settings` from `API 23` and `No Activity`. Continue to import and install any settings suggested by the SDK. 

**Install** [IntelliJ](https://www.jetbrains.com/idea/download):
\
Install cucumber plugin
_`Preferences`_ > _`Plugins`_ > _`Cucumber for Java`_
    
**Download** [Appium Desktop](https://github.com/appium/appium-desktop): 
\
This is what we will use to inspect the devices.

Environment variables
---

#### Mac OSX:
**1.** Open a terminal and proceed with the following:
`$ open ~/.bash_profile
`\
**2.** Set environment variables
```
export ANDROID_HOME=/path/to/your/android/sdk
export ANDROID_SDK_ROOT=$ANDROID_HOME
export JAVA_HOME=$(/usr/libexec/java_home)
export PATH=${PATH}:$ANDROID_HOME/emulator:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools:$JAVA_HOME/bin
```
\
**3.** Save changes, reopen terminal and enter the following.
- **Homebrew**: 
`$ ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"`
    - After installation: `$ brew doctor` should state `Your system is ready to brew`
- **Node**:`$ brew install node`
- **Git**:`$ brew install git`
- **libimobiledevice - iOS**:`$ brew install libimobiledevice`
- **carthage - iOS**:`$ brew install carthage`
- **ios-deploy - iOS**:`$ npm install -g ios-deploy`
    - ios-deploy should be global.
    
#### Windows OS:
TO DO
    
Usage:
---
#### **Local**
- Start Appium Session
    - Install server dependencies:`$ npm install`
    - Start Appium:`$ appium` or `Start Server in Appium Desktop`
- Start Selenium Session
    - Install Selenium: `$ selenium-standalone install`
    - Start Selenium: `$ selenium-standalone start`
    
App Setup
----
- iOS & Android
    - for local runs create `apps` folder in project directory and move app to newly created folder
        - example `/your/path/to/PROJECT_Folder/apps/put app here`
        - note: if we get a downladable app link for the app this is no longer needed. 
        
Emulator/Simulator Setup
----
- iOS 
    - `Open Xcode` > `Preferences` > `Components` > `Install required simulators`
- Android
    - Create with Android Studio  
        - Open `Android Studio` and use previously created project 
            - `Tools` > `AVD Manager` > `Create Virtual Device` > Create emulator with needed settings

Appium Inspector Setup
----
1. With the appium server started from terminal or through appium desktop
    - `$ appium` in terminal, or select `Start Sever Version` in appium desktop
    - We will need to startup the emulator for android`$ emulator -avd DEVICE_ID_HERE`
2. Click `Search Icon` in top right
3. Make sure `Automatic Server` is selected to connect to the appium sessions
4. Under `Desired Capabilities` you will enter the following to get an inspector sessions started
    - NOTE: look at the appium desired capabilities resource linked above for more info

#### iOS capabilities example

Name           | Type   | Value
:---:          | :---:  | :---:
platformName   | text   | iOS
platformVersion| text   | 12.1
deviceName     | text   | iPhone X
app            | text   | path/to/app
automationName | text   | XCUITest
noReset        | boolean| true
    
#### Android capabilities example

Name          | Type   | Value
:---:         | :---:  | :---:
platformName  | text   | android
deviceName    | text   | emulator-5554
app           | text   | path/to/app
automationName| text   | uiautomator2
noReset       | boolean| true
    
#### Example of how the JSON will look:  

    {
      "platformName": "iOS",
      "platformVersion": "12.1",
      "deviceName": "iPhone X",
      "app": "/your/path/to/projectNameHERE/apps/appHere",
      "automationName": "XCUITest",
      "noReset": true
    }
   
Running tests
----  
**IntelliJ**
\
Create a run configuration. This will allow you to run Scenarios by right clicking them and selecting run in IntelliJ   
- Create new Cucumber Java run configuration: `Run` > `Edit Configurations`
    - Main class: `cucumber.api.cli.Main`
    - Glue: `core.utilities.setup cucumber.steps`
    - Feature or folder path: `/path/to/features` 
        - Example `/Users/your_username/project_name/src/test/resources/features`

We can also use program arguments to get screenshots or to only run specific tests  
- Example
    ```
     --plugin org.jetbrains.plugins.cucumber.java.run.CucumberJvm4SMFormatter --plugin html:target/cucumber-report/cucumber.html --monochrome --tags @TagsYouWantToRun
    ```
Note: *iOS sim must have `connect hardware keyboard` off.*

**Android CheatSheet**
----  
- To see [adb command options](https://developer.android.com/studio/command-line/adb): `$ adb help`
    - Shows List of Connected Devices:`$ adb devices` 
    - Commands For Specific Device: `$ adb -s DEVICE_ID_HERE`
- To see [emulator command options](https://developer.android.com/studio/run/emulator-commandline)`$ emulator -help`
    - List Emulators on Current Device: `$ emulator list-avds `
    - Start and run emulator: `$ emulator -avd DEVICE_ID_HERE` 
     
**iOS CheatSheet**
----  
- To see simulator command options: `$ xcrun simctl help`
    - boot sim: `xcrun simctl boot DEVICE_ID_HERE`
    - start sim `open /Applications/Xcode.app/Contents/Developer/Applications/Simulator.app/`
- To see device command options: `$ idevicediagnostics --help`    
    - Restart Device: `$ idevicediagnostics restart -u DEVICE_ID_HERE`
- You can use QuickTime Player to record the screen.
    - Open QuickTime 
        - Select File from the menu
        - Select New Screen recording
        - click on record button
            - It will provide you with an option to record the entire screen or a selective portion of your screen. You will have to make a selection of your simulator so that only the simulator portion will be recorded.
     
**Framework Workflow**
----  
- **Config:** (_`core/utilities/setup/Config.java`_)
    - This is where we _**create the desired capabilities**_ for our devices based of the current platform. 
        - `getDeviceCapabilities()` deserializes _`jsonData/devices.json`_ JSON data
        
        
- **Factory:** (_`core/utilities/setup/DriverFactory.java`_)
    - We use the set _** capabilities**_ to _**create the driver**_ here
        - `createDriver()` will create the driver for the current platform     
     
- **Hooks:** (_`core/utilities/setup/Hooks.java`_)
    - We use the _**created driver**_ from _**DriverFactory**_ to set the _**RemoteWebdriver**_ and perform actions based on test conditions. 
        - `beforeAll()` sets data, drivers, and variables for test run.
        - `beforeScenariol()` will set the driver from what was created in Factory. If that fails it will try again 5 times and until failing the run. 
        - `afterAll()` Setup will be set to false after all tests ran
        - `afterScenario()` The driver will be quit. On scenario failure a screenshot will be taken.
        
        
- **PageObjectBase:** (_`core/base/PageObjectBase.java`_)
    - This houses general use methods. The constructor sets the driver variable so this class can be used as a super. 
    - `getField(elementField)` Is how we use string parameters in gherkin steps to use elements on pages/modules
    
- **Pages Abstract Classes**
    - We use abstract classes to perform different logic on each platform when needed. 
    - There is an Abstract Class for each screen/page used. Methods used for both platform are stored in the abstract classes, then overridden on the platforms class.
    - The step classes choose which class to use based off the set platform
        - Example: The Abstract `ExamplePage` has abstract method`assertPagePresent()` which is overridden in `ExamplePageWeb` for that platforms needed functionality. 
