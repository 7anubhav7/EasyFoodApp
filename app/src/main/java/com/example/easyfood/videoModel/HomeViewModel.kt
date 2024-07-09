package com.example.easyfood.videoModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easyfood.pojo.Category
import com.example.easyfood.pojo.CategoryList

import com.example.easyfood.pojo.Meal
import com.example.easyfood.pojo.MealList
import com.example.easyfood.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// for popular items (6)
import com.example.easyfood.pojo.MealsByCategoryList
import com.example.easyfood.pojo.MealsByCategory


class HomeViewModel() : ViewModel() {

    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData< List<MealsByCategory>>() //for popular items(6)
    private var categoriesLiveData = MutableLiveData< List<Category>>() //for category items(6)

    fun getRandomMeal(){
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body() != null){
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal

                }
                else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment",t.message.toString())
            }

        })
    }


    fun observeRandomMealLivedata() : LiveData<Meal>{
        return randomMealLiveData
    }



    fun getPopularItems(){

        RetrofitInstance.api.getPopularItems("Seafood").enqueue( object : Callback<MealsByCategoryList>{
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if(response.body() != null ){
                    popularItemsLiveData.value = response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("HomeFragment",t.message.toString())
            }

        })
    }

    fun observePopularItemsLiveData() : LiveData<List<MealsByCategory>>{
        return popularItemsLiveData
    }


    //categories related functoins

    fun getCategoires(){
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                response.body()?.let { categoryList ->
                    categoriesLiveData.postValue(categoryList.categories)
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("HomeViewModel",t.message.toString())
            }

        })
    }

    fun observeCategoriesLiveData(): LiveData<List<Category>>{
        return categoriesLiveData
    }







}