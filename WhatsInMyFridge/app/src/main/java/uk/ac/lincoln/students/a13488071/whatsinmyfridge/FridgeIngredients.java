package uk.ac.lincoln.students.a13488071.whatsinmyfridge;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import java.util.ArrayList;

import android.util.Log;
import android.widget.*;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;

public class FridgeIngredients extends AppCompatActivity {

    ArrayList<String> items = new ArrayList<>();
    String apiUrl = "http://www.recipepuppy.com/api/?i=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge_ingredients);
    }

    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute()
        {
            items.clear();
        }

        @Override
        protected String doInBackground(String... arg0) {
            EditText ingredient = findViewById(R.id.ingredientText);
            try
            {
                HttpConnect jParser = new HttpConnect();
                String newUrl = apiUrl + ingredient.getText().toString();
                String json = jParser.getJSONFromUrl(newUrl);
                JSONObject jsonArray = new JSONObject(json);
                JSONArray arr = jsonArray.getJSONArray("results");

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject json_message = arr.getJSONObject(i);

                    if (json_message != null) {
                        items.add(json_message.getString("title"));
                    }
                }
                if (arr.length() == 0)
                {
                    items.isEmpty();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        ListView list = (ListView) findViewById(R.id.showRecipe);
        ArrayAdapter<String> recipeArrayAdapter = new ArrayAdapter<>(FridgeIngredients.this, android.R.layout.simple_list_item_multiple_choice, items);

        @Override
        protected void onPostExecute(String doInBackground)
        {
            if (items.isEmpty())
            {
                AlertDialog.Builder a = new AlertDialog.Builder(FridgeIngredients.this);
                a.setMessage("No Recipes Found!").setCancelable(true);
                AlertDialog ab = a.create();
                ab.show();
                list.setAdapter(recipeArrayAdapter);
            }
            else {
                list.setAdapter(recipeArrayAdapter);
                list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            }
        }
    }

    public void searchRecipe(View view)
    {
        new AsyncTaskParseJson().execute();
    }

    public void saveRecipe(View view)
    {
        ListView lstView = findViewById(R.id.showRecipe);

        String itemsSelected = "";

        for (int i =0; i< lstView.getCount(); i++)
        {
            if (lstView.isItemChecked(i))
            {
                itemsSelected += lstView.getItemAtPosition(i) + "\n";
            }
        }
        try
        {
            SharedPreferences recipeData = getSharedPreferences("recipeData", 0);
            SharedPreferences.Editor editor = recipeData.edit();
            String recipeList = recipeData.getString("recipe", "");
            recipeList += itemsSelected;
            editor.putString("recipe", recipeList);
            editor.apply();

            Context context = getApplicationContext();
            CharSequence text = "Recipe Saved! \n" + itemsSelected;
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        catch(Exception ex)
        {
            Toast.makeText(FridgeIngredients.this, "Saved Failed!", Toast.LENGTH_LONG).show();
        }
    }
}