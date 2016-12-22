package com.sbbi.obesityappv2.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.sbbi.obesityappv2.R;
import com.sbbi.obesityappv2.activity.PhotoMenuActivity;
import com.sbbi.obesityappv2.interf.FoodInterf;
import com.sbbi.obesityappv2.model.Food;
import com.sbbi.obesityappv2.recycleradapter.FoodRecyclerAdapter;
import com.sbbi.obesityappv2.request.HttpRequestListFood;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by bsilva on 10/18/16.
 */
public class MyMealsFragment extends Fragment implements FoodInterf{

    private FoodRecyclerAdapter foodAdapter;
    private RecyclerView recyclerView;
    private FoodInterf listener;
    private FloatingActionButton button;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_my_meals, container, false);

        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_food);
        foodAdapter = new FoodRecyclerAdapter(getActivity(), new ArrayList<Food>(), this);
        recyclerView.setAdapter(foodAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        button = (FloatingActionButton) layout.findViewById(R.id.buttonPlus);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"Hey",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), PhotoMenuActivity.class));
            }
        });

        Food f1 = new Food();
        f1.setName("Breakfast");

        Food f2 = new Food();
        f2.setName("Lunch");

        Food f3 = new Food();
        f3.setName("Dinner");

        Food f4 = new Food();
        f4.setName("Breakfast");

        /*Food foods[] = new Food[4];
        foods[0] = f1;
        foods[1] = f2;
        foods[2] = f3;
        foods[3] = f4;*/

        List<Food> listFood = new ArrayList<Food>();
        listFood.add(f1);
        listFood.add(f2);
        listFood.add(f3);
        listFood.add(f4);

        setLayoutAfterRequest(listFood);
        //new HttpRequestListFood(this).execute();

        return layout;
    }


    @Override
    public void setLayoutAfterRequest(List<Food> food) {
        if(food.size() == 0) {
            //Toast.makeText(getActivity(),Path.NO_EVENTS_FOUND,Toast.LENGTH_LONG).show();
            //errorMsg.setVisibility(View.VISIBLE);
        }
        else{
            //errorMsg.setVisibility(View.INVISIBLE);
            foodAdapter = new FoodRecyclerAdapter(getActivity(), food, this);
            recyclerView.setAdapter(foodAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        /*if(swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }*/
    }
}
