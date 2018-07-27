package co.etornam.sandwichclub.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.etornam.sandwichclub.model.Sandwich;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {

        // Declaring a sandwich object as null.
        Sandwich sandwich = null;

        /**
         * According to JSON structure we've 2 JsonObjects(including root object), 4 Key-Value Pairs, 2 JsonArrays.
         */

        // All KEY names.
        final String KEY_NAME = "name",
                KEY_MAIN_NAME = "mainName",
                KEY_ALSO_KNOWN_AS = "alsoKnownAs",
                KEY_PLACE_OF_ORIGIN = "placeOfOrigin",
                KEY_DESCRIPTION = "description",
                KEY_IMAGE_URL = "image",
                KEY_INGREDIENTS = "ingredients";


        try {

            //mainJsonObject is the root of the JSON.
            JSONObject mainJsonObject, nameObject;
            String mainName, placeOfOrigin, description, imageURL;
            List<String> alsoKnownAsArray, ingredientsArray;

            mainJsonObject = new JSONObject(json);

            nameObject = mainJsonObject.getJSONObject(KEY_NAME);

            mainName = nameObject.optString(KEY_MAIN_NAME);

            alsoKnownAsArray = jsonArrayValuesToList(nameObject.getJSONArray(KEY_ALSO_KNOWN_AS));

            placeOfOrigin = mainJsonObject.optString(KEY_PLACE_OF_ORIGIN);
            description = mainJsonObject.optString(KEY_DESCRIPTION);
            imageURL = mainJsonObject.optString(KEY_IMAGE_URL);

            ingredientsArray = jsonArrayValuesToList(mainJsonObject.getJSONArray(KEY_INGREDIENTS));

            sandwich = new Sandwich(mainName, alsoKnownAsArray, placeOfOrigin, description, imageURL, ingredientsArray);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sandwich;
    }

    /**
     * This method adds the JsonArray Values in to a List<String> and returns that List.
     *
     * @param jsonArray
     * @return myList
     */
    private static List<String> jsonArrayValuesToList(JSONArray jsonArray) {
        List<String> myList = new ArrayList<String>();

        // Adding each JSONArrayValues into myList.
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                myList.add(jsonArray.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return myList;
    }
}
