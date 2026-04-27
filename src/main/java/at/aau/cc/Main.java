package at.aau.cc;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class Main {


    public static void main(String[] args) {
        checkArgsLength(args);

        String link;
        try {
            link = formatLink(args[0]);
            checkLink(link);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        int depthLimit = Integer.parseInt(args[1]);
        checkDepthLimit(depthLimit);

        String[] domains = getDomains(args);
        checkDomains(domains);

        try {
            Document doc = Jsoup.connect(link).get();
            for (Element e : doc.select("h1")) {
                System.out.println(e.text());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("START SCANNING");

        WebCrawler crawler = new WebCrawler(depthLimit, domains);
        crawler.start(link);
    }


    private static void checkArgsLength(String[] args){
        if(args.length<3){
            throw new IllegalArgumentException("Wrong number of Arguments. Usage: java Main <URL> <Depth> <Domain1> <Domain2> ... <DomainN>");
        }
    }

    private static void checkLink(String link){
        if(!link.startsWith("http://") && !link.startsWith("https://")){
            throw new IllegalArgumentException("Invalid link, it must have https or http :// protocol" + link);
        }
        if(link.contains(" ") || !link.contains(".")){
            throw new IllegalArgumentException("Invalid link, it cannot contain spaces or dashes" + link);
        }
    }

    private static void checkDepthLimit(int depthLimit){
        if(depthLimit<1){
            throw new IllegalArgumentException("Depth limit must be greater than zero");
        }else if(depthLimit>10){
            throw new IllegalArgumentException("Depth limit must be less than 10");
        }
    }

    private static void checkDomains(String[] domains){
        for(String domain : domains){
            checkLink(domain);
        }
    }

    private static String formatLink(String link) throws IOException {
        if(!link.endsWith("/")){
            link += "/";
        }
        return formatProtocol(link);
    }


    private static String formatProtocol(String link) {
        if(!link.startsWith("https://") && !link.startsWith("http://")) {
            try {
                var Response = Jsoup.connect("https://" + link).followRedirects(true).execute();
                return Response.url().toString();
            } catch (final IOException e) {
                System.out.println("Invalid link, it must have https:// or http:// protocol" + e.getMessage());
            }
        }
        return link;
    }

    private static String[] formatDomains(String[] domains){
        for(int i = 0; i < domains.length; i++){
            try {
                domains[i] = formatLink(domains[i]);
            }catch (IOException e){
                System.out.println("Invalid Domain: "+domains[i]);
            }
        }
        return domains;
    }

    private static String[] getDomains(String[] args){
        String[] domains = new String[args.length-2];
        System.arraycopy(args, 2, domains, 0, args.length - 2);
        return formatDomains(domains);
    }
}
