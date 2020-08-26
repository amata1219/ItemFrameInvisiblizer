package amata1219.item.frame.invisibilizer.visualize

import com.google.common.math.DoubleMath
import org.bukkit.Location
import org.bukkit.entity.ItemFrame
import org.bukkit.util.BoundingBox
import org.bukkit.util.Vector
import java.awt.color.ColorSpace
import kotlin.reflect.KFunction1

object ItemFrameOutlinesGenerator {

    private fun generateLocationsOfEvenlySpacedPoints(
            min: Vector,
            max: Vector,
            vectorElementGetter: (Vector) -> Double,
            vectorElementSetter: (Vector, Double) -> Vector
    ): List<Vector> {
        return generateSequence(vectorElementGetter(min)) {
            it + 1.0 / 8
        }.takeWhile {
            it <= vectorElementGetter(max)
        }.flatMap {
            sequenceOf(
                    vectorElementSetter(min.clone(), it),
                    vectorElementSetter(max.clone(), it)
            )
        }.toList()
    }

    fun generateOutlinesVectors(frame: ItemFrame): List<Vector> {
        val min: Vector = frame.boundingBox.min
        val max: Vector = frame.boundingBox.max
        return sequenceOf<Pair<KFunction1<Vector, Double>, (Vector, Double) -> Vector>>(
                Vector::getX to { v, e -> v.setX(e)},
                Vector::getY to { v, e -> v.setY(e)},
                Vector::getZ to { v, e -> v.setZ(e)}
        ).map {
            generateLocationsOfEvenlySpacedPoints(min, max, it.first, it.second)
        }.reduce { l1, l2 ->
            l1 + l2
        }
    }

}