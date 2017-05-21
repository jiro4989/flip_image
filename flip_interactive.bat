@echo off
cls

:: ここで使用する一時変数の初期化
set img_from=
set img_to=
set change_tag=
set from_tag=
set to_tag=

echo プログラムを途中で終了する場合は Ctrl + C を押してください。
echo ----------------------------------------------------------------
echo.
echo [ フォルダ一覧 ]
echo ----------------
echo.
dir /a:d /b
echo.
set /p img_from="左右反転元の画像フォルダ名を入力してください。 >> "
echo 左右反転した画像を保存するフォルダ名を入力してください。
set /p img_to="( 存在しないフォルダの場合は自動で新規作成 ) >> "
set /p change_tag="ファイル名のタグも変更しますか？[y/n] >> "

:: if文の中での変数展開を実行できるようにするための設定
setlocal enabledelayedexpansion

:: 空文字列だった場合はelse文にジャンプ
if "%change_tag%"=="" goto el

if %change_tag%==y (
  set /p from_tag="変換元のファイルに含まれるタグを入力してください。 >> "
  set /p to_tag="変換後のファイルに付与するタグを入力してください。 >> "

  if "!from_tag!"=="" (
    echo タグの指定がありませんでした。
    goto el
  )

  if "!to_tag!"=="" (
    echo タグの指定がありませんでした。
    goto el
  )

  echo 画像の左右反転を実行します。
  java -jar flip_image.jar -f %img_from% -t %img_to% -ft !from_tag! -tt !to_tag!
) else (
  :el
  echo 画像の左右反転を実行します。
  java -jar flip_image.jar -f %img_from% -t %img_to%
)

pause
