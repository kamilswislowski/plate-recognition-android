package pl.swislowski.kamil.project.platerecognition.android;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class Main {
    private static final String TAG = "MainActivity";

    public static void main(String[] args) throws IOException {


        Document doc = Jsoup.connect("http://rejestracje.website.media.pl/?word=wpr313131").get();
        System.out.println("" + doc.title());

        Elements result = doc.getElementsByClass("result");
        System.out.println(result);

        for (Element element : result) {
            String text = element.text();
            System.out.println(text);
        }

        List<String> strings = result.eachText();
        System.out.println(strings.size());
        for (String string : strings) {
            System.out.println(string);
        }

//        strings.forEach(s -> System.out.println(s));

//        if (strings != null && strings.size() > 0) {
//            System.out.println("Tablica jest z województwa : " + strings.get(1));
//            System.out.println("Tablica jest z miasta : " + strings.get(2));
//        }
    }
}
