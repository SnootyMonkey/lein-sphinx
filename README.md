# -= lein-sphinx =- [![Build Status](https://travis-ci.org/SnootyMonkey/lein-sphinx.png?branch=master)](https://travis-ci.org/SnootyMonkey/lein-sphinx)

A [Leiningen](http://leiningen.org/) plugin to generate documentation from [reStructuredText](http://docutils.sourceforge.net/rst.html) using [Sphinx](http://sphinx-doc.org/).

## Usage

Put `[lein-sphinx "1.0.1"]` into the `:plugins` vector of your project.clj.

Once lein-sphinx is configured for your project, you can invoke it:

```console
$ lein sphinx
```

## Configuration

To configure lein-sphinx for your project, add a map of the [sphinx-build options](http://sphinx-doc.org/invocation.html#invocation) that will be provided to sphinx-build when it is invoked. These options are provided in a map with a key of :sphinx in your project map (or in a specific profile if you prefer).

The :sphinx map can have the following keys:

* `:builder` *(default: html)*: the Sphinx builder that will be invoked. It is one of: :html, :dirhtml, :singlehtml, :htmlhelp, :qhelp, :devhelp, :epub, :latex, :man, :texinfo, :text, :gettext, :doctest, :linkcheck
* `:source` *(default: "doc")*: Location of the documentation source directory
* `:output` *(default: "{source}/_build")*: Location of the generated documentation
* `:config` *(default: "{source}")*: Location of the directory containing conf.py.
* `:rebuild` *(default: false)*: Always write all the output files and the cross-reference cache (true), or use the cross reference cache and only write the new and changed output files (false). It is one of: true, false
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

### Multiple Doc Sets

Normally lein-sphinx invokes sphinx-build once with the options specified, which means you produce just one set of docs. This is sufficient for many projects, but if your project has multiple sets of documentation, or if you build your docs with multiple configurations, lein-sphinx still has you covered. In these scenarios, rather than providing a map of configuration options, your configuration is a map of maps, with a configuration for each build you need.

Configuration with multiple builds:

```clojure
:sphinx {
	:html {
		:builder :html
		:source "API/REST"
		:output "docs/HTML"
	}
	:epub {
		:builder :epub
		:source "API/REST"
		:output "docs/epub"
		:tags [:pdf, :toc]
	}
	:latex {
		:builder :latex
		:source "API/REST"
		:output "docs/latex"
		:tags [:toc]
	}
}
```

Invoking lein sphinx as normal will invoke sphinx-build for each configuration:

```console
$ lein sphinx
```
Invoking lein sphinx with additional arguments will invoke sphinx-build for just the specified configurations:

```console
$ lein sphinx html latex
```

### Additional configuration options

There are some relatively obscure [sphinx-build options](http://sphinx-doc.org/invocation.html#invocation) not included in the lein-sphinx configuration map. You can use one of these additional options with the :additional-options key. Arguments provided in :additional-options will be passed on to the invocation of sphinx-build.

Configuration with additional options:

```clojure
:sphinx {
	:builder :singlehtml
	:source "docs"
	:output "docs/HTML"
	:warn-as-error true
	:additional-options "-d ./trees -C"
}
```

## Issues and Feature Requests

If you find an issue, please open it in the GitHub issue tracker. If you need a feature, please fork the repo, add your feature, and issue a pull request! If you're not feeling that ambitious, you can request a feature in the GitHub issue tracker.

## License

lein-sphinx is distributed under the [Mozilla Public License v2.0](http://www.mozilla.org/MPL/2.0/).

Copyright © 2013-2014 [Snooty Monkey, LLC](http://snootymonkey.com/)
