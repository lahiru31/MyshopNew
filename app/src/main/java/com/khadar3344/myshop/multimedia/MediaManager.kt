package com.khadar3344.myshop.multimedia

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.util.Log
import java.io.File
import java.io.IOException
import javax.inject.Inject

class MediaManager @Inject constructor(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private var mediaRecorder: MediaRecorder? = null
    private var isRecordingState: Boolean = false
    private var isPlayingState: Boolean = false

    fun startRecording(outputFile: File) {
        try {
            mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                MediaRecorder()
            }.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(outputFile.absolutePath)
                
                try {
                    prepare()
                    start()
                    isRecordingState = true
                    Log.d("MediaManager", "Started recording to ${outputFile.absolutePath}")
                } catch (e: IOException) {
                    Log.e("MediaManager", "Failed to start recording", e)
                    release()
                }
            }
        } catch (e: Exception) {
            Log.e("MediaManager", "Error initializing recorder", e)
        }
    }

    fun stopRecording() {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            isRecordingState = false
            Log.d("MediaManager", "Stopped recording")
        } catch (e: Exception) {
            Log.e("MediaManager", "Error stopping recording", e)
        }
    }

    fun startPlaying(audioUri: Uri) {
        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(context, audioUri)
                prepare()
                start()
                isPlayingState = true
                
                setOnCompletionListener {
                    isPlayingState = false
                    release()
                    mediaPlayer = null
                }
            }
            Log.d("MediaManager", "Started playing audio")
        } catch (e: Exception) {
            Log.e("MediaManager", "Error playing audio", e)
        }
    }

    fun stopPlaying() {
        try {
            mediaPlayer?.apply {
                if (isPlaying) {
                    stop()
                }
                release()
            }
            mediaPlayer = null
            isPlayingState = false
            Log.d("MediaManager", "Stopped playing audio")
        } catch (e: Exception) {
            Log.e("MediaManager", "Error stopping playback", e)
        }
    }

    fun isRecording(): Boolean = isRecordingState
    fun isPlaying(): Boolean = isPlayingState

    fun release() {
        stopRecording()
        stopPlaying()
    }
}
