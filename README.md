# Fonts, plugin for gradle

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-fonts-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/5514)

```java
apply plugin: 'com.android.application'
apply plugin: 'fonts'

fonts {
    output = "/src/main/assets/fonts/" //optionnal
    families {
        roboto { //download only Roboto-Light and Roboto-Regulat
            variants {
                Light
                Regular
            }
        }
        roboto_condensed //download all Roboto-Condensed
        raleway { //download only Raleway-Thin
            variants {
                thin
               
            }
        }
    }
}
```

Then run `./gradlew downloadSelectedFonts`

This will create

```
app/
----src/
--------main/
------------assets/
----------------fonts/
--------------------Roboto-Regular.ttf
--------------------Roboto-Light.ttf

--------------------RobotoCondensed-Light.ttf
--------------------RobotoCondensed-LightItalic.ttf
--------------------RobotoCondensed-Regular.ttf
--------------------RobotoCondensed-Italic.ttf
--------------------RobotoCondensed-Bold.ttf
--------------------RobotoCondensed-BoldItalic.ttf

--------------------Raleway-Thin.ttf
```


# Import

<a href='https://ko-fi.com/A160LCC' target='_blank'><img height='36' style='border:0px;height:36px;' src='https://az743702.vo.msecnd.net/cdn/kofi1.png?v=0' border='0' alt='Buy Me a Coffee at ko-fi.com' /></a>

Import `fonts` in your root `build.gradle`

[ ![Download](https://api.bintray.com/packages/florent37/maven/fonts/images/download.svg) ](https://bintray.com/florent37/maven/fonts/_latestVersion)
```java 
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath "com.github.florent37:fonts:1.0"
    }
}
```


# Tasks

## Fetch all fonts

```
./gradlew displayAllAvailable

:app:displayAllAvailable
Downloading fonts...
- roboto
- open-sans
- slabo-27px
- lato
...
```

## Fetch fonts variants

Add a font in your project configuration
```
fonts {
    roboto
}
```

Then run `displaySelectedFontsVariants`

```
./gradlew displaySelectedFontsVariants

:app:displaySelectedFontsVariants
Variants of roboto
- Roboto-ThinItalic
- Roboto-Light
- Roboto-LightItalic
...
```

## Download fonts

```
./gradlew downloadSelectedFonts

:app:downloadSelectedFonts
Font roboto
downloading Roboto-Bold : https://fonts.gstatic.com/s/roboto/v15/d-6IYplOFocCacKzxwXSOKCWcynf_cDxXwCLxiixG1c.ttf
downloading Roboto-Light : https://fonts.gstatic.com/s/roboto/v15/Hgo13k-tfSpn0qi1SFdUfaCWcynf_cDxXwCLxiixG1c.ttf
downloading Roboto-Regular : https://fonts.gstatic.com/s/roboto/v15/zN7GBFwfMP4uA6AR0HCoLQ.ttf
...
```
