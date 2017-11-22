package com.mike.artisttracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import static com.mike.artisttracker.saved_artist.savedArtists;
import de.umass.lastfm.Album;
import de.umass.lastfm.Caller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class album_activity extends AppCompatActivity {

    private String api_key = "44ce572665909f50a88232d35e667812";
    ArrayList<String> saved_album_names = new ArrayList<>();
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album);

        ListView album_list_view =(ListView)findViewById(R.id.saved_album_list_view);
        for(saved_artist artist: savedArtists) {
            Caller.getInstance().setCache(null);
            Caller.getInstance().setUserAgent("tst");
            String artist_name = artist.getArtistName();
            String album_name = " ";
            if(artist.getArtistTopAlbums() == null) {
                artist.updateArtistTopAlbums(artist_name);
            }
            try {

                Document album_doc = Jsoup.connect("http://google.com/search?q=" + "new album" + " " + artist_name).get();
                Elements album_ele = album_doc.getElementsByClass("_XWk");
                if(album_ele != null) {
                    String[] lines;
                    String regex = "\\n";
                    lines = album_ele.toString().split(regex);
                    if(lines.length >1) {
                        album_name = lines[1];
                        //System.out.println(album_name); *****************************8
                    }


                }




                Document release_doc = Jsoup.connect("http://google.com/search?q=" + artist_name + " " + album_name + " date of release").get();
                //Element hreEles = doc.select("div#_XWk").last();
                Elements hreEles = release_doc.getElementsByClass("_XWk");
                if (hreEles != null) {
                    //hreEles.get(0).toString();
                    String[] lines;
                    String regex = "\\n";
                    lines = hreEles.toString().split(regex);
                    //System.out.println(lines[1].toString() + "hey");

                    if (lines.length > 2) {
                        //System.out.println(album_name + " " + lines[1]);
                        String[] splitDate = lines[1].split("\\s");
                        if (splitDate.length > 2) {
                            String month = splitDate[1];
                            String day = splitDate[2].substring(0, splitDate[2].length() - 1);
                            String year = splitDate[3];
                            //System.out.println(Integer.parseInt(year));
                            Date date = null;
                            try {
                                date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(month);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            int int_month = cal.get(Calendar.MONTH);
                            Calendar albumDate = Calendar.getInstance();
                            albumDate.set(Calendar.YEAR, Integer.parseInt(year));
                            albumDate.set(Calendar.MONTH, int_month);
                            albumDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));

                            //Date albumDate = new Date(Integer.parseInt(year), int_month, Integer.parseInt(day));
                            //Date today = new Date();
                            Calendar today = Calendar.getInstance();
                            //System.out.println(album_name);
//                            System.out.println("l"+year+"l");
//                            System.out.println(albumDate);
//                            System.out.println(today);
                            if (albumDate.after(today)) {
                                saved_album_names.add(album_name + " -" + lines[1]);
                                //artist.setUpcomingAlbum(album_name + " -" + lines[1]);
                                            //System.out.println("hi");

                                //WEIRD ERROR WERE THE YEAR OF ALBUM DATE IS 3917 not 20..
                                //MAYBE bECAUSE DATE IS DEPRICATED - HOW DO I FIX??
                                //https://stackoverflow.com/questions/5677470/java-why-is-the-date-constructor-deprecated-and-what-do-i-use-instead

                            }

                        } else if(splitDate.length == 2){ //case where just year is shown..we will accept for now
                            String year = splitDate[1];
;
                            Calendar now = Calendar.getInstance();   // Gets the current date and time
                            int now_year = now.get(Calendar.YEAR); ;

                            if(Integer.parseInt(year) >= now_year){
                                saved_album_names.add(album_name + " - " + year);
                            }


                        }

                                    //System.out.println(hreEles.toString());
                                    ///System.out.println();
                    } else { // not full date given - still a new album according to google
                        int year = Calendar.getInstance().get(Calendar.YEAR);

                        if(lines.length > 1) { //a result is shown
//                            String[] splitDate = lines[1].split("\\s");
//                            if(splitDate.length > 1) {
//                                saved_album_names.add(album_name + " - " + year);
//                            }
                            saved_album_names.add(album_name + " - " + year);
                        }
                    }

                }
            } catch(IOException e){

            }


        }


        adapter = new ArrayAdapter<String>(album_activity.this,R.layout.artist_list_detail, saved_album_names);
        album_list_view.setAdapter(adapter);

    }


    public void saveDataToText(){
        try {
            FileOutputStream os = openFileOutput("SavedArtists.txt", MODE_PRIVATE);
            ObjectOutputStream output = new ObjectOutputStream(os);
            output.writeObject(saved_artist.savedArtists);
            output.close();
        }
        catch (java.io.IOException e) {
            //do something if an IOException occurs.
            System.out.println("ERROR"); //temporary
        }
    }

    //grabs persisting data and updates the savedArtist Data
    public void grabDataFromFile(){
        try{

            String file_name = "SavedArtists.txt";
            FileInputStream inputStream = openFileInput("SavedArtists.txt");
            ObjectInputStream objStream = new ObjectInputStream(inputStream);
            saved_artist.savedArtists = (ArrayList<saved_artist>) objStream.readObject();

            inputStream.close();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
