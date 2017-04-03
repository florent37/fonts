package com.github.florent37.gradle

import groovy.json.JsonSlurper
import org.gradle.api.Plugin
import org.gradle.api.Project

class FontsPlugin implements Plugin<Project> {

    static def GROUP = "fonts"
    static def BASE_URL = "https://google-webfonts-helper.herokuapp.com/api/fonts/"
    static def DOWNLOAD_DIR = "/src/main/assets/fonts/"

    @Override
    void apply(Project project) {

        project.extensions.create('fonts', Fonts)

        project.fonts.extensions."families" = project.container(Family) {
            def familyExtension = project.fonts.families.extensions.create("$it", Family, "$it".toString())
            project.fonts.families."$it".extensions.'variants' = project.container(Variant)
            familyExtension
        }

        def displayFont = project.tasks.create("currentConfiguration").doLast{
            def fonts = project.fonts
            println ""
            println "output: $fonts.output"
            println "fonts:"
            fonts.families.each {
                def familyName = it.name
                println "\t$familyName"
                project.fonts.families."$familyName".variants.each {
                    def variantName = it.name
                    println "\t\t-$variantName"
                }

            }
        }
        displayFont.group = GROUP
        displayFont.description = "Display your current fonts configuration"

        def displayAllVariants = project.tasks.create("displaySelectedFontsVariants").doLast{
            project.fonts.families.each {
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


        def displayAllAvailable = project.tasks.create("displayAllAvailable").doLast{
            println "Downloading fonts..."
            for(item in downloadJson(BASE_URL)){
                println("- $item.id")
            }
        }
        displayAllAvailable.group = GROUP
        displayAllAvailable.description = "Display all available fonts"

        def fontsTask = project.tasks.create("downloadSelectedFonts").doLast{

            def dir = project.fonts.output
            if(dir == null){
                dir = DOWNLOAD_DIR
            }

            def outputDirectory = "$project.name$dir"
            def assetsFonts = new File(outputDirectory);
            if (!assetsFonts.exists()) {
                assetsFonts.mkdirs()
                println("creating $outputDirectory")
            }

            project.fonts.families.each {
                def familyName = it.name
                def familyNameFormatted = familyName.replaceAll("_","-")
                println("\nFont $familyName")

                try {
                    def fontJson = downloadJson("$BASE_URL/$familyNameFormatted")

                    def currentFamily = project.fonts.families."$familyName"
                    if(currentFamily.variants == null || currentFamily.variants.isEmpty()) {
                        for (def variant in fontJson.variants) {
                            def name = variant.local[1]
                            def urlTtf = variant.ttf

                            println("\tdownloading $name from $urlTtf")

                            downloadFile(urlTtf, outputDirectory + name + ".ttf")
                        }
                    }
                     else {
                        currentFamily.variants.each {
                            def variantName = it.name
                            final String endFix = "-" + variantName.toLowerCase()

                            for (def variant in fontJson.variants) {
                                final String name = variant.local[1]
                                if (name.toLowerCase().endsWith(endFix)) {
                                    def urlTtf = variant.ttf

                                    println("\tdownloading $name from $urlTtf")

                                    downloadFile(urlTtf, outputDirectory + name + ".ttf")
                                }
                            }
                        }
                    }
                } catch(all) {
                    println("----- unable to download $familyName / $all")
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