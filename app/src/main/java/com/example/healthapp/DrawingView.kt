package com.example.healthapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var drawPath: Path = Path()
    private var drawPaint: Paint = Paint()
    private var canvasPaint: Paint = Paint(Paint.DITHER_FLAG)
    private var drawCanvas: Canvas? = null
    private var canvasBitmap: Bitmap? = null
    private var brushSize: Float = 20f
    private var color = Color.BLACK
    private var isEraser = false

    init {
        setupDrawing()
        // Hardware acceleration can sometimes interfere with CLEAR mode
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    private fun setupDrawing() {
        drawPaint.color = color
        drawPaint.isAntiAlias = true
        drawPaint.strokeWidth = brushSize
        drawPaint.style = Paint.Style.STROKE
        drawPaint.strokeJoin = Paint.Join.ROUND
        drawPaint.strokeCap = Paint.Cap.ROUND
        drawPaint.alpha = 255
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0) {
            canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            drawCanvas = Canvas(canvasBitmap!!)
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvasBitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, canvasPaint)
        }
        // If we are erasing, we don't draw the current path on the main canvas 
        // until ACTION_UP, or we use a temporary canvas to show the "hole"
        if (!isEraser) {
            canvas.drawPath(drawPath, drawPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                drawPath.moveTo(touchX, touchY)
            }
            MotionEvent.ACTION_MOVE -> {
                drawPath.lineTo(touchX, touchY)
                if (isEraser) {
                    // For eraser, apply directly to the bitmap canvas for immediate effect
                    drawCanvas?.drawPath(drawPath, drawPaint)
                    drawPath.reset()
                    drawPath.moveTo(touchX, touchY)
                }
            }
            MotionEvent.ACTION_UP -> {
                drawCanvas?.drawPath(drawPath, drawPaint)
                drawPath.reset()
            }
            else -> return false
        }

        invalidate()
        return true
    }

    fun setColor(newColor: String) {
        isEraser = false
        drawPaint.xfermode = null
        color = Color.parseColor(newColor)
        drawPaint.color = color
        invalidate()
    }
    
    fun setBrushSize(newSize: Float) {
        brushSize = newSize
        drawPaint.strokeWidth = brushSize
    }

    fun setEraser(eraser: Boolean) {
        isEraser = eraser
        if (isEraser) {
            drawPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        } else {
            drawPaint.xfermode = null
            drawPaint.color = color
        }
    }

    fun setStyle(style: String) {
        isEraser = false
        drawPaint.xfermode = null
        when (style) {
            "Normal" -> {
                drawPaint.maskFilter = null
                drawPaint.pathEffect = null
                drawPaint.alpha = 255
            }
            "Neon" -> {
                drawPaint.maskFilter = BlurMaskFilter(15f, BlurMaskFilter.Blur.NORMAL)
                drawPaint.pathEffect = null
                drawPaint.alpha = 255
            }
            "Marker" -> {
                drawPaint.maskFilter = null
                drawPaint.pathEffect = null
                drawPaint.alpha = 100 
            }
            "Dash" -> {
                drawPaint.maskFilter = null
                drawPaint.pathEffect = DashPathEffect(floatArrayOf(20f, 20f), 0f)
                drawPaint.alpha = 255
            }
        }
    }

    fun clearCanvas() {
        drawCanvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        invalidate()
    }
}
