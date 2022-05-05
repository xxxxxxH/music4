package win.hapsunday.mplayer.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatSeekBar

class ISeek : AppCompatSeekBar {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas?) {
        canvas?.rotate(-90f);
        canvas?.translate(-(height.toFloat()), 0f);
        super.onDraw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredHeight
        val height = measuredWidth
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!isEnabled) return false;
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {

            }
            MotionEvent.ACTION_MOVE->{

            }
            MotionEvent.ACTION_UP ->{
                progress = (max - (max * event.y / height)).toInt()
                onSizeChanged(width, height, 0, 0);
            }
        }
        return true;
    }
}