package com.github.florent37.gradle

import groovy.json.JsonSlurper
import org.gradle.api.Plugin
import org.gradle.api.Project

class FontsPlugin implements Plugin<Project> {

    static def GROUP = "fonts"
    static def EXTENSION_NAME = "fonts"
    static def BASE_URL = "https://google-webfonts-helper.herokuapp.com/api/fonts/"
    static def DOWNLOAD_DIR = "/src/main/assets/fonts/"

    @Override
    void apply(Project project) {

        def fonts = project.container(FontExtension)
        fonts.all {
            // Here we have access to the project object, so we
            // can use the container() method to create a
            // NamedDomainObjectContainer for FontVariantExtension objects.
            variants = project.container(FontVariantExtension)
        }

        // Use fonts as name in the build script to define
        // fonts and variants.
        project.extensions.add(EXTENSION_NAME, fonts)

        def displayFont = project.tasks.create("currentConfiguration") << {
            println("")
            def fontExtensions = project.extensions.getByName(EXTENSION_NAME)
            for(FontExtension font in fontExtensions){
                println(font.name)
                font.variants.all{
                    println("     - $it.name")
                }
            }

        }
        displayFont.group = GROUP
        displayFont.description = "Display your current fonts configuration"

        def displayAllVariants = project.tasks.create("displaySelectedFontsVariants") << {
            project.extensions.getByName(EXTENSION_NAME).all{
                def fontName = it.name.replaceAll("_","-")
                println("\nVariants of $fontName")

                try {
                    def fontJson = downloadJson("$BASE_URL/$fontName")
                    for (def variant in fontJson.variants) {
                        def name = variant.local[1]

                        println("- $name")
                    }
                } catch (all) {
                    println("--- unable to download $fontName")
                }
            }

        }
        displayAllVariants.group = GROUP
        displayAllVariants.description = "List all variants of selected fonts"


        def displayAllAvailable = project.tasks.create("displayAllAvailable") << {
            println "Downloading fonts..."
            for(item in downloadJson(BASE_URL)){
                println("- $item.id")
            }
        }
        displayAllAvailable.group = GROUP
        displayAllAvailable.description = "Display all available fonts"

        def fontsTask = project.tasks.create("downloadSelectedFonts") << {

            def outputDirectory = "$project.name$DOWNLOAD_DIR"
            def assetsFonts = new File(outputDirectory);
            if (!assetsFonts.exists()) {
                assetsFonts.mkdirs()
                println("creating $outputDirectory")
            }

            def fontExtensions = project.extensions.getByName(EXTENSION_NAME)
            fontExtensions.all {
                def fontName = it.name.replaceAll("_", "-")
                println("\nFont $fontName")

                try {
                    def fontJson = downloadJson("$BASE_URL/$fontName")

                    if (it.variants == null || it.variants.isEmpty()) {
                        for (def variant in fontJson.variants) {
                            def name = variant.local[1]
                            def urlTtf = variant.ttf

                            println("downloading $name from $urlTtf")

                            downloadFile(urlTtf, outputDirectory + name + ".ttf")
                        }
                    }
                    else {
                        for (def localVariant in it.variants) {
                            final String endFix = "-" + localVariant.name.toLowerCase()
                            for (def variant in fontJson.variants) {
                                final String name = variant.local[1]
                                if (name.toLowerCase().endsWith(endFix)) {
                                    def urlTtf = variant.ttf

                                    println("downloading $name : $urlTtf")

                                    downloadFile(urlTtf, outputDirectory + name + ".ttf")
                                }
                            }
                        }
                    }
                } catch(all) {
                    println("----- unable to download $fontName")
                }
            }

        }

        fontsTask.group = GROUP
        fontsTask.description = "Download selected fonts (and variants)"

    }

    static def downloadJson(String url){
        return new JsonSlurper().parse(new URL(url))
    }

    static def downloadFile(String remoteUrl, String filePathAndName) {
        new File(filePathAndName).withOutputStream { out ->
            new URL(remoteUrl).withInputStream { from ->  out << from; }
        }
    }
}