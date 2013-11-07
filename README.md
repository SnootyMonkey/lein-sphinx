# -= lein-sphinx =-

A [Leiningen](http://leiningen.org/) plugin to generate documentation from [reStructuredText](http://docutils.sourceforge.net/rst.html) using [Sphinx](http://sphinx-doc.org/).

## Usage

Put `[lein-sphinx "1.0.0"]` into the `:plugins` vector of your project.clj.

Once lein-sphinx is configured for your project, you can invoke it:

```console
$ lein sphinx
```

## Configuration

To configure lein-sphinx for your project add a map of the [configuration options](http://sphinx-doc.org/invocation.html#invocation) that will be provided to sphinx-build when it is invoked. These options are provided in a map with a key of :sphinx in your project map (or in a specific profile if you prefer).

The :sphinx map can have the following keys:

* `:builder` *(default: html)*: the Sphinx builder that will be invoked. It is one of: :html, :dirhtml, :singlehtml, :htmlhelp, :qhelp, :devhelp, :epub, :latex, :man, :texinfo, :text, :gettext, :doctest, :linkcheck
* `:source` *(default: "doc")*: Location of the documentation source directory
* `:output` *(default: "{source}/_build")*: Location of the generated documentation
* `:config` *(default: "{source}")*: Location of the directory containing conf.py.
* `:rebuild` *(default: false)*: Always write all the output files and the cross reference cache (true), or use the cross reference cache and only write the new and changed output files (false). It is one of: true, false
* `:tags` *(default: [])*: A vector of keywords that define any tags used when building the docs
* `:nitpicky` *(default: false)*: Run in nitpicky mode generated warnings for missing references.
* `:warn-as-error` *(default: false)*: Turn all warnings into errors
* `:setting-values` *(default: {})*: A map of conf.py settings to override. If the setting you want to override is in a dictionary, specify it as :dictionary_name.setting_name (see the verbose configuration example below).
* `:html-template-values` *(default: {})*: A map of names and values for replacement in the HTML templates.

Simple configuration example:

```clojure
:sphinx {
	:builder :dirhtml
	:source "API/REST"
}
```

Verbose configuration example:

```clojure
:sphinx {
	:builder :singlehtml
	:source "docs"
	:output "docs/HTML"
	:config "."
	:rebuild true
	:tags [:html, :draft]
	:nitpicky true
	:warn-as-error true
	:setting-values {
		:pygments_style "solarizedlight"
		:html_theme_options.linkcolor "#B86644"
		:html_theme_options.visitedlinkcolor "#B86644"
	}
	:html-template-values {
		:author "Albert Camus"
		:mascot "Fighting Ferret"
	}
}
```

## Advanced Configuration

lein-sphinx invokes sphinx-build with the options specified, which means you build your docs just one way with one invocation of sphinx-build. This is sufficient for most projects, but if you need to build multiple doc sets, or build with multiple configurations, lein-sphinx still has you covered.

## Issues and Feature Requests

If you find an issue, please open it in the GitHub issue tracker. If you need a feature, please fork the repo, add your feature, and issue a pull request! If you're not feeling that ambitious, you can request a feature in the GitHub issue tracker.

## License

lein-sphinx is distributed under the [Mozilla Public License v2.0](http://www.mozilla.org/MPL/2.0/).

Copyright © 2013 [Snooty Monkey, LLC](http://snootymonkey.com/)
