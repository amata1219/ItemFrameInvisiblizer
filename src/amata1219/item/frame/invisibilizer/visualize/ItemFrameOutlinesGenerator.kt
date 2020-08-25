package amata1219.item.frame.invisibilizer.visualize

import com.google.common.math.DoubleMath
import org.bukkit.Location
import org.bukkit.entity.ItemFrame
import org.bukkit.util.BoundingBox
import org.bukkit.util.Vector
import java.awt.color.ColorSpace

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
        return generateLocationsOfEvenlySpacedPoints(min, max, Vector::getX) {
            vector, element -> vector.setX(element)
        } + generateLocationsOfEvenlySpacedPoints(min, max, Vector::getY) {
            vector, element -> vector.setY(element)
        } + generateLocationsOfEvenlySpacedPoints(min, max, Vector::getZ) {
            vector, element -> vector.setZ(element)
        }
    }

}