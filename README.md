# WebCrawler CleanCode
## AAU Klagenfurt - Clean Code 

How to build/run/test:
### Option A) (IntelliJ)
Building and testing remains as usual.<br>
To run, add an Application configuration that goes as follows:<br>
<code>URL Depth Domain1 Domain2 .. DomainN</code>


### Option B) (Terminal)
<strong>Requirements: JAVA_HOME jdk-21</strong><br>
First, navigate in the terminal to the project location<br>
to build:<br>
<code>.\mvnw.cmd package</code><br>
to run:<br>
<code>java -jar target\webcrawlercc.jar URL Depth Domain1 Domain2 ... DomainN</code><br>
to test:<br>
<code>.\mvnw.cmd test</code>
