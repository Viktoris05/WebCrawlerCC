package at.aau.cc;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Arrays;

public class Main {

//    String link;
//    String depth;
//    String domain;
//
//    public Main(String link, String depth, String domain) {
//        this.link = link;
//        this.depth = depth;
//        this.domain = domain;
//    }

    public static void main(String[] args) {
        checkArgsLength(args);

        String link = args[0];
        checkLink(link);


        int depthLimit = Integer.parseInt(args[1]);
        checkDepthLimit(depthLimit);

        String[] domains = getDomains(args);
        checkDomains(domains);

        System.out.println("Link: " + link + " Depth: " + depthLimit);
        System.out.println("Domains: " + Arrays.toString(domains));


        try {
            Document doc = Jsoup.connect(link).get();
            for (Element e : doc.select("h1")) {
                System.out.println(e.text());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //TODO: CALL THE CRAWLER FROM HERE, CREATE NEW CLASS(ES) FOR THE CRAWLER
    }


    private static void checkArgsLength(String[] args){
        if(args.length<3){
            System.out.println("Usage: java Main <URL> <Depth> <Domain1> <Domain2> ... <DomainN>");
            System.exit(1);
        }
    }

    private static void checkLink(String link){
        if(!link.startsWith("http://") && !link.startsWith("https://")){
            System.out.println("Invalid URL, JSoup needs HTTP or HTTPS protocol: "+link);
            System.exit(1);
        }
        if(link.contains(" ") || !link.contains(".")){
            System.out.println("Invalid URL: "+link);
            System.exit(1);
        }
    }

    private static void checkDepthLimit(int depthLimit){
        if(depthLimit<1){
            System.out.println("Invalid Depth Limit, limit is too low (minimum: 1): "+depthLimit);
            System.exit(1);
        }else if(depthLimit>10){
            System.out.println("Invalid Depth Limit, limit is too big (maximum: 10): "+depthLimit);
            System.exit(1);
        }
    }

    private static String[] getDomains(String[] args){
        String[] domains = new String[args.length-2];
        System.arraycopy(args, 2, domains, 0, args.length - 2);
        return domains;
    }

    private static void checkDomains(String[] domains){
        for(String domain : domains){
            checkLink(domain);
        }
    }
}
