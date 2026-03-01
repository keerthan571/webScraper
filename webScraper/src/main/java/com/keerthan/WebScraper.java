package com.keerthan;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class WebScraper {

    public static void main(String[] args) {
        String baseUrl = "http://books.toscrape.com/catalogue/page-";
        int totalPages = 3; // Change this if you want more pages
        // Create timestamped file
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = "products_" + timestamp + ".csv";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("Name,Price,Rating\n");
            for (int page = 1; page <= totalPages; page++) {
                String url = baseUrl + page + ".html";
                System.out.println("\nScraping page: " + page);
                Document doc = Jsoup.connect(url).get();
                Elements products = doc.select(".product_pod");
                for (Element product : products) {
                    String name = product.select("h3 a").attr("title");
                    String price = product.select(".price_color").text();
                    String ratingClass = product.select(".star-rating").attr("class");
                    String rating = ratingClass.replace("star-rating ", "");
                    // Print in console
                    System.out.println(name + " | " + price + " | " + rating);
                    // Write to CSV
                    writer.write(name + "," + price + "," + rating + "\n");
                }
            }

            System.out.println("\nData successfully scraped and saved to " + fileName);

        } catch (IOException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }
}