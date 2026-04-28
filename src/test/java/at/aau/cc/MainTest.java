package at.aau.cc;

import org.junit.jupiter.api.*;

@Disabled("Not fit for automated test as it crawls")
class MainTest {
    String[] inputArgs;

    @BeforeEach
    void setUp() {
        inputArgs = new String[3];
        inputArgs[0] = "benji.link"; //website from a friend
        inputArgs[1] = "3";
        inputArgs[2] = "benji.link";

    }

    void changeLink(String link){
        inputArgs[0] = link;
    }

    void changeDepth(int depth){
        inputArgs[1] = "" + depth;
    }

    void changeDomains(String domain1){
        inputArgs[2] = domain1;
    }

    @Disabled
    void mainTest(){
        Main.main(inputArgs);
    }

    @Test
    void mainTooFewInputs() {
        String[] input = new String[]{"benji.link", "2"};

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> Main.main(input));

        Assertions.assertEquals("Invalid Arguments: Wrong number of Arguments. Usage: java Main <URL> <Depth> <Domain1> <Domain2> ... <DomainN>", exception.getMessage());
    }

    @Test
    void mainWrongLink() {
        changeLink("benji link");
        String link = inputArgs[0];

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> Main.main(inputArgs));

        Assertions.assertEquals("Invalid Arguments: Invalid initial link: https://" + link + "/", exception.getMessage());
    }

    @Test
    void mainLowDepth() {
        changeDepth(0);

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> Main.main(inputArgs));

        Assertions.assertEquals("Invalid Arguments: Depth limit must be between 1 and 10", exception.getMessage());
    }

    @Test
    void mainHighDepth() {
        changeDepth(11);

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> Main.main(inputArgs));

        Assertions.assertEquals("Invalid Arguments: Depth limit must be between 1 and 10", exception.getMessage());
    }

    @Test
    void mainWrongDomain() {
        changeDomains("benji link");
        String wrongDomain = inputArgs[2];

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> Main.main(inputArgs));

        Assertions.assertEquals("Invalid Arguments: Invalid domains: [https://" + wrongDomain + "]", exception.getMessage());
    }
}