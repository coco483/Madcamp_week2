import android.content.Context
import com.google.gson.Gson
import java.io.File
import java.io.FileReader
import java.io.FileWriter

object FileHelper {
    const val FILENAME = "favorite_list.json"

    fun saveFavoriteList(context: Context, favoriteList: List<String>) {
        val gson = Gson()
        val json = gson.toJson(favoriteList)

        try {
            val file = File(context.filesDir, FILENAME)
            val writer = FileWriter(file)
            writer.use {
                it.write(json)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadFavoriteList(context: Context): List<String> {
        val gson = Gson()
        var favoriteList: List<String> = emptyList()

        try {
            val file = File(context.filesDir, FILENAME)
            val reader = FileReader(file)
            favoriteList = gson.fromJson(reader, Array<String>::class.java).toList()
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return favoriteList
    }
}
