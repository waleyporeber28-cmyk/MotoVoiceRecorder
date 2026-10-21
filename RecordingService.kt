package com.edverything.motovoicerecorder
import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.media.MediaRecorder
import android.os.Build
import android.os.IBinder
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecordingService:Service(){
 companion object { const val START="MVR_START"; const val STOP="MVR_STOP"; const val CHANNEL="mvr_channel"; const val ID=8 }
 private var recorder:MediaRecorder?=null
 private var audioManager:AudioManager?=null
 private var file:File?=null
 override fun onBind(intent:Intent?):IBinder?=null
 override fun onStartCommand(intent:Intent?,flags:Int,startId:Int):Int { if(intent?.action==STOP) stopRec() else if(intent?.action==START) startRec(); return START_NOT_STICKY }
 private fun startRec(){
  if(recorder!=null)return
  if(Build.VERSION.SDK_INT>=26){getSystemService(NotificationManager::class.java).createNotificationChannel(NotificationChannel(CHANNEL,"MotoVoice recording",NotificationManager.IMPORTANCE_LOW))}
  val n=Notification.Builder(this,CHANNEL).setContentTitle("MotoVoice Recorder").setContentText("Starting microphone…").setSmallIcon(android.R.drawable.ic_btn_speak_now).setOngoing(true).build()
  if(Build.VERSION.SDK_INT>=34) startForeground(ID,n,android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE) else startForeground(ID,n)
  try{
   audioManager=getSystemService(Context.AUDIO_SERVICE) as AudioManager
   audioManager?.mode=AudioManager.MODE_IN_COMMUNICATION
   val inputs=audioManager?.getDevices(AudioManager.GET_DEVICES_INPUTS).orEmpty()
   val bt=inputs.firstOrNull{it.type==AudioDeviceInfo.TYPE_BLUETOOTH_SCO || (Build.VERSION.SDK_INT>=31 && it.type==AudioDeviceInfo.TYPE_BLE_HEADSET)}
   val dir=getExternalFilesDir(Environment.DIRECTORY_MUSIC)?:filesDir; dir.mkdirs()
   file=File(dir,"MotoVoice_"+SimpleDateFormat("yyyyMMdd_HHmmss",Locale.US).format(Date())+".m4a")
   val r=if(Build.VERSION.SDK_INT>=31) MediaRecorder(this) else MediaRecorder()
   r.setAudioSource(MediaRecorder.AudioSource.MIC); r.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); r.setAudioEncoder(MediaRecorder.AudioEncoder.AAC); r.setAudioEncodingBitRate(128000); r.setAudioSamplingRate(44100); r.setOutputFile(file!!.absolutePath)
   if(Build.VERSION.SDK_INT>=28 && bt!=null) r.setPreferredDevice(bt)
   r.prepare(); r.start(); recorder=r
   val msg=if(bt!=null)"Recording; requested Bluetooth input: ${bt.productName}" else "Recording; no Bluetooth mic input reported, phone mic may be used"
   notifyStatus(msg)
  }catch(e:Exception){notifyStatus("Start failed: ${e.javaClass.simpleName}: ${e.message}"); stopForeground(STOP_FOREGROUND_REMOVE); stopSelf()}
 }
 private fun stopRec(){ try{recorder?.stop()}catch(_:Exception){}; try{recorder?.release()}catch(_:Exception){}; recorder=null; audioManager?.mode=AudioManager.MODE_NORMAL; notifyStatus("Saved: ${file?.name?: "no file"}"); stopForeground(STOP_FOREGROUND_REMOVE); stopSelf() }
 private fun notifyStatus(s:String){getSystemService(NotificationManager::class.java).notify(ID,Notification.Builder(this,CHANNEL).setContentTitle("MotoVoice Recorder").setContentText(s.take(180)).setSmallIcon(android.R.drawable.ic_btn_speak_now).build())}
 override fun onDestroy(){if(recorder!=null)stopRec();super.onDestroy()}
}
