# WebCrawler CleanCode
## AAU Klagenfurt - Clean Code 

You can input the links and domains (regardless of the option chosen) with or without the http:// or https://, however do so in a unified manner (please don't mix https:// and no https://).
<br><strong>Input link must be in the specified domains to be searched.</strong>
<br>Example: <code>github.com 2 github.com</code> or <code>https://github.com/ 2 https://github.com/</code> are both valid input options.
<br>

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
