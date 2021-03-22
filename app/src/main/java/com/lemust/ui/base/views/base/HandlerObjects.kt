package  com.example.dmitro.statistichistogram.base.render.base


import android.graphics.Canvas
import com.lemust.ui.base.views.base.models.GameObject
import java.util.*

class HandlerObjects {
    private val objects = ArrayList<GameObject>()
    private var isRedraw = true


    @Synchronized
    fun tick() {
      //  if (isRedraw) {

            val iterator = objects.iterator()
            while (iterator.hasNext()) {

                var obj = iterator.next()

                obj.tick(objects)

            }
            isRedraw = false
     //   }
    }

    @Synchronized
    fun render(canvas: Canvas) {
       // if (isRedraw) {
            val iterator = objects.iterator()
            while (iterator.hasNext()) {
                var obj = iterator.next()
                obj.render(canvas)

            }
            isRedraw = false
        //}
    }

    @Synchronized
    fun addObject(gameObject: GameObject) {
        objects.add(gameObject)
    }

    fun removeObject(gameObject: GameObject) {
        objects.remove(gameObject)
    }

    fun redraw() {
        isRedraw = true
    }

    fun clear(){
        objects.clear()
    }

}
