@echo off
cls

:: �����Ŏg�p����ꎞ�ϐ��̏�����
set img_from=
set img_to=
set change_tag=
set from_tag=
set to_tag=

echo �v���O������r���ŏI������ꍇ�� Ctrl + C �������Ă��������B
echo ----------------------------------------------------------------
echo.
echo [ �t�H���_�ꗗ ]
echo ----------------
echo.
dir /a:d /b
echo.
set /p img_from="���E���]���̉摜�t�H���_������͂��Ă��������B >> "
echo ���E���]�����摜��ۑ�����t�H���_������͂��Ă��������B
set /p img_to="( ���݂��Ȃ��t�H���_�̏ꍇ�͎����ŐV�K�쐬 ) >> "
set /p change_tag="�t�@�C�����̃^�O���ύX���܂����H[y/n] >> "

:: if���̒��ł̕ϐ��W�J�����s�ł���悤�ɂ��邽�߂̐ݒ�
setlocal enabledelayedexpansion

:: �󕶎��񂾂����ꍇ��else���ɃW�����v
if "%change_tag%"=="" goto el

if %change_tag%==y (
  set /p from_tag="�ϊ����̃t�@�C���Ɋ܂܂��^�O����͂��Ă��������B >> "
  set /p to_tag="�ϊ���̃t�@�C���ɕt�^����^�O����͂��Ă��������B >> "

  if "!from_tag!"=="" (
    echo �^�O�̎w�肪����܂���ł����B
    goto el
  )

  if "!to_tag!"=="" (
    echo �^�O�̎w�肪����܂���ł����B
    goto el
  )

  echo �摜�̍��E���]�����s���܂��B
  java -jar flip_image.jar -f %img_from% -t %img_to% -ft !from_tag! -tt !to_tag!
) else (
  :el
  echo �摜�̍��E���]�����s���܂��B
  java -jar flip_image.jar -f %img_from% -t %img_to%
)

pause
