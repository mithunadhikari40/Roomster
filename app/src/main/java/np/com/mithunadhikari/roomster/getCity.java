package np.com.mithunadhikari.roomster;


import java.util.ArrayList;
import java.util.List;

public class getCity {
    List<String> cityList = new ArrayList<>();

    public List<String> returnCity(String getDistrict) {

        if (getDistrict.equalsIgnoreCase("Kathmandu")) {

            cityList.add("Buspark");
            cityList.add("Kalanki");
            cityList.add("Baneshor");
            cityList.add("Kalanki");
            cityList.add("Koteshor");
            cityList.add("Chandragiri");
        } else if (getDistrict.equalsIgnoreCase("Bhaktapur")) {
            cityList.add("Thimi");
            cityList.add("Chagu");
            cityList.add("Pepsikola");
            cityList.add("Lokanthali");
        } else if (getDistrict.equalsIgnoreCase("Lalitpur")) {
            cityList.add("Imadole");
            cityList.add("Kupandol");
            cityList.add("Pharphing");
            cityList.add("Sukauna");
            cityList.add("Laganakhel");
        }

        return cityList;
    }

}
