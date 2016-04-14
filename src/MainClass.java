import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.util.StringTokenizer;

public class MainClass {

    private String APIKEY = "AIzaSyAOvc6vdULyXtI0j0xrbBcNqO3_aOSSy-Q";

    private String dire[] = {
            "Casa",
            "Acuario de Veracruz",
            "Acuario Mazatlán",
            "Acuario Inbursa",
            "Acuario de Guadalajara",
            "Interactive Aquarium Cancún",
            "Acuario del Zoológico de Morelia",
            "Acuarios en la Riviera Maya."
    };

    private List<Place> createList() {
        Place place;
        List<Place> localList = new ArrayList<>();

        double coor[][] = {
                {19.269229, -99.709236},
                {19.187063, -96.122183},
                {23.228647, -106.427885},
                {19.439975, -99.204981},
                {20.651417, -103.310689},
                {21.109842, -86.764680},
                {19.682859, -101.195127},
                {20.447542, -87.302804}
        };

        for (int i=0;i<dire.length;i++){
            place = new Place(i,coor[i][0],coor[i][1],dire[i], "Estado de México", "México");
            localList.add(place);
        }
        return localList;
    }

    private void printList(List<Place> places) {
        for (Place place : places) {
            System.out.println("Coor1:" + place.getLatitude());
            System.out.println("Coor2:" + place.getLongitude());
            System.out.println("Distance = " + place.getDirection());
        }
    }

    private List<Connection> getDistance(List<Place> list) {
        List<Connection> localList = new ArrayList<>();
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;

        try {
            for (int i=0;i<list.size()-1;i++){
                Place origin = list.get(i);
                String urlString = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="+origin.getLatitude()+","+origin.getLongitude();
                urlString = urlString + "&destinations=";
                for(int j=i+1;j<list.size();j++){
                    Place destination = list.get(j);
                    urlString = urlString + destination.getLatitude()+","+destination.getLongitude();
                    if(j!=list.size()-1)
                        urlString = urlString+"|";
                }
                urlString = urlString + "&key="+APIKEY;

                url = new URL(urlString);
                is = url.openStream();  // throws an IOException
                br = new BufferedReader(new InputStreamReader(is));

                String data= "";
                while ((line = br.readLine()) != null) {
                    data = data + line;
                }
                JSONObject request = new JSONObject(data);
                JSONArray rows = request.getJSONArray("rows");
                JSONObject elements = rows.getJSONObject(0);
                JSONArray elementsArray = elements.getJSONArray("elements");

                System.out.println(request);

                for(int j = 0; j< elementsArray.length(); j++ ) {
                    JSONObject json_data = elementsArray.getJSONObject(j);
                    JSONObject distance = json_data.getJSONObject("distance");
                    String distanceText = distance.getString("text");
                    Connection connection = new Connection(i,i+j+1,distanceText);
                    System.out.println(distanceText);
                    localList.add(connection);
                }
            }

        } catch (IOException | JSONException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                // nothing to see here
            }
        }

        return localList;
    }

    private void printDistance(List<Connection> connections) {

        int k = 0, i;
        for (i = 0;i < dire.length-1;i++) {
            System.out.print(dire[i]+" = " + i + ":");
            for (int j = i + 1; j < dire.length; j++) {
                Connection place = connections.get(k);
                System.out.print(" "+j+"="+place.distance);
                k++;
            }
            System.out.println("");
        }
        System.out.print(dire[i]+" = " + i + ":");

        /*for (Connection con : connections) {
            System.out.print("From:" + dire[con.place1_id]);
            System.out.println("   To:" + dire[con.place2_id]);
            System.out.print("From:" + con.place1_id);
            System.out.println("   To:" + con.place2_id);
            System.out.println("Distance:" + con.distance);
            System.out.println("");
        }*/
    }

    public static void main(String[] args) {

        MainClass mainC = new MainClass();
        List<Place> list = mainC.createList();

        mainC.printList(list);

        List<Connection> listConnection = mainC.getDistance(list);
        mainC.printDistance(listConnection);
    }

    private class Place {
        int id;
        double latitude, longitude;
        String direction, estate, country;
        Place(int id ,double latitude, double longitude, String direction, String estate, String country) {
            this.id = id;
            this.latitude = latitude;
            this.longitude = longitude;
            this.direction = direction;
            this.estate = estate;
            this.country = country;
        }

        String getDirection(){
            return direction;
        }

        double getLongitude() {
            return longitude;
        }

        double getLatitude() {
            return latitude;
        }
    }

    private class Connection{
        int place1_id,place2_id;
        String distance;
        Connection(int place1, int place2 , String distance){
            this.place1_id = place1;
            this.place2_id = place2;
            StringTokenizer st = new StringTokenizer(distance," ");
            //this.distance = Float.parseFloat((String) st.nextElement());
            this.distance = distance;
        }
    }
}
