package com.example.broadcastreceiverpro

import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.broadcastreceiverpro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //1. 브로드캐스터 리시버 만들어서 바로 밧데리 정보를 흭득함.
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val intent = registerReceiver(null, intentFilter)

        //2. 밧테리 정보량을 체크함.
        val extra_status = intent?.getIntExtra(BatteryManager.EXTRA_STATUS,-1)
        when(extra_status){
            //충전정보체크하는데 usb충전중,ac충전중,no충전중
            BatteryManager.BATTERY_STATUS_CHARGING ->{
                when(intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,-1)){
                    BatteryManager.BATTERY_PLUGGED_AC ->{
                        binding.ivBattery.setImageBitmap(BitmapFactory.decodeResource
                            (resources,R.drawable.ac))
                        binding.tvinfo.text = "PLUGGED_AC"
                    }
                    BatteryManager.BATTERY_PLUGGED_USB ->{
                        binding.ivBattery.setImageBitmap(BitmapFactory.decodeResource
                            (resources,R.drawable.usb))
                        binding.tvinfo.text = "PLUGGED_USB"
                    }
                    BatteryManager.BATTERY_PLUGGED_WIRELESS ->{
                        binding.ivBattery.setImageBitmap(BitmapFactory.decodeResource
                            (resources,R.drawable.wireless))
                        binding.tvinfo.text = "PLUGGED_WIRELESS"
                    }
                    else ->{
                        binding.ivBattery.setImageBitmap(BitmapFactory.decodeResource
                            (resources,R.drawable.battery_full_24))
                        binding.tvinfo.text = "Full CHARGING"
                    }
                }
            }
            //NO충전
            else ->{
                binding.ivBattery.setImageBitmap(BitmapFactory.decodeResource
                    (resources,R.drawable.battery_unknown_24))
                binding.tvinfo.text = "NO CHARGING~~~"
            }
        }
        //배터리 잔여량을 계산해서 보여줌.
        val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL,-1)
        val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE,-1)
        val percent = (level!!.toFloat() / scale!!.toFloat()) * 100
        binding.tvpercent.text = "${percent} %"


        //이벤트 처리 (내가 마든 MyReceiver 불러서 Notification 알림발생)
        binding.btnCallReceiver.setOnClickListener {
            val intent = Intent(this,MyReceiver::class.java)
            intent.putExtra("batteryPercent", "${binding.tvpercent.text}")
            sendBroadcast(intent)
        }
    }
}