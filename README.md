# Fonts, plugin for gradle

```java
apply plugin: 'com.android.application'
apply plugin: 'fonts'

fonts {

    roboto { //download only Roboto-Light and Roboto-Regulat
        variants {
            Light
            Regular
        }
    }
    roboto_condensed //download all Roboto-Condensed

    raleway { //download only Raleway-Thin, Raleway-Light and Raleway-Bold
        variants {
            thin
           
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

Import `fonts` in your root `build.gradle`

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


#Tasks

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
Font roboto
downloading Roboto-Bold : https://fonts.gstatic.com/s/roboto/v15/d-6IYplOFocCacKzxwXSOKCWcynf_cDxXwCLxiixG1c.ttf
downloading Roboto-Light : https://fonts.gstatic.com/s/roboto/v15/Hgo13k-tfSpn0qi1SFdUfaCWcynf_cDxXwCLxiixG1c.ttf
downloading Roboto-Regular : https://fonts.gstatic.com/s/roboto/v15/zN7GBFwfMP4uA6AR0HCoLQ.ttf
...



