package uk.ac.lincoln.students.a13488071.whatsinmyfridge;

//imports classes
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SavedRecipes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_recipes);
    }

    //loads in saved recipes
    public void loadRecipes(View view){
        //gets the saved values of the recipes from the shared preference file 'recipeData' file
        //adapted from workshop tasks from week 7... Supplied by Derek Foster
        SharedPreferences recipeData = getSharedPreferences("recipeData",0);
        String recipeList = recipeData.getString("recipe", "");
        //creates a list
        List<String> myList = new ArrayList<String>(Arrays.asList(recipeList.split("\n")));
        //error exception
        if (recipeList.length() == 0)
        {
            myList.clear(); //clears list
        }

        ListView lists = findViewById(R.id.showSaved); //finds ListView
        //creates an array adapter
        ArrayAdapter<String> saveRecipe = new ArrayAdapter<String>(SavedRecipes.this, android.R.layout.simple_list_item_multiple_choice,myList);

        //error handling
        if (recipeList.length() == 0)
        {
            AlertDialog.Builder a = new AlertDialog.Builder(SavedRecipes.this);
            a.setMessage("No Recipes Saved!").setCancelable(true);
            AlertDialog ab = a.create();
            ab.show();
            lists.setAdapter(saveRecipe);
        }
        //if recipes found display
        else {
            lists.setAdapter(saveRecipe);
            lists.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        }
    }

    //removing recipes from the saved data
    public void removeItem(View view){
        //get the saved values of username and email from the Shared Preference file 'userData' file
        SharedPreferences recipeData = getSharedPreferences("recipeData",0);
        String recipeList = recipeData.getString("recipe", "");
        SharedPreferences.Editor editor = recipeData.edit();
        //Create new Array List
        ArrayList<String> newList = new ArrayList<String>(Arrays.asList(recipeList.split("\n")));
        //find listvew
        ListView lists = findViewById(R.id.showSaved);

        for (int i =0; i< lists.getCount(); i++)
        {
            if (lists.isItemChecked(i)) //checks if the items are checked
            {
                newList.remove(lists.getItemAtPosition(i)); //remove recipe that was checked at the position
            }
        }
        //outputs the string array as string
        String output = new String(newList.toString());
        output = output.replace(", ", "\n");
        output = output.replace("[","");
        output = output.replace("]","");
        editor.putString("recipe", output);
        editor.commit();

        //displays the removed items
        ArrayAdapter<String> remRec = new ArrayAdapter<String>(SavedRecipes.this, android.R.layout.simple_list_item_multiple_choice,newList);
        lists.setAdapter(remRec);
        lists.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    public void clearList(View view){
        //get the saved values of username and email from the Shared Preference file 'userData' file
        SharedPreferences recipeData = getSharedPreferences("recipeData", 0);
        SharedPreferences.Editor editor = recipeData.edit();
        editor.putString("recipe", "");
        editor.apply();
        //display cleared list
        ListView lists = (ListView)findViewById(R.id.showSaved);
        ArrayAdapter<String> clearRecipe = new ArrayAdapter<String>(SavedRecipes.this, android.R.layout.simple_expandable_list_item_1);
        lists.setAdapter(clearRecipe);
    }
}