package com.example.healthapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.healthapp.databinding.ActivityDrawingBinding

class DrawingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDrawingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button functionality
        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        // Color selection
        binding.colorWhite.setOnClickListener { binding.drawingView.setColor("#FFFFFF") }
        binding.colorRed.setOnClickListener { binding.drawingView.setColor("#FF0000") }
        binding.colorGreen.setOnClickListener { binding.drawingView.setColor("#00FF00") }
        binding.colorBlue.setOnClickListener { binding.drawingView.setColor("#0000FF") }
        binding.colorBlack.setOnClickListener { binding.drawingView.setColor("#000000") }
        binding.colorYellow.setOnClickListener { binding.drawingView.setColor("#FFFF00") }
        binding.colorCyan.setOnClickListener { binding.drawingView.setColor("#00FFFF") }
        binding.colorMagenta.setOnClickListener { binding.drawingView.setColor("#FF00FF") }

        // Tools
        binding.btnBrush.setOnClickListener {
            binding.drawingView.setEraser(false)
            binding.drawingView.setStyle("Normal")
            binding.drawingView.setBrushSize(20f)
        }

        binding.btnEraser.setOnClickListener {
            binding.drawingView.setEraser(true)
            binding.drawingView.setBrushSize(50f)
        }

        binding.btnNeon.setOnClickListener {
            binding.drawingView.setEraser(false)
            binding.drawingView.setStyle("Neon")
            binding.drawingView.setBrushSize(30f)
        }

        binding.btnMarker.setOnClickListener {
            binding.drawingView.setEraser(false)
            binding.drawingView.setStyle("Marker")
            binding.drawingView.setBrushSize(40f)
        }

        binding.btnDash.setOnClickListener {
            binding.drawingView.setEraser(false)
            binding.drawingView.setStyle("Dash")
            binding.drawingView.setBrushSize(10f)
        }

        binding.btnClear.setOnClickListener {
            binding.drawingView.clearCanvas()
        }
    }
}
