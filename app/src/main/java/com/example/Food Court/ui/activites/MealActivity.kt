package com.example.`Food Court`.ui.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer

import androidx.lifecycle.ViewModelProviders

import androidx.recyclerview.widget.GridLayoutManager
import com.example.`Food Court`.R

import com.example.`Food Court`.adapters.MealRecyclerAdapter

import com.example.`Food Court`.adapters.SetOnMealClickListener

import com.example.`Food Court`.data.pojo.Meal

import com.example.`Food Court`.databinding.ActivityCategoriesBinding

import com.example.`Food Court`.mvvm.MealActivityMVVM


class MealActivity : AppCompatActivity() {
    private lateinit var mealActivityMvvm: MealActivityMVVM
    private lateinit var binding: ActivityCategoriesBinding
    private lateinit var myAdapter: MealRecyclerAdapter
    private var categoryNme = ""
    private var MEAL_ID = "com.example.easyfood.mealId"
    private var MEAL_STR = "com.example.easyfood.mealSTR"
    private var MEAL_THUMB = "com.example.easyfood.mealThumb"
    private  var CATEGORY_NAME ="NAME CATEGORY"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mealActivityMvvm = ViewModelProviders.of(this)[MealActivityMVVM::class.java]
        startLoading()
        prepareRecyclerView()
        mealActivityMvvm.getMealsByCategory(getCategory())
        mealActivityMvvm.observeMeal().observe(this, object : Observer<List<Meal>> {
            override fun onChanged(t: List<Meal>?) {
                if(t==null){
                    hideLoading()
                    Toast.makeText(applicationContext, "No meals in this category", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }else {
                    myAdapter.setCategoryList(t!!)
                    binding.tvCategoryCount.text = categoryNme + " : " + t.size.toString()
                    hideLoading()
                }
            }
        })

        myAdapter.setOnMealClickListener(object : SetOnMealClickListener {
            override fun setOnClickListener(meal: Meal) {
                val intent = Intent(applicationContext, MealDetailesActivity::class.java)
                intent.putExtra(MEAL_ID, meal.idMeal)
                intent.putExtra(MEAL_STR, meal.strMeal)
                intent.putExtra(MEAL_THUMB, meal.strMealThumb)
                startActivity(intent)
            }
        })
    }

    private fun hideLoading() {
        binding.apply {
            loadingGifMeals.visibility = View.INVISIBLE
            mealRoot.setBackgroundColor(ContextCompat.getColor(applicationContext,R.color.white))
        }    }

    private fun startLoading() {
        binding.apply {
            loadingGifMeals.visibility = View.VISIBLE
            mealRoot.setBackgroundColor(ContextCompat.getColor(applicationContext,R.color.g_loading))
        }
    }

    private fun getCategory(): String {
        val tempIntent = intent
        val x = intent.getStringExtra(CATEGORY_NAME)!!
        categoryNme = x
        return x
    }

    private fun prepareRecyclerView() {
        myAdapter = MealRecyclerAdapter()
        binding.mealRecyclerview.apply {
            adapter = myAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
    }
}