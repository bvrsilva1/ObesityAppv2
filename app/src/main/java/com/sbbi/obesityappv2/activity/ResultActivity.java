package com.sbbi.obesityappv2.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.sbbi.obesityappv2.R;
import com.sbbi.obesityappv2.helper.GetUserIdHelper;
import com.sbbi.obesityappv2.interf.ClassificationInterf;
import com.sbbi.obesityappv2.interf.FoodInterf;
import com.sbbi.obesityappv2.interf.RedirectListener;
import com.sbbi.obesityappv2.interf.SavedMealListener;
import com.sbbi.obesityappv2.model.Food;
import com.sbbi.obesityappv2.model.FoodsWeightEstimation;
import com.sbbi.obesityappv2.model.Prediction;
import com.sbbi.obesityappv2.model.ResponseFood;
import com.sbbi.obesityappv2.model.SendMeal;
import com.sbbi.obesityappv2.model.User;
import com.sbbi.obesityappv2.recycleradapter.FoodPredictionRecyclerAdapter;
import com.sbbi.obesityappv2.request.HttpSaveMeal;
import com.sbbi.obesityappv2.sqlite.ObesityDbDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Classification results
 *
 *
 */
public class ResultActivity extends AppCompatActivity implements SavedMealListener {

    private RecyclerView recyclerView;
    private FoodPredictionRecyclerAdapter foodAdapter;
    private final int CODE = 1;
    private final int TOTAL_FOOD = 13;
    private List<Food> listFood;
    private List<Number> listWeight;
    private List<String> listpredictedFood1;
    private List<String> listpredictedFood2;
    private List<String> listpredictedFood3;
    private List<List<String>> listAllpredictedFood;
    private Prediction predictions;
    private int typeMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);//new layout with CardView

        Bundle extras = getIntent().getExtras();

        //get predictions from previous activity
        predictions = (Prediction) extras.get("result");

        //get meal type
        this.typeMeal = (int) extras.getInt("typeMeal");
        predictions.setTypeMeal(this.typeMeal);

        recyclerView = (RecyclerView) findViewById(R.id.foodPrediction);

        //send data to adapter
        List<List<String>> list = new ArrayList<>();
        list.add(predictions.getPredictionsFoodLeft());
        list.add(predictions.getPredictionsFoodRight());
        list.add(predictions.getPredictionsFoodBottom());

        foodAdapter = new FoodPredictionRecyclerAdapter(list);
        recyclerView.setAdapter(foodAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.result_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save_meal:
                String predictionsString[] = foodAdapter.getTextPredictions();
                saveMeal(predictionsString);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveMeal(String predictionsString[]){
        Toast.makeText(getApplicationContext(), "Button save pressed", Toast.LENGTH_SHORT).show();
        //make predictions correct
        predictions.updatePredictionLeft(predictionsString[0]);
        predictions.updatePredictionRight(predictionsString[1]);
        predictions.updatePredictionBottom(predictionsString[2]);

        int userId = GetUserIdHelper.getUserId(getApplicationContext());

        new HttpSaveMeal(this, userId, predictions).execute();

    }

    private SendMeal makeSendMeal(ResponseFood responseFood) {

        SendMeal meal = new SendMeal();

        meal.setTypeMeal(typeMeal);

        meal.setFood1(responseFood.getFood1()[0]);
        meal.setFood2(responseFood.getFood2()[0]);
        meal.setFood3(responseFood.getFood3()[0]);
        meal.setWeightFood1(responseFood.getWeightFood1());
        meal.setWeightFood2(responseFood.getWeightFood2());
        meal.setWeightFood3(responseFood.getWeightFood3());
        meal.setNutrients1(responseFood.getNutrientsFood1());
        meal.setNutrients2(responseFood.getNutrientsFood2());
        meal.setNutrients3(responseFood.getNutrientsFood3());

        if (responseFood.getFood1() == null) {
            meal.setNutrients1(null);
            meal.setWeightFood1(0);
            meal.setFood1(null);
        } else if (responseFood.getFood2() == null) {
            meal.setNutrients2(null);
            meal.setWeightFood2(0);
            meal.setFood2(null);
        } else if (responseFood.getFood2() == null) {
            meal.setNutrients3(null);
            meal.setWeightFood3(0);
            meal.setFood3(null);
        }

        return meal;
    }

    public void startCorretPredictionActivity(Intent intent) {
        startActivityForResult(intent, CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /*super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE) {
            if (resultCode == RESULT_OK) {

                BundleCorrectFood bundle = (BundleCorrectFood) data.getExtras().getSerializable("myBundle");

                Food food = listFood.get(bundle.getPosition());
                food.setName(bundle.getName());

                listFood.get(bundle.getPosition()).setName(bundle.getName());

                //setLayoutAfterRequest(responseFood);
                setLayoutAfterRequest(listFood);

                Log.i("FOOD", bundle.getName() + " " + bundle.getPosition());
            }
        }*/
    }

    public int getUserId() {
        ObesityDbDao dao = new ObesityDbDao(this);
        User user = dao.getUser();
        return user.getId();
    }

    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.arg1 == 1)
                Toast.makeText(getApplicationContext(),"Meal has been added", Toast.LENGTH_SHORT).show();
            else if(msg.arg1 == 1)
                Toast.makeText(getApplicationContext(),"Error to save meal", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void sendToResultScreen(FoodsWeightEstimation foodsWeightEstimation) {

    }
}