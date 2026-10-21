package com.edverything.motovoicerecorder
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
 private lateinit var details: TextView
 override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState)
  val title=TextView(this).apply { text="MotoVoice Recorder"; textSize=24f; gravity=Gravity.CENTER }
  details=TextView(this).apply { textSize=15f; setPadding(12,20,12,20) }
  val check=Button(this).apply { text="Check microphone route"; setOnClickListener { checkRoute() } }
  val start=Button(this).apply { text="Start recording"; setOnClickListener { startRecording() } }
  val stop=Button(this).apply { text="Stop recording"; setOnClickListener { startService(Intent(this@MainActivity,RecordingService::class.java).setAction(RecordingService.STOP)) } }
  val bt=Button(this).apply { text="Bluetooth settings"; setOnClickListener { startActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS)) } }
  setContentView(LinearLayout(this).apply { orientation=1; gravity=Gravity.CENTER_HORIZONTAL; setPadding(18,30,18,18); addView(title); addView(details); addView(check); addView(start); addView(stop); addView(bt) })
  val perms=mutableListOf(Manifest.permission.RECORD_AUDIO)
  if(Build.VERSION.SDK_INT>=31) perms.add(Manifest.permission.BLUETOOTH_CONNECT)
  if(Build.VERSION.SDK_INT>=33) perms.add(Manifest.permission.POST_NOTIFICATIONS)
  val missing=perms.filter { checkSelfPermission(it)!=PackageManager.PERMISSION_GRANTED }
  if(missing.isNotEmpty()) requestPermissions(missing.toTypedArray(),12)
  checkRoute()
 }
 private fun checkRoute() {
  val am=getSystemService(Context.AUDIO_SERVICE) as AudioManager
  val ins=am.getDevices(AudioManager.GET_DEVICES_INPUTS)
  val outs=am.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
  fun label(d:AudioDeviceInfo)=d.productName.toString()+" — "+when(d.type){AudioDeviceInfo.TYPE_BLUETOOTH_SCO->"Bluetooth headset mic";AudioDeviceInfo.TYPE_BUILTIN_MIC->"Phone mic";AudioDeviceInfo.TYPE_WIRED_HEADSET->"Wired headset";else->"type "+d.type}
  details.text="INPUTS\n"+ins.joinToString("\n"){label(it)}+"\n\nOUTPUTS\n"+outs.joinToString("\n"){label(it)}+"\n\nThis list cannot by itself prove which mic is being recorded."
 }
 private fun startRecording() {
  if(checkSelfPermission(Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED){requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO),12);return}
  val i=Intent(this,RecordingService::class.java).setAction(RecordingService.START)
  if(Build.VERSION.SDK_INT>=26) startForegroundService(i) else startService(i)
 }
}
