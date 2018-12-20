package uk.ac.lincoln.students.a13488071.whatsinmyfridge;

//import classes
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import java.util.ArrayList;
import android.widget.*;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;

public class FridgeIngredients extends AppCompatActivity {

    //creates variable for the code
    ArrayList<String> items = new ArrayList<>(); //array list of the recipes displayed
    ArrayList<String> recipe = new ArrayList<>(); //array list for the links to the recipe
    String apiUrl = "http://www.recipepuppy.com/api/?i="; //API url

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge_ingredients);
    }

    //Parsing for JSON from the API
    //Adapted from week 5 workshop code... supplied by Derek Foster
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute()//done before the background
        {
            items.clear(); //makes sure that the array list is empty before searching
        }

        @Override
        protected String doInBackground(String... arg0) {
            EditText ingredient = findViewById(R.id.ingredientText); //finds the search bar
            try //JSON is parsed in through the HttpConnect class
            {
                HttpConnect jParser = new HttpConnect();
                String newUrl = apiUrl + ingredient.getText().toString(); //assigns a new api url
                String json = jParser.getJSONFromUrl(newUrl); //gets JSON from the url
                JSONObject jsonObject = new JSONObject(json); //gets the JSON Object from the
                JSONArray arr = jsonObject.getJSONArray("results"); //gets JSON Array from the JSON Object

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject json_message = arr.getJSONObject(i); //assigns the JSON object

                    if (json_message != null) {
                        items.add(json_message.getString("title")); //adds the recipe to the list
                    }
                }

                if (arr.length() == 0)
                {
                    items.isEmpty(); //assigns the list to be empty if no JSON Object is found (error handling)
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        ListView list = (ListView) findViewById(R.id.showRecipe); //finds the ListView
        ArrayAdapter<String> recipeArrayAdapter = new ArrayAdapter<>(FridgeIngredients.this, android.R.layout.simple_list_item_multiple_choice, items); //Creates an ArrayAdapter for the ListView

        @Override
        protected void onPostExecute(String doInBackground) //Done after the Background
        {
            if (items.isEmpty()) //if the list is empty then error message (error handling)
            {
                AlertDialog.Builder a = new AlertDialog.Builder(FridgeIngredients.this);
                a.setMessage("No Recipes Found!").setCancelable(true);
                AlertDialog ab = a.create();
                ab.show();
                list.setAdapter(recipeArrayAdapter); //show results
            }
            else {
                list.setAdapter(recipeArrayAdapter); //show search results
                list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); //allows the user to select recipes
            }
        }
    }

    //Parsing for the Recipe URL
    //Adapted from week 5 workshop code... supplied by Derek Foster
    public class AsyncTaskParseJsonRecipe extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute()
        {
            recipe.clear(); //clears the list before intialisation
        }

        @Override // same process as parsing the JSON from API
        protected String doInBackground(String... arg0) {
            EditText ingredient = findViewById(R.id.ingredientText);
            try
            {
                HttpConnect jParser = new HttpConnect();
                String newUrl = apiUrl + ingredient.getText().toString();
                String json = jParser.getJSONFromUrl(newUrl);
                JSONObject jsonArray = new JSONObject(json);
                JSONArray arr = jsonArray.getJSONArray("results");

                ListView lstView = findViewById(R.id.showRecipe);

                for (int i =0; i< lstView.getCount(); i++) //total results from the search
                {
                    if (lstView.isItemChecked(i)) //if item is selected
                    {
                        JSONObject json_message = arr.getJSONObject(i); //assign the array to the object

                        if (json_message != null) {
                            recipe.add(json_message.getString("href")); //add the object from that array
                        }
                    }
                }

                if (arr.length() == 0)
                {
                    recipe.isEmpty(); //assign the list as empty (error Handling)
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String doInBackground)
        {
            //ERROR HANDLING
            if (recipe.isEmpty())
            {
                AlertDialog.Builder a = new AlertDialog.Builder(FridgeIngredients.this);
                a.setMessage("No Recipes Found!").setCancelable(true);
                AlertDialog ab = a.create();
                ab.show();
            }
            //open a browser for the selected recipe URL
            else {
                recipe.subList(1,recipe.size()).clear(); //finds the 1st value in the array
                String output = new String(recipe.toString()); //outputs the array into a string array
                output = output.replace("[",""); //removes the square brackets
                output = output.replace("]","");
                //Adapted from week 2 workshop code... supplied by Derek Foster
                Uri webpage = Uri.parse(output); //parses the url
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage); //opens a new browser
                startActivity(intent);
            }
        }
    }

    //calls the Json parser
    public void searchRecipe(View view)
    {
        new AsyncTaskParseJson().execute();
    }

    //saves the selected recipes
    public void saveRecipe(View view)
    {
        ListView lstView = findViewById(R.id.showRecipe); //finds the listview

        String itemsSelected = ""; //creates an empty string

        for (int i =0; i< lstView.getCount(); i++) //total search results
        {
            if (lstView.isItemChecked(i)) //checks whether item is selected
            {
                itemsSelected += lstView.getItemAtPosition(i) + "\n"; //adds the item if checked to the string
            }
        }
        //saves the List to a local storage
        try
        {
            //adapted from workshop code week 7... Supplied by Derek Foster
            //creates a new shared preference file by name, if it already exists it will use existing file
            SharedPreferences recipeData = getSharedPreferences("recipeData", 0);
            SharedPreferences.Editor editor = recipeData.edit();
            String recipeList = recipeData.getString("recipe", "");
            recipeList += itemsSelected;
            editor.putString("recipe", recipeList);
            editor.apply();

            //shows toast message for successful save
            Context context = getApplicationContext();
            CharSequence text = "Recipe Saved! \n" + itemsSelected;
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        catch(Exception ex)
        {   //error handling
            Toast.makeText(FridgeIngredients.this, "Saved Failed!", Toast.LENGTH_LONG).show();
        }
    }
    //calls the Recipe Json Parser
    public void showRecipe(View view){
        new AsyncTaskParseJsonRecipe().execute();
    }
}