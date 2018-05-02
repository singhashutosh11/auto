Install testng plugin and dali/jaxb support plugin from eclipse, via install new software
Project setup actions-
1) for first time project setup do mvn clean install
2) right click src/test/generated folder, add it to build path by marking it to use as a source folder

Note- if made any change to the src/test/resourcs/xsd then do  mvn generate-sources on project

TO run in head less mode use the Property 
-DWEB_DRIVER_MODE=HEADLESS -DWEB_BROWSER_HOME="C:\Program Files (x86)\Google\Chrome\Application\chrome.exe" -Dwebdriver.chrome.driver="lib/ChromeDriver/chromedriver.exe"