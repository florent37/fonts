package com.github.florent37.gradle

import org.gradle.api.NamedDomainObjectContainer;

/**
 * Created by florentchampigny on 31/03/2017.
 */

class FontVariantExtension {
    def name

    FontVariantExtension(def name){
        this.name = name
    }

}
