package uk.ac.lincoln.students.a13488071.whatsinmyfridge;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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


    public void loadRecipes(View view){

        SharedPreferences recipeData = getSharedPreferences("recipeData",0);
        String recipeList = recipeData.getString("recipe", "");
        List<String> myList = new ArrayList<String>(Arrays.asList(recipeList.split("\n")));
        if (recipeList.length() == 0)
        {
            myList.clear();
        }

        ListView lists = (ListView)findViewById(R.id.showSaved);
        ArrayAdapter<String> saveRecipe = new ArrayAdapter<String>(SavedRecipes.this, android.R.layout.simple_list_item_multiple_choice,myList);

        if (recipeList.length() == 0)
        {
            AlertDialog.Builder a = new AlertDialog.Builder(SavedRecipes.this);
            a.setMessage("No Recipes Saved!").setCancelable(true);
            AlertDialog ab = a.create();
            ab.show();
            lists.setAdapter(saveRecipe);
        }
        else {
            lists.setAdapter(saveRecipe);
            lists.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        }
    }

    public void removeItem(View view){
        SharedPreferences recipeData = getSharedPreferences("recipeData",0);
        String recipeList = recipeData.getString("recipe", "");
        SharedPreferences.Editor editor = recipeData.edit();

        ArrayList<String> newList = new ArrayList<String>(Arrays.asList(recipeList.split("\n")));

        ListView lists = (ListView)findViewById(R.id.showSaved);

        for (int i =0; i< lists.getCount(); i++)
        {
            if (lists.isItemChecked(i))
            {
                newList.remove(lists.getItemAtPosition(i));
            }
        }
        String output = new String(newList.toString());
        output = output.replace(", ", "\n");
        output = output.replace("[","");
        output = output.replace("]","");
        editor.putString("recipe", output);
        editor.commit();

        ArrayAdapter<String> remRec = new ArrayAdapter<String>(SavedRecipes.this, android.R.layout.simple_list_item_multiple_choice,newList);
        lists.setAdapter(remRec);
        lists.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    public void clearList(View view){

        SharedPreferences recipeData = getSharedPreferences("recipeData", 0);
        SharedPreferences.Editor editor = recipeData.edit();
        editor.putString("recipe", "");
        editor.apply();

        ListView lists = (ListView)findViewById(R.id.showSaved);
        ArrayAdapter<String> clearRecipe = new ArrayAdapter<String>(SavedRecipes.this, android.R.layout.simple_expandable_list_item_1);
        lists.setAdapter(clearRecipe);
    }
}