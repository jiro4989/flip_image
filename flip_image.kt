import java.io.File
import java.io.FileNotFoundException
import javafx.application.*
import javafx.embed.swing.SwingFXUtils
import javafx.scene.image.*
import javafx.stage.*
import javax.imageio.ImageIO

fun printHelp(text: String) {
  print("""
  $text

  ?                : このヘルプを表示する
  -f    (from)     : 左右反転元の画像フォルダ
  -t    (to)       : 左右反転後画像を格納するフォルダ
  [-ft] (from_tag) : 元ファイルのファイル名内に含ませるタグ名
  [-tt] (to_tag)   : 変換後のファイルに付与するタグ

  使い方一例
  ----------
  1. java -jar flip.jar -f from_dir -t to_dir
  2. java -jar flip.jar -f from_dir -t to_dir -ft _from_tag_ -tt _to_tag_
  """)
}

class MyApp : Application() {

  override fun start(stage : Stage) {
    val args = parameters.raw

    // 引数チェック
    if (args.size < 1) {
      printHelp("引数が必要です。")
      Platform.exit()
      return
    }

    // ヘルプの表示
    if (args.contains("?")) {
      printHelp("ヘルプ")
      Platform.exit()
      return
    }

    val fIndex = args.indexOf("-f")
    val tIndex = args.indexOf("-t")

    try {

      if (fIndex != -1 && tIndex != -1) {
        val from = args[fIndex + 1]
        val to   = args[tIndex + 1]

        val fromDir = File(from)
        if (!fromDir.exists()) throw FileNotFoundException("$fromDir は存在しません。")
        File(to).mkdir()

        fromDir.listFiles { f -> f.isFile && f.extension == "png"}
        .forEach {
          val src = from + File.separator + it.name
          val tmp = to   + File.separator + it.name

          // ファイル名に含まれるタグ名を変更する場合は実行される
          val ftIndex = args.indexOf("-ft")
          val ttIndex = args.indexOf("-tt")
          val dist = 
          if (ftIndex != -1 && ttIndex != -1) {
            val ft = args[ftIndex + 1]
            val tt = args[ttIndex + 1]
            tmp.replace(ft, tt)
          } else tmp

          MyImage(src).flip().write(dist)
          println("$src >>> $dist | [ 出力成功 ]")
        }

        println("すべての出力が正常に完了しました。")
      } else {
        printHelp("引数が必要です。")
      }

    } catch (e: FileNotFoundException) {
      println(e)
      printHelp("変換元ファイルが見つかりませんでした。")
    } catch (e: ArrayIndexOutOfBoundsException) {
      printHelp("指定したオプションの直後に対象も入力してください。")
    }

    Platform.exit()
  }

}

fun main(args: Array<String>) {
  Application.launch(MyApp::class.java, *args)
}

// 不変Imageラッパークラス
class MyImage(image: Image) { // {{{
  val img = image
  val width = img.width.toInt()
  val height = img.height.toInt()
  val FORMAT = PixelFormat.getIntArgbInstance()

  constructor(filePath: String) : this(Image("file:" + filePath))

  // 左右反転した新しい自身のインスタンスを生成する
  fun flip(): MyImage {
    val pixels = getPixels()
    val newPixels = pixels.copyOf()

    // 左右反転したpixel画素を格納した配列を生成
    for (i in 0..pixels.size-1) {
      val a = (i + width) / width * width
      val b = i / width * width
      val reverseIndex = a.toInt() + b.toInt() - i - 1
      newPixels[reverseIndex] = pixels[i]
    }

    val wImage = WritableImage(width, height)
    val writer = wImage.getPixelWriter()
    writer.setPixels(0, 0, width, height, FORMAT, newPixels, 0, width)
    return MyImage(wImage)
  }

  fun write(fileName: String) = write(File(fileName))

  fun write(outFile: File) {
    val newImg = SwingFXUtils.fromFXImage(img, null)
    ImageIO.write(newImg, "png", outFile)
  }

  private fun getPixels(): IntArray {
    val reader = img.getPixelReader()
    val pixels = IntArray(width * height)
    reader.getPixels(0, 0, width, height, FORMAT, pixels, 0, width)
    return pixels
  }

} // }}}

