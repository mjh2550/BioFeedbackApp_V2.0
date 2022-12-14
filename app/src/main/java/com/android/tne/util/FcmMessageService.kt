package com.android.tne.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.android.tne.ui.activity.FcmActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmMessageService : FirebaseMessagingService() {
    //토큰이 생성, 업데이트 될 시 호출됨
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        //서버에 토큰을 저장하면 된다.
        Log.d("fcm","$token")
    }

    //메세지 수신 시 호출
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if(message.data.isNotEmpty()) sendMessage(message.notification?.title,message.notification!!.body!!)
    }

    //받은 메세지 알림창 구현
    private fun sendMessage(title:String? , body : String){
        Log.d("fcm","sendMessage")
        val intent = Intent(this@FcmMessageService, FcmActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this@FcmMessageService,0,intent,
            PendingIntent.FLAG_ONE_SHOT)
        val channelId = "my_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this,/** Request Code **/channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 오레오 버전 예외처리
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId, "channel human readable title"
                , NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0/** ID of notification **/,notificationBuilder.build())
    }
}