package com.example.neostorelokesh.activities

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neostorelokesh.R
import com.example.neostorelokesh.adapter.OrderDetailAdapter
import com.example.neostorelokesh.api.RetrofitClient
import com.example.neostorelokesh.dataclass.OrderDetail
import com.example.neostorelokesh.dataclass.OrderDetailResponse
import com.example.neostorelokesh.storage.SharedPreferenceManager
import kotlinx.android.synthetic.main.activity_order_activty_screen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderActivtyScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_activty_screen)

        getOrderDetail()
        myOrdersTollbarBack.setOnClickListener(){
            onBackPressed()
        }
    }

    private fun getOrderDetail() {


        val orderId = intent.getStringExtra("ORDER_ID")?.toInt()
        Log.d(TAG, "getOrderDetail: $orderId")

        myOrdersToolbarTitle.text = "Order ID: $orderId"
        val accessToken = SharedPreferenceManager.getInstance(this).data.access_token
        RetrofitClient.getClient.orderDeatilList(accessToken,orderId).enqueue(object : Callback<OrderDetailResponse?> {
            override fun onResponse(
                call: Call<OrderDetailResponse?>,
                response: Response<OrderDetailResponse?>
            ) {
                if(response.body()!!.status == 200){
                    val cost = RupeeConvertorHelperClass().convertorfunction(response.body()!!.data.cost)
                    Orderprice.text = cost
                    val orderDetailList = mutableListOf<OrderDetail>()
                    for (item in response.body()!!.data.order_details){
                        orderDetailList.add(item)
                    }
                    val orderDetailAdapter = OrderDetailAdapter(orderDetailList)
                    OrdersDetailRecyclerView.adapter = orderDetailAdapter
                    OrdersDetailRecyclerView.layoutManager = LinearLayoutManager(this@OrderActivtyScreen,
                        LinearLayoutManager.VERTICAL,false)

                }
            }

            override fun onFailure(call: Call<OrderDetailResponse?>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })


    }
}