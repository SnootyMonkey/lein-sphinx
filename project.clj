(defproject lein-sphinx "1.0.2-SNAPSHOT"
  :description "A Leiningen plugin to generate documentation from reStructuredText using Sphinx."
  :url "https://github.com/SnootyMonkey/lein-sphinx"
  :license {:name "Mozilla Public License v2.0"
            :url "http://www.mozilla.org/MPL/2.0/"}
  :scm {:name "git"
        :url "https://github.com/SnootyMonkey/lein-sphinx"}
  :eval-in-leiningen true
  :signing {:gpg-key "1FCE90D9"}

  :profiles {
    :dev {
      :dependencies [
        [midje "1.8.1"] ; Example-based testing https://github.com/marick/Midje
      ]
      :plugins [
        [lein-midje "3.2"] ; Example-based testing https://github.com/marick/lein-midje
      ]
    }
  }
)