package com.egco428.ex11_multitouch

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View

class MultiTouchView (context: Context):View(context){
    private var mActivePointer: SparseArray<PointF>? = null //? the value maybe changes in next line but init to be null
    private var mPaint: Paint = Paint()
    private var colors = intArrayOf(Color.BLUE,Color.RED,Color.GREEN,Color.YELLOW,Color.CYAN)
    private var txtPaint: Paint = Paint()

    init {
        initView()
    }
    private fun initView(){
        mActivePointer = SparseArray<PointF>()
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.setColor(Color.BLUE)
        mPaint.style = Paint.Style.FILL_AND_STROKE
        txtPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        txtPaint.textSize = 20F
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val pointerIndex = event!!.actionIndex
        val pointId = event.getPointerId(pointerIndex)
        val maskedAction = event.actionMasked

        when(maskedAction){
            //ถ้าแตะ2นิ้วพร้อมกัน มันต้องรู้ว่าแต่ละนิ้วใช้ pointer อะไร
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN ->{
                //กรณีหลายนิ้วลงพร้อมกัน
                val f = PointF()
                f.x = event.getX(pointerIndex)
                f.y = event.getY(pointerIndex)
                mActivePointer!!.put(pointId, f)
            }
            MotionEvent.ACTION_MOVE ->{
                val size = event.pointerCount
                var i = 0
                while(i<size){
                    val point = mActivePointer!!.get(event.getPointerId(i))
                    if(point != null){
                        point.x = event.getX(i)
                        point.y = event.getY(i)
                    }
                    i++
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_CANCEL ->{
                mActivePointer!!.remove(pointId)
            }
        }
        invalidate() //refresh canvas
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val size = mActivePointer!!.size()
        var i = 0
        while (i<size){
            val point = mActivePointer!!.valueAt(i)
            if (point!=null){
                mPaint.setColor(colors[i%5]) //if more than 5fingers 6th finger must be first again
            }
            canvas!!.drawCircle(point.x, point.y, 60F, mPaint) //!! can be null
            i++
        }
        canvas!!.drawText("Total Pointers: "+size, 10F, 40F, txtPaint)
    }
}