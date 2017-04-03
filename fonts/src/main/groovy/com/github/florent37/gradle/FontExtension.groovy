package com.github.florent37.gradle

import org.gradle.api.NamedDomainObjectContainer;

/**
 * Created by florentchampigny on 31/03/2017.
 */

class FontExtension {
    def name
    NamedDomainObjectContainer<FontVariantExtension> variants

    FontExtension(def name){
        this.name = name
    }

    def variants(final Closure configureClosure) {
        variants.configure(configureClosure)
    }
}
