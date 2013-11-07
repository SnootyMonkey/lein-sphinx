(defproject lein-sphinx "1.0.0-SNAPSHOT"
  :description "A Leiningen plugin to generate documentation from reStructuredText using Sphinx."
  :url "https://github.com/SnootyMonkey/lein-sphinx"
  :license {:name "Mozilla Public License v2.0"
            :url "http://www.mozilla.org/MPL/2.0/"}
  :eval-in-leiningen true

  :profiles {
    :qa {
      :dependencies [
        [midje "1.6-beta1"] ; Example-based testing https://github.com/marick/Midje
      ]
    }
  }

  :plugins [
    [lein-midje "3.1.3-RC2"] ; Example-based testing https://github.com/marick/lein-midje
  ]

  :aliases {
    "midje" ["with-profile" "qa" "midje"] ; run unit tests
  }

)
