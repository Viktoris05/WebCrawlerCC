package at.aau.cc;

public class WebsiteData {
    private String link;
    private int currentDepth;
    private String[][] headers;
    public WebsiteData(String link,  int currentDepth,  String[][] headers){
        this.link = link;
        this.currentDepth = currentDepth;
        this.headers = headers;
    }

    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }

    public int getCurrentDepth() {
        return currentDepth;
    }
    public void setCurrentDepth(int currentDepth) {
        this.currentDepth = currentDepth;
    }


    public String[][] getHeaders() {
        return headers;
    }
    public String getHeader(int index){
        return headers[index][0];
    }
    public int getHeaderTag(int header){
        return Integer.parseInt(headers[header][1]);
    }

    public void setHeaders(String[][] headers) {
        this.headers = headers;
    }
    public void setHeader(int index, String header){
        this.headers[index][0] = header;
    }
    public void setHeaderTag(int header, int headerTag){
        this.headers[header][1] = Integer.toString(headerTag);
    }
}
