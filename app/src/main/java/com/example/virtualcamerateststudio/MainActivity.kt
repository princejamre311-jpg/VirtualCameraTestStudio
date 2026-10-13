package com.example.virtualcamerateststudio

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ImageView
import android.widget.VideoView
import android.widget.MediaController

class MainActivity : Activity() {
    private lateinit var status: TextView
    private lateinit var imageView: ImageView
    private lateinit var videoView: VideoView
    private lateinit var mediaController: MediaController

    private val pickMedia = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
            setBackgroundColor(Color.WHITE)
        }

        val title = TextView(this).apply {
            text = "Virtual Camera Test Studio"
            textSize = 24f
            setTextColor(Color.rgb(25, 35, 50))
        }
        root.addView(title, matchWrap())

        val subtitle = TextView(this).apply {
            text = "Local test preview only — this app does not replace the Android system camera."
            textSize = 14f
            setTextColor(Color.DKGRAY)
            setPadding(0, 8, 0, 18)
        }
        root.addView(subtitle, matchWrap())

        val chooseImage = Button(this).apply {
            text = "Choose image"
            setOnClickListener { openPicker("image/*") }
        }
        root.addView(chooseImage, matchWrap())

        val chooseVideo = Button(this).apply {
            text = "Choose video"
            setOnClickListener { openPicker("video/*") }
        }
        root.addView(chooseVideo, matchWrap())

        status = TextView(this).apply {
            text = "Choose an image or video to preview."
            textSize = 14f
            setTextColor(Color.DKGRAY)
            setPadding(0, 12, 0, 12)
        }
        root.addView(status, matchWrap())

        imageView = ImageView(this).apply {
            adjustViewBounds = true
            scaleType = ImageView.ScaleType.FIT_CENTER
            visibility = ImageView.GONE
            setBackgroundColor(Color.rgb(245, 247, 250))
        }
        root.addView(imageView, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f
        ))

        videoView = VideoView(this).apply {
            visibility = VideoView.GONE
            setBackgroundColor(Color.BLACK)
        }
        root.addView(videoView, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f
        ))

        mediaController = MediaController(this)

        val stop = Button(this).apply {
            text = "Stop video"
            setOnClickListener {
                if (videoView.visibility == VideoView.VISIBLE) {
                    videoView.stopPlayback()
                    status.text = "Video stopped. Choose media to preview again."
                }
            }
        }
        root.addView(stop, matchWrap())

        setContentView(root)
    }

    private fun matchWrap() = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )

    private fun openPicker(mimeType: String) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = mimeType
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivityForResult(intent, pickMedia)
    }

    @Deprecated("Uses the classic activity result API for a compact sample project.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != pickMedia || resultCode != RESULT_OK) return
        val uri: Uri = data?.data ?: return
        val mime = contentResolver.getType(uri).orEmpty()

        if (mime.startsWith("image/")) {
            videoView.stopPlayback()
            videoView.visibility = VideoView.GONE
            imageView.visibility = ImageView.VISIBLE
            imageView.setImageURI(uri)
            status.text = "Image preview (inside this app only)."
        } else if (mime.startsWith("video/")) {
            imageView.setImageDrawable(null)
            imageView.visibility = ImageView.GONE
            videoView.visibility = VideoView.VISIBLE
            videoView.setVideoURI(uri)
            videoView.setMediaController(mediaController)
            mediaController.setAnchorView(videoView)
            videoView.setOnPreparedListener { player ->
                player.isLooping = true
                videoView.start()
                status.text = "Video preview looping (inside this app only)."
            }
            videoView.setOnErrorListener { _, _, _ ->
                status.text = "Couldn't play this video format on this device."
                true
            }
        } else {
            status.text = "Please choose a supported image or video file."
        }
    }
}
